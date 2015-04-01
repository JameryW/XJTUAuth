package edu.xjtu.XJTUAuth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class XJTUAuthUtils
{
    private static String TAG = "XJTUAuthUtils";

    public static boolean login(String username,String password)
    {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("action", "login"));
        params.add(new BasicNameValuePair("username",username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("ac_id", "1"));
        params.add(new BasicNameValuePair("type", "1"));
        params.add(new BasicNameValuePair("wbaredirect", "http://202.117.1.166:8080/xjtu/index.do?method"));
        params.add(new BasicNameValuePair("mac", ""));
        params.add(new BasicNameValuePair("user_ip", ""));

        String baseUrl = "http://10.6.8.2/cgi-bin/srun_portal";

        try
        {
            HttpPost postMethod = new HttpPost(baseUrl);
            postMethod.setEntity(new UrlEncodedFormEntity(params));
            postMethod.addHeader("Accept", "*/*");
            //postMethod.addHeader("Accept-Language", "zh-cn");
            //postMethod.addHeader("Referer", "http://202.117.2.41/index.html");
            //postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");
            //postMethod.addHeader("Accept-Encoding", "gzip, deflate");
            //postMethod.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
            postMethod.addHeader("Host", "10.6.8.2");
            //postMethod.addHeader("DNT", "1");


            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(postMethod);
            Log.i(TAG, "Sending message.....");
            HttpEntity httpEntity=response.getEntity();
            if(response.getStatusLine().getStatusCode()==200)
            {
                Log.i(TAG, "resCode = " + response.getStatusLine().getStatusCode());
                //Log.i(TAG, "result = " + EntityUtils.toString(httpEntity)+" "+httpEntity.getContentLength());
                if(httpEntity==null||httpEntity.getContentLength()==0)
                {
                    Log.i(TAG, "Login failed!!!");
                    return false;
                }else
                {
                    Log.i(TAG, "Login succeed!!!");
                    return true;
                }
                //Log.i(TAG, "Login succeed!!!");
                //return true;
            }
            Log.i(TAG, "resCode = " + response.getStatusLine().getStatusCode());
            Log.i(TAG, "result = " + EntityUtils.toString(httpEntity)+" "+httpEntity.getContentLength());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.i(TAG, "Login failed!!!");
        return false;
    }

    public static boolean logout()
    {
        String baseUrl = "http://10.6.8.2/cgi-bin/srun_portal?action=logout&ac_id=1";

        try
        {
            HttpGet getMethod = new HttpGet(baseUrl);
            getMethod.addHeader("Accept", "*/*");
            //getMethod.addHeader("Accept-Language", "zh-cn");
            //getMethod.addHeader("Referer", "http://202.117.2.41/index.html");
            //getMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");
            //getMethod.addHeader("Accept-Encoding", "gzip, deflate");
            //getMethod.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
            getMethod.addHeader("Host", "10.6.8.2");
            //getMethod.addHeader("DNT", "1");


            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(getMethod);
            Log.i(TAG, "Sending message.....");
            HttpEntity httpEntity=response.getEntity();
            if(response.getStatusLine().getStatusCode()==200)
            {
                String message=EntityUtils.toString(httpEntity);
                Log.i(TAG, "Logout succeed!!! message="+message);
                return true;
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.i(TAG, "Logout failed!!!");
        return false;
    }

    public static boolean getkeepaliveinfo()
    {
        String baseUrl = "http://10.6.8.2/cgi-bin/keeplive?";

        try
        {
            HttpGet getMethod = new HttpGet(baseUrl);
            getMethod.addHeader("Accept", "*/*");
            //getMethod.addHeader("Accept-Language", "zh-cn");
            //getMethod.addHeader("Referer", "http://202.117.2.41/index.html");
            //getMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");
            //getMethod.addHeader("Accept-Encoding", "gzip, deflate");
            //getMethod.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
            getMethod.addHeader("Host", "10.6.8.2");
            //getMethod.addHeader("DNT", "1");


            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(getMethod);
            Log.i(TAG, "Sending message.....");
            HttpEntity httpEntity=response.getEntity();
            if(response.getStatusLine().getStatusCode()==200)
            {
                String message=EntityUtils.toString(httpEntity);
                if(httpEntity==null||message.compareTo("error")==0)
                {
                    Log.i(TAG, "Get keepalive info failed!!!message="+message);
                    return false;
                }else
                {
                    Log.i(TAG, "Get keepalive info succeed!!!message="+message);
                    return true;
                }
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.i(TAG, "Get keepalive info failed!!!");
        return false;
    }
    
    public static void keepalive()
    {
    	new Thread(new KeepAliveThread()).start();
    }
}
