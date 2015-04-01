package edu.xjtu.XJTUAuth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class XJTUAuthActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "XJTUAuthActivity";
    private static final String PREFS_NAME = "XJTUAuthUserInfo";
    private static EditText etUser;
    private static EditText etPsw;
    private static TextView tvNotice;
    private static CheckBox cbRemember;
    private static CheckBox cbAuto;
    private static CheckBox cbStart;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        etUser=(EditText)findViewById(R.id.etUser);
        etPsw=(EditText)findViewById(R.id.etPsw);
        tvNotice = (TextView)findViewById(R.id.tvNotice);

        cbRemember = (CheckBox)findViewById(R.id.cbRemember);
        cbAuto = (CheckBox)findViewById(R.id.cbAuto);
        cbStart = (CheckBox)findViewById(R.id.cbStart);

        // Register the onClick listener with the implementation above
        Button buttonLogin = (Button)findViewById(R.id.btnLogin);
        Button buttonLogout = (Button)findViewById(R.id.btnLogout);
        buttonLogin.setOnClickListener(btnLoginListener);
        buttonLogout.setOnClickListener(btnLogoutListener);

        LoadUserData();

        if(cbAuto.isChecked()&&cbRemember.isChecked())
        {
            if(XJTUAuthUtils.login(etUser.getText().toString(),etPsw.getText().toString()))
            {
                tvNotice.setText("认证成功！");
                XJTUAuthUtils.keepalive();
            }else
            {
                tvNotice.setText("认证失败！");
            }
        }

        if(etUser.getText().toString().isEmpty()||etPsw.getText().toString().isEmpty())
        {
            tvNotice.setText("请输入用户名或密码！");
        }
    }
    private View.OnClickListener btnLoginListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Log.i(TAG, "username="+etUser.getText().toString());
            Log.i(TAG, "password="+etPsw.getText().toString());
            if(etUser.getText().toString().isEmpty()||etPsw.getText().toString().isEmpty())
            {
                tvNotice.setText("请输入用户名或密码！");
            }else
            {
                if(XJTUAuthUtils.login(etUser.getText().toString(),etPsw.getText().toString()))
                {
                    tvNotice.setText("认证成功！");
                    if(cbRemember.isChecked())
                        SaveUserData();
                    XJTUAuthUtils.keepalive();
                }else
                {
                    tvNotice.setText("认证失败！");
                }
            }
        }
    };

    private View.OnClickListener btnLogoutListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            LoadUserData();
            Log.i(TAG, "username:"+etUser.getText().toString());
            Log.i(TAG, "password:"+etPsw.getText().toString());

            if(etUser.getText().toString().isEmpty()||etPsw.getText().toString().isEmpty())
            {
                tvNotice.setText("请输入用户名或密码！");
            }else
            {
                if(XJTUAuthUtils.logout())
                {
                    tvNotice.setText("注销成功！");
                }else
                {
                    tvNotice.setText("注销失败！");
                }
            }
        }
    };

    private void LoadUserData()
    {
        //载入配置文件
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
        //读取配置文件
        if (sp.getBoolean("isSaved", false))
        {
            String username = sp.getString("username", "");
            String password = sp.getString("password", "");
            if (!("".equals(username) && "".equals(password)))
            {
                etUser.setText(username);
                etPsw.setText(password);
                cbRemember.setChecked(true);
                if(sp.getBoolean("autoAuth", true))
                {
                    cbAuto.setChecked(true);
                }else
                {
                    cbAuto.setChecked(false);
                }
                if(sp.getBoolean("autoStart", true))
                {
                    cbStart.setChecked(true);
                }else
                {
                    cbStart.setChecked(false);
                }
            }
        }
    }

    private void SaveUserData()
    {
        //载入配置文件
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
        //写入配置文件
        SharedPreferences.Editor spEd = sp.edit();
        if (cbRemember.isChecked())
        {
            spEd.putBoolean("isSaved", true);
            spEd.putString("username", etUser.getText().toString());
            spEd.putString("password", etPsw.getText().toString());
            if(cbAuto.isChecked())
            {
                spEd.putBoolean("autoAuth", true);
            }else
            {
                spEd.putBoolean("autoAuth", false);
            }
            if(cbStart.isChecked())
            {
                spEd.putBoolean("autoStart", true);
            }else
            {
                spEd.putBoolean("autoStart", false);
            }

        }else
        {
            spEd.putBoolean("isSaved", false);
            spEd.putString("username", "");
            spEd.putString("password", "");
            spEd.putBoolean("autoAuth", false);
            spEd.putBoolean("autoStart", false);
        }

        spEd.commit();
    }
}
