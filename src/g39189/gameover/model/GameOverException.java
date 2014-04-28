package g39189.gameover.model;

/**
 * Cette classe est une exception contrôlée par le compilateur.
 * Cette exception sera lancée dès que l'on demande à la partie
 * model quelque chose d'incohérent.
 * 
 * @author Bovyn Gatien - 39189
 */
public class GameOverException extends Exception
{
    /**
     * Construit une nouvelle GameOverException avec
     * le message de l’erreur.
     * 
     * @param err
     *            le message d’erreur à afficher
     */
    public GameOverException(String err)
    {
        super(err);
    }
}
