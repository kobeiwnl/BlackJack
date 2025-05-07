package Tests;
import View.CardImage;

import javax.swing.*;
import java.awt.*;
// ImageTest è una classe di test che carica l'immagine del retro della carta e la visualizza in un frame.
public class ImageTest
{
    public static void main(String[] args)
    {
        // Crea il frame
        JFrame frame = new JFrame("Test Immagine Carta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400); // Dimensioni della finestra

        // Crea un'istanza della classe CardImage per caricare l'immagine
        CardImage cardImageManager = new CardImage();

        // Prova a caricare il retro della carta
        ImageIcon backImage = cardImageManager.getBackImage();

        // Verifica se l'immagine è stata caricata correttamente
        if (backImage == null || backImage.getIconWidth() == -1)
        {
            System.err.println("Errore: Immagine del retro della carta non trovata o non caricata correttamente.");
        } else
        {
            System.out.println("Immagine del retro della carta caricata correttamente.");
        }

        // Crea un'etichetta con l'immagine e aggiungila al frame
        JLabel label = new JLabel(backImage);
        frame.add(label, BorderLayout.CENTER);

        // Mostra il frame
        frame.setVisible(true);
    }
}
