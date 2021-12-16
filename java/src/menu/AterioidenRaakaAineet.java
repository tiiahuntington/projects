package menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * @author tiia1
 * @version 22 Mar 2021
 *
 */
public class AterioidenRaakaAineet {

    private static final int MAX_OLIOT   = 5;
    private int lkm = 0;
    private String tiedostonNimi = "AterioidenRaakaAineet.dat";
    private AterianRaakaAine[] alkiot = new AterianRaakaAine[MAX_OLIOT];


    /**
     * Lisaa uuden aterian raaka-aineen tietorakenteeseen
     * @param aine lisättävän raaka-aineen viite
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * AterioidenRaakaAineet aterioidenRaakaAineet = new AterioidenRaakaAineet();
     * AterianRaakaAine makarooni = new AterianRaakaAine(), maito = new AterianRaakaAine();
     * aterioidenRaakaAineet.getLkm() === 0;
     * aterioidenRaakaAineet.lisaa(makarooni); aterioidenRaakaAineet.getLkm() === 1;
     * aterioidenRaakaAineet.lisaa(maito); aterioidenRaakaAineet.getLkm() === 2;
     * aterioidenRaakaAineet.lisaa(makarooni); aterioidenRaakaAineet.getLkm() === 3;
     * aterioidenRaakaAineet.anna(0) === makarooni;
     * aterioidenRaakaAineet.anna(1) === maito;
     * aterioidenRaakaAineet.anna(2) === makarooni;
     * aterioidenRaakaAineet.anna(1) == makarooni === false;
     * aterioidenRaakaAineet.anna(1) == maito === true;
     * aterioidenRaakaAineet.anna(3) === makarooni; #THROWS IndexOutOfBoundsException 
     * aterioidenRaakaAineet.lisaa(makarooni); aterioidenRaakaAineet.getLkm() === 4;
     * aterioidenRaakaAineet.lisaa(makarooni); aterioidenRaakaAineet.getLkm() === 5;
     * aterioidenRaakaAineet.lisaa(makarooni); aterioidenRaakaAineet.getLkm() === 6;
     * </pre>
     */
    public void lisaa(AterianRaakaAine aine) {
        if (lkm >= alkiot.length) {
            AterianRaakaAine[] uusi = new AterianRaakaAine[this.lkm*2];       
            for(int i = 0; i < this.alkiot.length; i++) {
                uusi[i] = this.alkiot[i];
            }
            this.alkiot = uusi;
        }
        alkiot[lkm] = aine;
        lkm++;
    }


    /**
     * Palauttaa viitteen i:n kohdalla olevaan olioon
     * @param i monennenko olion viite halutaan
     * @return viite raaka-aineeseen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public AterianRaakaAine anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Poistaa aterian raaka-aineen annetun indeksin kohdalta
     * @param ar aterian raaka-aine joka poistetaan
     */
    public void poista(AterianRaakaAine ar) {
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot[i] == ar) alkiot[i] = null; 
        }
    }
    
    
    /**
     * Peruuttaa aterian raaka-aineen lisäyksen vähentämällä lukumäärää. 
     * Itse poisto tapahtuu poista() aliohjelmalla.
     * @example
     * <pre name="test"
     * AterioidenRaakaAineet ar = new AterioidenRaakaAineet();
     * AterianRaakaAine aine = new AterianRaakaAine();
     * ar.lisaa(aine); ar.lisaa(aine); ar.lisaa(aine);
     * ar.getLkm() === 3;
     * ar.peruuta(); ar.peruuta();
     * ar.getLkm() === 1;
     * </pre>
     */
    public void peruuta() {
        lkm--;
    }
    
    
    /**
     * Tallennetaan olion tiedot
     * @example
     * <pre name="test">
     * #import java.io.*;
     * #import java.util.*;
     * AterioidenRaakaAineet atrt = new AterioidenRaakaAineet();
     * AterianRaakaAine keitonPeruna = new AterianRaakaAine(); keitonPeruna.setAteriaId(1); keitonPeruna.setRaakaAineId(5);
     * atrt.lisaa(keitonPeruna);
     * AterianRaakaAine muussinPeruna = new AterianRaakaAine(); muussinPeruna.setAteriaId(2); muussinPeruna.setRaakaAineId(5);
     * atrt.lisaa(muussinPeruna);
     * atrt.setTiedosto("aterioidenraakaaineetTesti.dat");
     * atrt.tallenna();
     * try (Scanner fi = new Scanner(new FileInputStream(new File(atrt.getTiedostonNimi())))) {
     *      String rivi = fi.nextLine();
     *      String rivi2 = fi.nextLine();
     *      rivi === "1|5|";
     *      rivi2 === "2|5|";
     *  } catch (FileNotFoundException e) {
     *      System.err.println("Tiedostoa ei löydy");
     *  } 
     * </pre>
     */
    public void tallenna() {
        String t = getTiedostonNimi();
        try (PrintStream o = new PrintStream(new FileOutputStream(t, false))){
            for (AterianRaakaAine a : alkiot) {
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
     * AterioidenRaakaAineet atrt = new AterioidenRaakaAineet();
     * AterianRaakaAine keitonPeruna = new AterianRaakaAine(); keitonPeruna.setAteriaId(1); keitonPeruna.setRaakaAineId(5);
     * atrt.lisaa(keitonPeruna);
     * AterianRaakaAine muussinPeruna = new AterianRaakaAine(); muussinPeruna.setAteriaId(2); muussinPeruna.setRaakaAineId(5);
     * atrt.lisaa(muussinPeruna);
     * atrt.setTiedosto("aterioidenraakaaineetTesti.dat");
     * atrt.tallenna();
     * atrt.lueTiedostosta();
     * atrt.anna(0) === keitonPeruna;
     * atrt.anna(1) === muussinPeruna;
     * </pre>
     */
    public void lueTiedostosta(){
        try (Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi())))) {
            while ( fi.hasNext() ) {
                AterianRaakaAine a = new AterianRaakaAine();
                String rivi = fi.nextLine();
                a.parse(rivi);
                lisaa(a);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Tiedostoa ei löydy");
        }
    }
    
    
    /**
     * Antaa olion tiedoston nimen.
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonNimi;
    }


    /**
     * Palauttaa raaka-aineiden määrän
     * @return raaka-aineiden
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Asetetaan tiedoston nimeksi saatu parametri
     * @param nimi haluttu tiedoston nimi
     */
    public void setTiedosto(String nimi) {
        this.tiedostonNimi = nimi;
    }


    /**
     * Testaillaan toimintoja.
     * @param args ei käytössä
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
            System.out.println("Raaka-aine nro: " + i);
            ateria.tulosta(System.out);
        }

    }

}
