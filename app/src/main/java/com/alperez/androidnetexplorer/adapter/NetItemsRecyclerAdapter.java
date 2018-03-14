package com.alperez.androidnetexplorer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alperez.androidnetexplorer.model.NetworkItemModel;

/**
 * Created by stanislav.perchenko on 3/14/2018.
 */

public class NetItemsRecyclerAdapter extends RecyclerView.Adapter<NetItemsRecyclerAdapter.MyViewHolder> {

    public NetItemsRecyclerAdapter(NetworkItemModel... data) {

    }

    public void replaceData(NetworkItemModel... data) {

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }


    }
}
