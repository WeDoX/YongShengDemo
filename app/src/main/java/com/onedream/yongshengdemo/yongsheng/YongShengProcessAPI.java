package com.onedream.yongshengdemo.yongsheng;


class YongShengProcessAPI {

    private static volatile IYongShengProcess mIYongShengProcess;

    static IYongShengProcess process() {
        if (mIYongShengProcess != null) {
            return mIYongShengProcess;
        }
        mIYongShengProcess = new YongShengProcess();
        return mIYongShengProcess;
    }
}
