package com.ctsi.android.instrumentation;

import android.util.Log;

/**
 * Created by doulala on 2017/3/2.
 */

public class AopInteceptor {
    public static void before() {
        Log.e("AOP", "before");
    }
}
