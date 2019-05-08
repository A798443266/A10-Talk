package com.eiplan.zuji.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

/**
 * 注册
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_agin_password)
    EditText etAginPassword;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_check)
    Button btnCheck;
    @BindView(R.id.cb_gree)
    CheckBox cbGree;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.et_checck_num)
    EditText etChecckNum;
    @BindView(R.id.tv_xieyi)
    TextView tvXieyi;
    @BindView(R.id.tv_login)
    TextView tvLogin;


    private int i = 30;
    private String nick;
    private String password;
    private String phone;
    private SweetAlertDialog pDialog;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                btnCheck.setText(i + " s");
            } else if (msg.what == -2) {
                btnCheck.setText("重新发送");
                btnCheck.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理成功得到验证码的结果
                        Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO 处理错误的结果
                        ((Throwable) data).printStackTrace();
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        // Toast.makeText(RegisterActivity.this, "验证码正确", Toast.LENGTH_SHORT).show();

                        //去自己的服务器中注册
                        OkHttpUtils
                                .post()
                                .addParams("name", nick)
                                .addParams("password", password)
                                .addParams("phone", phone)
                                .url(Constant.REGISTER)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        pDialog.dismiss();
                                        Log.e("TAG", "请求失败");
                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Log.e("TAG", response);
                                        parseJson(response, pDialog);
                                    }
                                });

                    } else {
                        // TODO 处理错误的结果
                        pDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        //registerEventHandler是用来往SMSSDK中注册一个事件接收器
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = 3;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    @OnClick({R.id.btn_check, R.id.cb_gree, R.id.btn_register, R.id.tv_login, R.id.tv_xieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_check:
                checkMSM();
                break;
            case R.id.cb_gree:
                break;
            case R.id.btn_register:
                registerUser();
                break;
            case R.id.tv_xieyi:
                break;
            case R.id.tv_login:
                finish();
                break;
        }
    }

    //获取验证码
    private void checkMSM() {
        String phoneNum = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.getVerificationCode("86", phoneNum);
        btnCheck.setClickable(false);
        //开始倒计时
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-1);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //倒计时结束执行
                handler.sendEmptyMessage(-2);
            }
        }).start();
    }

    private void registerUser() {

        nick = etNick.getText().toString();
        password = etPassword.getText().toString();
        String psw1 = etAginPassword.getText().toString();
        phone = etPhone.getText().toString();
        String checknum = etChecckNum.getText().toString();

        if (TextUtils.isEmpty(nick) || TextUtils.isEmpty(password) || TextUtils.isEmpty(psw1)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(checknum)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbGree.isChecked()) {
            Toast.makeText(this, "请先阅读并同意服务协议", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(psw1)) {
            Toast.makeText(this, "两次输入的密码不相等", Toast.LENGTH_SHORT).show();
            return;
        }

        //显示正在登录
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("正在注册...");
//        pDialog.setCancelable(false);
        pDialog.show();

        //先验证手机验证码是否正确
        confirm();
    }

    private void parseJson(String response, final SweetAlertDialog pDialog) {
        //用fastjson解析结果
        JSONObject jsonObject = JSONObject.parseObject(response);
        int code = jsonObject.getInteger("code");
        if (code == 100) {//自己的服务器注册成功
            //去环信注册
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 去环信服务器注册账号
                        EMClient.getInstance().createAccount(phone, password);
                        // 更新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("注册成功!")
                                        .show();
//                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            pDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "此手机号已经被注册", Toast.LENGTH_SHORT).show();
        }
    }


    private void confirm() {
        String phoneNum = etPhone.getText().toString().trim();
        String code = etChecckNum.getText().toString().trim();
        SMSSDK.submitVerificationCode("86", phoneNum, code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在完成短信验证之后，需要销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();
    }
}
