package ru.gsemenov.dragon.entity;

import android.graphics.Canvas;
import android.graphics.Rect;

import ru.gsemenov.dragon.util.SpriteTexture;

public class FlyingEntity extends Entity {

    private final SpriteTexture texture;
    private final int N_ANIMATION_STATES;
    private int state = 0;

    public FlyingEntity(SpriteTexture texture) {
        assert(texture.countLines() > 0);
        this.texture = texture;
        this.N_ANIMATION_STATES = texture.countFramesInLine(0);
    }

    @Override
    public int getVerticalCenter() {
        return occupiedRectangle().centerY();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!alive) { return; }
        canvas.drawBitmap(texture.getFrame(0, state), x, y, paint);
    }

    public void nextFrame() {
        if (state + 1 == N_ANIMATION_STATES) {
            state = 0;
        } else {
            state++;
        }
    }

    @Override
    public Rect occupiedRectangle() {
        Rect t = texture.getNonTransparentRect(0, state);
        return new Rect(x + t.left, y + t.top, x + t.right, y + t.bottom);
    }

}
