package Model;

/**
 * La classe AiPlayer rappresenta un giocatore controllato dal computer nel Blackjack.
 */
public class AiPlayer extends BasePlayer
{
    private String nickname;

    /**
     * Costruttore per creare un AI Player con un nickname.
     *
     * @param nickname Nome dell'AI Player.
     */
    public AiPlayer(String nickname)
    {
        super();
        this.nickname = nickname;
        this.chips=1000;
        this.betamount=0;
    }

    public String getNickname()
    {
        return nickname;
    }

    public int getChips()
    {
        return chips;
    }

    public void setChips(int chips)
    {
        this.chips = chips;
    }

    public void winChips(int amount)
    {
        chips += amount;
        System.out.println("AI Player " + getNickname() + " ha vinto " + amount + " fiches.");
    }

    /**
     * Metodo che simula il turno dell'AI, applicando logica per decidere se pescare, stare o raddoppiare.
     *
     * @param game     Riferimento all'oggetto BlackJackGame.
     */
    public void AITurn(BlackJackGame game)
    {

        // Controlla se ha Blackjack
        if (hasBlackJack())
        {
            System.out.println("AI Player " + getNickname() + " ha un Blackjack!");
            return;
        }

        // Controlla se può fare Double Down
        if ((calculateHandScore(getHand())[0] == 10 || calculateHandScore(getHand())[0] == 11) && chips >= betamount)
        {
            doubleDown(game.getDeck());
            return;
        }

        // Gioca il turno normalmente
        HitHandAI(game);
    }

    /**
     * Metodo per far giocare l'AI seguendo la logica di base del Blackjack.
     *
     * @param game Il gioco corrente.
     */
    public void HitHandAI(BlackJackGame game)
    {
        int[] scores = calculateHandScore(getHand());
        while (scores[0] < 17)
        {
            draw(game.getDeck(), false);
            scores = calculateHandScore(getHand());
        }
        System.out.println("AI Player " + getNickname() + " hits hand. Current hand: " + getHand());
    }

    /**
     * Metodo per il Double Down (raddoppia la scommessa e pesca una sola carta).
     * @param deck Il mazzo da cui pescare la carta.
     */
    public void doubleDown(Deck deck)
    {
        if (chips >= betamount && hand.size() == 2) // Controlla il numero di carte
        {
            chips-=betamount;
            betamount *= 2;
            draw(deck, false);
            System.out.println("AI Player " + getNickname() + " ha fatto Double Down.");
            System.out.println("Mano dopo Double Down: " + getHand());
        } else
        {
            System.out.println("AI Player " + getNickname() + " non può fare Double Down.");
        }
    }

    /**
     * Restituisce l'importo attuale della scommessa dell'AI.
     *
     * @return L'importo della scommessa.
     */
    public int getBetAmount()
    {
        return betamount;
    }

    /**
     * Resetta l'importo della scommessa dell'AI.
     */
    public void resetBetAmount()
    {
        betamount = 0;
    }

}
