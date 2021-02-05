package app.controller;


import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.FlatAlert;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static void alert(String msg){
        FlatAlert alert = new FlatAlert(FlatAlert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
