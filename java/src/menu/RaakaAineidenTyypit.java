package menu;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author tiia1
 * @version 20 Mar 2021
 *
 */
public class RaakaAineidenTyypit implements Iterable<RaakaAineenTyyppi>{
    
    private String tiedostonNimi = "RaakaAineidenTyypit.dat";
    private final ArrayList<RaakaAineenTyyppi> tyypit = new ArrayList<RaakaAineenTyyppi>();

    
    /**
     * Haetaan harrastusten maara tietorakenteessa.
     * @return maaran
     */
    public int getLkm() {
        return tyypit.size();
    }
    
    
    /**
     * Lisataan raaka-aineen tyyppi listaan.
     * @param t raaka-aineen tyyppi
     * @example
     * <pre name="test">
     * RaakaAineidenTyypit r = new RaakaAineidenTyypit();
     * RaakaAineenTyyppi vege = new RaakaAineenTyyppi(), liha = new RaakaAineenTyyppi();
     * r.getLkm() === 0;
     * r.lisaa(vege); r.getLkm() === 1;
     * r.lisaa(liha); r.getLkm() === 2;
     * r.lisaa(vege); r.getLkm() === 3;
     * r.anna(0) === vege;
     * r.anna(1) === liha;
     * r.anna(2) === vege;
     * r.anna(3) === vege; #THROWS IndexOutOfBoundsException 
     * r.lisaa(liha); r.getLkm() === 4;
     * </pre>
     */
    public void lisaa(RaakaAineenTyyppi t) {
        tyypit.add(t);
    }
    
    
    /**
     * Palauttaa viitteen i:n kohdalla olevaan olioon
     * @param i monennenko olion viite halutaan
     * @return viite raaka-aineen tyyppiin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public RaakaAineenTyyppi anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || tyypit.size() <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return tyypit.get(i);
    }
    
    
    /**
     * Antaa raaka-aineen tyypin id:n perusteella
     * @param id raaka-aineen tyypin id
     * @return id:ta vastaavan raaka-aineen tyypin 
     */
    public RaakaAineenTyyppi annaIdlla(int id) {
        for (RaakaAineenTyyppi rt : tyypit) {
            if (rt == null) continue;
            if (rt.getTyyppiId() == id) return rt;
        }
        throw new IndexOutOfBoundsException("Annetulla id:lla ei löytynyt raaka-aineen tyyppiä.");
    }
      
    
    /**
     * Poistaa raaka-aineen tyypin annetun indeksin kohdalta
     * @param i raaka-aineen tyypin indeksi tietorakenteessa
     */
    public void poista(int i) {
        tyypit.set(i, null);
    }
    
    
    /**
     * Tallennetaan olion tiedot
     * @example
     * <pre name="test">
     * #import java.io.*;
     * #import java.util.*;
     * RaakaAineidenTyypit tyypit = new RaakaAineidenTyypit();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi(); kasvis.setNimi("kasvis"); kasvis.setOsa("lisuke");
     * kasvis.rekisteroi(); tyypit.lisaa(kasvis);
     * RaakaAineenTyyppi peruna = new RaakaAineenTyyppi(); peruna.setNimi("peruna"); peruna.setOsa("lisuke");
     * peruna.rekisteroi(); tyypit.lisaa(peruna);
     * tyypit.setTiedosto("tyypitTesti.dat");
     * tyypit.tallenna();
     * try (Scanner fi = new Scanner(new FileInputStream(new File(tyypit.getTiedostonNimi())))) {
     *      String rivi = fi.nextLine();
     *      String rivi2 = fi.nextLine();
     *      rivi === "3|kasvis|lisuke|";
     *      rivi2 === "4|peruna|lisuke|";
     *  } catch (FileNotFoundException e) {
     *      System.err.println("Tiedostoa ei löydy");
     *  } 
     * </pre>
     */
    public void tallenna() {
        String t = getTiedostonNimi();
        try (PrintStream o = new PrintStream(new FileOutputStream(t, false))){
            for (RaakaAineenTyyppi a : tyypit) {
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
     * RaakaAineidenTyypit tyypit = new RaakaAineidenTyypit();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi(); kasvis.setNimi("kasvis"); kasvis.setOsa("lisuke");
     * kasvis.rekisteroi(); tyypit.lisaa(kasvis);
     * RaakaAineenTyyppi peruna = new RaakaAineenTyyppi(); peruna.setNimi("peruna"); peruna.setOsa("lisuke");
     * peruna.rekisteroi(); tyypit.lisaa(peruna);
     * tyypit.setTiedosto("tyypitTesti.dat");
     * tyypit.tallenna();
     * tyypit.lueTiedostosta();
     * tyypit.anna(0) === kasvis;
     * tyypit.anna(1) === peruna;
     * </pre>
     */
    public void lueTiedostosta(){
        try (Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi())))) {
            while ( fi.hasNext() ) {
                RaakaAineenTyyppi a = new RaakaAineenTyyppi();
                String rivi = fi.nextLine();
                a.parse(rivi);
                lisaa(a);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Tiedostoa ei löydy");
        }
    }
    
    
    /**
     * Hakee raaka-ainetyypin id:n nimen perusteella.
     * @param tyyppi tyypin nimi
     * @return tyypin id:n
     */
    public RaakaAineenTyyppi haeTyyppiNimella(String tyyppi) {
        for (RaakaAineenTyyppi r : tyypit) {
            if (r.getNimi().equals(tyyppi)) return r;
        }
        return null;
    }
    
    
    /**
     * Noudetaan olion hallinnoiman tiedoston nimi
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return this.tiedostonNimi;
    }
    
    
    /**
     * Asetetaan tiedoston nimeksi annettu parametri
     * @param nimi haluttu tiedoston nimi
     */
    public void setTiedosto(String nimi) {
        this.tiedostonNimi = nimi;
    }
    
    
    @Override
    public Iterator<RaakaAineenTyyppi> iterator() {
        return tyypit.iterator();
    }
    
    
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        // 

    }
}
