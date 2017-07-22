package com.lovegod.newbuy.view.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.lovegod.newbuy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索过滤列表适配器，实现Filterable接口完成自定义的过滤器
 * Created by ywx on 2017/7/14.
 */

public class MatchListAdapter extends BaseAdapter implements Filterable{
    private MatchListFilter filter;
    private List<String>originList=new ArrayList<>();
    private List<String>dataList=new ArrayList<>();
    private Context mContext;
    public MatchListAdapter(Context context,List<String>list){
        originList=list;
        dataList=list;
        mContext=context;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String itemText=dataList.get(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.match_list_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.matchItemText=(TextView)view.findViewById(R.id.match_item_text);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.matchItemText.setText(itemText);
        return view;
    }

    /**
     * 获取适配器的Filter对象
     * @return 返回Filter对象
     */
    @Override
    public Filter getFilter(){
        if(filter==null){
            filter=new MatchListFilter();
        }
        return filter;
    }

    class ViewHolder{
        TextView matchItemText;
    }

    //自定义Fileter接口
    class MatchListFilter extends Filter{

        /**
         * 根据传入的当前字符序列进行筛选
         * @param constraint 当前的字符序列
         * @return 放回一个结果类
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> newValues=new ArrayList<>();
            String filterString=constraint.toString().trim();
            if(TextUtils.isEmpty(filterString)){
                newValues=originList;
            }else {
                for(String str:originList){
                    if(str.contains(filterString)){
                        newValues.add(str);
                    }
                }
            }
            //values是一个Object类型，用来接收过滤后的数据
            results.values=newValues;
            //count是int类型，可以用来接收过滤后的数据数量
            results.count=newValues.size();
            return results;
        }

        /**
         * 用于根据过滤后的FilterResults来进行数据的更新
         * 这里让过滤后的数据给了适配器数组，然后更新了列表数据
         * @param constraint 当前字符序列
         * @param results 经过performFiltering过滤后的值
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList= (List<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
