package com.zdkj.androidtvlauncher.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.rxjava.rxlife.RxLife;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.api.AppNetWork;
import com.zdkj.androidtvlauncher.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static com.zdkj.androidtvlauncher.base.MyApp.getAndroidId;


/**
 * 生成二维码
 */
public class ScanCodeActivity extends BaseActivity {

    private static String KEY = "http://ad.zadtek.com/bind/index?key=";
    @BindView(R.id.ivScan)
    ImageView ivScan;
    private String AndroidId;
    private boolean checkBind = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        getImage(KEY + getAndroidId());
        AndroidId = getAndroidId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //需要在子线程中处理的逻辑
                while (checkBind) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getClassIdFromNet(AndroidId);

                }

            }
        }).start();
    }

    private void getImage(String s) {
        String textContent = s;
        if (TextUtils.isEmpty(textContent)) {
            Toast.makeText(ScanCodeActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        ivScan.setImageBitmap(mBitmap);
    }

    private void getClassIdFromNet(String AndroidID) {
        RxHttp.get(AppNetWork.ISBIND + AndroidID)
                .asString()
                .observeOn(AndroidSchedulers.mainThread())   //控制下游在主线程执行
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .as(RxLife.asOnMain(this))
                .subscribe(s -> {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        if (jsonObject.getString("code").equals("200")) {
                            checkBind = false;
                            startActivity(new Intent(ScanCodeActivity.this, PlayVideoFragment.class));
//                            startActivity(new Intent(ScanCodeActivity.this, TikTokActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkBind = false;
    }
}
