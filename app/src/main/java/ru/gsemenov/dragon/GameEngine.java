package ru.gsemenov.dragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import ru.gsemenov.dragon.entity.FireBall;
import ru.gsemenov.dragon.entity.FlyingEntity;
import ru.gsemenov.dragon.interfaces.IGame;
import ru.gsemenov.dragon.util.GameTimer;
import ru.gsemenov.dragon.util.SoundPlayer;
import ru.gsemenov.dragon.util.WaitAndRunTask;

public class GameEngine implements IGame {

    /**
     * Генератор случайных чисел
     * <p>
     * Сгенерированный уровень будет полностью зависеть от числа seed,
     * который вы передадите в конструктор
     */
    public final Random rnd = new Random(400);
    public final FireBall[] fireBalls = new FireBall[8];
    /**
     * Размеры игрового поля (Width - ширина, Height - высота)
     */
    public int W, H;
    /**
     * Таймер создания огненных шаров
     * <p>
     * Каждые 700 мс с вероятностью запускает новый огненный шар на случайной координате
     */
    protected final GameTimer spawnTimer = new GameTimer(700, () -> {
        for (FireBall fireBall : fireBalls) {
            if (!fireBall.isAlive()) {
                fireBall.spawn(W, rnd.nextInt(H));
                return;
            }
        }
    });
    /**
     * Текущее пройденное расстояние и максимальное расстояние за все игры
     */
    public int distance = 0, maxDistance = 0;
    /**
     * Игровые сущности
     */
    public FlyingEntity dragon = null;
    /**
     * Таймер анимации
     * <p>
     * Каждые 250 мс обновляет кадр дракона
     */
    protected final GameTimer animationTimer = new GameTimer(250, () -> {
        dragon.nextFrame();
    });
    /**
     * Игровые ресурсы (шрифты, звуки и пр.)
     */
    protected SoundPlayer backgroundMusic;
    protected SoundPlayer blastSound;
    protected Paint fontPaint;
    private boolean isPaused = false;
    /**
     * Задача по перезапуску игры после проигрыша
     * <p>
     * Вызывается при проигрыше, ждет промежуток времени и выполняет код внутри
     */
    protected final WaitAndRunTask restartTask = new WaitAndRunTask(1000, () -> {
        startGame();
    });

    /**
     * Конструктор игры
     *
     * @param context активности, в которой создана наша игра
     * @param W       ширина игрового поля
     * @param H       высота игрового поля
     */
    public GameEngine(Context context, int W, int H) {
        this.W = W;
        this.H = H;
        initResources(context);
    }

    /**
     * Вспомогательный метод для инициализации необходимых игре ресурсов (мобы, звуки, текстуры и пр.)
     * @param context, из которого будут браться необходимые ресурсы из папки res
     */
    void initResources(Context context) {
        dragon = new FlyingEntity(ResourceManager.DRAGON(context));
        dragon.setLimits(new Rect(160, 50, 240, H - 400));
        for (int i = 0; i < fireBalls.length; i++) {
            fireBalls[i] = new FireBall(context);
        }
        fontPaint = ResourceManager.FONT(context);
        backgroundMusic = new SoundPlayer(context, R.raw.eggy_toast_condemned);
        blastSound = new SoundPlayer(context, R.raw.mixkit_pixel_chiptune_explosion);
    }

    /**
     * Что должно произойти за один игровой такт
     */
    void tick() {
        if (restartTask.isRunning()) {
            return;
        }
        distance++;
        dragon.tick();
        for (FireBall fireBall : fireBalls) {
            fireBall.tick();
            if (fireBall.intersects(dragon)) {
                crash();
            }
        }
    }

    /**
     * Что должно произойти при столкновении
     */
    public void crash() {
        blastSound.play();
        for (FireBall fireBall : fireBalls) {
            fireBall.kill();
        }
        stopGame();
        restartTask.start();
    }

    /**
     * Нарисовать игровое поле на холсте
     *
     * @param canvas           холст
     * @param drawInRectangles если установлен true, то вместо текстур необходимо использовать прямоугольники
     */
    public void draw(Canvas canvas, boolean drawInRectangles) {
        if (drawInRectangles) {
            dragon.drawAsRectangle(canvas);
            for (FireBall fireBall : fireBalls) {
                fireBall.drawAsRectangle(canvas);
            }
        } else {
            dragon.draw(canvas);
            for (FireBall fireBall : fireBalls) {
                fireBall.draw(canvas);
            }
        }
        canvas.drawText("" + distance, W - 400, 100, fontPaint);
        canvas.drawText("max: " + maxDistance, W - 400, 180, fontPaint);
    }


    /**
     * Запустить игру
     */
    public void startGame() {
        distance = 0;
        isPaused = false;
        animationTimer.start();
        spawnTimer.start();
        backgroundMusic.play();
        dragon.spawn(200, H / 2);
    }

    /**
     * Восстановить игру после паузы, если она была восстановлена
     */
    public void resumeGame() {
        if (!isPaused) {
            return;
        }
        isPaused = false;
        spawnTimer.resume();
        animationTimer.resume();
        backgroundMusic.resume();
        restartTask.resume();
    }

    /**
     * Приостановить игру
     */
    public void pauseGame() {
        if (isPaused) {
            return;
        }
        isPaused = true;
        restartTask.pause();
        backgroundMusic.pause();
        animationTimer.pause();
        spawnTimer.pause();
    }

    /**
     * Остановить игру
     */
    public void stopGame() {
        maxDistance = Math.max(maxDistance, distance);
        backgroundMusic.stop();
        animationTimer.stop();
        spawnTimer.stop();
    }

}
