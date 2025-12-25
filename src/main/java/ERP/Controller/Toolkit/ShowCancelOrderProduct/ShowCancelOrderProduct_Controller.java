package ERP.Controller.Toolkit.ShowCancelOrderProduct;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ShowCancelOrderProduct_Controller {
    @FXML TableColumn<OrderProduct_Bean, CheckBox> SelectColumn_canCancel;
    @FXML TableColumn<OrderProduct_Bean, String> ItemNumberColumn_canCancel, ISBNColumn_canCancel, ProductNameColumn_canCancel, QuantityColumn_canCancel, UnitColumn_canCancel, BatchPriceColumn_canCancel, PricingColumn_canCancel, PriceAmountColumn_canCancel, InStockColumn_canCancel, SafetyStockColumn_canCancel;
    @FXML TableColumn<OrderProduct_Bean, String> SeriesNumberColumn_wantCancel, ISBNColumn_wantCancel, ProductNameColumn_wantCancel, UnitColumn_wantCancel, BatchPriceColumn_wantCancel, PricingColumn_wantCancel, PriceAmountColumn_wantCancel, InStockColumn_wantCancel, SafetyStockColumn_wantCancel;
    @FXML TableColumn<OrderProduct_Bean, Integer> QuantityColumn_wantCancel;
    @FXML TableView<OrderProduct_Bean> canCancelTableView, wantCancelTableView;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private Order_Model Order_Model;
    private Stage Stage,MainStage;
    private Order_Bean Order_Bean;
    public ShowCancelOrderProduct_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }

    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setMainStage(Stage MainStage){ this.MainStage = MainStage; }
    public void setOrder_Bean(Order_Bean Order_Bean){    this.Order_Bean = Order_Bean;   }
    public void setTableViewItems(ObservableList<OrderProduct_Bean> canCancelOrderProductList) throws Exception{
        for(OrderProduct_Bean OrderProduct_Bean : canCancelOrderProductList) {
        OrderProduct_Bean.setCheckBoxSelect(false);
        if(OrderProduct_Bean.getQuantity() == 0)    OrderProduct_Bean.setCheckBoxDisable(true);
        else    OrderProduct_Bean.setCheckBoxDisable(false);
            canCancelTableView.getItems().add(ToolKit.CopyProductBean(OrderProduct_Bean));
    }}
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){  this.EstablishOrder_Controller = EstablishOrder_Controller; }
    public void setComponent(){
        initialTableView();
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(SelectColumn_canCancel,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ItemNumberColumn_canCancel,"ItemNumber","CENTER-LEFT","16",null);
        setColumnCellValue(ISBNColumn_canCancel, ISBNColumn_wantCancel,"ISBN","CENTER-LEFT","16");
        setColumnCellValue(ProductNameColumn_canCancel, ProductNameColumn_wantCancel,"ProductName","CENTER-LEFT","16");
        ComponentToolKit.setColumnCellValue(QuantityColumn_canCancel,"Quantity","CENTER-LEFT","16",null);
        setColumnCellValue(UnitColumn_canCancel, UnitColumn_wantCancel,"Unit","CENTER-LEFT","16");
        setColumnCellValue(BatchPriceColumn_canCancel, BatchPriceColumn_wantCancel,"BatchPrice","CENTER-LEFT","16");
        setColumnCellValue(PricingColumn_canCancel, PricingColumn_wantCancel,"Pricing","CENTER-LEFT","16");
        setColumnCellValue(PriceAmountColumn_canCancel, PriceAmountColumn_wantCancel,"PriceAmount","CENTER-LEFT","16");
        setColumnCellValue(InStockColumn_canCancel, InStockColumn_wantCancel,"InStock","CENTER-LEFT","16");
        setColumnCellValue(SafetyStockColumn_canCancel, SafetyStockColumn_wantCancel,"SafetyStock","CENTER-LEFT","16");
        ComponentToolKit.setColumnCellValue(SeriesNumberColumn_wantCancel,"SeriesNumber","CENTER-LEFT","16",null);
        setColumnCellIntegerValueAndSpinner(QuantityColumn_wantCancel,"Quantity","CENTER-LEFT","16");
    }
    @FXML protected void canCancelTableViewKeyReleased(KeyEvent KeyEvent) throws Exception{
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
            ObservableList<OrderProduct_Bean> selectProductList = ComponentToolKit.getSelectProductList(canCancelTableView);
            if(selectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else    importSelectProductIntoTableView(selectProductList);
        }else if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(canCancelTableView);
            if(OrderProduct_Bean.isCheckBoxSelect())  OrderProduct_Bean.setCheckBoxSelect(false);
            else if(!OrderProduct_Bean.isCheckBoxDisable() && !OrderProduct_Bean.isCheckBoxSelect())  OrderProduct_Bean.setCheckBoxSelect(true);
        }
    }
    @FXML protected void importProductMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<OrderProduct_Bean> selectProductList = ComponentToolKit.getSelectProductList(canCancelTableView);
            if(selectProductList.size() == 0)
                DialogUI.MessageDialog("※ 請選擇商品");
            else
                importSelectProductIntoTableView(selectProductList);
        }
    }
    private void importSelectProductIntoTableView(ObservableList<OrderProduct_Bean> selectProductList) throws Exception{
        for(OrderProduct_Bean OrderProduct_Bean : selectProductList){
            ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView).add(ToolKit.CopyProductBean(OrderProduct_Bean));
            OrderProduct_Bean.setCheckBoxSelect(false);
            OrderProduct_Bean.setCheckBoxDisable(true);
        }
        refreshProductSeriesNumber(wantCancelTableView);
    }
    @FXML protected void deleteProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            OrderProduct_Bean selectOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(wantCancelTableView);
            if(selectOrderProduct_Bean == null)
                DialogUI.MessageDialog("※ 請選擇商品");
            else{
                ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView).remove(selectOrderProduct_Bean);
                ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(canCancelTableView);
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(OrderProduct_Bean.getISBN().equals(selectOrderProduct_Bean.getISBN()) && OrderProduct_Bean.getItemNumber() == selectOrderProduct_Bean.getItemNumber()){
                        OrderProduct_Bean.setCheckBoxDisable(false);
                        break;
                    }
                }
                refreshProductSeriesNumber(wantCancelTableView);
            }
        }
    }
    @FXML protected void importAllProductMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            deleteAllProduct();
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(canCancelTableView);
            for(OrderProduct_Bean OrderProduct_Bean : productList){
                ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView).addAll(ToolKit.CopyProductBean(OrderProduct_Bean));
                OrderProduct_Bean.setCheckBoxSelect(false);
                OrderProduct_Bean.setCheckBoxDisable(true);
            }
            refreshProductSeriesNumber(wantCancelTableView);
        }
    }
    @FXML protected void deleteAllProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            deleteAllProduct();
        }
    }
    private void deleteAllProduct(){
        ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView).clear();
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(canCancelTableView);
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            OrderProduct_Bean.setCheckBoxDisable(false);
        }
    }
    @FXML protected void cancelOrderProductMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.ConfirmDialog("※ 確定註銷 ?",false,false,0,0)){
                ObservableList<OrderProduct_Bean> wantCancelProductList = ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView);
                ObservableList<OrderProduct_Bean> newProductList = generateNewProductList(wantCancelProductList);
                if(wantCancelProductList.size() == 0)   DialogUI.MessageDialog("※ 請選擇欲註銷商品");
                else if(newProductList.size() == 0) DialogUI.MessageDialog("※ 註銷失敗，註銷後項目個數為0");
                else{
                    Order_Bean Order_Bean = ToolKit.CopyOrderBean(this.Order_Bean);
                    if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單)  Order_Bean.setOrderSource(Order_Enum.OrderSource.採購單);
                    else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單) Order_Bean.setOrderSource(Order_Enum.OrderSource.報價單);
                    Order_Bean.setIsCheckout(this.Order_Bean.isCheckout().value());
                    Order_Bean.setIsOffset(this.Order_Bean.getOffsetOrderStatus());
                    Order_Bean.setIsBorrowed(this.Order_Bean.isBorrowed().value());

                    Order_Bean.setProductList(newProductList);
                    Order_Bean.setNumberOfItems(String.valueOf(newProductList.size()));
                    refreshOrderInfo(Order_Bean);
                    //  修改貨單、修改銷售數量、重開貨單
                    if(Order_Model.modifyOrder(Order_Bean,wantCancelProductList,true,false)) {
                        if(Order_Bean.getOrderSource().getOrderObject() == Order_Enum.OrderObject.廠商)
                            ERPApplication.Logger.info("※ [成功] 註銷「待入倉單 : " + Order_Bean.getWaitingOrderNumber() + "(" + Order_Bean.getNowOrderNumber() + ")」");
                        else if(Order_Bean.getOrderSource().getOrderObject() == Order_Enum.OrderObject.客戶)
                            ERPApplication.Logger.info("※ [成功] 註銷「待出貨單 : " + Order_Bean.getWaitingOrderNumber() + "(" + Order_Bean.getNowOrderNumber() + ")」");
                        ERPApplication.Logger.info("-----------------------------------------------------------------------------------------");
                        DialogUI.MessageDialog("※ 註銷成功，請重新開啟此貨單!");
                        ComponentToolKit.closeThisStage(Stage);
                        ComponentToolKit.closeThisStage(MainStage);
                        EstablishOrder_Controller.refreshSearchOrderView();
                    }else{
                        if(Order_Bean.getOrderSource().getOrderObject() == Order_Enum.OrderObject.廠商)
                            ERPApplication.Logger.warn("※ [失敗] 註銷「待入倉單 : " + Order_Bean.getWaitingOrderNumber() + "(" + Order_Bean.getNowOrderNumber() + ")」");
                        else if(Order_Bean.getOrderSource().getOrderObject() == Order_Enum.OrderObject.客戶)
                            ERPApplication.Logger.warn("※ [失敗] 註銷「待出貨單 : " + Order_Bean.getWaitingOrderNumber() + "(" + Order_Bean.getNowOrderNumber() + ")」");
                        ERPApplication.Logger.info("-----------------------------------------------------------------------------------------");
                        DialogUI.MessageDialog("※ 註銷失敗!");
                    }
                }
            }
        }
    }
    private ObservableList<OrderProduct_Bean> generateNewProductList(ObservableList<OrderProduct_Bean> wantCancelProductList) throws Exception{
        ObservableList<OrderProduct_Bean> originProductList = Order_Bean.getProductList();
        ObservableList<OrderProduct_Bean> newProductList = FXCollections.observableArrayList();
        for(OrderProduct_Bean OrderProduct_Bean : originProductList)
            newProductList.add(ToolKit.CopyProductBean(OrderProduct_Bean));

        for(OrderProduct_Bean wantCancelProduct_Bean : wantCancelProductList){
            for(OrderProduct_Bean originProduct_Bean : newProductList){
                if(originProduct_Bean.getItemNumber() == wantCancelProduct_Bean.getItemNumber() && originProduct_Bean.getISBN().equals(wantCancelProduct_Bean.getISBN())){
                    if(originProduct_Bean.getQuantity() == wantCancelProduct_Bean.getQuantity())
                        newProductList.remove(originProduct_Bean);
                    else {
                        originProduct_Bean.setQuantity(originProduct_Bean.getQuantity() - wantCancelProduct_Bean.getQuantity());
                        if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單)
                            originProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(originProduct_Bean.getQuantity()*originProduct_Bean.getBatchPrice()));
                        else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單)
                            originProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(originProduct_Bean.getQuantity()*originProduct_Bean.getSinglePrice()));
                    }
                    break;
                }
            }
        }
        return newProductList;
    }
    private void refreshOrderInfo(Order_Bean Order_Bean){
        int discount = Integer.parseInt(Order_Bean.getDiscount());
        int tax;
        int totalPriceNoneTax = Integer.parseInt(Order_Model.calculateOrderTotalPrice_NoneTax(false, Order_Bean.getProductList()));
        int totalPriceIncludeTax = Order_Model.calculateOrderTotalPrice_IncludeTax(Order_Bean.getOrderTaxStatus(), totalPriceNoneTax);
        if (totalPriceNoneTax > 0) {
            tax = totalPriceIncludeTax - totalPriceNoneTax;
            totalPriceIncludeTax = totalPriceIncludeTax - discount;
        } else {
            totalPriceIncludeTax = 0;
            tax = 0;
        }
        Order_Bean.setTotalPriceNoneTax(String.valueOf(totalPriceNoneTax));
        Order_Bean.setTax(String.valueOf(tax));
        Order_Bean.setTotalPriceIncludeTax(String.valueOf(totalPriceIncludeTax));
        if(!Order_Bean.getProjectTotalPriceNoneTax().equals(""))
            Order_Bean.setProjectDifferentPrice(String.valueOf(Integer.parseInt(Order_Bean.getProjectTotalPriceIncludeTax()) - totalPriceIncludeTax));
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.Stage.close();
        }
    }
    private void refreshProductSeriesNumber(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> OrderProductList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(int index = 0 ; index < OrderProductList.size() ; index++){
            OrderProduct_Bean OrderProduct_Bean = OrderProductList.get(index);
            OrderProduct_Bean.setSeriesNumber(index+1);
        }
    }
    private void setColumnCellValue(TableColumn<OrderProduct_Bean, String> TableColumn1,TableColumn<OrderProduct_Bean, String> TableColumn2,String ColumnValue,String Alignment,String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn1,ColumnValue,Alignment,FontSize,null);
        ComponentToolKit.setColumnCellValue(TableColumn2,ColumnValue,Alignment,FontSize,null);
    }
    private void setColumnCellValueAndCheckBox(TableColumn<OrderProduct_Bean,CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, CheckBox> {
        private String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBox, boolean empty) {
            super.updateItem(CheckBox, empty);
            if(!empty){
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                CheckBox SelectCheckBox = ComponentToolKit.getOrderProductTableViewSelectItem(canCancelTableView,getIndex()).getCheckBox();
                setGraphic(SelectCheckBox);
                SelectCheckBox.disabledProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue)    getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-background-color: #B9B9B9");
                    else    getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";");
                });
            }else   setGraphic(null);
        }
    }
    private void setColumnCellIntegerValueAndSpinner(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setCellFactory(column -> new setColumnCellIntegerValueAndSpinner(Alignment,FontSize));
    }
    private class setColumnCellIntegerValueAndSpinner extends TableCell<OrderProduct_Bean, Integer> {
        private String Alignment,FontSize;
        Spinner<Integer> QuantitySpinner;
        setColumnCellIntegerValueAndSpinner(String Alignment,String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
            QuantitySpinner = new Spinner<>();
            QuantitySpinner.setEditable(true);

            ComponentToolKit.setIntegerSpinnerValueFactory(QuantitySpinner,1, 999, 1, 1);
            QuantitySpinner.setOnKeyReleased(KeyEvent -> {
                OrderProduct_Bean selectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView).get(getIndex());
                ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(canCancelTableView);
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(OrderProduct_Bean.getItemNumber() == selectProduct_Bean.getItemNumber() && OrderProduct_Bean.getISBN().equals(selectProduct_Bean.getISBN())){
                        if(QuantitySpinner.getValueFactory().getValue() > OrderProduct_Bean.getQuantity())    //  超出範圍
                            QuantitySpinner.getValueFactory().setValue(selectProduct_Bean.getQuantity());
                        else
                            selectProduct_Bean.setQuantity(QuantitySpinner.getValueFactory().getValue());
                        break;
                    }
                }
            });

            QuantitySpinner.setOnMouseClicked(event -> {
                OrderProduct_Bean selectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(wantCancelTableView).get(getIndex());
                ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(canCancelTableView);
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(OrderProduct_Bean.getItemNumber() == selectProduct_Bean.getItemNumber() && OrderProduct_Bean.getISBN().equals(selectProduct_Bean.getISBN())){
                        if(QuantitySpinner.getValueFactory().getValue() > OrderProduct_Bean.getQuantity())    //  超出範圍
                            QuantitySpinner.getValueFactory().setValue(selectProduct_Bean.getQuantity());
                        else
                            selectProduct_Bean.setQuantity(QuantitySpinner.getValueFactory().getValue());
                        break;
                    }
                }
            });
        }
        @Override
        protected void updateItem(Integer Quantity, boolean empty) {
            super.updateItem(Quantity, empty);
            if (empty)  setGraphic(null);
            else{
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                ComponentToolKit.setSpinnerIntegerValue(QuantitySpinner, Quantity);
                setGraphic(QuantitySpinner);
            }
        }
    }
}
