import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.*;
import javax.swing.JOptionPane;

public class FractalExplorer {
    /** Целочисленный размер отображения - это ширина и высота отображения в пикселях. **/
    private int displaySize;

    /**
     * Ссылка JImageDisplay для обновления отображения с помощью различных методов как,
     * таких как вычиследние фракталов.
     */
    private JImageDisplay display;

    /** Объект FractalGenerator для каждого типа фрактала. **/
    private FractalGenerator fractal;

    /**
     * Объект Rectangle2D.Double, который определяет диапазон
     * того, что мы в настоящее время показываем.
     */
    private Rectangle2D.Double range;

    /**
     * Конструктор, который принимает размер дисплея, сохраняет его
     * и инициализирует объекты диапазона и генератора фракталов.
     */
    public FractalExplorer(int size) {
        /** Размер дисплея  **/
        displaySize = size;

        /** Инициализирует фрактальный генератор и объекты диапазона. **/
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    /**
     * Этот метод инициализирует графический интерфейс Swing с помощью JFrame,
     * содержащего объект JImageDisplay и кнопку для очистки дисплея.
     */
    public void createAndShowGUI() {
        /** Установка frame для использование java.awt.BorderLayout для содержимого. **/
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");
        myFrame.add(display, BorderLayout.CENTER);

        /** Создаение кнопки очистки. **/
        JButton resetButton = new JButton("Обновить");
        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);
        myFrame.add(resetButton, BorderLayout.SOUTH);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /** Создание списка выбора фракталов **/
        JComboBox myComboBox = new JComboBox();
        FractalGenerator MandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(MandelbrotFractal);
        FractalGenerator TricornFractal = new Tricorn();
        myComboBox.addItem(TricornFractal);
        FractalGenerator BurningShipFractal = new BurningShip();
        myComboBox.addItem(BurningShipFractal);
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        /** Создание новых объектов JPanel */
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Фрактал:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);
        JButton saveButton = new JButton("Сохранить");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        /** Размещаем содержимое фрейма, делаем его видимым и
         * запрещаем изменение размера окна.  */
        myFrame.setTitle("Фракталы");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        myFrame.setBounds(dimension.width/2 - 300,dimension.height/2 - 300, 550, 550);
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }

    /** метод для вывода фрактала на экран */
    private void drawFractal() {

        /**Проходим через каждый пиксель на дисплее **/
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {

                /** Находим соответствующие координаты xCoord и yCoord
                 * в области отображения фрактала. */
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                /** Вычисляем количество итераций для координат в области отображения фрактала. */
                int iteration = fractal.numIterations(xCoord, yCoord);

                /** Если число итераций равно -1, установите для пикселя черный цвет.**/
                if (iteration == -1) {
                    display.drawPixel(x, y, 0);
                }

                /** В противном случае выбераем значение оттенка на основе числа итераций.*/
                else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    /** Обновляем дисплей цветом для каждого пикселя. **/
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }

        /** Когда все пиксели будут нарисованы, перекрасим JImageDisplay, чтобы он соответствовал
         * текущему содержимому его изображения. */
        display.repaint();
    }

    /**
     *  внутренний класс для обработки событий ActionListener от кнопки сброса
     */
    private class ResetHandler implements ActionListener
    {
        /**
         * метод для сброса до начального диапазона
         */
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }

    /** внутренний класс для обработки событий MouseListener с дисплея */
    private class MouseHandler extends MouseAdapter {

        /** Когда обработчик получает событие щелчка мыши, он отображает
         * координаты щелчка пикселя в области отображаемого фрактала,
         * а затем вызывает метод генератора RecenterAndZoomRange()
         * с координатами щелчка и масштабом 0,5 */
        @Override
        public void mouseClicked(MouseEvent e) {

            /** Получаем координату x области отображения щелчка мыши. **/
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            /** Получаем координату y области отображения щелчка мышью. **/
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            /** Вызывааем метод генератора RecenterAndZoomRange() с помощью
             * координатам, по которым был выполнен щелчок, и шкала 0,5. */
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            /** Перерисовываем фрактал после изменения отображаемой области. */
            drawFractal();
        }
    }

    /**
     * внутренний класс для обработки событий ActionListener
     */
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
            }
            else if (action.equals("Обновить")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            else if (action.equals("Сохранить")) {
                JFileChooser myFileChooser = new JFileChooser();
                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                myFileChooser.setAcceptAllFileFilterUsed(false);

                int userSelection = myFileChooser.showSaveDialog(display);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();
                    try {
                        BufferedImage displayImage = display.getDisplayImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(display, exception.getMessage() + exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }

    /** метод для запуска FractalExplorer */
    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(550);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}