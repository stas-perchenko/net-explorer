package com.alperez.androidnetexplorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alperez.androidnetexplorer.fragment.ConnManagerFragment;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.content_container, new ConnManagerFragment(), "fragment_conn_manager").commit();
    }

}
