package com.eiplan.zuji.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.VedioCommentAdapter;
import com.eiplan.zuji.bean.CommentInfo;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class VedioDetailsActivity extends AppCompatActivity {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_fabiao)
    TextView tvFabiao;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private List<CommentInfo> comments;
    private VedioCommentAdapter adapter;
    private VideoInfo video;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (comments != null && comments.size() > 0) {
                adapter.refresh(comments);
                lv.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_details);
        ButterKnife.bind(this);

        tvFabiao.setClickable(false);
        video = (VideoInfo) getIntent().getSerializableExtra("video");//获取传递过来的视频
        initView();
        initData();
    }

    private void initData() {
        if (video != null) {
            Glide.with(this).load(video.getPic()).error(R.drawable.logo).into(cvUserPic);
            tvName.setText(video.getUploader());
            tvDesc.setText("    " + video.getIntroduce());
            videoplayer.setUp(video.getPlay(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, video.getTitle());
//            videoplayer.thumbImageView.setImageURI(Uri.parse(video.getCover()));
            Glide.with(this).load(video.getCover()).into(videoplayer.thumbImageView);
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

    private void initView() {

        comments = new ArrayList<>();
        adapter = new VedioCommentAdapter(this);
        OkHttpUtils.post().addParams("vid", video.getId() + "").url(Constant.getVideoCommnet).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                comments = JsonUtils.parseVideoComments(response);
                handler.sendEmptyMessage(1);
            }
        });


        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    tvFabiao.setClickable(false);
                    tvFabiao.setTextColor(UIUtils.getColor(R.color.color_system_gray));
                } else {
                    tvFabiao.setClickable(true);
                    tvFabiao.setTextColor(UIUtils.getColor(R.color.system_blue));
                }
            }
        });
    }


    @OnClick(R.id.tv_fabiao)
    public void onViewClicked() {
        CommentInfo comment = new CommentInfo();
        comment.setContent(etInput.getText().toString());
        comment.setUserName(SpUtils.getString(this, Constant.current_name));
        comment.setUserPic(SpUtils.getString(this, Constant.current_pic));
        Date date = new Date();
        comment.setTime(setDate(date));
        comment.setGood(0);
        comment.setReply(0);
        comment.setVedioId(video.getId());
        comments.add(0, comment);

        adapter.refresh(comments);
        lv.setAdapter(adapter);
        lv.setSelection(0);//定位到第一个
        etInput.setText("");
        Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    public String setDate(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked1() {
        finish();
    }
}
