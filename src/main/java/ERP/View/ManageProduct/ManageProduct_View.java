package ERP.View.ManageProduct;

import ERP.ERPApplication;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.CallJAR;
import ERP.ToolKit.ComponentToolKit;
import ERP.Enum.Product.Product_Enum;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.Observable;

/** [ERP.View] - Manage product */
public class ManageProduct_View extends Observable {
    private Stage Stage;
    private BorderPane BorderPane;
    private Button ManageProductCategory, EstablishProductInfo, SearchProductInfo, Inventory, WebStore, Return;

    private ComponentToolKit ComponentToolKit;
    private CallFXML CallFXML;
    private CallJAR CallJAR;
    public ManageProduct_View(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        CallFXML = ERPApplication.ToolKit.CallFXML;
        CallJAR = ERPApplication.ToolKit.CallJAR;
        initComponents();
    }
    private void initComponents(){
        Stage = ComponentToolKit.setStage("商品資料管理【" + SqlAdapter.getDbDisplayName() + "】",true,true);
        BorderPane = ComponentToolKit.setBorderPane(10,10,10,10);
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,5,0,10,0,10);
        ObservableList<Node> HBoxChild = HBox.getChildren();
        ManageProductCategory = ComponentToolKit.setButton("商品類別管理",310,15,18);
        HBoxChild.add(ManageProductCategory);
        EstablishProductInfo = ComponentToolKit.setButton("商品資料建立",310,15,18);
        HBoxChild.add(EstablishProductInfo);
        SearchProductInfo = ComponentToolKit.setButton("商品資料速查",310,15,18);
        HBoxChild.add(SearchProductInfo);
        Inventory = ComponentToolKit.setButton("盤點",310,15,18);
        HBoxChild.add(Inventory);
        WebStore = ComponentToolKit.setButton("網路/網站 上架",310,15,18);
        HBoxChild.add(WebStore);
        Return = ComponentToolKit.setButton("返回系統主選單",310,15,18);
        HBoxChild.add(Return);
        BorderPane.setTop(HBox);

        CreateAction();
        Stage.setScene(ComponentToolKit.setScene(BorderPane, 1320, 900));
        Stage.show();
    }
    private void CreateAction(){
        Stage.setOnCloseRequest(event -> {
            setChanged();
            notifyObservers();
            ComponentToolKit.closeThisStage(Stage);
        });
        ManageProductCategory.setOnAction(ActionEvent -> BorderPane.setCenter(CallFXML.ManageProductCategory()));
        EstablishProductInfo.setOnAction(ActionEvent -> BorderPane.setCenter(CallFXML.ManageProductInfo(Product_Enum.ManageProductStatus.建立, Stage)));
        SearchProductInfo.setOnAction(ActionEvent -> BorderPane.setCenter(CallFXML.ManageProductInfo(Product_Enum.ManageProductStatus.搜尋, Stage)));
        Inventory.setOnAction(ActionEvent -> CallJAR.Label_Reader());
        WebStore.setOnAction(ActionEvent -> BorderPane.setCenter(CallFXML.ManageProductOnShelf(Stage)));
        Return.setOnAction(ActionEvent -> {
            setChanged();
            notifyObservers();
            ComponentToolKit.closeThisStage(Stage);
        });
    }
}
