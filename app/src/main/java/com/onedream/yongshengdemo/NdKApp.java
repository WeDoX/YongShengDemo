package com.onedream.yongshengdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.onedream.yongshengdemo.yongsheng.YongSheng;
import com.onedream.yongshengdemo.yongsheng.YongShengConfig;

public class NdKApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //
        Log.e("YongSheng", "NdKApp===" + this.toString() + "====" + getProcessNameByPID(this, Process.myPid()) + "=====" + Process.myPid());
        //
        YongSheng.init(base, new YongShengConfig(
                new YongShengConfig.YongShengConfigItem(
                        getPackageName() + ":dead",
                        DeadService.class),
                new YongShengConfig.YongShengConfigItem(
                        getPackageName() + ":assist",
                        AssistService.class)
        ));
    }

    /**
     * 根据 pid 获取进程名
     *
     * @param context
     * @param pid
     * @return
     */
    public String getProcessNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return "";
        }
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo == null) {
                continue;
            }
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }
}
