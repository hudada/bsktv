package com.example.bsproperty.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.utils.AudioRecoderUtils;
import com.example.bsproperty.utils.Player;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.sb_bar)
    SeekBar sbBar;
    @BindView(R.id.iv_cd)
    ImageView ivCd;
    @BindView(R.id.iv_v)
    ImageView ivV;
    @BindView(R.id.btn_play)
    Button btnPlay;

    private Player player;
    private int mProgress;
    private AudioRecoderUtils audioRecoderUtils;

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnRight.setText("发布");
        btnRight.setVisibility(View.VISIBLE);

        sbBar.setClickable(false);
        sbBar.setEnabled(false);
        sbBar.setSelected(false);
        sbBar.setFocusable(false);

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

        try {
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor afd = assetManager.openFd("test.mp3");
            player = new Player(afd, sbBar, new Player.OnPlayListener() {
                @Override
                public void onLoad(int duration) {
                }

                @Override
                public void onProgress(int position) {
                }

                @Override
                public void onCompletion() {
                    sbBar.setProgress(0);
                    audioRecoderUtils.stopRecord(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        audioRecoderUtils = new AudioRecoderUtils();
        audioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            @Override
            public void onUpdate(double db, long time) {
                int dbint = (int) ((int) db * 1.5);
                int v = dbint / 25;
                if (dbint > 0 && v <= 1) {
                    ivV.getDrawable().setLevel(1);
                } else {
                    ivV.getDrawable().setLevel(v);
                }
            }

            @Override
            public void onStop(String filePath) {
                Intent intent = new Intent(mContext, SubmitActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (audioRecoderUtils != null) {
            audioRecoderUtils.stopRecord(false);
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_play;
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.btn_right, R.id.btn_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示")
                        .setMessage("是否立即完成录制？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                player.stop();
                                player.release();
                                player = null;
                                audioRecoderUtils.stopRecord(true);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.btn_play:
                player.play();
                audioRecoderUtils.startRecord();
                btnPlay.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
