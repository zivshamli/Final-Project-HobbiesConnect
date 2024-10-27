package com.example.finalprojecthobbiesconnect.utilties

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SoundManager(private val context: Context) {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private var mediaPlayer: MediaPlayer? = null


    fun playSound(resId: Int) {
        executor.execute {
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                isLooping = false
                setVolume(1.0f, 1.0f)
                start()


                Log.d("BackgroundSound", "Sound started")

                setOnCompletionListener {

                    Log.d("BackgroundSound", "Sound completed")
                    release()
                    mediaPlayer = null
                }
            }
        }
    }

    fun stopSound() {
        mediaPlayer?.let {
            executor.execute {
                if (it.isPlaying) {
                    it.stop()
                    Log.d("BackgroundSound", "Sound stopped")
                }
                it.release()
                mediaPlayer = null
            }
        }
    }



}