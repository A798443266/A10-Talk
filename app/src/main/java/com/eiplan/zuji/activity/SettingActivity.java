package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.UIUtils;
import com.eiplan.zuji.view.ConfirmDialogQuit;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        tvRight.setVisibility(View.GONE);
        tvTitle.setText("设置");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        ivBack.setImageResource(R.drawable.icon_back);
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
    }

    @OnClick({R.id.iv_back, R.id.ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll:
                ConfirmDialogQuit confirmDialog = new ConfirmDialogQuit(this);
                confirmDialog.setOnDialogClickListener(new ConfirmDialogQuit.OnDialogClickListener() {
                    @Override
                    public void onOKClick() {
                        logout();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
                confirmDialog.setCancelable(false);
                confirmDialog.show();
                break;
        }
    }

    private void logout() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //让环信服务器退出登录
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //清除sp数据

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 更新ui显示
                                Toast.makeText(SettingActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                                // 回到登录页面
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this, "退出失败" + s, Toast.LENGTH_SHORT).show();
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

}
