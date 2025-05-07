// BlackJackGame.java
package Model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe {@code BlackJackGame} che gestisce la logica del gioco di Blackjack.
 * Contiene il mazzo, il dealer, il giocatore e gli AI Players.
 * Implementa {@code Observable} per notificare le modifiche agli osservatori.
 */
public class BlackJackGame extends Observable
{
    private Player player;
    private Dealer dealer;
    private Deck deck;
    private static final int NUM_DECKS = 6;
    private List<AiPlayer> aiPlayers = new ArrayList<>();

    // Parametri di gioco
    private int betamount;
    private int numAiPlayers;

    /**
     * Costruttore che inizializza il gioco creando il mazzo, il dealer e il giocatore.
     */
    public BlackJackGame()
    {
        deck = new Deck(NUM_DECKS);
        player = Player.getInstance();
        dealer = new Dealer();
        aiPlayers = new ArrayList<>();
    }

     /**
     * Inizializza gli AI Players impostando il numero desiderato.
     * @param numAiPlayers Numero di AI Players da creare.
     */
    public void initializeAiPlayers(int numAiPlayers)
    {
        this.numAiPlayers = numAiPlayers; //Salva il numero di AI players
        aiPlayers.clear();
        for (int i = 1; i <= numAiPlayers; i++)
        {
            aiPlayers.add(new AiPlayer("AI" + i));
        }
    }
    /**
     * Distribuisce le carte iniziali a tutti i giocatori e al dealer.
     */
    public void cardsGivenToPlayers()
    {
        // Resetta le mani dei giocatori
        player.clearHand();
        dealer.clearHand();
        aiPlayers.forEach(AiPlayer::clearHand);

        dealInitialCards();

        setChanged();
        notifyObservers("Cards Given To Players");
    }
    /**
     * Distribuisce due carte iniziali a tutti i giocatori e al dealer.
     */
    private void dealInitialCards()
    {
        player.draw(deck, false);
        player.draw(deck, false);

        dealer.draw(deck);
        dealer.draw(deck);
        aiPlayers.forEach(ai -> {
            ai.draw(deck, false);
            ai.draw(deck, false);
        });
    }

    /**
     * Esegue il turno degli AI Players e determina i vincitori.
     */
    public void aiPlayersTurn()
    {
        for (AiPlayer aiPlayer : aiPlayers)
        {
            aiPlayer.AITurn(this);
        }
        determineWinnerForAIPlayers();
        setChanged();
        notifyObservers("AI Players Turn");
    }

    /**
     * Imposta l'importo della scommessa.
     * @param amount Importo della scommessa.
     */
    public void setBetAmount(int amount)
    {
        this.betamount = amount;
    }
    /**
     * Esegue il turno del dealer seguendo la regola del minimo di 17 punti.
     */
    public void dealerTurn()
    {
        int[] dealerScores = dealer.calculateHandScore(dealer.getHand());

        while (dealerScores[0] < 17)
        {
            dealer.draw(deck);
            dealerScores = dealer.calculateHandScore(dealer.getHand());
        }
        setChanged();
        notifyObservers("Dealer Turn");
    }

    /**
     * Controlla i pagamenti dell'assicurazione se il dealer ha un Blackjack.
     */
    public void checkInsurancePayout()
    {
        if (dealer.hasBlackJack())
        {
            player.winChips(2 * player.getInsuranceBet());

            for (AiPlayer aiPlayer : aiPlayers)
            {
                aiPlayer.winChips(2 * aiPlayer.getInsuranceBet());
            }
        }
        resetAllInsuranceBets();
    }

    /**
     * Notifica agli osservatori l'aggiornamento delle chips.
     */
    public void notifyChipsUpdated()
    {
        setChanged();
        notifyObservers("Chips Updated");
    }

    /**
     * Determina il vincitore tra il giocatore e il dealer.
     * @return Messaggio di risultato della partita.
     */
    public String determineWinnerForPlayer()
    {
        int[] playerScores = player.calculateScore(false);
        int[] splitScores = player.calculateScore(true);
        int[] dealerScores = dealer.calculateHandScore(dealer.getHand());

        int playerFinalScore = (playerScores.length > 1 && playerScores[1] <= 21) ? playerScores[1] : playerScores[0];
        int splitFinalScore = (splitScores.length > 1 && splitScores[1] <= 21) ? splitScores[1] : splitScores[0];
        int dealerFinalScore = (dealerScores.length > 1 && dealerScores[1] <= 21) ? dealerScores[1] : dealerScores[0];

        player.incrementGamesPlayed();

        StringBuilder resultMessage = new StringBuilder();

        // Check main hand result
        if (playerFinalScore > 21)
        {
            player.incrementGamesLost();
            resultMessage.append("Il giocatore perde con la prima mano con" +playerFinalScore + " punti.\n");
        } else if (dealerFinalScore > 21 || playerFinalScore > dealerFinalScore)
        {
            player.incrementGamesWon();
            player.winChips(2 * betamount);
            resultMessage.append("Il giocatore vince con la prima mano con " + playerFinalScore + " punti.\n");
        } else if (playerFinalScore == dealerFinalScore)
        {
            player.winChips(betamount);
            player.incrementGamesTied();
            resultMessage.append("Pareggio con la prima mano!\n");
        } else
        {
            player.incrementGamesLost();
            resultMessage.append("Il dealer vince con la prima mano con " + dealerFinalScore + " punti.\n");
        }

        // Check split hand result
        if (!player.getSplitHand().isEmpty())
        {
            if (splitFinalScore > 21)
            {
                player.incrementGamesLost();
                resultMessage.append("Il giocatore perde con la seconda mano.\n");
            } else if (dealerFinalScore > 21 || splitFinalScore > dealerFinalScore)
            {
                player.incrementGamesWon();
                player.winChips(2 * betamount);
                           System.out.println("Il giocatore vince con la seconda mano con " + splitFinalScore + " punti.");
            } else if (splitFinalScore == dealerFinalScore)
            {
                player.winChips(betamount);
                player.incrementGamesTied();
                            System.out.println("Pareggio con la seconda mano!");
            } else
            {
                player.incrementGamesLost();
                           System.out.println("Il dealer vince con la seconda mano con " + dealerFinalScore + " punti.");
            }
        }

        //Aggiorna la view con le chips attuali degli AI
        setChanged();
        notifyChipsUpdated();
        return resultMessage.toString();
    }

    //Metodo per determinare il vincitore tra i giocatori AI e il dealer
    public String determineWinnerForAIPlayers()
    {
        StringBuilder resultMessage = new StringBuilder();
        boolean dealerBlackjack = dealer.hasBlackJack();

        for (AiPlayer aiPlayer : aiPlayers)
        {
            int[] aiPlayerScores = aiPlayer.calculateScore(false);
            int[] dealerScores = dealer.calculateHandScore(dealer.getHand());

            int aiPlayerFinalScore = (aiPlayerScores.length > 1 && aiPlayerScores[1] <= 21) ? aiPlayerScores[1] : aiPlayerScores[0];
            int dealerFinalScore = (dealerScores.length > 1 && dealerScores[1] <= 21) ? dealerScores[1] : dealerScores[0];

            boolean aiHasBlackjack = aiPlayer.hasBlackJack();
            // Determina il risultato per ogni AI Player
            if (aiHasBlackjack)
            {
                if (dealerBlackjack)
                {
                    System.out.println(aiPlayer.getNickname() + " ha un blackjack! Il dealer ha un blackjack! Pareggio.");
                } else
                {
                    aiPlayer.winChips(2 * aiPlayer.getBetAmount());
                    System.out.println(aiPlayer.getNickname() + " ha un blackjack! " + aiPlayer.getNickname() + " vince " + 2 * aiPlayer.getBetAmount() + " chips.");
                }
                aiPlayer.resetBetAmount();
                continue;
            }
            if (aiPlayerFinalScore > 21)
            {
                System.out.println(aiPlayer.getNickname() + " ha sballato! Il dealer vince automaticamente.");
                aiPlayer.resetBetAmount();
            } else if (dealerFinalScore > 21 || aiPlayerFinalScore > dealerFinalScore)
            {
                aiPlayer.winChips(2 * aiPlayer.getBetAmount());
                System.out.println(aiPlayer.getNickname() + " vince " + 2 * aiPlayer.getBetAmount() + " con " + aiPlayerFinalScore + " punti.");
                aiPlayer.resetBetAmount();
            } else if (aiPlayerFinalScore == dealerFinalScore)
            {
                aiPlayer.winChips(aiPlayer.getBetAmount());
                System.out.println("Pareggio con " + aiPlayer.getNickname() + " ottiene indietro la scommessa.");
                aiPlayer.resetBetAmount();
            } else
            {
                System.out.println(aiPlayer.getNickname() + " perde contro il dealer con " + dealerFinalScore + " punti.");
                aiPlayer.resetBetAmount();
            }
        }
        setChanged();
        notifyChipsUpdated();
        return resultMessage.toString();
    }

    //metodo per far scommettere i giocatori AI
    public void aiPLayerPlaceBets()
    {
        Random random = new Random();
        for (AiPlayer aiPlayer : aiPlayers)
        {
            int maxBet = Math.max(1, aiPlayer.getChips() / 2); // Massimo 50% delle fiches
            int betAmount = random.nextInt(maxBet) + 1; // Evita 0
            aiPlayer.betChips(betAmount);
            System.out.println("AI Player " + aiPlayer.getNickname() + " scommette " + betAmount + " chips.");
        }
        setChanged();
        notifyObservers("Chips updated");
        notifyObservers("AI Players Place Bets");
    }

    /**
     * Reset degli insurance bet di tutti i giocatori.
     */
    public void resetAllInsuranceBets()
    {
        player.resetInsuranceAmount();
        for (AiPlayer aiPlayer : aiPlayers)
        {
            aiPlayer.resetInsuranceAmount();
        }
    }
    public Player getPlayer()
    {
        return player;
    }

    public Dealer getDealer()
    {
        return dealer;
    }

    public Deck getDeck()
    {
        return deck;
    }

    public int getBetAmount()
    {
        return betamount;
    }

    public List<AiPlayer> getAiPlayers()
    {
        return aiPlayers;
    }

    //Metodi per controllare se le mani del giocatore sono sballate
    public boolean isMainHandBusted()
    {
        return player.calculateScore(false)[0] > 21;
    }
    public boolean isSplitHandBusted()
    {
        return player.calculateScore(true)[0] > 21;
    }

    //Metodo per ottenere la mani degli AI Players
    public List<List<Card>> getAIPlayersHands()
    {
        return aiPlayers.stream()
                .map(AiPlayer::getHand)
                .collect(Collectors.toList());
    }

    //Metodo per ottenere i punteggi degli AI Players
    public Map<Integer, int[]> getAiPlayerScores()
    {
        Map<Integer, int[]> aiPlayerScores = new HashMap<>();
        for (int i = 0; i < aiPlayers.size(); i++)
        {
            aiPlayerScores.put(i, aiPlayers.get(i).calculateScore(false));
        }
        return aiPlayerScores;
    }
}