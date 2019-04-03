package ch.joris.morse;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

import static android.content.ContentValues.TAG;

/**
 * Created by Joris on 03.04.19.
 */

public class WaitingTask extends AsyncTask<Void, Integer, Void> {
    private ProgressObject mProgressObject;
    private WeakReference<WaitingListener> mWaitingListenerWeakReference;

    WaitingTask(ProgressObject progressObject, WaitingListener waitingListener) {
        mProgressObject = progressObject;
        updateListener(waitingListener);
    }

    void updateListener(@Nullable WaitingListener waitingListener) {
        mWaitingListenerWeakReference = new WeakReference<>(waitingListener);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(50);	// Random delay
                mProgressObject.setProgress(i);	// Update data set
                publishProgress(i);	// Inform UI of progress
            }
        } catch (InterruptedException ie) {
            Log.e(TAG, "Interrupted Exception: " + ie.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mWaitingListenerWeakReference != null) {
            WaitingListener listener = mWaitingListenerWeakReference.get();
            if (listener != null) {
                listener.onProgressUpdated(values[0]);
            }
        }
    }
}