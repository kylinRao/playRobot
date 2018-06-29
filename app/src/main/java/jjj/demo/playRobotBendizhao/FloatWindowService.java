package jjj.demo.playRobotBendizhao;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.List;

public class FloatWindowService extends Service {
    OutputStream os;
    String classToStart;
    Boolean floatRuningStatus;
    String packNameToStart;
    View floatView;
    WindowManager mWindowManager;
    LayoutParams wmParams;
    int sW;
    int sH;
    EatBeanMan eatBeanMan;
    LayoutParams eatParams;
    boolean hasAddEat = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO 自动生成的方法存根
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO 自动生成的方法存根
        createFloatView();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public String getAPhoneToLogin() {
        String[] phoneLists = CommonTools.phoneLists.split(";");
        return phoneLists[(int) (Math.random() * phoneLists.length)];
    }

    private void sleepMs(int ms) {
        //线程睡眠ms毫秒
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行shell指令
     *
     * @param cmd 指令
     */
    public void exec(String cmd) {
        Toast.makeText(getApplicationContext(), cmd, Toast.LENGTH_SHORT).show();

        try {
            if (os == null) {
                os = Runtime.getRuntime().exec("su").getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void tapXY(int x, int y) {
        exec("input tap " + x + " " + y + "\n");
        Log.d("tapXY", "toutiaoGongZhongHao: " + "input tap " + x + " " + y + "\n");
    }

    public final void swipe(int startX, int startY, int endX, int endY) {
        exec("input swipe  " + startX + " " + startY + " " + endX + " " + endY + "\n");
    }

    public final void sendKeyevent(int key) {
//        home键的keycode=3，back键的keycode=4.,66 enter
        exec("input keyevent " + key + "\n");
        Log.d("sendKeyevent", "toutiaoGongZhongHao: " + "input keyevent " + key + "\n");

    }

    public final void inputText(String text) {
//        输入字符.
        exec("input text " + text + "\n");
    }

    public void readANewsAndBack() {
        //                    //点击推荐页的第一条新闻
        int x = (int) (sW / 2);
        int y = (int) (sH * 25 / 95);
        tapXY(x, y);
        Toast.makeText(getApplicationContext(), "点击推荐页的第一条新闻完毕", Toast.LENGTH_SHORT).show();
        sleepMs(45000);
        sendKeyevent(4);

    }

    public void startPackage(String packNameToStart, String classToStart) {
        exec("am start -n " + packNameToStart + "/" + classToStart + "\n");

    }

    public void reStartPackage(String packNameToStart, String classToStart) {
        exec("am start -S -a android.intent.action.MAIN -c android.intent.category.LAUNCHER  -n " + packNameToStart + "/" + classToStart + "\n");
        Log.d("XXX", "toutiaoGongZhongHao: " + "am start -S -a android.intent.action.MAIN -c android.intent.category.LAUNCHER  -n " + packNameToStart + "/" + classToStart + "\n");

    }

    public void clearApk(String packName) {
        exec("pm clear " + packName + "\n");
        Log.d("XXX", "toutiaoGongZhongHao: " + "清除" + packName + "的缓存数据" + "\n");
    }

    public void loginApk() {
        String userName = getAPhoneToLogin();
        String password = CommonTools.password;
        Log.d("XXX", "toutiaoGongZhongHao: " + "即将登录的账号是：" + userName + "，密码是：" + password + "\n");
        sleepMs(40000);
        int x;
        int y;
//        随便点一下绕过开app的奖品引导

        x = (int) (sW * 50 / 100);
        y = (int) (sH * 67 / 100);
        sendKeyevent(4);
        sleepMs(2000);
        tapXY(x, y);
        sleepMs(6000);
//        此时应该在登录页面
//        输入登录账户名
        Log.d("XXX", "toutiaoGongZhongHao: " + "开始输入账号：" + userName + "\n");
        x = (int) (sW * 50 / 100);
        y = (int) (sH * 40 / 100);
        tapXY(x, y);
        sleepMs(1000);
        inputText(userName);
//        输入密码
        Log.d("XXX", "toutiaoGongZhongHao: " + "开始输入密码：" + password + "\n");
        x = (int) (sW * 50 / 100);
        y = (int) (sH * 52 / 100);
        tapXY(x, y);
        sleepMs(1000);
        inputText(password);
        sleepMs(1000);
        sendKeyevent(66);
//        点击登录按钮
        x = (int) (sW * 50 / 100);
        y = (int) (sH * 59 / 100);
        tapXY(x, y);
        x = (int) (sW * 50 / 100);
        y = (int) (sH * 62 / 100);
        tapXY(x, y);

        sleepMs(5000);
//        到此登录完成
    }

//    public void appLocalNews() {
//        clearApk(packNameToStart);
//        reStartPackage(packNameToStart, classToStart);
//        loginApk();
//
//
//        //如果浮标从未启动变为启动状态，那么我们开始执行既定的刷app的操作
//        Toast.makeText(getApplicationContext(), "开始刷app啦", Toast.LENGTH_SHORT).show();
////					关联启动需要刷的app
//
//
//        //
////                    //利用ProcessBuilder执行shell命令,一堆点击操作猛如虎
////                    //首先点击头条tab（左下角那个）
//        int x;
//        int y;
////                    这里是需要循环执行的操作，需要做while处理
//        int count = 10;
//        while (count > 0) {
////            点击右下角刷新按钮
//            x = (int) (sW * 91 / 100);
//            y = (int) (sH * 87 / 100);
//
//            tapXY(x, y);
//            sleepMs(2000);
//            tapXY(x, y);
//            Toast.makeText(getApplicationContext(), "点击刷新按钮", Toast.LENGTH_SHORT).show();
//            sleepMs(4000);
//            //                    //点击推荐页的第一条新闻
//            x = (int) (sW / 2);
//            y = (int) (sH * 2 / 3);
//            tapXY(x, y);
//            Toast.makeText(getApplicationContext(), "点击推荐页的第一条新闻完毕", Toast.LENGTH_SHORT).show();
//            sleepMs(45000);
//            sendKeyevent(4);
//        }
//    }

    //num：
//    0：表示推荐TAB
//    1：公众号
//    2：养生
//    3：历史
//    4：育儿
//    5：旅游
//    6:探索
//    7：时尚
//    8：故事
//    9：美食
//    10：游戏
//    6：localNews
    public void touTiaoTAB
    (int num) {
        clearApk(packNameToStart);
        reStartPackage(packNameToStart, classToStart);
        loginApk();
        //如果浮标从未启动变为启动状态，那么我们开始执行既定的刷app的操作
        Toast.makeText(getApplicationContext(), "开始刷app啦", Toast.LENGTH_SHORT).show();
//					关联启动需要刷的app
//        startPackage(packNameToStart,classToStart);
        Toast.makeText(getApplicationContext(), "等你8s钟", Toast.LENGTH_SHORT).show();
        sleepMs(10000);
        Toast.makeText(getApplicationContext(), "打开你的app啦", Toast.LENGTH_SHORT).show();
        //
//                    //利用ProcessBuilder执行shell命令,一堆点击操作猛如虎
//                    //首先点击头条tab（左下角那个）
        int x = (int) (sW / 10);
        int y = (int) (sH * 95 / 100);
        tapXY(x, y);
        Toast.makeText(getApplicationContext(), "点击头条完毕", Toast.LENGTH_SHORT).show();

        Toast.makeText(getApplicationContext(), "点击左下角的头条入口", Toast.LENGTH_SHORT).show();
        sleepMs(6000);
//        点击推荐TAB，公众号tab，养生tab，历史tab，育儿tab，旅游tab
        if (num == 1) {
            num = 10;
        }
        if (num < 7) {
            x = (int) (sW * (8 + 17 * num) / 100);
            y = (int) (sH * 14 / 100);
            tapXY(x, y);
            sleepMs(4000);
        } else {
//            num大于等于7说明TAB超过当前屏幕了需要滑动一下
//            滑动TAB
            swipe(sW - 1, (int) (sH * 14 / 100), 1, (int) (sH * 14 / 100));
            sleepMs(1000);
            x = (int) (sW * (8 + 17 * (num - 7)) / 100);
            y = (int) (sH * 14 / 100);
            tapXY(x, y);
            sleepMs(1000);


            //            点击刷新按钮

            x = (int) (sW * 9 / 10);
            y = (int) (sH * 76 / 100);

            tapXY(x, y);
            sleepMs(2000);


        }

        readANewsAndBack();
//                    这里是需要循环执行的操作，需要做while处理
        int count = 30;
        while (count > 0) {
            count = count - 1;
            x = (int) (sW / 10);
            y = (int) (sH * 95 / 100);
            tapXY(x, y);
            Toast.makeText(getApplicationContext(), "点击头条完毕", Toast.LENGTH_SHORT).show();

            sleepMs(1000);
//            点击刷新按钮

            x = (int) (sW * 9 / 10);
            y = (int) (sH * 76 / 100);

//            tapXY(x, y);
//            sleepMs(2000);
            tapXY(x, y);
            Toast.makeText(getApplicationContext(), "点击刷新按钮", Toast.LENGTH_SHORT).show();
            sleepMs(4000);
            readANewsAndBack();
        }


    }

    public void killPackage(String packageName) {
        exec("am force-stop " + packageName + "\n");
    }


    private void createFloatView() {
        packNameToStart = "com.bdvtt.www";
        classToStart = "com.uzmap.pkg.EntranceActivity";
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        sW = mWindowManager.getDefaultDisplay().getWidth();
        sH = mWindowManager.getDefaultDisplay().getHeight();

        initWindowParams();// 初始化WindowParams
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        floatView = inflater.inflate(R.layout.bo_view, null);
        //初始化成未执行人物的状态
        floatRuningStatus = Boolean.FALSE;
        mWindowManager.addView(floatView, wmParams);


        // 至此悬浮窗已经显示出来了，后面的只是监听触摸事件
        // 通过mWindowManager.updateViewLayout方法让悬浮窗随手指移动
        // 并且在移动到某个区域时显示一个吃豆人，有兴趣的可以看下
        eatBeanMan = new EatBeanMan(getApplicationContext());
        View bo = floatView.findViewById(R.id.bobobo);
        bo.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        wmParams.x = (int) event.getRawX() - 15;
                        wmParams.y = (int) event.getRawY() - 15 - 35;// 这里35应该换成相应状态栏的高度
                        mWindowManager.updateViewLayout(floatView, wmParams);
                        // 上面的是让floatView随手指移动
                        // 下面的是判断要不要显示吃豆人
                        if (!hasAddEat) {
                            if (wmParams.x < 350 && wmParams.y > (sH / 2 - 175) && wmParams.y < (sH / 2 + 200)) {
                                mWindowManager.addView(eatBeanMan, eatParams);
                                hasAddEat = true;
                            }
                        } else {
                            if (wmParams.x < 350 && wmParams.y > (sH / 2 - 175) && wmParams.y < (sH / 2 + 200)) {

                            } else {
                                mWindowManager.removeView(eatBeanMan);
                                hasAddEat = false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (hasAddEat) {
                            mWindowManager.removeView(eatBeanMan);
                        }
                        hasAddEat = false;
                        // 抬起手指时让floatView紧贴屏幕左右边缘
                        wmParams.x = wmParams.x <= (sW / 2) ? 0 : sW;
                        mWindowManager.updateViewLayout(floatView, wmParams);
                        break;
                }
                return false;
            }
        });
        bo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO 自动生成的方法存根


                floatRuningStatus = !floatRuningStatus;
                if (floatRuningStatus) {
                    Toast.makeText(getApplicationContext(), "执行任务开始！！", Toast.LENGTH_SHORT).show();
//                    while (Boolean.TRUE) {
//
//                        touTiaoTAB((int) (Math.random() * 11));
//
//                    }


                } else {
                    //如果浮标从启动变成未启动状态，那么我们关闭整个浮标应用
                    Toast.makeText(getApplicationContext(), "在浮标app内部隐藏浮标可以关闭当前任务", Toast.LENGTH_SHORT).show();


                }

            }
        });
    }

    private void initWindowParams() {
        wmParams = new LayoutParams();
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;// 特别注意在这里设置等级为系统警告
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = 30;
        wmParams.height = 30;
        eatParams = new LayoutParams();
        eatParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        eatParams.format = PixelFormat.RGBA_8888;
        eatParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        eatParams.gravity = Gravity.LEFT | Gravity.TOP;
        eatParams.x = 0;
        eatParams.y = sH / 2 - 150;
        eatParams.width = 300;
        eatParams.height = 300;
    }

    @Override
    public void onDestroy() {
        if (hasAddEat) {
            mWindowManager.removeView(eatBeanMan);
        }
        mWindowManager.removeView(floatView);
        super.onDestroy();
    }
}
