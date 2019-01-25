package boost.zkk.com.toucheevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import boost.zkk.com.toucheevent.R;

/**
 * Created by zkk on 2017/9/1.
 */

public class SelectSpan extends RelativeLayout {
    private String TAG = "TimeSpan";
    private int prevRawX = 0;
    private boolean mCanMoveHandle = false;

    //
    private int mHandleWidth = 0;
    private long mInPoint = 0;
    private long mOutPoint = 0;
    private double mPixelPerMicrosecond = 0D;
    private boolean hasSelected = true;
    private int mTotalWidth = 0;
    //
    private long minDraggedTimeSpanDuration = 1000000;
    private static final float TIMEBASE = 1000000f;
    private int minDraggedTimeSpanPixel = 0;
    private int originLeft = 0;
    private int originRight = 0;
    private int dragDirection = 0;
    private static final int LEFT = 0x16;
    private static final int CENTER = 0x17;
    private static final int RIGHT = 0x18;

    private View timeSpanshadowView;

    public SelectSpan(Context context){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.timespan, this);
        ImageView leftHandleView = (ImageView) view.findViewById(R.id.leftHandle);
        mHandleWidth = leftHandleView.getLayoutParams().width;

        timeSpanshadowView = view.findViewById(R.id.timeSpanShadow);
        init();
    }

    private void init() {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        mTotalWidth = screenWidth;
        mPixelPerMicrosecond = screenWidth / 20 / TIMEBASE;
    }


    public SelectSpan(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        View view = LayoutInflater.from(context).inflate(R.layout.timespan, this);
        ImageView leftHandleView = (ImageView) view.findViewById(R.id.leftHandle);
        mHandleWidth = leftHandleView.getLayoutParams().width;

        timeSpanshadowView = view.findViewById(R.id.timeSpanShadow);
        init();
    }

    public View getTimeSpanshadowView(){return timeSpanshadowView;}
    public long getInPoint() {
        return mInPoint;
    }

    public void setInPoint(long mInPoint) {
        this.mInPoint = mInPoint;
    }


    public long getOutPoint() {
        return mOutPoint;
    }

    public void setOutPoint(long mOutPoint) {
        this.mOutPoint = mOutPoint;
    }

    public void setPixelPerMicrosecond(double mPixelPerMicrosecond) {
        this.mPixelPerMicrosecond = mPixelPerMicrosecond;
    }

    public int getHandleWidth() {
        return mHandleWidth;
    }
    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }
    public void setTotalWidth(int mTotalWidth) {
        this.mTotalWidth = mTotalWidth;
    }
    private void updateTimeSpan(){
        ImageView mLeftView = (ImageView) findViewById(R.id.leftHandle);
        ImageView mRightView = (ImageView) findViewById(R.id.rightHandle);
        if(mLeftView == null || mRightView == null){
            return;
        }

        if(hasSelected){
            mLeftView.setVisibility(View.VISIBLE);
            mRightView.setVisibility(View.VISIBLE);
        }else {
            mLeftView.setVisibility(View.INVISIBLE);
            mRightView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!hasSelected){

            return false;//未被选中，不作编辑操作
        }

        minDraggedTimeSpanPixel = (int) Math.floor(minDraggedTimeSpanDuration * mPixelPerMicrosecond + 0.5D);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCanMoveHandle = !(mHandleWidth < ev.getX() && ev.getX() < getWidth() - mHandleWidth);
                getParent().requestDisallowInterceptTouchEvent(true);
                originLeft = getLeft();
                originRight = getRight();

                prevRawX = (int) ev.getRawX();
                dragDirection = getDirection((int) ev.getX(), (int) ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                int tempRawX = (int)ev.getRawX();

                int dx = tempRawX - prevRawX;
                prevRawX = tempRawX;
                if(dragDirection == LEFT){
                    left(dx);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
                    lp.width = originRight - originLeft;
                    Log.i("zhouke","左按钮>> originLeft " +originLeft + " mTotalWidth - originRight = " + (mTotalWidth - originRight));
                    lp.setMargins(originLeft, RelativeLayout.LayoutParams.MATCH_PARENT, mTotalWidth - originRight , 0);
                    setLayoutParams(lp);
                    mInPoint = (long) Math.floor(originLeft / mPixelPerMicrosecond + 0.5D);
                }
                if(dragDirection == RIGHT){
                    right(dx);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
                    lp.width = originRight - originLeft;

                    lp.setMargins(originLeft, RelativeLayout.LayoutParams.MATCH_PARENT, mTotalWidth - originRight , 0);
                    setLayoutParams(lp);
                    mOutPoint = (long) Math.floor((originRight - 2 * mHandleWidth) / mPixelPerMicrosecond + 0.5D);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return mCanMoveHandle;
    }




    public interface OnTrimInChangeListener {
        void onChange(long timeStamp,boolean isDragEnd);
    }

    public interface OnTrimOutChangeListener {
        void onChange(long timeStamp,boolean isDragEnd);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateTimeSpan();
    }

    private int getDirection( int x, int y) {
        int left = getLeft();
        int right = getRight();

        if (x < mHandleWidth) {
            return LEFT;
        }
        if (right - left - x < mHandleWidth) {
            return RIGHT;
        }
        return CENTER;
    }
    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        originRight += dx;
        Log.i("zhouke","right >> dx >> "+ dx + ">> minDraggedTimeSpanPixel "+ minDraggedTimeSpanPixel);
        if (originRight > mTotalWidth ) {
            originRight = mTotalWidth;
        }
        if (originRight - originLeft - 2 * mHandleWidth  < minDraggedTimeSpanPixel) {
            originRight = originLeft  + minDraggedTimeSpanPixel + 2 * mHandleWidth;
        }
    }

    /**
     * 触摸点为左边缘
     */
    private void left(int dx) {
        Log.i("zhouke","left >> dx >> "+ dx + ">> minDraggedTimeSpanPixel "+ minDraggedTimeSpanPixel);
        originLeft += dx;
        if (originLeft < 0) {
            originLeft = 0;
        }
        if (originRight - originLeft - 2 * mHandleWidth  < minDraggedTimeSpanPixel) {
            Log.i("zhouke","小于最小");
            originLeft = originRight - 2 * mHandleWidth - minDraggedTimeSpanPixel;
        }
    }
}
