package com.tiger.hdl.hdlhome;

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
    private final List<DummyItem> mValues;

    SimpleItemRecyclerViewAdapter(LauncherActivity parent,
                                  List<DummyItem> items) {
        mValues = items;
        mParentActivity = parent;
    }

    @Override
    public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        view.setMinimumHeight((int) Math.floor(DisplayUtil.getScreenHeight(view.getContext()) / (int) Math.ceil(getItemCount() / 20f)) - 7);
        return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
//            holder.mIdView.setText(mValues.get(position).id);
        String value = String.valueOf(position);
        if (position != 0 && !value.contains("4") && !value.contains("7")) {
            holder.mContentView.setText(value);
//                holder.itemView.setTag(mValues.get(position));
//                holder.itemView.setOnClickListener(mOnClickListener);
        } else {
            holder.itemView.setBackgroundResource(R.color.gray_666666);
//                holder.itemView.getLayoutParams().width = holder.itemView.getWidth()/2;
        }

//            if (position > 0 && position % 3 == 0)
//                holder.itemView.setBackgroundColor(Color.BLUE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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