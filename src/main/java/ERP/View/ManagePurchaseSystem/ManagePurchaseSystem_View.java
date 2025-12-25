package ERP.View.ManagePurchaseSystem;

import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSource;
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

/** [ERP.View] - Manage purchase system */
public class ManagePurchaseSystem_View extends Observable {
    private Stage Stage;
    private BorderPane BorderPane;
    private Button ManageManufacturerInfo, EstablishPurchaseOrder,SearchQuotation_WaitingWarehousingOrder,SearchPurchase_ReturnOrder,SearchOrderProgress,AccountPayable,Return;

    private ToolKit ToolKit;
    private Product_Model Product_Model;
    private Order_Model Order_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private SystemSetting_Model SystemSetting_Model;
    public ManagePurchaseSystem_View(ToolKit ToolKit, Product_Model Product_Model, Order_Model Order_Model, ManagePayableReceivable_Model ManagePayableReceivable_Model, ProductWaitingConfirm_Model ProductWaitingConfirm_Model, SystemSetting_Model SystemSetting_Model){
        this.ToolKit =  ToolKit;
        this.Product_Model = Product_Model;
        this.Order_Model = Order_Model;
        this.ManagePayableReceivable_Model = ManagePayableReceivable_Model;
        this.ProductWaitingConfirm_Model = ProductWaitingConfirm_Model;
        this.SystemSetting_Model = SystemSetting_Model;
        initComponents();
    }
    private void initComponents(){
        Stage = ToolKit.ComponentToolKit.setStage("進貨管理系統【" + SqlAdapter.getDbDisplayName() + "】", true,true);
        BorderPane = ToolKit.ComponentToolKit.setBorderPane(10,0,10,0);
        HBox HBox = ToolKit.ComponentToolKit.setHBox(Pos.CENTER,5,0,10,0,10);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        ManageManufacturerInfo = ToolKit.ComponentToolKit.setButton("廠商資料管理",270,15,18);
        HBoxChild.add(ManageManufacturerInfo);
        EstablishPurchaseOrder = ToolKit.ComponentToolKit.setButton("採購 / 退貨單 建立",270,15,18);
        HBoxChild.add(EstablishPurchaseOrder);
        SearchQuotation_WaitingWarehousingOrder = ToolKit.ComponentToolKit.setButton("採購 / 待入倉 查詢",270,15,18);
        HBoxChild.add(SearchQuotation_WaitingWarehousingOrder);
        SearchPurchase_ReturnOrder = ToolKit.ComponentToolKit.setButton("進貨 / 退貨單 查詢",270,15,18);
        HBoxChild.add(SearchPurchase_ReturnOrder);
        SearchOrderProgress = ToolKit.ComponentToolKit.setButton("貨單進度 查詢",270,15,18);
        HBoxChild.add(SearchOrderProgress);
        AccountPayable = ToolKit.ComponentToolKit.setButton("應付帳總表及明細",270,15,18);
        HBoxChild.add(AccountPayable);
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
        ManageManufacturerInfo.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.ManageManufacturerInfo(Stage)));
        EstablishPurchaseOrder.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.EstablishOrder(Stage, OrderSource.採購單)));
        SearchQuotation_WaitingWarehousingOrder.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchOrder(Stage, OrderObject.廠商, SearchOrderSource.採購單)));
        SearchPurchase_ReturnOrder.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchOrder(Stage, OrderObject.廠商, SearchOrderSource.進貨與進退貨單)));
        SearchOrderProgress.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchOrderProgress(Stage, OrderObject.廠商)));
        AccountPayable.setOnAction(ActionEvent -> BorderPane.setCenter(ToolKit.CallFXML.SearchNonePayReceive(Stage, OrderObject.廠商)));
        Return.setOnAction(ActionEvent -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
    }
}
