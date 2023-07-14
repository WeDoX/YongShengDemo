package com.onedream.yongshengdemo.yongsheng;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class YongSheng {

    private final YongShengConfig mConfigurations;
    private BufferedReader mBufferedReader;


    private YongSheng(YongShengConfig configurations) {
        this.mConfigurations = configurations;
    }

    public static void init(Context base, YongShengConfig configurations) {
        YongSheng client = new YongSheng(configurations);
        client.initDaemon(base);
    }

    private void initDaemon(Context base) {
        if (mConfigurations == null) {
            return;
        }
        String processName = getProcessName();
        String packageName = base.getPackageName();

        if (processName.startsWith(mConfigurations.selfConfigItem.processName)) {
            YongShengProcessAPI.process().onSelfCreate(base, mConfigurations);
        } else if (processName.startsWith(mConfigurations.assistConfigItem.processName)) {
            YongShengProcessAPI.process().onAssistCreate(base, mConfigurations);
        } else if (processName.startsWith(packageName)) {
            YongShengProcessAPI.process().onInit(base);
        }

        releaseIO();
    }


    private String getProcessName() {
        try {
            File file = new File("/proc/self/cmdline");
            mBufferedReader = new BufferedReader(new FileReader(file));
            return mBufferedReader.readLine().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void releaseIO() {
        if (mBufferedReader != null) {
            try {
                mBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBufferedReader = null;
        }
    }
}
