package fxmenu;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import menu.Ateria;
import menu.AterianRaakaAine;
import menu.AterioidenRaakaAineet;
import menu.Menu;

/**
 * @author tiia1
 * @version 16 Feb 2021
 *
 */
public class PaaIkkunaGUIController implements Initializable {

    @FXML private Button nappiArvo;
    @FXML private Button nappiListaa;
    @FXML private ComboBox<String> ateriaAika;
    @FXML private ComboBox<String> paa;
    @FXML private ComboBox<String> lisu;
    @FXML private ComboBox<String> ateriaAika2;
    @FXML private ComboBox<String> paa2;
    @FXML private ComboBox<String> lisu2;
    @FXML private Tab listaus;
    @FXML private TabPane TabPane;
    @FXML private Tab ArpojaTab;
    @FXML private ListChooser<Ateria> ateriaLista;
    @FXML private Label ateriaOtsikko;
    @FXML private TextArea aineAlue;
    

    @FXML
    void avaaArvo() {
        arvo();
    }

    @FXML
    void avaaListaus() {
        TabPane.getSelectionModel().select(listaus);
        hae(true, false);
    }

    @FXML
    void avaaMuokkausNakyma() {
        avaaMuokkaus(true);
    }
    
    @FXML
    void avaaLisaaAteria() {
        avaaMuokkaus(false);
    }

    @FXML
    void avaaTietoja() {
        Dialogs.showMessageDialog("Vielä ei tiedetä paljoa.");
    }

    @FXML
    void haku() {
        hae(false, false);
    }

    @FXML
    void poista() {
        poistaAteria();
    }

    @FXML
    void poistu() {
        tallenna();
        ModalController.closeStage(ateriaOtsikko);
    }

    @FXML
    void tallenna() {
        if (Dialogs.showQuestionDialog("Tallennus", "Haluatko varmasti tallentaa?", "Kyllä", "Ei")) menu.tallenna();
    }
    
    @FXML
    void nollaaHaku() {
        ateriaAika.getSelectionModel().clearSelection();
        ateriaAika2.getSelectionModel().clearSelection();
        paa.getSelectionModel().clearSelection();
        paa2.getSelectionModel().clearSelection();
        lisu.getSelectionModel().clearSelection();
        lisu2.getSelectionModel().clearSelection();
    }
    
    //----------------------------------------------------------
    
    private static Menu menu;
    private Ateria valittuAteria;
    private static boolean uudestaan = false;
    
    /**
     * Asettaa parametrilla annetun menun ilmentyman. 
     * @param m menu olio
     */
    public void setMenu(Menu m) {
        menu = m;
        menu.lueTiedosto();
    }
    
    
    /**
     * Antaa taman luokan ilmentyman menusta.
     * @return tama menu
     */
    public static Menu getMenu() {
        return menu;
    }
    
    
    /**
     * Asettaa parametrin luokan uudestaan muuttujaksi.
     * @param b arvotaanko uudestaan vai ei.
     */
    public static void setUudestaan(boolean b) {
        uudestaan = b;
    }
    
    
    /**
     * Avaa muokkausnäkymän.
     * @param onkoAteriaa ollaanko muokkaamassa ateriaa vai luomassa uutta
     */
    public void avaaMuokkaus(boolean onkoAteriaa) {
        Ateria palautettu = new Ateria();
        if (onkoAteriaa) palautettu = ModalController.showModal(PaaIkkunaGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaus", null, valittuAteria);
        else palautettu = ModalController.showModal(PaaIkkunaGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaus", null, null);
        int id = 0;
        if (palautettu.getAika().length() > 0) {
            id = palautettu.getAteriaId();
            menu.lisaaPaikkaan(menu.annaAteriaIndex(palautettu.getAteriaId()), palautettu);
            
        }
        listaaAteriat(id);
    }
    
    
    /**
     * Hakee aterian hakuehtojen ja halutun toiminnon mukaan.
     * @param kutsuEkastaTabista onko kutsu ensimmaisesta vai toisesta tabista
     * @param arvotaanko arvotaanko ateriaa vai listataan
     * @return satunnaisesti valitun aterian
     */
    public Ateria hae(boolean kutsuEkastaTabista, boolean arvotaanko) {
        String aika;
        String paaAine;
        String lisuke;
        if (kutsuEkastaTabista) {
            aika = ateriaAika.getSelectionModel().getSelectedItem();
            paaAine = paa.getSelectionModel().getSelectedItem();
            lisuke = lisu.getSelectionModel().getSelectedItem();
            ateriaAika2.getSelectionModel().select(aika);
            paa2.getSelectionModel().select(paaAine);
            lisu2.getSelectionModel().select(lisuke);
        }
        else {
            aika = ateriaAika2.getSelectionModel().getSelectedItem();
            paaAine = paa2.getSelectionModel().getSelectedItem();
            lisuke = lisu2.getSelectionModel().getSelectedItem();
        }
        
        if (aika == null && paaAine == null && lisuke == null) {
            listaaAteriat(1);
            return null; 
        }
        Collection<Ateria> ateriat;
        List<Ateria> apulista = new ArrayList<Ateria>();
        List<Ateria> apulista2 = new ArrayList<Ateria>();
        int rtId = 0;
        int verrokki;
        ateriat = menu.etsiAteriat(aika);
        if (paa != null) {
            rtId = menu.haeTyyppiNimella(paaAine).getTyyppiId();
            for (Ateria a : ateriat) {
                AterioidenRaakaAineet art = menu.haeAterioidenRaakaAineet(a.getAteriaId());
                for (int i = 0; i < art.getLkm(); i++) {
                    verrokki = menu.annaRaakaAineIdlla(art.anna(i).getRaakaAineId()).getTyyppiId();
                    if (verrokki == rtId) apulista.add(a);
                }
            }
            ateriat = apulista;
        }
        if (lisuke != null) {
            rtId = menu.haeTyyppiNimella(lisuke).getTyyppiId();
            for (Ateria a : ateriat) {
                AterioidenRaakaAineet art = menu.haeAterioidenRaakaAineet(a.getAteriaId());
                for (int i = 0; i < art.getLkm(); i++) {
                    verrokki = menu.annaRaakaAineIdlla(art.anna(i).getRaakaAineId()).getTyyppiId();
                    if (verrokki == rtId) {
                        apulista2.add(a);
                        break;
                    }
                }
            }
            ateriat = apulista2;
        }
        ateriaLista.clear();
        if (ateriat.size() == 0) {
            Dialogs.showMessageDialog("Annetuilla hakuehdoilla ei löydy tuloksia");
            if (arvotaanko) return null;
            aika = null;
            ateriat = menu.etsiAteriat(aika);
        }
        if (kutsuEkastaTabista && arvotaanko) {
            Random r = new Random();
            int arvottuLuku;
            while (true) {
                arvottuLuku = r.nextInt(ateriat.size())+1;
                if (arvottuLuku != 0) break;
            }
            int i = 0;
            for (Ateria a : ateriat) {
                i++;
                if (i == arvottuLuku) return a;
            }
        }
        for (Ateria a : ateriat) {
            ateriaLista.add(a.getNimi(), a);
        }
        ateriaLista.setSelectedIndex(0);
        return valittuAteria;
    }
    
    
    /**
     * Arpoo hakuehtojen mukaisen aterian .
     */
    public void arvo() {
        uudestaan = true;
        while (uudestaan) {
            Ateria a = hae(true, true);
            if (a == null) {
                Random r = new Random();
                int i = r.nextInt(menu.getAteriat());
                a = menu.annaAteria(i);
            }
            ModalController.showModal(PaaIkkunaGUIController.class.getResource("ArvoGUIView.fxml"), "Muokkaus", null, a);
        }
    }
    
    
    /**
     * Listaa ateriat näkymään. 
     * @param id Aterian id jonka halutaan näkyvän 
     */
    public void listaaAteriat(int id) {
        if (ateriaLista != null) ateriaLista.clear();

        int index = 0;
        for (int i = 0; i < menu.getAteriat(); i++) {
            Ateria ateria = menu.annaAteria(i);
            if (ateria == null) continue;
            if (ateria.getAteriaId() == id) index = i;
            ateriaLista.add(ateria.getNimi(), ateria);
        }
        ateriaLista.setSelectedIndex(index);

    }
    
    
    /**
     * Näytetään valitun aterian raaka-aineet.
     */
    public void naytaRaakaAineet() {
        valittuAteria = ateriaLista.getSelectedObject();
        if (valittuAteria == null) return;
        ateriaOtsikko.setText(valittuAteria.getNimi());
        int id = valittuAteria.getAteriaId();
        AterioidenRaakaAineet r = new AterioidenRaakaAineet();
        r = menu.haeAterioidenRaakaAineet(id);
        StringBuilder aineetJonossa = new StringBuilder();
        for (int i = 0; i < r.getLkm(); i++) {
            String nimi = menu.annaRaakaAineIdlla(r.anna(i).getRaakaAineId()).getNimi();
            aineetJonossa.append(nimi + "\n");
        }
        aineAlue.setText(aineetJonossa.toString());
    }
    
    
    /**
     * Poistaa valitun aterian.
     */
    public void poistaAteria() {
        int id = 0;
        if (valittuAteria == null ) return;
        if (Dialogs.showQuestionDialog("Tallennus", "Haluatko varmasti poistaa?", "Kyllä", "Ei")) {
            id = valittuAteria.getAteriaId();
            ateriaLista.clear();
            menu.poista(valittuAteria);
        }
        for (int i = 0; i < menu.getAterioidenRaakaAineet(); i++) {
            AterianRaakaAine ar = menu.annaAterianRaakaAine(i);
            if (ar == null) continue;
            if (id == ar.getAteriaId()) {
                menu.poista(ar);
                menu.peruutaRaakaAineenLisays();
            } 
        }
        valittuAteria = menu.annaAteria(1);
        listaaAteriat(1);
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alustus();        
    }
    
    
    /**
     * alustus
     */
    public void alustus() {
        ateriaAika.getItems().addAll("päivällinen", "iltapala");
        paa.getItems().addAll("nauta", "kana", "possu", "kala", "kasvispääraaka-aine");
        lisu.getItems().addAll("kasvislisuke", "pasta", "riisi", "peruna", "leipä");
        ateriaAika2.getItems().addAll("päivällinen", "iltapala");
        paa2.getItems().addAll("nauta", "kana", "possu", "kala", "kasvispääraaka-aine");
        lisu2.getItems().addAll("kasvislisuke", "pasta", "riisi", "peruna", "leipä");
        ateriaLista.clear();
        ateriaLista.addSelectionListener(e -> naytaRaakaAineet());
        ateriaOtsikko.setText("Suorita haku");
    }
}