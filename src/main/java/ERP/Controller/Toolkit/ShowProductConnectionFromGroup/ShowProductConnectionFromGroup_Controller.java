package ERP.Controller.Toolkit.ShowProductConnectionFromGroup;

import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowProductConnectionFromGroup_Controller {
    @FXML private Accordion accordion;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;

    private Order_Model Order_Model;

    private EstablishOrder_Controller EstablishOrder_Controller;
    private String ISBN;
    private String objectID;
    private ArrayList<ProductGroup_Bean> productGroupList;
    private Stage Stage;
    private HashMap<TitledPane,Button> TitlePaneAndButtonMap = new HashMap<>();

    private final String[] TableColumns = { "選擇", "項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量", "安全存量"};
    private final String[] TableColumnCellValue = new String[]{"SelectCheckBox","ItemNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock","SafetyStock"};
    private final int[] ColumnsLength = { 40, 45, 130, 450, 60, 50, 80, 80, 80, 50, 80};
    public ShowProductConnectionFromGroup_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;

        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){
        this.EstablishOrder_Controller = EstablishOrder_Controller;
    }
    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }
    public void setObjectID(String objectID){
        this.objectID = objectID;
    }
    public void setProductGroupList(ArrayList<ProductGroup_Bean> productGroupList){
        this.productGroupList = productGroupList;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        loadProductConnection();
    }
    private void loadProductConnection(){
        ComponentToolKit.getOrderReferenceAccordionList(accordion).clear();
        for(int index = 0; index < productGroupList.size(); index++){
            ProductGroup_Bean ProductGroup_Bean = productGroupList.get(index);
            TitledPane TitlePane = createTitlePane(index,ProductGroup_Bean);
            BorderPane BorderPane = createBorderPane(ProductGroup_Bean.getProductList());
            TitlePane.setContent(BorderPane);
            ComponentToolKit.getOrderReferenceAccordionList(accordion).add(TitlePane);
        }
    }
    private TitledPane createTitlePane(int index, ProductGroup_Bean ProductGroup_Bean){
        TitledPane TitledPane = new TitledPane();
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();
        HBoxChild.add(ComponentToolKit.setLabel((index+1) + " ",-1, -1,18,null));
        Button openOrderButton = ComponentToolKit.setButton("開啟貨單",120,-1,16);
        openOrderButton.setDisable(true);
        openOrderButton.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        openOrderButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                Order_Bean Order_Bean = new Order_Bean();
                Order_Bean.setOrderSource(Order_Enum.OrderSource.報價單);
                Order_Bean.setNowOrderNumber(ProductGroup_Bean.getOrderNumber());

                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
                establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                establishOrder_NewStage_Bean.setOrderSource(Order_Enum.OrderSource.報價單);
                establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
                establishOrder_NewStage_Bean.setOrder_Bean(Order_Bean);
                establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
                establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
                establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        });
        HBoxChild.add(openOrderButton);

        HBoxChild.add(ComponentToolKit.setLabel(ProductGroup_Bean.getGroupName(),-1, -1,18,null));
        HBoxChild.add(ComponentToolKit.setLabel("$" + ToolKit.RoundingString(ProductGroup_Bean.getPriceAmount()),-1, -1,18,null));
        TitledPane.setGraphic(HBox);
        TitledPane.setExpanded(false);
        TitledPane.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)){
                ObservableList<TitledPane> titlePaneList = accordion.getPanes();
                for(TitledPane selectTitledPane : titlePaneList){
                    Button selectButton = TitlePaneAndButtonMap.get(selectTitledPane);
                    if(selectTitledPane == TitledPane && TitledPane.isExpanded()){
                        selectButton.setDisable(false);
                    }else
                        selectButton.setDisable(true);
                }
            }
        });
        TitlePaneAndButtonMap.put(TitledPane,openOrderButton);
        return TitledPane;
    }
    private BorderPane createBorderPane(ObservableList<OrderProduct_Bean> productList){
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        TableView.setEditable(true);
        for(int index = 0 ; index < TableColumns.length ; index++) {
            if(index == 0) {
                TableColumn<OrderProduct_Bean,CheckBox> tableColumn = ComponentToolKit.createOrderProductTableCheckBoxColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]);
                tableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox("CENTER-LEFT", "16"));
                TableView.getColumns().add(tableColumn);
            }else if(index == 1 || index == 4 || index == 8 || index == 9 || index == 10) {
                TableColumn<OrderProduct_Bean,Integer> TableColumn = ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableColumn.setCellFactory(column -> new setIntegerTableColumnTextFill("CENTER-LEFT", "16"));
                TableView.getColumns().add(TableColumn);
            }else if(index ==6 || index == 7){
                TableColumn<OrderProduct_Bean,Double> TableColumn = ComponentToolKit.createOrderProductTableDoubleColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableColumn.setCellFactory(column -> new setDoubleTableColumnTextFill("CENTER-LEFT", "16"));
                TableView.getColumns().add(TableColumn);
            }else {
                TableColumn<OrderProduct_Bean,String> TableColumn = ComponentToolKit.createOrderProductTableStringColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableColumn.setCellFactory(column -> new setStringTableColumnTextFill("CENTER-LEFT", "16"));
                TableView.getColumns().add(TableColumn);
            }
        }
        TableView.setOnMouseClicked((MouseEvent MouseEvent) -> {
            if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
                OrderProduct_Bean selectOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
                if(selectOrderProduct_Bean != null &&
                        DialogUI.ConfirmDialog("※ 確定將商品【" + selectOrderProduct_Bean.getISBN() + "】新增到貨單 ?",true,false,0,0)){

                    OrderProduct_Bean OrderProduct_Bean = Order_Model.getProduct(Order_Enum.OrderObject.客戶, Order_Enum.ProductSearchColumn.ISBN, objectID, selectOrderProduct_Bean.getISBN()).get(0);
                    OrderProduct_Bean.setQuantity(1);
                    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity() * OrderProduct_Bean.getSinglePrice()));
                    ObservableList<OrderProduct_Bean> selectProductList = FXCollections.observableArrayList();
                    selectProductList.add(selectOrderProduct_Bean);
                    EstablishOrder_Controller.setSelectProductBean(false, selectProductList);
                }
            }
        });
        TableView.setOnKeyReleased(KeyEvent -> {
            if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
                if(OrderProduct_Bean.isCheckBoxSelect())    OrderProduct_Bean.setCheckBoxSelect(false);
                else if(!OrderProduct_Bean.isCheckBoxSelect())  OrderProduct_Bean.setCheckBoxSelect(true);
            }
        });
        TableView.setPrefSize(1170,630);
        TableView.getItems().addAll(productList);

        BorderPane BorderPane = ComponentToolKit.setBorderPane(5,5,5,5);
        BorderPane.setCenter(TableView);
        return BorderPane;
    }
    @FXML protected void GetProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            HashMap<String,Integer> selectProductMap = null;
            for(ProductGroup_Bean ProductGroup_Bean : productGroupList){
                ObservableList<OrderProduct_Bean> productList = ProductGroup_Bean.getProductList();
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(OrderProduct_Bean.isCheckBoxSelect()){
                        if(selectProductMap == null)
                            selectProductMap = new HashMap<>();
                        if(!selectProductMap.containsKey(OrderProduct_Bean.getISBN()))
                            selectProductMap.put(OrderProduct_Bean.getISBN(),1);
                        else
                            selectProductMap.put(OrderProduct_Bean.getISBN(),selectProductMap.get(OrderProduct_Bean.getISBN())+1);
                    }
                }
            }
            if(selectProductMap == null)
                DialogUI.MessageDialog("※ 未選擇任何商品");
            else {
                ObservableList<OrderProduct_Bean> selectProductList = FXCollections.observableArrayList();
                for(String ISBN: selectProductMap.keySet()){
                    int selectProductQuantity = selectProductMap.get(ISBN);
                    OrderProduct_Bean OrderProduct_Bean = Order_Model.getProduct(Order_Enum.OrderObject.客戶, Order_Enum.ProductSearchColumn.ISBN, objectID, ISBN).get(0);
                    if(selectProductQuantity != 1){
                        OrderProduct_Bean.setQuantity(selectProductQuantity);
                        OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity() * OrderProduct_Bean.getSinglePrice()));
                    }
                    selectProductList.add(OrderProduct_Bean);
                }
                EstablishOrder_Controller.setSelectProductBean(false, selectProductList);
                ComponentToolKit.closeThisStage(Stage);
            }
        }
    }
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, CheckBox> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBox, boolean empty) {
            super.updateItem(CheckBox, empty);
            if(!empty){
                String style = "-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;";
                setGraphic(ComponentToolKit.getOrderProductTableViewSelectItem(getTableView(), getIndex()).getCheckBox());
                OrderProduct_Bean OrderProduct_Bean = getTableView().getItems().get(getIndex());
                if(OrderProduct_Bean.getISBN().equals(ISBN))
                    style = style + "-fx-background-color:#15BCBC;";
                setStyle(style);
            }else   setGraphic(null);
        }
    }
    private class setStringTableColumnTextFill extends TableCell<OrderProduct_Bean, String> {
        String Alignment, FontSize;
        setStringTableColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item);
                String style = "-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;";
                OrderProduct_Bean OrderProduct_Bean = getTableView().getItems().get(getIndex());
                if(OrderProduct_Bean.getISBN().equals(ISBN))
                    style = style + "-fx-background-color:#15BCBC;";
                setStyle(style);
            }
        }
    }
    private class setIntegerTableColumnTextFill extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        setIntegerTableColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                String style = "-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;";
                OrderProduct_Bean OrderProduct_Bean = getTableView().getItems().get(getIndex());
                setText(String.valueOf(item));
                if(OrderProduct_Bean.getISBN().equals(ISBN))
                    style = style + "-fx-background-color:#15BCBC;";
                setStyle(style);
            }
        }
    }
    private class setDoubleTableColumnTextFill extends TableCell<OrderProduct_Bean, Double> {
        String Alignment, FontSize;
        setDoubleTableColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(String.valueOf(item));
                String style = "-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;";
                OrderProduct_Bean OrderProduct_Bean = getTableView().getItems().get(getIndex());
                if(OrderProduct_Bean.getISBN().equals(ISBN))
                    style = style + "-fx-background-color:#15BCBC;";
                setStyle(style);
            }
        }
    }
}
