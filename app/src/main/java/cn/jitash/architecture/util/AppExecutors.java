package cn.jitash.architecture.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by jbs on 2018/9/12
 */
public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public AppExecutors() {
        this(new DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT), new MainThreadExecutor());
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
