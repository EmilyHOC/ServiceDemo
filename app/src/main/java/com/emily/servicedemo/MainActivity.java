package com.emily.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emily.servicedemo.service.FirstService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bindServiceButton;
    private Button unbindServiceButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bindServiceButton=this.findViewById(R.id.bind_service);
        unbindServiceButton=this.findViewById(R.id.unbind_service);
        textView=this.findViewById(R.id.text);
        bindServiceButton.setOnClickListener(this);
        unbindServiceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_service:
                //创建意图
                Intent bindIntent=new Intent(this, FirstService.class);
                //绑定服务
                bindService(bindIntent,serviceConnection,Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(serviceConnection);
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private FirstService.MyBinder binder;
    private FirstService firstService;

    public ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //得到binder实例
            binder=(FirstService.MyBinder)service;
            //给Service中的message设置一个值
            binder.setData("MainActivity:");
            //得到Service实例
            firstService=binder.getService();
            //设置接口回调获取Service中的数据
            firstService.setOndataCallback(new FirstService.OnDataCallback() {
                @Override
                public void onDataChange(final String message) {
                    //在非UI线程想要修改UI界面的内容的时候，使用这个方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(message);
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
                firstService=null;
        }
    };

}
