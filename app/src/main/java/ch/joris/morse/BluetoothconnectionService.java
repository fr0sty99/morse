package ch.joris.morse;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Joris on 22.04.19.
 */

public class BluetoothconnectionService {

    // TODO: do logs: errors, server setup  with uuid, run, etc...

    // TODO: next tut: https://www.youtube.com/watch?v=UGtMNk9lriw
    
    private static final String TAG = "BluetoothconnectionServ";
    private static final  String appname = "bluetoothAdapter";
    private static final UUID MY_UUID_INSECURE = UUID.randomUUID();
    private final BluetoothAdapter bluetoothAdapter;
    Context context;

    private AcceptThread insecureAcceptThread;


    public BluetoothconnectionService(Context context, BluetoothAdapter adapter) {
        this.context = context;
        bluetoothAdapter = adapter;

    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appname, MY_UUID_INSECURE);
            } catch (IOException e) {
                e.printStackTrace();

            }

            serverSocket = tmp;

        }

        public void run() {
            BluetoothSocket socket = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket != null) {
                connected(socket, device);
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
