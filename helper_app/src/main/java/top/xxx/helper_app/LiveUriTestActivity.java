package top.xxx.helper_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LiveUriTestActivity extends AppCompatActivity implements
        OnBufferingUpdateListener, OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private MediaPlayer mediaPlayer;

    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    private int videoWidth;

    private int videoHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_uri_test);

        this.surfaceView = (SurfaceView) this.findViewById(R.id.surface);
        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.v("mplayer", ">>>create ok.");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}