package top.xxx.remote_control_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.channel_up).setOnClickListener(this);
        findViewById(R.id.channel_down).setOnClickListener(this);
        findViewById(R.id.volume_up).setOnClickListener(this);
        findViewById(R.id.volume_down).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.channel_up:
                sendMessageToTvInNewThread(KeyEvent.KEYCODE_DPAD_UP);
                break;
            case R.id.channel_down:
                sendMessageToTvInNewThread(KeyEvent.KEYCODE_DPAD_DOWN);
                break;
            case R.id.volume_up:
                sendMessageToTvInNewThread(KeyEvent.KEYCODE_VOLUME_UP);
                break;
            case R.id.volume_down:
                sendMessageToTvInNewThread(KeyEvent.KEYCODE_VOLUME_DOWN);
                break;
        }
    }

    private void sendMessageToTvInNewThread(final int keyCode){
        new Thread(){
            @Override
            public void run() {
                super.run();
                sendMessageToTv(keyCode);
            }
        }.start();
    }

    private void sendMessageToTv(int keyCode){

        InetAddress broadcastAddress = null;
        DatagramSocket socket = null;
        try {
            broadcastAddress = InetAddress.getByName("255.255.255.255");
            int tvPort = 2106;
            socket = new DatagramSocket();
            byte[] sendData = (keyCode+"").getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, tvPort);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}