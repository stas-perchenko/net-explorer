package com.alperez.androidnetexplorer.utils;

import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by stanislav.perchenko on 3/13/2018.
 */

public class NetworkUtils {

    public static boolean isNetworkInfosForTheSameNatwork(@NonNull NetworkInfo ni1, @NonNull NetworkInfo ni2) {
        boolean equals = ni1.getType() == ni2.getType()
                && TextUtils.equals(ni1.getTypeName(), ni2.getTypeName())
                && ni1.getSubtype() == ni2.getSubtype()
                && TextUtils.equals(ni1.getSubtypeName(), ni2.getSubtypeName())
                && TextUtils.equals(ni1.getExtraInfo(), ni2.getExtraInfo());
        return equals;
    }

}
