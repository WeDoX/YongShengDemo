package com.onedream.yongshengdemo.yongsheng;

public class YongShengConfig {

    public final YongShengConfigItem selfConfigItem;
    public final YongShengConfigItem assistConfigItem;

    public YongShengConfig(YongShengConfigItem selfConfigItem, YongShengConfigItem assistConfigItem) {
        this.selfConfigItem = selfConfigItem;
        this.assistConfigItem = assistConfigItem;
    }

    public static class YongShengConfigItem {
        final String processName;
        final Class serviceNameClass;

        public YongShengConfigItem(String processName, Class serviceNameClass) {
            this.processName = processName;
            this.serviceNameClass = serviceNameClass;
        }
    }
}
