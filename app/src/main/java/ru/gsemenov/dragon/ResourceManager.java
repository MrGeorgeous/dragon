package ru.gsemenov.dragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import ru.gsemenov.dragon.util.SpriteTexture;

/**
 * Вспомогательный класс для получения текстур, шрифтов и пр.
 */
public class ResourceManager {

    public static SpriteTexture DRAGON(Context context) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_dragon_right);
        return new SpriteTexture.Builder(bmp).addLine(3).build();
    }

    public static Paint FONT(Context context) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTypeface(context.getResources().getFont(R.font.pixeloid_sans));
        p.setTextSize(70.0f);
        p.setColor(Color.parseColor("#dd1111"));
        return p;
    }

    public static Drawable BACKGROUND(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.cave);
    }

    public static Bitmap FIREBALL(Context context, int radius) {
        Bitmap texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball);
        return Bitmap.createScaledBitmap(texture, radius, radius, false);
    }


}
