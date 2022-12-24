package ru.gsemenov.dragon.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Класс, представляющий набор текстур сущности
 * Используется для анимации
 *
 * Чтобы получить текстуру из файла, необходимо использовать класс SpriteTexture.Builder
 * Добавьте линии внутри файла с помощью метода addLine(n),
 * где n - количество фреймов очередной линии.
 * Затем вызовите метод build(), чтобы получить набор фреймов SpringTexture
 *
 */
public class SpriteTexture {

    protected Bitmap[][] frames;
    protected Rect[][] nonTransparentRects;

    private SpriteTexture() {
    }

    /**
     * Получить i-ый фрейм в линии line
     */
    public Bitmap getFrame(int line, int i) {
        return frames[line][i];
    }

    /**
     * Получить непрозрачную границу i-ого фрейма в линии line
     */
    public Rect getNonTransparentRect(int line, int i) {
        return nonTransparentRects[line][i];
    }

    /**
     * Получить количество линий
     */
    public int countLines() {
        return frames.length;
    }

    /**
     * Получить количество фреймов внутри i-ой линии
     */
    public int countFramesInLine(int i) {
        return frames[i].length;
    }

    public static class Builder {

        private static final int MAX_LINES = 128;

        protected final Bitmap bitmap;
        private final SpriteTexture texture;

        private final int[] lines = new int[MAX_LINES];
        private int linesCount = 0;

        public Builder(Bitmap texture) {
            this.bitmap = texture;
            this.texture = new SpriteTexture();
        }

        public Builder addLine(int n) {
            if (linesCount >= MAX_LINES) {
                throw new RuntimeException("Слишком много линий");
            }
            lines[linesCount++] = n;
            return this;
        }

        public SpriteTexture build() {
            // Найдем количество фреймов на нашей картинке
            int yCount = linesCount;
            int xCount = 0;
            for (int i = 0; i < linesCount; i++) {
                if (xCount < lines[i]) {
                    xCount = lines[i];
                }
            }
            // Разрежем картинку на xCount x yCount фреймов
            Bitmap[][] frames = BitmapUtil.splitBitmap(bitmap, xCount, yCount);
            // Добавим фреймы по линиям в нашу текстуру
            texture.frames = new Bitmap[yCount][];
            texture.nonTransparentRects = new Rect[yCount][];
            for (int i = 0; i < linesCount; i++) {
                int frameInLine = lines[i];
                texture.frames[i] = new Bitmap[frameInLine];
                texture.nonTransparentRects[i] = new Rect[frameInLine];
                for (int j = 0; j < frameInLine; j++) {
                    texture.frames[i][j] = frames[j][i];
                    texture.nonTransparentRects[i][j] = BitmapUtil.nonTransparentBorder(texture.frames[i][j]);
                }
            }
            // вернем текстуру с фреймами
            return texture;
        }

    }

}
