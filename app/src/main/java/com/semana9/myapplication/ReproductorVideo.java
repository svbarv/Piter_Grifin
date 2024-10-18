package com.semana9.myapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReproductorVideo extends AppCompatActivity {

    private VideoView videoView;
    private Button playVideoButton;
    private SeekBar volumeSeekBar;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reproductor_video);

        // Configura los insets para diseño edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa el VideoView, botón y SeekBar
        videoView = findViewById(R.id.videoView);
        playVideoButton = findViewById(R.id.playVideo);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        // Configura el SeekBar para el volumen
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Configuracion el evento del botón para reproducir el video
        playVideoButton.setOnClickListener(v -> playVideo());
    }

    private void playVideo() {
        // Verificacion
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Toast.makeText(this, "El video ya se está reproduciendo.", Toast.LENGTH_SHORT).show();
            return;
        }
        //ruta
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video); // Cambia 'tu_video' por el nombre correcto de tu archivo

        videoView.setVideoURI(videoUri);

        // Configura los listeners para el video
        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp; // Guarda la referencia del MediaPlayer
            mp.start(); // Inicia la reproducción cuando esté preparado
        });

        videoView.setOnCompletionListener(mp -> {
            // Limpia la referencia del MediaPlayer al finalizar
            mediaPlayer = null;
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            return true; // Devuelve true si manejaste el error
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
