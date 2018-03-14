package com.alperez.androidnetexplorer.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
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

        mConnManager.registerNetworkCallback(netRequestAll, netCallback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mConnManager.unregisterNetworkCallback(netCallback);
    }

    private void populateData() {
        boolean isActive = mConnManager.isDefaultNetworkActive();
        vTxtDefaultNetActive.setSelected(isActive);
        vTxtDefaultNetActive.setText(getString(isActive ? R.string.default_network_active : R.string.default_network_not_active));

        if (adapter == null) {
            adapter = new NetItemsRecyclerAdapter(getContext(), getData());
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


    private final ConnectivityManager.NetworkCallback netCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            getActivity().runOnUiThread(() -> populateData());
        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            getActivity().runOnUiThread(() -> populateData());
        }

        @Override
        public void onLost(Network network) {
            getActivity().runOnUiThread(() -> populateData());
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            getActivity().runOnUiThread(() -> {
                if (adapter != null) adapter.updateNetworkCapabilities(network, networkCapabilities);
            });
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            getActivity().runOnUiThread(() -> {
                if (adapter != null) adapter.updateLinkProperties(network, linkProperties);
            });
        }
    };


    private final NetworkRequest netRequestAll = new NetworkRequest.Builder()
            /*.addCapability(NetworkCapabilities.NET_CAPABILITY_MMS)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_SUPL)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_DUN)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_FOTA)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_IMS)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_CBS)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_IA)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_RCS)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_XCAP)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_EIMS)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            .addCapability((Build.VERSION.SDK_INT >= 23) ? NetworkCapabilities.NET_CAPABILITY_VALIDATED : NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability((Build.VERSION.SDK_INT >= 23) ? NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL : NetworkCapabilities.NET_CAPABILITY_INTERNET)*/
            .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build();

}
