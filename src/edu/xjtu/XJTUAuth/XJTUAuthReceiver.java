package edu.xjtu.XJTUAuth;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class XJTUAuthReceiver extends BroadcastReceiver
{
    private static final String TAG = "XJTUAuthReceiver";
    private static final String PREFS_NAME = "XJTUAuthUserInfo";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "BootReceived");
        if(intent.getAction().compareTo("android.intent.action.BOOT_COMPLETED")==0)
        {
            HashMap<String, String> configInfo = LoadUserData(context);
            if (!configInfo.isEmpty())
            {
                String username = configInfo.get("username");
                String password = configInfo.get("password");

                XJTUAuthUtils.login(username, password);
                XJTUAuthUtils.keepalive();
            }
        }
    }

    private HashMap<String, String> LoadUserData(Context context)
    {
        HashMap<String, String> configInfo = new HashMap<String, String>();
        //载入配置文件
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, 0);
        //读取配置文件
        if (sp.getBoolean("isSaved", false))
        {
            String username = sp.getString("username", "");
            String password = sp.getString("password", "");
            if (!("".equals(username) && "".equals(password)))
            {
                if(sp.getBoolean("autoStart", true))
                {
                    configInfo.put("username", username);
                    configInfo.put("password", password);
                }
            }
        }
        return configInfo;
    }
}
