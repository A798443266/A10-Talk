package com.eiplan.zuji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.MyExpertInfo;
import com.eiplan.zuji.utils.Constant;
import com.sdsmdg.tastytoast.TastyToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class PingFenActivity extends Activity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.ll)
    LinearLayout ll;

    private String name;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_fen);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");

        tvName.setText(name);
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        String eva = et.getText().toString().trim();
        if (TextUtils.isEmpty(eva)) {
            Toast.makeText(this, "评价内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpUtils.post().url(Constant.orderEva)
                .addParams("id", id)
                .addParams("eva", eva)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //发送消息，让待评价界面刷新
                EventBus.getDefault().post(new ExpertInfo());
                TastyToast.makeText(getApplicationContext(), "评价失败!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                finish();
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                if (jsonObject.getInteger("code") == 100) {
                    TastyToast.makeText(getApplicationContext(), "评价成功!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    //发送消息，让已完成界面刷新
                    EventBus.getDefault().post(new MyExpertInfo());
                    EventBus.getDefault().post(new ExpertInfo());
                    finish();
                } else {
                    TastyToast.makeText(getApplicationContext(), "评价失败!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    //发送消息，让待评价界面刷新
                    EventBus.getDefault().post(new ExpertInfo());
                    finish();
                }

            }
        });


    }
}
