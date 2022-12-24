package ru.gsemenov.dragon.util;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.Closeable;

/**
 * Класс для проигрывания звуков
 */
public class SoundPlayer implements Closeable {

    /**
     * Используются методы MediaPlayer:
     * - start(): продолжить проигрывание
     * - seekTo(0): переместиться на начало звукового файла
     * - pause(): приостановить проигрывание
     * - setVolume(1.0f, 1.0f): поставить громкость проигрываемого звука
     */

    protected final MediaPlayer mp;
    protected boolean needsToPlayOnResume = false;

    public SoundPlayer(Context context, int resource) {
        mp = MediaPlayer.create(context, resource);
        mp.setVolume(1.0f, 1.0f);
    }

    /**
     * Начать проигрывание музыки/звука с начала
     */
    public void play() {
        mp.seekTo(0);
        mp.start();
    }

    /**
     * Приостановить проигрывание
     */
    public void pause() {
        needsToPlayOnResume = mp.isPlaying();
        mp.pause();
    }

    /**
     * Продолжить воспроизведение звука после pause
     *
     * Если на момент pause звук проигрывался, продолжить его проигрывание
     * Если на момент pause ничего не играло, ничего не играть и дальше
     */
    public void resume() {
        if (needsToPlayOnResume) {
            mp.start();
        }
    }

    /**
     * Остановить проигрывание звука
     */
    public void stop() {
        mp.pause();
    }

    // Очистить память от плеера
    @Override
    public void close()  {
        mp.release();
    }

}
