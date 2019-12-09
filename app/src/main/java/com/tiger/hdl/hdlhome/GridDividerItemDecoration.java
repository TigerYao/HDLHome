package com.tiger.hdl.hdlhome;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by _SOLID
 * Date:2016/10/8
 * Time:16:50
 * Desc:
 */


public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mDividerWidth;
    private int color;

    public GridDividerItemDecoration(int height, @ColorInt int color) {
        mDividerWidth = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        this.color = color;
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        boolean isLastRow = isLastRow(parent, itemPosition, spanCount, childCount);
        boolean isLastColumn = isLastColumn(parent, itemPosition, spanCount, childCount);
//        if(itemPosition!= 0 && String.valueOf(itemPosition).endsWith("3"))
//            mDividerWidth += mDividerWidth;
//        else
//            mDividerWidth = 10;
        int gap = 0;
        //画垂直分割线
        if(itemPosition > 0 && (itemPosition / 10) % 2 == 1 && String.valueOf(itemPosition-1).endsWith("9")){
            mPaint.setColor(Color.parseColor("#42a0e1"));
            gap = - 20;

        }
        int left;
        int right;
        int bottom;
        int eachWidth = (spanCount - 1) * mDividerWidth * 2 / spanCount;
        int dl = mDividerWidth - eachWidth;
        left = itemPosition % spanCount * dl + gap;
        right = eachWidth - left;
        bottom = mDividerWidth;
        //Log.e("zzz", "itemPosition:" + itemPosition + " |left:" + left + " right:" + right + " bottom:" + bottom);
        if (isLastRow) {
            bottom = 0;
        }
//        if(itemPosition > 0 && String.valueOf(itemPosition).endsWith("4")){
//            outRect.left = left;
//            outRect.right = eachWidth;
//            return;
//        }
        outRect.set(left, 0, right, bottom);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        draw(c, parent);
        super.onDraw(c, parent, state);
    }

    //绘制横向 item 分割线
    private void draw(Canvas canvas, RecyclerView parent) {
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            mPaint.setColor(color);

            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            //画水平分隔线
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerWidth;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

            int gap = 0;
//            //画垂直分割线
            if(i > 0 && (i / 10) % 2 == 1 && String.valueOf(i-1).endsWith("9")){
                mPaint.setColor(Color.parseColor("#42a0e1"));
                gap = 200;
            }
            if(i > 0 && String.valueOf(i).endsWith("4")){
                mPaint.setColor(Color.parseColor("#42a0e1"));
            }
            top = child.getTop();
            bottom = child.getBottom() + mDividerWidth;
            left = child.getRight() + layoutParams.rightMargin + gap;
            right = left + mDividerWidth;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRow(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            return lines == pos / spanCount + 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }
}