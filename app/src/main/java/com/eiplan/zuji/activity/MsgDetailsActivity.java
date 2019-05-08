package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.RecommendMSG;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_face)
    ImageView ivFace;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    private RecommendMSG msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);
        ButterKnife.bind(this);

        msg = (RecommendMSG) getIntent().getSerializableExtra("msg");
        initView();
    }

    private void initView() {
        tvTitle.setText(msg.getTitle());
        tvDesc.setText(msg.getContent());
        Glide.with(this).load(msg.getPic()).error(R.drawable.p).into(ivFace);
    }

    @OnClick({R.id.iv_back, R.id.iv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                share();
                break;
        }
    }

    private void share() {
        UMWeb web = new UMWeb("https://www.baidu.com");
        web.setTitle(msg.getTitle());
        UMImage image = new UMImage(this,"http://img5.mtime.cn/mg/2019/01/23/195948.14644746_120X90X4.jpg");
        web.setDescription(msg.getIntroduce());
        web.setThumb(image);
        new ShareAction(this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.LINKEDIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
//                        Toast.makeText(MsgDetailsActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        Toast.makeText(MsgDetailsActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                }).open();
    }
}
