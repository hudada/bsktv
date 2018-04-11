package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.ReplyListBean;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MySongDetailActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.sb_bar)
    SeekBar sbBar;
    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.tv_pro)
    TextView tvPro;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.tv_like)
    TextView tvLike;

    private ArrayList<ReplyBean> mdata = new ArrayList<>();
    private MySongDetailActivity.MyAdapter adapter;
    private SongBean shopBean;
    private int mPosition;
    private Player player;
    private int mProgress;

    private SongBean mSong;

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnRight.setVisibility(View.GONE);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        shopBean = (SongBean) getIntent().getSerializableExtra("data");
        adapter = new MySongDetailActivity.MyAdapter(mContext, R.layout.item_reply, mdata);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                //TODO 删除
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(adapter);

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
        return R.layout.activity_my_song_detail;
    }

    @Override
    protected void loadData() {
        mSong = (SongBean) getIntent().getSerializableExtra("data");
        tvTitle.setText(mSong.getName());
        tvLike.setText("点赞数" + mSong.getLikeSum());
        player = new Player(ApiManager.MP3_PATH + mSong.getAddr(), sbBar, new Player.OnPlayListener() {
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

        OkHttpTools.sendPost(mContext, ApiManager.COMMENT_LIST)
                .addParams("sid", mSong.getId() + "")
                .build()
                .execute(new BaseCallBack<ReplyListBean>(mContext, ReplyListBean.class) {
                    @Override
                    public void onResponse(ReplyListBean replyListBean) {
                        mdata = replyListBean.getData();
                        adapter.notifyDataSetChanged(mdata);
                    }
                });
    }


    @OnClick({R.id.btn_back, R.id.btn_right, R.id.btn_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_play:
                player.play();
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<ReplyBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ReplyBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ReplyBean replyBean, int position) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            holder.setText(R.id.tv_name, "用户名：" + replyBean.getUname());
            holder.setText(R.id.tv_total, "时间：" + format.format(replyBean.getTime()));
            holder.setText(R.id.tv_username, "评论：" + replyBean.getMsg());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        }
    }
}
