package ERP.View.ManagePayableReceivableSystem;

import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
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

/** [ERP.View] - Manage payable or receivable system */
public class ManagePayableReceivableSystem_View extends Observable {
    private ToolKit ToolKit;
    private Product_Model Product_Model;
    private Order_Model Order_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private SystemSetting_Model SystemSetting_Model;
    private Stage Stage;
    private BorderPane BorderPane;
    private Button manageBankInfo, createReceivableInfo, inquireReceivableInfo, manageCompanyAccount, createPayableInfo, inquirePayableInfo, paymentCompareSystem,Return;
    public ManagePayableReceivableSystem_View(ToolKit ToolKit, Product_Model Product_Model, Order_Model Order_Model, ManagePayableReceivable_Model ManagePayableReceivable_Model, SystemSetting_Model SystemSetting_Model){
        this.ToolKit = ToolKit;
        this.Product_Model = Product_Model;
        this.Order_Model = Order_Model;
        this.ManagePayableReceivable_Model = ManagePayableReceivable_Model;
        this.SystemSetting_Model = SystemSetting_Model;
        initComponents();
    }
    private void initComponents(){
        Stage = ToolKit.ComponentToolKit.setStage("應付應收管理系統【" + SqlAdapter.getDbDisplayName() + "】", true,true);
        BorderPane = ToolKit.ComponentToolKit.setBorderPane(10,0,10,0);
        HBox HBox = ToolKit.ComponentToolKit.setHBox(Pos.CENTER,5,0,10,0,10);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        createPayableInfo = ToolKit.ComponentToolKit.setButton("付款資料建立",270,15,18);
        HBoxChild.add(createPayableInfo);
        inquirePayableInfo = ToolKit.ComponentToolKit.setButton("付款資料速查",270,15,18);
        HBoxChild.add(inquirePayableInfo);
        createReceivableInfo = ToolKit.ComponentToolKit.setButton("收款資料建立",270,15,18);
        HBoxChild.add(createReceivableInfo);
        inquireReceivableInfo = ToolKit.ComponentToolKit.setButton("收款資料速查",270,15,18);
        HBoxChild.add(inquireReceivableInfo);
        manageCompanyAccount = ToolKit.ComponentToolKit.setButton("公司帳戶管理",270,15,18);
        HBoxChild.add(manageCompanyAccount);
        manageBankInfo = ToolKit.ComponentToolKit.setButton("銀行資料管理",270,15,18);
        HBoxChild.add(manageBankInfo);
        paymentCompareSystem = ToolKit.ComponentToolKit.setButton("出納比對系統",270,15,18);
        HBoxChild.add(paymentCompareSystem);
        Return = ToolKit.ComponentToolKit.setButton("返回系統主選單",270,15,18);
        HBoxChild.add(Return);

        BorderPane.setTop(HBox);

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
        createPayableInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.EstablishPayableReceivable(Stage, OrderObject.廠商)));
        inquirePayableInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchPayableReceivable(Stage, OrderObject.廠商)));
        createReceivableInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.EstablishPayableReceivable(Stage, OrderObject.客戶)));
        inquireReceivableInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchPayableReceivable(Stage, OrderObject.客戶)));
        manageCompanyAccount.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.ManageCompanyAccount()));
        manageBankInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.ManageBankInfo()));
        paymentCompareSystem.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.PaymentCompareSystem(Stage)));
        Return.setOnAction(ActionEvent -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
    }

}
