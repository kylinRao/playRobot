package jjj.demo.playRobotBendizhao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.io.OutputStream;

public class MainActivity extends Activity implements OnClickListener {
    Boolean isFloatOpened;
    Intent floatSeviceIntent;
    OutputStream os;
    Boolean floatRuningStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isFloatOpened = Boolean.FALSE;
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        CommonTools.setsW(mWindowManager.getDefaultDisplay().getWidth());
        CommonTools.setsH(mWindowManager.getDefaultDisplay().getHeight());
        CommonTools.initApk(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mian);
        findViewById(R.id.main_btn_show).setOnClickListener(this);
        findViewById(R.id.main_btn_hide).setOnClickListener(this);
        floatSeviceIntent = new Intent(this, FloatWindowService.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_not_login:
                if (!isFloatOpened) {

                    startService(new Intent(this, FloatWindowService.class));
                    isFloatOpened = Boolean.TRUE;
                    CommonTools.isNeedLogin = Boolean.TRUE;
                    new playRobotAsync().execute();


                }

                break;
            case R.id.main_btn_show:
                if (!isFloatOpened) {

                    startService(new Intent(this, FloatWindowService.class));
                    isFloatOpened = Boolean.TRUE;
                    CommonTools.isNeedLogin = Boolean.FALSE;
                    new playRobotAsync().execute();


                }

                break;
            case R.id.main_btn_hide:
                stopService(new Intent(this, FloatWindowService.class));
                this.finish();

                break;
        }

    }

    private void initWindowParams() {
        wmParams = new LayoutParams();
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;// 特别注意在这里设置等级为系统通话界面
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = 30;
        wmParams.height = 30;
    }

    View floatView;
    WindowManager mWindowManager;
    WindowManager.LayoutParams wmParams;
    int sW;
    int sH;
}

class playRobotAsync extends AsyncTask {
    //上面的方法中，第一个参数：网络图片的路径，第二个参数的包装类：进度的刻度，第三个参数：任务执行的返回结果

    @Override
    protected Object doInBackground(Object[] objects) {

        String packNameToStart = "com.bdvtt.www";
        String classToStart = "com.uzmap.pkg.EntranceActivity";
        while (Boolean.TRUE) {

            CommonTools.touTiaoTAB((int) (Math.random() * 11),packNameToStart,classToStart);

        }
        return null;
    }

    //在界面上显示进度条
    protected void onPreExecute() {

    };
    protected void doInBackground() {  //三个点，代表可变参数

    }

}