package com.example.apoiodigital.Recorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class AudioRecorderInput implements AudioRecorder {
    private final Context context;
    private MediaRecorder _recorder;

    private void createRecorder(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("STTResponse", "versao valida" );
            _recorder = new MediaRecorder(context);
            return;
        }
        Log.e("STTResponse", "versao nao valida" );

        //        _recorder = new MediaRecorder();
    }

    public AudioRecorderInput(Context context) {
        this.context = context;
    }

    @Override
    public void start(String outputFile) {
        createRecorder();

        _recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        _recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try{
            _recorder.setOutputFile(outputFile);
            _recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        _recorder.start();
    }

    @Override
    public void stop() {

        if(_recorder == null) return;
        _recorder.stop();
        _recorder.release();
        Log.e("STTResponse", "stop: Parando a gravacao" );
        _recorder = null;

        //        recorder.reset();
    }
}
