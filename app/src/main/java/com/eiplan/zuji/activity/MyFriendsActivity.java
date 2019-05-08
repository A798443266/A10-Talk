package com.eiplan.zuji.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.gui.ContactsAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 我的好友
 */

public class MyFriendsActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.wavesidebar)
    WaveSideBar wavesidebar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_contact_red)
    TextView tvContactRed;

    private List<ContactInfo> datas = new ArrayList<>();
    private List<ContactInfo> allContacts = new ArrayList<>();
    private LocalBroadcastManager mLBM;
    private ContactsAdapter adapter;
    //监听新好友请求的广播
    private BroadcastReceiver ContactInviteChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 更新红点显示
            tvContactRed.setVisibility(View.VISIBLE);
            //保存红点显示的状态到sp中
            SpUtils.putBoolean(MyFriendsActivity.this, Constant.IS_NEW_INVITE, true);
        }
    };
    //监听联系人改变的广播（增、删）
    private BroadcastReceiver ContactChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //当监听到联系人发生变化时刷新页面
            refreshContact();
        }
    };

    //监听群邀请的广播
    private BroadcastReceiver GroupChangeRecevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 更新红点显示
            tvContactRed.setVisibility(View.VISIBLE);
            //保存红点显示的状态到sp中
            SpUtils.putBoolean(MyFriendsActivity.this, Constant.IS_NEW_INVITE, true);
        }
    };

    // 刷新页面
    private void refreshContact() {
        // 获取数据
        List<ContactInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();
        // 校验
        if (contacts != null && contacts.size() >= 0) {
            datas.clear();
            // 设置数据
            datas.addAll(contacts);
            sort();
            // 刷新页面
            adapter.notifyDataSetChanged();
        }
    }

    private void sort() {
        //排序
        Collections.sort(datas, new Comparator<ContactInfo>() {
            @Override
            public int compare(ContactInfo lhs, ContactInfo rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        ButterKnife.bind(this);

        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("我的好友");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        ivBack.setBackgroundResource(R.drawable.icon_back);
        adapter = new ContactsAdapter();

        // 初始化红点显示
        boolean isNewInvite = SpUtils.getBoolean(this, Constant.IS_NEW_INVITE);
        tvContactRed.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);

        listview.setAdapter(adapter);
        wavesidebar.setIndexItems("#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        wavesidebar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getPinyin().substring(0, 1).equals(index)) {
                        listview.setSelection(i);
                        return;
                    } else
                        listview.setSelection(0);
                }
            }
        });

        initView();
    }

    private void initView() {
        //注册广播
        mLBM = LocalBroadcastManager.getInstance(this);
        //好友邀请信息变化的广播
        mLBM.registerReceiver(ContactInviteChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        //联系人信息变化的广播
        mLBM.registerReceiver(ContactChangeReceiver, new IntentFilter(Constant.CONTACT_CHANGED));
        //群邀请信息变化的广播
        mLBM.registerReceiver(GroupChangeRecevier, new IntentFilter(Constant.GROUP_INVITE_CHANGED));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyFriendsActivity.this, ChatActivity.class);
                // 传递环信id，即电话号码
                intent.putExtra(EaseConstant.EXTRA_USER_ID, datas.get(position).getPhone());
                intent.putExtra("name", datas.get(position).getName()); //传递名称过去
                startActivity(intent);
                finish();
            }
        });
        //显示所有联系人信息
        getContact();
    }

    private void getContact() {
        //思路：1.先从自己的服务器中获取全部用户的头像名称信息，包括专家
        //      2.再从环信服务器中获取自己的好友列表，得到一个List<String>
        //     3.取出每个环信id，从1中获取的信息对比手机号，得到该手机号的信息
        //     4.匹配完信息之后存入本地数据库

        //从自己的服务器中取出全部用户的头像名称信息，包括专家
        OkHttpUtils.get().url(Constant.getAllContact).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                //解析json，得到所有用户的信息
                allContacts = JsonUtils.parseAllContacts(response);
                getContactFromHxServer();

            }
        });

    }

    //从环信服务获取好友信息
    private void getContactFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 获取到所有的好友的环信id
                try {
                    List<String> hxids = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (hxids != null && hxids.size() >= 0) {
                        List<ContactInfo> contacts = new ArrayList<>();
                        for (String hxid : hxids) {
                            ContactInfo userInfo = new ContactInfo();
                            for (ContactInfo u : allContacts) {//找出该环信id的用户信息
                                if (u.getPhone().equals(hxid)) {
                                    userInfo.setPhone(hxid);
                                    userInfo.setName(u.getName());
                                    userInfo.setPic(u.getPic());
                                    contacts.add(userInfo);
                                    break;
                                }

                            }
                        }
                        // 保存好友信息到本地数据库
                        Model.getInstance().getDbManager().getContactTableDao().saveContacts(contacts, true);

                        // 刷新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 刷新页面的方法
                                refreshContact();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册广播
        mLBM.unregisterReceiver(ContactInviteChangeReceiver);
        mLBM.unregisterReceiver(ContactChangeReceiver);
        mLBM.unregisterReceiver(GroupChangeRecevier);
    }

    @OnClick({R.id.iv_back, R.id.rl_new_friend, R.id.rl_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_new_friend:
                // 查看之后红点消失
                tvContactRed.setVisibility(View.GONE);
                //保存红点状态到sp中
                SpUtils.putBoolean(this, Constant.IS_NEW_INVITE, false);
                // 跳转到邀请信息列表页面
                Intent intent = new Intent(this, InviteActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_group:
                startActivity(new Intent(this, GroupListActivity.class));
                break;
        }

    }


    class ContactsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            ContactsViewHolder holder = null;
            if (holder == null) {
                convertView = View.inflate(MyFriendsActivity.this, R.layout.item_contacts, null);
                holder = new ContactsViewHolder();
                holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
                holder.tvIndex = convertView.findViewById(R.id.tv_index);
                holder.tvName = convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ContactsViewHolder) convertView.getTag();
            }

            ContactInfo user = datas.get(i);
            String s = user.getPinyin().substring(0, 1);
            if (i == 0 || !datas.get(i - 1).getPinyin().substring(0, 1).equals(s)) {
                holder.tvIndex.setVisibility(View.VISIBLE);
                holder.tvIndex.setText(user.getPinyin().substring(0, 1));
            } else {
                holder.tvIndex.setVisibility(View.GONE);
            }
            holder.tvName.setText(user.getName());
            Glide.with(MyFriendsActivity.this).load(user.getPic()).error(R.drawable.logo).into(holder.cv_user_pic);
            return convertView;
        }
    }

    class ContactsViewHolder {
        TextView tvIndex;
        CircleImageView cv_user_pic;
        TextView tvName;
    }

}
