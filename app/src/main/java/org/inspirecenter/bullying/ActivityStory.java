package org.inspirecenter.bullying;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import org.inspirecenter.bullying.model.Resource;
import org.inspirecenter.bullying.model.Scene;
import org.inspirecenter.bullying.model.Step;
import org.inspirecenter.bullying.model.Story;

import java.io.IOException;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 */
public class ActivityStory extends Activity {

    public static final String TAG = "bullying.ActivityStory";

    public static final String PREFERENCE_KEY_CURRENT_SCENE = "current-scene";
    public static final String PREFERENCE_KEY_CURRENT_STEP  = "current-step";

    // background
    @BindView(R.id.activity_story_content)
    RelativeLayout content;

    @BindView(R.id.video_view)
    VideoView videoView;

    private MediaController mediaController;

    @BindView(R.id.show_options)
    LinearLayout showOptions;

    @BindView(R.id.hide_options)
    LinearLayout hideOptions;

    @BindView(R.id.activity_story_options_list_view)
    ListView optionsListView;

    private Player player;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        ButterKnife.bind(this);

        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        // initially, make all UI invisible
        videoView.setVisibility(View.GONE);
        showOptions.setVisibility(View.GONE);
        hideOptions.setVisibility(View.GONE);
        optionsListView.setVisibility(View.GONE);

        preferences = getPreferences(MODE_PRIVATE);

        final Intent intent = getIntent();
        final Story story = (Story) intent.getSerializableExtra(Utils.STORY_SERIALIZED);

        player = new Player(story);
    }

    @Override
    protected void onResume() {
        super.onResume();

        player.load();
        player.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        player.pause();
        player.save();
    }

    private MediaPlayer soundtrackMediaPlayer = new MediaPlayer();

    private void startScene(final Story story, final Scene scene) {
        // set background image - if needed
        final String backgroundId = scene.getBackground();
        if(backgroundId != null && !backgroundId.isEmpty()) {
            final Resource backgroundResource = story.getResourceById(backgroundId);
            final byte [] data = Utils.getResourceFromCache(this, story.getId(), backgroundResource);
            assert data != null;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            content.setBackground(new BitmapDrawable(getResources(), bitmap));
        }

        // set background audio - if needed
        final String soundtrackId = scene.getSoundtrack();
        if(soundtrackId != null && !soundtrackId.isEmpty()) {
            final Resource soundtrackResource = story.getResourceById(soundtrackId);
            soundtrackMediaPlayer.reset();
            try {
                soundtrackMediaPlayer.setDataSource(Utils.getResourcePath(this, story.getId(), soundtrackResource));
                soundtrackMediaPlayer.setLooping(true);
                soundtrackMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
                soundtrackMediaPlayer.prepareAsync();
            } catch (IOException ioe) {
                Log.e(TAG, "I/O error while playing soundtrack for " + soundtrackId + ": " + ioe.getMessage());
                Toast.makeText(this, "I/O error while playing soundtrack for " + soundtrackId + ": " + ioe.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopScene() {
        soundtrackMediaPlayer.reset();
    }

    private void startStep(final Story story, final Scene scene, final Step step) {

        switch (step.getAction()) {
            case "video":
                Log.d(TAG, "Starting step: " + step);
                // prepare UI
                videoView.setVisibility(View.VISIBLE);

                // play video resource
                final String videoId = step.getResourceId();
                final Resource videoResource = story.getResourceById(videoId);
                final String videoPath = Utils.getResourcePath(this, story.getId(), videoResource);
                videoView.setVideoPath(videoPath);
                videoView.requestFocus();
                videoView.start();

                break;

            case "interaction":
                break;

            default:
                Log.e(TAG, "Unknown step action: " + step.getAction());
                throw new RuntimeException("Unknown step action: " + step.getAction());
        }
    }

    private void stopStep() {

    }

    public void showOptions(final View view) {
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show();
        showOptions.setVisibility(View.INVISIBLE);
        hideOptions.setVisibility(View.VISIBLE);
    }

    public void hideOptions(final View view) {
        Toast.makeText(this, "hide", Toast.LENGTH_SHORT).show();
        showOptions.setVisibility(View.VISIBLE);
        hideOptions.setVisibility(View.INVISIBLE);
    }

    private class Player {

        private final Story story;
        private final Vector<Scene> orderedScenes;
        private final Vector<Step> orderedSteps = new Vector<>();
        private int currentSceneIndex;
        private int currentStepIndex;

        private Player(final Story story) {
            this.story = story;
            this.orderedScenes = story.getOrderedScenes();
        }

        private void load() {
            currentSceneIndex = preferences.getInt(PREFERENCE_KEY_CURRENT_SCENE, 0);
            currentSceneIndex = preferences.getInt(PREFERENCE_KEY_CURRENT_STEP, 0);
        }

        private void save() {
            preferences.edit().putInt(PREFERENCE_KEY_CURRENT_SCENE, currentSceneIndex).apply();
            preferences.edit().putInt(PREFERENCE_KEY_CURRENT_STEP, currentStepIndex).apply();
        }

        private void resume() {
            // setup scene
            final Scene currentScene = orderedScenes.elementAt(currentSceneIndex);
            startScene(story, currentScene);

            // select and play step
            orderedSteps.clear();
            orderedSteps.addAll(currentScene.getOrderedSteps());
            final Step currentStep = orderedSteps.elementAt(currentStepIndex);
            startStep(story, currentScene, currentStep);
        }

        private void pause() {
            stopStep();
            stopScene();
        }
    }
}