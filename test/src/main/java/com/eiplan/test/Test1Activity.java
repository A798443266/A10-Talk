package com.eiplan.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Test1Activity extends Activity implements AdapterView.OnItemClickListener {

    private GridView gridview;
    private GridAdapter adapter;
    private float dp;
    public static List<Bitmap> bmp = new ArrayList<Bitmap>();
    public static List<String> drr = new ArrayList<String>();
    private LinearLayout ll_popup;
    private Button button1;// 发布按钮

    List<String> urList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        init();

        final EditText editText1 = (EditText) findViewById(R.id.editText1);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().length() == 0) {
                    Toast.makeText(Test1Activity.this, "请简述详情", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Test1Activity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void init() {
//        dp = getResources().getDimension(R.dimen.dp);
        gridview = (GridView) findViewById(R.id.Scrollgridview);
        gridviewInit();
    }

    public void gridviewInit() {
        adapter = new GridAdapter(this, bmp);
        adapter.setSelectedPosition(0);
        gridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridview.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(Test1Activity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (position == bmp.size()) {
            String sdcardState = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                if (bmp.size() < 5) {
                    new PopupWindows(Test1Activity.this, gridview);
                } else {
                    Toast.makeText(getApplicationContext(), "一次只能传5张", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(Test1Activity.this, PhotoActivity.class);
            intent.putExtra("ID", position);
            startActivity(intent);
        }
    }

    private class GridAdapter extends BaseAdapter {
        private LayoutInflater listContainer;
        private int selectedPosition = -1;
        private List<Bitmap> bmps = new ArrayList<Bitmap>();

        private class ViewHolder {
            public ImageView image;
        }

        private GridAdapter(Context context, List<Bitmap> bmp) {
            listContainer = LayoutInflater.from(context);
            bmps = bmp;
        }

        public int getCount() {
            return bmps.size() + 1;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int sign = position;
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = listContainer.inflate(R.layout.item_published_grida, null);
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == bmps.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(bmps.get(position));
            }
            return convertView;
        }
    }


    private class PopupWindows extends PopupWindow {
        private PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
            Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }


    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int UI = 2;
    private String path = "";
    private Uri mOutPutFileUri;
    String picturePath;

    private void photo() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = Environment.getExternalStorageDirectory().toString() + "/elife/img";
        File path1 = new File(path);
        if (!path1.exists()) {
            path1.mkdirs();
        }
        File file = new File(path1, System.currentTimeMillis() + ".jpg");
        mOutPutFileUri = Uri.fromFile(file);
        picturePath = file.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private void photoview() {
        Intent pc = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pc, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    startPhotoZoom(picturePath);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] files = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(uri, files, null, null, null);
                    c.moveToFirst();
                    int ii = c.getColumnIndex(files[0]);
                    path = c.getString(ii);
                    c.close();
                    startPhotoZoom(path);
                }
                break;
        }
    }

    private Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        Drawable imageDrawable = new BitmapDrawable(image);
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF outerRect = new RectF(0, 0, x, y);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
        //		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();
        return output;
    }

    private void startPhotoZoom(String uri) {
        drr.add(uri);
        Bitmap bitmap = getLoacalBitmap(drr.get(drr.size() - 1));
        bmp.add(bitmap);
        gridviewInit();
    }

    @Override
    protected void onDestroy() {
        bmp.clear();
        drr.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}

