package com.lovegod.newbuy.view.myinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lovegod.newbuy.R;

public class AddAssessActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assess);
        toolbar=(Toolbar)findViewById(R.id.add_assess_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.add_assess_recyclerview);
        setSupportActionBar(toolbar);

        LinearLayoutManager manager=new LinearLayoutManager(this);
    }
}
