package com.example.smarthelathapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AppUsageDetails extends AppCompatActivity {

    private TextView time;
    private TextView appName;
    private TextView hr;
    private TextView eda;
    private ImageView appImage;
    private ImageView moodColour;
    private TextView moodDescritpion;

    private int colourIndicators[] = {R.drawable.red, R.drawable.amber, R.drawable.green};
    private String colourDescritpions[] = {"Stress levels High", "Stress levels rising", "Calm and Stable"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage_details);

        appName = findViewById(R.id.tvAppName);
        time = findViewById(R.id.tvTime);
        appImage = findViewById(R.id.ivAppImage);
        hr = findViewById(R.id.tvHRDisplay);
        eda = findViewById(R.id.tvEDAdisplay);
        moodColour = findViewById(R.id.ivMoodColour);
        moodDescritpion = findViewById(R.id.tvMoodDescription);


        moodColour.setImageResource(colourIndicators[2]);
        moodDescritpion.setText(colourDescritpions[2]);

        Intent intent = getIntent();
        appName.setText(intent.getStringExtra("name"));
        time.setText(intent.getStringExtra("time"));
        appImage.setImageResource(intent.getIntExtra("image",0));
        hr.setText(intent.getStringExtra("hr"));
        eda.setText(intent.getStringExtra("eda"));

        String hr = intent.getStringExtra("hr");
        int intHr = Integer.parseInt(hr);


        int stress = 200;
        int risingStress = 180;
        int calm = 140;


        if(intHr >= stress){
            moodDescritpion.setText(colourDescritpions[0]);
            moodColour.setImageResource(colourIndicators[0]);

        } else if(intHr >= risingStress){
            moodDescritpion.setText(colourDescritpions[1]);
            moodColour.setImageResource(colourIndicators[1]);

        } else if(intHr >= calm){
            moodDescritpion.setText(colourDescritpions[2]);
            moodColour.setImageResource(colourIndicators[2]);
        }

    }
}