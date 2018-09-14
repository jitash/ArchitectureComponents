package cn.jitash.architecture.data;

import android.support.annotation.NonNull;

import cn.jitash.architecture.data.source.TasksDataSource;

/**
 * Created by jbs on 2018/9/14
 */
public class FakeTasksRemoteDataSource implements TasksDataSource {
    private static FakeTasksRemoteDataSource INSTANCE;

    public static FakeTasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeTasksRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallBack callBack) {

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallBack callBack) {

    }

    @Override
    public void saveTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }
}
