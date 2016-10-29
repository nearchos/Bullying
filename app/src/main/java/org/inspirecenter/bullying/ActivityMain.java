package org.inspirecenter.bullying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity {

    @BindView(R.id.storiesGridView)
    GridView storiesGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        storiesGridView.setAdapter(new ImageAdapter(this));

        storiesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(ActivityMain.this, "Opening Story:" + position,
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActivityMain.this, StepActivity.class);
                String message = "bullying-model.json";
                intent.putExtra(Utils.STORY_ID_MSG, message);
                startActivity(intent);
            }
        });

    }
}