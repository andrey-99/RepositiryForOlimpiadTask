import javax.swing.*;
import java.awt.*;

public class DialogAboutError extends JDialog {

    DialogAboutError(String message){

        this.message = message;

        this.setTitle("Ошибка");
        this.setMinimumSize(new Dimension(550,120));
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jInfo = new JLabel(message);
        jInfo.setHorizontalAlignment(SwingConstants.CENTER);
        jInfo.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(jInfo, gbc);

        pack();
        this.setVisible(true);
    }

    String message;
    JLabel jInfo;

}