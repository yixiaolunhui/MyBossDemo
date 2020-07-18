package com.zwl.mybossdemo.filter.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zwl.mybossdemo.R;

/**
 * 流布局
 * 1、是否折叠
 * 2、折叠行数
 * 3、对其方式
 * 4、平均宽度及数量
 * 5、水平竖直间隔
 */
public class FlowLayout extends ViewGroup {

    //默认折叠状态
    private static final boolean DEFAULT_FOLD = false;
    //折叠的行数
    private static final int DEFAULT_FOLD_LINES = 1;
    //左对齐
    private static final int DEFAULT_GRAVITY_LEFT = 0;
    //右对齐
    private static final int DEFAULT_GRAVITY_RIGHT = 1;

    //是否折叠，默认false不折叠
    private boolean mFold;
    //折叠行数
    private int mFoldLines = 1;
    //对齐 默认左对齐
    private int mGravity = DEFAULT_GRAVITY_LEFT;
    //折叠状态
    private Boolean mFoldState;
    //是否平均
    private boolean mEqually;
    //一行平局数量
    private int mEquallyCount;
    //水平距离
    private int mHorizontalSpacing;
    //竖直距离
    private int mVerticalSpacing;

    private OnFoldChangedListener mOnFoldChangedListener;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mFold = a.getBoolean(R.styleable.FlowLayout_flow_fold, DEFAULT_FOLD);
        mFoldLines = a.getInt(R.styleable.FlowLayout_flow_foldLines, DEFAULT_FOLD_LINES);
        mGravity = a.getInt(R.styleable.FlowLayout_flow_gravity, DEFAULT_GRAVITY_LEFT);
        mEqually = a.getBoolean(R.styleable.FlowLayout_flow_equally, true);
        mEquallyCount = a.getInt(R.styleable.FlowLayout_flow_equally_count, 0);
        mHorizontalSpacing = a.getDimensionPixelOffset(R.styleable.FlowLayout_flow_horizontalSpacing, dp2px(4));
        mVerticalSpacing = a.getDimensionPixelOffset(R.styleable.FlowLayout_flow_verticalSpacing, dp2px(4));
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //当设置折叠 折叠数设置小于0直接隐藏布局
        if (mFold && mFoldLines <= 0) {
            setVisibility(GONE);
            changeFold(true, true);
            return;
        }
        //获取mode 和 size
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int layoutWidth = widthSize - getPaddingLeft() - getPaddingRight();
        //判断如果布局宽度抛去左右padding小于0，也不能处理了
        if (layoutWidth <= 0) {
            return;
        }

        //这里默认宽高默认值默认把左右，上下padding加上
        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();

        //初始一行的宽度
        int lineWidth = 0;
        //初始一行的高度
        int lineHeight = 0;

        //测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int[] wh = null;
        int childWidth, childHeight;
        int childWidthMeasureSpec = 0, childHeightMeasureSpec = 0;
        //行数
        int line = 0;
        //折叠的状态
        boolean newFoldState = false;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            //这里需要先判断子view是否被设置了GONE
            if (view.getVisibility() == GONE) {
                continue;
            }

            //如果设置是平局显示
            if (mEqually) {
                //这里只要计算一次就可以了
                if (wh == null) {
                    //取子view最大的宽高
                    wh = getMaxWH();
                    //求一行能显示多少个
                    int oneRowItemCount = (layoutWidth + mHorizontalSpacing) / (mHorizontalSpacing + wh[0]);
                    //当你设置了一行平局显示多少个
                    if (mEquallyCount > 0) {
                        //判断当你设定的数量小于计算的数量时，使用设置的，所以说当我们计算的竖直小于设置的值的时候这里并没有强制设置设定的值
                        //如果需求要求必须按照设定的来，这里就不要做if判断，直接使用设定的值，但是布局显示会出现显示不全或者...的情况。
                        if (oneRowItemCount > mEquallyCount) {
                            oneRowItemCount = mEquallyCount;
                        }
                    }
                    // 根据上面计算的一行显示的数量来计算一个的宽度
                    int newWidth = (layoutWidth - (oneRowItemCount - 1) * mHorizontalSpacing) / oneRowItemCount;
                    wh[0] = newWidth;
                    //重新获取子view的MeasureSpec
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(wh[0], MeasureSpec.EXACTLY);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(wh[1], MeasureSpec.EXACTLY);
                }
                childWidth = wh[0];
                childHeight = wh[1];
                //重新测量子view的大小
                getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
            // 自适显示
            else {
                childWidth = view.getMeasuredWidth();
                childHeight = view.getMeasuredHeight();
            }
            if (i == 0) {//第一行
                lineWidth = getPaddingLeft() + getPaddingRight() + childWidth;
                lineHeight = childHeight;
            } else {
                //判断是否需要换行
                //换行
                if (lineWidth + mHorizontalSpacing + childWidth > widthSize) {
                    line++;//行数增加
                    width = Math.max(lineWidth, width);// 取最大的宽度
                    //这里判断是否设置折叠及行数是否超过了设定值
                    if (mFold && line >= mFoldLines) {
                        line++;
                        height += lineHeight;
                        newFoldState = true;
                        break;
                    }
                    //重新开启新行，开始记录
                    lineWidth = getPaddingLeft() + getPaddingRight() + childWidth;
                    //叠加当前高度，
                    height += mVerticalSpacing + lineHeight;
                    //开启记录下一行的高度
                    lineHeight = childHeight;
                }
                //不换行
                else {
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
        //根据计算的值重新设置
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
        //折叠状态
        changeFold(line > mFoldLines, newFoldState);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int layoutWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        if (layoutWidth <= 0) {
            return;
        }
        int childWidth, childHeight;
        //需要加上top padding
        int top = getPaddingTop();
        final int[] wh = getMaxWH();
        int lineHeight = 0;
        int line = 0;

        //左对齐
        if (mGravity == DEFAULT_GRAVITY_LEFT) {
            //左侧需要先加上左边的padding
            int left = getPaddingLeft();
            for (int i = 0, count = getChildCount(); i < count; i++) {
                final View view = getChildAt(i);
                //这里一样判断下显示状态
                if (view.getVisibility() == GONE) {
                    continue;
                }
                //如果设置的平均 就使用最大的宽度和高度 否则直接自适宽高
                if (mEqually) {
                    childWidth = wh[0];
                    childHeight = wh[1];
                } else {
                    childWidth = view.getMeasuredWidth();
                    childHeight = view.getMeasuredHeight();
                }
                //第一行开始摆放
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
                        //重新起行
                        left = getPaddingLeft();
                        top = top + mVerticalSpacing + lineHeight;
                        lineHeight = childHeight;
                    } else {
                        left = left + mHorizontalSpacing;
                        lineHeight = Math.max(lineHeight, childHeight);
                    }
                    view.layout(left, top, left + childWidth, top + childHeight);
                }
                //累加left
                left += childWidth;
            }
        }
        //右对齐
        else {
            int paddingLeft = getPaddingLeft();
            int right = layoutWidth + paddingLeft;// 相当于getMeasuredWidth() -  getPaddingRight();

            for (int i = 0, count = getChildCount(); i < count; i++) {
                final View view = getChildAt(i);
                if (view.getVisibility() == GONE) {
                    continue;
                }
                //如果设置的平均 就使用最大的宽度和高度 否则直接自适宽高
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
                        //重新起行
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

    /**
     * 取最大的子view的宽度和高度
     *
     * @return
     */
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

    /**
     * 折叠状态改变回调
     *
     * @param canFold
     * @param newFoldState
     */
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
     * dp->px
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp);
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
