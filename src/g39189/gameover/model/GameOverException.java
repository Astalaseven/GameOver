package g39189.gameover.model;

public class GameOverException extends Exception
{
    /** Constructeur de l’exception GameOverException
     * @param err le message d’erreur à afficher
     */
    public GameOverException(String err)
    {
        System.out.print(err);
        System.out.print("  −  Tapez sur Enter pour continuer");
        System.console().readLine();
    }
}
