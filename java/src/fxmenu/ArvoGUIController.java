package fxmenu;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import menu.Ateria;
import menu.AterioidenRaakaAineet;
import menu.Menu;

/**
 * @author tiia1
 * @version 16 Feb 2021
 *
 */
public class ArvoGUIController implements ModalControllerInterface<Ateria>{

    @FXML private ListView<String> ainesosaListaus;
    @FXML private Label otsikko;

    @FXML
    void haeUudestaan() {
        ModalController.closeStage(otsikko);
    }

    @FXML
    void poistu() {
        PaaIkkunaGUIController.setUudestaan(false);
        ModalController.closeStage(otsikko);
    }
    
    //-----------------------------------------------------------------------------
    private Ateria ateria;
    private Menu menu;

    @Override
    public void handleShown() {
        otsikko.setText(ateria.getNimi());
        AterioidenRaakaAineet art = menu.haeAterioidenRaakaAineet(ateria.getAteriaId());
        for (int i = 0; i < art.getLkm(); i++) {
            ainesosaListaus.getItems().add(menu.annaRaakaAineIdlla(art.anna(i).getRaakaAineId()).getNimi());
        }      
    }

    @Override 
    public Ateria getResult() {
        return ateria;
    }

    @Override
    public void setDefault(Ateria arg0) {
        ateria = arg0;
        menu = PaaIkkunaGUIController.getMenu();
    }

}
