package com.hy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.flystudio.wifihotspot.R;
import com.hy.wificonnect.WifilistconnectActivity;
import com.hy.wifihotspot.WifihotspotActivity;


/**
 * 本类仅为测试使用，为了方便copy，所以一些变量没有声明为全局的，如wifiManager
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWificonnect:
                startActivity(new Intent(MainActivity.this, WifilistconnectActivity.class));
                break;
            case R.id.btnWifihotspot:
                startActivity(new Intent(MainActivity.this, WifihotspotActivity.class));
                break;

           default:
               break;
        }
    }



}
