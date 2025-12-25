package ERP.Controller.Toolkit.ShowSearchOrderOverview;

import ERP.Bean.Order.*;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Enum.Order.Order_Enum.OrderSearchColumn;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Model.Order.Order_Model;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowSearchOrderOverview_Controller {
    @FXML Label searchLabel;
    @FXML Button searchButton;
    @FXML TextField searchText;
    @FXML Accordion accordion;
    @FXML ProgressIndicator ProgressIndicator;

    private final String[] TableColumns = { "項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量", "安全存量"};
    private final String[] TableColumnCellValue = new String[]{"ItemNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock","SafetyStock"};
    private final int[] ColumnsLength = { 45, 130, 450, 85, 50, 80, 80, 80, 50, 80};

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.CallFXML CallFXML;
    private Order_Model Order_Model;
    private OrderObject OrderObject;
    private Stage Stage;

    private HashMap<TitledPane,Order_Bean> TitlePaneAndOrderMap = new HashMap<>();
    private HashMap<TitledPane,Button> TitlePaneAndButtonMap = new HashMap<>();
    public ShowSearchOrderOverview_Controller(){
        ToolKit = ERPApplication.ToolKit;
        KeyPressed = ToolKit.KeyPressed;
        ComponentToolKit = ToolKit.ComponentToolKit;
        CallFXML = ToolKit.CallFXML;
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    public void setComponent(){
        searchLabel.setText(OrderObject.name() + "搜尋");
    }
    @FXML protected void searchTextKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String searchKeyWord = searchText.getText();
            if(searchKeyWord.equals("")) {
                DialogUI.MessageDialog("※ 請輸入關鍵字");
                return;
            }
            searchOrder(searchKeyWord);
        }
    }
    @FXML protected void searchOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String searchKeyWord = searchText.getText();
            if(searchKeyWord.equals("")) {
                DialogUI.MessageDialog("※ 請輸入關鍵字");
                return;
            }
            searchOrder(searchKeyWord);
        }
    }
    private void searchOrder(String searchText){
        Task Task = searchTask(searchText);
        ProgressIndicator.progressProperty().bind(Task.progressProperty());
        new Thread(Task).start();
    }
    private Task searchTask(String searchKeyWord){
        return new Task() {
            @Override public Void call(){
                ComponentToolKit.setButtonDisable(searchButton,true);
                Platform.runLater(()-> ComponentToolKit.setProgressIndicatorVisible(ProgressIndicator,true));


                ConditionalOrderSearch_Bean conditionalOrderSearch_Bean = new ConditionalOrderSearch_Bean();
                conditionalOrderSearch_Bean.setOrderObject(OrderObject);
                if(OrderObject == Order_Enum.OrderObject.廠商)
                    conditionalOrderSearch_Bean.setSearchOrderSource(SearchOrderSource.採購單);
                else
                    conditionalOrderSearch_Bean.setSearchOrderSource(SearchOrderSource.報價單);
                conditionalOrderSearch_Bean.setOrderSearchMethod(OrderSearchMethod.關鍵字搜尋);
                conditionalOrderSearch_Bean.setBorrowed(false);
                conditionalOrderSearch_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                conditionalOrderSearch_Bean.setStartDate(null);
                conditionalOrderSearch_Bean.setEndDate(null);
                conditionalOrderSearch_Bean.setSearchKeyWord(searchKeyWord);
                conditionalOrderSearch_Bean.setSearchOrderReviewStatus(null);

                ObservableList<SearchOrder_Bean> searchOrderList;
                if(OrderObject == Order_Enum.OrderObject.廠商)
                    searchOrderList = Order_Model.searchOrder(OrderSource.採購單, OrderSearchColumn.對象與專案名稱, conditionalOrderSearch_Bean);
                else
                    searchOrderList = Order_Model.searchOrder(OrderSource.報價單, OrderSearchColumn.對象與專案名稱, conditionalOrderSearch_Bean);
                ToolKit.sortSearchOrderNumber(searchOrderList);

                if(searchOrderList.size() == 0)
                    Platform.runLater(()-> DialogUI.MessageDialog("※ 找無相關貨單"));
                else{
                    ArrayList<Order_Bean> allOrderList = new ArrayList<>();
                    for(SearchOrder_Bean SearchOrder_Bean : searchOrderList){
                        allOrderList.add(generateOrderBean(SearchOrder_Bean));
                    }
                    Platform.runLater(()-> putOrderInfoIntoVBox(allOrderList));
                }
                ComponentToolKit.setButtonDisable(searchButton,false);
                Platform.runLater(()-> ComponentToolKit.setProgressIndicatorVisible(ProgressIndicator,false));
                return null;
            }
        };
    }
    private Order_Bean generateOrderBean(SearchOrder_Bean SearchOrder_Bean){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setNowOrderNumber(getSearchOrder_NowOrderNumber(SearchOrder_Bean));
        Order_Bean.setWaitingOrderNumber(SearchOrder_Bean.getWaitingOrderNumber());
        Order_Bean.setAlreadyOrderNumber(SearchOrder_Bean.getAlreadyOrderNumber());

        if(Order_Bean.getAlreadyOrderNumber() != null)  Order_Bean.setOrderSource(OrderObject == Order_Enum.OrderObject.廠商 ? OrderSource.進貨單 : OrderSource.出貨單);
        else if(Order_Bean.getWaitingOrderNumber() != null) Order_Bean.setOrderSource(OrderObject == Order_Enum.OrderObject.廠商 ? OrderSource.待入倉單 : OrderSource.待出貨單);
        else    Order_Bean.setOrderSource(OrderObject == Order_Enum.OrderObject.廠商 ? OrderSource.採購單 : OrderSource.報價單);

        Order_Bean.setProjectName(SearchOrder_Bean.getProjectName());
        Order_Bean.setProductList(Order_Model.getOrderItems(OrderObject == Order_Enum.OrderObject.廠商 ? OrderSource.採購單 : OrderSource.報價單, SearchOrder_Bean.getId()));
        Order_Bean.setEstablishSource(SearchOrder_Bean.getEstablishSource());
        Order_Bean.setIsCheckout(SearchOrder_Bean.isCheckout());
        return Order_Bean;
    }
    private String getSearchOrder_NowOrderNumber(SearchOrder_Bean SearchOrder_Bean){
        if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.採購單 || SearchOrder_Bean.getOrderSource() == OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.報價單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
            return SearchOrder_Bean.getQuotationNumber();
        else if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
            return SearchOrder_Bean.getOrderNumber();
        return "";
    }
    private void putOrderInfoIntoVBox(ArrayList<Order_Bean> allOrderList){
        accordion.getPanes().clear();
        TitlePaneAndButtonMap.clear();
        TitlePaneAndOrderMap.clear();
        for (Order_Bean Order_Bean : allOrderList) {
            TitledPane TitlePane = createTitlePane(Order_Bean.getProjectName());
            BorderPane BorderPane = createBorderPane(Order_Bean);
            TitlePane.setContent(BorderPane);
            accordion.getPanes().add(TitlePane);
            TitlePaneAndOrderMap.put(TitlePane, Order_Bean);
        }
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private TitledPane createTitlePane(String projectName){
        TitledPane TitledPane = new TitledPane();
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();
        HBoxChild.add(ComponentToolKit.setLabel(projectName,-1, -1,18,""));
        Button Button = ComponentToolKit.setButton("開啟貨單",120,-1,16);
        Button.setDisable(true);
        Button.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        HBoxChild.add(Button);
        TitledPane.setGraphic(HBox);
        TitledPane.setExpanded(false);
        TitlePaneAndButtonMap.put(TitledPane,Button);

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
        Button.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                Order_Bean Order_Bean = TitlePaneAndOrderMap.get(accordion.getExpandedPane());
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
                establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                establishOrder_NewStage_Bean.setOrderSource(Order_Bean.getOrderSource());
                establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
                establishOrder_NewStage_Bean.setOrder_Bean(Order_Bean);
                establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
                establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
                establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        });
        return TitledPane;
    }
    private BorderPane createBorderPane(Order_Bean Order_Bean){
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        TableView.setEditable(true);
        for(int index = 0 ; index < TableColumns.length ; index++) {
            if(index == 0 || index == 3 || index == 5 || index == 6 || index == 7 || index == 8 || index == 9)
                TableView.getColumns().add(ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]));
            else {
                TableColumn<OrderProduct_Bean,String> TableColumn = ComponentToolKit.createOrderProductTableStringColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                if(index == 1)
                    TableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                TableView.getColumns().add(TableColumn);
            }
        }
        TableView.setPrefSize(-1,200);
        TableView.getItems().addAll(Order_Bean.getProductList());
        BorderPane BorderPane = ComponentToolKit.setBorderPane(5,5,5,5);
        BorderPane.setCenter(TableView);
        return BorderPane;
    }
}
