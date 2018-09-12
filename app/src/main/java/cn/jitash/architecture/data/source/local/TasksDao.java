package cn.jitash.architecture.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cn.jitash.architecture.data.Task;

/**
 * Created by jbs on 2018/9/12
 */
@Dao
public interface TasksDao {
    @Query("SELECT *FROM tasks")
    List<Task> getTasks();

    @Query("SELECT * FROM tasks WHERE entryid = :taskId")
    Task getTaskById(String taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("UPDATE tasks SET completed = :completed WHERE entryid =:taskId")
    void updateCompleted(String taskId, boolean completed);

    @Query("DELETE FROM tasks WHERE entryid = :taskId")
    int deleteTaskById(String taskId);

    @Query("DELETE FROM tasks")
    void deleteTasks();

    @Query("DELETE FROM tasks WHERE completed = 1")
    int deleteCompletedTasks();
}
