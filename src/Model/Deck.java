package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * La classe Deck rappresenta un mazzo di carte utilizzato nel gioco del Blackjack.
 * Il mazzo può essere composto da più mazzi di carte standard (tra 4 e 8).
 * Supporta operazioni di mescolamento, estrazione di carte e gestione delle carte rimanenti.
 */
public class Deck
{
    private final List<Card> cards; // Lista di carte del mazzo
    private final int numDecks; // Numero di mazzi inclusi nel deck

    /**
     * Costruttore della classe Deck.
     * Crea un mazzo contenente un numero specificato di mazzi standard.
     *
     * @param numDecks Il numero di mazzi standard da includere (deve essere tra 4 e 8).
     * @throws IllegalArgumentException se il numero di mazzi è inferiore a 4 o superiore a 8.
     */
    public Deck(int numDecks)
    {
        if (numDecks < 4 || numDecks > 8)
        {
            throw new IllegalArgumentException("Number of decks must be between 4 and 8");
        }
        this.numDecks = numDecks; // Salva il numero di mazzi
        cards = new ArrayList<>();

        // Popola il mazzo con numDecks mazzi standard
        refillDeck();
        shuffle();
    }

    /**
     * Aggiunge una carta specifica al mazzo.
     *
     * @param card La carta da aggiungere al mazzo.
     */
    public void addCardDeck(Card card)
    {
        cards.add(card);
    }

    /**
     * Mescola il mazzo di carte casualmente.
     */
    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    /**
     * Pesca una carta dalla cima del mazzo.
     * Se il mazzo è vuoto, viene ricreato e rimescolato prima di restituire una carta.
     *
     * @return La carta pescata dal mazzo.
     */
    public Card drawCard()
    {
        if (cards.isEmpty())
        {
            refillDeck(); // Ricrea il mazzo
            shuffle();    // Mescola il mazzo
        }
        return cards.remove(0);
    }

    /**
     * Ricrea il mazzo con un set completo di carte e lo svuota prima di riempirlo nuovamente.
     */
    private void refillDeck()
    {
        cards.clear();

        for (int i = 0; i < numDecks; i++) // Ora usa numDecks memorizzato
        {
            for (Suit suit : Suit.values())
            {
                for (Value value : Value.values())
                {
                    cards.add(new Card(suit, value));
                }
            }
        }
    }

    /**
     * Restituisce il numero di carte rimanenti nel mazzo.
     *
     * @return Il numero di carte ancora presenti nel mazzo.
     */
    public int cardsRemaining()
    {
        return cards.size();
    }

    /**
     * Restituisce una copia della lista delle carte ancora disponibili nel mazzo.
     *
     * @return Una lista di carte rimanenti nel mazzo.
     */
    public List<Card> getRemainingCards()
    {
        return new ArrayList<>(cards);
    }

    /**
     * Restituisce una rappresentazione testuale del mazzo di carte.
     *
     * @return Una stringa contenente tutte le carte del mazzo separate da una nuova riga.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards)
        {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString().trim();
    }
}
