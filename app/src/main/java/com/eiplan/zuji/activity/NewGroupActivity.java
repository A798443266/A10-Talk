package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建群聊页面
 */

public class NewGroupActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.et_newgroup_name)
    EditText etNewgroupName;
    @BindView(R.id.et_newgroup_desc)
    EditText etNewgroupDesc;
    @BindView(R.id.cb_newgroup_public)
    CheckBox cbNewgroupPublic;
    @BindView(R.id.cb_newgroup_invite)
    CheckBox cbNewgroupInvite;

    List<EMGroupInfo> groupsList = new ArrayList<>();//存放从环信服务器中获取的群组列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("创建群组");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        ivBack.setBackgroundResource(R.drawable.icon_back);
    }

    @OnClick({R.id.iv_back, R.id.cb_newgroup_public, R.id.cb_newgroup_invite, R.id.bt_newgroup_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.cb_newgroup_public:
                break;
            case R.id.cb_newgroup_invite:
                break;
            case R.id.bt_newgroup_create:
                if (TextUtils.isEmpty(etNewgroupName.getText().toString())) {
                    Toast.makeText(this, "群组名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 跳转到选择联系人页面
                Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 成功获取到联系人
        if (resultCode == RESULT_OK) {
            // 创建群
            createGroup(data.getStringArrayExtra("members"));
        }
    }

    private void createGroup(final String[] members) {
        // 群名称
        final String groupName = etNewgroupName.getText().toString();
        // 群描述
        final String groupDesc = etNewgroupDesc.getText().toString();
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器创建群
                EMGroupOptions options = new EMGroupOptions();
                options.maxUsers = 200;//群最多容纳多少人
                EMGroupManager.EMGroupStyle groupStyle = null;

                if (cbNewgroupPublic.isChecked()) {//公开
                    if (cbNewgroupInvite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {
                    if (cbNewgroupInvite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                options.style = groupStyle; // 创建群的类型

                try {
                    // 参数一：群名称；参数二：群描述；参数三：群成员；参数四：原因；参数五：参数设置
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc, members, "邀请你加入群", options);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //从服务器中获取所有的公开群列表
                            getAllPublicFromHX();
                            // 结束当前页面
//                            finish();
                            Toast.makeText(NewGroupActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //从环信服务器中获取公开群列表
    private void getAllPublicFromHX() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(20, null);
                    groupsList = result.getData();
                    String cursor = result.getCursor();
                    if (groupsList != null && groupsList.size() > 0) {
                        Log.e("TAG", groupsList.get(0).getGroupId());
                        Log.e("TAG", cursor);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
