package com.lovegod.newbuy.view.goods;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Assess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lovegod.newbuy.MyApplication.getContext;
import static com.lovegod.newbuy.R.id.assess_image_gridview;
import static com.lovegod.newbuy.R.id.assess_user_name;
import static com.lovegod.newbuy.R.id.dialog_listview;
import static com.lovegod.newbuy.R.id.grid_image;
import static com.lovegod.newbuy.R.id.view;

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
    public void onBindViewHolder(AssessViewHolder holder, int position) {

        holder.assess_user_name.setText(String.valueOf(mDatas.get(position).getUid()));
        holder.assess_user_time.setText(String.valueOf(mDatas.get(position).getCid()));
        holder.assess_user_assess.setText(mDatas.get(position).getDetail());
        if (mDatas.get(position).getPics() == null) {
            holder.assess_image_gridview.setVisibility(View.GONE);
            Log.v("不是空的吗。。。。","kongde");
        }
       if (mDatas.get(position).getPics()!=null)
        {
            Log.v("不是空的。。。。","BUkongde");
            final String[] pics = mDatas.get(position).getPics().split(";");
            WindowManager wm = (WindowManager) getContext().getSystemService(mContext.WINDOW_SERVICE);

            final int width = wm.getDefaultDisplay().getWidth();
            final int height=wm.getDefaultDisplay().getHeight();
            GridAdapter GridAdapter = new GridAdapter(width, pics, mContext);
            holder.assess_image_gridview.setAdapter(GridAdapter);
            holder.assess_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //对话框
                    imageDialog = new Dialog(mContext,R.style.map_dialog);
                    RelativeLayout root = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.assess_dialog, null);
                    root.findViewById(R.id.re_diaog).setOnClickListener(dialoglistener);
                    ImageView dialog_image = (ImageView) root.findViewById(R.id.dialog_image);

                    Glide.with(mContext).load(pics[position]).into(dialog_image);
                    imageDialog.setContentView(root);
                    Window dialogWindow = imageDialog.getWindow();
                    dialogWindow.getDecorView().setPadding(0,0,0,0);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值

                    lp.width = width; // 宽度
                    lp.height =height;
                    imageDialog.getWindow().setAttributes(lp);
                    imageDialog.show();
                }
            });
        }

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
        TextView assess_user_assess;
        MyGridView assess_image_gridview;

        public AssessViewHolder(View itemView) {
            super(itemView);
            assess_user_name = (TextView) itemView.findViewById(R.id.assess_user_name);
            assess_user_time = (TextView) itemView.findViewById(R.id.assess_user_time);
            assess_user_choice = (TextView) itemView.findViewById(R.id.assess_user_choice);
            assess_user_assess = (TextView) itemView.findViewById(R.id.assess_user_assess);
            assess_image_gridview = (MyGridView) itemView.findViewById(R.id.assess_image_gridview);
        }
    }


    /**
     * GridView的适配器
     */
    private class GridAdapter extends BaseAdapter {
        private String[] gridData;
        private int width;
        private Context context;
        private LayoutInflater inflater;

        public GridAdapter(int width, String[] gridData, Context context) {

            this.gridData = gridData;
            this.width = width;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return gridData.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderGrid holer = null;
            View view = inflater.inflate(R.layout.assess_grid_pic, null);
            if (holer == null) {
                holer = new ViewHolderGrid();
                holer.grid_image = (ImageView) view.findViewById(R.id.grid_image);
                view.setTag(holer);

            } else {
                holer = (ViewHolderGrid) view.getTag();
            }

            Glide.with(context)
                    .load(gridData[position])
                    .into(holer.grid_image);
            return view;
        }

        //  适配器中的GridView缓存类
        class ViewHolderGrid {
            ImageView grid_image;
        }
    }
}
