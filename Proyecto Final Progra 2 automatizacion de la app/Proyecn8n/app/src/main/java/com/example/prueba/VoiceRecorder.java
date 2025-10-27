package com.example.prueba;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VoiceRecorder {
    
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE_FACTOR = 2;
    
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private File audioFile;
    private Context context;
    private VoiceRecorderCallback callback;
    
    public interface VoiceRecorderCallback {
        void onRecordingStarted();
        void onRecordingStopped(String audioFilePath);
        void onRecordingError(String error);
        void onPermissionDenied();
    }
    
    public VoiceRecorder(Context context, VoiceRecorderCallback callback) {
        this.context = context;
        this.callback = callback;
    }
    
    public void startRecording() {
        if (!hasPermission()) {
            if (callback != null) {
                callback.onPermissionDenied();
            }
            return;
        }
        
        try {
            int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT) * BUFFER_SIZE_FACTOR;
            
            audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    bufferSize
            );
            
            if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                if (callback != null) {
                    callback.onRecordingError("Error al inicializar el grabador de audio");
                }
                return;
            }
            
            // Create audio file
            audioFile = createAudioFile();
            
            audioRecord.startRecording();
            isRecording = true;
            
            if (callback != null) {
                callback.onRecordingStarted();
            }
            
            // Start recording thread
            recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    recordAudio();
                }
            });
            recordingThread.start();
            
        } catch (Exception e) {
            if (callback != null) {
                callback.onRecordingError("Error al iniciar la grabación: " + e.getMessage());
            }
        }
    }
    
    public void stopRecording() {
        if (!isRecording || audioRecord == null) {
            return;
        }
        
        isRecording = false;
        
        try {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            
            if (recordingThread != null) {
                recordingThread.join();
                recordingThread = null;
            }
            
            if (callback != null) {
                callback.onRecordingStopped(audioFile.getAbsolutePath());
            }
            
        } catch (Exception e) {
            if (callback != null) {
                callback.onRecordingError("Error al detener la grabación: " + e.getMessage());
            }
        }
    }
    
    private void recordAudio() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(audioFile);
            byte[] buffer = new byte[1024];
            
            while (isRecording && audioRecord != null) {
                int bytesRead = audioRecord.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            
        } catch (IOException e) {
            if (callback != null) {
                callback.onRecordingError("Error al escribir el archivo de audio: " + e.getMessage());
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private File createAudioFile() throws IOException {
        File audioDir = new File(context.getExternalFilesDir(null), "voice_recordings");
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        
        String fileName = "voice_" + System.currentTimeMillis() + ".wav";
        return new File(audioDir, fileName);
    }
    
    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) 
                == PackageManager.PERMISSION_GRANTED;
    }
    
    public boolean isRecording() {
        return isRecording;
    }
    
    public void release() {
        if (isRecording) {
            stopRecording();
        }
    }
}
