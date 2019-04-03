package ch.joris.morse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joris on 03.04.19.
 */

public class ProgressAdapter extends RecyclerView.Adapter<ProgressViewHolder> {
    private List<ProgressObject> mProgressObjectList = new ArrayList<>();
    private SparseArray<WaitingTask> mWaitingTaskSparseArray;

    void updateProgressObjects(@NonNull List<ProgressObject> progressObjects, SparseArray<WaitingTask> mWaitingTaskSparseArray) {
        this.mWaitingTaskSparseArray = mWaitingTaskSparseArray;
        mProgressObjectList = progressObjects;
        notifyDataSetChanged();
    }

    @Override
    public ProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false); // todo: debug and check if row layout is the correct view to be inflated here
        return new ProgressViewHolder(root, mWaitingTaskSparseArray);
    }

    @Override
    public void onBindViewHolder(ProgressViewHolder holder, int position) {
        holder.bind(mProgressObjectList.get(position));
    }

    /**
     * If a task has been created for the {@link ProgressObject} for this {@link ProgressViewHolder}
     * then the listener is removed and the view is then recycled
     * @param holder	{@link ProgressViewHolder} ViewHolder to recycle
     */
    @Override
    public void onViewRecycled(ProgressViewHolder holder) {
        WaitingTask task = mWaitingTaskSparseArray.get(holder.getId());
        if (task != null) {
            task.updateListener(null);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mProgressObjectList.size();
    }
}
