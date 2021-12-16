package menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author tiia1
 * @version 20 Mar 2021
 *
 */
public class Menu {
    private final Ateriat ateriat = new Ateriat();
    private final AterioidenRaakaAineet aterioidenraakaaineet = new AterioidenRaakaAineet();
    private final RaakaAineet raakaaineet = new RaakaAineet();
    private final RaakaAineidenTyypit raakaaineidentyypit= new RaakaAineidenTyypit();
    
    //Metodit, jotka hakevat tallennettujen olioden lukumaaran
    /**
     * Hakee atrioiden lukumaaran Ateriat luokasta. 
     * @return aterioiden lukumaara
     */
    public int getAteriat() {
        return ateriat.getLkm();
    }
    
    
    /**
     * Hakee aterioiden raaka-aineiden lukumaaran AterioidenRaakaAineet luokasta
     * @return aterioiden raaka-aineiden lukumäärä
     */
    public int getAterioidenRaakaAineet() {
        return aterioidenraakaaineet.getLkm();
    }
    
    
    /**
     * Hakee raaka-aineiden lukumaaran AterioidenRaakaAineet luokasta
     * @return raaka-aineiden lukumäärä
     */
    public int getRaakaAineet() {
        return raakaaineet.getLkm();
    }
    
    
    /**
     * Hakee raaka-aineiden tyyppien lukumaaran RaakaAineidenTyyppit luokasta
     * @return raaka-aineiden tyypien lukumäärä
     */
    public int getRaakaAineidenTyypit() {
        return raakaaineidentyypit.getLkm();
    }
    //-------------------------------------------------------
    
    //Lisaa metodit kaikille luokille------------------------

    /**
     * Lisaa aterian Ateriat luokat listaan.
     * @param ateria lisättävä ateria
     */
    public void lisaa(Ateria ateria) {
        ateriat.lisaa(ateria);
    }
    
    
    /**
     * Lisaa ateriat taulukkoon annetun aterian indeksin osoittamaan paikkaan
     * @param i indeksi
     * @param a ateria joka lisataan
     */
    public void lisaaPaikkaan(int i, Ateria a) {
        ateriat.lisaaPaikkaan(i, a);
    }
    
    
    /**
     * Lisaa aterialle raaka-aineen aterioidenRaakaAineet luokan listaan.
     * @param raakaAine lisättävä raakaine
     */
    public void lisaa(AterianRaakaAine raakaAine) {
        aterioidenraakaaineet.lisaa(raakaAine);
    }
    
    
    /**
     * Lisaa aterialle raaka-aineen aterioidenRaakaAineet luokan listaan.
     * @param t lisattava raaka-aineen tyyppi
     */
    public void lisaa(RaakaAineenTyyppi t) {
        raakaaineidentyypit.lisaa(t);
    }
    
    
    /**
     * Lisaa aterialle raaka-aineen aterioidenRaakaAineet luokan listaan.
     * @param raakaAine lisättävä raakaine
     */
    public void lisaa(RaakaAine raakaAine) {
        raakaaineet.lisaa(raakaAine);
    }
    //--------------------------------------------------
        
    //Hakumetodit kaikille luokille--------------------
    /**
     * Palauttaa i:n aterian
     * @param i monesko ateria palautetaan
     * @return viite i:teen ateriaan
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * Ateria salaatti = new Ateria();
     * salaatti.rekisteroi();
     * menu.lisaa(salaatti);
     * Ateria keitto = new Ateria();
     * keitto.rekisteroi();
     * menu.lisaa(keitto);
     * menu.annaAteria(2) === salaatti; #THROWS IndexOutOfBoundsException
     * Ateria keitto2 = new Ateria();
     * keitto2.rekisteroi();
     * menu.lisaa(keitto2);
     * menu.lisaa(keitto2);
     * menu.poista(keitto2);
     * menu.poista(keitto2);
     * menu.lisaaPaikkaan(3, keitto2);
     * menu.annaAteria(0) === salaatti;
     * menu.annaAteria(1) === keitto; 
     * menu.annaAteria(2) === null;
     * menu.annaAteria(3) === keitto2;
     * </pre>
     */
    public Ateria annaAteria(int i) {
        return ateriat.anna(i);
    }
    
    
    /**
     * Palauttaa i:n raaka-aineen
     * @param i monesko raaka-aine palautetaan
     * @return viite i:teen raaka-aineeseen
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * salaatinTomaatti.rekisteroi();
     * menu.lisaa(salaatinTomaatti);
     * AterianRaakaAine keitonSipuli = new AterianRaakaAine();
     * keitonSipuli.rekisteroi();
     * menu.lisaa(keitonSipuli);
     * menu.annaAterianRaakaAine(0) === salaatinTomaatti;
     * menu.annaAterianRaakaAine(1) === keitonSipuli;
     * menu.annaAterianRaakaAine(2) === keitonSipuli; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public AterianRaakaAine annaAterianRaakaAine(int i) {
        return aterioidenraakaaineet.anna(i);
    }
    
    
    /**
     * Palauttaa i:n raaka-aineen
     * @param i monesko raaka-aine palautetaan
     * @return viite i:teen raaka-aineeseen
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAine tomaatti = new RaakaAine();
     * tomaatti.rekisteroi();
     * menu.lisaa(tomaatti);
     * RaakaAine sipuli = new RaakaAine();
     * sipuli.rekisteroi();
     * menu.lisaa(sipuli);
     * menu.annaRaakaAine(0) === tomaatti;
     * menu.annaRaakaAine(1) === sipuli;
     * menu.annaRaakaAine(2) === sipuli; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public RaakaAine annaRaakaAine(int i) {
        return raakaaineet.anna(i);
    }
    
    
    /**
     * Palauttaa i:n raaka-aineen
     * @param i monesko raaka-aine palautetaan
     * @return viite i:teen raaka-aineeseen
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi();
     * kasvis.rekisteroi();
     * menu.lisaa(kasvis);
     * RaakaAineenTyyppi pasta = new RaakaAineenTyyppi();
     * pasta.rekisteroi();
     * menu.lisaa(pasta);
     * menu.annaRaakaAineenTyyppi(0) === kasvis;
     * menu.annaRaakaAineenTyyppi(1) === pasta;
     * menu.annaRaakaAineenTyyppi(2) === null; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public RaakaAineenTyyppi annaRaakaAineenTyyppi(int i) {
        return raakaaineidentyypit.anna(i);
    }
    
    //--------------------------------------------------
    //Hakumetodit kaikille tarvittaville luokille id:n perusteella--------------------
    /**
     * Palauttaa aterian annetun id:n perusteella
     * @param id aterian id
     * @return viite ateriaan id:lla
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * Ateria salaatti = new Ateria();
     * salaatti.rekisteroi();
     * menu.lisaa(salaatti);
     * Ateria keitto = new Ateria();
     * keitto.rekisteroi();
     * menu.lisaa(keitto);
     * menu.annaAteriaIdlla(2) === salaatti;
     * menu.annaAteriaIdlla(3) === keitto;
     * menu.annaAteriaIdlla(4) === keitto; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public Ateria annaAteriaIdlla(int id) {
        return ateriat.annaIdlla(id);
    }
    
    
    /**
     * Palauttaa raaka-aineen id:n perusteella
     * @param id raaka-aineen id
     * @return viite raaka-aineeseen id:lla
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAine tomaatti = new RaakaAine();
     * tomaatti.rekisteroi();
     * menu.lisaa(tomaatti);
     * RaakaAine sipuli = new RaakaAine();
     * sipuli.rekisteroi();
     * menu.lisaa(sipuli);
     * menu.annaRaakaAineIdlla(101) === tomaatti;
     * menu.annaRaakaAineIdlla(102) === sipuli;
     * menu.annaRaakaAineIdlla(3) === sipuli; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public RaakaAine annaRaakaAineIdlla(int id) {
        return raakaaineet.annaIdlla(id);
    }
    
    
    /**
     * Palauttaa raaka-aineen tyypin id:n perusteella
     * @param id raaka-aineen id
     * @return viite raaka-aineen tyyppiin id:lla
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi();
     * kasvis.rekisteroi();
     * menu.lisaa(kasvis);
     * RaakaAineenTyyppi pasta = new RaakaAineenTyyppi();
     * pasta.rekisteroi();
     * menu.lisaa(pasta);
     * menu.annaRaakaAineenTyyppiIdlla(9) === kasvis;
     * menu.annaRaakaAineenTyyppiIdlla(10) === pasta;
     * menu.annaRaakaAineenTyyppiIdlla(11) === null; #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public RaakaAineenTyyppi annaRaakaAineenTyyppiIdlla(int id) {
        return raakaaineidentyypit.annaIdlla(id);
    }
    
    //-----------------------------------------------------------------------
    
    //Hakumetodit olioiden indekseilla tietorakenteesta------------------------
    /**
     * Hakee aterian paikan aterioiden tietorakenteesta id:n avulla.
     * @param id aterian id
     * @return aterian indeksi ateriat taulukossa
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * Ateria salaatti = new Ateria();
     * salaatti.rekisteroi();
     * menu.lisaa(salaatti);
     * Ateria keitto = new Ateria();
     * keitto.rekisteroi();
     * menu.lisaa(keitto);
     * menu.annaAteriaIndex(salaatti.getAteriaId()) === 0;
     * menu.annaAteriaIndex(keitto.getAteriaId()) === 1;
     * </pre>
     */
    public int annaAteriaIndex(int id) {
        for (int i = 0; i < ateriat.getLkm(); i++) {
            Ateria a = ateriat.anna(i);
            if (a == null) continue;
            else if (a.getAteriaId() == id) return i;
        }
        return 0;
    }
    
    
    /**
     * Hakee aterian raaka-aineen paikan aterioiden raaka-aineiden tietorakenteesta id:n avulla.
     * @param id aterian raaka-aineen id
     * @return aterian raaka-aineen indeksi aterioiden raaka-aineet taulukossa
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * salaatinTomaatti.rekisteroi();
     * menu.lisaa(salaatinTomaatti);
     * AterianRaakaAine keitonSipuli = new AterianRaakaAine();
     * keitonSipuli.rekisteroi();
     * menu.lisaa(keitonSipuli);
     * menu.annaAterianRaakaAineIndex(salaatinTomaatti.getAteriaId()) === 0;
     * menu.annaAterianRaakaAineIndex(keitonSipuli.getAteriaId()) === 1;
     * </pre>
     */
    public int annaAterianRaakaAineIndex(int id) {
        for (int i = 0; i < aterioidenraakaaineet.getLkm(); i++) {
            AterianRaakaAine ar = aterioidenraakaaineet.anna(i);
            if (ar == null) continue;
            if (ar.getAteriaId() == id) return i;
        }
        return 0;
    }
    
    
    /**
     * Hakee raaka-aineen paikan raaka-aineiden tietorakenteesta id:n avulla.
     * @param id raaka-aineen id
     * @return raaka-aineen indeksi raaka-aineet taulukossa
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAine tomaatti = new RaakaAine();
     * tomaatti.rekisteroi();
     * menu.lisaa(tomaatti);
     * RaakaAine sipuli = new RaakaAine();
     * sipuli.rekisteroi();
     * menu.lisaa(sipuli);
     * menu.lisaa(sipuli);
     * menu.annaRaakaAineIndex(tomaatti.getRaakaAineenId()) === 0;
     * menu.annaRaakaAineIndex(sipuli.getRaakaAineenId()) === 1;
     * </pre>
     */
    public int annaRaakaAineIndex(int id) {
        for (int i = 0; i < raakaaineet.getLkm(); i++) {
            RaakaAine r = raakaaineet.anna(i);
            if (r == null) continue;
            if (raakaaineet.anna(i).getRaakaAineenId() == id) return i;
        }
        return 0;
    }
    
    
    /**
     * Hakee raaka-aineen tyypin paikan raaka-aineiden tyyppien tietorakenteesta id:n avulla.
     * @param id raaka-aineen tyypin id
     * @return raaka-aineen tyypin indeksi raaka-aineet taulukossa
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi();
     * kasvis.rekisteroi();
     * menu.lisaa(kasvis);
     * RaakaAineenTyyppi pasta = new RaakaAineenTyyppi();
     * pasta.rekisteroi();
     * menu.lisaa(pasta);
     * menu.annaRaakaAineidenTyypinIndex(kasvis.getTyyppiId()) === 0;
     * menu.annaRaakaAineidenTyypinIndex(pasta.getTyyppiId()) === 1;
     * </pre>
     */
    public int annaRaakaAineidenTyypinIndex(int id) {
        for (int i = 0; i < raakaaineidentyypit.getLkm(); i++) {
            RaakaAineenTyyppi rt = raakaaineidentyypit.anna(i);
            if (rt == null) continue;
            if (rt.getTyyppiId() == id) return i;
        }
        return 0;
    }
    //--------------------------------------------------
    
    //-rekisteroi kaikille---------------------------------------
    /**
     * Rekisteroidaan ateria
     * @param a rekisteroitava ateria
     */
    public void rekisteroi(Ateria a) {
        a.rekisteroi();
    }
    
    
    /**
     * Rekisteroidaan raaka-aine
     * @param r rekisteroitava raaka-aine
     */
    public void rekisteroi(RaakaAine r) {
        r.rekisteroi();
    }
    
    
    /**
     * Rekisteroidaan aterian raaka-aine
     * @param ar aterian raaka-aine
     */
    public void rekisteroi(AterianRaakaAine ar) {
        ar.rekisteroi();
    }
    //------------------------------------------
    
    //poista metodit kaikille-------------------
    
    /**
     * Poistaa aterian 
     * @param a poistettava ateria
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * Ateria laatikko = new Ateria();
     * Ateria salaatti = new Ateria();
     * Ateria keitto = new Ateria();
     * Ateria kiusaus = new Ateria();
     * menu.rekisteroi(laatikko);
     * menu.lisaa(laatikko);
     * menu.rekisteroi(salaatti);
     * menu.lisaa(salaatti);
     * menu.rekisteroi(keitto);
     * menu.lisaa(keitto);
     * menu.rekisteroi(kiusaus);
     * menu.poista(salaatti);
     * menu.annaAteriaIdlla(2) === null; #THROWS IndexOutOfBoundsException
     * menu.lisaa(kiusaus);
     * menu.poista(keitto);
     * menu.annaAteriaIndex(kiusaus.getAteriaId()) === 3;
     * </pre>
     */
    public void poista(Ateria a) {
        try {
            ateriat.poista(annaAteriaIndex(a.getAteriaId()));
        } catch (Exception e) {
            System.err.println("Ei löydy!");
        }
    }
    
    
    /**
     * Poistaa aterian raaka-aineen
     * @param ar poistettava aterian raaka-aine
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * AterianRaakaAine salaatinKurkku = new AterianRaakaAine();
     * AterianRaakaAine keitonPeruna = new AterianRaakaAine();
     * AterianRaakaAine kiusauksenPeruna = new AterianRaakaAine();
     * menu.rekisteroi(salaatinTomaatti);
     * menu.lisaa(salaatinTomaatti);
     * menu.rekisteroi(salaatinKurkku);
     * menu.lisaa(salaatinKurkku);
     * menu.rekisteroi(keitonPeruna);
     * menu.lisaa(keitonPeruna);
     * menu.poista(salaatinKurkku);
     * menu.annaAterianRaakaAine(1) === null; 
     * menu.rekisteroi(kiusauksenPeruna);
     * menu.lisaa(kiusauksenPeruna);
     * keitonPeruna.setRaakaAineId(2); kiusauksenPeruna.setRaakaAineId(2);
     * menu.poista(keitonPeruna);
     * menu.poista(keitonPeruna); 
     * menu.annaAterianRaakaAine(2) === null;
     * menu.annaAterianRaakaAine(3) === kiusauksenPeruna;
     * </pre>
     */
    public void poista(AterianRaakaAine ar) {
        try {
            aterioidenraakaaineet.poista(ar);
        } catch (Exception e) {
            System.err.println("Ei löydy!");
        }   
    }
    
    
    /**
     * Poistaa raaka-aineen
     * @param ra poistettava raaka-aine
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAine tomaatti = new RaakaAine();
     * RaakaAine kurkku = new RaakaAine();
     * RaakaAine peruna = new RaakaAine();
     * RaakaAine bataatti = new RaakaAine();
     * menu.rekisteroi(tomaatti);
     * menu.lisaa(tomaatti);
     * menu.rekisteroi(kurkku);
     * menu.lisaa(kurkku);
     * menu.rekisteroi(peruna);
     * menu.lisaa(peruna);
     * menu.poista(kurkku);
     * menu.annaRaakaAine(1) === null; 
     * menu.rekisteroi(bataatti);
     * menu.lisaa(bataatti);
     * peruna.setTyyppiId(2); bataatti.setTyyppiId(2);
     * menu.poista(peruna);
     * menu.annaRaakaAine(2) === null; 
     * menu.annaRaakaAine(3) === bataatti;
     * </pre>
     */
    public void poista(RaakaAine ra) {
        try {
            raakaaineet.poista(annaRaakaAineIndex(ra.getRaakaAineenId()));
        } catch (Exception e) {
            System.err.println("Ei löydy!");
        }
    }
    
    
    /**
     * Poistaa raaka-aineen tyypin (toistaiseksi ei tarvetta toiminnolle) 
     * @param rat poistettava raaka-aineen tyyppi
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi();
     * RaakaAineenTyyppi kala = new RaakaAineenTyyppi();
     * RaakaAineenTyyppi peruna = new RaakaAineenTyyppi();
     * RaakaAineenTyyppi riisi = new RaakaAineenTyyppi();
     * kasvis.rekisteroi();
     * menu.lisaa(kasvis);
     * kala.rekisteroi();
     * menu.lisaa(kala);
     * peruna.rekisteroi();
     * menu.lisaa(peruna);
     * menu.poista(kala);
     * menu.annaRaakaAineenTyyppi(1) === null; 
     * riisi.rekisteroi();
     * menu.lisaa(riisi);
     * menu.poista(peruna);
     * menu.annaRaakaAineenTyyppi(2) === null; 
     * menu.annaRaakaAineenTyyppi(3) === riisi;
     * </pre>
     */
    public void poista(RaakaAineenTyyppi rat) {
        try {
            raakaaineidentyypit.poista(annaRaakaAineidenTyypinIndex(rat.getTyyppiId()));
        } catch (Exception e) {
            System.err.println("Ei löydy!");
        }
    }
    
    //-------------------------------------------
   
    /**
     * Luetaan tiedostot.
     */
    public void lueTiedosto() {
        ateriat.lueTiedostosta(); 
        aterioidenraakaaineet.lueTiedostosta(); 
        raakaaineet.lueTiedostosta(); 
        raakaaineidentyypit.lueTiedostosta();
    }
    
    
    /**
     * Tallentaa tiedot.
     */
    public void tallenna() {
        ateriat.tallenna(); 
        aterioidenraakaaineet.tallenna(); 
        raakaaineet.tallenna(); 
        raakaaineidentyypit.tallenna();
    }
    
    
    /**
     * Hakee aterian id:n perusteella raaka-aineet
     * @param id aterian id jolla haetaan raaka-aineet
     * @return listan aterian id:ta vastaavat raaka-aineet
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * Ateria salaatti = new Ateria(); salaatti.rekisteroi(); menu.lisaa(salaatti);
     * AterianRaakaAine salaatinTomaatti = new AterianRaakaAine();
     * menu.lisaa(salaatinTomaatti); salaatinTomaatti.setAteriaId(1); salaatinTomaatti.setRaakaAineId(1);
     * AterianRaakaAine salaatinKurkku = new AterianRaakaAine();
     * menu.lisaa(salaatinKurkku); salaatinKurkku.setAteriaId(1); salaatinKurkku.setRaakaAineId(2);
     * AterianRaakaAine spagetinTomaatti = new AterianRaakaAine();
     * menu.lisaa(spagetinTomaatti); spagetinTomaatti.setAteriaId(2); spagetinTomaatti.setRaakaAineId(1);
     * AterioidenRaakaAineet ar;
     * ar = menu.haeAterioidenRaakaAineet(1);
     * ar.anna(0) === salaatinTomaatti;
     * ar.getLkm() === 2;
     * ar = menu.haeAterioidenRaakaAineet(2);
     * ar.anna(0) === spagetinTomaatti;
     * </pre> 
     */
    public AterioidenRaakaAineet haeAterioidenRaakaAineet(int id) {
        AterioidenRaakaAineet r = new AterioidenRaakaAineet();
        for (int i = 0; i < aterioidenraakaaineet.getLkm(); i++) {
            AterianRaakaAine aine = aterioidenraakaaineet.anna(i);
            if (aine == null) continue;
            if (aine.getAteriaId() == id) r.lisaa(aine);
        }
        return r;
    }
    
    
    /**
     * Asetetaan uudelle raaka-aineelle tyyppi id sen perusteella mitä kayttäja antoi tyypiksi. 
     * @param raakaaine uusi raaka-aine
     * @param tyyppi kayttajan antama tyyppi
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi(); 
     * kasvis.rekisteroi(); menu.lisaa(kasvis); kasvis.setNimi("kasvis");
     * RaakaAine sipuli = new RaakaAine(); sipuli.rekisteroi(); menu.lisaa(sipuli);
     * menu.setTyyppi(sipuli, "kasvis");
     * kasvis.getTyyppiId() === sipuli.getTyyppiId();
     * </pre>
     */
    public void setTyyppi(RaakaAine raakaaine, String tyyppi) {
        int id = 0;
        id = haeTyyppiNimella(tyyppi).getTyyppiId();
        raakaaine.setTyyppiId(id);
    }
    
    
    /**
     * Hakee raaka-aineen tyypin nimen perusteella.
     * @param tyyppi tyypin nimi
     * @return tyypin id:n
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAineenTyyppi kasvis = new RaakaAineenTyyppi(); 
     * kasvis.rekisteroi(); menu.lisaa(kasvis); kasvis.setNimi("kasvis");
     * menu.haeTyyppiNimella("kasvis") === kasvis;
     * </pre>
     */
    public RaakaAineenTyyppi haeTyyppiNimella(String tyyppi) {
        String tyyppi2 = tyyppi.toLowerCase();
        RaakaAineenTyyppi rt = raakaaineidentyypit.haeTyyppiNimella(tyyppi2);
        return rt;
    }
    
    
    /**
     * Hakee raaka-aineen sen nimen perusteella.
     * @param nimi raaka-aineen nimi
     * @return raaka-aine olio
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * RaakaAine tomaatti = new RaakaAine();
     * RaakaAine kurkku = new RaakaAine();
     * menu.rekisteroi(tomaatti);
     * menu.lisaa(tomaatti); tomaatti.setNimi("tomaatti");
     * menu.rekisteroi(kurkku);
     * menu.lisaa(kurkku); kurkku.setNimi("kurkku");
     * menu.haeRaakaAineNimella("tomaatti") === tomaatti;
     * menu.haeRaakaAineNimella("kurkku") === kurkku;
     * </pre>
     */
    public RaakaAine haeRaakaAineNimella(String nimi) {
        RaakaAine ra = raakaaineet.haeRaakaAineNimella(nimi);
        return ra;
    }
    
    
    /**
     * Palauttaa String tyypin listan raaka-aineiden nimista.
     * @return lista raaka-aineiden nimista
     * @example
     * <pre name="test">
     * #import java.util.ArrayList;
     * Menu menu = new Menu();
     * RaakaAine tomaatti = new RaakaAine();
     * RaakaAine kurkku = new RaakaAine();
     * RaakaAine peruna = new RaakaAine();
     * menu.rekisteroi(tomaatti);
     * menu.lisaa(tomaatti); tomaatti.setNimi("tomaatti");
     * menu.rekisteroi(kurkku);
     * menu.lisaa(kurkku); kurkku.setNimi("kurkku");
     * menu.rekisteroi(peruna);
     * menu.lisaa(peruna); peruna.setNimi("peruna");
     * ArrayList<String> aineet = (ArrayList<String>)menu.getListaRaakaAineista();
     * aineet.get(0) === "kurkku";
     * aineet.get(1) === "peruna";
     * aineet.get(2) === "tomaatti";
     * </pre>
     */
    public Collection<String> getListaRaakaAineista() {
        ArrayList<String> lista = new ArrayList<String>();
        for (RaakaAine r : raakaaineet) {
            lista.add(r.getNimi());
        }
        Collections.sort(lista);
        return lista;
    }
    
    
    /**
     * Etsii aika ehdon tayttavat ateriat ja palauttaa ne listassa. 
     * @param aika ehto
     * @return lista ehdon tayttavia aterioita
     * @example
     * <pre name="test">
     * Menu menu = new Menu();
     * Ateria salaatti = new Ateria(); salaatti.setAika("iltapala"); menu.lisaa(salaatti);
     * Ateria keitto = new Ateria(); keitto.setAika("päivällinen"); menu.lisaa(keitto);
     * Ateria makaroonilaatikko = new Ateria(); makaroonilaatikko.setAika("päivällinen"); menu.lisaa(makaroonilaatikko);
     * Ateria makkaraperunat = new Ateria(); makkaraperunat.setAika("iltapala"); menu.lisaa(makkaraperunat);
     * ArrayList<Ateria> ateriat = (ArrayList<Ateria>)menu.etsiAteriat("iltapala");
     * ateriat.get(0) === salaatti;
     * ateriat.get(1) === makkaraperunat; 
     * </pre>
     */
    public Collection<Ateria> etsiAteriat(String aika){
        List<Ateria> hakuAteriat = new ArrayList<Ateria>();
        int lkm = ateriat.getLkm();
        if (aika == null) {
            for (int i = 0; i < lkm; i++) hakuAteriat.add(annaAteria(i));
        }
        else {
            for (int i = 0; i < lkm; i++) {
                Ateria a = annaAteria(i);
                if (a.getAika().equals(aika))hakuAteriat.add(a);
            }
        }
        return hakuAteriat;
    }
    
    
    /**
     * Peruuttaa raaka-aineen lisäyksen aterialle.
     */
    public void peruutaRaakaAineenLisays() {
        aterioidenraakaaineet.peruuta();
    }
    

    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
