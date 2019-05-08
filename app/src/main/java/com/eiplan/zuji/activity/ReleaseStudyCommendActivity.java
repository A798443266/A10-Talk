package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.StudyCommendInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发表课程评论
 */
public class ReleaseStudyCommendActivity extends AppCompatActivity {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn)
    Button btn;

    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_study_commend);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        StudyCommendInfo studyCommendInfo = new StudyCommendInfo();
        studyCommendInfo.setContent(content);
        studyCommendInfo.setGood(0);
        studyCommendInfo.setTime(setDate(new Date()));
        studyCommendInfo.setUserName(SpUtils.getString(this, Constant.current_name));
        studyCommendInfo.setUserPic(SpUtils.getString(this, Constant.current_pic));
        EventBus.getDefault().post(studyCommendInfo);

        Toast.makeText(this, "评论成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public String setDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked1() {
        finish();
    }
}
