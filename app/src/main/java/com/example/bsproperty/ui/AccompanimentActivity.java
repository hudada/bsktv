package com.example.bsproperty.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.utils.DenstityUtils;
import com.example.bsproperty.utils.LQRPhotoSelectUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class AccompanimentActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.ll_my)
    LinearLayout llMy;
    @BindView(R.id.ll_web)
    LinearLayout llWeb;

    private LayoutInflater mInflater;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_accompaniment;
    }

    @Override
    protected void loadData() {
        for (int i = 0; i < 3; i++) {
            View view = mInflater.inflate(R.layout.item_accom, null, true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DenstityUtils.dp2px(mContext, 50));
            view.findViewById(R.id.rl_root).setTag(i);
            view.findViewById(R.id.btn_act).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionGen.with((Activity) mContext)
                            .addRequestCode(521)
                            .permissions(Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).request();
                }
            });
            llMy.addView(view, params);
        }

        for (int i = 0; i < 13; i++) {
            View view = mInflater.inflate(R.layout.item_accom, null, true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DenstityUtils.dp2px(mContext, 50));
            llWeb.addView(view, params);
        }
    }

    @PermissionSuccess(requestCode = 521)
    private void ok() {
        Intent intent = new Intent(mContext, PlayActivity.class);
        startActivity(intent);
    }

    @PermissionFail(requestCode = 521)
    private void showTip1() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-权限 中开启相关权限");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
