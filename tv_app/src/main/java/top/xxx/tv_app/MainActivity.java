package top.xxx.tv_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.pili.pldroid.player.widget.PLVideoView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import top.xxx.tv_app.util.KeyUtil;

public class MainActivity extends AppCompatActivity {

    private PLVideoView plVideoview;
    Thread remoteControlServerThread = new Thread(){
        @Override
        public void run() {
            super.run();
            super.run();
            try {
                listenPhoneKeyMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        remoteControlServerThread.start();

        plVideoview = (PLVideoView) findViewById(R.id.plVideoView);

        Uri uri = Uri.parse("http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8");
        plVideoview.setVideoURI(uri);
        // 设置全屏播放, 不能取消标题栏和状态栏
//        plVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        plVideoview.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteControlServerThread.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("1", ""+keyCode);
        // keycode      action
        // 24           volume+
        // 25           volume-
        // 19,20,21,22  up,down,left,right
        return super.onKeyDown(keyCode, event);
    }

    private void listenPhoneKeyMessage() throws Exception {
        int selfPort = 2106;

        DatagramSocket socket = new DatagramSocket(selfPort);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        while (true) {
            socket.receive(receivePacket);
            int keyCode = Integer.parseInt(new String(receiveData, 0, receivePacket.getLength()));
            KeyUtil.sendKeyCode(keyCode);
        }
    }
}