package top.xxx.tv_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pili.pldroid.player.widget.PLVideoView;

public class MainActivity extends AppCompatActivity {

    private PLVideoView plVideoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        无标题栏：
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        全屏：无状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        Uri uri = Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        Uri uri = Uri.parse("http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8");

        plVideoview = (PLVideoView) findViewById(R.id.plVideoView);
        plVideoview.setVideoURI(uri);

        // 设置全屏播放, 不能取消标题栏和状态栏
        plVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        plVideoview.start();
//        android:theme="@style/Theme.OldTV"
    }
}