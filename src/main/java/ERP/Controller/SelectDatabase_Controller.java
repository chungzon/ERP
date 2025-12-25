package ERP.Controller;

import ERP.Bean.DataBaseServer_Bean;
import ERP.ERPApplication;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.CallFXML;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import com.ERPServer.ERP.ErpServerApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.pdfsam.ui.RingProgressIndicator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SelectDatabase_Controller {

    private Stage Stage;
    boolean restart;
    private CallConfig CallConfig;
    private CallFXML CallFXML;
    public SelectDatabase_Controller(){
        this.CallConfig = ERPApplication.ToolKit.CallConfig;
        this.CallFXML = ERPApplication.ToolKit.CallFXML;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setRestart(boolean restart){
        this.restart = restart;
    }
    public void setDataBaseButton(Parent Parent) throws Exception {
        Node Node = Parent.lookup("#MainPanel");
        if (Node instanceof GridPane) {
            SqlAdapter sqlAdapter = SqlAdapter.getInstance();
            ArrayList<DataBaseServer_Bean> DataBaseList = sqlAdapter.getDataBaseList();
            for (DataBaseServer_Bean Server : DataBaseList) {
                Button DBSelect = new Button(Server.getDisplay());
                DBSelect.setStyle("-fx-background-radius:120px;");// 圓形
                DBSelect.setPrefSize(90, 90);
                DBSelect.setId(String.valueOf(Server.getIndex()));
                DBSelect.setOnAction((ActionEvent e) -> {
                    try {
                        // 检查 OpenCV 是否已加载，如果未加载则尝试加载
                        if (!ERPApplication.isOpenCVLoaded()) {
                            if (!ERPApplication.tryLoadOpenCVLibrary()) {
                                throw new UnsatisfiedLinkError("无法加载 OpenCV 库，请检查 opencv-dll 目录");
                            }
                        }
                        
                        DataBaseServer_Bean DataBaseServer_Bean = sqlAdapter.getDataBaseServer_Bean(Integer.parseInt(DBSelect.getId()) - 1);
                        sqlAdapter.connectDB(restart,Integer.parseInt(DBSelect.getId()) - 1);
                        new openServer(DataBaseServer_Bean).start();
                        showRingProgress(Stage);
                    }catch (UnsatisfiedLinkError Ex){
                        Ex.printStackTrace();
                        ERPApplication.Logger.error("OpenCV 库加载失败", Ex);
                        DialogUI.AlarmDialog("【新系統架設】未設置 openCV dll");
                        try {
                            File dllDir = new File("opencv-dll");
                            if (dllDir.exists()) {
                                Desktop.getDesktop().open(dllDir);
                            } else {
                                ERPApplication.Logger.warn("opencv-dll 目录不存在");
                            }
                        } catch (IOException ex) {
                            ERPApplication.Logger.error("无法打开 opencv-dll 目录", ex);
                        }
                    }catch (Exception Ex){
                        ERPApplication.Logger.catching(Ex);
                        Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                    }
                });
                ((GridPane) Node).add(DBSelect, Server.getIndex(), 1);
            }
        }
    }
    class openServer extends Thread{
        private DataBaseServer_Bean DataBaseServer_Bean;
        public openServer(DataBaseServer_Bean DataBaseServer_Bean){
            this.DataBaseServer_Bean = DataBaseServer_Bean;
        }
        public void run(){
            CallConfig.setERPServerDatabase(DataBaseServer_Bean);
            ErpServerApplication.openServer();
        }
    }
    private void showRingProgress(Stage Stage){
        RingProgressIndicator ringProgressIndicator = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader();
            FXMLLoader.setLocation(getClass().getResource("/fxml/Main.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = new Scene(Parent, 400, 275);
            Node node = Parent.lookup("#MainPanel");
            ringProgressIndicator = new RingProgressIndicator();
            ((GridPane) node).add(ringProgressIndicator, 1, 1);
            Stage.setScene(Scene);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
        }
        new RingProgressThread(ringProgressIndicator).start();
    }
    class RingProgressThread extends Thread{
        int count = 0;
        RingProgressIndicator ringProgressIndicator;
        public RingProgressThread(RingProgressIndicator ringProgressIndicator){
            this.ringProgressIndicator = ringProgressIndicator;
        }
        public void run(){
            while(true){
                try {
                    Thread.sleep(10);
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                }
                count++;
                Platform.runLater(() -> ringProgressIndicator.setProgress(count));
                if(count > 100) {
                    Platform.runLater(() -> CallFXML.loadSystemMenu(Stage));
                    break;
                }
            }
        }
    }
}
