package ru.gsemenov.dragon.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Arrays;

/**
 * Вспомогательные классы для работы с изображениями
 */
public class BitmapUtil {

    /**
     * Разрезать картинку на прямоугольники
     * @param bitmap исходная картинка
     * @param xCount количество вертикальных полосок
     * @param yCount количество горизонтальных полосок
     * @return двумерный массив разрезанных картинок размера xCount x yCount
     */
    public static Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        Bitmap[][] bitmaps = new Bitmap[xCount][yCount];
        int width, height;
        width = bitmap.getWidth() / xCount;
        height = bitmap.getHeight() / yCount;
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
            }
        }
        return bitmaps;
    }

    /**
     * Определяет истинный размер картинки, без лишних прозрачных пикселей по границам. <br>
     * Позволяет избавиться от ненужного прозрачного ободка в текстурах, когда дело касается
     * реально занимаемых размеров на экране персонажем
     * @param bitmap картинка
     * @return прямоугольник с относительными координатами
     */
    public static Rect nonTransparentBorder(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] empty = new int[width];
        int[] buffer = new int[width];
        Arrays.fill(empty, 0);
        int top = 0;
        int left = 0;
        int bottom = height;
        int right = width;

        for (int y = 0; y < height; y++) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                top = y;
                break;
            }
        }

        for (int y = height - 1; y > top; y--) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                bottom = y;
                break;
            }
        }

        empty = new int[height];
        buffer = new int[height];
        Arrays.fill(empty, 0);

        for (int x = 0; x < width; x++) {
            bitmap.getPixels(buffer, 0, 1, x, 0, 1, height);
            if (!Arrays.equals(empty, buffer)) {
                left = x;
                break;
            }
        }

        for (int x = width - 1; x > left; x--) {
            bitmap.getPixels(buffer, 0, 1, x, 0, 1, height);
            if (!Arrays.equals(empty, buffer)) {
                right = x;
                break;
            }
        }

        return new Rect(left, top, right, bottom);
    }

}
