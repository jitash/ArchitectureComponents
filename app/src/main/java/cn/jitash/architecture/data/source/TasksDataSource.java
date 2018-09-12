package cn.jitash.architecture.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import cn.jitash.architecture.data.Task;

/**
 * Created by jbs on 2018/9/12
 */
public interface TasksDataSource {
    interface LoadTasksCallBack {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallBack {
        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallBack callBack);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallBack callBack);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Task task);

    void activateTask(@NonNull String taskId);

    void clearCompletedTasks();

    void refreshTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);
}
