package com.example.ldtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);  //得到Toolbar的实例
        setSupportActionBar(toolbar);       //调用方法传入toolbar实例，做到即使用toolbar，又让他的外观和功能与actionbar一致
        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);//显示导航
       NavigationView navView=(NavigationView)findViewById(R.id.nav_view);//1.菜单的点击事件
        ActionBar actionBar=getSupportActionBar();  //实际上ActionBar的具体实现是由ToolBar实现的
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);  //让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);//设置导航按钮的图标
        }
       navView.setCheckedItem(R.id.nav_msg);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    //菜单处理,在该方法中加载了toolbar.xml这个菜单文件
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.quit:             //强制退出登录
                Intent intent=new Intent("com.example.ldtest.FORCE_OFFLINE");
                //广播，通知程序强制下线，该逻辑写在广播接收器里
                sendBroadcast(intent);
            case android.R.id.home:         //显示导航栏
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
}
