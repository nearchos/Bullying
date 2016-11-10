package org.inspirecenter.bullying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.inspirecenter.bullying.model.Choice;
import org.inspirecenter.bullying.model.Story;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 */
public class StepActivity extends AppCompatActivity {

    // story Model
    Story story = null;

    // radio Buttons
    @BindView(R.id.Opt1Rb)
    RadioButton Opt1Rb;

    @BindView(R.id.Opt2Rb)
    RadioButton Opt2Rb;

    @BindView(R.id.Opt3Rb)
    RadioButton Opt3Rb;

    @BindView(R.id.Opt4Rb)
    RadioButton Opt4Rb;

    // options description
    @BindView(R.id.Opt1Desc)
    Button Opt1Desc;

    @BindView(R.id.Opt2Desc)
    Button Opt2Desc;

    @BindView(R.id.Opt3Desc)
    Button Opt3Desc;

    @BindView(R.id.Opt4Desc)
    Button Opt4Desc;

    // background
    @BindView(R.id.BackgroundImageView)
    ImageView BgImg;

    // submit Button
    @BindView(R.id.SubmitButton)
    Button submitButton;

    @BindView(R.id.StepTextView)
    TextView stepDescTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        // since radio buttons are nested in layouts, you need to manually un-check the other buttons
        setRadioButtonsBehaviour();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Intent intent = getIntent();
        this.story = (Story) intent.getSerializableExtra(Utils.STORY_SERIALIZED);

        // download resources
        ResourceManager resourceManager = new ResourceManager(this);
        resourceManager.DownloadResources(story.getResources());

        // set text description of the first step
        stepDescTextView.setText(story.getInteractions().get(0).getPrompt());
        List<Choice> interactionChoices = story.getInteractions().get(0).getChoices();
        for (Choice choice : interactionChoices) {
            if (choice.getOrder() == 1)
                Opt1Desc.setText(choice.getPrompt());
            if (choice.getOrder() == 2)
                Opt2Desc.setText(choice.getPrompt());
            if (choice.getOrder() == 3)
                Opt3Desc.setText(choice.getPrompt());
            if (choice.getOrder() == 4)
                Opt4Desc.setText(choice.getPrompt());
        }
    }

    private void setRadioButtonsBehaviour() {
        Opt1Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opt2Rb.setChecked(false);
                Opt3Rb.setChecked(false);
                Opt4Rb.setChecked(false);
            }
        });
        Opt2Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opt1Rb.setChecked(false);
                Opt3Rb.setChecked(false);
                Opt4Rb.setChecked(false);
            }
        });
        Opt3Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opt2Rb.setChecked(false);
                Opt1Rb.setChecked(false);
                Opt4Rb.setChecked(false);
            }
        });
        Opt4Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opt2Rb.setChecked(false);
                Opt3Rb.setChecked(false);
                Opt1Rb.setChecked(false);
            }
        });
    }

    private int getSelectedRadioButtonOrder() {
        if (Opt1Rb.isChecked())
            return 1;
        else if (Opt2Rb.isChecked())
            return 2;
        else if (Opt3Rb.isChecked())
            return 3;
        else if (Opt4Rb.isChecked())
            return 4;
        else
            return -1;
    }
}
