package fxmenu;

import javafx.application.Application;
import javafx.stage.Stage;
import menu.Menu;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author tiia1
 * @version 16.2.2021
 *
 */
public class PaaIkkunaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("PaaIkkunaGUIView.fxml"));
            final Pane root = ldr.load();
            final PaaIkkunaGUIController paaikkunaCtrl = (PaaIkkunaGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("paaikkuna.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("PaaIkkuna");
            primaryStage.show();
            
            Menu menu = new Menu();
            paaikkunaCtrl.setMenu(menu);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei k�yt�ss�
     */
    public static void main(String[] args) {
        launch(args);
    }
}