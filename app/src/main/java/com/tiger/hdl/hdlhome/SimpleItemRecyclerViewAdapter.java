package com.tiger.hdl.hdlhome;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tiger.hdl.hdlhome.dummy.DummyItem;
import com.tiger.hdl.hdlhome.utils.DisplayUtil;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final LauncherActivity mParentActivity;
    private List<DummyItem> mValues;
    int realIndex = 0;
    int realWidh = 0;
    int realHeight = 0;

    SimpleItemRecyclerViewAdapter(LauncherActivity parent,
                                  List<DummyItem> items) {
        mValues = items;
        mParentActivity = parent;
        realIndex = 0;
        realWidh = DisplayUtil.realWidh;
        realHeight = DisplayUtil.realHeight;
    }

    public void setValues(List<DummyItem> items) {
        mValues = items;
        realIndex = 0;
        notifyDataSetChanged();
    }

    @Override
    public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        view.getLayoutParams().height = realHeight;
        view.getLayoutParams().width = realWidh;
        if (viewType == 1)
            view.getLayoutParams().width = realWidh/2;
        return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        String value = String.valueOf(position);
        if (position != 0 && (value.endsWith("4") || value.endsWith("7")))
            return 1;
        return 0;
    }

    @Override
    public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
        String value = String.valueOf(position);
        holder.mBgView.setBackgroundResource(R.color.gray_7f7f7f);
        if(mValues == null || mValues.size() == 0){
            holder.mBgView.setBackgroundResource(R.color.gray_7f7f7f);
        } else if (position != 0 && !value.contains("4") && !value.contains("7")) {
            if (mValues != null && realIndex < mValues.size()) {
                DummyItem dummyItem = mValues.get(realIndex);
                String name = dummyItem.did;
                holder.mContentView.setTextColor(ContextCompat.getColor(holder.mContentView.getContext(),dummyItem.status == 3? R.color.black : R.color.white));
                if(((TextUtils.isDigitsOnly(name) && Integer.parseInt(name) > 199) || !TextUtils.isDigitsOnly(name)) && position > 199) {
                    realIndex += 1;
                    holder.mContentView.setText(dummyItem.did);
                    holder.mBgView.setBackgroundResource(dummyItem.getColorId());
                }else if(TextUtils.isDigitsOnly(name) && Integer.parseInt(name) <= 199  && position == Integer.parseInt(name)){
                    realIndex += 1;
                    holder.mBgView.setBackgroundResource(dummyItem.getColorId());
                    holder.mContentView.setText(dummyItem.did);
                }
            }
        } else {
            holder.mBgView.setBackgroundResource(R.color.gray_7f7f7f);
        }
    }

    @Override
    public int getItemCount() {
        return 220;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        final TextView mIdView;
        final TextView mContentView;
        final View mBgView;

        ViewHolder(View view) {
            super(view);
//            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.id_text);
            mBgView = view.findViewById(R.id.bg);
        }
    }
}