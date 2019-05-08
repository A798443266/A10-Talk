package com.eiplan.zuji.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.HomeDocAdapter;
import com.eiplan.zuji.adapter.HomeExpertAdapter;
import com.eiplan.zuji.adapter.SearchRecordAdapter;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.RecordInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonParser;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

/**
 * 搜索页面
 */

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_yuyin)
    LinearLayout llYuyin;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.lv_expert)
    ListView lv;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.lv_record)
    ListView lvRecord;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;

    private SweetAlertDialog pDialog;
    private int K;//判断用户要搜索的是专家还是文档
    private String content;//获取搜索框的内容
    private List<RecordInfo> records;//搜索记录
    private SearchRecordAdapter recordAdapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (experts.size() > 0 && experts != null) {
                    llRecord.setVisibility(View.GONE);
                    adapter = new HomeExpertAdapter(experts, SearchActivity.this);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ExpertInfo expert = experts.get(position);
                            if (expert == null)
                                return;
                            Intent intent = new Intent(SearchActivity.this, ExpertDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("expert", expert);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("抱歉！")
                            .setContentText("找不到指定内容")
                            .show();
                    llRecord.setVisibility(View.VISIBLE);
                }

            } else if (msg.what == 2) {
                if (docs.size() > 0 && docs != null) {
                    llRecord.setVisibility(View.GONE);
                    adapter1 = new HomeDocAdapter(docs, SearchActivity.this);
                    lv.setAdapter(adapter1);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DocInfo doc = docs.get(position);
                            if (doc == null)
                                return;
                            Intent intent = new Intent(SearchActivity.this, DocDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("doc", doc);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("抱歉！")
                            .setContentText("找不到指定内容")
                            .show();
                    llRecord.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private HomeExpertAdapter adapter;
    private HomeDocAdapter adapter1;
    private List<ExpertInfo> experts;
    private List<DocInfo> docs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        //用ToolBar代替原始的actionBar
        setSupportActionBar(toolbar);
        initView();

    }

    private void initView() {
        llRecord.setVisibility(View.GONE);
        K = getIntent().getIntExtra("content", 1);//获取传递过来的是搜索专家还是搜索文档信息
        //从本地数据库获取搜索记录
        records = Model.getInstance().getSearchRecordDao().queryByphone(SpUtils.getString(this, Constant.current_phone), K);
        experts = new ArrayList<>();
        docs = new ArrayList<>();
        if (K == 1) {
            etInput.setHint("输入专家名称或相关领域搜索");
        } else {
            etInput.setHint("输入文档名称或相关领域搜索");
        }

        //让软键盘出现搜索
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

        if (records != null && records.size() > 0) {
            llRecord.setVisibility(View.VISIBLE);
            recordAdapter = new SearchRecordAdapter(this, records);
            lvRecord.setAdapter(recordAdapter);

            recordAdapter.setOnItemDeleteClickListener(new SearchRecordAdapter.ItemDeleteClickListener() {
                @Override
                public void itemDeleteClick(View view) {
                    int position = (int) view.getTag();
                    RecordInfo record = records.get(position);
                    //从本地数据库中删除本条记录
                    Model.getInstance().getSearchRecordDao().deleteOne(SpUtils.getString(SearchActivity.this, Constant.current_phone)
                            , record.getType(), record.getContent());

                    //刷新适配器
                    records.remove(position);
                    recordAdapter.notifyDataSetChanged();
                }

            });
        }

        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etInput.setText(records.get(position).getContent());
                etInput.setSelection(etInput.length());//指针移动到文字末尾
            }
        });


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.tv_search, R.id.ll_yuyin, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                search();
                break;
            case R.id.ll_yuyin:
                showDialog();
                break;
            case R.id.iv_delete:

                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否要清除所有的聊天记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //从本地数据库删除所有记录
                                Model.getInstance().getSearchRecordDao().deleteAll(SpUtils.getString(SearchActivity.this, Constant.current_phone), K);
                                records.clear();
                                recordAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
        }
    }

    private void search() {
        content = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        pDialog.setTitleText("请稍后...");
        pDialog.show();

        if (K == 1) {
            //搜索记录存入本地数据库
            Model.getInstance().getSearchRecordDao().addRecord(SpUtils.getString(this, Constant.current_phone), 1, content);
            searchForExpert();//搜索专家
        } else {
            //搜索记录存入本地数据库
            Model.getInstance().getSearchRecordDao().addRecord(SpUtils.getString(this, Constant.current_phone), 2, content);
            searchForDoc();//搜索文档
        }

    }

    private void searchForDoc() {
        OkHttpUtils.post().addParams("key", content).url(Constant.DOCSEARCH)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                pDialog.dismiss();
                new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("抱歉！")
                        .setContentText("找不到指定内容")
                        .show();

            }

            @Override
            public void onResponse(String response, int id) {
                docs = JsonUtils.parseDocs(response);//解析json数据
                pDialog.dismissWithAnimation();
                handler.sendEmptyMessage(2);
            }
        });
    }

    private void searchForExpert() {
        OkHttpUtils.post().addParams("key", content).url(Constant.SEARCH_EXPERT)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                pDialog.dismiss();
                new SweetAlertDialog(SearchActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("抱歉！")
                        .setContentText("找不到指定内容")
                        .show();

            }

            @Override
            public void onResponse(String response, int id) {
                experts = JsonUtils.parseExperts(response);//解析json数据
                pDialog.dismissWithAnimation();
                handler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    //科大讯飞语音输入
    private void showDialog() {
        //1.创建 RecognizerDialog 对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示 dialog，接收语音输入
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {
        //返回的结果
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String results = recognizerResult.getResultString();//语音解析的结果（json数据）
            String text = JsonParser.parseIatResult(results);//解析json数据
            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results);
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            String s = resultBuffer.toString();
            String s1 = s.substring(0, s.length() - 1);
            etInput.setText(s1);//把数据写到输入框上
            etInput.setSelection(etInput.length());//指针移动到文字末尾

        }

        //出错了
        @Override
        public void onError(SpeechError speechError) {

        }
    }

    class MyInitListener implements InitListener {
        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(SearchActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
