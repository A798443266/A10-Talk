package com.eiplan.zuji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

public class BindPhoneActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_agin_password)
    EditText etAginPassword;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_checck_num)
    EditText etChecckNum;
    @BindView(R.id.btn_check)
    Button btnCheck;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private SweetAlertDialog pDialog;
    private String psw;
    private String phone;
    private UserInfo user;
    private int i = 30;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                btnCheck.setText(i + " s");
            } else if (msg.what == -2) {
                btnCheck.setText("重新发送");
                btnCheck.setClickable(true);
                i = 30;
            }else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理成功得到验证码的结果
                        Toast.makeText(BindPhoneActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO 处理错误的结果
                        ((Throwable) data).printStackTrace();
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //去自己的服务器中注册
                        OkHttpUtils
                                .post()
                                .addParams("name", user.getName())
                                .addParams("password", psw)
                                .addParams("phone", phone)
                                .addParams("pic",user.getPic())
                                .url(Constant.REGISTER2)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        pDialog.dismiss();
                                        Toast.makeText(BindPhoneActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BindPhoneActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);

        tvRight.setVisibility(View.GONE);
        tvTitle.setText("您还需要绑定手机号码");

        user = (UserInfo) getIntent().getSerializableExtra("user");

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

    @OnClick({R.id.iv_back, R.id.btn_check, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_check:
                checkMSM();
                break;
            case R.id.btn_register:
                registerAndLogin();
                break;
        }
    }

    private void registerAndLogin() {
        psw = etPassword.getText().toString();
        String psw1 = etAginPassword.getText().toString();
        phone = etPhone.getText().toString();
        String checknum = etChecckNum.getText().toString();

        if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(psw1)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(checknum)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!psw.equals(psw1)) {
            Toast.makeText(this, "两次输入的密码不相等", Toast.LENGTH_SHORT).show();
            return;
        }



        //显示正在登录
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("请稍后...");
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
                        EMClient.getInstance().createAccount(phone, psw);
                        // 更新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //登录环信
                                loginHX();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                Toast.makeText(BindPhoneActivity.this, "网络出错了" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            pDialog.dismiss();
            Toast.makeText(BindPhoneActivity.this, "此手机号已经被注册", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginHX() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(phone, psw, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                user.setIsExpert(0);//刚开始注册的不是专家
                                Model.getInstance().getUserAccountDao().addAccount(user);//存入数据库
                                SpUtils.putString(BindPhoneActivity.this,Constant.current_phone,user.getPhone());
                                SpUtils.putInt(BindPhoneActivity.this,Constant.current_isexpert,user.getIsExpert());
                                SpUtils.putString(BindPhoneActivity.this, Constant.current_pic, user.getPic());
                                SpUtils.putString(BindPhoneActivity.this, Constant.current_name, user.getName());
                                SpUtils.putInt(BindPhoneActivity.this, Constant.INDUSTRY, 1);//已经有专业了

                                pDialog.dismissWithAnimation();
                                Toast.makeText(BindPhoneActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                // 跳转到主页面
                                Intent intent = new Intent(BindPhoneActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, final String s) {
                        pDialog.dismissWithAnimation();
                        // 提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BindPhoneActivity.this, "网络出错了" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
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
