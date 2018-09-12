package cn.jitash.architecture.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import cn.jitash.architecture.data.Task;
import cn.jitash.architecture.util.EspressoIdlingResource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jbs on 2018/9/12
 */
public class TasksRepository implements TasksDataSource {
    private volatile static TasksRepository INSTANCE = null;
    private final TasksDataSource mTasksRemoteDataSource;
    private final TasksDataSource mTasksLocalDataSource;

    Map<String, Task> mCachedTasks;
    private boolean mCacheIsDirty = false;

    public TasksRepository(TasksDataSource tasksRemoteDataSource, TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = tasksRemoteDataSource;
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource, TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (TasksRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksRepository(tasksLocalDataSource, tasksLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallBack callBack) {
        checkNotNull(callBack);
        if (mCachedTasks != null && !mCacheIsDirty) {
            callBack.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        EspressoIdlingResource.increment();

        if (mCacheIsDirty) {
            getTasksFromRemoteDataSource(callBack);
        } else {
            mTasksLocalDataSource.getTasks(new LoadTasksCallBack() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    EspressoIdlingResource.decrement();
                    callBack.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callBack);
                }
            });
        }

    }

    private void getTasksFromRemoteDataSource(@Nonnull final LoadTasksCallBack callBack) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallBack() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                EspressoIdlingResource.decrement();
                callBack.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                EspressoIdlingResource.decrement();
                callBack.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTasksLocalDataSource.saveTask(task);
        }
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallBack callBack) {
        checkNotNull(taskId);
        checkNotNull(callBack);
        final Task cachedTask = getTaskWithId(taskId);
        if (cachedTask != null) {
            callBack.onTaskLoaded(cachedTask);
            return;
        }
        EspressoIdlingResource.increment();
        mTasksLocalDataSource.getTask(taskId, new GetTaskCallBack() {
            @Override
            public void onTaskLoaded(Task task) {
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
                mCachedTasks.put(task.getId(), task);
                EspressoIdlingResource.decrement();
                ;
                callBack.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                EspressoIdlingResource.decrement();
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.completeTask(task);
        mTasksLocalDataSource.completeTask(task);
        Task completedTask = new Task(task.getId(), task.getTitle(), task.getDescription(), true);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.activateTask(task);
        mTasksLocalDataSource.activateTask(task);
        Task activeTask = new Task(task.getId(), task.getTitle(), task.getDescription());
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksLocalDataSource.clearCompletedTasks();
        mTasksRemoteDataSource.clearCompletedTasks();
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompeleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));
        mTasksRemoteDataSource.deleteTask(checkNotNull(taskId));
        mCachedTasks.remove(taskId);
    }

    private Task getTaskWithId(@Nonnull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;

        } else {
            return mCachedTasks.get(id);
        }
    }
}
