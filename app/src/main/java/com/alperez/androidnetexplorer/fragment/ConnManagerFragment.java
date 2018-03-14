package com.alperez.androidnetexplorer.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alperez.androidnetexplorer.R;
import com.alperez.androidnetexplorer.adapter.NetItemsRecyclerAdapter;
import com.alperez.androidnetexplorer.model.NetworkItemModel;
import com.alperez.androidnetexplorer.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stanislav.perchenko on 3/14/2018.
 */

public class ConnManagerFragment extends Fragment {

    private ConnectivityManager mConnManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    private View vContent;
    private TextView vTxtDefaultNetActive;
    private SwipeRefreshLayout vRefresher;
    private AppBarLayout vAppBar;


    private NetItemsRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vContent == null) {
            vContent = inflater.inflate(R.layout.fragment_conn_manager, container, false);
        } else {
            container.removeView(vContent);
        }
        return vContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        vAppBar = (AppBarLayout) vContent.findViewById(R.id.app_bar);
        vRefresher = (SwipeRefreshLayout) vContent.findViewById(R.id.refresher);
        vRefresher.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) vAppBar.getLayoutParams()).getBehavior();
                return (behavior instanceof AppBarLayout.Behavior) ? (((AppBarLayout.Behavior) behavior).getTopAndBottomOffset() != 0) : false;
            }
        });
        vRefresher.setOnRefreshListener(() -> {
            vRefresher.postDelayed(() -> populateData(), 60);
            vRefresher.postDelayed(() -> vRefresher.setRefreshing(false), 600);
        });

        vTxtDefaultNetActive = (TextView) vContent.findViewById(R.id.txt_default_net_active);

        RecyclerView vRec = (RecyclerView) vContent.findViewById(R.id.recycler);
        vRec.setHasFixedSize(false);
        vRec.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateData();
    }

    private void populateData() {
        boolean isActive = mConnManager.isDefaultNetworkActive();
        vTxtDefaultNetActive.setSelected(isActive);
        vTxtDefaultNetActive.setText(getString(isActive ? R.string.default_network_active : R.string.default_network_not_active));

        if (adapter == null) {
            adapter = new NetItemsRecyclerAdapter(getData());
            ((RecyclerView) vContent.findViewById(R.id.recycler)).setAdapter(adapter);
        } else {
            adapter.replaceData(getData());
        }
    }

    private NetworkItemModel[] getData() {
        NetworkInfo activeNI = mConnManager.getActiveNetworkInfo();

        List<NetworkItemModel> buffer = new ArrayList<>(4);
        for (Network net : mConnManager.getAllNetworks()) {
            NetworkInfo ni = mConnManager.getNetworkInfo(net);
            NetworkItemModel item = NetworkItemModel.builder()
                    .setIsActiveDefaultDataNetwork((activeNI != null) && (ni != null) && NetworkUtils.isNetworkInfosForTheSameNatwork(activeNI, ni))
                    .setNetwork(net)
                    .setNetworkInfo(ni)
                    .setNetworkCapabilities(mConnManager.getNetworkCapabilities(net))
                    .setLinkProperties(mConnManager.getLinkProperties(net))
                    .build();
            if (item.isActiveDefaultDataNetwork()) {
                buffer.add(0, item);
            } else {
                buffer.add(item);
            }
        }
        return buffer.toArray(new NetworkItemModel[buffer.size()]);
    }

}
