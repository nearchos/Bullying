package org.inspirecenter.bullying;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.inspirecenter.bullying.model.Resource;
import org.inspirecenter.bullying.model.Story;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Nearchos
 *         Created: 13-Nov-16
 */
public class ActivityLoadResources extends AppCompatActivity {

    public static final String TAG = "bullying.LoadResources";
    public static final int BUFFER_SIZE = 16384;

    private Story story = null;

    @BindView(R.id.activity_load_resources_text_view)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_resources);
        ButterKnife.bind(this);

        final Typeface planetbeTypeface = Typeface.createFromAsset(getAssets(), "fonts/planetbe.ttf");
        textView.setTypeface(planetbeTypeface);
    }

    @Override
    protected void onResume() {
        super.onResume();

        story = (Story) getIntent().getSerializableExtra(Utils.STORY_SERIALIZED);
        // check if resources are available

        final Vector<Resource> resourcesToBeDownloaded = new Vector<>();

        final List<Resource> resources = story.getResources();
        for(final Resource resource : resources) {
            final boolean resourceExists = Utils.hasResourceInCache(this, story.getId(), resource);
            if(!resourceExists)
            resourcesToBeDownloaded.add(resource);
        }

        new DownloadResourceTask(story.getId(), resourcesToBeDownloaded).execute(this);
    }

    private class DownloadResourceTask extends AsyncTask<Context, Integer, Void> {
        final String storyId;
        final Vector<Resource> resourcesToBeDownloaded;
        final int total;

        public DownloadResourceTask(final String storyId, final Vector<Resource> resourcesToBeDownloaded) {
            this.storyId = storyId;
            this.resourcesToBeDownloaded = resourcesToBeDownloaded;
            this.total = resourcesToBeDownloaded.size();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            textView.setText(getResources().getString(R.string.Downloading_resources, total - progress[0] + 1, total));
//            textView.setText(getResources().getQuantityString(R.plurals.Loading_resources, progress[0], progress[0]));
        }

        @Override
        protected Void doInBackground(Context... contexts) {
            while(resourcesToBeDownloaded.size() > 0) {
                publishProgress(resourcesToBeDownloaded.size());

                try {
                    final Resource resource = resourcesToBeDownloaded.remove(0);
                    final InputStream inputStream = new java.net.URL(resource.getSource()).openStream();
                    final byte [] data = new byte[BUFFER_SIZE];
                    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();

                    Utils.saveResourceInCache(contexts[0], storyId, resource, buffer.toByteArray());
                } catch (IOException ioe) {
                    Log.e("Error", "I/O error while writing in cache: " + ioe);
                }
            }
            publishProgress(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // start next activity
            final Intent intent = new Intent(ActivityLoadResources.this, ActivityStory.class);
            intent.putExtra(Utils.STORY_SERIALIZED, story);
            startActivity(intent);
        }
    }
}