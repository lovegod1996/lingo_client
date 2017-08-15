package com.lovegod.newbuy.view.myinfo.address;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.api.BaseObserver;
import com.lovegod.newbuy.api.NetWorks;
import com.lovegod.newbuy.bean.City;
import com.lovegod.newbuy.bean.Community;
import com.lovegod.newbuy.bean.District;
import com.lovegod.newbuy.bean.Province;
import com.lovegod.newbuy.bean.Town;
import com.lovegod.newbuy.view.search.MatchListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowAreaActivity extends AppCompatActivity {
    private static final int LEVEL_PROVINCE=1;
    private static final int LEVEL_CITY=2;
    private static final int LEVEL_COUNTY=3;
    private static final int LEVEL_TOWN=4;
    private static final int LEVEL_COMMUNITY=5;
    private int currentLevel;
    private ListView listView;
    private Toolbar toolbar;
    private MatchListAdapter adapter;
    private Province selectedProvince;
    private Town selectedTown;
    private City selectedCity;
    private District selectedDistrict;
    private List<String>dataList=new ArrayList<>();
    private List<Province>provinceList=new ArrayList<>();
    private List<District>districtList=new ArrayList<>();
    private List<City>cityList=new ArrayList<>();
    private List<Town>townList=new ArrayList<>();
    private List<Community>communityList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_area);
        listView=(ListView)findViewById(R.id.choose_area_listview);
        toolbar=(Toolbar)findViewById(R.id.choose_area_toolbar);
        setSupportActionBar(toolbar);
        adapter=new MatchListAdapter(this,dataList);
        listView.setAdapter(adapter);

        //首先获取所有省信息
        getAllprovince();

        /**
         * 列表项点击监听
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    getCityById(provinceList.get(position).getPid());
                    selectedProvince=provinceList.get(position);
                }else if(currentLevel==LEVEL_CITY){
                    getDistrictbyCid(cityList.get(position).getCid());
                    selectedCity=cityList.get(position);
                }else if(currentLevel==LEVEL_COUNTY){
                    getTownbyDid(districtList.get(position).getDid());
                    selectedDistrict=districtList.get(position);
                }else if(currentLevel==LEVEL_TOWN){
                    getCommunitybyTid(townList.get(position).getTid());
                    selectedTown=townList.get(position);
                }else if(currentLevel==LEVEL_COMMUNITY){
                    String result=selectedProvince.getProvince()+" "+selectedCity.getCityname()+" "+selectedDistrict.getDistrictname()
                            +";"+selectedTown.getTownsname()+" "+communityList.get(position).getCommunityname();
                    Intent intent=new Intent();
                    intent.putExtra("area_data_return",result);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        /**
         * 返回键监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_PROVINCE){
                    finish();
                }else if(currentLevel==LEVEL_CITY){
                    getAllprovince();
                }else if(currentLevel==LEVEL_COUNTY){
                    getCityById(selectedProvince.getPid());
                }else if(currentLevel==LEVEL_TOWN){
                    getDistrictbyCid(selectedCity.getCid());
                }else if(currentLevel==LEVEL_COMMUNITY){
                    getTownbyDid(selectedDistrict.getDid());
                }
            }
        });
    }

    /**
     * 获取省信息
     */
    private void getAllprovince(){
        currentLevel=LEVEL_PROVINCE;
        NetWorks.getAllProvince(new BaseObserver<List<Province>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Province> provinces) {
                dataList.clear();
                provinceList=provinces;
                for(Province province:provinces){
                    dataList.add(province.getProvince());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Province> provinces) {

            }
        });
    }

    /**
     * 通过pid获取市信息
     * @param pid 省的Id
     */
    private void getCityById(int pid){
        currentLevel=LEVEL_CITY;
        NetWorks.getCity(pid, new BaseObserver<List<City>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<City> cities) {
                dataList.clear();
                cityList=cities;
                for(City city:cities){
                    dataList.add(city.getCityname());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<City> cities) {

            }
        });
    }

    /**
     * 通过cid获取县区信息
     * @param cid 市的id
     */
    private void getDistrictbyCid(int cid){
        currentLevel=LEVEL_COUNTY;
        NetWorks.getDistrict(cid, new BaseObserver<List<District>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<District> districts) {
                dataList.clear();
                districtList=districts;
                for(District district:districts){
                    dataList.add(district.getDistrictname());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<District> districts) {

            }
        });
    }

    /**
     * 通过did获取街道/镇信息
     * @param did 区县id
     */
    private void getTownbyDid(int did){
        currentLevel=LEVEL_TOWN;
        NetWorks.getTown(did, new BaseObserver<List<Town>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Town> towns) {
                dataList.clear();
                townList=towns;
                for(Town town:towns){
                    dataList.add(town.getTownsname());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Town> towns) {

            }
        });
    }

    /**
     * 根据tid获取村委会/居委会信息
     * @param tid 街道id
     */
    private void getCommunitybyTid(int tid){
        currentLevel=LEVEL_COMMUNITY;
        NetWorks.getCommunity(tid, new BaseObserver<List<Community>>(this,new ProgressDialog(this)) {
            @Override
            public void onHandleSuccess(List<Community> communities) {
                dataList.clear();
                communityList=communities;
                for(Community community:communities){
                    dataList.add(community.getCommunityname());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHandleError(List<Community> communities) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(currentLevel==LEVEL_PROVINCE){
            finish();
        }else if(currentLevel==LEVEL_CITY){
            getAllprovince();
        }else if(currentLevel==LEVEL_COUNTY){
            getCityById(selectedProvince.getPid());
        }else if(currentLevel==LEVEL_TOWN){
            getDistrictbyCid(selectedCity.getCid());
        }else if(currentLevel==LEVEL_COMMUNITY){
            getTownbyDid(selectedDistrict.getDid());
        }
    }
}
