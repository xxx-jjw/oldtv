package top.xxx.helper_app;

import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pili.pldroid.player.widget.PLVideoView;


public class LiveUriTestActivity extends AppCompatActivity {

    private PLVideoView plVideoview;
    private EditText uriText;
    private Button uriTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_uri_test);

        uriText = findViewById(R.id.liveUri);
        uriTestBtn = findViewById(R.id.liveUriTestBtn);
        plVideoview = (PLVideoView) findViewById(R.id.uriTestVideoView);

        uriTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("1", "on click... ");
                playWithUriStr(uriText.getText().toString());
            }
        });
    }

    private void playWithUriStr(String uriStr){
        plVideoview.stopPlayback();
//        Log.e("1", "~"+uriStr+"~");
//        Uri uri = Uri.parse("http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8");
        Uri uri = Uri.parse(uriStr.replace(" ", ""));
        plVideoview.setVideoURI(uri);
        plVideoview.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        plVideoview.stopPlayback();
    }

}