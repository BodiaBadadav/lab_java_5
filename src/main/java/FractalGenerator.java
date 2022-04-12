import java.awt.geom.Rectangle2D;

public abstract class FractalGenerator {

    /**
     * Этот метод принимает целочисленную координату и преобразует ее
     * в более точное значение, соответствующее определенному диапазону.
     * Он  используется для преобразования координат пикселей в
     * значения для вычисления фракталов
     *
     *
     * @param rangeMin - минимальное значение
     *
     * @param rangeMax - максимальнео значение диапазона
     *
     * @param size размер измерения, из которого берется координата пикселя.
     * Например, это может быть ширина изображения или высота изображения.
     *
     * @param coord - это координата.
     *        Координата должна находиться в диапазоне [0, size].
     */
    public static double getCoord(double rangeMin, double rangeMax,
                                  int size, int coord) {

        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }


    /**
     * Задает указанный прямоугольник, содержащий начальный диапазон,
     * подходящий для создаваемого фрактала.
     */
    public abstract void getInitialRange(Rectangle2D.Double range);


    /**
     * Обновляет текущий диапазон, который должен быть центрирован по указанным координатам
     * и увеличен или уменьшен на указанный коэффициент масштабирования.
     */
    public void recenterAndZoomRange(Rectangle2D.Double range,
                                     double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }


    /**
     * Учитывая координаты <em>x</em> + <em>iy</em> в комплексной плоскости,
     * вычисляет и возвращает количество итераций, прежде чем фрактальная
     * функция покинет ограничивающую область для этой точки.
     */
    public abstract int numIterations(double x, double y);

}