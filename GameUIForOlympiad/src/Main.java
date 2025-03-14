import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args){

        MainUi mainUi = new MainUi();

        try {
            String filePath = "files/example.png";
            Image img = ImageIO.read(new File(filePath));
            mainUi.bufferedLoadImg = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
            Graphics2D bGr1 = mainUi.bufferedLoadImg.createGraphics();
            bGr1.drawImage(img, 0, 0, null);
            bGr1.dispose();
            mainUi.jImgOriginal.setIcon(new ImageIcon(mainUi.resize(mainUi.jImgOriginal, mainUi.bufferedLoadImg)));
            mainUi.jImgOriginal.repaint();
            mainUi.jImgResult.setIcon(new ImageIcon());
        } catch (IOException ex) {
            new DialogAboutError("Ошибка при открытии файла. Возможно файл не существует в этой папке.");
        } catch (NullPointerException ex){
            new DialogAboutError("Выберите изображения формата .png или .jpg!");
        }
        mainUi.jButtonResult.doClick();
        mainUi.jButtonClear.doClick();

        mainUi.setVisible(true);
    }
}

