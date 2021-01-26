package app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.RequestServices;
import services.impl.RequestServicesImpl;
import utils.BaseUrl;
import utils.EhcacheUtil;
import utils.HttpClientUtils;

import javax.xml.soap.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
     @FXML
     private TextField userName;
     @FXML
     private TextField userPwd;
     @FXML
     private CheckBox isCheck;

     private RequestServices requestServices = new RequestServicesImpl();
    /**
     * 启动设置
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String userId = (String)EhcacheUtil.getInstance().get("user", "userId");
        String password =  (String)EhcacheUtil.getInstance().get("user", "password");
        Object tag = EhcacheUtil.getInstance().get("user", "auto");
        if (tag != null) {
            if (tag.toString().equals("true")) {
                isCheck.setSelected(true);
            }
        }
        userName.setText(userId);
        userPwd.setText(password);
    }


    // 打开主窗口
    public void open(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("../ui/mainFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("导入导出工具");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setFocused(true);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent arg0) {
                    EhcacheUtil.getInstance().clodeManager();
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();

            //关闭当前窗口
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测用户名和密码是否正确
     * @param event
     */
    public void checkUser(ActionEvent event){
        try{
            if(userName.getText().isEmpty()||userName.getText().trim()==""){
                AlertController.alert("用户名不能为空！");
                return;
            }
            if(userPwd.getText().isEmpty()||userPwd.getText().trim()==""){
                AlertController.alert("密码不能为空！");
                return;
            }
            JSONObject jsonObject = requestServices.queryUser(userName.getText(), userPwd.getText());
            if(jsonObject==null){
                AlertController.alert("登录异常！");
                return;
            }else {
                String status =jsonObject.get("status").toString();
                if ("453".equals(status)) {
                    AlertController.alert("用户名或密码不正确！");
                    return;
                }else {
                    JSONObject tokenObj = JSON.parseObject(jsonObject.get("data").toString());
                    String token = tokenObj.get("token").toString();
                    EhcacheUtil.getInstance().put("token", "token", token);
                    //如果登陆成功就将邮箱和密码存入缓存,并做硬盘持久化保存以便下次登陆自动填写
                    EhcacheUtil.getInstance().put("user", "userId", userName.getText());
                    if(isCheck.isSelected()){
                        EhcacheUtil.getInstance().put("user", "password", userPwd.getText());
                    }
                    EhcacheUtil.getInstance().put("user", "auto", isCheck.isSelected() + "");
                    open(event);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
