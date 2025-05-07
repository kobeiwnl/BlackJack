package Model;

/**
 * Enum per rappresentare il valore di una carta nel gioco del BlackJack.
 * Ogni valore ha un corrispondente valore numerico utilizzato per il calcolo del punteggio.
 *
 * - Le carte numeriche hanno il loro valore nominale.
 * - Le figure (JACK, QUEEN, KING) valgono 10 punti.
 * - L'ASSO (ACE) può valere 1 o 11, a seconda del contesto di gioco.
 */
public enum Value
{
    ACE(1),      // Può valere 1 o 11 a seconda del contesto
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    /** Valore numerico della carta. */
    private final int value;

    /**
     * Costruttore per l'enum Value.
     *
     * @param value Valore numerico associato alla carta.
     */
    Value(int value)
    {
        this.value = value;
    }

    /**
     * Restituisce il valore numerico della carta.
     *
     * @return Il valore della carta.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Restituisce una rappresentazione leggibile del valore della carta.
     *
     * @return Nome della carta con la prima lettera maiuscola.
     */
    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
