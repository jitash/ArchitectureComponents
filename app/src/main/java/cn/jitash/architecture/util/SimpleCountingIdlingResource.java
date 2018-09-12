package cn.jitash.architecture.util;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jbs on 2018/9/12
 */
public class SimpleCountingIdlingResource implements IdlingResource {
    private final String mResourceName;
    private final AtomicInteger counter = new AtomicInteger(0);
    private volatile ResourceCallback resourceCallback;

    public SimpleCountingIdlingResource(String resourceName) {
        mResourceName = checkNotNull(resourceName);
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = resourceCallback;
    }

    public void increment() {
        counter.getAndIncrement();
    }

    public void decrement() {
        int counterVal = counter.decrementAndGet();
        if (counterVal == 0) {
            if (null != resourceCallback) {
                resourceCallback.onTransitionToIdle();
            }
        }
        if (counterVal < 0) {
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }


}
