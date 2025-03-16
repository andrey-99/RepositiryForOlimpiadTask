import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainUi extends JFrame{

    MainUi(){

        this.setTitle("Редактор");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(1280,650));
        this.setLocationRelativeTo(null);

        jImgOriginal = new JLabel();
        jImgOriginal.setPreferredSize(new Dimension(450,450));
        jImgOriginal.setBorder(BorderFactory.createLineBorder(Color.black));
        jImgOriginal.setHorizontalAlignment(SwingConstants.CENTER);
        jImgOriginal.setVerticalAlignment(SwingConstants.CENTER);
        jImgResult = new JLabel();
        jImgResult.setPreferredSize(new Dimension(450,450));
        jImgResult.setBorder(BorderFactory.createLineBorder(Color.black));
        jImgResult.setHorizontalAlignment(SwingConstants.CENTER);
        jImgResult.setVerticalAlignment(SwingConstants.CENTER);
        jInfo = new JLabel("Введите желаемую степень подсветки 0-255 (рекомендованная-90)");
        jInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        jTimeInfo = new JLabel("Время выполнения алгоритма: ");
        jTimeInfo.setHorizontalAlignment(SwingConstants.CENTER);
        jTimeInfo.setVerticalAlignment(SwingConstants.CENTER);

        jButtonClear = new JButton("Очистить");
        jButtonClear.setFocusable(false);
        jButtonResult = new JButton("Преобразовать");
        jButtonResult.setFocusable(false);

        jTextField = new JTextField("90");

        jFileChooser = new JFileChooser();

        jMenuBar = new JMenuBar();
        jMenuFile = new JMenu("Файл");
        jMenuItemOpen = new JMenuItem("Открыть");
        jMenuItemSaveAs = new JMenuItem("Сохранить результат");
        jMenuBar.add(jMenuFile);
        jMenuFile.add(jMenuItemOpen);
        jMenuFile.add(jMenuItemSaveAs);

        this.setJMenuBar(jMenuBar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridwidth = 8;
        gbc.gridheight = 10;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(jImgOriginal,gbc);

        gbc.gridx = 8;
        gbc.gridy = 0;
        add(jImgResult, gbc);
        //
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,10,0,10);

        gbc.gridheight = 1;

        gbc.gridwidth = 5;
        gbc.gridy = 10;

        gbc.gridx = 0;
        add(jInfo, gbc);

        gbc.gridwidth = 3;

        gbc.gridx = 5;
        add(jTextField, gbc);

        gbc.gridwidth = 4;

        gbc.gridx = 8;
        add(jButtonResult, gbc);

        gbc.gridx = 12;
        add(jButtonClear, gbc);

        gbc.gridheight = 1;
        gbc.gridwidth = 16;
        gbc.insets = new Insets(0,10,10,10);

        gbc.gridy = 11;
        gbc.gridx = 0;
        add(jTimeInfo, gbc);

        pack();

        jMenuItemOpen.addActionListener(_ -> {
            bufferedResultImg = null;
            String defaultPath = new File("files/example.png").getAbsolutePath();
            jFileChooser.setSelectedFile(new File(defaultPath));
            int returnFileChooser = jFileChooser.showOpenDialog(this);
            if (returnFileChooser == JFileChooser.APPROVE_OPTION){
                try {
                    String filePath = jFileChooser.getSelectedFile().getAbsolutePath();
                    Image img = ImageIO.read(new File(filePath));
                    bufferedLoadImg = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
                    Graphics2D bGr1 = bufferedLoadImg.createGraphics();
                    bGr1.drawImage(img, 0, 0, null);
                    bGr1.dispose();
                    jImgOriginal.setIcon(new ImageIcon(resize(jImgOriginal, bufferedLoadImg)));
                    jImgOriginal.repaint();
                    jImgResult.setIcon(new ImageIcon());
                } catch (IOException ex) {
                    new DialogAboutError("Ошибка при открытии файла. Возможно такой файл не существует в этой папке.");
                } catch (NullPointerException ex){
                    new DialogAboutError("Выберите изображения формата .png или .jpg!");
                }
            }
        });

        jButtonResult.addActionListener(_ -> {
            if (bufferedLoadImg != null){
                bufferedResultImg = new BufferedImage(bufferedLoadImg.getWidth(),bufferedLoadImg.getHeight(),BufferedImage.TYPE_INT_RGB);
                bufferedResultImg.setData(bufferedLoadImg.getData());
                try {
                    sila = 255-Integer.parseInt(jTextField.getText());
                    if (sila>-1 && sila<256){
                        double start = System.currentTimeMillis();
                        for (int i = 0; i < bufferedLoadImg.getWidth(); i++) {
                            for (int j = 0; j < bufferedLoadImg.getHeight(); j++) {
                                Color c = new Color(bufferedLoadImg.getRGB(i,j));
                                if (c.getGreen() > sila){
                                    bufferedResultImg.setRGB(i,j,yellow);
                                }
                            }
                        }
                        double finish = System.currentTimeMillis();
                        double time = finish-start;
                        jTimeInfo.setText("Время выполнения алгоритма: " + (int) time + " миллисекунд(а)(ы)");
                        jImgResult.setIcon(new ImageIcon(resize(jImgResult, bufferedResultImg)));
                        jImgResult.repaint();
                    }else {
                        new DialogAboutError("Введите в поле ввода число от 0 до 255!");
                    }
                }catch (NumberFormatException ex){
                    new DialogAboutError("Введите в поле ввода целое число от 0 до 255!");
                }
            }else {
                new DialogAboutError("Сначала откройте изображение! Файл->Открыть");
            }
        });

        jMenuItemSaveAs.addActionListener(_ -> {
            String defaultPath = new File("files/default.png").getAbsolutePath();
            jFileChooser.setSelectedFile(new File(defaultPath));
            if (bufferedResultImg != null){
                int returnFileChooser = jFileChooser.showSaveDialog(this);
                File saveFile = jFileChooser.getSelectedFile();
                if (returnFileChooser == JFileChooser.APPROVE_OPTION){
                    try {
                        ImageIO.write(bufferedResultImg, "png", saveFile);
                    } catch (IOException ex) {
                        new DialogAboutError("Возникла непредвиденная ошибка");
                    }
                }
            }else {
                new DialogAboutError("Создайте изображение, прежде чем сохранить результат!");
            }
        });

        jButtonClear.addActionListener(_ -> {
            bufferedLoadImg = null;
            bufferedResultImg = null;
            jImgOriginal.setIcon(new ImageIcon());
            jImgResult.setIcon(new ImageIcon());
            jTextField.setText("90");
            jTimeInfo.setText("Время выполнения алгоритма: ");
        });
    }

    Image resize(JLabel jl, Image image){
        double widthProp  = (double) jl.getWidth()/image.getWidth(null);
        double heightProp = (double) jl.getHeight()/image.getHeight(null);
        double prop = Math.min(widthProp,heightProp);
        int newWidth  = (int) (image.getWidth(null)*prop);
        int newHeight = (int) (image.getHeight(null)*prop);
        return image.getScaledInstance(newWidth,newHeight,Image.SCALE_SMOOTH);
    }

    int sila;
    int yellow = Color.yellow.getRGB();
    JMenuBar jMenuBar;
    JMenu jMenuFile;
    JMenuItem jMenuItemOpen;
    JMenuItem jMenuItemSaveAs;
    JFileChooser jFileChooser;
    JLabel jImgOriginal;
    JLabel jImgResult;
    JButton jButtonResult;
    JButton jButtonClear;
    JTextField jTextField;
    JLabel jInfo;
    JLabel jTimeInfo;
    BufferedImage bufferedLoadImg;
    BufferedImage bufferedResultImg;
}
