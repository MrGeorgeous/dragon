package ru.gsemenov.dragon;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    protected GameView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Убрать заголовок активности
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        // Запустить активность на полный экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Скрыть меню навигации
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Создать новый игровой графический элемент GameView
        game = new GameView(this);
        // Поставить фон для нашей игры
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            game.setBackground(ResourceManager.BACKGROUND(getApplicationContext()));
        }

        // Поставить наш GameView в качестве корневого графического элемента нашей активности
        setContentView(game);

    }

    /**
     * Когда активность скрывают, необходимо приостановить игру
     */
    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }

    /**
     * Когда активность появляется вновь, необходимо возобновить игру
     */
    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
    }


}