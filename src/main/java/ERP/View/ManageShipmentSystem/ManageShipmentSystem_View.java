package ERP.View.ManageShipmentSystem;

import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
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

/** [ERP.View] - Manage shipment system */
public class ManageShipmentSystem_View extends Observable {
    private Stage Stage;
    private BorderPane BorderPane;
    private Button ManageCustomerInfo, EstablishShipmentOrder, SearchQuotation_WaitingShipmentOrder, SearchShipment_ReturnOrder, SearchOrderProgress, AccountReceivable, Return;

    private ToolKit ToolKit;
    private Product_Model Product_Model;
    private Order_Model Order_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private SystemSetting_Model SystemSetting_Model;
    public ManageShipmentSystem_View(ToolKit ToolKit, Product_Model Product_Model, Order_Model Order_Model, ManagePayableReceivable_Model ManagePayableReceivable_Model, ProductWaitingConfirm_Model ProductWaitingConfirm_Model, SystemSetting_Model SystemSetting_Model){
        this.ToolKit =  ToolKit;
        this.Product_Model = Product_Model;
        this.Order_Model = Order_Model;
        this.ManagePayableReceivable_Model = ManagePayableReceivable_Model;
        this.ProductWaitingConfirm_Model = ProductWaitingConfirm_Model;
        this.SystemSetting_Model = SystemSetting_Model;
        initComponents();
    }
    private void initComponents(){
        Stage = ToolKit.ComponentToolKit.setStage("出貨管理系統【" + SqlAdapter.getDbDisplayName() + "】", true,true);
        BorderPane = ToolKit.ComponentToolKit.setBorderPane(10,0,10,0);
        HBox HBox = ToolKit.ComponentToolKit.setHBox(Pos.CENTER,5,0,10,0,10);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        ManageCustomerInfo = ToolKit.ComponentToolKit.setButton("客戶資料管理",270,15,18);
        HBoxChild.add(ManageCustomerInfo);
        EstablishShipmentOrder = ToolKit.ComponentToolKit.setButton("報價 / 退貨單 建立",270,15,18);
        HBoxChild.add(EstablishShipmentOrder);
        SearchQuotation_WaitingShipmentOrder = ToolKit.ComponentToolKit.setButton("報價 / 待出貨 & 子貨單 查詢",270,15,18);
        HBoxChild.add(SearchQuotation_WaitingShipmentOrder);
        SearchShipment_ReturnOrder = ToolKit.ComponentToolKit.setButton("出貨 / 退貨單 查詢",270,15,18);
        HBoxChild.add(SearchShipment_ReturnOrder);
        SearchOrderProgress = ToolKit.ComponentToolKit.setButton("貨單進度 查詢",270,15,18);
        HBoxChild.add(SearchOrderProgress);
        AccountReceivable = ToolKit.ComponentToolKit.setButton("應收帳總表及明細",270,15,18);
        HBoxChild.add(AccountReceivable);
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
        ManageCustomerInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.ManageCustomerInfo(Stage)));
        EstablishShipmentOrder.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.EstablishOrder(Stage, OrderSource.報價單)));
        SearchQuotation_WaitingShipmentOrder.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchOrder(Stage, OrderObject.客戶, SearchOrderSource.報價單)));
        SearchShipment_ReturnOrder.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchOrder(Stage, OrderObject.客戶, SearchOrderSource.出貨與出退貨單)));
        SearchOrderProgress.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchOrderProgress(Stage, OrderObject.客戶)));
        AccountReceivable.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchNonePayReceive(Stage, Order_Enum.OrderObject.客戶)));
        Return.setOnAction(ActionEvent -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
    }
}
