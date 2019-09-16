package ch.joris.morse;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;

public class BluetoothMorseChatService {

    private BluetoothAdapter adapter;
    private int state;
    private int newState;
    private Handler handler;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    public BluetoothChatService(Context context, Handler handler) {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.state = STATE_NONE;
        this.newState = state;
        this.handler = handler;
    }

    public synchronized void stop() {

    }

    public synchronized void start() {

    }

    public int getState() {
        return state;
    }
}
