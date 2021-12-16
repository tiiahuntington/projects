package menu;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author tiia1
 * @version 23 Feb 2021
 *
 */
public class Ateria implements Cloneable{
    
    private int ateriaId;
    private String nimi = "";
    private String ateriaAika = "";
    private static int seuraavaIndx = 1;
    
    
    /**
     * Annetaan aterialle tunnus
     * @example
     * <pre name="test">
     * Ateria keitto = new Ateria();
     * keitto.rekisteroi();
     * Ateria keitto2 = new Ateria();
     * keitto2.rekisteroi();
     * keitto2.getAteriaId() === keitto.getAteriaId()+1;
     * </pre>
     */
    public void rekisteroi() {
        this.ateriaId = seuraavaIndx;
        seuraavaIndx++;
    }

    
    /**
     * Haetaan aterian id.
     * @return olion ateria id:n
     */
    public int getAteriaId() { 
        return this.ateriaId;
    }
    
    
    /**
     * hakee aterian nimen
     * @return atrian nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * Palauttaa taman aterian ajankohdan.
     * @return aterian ajankohta
     */
    public String getAika() {
        return this.ateriaAika;
    }
    
    
    /**
     * Asettaa annetun parametrin olion nimeksi.
     * @param nimi annettu nimi 
     * @example
     * <pre name="test">
     * Ateria salaatti = new Ateria();
     * salaatti.setNimi("SALAATTI");
     * salaatti.getNimi() === "Salaatti";
     * salaatti.setNimi("salaatti");
     * salaatti.getNimi() === "Salaatti";
     * salaatti.setNimi("SALAATTI  ");
     * salaatti.getNimi() === "Salaatti";
     * </pre>
     */  
    public void setNimi(String nimi) {
        String isolla = nimi.toLowerCase();
        isolla = isolla.trim();
        isolla = isolla.substring(0, 1).toUpperCase() + isolla.substring(1);
        this.nimi = isolla;
    }
    
    
    /**
     * Asettaa annetun parametrin olion ateriointiajaksi.
     * @param aika annettu atriointiaika
     * @example
     * <pre name="test">
     * Ateria salaatti = new Ateria();
     * salaatti.setAika("päivällinen");
     * salaatti.getAika() === "päivällinen";
     * </pre>
     */
    public void setAika(String aika) {
        this.ateriaAika = aika;
    }
    
    /**
     * Kloonaa olion.
     * @example
     * <pre name="test">
     * Ateria salaatti = new Ateria(); salaatti.rekisteroi(); salaatti.setNimi("salaatti");
     * Ateria salaattiKlooni = new Ateria();
     * try {
     * salaattiKlooni = salaatti.clone();
     * } catch (CloneNotSupportedException e) {
     *     System.err.println("Kloonaus ei onnistunut!");
     * }
     * salaattiKlooni.setNimi("s");
     * salaatti.getNimi() === "Salaatti";
     * salaattiKlooni.getNimi() === "S";
     * </pre>
     */
    @Override
    public Ateria clone() throws CloneNotSupportedException {
        Ateria klooni = (Ateria) super.clone();
        return klooni;
    }
    
    
    /**
     * Erottaa annetusta tiedoston rivista olion tiedot ja asettaa ne.
     * @param rivi tiedostosta luettu rivi
     * @example
     * <pre name="test">
     * Ateria salaatti = new Ateria();
     * salaatti.parse("1|Salaatti|iltapala|");
     * salaatti.getNimi() === "Salaatti";
     * salaatti.getAteriaId() === 1;
     * salaatti.getAika() === "iltapala";
     * </pre>
     */
    public void parse(String rivi) { //aterian id|aterian nimi|ateria-aika|
        String[] tiedot = rivi.split("\\|");
        int i = 0;
        try {
           i = Integer.parseInt(tiedot[0]);  
        }  catch (NumberFormatException ex) {
            System.err.println("Ei voida lue numeroa!");
        }
        if (i != 0) {
            ateriaId = i;
            if (ateriaId > seuraavaIndx) seuraavaIndx = ateriaId;
            seuraavaIndx++;
        }
        this.nimi = tiedot[1];
        this.ateriaAika = tiedot[2];
    }
    
    
    /**
     * Muutetaan olion tiedot merkkijonoksi
     * @example
     * <pre name="test">
     * Ateria salaatti = new Ateria();
     * salaatti.rekisteroi();
     * salaatti.setAika("iltapala");
     * salaatti.toString() === "1||iltapala|";
     * salaatti.setNimi("Salaatti");
     * salaatti.toString() === "1|Salaatti|iltapala|";
     * </pre>
     */
    @Override
    public String toString() {
        return ateriaId + "|" + nimi + "|" + ateriaAika + "|";
    }
    
    
    /**
     * Luo tulostuksen uuteen näyttöön.
     * @param virta tulostustreamin nimi
     */
    public void tulosta(OutputStream virta) {
        tulosta(new PrintStream(virta));
    }
    
    
    /**
     * Tulostaa olion tiedot haluamallamme tavalla
     * @param out tulostusstreamin nimi
     */
    public void tulosta(PrintStream out) {
        out.println(this.ateriaId + " " + this.nimi);
        out.println(this.ateriaAika );
    }

    
    /**
     * - tietää aterian tiedot                       
     * - osaa muuttaa aterian nimen syntäksin mukaiseksi  
     * - osaa muuttaa merkkijonon jäsenen tiedoiksi       
     * - osaa antaa merkkijonona i sarakkeen tiedot       
     * - osaa asettaan merkkijonon i sarakkeen paikkaan
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Ateria a = new Ateria();
        Ateria b = new Ateria();
        a.rekisteroi();
        b.rekisteroi();
        a.tulosta(System.out);
        b.tulosta(System.out);

    }

} 
