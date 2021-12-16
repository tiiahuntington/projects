package menu;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author tiia1
 * @version 22 Mar 2021
 *
 */
public class RaakaAineenTyyppi implements Cloneable{
    
    private int tyyppiId;
    private String tyyppi = "";
    private String osa = "";
    private static int seuraavaIndx3 = 1;

    /**
     * Annetaan aterialle tunnus
     * @example
     * <pre name="test">
     * RaakaAineenTyyppi liha = new RaakaAineenTyyppi();
     * liha.rekisteroi();
     * RaakaAineenTyyppi vege = new RaakaAineenTyyppi();
     * vege.rekisteroi();
     * vege.getTyyppiId() === liha.getTyyppiId()+1;
     * </pre>
     */
    public void rekisteroi() {
        this.tyyppiId = seuraavaIndx3;
        seuraavaIndx3++;
    }
    
    
    /**
     * 
     * @return olion ateria id:n
     */
    public int getTyyppiId() { 
        return this.tyyppiId;
    }
    
    
    /**
     * Hakee raaka-aineen tyypin nimen
     * @return nimi
     */
    public String getNimi() {
        return this.tyyppi;
    }
    
    
    /**
     * Asettaa tyypin nimeksi annetun parametrin
     * @param nimi tyypin nimi
     */
    public void setNimi(String nimi) {
        this.tyyppi = nimi;
    }
    
    
    /**
     * Asetettaa tyypin aterian osaksi annetun parametrin 
     * @param osa raaka-aineen tyypin osa ateriasta
     */
    public void setOsa(String osa) {
        this.osa = osa;
    }
    
    /**
     * Kloonaa olion.
     * @example
     * <pre name="test">
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi(); 
     * kasvis.rekisteroi(); kasvis.setNimi("kasvis");
     * RaakaAineenTyyppi kasvisKlooni = new RaakaAineenTyyppi();
     * try {
     * kasvisKlooni = kasvis.clone();
     * } catch (CloneNotSupportedException e) {
     *     System.err.println("Kloonaus ei onnistunut!");
     * }
     * kasvisKlooni.setNimi("s");
     * kasvis.getNimi() === "kasvis";
     * kasvisKlooni.getNimi() === "s";
     * </pre>
     */
    @Override
    public RaakaAineenTyyppi clone() throws CloneNotSupportedException {
        RaakaAineenTyyppi klooni = (RaakaAineenTyyppi) super.clone();
        return klooni;
    }
    
    
    /**
     * Erottaa annetusta tiedoston rivista olion tiedot ja asettaa ne.
     * @param rivi tiedostosta luettu rivi
     * @example
     * <pre name="test">
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi();
     * kasvis.parse("1|kasvis|lisuke|");
     * kasvis.getNimi() === "kasvis";
     * kasvis.getTyyppiId() === 1;
     * </pre>
     */
    public void parse(String rivi) { //raakaaineen tyyppi id|tyyppi nimi|aterian osa
        String[] tiedot = rivi.split("\\|");
        int i = 0;
        try {
           i = Integer.parseInt(tiedot[0]);  
        }  catch (NumberFormatException ex) {
            System.err.println("Ei voida lukea numeroa!");
        }
        if (i != 0) this.tyyppiId = i;
        this.tyyppi = tiedot[1];
        this.osa = tiedot[2];
        
    }
    
    
    /**
     * Muutetaan olion tiedot merkkijonoksi
     * @example
     * <pre name="test">
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi();
     * kasvis.rekisteroi();
     * kasvis.setOsa("lisuke");
     * kasvis.toString() === "1||lisuke|";
     * kasvis.setNimi("kasvis");
     * kasvis.toString() === "1|kasvis|lisuke|";
     * </pre>
     */
    @Override
    public String toString() {
        return this.tyyppiId + "|" + this.tyyppi + "|" + this.osa + "|";
    }
    
    
    /**
     * @param virta tulostustreamin nimi
     */
    public void tulosta(OutputStream virta) {
        tulosta(new PrintStream(virta));
    }
    
    
    /**
     * @param out tulostusstreamin nimi
     */
    public void tulosta(PrintStream out) {
        out.println(this.tyyppiId + " " + this.tyyppi + " " + this.osa);
    }
    
    
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        // Auto-generated method stub

    }

}
