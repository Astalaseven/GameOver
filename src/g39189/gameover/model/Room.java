package g39189.gameover.model;

public class Room
{
    private RoomType type;
    private boolean hidden;
    private WeaponType weapon;
    private BarbarianColor color;

    /**
     * Constructeur de Room
     * 
     * @param type
     *            type de figure de l’élément du donjon
     * @param hidden
     *            indique si la carte est cachée
     * @param weapon
     *            type d’arme de l’élément du donjon
     * @param color
     *            couleur de la carte
     */
    public Room(RoomType type, boolean hidden, WeaponType weapon,
            BarbarianColor color)
    {
        this.type = type;
        this.hidden = hidden;
        this.weapon = weapon;
        this.color = color;
    }

    /**
     * Modifie la valeur de hidden
     * 
     * @param hidden
     *            la nouvelle valeur de hidden
     */
    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    /**
     * Retourne la valeur de hidden
     * 
     * @return vrai si la carte est cachée, faux si elle est dévoilée
     */
    public boolean isHidden()
    {
        return hidden;
    }

    /**
     * Retourne la valeur de type
     * 
     * @return le type de la carte
     */
    public RoomType getType()
    {
        return type;
    }

    /**
     * Retourne la valeur de weapon
     * 
     * @return l’arme de la carte
     */
    public WeaponType getWeapon()
    {
        return weapon;
    }

    /**
     * Retourne la valeur de color
     * 
     * @return la couleur de la carte
     */
    public BarbarianColor getColor()
    {
        return color;
    }

    /**
     * Génère le hashcode d’une carte
     */
    @Override
    public int hashCode()
    {
        // utilisation d’un nombre premier assez grand et proche d’une puissance
        // de 2 pour une distribution optimale des données dans le bucket
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((color == null) ? 0 : color.hashCode());
        // 1231 et 1237 sont les valeurs utilisées dans le hashCode de Boolean
        result = (prime * result) + (hidden ? 1231 : 1237);
        result = (prime * result) + ((type == null) ? 0 : type.hashCode());
        result = (prime * result) + ((weapon == null) ? 0 : weapon.hashCode());
        return result;
    }

    /**
     * Compare si 2 cartes sont égales
     */
    @Override
    public boolean equals(Object obj)
    {
        // si l’objet se teste lui-même, on considère qu’il est égal
        if (this == obj)
        {
            return true;
        }
        // si l’objet est null ou d’une classe autre que Room, il est différent
        if ((obj == null) || (getClass() != obj.getClass()))
        {
            return false;
        }

        // les tests ci-dessus nous assurent que l’objet n’est pas null
        // et est de la classe Room, nous pouvons donc le caster
        Room other = (Room) obj;
        if ((color != other.color) || (hidden != other.hidden) 
                || (type != other.type) || (weapon != other.weapon))
        {
            return false;
        }
        return true;
    }

    // TODO remove
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Room [type=" + type + ", hidden=" + hidden + ", weapon="
                + weapon + ", color=" + color + "]";
    }
}
