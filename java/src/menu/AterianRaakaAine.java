package menu;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author tiia1
 * @version 23 Feb 2021
 *
 */
public class AterianRaakaAine implements Cloneable{

    private int aterianId;
    private int raakaAineenId;
    private static int seuraavaIndx1 = 1;
    /**
     * Annetaan raaka-aineelle tunnus
     * @example
     * <pre name="test">
     * AterianRaakaAine pottu = new AterianRaakaAine();
     * pottu.rekisteroi();
     * AterianRaakaAine kurkku = new AterianRaakaAine();
     * kurkku.rekisteroi();
     * kurkku.getAteriaId() === pottu.getAteriaId() + 1;
     * </pre>
     */
    public void rekisteroi() {
        this.aterianId = seuraavaIndx1;
        seuraavaIndx1++;
    }
    
    
    /**
     * Hakee aterian id:n.
     * @return raaka-aine olion id:n
     */
    public int getAteriaId() { 
        return this.aterianId;
    }
    
    
    /**
     * Antaa olion raaka-aineen id:n.
     * @return raaka-aineen id
     */
    public int getRaakaAineId() {
        return this.raakaAineenId;
    }
    
    
    /**
     * Asetetaan aterian id:ksi parametrilla annettu tieto
     * @param i aterian id
     * @example
     * <pre name="test">
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * salaatinTomaatti.setAteriaId(1);
     * salaatinTomaatti.getAteriaId() === 1;
     * </pre>
     */
    public void setAteriaId(int i) {
        this.aterianId = i;
    }
    
    
    /**
     * Asetetaan raaka-aineen id:ksi parametrilla annettu tieto
     * @param i raaka-aineen id
     * @example
     * <pre name="test">
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * salaatinTomaatti.setRaakaAineId(1);
     * salaatinTomaatti.getRaakaAineId() === 1;
     * </pre>
     */
    public void setRaakaAineId(int i) {
        this.raakaAineenId = i;
    }
    
    
    /**
     * Kloonaa olion
     * @example
     * <pre name="test">
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine(); 
     * salaatinTomaatti.rekisteroi(); salaatinTomaatti.setRaakaAineId(2);
     * AterianRaakaAine tomaatinKlooni = new AterianRaakaAine();
     * try {
     * tomaatinKlooni = salaatinTomaatti.clone();
     * } catch (CloneNotSupportedException e) {
     *     System.err.println("Kloonaus ei onnistunut!");
     * }
     * tomaatinKlooni.setRaakaAineId(3);
     * salaatinTomaatti.getRaakaAineId() === 2;
     * tomaatinKlooni.getRaakaAineId() === 3;
     * </pre>
     */
    @Override
    public AterianRaakaAine clone() throws CloneNotSupportedException {
        AterianRaakaAine klooni = (AterianRaakaAine) super.clone();
        return klooni;
    }
    
    
    /**
     * Erottaa annetusta tiedoston rivista olion tiedot ja asettaa ne.
     * @param rivi tiedostosta luettu rivi
     * @example
     * <pre name="test">
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * salaatinTomaatti.parse("1|1|");
     * salaatinTomaatti.getAteriaId() === 1;
     * salaatinTomaatti.getRaakaAineId() === 1;
     * </pre>
     */
    public void parse(String rivi) { 
        String[] tiedot = rivi.split("\\|");
        int i = 0;
        try {
           i = Integer.parseInt(tiedot[0]);  
        }  catch (NumberFormatException ex) {
            System.err.println("Ei voida lukea numeroa!");
        }
        if (i != 0) {
            this.aterianId = i;
            if (aterianId > seuraavaIndx1) seuraavaIndx1 = aterianId;
            seuraavaIndx1++;
        }
        try {
            i = Integer.parseInt(tiedot[1]);
        }  catch (NumberFormatException ex) {
            System.err.println("Ei voida lue numeroa!");
        }
        if (i != 0) this.raakaAineenId = i;
    }
    
    
    /**
     * Muutetaan olion tiedot merkkijonoksi
     * @example
     * <pre name="test">
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * salaatinTomaatti.rekisteroi(); salaatinTomaatti.setRaakaAineId(3);
     * salaatinTomaatti.toString() === "1|3|";
     * </pre>
     */
    @Override
    public String toString() {
        return aterianId + "|" + raakaAineenId + "|" ;
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
        out.println(this.aterianId + " " + this.raakaAineenId);
    }
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

}
