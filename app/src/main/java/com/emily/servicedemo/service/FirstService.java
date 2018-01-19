package com.emily.servicedemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class FirstService extends Service{
    private Thread thread;
    private FirstService.ServiceThread serviceThread;
    private IBinder binder=new MyBinder();
    private String message;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service","onBind");
        serviceThread=new ServiceThread();
        thread =new Thread(serviceThread);
        //开启一个线程
        thread.start();
        return binder;//Activity和Service通信的桥梁
    }

    @Override
    public void onCreate(){
        Log.i("service","onCreate");
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("service","onStartCommand");
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public boolean onUnbind(Intent intent){
        Log.i("service","onUnbind");
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        serviceThread.flag=false;//结束run方法的循环
        Log.i("service","onDestroy");

    }

    public class ServiceThread implements Runnable{
        //用volatile修饰保证变量在线程可见
        volatile boolean flag=true;
        @Override
        public void run() {
            Log.i("service","thread开始运行了");
            int i=1;
            while (flag){
                if(mOnDataCallback!=null){
                    mOnDataCallback.onDataChange(message+i);
                }
                i++;
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }
        public class MyBinder extends Binder {
            public  void setData(String message) {
            //从Activity传入message的值
            FirstService.this.message = message;
        }

            public FirstService getService() {
                return FirstService.this;
        }
    }
            private OnDataCallback mOnDataCallback=null;
            public interface OnDataCallback{
                void onDataChange(String message);
        }
        public void setOndataCallback(OnDataCallback mOnDataCallback){
            this.mOnDataCallback=mOnDataCallback;
    }
}

