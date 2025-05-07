package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rappresenta un partecipante al gioco del Blackjack.
 * Ogni partecipante ha una mano di carte e metodi per gestire e calcolare il punteggio.
 */
public abstract class BlackJackParticipant {

    /**
     * Lista di carte che rappresenta la mano del partecipante.
     */
    protected List<Card> hand = new ArrayList<>();

    /**
     * Aggiunge una carta alla mano del partecipante.
     *
     * @param card La carta da aggiungere alla mano.
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Rimuove tutte le carte dalla mano del partecipante, svuotandola.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Calcola il punteggio della mano data come parametro.
     * Gli Assi possono valere sia 1 che 11 a seconda del contesto.
     *
     * @param hand La mano di carte di cui calcolare il punteggio.
     * @return Un array contenente i possibili punteggi della mano.
     */
    public int[] calculateHandScore(List<Card> hand) {
        int totalScore = 0;
        int aceCount = 0;

        for (Card card : hand) {
            int cardValue = card.getCardValueType().getValue();
            if (card.getCardValueType() == Value.ACE) {
                aceCount++;
            }
            totalScore += cardValue;
        }

        int minScore = totalScore;
        int maxScore = totalScore + (aceCount > 0 && totalScore + 10 <= 21 ? 10 : 0);

        return (maxScore > 21) ? new int[]{minScore} : new int[]{minScore, maxScore};
    }

    /**
     * Determina se il partecipante ha un Blackjack.
     * Un Blackjack si verifica solo se la mano contiene esattamente due carte:
     * un Asso e una carta con valore dieci (10, Jack, Queen o King).
     *
     * @return {@code true} se il partecipante ha un Blackjack, altrimenti {@code false}.
     */
    public boolean hasBlackJack() {
        if (hand.size() != 2) {
            return false;
        }
        return (isAce(hand.get(0)) && isTenValue(hand.get(1))) ||
               (isAce(hand.get(1)) && isTenValue(hand.get(0)));
    }

    /**
     * Verifica se una carta è un Asso.
     *
     * @param card La carta da controllare.
     * @return {@code true} se la carta è un Asso, altrimenti {@code false}.
     */
    protected boolean isAce(Card card) {
        return card.getCardValueType() == Value.ACE;
    }

    /**
     * Verifica se una carta ha un valore di dieci (10, Jack, Queen o King).
     *
     * @param card La carta da controllare.
     * @return {@code true} se la carta ha un valore di dieci, altrimenti {@code false}.
     */
    protected boolean isTenValue(Card card) {
        return card.getCardValueType() == Value.TEN ||
               card.getCardValueType() == Value.JACK ||
               card.getCardValueType() == Value.QUEEN ||
               card.getCardValueType() == Value.KING;
    }

    /**
     * Restituisce la mano attuale del partecipante.
     *
     * @return Una lista contenente le carte attualmente in mano al partecipante.
     */
    public List<Card> getHand() {
        return hand;
    }
}
