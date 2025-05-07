package Model;

public class Dealer extends BlackJackParticipant
{
    /**
     * Costruttore che inizializza un dealer con una mano vuota.
     */
    public Dealer()
    {
        super(); // Chiama il costruttore della superclasse
    }

    /**
     * Pesca una carta dal mazzo e la aggiunge alla mano del dealer.
     *
     * @param deck Il mazzo da cui pescare la carta.
     */
    public void draw(Deck deck)
    {
        hand.add(deck.drawCard());
    }

    /**
     * Aggiunge una carta direttamente alla mano del dealer.
     *
     * @param card La carta da aggiungere.
     */
    public void addCard(Card card)
    {
        hand.add(card);
    }


    /**
     * Pulisce la mano del dealer per iniziare una nuova partita.
     */
    public void clearHand()
    {
        hand.clear();
    }
}
