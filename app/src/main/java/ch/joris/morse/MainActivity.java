package ch.joris.morse;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnTouchListener {
    // TODO: implement communication function, where you can send morse-messages to a friend, maybe even in the background, when the phone is locked

    private Gson gson;

    private Button recordButton;
    private Button playButton;
    private Button morseButton;

    private Vibrator vibrator;

    private SharedPreferences sharedPreferences;
    private ArrayList<Long> morsePattern = new ArrayList<>();
    private ArrayList<MorsePattern> morsePatternList = new ArrayList<>();
    private int counter = 0;
    long startVibrate;
    long afterVibrate;
    boolean recording = false;
    private int id = 0;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        recyclerView =  (RecyclerView) findViewById(R.id.morseList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.sharedPrefKey), MODE_PRIVATE);
        gson = new Gson();

        vibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        myAdapter = new MyAdapter(morsePatternList, vibrator);
        recyclerView.setAdapter(myAdapter);

        recordButton= (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        morseButton = (Button) findViewById(R.id.morseButton);

        recordButton.setOnTouchListener(this);
        playButton.setOnTouchListener(this);
        playButton.setEnabled(false);
        morseButton.setOnTouchListener(this);
        morseButton.setEnabled(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.recordButton:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!recording) {
                        afterVibrate = System.currentTimeMillis(); // start Recordinpg
                        recordButton.setText("Stop Recording");
                        recording = true;
                        morseButton.setEnabled(true);
                    } else {
                        recording = false;
                        morseButton.setEnabled(false);
                        recordButton.setText("Record Pattern");
                        playButton.setEnabled(true);

                        if(morsePattern.size() > 0) {
                            morsePatternList.add(new MorsePattern(id++, morsePattern));
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case R.id.morseButton:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    startVibrate = System.currentTimeMillis();
                    morsePattern.add(counter++, (startVibrate-afterVibrate));
                    vibrator.vibrate(100000);
                    Log.d("Main:","beforeLastTime: " + (startVibrate-afterVibrate));
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    afterVibrate = System.currentTimeMillis();
                    vibrator.cancel();
                    morsePattern.add(counter++, (afterVibrate-startVibrate));
                }
                    break;
            case R.id.playButton:
                long[] vibrationPattern;
                Type type = new TypeToken<ArrayList<Long>>() {}.getType();
                String json = sharedPreferences.getString(getString(R.string.patternKey), "failed");
                ArrayList<Long> patternAsList =  gson.fromJson(json, type);
                vibrationPattern = new long[patternAsList.size()];
                for(int i = 0; i < patternAsList.size(); i++) {
                    vibrationPattern[i] = patternAsList.get(i);
                }
                vibrator.vibrate(vibrationPattern, -1); // -1 equals NO_REPEAT
                break;
        }

        return false;
    }
}