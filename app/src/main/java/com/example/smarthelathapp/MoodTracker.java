package com.example.smarthelathapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MoodTracker extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference moodDataRef;

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    private static Random random = new Random();

    private GraphView moodChart;
    private LineGraphSeries edaSeries, hrSeries;


    private ListView appUsageListView;
    private ArrayList<String> appUsageList = new ArrayList<String>();
    private ArrayList<AppUsageData> arrayList = new ArrayList<>();
    private ArrayList<MoodData> moodDataArrayList = new ArrayList<>();
    private AppUsageDataAdapter AUadapter;

    String mTitle[] ;
    String mDescription[] = {"Facebook", "Flappy Bird", "Twitter", "Instagram", "Youtube"};
    private int images[] = {R.drawable.facebook, R.drawable.flappybird, R.drawable.twitter, R.drawable.instagram, R.drawable.youtube};

    private Integer indexVal;
    private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);

        mAuth =FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        moodDataRef = database.getReference("MoodChartData").child(user.getUid());
        appUsageListView= (ListView) findViewById(R.id.lvAppUsage);
        moodChart = (GraphView) findViewById(R.id.gvMoodChart);

        //displaying data in graphView

        hrSeries = new LineGraphSeries();
        hrSeries.setColor(Color.BLUE);
        hrSeries.setTitle("HR");
        moodChart.addSeries(hrSeries);

        edaSeries = new LineGraphSeries();
        edaSeries.setColor(Color.RED);
        edaSeries.setTitle("EDA");
        moodChart.addSeries(edaSeries);

        moodChart.getLegendRenderer().setVisible(true);
        moodChart.getGridLabelRenderer().setNumHorizontalLabels(3);
        moodChart.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if(isValueX){
                    return  sdf.format(new Date((long)value));
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });


        //display list entries

        AUadapter = new AppUsageDataAdapter(MoodTracker.this,R.layout.row,arrayList);

        appUsageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView APPNAME = (TextView) view.findViewById(R.id.textView2);
                String appName = APPNAME.getText().toString();

                TextView TIME = (TextView) view.findViewById(R.id.textView1);
                String appTime = TIME.getText().toString();

                ImageView IMAGE = (ImageView) view.findViewById(R.id.ivImageRow);
                //String imageName = IMAGE.get


                Intent intent = new Intent(getApplicationContext(),AppUsageDetails.class);
                intent.putExtra("name",appName);
                intent.putExtra("time",appTime);
//                intent.putExtra("image",imageName);
                intent.putExtra("eda",String.valueOf(moodDataArrayList.get(position).getEDA()));
                intent.putExtra("hr",String.valueOf(moodDataArrayList.get(position).getHR()));

                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        moodDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataPoint[]dp1 = new DataPoint[(int) snapshot.getChildrenCount()];
                DataPoint[]dp2 = new DataPoint[(int) snapshot.getChildrenCount()];

                int index = 0;
                int noApps = 4;

                arrayList.clear();

                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    int num = random.nextInt(noApps)+0;

                    MoodData moodData = myDataSnapshot.getValue(MoodData.class);
                    dp1[index]= new DataPoint(moodData.getCurrentDate(), moodData.getEDA());
                    dp2[index]= new DataPoint(moodData.getCurrentDate(), moodData.getHR());

                    moodDataArrayList.add(moodData);
                    arrayList.add(new AppUsageData(images[num],sdf.format(moodData.getCurrentDate()),mDescription[num]));

                    index++;
                }

                edaSeries.resetData(dp1);
                hrSeries.resetData(dp2);

                appUsageListView.setAdapter(AUadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

