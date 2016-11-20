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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.inspirecenter.bullying.model.Resource;
import org.inspirecenter.bullying.model.Scene;
import org.inspirecenter.bullying.model.Step;
import org.inspirecenter.bullying.model.Story;
import org.inspirecenter.bullying.model.TimelineElement;

import java.io.IOException;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 */
public class ActivityStory extends Activity {

    public static final String TAG = "bullying.ActivityStory";

    public static final String PREFERENCE_KEY_CURRENT_TIMELINE  = "current-timeline";

    // background
    @BindView(R.id.activity_story_content)
    RelativeLayout content;

    @BindView(R.id.dialog_container)
    RelativeLayout dialogContainer;

    @BindView(R.id.dialog_title)
    TextView dialogTitleTextView;

    @BindView(R.id.dialog_message)
    TextView dialogMessageTextView;

    @BindView(R.id.video_container)
    RelativeLayout videoContainer;

    @BindView(R.id.video_view)
    VideoView videoView;

    @BindView(R.id.video_play_pause_repeat)
    ImageButton videoPlayPauseRepeatButton;

    @BindView(R.id.video_close)
    ImageButton videoCloseButton;

    @BindView(R.id.show_options)
    RelativeLayout showOptions;

    @BindView(R.id.hide_options)
    RelativeLayout hideOptions;

    @BindView(R.id.activity_story_options_list_view)
    ListView optionsListView;

    private Player player;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        ButterKnife.bind(this);

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

    private void playSoundtrack(final Story story, final Scene scene) {
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

    private void playScene(final Story story, final Scene scene) {

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
        playSoundtrack(story, scene);

        // show dialog with title/description

        // prepare UI
        dialogContainer.setVisibility(VISIBLE);
        videoContainer.setVisibility(GONE);
        videoView.setVisibility(GONE);
        showOptions.setVisibility(GONE);
        hideOptions.setVisibility(GONE);
        optionsListView.setVisibility(GONE);

        dialogTitleTextView.setText(scene.getTitle());
        dialogMessageTextView.setText(scene.getDescription());
    }

    private void stopScene() {
        soundtrackMediaPlayer.reset();
    }

    private void playStep(final Story story, final Step step) {

        Log.d(TAG, "Starting step: " + step);

        switch (step.getAction()) {
            case "video":
            {
                // prepare UI
                dialogContainer.setVisibility(INVISIBLE);
                videoContainer.setVisibility(VISIBLE);
                videoView.setVisibility(VISIBLE);
                showOptions.setVisibility(INVISIBLE);
                hideOptions.setVisibility(INVISIBLE);

                // pause soundtrack
                soundtrackMediaPlayer.pause();

                // play video resource
                final String videoId = step.getResourceId();
                final Resource videoResource = story.getResourceById(videoId);
                final String videoPath = Utils.getResourcePath(this, story.getId(), videoResource);
                final boolean isProgressionAutomatic = "automatic".equalsIgnoreCase(step.getProgression());
                videoView.setVideoPath(videoPath);
                videoView.requestFocus();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override public void onCompletion(MediaPlayer mediaPlayer) {
                        videoPlayPauseRepeatButton.setImageResource(R.drawable.ic_replay_white_24dp);
                        if(isProgressionAutomatic) {
                            soundtrackMediaPlayer.start();
                            player.progress();
                        }
                    }
                });
                videoView.start();

                break;
            }
            case "interaction":
            {
                // prepare UI
                dialogContainer.setVisibility(INVISIBLE);
                showOptions.setVisibility(VISIBLE);
                hideOptions.setVisibility(INVISIBLE);
                videoContainer.setVisibility(INVISIBLE);
                videoView.setVisibility(INVISIBLE);

                // resume soundtrack if there
                if(!soundtrackMediaPlayer.isPlaying()) soundtrackMediaPlayer.start();

                //todo

                break;
            }
            default:
            {
                Log.e(TAG, "Unknown step action: " + step.getAction());
                throw new RuntimeException("Unknown step action: " + step.getAction());
            }
        }
    }

    private void stopStep() {
        // if playing video, then stop
        if(videoView.isPlaying()) {
            videoView.pause();
            videoPlayPauseRepeatButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    public void videoPlayPauseRepeat(final View view) {
        if(videoView.isPlaying()) {
            videoView.pause();
            videoPlayPauseRepeatButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        } else {
            videoView.start();
            videoPlayPauseRepeatButton.setImageResource(R.drawable.ic_pause_white_24dp);
        }
    }

    public void dialogClose(final View view) {
        dialogContainer.setVisibility(GONE);
        player.progress();
    }

    public void videoClose(final View view) {
        videoContainer.setVisibility(GONE);
        player.progress();
    }

    public void showOptions(final View view) {
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show();
        showOptions.setVisibility(INVISIBLE);
        hideOptions.setVisibility(VISIBLE);
    }

    public void hideOptions(final View view) {
        Toast.makeText(this, "hide", Toast.LENGTH_SHORT).show();
        showOptions.setVisibility(VISIBLE);
        hideOptions.setVisibility(INVISIBLE);
    }

    private class Player {

        private final Story story;

        private Vector<TimelineElement> timeline;
        private int timelineIndex;

        private Player(final Story story) {
            this.story = story;
            this.timeline = new Vector<>();
        }

        private void load() {
            final Vector<Scene> orderedScenes = story.getOrderedScenes();
            for(final Scene scene : orderedScenes) {
                timeline.add(scene);
                final Vector<Step> orderedSteps = scene.getOrderedSteps();
                for(final Step step : orderedSteps) {
                    timeline.add(step);
                }
            }
            timelineIndex = preferences.getInt(PREFERENCE_KEY_CURRENT_TIMELINE, 0);
        }

        private void resume() {
            if(timelineIndex < timeline.size()) {
                final TimelineElement timelineElement = timeline.elementAt(timelineIndex);
                if(timelineElement instanceof Scene) {
                    playScene(story, (Scene) timelineElement);
                } else if(timelineElement instanceof Step) {
                    playStep(story, (Step) timelineElement);
                } else {
                    // this should never happen
                    throw new RuntimeException("Unkown timeline element: " + timelineElement);
                }
            } else {
                // we reached the end of the scene
                // todo
                finish();
            }
        }

        private void pause() {
            stopStep();
            stopScene();
        }

        private void save() {
            preferences.edit().putInt(PREFERENCE_KEY_CURRENT_TIMELINE, timelineIndex).apply();
        }

        private void progress() {
            timelineIndex++;
            resume();
        }
    }
}