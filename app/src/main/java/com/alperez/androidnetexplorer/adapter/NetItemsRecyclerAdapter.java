package com.alperez.androidnetexplorer.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alperez.androidnetexplorer.R;
import com.alperez.androidnetexplorer.databinding.ItemNetworkBinding;
import com.alperez.androidnetexplorer.model.NetworkItemModel;

import java.util.ArrayList;

/**
 * Created by stanislav.perchenko on 3/14/2018.
 */

public class NetItemsRecyclerAdapter extends RecyclerView.Adapter<NetItemsRecyclerAdapter.MyViewHolder> {
    private final ArrayList<NetworkItemModel> mData;
    private final LayoutInflater inflater;

    public NetItemsRecyclerAdapter(Context ctx, NetworkItemModel... data) {
        inflater = LayoutInflater.from(ctx);
        mData = new ArrayList<>(2);
        replaceData(data);
    }

    public void replaceData(NetworkItemModel... data) {
        mData.clear();
        mData.ensureCapacity(data.length);
        for (NetworkItemModel item : data) {
            mData.add(item);
        }
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNetworkBinding bnd = DataBindingUtil.inflate(inflater, R.layout.item_network, parent, false);
        return new MyViewHolder(bnd);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.binding.setNetItemModel(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemNetworkBinding binding;
        public MyViewHolder(ItemNetworkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
