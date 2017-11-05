package com.example.ldtest;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by 李非 on 2017/10/25.
 */

public class BaseActivity extends AppCompatActivity {    //所有活动的父类
    private ForceOfflineReceiver receiver;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        ActivityCollector.addActivity(this);
        //初始化Bmob的SDK
        //加载Bmob的SDK
        Bmob.initialize(this,"d5b2c8b29827b087bfd96f0892fff1d8");

    }
    @Override
    protected void onResume(){ //一个活动位于栈顶就注册广播接收器
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.ldtest.FORCE_OFFLINE");
        receiver =new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);  //注册广播接收器的方法
    }
    @Override
    protected void onPause(){//一个活动失去栈顶就取消注册广播接收器
        super.onPause();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class ForceOfflineReceiver extends BroadcastReceiver{ //广播接收器
        @Override
        public void onReceive(final Context context,Intent intent){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("确认退出当前账号？");
            builder.setCancelable(false);
            builder.setPositiveButton("是",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                    ActivityCollector.finishAll();    //销毁所有活动
                    Intent intent=new Intent(context,LoginActivity.class);
                    context.startActivity(intent);   //重新启动LoginActivity
                }
            });
            builder.setNegativeButton("否",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                }
            });
            builder.show();
        }
    }

}
