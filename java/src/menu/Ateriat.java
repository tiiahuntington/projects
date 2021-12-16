package menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * @author tiia1
 * @version 23 Feb 2021
 *
 */
public class Ateriat {

    private static final int MAX_OLIOT   = 5;
    private int lkm = 0;
    private String tiedostonNimi = "Ateriat.dat";
    private Ateria[] alkiot = new Ateria[MAX_OLIOT];


    /**
     * Lisää uuden aterian tietorakenteeseen
     * @param ateria lisättävän aterian viite
     * @example
     * <pre name="test"> 
     * Ateriat ateriat = new Ateriat();
     * Ateria hernekeitto = new Ateria(), sosekeitto = new Ateria();
     * ateriat.getLkm() === 0;
     * ateriat.lisaa(hernekeitto); ateriat.getLkm() === 1;
     * ateriat.lisaa(sosekeitto); ateriat.getLkm() === 2;
     * ateriat.lisaa(hernekeitto); ateriat.getLkm() === 3;
     * ateriat.anna(0) === hernekeitto;
     * ateriat.anna(1) === sosekeitto;
     * ateriat.anna(2) === hernekeitto;
     * ateriat.anna(1) == hernekeitto === false;
     * ateriat.anna(1) == sosekeitto === true;
     * ateriat.anna(3) === hernekeitto; #THROWS IndexOutOfBoundsException 
     * ateriat.lisaa(hernekeitto); ateriat.getLkm() === 4;
     * ateriat.lisaa(hernekeitto); ateriat.getLkm() === 5;
     * ateriat.lisaa(hernekeitto); ateriat.getLkm() === 6;
     * </pre>
     */
    public void lisaa(Ateria ateria) {
        if (lkm >= alkiot.length) {
            Ateria[] uusi = new Ateria[this.lkm*2];       
            for(int i = 0; i < this.alkiot.length; i++) {
                uusi[i] = this.alkiot[i];
            }
            this.alkiot = uusi;
        }
        alkiot[lkm++] = ateria;
    }
    
    
    /**
     * Lisaa indeksin osoittamaan paikkaan annetun aterian. 
     * @param i indeksi
     * @param a ateria joka halutaan lisata
     * @example
     * <pre name="test">
     * Ateriat ateriat = new Ateriat();
     * Ateria hernekeitto = new Ateria(), sosekeitto = new Ateria();
     * ateriat.getLkm() === 0;
     * ateriat.lisaaPaikkaan(0, hernekeitto); ateriat.getLkm() === 1;
     * ateriat.lisaaPaikkaan(0, sosekeitto); ateriat.getLkm() === 1;
     * </pre>
     */
    public void lisaaPaikkaan(int i, Ateria a) {
        if (alkiot[i] == null) lkm++;
        this.alkiot[i] = a;
    }


    /**
     * Palauttaa viitteen i:n kohdalla olevaan olioon
     * @param i monennenko olion viite halutaan
     * @return viite ateriaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella 
     * @example
     * <pre name="test">
     * Ateriat ateriat = new Ateriat();
     * Ateria hernekeitto = new Ateria(), sosekeitto = new Ateria();
     * ateriat.lisaa(hernekeitto);
     * ateriat.lisaa(sosekeitto); 
     * ateriat.lisaa(hernekeitto);
     * ateriat.anna(0) === hernekeitto;
     * ateriat.anna(3) === null; #THROWS IndexOutOfBoundsException
     * </pre> 
     */
    public Ateria anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Antaa aterian annetun id:n perusteella
     * @param id aterian id
     * @return id:ta vatsaavan aterian
     * @example
     * <pre name="test">
     * Ateriat ateriat = new Ateriat();
     * Ateria hernekeitto = new Ateria(), sosekeitto = new Ateria();
     * hernekeitto.rekisteroi();
     * ateriat.lisaa(hernekeitto);
     * ateriat.lisaa(sosekeitto);
     * ateriat.annaIdlla(5) === hernekeitto;
     * ateriat.annaIdlla(6) === hernekeitto; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public Ateria annaIdlla(int id) {
        for (Ateria a : alkiot) {
            if (a == null) continue;
            if (a.getAteriaId() == id) return a;
        }
        throw new IndexOutOfBoundsException("Annetulla id:lla ei löytynyt ateriaa.");
    }
    
    
    /**
     * Poistaa aterian annetun indeksin kohdalta
     * @param i aterian indeksi tietorakenteessa
     */
    public void poista(int i) {
        alkiot[i] = null;
    }
    
    
    /**
     * Tallennetaan olion tiedot
     * @example
     * <pre name="test">
     * #import java.io.*;
     * #import java.util.*;
     * Ateriat ateriat = new Ateriat();
     * Ateria keitto = new Ateria(); keitto.setNimi("keitto"); keitto.setAika("päivällinen");
     * keitto.rekisteroi(); ateriat.lisaa(keitto);
     * Ateria salaatti = new Ateria(); salaatti.setNimi("salaatti"); salaatti.setAika("iltapala");
     * salaatti.rekisteroi(); ateriat.lisaa(salaatti);
     * ateriat.setTiedosto("ateriatTesti.dat");
     * ateriat.tallenna();
     * try (Scanner fi = new Scanner(new FileInputStream(new File(ateriat.getTiedostonNimi())))) {
     *      String rivi = fi.nextLine();
     *      String rivi2 = fi.nextLine();
     *      rivi === "3|Keitto|päivällinen|";
     *      rivi2 === "4|Salaatti|iltapala|";
     *  } catch (FileNotFoundException e) {
     *      System.err.println("Tiedostoa ei löydy");
     *  }
     * </pre>
     */
    public void tallenna() {
        String t = getTiedostonNimi();
        try (PrintStream o = new PrintStream(new FileOutputStream(t, false))){
            for (Ateria a : alkiot) {
                if (a == null) continue;
                o.println(a);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Tiedostoa ei löydy");
            return;
        }
    }
    
    
    /**
     * Luetaan ateriat tiedotosta
     * @example
     * <pre name="test">
     * #import java.io.*;
     * #import java.util.*;
     * Ateriat ateriat = new Ateriat();
     * Ateria keitto = new Ateria(); keitto.setNimi("keitto"); keitto.setAika("päivällinen");
     * keitto.rekisteroi(); ateriat.lisaa(keitto);
     * Ateria salaatti = new Ateria(); salaatti.setNimi("salaatti"); salaatti.setAika("iltapala");
     * salaatti.rekisteroi(); ateriat.lisaa(salaatti);
     * ateriat.setTiedosto("ateriatTesti.dat");
     * ateriat.tallenna();
     * ateriat.lueTiedostosta();
     * ateriat.anna(0) === keitto;
     * ateriat.anna(1) === salaatti;
     * </pre>
     */
    public void lueTiedostosta(){
        try (Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi())))) {
            while ( fi.hasNext() ) {
                Ateria a = new Ateria();
                String rivi = fi.nextLine();
                a.parse(rivi);
                lisaa(a);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Tiedostoa ei löydy");
        }
    }
    
    
    
    /**
     * Noudetaan olion hallinnoiman tiedoston nimi
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return this.tiedostonNimi;
    }


    /**
     * Palauttaa aterioiden maaran
     * @return aterioiden lukumaara
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Asetetaan annettu parametrin tiedoston nimeksi. Testaamiseen. 
     * @param nimi haluttu tiedoston nimi
     */
    public void setTiedosto(String nimi) {
        this.tiedostonNimi = nimi;
    }


    /**
     * Testataan ohjeman toimintaa.
     * @param args ei kaytossa
     */
    public static void main(String args[]) {
        Ateriat at = new Ateriat();
        Ateria a = new Ateria(); 
        Ateria b = new Ateria();
        a.rekisteroi();
        b.rekisteroi();
        at.lisaa(a);
        at.lisaa(b);
        for (int i = 0; i < at.getLkm(); i++) {
            Ateria ateria = at.anna(i);
            System.out.println("Ateria nro: " + i);
            ateria.tulosta(System.out);
        }

    }
}
