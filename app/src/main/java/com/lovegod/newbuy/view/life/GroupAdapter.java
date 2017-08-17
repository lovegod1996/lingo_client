package com.lovegod.newbuy.view.life;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.view.carts.ShopCartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywx on 2017/8/16.
 * 群组的列表的适配器
 */

public class GroupAdapter extends RecyclerView.Adapter {
    private static final int SEARCH_TYPE=0;
    private static final int ADD_TYPE=1;
    private static final int NORMAL_TYPE=2;
    private List<EMChatRoom> groups=new ArrayList<>();
    private Context mContext;
    private ShopCartAdapter.OnItemClickListener onItemClickListener;

    public GroupAdapter(Context context, List<EMChatRoom>list){
        groups=list;
        mContext=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case SEARCH_TYPE:
                view= LayoutInflater.from(mContext).inflate(R.layout.group_search_item,parent,false);
                SearchViewHolder searchViewHolder=new SearchViewHolder(view);
                return searchViewHolder;
            case ADD_TYPE:
                view= LayoutInflater.from(mContext).inflate(R.layout.group_add_item,parent,false);
                AddViewHolder addViewHolder=new AddViewHolder(view);
                return addViewHolder;
            case NORMAL_TYPE:
                view= LayoutInflater.from(mContext).inflate(R.layout.group_item,parent,false);
                ItemViewHolder itemViewHolder=new ItemViewHolder(view);
                return itemViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)){
            case SEARCH_TYPE:
                final SearchViewHolder searchViewHolder= (SearchViewHolder) holder;
                searchViewHolder.query.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                searchViewHolder.clearSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchViewHolder.query.getText().clear();
                    }
                });
                break;
            case ADD_TYPE:
                AddViewHolder addViewHolder= (AddViewHolder) holder;
                addViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener!=null){
                            onItemClickListener.onItemClick(v,position);
                        }
                    }
                });
                break;
            case NORMAL_TYPE:
                EMChatRoom room=groups.get(position-2);
                ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
                itemViewHolder.groupName.setText(room.getName());
                break;
        }
    }

    @Override
    public int getItemCount() {
        //列表个数等于聊天室个数加上一个添加item一个搜索item
        return groups.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return SEARCH_TYPE;
        } else if (position == 1) {
            return ADD_TYPE;
        } else{
            return NORMAL_TYPE;
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        public AddViewHolder(View itemView) {
            super(itemView);
            layout=(RelativeLayout)itemView.findViewById(R.id.add_group_layout);
        }
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        EditText query;
        ImageButton clearSearch;
        public SearchViewHolder(View itemView) {
            super(itemView);
            query=(EditText)itemView.findViewById(R.id.group_search_query);
            clearSearch=(ImageButton)itemView.findViewById(R.id.search_clear);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            groupName=(TextView)itemView.findViewById(R.id.group_name);
        }
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
