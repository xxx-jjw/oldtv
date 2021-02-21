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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import top.xxx.tv_app.pojo.Channel;
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
    List<Channel> channelList = new ArrayList<Channel>();
    int curChannelNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        remoteControlServerThread.start();

        plVideoview = (PLVideoView) findViewById(R.id.plVideoView);

        // 设置全屏播放, 不能取消标题栏和状态栏
//        plVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);;

        channelList.add(new Channel("点播台", ""));
        channelList.add(new Channel("戏曲台", ""));
        channelList.add(new Channel("电视剧台", ""));
        channelList.add(new Channel("电影台", ""));

        readChannelByFileAndAddChannelToList();
        readCurChannelNumByFile();
        changeChannel(curChannelNum);
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
        // up做频道+, down做频道-

        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP://频道+
                Toast.makeText(MainActivity.this, "频道+", Toast.LENGTH_SHORT).show();
                curChannelNum = (curChannelNum+1) % channelList.size();
                changeChannel(curChannelNum);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://频道-
                Toast.makeText(MainActivity.this, "频道-", Toast.LENGTH_SHORT).show();
                curChannelNum = (curChannelNum -1 + channelList.size()) % channelList.size();
                changeChannel(curChannelNum);
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
            KeyUtil.sendKeyCode(keyCode);
        }
    }

    private void readChannelByFileAndAddChannelToList(){
        try {
            InputStream is = getResources().getAssets().open("channel.data");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while ((str = br.readLine()) != null) {
                //            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                String[] temp = str.split(" ");
                channelList.add(new Channel(temp[0], temp[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readCurChannelNumByFile(){
        try {
            InputStream is = getResources().getAssets().open("cur_channel_num.data");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = br.readLine();
            curChannelNum = Integer.parseInt(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void playWithUriStr(String uriStr){
        plVideoview.stopPlayback();
        Uri uri = Uri.parse(uriStr.replace(" ", ""));
        plVideoview.setVideoURI(uri);
        plVideoview.start();
    }

    private void changeChannel(int channelNum){
        if(channelNum<channelList.size())
            playWithUriStr(channelList.get(channelNum).getUri());
        else
            playWithUriStr(channelList.get(channelList.size() - 1).getUri());
    }
}