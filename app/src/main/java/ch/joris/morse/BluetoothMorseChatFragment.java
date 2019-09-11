package ch.joris.morse;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class BluetoothMorseChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothMorseChatService chatService;

    // 1 view to display messages, on the bottom on this view theres a button to start record a pattern, record a pattern and send it. (overlay with showing/hiding each buttons)
    private RecyclerView conversationView;
    private Button startRecordButton;
    private Button recordPatternButton;
    private Button sendMorseButton;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<MorsePattern> conversationArrayAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // get local bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if adapter is null, bluetooth is not supported on this device
        if(bluetoothAdapter == null) {
            FragmentActivity activity = (FragmentActivity) getActivity();
            Toast.makeText(activity, "Bluetooth is not available on this device", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (chatService == null) {
            setupBluetoothMorseChat();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(chatService != null) {
            chatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (chatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (chatService.getState() == BluetoothMorseChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                chatService.start();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    public void setupBluetoothMorseChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

    }
}
