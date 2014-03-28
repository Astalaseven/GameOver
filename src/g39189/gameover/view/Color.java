package g39189.gameover.view;

/**
 * Regroupe des méthodes permettant d’avoir des chaînes de caractères en
 * couleur sous Linux
 *
 * @author Bovyn Gatien - 39189
 */
public class Color
{
    
    /**
     * Retourne une chaîne de caractère qui sera imprimée en rouge
     * dans la console
     * 
     * @param str
     * @return la chaîne passée en paramètre entourée de codes ANSI
     * pour l’afficher en rouge
     */
    public static String red(String str)
    {
        return "\033[31m" + str + "\033[37m";
    }
    
    /**
     * Retourne une chaîne de caractère qui sera imprimée en vert
     * dans la console
     * 
     * @param str
     * @return la chaîne passée en paramètre entourée de codes ANSI
     * pour l’afficher en vert
     */
    public static String green(String str)
    {
        return "\033[32m" + str + "\033[37m";
    }
    
    /**
     * Retourne une chaîne de caractère qui sera imprimée en bleu
     * dans la console
     * 
     * @param str
     * @return la chaîne passée en paramètre entourée de codes ANSI
     * pour l’afficher en bleu
     */
    public static String blue(String str)
    {
        return "\033[34m" + str + "\033[37m";
    }
    
    /**
     * Retourne une chaîne de caractère qui sera imprimée en jaune
     * dans la console
     * 
     * @param str
     * @return la chaîne passée en paramètre entourée de codes ANSI
     * pour l’afficher en jaune
     */
    public static String yellow(String str)
    {
        return "\033[33m" + str + "\033[37m";
    }

}
