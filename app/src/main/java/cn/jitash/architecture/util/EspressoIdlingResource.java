package cn.jitash.architecture.util;

import android.support.test.espresso.IdlingResource;

/**
 * Created by jbs on 2018/9/12
 * Constants a static reference to {@link android.support.test.espresso.IdlingResource}, only available
 * in the 'mock' build type.
 */
public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";
    private static SimpleCountingIdlingResource mCountingIdingResource = new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdingResource.increment();
    }

    public static void decrement() {
        mCountingIdingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdingResource;
    }

}
