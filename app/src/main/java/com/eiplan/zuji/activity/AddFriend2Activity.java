package com.eiplan.zuji.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.SearchContactAdapter;
import com.eiplan.zuji.adapter.SearchGroupAdapter;
import com.eiplan.zuji.bean.SearchContactInfo;
import com.eiplan.zuji.bean.SearchGroupInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class AddFriend2Activity extends AppCompatActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_num1)
    TextView tvNum1;
    @BindView(R.id.ll_look1)
    LinearLayout llLook1;
    @BindView(R.id.ll_look2)
    LinearLayout llLook2;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.lv)
    ListView lv;

    private SweetAlertDialog pDialog;
    private AlertDialog dialog;
    private String num;//要搜索的手机号
    private List<SearchGroupInfo> groups;
    private List<SearchContactInfo> contacts;
    private SearchGroupAdapter adapter;
    private SearchContactAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend2);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ll.setVisibility(View.GONE);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    tvNum.setText(num);
                    tvNum1.setText(num);
                }
            }
        });
    }


    @OnClick({R.id.ll_look1, R.id.ll_look2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_look1:
                searchFriend();
                break;
            case R.id.ll_look2:
                searchGroup();
                break;
        }
    }

    long a = -1;

    //搜索群
    private void searchGroup() {
        try {
            a = Long.parseLong(num);
            Log.e("TAG", a + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            Log.e("TAG", a + "");
            if (a == -1)
                searchGroupByMajor(num);
            else
                searchGroupById(num);
            a = -1;
        }
    }


    //群id搜索
    private void searchGroupById(String id) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("请稍后...");
        pDialog.show();
        OkHttpUtils.post().url(Constant.getGroupById)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pDialog.dismiss();
                        Toast.makeText(AddFriend2Activity.this, "抱歉，找不到该群组信息", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pDialog.dismiss();
                        groups = JsonUtils.parseGroups(response);
                        if (groups != null && groups.size() > 0) {
                            adapter = new SearchGroupAdapter(groups, AddFriend2Activity.this);
                            lv.setAdapter(adapter);
                            adapter.setOnAcceptClickListener(new SearchGroupAdapter.OnAcceptClickListener() {
                                @Override
                                public void acceptClick(View view, int position) {
                                    //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
                                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().groupManager().joinGroup(groups.get(0).getId());//需异步处理
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(AddFriend2Activity.this, "发送请求成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } catch (HyphenateException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                                //需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
//                                    EMClient.getInstance().groupManager().applyJoinToGroup(groupid, "求加入");//需异步处理
                            });
                        } else {
                            Toast.makeText(AddFriend2Activity.this, "没有找到该群", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //群专业搜索
    private void searchGroupByMajor(String major) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("请稍后...");
        pDialog.show();
        OkHttpUtils.post().url(Constant.getGroupByMajor)
                .addParams("key", major)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pDialog.dismiss();
                        Toast.makeText(AddFriend2Activity.this, "抱歉，找不到群组信息", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pDialog.dismiss();
                        groups = JsonUtils.parseGroups(response);
                        if (groups != null && groups.size() > 0) {
                            adapter = new SearchGroupAdapter(groups, AddFriend2Activity.this);
                            lv.setAdapter(adapter);

                            adapter.setOnAcceptClickListener(new SearchGroupAdapter.OnAcceptClickListener() {
                                @Override
                                public void acceptClick(View view, final int position) {
                                    //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
                                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().groupManager().joinGroup(groups.get(position).getId());//需异步处理

                                            } catch (HyphenateException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    Toast.makeText(AddFriend2Activity.this, "发送请求成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(AddFriend2Activity.this, "没有找到该群", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void searchFriend() {
        try {
            a = Long.parseLong(num);
            Log.e("TAG", a + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            Log.e("TAG", a + "");
            if (a == -1)
                searchContactByMajor(num);
            else
                searchContactById(num);
            a = -1;
        }
    }

    //按照关键词搜索联系人
    private void searchContactByMajor(String num) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("请稍后...");
        pDialog.show();

        OkHttpUtils.post().url(Constant.contactSearchByMajor)
                .addParams("key", num)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pDialog.dismiss();
                        Toast.makeText(AddFriend2Activity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pDialog.dismiss();
                        JSONObject jsonObject1 = JSON.parseObject(response).getJSONObject("extend");
                        String s = jsonObject1.getString("yh");
                        contacts = JSON.parseArray(s, SearchContactInfo.class);
                        show1();
                    }

                });
    }

    //按照id搜索联系人
    private void searchContactById(String num) {
        if (num.equals(SpUtils.getString(this, Constant.current_phone))) {
            Toast.makeText(this, "搜索内容不能是自己", Toast.LENGTH_SHORT).show();
            return;
        }
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("请稍后...");
        pDialog.show();
        // 去服务器判断当前用户是否存在,存在返回用户信息
        OkHttpUtils.post().url(Constant.contactSearch)
                .addParams("phone", num)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pDialog.dismiss();
                        Toast.makeText(AddFriend2Activity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pDialog.dismiss();
                        JSONObject jsonObject1 = JSON.parseObject(response).getJSONObject("extend");
                        String s = jsonObject1.getString("yh");
                        contacts = JSON.parseArray(s, SearchContactInfo.class);
                        show1();
                    }

                });

    }

    //显示搜索到的联系人信息
    private void show1() {
        if (contacts != null && contacts.size() > 0) {
            adapter1 = new SearchContactAdapter(contacts, this);
            lv.setAdapter(adapter1);
            adapter1.setOnAcceptClickListener(new SearchContactAdapter.OnAcceptClickListener() {
                @Override
                public void acceptClick(View view, int position) {
                    addContact(position);
                }
            });
        }
    }

    private void addContact(int position) {
        View v = View.inflate(AddFriend2Activity.this, R.layout.add_friend_dialog, null);
        Button btn_send = v.findViewById(R.id.btn_send);
        Button btn_cancel = v.findViewById(R.id.btn_cancel);
        EditText et_reason = v.findViewById(R.id.et_reason);
        dialog = new AlertDialog.Builder(AddFriend2Activity.this).create();
        dialog.setView(v);
        dialog.setCancelable(true);
        dialog.show();

        final String reason = et_reason.getText().toString().trim();
        //发送添加好友
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 去环信服务器添加好友
                            EMClient.getInstance().contactManager().addContact(num, reason);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriend2Activity.this, "发送请求成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriend2Activity.this, "发送添加好友请求失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked1() {
        finish();
    }

}
