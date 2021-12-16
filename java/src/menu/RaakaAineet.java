package menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author tiia1
 * @version 22 Mar 2021
 *
 */
public class RaakaAineet implements Iterable<RaakaAine>{

    private int lkm = 0;
    private String tiedostonNimi = "RaakaAineet.dat";
    private RaakaAine[] alkiot = new RaakaAine[5];


    /**
     * Lisää uuden raaka-aineen tietorakenteeseen, tarvittaessa kasvattaa tietorakenteen kokoa *2
     * @param raakaAine lisattava raaka-aine
     * @example
     * <pre name="test">
     * RaakaAineet r = new RaakaAineet();
     * RaakaAine kurkku = new RaakaAine(), tomaatti = new RaakaAine();
     * r.getLkm() === 0;
     * r.lisaa(kurkku); r.getLkm() === 1;
     * r.lisaa(tomaatti); r.getLkm() === 2;
     * r.lisaa(kurkku); r.getLkm() === 3;
     * r.anna(0) === kurkku;
     * r.anna(1) === tomaatti;
     * r.anna(2) === kurkku;
     * r.anna(3) === kurkku; #THROWS IndexOutOfBoundsException 
     * r.lisaa(kurkku); r.getLkm() === 4;
     * </pre>
     */
    public void lisaa(RaakaAine raakaAine) {
        if (lkm >= alkiot.length) {
            RaakaAine[] uusi = new RaakaAine[this.lkm*2];       
            for(int i = 0; i < this.alkiot.length; i++) {
                uusi[i] = this.alkiot[i];
            }
            this.alkiot = uusi;
        }
        alkiot[lkm++] = raakaAine;
    }


    /**
     * Palauttaa viitteen i:n kohdalla olevaan olioon
     * @param i monennenko olion viite halutaan
     * @return viite raaka-aineeseen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public RaakaAine anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Palauttaa raaka-aineen id:n perusteella
     * @param id raaka-aineen id
     * @return id:ta vastaavan raaka-aineen
     */
    public RaakaAine annaIdlla(int id) {
        for (RaakaAine r : alkiot) {
            if (r == null) continue;
            if (r.getRaakaAineenId() == id) return r;
        }
        throw new IndexOutOfBoundsException("Annetulla id:lla ei löytynyt raaka-ainetta.");
    }
    
    
    /**
     * Poistaa raaka-aineen annetun indeksin kohdalta
     * @param i raaka-aineen indeksi tietorakenteessa
     */
    public void poista(int i) {
        alkiot[i] = null;
    }


    /**
     * Palauttaa tietorakenteessa olevien raaka-aineiden lukumaaran
     * @return lukumaara
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Tallennetaan olion tiedot
     * @example
     * <pre name="test">
     * #import java.io.*;
     * #import java.util.*;
     * RaakaAineet aineet = new RaakaAineet();
     * RaakaAine tomaatti = new RaakaAine(); tomaatti.setNimi("tomaatti"); tomaatti.setTyyppiId(3);
     * tomaatti.rekisteroi(); aineet.lisaa(tomaatti);
     * RaakaAine kurkku = new RaakaAine(); kurkku.setNimi("kurkku"); kurkku.setTyyppiId(3);
     * kurkku.rekisteroi(); aineet.lisaa(kurkku);
     * aineet.setTiedosto("raakaaineetTesti.dat");
     * aineet.tallenna();
     * try (Scanner fi = new Scanner(new FileInputStream(new File(aineet.getTiedostonNimi())))) {
     *      String rivi = fi.nextLine();
     *      String rivi2 = fi.nextLine();
     *      rivi === "102|tomaatti|3|";
     *      rivi2 === "103|kurkku|3|";
     *  } catch (FileNotFoundException e) {
     *      System.err.println("Tiedostoa ei löydy");
     *  }
     * </pre>
     */
    public void tallenna() {
        String t = getTiedostonNimi();
        try (PrintStream o = new PrintStream(new FileOutputStream(t, false))){
            for (RaakaAine a : alkiot) {
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
     * RaakaAineet aineet = new RaakaAineet();
     * RaakaAine tomaatti = new RaakaAine(); tomaatti.setNimi("tomaatti"); tomaatti.setTyyppiId(3);
     * tomaatti.rekisteroi(); aineet.lisaa(tomaatti);
     * RaakaAine kurkku = new RaakaAine(); kurkku.setNimi("kurkku"); kurkku.setTyyppiId(3);
     * kurkku.rekisteroi(); aineet.lisaa(kurkku);
     * aineet.setTiedosto("raakaaineetTesti.dat");
     * aineet.tallenna();
     * aineet.lueTiedostosta();
     * aineet.anna(0) === tomaatti;
     * aineet.anna(1) === kurkku;
     * </pre>
     */
    public void lueTiedostosta(){
        try (Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi())))) {
            while ( fi.hasNext() ) {
                RaakaAine a = new RaakaAine();
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
     * Hakee raaka-aineen nimen perusteella
     * @param nimi raaka-aineen nimi
     * @return raaka-aineolio
     */
    public RaakaAine haeRaakaAineNimella(String nimi) {
        for (RaakaAine r : alkiot) {
            if (r == null) continue;
            if (r.getNimi().equals(nimi)) return r;
        }
        return null;
    }
    
    
    /**
     * Asettaa tiedoston nimeksi annetun parametrin. Testaamiseen.
     * @param nimi haluttu tiedoston nimi
     */
    public void setTiedosto(String nimi) {
        this.tiedostonNimi = nimi;
    }


    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }


    @Override
    public Iterator<RaakaAine> iterator() {
        return new RaakaAineIterator();
    }
    
    class RaakaAineIterator implements Iterator<RaakaAine>{
        private int nykyinen;
        
        @Override
        public boolean hasNext() {
            return nykyinen < getLkm();
        }

        @Override
        public RaakaAine next() {   
            return anna(nykyinen++);
        }       
    }
}


