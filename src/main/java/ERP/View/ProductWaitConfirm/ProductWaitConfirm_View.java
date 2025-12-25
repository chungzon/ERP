package ERP.View.ProductWaitConfirm;

import ERP.ERPApplication;
import ERP.Model.ProductWaitingConfirm.ProductWaitingConfirm_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Observable;

/** [ERP.View] - Product waiting confirm */
public class ProductWaitConfirm_View extends Observable {
    private Stage Stage;
    private Button Return;
    private ToolKit ToolKit;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private SystemSetting_Model SystemSetting_Model;
    public ProductWaitConfirm_View(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ProductWaitingConfirm_Model = ToolKit.ModelToolKit.getProductWaitingConfirmModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
        initComponents();
    }
   private void initComponents(){
        Stage = ToolKit.ComponentToolKit.setStage("待確認商品【" + SqlAdapter.getDbDisplayName() + "】", true,true);
        BorderPane BorderPane = ToolKit.ComponentToolKit.setBorderPane(10,0,10,0);

        HBox HBox = ToolKit.ComponentToolKit.setHBox(Pos.CENTER_LEFT,5,0,10,0,10);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        Return = ToolKit.ComponentToolKit.setButton("回主選單",270,15,18);
        HBoxChild.add(Return);
        BorderPane.setTop(HBox);

        BorderPane.setCenter(ToolKit.CallFXML.ShowProductWaitConfirm(Stage));
        CreateAction();
        Stage.setScene(ToolKit.ComponentToolKit.setScene(BorderPane));
        Stage.show();
    }
    private void CreateAction(){
        Stage.setOnCloseRequest(event -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
        Return.setOnAction(ActionEvent -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
    }
}
