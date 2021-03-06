package tech.xinong.xnsm.pro.base.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import java.util.LinkedList;

import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2016/11/17.
 */

public class UnCatchExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CatchExcep";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    XnsApplication application;

    public UnCatchExceptionHandler(XnsApplication application) {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            Intent intent = new Intent(application.getApplicationContext(),
                    MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    application.getApplicationContext(), 0, intent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            // 1秒钟后重启应用
            AlarmManager mgr = (AlarmManager) application
                    .getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent);
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        SharedPreferences preferences = application.getApplicationContext()
                .getSharedPreferences("error.log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(System.currentTimeMillis()),
                ex.toString() + "<------>" + ex.getMessage());
        editor.commit();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                T.showShort(application.getApplicationContext(),
                        "很抱歉,程序出现异常,即将退出!");
                Looper.loop();
            }
        }.start();

        LinkedList ll = new LinkedList();
        return true;
    }
}
