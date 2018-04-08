package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.SongBean;

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

    private ArrayList<ReplyBean> mdata = new ArrayList<>();
    private MyAdapter adapter;
    private SongBean shopBean;
    private int mPosition;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("歌曲详情");
        btnRight.setVisibility(View.GONE);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        shopBean = (SongBean) getIntent().getSerializableExtra("data");
        mdata = (ArrayList<ReplyBean>) shopBean.getReplyBeans();
        adapter = new MyAdapter(mContext, R.layout.item_reply, mdata);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
              //TODO 删除
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(adapter);
    }



    @Override
    protected int getRootViewId() {
        return R.layout.activity_song_detail;
    }

    @Override
    protected void loadData() {
    }


    @OnClick({R.id.btn_back, R.id.btn_right,R.id.btn_re})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_re:
                // TODO 提交回复
                if (TextUtils.isEmpty(et_re.getText().toString())){
                    showToast("回复内容不能为空！");
                    return;
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
//            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//            holder.setText(R.id.tv_name,"用户名："+replyBean.getUid());
//            holder.setText(R.id.tv_name,"时间："+format.format(new Date(replyBean.getTime())));
//            holder.setText(R.id.tv_name,"评论："+replyBean.getInfo());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        }
    }
}
