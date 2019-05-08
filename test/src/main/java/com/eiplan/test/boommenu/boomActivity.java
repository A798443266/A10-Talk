package com.eiplan.test.boommenu;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.eiplan.test.MainActivity;
import com.eiplan.test.R;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

public class boomActivity extends AppCompatActivity {

    private BoomMenuButton boomMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boom);

        boomMenuButton = findViewById(R.id.bmb);
        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Toast.makeText(boomActivity.this, "点击了 " + index, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .buttonRadius(90)
                    .textSize(12)
//                    .normalTextColor(Color.BLACK)
                    .imagePadding(new Rect(20, 25, 80, 80))
                    .normalImageRes(getImageResource())
                    .normalColor(getColor())
                    .normalText(getext());
            boomMenuButton.addBuilder(builder);
        }
    }

    private static int index = 0;

    static String getext() {
        if (index >= text.length) index = 0;
        return text[index++];

    }
    private static int color1 = 0;

    static int getColor() {
        if (color1 >= color.length) color1 = 0;
        return color[color1++];

    }

    private static String[] text = new String[]{"发布文档", "拍摄小视频", "开启直播", "发布问题", "发布需求"};
    private static int[] color = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.MAGENTA};
    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int[] imageResources = new int[]{
            R.drawable.icon_consult2,
            R.drawable.icon_consult3,
            R.drawable.icon_consult2,
            R.drawable.icon_consult2,
            R.drawable.icon_consult3
    };


}
