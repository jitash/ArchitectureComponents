package cn.jitash.architecture.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by jbs on 2018/9/13
 */
public class SnackbarUtils {
    public static void showSnackbar(View v, String snackbarText) {
        if (v == null || snackbarText == null) {
            return;
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_LONG).show();
    }
}
