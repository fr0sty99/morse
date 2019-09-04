package ch.joris.morse;

import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joris on 14.03.19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {
    private ArrayList<MorsePattern> values;
    private Vibrator vibrator;
    public ProgressBar progressBar;
    public int progressStatus = 0;
    private Handler handler = new Handler();

    public MorsePattern findVibrationPatternById(int id) {
        MorsePattern pattern = null;
        int counter = 0;
        while(pattern == null || pattern.getId() != id) {
            pattern = values.get(counter++);
        }
        return pattern;
    }

    public MyAdapter(ArrayList<MorsePattern> data, Vibrator vibrator) {
        values = data;
        this.vibrator = vibrator;
        // TODO: DEBUG: finish list with patterns for save and replay function.

    }

    @Override
    public void onClick(View v) {
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        int id = Integer.valueOf(((TextView) parent.findViewById(R.id.textView)).getText().toString());
        // start vibration
        MorsePattern pattern = findVibrationPatternById(id);
        ArrayList<Long> morsePattern = pattern.getMorsePattern();
        long[] vibrationPattern = new long[morsePattern.size()];
        for (int i = 0; i < morsePattern.size(); i++) {
            vibrationPattern[i] = morsePattern.get(i);
        }

        vibrator.vibrate(vibrationPattern, -1);

        Log.d(",,", "PLAYING MORSE WITH ID: " + id);
    }

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView textView;
    public Button playButton;
    public ProgressBar progressBar;

    public MyViewHolder(View v) {
        super(v);
        textView = (TextView) v.findViewById(R.id.textView);
        playButton = (Button) v.findViewById(R.id.playFromListButton);

      /*  new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start(); */
    }
}

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MorsePattern pattern = values.get(position);
        holder.textView.setText(String.valueOf(pattern.getId()));
        holder.playButton.setOnClickListener(this);

        // TODO: find out what add() and remove() is for
        // add(position, pattern);

        // TODO: write remove funuction with an icon in the card
    }

    public void add(int position, MorsePattern item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}


/*
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView txtHeader;

    public View layout;

    public ViewHolder(View v) {
        super(v);
        layout = v;
        txtHeader = (TextView) v.findViewById(R.id.textView);
    }
}
 */