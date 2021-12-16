package fxmenu;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import menu.Menu;
import menu.RaakaAine;

/**
 * @author tiia1
 * @version 16 Apr 2021
 *
 */
public class AinesosaGUIController implements ModalControllerInterface<Menu>, Initializable{

    @FXML private TextField nimiTextField;
    @FXML private ComboBox<String> tyyppiChooser;
    
    @FXML void poistu2() {
        ModalController.closeStage(nimiTextField);
    }

    @FXML
    void tallennaAines() {
        uusiAines();
    }
    //--------------------------------------------------
    
    private Menu menu;
    
    /**
     * Luodaan ja tallennetaan uusi ainesosa.
     */
    public void uusiAines() {
        String nimi = nimiTextField.getText();
        nimi = nimi.toLowerCase();
        String tyyppi = tyyppiChooser.getSelectionModel().getSelectedItem();
        if (nimi.length() < 3 || tyyppi.length() < 3 || tyyppi.equals(null)) {
            Dialogs.showMessageDialog("Lisää jotain kenttiin.");
            return;
        }
        RaakaAine uusi = new RaakaAine();
        menu.rekisteroi(uusi);
        menu.lisaa(uusi); 
        uusi.setNimi(nimi);
        menu.setTyyppi(uusi, tyyppi);
        ModalController.closeStage(nimiTextField);
    }

    @Override
    public void handleShown() {
        //        
    }

    @Override
    public Menu getResult() {
        return menu;
    }

    @Override
    public void setDefault(Menu oletus) {
        this.menu = oletus;
        for (int i = 0; i < menu.getRaakaAineidenTyypit(); i++) {
            tyyppiChooser.getItems().add(menu.annaRaakaAineenTyyppi(i).getNimi());
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }

}
