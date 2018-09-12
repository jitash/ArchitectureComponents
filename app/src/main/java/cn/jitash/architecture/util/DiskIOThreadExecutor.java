package cn.jitash.architecture.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

/**
 * Created by jbs on 2018/9/12
 */
public class DiskIOThreadExecutor implements Executor {
    private final Executor mDiskIO;

    public DiskIOThreadExecutor() {
        mDiskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@Nonnull Runnable command) {
        mDiskIO.execute(command);
    }
}
