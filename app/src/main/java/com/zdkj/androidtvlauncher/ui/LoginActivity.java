package com.zdkj.androidtvlauncher.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rxjava.rxlife.RxLife;
import com.zdkj.androidtvlauncher.R;
import com.zdkj.androidtvlauncher.api.AppNetWork;
import com.zdkj.androidtvlauncher.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import pub.devrel.easypermissions.EasyPermissions;
import rxhttp.wrapper.param.RxHttp;

import static com.zdkj.androidtvlauncher.base.MyApp.getAndroidId;


public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String[] PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_STORAGE_CODE = 10001;
    @BindView(R.id.tv_Loading)
    TextView tvLoading;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String PERMISSION_STORAGE_MSG = "请授予权限，否则影响部分使用功能";
    private int i = 0;
    private boolean isJumpMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (NetUtils.isNetworkConnected(this)) {

            getClassIdFromNet(getAndroidId());
        } else {
            isJumpMain = true;
        }
        getPermissions();

    }

    private void getPermissions() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            JumpActivity();
        } else {
            EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        JumpActivity();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
    }

    /**
     * 第一步检查是否有网 ，如果没有网一直停留在二维码页面
     * 第二步如果有网，检查是否注册 有200 负责201
     */
    private void JumpActivity() {
        new CountDownTimer(2 * 1000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                i += 50;
                tvLoading.setText("loading" + i + "%");
                progressBar.setProgress(i);
            }

            @Override
            public void onFinish() {
                if (isJumpMain) {
                    startActivity(new Intent(LoginActivity.this, PlayVideoFragment.class));
//                    startActivity(new Intent(LoginActivity.this, TikTokActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this, ScanCodeActivity.class));
                    finish();
                }
            }
        }.start();

    }

    /**
     * 检查是否绑定 绑定跳广告页
     * 没有绑定 跳转绑定页
     *
     * @param AndroidID ANdroid唯一ID
     */
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
                        isJumpMain = jsonObject.getString("code").equals("200");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                });


    }
}
