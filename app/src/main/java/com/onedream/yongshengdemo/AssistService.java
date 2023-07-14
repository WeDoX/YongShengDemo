package com.onedream.yongshengdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AssistService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        //
        try{
            //调用startForeground() 方法
            startForeground(2, getNotification(this, "ObserveServiceAPP通知", "ObserveService欢迎来到APP！"));//创建一个通知，创建通知前记得获取开启通知权限
        }catch (Exception e){
            e.printStackTrace();
            Log.e("YongSheng","ObserveService onCreate 异常"+e.toString());
        }

        Log.e("YongSheng","ObserveService onCreate done");
    }

    private Notification getNotification(Context context, String title, String text) {
        boolean isSilent = true;//是否静音
        boolean isOngoing = true;//是否持续(为不消失的常驻通知)
        String channelName = "服务常驻通知";
        String channelId = "Service_Id";
        String category = Notification.CATEGORY_SERVICE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent nfIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nfIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent) //设置PendingIntent
                .setSmallIcon(R.mipmap.ic_launcher) //设置状态栏内的小图标
                .setContentTitle(title) //设置标题
                .setContentText(text) //设置内容
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//设置通知公开可见
                .setOngoing(isOngoing)//设置持续(不消失的常驻通知)
                .setCategory(category)//设置类别
                .setPriority(NotificationCompat.PRIORITY_MAX);//优先级为：重要通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//安卓8.0以上系统要求通知设置Channel,否则会报错
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);//锁屏显示通知
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelId);
        }
        return builder.build();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
