package boost.zkk.com.toucheevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    RelativeLayout rl_layout;
    MyButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_layout = findViewById(R.id.rl_layout);
        btn = findViewById(R.id.btn);
        rl_layout.setOnTouchListener(this);
        btn.setOnTouchListener(this);

        rl_layout.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("zhouke","OnTouchListener:action-- " + event.getAction() + " --v:  " +v);
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.i("zhouke","OnCLikListener:--- " +v);
    }
}
