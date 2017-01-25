package org.inspirecenter.bullying;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

    @BindView(R.id.activity_main_list_view)
    ListView listView;

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

        listView.setAdapter(new StoriesAdapter(this, stories));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Toast.makeText(ActivityMain.this, "Opening Story:" + position, Toast.LENGTH_SHORT).show();

                final Intent intent = new Intent(ActivityMain.this, ActivityLoadResources.class);
                intent.putExtra(Utils.STORY_SERIALIZED, stories.elementAt(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Story story = stories.elementAt(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                builder.setTitle(R.string.Reset)
                        .setMessage(R.string.Are_you_sure_you_want_reset_this_story)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // reset command
//                                final SharedPreferences sharedPreferences = getSharedPreferences(ActivityStory.class.toString(), MODE_PRIVATE);
//                                sharedPreferences.edit().clear().apply();
                                Preferences.resetState(ActivityMain.this, story.getId());
                                Toast.makeText(ActivityMain.this, R.string.Story_reset, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // dismiss
                                Toast.makeText(ActivityMain.this, R.string.Cancelled, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }
}