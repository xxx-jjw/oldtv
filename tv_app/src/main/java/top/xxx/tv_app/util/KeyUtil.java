package top.xxx.tv_app.util;

import android.app.Instrumentation;
import android.util.Log;

public class KeyUtil {

    public static void sendKeyCode(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    Log.e("sendPointerSync error", e.toString());
                }
            }
        }.start();
    }
}
