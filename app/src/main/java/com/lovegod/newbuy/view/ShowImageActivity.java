package com.lovegod.newbuy.view;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lovegod.newbuy.R;
import com.lovegod.newbuy.commen.Commen;
import com.lovegod.newbuy.view.fragment.PictureSlideFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowImageActivity extends AppCompatActivity {
    @BindView(R.id.image_viewpager)
    ViewPager imageViewpager;
    @BindView(R.id.page_text)
    TextView pageText;
    // private String[] imageurl;
    private ArrayList<String> imagelist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        imagelist=new ArrayList<>();
        imagelist=getIntent().getStringArrayListExtra("pic_list");


        imageViewpager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        imageViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pageText.setText(String.valueOf(position+1)+"/"+imagelist.size());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private  class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(imagelist.get(position));
        }

        @Override
        public int getCount() {
            return imagelist.size();
        }
    }
}
