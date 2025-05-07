package View;

import Model.AiPlayer;
import Model.AudioManager;
import Model.Card;
import Model.Value;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

// Classe che gestisce l'interfaccia grafica del gioco di Blackjack.
//Estende {@link JFrame} per la gestione della GUI e implementa {@link Observer}
//per aggiornare la vista in base ai cambiamenti del modello {@link Model.BlackJackGame}.

public class BlackJackView extends JFrame implements Observer
{
    // ==========================
    // Constants
    // ==========================
    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 160;
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;

    // ==========================
    // Components: Welcome Screen
    // ==========================
    private JPanel welcomePanel;
    private JTextField nicknameField;
    private JComboBox<String> difficultyComboBox;
    private JComboBox<Integer> numAIPlayersComboBox;
    private JButton startButton;

    // ==========================
    // Components: Game Screen
    // ==========================
    private JPanel mainPanel;
    private JPanel playerPanel;
    private JPanel dealerPanel;
    public JPanel splitPanel;

    private JLabel playerScoreLabel; // Score for player's main hand
    private JLabel dealerScoreLabel; // Score for dealer's hand
    public JLabel splitScoreLabel; // Score for split hand
    private JLabel chipsLabel; // Chips label
    private JLabel messageLabel; // Message label

    private JLabel levelLabel; // Level label
    private JLabel gamesPlayedLabel; // Games played label
    private JLabel gamesWonLabel; // Games won label
    private JLabel gamesTiedLabel; // Games tied label
    private JLabel gamesLostLabel; //Games Lost label

    //AI Player Chips
    private JLabel AIPlayer1ChipsBet;
    private JLabel AIPlayer2ChipsBet;
    private JLabel AIPlayer3ChipsBet;

    private JButton hitButton;
    private JButton standButton;
    public JButton splitButton; // Public to enable access
    private JButton doubleDownButton;
    private JButton restartButton;
    private JTextField betAmountField;

    private JPanel[] aiPlayerPanels;
    private JLabel[] aiPlayerScoreLabels;
    private JLabel[] aiPlayerChipsLabels;

    // ==========================
    // Dependencies
    // ==========================
    private CardImage cardImageManager;

    // ==========================
    // Constructor
    // ==========================

    AudioManager backGroundMusic = new AudioManager();

    public BlackJackView()
    {
        setUpFrame(); // Set up the frame
        cardImageManager = new CardImage(); // Initialize card image manager
        setupWelcomeScreen(); // Set up the welcome screen
        initalizeGameComponents(); // Initialize game components
        initalizeGameScreenComponents(); // Initialize game screen components
    }

    public void showButtons()
    {
        hitButton.setVisible(false);
        doubleDownButton.setVisible(false);
        standButton.setVisible(false);
        splitButton.setVisible(false);
    }

    //Mi serve un metodo che mette tutti i pannelli setVisible(false)
    public void hideAllPanels()
    {
        Component[] components = {
                playerPanel, dealerPanel, splitPanel, playerScoreLabel, dealerScoreLabel, splitScoreLabel,
                chipsLabel, messageLabel, levelLabel, gamesPlayedLabel, gamesWonLabel, gamesTiedLabel,
                gamesLostLabel, hitButton, doubleDownButton, standButton, splitButton, restartButton
        };

        for (Component comp : components)
        {
            comp.setVisible(false);
        }

        for (int i = 0; i < 3; i++)
        {
            aiPlayerPanels[i].setVisible(false);
            aiPlayerScoreLabels[i].setVisible(false);
        }
    }

    private void setUpFrame()
    {
        setTitle("Blackjack Game");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        Image icon = new ImageIcon(getClass().getResource("/Images/BlackJackIcon.jpg")).getImage();
        setIconImage(icon);
    }

    private void setupWelcomeScreen()
    {
        cardImageManager = new CardImage();
        // Initialize welcome screen
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(0, 128, 0));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        JLabel nicknameLabel = new JLabel("Nickname:");
        nicknameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        nicknameLabel.setForeground(Color.WHITE);
        welcomePanel.add(nicknameLabel, gbc);

        nicknameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        welcomePanel.add(nicknameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        difficultyLabel.setForeground(Color.WHITE);
        welcomePanel.add(difficultyLabel, gbc);

        difficultyComboBox = new JComboBox<>(new String[]{"Hard=1000 Chips", "Medium=2000 Chips", "Easy=3000 Chips"});
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        welcomePanel.add(difficultyComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel numAIPlayersLabel = new JLabel("Number of AI Players:");
        numAIPlayersLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        numAIPlayersLabel.setForeground(Color.WHITE);
        welcomePanel.add(numAIPlayersLabel, gbc);

        numAIPlayersComboBox = new JComboBox<>(new Integer[]{1, 2, 3});
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        welcomePanel.add(numAIPlayersComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(0, 102, 204));
        startButton.setForeground(Color.WHITE);
        welcomePanel.add(startButton, gbc);

        add(welcomePanel);
    }

    // Metodo per ridimensionare le immagini delle carte
    public void initalizeGameComponents()
    {
        // Initialize game screen components
        playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        splitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Voglio che nella scoreLabel ci sta il nome del giocatore
        playerScoreLabel = new JLabel("First Hand: ");
        dealerScoreLabel = new JLabel("Punteggio Dealer: ");
        splitScoreLabel = new JLabel("Second Hand: ");
        chipsLabel = new JLabel("Fiches: ");
        messageLabel = new JLabel("Benvenuto al Blackjack!");
        levelLabel = new JLabel("Livello: 1");
        gamesPlayedLabel = new JLabel("Partite Giocate: 0");
        gamesWonLabel = new JLabel("Partite Vinte: 0");
        gamesTiedLabel = new JLabel("Partite Pareggiate: 0");
        gamesLostLabel = new JLabel("Partite Perse: 0 ");
        AIPlayer1ChipsBet = new JLabel("");
        AIPlayer2ChipsBet = new JLabel("");
        AIPlayer3ChipsBet = new JLabel("");
        hitButton = new JButton("Pescare");
        doubleDownButton = new JButton("Raddoppia");
        standButton = new JButton("Stare");
        splitButton = new JButton("Split");

        restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setBackground(new Color(0, 102, 204));
        restartButton.setForeground(Color.WHITE);
        restartButton.setVisible(false); // Initially hidden


        // Initialize main game screen components
        playerPanel = new JPanel(new OverlapLayout(120));
        playerPanel.setOpaque(false);

        aiPlayerPanels = new JPanel[3];
        aiPlayerScoreLabels = new JLabel[3];
        for (int i = 0; i < 3; i++)
        {
            aiPlayerPanels[i] = new JPanel(new OverlapLayout(120));
            aiPlayerPanels[i].setOpaque(false);
            aiPlayerScoreLabels[i] = new JLabel("AI Player " + (i + 1) + ": 0");
        }

        aiPlayerChipsLabels = new JLabel[3];
        for (int i = 0; i < 3; i++)
        {
            aiPlayerChipsLabels[i] = new JLabel("AI Player " + (i + 1) + " Fiches: 1000");
            aiPlayerChipsLabels[i].setForeground(Color.ORANGE);
        }

        // Initialize mainPanel in the constructor
        mainPanel = new BackGroundPanel("src/Images/BackGroundImage.jpeg");
        mainPanel.setLayout(new BorderLayout());


        // Aggiungere questi pannelli ai container principali
        JPanel aiPlayersContainer = new JPanel();
        aiPlayersContainer.setLayout(new BoxLayout(aiPlayersContainer, BoxLayout.X_AXIS));
        aiPlayersContainer.setOpaque(false);

        for (int i = 0; i < aiPlayerPanels.length; i++)
        {
            JPanel aiPlayerContainer = new JPanel();
            aiPlayerContainer.setLayout(new BoxLayout(aiPlayerContainer, BoxLayout.Y_AXIS)); // Colonna per mani principali + split
            aiPlayerContainer.setOpaque(false);

            // Aggiungere i pannelli della mano principale e split
            aiPlayerContainer.add(aiPlayerPanels[i]);
            aiPlayerContainer.add(Box.createVerticalStrut(10)); // Spazio tra i due pannelli

            aiPlayersContainer.add(aiPlayerContainer);
        }
        mainPanel.add(aiPlayersContainer, BorderLayout.CENTER); // Conferma l'aggiunta dei pannelli principali e split
    }

    // Metodo per ridimensionare le immagini delle carte
    public void initalizeGameScreenComponents()
    {
        // Initially hide game screen components
        setVisibility(false, playerPanel, dealerPanel, splitPanel, playerScoreLabel, dealerScoreLabel, splitScoreLabel,
                chipsLabel, messageLabel, levelLabel, gamesPlayedLabel, gamesWonLabel, gamesTiedLabel, gamesLostLabel,
                hitButton, doubleDownButton, standButton, splitButton, restartButton);

        for (int i = 0; i < 3; i++)
        {
            aiPlayerPanels[i].setVisible(false);
            aiPlayerScoreLabels[i].setVisible(false);
        }

        setVisible(true);

    }

    public String getNickname()
    {
        return nicknameField.getText();
    }

    public String getSelectedAvatar()
    {
        return (String) difficultyComboBox.getSelectedItem();
    }

    public int getNumAIPlayers()
    {
        return (int) numAIPlayersComboBox.getSelectedItem();
    }

    public void addStartButtonListener(ActionListener listener)
    {

        startButton.addActionListener(listener);
    }

    public void showPlayerPanels(boolean show)
    {
        playerPanel.setVisible(show);//Mostrare la mano principale solo se non si vuole
        splitPanel.setVisible(show);
    }
    public void addButtonListener(JButton button, ActionListener listener)
    {
        button.addActionListener(listener);
    }

    public void showWelcomePanel()
    {
        remove(mainPanel);
        add(welcomePanel);
        welcomePanel.setVisible(true);
        revalidate();
        repaint();
    }

    // Metodo per aggiornare le carte del giocatore AI
    public void updateSingleAIPlayerHandWithDelay(int aiPlayerIndex, List<Card> aiPlayerCards, int[] aiPlayerScores)
    {
        // Mostra immediatamente le prime 2 carte
        SwingUtilities.invokeLater(() -> {
            JPanel cardPanel = new JPanel(new OverlapLayout(120)); // Layout sovrapposto per le carte
            cardPanel.setOpaque(false);

            // Mostra subito le prime 2 carte
            for (int i = 0; i < Math.min(2, aiPlayerCards.size()); i++)
            {
                Card card = aiPlayerCards.get(i);
                String value = card.getCardValueType().name();
                String suit = card.getSuit().name();
                ImageIcon cardImage = resizeCardImage(cardImageManager.getCardImage(value, suit));
                cardPanel.add(new JLabel(cardImage));
            }

            // Aggiorna graficamente il pannello con le prime 2 carte
            aiPlayerPanels[aiPlayerIndex].removeAll();
            aiPlayerPanels[aiPlayerIndex].add(cardPanel);
            aiPlayerPanels[aiPlayerIndex].revalidate();
            aiPlayerPanels[aiPlayerIndex].repaint();

            // Mostra il punteggio parziale per le prime 2 carte
            boolean hasAce = aiPlayerCards.subList(0, 2).stream().anyMatch(card -> card.getCardValueType() == Value.ACE);
            if (hasAce && aiPlayerScores.length > 1)
            {
                aiPlayerScoreLabels[aiPlayerIndex].setText(
                        "AI Player " + (aiPlayerIndex + 1) + ": " + aiPlayerScores[0] + "/" + aiPlayerScores[1]
                );
            } else
            {
                aiPlayerScoreLabels[aiPlayerIndex].setText(
                        "AI Player " + (aiPlayerIndex + 1) + ": " + aiPlayerScores[0]
                );
            }
        });

        // Aggiunge progressivamente le carte successive con un ritardo
        new Thread(() -> {
            try
            {
                Thread.sleep(1000); // Breve ritardo di 1 secondo per separare l'azione
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            for (int i = 2; i < aiPlayerCards.size(); i++)
            { // Aggiunge le carte oltre le prime 2
                final int currentCardIndex = i;
                SwingUtilities.invokeLater(() -> {
                    JPanel cardPanel = new JPanel(new OverlapLayout(120));
                    cardPanel.setOpaque(false);

                    // Ricostruisce l'intera mano fino alla carta corrente
                    for (int j = 0; j <= currentCardIndex; j++)
                    {
                        Card card = aiPlayerCards.get(j);
                        String value = card.getCardValueType().name();
                        String suit = card.getSuit().name();
                        ImageIcon cardImage = resizeCardImage(cardImageManager.getCardImage(value, suit));
                        cardPanel.add(new JLabel(cardImage));
                    }

                    // Reimposta il pannello man mano che si aggiungono le carte
                    aiPlayerPanels[aiPlayerIndex].removeAll();
                    aiPlayerPanels[aiPlayerIndex].add(cardPanel);
                    aiPlayerPanels[aiPlayerIndex].revalidate();
                    aiPlayerPanels[aiPlayerIndex].repaint();

                    // Aggiorna il punteggio ad ogni carta aggiunta
                    boolean hasAce = aiPlayerCards.stream().anyMatch(card -> card.getCardValueType() == Value.ACE);
                    if (hasAce && aiPlayerScores.length > 1)
                    {
                        aiPlayerScoreLabels[aiPlayerIndex].setText(
                                "AI Player " + (aiPlayerIndex + 1) + ": " + aiPlayerScores[0] + "/" + aiPlayerScores[1]
                        );
                    } else
                    {
                        aiPlayerScoreLabels[aiPlayerIndex].setText(
                                "AI Player " + (aiPlayerIndex + 1) + ": " + aiPlayerScores[0]
                        );
                    }
                });

                try
                {
                    Thread.sleep(2000); // Ritardo di 1 secondo tra una carta e la successiva
                } catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public int askForBet()
    {
        while (true)
        {
            String betInput = JOptionPane.showInputDialog(this, "Quanto vuoi scommettere?");
            if (betInput == null)
            {
                return 0; // Oppure un valore predefinito
            }
            try
            {
                int bet = Integer.parseInt(betInput);
                if (bet > 0)
                {
                    return bet;
                }
            } catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "Inserisci un numero valido.", "Errore Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodo per aggiornare e mostrare le informazioni di gioco dopo che la scommessa è stata piazzata
    public void showGameInfo(int chips)
    {
        dealerScoreLabel.setText("Punteggio Dealer: ");
        playerScoreLabel.setText("First Hand: ");
        splitScoreLabel.setText("Second Hand: ");
        chipsLabel.setText("Fiches: " + chips);
        hitButton.setVisible(true);
        doubleDownButton.setVisible(true);
        standButton.setVisible(true);
        splitButton.setVisible(true);
        dealerScoreLabel.setVisible(true);
        playerScoreLabel.setVisible(true);
        splitScoreLabel.setVisible(true);
    }

    // Metodo per aggiornare e mostrare le informazioni di gioco dopo che la scommessa è stata piazzata
    public void showGameScreen()
    {
        remove(welcomePanel);

        // Main game panel
        mainPanel.setLayout(new BorderLayout());


        // Dealer panel
        JPanel dealerContainer = new JPanel(new BorderLayout());
        dealerContainer.setOpaque(false);
        dealerPanel.setOpaque(false);
        dealerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center the cards
        dealerScoreLabel.setForeground(Color.RED);
        dealerContainer.add(dealerPanel, BorderLayout.CENTER);
        dealerContainer.add(dealerScoreLabel, BorderLayout.SOUTH);
        mainPanel.add(dealerContainer, BorderLayout.NORTH);

        // Players container (Player first, then AI players)
        JPanel playersContainer = new JPanel(new GridLayout(1, aiPlayerPanels.length + 1, 10, 10)); // One row: Player + AI
        playersContainer.setOpaque(false);

        // Player panel
        JPanel playerContainer = new JPanel(new BorderLayout());
        playerContainer.setOpaque(false);
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerScoreLabel.setForeground(Color.GREEN);
        splitScoreLabel.setForeground(Color.WHITE);
        playerContainer.add(playerPanel, BorderLayout.NORTH); // Move cards above the score
        playerContainer.add(splitScoreLabel, BorderLayout.SOUTH);
        playerContainer.add(playerScoreLabel, BorderLayout.SOUTH);
        playersContainer.add(playerContainer);

        // Split Hand panel
        splitPanel.setOpaque(false);
        splitPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        splitScoreLabel.setForeground(Color.GREEN);

        // Add split hand panel above the main hand panel
        playerContainer.add(splitPanel, BorderLayout.CENTER);
        playerContainer.add(playerPanel, BorderLayout.NORTH);
        playerContainer.add(playerScoreLabel, BorderLayout.SOUTH);
        playersContainer.add(playerContainer);

        // Chips panel
        JPanel chipsContainer = new JPanel();
        chipsContainer.setLayout(new BoxLayout(chipsContainer, BoxLayout.Y_AXIS));
        chipsContainer.setOpaque(false);
        chipsLabel.setForeground(Color.GREEN);
        chipsContainer.add(Box.createVerticalStrut(150)); // Spacing
        chipsContainer.add(Box.createVerticalGlue());
        mainPanel.add(chipsContainer, BorderLayout.LINE_END);

        // AI players panel
        for (int i = 0; i < aiPlayerPanels.length; i++)
        {
            JPanel aiPlayerContainer = new JPanel(new BorderLayout());
            aiPlayerContainer.setOpaque(false);
            aiPlayerPanels[i].setOpaque(false);
            aiPlayerPanels[i].setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            aiPlayerScoreLabels[i].setForeground(Color.ORANGE);
            aiPlayerContainer.add(aiPlayerPanels[i], BorderLayout.NORTH); // Move cards above the score
            aiPlayerChipsLabels[i].setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0)); // Add left margin
            aiPlayerContainer.add(aiPlayerChipsLabels[i], BorderLayout.SOUTH);
            playersContainer.add(aiPlayerContainer);
        }

        // Add margin to lower the player panels
        JPanel loweredPlayersContainer = new JPanel(new BorderLayout());
        loweredPlayersContainer.setOpaque(false);
        loweredPlayersContainer.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // Lower all players
        loweredPlayersContainer.add(playersContainer, BorderLayout.CENTER);

        mainPanel.add(loweredPlayersContainer, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        levelLabel.setForeground(Color.GREEN);
        gamesPlayedLabel.setForeground(Color.GREEN);
        gamesWonLabel.setForeground(Color.GREEN);
        gamesTiedLabel.setForeground(Color.GREEN);
        gamesLostLabel.setForeground(Color.GREEN);
        AIPlayer1ChipsBet.setForeground(Color.ORANGE);
        AIPlayer2ChipsBet.setForeground(Color.ORANGE);
        AIPlayer3ChipsBet.setForeground(Color.ORANGE);
        infoPanel.add(levelLabel);
        infoPanel.add(gamesPlayedLabel);
        infoPanel.add(gamesWonLabel);
        infoPanel.add(gamesTiedLabel);
        infoPanel.add(gamesLostLabel);
        infoPanel.add(AIPlayer1ChipsBet);
        infoPanel.add(AIPlayer2ChipsBet);
        infoPanel.add(AIPlayer3ChipsBet);
        mainPanel.add(infoPanel, BorderLayout.LINE_END);

        // Player scores panel
        JPanel playerScoresPanel = new JPanel();
        playerScoresPanel.setLayout(new BoxLayout(playerScoresPanel, BoxLayout.Y_AXIS));
        playerScoresPanel.setOpaque(false);
        playerScoresPanel.add(chipsLabel);
        playerScoresPanel.add(playerScoreLabel);
        playerScoresPanel.add(splitScoreLabel);

        for (JLabel aiPlayerScoreLabel : aiPlayerScoreLabels)
        {
            playerScoresPanel.add(aiPlayerScoreLabel);
        }
        mainPanel.add(playerScoresPanel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(220, 220, 220));
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(splitButton);
        buttonPanel.add(doubleDownButton);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set visibility for all components
        setVisibility(true, playerPanel, dealerPanel, splitPanel, playerScoreLabel, dealerScoreLabel, splitScoreLabel,
                chipsLabel, messageLabel, levelLabel, gamesPlayedLabel, gamesWonLabel, gamesTiedLabel, gamesLostLabel,
                hitButton, doubleDownButton, standButton, splitButton, restartButton);

        for (int i = 0; i < aiPlayerPanels.length; i++)
        {
            aiPlayerPanels[i].setVisible(true);
            aiPlayerScoreLabels[i].setVisible(true);
        }

        revalidate();
        repaint();
    }

    private void setVisibility(boolean visible, Component... components)
    {
        for (Component component : components)
        {
            if (component != null)
            {
                component.setVisible(visible);
            }
        }
    }
    public void updateChips(int chips)
    {
        chipsLabel.setText("Fiches: " + chips);
    }

    // Modifica i metodi come updateAIChips() in modo che ricevano i dati dall'esterno
    public void updateAIChips(List<AiPlayer> aiPlayers)
    {
        for (int i = 0; i < aiPlayers.size(); i++)
        {
            AiPlayer aiPlayer = aiPlayers.get(i);
            aiPlayerChipsLabels[i].setText("AI Player " + (i + 1) + " Fiches: " + aiPlayer.getChips());
        }
    }

    public void updateAIChipsBet(List<AiPlayer> aiPlayers)
    {
        JLabel[] aiBetsLabels = {AIPlayer1ChipsBet, AIPlayer2ChipsBet, AIPlayer3ChipsBet};

        for (int i = 0; i < aiPlayers.size(); i++)
        {
            aiBetsLabels[i].setText("AI" + (i + 1) + " Fiches scommesse: " + aiPlayers.get(i).getBetAmount());
            aiBetsLabels[i].revalidate();
            aiBetsLabels[i].repaint();
        }
    }

    public void updatePlayerScore(int minScore, int maxScore)
    {
        if (minScore == maxScore)
        {
            playerScoreLabel.setText(getPlayerNickname() + "'s First Hand: " + minScore);
        } else
        {
            playerScoreLabel.setText(getPlayerNickname() + "'s First Hand: " + minScore + "/" + maxScore);
        }
    }
    public void updateAIPlayerChips(int aiPlayerIndex, int chips)
    {
        if (aiPlayerIndex < 0 || aiPlayerIndex >= aiPlayerChipsLabels.length)
        {
            System.err.println("Errore: Indice AIPlayer fuori dai limiti: " + aiPlayerIndex);
            return;
        }
        if (aiPlayerChipsLabels[aiPlayerIndex] == null)
        {
            System.err.println("Errore: AI Player Label non inizializzata per l'indice: " + aiPlayerIndex);
            return;
        }
        aiPlayerChipsLabels[aiPlayerIndex].setText("AI Player " + (aiPlayerIndex + 1) + " Chips: " + chips);
        aiPlayerChipsLabels[aiPlayerIndex].revalidate();
        aiPlayerChipsLabels[aiPlayerIndex].repaint();
    }

    public void updateAllAIPlayerChips(List<AiPlayer> aiPlayers)
    {
        for (int i = 0; i < aiPlayers.size(); i++)
        {
            AiPlayer aiPlayer = aiPlayers.get(i);
            int chips = aiPlayer.getChips(); // Recupera le fiches dall'oggetto AiPlayer
            updateAIPlayerChips(i, chips); // Aggiorna l'etichetta
        }
    }

    public void updateAllChips(int playerChips, List<AiPlayer> aiPlayers)
    {
        updateChips(playerChips);
        updateAIChips(aiPlayers);
    }

    public void addDoubleDownListener(ActionListener listener)
    {
        doubleDownButton.addActionListener(listener);
    }
    public void updateDealerScore(int minScore, int maxScore)
    {
        if (minScore == maxScore)
        {
            dealerScoreLabel.setText("Punteggio Dealer: " + minScore);
        } else
        {
            dealerScoreLabel.setText("Punteggio Dealer: " + minScore + "/" + maxScore);
        }
    }

    public void updateSplitScore(int minScore, int maxScore)
    {
        if (minScore == maxScore)
        {
            splitScoreLabel.setText(getPlayerNickname() + "'s Second Hand: " + minScore);
        } else
        {
            splitScoreLabel.setText(getPlayerNickname() + "'s Second Hand: " + minScore + "/" + maxScore);
        }
    }

    public void resetScores()
    {
        playerScoreLabel.setText("First Hand: ");
        dealerScoreLabel.setText("Punteggio Dealer: ");
        splitScoreLabel.setText("Second Hand: ");
    }

    public void disableGameControls()
    {
        hitButton.setEnabled(false);
        doubleDownButton.setEnabled(false);
        standButton.setEnabled(false);
        splitButton.setEnabled(false);
    }

    public void enableGameControls()
    {
        hitButton.setEnabled(true);
        doubleDownButton.setEnabled(true);
        standButton.setEnabled(true);
        splitButton.setEnabled(true);
    }

    public void clearSplitHand()
    {
        splitPanel.removeAll();
        splitPanel.revalidate();
        splitPanel.repaint();
    }
    public void updateDealerHand(List<Card> dealerCards, boolean showAll)
    {
        dealerPanel.removeAll();
        int visibleScore = 0;

        for (int i = 0; i < dealerCards.size(); i++)
        {
            Card card = dealerCards.get(i);
            ImageIcon cardImage = (i == 1 && !showAll)
                    ? resizeCardImage(cardImageManager.getBackImage()) // Seconda carta nascosta
                    : resizeCardImage(cardImageManager.getCardImage(card.getCardValueType().name(), card.getSuit().name()));

            dealerPanel.add(new JLabel(cardImage));
            if (showAll || i != 1)
            {
                visibleScore += card.getCardValueType().getValue();
            }
        }

        updateDealerScore(visibleScore, visibleScore);
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    public void revealDealerHand(List<Card> dealerCards)
    {
        dealerPanel.removeAll();
        for (Card card : dealerCards)
        {
            String value = card.getCardValueType().name();
            String suit = card.getSuit().name();
            ImageIcon cardImage = resizeCardImage(cardImageManager.getCardImage(value, suit));
            dealerPanel.add(new JLabel(cardImage));
        }
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    // Metodi per i listener dei pulsanti
    public void addHitListener(ActionListener listener)
    {
        hitButton.addActionListener(listener);
    }

    public void addStandListener(ActionListener listener)
    {
        standButton.addActionListener(listener);
    }

    public void addSplitListener(ActionListener listener)
    {
        splitButton.addActionListener(listener);
    }

    private ImageIcon resizeCardImage(ImageIcon originalIcon)
    {
        Image image = originalIcon.getImage();
        Image scaledImage = image.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void showScoreLabels(boolean visible)
    {
        dealerScoreLabel.setVisible(visible);
        playerScoreLabel.setVisible(visible);
        splitScoreLabel.setVisible(visible);
    }

    public void showMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message);
    }

    public void enableSplitControls()
    {
        // Enable Controls related to split hand
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        splitButton.setEnabled(false);
    }

    public void addSplitPanel()
    {
        if (splitPanel.getParent() == null)
        {
            add(splitPanel, BorderLayout.EAST);
        }
    }

    // Metodo per aggiungere un listener al pulsante di selezione della mano
    private void addCardClickListener(JLabel cardLabel, boolean isSplitHand)
    {
        cardLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                if (isSplitHand)
                {
                    hitSplitHand();
                } else
                {
                    hitMainHand();
                }
            }
        });
    }

    // Add methods to handle the card click
    private void hitMainHand()
    {
        for (ActionListener listener : hitButton.getActionListeners())
        {
            listener.actionPerformed(new ActionEvent(hitButton, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    private void hitSplitHand()
    {
        for (ActionListener listener : hitButton.getActionListeners())
        {
            listener.actionPerformed(new ActionEvent(hitButton, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public void updatePlayerHand(List<Card> playerCards, int[] playerScores)
    {
        playerPanel.removeAll();
        JPanel cardPanel = new JPanel(new OverlapLayout(120)); // Adjust the overlap value here
        cardPanel.setOpaque(false);
        for (Card card : playerCards)
        {
            String value = card.getCardValueType().name();
            String suit = card.getSuit().name();
            ImageIcon cardImage = resizeCardImage(cardImageManager.getCardImage(value, suit));
            cardPanel.add(new JLabel(cardImage));
        }
        playerPanel.add(cardPanel);
        updatePlayerScore(playerScores[0], playerScores.length > 1 ? playerScores[1] : playerScores[0]);
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    public void updateSplitHand(List<Card> splitCards, int[] splitScores)
    {
        splitPanel.removeAll();

        // Crea e aggiungi carte al pannello split
        JPanel cardPanel = new JPanel(new OverlapLayout(120));
        cardPanel.setOpaque(false);
        for (Card card : splitCards)
        {
            String value = card.getCardValueType().name();
            String suit = card.getSuit().name();
            ImageIcon cardImage = resizeCardImage(cardImageManager.getCardImage(value, suit));
            cardPanel.add(new JLabel(cardImage));
        }

        splitPanel.add(cardPanel);
        updateSplitScore(splitScores[0], splitScores.length > 1 ? splitScores[1] : splitScores[0]);
        splitPanel.revalidate();
        splitPanel.repaint();
    }

    public JComboBox<Integer> getNumAIPlayersComboBox()
    {
        return numAIPlayersComboBox;
    }

    public void updateScores(int gamesPlayed, int gamesWon, int gamesTied, int gamesLost, int level)
    {
        gamesPlayedLabel.setText("Games Played: " + gamesPlayed);
        gamesWonLabel.setText("Games Won: " + gamesWon);
        gamesTiedLabel.setText("Games Tied: " + gamesTied);
        gamesLostLabel.setText("Games Lost: " + gamesLost);
        levelLabel.setText("Level: " + level);


    }

    public void hideDoubleDownButton()
    {
        doubleDownButton.setVisible(false);
    }
    public void hideSplitButton()
    {
        splitButton.setVisible(false);
    }

    public void updateAIPlayerChips(List<AiPlayer> aiPlayers)
    {
        for (int i = 0; i < aiPlayers.size(); i++)
        {
            AiPlayer aiPlayer = aiPlayers.get(i);
            aiPlayerChipsLabels[i].setText("AI Player " + (i + 1) + " Fiches: " + aiPlayer.getChips());
        }
    }
    public void showRestartButton(boolean b)
    {
        restartButton.setVisible(b);
    }

    public void addRestartButtonListener(ActionListener listener)
    {
        restartButton.addActionListener(listener);
    }

    public void hideRestartButton()
    {
        restartButton.setVisible(false);
    }
    public void updateLevel(int level)
    {
        levelLabel.setText("Level: " + level);
    }
    //Ottenere Fiches iniziali
    public int getInitialChips()
    {
        String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
        if (selectedDifficulty.contains("Hard"))
        {
            return 1000;
        }
        if (selectedDifficulty.contains("Medium"))
        {
            return 2000;
        }
        if (selectedDifficulty.contains("Easy"))
        {
            return 3000;
        }
        return 1000;
    }

    // Metodo per mostrare un messaggio di assicurazione
    public boolean askForInsurance()
    {
        int option = JOptionPane.showConfirmDialog(this, "Il dealer ha un asso. Vuoi fare l'assicurazione?", "Assicurazione", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    // Metodo per aggiornare la visibilità dei pannelli AI
    public void updateAIPlayersVisibility(int numAIPlayers)
    {
        for (int i = 0; i < aiPlayerPanels.length; i++)
        {
            boolean isVisible = (i < numAIPlayers);
            aiPlayerPanels[i].setVisible(isVisible);
            aiPlayerScoreLabels[i].setVisible(isVisible);
            aiPlayerChipsLabels[i].setVisible(isVisible);
        }
    }

    // Metodo per mostrare un messaggio di fine partita
    public String getPlayerNickname()
    {
        return nicknameField.getText();
    }

    // Metodo per mostrare l'assicurazione
    public void showInsuranceResult(boolean dealerHasBlackjack, int playerInsuranceBet, List<AiPlayer> aiPlayers)
    {
        if (aiPlayers == null)
        {
            return; //Evita il clash con il metodo updateAIChips
        }

        StringBuilder message = new StringBuilder();
        // Check if the player placed an insurance bet
        if (playerInsuranceBet > 0)
        {
            if (dealerHasBlackjack)
            {
                int winnings = playerInsuranceBet * 2;
                message.append("You placed an insurance bet of ").append(playerInsuranceBet)
                        .append(" and won ").append(winnings).append(" chips.\n");
            } else
            {
                message.append("You placed an insurance bet of ").append(playerInsuranceBet)
                        .append(" and lost it.\n");
            }
        }

        // Check if AI players placed an insurance bet
        for (int i = 0; i < aiPlayers.size(); i++)
        {
            AiPlayer aiPlayer = aiPlayers.get(i);
            int aiInsuranceBet = aiPlayer.getInsuranceBet();
            if (aiInsuranceBet > 0)
            {
                if (dealerHasBlackjack)
                {
                    int winnings = aiInsuranceBet * 2;
                    message.append("AI Player ").append(i + 1).append(" placed an insurance bet of ")
                            .append(aiInsuranceBet).append(" and won ").append(winnings).append(" chips.\n");
                } else
                {
                    message.append("AI Player ").append(i + 1).append(" placed an insurance bet of ")
                            .append(aiInsuranceBet).append(" and lost it.\n");
                }
            } else
            {
                message.append("AI Player ").append(i + 1).append(" did not place an insurance bet.\n");
            }
        }

        JOptionPane.showMessageDialog(this, message.toString(), "Insurance Result", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateMessage(String message)
    {
        messageLabel.setText(message);
    }
    public void updateBackgroundMessage(String s)
    {
        messageLabel.setText(s);
    }

    public void hideSplitPanel()
    {
        splitPanel.setVisible(false);
        splitScoreLabel.setVisible(false);
        clearSplitHand();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        // Update the view based on the model changes
    }
}
