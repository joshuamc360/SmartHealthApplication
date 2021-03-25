package com.example.smarthelathapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
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
    private ArrayAdapter mAdapter1;

    String mTitle[] = {"Facebook", "FlappyBird", "Twitter", "Instagram", "Youtube"};
    String mDescription[] = {"Facebook Description", "flappyBird Description", "Twitter Description", "Instagram Description", "Youtube Description"};
    int images[] = {R.drawable.facebook, R.drawable.flappybird, R.drawable.twitter, R.drawable.instagram, R.drawable.youtube};

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

        moodChart.getGridLabelRenderer().setNumHorizontalLabels(3);

        //display list entries

        appUsageListView= (ListView) findViewById(R.id.lvAppUsage);
//        mAdapter1 = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, appUsageList
//        );

        MyAdapter adapter = new MyAdapter(this, mTitle,mDescription,images);
        appUsageListView.setAdapter(adapter);

        appUsageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position ==  0) {
                    Toast.makeText(MoodTracker.this, "Facebook Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(MoodTracker.this, "Whatsapp Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(MoodTracker.this, "Twitter Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(MoodTracker.this, "Instagram Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(MoodTracker.this, "Youtube Description", Toast.LENGTH_SHORT).show();
                }

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

                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    MoodData moodData = myDataSnapshot.getValue(MoodData.class);
                    dp1[index]= new DataPoint(moodData.getCurrentDate(), moodData.getEDA());
                    dp2[index]= new DataPoint(moodData.getCurrentDate(), moodData.getHR());


                    int range = random.nextInt(250)+0;
//                    appUsageList.add(sdf.format(moodData.getCurrentDate()));

                    index++;
                }

                edaSeries.resetData(dp1);
                hrSeries.resetData(dp2);

//                appUsageListView.setAdapter(mAdapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

            return row;
        }
    }


}
