package Model;

/**
 * La classe Player rappresenta il giocatore principale nel gioco di Blackjack.
 * Implementa il pattern Singleton per garantire che ci sia un solo giocatore per partita.
 * Questa classe estende {@link BasePlayer} e gestisce le statistiche del giocatore,
 * tra cui vittorie, sconfitte, pareggi e livello di gioco.
 */
public class Player extends BasePlayer
{
    /** Istanza unica di Player per l'implementazione del Singleton. */
    private static Player instance;

    /** Numero di vittorie necessarie per aumentare di livello. */
    private static final int WINS_TO_LEVEL_UP = 3;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesTied;
    private int winsToLevelUp;
    private int level;

    /**
     * Costruttore privato per impedire la creazione di più istanze.
     * Inizializza le statistiche del giocatore.
     */
    private Player()
    {
        super();
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesTied = 0;
        this.level = 1;
        this.winsToLevelUp = 0;
    }

    /**
     * Metodo per ottenere l'unica istanza del giocatore.
     *
     * @return L'istanza unica di {@code Player}.
     */
    public static Player getInstance()
    {
        if (instance == null)
        {
            instance = new Player();
        }
        return instance;
    }

    // ===============================
    // Metodi per la gestione delle partite
    // ===============================

    /**
     * Restituisce il numero totale di partite giocate.
     *
     * @return Numero di partite giocate.
     */
    public int getGamesPlayed()
    {
        return gamesPlayed;
    }

    /**
     * Incrementa il numero di vittorie del giocatore e aggiorna il conteggio
     * delle vittorie consecutive necessarie per il livello successivo.
     * Se il numero di vittorie consecutive raggiunge {@code WINS_TO_LEVEL_UP},
     * il livello del giocatore viene aumentato.
     */
    public void incrementGamesWon()
    {
        this.gamesWon++;
        this.winsToLevelUp++;

        if (this.winsToLevelUp >= WINS_TO_LEVEL_UP)
        {
            incrementLevel();
            this.winsToLevelUp = 0;
        }
    }

    /**
     * Incrementa il numero di partite giocate dal giocatore.
     */
    public void incrementGamesPlayed()
    {
        this.gamesPlayed++;
    }

    /**
     * Restituisce il numero totale di partite vinte dal giocatore.
     *
     * @return Numero di partite vinte.
     */
    public int getGamesWon()
    {
        return gamesWon;
    }

    /**
     * Restituisce il numero totale di partite perse dal giocatore.
     *
     * @return Numero di partite perse.
     */
    public int getGamesLost()
    {
        return gamesLost;
    }

    /**
     * Incrementa il numero di partite perse e azzera il conteggio delle vittorie consecutive.
     */
    public void incrementGamesLost()
    {
        this.gamesLost++;
        this.winsToLevelUp = 0;
    }

    /**
     * Restituisce il numero totale di partite pareggiate dal giocatore.
     *
     * @return Numero di partite pareggiate.
     */
    public int getGamesTied()
    {
        return gamesTied;
    }

    /**
     * Incrementa il numero di partite pareggiate.
     */
    public void incrementGamesTied()
    {
        this.gamesTied++;
    }

    // ===============================
    // Metodi per la gestione del livello
    // ===============================

    /**
     * Restituisce il livello attuale del giocatore.
     *
     * @return Il livello del giocatore.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Aumenta il livello del giocatore di 1 unità.
     */
    public void incrementLevel()
    {
        this.level++;
    }

    // ===============================
    // Metodi per la gestione delle fiches
    // ===============================


    /**
     * Resetta l'importo della scommessa del giocatore a 0.
     */
    public void resetBetAmount()
    {
        this.betamount = 0;
    }

    // ===============================
    // Metodi per il reset del giocatore
    // ===============================

    /**
     * Restituisce il numero di vittorie consecutive del giocatore.
     *
     * @return Il numero di vittorie consecutive.
     */
    public int getWinsToLevelUp()
    {
        return winsToLevelUp;
    }

    /**
     * Imposta il numero di vittorie consecutive necessarie per il livello successivo.
     *
     * @param winsToLevelUp Numero di vittorie consecutive.
     */
    public void setWinsToLevelUp(int winsToLevelUp)
    {
        this.winsToLevelUp = winsToLevelUp;
    }

    /**
     * Imposta il livello del giocatore.
     *
     * @param level Il nuovo livello del giocatore.
     */
    public void setLevel(int level)
    {
        this.level = level;
    }
}
