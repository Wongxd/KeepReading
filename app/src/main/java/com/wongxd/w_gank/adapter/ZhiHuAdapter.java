package com.wongxd.w_gank.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wongxd.w_gank.R;
import com.wongxd.w_gank.model.ZhiHuBean;
import com.wongxd.w_gank.utils.glide.GlideLoader;

public class ZhiHuAdapter extends RecyclerArrayAdapter<ZhiHuBean.StoriesBean> {

        public ZhiHuAdapter(Context context) {
            super(context);
        }


        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ZhiHuViewHolder(parent);
        }


        public class ZhiHuViewHolder extends BaseViewHolder<ZhiHuBean.StoriesBean> {
            private ImageView ivImg;
            private TextView tvTitle;
            private TextView tvDate;
            private CardView cv_content;

            public ZhiHuViewHolder(ViewGroup parent) {
                super(parent, R.layout.rv_zhihu_list_item);
                ivImg = $(R.id.iv_zhihu_item_img);
                tvTitle = $(R.id.tv_zhihu_item_title);
                tvDate = $(R.id.tv_zhihu_item_date);
                cv_content = $(R.id.cv_zhihu_item_content);
            }

            @Override
            public void setData(ZhiHuBean.StoriesBean data) {
                if (data.getDate().contains("-")) {
                    cv_content.setVisibility(View.GONE);
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText(data.getDate());
                } else {
                    cv_content.setVisibility(View.VISIBLE);
                    tvDate.setVisibility(View.GONE);
                    GlideLoader.LoadAsRoundImage(getContext(), data.getImages().get(0), ivImg);
                    tvTitle.setText(data.getTitle());
                }
            }
        }
    }