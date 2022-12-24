package ru.gsemenov.dragon.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import ru.gsemenov.dragon.interfaces.IDrawable;

public abstract class Entity implements IDrawable {

    protected final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected volatile int x, y;
    protected volatile int vx = 0, vy = 0;
    protected boolean alive = false;

    public int getVerticalCenter() { return y; }
    public int getHorizontalCenter() { return x; }

    /**
     * Поместить игровую сущность на игровое поле с координатами
     * @param x координата
     * @param y координата
     */
    public void spawn(int x, int y) {
        this.vx = 0;
        this.vy = 0;
        this.x = x;
        this.y = y;
        alive = true;
    }

    /**
     * Проверить, находится ли сущность на игровом поле
     * @return true, если сущность находится на игровом поле, false - если нет
     */
    public final boolean isAlive() {
        return alive;
    }

    /**
     * Убрать сущность с игрового поля
     */
    public void kill() {
        alive = false;
    }

    /**
     * Отрисовать сущность на холсте
     * @param canvas холст, на котором необходимо отрисовать сущность
     */
    public abstract void draw(Canvas canvas);

    /**
     * Отрисовать сущность на холсте как прямоугольник
     *
     * Может использоваться для проверки границ, занимаемых сущностью
     * @param canvas холст, на котором необходимо отрисовать сущность
     */
    public final void drawAsRectangle(Canvas canvas) {
        if (alive) {
            canvas.drawRect(occupiedRectangle(), paint);
        }
    }

    /**
     * Границы игрового поля, в пределах которых сущность может находиться
     */
    protected Rect limits = new Rect(-20, -20, Integer.MAX_VALUE, Integer.MAX_VALUE);

    /**
     * Установить границы игрового поля, в пределах которых сущность может находиться
     * @param limits прямоугольник, за который сущность не может выходить
     */
    public void setLimits(Rect limits) {
        this.limits = limits;
    }

    /**
     * Может ли сущность пойти из текущего местоположения в точку (toX, toY)
     * @param toX координата x
     * @param toY координата y
     * @return
     */
    public boolean canGo(int toX, int toY) {
        return limits.contains(toX, toY);
    }

    /**
     * Установить текущую скорость сущности
     * @param vx горизонтальная скорость
     * @param vy вертикальная скорость
     */
    public void setSpeed(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * Что должно произойти с сущностью за один такт игры
     */
    public void tick() {
        if (canGo(x + vx, y + vy)) {
            this.x += this.vx;
            this.y += this.vy;
        } else if (canGo(x, y + vy)) {
            this.y += this.vy;
        } else if (canGo(x + vx, y)) {
            this.x += this.vx;
        }
    }

    /**
     * Пересекается ли сущность с другой сущностью
     * @param entity другая сущность
     * @return true, если пересекаются, false - если не пересекаются
     */
    public boolean intersects(Entity entity) {
        if (!this.alive || !entity.alive) {
            return false;
        }
        return Rect.intersects(this.occupiedRectangle(), entity.occupiedRectangle());
    }

    /**
     * Занимаемый сущностью прямоугольник, использующийся при вычислении столкновений
     * @return Rect, содержащий границы, занимаемые сущностью
     */
    public Rect occupiedRectangle() {
        return new Rect(x, y, x, y);
    }

}
