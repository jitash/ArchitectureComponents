package cn.jitash.architecture.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Created by jbs on 2018/9/12
 */
@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = "description")
    private final String mDescription;

    @Nullable
    @ColumnInfo(name = "completed")
    private final boolean mCompeleted;

    /**
     * Use this constructor to create a new active Task.
     *
     * @param title       title fo the task
     * @param description description of the task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description) {
        this(UUID.randomUUID().toString(), title, description, false);
    }

    /**
     * Use this constructor to create an active Task if the Task already has an id (copy of another Task).
     *
     * @param id          id of the task
     * @param title       title of the task
     * @param description description of the task
     */
    @Ignore
    public Task(@NonNull String id, @Nullable String title, @Nullable String description) {
        this(id, title, description, false);
    }

    /**
     * Use this constructor to create a new comopleted Task.
     *
     * @param title       title of the task
     * @param description description of the task
     * @param completed   true if the task is completed, false if it's active
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, @Nullable boolean completed) {
        this(UUID.randomUUID().toString(), title, description, completed);
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id(copy of another
     * Task).
     *
     * @param id          id of the task
     * @param title       title of the task
     * @param description description of the task
     * @param completed   true if the task is completed, false if it's active
     */
    public Task(@NonNull String id, @Nullable String title, @Nullable String description, @Nullable boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompeleted = completed;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public boolean isCompeleted() {
        return mCompeleted;
    }

    public boolean isActive() {
        return !mCompeleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) && Strings.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
