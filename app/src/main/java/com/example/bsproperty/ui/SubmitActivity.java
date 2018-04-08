package com.example.bsproperty.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.utils.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_pro)
    TextView tvPro;
    @BindView(R.id.sb_bar)
    SeekBar sbBar;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.btn_play)
    Button btnPlay;

    private Player player;
    private int mProgress;
    private String mPath;

    @Override
    protected void initView(Bundle savedInstanceState) {
        sbBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress * player.mediaPlayer.getDuration()
                        / seekBar.getMax();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.mediaPlayer.seekTo(mProgress);
            }
        });

        mPath = getIntent().getStringExtra("path");
        try {
            player = new Player(mPath, sbBar, new Player.OnPlayListener() {
                @Override
                public void onLoad(int duration) {
                    tvTotal.setText(MyApplication.formatTime.format(duration));
                }

                @Override
                public void onProgress(int position) {
                    tvPro.setText(MyApplication.formatTime.format(position));
                }

                @Override
                public void onCompletion() {
                    tvPro.setText("00:00");
                    sbBar.setProgress(0);
                }
            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_submit;
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.btn_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示")
                        .setMessage("是否放弃本次录制？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(mPath);
                                file.delete();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.btn_play:
                File file = new File(Environment.getExternalStorageDirectory() + "/record/test.mp3");
                break;
        }
    }

    public void uniteAMRFile(String[] partsPaths, String unitedFilePath) {
        try {
            File unitedFile = new File(unitedFilePath);
            FileOutputStream fos = new FileOutputStream(unitedFile);
            RandomAccessFile ra = null;
            for (int i = 0; i < partsPaths.length; i++) {
                ra = new RandomAccessFile(partsPaths[i], "r");
                if (i != 0) {
                    ra.seek(6);
                }
                byte[] buffer = new byte[1024 * 8];
                int len = 0;
                while ((len = ra.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            ra.close();
            fos.close();
        } catch (Exception e) {
        }
    }
}
