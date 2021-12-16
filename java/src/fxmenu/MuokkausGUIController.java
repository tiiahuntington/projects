package fxmenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import menu.Ateria;
import menu.AterianRaakaAine;
import menu.AterioidenRaakaAineet;
import menu.Menu;

/**
 * @author tiia1
 * @version 16 Feb 2021
 *
 */
public class MuokkausGUIController implements ModalControllerInterface<Ateria>, Initializable {

    @FXML private ComboBox<String> aikaField;
    @FXML private TextField NimiField;
    @FXML private ComboBox<String> ainesosatField;
    @FXML private ListView<String> reseptiNakyma;
    

    @FXML
    void lisaaAinesosa() {
        lisaaAterialleAines();
    }

    @FXML
    void lisaaUusiAinesosa() { 
        this.menu = ModalController.showModal(MuokkausGUIController.class.getResource("AinesosaGUIView.fxml"), "Uusi ainesosa", null, menu);
        ainesosatField.getItems().clear();
        alustaFieldit();
    }

    @FXML
    void poista() {
        if (Dialogs.showQuestionDialog("Tallennus", "Haluatko varmasti poistaa?", "Kyllä", "Ei")) {
            int id = muokattava.getAteriaId();
            menu.poista(muokattava);
            for (int i = 0; i < menu.getAterioidenRaakaAineet(); i++) {
                AterianRaakaAine ar = menu.annaAterianRaakaAine(i);
                if (ar == null) continue;
                if (id == ar.getAteriaId()) {
                    menu.poista(ar);
                    menu.peruutaRaakaAineenLisays();
                }
            }
            ModalController.closeStage(NimiField);
        }      
    }

    @FXML
    void poistu() {
        if (!muutettu) ModalController.closeStage(NimiField);
        else {
            int id = muokattava.getAteriaId();
            AterioidenRaakaAineet art = menu.haeAterioidenRaakaAineet(id);
            for (int i = 0; i < art.getLkm(); i++) {
                for (int j = 0; j < lisatyt.size(); j++) {   
                    if (art.anna(i).getRaakaAineId() == lisatyt.get(j)) menu.poista(art.anna(i));
                }
            }
            peruutaRaakaAineidenLisays();          
            ModalController.closeStage(NimiField);
        }
    }

    @FXML
    void tallenna() {
        if (muokattava.getAika().length() == 0) uusiAteria();
        else {
            muokkaaTietoja();
            ModalController.closeStage(NimiField);
        }
        
    }

    
    //-------------------------------------------------------
    
    private Menu menu;
    private Ateria muokattava;
    private Ateria muokattavaKlooni;
    private boolean muutettu = false;
    private ArrayList<Integer> lisatyt = new ArrayList<Integer>();
    
    
    /**
     * uuden lisäys 
     */
    public void uusiAteria() {        
        String nimi = NimiField.getText();
        String aika = aikaField.getSelectionModel().getSelectedItem();
        if (nimi == null || aika == null) {
            peruutaRaakaAineidenLisays();
            Dialogs.showMessageDialog("Täytä kaikki kentät!");
            return;
        }
        muokattava = muokattavaKlooni;
        menu.lisaa(muokattava);
        muokattava.setNimi(nimi);
        muokattava.setAika(aika);       
    }
    
    
    /**
     * Muokkaa muokattavan aterian tietoja.
     */
    public void muokkaaTietoja() {
        muokattava = muokattavaKlooni;
        String nimi = NimiField.getText();
        String aika = aikaField.getSelectionModel().getSelectedItem();
        if (!nimi.equals(muokattava.getNimi()))muokattava.setNimi(nimi);
        if (aika != null)muokattava.setAika(aika);
    }
    
    
    /**
     * Lisätään valittu merkkijono viereiseen listaan ja "alustetaan" valitseminen uudestaan. 
     * Samalla luodaan aterian ja ainesosan välille kytkös.
     */
    public void lisaaAterialleAines() {
        String uusiAines = ainesosatField.getSelectionModel().getSelectedItem();
        if (uusiAines == null) {
            Dialogs.showMessageDialog("Valitse listasta raaka-aine!");
            return;
        }
        reseptiNakyma.getItems().add(uusiAines);
        AterianRaakaAine uus = new AterianRaakaAine();
        uus.setAteriaId(muokattavaKlooni.getAteriaId());
        int id = menu.haeRaakaAineNimella(uusiAines).getRaakaAineenId();
        uus.setRaakaAineId(id);
        lisatyt.add(id);
        menu.lisaa(uus);
        muutettu = true;
    }
    
    
    /**
     * Peruuttaa raaka-aineiden lisayksen.
     */
    public void peruutaRaakaAineidenLisays() {
        for (int i = 0; i < lisatyt.size(); i++) {
            menu.peruutaRaakaAineenLisays();
        }
    }
    

    @Override
    public Ateria getResult() {
        return muokattava;
    }

    @Override
    public void handleShown() {
        //     
    }

    @Override
    public void setDefault(Ateria arg0) {
        muokattava = arg0;
        if (muokattava == null) {
            muokattava = new Ateria();
            menu.rekisteroi(muokattava);
            try {
                muokattavaKlooni = muokattava.clone();
            } catch (CloneNotSupportedException e) {
                NimiField.setText("Kloonaus epäonnistui, kokeile uudestaan.");
            }
            return;
        }
        try {
            muokattavaKlooni = muokattava.clone();
        } catch (CloneNotSupportedException e) {
            NimiField.setText("Kloonaus epäonnistui, kokeile uudestaan.");
        }
        NimiField.setText(muokattavaKlooni.getNimi());
        aikaField.setPromptText(muokattavaKlooni.getAika());
        AterioidenRaakaAineet ar = menu.haeAterioidenRaakaAineet(muokattavaKlooni.getAteriaId());
        for (int i = 0; i < ar.getLkm();i++) {
            reseptiNakyma.getItems().add(menu.annaRaakaAineIdlla(ar.anna(i).getRaakaAineId()).getNimi());
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }
    
    
    /**
     * Alustetaan dialogin tarvitemat tiedot
     */
    public void alusta() {
        menu = PaaIkkunaGUIController.getMenu();
        aikaField.getItems().addAll("päivällinen", "iltapala");
        alustaFieldit();        
    }
    
    
    /**
     * Alustetaan drop down laatikoiden sisallot.
     */
    public void alustaFieldit() {
        Collection<String> raakaAineet = menu.getListaRaakaAineista();
        for (String nimi : raakaAineet) {
        ainesosatField.getItems().add(nimi);
        }
    }
}
