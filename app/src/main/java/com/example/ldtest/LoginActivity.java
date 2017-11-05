package com.example.ldtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.SMSSDK;


public class LoginActivity extends BaseActivity {   //登录界面
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button register;
    private CheckBox rememberPass;
    private SharedPreferences pref;             //此对象用于获取密码
    private SharedPreferences.Editor editor;    //用于存储密码
    //String Mob_AppKey="21f8318243cda";
   // String Mob_AppSecret="46bc8f86202eda9a11de40f6e1e15b8b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);  //创建存储文件

        accountEdit =(EditText) findViewById(R.id.account);
        passwordEdit =(EditText) findViewById(R.id.password);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);    //是否记住密码按钮
        login =(Button)findViewById(R.id.login);
        boolean isRemember=pref.getBoolean("remember_password",false);  //一开始并没有记住密码，所以默认false
        if(isRemember){
            //将账号和密码都设置到文本框中
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                //如果账号是admin且密码是123456，就认为登录成功
                if(account.equals("admin")&&password.equals("123456")){
                    editor=pref.edit();
                    if(rememberPass.isChecked()){//检查复选框是否被选中
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else{
                        editor.clear();
                    }
                    editor.apply(); //追加到文件中
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                Toast.makeText(LoginActivity.this,"wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register=(Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){      //手机号注册
               Intent intent_register=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent_register);
                finish();

            }
        });

    }


}
