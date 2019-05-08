package com.eiplan.zuji.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.qiandao.DateUtil;
import com.eiplan.zuji.qiandao.OnSignedSuccess;
import com.eiplan.zuji.qiandao.SignDate;
import com.eiplan.zuji.utils.UIUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到
 */

/**
 * 签到页面
 */

public class QianDaoActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.signDate)
    SignDate signDate;
    @BindView(R.id.btn_qiandao)
    Button btnQiandao;

    private int fredays;
    private int day;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qian_dao);
        ButterKnife.bind(this);

        ivBack.setImageResource(R.drawable.icon_back);
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        tvTitle.setText("签到");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        tvRight.setVisibility(View.GONE);

        fredays = DateUtil.getFirstDayOfMonth();//取当月第一天是星期几，星期日是第一天，依次类推
        Calendar a = Calendar.getInstance();
        day = a.get(Calendar.DAY_OF_MONTH); //今天几号

        signDate.setStatus(new int[]{6, 8, 9, 11});
        signDate.refresh();

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.btn_qiandao)
    public void onViewClicked1() {
        signDate.setStatus(day + fredays - 1);//改变日历数组下标今天的状态为已签
        signDate.refresh();
//        Toast.makeText(this, "签到成功！", Toast.LENGTH_SHORT).show();
        btnQiandao.setText("已签到");
        btnQiandao.setClickable(false);
        showDialog();
    }

    private void showDialog() {
        View view = View.inflate(this, R.layout.qiandao_diaog, null);
        TextView tv_week = view.findViewById(R.id.tv_week);
        TextView tv_time = view.findViewById(R.id.tv_time);
        ImageView iv_close = view.findViewById(R.id.iv_close);

        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();

        Calendar a = Calendar.getInstance();
        int month = a.get(Calendar.MONTH) + 1;

        tv_time.setText(month + "-" + day);
        String s = "hao";
        int i = a.get(Calendar.DAY_OF_WEEK);
        if (i == 1) {
            s = "日";
        } else if (i == 2) {
            s = "一";
        } else if (i == 3) {
            s = "二";
        } else if (i == 4) {
            s = "三";
        } else if (i == 5) {
            s = "四";
        } else if (i == 6) {
            s = "五";
        } else if (i == 7) {
            s = "六";
        }
        tv_week.setText("星期" + s);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
