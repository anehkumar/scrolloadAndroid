package com.scrolloadrecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomViewHolder> {
    private List<GetterSetter> feedItemList;
    private Context mContext;

    public ProductAdapter(Context context, ArrayList<GetterSetter> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

    }

    @Override
    public ProductAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        GetterSetter dc_list = feedItemList.get(position);
        Picasso.with(mContext).load(URLs.imageBAse + "w450-h450/" + dc_list.getImage()).placeholder(R.drawable.ic_dress).into(holder.imageView);
        holder.name.setText(dc_list.getProduct());

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.product);
        }
    }
}
