package menu;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author tiia1
 * @version 7 Apr 2021
 *
 */
public class RaakaAine implements Cloneable{
    
    private int raakaAineenId;
    private String nimi= "";
    private int tyyppiId;
    private static int seuraavaIndx2 = 100;

    /**
     * Annetaan raaka-aineelle tunnus
     * @example
     * <pre name="test">
     * RaakaAine tomaatti = new RaakaAine();
     * tomaatti.rekisteroi();
     * RaakaAine kurkku = new RaakaAine();
     * kurkku.rekisteroi();
     * kurkku.getRaakaAineenId() === tomaatti.getRaakaAineenId()+1;
     * </pre>
     */
    public void rekisteroi() {
        this.raakaAineenId = seuraavaIndx2;
        seuraavaIndx2++;
    }
    
    
    /**
     * Antaa olion id:n.
     * @return raaka-aine olion id:n
     */
    public int getRaakaAineenId() { 
        return this.raakaAineenId;
    }
    
    
    /**
     * Antaa olion raaka-aineen tyypin id:n.
     * @return tyypin id
     */
    public int getTyyppiId() {
        return this.tyyppiId;
    }
    
    
    /**
     * Antaa olion nimen.
     * @return nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * Asetetaan raaka-aineen nimi.
     * @param nimi joka halutaan asettaa
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    
    /**
     * Asetetaan raaka-aineelle tyyppi id:n perusteella.
     * @param id tyypin id
     */
    public void setTyyppiId(int id) {
        this.tyyppiId = id;
    }
    
    
    /**
     * Kloonaa olion.
     * @example
     * <pre name="test">
     * RaakaAine salaatti = new RaakaAine(); salaatti.rekisteroi(); salaatti.setNimi("salaatti");
     * RaakaAine salaattiKlooni = new RaakaAine();
     * try {
     * salaattiKlooni = salaatti.clone();
     * } catch (CloneNotSupportedException e) {
     *     System.err.println("Kloonaus ei onnistunut!");
     * }
     * salaattiKlooni.setNimi("s");
     * salaatti.getNimi() === "salaatti";
     * salaattiKlooni.getNimi() === "s";
     * </pre>
     */
    @Override
    public RaakaAine clone() throws CloneNotSupportedException {
        RaakaAine klooni = (RaakaAine) super.clone();
        return klooni;
    }
    
    
    /**
     * Erottaa annetusta tiedoston rivista olion tiedot ja asettaa ne.
     * @param rivi tiedostosta luettu rivi
     * @example
     * <pre name="test">
     * RaakaAine salaatti = new RaakaAine();
     * salaatti.parse("1|Salaatti|2|");
     * salaatti.getNimi() === "Salaatti";
     * salaatti.getTyyppiId() === 2;
     * salaatti.getRaakaAineenId() === 1;
     * </pre>
     */
    public void parse(String rivi) { //raakaaineenId|nimi|tyyppiId
        String[] tiedot = rivi.split("\\|");
        int i = 0;
        try {
           i = Integer.parseInt(tiedot[0]);  
        }  catch (NumberFormatException ex) {
            System.err.println("Ei voida lukea numeroa!");
        }
        if (i != 0) {
            this.raakaAineenId = i;
            if (raakaAineenId > seuraavaIndx2) seuraavaIndx2 = raakaAineenId;
            seuraavaIndx2++;
        }
        this.nimi = tiedot[1];
        try {
            this.tyyppiId = Integer.parseInt(tiedot[2]);
        }  catch (NumberFormatException e) {
            System.err.println("Ei voida lukea numeroa!");
        }
        
    }
    
    
    /**
     * Muutetaan olion tiedot merkkijonoksi
     * @example
     * <pre name="test">
     * RaakaAine salaatti = new RaakaAine();
     * salaatti.rekisteroi();
     * salaatti.setTyyppiId(3);
     * salaatti.toString() === "100||3|";
     * salaatti.setNimi("salaatti");
     * salaatti.toString() === "100|salaatti|3|";
     * </pre>
     */
    @Override
    public String toString() {
        return raakaAineenId + "|" + nimi + "|" + tyyppiId + "|";
    }
    
    
    /**
     * 
     * @param virta tulostustreamin nimi
     */
    public void tulosta(OutputStream virta) {
        tulosta(new PrintStream(virta));
    }
    
    
    /**
     * Tulostaa raaka-aineen id:n, nimen ja tyypin id:n
     * @param out tulostusstream
     */
    public void tulosta(PrintStream out) {
        out.println(this.raakaAineenId + " " + this.nimi + " " + this.tyyppiId);
    }
    
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        // 

    }
}
