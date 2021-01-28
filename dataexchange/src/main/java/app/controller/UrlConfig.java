package app.controller;

import com.yzh.utilts.EnvironmentSelectTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.BaseUrl;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Yzh
 * @create 2021-01-27 16:08
 * @details
 */
public class UrlConfig implements Initializable {

    @FXML
    private TextField datastoreUrl, modelUrl, hdfsUrl, geomesaUrl, ucUrl;
    @FXML
    private Button ok, cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datastoreUrl.setText(BaseUrl.DATASTORE_URL);
        modelUrl.setText(BaseUrl.MODEL_URL);
        hdfsUrl.setText(BaseUrl.HDFS_URL);
        geomesaUrl.setText(BaseUrl.GEOMESA_URL);
        ucUrl.setText(BaseUrl.UC_URL);
    }

    @FXML
    public void updateUrl() {
        EnvironmentSelectTool.finalUrl = BaseUrl.DATASTORE_URL = datastoreUrl.getText().trim();
        EnvironmentSelectTool.finalModelUrl = BaseUrl.MODEL_URL = modelUrl.getText().trim();
        EnvironmentSelectTool.finalHDFSUrl = BaseUrl.HDFS_URL = hdfsUrl.getText().trim();
        BaseUrl.GEOMESA_URL = geomesaUrl.getText().trim();
        EnvironmentSelectTool.finalUrl = BaseUrl.UC_URL = ucUrl.getText().trim();
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
