package com.example.ldtest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李非 on 2017/10/25.
 */

public class ActivityCollector {    //用于管理所有的活动
    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
