package Model;

import java.util.Objects;
/**
 * Rappresenta una carta da gioco con un valore e un seme.
 * Le carte sono immutabili dopo la creazione.
 */
public class Card
{
    private final Suit suit; // Seme della carta (es. CUORI, QUADRI, FIORI, PICCHE)
    private final Value value; // Valore della carta (es. ASSO, RE, REGINA, JACK)

    /**
     * Costruisce una nuova carta con un seme e un valore specifico.
     *
     * @param suit  Il seme della carta (es. HEARTS, DIAMONDS, CLUBS, SPADES).
     * @param value Il valore della carta (es. ACE, KING, QUEEN, JACK, TWO...).
     */
    public Card(Suit suit, Value value)
    {
        this.suit = suit; //Inizializza il seme della carta
        this.value = value; //Inizializza il valore della carta
    }

    /**
     * Restituisce il seme della carta.
     *
     * @return Il seme della carta (Suit).
     */
    public Suit getSuit()
    {
        return suit;
    }

    /**
     * Restituisce il valore della carta.
     *
     * @return Il valore della carta (Value).
     */
    public Value getCardValueType()
    {
        return value;
    }

    /**
     * Controlla se due carte sono uguali confrontando il seme e il valore.
     *
     * @param obj L'oggetto da confrontare.
     * @return true se le carte hanno lo stesso seme e valore, false altrimenti.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        Card card = (Card) obj;
        return suit == card.suit && value == card.value;
    }

    /**
     * Genera una rappresentazione testuale della carta, es. "ACE of HEARTS".
     *
     * @return Una stringa che rappresenta la carta nel formato "Valore of Seme".
     */
    @Override
    public String toString()
    {
        return value + " of " + suit;
    }

    /**
     * Genera un valore hash per la carta, coerente con il metodo equals().
     *
     * @return Il valore hash della carta.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(suit,value);
    }

}
