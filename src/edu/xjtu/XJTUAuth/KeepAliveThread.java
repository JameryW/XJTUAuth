package edu.xjtu.XJTUAuth;

import android.util.Log;

public class KeepAliveThread implements Runnable
{
	private static String TAG = "KeepAliveThread";
	
	@Override
	public void run() 
	{
		boolean flag=true;
    	while (flag) 
    	{
        	try 
        	{
        		if(XJTUAuthUtils.getkeepaliveinfo())
                {
        			Thread.sleep(1000);//线程暂停10秒，单位毫秒
                }
                else
                {
                	flag=false;
                	Log.i(TAG, "KeepAliveThread over!!!");
                }
        	} catch (InterruptedException e) 
        	{
        		e.printStackTrace();
        	}
        }
	}
}
