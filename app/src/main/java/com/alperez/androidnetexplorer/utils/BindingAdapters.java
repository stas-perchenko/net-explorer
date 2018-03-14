package com.alperez.androidnetexplorer.utils;

import android.databinding.BindingAdapter;
import android.net.NetworkCapabilities;
import android.widget.TextView;

/**
 * Created by stanislav.perchenko on 3/14/2018.
 */

public final class BindingAdapters {

    private BindingAdapters() {}

    @BindingAdapter("number_text")
    public static void setTextView_NumberText(TextView tv, Number num) {
        tv.setText(num.toString());
    }


    @BindingAdapter("networkTransport")
    public static void setTextView_NetworkTransport(TextView tv, NetworkCapabilities nCaps) {
        StringBuilder sb = new StringBuilder();
        if (nCaps != null) {
            if (nCaps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) sb.append("Bluetooth");
            if (nCaps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("WiFi");
            }
            if (nCaps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("Ethernet");
            }
            if (nCaps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("CELLULAR");
            }
            if (nCaps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("VPN");
            }

        }

        tv.setText((sb.length() > 0) ? sb.toString() : "-");
    }

    @BindingAdapter("secondaryCapabilities")
    public static void setTextView_SecondaryCapabilities(TextView tv, NetworkCapabilities nCaps) {
        StringBuilder sb = new StringBuilder();
        if (nCaps != null) {
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)) sb.append("Captive portal");
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_CBS)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("CBS");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_DUN)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("DUN");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_EIMS)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("EIMS");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_FOTA)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("FOTA");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_IA)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("IA");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_IMS)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("IMS");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_RCS)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("RCS");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_SUPL)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("SUPL");
            }
            if (nCaps.hasTransport(NetworkCapabilities.NET_CAPABILITY_XCAP)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("XCAP");
            }
        }

        tv.setText((sb.length() > 0) ? sb.toString() : "-");
    }
}
