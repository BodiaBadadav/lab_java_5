import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

/**
 * Класс для отображения фракталов
 */
class JImageDisplay extends JComponent {
    /**
     * Экземпляр буферизованного изображения.
     * Объект класса BufferedImage
     * Правляет изображением, содержимое которого мы можем писать.
     */
    private BufferedImage displayImage;

    /**
     * Конструктор принимает целые значения ширины и высоты и инициализирует его
     * Объект BufferedImage должен быть новым изображением с такой шириной и высотой
     * типа TYPE_INT_RGB.
     */
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        /**
         * Вызваем метод setPreferredSize() родительского класса
         * с заданной шириной и высотой.
         * метод для отображения на экране изображения
         */
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);
    }

    /**
     * Реализация суперкласса paintComponent(g) вызывает так, что границы и
     * черты нарисованы правильно. Затем изображение втягивается в компонент.
     * @Override - переопределение
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(),
                displayImage.getHeight(), null);
    }

    /**
     * Устанавливает все пиксели в данных изображения черными.
     */
    public void clearImage() {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }

    /**
     * Устанавливает пиксель определенного цвета.
     */
    public void drawPixel(int x, int y, int rgbColor) {

        displayImage.setRGB(x, y, rgbColor);
    }

    /**
     *  Метод для получения изображения из другого класса
     */
    public BufferedImage getDisplayImage() {
        return displayImage;
    }
}

