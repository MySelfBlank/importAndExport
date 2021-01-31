package app.controller;

import com.alibaba.fastjson.JSONObject;
import com.yzh.Index;
import enums.KeyType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import model.DomainModel;
import model.ResponseResult;
import onegis.common.paging.PageInfo;
import services.ExportServices;
import services.ImportServices;
import services.RequestServices;
import services.impl.ExportServicesImpl;
import services.impl.ImprotServicesImpl;
import services.impl.RequestServicesImpl;
import utils.BaseUrl;
import utils.EhcacheUtil;
import com.yzh.importTask.importUtils.ImportBaseInfo;
import utils.PathUtil;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainFrameController implements Initializable {

    @FXML
    private Label userName;
    @FXML
    private Label pages;
    @FXML
    private TextField keyword;
    @FXML
    private TableView table;
    @FXML
    private TableColumn tabIndex;
    @FXML
    private TableColumn tabName;
    @FXML
    private TableColumn tabId;
    @FXML
    private Label sdomainName;
    @FXML
    private Button exportButton;
    @FXML
    private Button importButton;
    @FXML
    private HBox hBox;
    @FXML
    private ToggleButton dotypeButton;
    @FXML
    private TextField dotypeInput;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private CheckBox isExportBaseData, isImprotBaseData;

    private RequestServices requestServices = new RequestServicesImpl();

    private ExportServices exportServices = new ExportServicesImpl();

    private ImportServices importServices = new ImprotServicesImpl();

    private String curKey = null;//当前输入的时空域名称

    private DomainModel curDoamin = null;//当前选中的时空域

    private final ObservableList<DomainModel> tableData = FXCollections.observableArrayList();//表数据
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //设置表头
        tabIndex.setCellValueFactory(new PropertyValueFactory<Object, Object>("index"));
        tabId.setCellValueFactory(new PropertyValueFactory<Object, Object>("id"));
        tabName.setCellValueFactory(new PropertyValueFactory<Object, Object>("name"));
        String token = EhcacheUtil.getInstance().get("token", "token").toString();
        BaseUrl.token = token;
        try{
            String nickName = requestServices.getNickName(token);
            userName.setText(nickName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void querySdomain(){
        curDoamin = null;
        querySdomain(KeyType.QUERY);
    }
    @FXML
    private void NextSdomain(){
        querySdomain(KeyType.NEXT);
    }
    @FXML
    private void PreviousSdomain(){
        querySdomain(KeyType.PREVIOUS);
    }

    /**
     * 执行数据的导出
     */
    @FXML
    private void ExportData() throws Exception {
        if(curKey==null||curKey.isEmpty()||curKey.equals("")||curKey.equals("null")){
            AlertController.alert("请选择时空域！");
            return;
        }
        exportButton.setDisable(true);
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(userName.getScene().getWindow());
        if(file==null){
            AlertController.alert("请选择导出目录！");
            exportButton.setDisable(false);
            return;
        }
        String path = file.getPath();//选择的文件夹路径

        Task<Void> exportTask = new Task<Void>() {
            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Succeeded");
                exportButton.setDisable(false);
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                updateMessage("Cancelled");
                exportButton.setDisable(false);
            }

            @Override
            protected void failed() {
                super.failed();
                updateMessage("Failed");
                exportButton.setDisable(false);
            }

            @Override
            protected Void call() throws Exception {
                exportServices.exportSobject(path,curDoamin.getId(),curDoamin.getName());
                updateMessage("Finish");
                updateProgress(1,1);
                System.out.println("Finish");
                exportButton.setDisable(false);
                return null;
            }
        };
        progress.progressProperty().bind(exportTask.progressProperty());
        new Thread(exportTask).start();

        //基本信息下载
        if(isExportBaseData.isSelected()){
            new Thread(()->{
                try {
                    PageInfo<String> listPageInfo = requestServices.queryObjectIds(curDoamin.getId(), "1", "1000");
                    long total = listPageInfo.getTotal();
                    PathUtil.setDir(total,curDoamin.getName(),path);//设置路径
                    Index.startVoid(PathUtil.baseInfoDir ,curDoamin.getName(),Long.parseLong(curDoamin.getId()),dotypeInput.getText().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 导入数据
     */
    @FXML
    private void ImportData(){
        if(curKey==null||curKey.isEmpty()||curKey.equals("")||curKey.equals("null")){
            AlertController.alert("请选择时空域！");
            return;
        }
        importButton.setDisable(true);
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(userName.getScene().getWindow());
        if(file==null){
            AlertController.alert("请选择导入数据的文件夹！");
            importButton.setDisable(false);
            return;
        }
        String path = file.getPath();//选择的文件夹路径
        //导入之前先导入基本数据
        Task<Void> importTask = new Task<Void>() {
            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Succeeded");
                System.out.println("Succeeded");
                importButton.setDisable(false);
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                updateMessage("Cancelled");
                System.out.println("Cancelled");
                importButton.setDisable(false);
            }

            @Override
            protected void failed() {
                super.failed();
                updateMessage("Failed");
                Throwable exception = this.getException();
                System.out.println("Failed:"+exception.getMessage());
                importButton.setDisable(false);
            }
            @Override
            protected Void call() throws Exception {
                //对象前先导入基础数据
                if(isImprotBaseData.isSelected()){
                    ImportBaseInfo.orderImport(path);
                }
                importServices.importData(path,Long.parseLong(curDoamin.getId()));
                updateMessage("Finish");
                updateProgress(1,1);
                System.out.println("Finish");
                importButton.setDisable(false);
                return null;
            }
        };
        progress.progressProperty().bind(importTask.progressProperty());
        new Thread(importTask).start();
    }

    /**
     * 表点击选中
     */
    @FXML
    private void click(){
        ObservableList selectedItems = table.getSelectionModel().getSelectedItems();
        DomainModel selectDoamin = (DomainModel)selectedItems.get(0);
        if(selectDoamin!=null){
            curDoamin = selectDoamin;
            sdomainName.setText(selectDoamin.getName());
        }
    }

    /**
     * 查询时空域中对象信息
     * @param keyType 时空域
     */
    private void querySdomain(KeyType keyType){
        if(keyword.getText().equals("")||keyword.getText().isEmpty()){
            AlertController.alert("请输入要查询的时空域");
            keyword.requestFocus();
            return;
        }
        Integer pageIndex = 1;
        String[] pageString = pages.getText().split("/");
        Integer curPage = Integer.parseInt(pageString[0]);
        Integer lastPage = Integer.parseInt(pageString[1]);
        try{
            switch (keyType){
                case NEXT:
                    pageIndex = curPage+1;
                    break;
                case QUERY:
                    pageIndex = 1;
                    curKey = keyword.getText();
                    break;
                case PREVIOUS:
                    pageIndex = curPage-1;
                    break;
            }
            if(pageIndex<1||pageIndex>lastPage){
                return;
            }
            if(curKey.isEmpty()||curKey.equals("")){
                return;
            }
            ResponseResult responseResult = requestServices.queryDomain(pageIndex, 10, curKey);
            if(responseResult.getStatus()==200){
                tableData.clear();
                Object content = responseResult.getData();
                JSONObject data = JSONObject.parseObject(content.toString());
                PageInfo pageInfo = data.toJavaObject(PageInfo.class);
                List<JSONObject> list = pageInfo.getList();
                table.setItems(tableData);
                for(int num=0;num<list.size();num++){
                    DomainModel domainModel = new DomainModel(list.get(num).getString("name"),list.get(num).getString("id"),num+1+"");
                    tableData.add(domainModel);
                }
                pages.setText(pageInfo.getPageNum()+"/"+pageInfo.getPages());
            }else {
                System.out.println(responseResult.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void inputDOTypeName (){
        if(dotypeButton.isSelected()){
            dotypeInput.setVisible(true);
            hBox.setVisible(true);
        }else {
            dotypeInput.setVisible(false);
            hBox.setVisible(false);
            //关闭时把类模板名称制空
            dotypeInput.setText(null);
        }

    }
}
