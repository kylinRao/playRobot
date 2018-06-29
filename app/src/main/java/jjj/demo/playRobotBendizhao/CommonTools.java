package jjj.demo.playRobotBendizhao;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class CommonTools {
    public static String phoneLists;
    public static String password;
    public static OutputStream os;

    public static int sW;
    public static int sH;
    public static Boolean isNeedLogin;

    public static void setsW(int sW) {
        CommonTools.sW = sW;
    }

    public static void setsH(int sH) {
        CommonTools.sH = sH;
    }


    private static String LOG_TAG = CommonTools.class.getName();

    public boolean isDeviceRooted() {
        if (checkRootMethod1()) {
            return true;
        }
        if (checkRootMethod2()) {
            return true;
        }
        if (checkRootMethod3()) {
            return true;
        }
        return false;
    }

    public boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;

        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    public boolean checkRootMethod2() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean checkRootMethod3() {
        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null) {
            return true;
        } else {
            return false;
        }
    }


    public static void initApk(Context context) {
        phoneLists = context.getString(R.string.phoneLists);
        password = context.getString(R.string.password);

        try {
//            Log.d("initApk", String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
//            Log.d("initApk", String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
            Log.d("initApk", (String.valueOf(Environment.getExternalStoragePublicDirectory("/")) + "/float.properties"));
            Properties props = new Properties();
            InputStream in;

            in = new FileInputStream(String.valueOf(Environment.getExternalStoragePublicDirectory("/")) + "/float.properties");
            props.load(in);

            phoneLists = props.getProperty("phoneLists");
            password = props.getProperty("password", "123456");
            System.out.println(phoneLists);
        } catch (FileNotFoundException e) {
            Log.d("XXX", "toutiaoGongZhongHao: " + "float.properties配置文件未找到" + "\n");
            e.printStackTrace();

        } catch (IOException e) {
            Log.d("XXX", "toutiaoGongZhongHao: " + "float.properties配置文件读取异常" + "\n");
            e.printStackTrace();
        } finally {
            Log.d("XXX", "toutiaoGongZhongHao: " + "phoneLists获取到的结果是" + phoneLists + "\n");

        }


    }

    public static HashMap<String, Object> getItems(Context context, String packageName) {

        PackageManager pckMan = context.getPackageManager();
        HashMap<String, Object> item = new HashMap<String, Object>();

        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);

        for (PackageInfo pInfo : packageInfo) {
            if (pInfo.packageName.equals(packageName)) {


                item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
                item.put("packageName", pInfo.packageName);
                item.put("versionCode", pInfo.versionCode);
                item.put("versionName", pInfo.versionName);
                item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());


            }
        }

        return item;
    }


    //静态内部类
    static class ExecShell {

        private static String LOG_TAG = ExecShell.class.getName();

        public static enum SHELL_CMD {
            check_su_binary(new String[]{"/system/xbin/which", "su"}),;

            String[] command;

            SHELL_CMD(String[] command) {
                this.command = command;
            }
        }

        public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
            String line = null;
            ArrayList<String> fullResponse = new ArrayList<String>();
            Process localProcess = null;

            try {
                localProcess = Runtime.getRuntime().exec(shellCmd.command);
            } catch (Exception e) {
                return null;
                //e.printStackTrace();
            }

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));

            try {
                while ((line = in.readLine()) != null) {
                    Log.d(LOG_TAG, "--> Line received: " + line);
                    fullResponse.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(LOG_TAG, "--> Full response was: " + fullResponse);

            return fullResponse;
        }
    }


    public static String getAPhoneToLogin() {
        String[] phoneLists = CommonTools.phoneLists.split(";");
        return phoneLists[(int) (Math.random() * phoneLists.length)];
    }

    private static void sleepMs(int ms) {
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
    public static void exec(String cmd) {


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

    public static void tapXY(int x, int y) {
        exec("input tap " + x + " " + y + "\n");
        Log.d("tapXY", "toutiaoGongZhongHao: " + "input tap " + x + " " + y + "\n");
    }

    public static void swipe(int startX, int startY, int endX, int endY) {
        exec("input swipe  " + startX + " " + startY + " " + endX + " " + endY + "\n");
    }

    public static void sendKeyevent(int key) {
//        home键的keycode=3，back键的keycode=4.,66 enter
        exec("input keyevent " + key + "\n");
        Log.d("sendKeyevent", "toutiaoGongZhongHao: " + "input keyevent " + key + "\n");

    }

    public static void inputText(String text) {
//        输入字符.
        exec("input text " + text + "\n");
    }

    public static void readANewsAndBack() {
        //                    //点击推荐页的第一条新闻
        int x = (int) (sW / 2);
        int y = (int) (sH * 25 / 95);
        CommonTools.tapXY(x, y);

        sleepMs(45000);
        sendKeyevent(4);

    }

    public static void startPackage(String packNameToStart, String classToStart) {
        exec("am start -n " + packNameToStart + "/" + classToStart + "\n");

    }

    public static void reStartPackage(String packNameToStart, String classToStart) {
        exec("am start -S -a android.intent.action.MAIN -c android.intent.category.LAUNCHER  -n " + packNameToStart + "/" + classToStart + "\n");
        Log.d("XXX", "toutiaoGongZhongHao: " + "am start -S -a android.intent.action.MAIN -c android.intent.category.LAUNCHER  -n " + packNameToStart + "/" + classToStart + "\n");

    }

    public static void clearApk(String packName) {
        exec("pm clear " + packName + "\n");
        Log.d("XXX", "toutiaoGongZhongHao: " + "清除" + packName + "的缓存数据" + "\n");
    }

    public static void loginApk() {
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


    public static void touTiaoTAB
            (int num, String packNameToStart, String classToStart) {
        Log.d("touTiaoTAB", "toutiaoGongZhongHao: " + "sW：" + sW + ",sH:"+sH);
        if(isNeedLogin){
            clearApk(packNameToStart);
        }

        reStartPackage(packNameToStart, classToStart);
        if(isNeedLogin){
            loginApk();
        }

        //如果浮标从未启动变为启动状态，那么我们开始执行既定的刷app的操作
        sleepMs(10000);        //
//                    //利用ProcessBuilder执行shell命令,一堆点击操作猛如虎
//                    //首先点击头条tab（左下角那个）
        int x = (int) (sW / 10);
        int y = (int) (sH * 95 / 100);
        tapXY(x, y);

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


            sleepMs(1000);
//            点击刷新按钮

            x = (int) (sW * 9 / 10);
            y = (int) (sH * 76 / 100);

//            tapXY(x, y);
//            sleepMs(2000);
            tapXY(x, y);

            sleepMs(4000);
            readANewsAndBack();
        }


    }

    public static void killPackage(String packageName) {
        exec("am force-stop " + packageName + "\n");
    }


}
