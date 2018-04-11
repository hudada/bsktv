package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.LikeObjBean;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.ReplyListBean;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.bean.ZanObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SongDetailActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.btn_re)
    Button btn_re;
    @BindView(R.id.et_re)
    EditText et_re;
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
    @BindView(R.id.btn_like)
    Button btnLike;

    private ArrayList<ReplyBean> mdata = new ArrayList<>();
    private MyAdapter adapter;
    private SongBean shopBean;
    private int mPosition;
    private Player player;
    private int mProgress;

    private SongBean mSong;
    private boolean isFollow;
    private boolean isLike;
    private int likeCount;

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnLike.setEnabled(false);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        shopBean = (SongBean) getIntent().getSerializableExtra("data");
        adapter = new MyAdapter(mContext, R.layout.item_reply, mdata);
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
        return R.layout.activity_song_detail;
    }

    @Override
    protected void loadData() {
        mSong = (SongBean) getIntent().getSerializableExtra("data");
        likeCount = mSong.getLikeSum();
        tvTitle.setText(mSong.getName());
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

        long uid = MyApplication.getInstance().getUserBean().getId();
        long likeId = mSong.getUid();
        OkHttpTools.sendGet(mContext, ApiManager.LIKE_FIND)
                .addParams("uid", uid + "")
                .addParams("likeUid", likeId + "")
                .build()
                .execute(new BaseCallBack<LikeObjBean>(mContext, LikeObjBean.class) {
                    @Override
                    public void onResponse(LikeObjBean likeObjBean) {
                        if (likeObjBean.getData() == null) {
                            isFollow = false;
                            btnRight.setText("关注");
                        } else {
                            isFollow = true;
                            btnRight.setText("已关注");
                        }
                        btnRight.setVisibility(View.VISIBLE);
                    }
                });

        OkHttpTools.sendGet(mContext, ApiManager.SONG_ISZAN)
                .addParams("uid", uid + "")
                .addParams("sid", mSong.getId() + "")
                .build()
                .execute(new BaseCallBack<ZanObjBean>(mContext, ZanObjBean.class) {
                    @Override
                    public void onResponse(ZanObjBean zanObjBean) {
                        if (zanObjBean.getData() == null) {
                            btnLike.setText("赞（" + mSong.getLikeSum() + "）");
                            isLike = false;
                        } else {
                            btnLike.setText("已赞（" + mSong.getLikeSum() + "）");
                            isLike = true;
                        }
                        btnLike.setEnabled(true);
                    }
                });

        loadCommentData();

    }

    private void loadCommentData() {
        mdata.clear();
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


    @OnClick({R.id.btn_back, R.id.btn_right, R.id.btn_re, R.id.btn_play, R.id.btn_like})
    public void onViewClicked(View view) {
        long uid = MyApplication.getInstance().getUserBean().getId();
        long likeId = mSong.getUid();
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_re:
                if (TextUtils.isEmpty(et_re.getText().toString().trim())) {
                    showToast("回复内容不能为空！");
                    return;
                }
                OkHttpTools.sendPost(mContext, ApiManager.COMMENT_ADD)
                        .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                        .addParams("sid", mSong.getId() + "")
                        .addParams("msg", et_re.getText().toString().trim())
                        .build()
                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                            @Override
                            public void onResponse(BaseResponse baseResponse) {
                                showToast("发布成功");
                                loadCommentData();
                                et_re.setText("");
                            }
                        });
                break;
            case R.id.btn_play:
                player.play();
                break;
            case R.id.btn_like:
                if (isLike) {
                    OkHttpTools.sendPost(mContext, ApiManager.SONG_DOLIKE)
                            .addParams("id", uid + "")
                            .addParams("type", 0 + "")
                            .addParams("sid", mSong.getId() + "")
                            .build()
                            .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                @Override
                                public void onResponse(BaseResponse baseResponse) {
                                    likeCount--;
                                    btnLike.setText("赞（" + likeCount + "）");
                                    isLike = false;
                                }
                            });
                } else {
                    OkHttpTools.sendPost(mContext, ApiManager.SONG_DOLIKE)
                            .addParams("id", uid + "")
                            .addParams("type", 1 + "")
                            .addParams("sid", mSong.getId() + "")
                            .build()
                            .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                @Override
                                public void onResponse(BaseResponse baseResponse) {
                                    likeCount++;
                                    btnLike.setText("已赞（" + likeCount + "）");
                                    isLike = true;
                                }
                            });
                }
                break;
            case R.id.btn_right:
                if (uid == likeId) {
                    showToast("不能关注自己");
                    return;
                }
                if (isFollow) {
                    OkHttpTools.sendPost(mContext, ApiManager.LIKE_DEL)
                            .addParams("uid", uid + "")
                            .addParams("likeUid", likeId + "")
                            .build()
                            .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                @Override
                                public void onResponse(BaseResponse baseResponse) {
                                    showToast("取消关注");
                                    btnRight.setText("关注");
                                    isFollow = false;
                                }
                            });
                } else {
                    OkHttpTools.sendPost(mContext, ApiManager.LIKE_ADD)
                            .addParams("uid", uid + "")
                            .addParams("likeUid", likeId + "")
                            .build()
                            .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                @Override
                                public void onResponse(BaseResponse baseResponse) {
                                    showToast("关注成功");
                                    btnRight.setText("已关注");
                                    isFollow = true;
                                }
                            });
                }

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
