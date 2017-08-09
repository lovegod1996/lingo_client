package com.lovegod.newbuy.view.goods;

import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daasuu.bl.BubbleLayout;
import com.example.ywx.lib.StarRating;
import com.github.chrisbanes.photoview.PhotoView;

import com.lovegod.newbuy.R;

import com.lovegod.newbuy.bean.Assess;

import com.lovegod.newbuy.view.ShowImageActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;


import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;


/**
 * *****************************************
 * Created by thinking on 2017/6/13.
 * 创建时间：
 * <p>
 * 描述：
 * <p/>
 * <p/>
 * *******************************************
 */

public class AssessRecyclerViewAdapter extends RecyclerView.Adapter<AssessRecyclerViewAdapter.AssessViewHolder> implements View.OnClickListener {
    public Context mContext;
    public List<Assess> mDatas;
    public LayoutInflater mLayoutInflater;
    String[] pics;
    /*对话框*/
    Dialog imageDialog;


    public AssessRecyclerViewAdapter(Context mContext, List<Assess> assesses) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mDatas = assesses;
    }

    @Override
    public AssessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = mLayoutInflater.inflate(R.layout.assess_item, parent, false);
        AssessViewHolder mViewHolder = new AssessViewHolder(mView);
        mView.setOnClickListener(this);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final AssessViewHolder holder, final int position) {
        //当前点击的图片列表
       final ArrayList<String>picList=new ArrayList<>();
        holder.assess_user_time.setText(String.valueOf(mDatas.get(position).getDate().substring(0,10)));
        holder.assess_text_view.setText(mDatas.get(position).getDetail());
        holder.overviewAssess.setText(mDatas.get(position).getHollrall());
        holder.assess_user_name.setText(newName(mDatas.get(position).getUsername()));
        holder.assess_user_num.setText("x" + mDatas.get(position).getCount());
        holder.assess_user_choice.setText(mDatas.get(position).getParam());
        holder.assess_ratingbar.setCurrentStarCount((int) mDatas.get(position).getGrade());
        if (mDatas.get(position).getBossback()!=null) {
            if (!mDatas.get(position).getBossback().equals("")) {
                holder.reply_bubblelayout.setVisibility(View.VISIBLE);
                holder.reply_textview.setText("店家回复:"+mDatas.get(position).getBossback());
            }
        }
        if (mDatas.get(position).getPics() == null) {
            holder.assessGridview.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getPics() != null) {
            pics = mDatas.get(position).getPics().split(";");

            //将字符串数组转变为字符串list
            for(int i=0;i<pics.length;i++){
                picList.add(pics[i]);
            }
            holder.assessGridview.setVisibility(View.VISIBLE);
            int size = pics.length;
            int length = 100;
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 4) * density);
            int itemWidth = (int) (length * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            holder.assessGridview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            holder.assessGridview.setColumnWidth(itemWidth);
            holder.assessGridview.setHorizontalSpacing(5); // 设置列表项水平间距
            holder.assessGridview.setStretchMode(GridView.NO_STRETCH);
            holder.assessGridview.setNumColumns(size); // 设置列数量=列表集合数
            MyHorizontalListAdapter HorizontalAdapter = new MyHorizontalListAdapter(pics, mContext);
            holder.assessGridview.setAdapter(HorizontalAdapter);
            holder.assessGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(mContext, ShowImageActivity.class);
                    intent.putStringArrayListExtra("pic_list",picList);
                    mContext.startActivity(intent);
                }
            });
        }



    }

    private String newName(String username) {
        String newname;
        int num=username.length();
        newname=username.charAt(0)+"**"+username.charAt(num-2)+username.charAt(num-1);
        return newname;
    }


    //对话框的点击事件
    private View.OnClickListener dialoglistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_diaog:
                    imageDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {

    }


    class AssessViewHolder extends RecyclerView.ViewHolder {
        TextView assess_user_name;
        TextView assess_user_time;
        TextView assess_user_choice;
        TextView assess_user_num;
        StarRating assess_ratingbar;
        TextView overviewAssess;
        ExpandableTextView assess_text_view;
        MyGridView assessGridview;
        CircleImageView assess_user_image;
        BubbleLayout reply_bubblelayout;
        TextView reply_textview;


        public AssessViewHolder(View itemView) {
            super(itemView);
            assess_user_name = (TextView) itemView.findViewById(R.id.assess_user_name);
            assess_user_time = (TextView) itemView.findViewById(R.id.assess_user_time);
            assess_user_choice = (TextView) itemView.findViewById(R.id.assess_user_choice);
            assess_user_num = (TextView) itemView.findViewById(R.id.assess_user_num);

            assess_ratingbar = (StarRating) itemView.findViewById(R.id.assess_ratingbar);
            assess_text_view = (ExpandableTextView) itemView.findViewById(R.id.assess_text_view);
            overviewAssess = (TextView) itemView.findViewById(R.id.overview_assess);
            assessGridview = (MyGridView) itemView.findViewById(R.id.assess_gridview);
            assess_user_image = (CircleImageView) itemView.findViewById(R.id.assess_user_image);
            reply_bubblelayout = (BubbleLayout) itemView.findViewById(R.id.reply_bubblelayout);
            reply_textview = (TextView) itemView.findViewById(R.id.reply_textview);
        }
    }

    /*HorizontalAdapter*/
    public class MyHorizontalListAdapter extends BaseAdapter {
        private String[] pics;
        private Context context;
        private LayoutInflater inflater;

        public MyHorizontalListAdapter(String[] pics, Context mContext) {
            this.context = mContext;
            this.pics = pics;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return pics.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(mContext, R.layout.assess_grid_pic, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            Glide.with(mContext).load(pics[position]).into(imageView);
            return convertView;
        }
    }
}