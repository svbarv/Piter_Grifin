package com.semana9.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";  // Para logs de depuración
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button playAudioButton = findViewById(R.id.sra_play_audio);
        playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.audio);
                        mediaPlayer.setOnPreparedListener(mp -> {
                            mp.start();  // Inicia el audio al estar listo
                            Toast.makeText(MainActivity.this, "¡Reproduciendo consejo de guerra!", Toast.LENGTH_SHORT).show();
                        });

                        mediaPlayer.setOnCompletionListener(mp -> {
                            Toast.makeText(MainActivity.this, "Audio finalizado.", Toast.LENGTH_SHORT).show();
                        });

                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            Log.e(TAG, "Error al reproducir: " + what + ", extra: " + extra);
                            Toast.makeText(MainActivity.this, "Error en la reproducción.", Toast.LENGTH_SHORT).show();
                            return true;
                        });
                    } else if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    } else {
                        Log.w(TAG, "El audio ya se está reproduciendo.");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error al intentar reproducir audio.", e);
                    Toast.makeText(MainActivity.this, "Error al reproducir audio.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonPiterVsPollo = findViewById(R.id.piterVSpollo);
        buttonPiterVsPollo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReproductorVideo.class);
            startActivity(intent);
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
