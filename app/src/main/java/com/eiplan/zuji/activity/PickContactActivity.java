package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.PickContactAdapter;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.PickContactInfo;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建群组选择联系人界面
 */
public class PickContactActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_pick)
    ListView lvPick;

    private List<PickContactInfo> mPicks;
    private PickContactAdapter pickContactAdapter;
    private List<String> mExistMembers;//保存群中已经有的成员,方便传递给适配器，不允许修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        ButterKnife.bind(this);

        // 获取传递过来的数据
        getData1();
        initData();

        lvPick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果群中已经存在此成员,不操作
                if (mExistMembers.contains(mPicks.get(position).getContact().getPhone())) {
                    return;
                }
                //获取点击条目的CheckBox
                CheckBox checkBox = view.findViewById(R.id.cb_pick);
                checkBox.setChecked(!checkBox.isChecked());
                //更改数据
                PickContactInfo pickContactInfo = mPicks.get(position);
                pickContactInfo.setIsChecked(checkBox.isChecked());
                //刷新适配器
                pickContactAdapter.notifyDataSetChanged();
            }
        });
    }


    private void getData1() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);
        if (groupId != null) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            // 获取群中已经存在的所有群成员
            mExistMembers = group.getMembers();
        }
        if (mExistMembers == null) {
            mExistMembers = new ArrayList<>();
        }
    }

    private void initData() {
        // 从本地数据库中获取所有的联系人信息
        List<ContactInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();
        mPicks = new ArrayList<>();
        if (contacts != null && contacts.size() >= 0) {
            // 转换
            for (ContactInfo contact : contacts) {
                PickContactInfo pickContactInfo = new PickContactInfo(contact, false);
                mPicks.add(pickContactInfo);
            }
        }
        // 初始化listview
        pickContactAdapter = new PickContactAdapter(this, mPicks, mExistMembers);
        lvPick.setAdapter(pickContactAdapter);
    }

    @OnClick({R.id.iv_back, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                // 获取到已经选择的联系人
                List<String> names = pickContactAdapter.getPickContacts();
                // 给启动页面返回数据
                Intent intent = new Intent();
                intent.putExtra("members", names.toArray(new String[0]));//把list转换为数组
                // 设置返回的结果码
                setResult(RESULT_OK, intent);
                // 结束当前页面
                finish();
                break;
        }
    }
}
