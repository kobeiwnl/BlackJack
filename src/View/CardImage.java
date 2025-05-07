package View;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class CardImage
{
    private Map<String, ImageIcon> cardImages; // Mappa per memorizzare le immagini delle carte
    private ImageIcon backImage; // Immagine del retro della carta
    // Percorso assoluto delle immagini nel tuo progetto
    private static final String IMAGE_PATH = "src/Images/";

    public CardImage()
    {
        cardImages = new HashMap<>();
        loadCardImages(); // Carica tutte le immagini all'avvio
    }

    // Metodo per caricare tutte le immagini
    private void loadCardImages()
    {
        String[] suits = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"};
        String[] values = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};

        // Carica tutte le immagini per ogni combinazione di valore e seme
        for (String suit : suits)
        {
            for (String value : values)
            {
                String cardName = value + " of " + suit;
                String imagePath = IMAGE_PATH + cardName + ".png";
                ImageIcon imageIcon = new ImageIcon(imagePath);

                // Controlla se l'immagine è caricata correttamente
                if (imageIcon.getIconWidth() == -1)
                {
                    //System.err.println("Errore caricamento immagine: " + imagePath);
                } else
                {
                    cardImages.put(cardName, imageIcon); // Salva l'immagine nella mappa
                    //System.out.println("Immagine caricata correttamente: " + imagePath);
                }
            }
        }
        // Carica una sola volta l'immagine del retro della carta
        String backImagePath = IMAGE_PATH + "BACK.png";
        backImage = new ImageIcon(backImagePath);
    }

    // Metodo per ottenere l'immagine di una carta specifica
    public ImageIcon getCardImage(String value, String suit)
    {
        String cardName = value.toUpperCase() + " of " + suit.toUpperCase();
        return cardImages.getOrDefault(cardName, null); // Restituisce l'immagine o null se non trovata
    }

    // Metodo per ottenere l'immagine del retro della carta
    public ImageIcon getBackImage()
    {
        return backImage; // Restituisce sempre la stessa immagine per il retro della carta
    }

}
