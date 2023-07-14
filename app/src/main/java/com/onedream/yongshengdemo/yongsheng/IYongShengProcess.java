package com.onedream.yongshengdemo.yongsheng;

import android.content.Context;

public interface IYongShengProcess {
	/**
	 * Initialization some files or other when 1st time 
	 */
	boolean onInit(Context context);

	/**
	 * when self process create
	 * 
	 */
	void onSelfCreate(Context context, YongShengConfig config);

	/**
	 * when assist process create
	 */
	void onAssistCreate(Context context, YongShengConfig config);

	/**
	 * when watches the process dead which it watched
	 */
	void haveProcessDead();
}
