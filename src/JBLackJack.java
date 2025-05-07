import Controller.BlackJackController;
import Model.BlackJackGame;
import View.BlackJackView;
public class JBLackJack
{
    public static void main(String[] args)
    {
        //Inizializza il modello
        BlackJackGame game = new BlackJackGame();
        //Inizializza la vista
        BlackJackView view = new BlackJackView();
        //Inizializza il controller
        BlackJackController controller = new BlackJackController(view, game);
    }
}