package menu;

/**
 * @author tiia1
 * @version 22 Mar 2021
 *
 */
public class SailoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * käytettävä viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
