package com.eiplan.zuji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.sdsmdg.tastytoast.TastyToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 购买文档页面
 */

public class buyDocActivity extends Activity {

    @BindView(R.id.ll_layout)
    LinearLayout llLayout;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_price1)
    TextView tvPrice1;
    @BindView(R.id.ll2)
    LinearLayout ll2;

    private String docname;
    private float price;
    private String fileId;//文档id
    private int action = 0;  //用来标记从哪里过来的
    private String id; //专家订单id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_doc);
        ButterKnife.bind(this);


        //安卓4.0或以上的手机测试时滑出的的窗口与左右会有间隙，解决方法如下：
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        llLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        docname = getIntent().getStringExtra("docname");
        id = getIntent().getStringExtra("id");
        fileId = getIntent().getStringExtra("fileId");
        price = getIntent().getFloatExtra("price", 50);
        action = getIntent().getIntExtra("action", 0);
        if (TextUtils.isEmpty(docname)) {
            tvName.setText("咨询订单支付");
        } else {
            tvName.setText(docname);
        }
        tvPrice.setText(price + "");
        tvPrice1.setText(price + "");
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick(R.id.btn_buy)
    public void onViewClicked() {
        llLayout.setVisibility(View.GONE);
        ll2.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked1() {
        if (action == 1) {
            OkHttpUtils.post().url(Constant.payAndwancheng)
                    .addParams("id", id)
                    .addParams("state", "支付")
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    finish();
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("TAG", response);
                    JSONObject jsonObject = JSON.parseObject(response);
                    if (jsonObject.getInteger("code") == 100) {
                        EventBus.getDefault().post(new LiveRoom());//发消息让支付订单刷新

                    } else {

                    }
                    finish();
                }
            });

        } else {
            OkHttpUtils.post().url(Constant.buyfile)
                    .addParams("phone",SpUtils.getString(buyDocActivity.this,Constant.current_phone))
                    .addParams("fileId",fileId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(getApplicationContext(),"购买失败！",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            finish();
                        }
                    });

        }

    }
}
