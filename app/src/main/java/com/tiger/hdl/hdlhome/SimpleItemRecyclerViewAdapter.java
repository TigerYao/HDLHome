package com.tiger.hdl.hdlhome;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        if (position != 0 && !value.contains("4") && !value.contains("7")) {
            if (mValues != null && realIndex < mValues.size()) {
                DummyItem dummyItem = mValues.get(realIndex);
                realIndex += 1;
                int color = dummyItem.getColor();
                holder.mContentView.setText(dummyItem.did);
                holder.itemView.setBackgroundColor(color);
            }
        } else {
            holder.itemView.setBackgroundResource(R.color.gray_666666);
        }
    }

    @Override
    public int getItemCount() {
        return 220;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}