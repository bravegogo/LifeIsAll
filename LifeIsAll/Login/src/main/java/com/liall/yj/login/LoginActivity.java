package com.liall.yj.login;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import com.liall.yj.login.json.TestGson;

public class LoginActivity extends AppCompatActivity {


    private EditText id_login;
    private EditText password_login;
    private ImageView avatar_login;
    private CheckBox rememberpassword_login;
    private CheckBox auto_login;
    private Button button_login;
    private SharedPreferences sp;
    private String idvalue;
    private String passwordvalue;
    private static final int PASSWORD_MIWEN = 0x81;

    private LoginManager loginM = new LoginManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //找到相应的布局及控件
        setContentView(R.layout.login_layout);
        id_login=(EditText) findViewById(R.id.login_id);
        password_login=(EditText) findViewById(R.id.login_password);
        avatar_login=(ImageView) findViewById(R.id.login_avatar);
        rememberpassword_login=(CheckBox) findViewById(R.id.login_rememberpassword);
        auto_login=(CheckBox) findViewById(R.id.login_autologin);
        button_login=(Button) findViewById(R.id.login_button);

        List<String> arrlist = new ArrayList();
        arrlist.add("CREATE TABLE IF NOT EXISTS user_login_info (_id integer NOT NULL PRIMARY KEY autoincrement, user_id text NOT NULL , user_password text NOT NULL);");


        jsonTest();

        if (sp.getBoolean("ischeck",false)){
            rememberpassword_login.setChecked(true);
            id_login.setText(sp.getString("PHONEEDIT",""));
            password_login.setText(sp.getString("PASSWORD",""));
            //密文密码
            password_login.setInputType(PASSWORD_MIWEN);
            if (sp.getBoolean("auto_ischeck",false)){
                auto_login.setChecked(true);
                Intent intent = new Intent(LoginActivity.this,SnackbarActivity.class);
                startActivity(intent);
            }
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_login.getPaint().setFlags(0);
                idvalue=id_login.getText().toString();
                password_login.getPaint().setFlags(0);
                passwordvalue = password_login.getText().toString();


                try {

                    loginM.createTables(idvalue,arrlist);

                    loginM.saveLoginData(idvalue,passwordvalue);

                    loginM.queryLoginData();

                    loginM.queryLoginData();

                    loginM.queryLoginData();

                }catch (Exception ex){

                }finally {

                }



                if (idvalue.equals("1234")&&passwordvalue.equals("1234")){



                    if (rememberpassword_login.isChecked()){
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("PHONEEDIT",idvalue);
                        editor.putString("PASSWORD",passwordvalue);
                        editor.commit();

//                        loginM.saveLoginData(idvalue,passwordvalue);
//
//                        loginM.queryLoginData();
//
//                        loginM.queryLoginData();
//
//                        loginM.queryLoginData();


                    }

                    Intent intent = new Intent(LoginActivity.this,SnackbarActivity.class);
                    startActivity(intent);
                    finish();
                }else{


                    Intent intent = new Intent();

                    intent.setClass(LoginActivity.this, SnackbarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.up_in, R.anim.up_out);
                }
            }
        });

        rememberpassword_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rememberpassword_login.isChecked()){
                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ischeck",true).commit();


                }
                else {
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ischeck",false).commit();
                }
            }
        });

        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()){
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("auto_ischeck",true).commit();
                }else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("auto_ischeck",false).commit();
                }
            }
        });
    }

    // 点击空白区域 自动隐藏软键盘

    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */

            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }



    public void jsonTest(){

        TestGson test = new  TestGson();
        test.jsonTest();

    }
}
