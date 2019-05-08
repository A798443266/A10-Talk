package com.hyphenate.easeui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telecom.Call;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(final Context context, String username, final ImageView imageView) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(R.drawable.ease_default_avatar)
                        .transform(new GlideGetCircle(context))
                        .into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(R.drawable.ease_default_avatar)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .transform(new GlideGetCircle(context))
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar)
                    .transform(new GlideGetCircle(context))
                    .into(imageView);
        }
    }
    public static void setUserAvatar2(Context context, String username, ImageView imageView) {
        //根据username从本地数据库中提取发消息用户的头像（或者可以从服务器中根据username（环信id）获取用户头像）
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
                Glide.with(context).load(R.drawable.ease_default_avatar)
//                        .transform(new GlideGetCircle(context))
                        .into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(R.drawable.ease_default_avatar)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
//                        .transform(new GlideGetCircle(context))
                        .into(imageView);
            }
        } else {
            //加载圆形图片
            Glide.with(context).load(R.drawable.ease_default_avatar)
//                    .transform(new GlideGetCircle(context))
                    .into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNickname() != null) {
                textView.setText(user.getNickname());
            } else {
                textView.setText(username);
            }
        }
    }


    /**
     * 思路：
     * 第一次先根据url去服务器请求下载图片并保存在本地，接下来接可以从本地加载图片
     *
     *
     * //本地文件
     *     File file = new File(Environment.getExternalStorageDirectory(), "xiayu.png");
     *     //加载图片
     *     Glide.with(this).load(file).into(iv);
     */

    /**
     * 保存位图到本地
     * @param bitmap
     * @param path 本地路径
     * @return void
     */
   /* public void SavaImage(Bitmap bitmap, String path){
        File file=new File(path);
        FileOutputStream fileOutputStream=null;
        //文件夹不存在，则创建它
        if(!file.exists()){
            file.mkdir();
        }
        try {
            fileOutputStream=new FileOutputStream(path+"/"+System.currentTimeMillis()+".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*//使用okhttp下载图片到指定文件夹

    OkHttpClient okHttpClient=new OkHttpClient();
    Request request=new Request.Builder()
            .get()
            .url("http://pic.qiantucdn.com/58pic/17/85/35/559de1de9b223_1024.jpg")
            .build();
    Call call=okHttpClient.newCall(request);
  call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            //将响应数据转化为输入流数据
            InputStream inputStream=response.body().byteStream();
            //将输入流数据转化为Bitmap位图数据
            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
            File file=new File("/mnt/sdcard/picture.jpg");
            file.createNewFile();
            //创建文件输出流对象用来向文件中写入数据
            FileOutputStream out=new FileOutputStream(file);
            //将bitmap存储为jpg格式的图片
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            //刷新文件流
            out.flush();
            out.close();
            Message msg=Message.obtain();
            msg.obj=bitmap;
            handler.sendMessage(msg);
        }
    });

   // 四.如果要把下载的图片显示出来，可以在主线程中添加自定义Handler内部类
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            image.setImageBitmap((Bitmap) msg.obj);
        }
    };*/

}
