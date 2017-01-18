package org.inspirecenter.bullying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.inspirecenter.bullying.model.Resource;
import org.inspirecenter.bullying.model.Story;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import static org.inspirecenter.bullying.ActivityLoadResources.BUFFER_SIZE;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 * 29/10/2016.
 */
class ImageAdapter extends BaseAdapter {

    private static final String TAG = "bullying.ImageAdapter";

    private Context mContext;
    private Vector<Story> stories;

    ImageAdapter(final Context context, final Vector<Story> stories) {
        this.mContext = context;
        this.stories = stories;
    }

    public int getCount() {
        return stories.size();
    }

    public Object getItem(int position) {
        return stories.elementAt(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * create a new ImageView for each item referenced by the Adapter
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int width = (int) mContext.getResources().getDimension(R.dimen.story_image_icon_width);
        final int padding = (int) mContext.getResources().getDimension(R.dimen.story_image_icon_padding);
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(padding, padding, padding, padding);
        } else {
            imageView = (ImageView) convertView;
        }

        Log.d(TAG, "stories[position]: " + stories.get(position));

        final Story story = stories.elementAt(position);
        final String thumbnailResourceId = story.getThumbnail();
        final Resource thumbnailResource = story.getResourceById(thumbnailResourceId);

        if(Utils.hasResourceInCache(mContext, story.getId(), thumbnailResource)) {
            final byte [] data = Utils.getResourceFromCache(mContext, story.getId(), thumbnailResource);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imageView.setImageBitmap(bitmap);
        } else {
            new DownloadImageTask(thumbnailResource, imageView).execute(story.getId(), thumbnailResource.getSource());
        }

        return imageView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Resource resource;
        private ImageView imageView;

        DownloadImageTask(final Resource resource, final ImageView imageView) {
            this.resource = resource;
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... params) {
            String storyId = params[0];
            String url = params[1];
            Bitmap bitmap = null;
            try {
                {
                    final InputStream inputStream = new java.net.URL(url).openStream();
                    final byte[] data = new byte[BUFFER_SIZE];
                    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    Utils.saveResourceInCache(mContext, storyId, resource, buffer.toByteArray());
                }
                {
                    final byte[] data = Utils.getResourceFromCache(mContext, storyId, resource);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
            } catch (IOException ioe) {
                Log.e("Error", "I/O error while downloading and caching resource: " + ioe.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}