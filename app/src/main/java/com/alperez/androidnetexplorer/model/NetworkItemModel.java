package com.alperez.androidnetexplorer.model;

import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import com.google.auto.value.AutoValue;

/**
 * This model class is to be used to display all options of Network instances provided
 * by the ConnectivityManager
 *
 * Created by stanislav.perchenko on 3/13/2018.
 */
@AutoValue
public abstract class NetworkItemModel {

    public abstract boolean isActiveDefaultDataNetwork();
    public abstract Network network();
    public abstract NetworkInfo networkInfo();
    public abstract NetworkCapabilities networkCapabilities();
    public abstract LinkProperties linkProperties();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setIsActiveDefaultDataNetwork(boolean isActiveDefaultDataNetwork);
        public abstract Builder setNetwork(Network network);
        public abstract Builder setNetworkInfo(NetworkInfo networkInfo);
        public abstract Builder setNetworkCapabilities(NetworkCapabilities networkCapabilities);
        public abstract Builder setLinkProperties(LinkProperties linkProperties);

        public abstract NetworkItemModel build();
    }

    abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_NetworkItemModel.Builder();
    }

    public NetworkItemModel clone() {
        return builder()
                .setIsActiveDefaultDataNetwork(isActiveDefaultDataNetwork())
                .setNetwork(network())
                .setNetworkInfo(networkInfo())
                .setNetworkCapabilities(networkCapabilities())
                .setLinkProperties(linkProperties())
                .build();
    }
}
