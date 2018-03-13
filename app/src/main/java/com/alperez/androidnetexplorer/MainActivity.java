package com.alperez.androidnetexplorer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alperez.androidnetexplorer.databinding.ItemNetworkBinding;
import com.alperez.androidnetexplorer.model.NetworkItemModel;
import com.alperez.androidnetexplorer.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNI = cm.getActiveNetworkInfo();
        if (activeNI == null) {
            displayError("No default active network.");
            return;
        }


        for (Network net : cm.getAllNetworks()) {
            NetworkInfo ni = cm.getNetworkInfo(net);
            if ((ni != null) && NetworkUtils.isNetworkInfosForTheSameNatwork(activeNI, ni)) {
                displayNetworkItem(NetworkItemModel.builder()
                        .setIsActiveDefaultDataNetwork(true)
                        .setNetwork(net)
                        .setNetworkInfo(ni)
                        .setNetworkCapabilities(cm.getNetworkCapabilities(net))
                        .setLinkProperties(cm.getLinkProperties(net))
                        .build());
                return;
            }
        }

        displayError("Has default active network from the getActiveNetworkInfo(), no neither Network matches this one.");
    }



    private void displayNetworkItem(@NonNull NetworkItemModel netItem) {
        findViewById(R.id.txt_error).setVisibility(View.GONE);
        ItemNetworkBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_network, (ViewGroup) findViewById(R.id.content_container), true);
        binding.setNetItemModel(netItem);
    }

    private void displayError(String reason) {
        TextView tv = (TextView) findViewById(R.id.txt_error);
        tv.setText(reason);
        tv.setVisibility(View.VISIBLE);
    }
}
