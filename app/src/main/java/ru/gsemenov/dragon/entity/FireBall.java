package ru.gsemenov.dragon.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import ru.gsemenov.dragon.R;
import ru.gsemenov.dragon.ResourceManager;

public class FireBall extends Entity {

    public static Bitmap texture = null;
    public static final int RADIUS = 40;

    public FireBall(Context context) {
        if (texture == null) {
            texture = ResourceManager.FIREBALL(context, RADIUS);
        }
    }

    @Override
    public void spawn(int x, int y) {
        super.spawn(x, y);
        setSpeed(-20, 0);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!alive) {
            return;
        }
        canvas.drawBitmap(texture, x - RADIUS / 2, y - RADIUS / 2, paint);
    }

    @Override
    public void tick() {
        if (x < 0) {
            alive = false;
            return;
        }
        super.tick();
    }

    @Override
    public Rect occupiedRectangle() {
        return new Rect(x - RADIUS / 2, y - RADIUS / 2,
                x + RADIUS / 2, y + RADIUS / 2);
    }

}
