package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.ui.SongDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment02 extends BaseFragment {
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
    private UserFragment02.MyAdapter adapter;

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
        tvTitle.setText("我关注的");
        btnBack.setVisibility(View.GONE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        adapter = new UserFragment02.MyAdapter(mContext, R.layout.item_songs, mData);
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
