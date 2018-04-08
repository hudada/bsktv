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

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.utils.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MySongActivity extends BaseActivity {

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


    private ArrayList<SongBean> mData;
    private MySongActivity.MyAdapter adapter;


    @Override
    public void onResume() {
        super.onResume();
        loadWebData();
    }

    private void loadWebData() {
        mData.clear();
        for (int i = 0; i < 5; i++) {
            ArrayList<ReplyBean> re = new ArrayList<>();
            SongBean songBean = new SongBean();
            for (int u = 0; u < 8; u++) {
                re.add(new ReplyBean());
            }
            songBean.setReplyBeans(re);
            mData.add(songBean);
        }
//        OkHttpTools.sendGet(mContext, ApiManager.SHOP_LIST)
//                .build()
//                .execute(new BaseCallBack<ShopListBean>(mContext, ShopListBean.class) {
//                    @Override
//                    public void onResponse(ShopListBean shopListBean) {
//                        mData = shopListBean.getData();
//                        adapter.notifyDataSetChanged(mData);
//                    }
//                });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("我的歌曲");
        btnBack.setVisibility(View.GONE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        adapter = new MySongActivity.MyAdapter(mContext, R.layout.item_songs, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(mContext, MySongDetailActivity.class);
                intent.putExtra("data", mData.get(position));
                startActivity(intent);
            }
        });
        rvList.setAdapter(adapter);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });


    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_user02;
    }

    private class MyAdapter extends BaseAdapter<SongBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<SongBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, SongBean songBean, int position) {
//            holder.setText(R.id.tv_name, songBean.getSong_name());
//            //TODO 都没转换的
//            holder.setText(R.id.tv_total, "时间："+songBean.getSong_name());
//            holder.setText(R.id.tv_username, "用户名："+songBean.getUserid());
        }
    }
}
