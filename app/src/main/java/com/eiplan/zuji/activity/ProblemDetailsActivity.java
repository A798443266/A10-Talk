package com.eiplan.zuji.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.ProblemCommentAdapter;
import com.eiplan.zuji.bean.ProblemCommentInfo;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.bean.UserInfo;
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
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 论坛问题详情界面
 */

public class ProblemDetailsActivity extends AppCompatActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_problem_name)
    TextView tvProblemName;
    @BindView(R.id.tv_problem_desc)
    TextView tvProblemDesc;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_comment)
    ImageView ivComment;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.iv_zan)
    ImageView ivZan;
    @BindView(R.id.tv_zan)
    TextView tvZan;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.iv_pen)
    ImageView ivPen;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_fabiao)
    TextView tvFabiao;
    @BindView(R.id.rl_write)
    RelativeLayout rlWrite;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    private ProblemCommentAdapter adapter;
    private List<ProblemCommentInfo> datas = new ArrayList<>();
    private ProblemInfo problem = new ProblemInfo();
    private int caina = 0;//判断是否是我的问题界面过来的，若果是点击问题详情可以采纳答案

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_details);
        ButterKnife.bind(this);

        problem = (ProblemInfo) getIntent().getSerializableExtra("problem");//获取传递过来的问题对象
        caina = getIntent().getIntExtra("caina", 0);

        initView();
        initData();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (datas != null && datas.size() > 0) {
                adapter.refresh(datas);
                lv.setAdapter(adapter);
                if (caina != 0)
                    adapter.setOnAcceptClickListener(new ProblemCommentAdapter.OnAcceptClickListener() {
                        @Override
                        public void OnAcceptClick(View v, int position) {
                            if (datas.get(position).getState().equals("未采纳"))
                                caina(position);
                        }
                    });
            }
        }
    };

    //采纳
    private void caina(final int position) {
        OkHttpUtils.post().url(Constant.adoptAnswer)
                .addParams("phone", datas.get(position).getPhone())
                .addParams("forumId", datas.get(position).getForumId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ProblemDetailsActivity.this, "采纳失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            Toast.makeText(ProblemDetailsActivity.this, "采纳成功", Toast.LENGTH_SHORT).show();
                            datas.get(position).setState("采纳");
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ProblemDetailsActivity.this, "采纳失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initData() {
        //从服务器中获取评论信息
        OkHttpUtils.post().url(Constant.getPrombleComments)
                .addParams("id", problem.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析数据
                        datas = JsonUtils.parseProblemComment(response);
                        handler.sendEmptyMessage(1);
                    }
                });
    }

    private void initView() {

        adapter = new ProblemCommentAdapter(this);

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

        Glide.with(this).load(problem.getToupic()).error(R.drawable.logo).into(cvUserPic);
        tvUserName.setText(problem.getPubisher());
        tvTime.setText(problem.getTime());
        tvJifen.setText(problem.getPoint() + "");
        tvProblemName.setText(problem.getQuestion());
        tvProblemDesc.setText(problem.getContent());
        tvZan.setText(problem.getGood() + "");
        tvComment.setText(problem.getComment() + "");
        if (!problem.getPic().equals("0")) {//如果有图片
            llPhoto.setVisibility(View.VISIBLE);
            Glide.with(this).load(problem.getPic()).error(R.drawable.ease_default_image).into(iv1);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProblemDetailsActivity.this, BigPhotoActivity.class);
                    intent.putExtra("path", problem.getPic());
                    startActivity(intent);
                }
            });
        } else {
            llPhoto.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_fabiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_fabiao:
                //发布回答
                comment();

               /* ProblemCommentInfo comment = new ProblemCommentInfo();
                comment.setContent(etInput.getText().toString());
                comment.setName(SpUtils.getString(this, Constant.current_name));
                comment.setPic(SpUtils.getString(this, Constant.current_pic));
                Date date = new Date();
                comment.setTime(setDate(date));
                comment.setGood(0);
                comment.setReply(0);
                comment.setState("未采纳");

                datas.add(0, comment);
                adapter.refresh(datas);
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
                }*/
                break;
        }
    }

    //评论
    private void comment() {
        String content = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.post().url(Constant.upAnswer)
                .addParams("phone", SpUtils.getString(ProblemDetailsActivity.this, Constant.current_phone))
                .addParams("forumId", problem.getId() + "")
                .addParams("content", content)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ProblemDetailsActivity.this,"评论失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            Toast.makeText(ProblemDetailsActivity.this, "回答成功!", Toast.LENGTH_SHORT).show();
                            etInput.setText("");
                            // 关闭软键盘
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            // 得到InputMethodManager的实例
                            if (imm.isActive()) {
                                // 如果开启
                                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                            }
                            initData();
                        }
                    }
                });
    }


    public String setDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

}
