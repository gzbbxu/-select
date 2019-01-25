package boost.zkk.com.toucheevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("zhouke","dispatchTouchEvent action-- > "+event.getAction() +   "  view = button");
        return super.dispatchTouchEvent(event);
//        return false; // 默认dispatchTouchEvent 返回true 。 交给父控件, 自己不消耗down 之后的事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("zhouke","onTouchEvent action --> "+event.getAction() + "  view = button");
          return super.onTouchEvent(event);
//        return true; //返回false 交给父控件, 自己不消耗down 之后的事件
    }
}
