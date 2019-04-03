package ch.joris.morse;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Joris on 03.04.19.
 */

public class ProgressViewHolder extends RecyclerView.ViewHolder implements WaitingListener {
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    SparseArray<WaitingTask> mWaitingTaskSparseArray;
    private int mId;

    ProgressViewHolder(View itemView, SparseArray<WaitingTask> mWaitingTaskSparseArray) {
        super(itemView);
        this.mWaitingTaskSparseArray = mWaitingTaskSparseArray;
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }

    /**
     * When binding the {@link ProgressObject} to the ViewHolder, ensure that there is some form
     * of ID held as a member variable to identify the task for this item, then update the
     * progress bar to it's current value and if a task already exists for this data item, update
     * the listener.
     *
     * @param progressObject	{@link ProgressObject} Data to be bound
     */
    void bind(final ProgressObject progressObject) {
        mId = progressObject.getId();
        mProgressText.setText(progressObject.getTitle());
        mProgressBar.setProgress(progressObject.getProgress());

        WaitingTask task = mWaitingTaskSparseArray.get(mId);
        if (task != null) {
            task.updateListener(ProgressViewHolder.this);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the task, set the listener, add to the task controller, and run
                WaitingTask task = new WaitingTask(progressObject, ProgressViewHolder.this);
                mWaitingTaskSparseArray.put(mId, task);
                task.execute();
            }
        });
    }

    @Override
    public void onProgressUpdated(int progress) {
        // Must be run on the UI thread, updates the progress bar to a new value via the callback
        mProgressBar.setProgress(progress);
    }

    public int getId() {
        return mId;
    }
}