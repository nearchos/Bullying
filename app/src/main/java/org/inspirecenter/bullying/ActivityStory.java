package org.inspirecenter.bullying;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.inspirecenter.bullying.model.Choice;
import org.inspirecenter.bullying.model.Dialog;
import org.inspirecenter.bullying.model.Interaction;
import org.inspirecenter.bullying.model.Resource;
import org.inspirecenter.bullying.model.Scene;
import org.inspirecenter.bullying.model.State;
import org.inspirecenter.bullying.model.Step;
import org.inspirecenter.bullying.model.Story;

import java.io.IOException;
import java.util.List;
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
public class ActivityStory extends Activity implements AdapterView.OnItemClickListener {

    public static final String TAG = "bullying.ActivityStory";

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

    @BindView(R.id.activity_story_state_text_view)
    TextView stateTextView;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        ButterKnife.bind(this);

        final ChoiceAdapter choiceAdapter = new ChoiceAdapter(this);
        optionsListView.setAdapter(choiceAdapter);
        optionsListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        player.pause();
        player.save();
    }

    private void handleIntent(final Intent intent) {
        final Story story = (Story) intent.getSerializableExtra(Utils.STORY_SERIALIZED);
        player = new Player(story);
        player.load();
        player.resume();
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

    private void setupScene(final Story story, final Scene scene) {
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
                soundtrackMediaPlayer.stop(); // todo understand why soundtrack doesn't stop when the video plays

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
                try {
                    throw new Exception("interaction stack trace");
                } catch (Exception e) {
                    Log.e(TAG, "look!");
                    e.printStackTrace();
                }
                // prepare UI
                dialogContainer.setVisibility(INVISIBLE);
                videoContainer.setVisibility(INVISIBLE);
                videoView.setVisibility(INVISIBLE);
                showOptions.setVisibility(INVISIBLE);
                hideOptions.setVisibility(VISIBLE);

                // resume soundtrack if there
                if(!soundtrackMediaPlayer.isPlaying()) soundtrackMediaPlayer.start();

                selectedItem = null;
                final Interaction interaction = story.getInteractionById(step.getResourceId());
                final List<Choice> choices = interaction.getChoices();
                final ChoiceAdapter choiceAdapter = (ChoiceAdapter) optionsListView.getAdapter();
                choiceAdapter.clear();
                choiceAdapter.addAll(choices);
Log.d(TAG, "setting vector choices: " + choices);//todo delete
Log.d(TAG, "choiceAdapter.getCount(): " + choiceAdapter.getCount());//todo delete
                choiceAdapter.notifyDataSetChanged();
                optionsListView.postInvalidate();

                break;
            }
            case "dialog":
            {
                // prepare UI
                dialogContainer.setVisibility(VISIBLE);
                videoContainer.setVisibility(GONE);
                videoView.setVisibility(GONE);
                showOptions.setVisibility(GONE);
                hideOptions.setVisibility(GONE);
                optionsListView.setVisibility(GONE);

                final Dialog dialog = story.getDialogById(step.getResourceId());
                dialogTitleTextView.setText(dialog.getTitle());
                dialogMessageTextView.setText(dialog.getMessage());

                break;
            }
            default:
            {
                Log.e(TAG, "Unknown step action: '" + step.getAction() + "'");
                throw new RuntimeException("Unknown step action: '" + step.getAction() + "'");
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
        showOptions.setVisibility(INVISIBLE);
        hideOptions.setVisibility(VISIBLE);
    }

    public void hideOptions(final View view) {
        showOptions.setVisibility(VISIBLE);
        hideOptions.setVisibility(INVISIBLE);
    }

    private View selectedItem = null;

    /**
     * Triggered when an option is selected in the options choice list-view.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(this, "view.isSelected: " + view.isSelected() + ", position: " + position, Toast.LENGTH_SHORT).show();
        if(selectedItem != null && selectedItem == view) {
            view.setSelected(false);
            Toast.makeText(this, "OK, choice done!", Toast.LENGTH_SHORT).show();
            player.increasePoints(position); // add points
            player.progress();
        } else {
            selectedItem = view;
            view.setSelected(true);
            Toast.makeText(this, R.string.Select_again_to_finalize_your_choice, Toast.LENGTH_SHORT).show();
        }
    }

    private class Player {

        private final Story story;

        private Vector<SceneAndStep> orderedScenesAndSteps = new Vector<>();
        private int currentSceneAndStepIndex;
        private int currentPoints;

        private Player(final Story story) {
            this.story = story;
            this.orderedScenesAndSteps.clear();
        }

        private void load() {
            final Vector<Scene> orderedScenes = story.getOrderedScenes();
            for(final Scene scene : orderedScenes) {
                final Vector<Step> orderedSteps = scene.getOrderedSteps();
                for(final Step step : orderedSteps) {
                    final SceneAndStep sceneAndStep = new SceneAndStep(scene, step);
                    orderedScenesAndSteps.add(sceneAndStep);
                }
            }
            final State state = Preferences.getState(ActivityStory.this, story.getId());
            currentSceneAndStepIndex = state.getIndex();
            currentPoints = state.getPoints();
        }

        private void resume() {
            final SceneAndStep currentSceneAndStep = orderedScenesAndSteps.get(currentSceneAndStepIndex);
            final Scene currentScene = currentSceneAndStep.getScene();
            final Step currentStep = currentSceneAndStep.getStep();
            stateTextView.setText("Story: " + story.getId() + ", Scene: " + currentScene.getId() + ", Step: " + currentStep.getId() + ", Points: " + currentPoints);

            setupScene(story, currentScene);
            playStep(story, currentStep);
        }

        private void pause() {
            stopStep();
            stopScene();
        }

        private void save() {
//            final SceneAndStep currentSceneAndStep = orderedScenesAndSteps.get(currentSceneAndStepIndex);
//            Preferences.setState(ActivityStory.this, story.getId(), currentSceneAndStep.getScene().getId(), currentSceneAndStep.getStep().getId(), currentPoints);
            Preferences.setState(ActivityStory.this, story.getId(), new State(currentSceneAndStepIndex, currentPoints));
        }

        private int increasePoints(int position) {
            final SceneAndStep currentSceneAndStep = orderedScenesAndSteps.get(currentSceneAndStepIndex);
            final Step currentStep = currentSceneAndStep.getStep();
            if("interaction".equals(currentStep.getAction())) {
                final Interaction interaction = story.getInteractionById(currentStep.getResourceId());
                currentPoints += interaction.getChoices().get(position).getScore();
            } else {
                Log.w(TAG, "Warning: trying to increase points for a step other than 'interaction': " + currentStep);
            }
            return currentPoints;
        }

        private void progress() {

            // check if another scene/step combo is available
            if(currentSceneAndStepIndex < orderedScenesAndSteps.size() - 1) {
                // move to next step
                currentSceneAndStepIndex++;
            } else {
                // no more scenes...
                Toast.makeText(ActivityStory.this, "TODO SCENE(S) FINISHED", Toast.LENGTH_SHORT).show();
                finish();
            }

            resume();
        }
    }

    private class SceneAndStep {
        private final Scene scene;
        private final Step step;

        SceneAndStep(Scene scene, Step step) {
            this.scene = scene;
            this.step = step;
        }

        public Scene getScene() {
            return scene;
        }

        public Step getStep() {
            return step;
        }

        @Override
        public String toString() {
            return scene.getId() + "+" + step.getId();
        }
    }
}