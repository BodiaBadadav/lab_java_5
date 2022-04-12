import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    /**
     * Константа для количества максимальных итераций.
     */
    public static final int MAX_ITERATIONS = 2000;

    /**
     * Этот метод позволяет генератору фракталов указать, какая часть
     * комплексной плоскости наиболее интересена для фрактала.
     * Ему передается объект прямоугольника, и метод изменяет
     * поля прямоугольника, чтобы показать правильный начальный диапазон для фрактала.
     * Эта реализация устанавливает начальный диапазон в (-2 - 1.5i) - (1 + 1.5i)
     * или x = -3, y = -1,7, width = height = 4.
     */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /**
     * Этот метод реализует итерационную функцию для фрактала Мандельброта.
     * Требуется два числа double для действительной и мнимой частей комплекса
     * plane и возвращаетcz количество итераций для соответствующей
     * координаты.
     */
    public int numIterations(double x, double y)
    {
        /** начло всех итераций с 0. */
        int iteration = 0;
        /** Инициализируем zreal и zimaginary. */
        double zreal = 0;
        double zimaginary = 0;

        /**
         * Рассчитайте Zn = Zn-1^2 + c, где значения — это комплексные числа,
         * представленные zreal и zimaginary, Z0 = 0, а c — особая точка в показываемом
         * нами фрактале (с учетом x и y). Он повторяется до Z^2 > 4 (абсолютное значение Z больше 2)
         * или до достижения максимального количества итераций.
         */
        while (iteration < MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * zreal * zimaginary + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }
        /**
         * Если количество максимальных итераций достигнуто, возвращаем -1, чтобы
         * указать, что точка не вышла за границу.
         */
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }

    /**
     * Реализация toString() в этой реализации фрактала. Возвращает
     * название фрактала: «Мандельброт».
     */
    public String toString() {
        return "Мандельброт";
    }
}
