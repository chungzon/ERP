package ERP.Controller.Toolkit.ShowOrderReferenceOverview;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.*;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ShowOrderReferenceOverview_Controller {
    @FXML private GridPane operationGridPane;
    @FXML private TextField CustomerCodeText,ISBNText;
    @FXML private CheckBox ShipmentOrderPriorityCheckBox;
    @FXML private Accordion accordion;

    @FXML private Label tooltip_Label;
    @FXML private Tooltip tooltip;

    private final String[] TableColumns = { "項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量", "安全存量"};
    private final String[] TableColumnCellValue = new String[]{"ItemNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock","SafetyStock"};
    private final int[] ColumnsLength = { 45, 130, 550, 85, 50, 80, 80, 80, 50, 80};

    private HashMap<TitledPane,Order_Bean> TitlePaneAndOrderMap = new HashMap<>();

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;
    private CallConfig CallConfig;
    private ERP.ToolKit.Tooltip Tooltip;
    private Order_Model Order_Model;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private boolean canModifyOrderReference;
    private Order_Bean Order_Bean;
    private HashMap<Order_Enum.OrderSource, HashMap<Integer,Boolean>> orderReferenceMap;
    private ObjectInfo_Bean ObjectInfo_Bean;
    private HashMap<String,Boolean> orderProductMap;
    public ShowOrderReferenceOverview_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.CallConfig = ToolKit.CallConfig;
        this.Tooltip = ToolKit.Tooltip;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){
        this.EstablishOrder_Controller = EstablishOrder_Controller;
    }
    public void setCanModifyOrderReference(boolean canModifyOrderReference){
        this.canModifyOrderReference = canModifyOrderReference;
    }
    public void setOrder_Bean(Order_Bean Order_Bean){
        this.Order_Bean = Order_Bean;
    }
    public void setOrderReferenceMap(HashMap<Order_Enum.OrderSource, HashMap<Integer,Boolean>> orderReferenceMap){
        this.orderReferenceMap = orderReferenceMap;
    }
    public void setObjectInfo_Bean(ObjectInfo_Bean ObjectInfo_Bean){
        this.ObjectInfo_Bean = ObjectInfo_Bean;
    }
    public void setOrderProductMap(HashMap<String,Boolean> orderProductMap) {
        this.orderProductMap = orderProductMap;
    }
    public void setComponent(){
        setTextFieldLimitDigital();
        ComponentToolKit.setGridPaneDisable(operationGridPane, !canModifyOrderReference);
        CustomerCodeText.setText(ObjectInfo_Bean.getObjectID());
        if(orderProductMap != null){
            for(String ISBN : orderProductMap.keySet()){
                if(orderProductMap.get(ISBN)){
                    ISBNText.setText(ISBN);
                    break;
                }
            }
        }
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.showOrderReference());
        loadOrderReference(orderReferenceMap);
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(ISBNText, false);
    }
    private void loadOrderReference(HashMap<Order_Enum.OrderSource, HashMap<Integer,Boolean>> orderReferenceMap){
        HashMap<Integer,Boolean> mainOrderReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.報價單);
        HashMap<Integer,Boolean> subBillReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單);
        if(mainOrderReferenceMap.size() == 0 && subBillReferenceMap.size() == 0)
            DialogUI.AlarmDialog("※ 無任何參考貨單");
        else {
            ObservableList<Order_Bean> orderReferenceList = Order_Model.getOrderReferenceByOrderID(mainOrderReferenceMap,subBillReferenceMap);
            ObservableList<TitledPane> originalTitlePaneList = ComponentToolKit.getOrderReferenceAccordionList(accordion);
            if(originalTitlePaneList != null && originalTitlePaneList.size() != 0) {
                boolean addStatus;
                for (Order_Bean Order_Bean : orderReferenceList) {
                    addStatus = true;
                    for (TitledPane titledPane : originalTitlePaneList) {
                        Order_Bean originalOrder_Bean = TitlePaneAndOrderMap.get(titledPane);
                        if (originalOrder_Bean.getOrderSource() == Order_Bean.getOrderSource() &&
                                originalOrder_Bean.getOrderID().equals(Order_Bean.getOrderID())) {
                            addStatus = false;
                            break;
                        }
                    }
                    if (addStatus)
                        generateSingleTitlePane(Order_Bean);
                }
            }else
                generateAllTitlePane(orderReferenceList);
        }
    }
    @FXML protected void CalculateAndLoadMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            boolean isCalculateSingleProduct;
            boolean isShipmentOrderPriority = ShipmentOrderPriorityCheckBox.isSelected();
            String confirmString;
            if(isShipmentOrderPriority){
                confirmString = " √ 【出貨單】優先\n\n※ 是否計算單一品項 ? \n【是】單一品項\n【否】所有品項";
                isCalculateSingleProduct = DialogUI.ConfirmDialog(confirmString,true,true,0,11);
            }else{
                confirmString = "※ 是否計算單一品項 ? \n【是】單一品項\n【否】所有品項";
                isCalculateSingleProduct = DialogUI.ConfirmDialog(confirmString,true,false,0,0);
            }
            if(isCalculateSingleProduct){
                String ISBN = ISBNText.getText();
                if(ISBN == null || ISBN.equals("")) {
                    DialogUI.MessageDialog("※ 請輸入 ISBN");
                    return;
                }else if(ISBN.length() != 13) {
                    DialogUI.MessageDialog("※ ISBN 錯誤");
                    return;
                }
            }
            if(isCalculateSingleProduct){
                for(String ISBN : orderProductMap.keySet()){
                    if(orderProductMap.get(ISBN)){
                        calculateSingleProduct(ISBN,isShipmentOrderPriority);
                        break;
                    }
                }
            }else
                calculateAllProduct(isShipmentOrderPriority);
        }
    }
    private void calculateSingleProduct(String ISBN,boolean isShipmentOrderPriority){
        ObservableList<Order_Bean> similarOrderReferenceList = Order_Model.findSimilarOrderReference(Order_Bean.getOrderSource(),Order_Bean.getOrderID(),CustomerCodeText.getText(),ISBN,isShipmentOrderPriority);
        if(similarOrderReferenceList.size() != 0) {
            Order_Bean similarOrder_Bean = getSimilarOrderReference(similarOrderReferenceList,isShipmentOrderPriority, Order_Bean.getObjectID());
            if(isTableViewExistSimilarOrderReference(similarOrder_Bean)){
                if(similarOrder_Bean.getOrderSource() == OrderSource.出貨子貨單)
                    DialogUI.AlarmDialog("※ 表格內已存在該參考貨單，子貨單號【" + similarOrder_Bean.getNowOrderNumber()+ "】");
                else
                    DialogUI.AlarmDialog("※ 表格內已存在該參考貨單，報價單號【" + similarOrder_Bean.getNowOrderNumber()+ "】");
            }else {
                if (handleOrderReference(similarOrder_Bean, true)) {
                    generateSingleTitlePane(similarOrder_Bean);
                    DialogUI.MessageDialog("※ 加入成功");
                }else
                    DialogUI.MessageDialog("※ 加入失敗");
            }
        }else
            DialogUI.MessageDialog("※ 無相似參考貨單");
    }
    private void calculateAllProduct(boolean isShipmentOrderPriority){
        ObservableList<Order_Bean> orderReferenceList = null;
        for(String ISBN : orderProductMap.keySet()) {
            ObservableList<Order_Bean> similarOrderReferenceList = Order_Model.findSimilarOrderReference(Order_Bean.getOrderSource(), Order_Bean.getOrderID(), CustomerCodeText.getText(), ISBN,isShipmentOrderPriority);
            if (similarOrderReferenceList.size() != 0) {
                if(orderReferenceList == null)  orderReferenceList = FXCollections.observableArrayList();
                Order_Bean similarOrder_Bean = getSimilarOrderReference(similarOrderReferenceList,isShipmentOrderPriority, Order_Bean.getObjectID());
                orderReferenceList.add(similarOrder_Bean);
            }
        }
        if(orderReferenceList == null)
            DialogUI.MessageDialog("※ 無相似參考貨單");
        else{
            boolean status = true;
            for(Order_Bean similarOrder_Bean : orderReferenceList){
                if(!isTableViewExistSimilarOrderReference(similarOrder_Bean)){
                    if (!handleOrderReference(similarOrder_Bean, true)) {
                        status = false;
                        break;
                    }else{
                        generateSingleTitlePane(similarOrder_Bean);
                    }
                }
            }
            if(status)
                DialogUI.MessageDialog("※ 加入成功");
            else
                DialogUI.MessageDialog("※ 加入失敗");
        }
    }
    private Order_Bean getSimilarOrderReference(ObservableList<Order_Bean> similarOrderReferenceList,boolean isShipmentOrderPriority,String customerCode){
        if(isShipmentOrderPriority){
            for(int index = similarOrderReferenceList.size()-1 ; index >= 0 ; index--){
                Order_Bean Order_Bean = similarOrderReferenceList.get(index);
                if(Order_Bean.getAlreadyOrderDate() != null && Order_Bean.getObjectID().equals(customerCode)) {
                    return Order_Bean;
                }
            }
            for(int index = similarOrderReferenceList.size()-1 ; index >= 0 ; index--){
                Order_Bean Order_Bean = similarOrderReferenceList.get(index);
                if(Order_Bean.getAlreadyOrderDate() != null) {
                    return Order_Bean;
                }
            }
        }
        for(int index = similarOrderReferenceList.size()-1 ; index >= 0 ; index--){
            Order_Bean Order_Bean = similarOrderReferenceList.get(index);
            if(Order_Bean.getAlreadyOrderDate() == null && Order_Bean.getObjectID().equals(customerCode)) {
                return Order_Bean;
            }
        }
        for(int index = similarOrderReferenceList.size()-1 ; index >= 0 ; index--){
            Order_Bean Order_Bean = similarOrderReferenceList.get(index);
            if(Order_Bean.getAlreadyOrderDate() == null) {
                return Order_Bean;
            }
        }
        return similarOrderReferenceList.get(similarOrderReferenceList.size()-1);
    }
    private boolean isTableViewExistSimilarOrderReference(Order_Bean similarOrder_Bean){
        ObservableList<TitledPane> titlePaneList = ComponentToolKit.getOrderReferenceAccordionList(accordion);
        for(TitledPane TitledPane : titlePaneList){
            Order_Bean Order_Bean = TitlePaneAndOrderMap.get(TitledPane);
            if(similarOrder_Bean.getOrderSource() == Order_Bean.getOrderSource() && similarOrder_Bean.getOrderID().equals(Order_Bean.getOrderID()))
                return true;
        }
        return false;
    }
    @FXML protected void ImportOrderReferenceMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = Order_Model.getOrderReferenceFromDatabase(Order_Bean);
            if(orderReferenceMap == null)
                DialogUI.MessageDialog("※ 無現有參考貨單");
            else{
                loadOrderReference(orderReferenceMap);
                HashMap<Integer,Boolean> mainOrderReferenceMap = this.orderReferenceMap.get(OrderSource.報價單);
                HashMap<Integer,Boolean> subBillReferenceMap = this.orderReferenceMap.get(OrderSource.出貨子貨單);
                ObservableList<TitledPane> titlePaneList = ComponentToolKit.getOrderReferenceAccordionList(accordion);
                for(TitledPane TitledPane : titlePaneList){
                    Order_Bean Order_Bean = TitlePaneAndOrderMap.get(TitledPane);
                    if(Order_Bean.getOrderSource() == OrderSource.出貨子貨單)
                        subBillReferenceMap.put(Order_Bean.getOrderID(),true);
                    else
                        mainOrderReferenceMap.put(Order_Bean.getOrderID(),true);
                }
                if(CallConfig.setOrderReferenceJson(mainOrderReferenceMap,subBillReferenceMap))
                    DialogUI.MessageDialog("※ 匯入完成");
                else
                    DialogUI.MessageDialog("※ 貨單參考檔案寫入失敗");
            }
        }
    }
    @FXML protected void removeAllMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<TitledPane> titlePaneList = ComponentToolKit.getOrderReferenceAccordionList(accordion);
            if(titlePaneList.size() == 0)
                DialogUI.MessageDialog("※ 無任何參考貨單");
            else{
                if(CallConfig.setOrderReferenceJson(new HashMap<>(),new HashMap<>())) {
                    ComponentToolKit.getOrderReferenceAccordionList(accordion).clear();
                    orderReferenceMap.get(Order_Enum.OrderSource.報價單).clear();
                    orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單).clear();
                    DialogUI.MessageDialog("※ 移除成功");
                }else
                    DialogUI.MessageDialog("※ 移除失敗");

            }
        }
    }
    private TitledPane createTitlePane(Order_Bean Order_Bean){
        TitledPane TitledPane = new TitledPane();
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        Button removeButton = ComponentToolKit.setButton("移除",60,-1,16);
        removeButton.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        ComponentToolKit.setButtonDisable(removeButton, !canModifyOrderReference);
        HBoxChild.add(removeButton);

        HBoxChild.add(ComponentToolKit.setLabel(""+getReferenceProductSize(Order_Bean.getProductList()),20,-1,16,null));
        HBoxChild.add(ComponentToolKit.setLabel(getTitlePaneName(Order_Bean),-1, -1,18,"-fx-text-fill:"+ getTextFillColor(Order_Bean.getOrderSource())));

        TitledPane.setGraphic(HBox);
        TitledPane.setExpanded(false);

        TitledPane.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
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
        removeButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                if(handleOrderReference(Order_Bean,false)) {
                    removeSingleTitlePane(TitledPane);
                    DialogUI.MessageDialog("※ 移除成功");
                }else
                    DialogUI.MessageDialog("※ 移除失敗");
            }
        });
        return TitledPane;
    }
    private boolean handleOrderReference(Order_Bean Order_Bean, boolean addOrRemove){
        HashMap<Integer,Boolean> mainOrderReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.報價單);
        HashMap<Integer,Boolean> subBillReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單);
        if(Order_Bean.getOrderSource() == OrderSource.出貨子貨單) {
            if(!addOrRemove)    subBillReferenceMap.remove(Order_Bean.getOrderID());
            else    subBillReferenceMap.put(Order_Bean.getOrderID(),true);
        }else {
            if(!addOrRemove)    mainOrderReferenceMap.remove(Order_Bean.getOrderID());
            else    mainOrderReferenceMap.put(Order_Bean.getOrderID(),true);
        }
        return CallConfig.setOrderReferenceJson(mainOrderReferenceMap, subBillReferenceMap);
    }
    private void generateAllTitlePane(ObservableList<Order_Bean> orderReferenceList){
        for (Order_Bean Order_Bean : orderReferenceList) {
            TitledPane TitlePane = createTitlePane(Order_Bean);
            BorderPane BorderPane = createBorderPane(Order_Bean);
            TitlePane.setContent(BorderPane);
            ComponentToolKit.getOrderReferenceAccordionList(accordion).add(TitlePane);
            TitlePaneAndOrderMap.put(TitlePane, Order_Bean);
        }
    }
    private void generateSingleTitlePane(Order_Bean Order_Bean){
        TitledPane TitlePane = createTitlePane(Order_Bean);
        BorderPane BorderPane = createBorderPane(Order_Bean);
        TitlePane.setContent(BorderPane);
        ComponentToolKit.getOrderReferenceAccordionList(accordion).add(sortTitlePaneByAddSingleTitlePane(Order_Bean),TitlePane);
        TitlePaneAndOrderMap.put(TitlePane, Order_Bean);
    }
    private int sortTitlePaneByAddSingleTitlePane(Order_Bean insertOrder_Bean){
        ObservableList<TitledPane> titlePaneList = ComponentToolKit.getOrderReferenceAccordionList(accordion);
        for(int index = 0 ; index < titlePaneList.size() ; index++){
            Order_Bean Order_Bean = TitlePaneAndOrderMap.get(titlePaneList.get(index));
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(Order_Bean.getAlreadyOrderDate() != null && insertOrder_Bean.getAlreadyOrderDate() != null){
                    if(sdf.parse(Order_Bean.getAlreadyOrderDate()).before(sdf.parse(insertOrder_Bean.getAlreadyOrderDate())))
                        return index;
                }else{
                    if(Order_Bean.getAlreadyOrderDate() == null && sdf.parse(Order_Bean.getOrderDate()).before(sdf.parse(insertOrder_Bean.getOrderDate())))
                        return index;
                }
            }catch (Exception Ex){
                DialogUI.ExceptionDialog(Ex);
                ERPApplication.Logger.catching(Ex);
            }
        }
        return titlePaneList.size();
    }
    private void removeSingleTitlePane(TitledPane titledPane){
        TitlePaneAndOrderMap.remove(titledPane);
        ComponentToolKit.getOrderReferenceAccordionList(accordion).remove(titledPane);
    }
    private int getReferenceProductSize(ObservableList<OrderProduct_Bean> orderProductList){
        int size = 0;
        for(OrderProduct_Bean OrderProduct_Bean : orderProductList){
            if(orderProductMap.containsKey(OrderProduct_Bean.getISBN()))
                size++;
        }
        return size;
    }
    private String getTitlePaneName(Order_Bean Order_Bean){
        String titlePaneName;
        if(Order_Bean.getOrderSource() == OrderSource.出貨子貨單) {
            if(Order_Bean.getAlreadyOrderDate() != null && Order_Bean.getAlreadyOrderNumber() != null)
                titlePaneName = "【出貨-子】  " + Order_Bean.getObjectID() + ToolKit.fillSpecificCharacter(" ",6) + Order_Bean.getOrderDate() + ToolKit.fillSpecificCharacter(" ",4) + Order_Bean.getNowOrderNumber() + ToolKit.fillSpecificCharacter(" ",6) + Order_Bean.getAlreadyOrderDate() + ToolKit.fillSpecificCharacter(" ",4) + Order_Bean.getAlreadyOrderNumber();
            else
                titlePaneName = "【出貨-子】   " + Order_Bean.getObjectID() + ToolKit.fillSpecificCharacter(" ",6) + Order_Bean.getOrderDate() + ToolKit.fillSpecificCharacter(" ",2) + Order_Bean.getNowOrderNumber() + ToolKit.fillSpecificCharacter(" ",32);
        }else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單) {
            titlePaneName = "【出貨單】   " + Order_Bean.getObjectID() + ToolKit.fillSpecificCharacter(" ",6) + Order_Bean.getOrderDate() + ToolKit.fillSpecificCharacter(" ",4) + Order_Bean.getNowOrderNumber() + ToolKit.fillSpecificCharacter(" ",12) + Order_Bean.getAlreadyOrderDate() + ToolKit.fillSpecificCharacter(" ",4) + Order_Bean.getAlreadyOrderNumber();
        }else {
            titlePaneName = "【報價單】   " + Order_Bean.getObjectID() + ToolKit.fillSpecificCharacter(" ",6) + Order_Bean.getOrderDate() + ToolKit.fillSpecificCharacter(" ",4) + Order_Bean.getNowOrderNumber() + ToolKit.fillSpecificCharacter(" ",66);
        }
        titlePaneName = titlePaneName + ToolKit.fillSpecificCharacter(" ",8) + Order_Bean.getProjectName();
        return titlePaneName;
    }
    private BorderPane createBorderPane(Order_Bean Order_Bean){
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        TableView.setEditable(true);
        for(int index = 0 ; index < TableColumns.length ; index++) {
            if(index == 0 || index == 3 || index == 7 || index == 8 || index == 9) {
                TableColumn<OrderProduct_Bean,Integer> TableColumn = ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableColumn.setCellFactory(column -> new setIntegerTableColumnTextFill("CENTER-LEFT", "16"));
                TableView.getColumns().add(TableColumn);
            }else if(index == 5 || index == 6) {
                TableColumn<OrderProduct_Bean,Double> TableColumn = ComponentToolKit.createOrderProductTableDoubleColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableColumn.setCellFactory(column -> new setDoubleTableColumnTextFill("CENTER-LEFT", "16"));
                TableView.getColumns().add(TableColumn);
            }else {
                TableColumn<OrderProduct_Bean,String> TableColumn = ComponentToolKit.createOrderProductTableStringColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableColumn.setCellFactory(column -> new setStringTableColumnTextFill("CENTER-LEFT", "16"));
                TableView.getColumns().add(TableColumn);
            }
        }
        TableView.setPrefSize(1250,480);
        TableView.setOnMouseClicked((MouseEvent MouseEvent) -> {
            if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
                OrderProduct_Bean selectOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
                if(selectOrderProduct_Bean != null &&
                        DialogUI.ConfirmDialog("※ 確定將商品【" + selectOrderProduct_Bean.getISBN() + "】新增到貨單 ?",true,false,0,0)){
                    selectOrderProduct_Bean.setCheckBoxSelect(true);
                    EstablishOrder_Controller.setSelectProductBean(true,ComponentToolKit.getSelectProductList(TableView));
                    selectOrderProduct_Bean.setCheckBoxSelect(false);
                }
            }
        });

        TableView.getItems().addAll(Order_Bean.getProductList());
        BorderPane BorderPane = ComponentToolKit.setBorderPane(5,5,5,5);
        BorderPane.setCenter(TableView);
        return BorderPane;
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
                if(orderProductMap.containsKey(OrderProduct_Bean.getISBN()))
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
                if(getTableColumn().getText().equals("項號")){
                    if(orderProductMap.containsKey(OrderProduct_Bean.getISBN())) {
                        style = style + "-fx-background-color:#15BCBC;";
                        setText("* " + item);
                    }else
                        setText("   " + item);
                }else{
                    setText(String.valueOf(item));
                    if(orderProductMap.containsKey(OrderProduct_Bean.getISBN()))
                        style = style + "-fx-background-color:#15BCBC;";
                }
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
                if(orderProductMap.containsKey(OrderProduct_Bean.getISBN()))
                    style = style + "-fx-background-color:#15BCBC;";
                setStyle(style);
            }
        }
    }
    private String getTextFillColor(OrderSource OrderSource){
        switch (OrderSource) {
            case 報價單:
                return "#4F9D9D";
            case 待出貨單:
                return "#009900";
            case 出貨單:
            case 出貨子貨單:
                return "#FF3300";
        }
        return "";
    }
}
