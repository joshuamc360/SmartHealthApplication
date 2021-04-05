package com.example.smarthelathapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Tracking extends AppCompatActivity {

    private static Random random = new Random();
    private Handler mHandler = new Handler();

    private TextView bpm;
    private TextView eda;

    private Button startTracking;
    private Button stopTracking;

    private Date currentDate;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("MoodChartData");

        eda = (TextView)findViewById(R.id.tvEdaData);
        bpm = (TextView)findViewById(R.id.tvHrData);
        startTracking=(Button)findViewById(R.id.btnStartTracking);
        stopTracking=(Button)findViewById(R.id.btnStopTracking);


        //tracking and generating data
        startTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRepeating();
            }
        });

        stopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRepeating();

                Intent intent = new Intent(Tracking.this, MoodTracker.class);
                startActivity(intent);
            }
        });

    }

    public void startRepeating(){
        updateUI.run();
    }

    public void stopRepeating(){
        mHandler.removeCallbacks(updateUI);
    }

    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            String beatsPerMinute = String.valueOf(generateBPM());
            String edActivity = String.valueOf(generateEDA());

            bpm.setText(beatsPerMinute+" BPM");
            eda.setText(edActivity+" Seimens");

            String id = myRef.push().getKey();
            long currentDate = new Date().getTime();

            MoodData moodData = new MoodData(generateEDA(),generateBPM(),currentDate);
            myRef.child(user.getUid()).child(id).setValue(moodData);

            mHandler.postDelayed(this, 5000);
        }
    };

    public static int generateBPM(){
        int range = random.nextInt(250)+0;
        return range;
    }

    public static int generateEDA(){
        int eda = random.nextInt(100)+0;
        return eda;
    }


    //phasic measures. https://www.sciencedirect.com/topics/psychology/electrodermal-activity#:~:text=Electrodermal%20activity%20is%20usually%20expressed,tonic%20and%20phasic%20electrodermal%20measures.

    //microsiemens

    //seimens 0-100
}