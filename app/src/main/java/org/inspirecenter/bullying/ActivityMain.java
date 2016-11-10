package org.inspirecenter.bullying;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import org.inspirecenter.bullying.model.Story;

import java.io.IOException;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 */
public class ActivityMain extends AppCompatActivity {

    @BindView(R.id.storiesGridView)
    GridView storiesGridView;

    private String [] storyFiles;
    private Vector<Story> stories = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final AssetManager assetManager = getAssets();
        try {
            storyFiles = assetManager.list(Utils.STORIES_ASSETS_PATH);
        } catch (IOException e) {
            Toast.makeText(this, R.string.Error_loading_story_files, Toast.LENGTH_SHORT).show();
            finish();
        }

        final GsonBuilder gsonBuilder = new GsonBuilder();
        for(final String storyFile : storyFiles) {
            final Story story = gsonBuilder.create().fromJson(Utils.loadJSONFromAsset(this, storyFile), Story.class);
            stories.add(story);
        }

        storiesGridView.setAdapter(new ImageAdapter(this, stories));

        storiesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Toast.makeText(ActivityMain.this, "Opening Story:" + position, Toast.LENGTH_SHORT).show();

                final Intent intent = new Intent(ActivityMain.this, StepActivity.class);
                intent.putExtra(Utils.STORY_SERIALIZED, stories.elementAt(position));
                startActivity(intent);
            }
        });

    }
}