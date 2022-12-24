package ru.gsemenov.dragon.util;

import android.os.CountDownTimer;

/**
 * Вспомогательный класс для создания игрового таймера
 *
 * Игровой таймер производит какое-то действие с заданным интервалом
 */
public class GameTimer  {

    private final CountDownTimer timer;

    protected Runnable task = null;
    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;

    /**
     * Создать таймер, который будет вызывать переопределенный вами метод tick()
     * @param interval, с которым необходимо вызывать метод tick()
     */
    public GameTimer(int interval) {
        timer = new CountDownTimer(Integer.MAX_VALUE, interval) {
            @Override
            public void onTick(long l) {
                if (isRunning) {
                    tick();
                } else {
                    cancel();
                }
            }

            @Override
            public void onFinish() {
            }
        };
    }

    /**
     * Создать таймер, который будет вызывать переданный код
     * @param interval, с которым необходимо запускать код task
     * @param task задача, которая будет запускаться
     */
    public GameTimer(int interval, Runnable task) {
        this(interval);
        this.task = task;
    }

    /**
     * По умолчанию, если есть задача task, вызывается она
     * Вам необходимо переопределить этот метод, если вы не передали задачу
     */
    protected void tick() {
        if (task != null) {
            task.run();
        }
    }

    /**
     * Запустить таймер
     */
    public final void start() {
        if (!isRunning) {
            isRunning = true;
            timer.start();
        }
    }

    /**
     * Приостановить работу таймера, если он работал
     */
    public final void pause() {
        if (isRunning && !isPaused) {
            stop();
            isPaused = true;
        }
    }

    /**
     * Продолжить работу таймера, если он был приостановлен
     */
    public final void resume() {
        if (!isRunning && isPaused) {
            start();
            isPaused = false;
        }
    }

    /**
     * Остановить таймер
     */
    public final void stop() {
        if (isRunning) {
            isRunning = false;
            timer.cancel();
        }
    }

}
