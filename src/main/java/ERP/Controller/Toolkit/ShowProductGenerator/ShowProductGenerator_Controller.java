package ERP.Controller.Toolkit.ShowProductGenerator;

import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.ToolKit.ProductGenerator.ProductGenerator_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ShowProductGenerator_Controller {
    @FXML TextField ProductQuantity_TextField, SinglePrice_TextField, InputTotalPrice_TextField, DifferentPrice_TextField;
    @FXML Button SuggestSinglePrice_Button;
    @FXML CheckBox removeDuplicateProduct_CheckBox, includeWaitingConfirm_CheckBox;
    @FXML TextField duplicateProductDate_TextField;
    @FXML MenuButton ProductCategory_Button;
    @FXML TableColumn<OrderProduct_Bean, String> SelectColumn, ISBNColumn, ProductNameColumn, PricingColumn, PriceAmountColumn;
    @FXML TableColumn<OrderProduct_Bean, Integer> QuantityColumn;
    @FXML TableColumn<OrderProduct_Bean, Double> SinglePriceColumn;
    @FXML TableView<OrderProduct_Bean> TableView;

    private ToolKit ToolKit;
    private CallConfig CallConfig;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private OrderObject orderObject;
    private String ObjectID;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private Order_Model Order_Model;
    private Stage Stage;
    private ObservableList<ProductCategory_Bean> ProductFirstCategoryList;
    private JSONObject ProductCategoryJsonObject;
    public ShowProductGenerator_Controller(){
        ToolKit = ERPApplication.ToolKit;
        CallConfig = ToolKit.CallConfig;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){
        this.EstablishOrder_Controller=  EstablishOrder_Controller;
    }
    public void setOrderObject(Order_Enum.OrderObject orderObject){
        this.orderObject = orderObject;
    }
    public void setObjectID(String ObjectID){
        this.ObjectID = ObjectID;
    }
    public void setComponent() throws Exception{
        setKeyWordTextLimiter();
        initialTableView();
        ProductCategoryJsonObject = CallConfig.getProductGeneratorJsonConfig();
        setMenuButton_ProductCategory();
    }
    private void setKeyWordTextLimiter(){
        ComponentToolKit.addTextFieldLimitDigital(ProductQuantity_TextField,false);
        ComponentToolKit.addTextFieldLimitDigital(SinglePrice_TextField,false);
        ComponentToolKit.addTextFieldLimitDigital(duplicateProductDate_TextField,false);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(SelectColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        setColumnCellValueAndQuantitySpinner(QuantityColumn,"Quantity", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(SinglePriceColumn,"SinglePrice", "CENTER-LEFT", "16",null);
        SinglePriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ComponentToolKit.setColumnCellValue(PricingColumn,"Pricing", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PriceAmountColumn,"PriceAmount", "CENTER-LEFT", "16",null);
    }

    private void setMenuButton_ProductCategory(){
        ProductFirstCategoryList = Order_Model.getAllFirstCategoryOfProduct(CategoryLayer.大類別);
        for(ProductCategory_Bean ProductCategory_Bean : ProductFirstCategoryList){
            CheckBox CheckBox = ComponentToolKit.initialCheckBox(ProductCategory_Bean.getCategoryName(),16);
            if(ProductCategoryJsonObject.containsKey(ProductCategory_Bean.getCategoryName()))
                ComponentToolKit.setCheckBoxSelect(CheckBox, ProductCategoryJsonObject.getBoolean(ProductCategory_Bean.getCategoryName()));
            else
                ComponentToolKit.setCheckBoxSelect(CheckBox, false);
            setProductCategoryCheckBoxListener(CheckBox, ProductFirstCategoryList);

            CustomMenuItem CustomMenuItem = new CustomMenuItem(CheckBox);
            CustomMenuItem.setHideOnClick(false);
            ProductCategory_Button.getItems().add(CustomMenuItem);
        }
    }
    @FXML protected void ProductQuantityKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            generateProduct();
    }
    @FXML protected void SinglePriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            generateProduct();
    }

    @FXML protected void removeDuplicateProductOnAction(){
        if(!removeDuplicateProduct_CheckBox.isSelected())   duplicateProductDate_TextField.setText("");
        else    duplicateProductDate_TextField.setText("180");
        ComponentToolKit.setTextFieldDisable(duplicateProductDate_TextField, !removeDuplicateProduct_CheckBox.isSelected());
    }

    @FXML protected void generateMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            generateProduct();
    }
    @FXML protected void InputTotalPriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            generateProduct();
    }
    @FXML protected void duplicateProductDateKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            generateProduct();
    }

    private void generateProduct(){
        if (ProductQuantity_TextField.getText().equals("")) {
            DialogUI.MessageDialog("※ 請輸入品項數量");
            return;
        }
        if (SinglePrice_TextField.getText().equals("")) {
            DialogUI.MessageDialog("※ 請輸入單筆含稅金額");
            return;
        }
        if (InputTotalPrice_TextField.getText().equals("")) {
            DialogUI.MessageDialog("※ 請輸入總金額");
            return;
        }
        if (removeDuplicateProduct_CheckBox.isSelected() && duplicateProductDate_TextField.getText().equals("")) {
            DialogUI.MessageDialog("※ 請輸入天數區間");
            return;
        }
        ObservableList<OrderProduct_Bean> SaveProductList = getSaveProduct();
        if(SaveProductList != null && (SaveProductList.size() > Integer.parseInt(ProductQuantity_TextField.getText()))){
            DialogUI.MessageDialog("※ 保留品項大於產生數量");
            return;
        }
        ProductGenerator_Bean ProductGenerator_Bean = new ProductGenerator_Bean();
        ProductGenerator_Bean.setProductQuantity(Integer.parseInt(ProductQuantity_TextField.getText()));
        ProductGenerator_Bean.setSinglePrice(Integer.parseInt(SinglePrice_TextField.getText()));
        ProductGenerator_Bean.setTotalPrice(Integer.parseInt(InputTotalPrice_TextField.getText()));
        ProductGenerator_Bean.setFirstCategoryIDList(getProductFirstCategory());
        ProductGenerator_Bean.setIncludeWaitingConfirmProduct(includeWaitingConfirm_CheckBox.isSelected());
        ProductGenerator_Bean.setRemoveDuplicateProduct(removeDuplicateProduct_CheckBox.isSelected());
        if (removeDuplicateProduct_CheckBox.isSelected())
            ProductGenerator_Bean.setDuplicateProductDate(Integer.parseInt(duplicateProductDate_TextField.getText()));
        else
            ProductGenerator_Bean.setDuplicateProductDate(0);
        ProductGenerator_Bean.setSaveProductList(SaveProductList);

        ObservableList<OrderProduct_Bean> productList = Order_Model.getProductGenerator(ObjectID, ProductGenerator_Bean);
        TableView.getItems().clear();
        if (SaveProductList != null) {
            SaveProductList.addAll(productList);
            autoSetProductQuantity(SaveProductList);
            TableView.setItems(SaveProductList);
        } else if (productList.size() != 0) {
            autoSetProductQuantity(productList);
            TableView.setItems(productList);
        }else
            DialogUI.MessageDialog("※ 查無資料");
        refreshTotalPrice();
    }
    private ObservableList<OrderProduct_Bean> getSaveProduct(){
        ObservableList<OrderProduct_Bean> SaveProductList = null;
        ObservableList<OrderProduct_Bean> OrderProduct = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(OrderProduct_Bean OrderProduct_Bean : OrderProduct){
            if(OrderProduct_Bean.isCheckBoxSelect()){
                if(SaveProductList == null)   SaveProductList = FXCollections.observableArrayList();
                SaveProductList.add(OrderProduct_Bean);
            }
        }
        return SaveProductList;
    }
    private ArrayList<String> getProductFirstCategory(){
        ArrayList<String> FirstCategoryIDList = null;
        for(ProductCategory_Bean ProductCategory_Bean: ProductFirstCategoryList){
            if(ProductCategoryJsonObject.containsKey(ProductCategory_Bean.getCategoryName()) &&
                    ProductCategoryJsonObject.getBoolean(ProductCategory_Bean.getCategoryName())){
                if(FirstCategoryIDList == null) FirstCategoryIDList = new ArrayList<>();
                FirstCategoryIDList.add(ProductCategory_Bean.getCategoryID());
            }
        }
        return FirstCategoryIDList;
    }
    private void autoSetProductQuantity(ObservableList<OrderProduct_Bean> ProductList){
        int InputTotalPrice = (int)Math.round(Double.parseDouble(InputTotalPrice_TextField.getText())/1.05);
        for(int index = 0; index < ProductList.size(); index++){
            OrderProduct_Bean OrderProduct_Bean = ProductList.get(index);
            if(index == ProductList.size()-1) {
                if(InputTotalPrice <= 0)    InputTotalPrice = (int) ProductList.get(index).getSinglePrice();
                int Quantity = InputTotalPrice / (int) ProductList.get(index).getSinglePrice();
                if(Quantity == 0)   Quantity = 1;
                OrderProduct_Bean.setQuantity(Quantity);
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
            }
            InputTotalPrice = InputTotalPrice - ProductList.get(index).getPriceAmount();
        }
    }
    private void refreshTotalPrice(){
        double TotalPrice = 0;
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(OrderProduct_Bean OrderProduct_Bean : ProductList)
            TotalPrice = TotalPrice + OrderProduct_Bean.getPriceAmount();
        double DifferentPrice = TotalPrice*1.05 - Double.parseDouble(InputTotalPrice_TextField.getText());
        if(DifferentPrice >= 0) DifferentPrice_TextField.setStyle("-fx-text-fill: blue; -fx-background-color:  #bebebe;");
        else    DifferentPrice_TextField.setStyle("-fx-text-fill: red; -fx-background-color:  #bebebe;");
        DifferentPrice_TextField.setText(ToolKit.RoundingString(DifferentPrice));
    }
    @FXML protected void confirmMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
            productList = handleCheckStoreProduct(productList);
            EstablishOrder_Controller.setSelectProductBean(true, productList);
            Stage.close();
        }
    }
    private ObservableList<OrderProduct_Bean> handleCheckStoreProduct(ObservableList<OrderProduct_Bean> productList){
        ObservableList<OrderProduct_Bean> newProductList;
        HashMap<String, OrderProduct_Bean> checkStoreMap = null;
        for(OrderProduct_Bean orderProduct_bean : productList){
            if(orderProduct_bean.getISBN().equals("")){
                if(checkStoreMap == null){
                    checkStoreMap = new HashMap<>();
                }
                checkStoreMap.put(orderProduct_bean.getProductCode(), orderProduct_bean);
            }
        }
        if(checkStoreMap != null){
            newProductList = FXCollections.observableArrayList();
            for(String productCode : checkStoreMap.keySet()){
                String productName = checkStoreMap.get(productCode).getProductName();
                double singlePrice = checkStoreMap.get(productCode).getSinglePrice();
                OrderProduct_Bean orderProduct_bean = Order_Model.insertCheckStoreToStore(orderObject, ObjectID, productCode, singlePrice);
                if(orderProduct_bean == null){
                    ERPApplication.Logger.error("商品產生器【store 新增異常】" + productCode + " - " + productName);
                    DialogUI.AlarmDialog("【新增異常】" + productCode);
                }else{
                    newProductList.add(orderProduct_bean);
                }
            }
            for(OrderProduct_Bean orderProduct_bean : productList){
                if(!checkStoreMap.containsKey(orderProduct_bean.getProductCode())){
                    newProductList.add(orderProduct_bean);
                }
            }
            return newProductList;
        }else{
            return productList;
        }
    }
      @FXML protected void SinglePriceEditCommit(TableColumn.CellEditEvent<OrderProduct_Bean, Double> KeyEvent){
          OrderProduct_Bean OrderProduct_Bean = KeyEvent.getRowValue();
          refreshProductPrice(OrderProduct_Bean, KeyEvent.getNewValue());
    }
    @FXML protected void suggestSinglePriceMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
            refreshProductPrice(OrderProduct_Bean,Double.parseDouble(SuggestSinglePrice_Button.getText()));
        }
    }
    private void refreshProductPrice(OrderProduct_Bean OrderProduct_Bean, double singlePrice){
        OrderProduct_Bean.setSinglePrice(singlePrice);
        OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
        refreshTotalPrice();
    }
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
            if (OrderProduct_Bean != null) {
                ComponentToolKit.setButtonDisable(SuggestSinglePrice_Button, false);
                int DifferentPrice = Integer.parseInt(DifferentPrice_TextField.getText());
                if (DifferentPrice <= 0) {
                    SuggestSinglePrice_Button.setText(ToolKit.RoundingString(true, (double) -DifferentPrice / 1.05 / (double) OrderProduct_Bean.getQuantity() + OrderProduct_Bean.getSinglePrice()));
                } else {
                    double SuggestSinglePrice = (double) DifferentPrice / 1.05 / (double) OrderProduct_Bean.getQuantity();
                    if (SuggestSinglePrice <= OrderProduct_Bean.getSinglePrice())
                        SuggestSinglePrice = OrderProduct_Bean.getSinglePrice() - SuggestSinglePrice;
                    else
                        SuggestSinglePrice = OrderProduct_Bean.getSinglePrice();
                    SuggestSinglePrice_Button.setText(ToolKit.RoundingString(true, SuggestSinglePrice));
                }
            }else
                ComponentToolKit.setButtonDisable(SuggestSinglePrice_Button,true);
        }
    }
    private void setColumnCellValueAndCheckBox(TableColumn<OrderProduct_Bean, String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn, ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, String> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                setGraphic(getTableView().getItems().get(getIndex()).getCheckBox());
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }else   setGraphic(null);
        }
    }
    private void setColumnCellValueAndQuantitySpinner(TableColumn<OrderProduct_Bean, Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn, ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndQuantitySpinner(Alignment, FontSize));
    }
    private class setColumnCellValueAndQuantitySpinner extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        Spinner<Integer> QuantitySpinner;
        setColumnCellValueAndQuantitySpinner(String Alignment, String FontSize) {
            this.Alignment = Alignment;
            this.FontSize = FontSize;
            QuantitySpinner = new Spinner<>();
            QuantitySpinner.setEditable(true);
            ComponentToolKit.setIntegerSpinnerValueFactory(QuantitySpinner, 1, 999, 1, 1);
            QuantitySpinner.setOnKeyReleased(KeyEvent -> {
                if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
                    OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView, getIndex());
                    OrderProduct_Bean.setQuantity(QuantitySpinner.getValueFactory().getValue());
                    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
                    autoSetProductQuantity(ComponentToolKit.getOrderProductTableViewItemList(TableView));
                    refreshTotalPrice();
                }
            });
            QuantitySpinner.setOnMouseClicked(e -> {
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView, getIndex());
                OrderProduct_Bean.setQuantity(QuantitySpinner.getValueFactory().getValue());
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
                autoSetProductQuantity(ComponentToolKit.getOrderProductTableViewItemList(TableView));
                refreshTotalPrice();
            });
        }
        @Override
        protected void updateItem(Integer Quantity, boolean empty) {
            super.updateItem(Quantity, empty);
            if(!empty){
                setGraphic(QuantitySpinner);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                if(getIndex() == ComponentToolKit.getOrderProductTableViewItemList(TableView).size()-1)
                    QuantitySpinner.setEditable(false);
               else
                    QuantitySpinner.setEditable(true);
               ComponentToolKit.setSpinnerIntegerValue(QuantitySpinner, Quantity);
            }else   setGraphic(null);
        }
    }
    private void setProductCategoryCheckBoxListener(CheckBox CheckBox, ObservableList<ProductCategory_Bean> ProductFirstCategoryList){
        CheckBox.selectedProperty().addListener((ov, oldValue, newValue) -> {
            try{
                ProductCategoryJsonObject = createJsonObjectBuilder(CheckBox, ProductFirstCategoryList);
                saveProductCategory(ProductCategoryJsonObject);
            }catch (Exception Ex){
                Ex.printStackTrace();
            }
        });
    }
    private JSONObject createJsonObjectBuilder(CheckBox CheckBox, ObservableList<ProductCategory_Bean> ProductFirstCategoryList){
        JSONObject JSONObject = new JSONObject(new LinkedHashMap<>());
        for(ProductCategory_Bean ProductCategory_Bean : ProductFirstCategoryList){
            if(CheckBox.getId().equals(ProductCategory_Bean.getCategoryName()))
                JSONObject.put(ProductCategory_Bean.getCategoryName(), CheckBox.isSelected());
            else{
                if(ProductCategoryJsonObject.containsKey(ProductCategory_Bean.getCategoryName()))
                    JSONObject.put(ProductCategory_Bean.getCategoryName(), ProductCategoryJsonObject.getBoolean(ProductCategory_Bean.getCategoryName()));
                else
                    JSONObject.put(ProductCategory_Bean.getCategoryName(), false);
            }

        }
        return JSONObject;
    }
    private void saveProductCategory(JSONObject ProductCategoryJsonObject) throws Exception{
        CallConfig.setProductGeneratorJsonConfig(Product_Enum.CategoryLayer.大類別, ProductCategoryJsonObject);
    }
}
