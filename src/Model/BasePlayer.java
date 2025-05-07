package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rappresenta un giocatore di base nel gioco del Blackjack.
 * Fornisce metodi comuni per la gestione delle carte, delle fiches e delle scommesse.
 */
public abstract class BasePlayer extends BlackJackParticipant
{
    /**
     * Lista delle carte della mano split del giocatore.
     */
    protected List<Card> splitHand;

    /**
     * Numero di fiches possedute dal giocatore.
     */
    protected int chips;

    /**
     * Importo della scommessa corrente del giocatore.
     */
    protected int betamount;

    /**
     * Importo della scommessa per l'assicurazione (se effettuata).
     */
    private int insuranceBet;

    /**
     * Costruttore che inizializza il giocatore con una mano vuota,
     * una mano divisa vuota e un saldo iniziale di 1000 fiches.
     */
    public BasePlayer()
    {
        this.hand = new ArrayList<>();
        this.splitHand = new ArrayList<>();
        this.chips = 1000;
        this.betamount = 0;
        this.insuranceBet = 0;
    }

    /**
     * Pesca una carta dal mazzo e la aggiunge alla mano del giocatore.
     *
     * @param deck        Il mazzo da cui pescare la carta.
     * @param isSplitHand {@code true} se la carta deve essere aggiunta alla mano divisa, {@code false} altrimenti.
     */
    public void draw(Deck deck, boolean isSplitHand)
    {
        if (isSplitHand)
        {
            splitHand.add(deck.drawCard());
        } else
        {
            hand.add(deck.drawCard());
        }
    }

    /**
     * Aggiunge una carta alla mano principale del giocatore.
     *
     * @param card La carta da aggiungere.
     */
    public void addMainHand(Card card)
    {
        hand.add(card);
    }

    /**
     * Aggiunge una carta alla mano divisa (split).
     *
     * @param card La carta da aggiungere.
     */
    public void addCardToSplitHand(Card card)
    {
        splitHand.add(card);
    }

    /**
     * Restituisce il numero di fiches del giocatore.
     *
     * @return Il numero di fiches attuali del giocatore.
     */
    public int getChips()
    {
        return chips;
    }

    /**
     * Imposta il numero di fiches del giocatore.
     *
     * @param amount Il nuovo valore delle fiches.
     */
    public void setChips(int amount)
    {
        chips = amount;
    }

    /**
     * Restituisce la seconda mano del giocatore.
     *
     * @return La lista di carte nella mano divisa.
     */
    public List<Card> getSplitHand()
    {
        return splitHand;
    }

    /**
     * Esegue un Double Down (raddoppio della scommessa).
     *
     * @param betAmount L'importo della scommessa attuale.
     * @return {@code true} se il Double Down è stato eseguito con successo, {@code false} altrimenti.
     */
    public boolean doubleDown(int betAmount)
    {
        if (chips >= betAmount)
        {
            chips -= betAmount;
            betamount += betAmount;
            return true;
        }
        return false;
    }

    /**
     * Effettua una scommessa.
     *
     * @param amount L'importo della scommessa.
     * @return {@code true} se la scommessa è stata accettata, {@code false} altrimenti.
     */
    public boolean betChips(int amount)
    {
        if (chips < amount)
        {
            return false;
        }
        chips -= amount;
        betamount += amount;
        return true;
    }

    /**
     * Aggiunge fiches al saldo del giocatore dopo una vincita.
     *
     * @param amount L'importo delle fiches vinte.
     */
    public void winChips(int amount)
    {
        chips += amount;
        System.out.println("Hai vinto " + amount + " fiches!");
    }

    /**
     * Svuota entrambe le mani del giocatore.
     */
    public void clearHand()
    {
        hand.clear();
        splitHand.clear();
    }

    /**
     * Calcola il punteggio della mano principale o della mano divisa.
     *
     * @param isSplitHand {@code true} per calcolare il punteggio della mano divisa, {@code false} per la mano principale.
     * @return Un array contenente i possibili punteggi della mano selezionata.
     */
    public int[] calculateScore(boolean isSplitHand)
    {
        List<Card> hand = isSplitHand ? splitHand : this.hand;
        return calculateHandScore(hand);
    }

    /**
     * Verifica se il giocatore può eseguire uno Split.
     *
     * @return {@code true} se il giocatore può dividere la mano, {@code false} altrimenti.
     */
    public boolean canSplit()
    {
        return hand.size() == 2 && hand.get(0).getCardValueType() == hand.get(1).getCardValueType() && chips >= betamount;
    }

    /**
     * Restituisce l'importo della scommessa corrente.
     *
     * @return L'importo della scommessa.
     */
    public int getBetamount()
    {
        return betamount;
    }

    /**
     * Resetta il numero di fiches del giocatore al valore iniziale (1000) e l'importo della scommessa.
     */
    public void resetChips()
    {
        chips = 1000;
    }

    /**
     * Effettua una scommessa per l'assicurazione.
     *
     * @param amount L'importo della scommessa per l'assicurazione.
     */
    public void placeInsuranceBet(int amount)
    {
        if (chips >= amount)
        {
            chips -= amount;
            insuranceBet += amount;
        }
    }

    /**
     * Restituisce l'importo della scommessa per l'assicurazione.
     *
     * @return L'importo della scommessa per l'assicurazione.
     */
    public int getInsuranceBet()
    {
        return insuranceBet;
    }

    /**
     * Resetta l'importo dell'assicurazione.
     */
    public void resetInsuranceAmount()
    {
        insuranceBet = 0;
    }
}
