package View;
import javax.swing.*;
import java.awt.*;

public class BackGroundPanel extends JPanel
{
    private Image backgroundImage;

    public BackGroundPanel(String filePath)
    {
        try
        {
            backgroundImage = new ImageIcon(filePath).getImage();
        } catch (Exception e)
        {
            System.out.println("Impossibile caricare l'immagine: " + filePath);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (backgroundImage != null)
        {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}