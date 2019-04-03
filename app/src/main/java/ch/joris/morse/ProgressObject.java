package ch.joris.morse;

/**
 * Created by Joris on 03.04.19.
 */

public class ProgressObject {
    private int mId;
    private String mTitle;
    private int mProgress;

    ProgressObject(int id, String title, int progress) {
        mId = id;
        mTitle = title;
        mProgress = progress;
    }

    int getId() {
        return mId;
    }

    void setId(int id) {
        mId = id;
    }

    String getTitle() {
        return mTitle;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    int getProgress() {
        return mProgress;
    }

    void setProgress(int progress) {
        mProgress = progress;
    }
}