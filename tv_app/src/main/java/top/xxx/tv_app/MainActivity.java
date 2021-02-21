package top.xxx.tv_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.pili.pldroid.player.widget.PLVideoView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import top.xxx.tv_app.pojo.Channel;
import top.xxx.tv_app.function.ChannelFunction;
import top.xxx.tv_app.util.FileUtil;
import top.xxx.tv_app.util.KeyUtil;

public class MainActivity extends AppCompatActivity {

    private PLVideoView plVideoview;

    Thread remoteControlServerThread = new Thread(){
        @Override
        public void run() {
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        remoteControlServerThread.start();

        Log.e("curNum", FileUtil.getSdcardRootPath());

        plVideoview = (PLVideoView) findViewById(R.id.plVideoView);

        // 设置全屏播放, 不能取消标题栏和状态栏
//        plVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);;

        ChannelFunction.initChannel(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteControlServerThread.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("onKeyDown", ""+keyCode);
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP://频道+
                ChannelFunction.changeChannel(this, true);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://频道-
                ChannelFunction.changeChannel(this, false);
                break;
        }
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
            Log.e("keyCode", keyCode + "in listen 2106.");
            KeyUtil.sendKeyCode(keyCode);
        }
    }


    public void playWithUriStr(String uriStr){
        plVideoview.stopPlayback();
        Uri uri = Uri.parse(uriStr.replace(" ", ""));
        plVideoview.setVideoURI(uri);
        plVideoview.start();
    }

}