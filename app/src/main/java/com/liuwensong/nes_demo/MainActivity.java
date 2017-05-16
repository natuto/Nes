package com.liuwensong.nes_demo;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuwensong.nes_demo.fragment.ParentFragmen;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private ParentFragmen parentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transaction = getSupportFragmentManager().beginTransaction();
        parentFragment = new ParentFragmen();
        transaction.add(R.id.fl_content,parentFragment).commit();

    }
}
