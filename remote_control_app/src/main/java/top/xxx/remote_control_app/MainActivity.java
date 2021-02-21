package top.xxx.remote_control_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    InetAddress broadcastAddress = null;
    DatagramSocket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            broadcastAddress = InetAddress.getByName("255.255.255.255");
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.channel_up).setOnClickListener(this);
        findViewById(R.id.channel_down).setOnClickListener(this);
        findViewById(R.id.volume_up).setOnClickListener(this);
        findViewById(R.id.volume_down).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.channel_up:
                sendMessageToTv(KeyEvent.KEYCODE_DPAD_UP);
                break;
            case R.id.channel_down:
                sendMessageToTv(KeyEvent.KEYCODE_DPAD_DOWN);
                break;
            case R.id.volume_up:
                sendMessageToTv(KeyEvent.KEYCODE_VOLUME_UP);
                break;
            case R.id.volume_down:
                sendMessageToTv(KeyEvent.KEYCODE_VOLUME_DOWN);
                break;
        }
    }

    private void sendMessageToTv(int keyCode){
        try {
            int tvPort = 2106;
            byte[] sendData = (keyCode+"").getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, tvPort);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}