

package com.example.ldtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EventHandler eventHandler;
    private Button btn_verity;      //获取验证码按钮
    private Button btn_register;    //注册按钮
    private EditText account_register;  //手机号输入框
    private EditText verify_register;     //验证码输入框
    private EditText password_registe;  //密码输入框
    private String phone;
    private String password;
    //private Handler handler;
    int i = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();     //初始化

    }

    /**
     * 初始化控件和SDK
     */
    private void init() {

        account_register = (EditText) findViewById(R.id.account_register);
        verify_register = (EditText) findViewById(R.id.verify_register);
        password_registe = (EditText) findViewById(R.id.password_register);
        btn_verity = (Button) findViewById(R.id.btn_verify);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_verity.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        // 如果希望在读取通信录的时候提示用户，可以添加下面的代码，并且必须在其他代码调用之前，否则不起作用；如果没这个需求，可以不加这行代码
        //  SMSSDK.setAskPermisionOnReadContact(boolean ShowInDialog);
        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        phone = account_register.getText().toString();
        password = password_registe.getText().toString();
        switch (v.getId()) {
            case R.id.btn_verify:  //获取验证码
                //1.通过规则判断手机号
                if (!judgePhone(phone)) {
                    return;
                }
                //2.通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phone);
                //3.把按钮变成步科电机，并且显示倒计时（正在获取）
                btn_verity.setClickable(false);
                btn_verity.setText("重新发送（" + i + "）");
                new Thread(new Runnable() { //倒计时
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.btn_register:
                //注册按钮，将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phone, verify_register.getText().toString());
                Toast.makeText(getApplicationContext(), "核对成功"
                        , Toast.LENGTH_SHORT).show();
                //createProgressBar();
                break;
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //调节获取验证码按钮
            if (msg.what == -9) {
                btn_verity.setText("重新发送（" + i + "）");
            } else if (msg.what == -8) {
                btn_verity.setText("获取验证码");
                btn_verity.setClickable(true);
                Toast.makeText(getApplicationContext(), "为什么？？？"
                        , Toast.LENGTH_SHORT).show();
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //短信注册成功后，返回注册成功界面，然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功"
                                , Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        //startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 判断手机号码是否合理
     */
    private boolean judgePhone(String phone) {
        if (isMatchLength(phone, 11)) {
            if(isMobileNO(phone))
                return true;
            else {
                Toast.makeText(this, "手机号码错误！", Toast.LENGTH_SHORT).show();
                return false;
                }
        }
        Toast.makeText(this, "手机号位数为"+phone.length()+" "+phone, Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobile) {
        /*
         * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
         * 联通：130、131、132、155、156、145、175、176、185、186、166
         * 电信：133、149、153、173、177、180、181、189、199（1349卫通）
         *
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][345789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobile))
            return false;
        else
            return mobile.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }




}

