package com.lovegod.newbuy.view.myinfo.track;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.Track;
import com.lovegod.newbuy.bean.User;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.utils.view.AdapterWrapper;
import com.lovegod.newbuy.view.TimeStickyDecoration;

import java.util.ArrayList;
import java.util.List;

public class Track_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Track>trackList=new ArrayList<>();
    private List<String>timeList=new ArrayList<>();
    private TrackAdapter adapter;
    private AdapterWrapper wrapper;
    private int currentPage;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        user=(User) SpUtils.getObject(this,"userinfo");
        recyclerView=(RecyclerView)findViewById(R.id.track_recyclerview);

        //初始化布局管理器以及适配器
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter=new TrackAdapter(this,trackList);
        RelativeLayout emptyLayout= (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.no_data_layout,null);
        //包装适配器
        wrapper=new AdapterWrapper(this,adapter,emptyLayout);
        recyclerView.setAdapter(wrapper);
        recyclerView.addItemDecoration(new TimeStickyDecoration(this,timeList,wrapper));
        currentPage=0;

        //获取足迹数据
        getTrack();
    }

    /**
     * 获取足迹
     */
    private void getTrack(){
        NetWorks.getAllTrack(user.getUid(), currentPage, new BaseObserver<List<Track>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Track> tracks) {
                currentPage++;
                for(Track track:tracks){
                    trackList.add(track);
                    timeList.add(track.getCordtime().substring(0,10));
                }
                wrapper.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Track> tracks) {

            }
        });
    }
}
