package ERP.Controller.Toolkit.ShowProductGroup;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowProductGroup_Controller {
    @FXML HBox insertProductGroup_HBox, modifyProductGroup_HBox;
    @FXML Label insertProductGroupSwitch_Label, modifyProductGroupSwitch_Label;
    @FXML Spinner<Integer> groupIndexSpinner, insertProductGroup_smallQuantitySpinner, modifyProductGroup_smallQuantitySpinner;
    @FXML TextField insertProductGroup_NameText, insertProductGroup_QuantityText, insertProductGroup_UnitText, insertProductGroup_SinglePriceText, insertProductGroup_SmallSinglePriceText, insertProductGroup_TotalPriceText, insertProductGroup_SmallTotalPriceText;
    @FXML TextField modifyProductGroup_NameText, modifyProductGroup_QuantityText, modifyProductGroup_UnitText, modifyProductGroup_SinglePriceText, modifyProductGroup_SmallSinglePriceText, modifyProductGroup_TotalPriceText, modifyProductGroup_SmallTotalPriceText;
    @FXML Label singlePrice_originDifferentLabel, totalPrice_originDifferentLabel, singlePrice_smallDifferentLabel, totalPrice_smallDifferentLabel;
    @FXML Button importProduct_Button,deleteProduct_Button,insertGroup_Button,modifyGroup_Button,deleteGroup_Button,establishOrderProductGroup_Button,modifyOrderProductGroup_Button;

    @FXML CheckBox SelectAll_CheckBox;
    @FXML TableView<OrderProduct_Bean> MainTableView;
    @FXML TableColumn<OrderProduct_Bean, CheckBox> SelectColumn;
    @FXML TableColumn<OrderProduct_Bean, Integer> QuantityColumn;
    @FXML TableColumn<OrderProduct_Bean, String> ItemNumberColumn, ISBNColumn, ProductNameColumn, UnitColumn, BatchPricePriceColumn, PricingColumn, PriceAmountColumn, InStockColumn, SafetyStockColumn;
    @FXML TabPane TabPane;

    private final String[] TableColumns = { "項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量", "安全存量"};
    private final String[] TableColumnCellValue = new String[]{"SeriesNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock","SafetyStock"};
    private final int[] ColumnsLength = { 45, 130, 450, 100, 50, 80, 80, 80, 50, 80};

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private  EstablishOrder_Controller EstablishOrder_Controller;
    private Order_Model Order_Model;

    private Order_Bean Order_Bean;
    public ArrayList<HashMap<ProductGroup_Bean, TableView<OrderProduct_Bean>>> ProductGroupTableViewList = new ArrayList<>();
    private Stage Stage;

    private InsertProductGroup_ToggleSwitch insertProductGroup_ToggleSwitch = null;
    public ModifyProductGroup_ToggleSwitch modifyProductGroup_ToggleSwitch = null;
    public ShowProductGroup_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;

        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }

    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){
        this.EstablishOrder_Controller = EstablishOrder_Controller;
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setOrder_Bean(Order_Bean Order_Bean) throws Exception {
        this.Order_Bean = Order_Bean;
        setTableViewItems(Order_Bean.getProductList(), Order_Bean.getProductGroupMap());
        ComponentToolKit.setButtonDisable(establishOrderProductGroup_Button,Order_Bean.getProductGroupMap() != null);
        ComponentToolKit.setButtonDisable(modifyOrderProductGroup_Button,Order_Bean.getProductGroupMap() == null);
        if(Order_Bean.getProductGroupMap() != null){
            setModifyProductGroupTextFieldEditable(true,true);
            setProductGroup(Order_Bean.getProductList(), Order_Bean.getProductGroupMap());
        }
        setButtonDisable(Order_Bean.getProductGroupMap() == null);
    }
    private void setTableViewItems(ObservableList<OrderProduct_Bean> mainProductList, HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap) throws Exception {
        for(OrderProduct_Bean OrderProduct_Bean : mainProductList) {
            OrderProduct_Bean copyOrderProduct_Bean = ToolKit.CopyProductBean(OrderProduct_Bean);
            copyOrderProduct_Bean.setCheckBoxSelect(false);
            if(copyOrderProduct_Bean.getQuantity() == 0)
                copyOrderProduct_Bean.setCheckBoxDisable(true);
            else {
                copyOrderProduct_Bean.setCheckBoxDisable(false);
            }
            MainTableView.getItems().add(copyOrderProduct_Bean);
        }
        if(productGroupMap != null){
            mainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            for(Integer group_itemNumber : productGroupMap.keySet()){
                ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                for(Integer item_id : itemMap.keySet()){
                    ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                    for(OrderProduct_Bean OrderProduct_Bean : mainProductList) {
                        if(item_id.equals(OrderProduct_Bean.getItem_id())){
                            OrderProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity()-ItemGroup_Bean.getItem_quantity()*ProductGroup_Bean.getQuantity());
                            if(OrderProduct_Bean.getQuantity() == 0)
                                OrderProduct_Bean.setCheckBoxDisable(true);
                            else
                                OrderProduct_Bean.setCheckBoxDisable(false);
                            break;
                        }
                    }
                }
            }
        }
    }
    private void setProductGroup(ObservableList<OrderProduct_Bean> productList, HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap) throws Exception {
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            HashMap<Integer,ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
            ObservableList<OrderProduct_Bean> selectProductList = FXCollections.observableArrayList();
            for(Integer item_id : itemMap.keySet()){
                ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                for(OrderProduct_Bean OrderProduct_Bean : productList) {
                    if(item_id.equals(OrderProduct_Bean.getItem_id())){
                        OrderProduct_Bean copyOrderProduct_Bean = ToolKit.CopyProductBean(OrderProduct_Bean);
                        copyOrderProduct_Bean.setQuantity(ItemGroup_Bean.getItem_quantity());
                        copyOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(copyOrderProduct_Bean.getQuantity()*copyOrderProduct_Bean.getSinglePrice()));
                        selectProductList.add(copyOrderProduct_Bean);
                        break;
                    }
                }
            }
            ProductGroup_Bean copyProductGroup_Bean = ToolKit.CopyProductGroupBean(ProductGroup_Bean);
            createProductGroupTab(false,copyProductGroup_Bean, selectProductList);
        }
    }
    public void setComponent(){
        setSpinnerLimitDigital();

        insertProductGroup_ToggleSwitch = createInsertProductGroupSwitchButton();
        modifyProductGroup_ToggleSwitch = createModifyProductGroupSwitchButton();

        setTextFieldLimitDigital();
        initialTableView();
        setDifferentPriceTextFill();
        setInsertProductGroupTextFieldEditable(true,false);
        setModifyProductGroupTextFieldEditable(true,false);
    }
    private void setSpinnerLimitDigital(){
        ComponentToolKit.setIntegerSpinnerValueFactory(groupIndexSpinner,0,100,0,1);
        ComponentToolKit.setIntegerSpinnerValueFactory(insertProductGroup_smallQuantitySpinner,0,100,0,1);
        ComponentToolKit.setIntegerSpinnerValueFactory(modifyProductGroup_smallQuantitySpinner,0,100,0,1);
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(insertProductGroup_smallQuantitySpinner.getEditor(), false);
        ComponentToolKit.addTextFieldLimitDigital(insertProductGroup_QuantityText, false);
        ComponentToolKit.addTextFieldLimitDouble(insertProductGroup_SinglePriceText);
        ComponentToolKit.addTextFieldLimitDouble(insertProductGroup_SmallSinglePriceText);
        ComponentToolKit.addTextFieldLimitDigital(insertProductGroup_TotalPriceText, false);
        ComponentToolKit.addTextFieldLimitDigital(insertProductGroup_SmallTotalPriceText, false);

        ComponentToolKit.addTextFieldLimitDigital(groupIndexSpinner.getEditor(), false);
        ComponentToolKit.addTextFieldLimitDigital(modifyProductGroup_smallQuantitySpinner.getEditor(), false);
        ComponentToolKit.addTextFieldLimitDigital(modifyProductGroup_QuantityText, false);
        ComponentToolKit.addTextFieldLimitDouble(modifyProductGroup_SinglePriceText);
        ComponentToolKit.addTextFieldLimitDouble(modifyProductGroup_SmallSinglePriceText);
        ComponentToolKit.addTextFieldLimitDigital(modifyProductGroup_TotalPriceText, false);
        ComponentToolKit.addTextFieldLimitDigital(modifyProductGroup_SmallTotalPriceText, false);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(SelectColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ItemNumberColumn,"ItemNumber","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        ProductNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        setColumnCellIntegerValue(QuantityColumn,"Quantity", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(BatchPricePriceColumn,"BatchPrice", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PricingColumn,"Pricing", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PriceAmountColumn,"PriceAmount", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InStockColumn,"InStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SafetyStockColumn,"SafetyStock", "CENTER-LEFT", "16",null);
    }
    private void setDifferentPriceTextFill(){
        singlePrice_originDifferentLabel.textProperty().addListener((ov, oldValue, newValue) -> {
            String differentSinglePrice = singlePrice_originDifferentLabel.getText();
            if(!differentSinglePrice.equals("")){
                if(Double.parseDouble(differentSinglePrice) >= 0)
                    singlePrice_originDifferentLabel.setStyle("-fx-text-fill: #0089EB");
                else
                    singlePrice_originDifferentLabel.setStyle("-fx-text-fill: red");
            }
        });
        totalPrice_originDifferentLabel.textProperty().addListener((ov, oldValue, newValue) -> {
            String differentTotalPrice = totalPrice_originDifferentLabel.getText();
            if(!differentTotalPrice.equals("")){
                if(Integer.parseInt(differentTotalPrice) >= 0)
                    totalPrice_originDifferentLabel.setStyle("-fx-text-fill: #0089EB");
                else
                    totalPrice_originDifferentLabel.setStyle("-fx-text-fill: red");
            }
        });
        singlePrice_smallDifferentLabel.textProperty().addListener((ov, oldValue, newValue) -> {
            String differentSinglePrice = singlePrice_smallDifferentLabel.getText();
            if(!differentSinglePrice.equals("")){
                if(Double.parseDouble(differentSinglePrice) >= 0)
                    singlePrice_smallDifferentLabel.setStyle("-fx-text-fill: #0089EB");
                else
                    singlePrice_smallDifferentLabel.setStyle("-fx-text-fill: red");
            }
        });
        totalPrice_smallDifferentLabel.textProperty().addListener((ov, oldValue, newValue) -> {
            String differentTotalPrice = totalPrice_smallDifferentLabel.getText();
            if(!differentTotalPrice.equals("")){
                if(Integer.parseInt(differentTotalPrice) >= 0)
                    totalPrice_smallDifferentLabel.setStyle("-fx-text-fill: #0089EB");
                else
                    totalPrice_smallDifferentLabel.setStyle("-fx-text-fill: red");
            }
        });
    }
    @FXML protected void SelectAllOnAction(){
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        if(productList.size() != 0){
            for(OrderProduct_Bean OrderProduct_Bean : productList){
                if(!OrderProduct_Bean.isCheckBoxDisable())
                    OrderProduct_Bean.setCheckBoxSelect(SelectAll_CheckBox.isSelected());
            }
            setInsertProductGroupInfo();
        }
    }
    @FXML protected void InsertProductGroupNameKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            insertProductGroup_UnitText.requestFocus();
        }
    }
    @FXML protected void InsertProductGroupUnitKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) {
            if (insertProductGroup_ToggleSwitch.switchedOnProperty().get()) {
                insertProductGroup_QuantityText.requestFocus();
            }else {
                insertProductGroup_SinglePriceText.requestFocus();
            }
        }
    }
    @FXML protected void InsertProductGroupQuantityKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(insertProductGroup_QuantityText.getText() == null || insertProductGroup_QuantityText.getText().equals("")){
                DialogUI.AlarmDialog("請輸入正確數量");
            }else {
                int insertProductQuantity = Integer.parseInt(insertProductGroup_QuantityText.getText());
                if(insertProductQuantity == 0){
                    DialogUI.MessageDialog("數量需大於「0」");
                }else{
                    calculateInsertGroupPriceAmount();
                    insertProductGroup_SinglePriceText.requestFocus();
                }
            }
        }
    }
    @FXML protected void InsertProductGroupQuantityKeyReleased(){
        if(ComponentToolKit.getSelectProductList(MainTableView).size() != 0) {
            if(insertProductGroup_QuantityText.getText().equals("0")) {
                DialogUI.MessageDialog("※ 群組數量不允許「0」");
                insertProductGroup_QuantityText.setText("1");
            }
            calculateInsertGroupPriceAmount();
        }
    }
    @FXML protected void InsertProductGroupSmallQuantityKeyReleased(KeyEvent KeyEvent){
        String smallQuantity = insertProductGroup_smallQuantitySpinner.getEditor().getText();
        if(smallQuantity != null && !smallQuantity.equals("")){
            insertProductGroup_smallQuantitySpinner.getValueFactory().setValue(Integer.parseInt(smallQuantity));
            if(KeyPressed.isDigitalKeyPressed(KeyEvent)){
                insertProductGroup_smallQuantitySpinner.getEditor().positionCaret(smallQuantity.length()+1);
            }
            int newSmallQuantity = insertProductGroup_smallQuantitySpinner.getValueFactory().getValue();
            insertProductGroupSmallQuantitySpinnerEvent(newSmallQuantity);
        }else{
            insertProductGroup_smallQuantitySpinner.getEditor().setText("0");
            insertProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            insertProductGroup_SmallSinglePriceText.setText("");
        }
        calculateInsertSmallGroupPriceAmount();
    }
    @FXML protected void InsertProductGroupSmallQuantityMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            Integer smallQuantity = insertProductGroup_smallQuantitySpinner.getValueFactory().getValue();
            if(smallQuantity == null){
                insertProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
                smallQuantity = 0;
            }
            insertProductGroupSmallQuantitySpinnerEvent(smallQuantity);
            calculateInsertSmallGroupPriceAmount();
        }
    }
    private void insertProductGroupSmallQuantitySpinnerEvent(int smallQuantity){
        if(smallQuantity == 0){
            insertProductGroup_SmallSinglePriceText.setText("");
        }else {
            String singlePrice = insertProductGroup_SinglePriceText.getText();
            if(singlePrice == null || singlePrice.equals("")){
                ObservableList<OrderProduct_Bean> selectProductList = ComponentToolKit.getSelectProductList(MainTableView);
                calculateInsertProductGroupSinglePrice(selectProductList, insertProductGroup_ToggleSwitch.switchedOnProperty().get());
            }
            refreshInsertProductGroupSmallSinglePrice();
        }
    }
    private void refreshInsertProductGroupSmallSinglePrice(){
        Integer smallQuantity = insertProductGroup_smallQuantitySpinner.getValueFactory().getValue();
        if(smallQuantity != null && smallQuantity != 0){
            insertProductGroup_SmallSinglePriceText.setText(ToolKit.RoundingString(true,(ToolKit.RoundingDouble(insertProductGroup_SinglePriceText.getText())/smallQuantity)));
            calculateInsertSmallGroupPriceAmount();
        }
    }
    @FXML protected void InsertProductGroupSinglePriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(insertProductGroup_SinglePriceText.getText() == null || insertProductGroup_SinglePriceText.getText().equals("")){
                DialogUI.AlarmDialog("請輸入正確金額");
            }else {
                if(insertProductGroup_SmallSinglePriceText.isEditable()) {
                    insertProductGroup_SmallSinglePriceText.requestFocus();
                }else {
                    insertProductGroup_NameText.requestFocus();
                }
            }
        }
    }
    @FXML protected void InsertProductGroupSmallSinglePriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            int smallQuantity = insertProductGroup_smallQuantitySpinner.getValueFactory().getValue();
            String smallSinglePrice = insertProductGroup_SmallSinglePriceText.getText();
            if(smallQuantity != 0 && (smallSinglePrice == null || smallSinglePrice.equals(""))){
                DialogUI.AlarmDialog("請輸入正確金額");
            }else {
                insertProductGroup_NameText.requestFocus();
            }
        }
    }
    @FXML protected void InsertProductGroupSinglePriceKeyReleased(){
        if(ComponentToolKit.getSelectProductList(MainTableView).size() != 0){
            calculateInsertGroupPriceAmount();
            refreshInsertProductGroupSmallSinglePrice();
        }
    }
    @FXML protected void InsertProductGroupSmallSinglePriceKeyReleased(){
        if(ComponentToolKit.getSelectProductList(MainTableView).size() != 0){
            calculateInsertSmallGroupPriceAmount();
        }
    }
    /** TableView Key Release - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent) {
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
            insertGroup();
        }else if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
            OrderProduct_Bean thisOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(thisOrderProduct_Bean.isCheckBoxSelect()) {
                thisOrderProduct_Bean.setCheckBoxSelect(false);
            }else if(!thisOrderProduct_Bean.isCheckBoxDisable() && !thisOrderProduct_Bean.isCheckBoxSelect()) {
                thisOrderProduct_Bean.setCheckBoxSelect(true);
            }
            calculateInsertProductGroupSinglePrice(ComponentToolKit.getSelectProductList(MainTableView),
                            insertProductGroup_ToggleSwitch.switchedOnProperty().get());
        }
    }
    /** Button Mouse Clicked - 自動編群(剩餘商品) */
    @FXML protected void AutoGroupMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try{
                boolean isExistLessProduct = false;
                ObservableList<OrderProduct_Bean> mainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                for(OrderProduct_Bean OrderProduct_Bean : mainProductList){
                    if(!OrderProduct_Bean.isCheckBoxSelect() && !OrderProduct_Bean.isCheckBoxDisable()) {
                        isExistLessProduct = true;
                        OrderProduct_Bean.setCheckBoxSelect(true);
                        ProductGroup_Bean ProductGroup_Bean = new ProductGroup_Bean();
                        ProductGroup_Bean.setItemNumber((ProductGroupTableViewList.size()+1));
                        ProductGroup_Bean.setGroupName(OrderProduct_Bean.getProductName());
                        ProductGroup_Bean.setQuantity(OrderProduct_Bean.getQuantity());
                        ProductGroup_Bean.setUnit(OrderProduct_Bean.getUnit());
                        ProductGroup_Bean.setBatchPrice(ToolKit.RoundingDouble(OrderProduct_Bean.getBatchPrice()));
                        ProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(OrderProduct_Bean.getSinglePrice()));
                        ProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(OrderProduct_Bean.getSinglePrice()));
                        ProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getSinglePrice()*OrderProduct_Bean.getQuantity()));
                        ProductGroup_Bean.setCombineGroup(true);
                        createGroupTab(ProductGroup_Bean,FXCollections.observableArrayList(OrderProduct_Bean));
                    }
                }
                if(!isExistLessProduct)
                    DialogUI.MessageDialog("※ 無剩餘商品");
            }catch (Exception Ex){
                DialogUI.ExceptionDialog(Ex);
                ERPApplication.Logger.catching(Ex);
            }
        }
    }
    /** Button Mouse Clicked - 匯入 */
    @FXML protected void ImportProductMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ProductGroupTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> selectProductList = ComponentToolKit.getSelectProductList(MainTableView);
            if(selectProductList.size() == 0) {
                DialogUI.MessageDialog("※ 請選擇商品");
                return;
            }
            ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
            if(selectProductGroup_Bean == null){
                DialogUI.MessageDialog("※ 不存在商品群組");
                return;
            }else if(selectProductGroup_Bean.isCombineGroup()){
                if(isSelectedProductQuantityNotEnoughToGroup(selectProductGroup_Bean.getQuantity(), selectProductList))
                    return;
            }
            try{
                ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
                for(OrderProduct_Bean MainOrderProduct_Bean : selectProductList){
                    if(MainOrderProduct_Bean.getItemNumber() < TabProductList.get(0).getItemNumber()) {
                        addImportProduct(selectProductGroup_Bean, TabProductList,0, MainOrderProduct_Bean);
                    }else if(MainOrderProduct_Bean.getItemNumber() > TabProductList.get(TabProductList.size()-1).getItemNumber()) {
                        addImportProduct(selectProductGroup_Bean, TabProductList, TabProductList.size(), MainOrderProduct_Bean);
                    }else {
                        if(!isTabTableViewExistProduct(selectProductGroup_Bean, MainOrderProduct_Bean, TabProductList)){
                            for (int index = 0; index < TabProductList.size(); index++) {
                                OrderProduct_Bean TabProductBean = TabProductList.get(index);
                                if (MainOrderProduct_Bean.getItemNumber() < TabProductBean.getItemNumber()) {
                                    addImportProduct(selectProductGroup_Bean, TabProductList, index, MainOrderProduct_Bean);
                                    break;
                                }else if(index == TabProductList.size()-1)
                                    addImportProduct(selectProductGroup_Bean, TabProductList, index+1, MainOrderProduct_Bean);
                            }
                        }
                    }
                }
                refreshMainTableViewByImportProduct(selectProductGroup_Bean,selectProductList);
                refreshPurchaseQuotationTableView();
                refreshProductSeriesNumber(getSelectTabProductTableView());
                ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
                setInsertProductGroupTextFieldEditable(true,false);
            }catch (Exception Ex){
                DialogUI.ExceptionDialog(Ex);
                ERPApplication.Logger.catching(Ex);
            }
        }
    }
    private void addImportProduct(ProductGroup_Bean selectProductGroup_Bean, ObservableList<OrderProduct_Bean> TabProductList, int addPosition, OrderProduct_Bean MainOrderProduct_Bean) throws Exception {
        OrderProduct_Bean copyOrderProduct_Bean = generateImportProductBean(selectProductGroup_Bean, MainOrderProduct_Bean);
        TabProductList.add(addPosition, copyOrderProduct_Bean);

        selectProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getSinglePrice() + copyOrderProduct_Bean.getPriceAmount()));
        selectProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice()+copyOrderProduct_Bean.getPriceAmount()));
        selectProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(selectProductGroup_Bean.getSinglePrice()*selectProductGroup_Bean.getQuantity()));
        groupIndexSpinner.getValueFactory().setValue(selectProductGroup_Bean.getItemNumber());
        modifyProductGroup_NameText.setText(selectProductGroup_Bean.getGroupName());
        modifyProductGroup_QuantityText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getQuantity()));
        modifyProductGroup_UnitText.setText(selectProductGroup_Bean.getUnit());
        modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
        modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getPriceAmount()));
        setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
        refreshModifyProductGroupSmallSinglePrice();
    }
    private OrderProduct_Bean generateImportProductBean(ProductGroup_Bean ProductGroup_Bean, OrderProduct_Bean MainOrderProduct_Bean) throws Exception {
        OrderProduct_Bean copyOrderProduct_Bean = ToolKit.CopyProductBean(MainOrderProduct_Bean);
        if(ProductGroup_Bean.isCombineGroup())
            copyOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()/ProductGroup_Bean.getQuantity());
        copyOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(copyOrderProduct_Bean.getQuantity()*copyOrderProduct_Bean.getSinglePrice()));
        return copyOrderProduct_Bean;
    }

    private boolean isTabTableViewExistProduct(ProductGroup_Bean selectProductGroup_Bean, OrderProduct_Bean MainOrderProduct_Bean, ObservableList<OrderProduct_Bean> TabProductList){
        for (OrderProduct_Bean TabProductBean : TabProductList) {
            if (MainOrderProduct_Bean.getItemNumber() == TabProductBean.getItemNumber()) {
                int originalProductPriceAmount = TabProductBean.getPriceAmount();
                if(selectProductGroup_Bean.isCombineGroup())
                    TabProductBean.setQuantity(TabProductBean.getQuantity() + MainOrderProduct_Bean.getQuantity()/selectProductGroup_Bean.getQuantity());
                else
                    TabProductBean.setQuantity(TabProductBean.getQuantity() + MainOrderProduct_Bean.getQuantity());
                TabProductBean.setPriceAmount(ToolKit.RoundingInteger(TabProductBean.getQuantity()*TabProductBean.getSinglePrice()));

                selectProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice() - originalProductPriceAmount + TabProductBean.getPriceAmount()));
                selectProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getSinglePrice() - originalProductPriceAmount + TabProductBean.getPriceAmount()));
                selectProductGroup_Bean.setPriceAmount(selectProductGroup_Bean.getPriceAmount() -
                        originalProductPriceAmount*selectProductGroup_Bean.getQuantity() +
                        TabProductBean.getPriceAmount()*selectProductGroup_Bean.getQuantity());
                modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
                modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getPriceAmount()));
                setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
                return true;
            }
        }
        return false;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ProductGroupTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(getSelectTabProductTableView());
            if(OrderProduct_Bean == null)   DialogUI.MessageDialog("※ 請選擇商品");
            else{
                ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
                for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                    MainOrderProduct_Bean.setCheckBoxSelect(false);
                    if(MainOrderProduct_Bean.getItemNumber() == OrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(OrderProduct_Bean.getISBN())){
                        ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
                        if(selectProductGroup_Bean == null){
                            DialogUI.MessageDialog("※ 不存在商品群組");
                            return;
                        }
                        MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()+OrderProduct_Bean.getQuantity()*selectProductGroup_Bean.getQuantity());
                        TabProductList.remove(OrderProduct_Bean);

                        selectProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getSinglePrice() - OrderProduct_Bean.getPriceAmount()));
                        selectProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice()-OrderProduct_Bean.getPriceAmount()));
                        selectProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(selectProductGroup_Bean.getSinglePrice()*selectProductGroup_Bean.getQuantity()));
                        modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
                        modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getPriceAmount()));
                        setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
                        refreshModifyProductGroupSmallSinglePrice();

                        MainOrderProduct_Bean.setCheckBoxDisable(false);
                        refreshProductSeriesNumber(getSelectTabProductTableView());
                    }
                }
                if(TabProductList.size() == 0)  deleteTab();
                if(ProductGroupTableViewList.size() == 0) {
                    setModifyProductGroupTextFieldEditable(true,false);
                }
                setButtonDisable(ProductGroupTableViewList.size() == 0);
            }
        }
    }
    @FXML protected void GroupIndexKeyReleased(KeyEvent KeyEvent){
        ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) {
            if(selectProductGroup_Bean == null){
                DialogUI.MessageDialog("※ 不存在商品群組");
                return;
            }
            int selectTabIndex = ComponentToolKit.getTabPaneSelectTabIndex(TabPane);
            int groupIndex = groupIndexSpinner.getValue();
            if (groupIndex > ProductGroupTableViewList.size()) {
                DialogUI.MessageDialog("※ 超出頁籤範圍");
                groupIndexSpinner.getValueFactory().setValue(selectTabIndex+1);
            } else if (groupIndex == 0) {
                DialogUI.MessageDialog("※ 頁籤範圍不能為0");
                groupIndexSpinner.getValueFactory().setValue(selectTabIndex+1);
            } else {
                selectProductGroup_Bean.setItemNumber(groupIndex);
                moveTab(selectTabIndex, selectProductGroup_Bean);
            }
        }
    }
    @FXML protected void GroupIndexMouseClicked(MouseEvent MouseEvent){
        ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
        if(selectProductGroup_Bean == null){
            DialogUI.MessageDialog("※ 不存在商品群組");
            groupIndexSpinner.getValueFactory().setValue(0);
            return;
        }
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            int Discount = groupIndexSpinner.getValueFactory().getValue() - selectProductGroup_Bean.getItemNumber();
            int selectTabIndex = ComponentToolKit.getTabPaneSelectTabIndex(TabPane);
            if(Discount == 1) {
                if(selectProductGroup_Bean.getItemNumber() != ProductGroupTableViewList.size()){
                    selectProductGroup_Bean.setItemNumber(selectProductGroup_Bean.getItemNumber()+1);
                    moveTab(selectTabIndex, selectProductGroup_Bean);
                }else
                    groupIndexSpinner.getValueFactory().setValue(selectProductGroup_Bean.getItemNumber());
            }else if(Discount == -1) {
                if(selectProductGroup_Bean.getItemNumber() != 1){
                    selectProductGroup_Bean.setItemNumber(selectProductGroup_Bean.getItemNumber()-1);
                    moveTab(selectTabIndex, selectProductGroup_Bean);
                }else
                    groupIndexSpinner.getValueFactory().setValue(selectProductGroup_Bean.getItemNumber());
            }else
                groupIndexSpinner.getValueFactory().setValue(selectProductGroup_Bean.getItemNumber());
        }else{
            groupIndexSpinner.getValueFactory().setValue(selectProductGroup_Bean.getItemNumber());
        }
    }
    private void moveTab(int selectTabIndex, ProductGroup_Bean ProductGroup_Bean){
        Tab selectTab = ComponentToolKit.getTabPaneSelectTab(TabPane);
        TabPane.getTabs().remove(selectTabIndex);
        TabPane.getTabs().add(ProductGroup_Bean.getItemNumber()-1,selectTab);
        TabPane.getSelectionModel().select(selectTab);

        HashMap<ProductGroup_Bean, TableView<OrderProduct_Bean>> productGroupMap = ProductGroupTableViewList.get(selectTabIndex);
        ProductGroupTableViewList.remove(selectTabIndex);
        ProductGroupTableViewList.add(ProductGroup_Bean.getItemNumber()-1,productGroupMap);
        TabRename();
    }
    @FXML protected void ModifyProductGroupNameKeyReleased(){
        ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave()? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
    }
    @FXML protected void ModifyProductGroupNameKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
            if(ComponentToolKit.getSelectProductList(MainTableView).size() == 0 && selectProductGroup_Bean != null){
                String groupName = modifyProductGroup_NameText.getText();
                if(groupName == null || groupName.equals("")) {
                    DialogUI.MessageDialog("※ 請輸入群組名稱");
                }else{
                    if(modifyGroup()){
                        modifyProductGroup_UnitText.requestFocus();
                    }
                }
            }else
                modifyProductGroup_UnitText.requestFocus();
        }
    }
    @FXML protected void ModifyProductGroupUnitKeyReleased(){
        ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave()? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
    }
    @FXML protected void ModifyProductGroupUnitKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) {
            ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
            if(ComponentToolKit.getSelectProductList(MainTableView).size() == 0 && selectProductGroup_Bean != null){
                String groupUnit = modifyProductGroup_UnitText.getText();
                if(groupUnit == null || groupUnit.equals("")) {
                    DialogUI.MessageDialog("※ 請輸入群組單位");
                }else{
                    if(modifyGroup()){
                        if(modifyProductGroup_QuantityText.isEditable()){
                            modifyProductGroup_QuantityText.requestFocus();
                        }else{
                            modifyProductGroup_SinglePriceText.requestFocus();
                        }
                    }
                }
            }else
                modifyProductGroup_QuantityText.requestFocus();
        }
    }
    @FXML protected void ModifyProductGroupQuantityKeyReleased(){
        ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
        if(selectProductGroup_Bean != null){
            if(modifyProductGroup_QuantityText.getText().equals("0")) {
                DialogUI.MessageDialog("※ 群組數量不允許「0」");
                modifyProductGroup_QuantityText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getQuantity()));
            }else{
                calculateModifyGroupPriceAmount();
            }
        }
        ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave()? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
    }
    @FXML protected void ModifyProductGroupQuantityKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(modifyProductGroup_QuantityText.getText() == null || modifyProductGroup_QuantityText.getText().equals("")){
                DialogUI.AlarmDialog("請輸入正確數量");
            }else {
                int insertProductQuantity = Integer.parseInt(modifyProductGroup_QuantityText.getText());
                if(insertProductQuantity == 0){
                    DialogUI.MessageDialog("數量需大於「0」");
                }else{
                    if(modifyGroup()){
                        modifyProductGroup_SinglePriceText.requestFocus();
                    }
                }
            }
        }
    }
    @FXML protected void ModifyProductGroupSmallQuantityKeyReleased(KeyEvent KeyEvent){
        String smallQuantity = modifyProductGroup_smallQuantitySpinner.getEditor().getText();
        if(smallQuantity != null && !smallQuantity.equals("")){
            modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(Integer.parseInt(smallQuantity));
            if(KeyPressed.isDigitalKeyPressed(KeyEvent)){
                modifyProductGroup_smallQuantitySpinner.getEditor().positionCaret(smallQuantity.length()+1);
            }
            int newSmallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
            modifyProductGroupSmallQuantitySpinnerEvent(newSmallQuantity);
        }else{
            modifyProductGroup_smallQuantitySpinner.getEditor().setText("0");
            modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            modifyProductGroup_SmallSinglePriceText.setText("");
        }
        calculateModifySmallGroupPriceAmount();
        ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave()? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
    }
    @FXML protected void ModifyProductGroupSmallQuantityMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            Integer smallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
            if(smallQuantity == null){
                modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
                smallQuantity = 0;
            }
            modifyProductGroupSmallQuantitySpinnerEvent(smallQuantity);
            ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave()? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
        }
    }
    private void modifyProductGroupSmallQuantitySpinnerEvent(int smallQuantity){
        if(smallQuantity == 0){
            modifyProductGroup_SmallSinglePriceText.setText("");
        }else {
            refreshModifyProductGroupSmallSinglePrice();
        }
    }
    private void refreshModifyProductGroupSmallSinglePrice(){
        Integer smallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
        if(smallQuantity != null && smallQuantity != 0){
            modifyProductGroup_SmallSinglePriceText.setText(ToolKit.RoundingString(true, (ToolKit.RoundingDouble(modifyProductGroup_SinglePriceText.getText()) / smallQuantity)));
            calculateModifySmallGroupPriceAmount();
            setCalculateSmallDifferentSinglePriceAndTotalPrice();
        }
    }
    @FXML protected void ModifyProductGroupSinglePriceKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            return;
        }
        if(getSelectTabProductGroupBean() != null) {
            calculateModifyGroupPriceAmount();
            refreshModifyProductGroupSmallSinglePrice();
        }
        ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave() ? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
    }
    @FXML protected void ModifyProductGroupSinglePriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String smallSinglePrice = modifyProductGroup_SinglePriceText.getText();
            if(smallSinglePrice == null || smallSinglePrice.equals("")){
                DialogUI.AlarmDialog("請輸入正確金額");
            }else {
                if(modifyGroup()){
                    if(modifyProductGroup_SmallSinglePriceText.isEditable()){
                        modifyProductGroup_SmallSinglePriceText.requestFocus();
                    }else {
                        modifyProductGroup_NameText.requestFocus();
                    }
                }
            }
        }
    }
    @FXML protected void ModifyProductGroupSmallSinglePriceKeyReleased(){
        if(getSelectTabProductGroupBean() != null){
            calculateModifySmallGroupPriceAmount();
            setCalculateSmallDifferentSinglePriceAndTotalPrice();
        }
        ComponentToolKit.setButtonStyle(modifyGroup_Button, isPreviousGroupModifiedAndNeededSave() ? "-fx-background-color:" + ToolKit.getPink_BackgroundColor() : "");
    }
    @FXML protected void ModifyProductGroupSmallSinglePriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            int smallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
            String smallSinglePrice = modifyProductGroup_SmallSinglePriceText.getText();
            if(smallQuantity != 0 && (smallSinglePrice == null || smallSinglePrice.equals(""))){
                DialogUI.AlarmDialog("請輸入正確金額");
            }else{
                if(modifyGroup()){
                    if(smallQuantity == 0 && smallSinglePrice.equals("0")){
                        modifyProductGroup_SmallSinglePriceText.setText("");
                    }
                    modifyProductGroup_NameText.requestFocus();
                }
            }
        }
    }
    @FXML protected void InsertGroupKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            insertGroup();
    }
    /** Button Mouse Clicked - 新增商品群組 */
    @FXML protected void InsertGroupMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            insertGroup();
    }
    private void insertGroup(){
        ObservableList<OrderProduct_Bean> selectProductList = ComponentToolKit.getSelectProductList(MainTableView);
        if(selectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
        else if(insertProductGroup_NameText.getText() == null || insertProductGroup_NameText.getText().equals("")){
            DialogUI.MessageDialog("※ 請輸入群組名稱");
            insertProductGroup_NameText.requestFocus();
        }else if(insertProductGroup_QuantityText.getText() == null || insertProductGroup_QuantityText.getText().equals("")){
            DialogUI.MessageDialog("※ 請輸入數量");
            insertProductGroup_QuantityText.requestFocus();
        }else if(insertProductGroup_UnitText.getText() == null || insertProductGroup_UnitText.getText().equals("")){
            DialogUI.MessageDialog("※ 請輸入單位");
            insertProductGroup_UnitText.requestFocus();
        }else if(insertProductGroup_SinglePriceText.getText() == null || insertProductGroup_SinglePriceText.getText().equals("")){
            DialogUI.MessageDialog("※ 請輸入單價");
            insertProductGroup_SinglePriceText.requestFocus();
        }else if(insertProductGroup_smallQuantitySpinner.getValueFactory().getValue() != 0 && insertProductGroup_SmallSinglePriceText.getText().equals("")){
            DialogUI.MessageDialog("※ 請輸入【非組合】細分單價");
            insertProductGroup_SmallSinglePriceText.requestFocus();
        }else if(!insertProductGroup_SmallSinglePriceText.getText().equals("") && insertProductGroup_smallQuantitySpinner.getValueFactory().getValue() == 0){
            DialogUI.MessageDialog("※ 請輸入【非組合】細分數量");
            insertProductGroup_smallQuantitySpinner.requestFocus();
        }else {
            if(insertProductGroup_ToggleSwitch.switchedOnProperty().get()){    //  組合群組：判斷數量是否超出所有品項
                int productGroupQuantity = ToolKit.RoundingInteger(insertProductGroup_QuantityText.getText());
                if(isSelectedProductQuantityNotEnoughToGroup(productGroupQuantity, selectProductList))
                    return;
            }
            try {
                ProductGroup_Bean ProductGroup_Bean = new ProductGroup_Bean();
                ProductGroup_Bean.setItemNumber((ProductGroupTableViewList.size()+1));
                ProductGroup_Bean.setGroupName(insertProductGroup_NameText.getText());
                ProductGroup_Bean.setQuantity(Integer.parseInt(insertProductGroup_QuantityText.getText()));
                ProductGroup_Bean.setUnit(insertProductGroup_UnitText.getText());
                ProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(insertProductGroup_SinglePriceText.getText()));
                ProductGroup_Bean.setOriginalSinglePrice(calculateProductGroupOriginalPriceAmount(insertProductGroup_ToggleSwitch.switchedOnProperty().get(),
                        ComponentToolKit.getSelectProductList(MainTableView)));
                ProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(insertProductGroup_TotalPriceText.getText()));
                ProductGroup_Bean.setCombineGroup(insertProductGroup_ToggleSwitch.switchedOnProperty().get());
                ProductGroup_Bean.setSmallQuantity(ProductGroup_Bean.isCombineGroup() ? null : insertProductGroup_smallQuantitySpinner.getValueFactory().getValue());
                ProductGroup_Bean.setSmallSinglePrice(insertProductGroup_SmallSinglePriceText.getText().equals("") ?
                        null : ToolKit.RoundingDouble(insertProductGroup_SmallSinglePriceText.getText()));
                ProductGroup_Bean.setSmallPriceAmount(insertProductGroup_SmallTotalPriceText.getText().equals("") ?
                        null : ToolKit.RoundingInteger(insertProductGroup_SmallTotalPriceText.getText()));
                createGroupTab(ProductGroup_Bean,selectProductList);
                setMainProductSelectedFalse();
                setInsertProductGroupTextFieldEditable(true,false);
            }catch (Exception Ex){
                DialogUI.ExceptionDialog(Ex);
                ERPApplication.Logger.catching(Ex);
            }
        }
    }
    private boolean isSelectedProductQuantityNotEnoughToGroup(int productGroupQuantity, ObservableList<OrderProduct_Bean> selectProductList){
        boolean status = false;
        for(OrderProduct_Bean OrderProduct_Bean : selectProductList){
            if(OrderProduct_Bean.isCheckBoxSelect() && OrderProduct_Bean.getQuantity() < productGroupQuantity){
                DialogUI.MessageDialog("※ 【組合】數量超出商品數量範圍");
                status = true;
                insertProductGroup_QuantityText.requestFocus();
                break;
            }
        }
        return status;
    }
    private void createGroupTab(ProductGroup_Bean selectProductGroup_Bean, ObservableList<OrderProduct_Bean> selectProductList) throws Exception {
        createProductGroupTab(true, selectProductGroup_Bean, selectProductList);
        setButtonDisable(false);
    }
    private ProductGroup_Bean selectProductGroup_Bean;
    private void createProductGroupTab(boolean refreshMainTableView, ProductGroup_Bean selectProductGroup_Bean, ObservableList<OrderProduct_Bean> selectProductList) throws Exception{
        TableView<OrderProduct_Bean> TableView = createTableView();
        for(OrderProduct_Bean OrderProduct_Bean : selectProductList){
            OrderProduct_Bean CopyBean = ToolKit.CopyProductBean(OrderProduct_Bean);
            if(refreshMainTableView && selectProductGroup_Bean.isCombineGroup()){
                CopyBean.setQuantity(1);
                CopyBean.setPriceAmount(ToolKit.RoundingInteger(CopyBean.getSinglePrice()));
            }else
                CopyBean.setPriceAmount(ToolKit.RoundingInteger(CopyBean.getQuantity()*CopyBean.getSinglePrice()));
            ComponentToolKit.getOrderProductTableViewItemList(TableView).add(CopyBean);
        }
        if(ComponentToolKit.getTableViewItemsSize(TableView) > 0){
            ProductGroupTableViewList.add(new HashMap<ProductGroup_Bean, TableView<OrderProduct_Bean>>(){{put(selectProductGroup_Bean, TableView);}});
            Tab Tab = ComponentToolKit.setTab("(" + ProductGroupTableViewList.size() + ") " + selectProductGroup_Bean.getGroupName());
            Tab.setOnSelectionChanged(event -> {
                if(Tab.isSelected()){
                    if(isPreviousGroupModifiedAndNeededSave()) {
                        if(modifyGroup()){
                            DialogUI.MessageDialog("修改完成");
                        }
                    }
                    setMainProductSelectedFalse();
                    setModifyProductGroupInfo(selectProductGroup_Bean);
                    this.selectProductGroup_Bean = selectProductGroup_Bean;
                }
            });
            BorderPane BorderPane = ComponentToolKit.setBorderPane(10,10,0,10);
            BorderPane.setCenter(TableView);
            Tab.setContent(BorderPane);
            TabPane.getTabs().add(Tab);
            TabPane.getSelectionModel().select(Tab);
            refreshProductSeriesNumber(TableView);
        }
        if(refreshMainTableView)
            refreshMainTableViewByInsertGroup(selectProductGroup_Bean, selectProductList);
    }
    public void setModifyProductGroupInfo(ProductGroup_Bean selectProductGroup_Bean){
        setModifyProductGroupTextFieldEditable(false,true);

        groupIndexSpinner.getValueFactory().setValue(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)+1);
        modifyProductGroup_NameText.setText(selectProductGroup_Bean.getGroupName());
        modifyProductGroup_UnitText.setText(selectProductGroup_Bean.getUnit());
        modifyProductGroup_ToggleSwitch.switchedOnProperty().set(selectProductGroup_Bean.isCombineGroup());
        modifyProductGroup_QuantityText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getQuantity()));
        modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
        modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getPriceAmount()));
        setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
        modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(selectProductGroup_Bean.isCombineGroup() || selectProductGroup_Bean.getSmallQuantity() == null ?
                0 : selectProductGroup_Bean.getSmallQuantity());
        modifyProductGroup_SmallSinglePriceText.setText(selectProductGroup_Bean.getSmallSinglePrice() == null ? "" : ToolKit.RoundingString(true,selectProductGroup_Bean.getSmallSinglePrice()));
        modifyProductGroup_SmallTotalPriceText.setText(selectProductGroup_Bean.getSmallPriceAmount() == null ? "" : ToolKit.RoundingString(selectProductGroup_Bean.getSmallPriceAmount()));
        setCalculateSmallDifferentSinglePriceAndTotalPrice();
    }
    private TableView<OrderProduct_Bean> createTableView(){
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        for(int index = 0 ; index < TableColumns.length ; index++) {
            TableColumn<OrderProduct_Bean,Integer> TableColumn = ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]);
            if(index == 3)  TableColumn.setCellFactory(p -> new createSpinner());
            TableView.getColumns().add(TableColumn);
        }
        TableView.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)){
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
                if(OrderProduct_Bean != null){
                    ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                        if(OrderProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber()){
                            MainTableView.getSelectionModel().select(MainOrderProduct_Bean);
                            break;
                        }
                    }
                }
            }
        });
        return TableView;
    }
    private class createSpinner extends TableCell<OrderProduct_Bean, Integer> {
        Spinner<Integer> QuantitySpinner;
        createSpinner() {
            QuantitySpinner = new Spinner<>();
            QuantitySpinner.setEditable(true);
            QuantitySpinner.setEditable(true);
            ComponentToolKit.addTextFieldLimitDigital(QuantitySpinner.getEditor(),false);
            ComponentToolKit.setIntegerSpinnerValueFactory(QuantitySpinner,1, 9999, 1, 1);
            AtomicInteger oldQuantity = new AtomicInteger();
            QuantitySpinner.setOnKeyReleased(KeyEvent -> {
                ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
                if(selectProductGroup_Bean == null){
                    DialogUI.MessageDialog("※ 不存在商品群組");
                    return;
                }
                if(!KeyPressed.isEnterKeyPressed(KeyEvent)){
                    oldQuantity.set(QuantitySpinner.getValueFactory().getValue());
                }else if(KeyPressed.isEnterKeyPressed(KeyEvent)){
                    if(oldQuantity.get() == QuantitySpinner.getValueFactory().getValue())
                        return;
                    if(modifyProductGroup_QuantityText.getText() == null || modifyProductGroup_QuantityText.getText().equals("")) {
                        QuantitySpinner.getValueFactory().setValue(oldQuantity.get());
                        DialogUI.MessageDialog("※ 請輸入商品群組數量");
                        return;
                    }
                    ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    OrderProduct_Bean SelectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView()).get(getIndex());
                    for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                        if(SelectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(SelectProduct_Bean.getISBN())){
                            int totalQuantity = MainOrderProduct_Bean.getQuantity() + SelectProduct_Bean.getQuantity()*Integer.parseInt(modifyProductGroup_QuantityText.getText());
                            int newQuantity = QuantitySpinner.getValueFactory().getValue()*Integer.parseInt(modifyProductGroup_QuantityText.getText());
                            if(newQuantity > totalQuantity || (newQuantity == totalQuantity && MainOrderProduct_Bean.getQuantity() == 0))
                                QuantitySpinner.getValueFactory().setValue(SelectProduct_Bean.getQuantity());
                            else{
                                totalQuantity = totalQuantity - newQuantity;
                                MainOrderProduct_Bean.setQuantity(totalQuantity);
                                MainOrderProduct_Bean.setCheckBoxSelect(false);

                                SelectProduct_Bean.setQuantity(QuantitySpinner.getValueFactory().getValue());
                                SelectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(SelectProduct_Bean.getQuantity()*SelectProduct_Bean.getSinglePrice()));

                                selectProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble((selectProductGroup_Bean.getSinglePrice() - SelectProduct_Bean.getSinglePrice()*oldQuantity.get()) + SelectProduct_Bean.getPriceAmount()));
                                selectProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice()-SelectProduct_Bean.getSinglePrice()*oldQuantity.get() + SelectProduct_Bean.getPriceAmount()));
                                selectProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(selectProductGroup_Bean.getSinglePrice()*Integer.parseInt(modifyProductGroup_QuantityText.getText())));
                                modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
                                modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getPriceAmount()));
                                setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
                                refreshModifyProductGroupSmallSinglePrice();

                                MainOrderProduct_Bean.setCheckBoxDisable(totalQuantity == 0);
                            }
                            oldQuantity.set(QuantitySpinner.getValueFactory().getValue());
                            break;
                        }
                    }
                }
            });

            QuantitySpinner.setOnMouseClicked(event -> {
                ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
                if(selectProductGroup_Bean == null){
                    DialogUI.MessageDialog("※ 不存在商品群組");
                    return;
                }
                if(modifyProductGroup_QuantityText.getText() == null || modifyProductGroup_QuantityText.getText().equals("")) {
                    QuantitySpinner.getValueFactory().setValue(oldQuantity.get());
                    DialogUI.MessageDialog("※ 請輸入商品群組數量");
                    return;
                }
                ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                OrderProduct_Bean SelectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView()).get(getIndex());
                int Discount = QuantitySpinner.getValueFactory().getValue() - SelectProduct_Bean.getQuantity();
                for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                    if(SelectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(SelectProduct_Bean.getISBN())){
                        if(!modifyGroup()){
                            return;
                        }
                        if((MainOrderProduct_Bean.getQuantity() - Integer.parseInt(modifyProductGroup_QuantityText.getText()) < 0 && Discount == 1) || Discount == 0)
                            QuantitySpinner.getValueFactory().setValue(SelectProduct_Bean.getQuantity());
                        else{
                            if(Discount == 1)   SelectProduct_Bean.setQuantity(SelectProduct_Bean.getQuantity()+1);
                            else if(Discount == -1) SelectProduct_Bean.setQuantity(SelectProduct_Bean.getQuantity() - 1);
                            SelectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(SelectProduct_Bean.getQuantity()*SelectProduct_Bean.getSinglePrice()));
                            MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()-Discount*Integer.parseInt(modifyProductGroup_QuantityText.getText()));
                            MainOrderProduct_Bean.setCheckBoxSelect(false);

                            if(Discount == 1){
                                selectProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getSinglePrice() + SelectProduct_Bean.getSinglePrice()));
                                selectProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice() + SelectProduct_Bean.getSinglePrice()));
                            }else if(Discount == -1){
                                double singlePrice = ToolKit.RoundingDouble(selectProductGroup_Bean.getSinglePrice() - SelectProduct_Bean.getSinglePrice());
                                selectProductGroup_Bean.setSinglePrice(Math.max(ToolKit.RoundingDouble(singlePrice), 0));
                                selectProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice() - SelectProduct_Bean.getSinglePrice()));
                            }
                            selectProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(selectProductGroup_Bean.getSinglePrice()*Integer.parseInt(modifyProductGroup_QuantityText.getText())));
                            modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
                            modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getPriceAmount()));
                            setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
                            refreshModifyProductGroupSmallSinglePrice();

                            MainOrderProduct_Bean.setCheckBoxDisable(MainOrderProduct_Bean.getQuantity() == 0);
                        }
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
                ComponentToolKit.setSpinnerIntegerValue(QuantitySpinner, Quantity);
                setGraphic(QuantitySpinner);
            }
        }
    }
    @FXML protected void ModifyGroupKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ProductGroupTableViewList.size() != 0) {
            if(modifyGroup()) {
                DialogUI.MessageDialog("修改完成");
            }
        }
    }
    /** Button Mouse Clicked - 修改商品群組 */
    @FXML protected void ModifyGroupMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ProductGroupTableViewList.size() != 0) {
            modifyGroup_Button.setDefaultButton(false);
            if(modifyGroup()) {
                DialogUI.MessageDialog("修改完成");
            }
        }
    }
    private boolean isPreviousGroupModifiedAndNeededSave(){
        if(selectProductGroup_Bean == null)
            return false;
        return !this.selectProductGroup_Bean.getGroupName().equals(modifyProductGroup_NameText.getText()) ||
                !this.selectProductGroup_Bean.getUnit().equals(modifyProductGroup_UnitText.getText()) ||
                (!modifyProductGroup_QuantityText.getText().equals("") && this.selectProductGroup_Bean.getQuantity() != Integer.parseInt(modifyProductGroup_QuantityText.getText())) ||
                (!modifyProductGroup_SinglePriceText.getText().equals("") && this.selectProductGroup_Bean.getSinglePrice() != ToolKit.RoundingDouble(modifyProductGroup_SinglePriceText.getText())) ||
                (this.selectProductGroup_Bean.getSmallQuantity() != null && !this.selectProductGroup_Bean.getSmallQuantity().equals(modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue())) ||
                (this.selectProductGroup_Bean.getSmallSinglePrice() != null && (modifyProductGroup_SmallSinglePriceText.getText().equals("") || this.selectProductGroup_Bean.getSmallSinglePrice() != ToolKit.RoundingDouble(modifyProductGroup_SmallSinglePriceText.getText()))
                || (this.selectProductGroup_Bean.getSmallSinglePrice() == null && !modifyProductGroup_SmallSinglePriceText.getText().equals("")));
    }

    private boolean modifyGroup(){
        boolean isModifyQuantity = false, isModifySinglePrice = false;
        String text = null;
        if(selectProductGroup_Bean == null) {
            return false;
        }else if(modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue() != 0 && modifyProductGroup_SmallSinglePriceText.getText().equals("")){
            DialogUI.MessageDialog("※ 請輸入【非組合】細項單價");
            modifyProductGroup_SmallSinglePriceText.requestFocus();
            return false;
        }else if(modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue() == 0 &&
                (!modifyProductGroup_SmallSinglePriceText.getText().equals("") && !modifyProductGroup_SmallSinglePriceText.getText().equals("0"))){
            DialogUI.MessageDialog("※ 請輸入【非組合】細項數量");
            modifyProductGroup_smallQuantitySpinner.requestFocus();
            return false;
        }
        if(!this.selectProductGroup_Bean.getGroupName().equals(modifyProductGroup_NameText.getText())){
            text = "【名稱】\n原：" + this.selectProductGroup_Bean.getGroupName() + "\n新：" + modifyProductGroup_NameText.getText();
        }
        if(!this.selectProductGroup_Bean.getUnit().equals(modifyProductGroup_UnitText.getText())){
            text = (text != null) ? (text + "\n\n") : "";
            text = text + "【單位】" + this.selectProductGroup_Bean.getUnit() + " --> " + modifyProductGroup_UnitText.getText();
        }
        if(!modifyProductGroup_QuantityText.getText().equals("") && this.selectProductGroup_Bean.getQuantity() != Integer.parseInt(modifyProductGroup_QuantityText.getText())){
            text = (text != null) ? (text + "\n\n") : "";
            text = text + "【數量】" + this.selectProductGroup_Bean.getQuantity() + " --> " + modifyProductGroup_QuantityText.getText();
            isModifyQuantity = true;
        }
        if(!modifyProductGroup_SinglePriceText.getText().equals("") && this.selectProductGroup_Bean.getSinglePrice() != ToolKit.RoundingDouble(modifyProductGroup_SinglePriceText.getText())){
            text = (text != null) ? (text + "\n\n") : "";
            text = text + "【單價】" + ToolKit.RoundingInteger(this.selectProductGroup_Bean.getSinglePrice()) + " --> " + modifyProductGroup_SinglePriceText.getText();
            isModifySinglePrice = true;
        }
        if((this.selectProductGroup_Bean.getSmallQuantity() != null && !this.selectProductGroup_Bean.getSmallQuantity().equals(modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue()))){
            text = (text != null) ? (text + "\n\n") : "";
            text = text + "【非組合-細項數量】" + this.selectProductGroup_Bean.getSmallQuantity() + " --> " + modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
        }
        if(this.selectProductGroup_Bean.getSmallSinglePrice() != null && (modifyProductGroup_SmallSinglePriceText.getText().equals("") || this.selectProductGroup_Bean.getSmallSinglePrice() != ToolKit.RoundingDouble(modifyProductGroup_SmallSinglePriceText.getText()))
                || (this.selectProductGroup_Bean.getSmallSinglePrice() == null && !modifyProductGroup_SmallSinglePriceText.getText().equals(""))){
            text = (text != null) ? (text + "\n\n") : "";
            text = text + "【非組合-細項單價】" + (this.selectProductGroup_Bean.getSmallSinglePrice() == null ? "" : this.selectProductGroup_Bean.getSmallSinglePrice() + " --> " + modifyProductGroup_SmallSinglePriceText.getText());
        }

        if(text == null){
            return true;
        }else {
            if(DialogUI.ConfirmDialog("群組資訊已變更：\n\n" + text + "\n\n是否儲存 ?",
                    true,
                    true,
                    text.length()+14-4,text.length()+16)){
                TableView<OrderProduct_Bean> previousSelectTabTableView = getSelectTabProductTableView();
                int modifyGroupQuantity = Integer.parseInt(modifyProductGroup_QuantityText.getText());
                if(modifyGroupQuantity == 0){
                    DialogUI.MessageDialog("※ 群組數量不允許「0」");
                    return false;
                }else if(isMainProductQuantityNotEnough(ComponentToolKit.getOrderProductTableViewItemList(previousSelectTabTableView),
                        selectProductGroup_Bean.getQuantity(), modifyGroupQuantity)){
                    DialogUI.MessageDialog("※ 群組數量大於商品總數量");
                    modifyProductGroup_QuantityText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getQuantity()));
                    ComponentToolKit.setButtonStyle(modifyGroup_Button,"");
                    return false;
                }else{
                    this.selectProductGroup_Bean.setGroupName(modifyProductGroup_NameText.getText());
                    TabRename();
                    this.selectProductGroup_Bean.setUnit(modifyProductGroup_UnitText.getText());
                    refreshMainProductQuantityAndCalculatePrice(this.selectProductGroup_Bean,
                            true,
                            isModifyQuantity,
                            isModifySinglePrice);
                    this.selectProductGroup_Bean.setQuantity(modifyGroupQuantity);
                    int smallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
                    this.selectProductGroup_Bean.setSmallQuantity((this.selectProductGroup_Bean.isCombineGroup() || smallQuantity == 0) ? null : smallQuantity);
                    this.selectProductGroup_Bean.setSmallSinglePrice(this.selectProductGroup_Bean.getSmallQuantity() == null || this.selectProductGroup_Bean.getSmallQuantity() == 0 ?
                            null : ToolKit.RoundingDouble(modifyProductGroup_SmallSinglePriceText.getText()));
                    this.selectProductGroup_Bean.setSmallPriceAmount(this.selectProductGroup_Bean.getSmallQuantity() == null || this.selectProductGroup_Bean.getSmallQuantity() == 0 ?
                            null : Integer.parseInt(modifyProductGroup_SmallTotalPriceText.getText()));
                    ComponentToolKit.setButtonStyle(modifyGroup_Button,"");
                    return true;
                }
            }else{
                ComponentToolKit.setButtonStyle(modifyGroup_Button,"");
                setModifyProductGroupInfo(this.selectProductGroup_Bean);
                return false;
            }
        }
    }
    private boolean isMainProductQuantityNotEnough(ObservableList<OrderProduct_Bean> selectGroupProductList, int originalGroupQuantity, int modifyGroupQuantity){
        ObservableList<OrderProduct_Bean> mainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean selectProduct_Bean : selectGroupProductList){
            for(OrderProduct_Bean MainOrderProduct_Bean : mainProductList) {
                if (selectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(selectProduct_Bean.getISBN())) {
                    if(selectProduct_Bean.getQuantity()*originalGroupQuantity+MainOrderProduct_Bean.getQuantity() < selectProduct_Bean.getQuantity()*modifyGroupQuantity)
                        return true;
                    break;
                }
            }
        }
        return false;
    }
    public void refreshMainProductQuantityAndCalculatePrice(ProductGroup_Bean selectProductGroup_Bean,
                                                             boolean isRefreshMainProductQuantity,
                                                             boolean isModifyQuantity,
                                                             boolean isModifySinglePrice){
        if(!isModifyQuantity && !isModifySinglePrice)
            return;
        int modifyGroupQuantity = Integer.parseInt(modifyProductGroup_QuantityText.getText());
        double originSinglePrice;
        if(isRefreshMainProductQuantity){
            originSinglePrice = refreshMainProductQuantity(ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView()),
                    selectProductGroup_Bean,
                    modifyGroupQuantity);
            selectProductGroup_Bean.setOriginalSinglePrice(originSinglePrice);
        }else{
            originSinglePrice = ToolKit.RoundingDouble(selectProductGroup_Bean.getOriginalSinglePrice());
        }
        if(isModifyQuantity && !isModifySinglePrice){
            selectProductGroup_Bean.setSinglePrice(originSinglePrice);
            modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,originSinglePrice));
//            calculateModifyGroupPriceAmount();
        }else{
            selectProductGroup_Bean.setSinglePrice(ToolKit.RoundingDouble(modifyProductGroup_SinglePriceText.getText()));
        }
        setCalculateOriginDifferentSinglePriceAndTotalPrice(selectProductGroup_Bean);
        selectProductGroup_Bean.setPriceAmount(ToolKit.RoundingInteger(modifyGroupQuantity*selectProductGroup_Bean.getSinglePrice()));
        calculateModifyGroupPriceAmount();
    }
    private double refreshMainProductQuantity(ObservableList<OrderProduct_Bean> selectGroupProductList, ProductGroup_Bean selectProductGroup_Bean,int modifyGroupQuantity){
        double groupSinglePrice = 0;
        ObservableList<OrderProduct_Bean> mainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean selectProduct_Bean : selectGroupProductList){
            for(OrderProduct_Bean MainOrderProduct_Bean : mainProductList) {
                if (selectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(selectProduct_Bean.getISBN())) {
                    MainOrderProduct_Bean.setQuantity(selectProduct_Bean.getQuantity()*selectProductGroup_Bean.getQuantity()+MainOrderProduct_Bean.getQuantity()
                            -selectProduct_Bean.getQuantity()*modifyGroupQuantity);
                    MainOrderProduct_Bean.setCheckBoxDisable(MainOrderProduct_Bean.getQuantity() == 0);
                    selectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(selectProduct_Bean.getSinglePrice())*selectProduct_Bean.getQuantity());
                    groupSinglePrice = groupSinglePrice + selectProduct_Bean.getSinglePrice();
                    break;
                }
            }
        }
        return ToolKit.RoundingDouble(groupSinglePrice);
    }

    @FXML protected void DeleteGroupKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ProductGroupTableViewList.size() != 0)
            deleteGroup();
    }
    /** Button Mouse Clicked - 刪除商品群組 */
    @FXML protected void DeleteGroupMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ProductGroupTableViewList.size() != 0) {
            deleteGroup_Button.setDefaultButton(false);
            deleteGroup();
        }
    }
    private void deleteGroup(){
        ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
        ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
        for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
            MainOrderProduct_Bean.setCheckBoxSelect(false);
            for (OrderProduct_Bean TabProductBean : TabProductList){
                if(MainOrderProduct_Bean.getItemNumber() == TabProductBean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(TabProductBean.getISBN())){
                    MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()+ToolKit.RoundingInteger(modifyProductGroup_QuantityText.getText())*TabProductBean.getQuantity());
                    MainOrderProduct_Bean.setCheckBoxDisable(false);
                }
            }
        }
        deleteTab();
        selectProductGroup_Bean = getSelectTabProductGroupBean();
        if(ProductGroupTableViewList.size() == 0) {
            setModifyProductGroupTextFieldEditable(true,false);
        }
        setButtonDisable(ProductGroupTableViewList.size() == 0);
    }
    private void deleteTab(){
        ProductGroupTableViewList.remove(ComponentToolKit.getTabPaneSelectTabIndex(TabPane));
        ComponentToolKit.deleteSelectTab(TabPane);
        TabRename();
    }
    private void TabRename(){
        ObservableList<Tab> TabList = ComponentToolKit.getTabPaneList(TabPane);
        for(int index = 0 ; index < TabList.size() ; index++){
            Tab Tab = TabList.get(index);
            ProductGroup_Bean ProductGroup_Bean = ProductGroupTableViewList.get(index).entrySet().iterator().next().getKey();
            ProductGroup_Bean.setItemNumber((index+1));
            Tab.setText("(" + (index+1) + ") " + ProductGroup_Bean.getGroupName());
        }
    }

    private void refreshMainTableViewByInsertGroup(ProductGroup_Bean selectProductGroup_Bean, ObservableList<OrderProduct_Bean> selectProductList){
        for (OrderProduct_Bean MainOrderProduct_Bean : selectProductList) {
            if(selectProductGroup_Bean.isCombineGroup())
                MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()-selectProductGroup_Bean.getQuantity());
            else
                MainOrderProduct_Bean.setQuantity(0);
            MainOrderProduct_Bean.setCheckBoxSelect(false);
            MainOrderProduct_Bean.setCheckBoxDisable(MainOrderProduct_Bean.getQuantity() == 0);
        }
    }
    private void refreshMainTableViewByImportProduct(ProductGroup_Bean selectProductGroup_Bean, ObservableList<OrderProduct_Bean> selectProductList){
        for (OrderProduct_Bean MainOrderProduct_Bean : selectProductList) {
            if(selectProductGroup_Bean.isCombineGroup())
                MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()-MainOrderProduct_Bean.getQuantity()/selectProductGroup_Bean.getQuantity()*selectProductGroup_Bean.getQuantity());
            else
                MainOrderProduct_Bean.setQuantity(0);
            MainOrderProduct_Bean.setCheckBoxSelect(false);
            MainOrderProduct_Bean.setCheckBoxDisable(MainOrderProduct_Bean.getQuantity() == 0);
        }
    }
    private void refreshPurchaseQuotationTableView(){
        getSelectTabProductTableView().refresh();
    }
    /** Button Mouse Clicked - 建立 */
    @FXML protected void EstablishOrderProductGroupMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ProductGroupTableViewList.size() == 0)    DialogUI.MessageDialog("※ 未分配商品");
            else{
                if(isRemainProduct() && !DialogUI.ConfirmDialog("※ 尚有剩餘商品，是否建立 ?",true,true,2,8))
                    return;

                HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = generateProductOrderMap();
                if(Order_Model.updateItemsProductGroup(null, productGroupMap)){
                    ComponentToolKit.closeThisStage(Stage);
                    DialogUI.MessageDialog("※ 商品群組建立成功");
                    refreshOrder_ProductGroupMap();
                }else {
                    DialogUI.MessageDialog("商品群組建立失敗");
                }
            }
        }
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void ModifyOrderProductGroupMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if (isMainProductQuantityError()) {
                DialogUI.MessageDialog("※ 貨單商品數量異常");
                return;
            }else if(Order_Bean.getReviewStatusMap().get(Order_Enum.ReviewObject.商品群組) == Order_Enum.ReviewStatus.審查通過){
                DialogUI.MessageDialog("※ 商品群組不允許修改：【狀態】審查通過");
                return;
            }

            if(isPreviousGroupModifiedAndNeededSave()){
                if(!modifyGroup()){
                    return;
                }
            }
            boolean isRemainProduct = ProductGroupTableViewList.size() != 0 && isRemainProduct();
            if(isRemainProduct && !DialogUI.ConfirmDialog("※ 尚有剩餘商品，是否修改 ?",true,true,2,8))
                return;

            HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> originalProductGroupMap = Order_Bean.getProductGroupMap();
            HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> newProductGroupMap = generateProductOrderMap();
            if(Order_Model.updateItemsProductGroup(originalProductGroupMap, newProductGroupMap)){
                DialogUI.MessageDialog("※ 商品群組修改成功");
                if(ProductGroupTableViewList.size() == 0){
                    if(!Order_Model.deleteProductGroupPrice(Order_Bean.getOrderID())){
                        DialogUI.AlarmDialog("※ 【商品群組】金額刪除失敗，請洽工程師");
                    }
                    if(Order_Model.updateOrderReviewStatus(Order_Bean.getOrderID(), Order_Enum.ReviewObject.商品群組, Order_Enum.ReviewStatus.無, null)){
                        Order_Bean.getReviewStatusMap().put(Order_Enum.ReviewObject.商品群組,Order_Enum.ReviewStatus.無);
                    }else{
                        DialogUI.AlarmDialog("審查狀態修改失敗!");
                    }
                }
                refreshOrder_ProductGroupMap();
                ComponentToolKit.closeThisStage(Stage);
            }else
                DialogUI.MessageDialog("※ 商品群組修改失敗");
        }
    }
    private boolean isMainProductQuantityError(){
        ObservableList<OrderProduct_Bean> mainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean OrderProduct_Bean : mainProductList){
            if(OrderProduct_Bean.getQuantity() < 0)
                return true;
        }
        return false;
    }
    private HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> generateProductOrderMap(){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = new HashMap<>();
        for (HashMap<ProductGroup_Bean, TableView<OrderProduct_Bean>> ProductGroupMap : ProductGroupTableViewList) {
            ProductGroup_Bean ProductGroup_Bean = ProductGroupMap.entrySet().iterator().next().getKey();
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(ProductGroupMap.entrySet().iterator().next().getValue());

            productGroupMap.put(ProductGroup_Bean.getItemNumber(), new HashMap<ProductGroup_Bean, LinkedHashMap<Integer,ItemGroup_Bean>>(){{put(ProductGroup_Bean, new LinkedHashMap<>());}});
            HashMap<ProductGroup_Bean,LinkedHashMap<Integer,ItemGroup_Bean>> itemGroupMap = productGroupMap.get(ProductGroup_Bean.getItemNumber());

            for (OrderProduct_Bean OrderProduct_Bean : productList) {
                ItemGroup_Bean ItemGroup_Bean = new ItemGroup_Bean(null, OrderProduct_Bean.getItem_id(), OrderProduct_Bean.getQuantity(),null, OrderProduct_Bean.getISBN(), OrderProduct_Bean.getProductName());
                itemGroupMap.get(itemGroupMap.entrySet().iterator().next().getKey()).put(OrderProduct_Bean.getItem_id(),ItemGroup_Bean);
            }
        }
        return productGroupMap;
    }
    private boolean isRemainProduct(){
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(!OrderProduct_Bean.isCheckBoxDisable())  return true;
        }
        return false;
    }
    private void refreshOrder_ProductGroupMap(){
        if(Order_Bean.getProductGroupMap() != null)
            Order_Bean.getProductGroupMap().clear();
        HashMap<Integer,HashMap<ProductGroup_Bean,LinkedHashMap<Integer,ItemGroup_Bean>>> productGroupMap = Order_Model.getProductGroupByOrderId(Order_Bean.getOrderID());
        Order_Bean.setProductGroupMap(productGroupMap);
        if(productGroupMap != null){
            Order_Bean.setProductGroupTotalPriceNoneTax(Order_Model.calculateProductGroupTotalPrice_NoneTax(false, productGroupMap));
            Order_Bean.setProductGroupDiscount(Order_Bean.getProductGroupDiscount() == null ? "0" : Order_Bean.getProductGroupDiscount());
            Order_Bean.setProductGroupTotalPriceIncludeTax(
                    ToolKit.RoundingString(
                            (Order_Model.calculateOrderTotalPrice_IncludeTax(Order_Bean.getOrderTaxStatus(), ToolKit.RoundingInteger(Order_Bean.getProductGroupTotalPriceNoneTax()))-ToolKit.RoundingInteger(Order_Bean.getProductGroupDiscount()))
                    ));
            Order_Bean.setProductGroupTax(ToolKit.RoundingString(ToolKit.RoundingInteger(Order_Bean.getProductGroupTotalPriceIncludeTax()) - ToolKit.RoundingInteger(Order_Bean.getProductGroupTotalPriceNoneTax())));
        }
        EstablishOrder_Controller.shipmentProductGroup_toggleSwitch.refreshAccordion();
        if(EstablishOrder_Controller.shipmentProductGroup_toggleSwitch.switchedOnProperty().get())
            EstablishOrder_Controller.RefreshProductInfoByAccordion();
        EstablishOrder_Controller.refreshProductGroupDifferencePrice();
        if(Order_Bean.getProductGroupMap() == null)
            EstablishOrder_Controller.shipmentProductGroup_toggleSwitch.setSwitchedOnStatus(false);
    }
    public TableView<OrderProduct_Bean> getSelectTabProductTableView(){
        return ProductGroupTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).entrySet().iterator().next().getValue();
    }
    private TableView<OrderProduct_Bean> getPreviousSelectTabProductTableView(ProductGroup_Bean productGroup_Bean){
        TableView<OrderProduct_Bean> tableView = null;
        ObservableList<Tab> tabPaneList = ComponentToolKit.getTabPaneList(TabPane);
        for(int index = 0 ; index < tabPaneList.size() ; index++){
            Tab tab = tabPaneList.get(index);
            String tabName = tab.getText().substring(tab.getText().indexOf(" ")+1);
            if(tabName.equals(productGroup_Bean.getGroupName())){
                tableView = ProductGroupTableViewList.get(index).entrySet().iterator().next().getValue();
                break;
            }
        }
        return tableView;
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    public ProductGroup_Bean getSelectTabProductGroupBean(){
        if(ProductGroupTableViewList.size() == 0)
            return null;
        return ProductGroupTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).entrySet().iterator().next().getKey();
    }
    private void setInsertProductGroupTextFieldEditable(boolean initialText, boolean editable){
        if(initialText){
            insertProductGroup_NameText.setText("");
            insertProductGroup_QuantityText.setText("1");
            insertProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            insertProductGroup_UnitText.setText("");
            insertProductGroup_SinglePriceText.setText("");
            insertProductGroup_SmallSinglePriceText.setText("");
            insertProductGroup_TotalPriceText.setText("");
            insertProductGroup_SmallTotalPriceText.setText("");
            insertProductGroup_ToggleSwitch.switchedOnProperty().set(false);
        }
        if(!editable){
            ComponentToolKit.setTextFieldStyle(insertProductGroup_NameText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(insertProductGroup_QuantityText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(insertProductGroup_UnitText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(insertProductGroup_SinglePriceText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(insertProductGroup_SmallSinglePriceText,"-fx-background-color:#bebebe");

            ComponentToolKit.setTextFieldEditable(insertProductGroup_NameText,false);
            ComponentToolKit.setTextFieldEditable(insertProductGroup_QuantityText,false);
            ComponentToolKit.setIntegerSpinnerDisable(insertProductGroup_smallQuantitySpinner,true);
            ComponentToolKit.setTextFieldEditable(insertProductGroup_UnitText,false);
            ComponentToolKit.setTextFieldEditable(insertProductGroup_SinglePriceText,false);
            ComponentToolKit.setTextFieldEditable(insertProductGroup_SmallSinglePriceText,false);
        }else{
            ComponentToolKit.setTextFieldEditable(insertProductGroup_NameText,true);
            setInsertProductGroupQuantityEditable(initialText, insertProductGroup_ToggleSwitch.switchedOnProperty().get());
            ComponentToolKit.setTextFieldEditable(insertProductGroup_UnitText,true);
            ComponentToolKit.setTextFieldEditable(insertProductGroup_SinglePriceText,true);
            ComponentToolKit.setTextFieldStyle(insertProductGroup_NameText,"-fx-light-text-color:white");
            ComponentToolKit.setTextFieldStyle(insertProductGroup_UnitText,"-fx-light-text-color:white");
            ComponentToolKit.setTextFieldStyle(insertProductGroup_SinglePriceText,"-fx-light-text-color:white");
            insertProductGroup_NameText.requestFocus();
        }
    }
    public void setInsertProductGroupQuantityEditable(boolean initialText, boolean toggleSwitch){
        ComponentToolKit.setTextFieldEditable(insertProductGroup_QuantityText, toggleSwitch);
        ComponentToolKit.setIntegerSpinnerDisable(insertProductGroup_smallQuantitySpinner, toggleSwitch);
        ComponentToolKit.setTextFieldEditable(insertProductGroup_SmallSinglePriceText, !toggleSwitch);
        ComponentToolKit.setTextFieldStyle(insertProductGroup_SmallSinglePriceText, !toggleSwitch? "-fx-light-text-color:white" : "-fx-background-color:#bebebe");
        if(initialText){
            insertProductGroup_QuantityText.setText("");
            insertProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            insertProductGroup_SmallSinglePriceText.setText("");
        }
        if(!toggleSwitch){
            insertProductGroup_QuantityText.setText("1");
        }else{
            insertProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            insertProductGroup_SmallSinglePriceText.setText("");
            insertProductGroup_SmallTotalPriceText.setText("");
        }
        ComponentToolKit.setTextFieldStyle(insertProductGroup_QuantityText, toggleSwitch? "-fx-light-text-color:white" : "-fx-background-color:#bebebe");
    }
    private void setModifyProductGroupTextFieldEditable(boolean initialText, boolean editable){
        if(initialText) {
            groupIndexSpinner.getValueFactory().setValue(0);
            modifyProductGroup_NameText.setText("");
            modifyProductGroup_QuantityText.setText("");
            modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            modifyProductGroup_UnitText.setText("");
            modifyProductGroup_SinglePriceText.setText("");
            modifyProductGroup_SmallSinglePriceText.setText("");
            modifyProductGroup_TotalPriceText.setText("");
            modifyProductGroup_SmallTotalPriceText.setText("");
            singlePrice_originDifferentLabel.setText("");
            totalPrice_originDifferentLabel.setText("");
            singlePrice_smallDifferentLabel.setText("");
            totalPrice_smallDifferentLabel.setText("");
            modifyProductGroup_ToggleSwitch.switchedOnProperty().set(false);
            ComponentToolKit.setButtonStyle(modifyGroup_Button, "");
        }
        if(!editable){
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_NameText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_QuantityText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_UnitText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_SinglePriceText,"-fx-background-color:#bebebe");
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_SmallSinglePriceText,"-fx-background-color:#bebebe");

            ComponentToolKit.setIntegerSpinnerDisable(groupIndexSpinner,true);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_NameText,false);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_QuantityText,false);
            ComponentToolKit.setIntegerSpinnerDisable(modifyProductGroup_smallQuantitySpinner,true);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_UnitText,false);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_SinglePriceText,false);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_SmallSinglePriceText,false);
        }else{
            ComponentToolKit.setIntegerSpinnerDisable(groupIndexSpinner,false);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_NameText,true);
            setModifyProductGroupQuantityEditable(initialText, modifyProductGroup_ToggleSwitch.switchedOnProperty().get());
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_UnitText,true);
            ComponentToolKit.setTextFieldEditable(modifyProductGroup_SinglePriceText,true);
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_NameText,"-fx-light-text-color:white");
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_UnitText,"-fx-light-text-color:white");
            ComponentToolKit.setTextFieldStyle(modifyProductGroup_SinglePriceText,"-fx-light-text-color:white");
            modifyProductGroup_NameText.requestFocus();
        }
    }
    public void setModifyProductGroupQuantityEditable(boolean initialText, boolean toggleSwitch){
        ComponentToolKit.setTextFieldEditable(modifyProductGroup_QuantityText, toggleSwitch);
        ComponentToolKit.setIntegerSpinnerDisable(modifyProductGroup_smallQuantitySpinner, toggleSwitch);
        ComponentToolKit.setTextFieldEditable(modifyProductGroup_SmallSinglePriceText, !toggleSwitch);
        ComponentToolKit.setTextFieldStyle(modifyProductGroup_SmallSinglePriceText, !toggleSwitch? "-fx-light-text-color:white" : "-fx-background-color:#bebebe");
        if(initialText){
            modifyProductGroup_QuantityText.setText("");
            modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            modifyProductGroup_SmallSinglePriceText.setText("");
        }
        if(!toggleSwitch){
            modifyProductGroup_QuantityText.setText("1");
        }else{
            modifyProductGroup_smallQuantitySpinner.getValueFactory().setValue(0);
            modifyProductGroup_SmallSinglePriceText.setText("");
            modifyProductGroup_SmallTotalPriceText.setText("");
        }
        ComponentToolKit.setTextFieldStyle(modifyProductGroup_QuantityText, toggleSwitch? "-fx-light-text-color:white" : "-fx-background-color:#bebebe");
    }
    private void refreshProductSeriesNumber(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> OrderProductList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(int index = 0 ; index < OrderProductList.size() ; index++){
            OrderProduct_Bean OrderProduct_Bean = OrderProductList.get(index);
            OrderProduct_Bean.setSeriesNumber(index+1);
        }
    }
    private void setMainProductSelectedFalse(){
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            OrderProduct_Bean.setCheckBoxSelect(false);
        }
        ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
        ComponentToolKit.setButtonDisable(insertGroup_Button,true);
    }
    private void setButtonDisable(boolean Disable){
        ComponentToolKit.setButtonDisable(importProduct_Button, Disable);
        ComponentToolKit.setButtonDisable(modifyGroup_Button, Disable);
        ComponentToolKit.setButtonDisable(deleteProduct_Button, Disable);
        ComponentToolKit.setButtonDisable(deleteProduct_Button, Disable);
        ComponentToolKit.setButtonDisable(deleteGroup_Button, Disable);
    }
    public void calculateInsertGroupPriceAmount(){
        String productGroup_quantity = insertProductGroup_QuantityText.getText();
        String productGroup_singlePrice = insertProductGroup_SinglePriceText.getText();
        if(!productGroup_quantity.equals("") && !productGroup_singlePrice.equals("")){
            insertProductGroup_TotalPriceText.setText(ToolKit.RoundingString(Integer.parseInt(productGroup_quantity)*ToolKit.RoundingDouble(productGroup_singlePrice)));
        }else{
            insertProductGroup_TotalPriceText.setText("");
        }
    }
    public void calculateInsertSmallGroupPriceAmount(){
        int smallQuantity = insertProductGroup_smallQuantitySpinner.getValueFactory().getValue();
        String smallSinglePrice = insertProductGroup_SmallSinglePriceText.getText();
        if(smallSinglePrice != null && !smallSinglePrice.equals("")){
            insertProductGroup_SmallTotalPriceText.setText(ToolKit.RoundingString(smallQuantity*ToolKit.RoundingDouble(smallSinglePrice)));
        }else{
            insertProductGroup_SmallTotalPriceText.setText("");
        }
    }
    public void calculateModifyGroupPriceAmount(){
        String productGroup_quantity = modifyProductGroup_QuantityText.getText();
        String productGroup_singlePrice = modifyProductGroup_SinglePriceText.getText();
        if(!productGroup_quantity.equals("") && !productGroup_singlePrice.equals("")){
            modifyProductGroup_TotalPriceText.setText(ToolKit.RoundingString(Integer.parseInt(productGroup_quantity)*ToolKit.RoundingDouble(productGroup_singlePrice)));
        }else{
            modifyProductGroup_TotalPriceText.setText("");
        }
    }
    public void calculateModifySmallGroupPriceAmount(){
        Integer smallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
        String smallSinglePrice = modifyProductGroup_SmallSinglePriceText.getText();

        if(smallQuantity == null || smallSinglePrice == null || smallSinglePrice.equals("")){
            modifyProductGroup_SmallTotalPriceText.setText("");
        }else{
            modifyProductGroup_SmallTotalPriceText.setText(ToolKit.RoundingString(smallQuantity*ToolKit.RoundingDouble(smallSinglePrice)));
        }
    }
    private double calculateProductGroupOriginalPriceAmount(boolean toggleSwitch, ObservableList<OrderProduct_Bean> selectTabProductList){
        double originalPriceAmount = 0;
        for(OrderProduct_Bean OrderProduct_Bean : selectTabProductList){
            if(toggleSwitch)
                originalPriceAmount = originalPriceAmount + OrderProduct_Bean.getSinglePrice();
            else
                originalPriceAmount = originalPriceAmount + OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice();
        }
        return ToolKit.RoundingDouble(originalPriceAmount);
    }
    public void setCalculateOriginDifferentSinglePriceAndTotalPrice(ProductGroup_Bean ProductGroup_Bean){
        double different = ToolKit.RoundingDouble(ProductGroup_Bean.getSinglePrice() - ProductGroup_Bean.getOriginalSinglePrice());
        singlePrice_originDifferentLabel.setText(ToolKit.RoundingString(true, different));

        different = ToolKit.RoundingInteger(ProductGroup_Bean.getSinglePrice()*ProductGroup_Bean.getQuantity() - ProductGroup_Bean.getOriginalSinglePrice()*ProductGroup_Bean.getQuantity());
        totalPrice_originDifferentLabel.setText(ToolKit.RoundingString(different));
    }
    private void setCalculateSmallDifferentSinglePriceAndTotalPrice(){
        Integer smallQuantity = modifyProductGroup_smallQuantitySpinner.getValueFactory().getValue();
        Double singlePrice = (modifyProductGroup_SinglePriceText.getText() == null || modifyProductGroup_SinglePriceText.getText().equals("") || smallQuantity == null || smallQuantity == 0) ?
                null : ToolKit.RoundingDouble(Double.parseDouble(modifyProductGroup_SinglePriceText.getText())/smallQuantity);
        Double smallSinglePrice = (modifyProductGroup_SmallSinglePriceText.getText() == null || modifyProductGroup_SmallSinglePriceText.getText().equals("")) ?
                null : ToolKit.RoundingDouble(modifyProductGroup_SmallSinglePriceText.getText());
        if(singlePrice != null || smallSinglePrice != null){
            singlePrice_smallDifferentLabel.setText(
                    ToolKit.RoundingString(true,(singlePrice == null ? 0 : ToolKit.RoundingDouble(singlePrice)) - (smallSinglePrice == null ? 0 : ToolKit.RoundingDouble(smallSinglePrice))));
        }else{
            singlePrice_smallDifferentLabel.setText("");
        }
        Integer totalPrice = (modifyProductGroup_TotalPriceText.getText() == null || modifyProductGroup_TotalPriceText.getText().equals("")) ?
                null : ToolKit.RoundingInteger(modifyProductGroup_TotalPriceText.getText());
        Integer smallTotalPrice = (modifyProductGroup_SmallTotalPriceText.getText() == null || modifyProductGroup_SmallTotalPriceText.getText().equals("")) ?
                null : ToolKit.RoundingInteger(modifyProductGroup_SmallTotalPriceText.getText());
        if(totalPrice != null || smallTotalPrice != null){
            totalPrice_smallDifferentLabel.setText(
                    ToolKit.RoundingString((totalPrice == null ? 0 : ToolKit.RoundingInteger(totalPrice)) - (smallTotalPrice == null ? 0 : ToolKit.RoundingInteger(smallTotalPrice))));
        }else{
            totalPrice_smallDifferentLabel.setText("");
        }
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
                CheckBox SelectCheckBox = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView,getIndex()).getCheckBox();
                setGraphic(SelectCheckBox);
                SelectCheckBox.setOnAction(ActionEvent -> {
                    setInsertProductGroupInfo();
                    boolean isSelectAll = true;
                    ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    for(OrderProduct_Bean OrderProduct_Bean : productList){
                        if(!OrderProduct_Bean.isCheckBoxDisable() && !OrderProduct_Bean.isCheckBoxSelect()) {
                            isSelectAll = false;
                            break;
                        }
                    }
                    ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,isSelectAll);
                });
            }else
                setGraphic(null);
        }
    }
    private void setInsertProductGroupInfo(){
        ObservableList<OrderProduct_Bean> selectProductList = ComponentToolKit.getSelectProductList(MainTableView);
        ComponentToolKit.setButtonDisable(insertGroup_Button,selectProductList.size() == 0);
        setInsertProductGroupTextFieldEditable(selectProductList.size() == 0,selectProductList.size() != 0);

        calculateInsertProductGroupSinglePrice(selectProductList, insertProductGroup_ToggleSwitch.switchedOnProperty().get());
        calculateInsertGroupPriceAmount();

        refreshInsertProductGroupSmallSinglePrice();
    }
    public void calculateInsertProductGroupSinglePrice(ObservableList<OrderProduct_Bean> selectProductList, boolean isCombineGroup){
        double groupSinglePrice = 0;
        boolean isProductSelect = false;
        for(OrderProduct_Bean OrderProduct_Bean : selectProductList){
            isProductSelect = true;
            if(isCombineGroup)
                groupSinglePrice = groupSinglePrice + OrderProduct_Bean.getSinglePrice();
            else
                groupSinglePrice = groupSinglePrice + (OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice());
        }
        insertProductGroup_SinglePriceText.setText(isProductSelect ? ToolKit.RoundingString(true, groupSinglePrice) : "");
    }
    public void calculateModifyProductGroupSinglePrice(boolean isCombineGroup){
        ProductGroup_Bean selectProductGroup_Bean = getSelectTabProductGroupBean();
        if(selectProductGroup_Bean == null)
            return;
        if(selectProductGroup_Bean.isCombineGroup() == isCombineGroup){
            modifyProductGroup_QuantityText.setText(ToolKit.RoundingString(selectProductGroup_Bean.getQuantity()));
            modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,selectProductGroup_Bean.getSinglePrice()));
        }else{
            double productGroupSinglePrice = 0;
            ObservableList<OrderProduct_Bean> mainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> selectGroupProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
            for(OrderProduct_Bean selectProduct_Bean : selectGroupProductList){
                for(OrderProduct_Bean MainOrderProduct_Bean : mainProductList) {
                    if (selectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(selectProduct_Bean.getISBN())) {
                        if(isCombineGroup)
                            productGroupSinglePrice = productGroupSinglePrice + ToolKit.RoundingInteger(selectProduct_Bean.getSinglePrice());
                        else
                            productGroupSinglePrice = productGroupSinglePrice + selectProduct_Bean.getPriceAmount();
//                            productGroupSinglePrice = productGroupSinglePrice + ToolKit.RoundingInteger(selectProduct_Bean.getQuantity()*selectProduct_Bean.getSinglePrice());
                        selectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(selectProduct_Bean.getQuantity()*selectProduct_Bean.getSinglePrice()));
                        break;
                    }
                }
            }
            modifyProductGroup_SinglePriceText.setText(ToolKit.RoundingString(true,productGroupSinglePrice));
        }
    }
    private void setColumnCellIntegerValue(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
        TableColumn.setCellFactory(column -> new setColumnCellIntegerValue(Alignment, FontSize));
    }
    private class setColumnCellIntegerValue extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        setColumnCellIntegerValue(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(String.valueOf(item));
                OrderProduct_Bean OrderProduct_Bean = getTableView().getItems().get(getIndex());
                if(OrderProduct_Bean.getQuantity() <= 0)
                    getTableRow().setStyle("-fx-background-color: #B9B9B9;");
                else
                    getTableRow().setStyle("");
            }
        }
    }
    private InsertProductGroup_ToggleSwitch createInsertProductGroupSwitchButton(){
        InsertProductGroup_ToggleSwitch toggle = new InsertProductGroup_ToggleSwitch(false,this, insertProductGroupSwitch_Label);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        insertProductGroup_HBox.getChildren().add(0,toggle);
        return toggle;
    }
    private ModifyProductGroup_ToggleSwitch createModifyProductGroupSwitchButton(){
        ModifyProductGroup_ToggleSwitch toggle = new ModifyProductGroup_ToggleSwitch(false,this, modifyProductGroupSwitch_Label);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        modifyProductGroup_HBox.getChildren().add(0,toggle);
        return toggle;
    }
}
