package app.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzh.userInfo.UserInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
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
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String userId = (String) EhcacheUtil.getInstance().get("user", "userId");
        String password = (String) EhcacheUtil.getInstance().get("user", "password");
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../ui/mainFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("导入导出工具");
            JMetro jMetro = new JMetro(Style.LIGHT);
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNIFIED);
            jMetro.setScene(scene);
            stage.setScene(scene);
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
     *
     * @param event
     */
    public void checkUser(ActionEvent event) {
        try {
            if (StrUtil.isBlank(userName.getText()) || StrUtil.isBlank(userName.getText())) {
                AlertController.alert("用户名不能为空！");
                return;
            }
            if (StrUtil.isBlank(userPwd.getText()) || StrUtil.isBlank(userPwd.getText())) {
                AlertController.alert("密码不能为空！");
                return;
            }
            JSONObject jsonObject = requestServices.queryUser(userName.getText(), userPwd.getText());
            if (jsonObject == null) {
                AlertController.alert("登录异常！");
                return;
            } else {
                String status = jsonObject.get("status").toString();
                if ("453".equals(status)) {
                    AlertController.alert("用户名或密码不正确！");
                    return;
                } else {
                    JSONObject tokenObj = JSON.parseObject(jsonObject.get("data").toString());
                    String token = tokenObj.get("token").toString();
                    EhcacheUtil.getInstance().put("token", "token", token);
                    //如果登陆成功就将邮箱和密码存入缓存,并做硬盘持久化保存以便下次登陆自动填写
                    EhcacheUtil.getInstance().put("user", "userId", userName.getText());
                    if (isCheck.isSelected()) {
                        EhcacheUtil.getInstance().put("user", "password", userPwd.getText());
                    }
                    EhcacheUtil.getInstance().put("user", "auto", isCheck.isSelected() + "");
                    UserInfo.username = userName.getText().trim();
                    UserInfo.password = userPwd.getText().trim();
                    open(event);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openUpdateUrl(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/urlConfig.fxml"));
        JMetro jMetro = new JMetro(Style.DARK);

        Stage stage = new Stage();
        stage.setTitle("修改服务链接配置");
        Scene scene = new Scene(root);
        jMetro.setScene(scene);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
