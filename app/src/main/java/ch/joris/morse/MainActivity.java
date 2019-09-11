package ch.joris.morse;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnTouchListener {
    // TODO: implement communication function, where you can send morse-messages to a friend, maybe even in the background, when the phone is locked

    private Gson gson;

    private Button recordButton;
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

    private BluetoothAdapter bluetoothAdapter;

    private final int REQUEST_ENABLE_BT = 1; // find out what this is exactly


    // bluetooth: In order to receive information about each device discovered, your application must register a BroadcastReceiver for the ACTION_FOUND intent.
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d("bluetooth", "Name: " + deviceName);
                Log.d("bluetooth", "MAC: " + deviceHardwareAddress);
                String name2 = device.getName();
                Log.d("bluetooth", "Name 2: " + name2);


                // TODO: To initiate a connection with a Bluetooth device, all that's needed from the associated BluetoothDevice object is the MAC address, which you retrieve by calling getAddress()
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            // this device doesnt support bluetooth
        }
        boolean enabled = bluetoothAdapter.isEnabled();
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        // from here, we can call startDiscovery
        // The discovery process usually involves an inquiry scan of about 12 seconds, followed by a page scan of each device found to retrieve its Bluetooth name.
        boolean discoverySuccessfullyStarted = bluetoothAdapter.startDiscovery();

        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.morseList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.sharedPrefKey), MODE_PRIVATE);
        gson = new Gson();

        vibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        /* todo. implement new adapter for asynchronous callbacks in progessbar in recyclerview
         ProgressAdapter progressAdapter = new ProgressAdapter();
         recyclerView.setAdapter(progressAdapter);

        List<ProgressObject> progressObjects = new ArrayList<>();
        for (int i = 0; i < NUM_OF_ITEMS; i++) {
            progressObjects.add(new ProgressObject(i, "Position " + i, 0));
        }
        progressAdapter.updateProgressObjects(progressObjects, mWaitingTaskSparseArray);
    */

        myAdapter = new MyAdapter(morsePatternList, vibrator);
        recyclerView.setAdapter(myAdapter);

        recordButton = (Button) findViewById(R.id.recordButton);
        morseButton = (Button) findViewById(R.id.morseButton);

        recordButton.setOnTouchListener(this);
        morseButton.setOnTouchListener(this);
        morseButton.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String asd = "asd";
        // super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.recordButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!recording) {
                        afterVibrate = System.currentTimeMillis(); // start Recordinpg
                        recordButton.setText("Stop Recording");
                        recording = true;
                        morseButton.setEnabled(true);
                    } else {
                        recording = false;
                        morseButton.setEnabled(false);
                        recordButton.setText("Record Pattern");
                        if (morsePattern.size() > 0) {
                            morsePatternList.add(new MorsePattern(id++, morsePattern));
                            myAdapter.notifyDataSetChanged();
                            morsePattern = new ArrayList<>();
                            counter = 0;
                        }
                    }
                }
                break;
            case R.id.morseButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startVibrate = System.currentTimeMillis();
                    morsePattern.add(counter++, (startVibrate - afterVibrate));
                    vibrator.vibrate(100000);
                    Log.d("Main:", "beforeLastTime: " + (startVibrate - afterVibrate));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    afterVibrate = System.currentTimeMillis();
                    vibrator.cancel();
                    morsePattern.add(counter++, (afterVibrate - startVibrate));
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);

    }
}