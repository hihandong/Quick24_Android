package myLogger;

import android.util.Log;

/**
 * Created by senior on 2016/9/2.
 */
public class L {

    public static void e() {
        e("");
    }

    public static void e(Object str) {
        e("My log=>", str + "");
    }

    public static void e(Object tag, Object str) {
        StackTraceElement targetTrace = getTargetStackTraceElement();
        if (targetTrace == null) {
            Log.e(tag + "", str + "没有获得 StackTrace信息!");
        } else {
            Log.e(tag + "", str + "(" + targetTrace.getFileName() + ":" + targetTrace.getLineNumber() + ")");
        }

    }

    public static void i() {
        i("");
    }

    public static void i(Object str) {
        i("My log=>", str + "");
    }

    public static void i(Object tag, Object str) {
        StackTraceElement targetTrace = getTargetStackTraceElement();
        if (targetTrace == null) {
            Log.e(tag + "", str + "没有获得 StackTrace信息!");
        } else {
            Log.i(tag + "", str + "(" + targetTrace.getFileName() + ":" + targetTrace.getLineNumber() + ")");
        }
    }

    private static StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(L.class.getName());
            // Log.i(String.valueOf(isLogMethod), stackTraceElement.getClassName()+":"+L.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                // break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }
}
