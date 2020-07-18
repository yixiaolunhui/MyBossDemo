package com.zwl.mybossdemo.filter.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zwl.mybossdemo.R;


public class FlowLayout extends ViewGroup {

    private static final boolean DEFAULT_FOLD = false;
    private static final int DEFAULT_FOLD_LINES = 1;
    private static final int DEFAULT_GRAVITY_EFT = 0;

    private boolean mFold;//是否折叠，默认false不折叠
    private int mFoldLines = 1;//折叠行数
    private int mGravity = DEFAULT_GRAVITY_EFT;
    private Boolean mFoldState;//折叠状态
    private boolean mEqually;
    private int mEquallyCount;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    private OnFoldChangedListener mOnFoldChangedListener;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mFold = a.getBoolean(R.styleable.FlowLayout_flow_fold, DEFAULT_FOLD);
        mFoldLines = a.getInt(R.styleable.FlowLayout_flow_foldLines, DEFAULT_FOLD_LINES);
        mGravity = a.getInt(R.styleable.FlowLayout_flow_gravity, DEFAULT_GRAVITY_EFT);
        mEqually = a.getBoolean(R.styleable.FlowLayout_flow_equally, true);
        mEquallyCount = a.getInt(R.styleable.FlowLayout_flow_equally_count, 0);
        mHorizontalSpacing = a.getDimensionPixelOffset(R.styleable.FlowLayout_flow_horizontalSpacing, dp2px(4));
        mVerticalSpacing = a.getDimensionPixelOffset(R.styleable.FlowLayout_flow_verticalSpacing, dp2px(4));
        a.recycle();
    }

    private int dp2px(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mFold && mFoldLines <= 0) {
            setVisibility(GONE);
            changeFold(true, true);
            return;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int layoutWidth = widthSize - getPaddingLeft() - getPaddingRight();
        if (layoutWidth <= 0) {
            return;
        }

        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();
        int lineWidth = 0;
        int lineHeight = 0;

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int[] wh = null;
        int childWidth, childHeight;
        int childWidthMeasureSpec = 0, childHeightMeasureSpec = 0;
        int line = 0;
        boolean newFoldState = false;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            final View view = getChildAt(i);
            if (view.getVisibility() == GONE) {
                continue;
            }
            if (mEqually) {
                if (wh == null) {
                    //重新计算，平均分配
                    wh = getMaxWH();
                    int oneRowItemCount = (layoutWidth + mHorizontalSpacing) / (mHorizontalSpacing + wh[0]);
                    if (mEquallyCount > 0) {
                        //填充
                        if (oneRowItemCount > mEquallyCount) {
                            oneRowItemCount = mEquallyCount;
                        }
                    }
                    int newWidth = (layoutWidth - (oneRowItemCount - 1) * mHorizontalSpacing) / oneRowItemCount;
                    wh[0] = newWidth;

                    Log.e("123123","wh[0]="+wh[0]);

                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(wh[0], MeasureSpec.EXACTLY);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(wh[1], MeasureSpec.EXACTLY);
                }
                childWidth = wh[0];
                childHeight = wh[1];
                getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
            } else {
                childWidth = view.getMeasuredWidth();
                childHeight = view.getMeasuredHeight();
            }
            if (i == 0) {
                lineWidth = getPaddingLeft() + getPaddingRight() + childWidth;
                lineHeight = childHeight;
            } else {
                //判断是否需要换行
                if (lineWidth + mHorizontalSpacing + childWidth > widthSize) {
                    line++;
                    width = Math.max(lineWidth, width);// 取最大的宽度
                    if (mFold && line >= mFoldLines) {
                        line++;
                        height += lineHeight;
                        newFoldState = true;
                        break;
                    }
                    lineWidth = getPaddingLeft() + getPaddingRight() + childWidth; // 重新开启新行，开始记录
                    // 叠加当前高度，
                    height += mVerticalSpacing + lineHeight;
                    // 开启记录下一行的高度
                    lineHeight = childHeight;
                } else {
                    lineWidth = lineWidth + mHorizontalSpacing + childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == count - 1) {
                line++;
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);

        changeFold(line > mFoldLines, newFoldState);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int layoutWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        if (layoutWidth <= 0) {
            return;
        }
        int childWidth, childHeight;
        int top = getPaddingTop();
        final int[] wh = getMaxWH();
        int lineHeight = 0;
        int line = 0;


        if (mGravity == DEFAULT_GRAVITY_EFT) {
            int left = getPaddingLeft();
            for (int i = 0, count = getChildCount(); i < count; i++) {
                final View view = getChildAt(i);
                if (view.getVisibility() == GONE) {
                    continue;
                }
                if (mEqually) {
                    childWidth = wh[0];
                    childHeight = wh[1];
                } else {
                    childWidth = view.getMeasuredWidth();
                    childHeight = view.getMeasuredHeight();
                }
                if (i == 0) {
                    view.layout(left, top, left + childWidth, top + childHeight);
                    lineHeight = childHeight;
                } else {
                    //判断是否需要换行
                    if (left + mHorizontalSpacing + childWidth > layoutWidth + getPaddingLeft()) {
                        line++;
                        if (mFold && line >= mFoldLines) {
                            break;
                        }
                        left = getPaddingLeft();
                        top = top + mVerticalSpacing + lineHeight;
                        lineHeight = childHeight;
                    } else {
                        left = left + mHorizontalSpacing;
                        lineHeight = Math.max(lineHeight, childHeight);
                    }
                    view.layout(left, top, left + childWidth, top + childHeight);
                }
                left += childWidth;
            }
        } else {
            int paddingLeft = getPaddingLeft();
            int right = layoutWidth + paddingLeft;

            for (int i = 0, count = getChildCount(); i < count; i++) {
                final View view = getChildAt(i);
                if (view.getVisibility() == GONE) {
                    continue;
                }
                if (mEqually) {
                    childWidth = wh[0];
                    childHeight = wh[1];
                } else {
                    childWidth = view.getMeasuredWidth();
                    childHeight = view.getMeasuredHeight();
                }
                if (i == 0) {
                    view.layout(right - childWidth, top, right, top + childHeight);
                    lineHeight = childHeight;
                } else {
                    //判断是否需要换行
                    if (right - childWidth - mHorizontalSpacing < paddingLeft) {
                        line++;
                        if (mFold && line >= mFoldLines) {
                            break;
                        }
                        right = layoutWidth + paddingLeft;
                        top = top + mVerticalSpacing + lineHeight;
                        lineHeight = childHeight;
                    } else {
                        right = right - mHorizontalSpacing;
                        lineHeight = Math.max(lineHeight, childHeight);
                    }
                    view.layout(right - childWidth, top, right, top + childHeight);
                }
                right -= childWidth;
            }
        }


    }

    private int[] getMaxWH() {
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            final View view = getChildAt(i);
            if (view.getVisibility() == GONE) {
                continue;
            }
            maxWidth = Math.max(maxWidth, view.getMeasuredWidth());
            maxHeight = Math.max(maxHeight, view.getMeasuredHeight());
        }
        return new int[]{maxWidth, maxHeight};
    }

    private void changeFold(boolean canFold, boolean newFoldState) {
        if (mFoldState == null || mFoldState != newFoldState) {
            if (canFold) {
                mFoldState = newFoldState;
            }
            if (mOnFoldChangedListener != null) {
                mOnFoldChangedListener.onFoldChanged(canFold, newFoldState);
            }
        }
        if (mOnFoldChangedListener != null) {
            mOnFoldChangedListener.onFoldChanging(canFold, newFoldState);
        }
    }

    /**
     * 设置是否折叠
     *
     * @param fold
     */
    public void setFold(boolean fold) {
        mFold = fold;
        if (mFoldLines <= 0) {
            setVisibility(fold ? GONE : VISIBLE);
            changeFold(true, fold);
        } else {
            requestLayout();
        }
    }

    /**
     * 折叠切换，如果之前是折叠状态就切换为未折叠状态，否则相反
     */
    public void toggleFold() {
        setFold(!mFold);
    }

    /**
     * 设置折叠状态回调
     *
     * @param listener
     */
    public void setOnFoldChangedListener(OnFoldChangedListener listener) {
        mOnFoldChangedListener = listener;
    }

    public interface OnFoldChangedListener {
        /**
         * 折叠状态回调
         *
         * @param canFold 是否可以折叠，true为可以折叠，false为不可以折叠
         * @param fold    当前折叠状态，true为折叠，false为未折叠
         */
        void onFoldChanged(boolean canFold, boolean fold);


        /**
         * 折叠状态时时回调
         *
         * @param canFold 是否可以折叠，true为可以折叠，false为不可以折叠
         * @param fold    当前折叠状态，true为折叠，false为未折叠
         */
        void onFoldChanging(boolean canFold, boolean fold);
    }
}
