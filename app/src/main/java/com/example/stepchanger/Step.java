package com.example.stepchanger;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by 科 on 2018/8/1.
 */

public class Step {
    static final int CURRENT_TODAY_STEP = 201;
    static final int SAVE_TODAY_TIME = 202;
    static final int PRE_SENSOR_STEP = 203;
    static final int LAST_SAVE_STEP_TIME = 204;
    static final int SENSOR_TIME_STAMP = 209;

    static final int SUCCESS = 0;
    static final int FAIL = 1;

    static final String WECHAT = "com.tencent.mm";
    static final String fileName = "stepcounter.cfg";
    private Context context = null;
    private HashMap<Integer,Long> mHashMap = null;
    private String filePath = "";

    public Step(Context context,String filePath){
        this.context = context;
        this.filePath = filePath;

        getAuth();

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath+fileName));
            ObjectInput oin = new ObjectInputStream(in);
            mHashMap = (HashMap<Integer,Long>) oin.readObject();
            oin.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取权限
    private int getAuth(){
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes("chmod o+rw "+ filePath+fileName+"\n");
            dos.flush();
            dos.writeBytes("chmod o+x "+ filePath+"\n");
            dos.flush();
            dos.close();

            p.waitFor();
            Toast.makeText(context, "获取root权限成功", Toast.LENGTH_SHORT).show();
            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "获取root权限失败", Toast.LENGTH_SHORT).show();
            return FAIL;
        }
    }

    //读取数据
    private int readValue() {
        if(mHashMap == null){
            return FAIL;
        }

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath+fileName));
            ObjectInputStream oin = new ObjectInputStream(in);

            mHashMap = (HashMap<Integer,Long>) oin.readObject();

            oin.close();;
            in.close();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return FAIL;
        }
    }

    //写数据
    private int writeValue(){
        try{
            //杀掉后台微信进程
            ActivityManager am = (ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(WECHAT);

            //写入信息
            FileOutputStream out = new FileOutputStream(filePath+fileName);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(mHashMap);
            out.close();
            oout.close();
            return SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
            return FAIL;
        }
    }

    public long getStepCount(){
        if(readValue() == SUCCESS){
            return mHashMap.get(CURRENT_TODAY_STEP);
        }else{
            Log.d("Step", "getStepCount false");
            return 0;
        }
    }

    public int setStep(long step) {
        if(mHashMap == null) {
            return FAIL;
        }
        mHashMap.put(CURRENT_TODAY_STEP, step);
        return writeValue();
    }



}
