package ru.gsemenov.dragon.util;

/**
 * Класс, позволяющий совершить задачу спустя какое-то время задержки
 */
public class WaitAndRunTask {

    private Thread thread = null;
    private final Runnable task;
    private final int timeToWait;

    /**
     * Конструктор задачи
     * @param timeToWait время, в течение которого нужно задержаться
     * @param task код, который необходимо выполнить
     */
    public WaitAndRunTask(int timeToWait, Runnable task) {
        this.task = task;
        this.timeToWait = timeToWait;
    }

    /**
     * Запустить задачу с задержкой
     */
    public synchronized void start() {
        if (thread != null && thread.isAlive()) {
            return;
        }
        thread = new Thread(() -> {
            synchronized (WaitAndRunTask.this) {
                while (true) {
                    try {
                        Thread.sleep(timeToWait);
                        break;
                    } catch (InterruptedException e) {
                        try {
                            wait();
                        } catch (InterruptedException ignored) {}
                    }
                }
            }
            task.run();
        });
        thread.start();
    }

    /**
     * Приостановить ожидание
     */
    public void pause() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    /**
     * Продолжить ожидание
     */
    public void resume() {
        if (thread != null && thread.isAlive()) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Узнать, происходит ли прямо сейчас ожидание
     */
    public boolean isRunning() {
        return thread != null && thread.isAlive();
    }

}
