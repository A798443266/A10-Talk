package com.eiplan.zuji.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    MaterialEditText etUsername;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_loss_psw)
    TextView tvLossPsw;
    @BindView(R.id.ll_wechat)
    LinearLayout llWechat;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;
    @BindView(R.id.ll_linying)
    LinearLayout llLinying;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.ll_others)
    LinearLayout llOthers;

    private SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {

    }

    @OnClick({R.id.btn_login, R.id.tv_loss_psw, R.id.ll_wechat, R.id.ll_qq, R.id.tv_register,
            R.id.ll_linying, R.id.ll_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_loss_psw:
                break;
            case R.id.ll_wechat:
                break;
            case R.id.ll_qq:
                loginWithQQ();
                break;
            case R.id.ll_linying:
                break;
            case R.id.ll_email:
                break;
        }
    }

    //需要在使用QQ分享或者授权的Activity中，添加：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //qq第三方登录
    private void loginWithQQ() {
        UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            //map即为登录成功后返回的用户资料
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map1) {
                UserInfo user = new UserInfo();
                user.setName(map1.get("screen_name"));
                user.setPic(map1.get("iconurl"));

                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    //登录
    private void login() {

        // 1 获取输入的用户名和密码
        final String phone = etUsername.getText().toString();
        final String loginPwd = etPassword.getText().toString();
        // 2 校验输入的用户名和密码
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(loginPwd)) {
            Toast.makeText(LoginActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示正在登录
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("正在登录...");
//        pDialog.setCancelable(false);
        pDialog.show();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(phone, loginPwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserInfo user = new UserInfo();
                                user.setPhone(phone);
                                // 对模型层数据的处理
                                Model.getInstance().loginSuccess(user);
                                //去自己的服务器取出用户信息存到本地数据库
                                getUserInfo(phone);


                            }
                        });
                    }

                    @Override
                    public void onError(int i, final String s) {
                        pDialog.dismiss();
                        // 提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败" + s, Toast.LENGTH_SHORT).show();
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

    private void getUserInfo(String phone) {
        Log.e("TAG", "开始联网请求");
        OkHttpUtils.post().url(Constant.LOGIN).addParams("phone", phone).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", response);
                //解析json
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = JSON.parseObject(response).getJSONObject("extend").getJSONObject("user");

                int isexp = jsonObject.getInteger("isexp");

                UserInfo user = new UserInfo();
                user.setName(jsonObject1.getString("name"));
                user.setPic(jsonObject1.getString("pic"));
                user.setCompany(jsonObject1.getString("company"));
                user.setIndustry(jsonObject1.getString("industry"));
                user.setPhone(jsonObject1.getString("phone"));
                user.setBalance(jsonObject1.getInteger("balance"));
                user.setBankcard(0);
                user.setPoint(jsonObject1.getInteger("point"));
                user.setIsExpert(isexp);
                //把用户存入数据库
//                Model.getInstance().getUserAccountDao().addAccount(user);

                //把当前登录的手机号存入sp，以便之后个人信息显示
                SpUtils.putInt(LoginActivity.this, Constant.current_isexpert, jsonObject.getInteger("isexp"));
                SpUtils.putString(LoginActivity.this, Constant.current_phone, jsonObject1.getString("phone"));
                SpUtils.putString(LoginActivity.this, Constant.current_pic, jsonObject1.getString("pic"));
                SpUtils.putString(LoginActivity.this, Constant.current_name, jsonObject1.getString("name"));
                SpUtils.putInt(LoginActivity.this, Constant.current_point, jsonObject1.getInteger("point"));
                //用户
                if (isexp == 0) {
                    SpUtils.putString(LoginActivity.this, Constant.current_industry, jsonObject1.getString("industry"));
                    if(!TextUtils.isEmpty(jsonObject1.getString("industry"))){
                        SpUtils.putInt(LoginActivity.this, Constant.INDUSTRY, 0);//已经有专业了
                    }
                    SpUtils.putString(LoginActivity.this, Constant.current_company, jsonObject1.getString("company"));
                }
                //专家
                else {
                    SpUtils.putString(LoginActivity.this, Constant.current_major, jsonObject1.getString("major"));
                    SpUtils.putString(LoginActivity.this, Constant.current_job, jsonObject1.getString("job"));
                    SpUtils.putString(LoginActivity.this, Constant.current_workyear, jsonObject1.getString("workyear"));
                    SpUtils.putInt(LoginActivity.this, Constant.INDUSTRY, 0);//已经有专业了
                }

                //
                pDialog.dismissWithAnimation();
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                // 跳转到主页面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
