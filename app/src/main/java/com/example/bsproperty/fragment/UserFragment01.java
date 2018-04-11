package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.bean.SongListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.AccompanimentActivity;
import com.example.bsproperty.ui.SongDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment01 extends BaseFragment {
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
    private MyAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        loadWebData();
    }

    private void loadWebData() {
        mData.clear();

        OkHttpTools.sendGet(mContext, ApiManager.SONG_LIST)
                .build()
                .execute(new BaseCallBack<SongListBean>(mContext, SongListBean.class) {
                    @Override
                    public void onResponse(SongListBean songListBean) {
                        mData = songListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("音乐广场");
        btnBack.setVisibility(View.GONE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        adapter = new MyAdapter(mContext, R.layout.item_songs, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(mContext, SongDetailActivity.class);
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

        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("录制");
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_user01;
    }


    @OnClick(R.id.btn_right)
    public void onViewClicked() {
        startActivity(new Intent(mContext, AccompanimentActivity.class));
    }

    private class MyAdapter extends BaseAdapter<SongBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<SongBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, SongBean songBean, int position) {
            holder.setText(R.id.tv_name, songBean.getName());
            holder.setText(R.id.tv_total, "时间：" + MyApplication.formatTime.format(songBean.getLength()));
            holder.setText(R.id.tv_username, "用户名：" + songBean.getUname());
        }
    }
}
