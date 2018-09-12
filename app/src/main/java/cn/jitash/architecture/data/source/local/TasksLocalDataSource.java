package cn.jitash.architecture.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import javax.annotation.Nonnull;

import cn.jitash.architecture.data.Task;
import cn.jitash.architecture.data.source.TasksDataSource;
import cn.jitash.architecture.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jbs on 2018/9/12
 */
public class TasksLocalDataSource implements TasksDataSource {
    private TasksDao mTasksDao;
    private AppExecutors mAppExecutors;

    private TasksLocalDataSource(@Nonnull AppExecutors appExecutors, @Nonnull TasksDao tasksDao) {
        mAppExecutors = appExecutors;
        mTasksDao = tasksDao;
    }

    private static volatile TasksLocalDataSource INSTANCE;

    public static TasksLocalDataSource getInstance(@Nonnull AppExecutors appExecutors, @Nonnull TasksDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = mTasksDao.getTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            callBack.onDataNotAvailable();
                        } else {
                            callBack.onTasksLoaded(tasks);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Task task = mTasksDao.getTaskById(taskId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task != null) {
                            callBack.onTaskLoaded(task);
                        } else {
                            callBack.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        checkNotNull(task);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.insertTask(task);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void completeTask(@NonNull final Task task) {
        Runnable completedRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), true);
            }
        };
        mAppExecutors.diskIO().execute(completedRunnable);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        //Not required for the local data source because the {@link TasksRepository}handles converting from a
        //{@code taskId} to a {@link task}using it's cached data.
    }

    @Override
    public void activateTask(@NonNull final Task task) {
        Runnable activateRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), false);
            }
        };
        mAppExecutors.diskIO().execute(activateRunnable);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        //Not required for the local data source because the {@link TasksRepository}handles converting from a
        //{@code taskId} to a {@link task}using it's cached data.
    }

    @Override
    public void clearCompletedTasks() {
        Runnable clearTasksRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteCompletedTasks();
            }
        };
        mAppExecutors.diskIO().execute(clearTasksRunnable);
    }

    @Override
    public void refreshTasks() {
        //Not required for the local data source because the {@link TasksRepository}handles converting from a
        //{@code taskId} to a {@link task}using it's cached data.
    }

    @Override
    public void deleteAllTasks() {
        Runnable deleteAllRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteTasks();
            }
        };
        mAppExecutors.diskIO().execute(deleteAllRunnable);
    }

    @Override
    public void deleteTask(@NonNull final String taskId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.getTaskById(taskId);
            }
        };
        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}
