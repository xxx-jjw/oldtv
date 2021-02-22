package top.xxx.tv_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.pili.pldroid.player.widget.PLVideoView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Observable;
import java.util.Observer;

import top.xxx.tv_app.util.FileUtil;
import top.xxx.tv_app.util.VirtualKeyUtil;

public class MainActivity extends AppCompatActivity implements Observer {

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

//        FileUtil._sdcard = getExternalFilesDir(null);

        Log.e("curNum", FileUtil.getSdcardRootPath());

        plVideoview = (PLVideoView) findViewById(R.id.plVideoView);

        // 设置全屏播放, 不能取消标题栏和状态栏
//        plVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);;

        Channel.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteControlServerThread.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("onKeyDown", ""+keyCode);
        int tempCurChannelNum = Channel.getInstance(this).getCurChannelNum();
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP://频道+
                Channel.getInstance(this).setCurChannelNum(tempCurChannelNum+1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://频道-
                Channel.getInstance(this).setCurChannelNum(tempCurChannelNum-1);
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
            VirtualKeyUtil.sendKeyCode(keyCode);
        }
    }

    public void playVideoWithUri(String uriStr) {
        plVideoview.stopPlayback();
        Uri uri = Uri.parse(uriStr.replace(" ", ""));
        plVideoview.setVideoURI(uri);
        plVideoview.start();
    }

    @Override
    public void update(Observable o, Object arg) {
//        Toast.makeText(this, ""+(Integer)arg, Toast.LENGTH_SHORT).show();
        Channel.ChannelEntry entry = (Channel.ChannelEntry)arg;
        ((TextView)findViewById(R.id.channel_num)).setText(""+Channel.getInstance(this).getCurChannelNum());
        ((TextView)findViewById(R.id.channel_name)).setText(Channel.getInstance(this).getCurChannelName());
        playVideoWithUri(entry.getUri());
    }
}