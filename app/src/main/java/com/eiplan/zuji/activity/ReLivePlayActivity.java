package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ReLiveRoom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 直播回放页面
 */

public class ReLivePlayActivity extends AppCompatActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;

    private ReLiveRoom reroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_live_play);
        ButterKnife.bind(this);

        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        reroom = (ReLiveRoom) getIntent().getSerializableExtra("reroom");
        if(reroom != null){
//            Glide.with(this).load(reroom.getPic()).error(R.drawable.logo).into(cvUserPic);
            videoplayer.setUp(reroom.getPlayurl(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, reroom.getTitle());
//            videoplayer.thumbImageView.setImageURI(Uri.parse(video.getCover()));
            Glide.with(this).load(reroom.getCover()).into(videoplayer.thumbImageView);
        }
    }


    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoplayer.releaseAllVideos();
    }
}
