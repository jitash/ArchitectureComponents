package cn.jitash.architecture;

import android.content.Context;
import android.support.annotation.NonNull;

import cn.jitash.architecture.data.FakeTasksRemoteDataSource;
import cn.jitash.architecture.data.source.TasksRepository;
import cn.jitash.architecture.data.source.local.TasksLocalDataSource;
import cn.jitash.architecture.data.source.local.ToDoDatabase;
import cn.jitash.architecture.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jbs on 2018/9/14
 */
public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        ToDoDatabase database = ToDoDatabase.getInstance(context);
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(),
                        database.taskDao()));


    }
}
