package com.example.psuplays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.video.VideoQuality;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class livevideouploader extends Activity implements RtspClient.Callback, Session.Callback, SurfaceHolder.Callback {
    // log tag
    public final static String TAG = livevideouploader.class.getSimpleName();

    // surfaceview
    private static SurfaceView mSurfaceView;

    // Rtsp session
    private Session mSession;
    private static RtspClient mClient;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_livevideouploader);
        ((TextView)findViewById(R.id.tvHTeam)).setText(getIntent().getExtras().getString("home_team_name"));
        ((TextView)findViewById(R.id.tvHScore)).setText(getIntent().getExtras().getString("home_team_score"));
        ((TextView)findViewById(R.id.tvATeam)).setText(getIntent().getExtras().getString("away_team_name"));
        ((TextView)findViewById(R.id.tvAScore)).setText(getIntent().getExtras().getString("away_team_score"));

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);

        mSurfaceView.getHolder().addCallback(this);

        // Initialize RTSP client
        initRtspClient();

    }

    @Override
    protected void onResume() {
        super.onResume();

        toggleStreaming();
    }

    @Override
    protected void onPause(){
        super.onPause();

        toggleStreaming();
    }

    private void initRtspClient() {
        // Configures the SessionBuilder
        mSession = SessionBuilder.getInstance()
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setAudioQuality(new AudioQuality(8000, 16000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
                .setSurfaceView(mSurfaceView).setPreviewOrientation(0)
                .setCallback(this).build();

        // Configures the RTSP client
        mClient = new RtspClient();
        mClient.setSession(mSession);
        mClient.setCallback(this);
        mSurfaceView.setCameraDistance(10);
        mSurfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);
        String ip, port, path;

        // We parse the URI written in the Editext
        Pattern uri = Pattern.compile("rtsp://(.+):(\\d+)/(.+)");
        Matcher m = uri.matcher(AppConfig.STREAM_URL);
        m.find();
        ip = m.group(1);
        port = m.group(2);
        path = m.group(3);

        mClient.setCredentials(AppConfig.PUBLISHER_USERNAME,
                AppConfig.PUBLISHER_PASSWORD);
        mClient.setServerAddress(ip, Integer.parseInt(port));
        mClient.setStreamPath("/" + path);
    }

    private void toggleStreaming() {
        if (!mClient.isStreaming()) {
            // Start camera preview
            mSession.startPreview();

            // Start video stream
            mClient.startStream();
        } else {
            // already streaming, stop streaming
            // stop camera preview
            mSession.stopPreview();

            // stop streaming
            mClient.stopStream();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClient.release();
        mSession.release();
        mSurfaceView.getHolder().removeCallback(this);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

     */

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {
        switch (reason) {
            case Session.ERROR_CAMERA_ALREADY_IN_USE:
                break;
            case Session.ERROR_CAMERA_HAS_NO_FLASH:
                break;
            case Session.ERROR_INVALID_SURFACE:
                break;
            case Session.ERROR_STORAGE_NOT_READY:
                break;
            case Session.ERROR_CONFIGURATION_NOT_SUPPORTED:
                break;
            case Session.ERROR_OTHER:
                break;
        }

        if (e != null) {
            alertError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void alertError(final String msg) {
        final String error = (msg == null) ? "Unknown error: " : msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(livevideouploader.this);
        builder.setMessage(error).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRtspUpdate(int message, Exception exception) {
        switch (message) {
            case RtspClient.ERROR_CONNECTION_FAILED:
            case RtspClient.ERROR_WRONG_CREDENTIALS:
                alertError(exception.getMessage());
                exception.printStackTrace();
                break;
        }
    }

    @Override
    public void onPreviewStarted() {
    }

    @Override
    public void onSessionConfigured() {
    }

    @Override
    public void onSessionStarted() {
    }

    @Override
    public void onSessionStopped() {
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onBitrateUpdate(long bitrate) {
    }

    public void endStream(View view) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/endGame/";
        JSONObject form = new JSONObject();
        String hteam = ((TextView)findViewById(R.id.tvHTeam)).getText().toString();
        String ateam = ((TextView)findViewById(R.id.tvATeam)).getText().toString();
        int hscore = Integer.valueOf(((TextView)findViewById(R.id.tvHScore)).getText().toString());
        int ascore = Integer.valueOf(((TextView)findViewById(R.id.tvAScore)).getText().toString());

        form.put("id",sharedPref.getInt("game_id",0));
        form.put("team_1",hteam);
        form.put("score_1",hscore);
        form.put("team_2",ateam);
        form.put("score_2",ascore);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Log.e(TAG,"Game ended Successful!");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Game ending failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        String responseBody = null;
                        try {
                            responseBody = new String(error.networkResponse.data, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        JSONObject data = null;
                        try {
                            data = new JSONObject(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String message = data.optString("reason");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
        Intent intent = new Intent(livevideouploader.this,Admin_Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void toggleCamera(View view) {
        mSession.switchCamera();
    }

    public void toggleFlash(View view) {
        mSession.toggleFlash();
        if(mSession.getVideoTrack().getFlashState()){
            ((FloatingActionButton)findViewById(R.id.fbFlash)).setImageResource(R.drawable.ic_flash_on);
        }
        else {
            ((FloatingActionButton)findViewById(R.id.fbFlash)).setImageResource(R.drawable.ic_flash_off);
        }
    }

    public void updateScore(View view) throws JSONException {
        if(view.getId() == R.id.btAddHScore){
            TextView tv = (TextView)findViewById(R.id.tvHScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) + 1));
        }
        if(view.getId() == R.id.btSubHScore){
            TextView tv = (TextView)findViewById(R.id.tvHScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) - 1));
        }
        if(view.getId() == R.id.btAddAScore){
            TextView tv = (TextView)findViewById(R.id.tvAScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) + 1));
        }
        if(view.getId() == R.id.btSubAScore){
            TextView tv = (TextView)findViewById(R.id.tvAScore);
            tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) - 1));
        }

        String hteam = ((TextView)findViewById(R.id.tvHTeam)).getText().toString();
        String ateam = ((TextView)findViewById(R.id.tvATeam)).getText().toString();
        int hscore = Integer.valueOf(((TextView)findViewById(R.id.tvHScore)).getText().toString());
        int ascore = Integer.valueOf(((TextView)findViewById(R.id.tvAScore)).getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http:73.188.242.140:8888/score/update/";
        JSONObject form = new JSONObject();
        form.put("id",sharedPref.getInt("game_id",0));
        form.put("team_1",hteam);
        form.put("score_1",hscore);
        form.put("team_2",ateam);
        form.put("score_2",ascore);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, form, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
                        try {
                            status = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("success")){
                            Toast.makeText(livevideouploader.this,"Score updated Successful!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(livevideouploader.this, "Score update failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        String responseBody = null;
                        try {
                            responseBody = new String(error.networkResponse.data, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        JSONObject data = null;
                        try {
                            data = new JSONObject(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String message = data.optString("reason");
                        Toast.makeText(livevideouploader.this,message, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
