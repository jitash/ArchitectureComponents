package cn.jitash.architecture;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Created by jbs on 2018/9/14
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
public class SnackbarMessage extends SingleLiveEvent<Integer> {

    public void observe(@NonNull LifecycleOwner owner, @NonNull final SnackbarObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) {
                    return;
                }
                observer.onNewMessage(integer);
            }
        });
    }

    public interface SnackbarObserver {
        void onNewMessage(@StringRes int snackbarMessageResourceId);
    }
}
