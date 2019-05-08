package com.eiplan.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eiplan.test.boommenu.boomActivity;
import com.eiplan.test.palette.PaletteActivity;
import com.eiplan.test.viewpager.ViewpagerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View v){
        startActivity(new Intent(this,Test1Activity.class));

    }
    public void click2(View v){
        startActivity(new Intent(this,TuyaActivity.class));

    }
    public void click3(View v){
        startActivity(new Intent(this,PaletteActivity.class));

    }
    public void click4(View v){
        startActivity(new Intent(this,boomActivity.class));

    }
    public void click5(View v){
        startActivity(new Intent(this,ViewpagerActivity.class));

    }
    public void click6(View v){
        startActivity(new Intent(this,MoreActivity.class));

    }

}
