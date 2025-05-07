package Controller;

import Model.*;
import View.BlackJackView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
/**
 * Controller per il gioco del BlackJack.
 * Gestisce la logica del gioco e l'interazione tra il modello e la vista.
 */
public class BlackJackController implements Observer
{
    private BlackJackView view;
    private BlackJackGame model;
    private AudioManager audioManager;
    private int gameNumber = 1;

    //Booleani per tenere traccia dei tasti premuti
    private boolean isSplithandPressed = false;
    private boolean isStandPressed = false;

    /**
     * Costruttore della classe BlackJackController.
     *
     * @param view  La vista del gioco.
     * @param model Il modello del gioco.
     */
    public BlackJackController(BlackJackView view, BlackJackGame model)
    {
        this.view = view;
        this.model = model;
        this.audioManager = new AudioManager();

        model.addObserver(this);
        //Registrazione della vista come osservatore del modello
        model.addObserver(view); // Add the view as an observer of the model

        view.addStartButtonListener(new StartButtonListener());
        view.addHitListener(new HitButtonListener());
        view.addStandListener(new StandButtonListener());
        view.addDoubleDownListener(new DoubleDownButtonListener());
        view.addSplitListener(new SplitButtonListener());
        view.addRestartButtonListener(new RestartButtonListener());

        audioManager.loopSound("src/Music/BackgroundMusic.wav");
        audioManager.setVolume(-20.0f);
    }

    /**
     * Metodo chiamato quando il modello notifica gli osservatori.
     *
     * @param o   L'oggetto osservato.
     * @param arg L'argomento passato nella notifica.
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (!(arg instanceof String))
        {
            return; // Se non è una stringa, non fare nulla
        }
        String event = (String) arg;

        switch (event)
        {
            case "Cards Given To Players":
                updateAIPlayersHands();
                updatePlayerHand();
                updateDealerHand();
                break;
            case "Chips updated":
                view.updateChips(model.getPlayer().getChips());
                view.updateAIChips(model.getAiPlayers());
                break;
            case "AI Players Turn":
                updateAIPlayersHands();
                break;
            case "Dealer Turn":
                List<Card> dealerHand = model.getDealer().getHand();
                revealDealerCardsWithDelay(dealerHand, 2);
            default:
                break;
        }
    }

    /**
     * Aggiorna la mano del giocatore nella vista.
     */
    private void updatePlayerHand()
    {
        List<Card> playerHand = model.getPlayer().getHand();
        int[] playerScores = model.getPlayer().calculateScore(false);
        view.updatePlayerHand(playerHand, playerScores);
    }

    private void updateDealerHand()
    {
        List<Card> dealerHand = model.getDealer().getHand();
        view.updateDealerHand(dealerHand, false); // Mostra solo una carta scoperta
    }

    /**
     * Listener per il pulsante di avvio del gioco.
     */
    class StartButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            view.showGameScreen();
            int initialChips = view.getInitialChips();
            model.getPlayer().setChips(initialChips);
            int numAIPlayers = view.getNumAIPlayers();
            model.initializeAiPlayers(numAIPlayers);
            view.updateAIPlayersVisibility(numAIPlayers);
            askForBetController();
        }
    }

    class RestartButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            restartGame();
        }
    }

    /**
     * Aggiorna le mani dei giocatori AI nella vista.
     */
    private void updateAIPlayersHands()
    {
        List<List<Card>> aiPlayerHands = model.getAIPlayersHands(); //Recupera le mani
        Map<Integer, int[]> aiPlayerScores = model.getAiPlayerScores(); // Recupera i punteggi

        for (int i = 0; i < aiPlayerHands.size(); i++)
        {
            view.updateSingleAIPlayerHandWithDelay(i, aiPlayerHands.get(i), aiPlayerScores.get(i));
        }
    }

    /**
     * Listener per il pulsante Double Down.
     */
    class DoubleDownButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int betAmount = model.getBetAmount();
            if (model.getPlayer().doubleDown(betAmount)) // Check if the player has enough chips to double down
            {
                view.updateChips(model.getPlayer().getChips()); // Update the player's chips in the view
                model.getPlayer().draw(model.getDeck(), false);
                List<Card> hand = model.getPlayer().getHand();
                int[] scores = model.getPlayer().calculateScore(false); // Calculate the scores after drawing a card
                view.updatePlayerHand(hand, scores); // Update the player's hand in the view
                if (model.isMainHandBusted())
                { // Check if the player's hand is busted
                    handleMainHandEnd(); // Handle the end of the player's main hand
                } else
                {
                    model.aiPlayersTurn(); // Call AI players' turn after the player stands
                    finishDealerTurn();
                    String resultMessage = model.determineWinnerForPlayer(); // Call determineWinner from BlackJackGame
                    view.updateMessage(resultMessage); // Update the view with the result message
                }
            } else
            {
                view.updateMessage("Non hai abbastanza fiches per raddoppiare.");
            }
        }
    }

    //Metodo per stampare le mani dei giocatori AI
    private void printAIPlayerHands()
    {
        List<List<Card>> aiPlayerHands = model.getAIPlayersHands();
        for (int i = 0; i < aiPlayerHands.size(); i++)
        {
            List<Card> aiPlayerHand = aiPlayerHands.get(i);
            System.out.println("AI Player" + (i + 1) + " Hand: " + aiPlayerHand);
        }
    }
    /*Listener di HitButton che aggiunge una carta alla mano del giocatore
     * e aggiorna la vista con la nuova mano e i punteggi.
     * */
    private class HitButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (isStandPressed)
            {
                // Aggiungi una carta alla seconda mano
                model.getPlayer().draw(model.getDeck(), true);
                List<Card> splitHand = model.getPlayer().getSplitHand();
                int[] scores = model.getPlayer().calculateScore(true);
                view.updateSplitHand(splitHand, scores);
                //System.out.println("Split Hand: " + splitHand);

                if (model.isSplitHandBusted())
                {
                    handleSplitHandEnd();
                }
            } else
            {
                // Aggiungi una carta alla mano principale
                model.getPlayer().draw(model.getDeck(), false);
                List<Card> hand = model.getPlayer().getHand();
                int[] scores = model.getPlayer().calculateScore(false);
                view.updatePlayerHand(hand, scores);
                //System.out.println("Main Hand: " + hand);

                if (model.isMainHandBusted())
                {
                    handleMainHandEnd();
                    isStandPressed = true;
                }
            }
        }
    }
    /*Listener per il pulsante Stand
     * */
    private class StandButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (isSplithandPressed && !isStandPressed)
            {
                // Se il pulsante Split è stato premuto e Stand non è stato premuto, gioca la seconda mano
                isStandPressed = true; // Imposta il booleano a true

            } else
            {
                // Se il pulsante Split non è stato premuto o Stand è già stato premuto, procedi con il turno del dealer
                model.aiPlayersTurn(); // Call AI players' turn after the player stands
                revealAllDealerCards();
                finishDealerTurn();

            }
        }
    }
    //Metodo per rivelare tutte le carte del dealer
    private void revealAllDealerCards()
    {
        List<Card> dealerHand = model.getDealer().getHand();
        view.revealDealerHand(dealerHand);
    }

    /*
     * Listener di SplitButton che gestisce la divisione delle carte del giocatore
     * */
    private class SplitButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //Rendi invisibile il tasto doubleDown
            view.hideDoubleDownButton();
            handleSplit(); // Call the handleSplit method
            model.getPlayer().incrementGamesPlayed(); //Incrementa games played when split is pressed

            //Raddoppia l'importo della scommessa
            int currentBet = model.getBetAmount(); // Get the current bet amount
            if (model.getPlayer().betChips(currentBet)) // Check if the player has enough chips to double down
            {
                {
                    view.updateChips(model.getPlayer().getChips());
                    //System.out.println("Bet amount after splitting: " + model.getBetAmount());
                    view.showPlayerPanels(true); // Mostra il pannello dello splitHand
                    isSplithandPressed = true;
                    view.hideDoubleDownButton(); // Nascondi il pulsante Double Down
                    view.hideSplitButton(); // Nascondi il pulsante Split
                }
            } else
            {
                view.updateMessage("Non hai abbastanza fiches per raddoppiare.");
            }
        }
    }

    //Metodo per gestire la divisione delle carte del giocatore
    private void handleSplit()
    {
        List<Card> playerHand = model.getPlayer().getHand();
        if (model.getPlayer().canSplit())
        {
            Card secondCard = playerHand.remove(1);
            model.getPlayer().addCardToSplitHand(secondCard);
            model.getPlayer().draw(model.getDeck(), false);
            model.getPlayer().draw(model.getDeck(), true);

            view.updatePlayerHand(model.getPlayer().getHand(), model.getPlayer().calculateScore(false));
            view.updateSplitHand(model.getPlayer().getSplitHand(), model.getPlayer().calculateScore(true));

            // Show the split hand components after the Split button is pressed
            view.showPlayerPanels(true);
            view.splitPanel.setVisible(true);
            view.splitScoreLabel.setVisible(true);
        }
    }

    //Metodo per chiedere la scommessa al giocatore con ritardo
    private void askForBetWithDelay()
    {
        Timer delayTimer = new Timer(1500, e -> askForBetController());
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    //Metodo per chiedere la scommessa al giocatore
    private void askForBetController()
    {
        view.disableGameControls();
        view.resetScores();
        view.showScoreLabels(false);

        int betAmount = view.askForBet();
        if (model.getPlayer().betChips(betAmount))
        {
            model.setBetAmount(betAmount);
            model.aiPLayerPlaceBets();
            view.updateChips(model.getPlayer().getChips());
            view.showGameInfo(model.getPlayer().getChips());
            startNewRound();
            gameNumber++;
        } else
        {
            view.updateMessage("Non hai abbastanza fiches per scommettere.");
            askForBetWithDelay();
        }
        view.updateAIChips(model.getAiPlayers());
        view.updateAIChipsBet(model.getAiPlayers());
    }

    //Inizia una nuova partita
    private void startNewRound()
    {
        view.hideSplitPanel(); // Nascondi il pannello dello splitHand
        view.enableGameControls(); // Abilita i controlli del gioco
        view.resetScores(); // Resetta i punteggi

        view.updateAIChips(model.getAiPlayers()); // Aggiorna le fiches dei giocatori AI
        model.cardsGivenToPlayers(); // Distribuisci le carte ai giocatori
        printGameStatistics(); // Stampa le statistiche del gioco

        printAIPlayerHands(); // Stampa le mani dei giocatori AI
        printAIPlayerChips(); // Stampa le fiches dei giocatori AI

        List<Card> dealerHand = model.getDealer().getHand();
        int dealerVisibleScore = dealerHand.get(1).getCardValueType().getValue();

        view.updateDealerHand(dealerHand, false);
        List<Card> playerHand = model.getPlayer().getHand();
        int[] playerScores = model.getPlayer().calculateScore(false);
        view.updatePlayerHand(playerHand, playerScores);

        //Resetto lo stato di stand per il nuovo round
        isStandPressed = false;

        if (model.getPlayer().hasBlackJack())
        {
            handlePlayerBlackjack();
        }

        InsuranceBet();
    }

    //Metodo per gestire il BlackJack del giocatore
    private void handlePlayerBlackjack()
    {
        int[] playerScores = model.getPlayer().calculateScore(false);

        // Check if the player has a blackjack
        int[] dealerScores = model.getDealer().calculateHandScore(model.getDealer().getHand());
        if (model.getDealer().hasBlackJack())
        { // Check if the dealer also has a blackjack
            view.updateMessage("Pareggio! Entrambi hanno BlackJack.");
            model.getPlayer().winChips(model.getBetAmount());
            model.getPlayer().incrementGamesPlayed();
            model.getPlayer().incrementGamesTied();
        } else
        {
            view.updateMessage("BlackJack! Hai vinto!");
            model.getPlayer().winChips(model.getBetAmount() * 2);
            model.getPlayer().incrementGamesPlayed();
            model.getPlayer().incrementGamesWon();
        }
        // Allow AI players to play their turns
        model.aiPlayersTurn();

        // Reveal dealer's cards and finish dealer's turn
        view.revealDealerHand(model.getDealer().getHand());
        finishDealerTurn();
    }

    /// Metodo per terminare il round
    private void endRound()
    {
        if (model.getPlayer().getChips() > 0)
        {
            delayNextRound();
        } else
        {
            handleGameOver();
        }
        updateGameStats();
    }

    //Metodo per chiedere la scommessa al giocatore con ritardo
    private void delayNextRound()
    {
        Timer delayTimer = new Timer(1500, e -> askForBetWithDelay());
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    //Metodo per gestire la fine del gioco
    private void handleGameOver()
    {

        audioManager.playSound("src/Music/gameOverSound.wav");
        view.updateMessage("Hai finito le fiches! Riavvia la partita per giocare di nuovo.");
        view.disableGameControls();

        int option = JOptionPane.showOptionDialog(view, "Hai 0 Fiches! Vuoi riavviare la partita?",
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Restart Game"}, "Restart Game");

        if (option == JOptionPane.YES_OPTION)
        {
            restartGame();
        }
    }

    //Metodo per aggiornare le statistiche del gioco
    private void updateGameStats()
    {
        updateScoresController();
        view.updateChips(model.getPlayer().getChips());
        view.updatePlayerHand(model.getPlayer().getHand(), model.getPlayer().calculateScore(false));
        view.updateSplitHand(model.getPlayer().getSplitHand(), model.getPlayer().calculateScore(true));
        view.updateDealerHand(model.getDealer().getHand(), true);
        printGameStatistics();
        view.updateAllAIPlayerChips(model.getAiPlayers());
        view.updateAIChips(model.getAiPlayers());
    }


    /**
     * Mostra una finestra di dialogo per confermare la vittoria del giocatore.
     */
    private void showWinDialog()
    {
        int option = JOptionPane.showOptionDialog(view,
                "Congratulazioni! Hai sconfitto tutti i giocatori AI!\nVuoi giocare di nuovo?",
                "VITTORIA!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Gioca di nuovo"},
                "Gioca di nuovo");

        if (option == JOptionPane.YES_OPTION)
        {
            restartGame();
        }
    }
    //Metodo per riavviare il gioco
    private void restartGame()
    {
        view.showButtons();
        view.hideAllPanels();
        view.showWelcomePanel();
    }
    //Metodo per stampare le statistiche del gioco
    public void printGameStatistics()
    {
        int level = model.getPlayer().getLevel();
        int gamesPlayed = model.getPlayer().getGamesPlayed();
        int gamesWon = model.getPlayer().getGamesWon();
        int gamesLost = model.getPlayer().getGamesLost();
        int gamesTied = model.getPlayer().getGamesTied();
        System.out.println("Player MainHand:" + model.getPlayer().getHand());
        System.out.println("Player SplitHand:" + model.getPlayer().getSplitHand());
    }
    //Metodo per gestire la fine della mano divisa
    private void handleSplitHandEnd()
    {
        int[] splitScores = model.getPlayer().calculateScore(true);
        if (splitScores[0] > 21)
        {
            view.updateBackgroundMessage("Hai sballato con la seconda mano!");
        } else if (splitScores.length > 1 && splitScores[1] == 21)
        {
            view.updateBackgroundMessage("BlackJack con la seconda mano!");
        }

        finishDealerTurn();
    }

    //Metodo per gestire la fine della mano principale
    private void handleMainHandEnd()
    {
        int[] playerScores = model.getPlayer().calculateScore(false);
        if (playerScores[0] > 21) // Check if the player's main hand is busted
        {
            view.updateBackgroundMessage("Hai sballato con la prima mano!");
        } else if (model.getPlayer().hasBlackJack()) // Check if the player has a blackjack
        {
            view.updateBackgroundMessage("BlackJack con la prima mano!");
        }

        if (!model.getPlayer().getSplitHand().isEmpty()) // Check if the player has a split hand
        {
            // Proceed to play the second hand
            view.showPlayerPanels(true);
            view.splitPanel.setVisible(true);
            view.splitScoreLabel.setVisible(true);
            view.updateMessage("Gioca la seconda mano.");
        } else
        {
            finishDealerTurn();
        }
    }
    //Metodo per terminare il turno del dealer
    private void finishDealerTurn()
    {
        view.disableGameControls();
        model.dealerTurn();

    }

    private void revealDealerCardsWithDelay(List<Card> dealerHand, int index)
    {

        if (index == 2)
        {
            // Reveal the second card first
            Timer revealTimer = new Timer(2000, e -> {
                view.updateDealerHand(dealerHand.subList(0, 2), true); // Show the first two cards
                revealDealerCardsWithDelay(dealerHand, index + 1);
            });
            revealTimer.setRepeats(false);
            revealTimer.start();
        } else if (index < dealerHand.size())
        {
            // Reveal the remaining cards one by one
            Timer revealTimer = new Timer(2000, e -> {
                view.updateDealerHand(dealerHand.subList(0, index + 1), true);
                revealDealerCardsWithDelay(dealerHand, index + 1);
            });
            revealTimer.setRepeats(false);
            revealTimer.start();
        } else
        {
            view.revealDealerHand(dealerHand); // Ensure all cards are revealed
            updateDealerScores();
            //Controlliamo se il giocatore ha già vinto
            if (!model.getPlayer().hasBlackJack())
            {
                String resultMessage = model.determineWinnerForPlayer();
            }
            endRound();
        }
    }

    public void printAIPlayerChips()
    {
        List<AiPlayer> aiPlayers = model.getAiPlayers();
        for (int i = 0; i < aiPlayers.size(); i++)
        {
            AiPlayer aiPlayer = aiPlayers.get(i);
            //System.out.println("AI Player " + (i + 1) + " Chips: " + aiPlayer.getChips());
        }
    }
    private void updateScoresController()
    {
        int gamesPlayed = model.getPlayer().getGamesPlayed(); // Get the updated games played
        int gamesWon = model.getPlayer().getGamesWon();
        int gamesTied = model.getPlayer().getGamesTied();
        int gamesLost = model.getPlayer().getGamesLost();
        int level = model.getPlayer().getLevel();
        view.updateScores(gamesPlayed, gamesWon, gamesTied, gamesLost, level); // Update the scores in the view
        view.updateLevel(level);
    }

    private void updateDealerScores()
    {
        int[] dealerScores = model.getDealer().calculateHandScore(model.getDealer().getHand());
        view.updateDealerScore(dealerScores[0], dealerScores.length > 1 ? dealerScores[1] : dealerScores[0]);
    }

    public void InsuranceBet()
    {
        // Check if the dealer's face-up card is an Ace
        if (model.getDealer().getHand().get(0).getCardValueType() == Value.ACE)
        {
            // Ask the main player if they want to place an insurance bet
            if (view.askForInsurance())
            {
                int insuranceBet = model.getPlayer().getBetamount() / 2;
                if (model.getPlayer().getChips() >= insuranceBet)
                {
                    model.getPlayer().placeInsuranceBet(insuranceBet);
                    view.updateChips(model.getPlayer().getChips());
                } else
                {
                    view.showMessage("Non hai abbastanza fiches per piazzare l'assicurazione.");
                }
            }

            // AI players place their insurance bets
            for (AiPlayer aiPlayer : model.getAiPlayers())
            {
                int insuranceBet = aiPlayer.getBetAmount() / 2; // Example logic: AI bets half of their current bet
                if (aiPlayer.getChips() >= insuranceBet)
                {
                    if (Math.random() < 1.0 / 3.0) // AI takes insurance bet one out of three times
                    {
                        aiPlayer.placeInsuranceBet(insuranceBet);
                        System.out.println("AI Player " + aiPlayer.getNickname() + " ha piazzato una scommessa assicurativa di " + insuranceBet + " fiches.");
                    } else
                    {
                        System.out.println("AI Player " + aiPlayer.getNickname() + " ha deciso di non piazzare l'assicurazione.");
                    }
                } else
                {
                    System.out.println("AI Player " + aiPlayer.getNickname() + " non ha abbastanza fiches per piazzare l'assicurazione.");
                }
            }
            view.updateAllChips(model.getPlayer().getChips(), model.getAiPlayers());
            model.checkInsurancePayout(); // Check the insurance payout
            boolean dealerHasBlackJack = model.getDealer().hasBlackJack();
            //Show the result of the insurancePayout
            view.showInsuranceResult(dealerHasBlackJack, model.getPlayer().getInsuranceBet(), model.getAiPlayers());
            view.updateAllChips(model.getPlayer().getChips(), model.getAiPlayers());
            if (dealerHasBlackJack)
            {
                //Rendi la seconda carta visibile
                view.updateDealerHand(model.getDealer().getHand(), true);
                askForBetController();
            }
        }
    }
}