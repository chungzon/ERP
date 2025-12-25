package ERP.Controller.ManageProductInfo.ManageProductInfo;

import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.ManageProductStatus;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import ERP.Enum.Product.Product_Enum.ManageProductInfoTableColumn;
import ERP.Enum.Product.Product_Enum.ManageProductInfoJsonConfigKey;
import ERP.ERPApplication;
import ERP.Model.Product.Product_Model;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.*;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** [Controller] Manage product info */
public class ManageProductInfo_Controller {

    private final KeyCombination KeyCombine = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    @FXML private HBox establishProduct_HBox, establishProduct_describe_HBox;
    @FXML private VBox searchProduct_VBox, searchProduct_describeAndSpecification_VBox;

    @FXML private CheckBox InternationalCode_CheckBox, FirmCode_CheckBox, ProductName_CheckBox, Unit_CheckBox, VendorCode_CheckBox, VendorName_CheckBox, FirstCategory_CheckBox,
            SecondCategory_CheckBox, ThirdCategory_CheckBox, ProductCode_CheckBox, BatchPrice_CheckBox, SinglePrice_CheckBox, Pricing_CheckBox, VipPrice1_CheckBox, VipPrice2_CheckBox, VipPrice3_CheckBox,
            InStock_CheckBox, SafetyStock_CheckBox, Discount_CheckBox, InventoryDate_CheckBox, PurchaseDate_CheckBox, SaleDate_CheckBox, ShipmentDate_CheckBox, UpdateDate_CheckBox;
    @FXML private TextField ISBNText, InternationalCodeText, FirmCodeText, ProductNameText, UnitText;
    @FXML private TextField VendorCodeText, VendorNameText;
    @FXML private TextField FirstCategoryIDText, FirstCategoryNameText, SecondCategoryIDText, SecondCategoryNameText, ThirdCategoryIDText, ThirdCategoryNameText, ProductCodeText;
    @FXML private TextField BatchPriceText, SinglePriceText, PricingText, VipPrice1Text, VipPrice2Text, VipPrice3Text;
    @FXML private TextField InStockText, SafetyStockText;
    @FXML private TextField InventoryDateText, PurchaseDateText, SaleDateText, ShipmentDateText, UpdateDateText;
    @FXML private Spinner<Double> SinglePricePercentageSpinner, PricingPercentageSpinner, VipPrice1PercentageSpinner, VipPrice2PercentageSpinner, VipPrice3PercentageSpinner, DiscountPercentageSpinner;
    @FXML private TextArea RemarkText;
    @FXML private ComboBox<String> ProductArea_ComboBox, ProductFloor_ComboBox;
    @FXML private TextField SearchKeyWordText;
    @FXML private DatePicker KeyinDateSearch_DatePicker;
    @FXML private ImageView Picture1ImageView, Picture2ImageView, Picture3ImageView;
    @FXML private Button ProductTagSetting_Button, BidCategorySetting_Button, SaveProductPicture_Button, ModifyProduct_Button, DeleteProduct_Button, SearchTransactionDetail_Button, PrintProductLabel_Button, Shelf_Button, ApplyBidCategory_Button;
    @FXML private Button InsertProduct_Button, Picture1Load_Button, Picture1Delete_Button, Picture2Load_Button, Picture2Delete_Button, Picture3Load_Button, Picture3Delete_Button;
    @FXML private Label InsertProductStatusLabel, SaveProductPictureStatusLabel;
    @FXML private ProgressBar InsertProductProgressBar, SaveProductPictureProgressBar, SearchProgressBar;
    @FXML private TableView<ProductInfo_Bean> TableView;
    @FXML private TableColumn<ProductInfo_Bean, String> Select_TableColumn;
    @FXML private TableColumn<ProductInfo_Bean, String> ISBNColumn, InternationalCodeColumn, FirmCodeColumn, ProductCodeColumn, ProductNameColumn, UnitColumn, VendorCodeColumn, VendorNameColumn,
            FirstCategoryColumn, SecondCategoryColumn, ThirdCategoryColumn, BatchPriceColumn, SinglePriceColumn, PricingColumn, VipPrice1Column, VipPrice2Column, VipPrice3Column,
            InStockColumn, SafetyStockColumn, DiscountColumn, InventoryDateColumn, PurchaseDateColumn, SaleDateColumn, ShipmentDateColumn, UpdateDateColumn;

    @FXML SplitMenuButton AdvancedSearch_SplitMenuButton;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.CallJAR CallJAR;
    private ERP.ToolKit.CallConfig CallConfig;
    private HashMap<String, ArrayList<String>> BookCase;
    private Product_Model Product_Model;
    private Order_Model Order_Model;
    private ProductInfo_Bean ProductInfo_Bean;
    public ManageProductStatus ManageProductStatus;
    private JSONObject TableViewSettingMap;
    private JSONObject AdvancedSearchMap;

    private Stage MainStage;
    public ManageProductInfo_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.CallJAR = ToolKit.CallJAR;
        this.CallConfig = ToolKit.CallConfig;
        this.Product_Model = ToolKit.ModelToolKit.getProductModel();
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }

    public void setManageProductStatus(ManageProductStatus ManageProductStatus){  this.ManageProductStatus = ManageProductStatus; }
    public void setMainStage(Stage MainStage){
        this.MainStage = MainStage;
    }

    /** Set component of manage product info */
    public void setComponent(ProductInfo_Bean ProductInfo_Bean) throws Exception {
        setUI();
        ComponentToolKit.setDoubleSpinnerValueFactory(SinglePricePercentageSpinner, 0.01, 100, 1, 0.01);
        ComponentToolKit.setDoubleSpinnerValueFactory(PricingPercentageSpinner, 0.01, 100, 1, 0.01);
        ComponentToolKit.setDoubleSpinnerValueFactory(VipPrice1PercentageSpinner, 0.01, 100, 1, 0.01);
        ComponentToolKit.setDoubleSpinnerValueFactory(VipPrice2PercentageSpinner, 0.01, 100, 1, 0.01);
        ComponentToolKit.setDoubleSpinnerValueFactory(VipPrice3PercentageSpinner, 0.01, 100, 1, 0.01);
        ComponentToolKit.setDoubleSpinnerValueFactory(DiscountPercentageSpinner, 0.01, 1, 1, 0.01);
        ComponentToolKit.setDatePickerValue(KeyinDateSearch_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        TableViewSettingMap = CallConfig.getManageProductInfoJsonConfig_TableViewSetting();
        AdvancedSearchMap = CallConfig.getManageProductInfoJsonConfig_AdvancedSearch();
        setAdvancedSearchItems();
        setInitialBookCase();
        initialTableView();
        setTableColumnSort();
        setKeyWordTextLimiterLength();
        setKeyWordTextLimiterDigital();

        if(ProductInfo_Bean != null){
            this.ProductInfo_Bean = ProductInfo_Bean;
            initialData();
        }else{
            this.ProductInfo_Bean = new ProductInfo_Bean();
        }
    }
    private void setUI(){
        if(ManageProductStatus == Product_Enum.ManageProductStatus.建立)  ShowEstablishUI();
        else if(ManageProductStatus == Product_Enum.ManageProductStatus.搜尋) ShowSearchUI();
    }
    private void setAdvancedSearchItems(){
        for(Product_Enum.AdvancedSearchColumn AdvancedSearchColumn : Product_Enum.AdvancedSearchColumn.values()){
            CheckBox CheckBox = ComponentToolKit.initialCheckBox(AdvancedSearchColumn.name(),16);
            ComponentToolKit.setCheckBoxSelect(CheckBox, AdvancedSearchMap.getBoolean(AdvancedSearchColumn.name()));
            setAdvancedSearchCheckBoxListener(CheckBox);

            CustomMenuItem CustomMenuItem = new CustomMenuItem(CheckBox);
            CustomMenuItem.setHideOnClick(false);
            AdvancedSearch_SplitMenuButton.getItems().add(CustomMenuItem);
        }
    }
    private void setInitialBookCase(){
        BookCase = Product_Model.getAllBookCase();
        ArrayList<String> ProductAreaList = new ArrayList<>(BookCase.keySet());
        ProductArea_ComboBox.getItems().addAll(ProductAreaList);
        ProductArea_ComboBox.getSelectionModel().selectFirst();

        ArrayList<String> ProductFloorList = new ArrayList<>(BookCase.get(ProductAreaList.get(0)));
        ProductFloor_ComboBox.getItems().addAll(ProductFloorList);
        ProductFloor_ComboBox.getSelectionModel().selectFirst();
    }
    private void setKeyWordTextLimiterLength(){
        ComponentToolKit.addKeyWordTextLimitLength(ISBNText,13);
        ComponentToolKit.addKeyWordTextLimitLength(SearchKeyWordText,13);
        ComponentToolKit.addKeyWordTextLimitLength(SecondCategoryIDText,2);
        ComponentToolKit.addKeyWordTextLimitLength(ThirdCategoryIDText,2);
    }
    private void setKeyWordTextLimiterDigital(){
        ComponentToolKit.addTextFieldLimitDigital(ISBNText,false);
        ComponentToolKit.addTextFieldLimitDigital(SecondCategoryIDText,false);
        ComponentToolKit.addTextFieldLimitDigital(ThirdCategoryIDText,false);

        ComponentToolKit.addTextFieldLimitDouble(BatchPriceText);
        ComponentToolKit.addTextFieldLimitDouble(SinglePriceText);
        ComponentToolKit.addTextFieldLimitDouble(PricingText);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice1Text);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice2Text);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice3Text);
        ComponentToolKit.addTextFieldLimitDigital(SafetyStockText,false);
    }
    /** TextField Key Pressed - 店內碼 */
    @FXML protected void ISBNKeyPressed(KeyEvent KeyEvent){
        if(isManageProduct_Establish()){
            if(KeyPressed.isEnterKeyPressed(KeyEvent))   InternationalCodeText.requestFocus();
            else if(KeyPressed.isF6KeyPressed(KeyEvent) && ISBNText.getText().length() >= 5){
                if(isVendorError())
                    DialogUI.MessageDialog("※ 查無廠商");
                else if(isFirstCategoryError())
                    DialogUI.MessageDialog("※ 無此大類別");
                else
                    ISBNText.setText(Product_Model.generateNewestISBN(ISBNText.getText()));
            }
        }
    }
    /** TextField Key Released - 店內碼 */
    @FXML protected void ISBNKeyReleased(){
        if(isManageProduct_Establish()){
            String ISBN = ISBNText.getText();
            generateVendorInfoByISBN(ISBN);
        }
    }
    private void generateVendorInfoByISBN(String ISBN){
        if(ISBN.length() >= 3){
            String VendorCode = ISBN.substring(0, 3);
            String VendorName = Product_Model.getManufacturerNameOfProduct(VendorCode);
            if(VendorName != null){
                VendorCodeText.setText(VendorCode);
                VendorNameText.setText(VendorName);
                if(ISBN.length() >= 5)  {
                    String CategoryID = ISBN.substring(3, 5);
                    String CategoryName = Product_Model.getCategoryName(CategoryID, CategoryLayer.大類別);
                    if(CategoryName != null) {
                        FirstCategoryIDText.setText(CategoryID);
                        FirstCategoryNameText.setText(CategoryName);
                    }
                }else{
                    FirstCategoryIDText.setText("");
                    FirstCategoryNameText.setText("");
                }
            }else
                DialogUI.MessageDialog("※ 查無廠商");
        }else{
            FirstCategoryIDText.setText("");
            FirstCategoryNameText.setText("");
            VendorCodeText.setText("");
            VendorNameText.setText("");
        }
    }
    /** TextField Key Pressed - 國際碼 */
    @FXML protected void InternationalCodeKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent)) FirmCodeText.requestFocus();   }
    /** TextField Key Released - 國際碼 */
    @FXML protected void InternationalCodeKeyReleased(KeyEvent KeyEvent){   if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null)  SaveProduct();  }
    /** TextField Mouse Clicked - 國際碼 */
    @FXML protected void InternationalCodeMouseClicked(MouseEvent MouseEvent){  if(KeyPressed.isMouseLeftClicked(MouseEvent))   InternationalCodeText.selectEnd();  }
    /** TextField Key Pressed - 商品碼 */
    @FXML protected void FirmCodeKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))  ProductNameText.requestFocus();   }
    /** TextField Key Released - 商品碼 */
    @FXML protected void FirmCodeKeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null) {
            SaveProduct();
        }
    }
    /** TextField Mouse Clicked - 商品碼 */
    @FXML protected void FirmCodeMouseClicked(MouseEvent MouseEvent){   if(KeyPressed.isMouseLeftClicked(MouseEvent))   FirmCodeText.selectEnd();   }
    /** TextField Key Pressed - 品名 */
    @FXML protected void ProductNameKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   UnitText.requestFocus();   }
    /** TextField Key Released - 品名 */
    @FXML protected void ProductNameKeyReleased(KeyEvent KeyEvent){ if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null)  SaveProduct();   }
    /** TextField Mouse Clicked - 品名 */
    @FXML protected void ProductNameMouseClicked(MouseEvent MouseEvent){    if(KeyPressed.isMouseLeftClicked(MouseEvent))   ProductNameText.selectEnd();    }
    /** TextField Key Pressed - 單位 */
    @FXML protected void UnitKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))  SecondCategoryIDText.requestFocus();   }
    /** TextField Key Released - 單位 */
    @FXML protected void UnitKeyReleased(KeyEvent KeyEvent){    if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null)  SaveProduct();   }
    /** TextField Key Pressed - 中類別 */
    @FXML protected void SecondCategoryKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))    ThirdCategoryIDText.requestFocus();   }
    /** TextField Key Released - 中類別 */
    @FXML protected void SecondCategoryKeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null) {
            SaveProduct();
        }else{
            String SecondCategoryID = SecondCategoryIDText.getText();
            if(SecondCategoryID.length() == 2)  SecondCategoryNameText.setText(Product_Model.getCategoryName(SecondCategoryID, Product_Enum.CategoryLayer.中類別));
            else   SecondCategoryNameText.setText("");
        }
    }
    /** TextField Key Pressed - 小類別 */
    @FXML protected void ThirdCategoryKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent)) BatchPriceText.requestFocus();   }
    /** TextField Key Released - 小類別 */
    @FXML protected void ThirdCategoryKeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null) {
            SaveProduct();
        }else{
            String ThirdCategoryID = ThirdCategoryIDText.getText();
            if(ThirdCategoryID.length() == 2)   ThirdCategoryNameText.setText(Product_Model.getCategoryName(ThirdCategoryID, Product_Enum.CategoryLayer.小類別));
            else   ThirdCategoryNameText.setText("");
        }
    }
    /** TextField Key Pressed - 成本價 */
    @FXML protected void BatchPriceKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   SinglePriceText.requestFocus(); }
    /** TextField Key Released - 成本價 */
    @FXML protected void BatchPriceKeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null) {
            SaveProduct();
        }else{
            String BatchPrice = BatchPriceText.getText();
            if(!BatchPrice.equals("") && ToolKit.isDouble(BatchPrice)){
                ComponentToolKit.setDoubleSpinnerDisable(SinglePricePercentageSpinner,false);
                ComponentToolKit.setTextFieldDisable(SinglePriceText,false);
            }else{
                setSpinnerValueAndDisable(SinglePricePercentageSpinner, 1.00, true);
                SinglePriceText.setText("");
                ComponentToolKit.setTextFieldDisable(SinglePriceText,true);
                setSpinnerValueAndDisable(PricingPercentageSpinner, 1.00, true);
                PricingText.setText("");
                ComponentToolKit.setTextFieldDisable(PricingText,true);
                setSpinnerValueAndDisable(VipPrice1PercentageSpinner, 1.00, true);
                VipPrice1Text.setText("");
                ComponentToolKit.setTextFieldDisable(VipPrice1Text,true);
                setSpinnerValueAndDisable(VipPrice2PercentageSpinner, 1.00, true);
                VipPrice2Text.setText("");
                ComponentToolKit.setTextFieldDisable(VipPrice2Text,true);
                setSpinnerValueAndDisable(VipPrice3PercentageSpinner, 1.00, true);
                VipPrice3Text.setText("");
                ComponentToolKit.setTextFieldDisable(VipPrice3Text,true);
            }
        }
    }
    /** Spinner Key Released - 單價 */
    @FXML protected void SinglePricePercentageKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())) {
            SinglePriceText.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * SinglePricePercentageSpinner.getValue()));
            SinglePriceText.requestFocus();
        }
    }
    /** Spinner Mouse Clicked - 單價 */
    @FXML private void SinglePricePercentageMouseClicked(){
        SinglePriceText.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * SinglePricePercentageSpinner.getValue()));
    }
    /** TextField Key Pressed - 單價 */
    @FXML protected void SinglePriceKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())){
            if(ToolKit.isDouble(SinglePriceText.getText()))    PricingText.requestFocus();
            else    DialogUI.MessageDialog("單價金額錯誤");
        }
    }
    /** TextField Key Released - 單價 */
    @FXML protected void SinglePriceKeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null){
              SaveProduct();
        }else{
            String SinglePrice = SinglePriceText.getText();
            if (!SinglePrice.equals("") && ToolKit.isDouble(SinglePrice)) {
                ComponentToolKit.setSpinnerDoubleValue(SinglePricePercentageSpinner, Double.parseDouble(SinglePrice) / Double.parseDouble(BatchPriceText.getText()));
                ComponentToolKit.setDoubleSpinnerDisable(PricingPercentageSpinner, false);
                ComponentToolKit.setTextFieldDisable(PricingText, false);
            } else {
                ComponentToolKit.setSpinnerDoubleValue(SinglePricePercentageSpinner, 1.00);
                setSpinnerValueAndDisable(PricingPercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(PricingText, true);
                setSpinnerValueAndDisable(VipPrice1PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice1Text, true);
                setSpinnerValueAndDisable(VipPrice2PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice2Text, true);
                setSpinnerValueAndDisable(VipPrice3PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice3Text, true);
            }
        }
    }
    /** Spinner Key Released - 市價 */
    @FXML protected void PricingPercentageKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())) {
            PricingText.setText(ToolKit.RoundingString(Double.parseDouble(BatchPriceText.getText()) * PricingPercentageSpinner.getValue()));
            PricingText.requestFocus();
        }
    }
    /** Spinner Mouse Clicked - 市價 */
    @FXML protected void PricingPercentageMouseClicked(){
        PricingText.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * PricingPercentageSpinner.getValue()));
    }
    /** TextField Key Pressed - 市價 */
    @FXML protected void PricingKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())){
            if(ToolKit.isDouble(PricingText.getText()))    VipPrice1Text.requestFocus();
            else    DialogUI.MessageDialog("市價金額錯誤");
        }
    }
    /** TextField Key Released - 市價 */
    @FXML void PricingKeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null) {
            SaveProduct();
        }else{
            String Pricing = PricingText.getText();
            if(ToolKit.isDouble(Pricing) && !Pricing.equals("")){
                ComponentToolKit.setSpinnerDoubleValue(PricingPercentageSpinner,Double.parseDouble(Pricing) / Double.parseDouble(BatchPriceText.getText()));
                ComponentToolKit.setDoubleSpinnerDisable(VipPrice1PercentageSpinner,false);
                ComponentToolKit.setTextFieldDisable(VipPrice1Text,false);
            }else{
                ComponentToolKit.setSpinnerDoubleValue(PricingPercentageSpinner,1.00);
                setSpinnerValueAndDisable(VipPrice1PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice1Text,true);
                setSpinnerValueAndDisable(VipPrice2PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice2Text,true);
                setSpinnerValueAndDisable(VipPrice3PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice3Text,true);
            }
        }
    }
    /** Spinner Key Released - Vip金額1 */
    @FXML protected void VipPrice1PercentageKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())) {
            VipPrice1Text.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * VipPrice1PercentageSpinner.getValue()));
            VipPrice1Text.requestFocus();
        }
    }
    /** Spinner Mouse Clicked - Vip金額1 */
    @FXML private void VipPrice1PercentageMouseClicked(){
        VipPrice1Text.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * VipPrice1PercentageSpinner.getValue()));
    }
    /** TextField Mouse Clicked - Vip金額1 */
    @FXML protected void VipPrice1KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())){
            if(ToolKit.isDouble(VipPrice1Text.getText()))  VipPrice2Text.requestFocus();
            else    DialogUI.MessageDialog("VipPrice1金額錯誤");
        }
    }
    /** TextField Key Released - Vip金額1 */
    @FXML void VipPrice1KeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null){
            SaveProduct();
        }else{
            String VipPrice1 = VipPrice1Text.getText();
            if(ToolKit.isDouble(VipPrice1) && !VipPrice1.equals("")){
                ComponentToolKit.setSpinnerDoubleValue(VipPrice1PercentageSpinner,Double.parseDouble(VipPrice1) / Double.parseDouble(BatchPriceText.getText()));
                ComponentToolKit.setDoubleSpinnerDisable(VipPrice2PercentageSpinner,false);
                ComponentToolKit.setTextFieldDisable(VipPrice2Text, false);
            }else{
                ComponentToolKit.setSpinnerDoubleValue(VipPrice1PercentageSpinner,1.00);
                setSpinnerValueAndDisable(VipPrice2PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice2Text,true);
                setSpinnerValueAndDisable(VipPrice3PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice3Text,true);
            }
        }
    }
    /** Spinner Key Released - Vip金額2 */
    @FXML protected void VipPrice2PercentageKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())) {
            VipPrice2Text.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * VipPrice2PercentageSpinner.getValue()));
            VipPrice2Text.requestFocus();
        }
    }
    /** Spinner Mouse Clicked - Vip金額2 */
    @FXML protected void VipPrice2PercentageMouseClicked(){
        VipPrice2Text.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * VipPrice2PercentageSpinner.getValue()));
    }
    /** TextField Key Pressed - Vip金額2 */
    @FXML protected void VipPrice2KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())){
            if(ToolKit.isDouble(VipPrice2Text.getText()))  VipPrice3Text.requestFocus();
            else    DialogUI.MessageDialog("VipPrice2金額錯誤");
        }
    }
    /** TextField Key Released - Vip金額2 */
    @FXML void VipPrice2KeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null) {
            SaveProduct();
        }else{
            String VipPrice2 = VipPrice2Text.getText();
            if(ToolKit.isDouble(VipPrice2) && !VipPrice2.equals("")){
                ComponentToolKit.setSpinnerDoubleValue(VipPrice2PercentageSpinner,Double.parseDouble(VipPrice2) / Double.parseDouble(BatchPriceText.getText()));
                ComponentToolKit.setDoubleSpinnerDisable(VipPrice3PercentageSpinner,false);
                ComponentToolKit.setTextFieldDisable(VipPrice3Text,false);
            }else{
                ComponentToolKit.setSpinnerDoubleValue(VipPrice2PercentageSpinner,1.00);
                setSpinnerValueAndDisable(VipPrice3PercentageSpinner, 1.00, true);
                ComponentToolKit.setTextFieldDisable(VipPrice3Text,true);
            }
        }
    }
    /** Spinner Key Released - Vip金額3 */
    @FXML protected void VipPrice3PercentageKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())) {
            VipPrice3Text.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * VipPrice3PercentageSpinner.getValue()));
            VipPrice3Text.requestFocus();
        }
    }
    /** Spinner Mouse Clicked - Vip金額3 */
    @FXML protected void VipPrice3PercentageMouseClicked(){
        VipPrice3Text.setText(ToolKit.RoundingString(true,Double.parseDouble(BatchPriceText.getText()) * VipPrice3PercentageSpinner.getValue()));
    }
    /** TextField Key Pressed - Vip金額3 */
    @FXML protected void VipPrice3KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && ToolKit.isDouble(BatchPriceText.getText())){
            if(ToolKit.isDouble(VipPrice3Text.getText()))    SafetyStockText.requestFocus();
            else    DialogUI.MessageDialog("VipPrice3金額錯誤");
        }
    }
    /** TextField Key Released - Vip金額3 */
    @FXML void VipPrice3KeyReleased(KeyEvent KeyEvent){
        if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null){
            SaveProduct();
        }else{
            String VipPrice3 = VipPrice3Text.getText();
            if(ToolKit.isDouble(VipPrice3) && !VipPrice3.equals(""))
                ComponentToolKit.setSpinnerDoubleValue(VipPrice3PercentageSpinner,Double.parseDouble(VipPrice3) / Double.parseDouble(BatchPriceText.getText()));
            else
                ComponentToolKit.setSpinnerDoubleValue(VipPrice3PercentageSpinner,1.00);
        }
    }
    /** TextField Key Pressed - 安全存量 */
    @FXML protected void SafetyStockKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   RemarkText.requestFocus();   }
    /** TextField Key Released - 安全存量 */
    @FXML protected void SafetyStockKeyReleased(KeyEvent KeyEvent){ if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null)  SaveProduct();   }
    /** Button Mouse Clicked - 商品描述 */
    @FXML protected void ProductDescribeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ProductInfo_Bean == null){
                DialogUI.MessageDialog("※ 未選擇商品");
                return;
            }
            CallFXML.ShowHTMLEditor(MainStage,this, ProductInfo_Bean,null);
        }
    }
    @FXML protected void ProductSpecificationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ProductInfo_Bean == null){
                DialogUI.MessageDialog("※ 未選擇商品");
                return;
            }
            CallFXML.ShowProductSpecification(MainStage,ProductInfo_Bean.getISBN());
        }
    }
    /** TextField Key Released - 備註 */
    @FXML protected void RemarkKeyReleased(KeyEvent KeyEvent){  if(isManageProduct_Search() && KeyCombine.match(KeyEvent) && ProductInfo_Bean != null)  SaveProduct();   }
    /** ComboBox On Action - 儲存區 */
    @FXML protected void ProductAreaOnAction(){
        ComponentToolKit.getComboBoxItemsStringFormat(ProductFloor_ComboBox).setAll(BookCase.get(ProductArea_ComboBox.getSelectionModel().getSelectedItem()));
        ProductFloor_ComboBox.getSelectionModel().selectFirst();
    }
    /** Button Mouse Clicked - 商品碼(標籤)設定設定 */
    @FXML protected void ProductTagSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ManageProductStatus == Product_Enum.ManageProductStatus.建立){
                DialogUI.MessageDialog("需建立商品後才能使用");
            }else{
                if(FirmCodeText.getText().equals(""))
                    DialogUI.MessageDialog("※ 未設定商品碼!");
                else {
                    ProductInfo_Bean.setProductTag(Product_Model.getProductTag(ProductInfo_Bean.getISBN(), ProductInfo_Bean.getFirmCode()));
                    CallFXML.ShowProductTagSetting(MainStage, ProductInfo_Bean.getISBN(), FirmCodeText.getText(), ProductInfo_Bean.getProductTag());
                }
            }
        }
    }

    /** Button Mouse Clicked - 拍賣類別設定 */
    @FXML protected void BidCategorySettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if (ISBNText.getText().equals("") || ISBNText.getText().length() != 13) {
                if (ManageProductStatus == Product_Enum.ManageProductStatus.建立) DialogUI.MessageDialog("※ ISBN 未設定!");
                else if (ManageProductStatus == Product_Enum.ManageProductStatus.搜尋) DialogUI.MessageDialog("※ 未選擇商品!");
            } else CallFXML.ShowBidCategorySetting(MainStage, ProductInfo_Bean, this);
        }
    }
    /** TextField Key Pressed - 搜尋關鍵字 */
    @FXML protected void SearchKeyWordKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String SearchKeyWord = SearchKeyWordText.getText();
            if (!SearchKeyWord.equals("")) {
                Task Task = SearchProductTask(SearchKeyWord, AdvancedSearchMap, false, "");
                SearchProgressBar.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }else
                DialogUI.MessageDialog("※ 請輸入搜尋關鍵字");
        }
    }
    /** Button Mouse Clicked - 日期搜尋 */
    @FXML protected void KeyinDateSearchMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String KeyinDateSearch = ComponentToolKit.getDatePickerValue(KeyinDateSearch_DatePicker, "yyyy-MM-dd");
            String SearchKeyWord = SearchKeyWordText.getText();
            Task Task = SearchProductTask(SearchKeyWord, AdvancedSearchMap, true, KeyinDateSearch);
            SearchProgressBar.progressProperty().bind(Task.progressProperty());
            new Thread(Task).start();
        }
    }
    /** Button Mouse Clicked - 進階搜尋 */
    @FXML protected void AdvancedSearchMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String SearchKeyWord = SearchKeyWordText.getText();
            if (!SearchKeyWord.equals("")) {
                Task Task = SearchProductTask(SearchKeyWord, AdvancedSearchMap, false, "");
                SearchProgressBar.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }else
                DialogUI.MessageDialog("※ 請輸入搜尋關鍵字");
        }
    }
    private Task SearchProductTask(String SearchKeyWord, JSONObject AdvancedSearchMap, boolean KeyinDateSearch, String KeyinDate){
        return new Task() {
            @Override public Void call(){
                boolean isAdvanceSearchClick = false;
                for(Product_Enum.AdvancedSearchColumn AdvancedSearchColumn : Product_Enum.AdvancedSearchColumn.values()){
                    if(AdvancedSearchMap.getBoolean(AdvancedSearchColumn.name())){
                        isAdvanceSearchClick = true;
                        break;
                    }
                }
                if(!isAdvanceSearchClick) {
                    Platform.runLater(() -> DialogUI.MessageDialog("※ 請選擇進階搜尋條件"));
                    return null;
                }
                ComponentToolKit.setProgressBarVisible(SearchProgressBar,true);
                TableView.getItems().clear();
                initialComponent();
                ObservableList<ProductInfo_Bean> ObservableList = Product_Model.SearchProduct(SearchKeyWord, AdvancedSearchMap, KeyinDateSearch, KeyinDate);
                ComponentToolKit.setProgressBarVisible(SearchProgressBar,false);
                if(ObservableList.size() != 0)  TableView.setItems(ObservableList);
                else    Platform.runLater(() -> DialogUI.MessageDialog("※ 無資料!"));
                return null;
            }
        };
    }
    /** Button Mouse Clicked - 儲存修改 */
    @FXML protected void ModifyProductMouseClicked(MouseEvent MouseEvent){   if(KeyPressed.isMouseLeftClicked(MouseEvent))  SaveProduct();  }
    /** Button Mouse Clicked - 儲存圖片 */
    @FXML protected void SaveProductPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Task Task = SaveProductPictureTask(ProductInfo_Bean);
            SaveProductPictureProgressBar.progressProperty().bind(Task.progressProperty());
            new Thread(Task).start();
        }
    }
    private Task SaveProductPictureTask(ProductInfo_Bean ProductInfo_Bean){
        return new Task() {
            @Override public Void call(){
                ComponentToolKit.setButtonDisable(SaveProductPicture_Button,true);
                Platform.runLater(()-> SaveProductPictureStatusLabel.setText("儲存中"));
                ComponentToolKit.setProgressBarVisible(SaveProductPictureProgressBar,true);
                if(Product_Model.saveProductPicture(ProductInfo_Bean))    Platform.runLater(() -> SaveProductPictureStatusLabel.setText("儲存完成"));
                else    Platform.runLater(() -> SaveProductPictureStatusLabel.setText("儲存失敗"));
                ComponentToolKit.setButtonDisable(SaveProductPicture_Button,false);
                ComponentToolKit.setProgressBarVisible(SaveProductPictureProgressBar,false);
                return null;
            }
        };
    }
    /** Button Mouse Clicked - 清空表格 */
    @FXML protected void ClearComponentMouseClicked(MouseEvent MouseEvent){ if(KeyPressed.isMouseLeftClicked(MouseEvent))   initialComponent(); }
    /** Button Mouse Clicked - 寫入 */
    @FXML protected void InsertProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(isISBNError()) {
                DialogUI.MessageDialog("※ 店內碼錯誤");
                return;
            }else if(isVendorError()){
                DialogUI.MessageDialog("※ 查無廠商");
                return;
            }else if(isFirstCategoryError()){
                DialogUI.MessageDialog("※ 無此大類別");
                return;
            }else if(isProductNameError()){
                DialogUI.MessageDialog("※ 請輸入品名");
                return;
            }else if(isPriceError()) {
                DialogUI.MessageDialog("※ 金額錯誤");
                return;
            }else if(!Product_Model.isISBNVerifyCorrect(ISBNText.getText())){
                DialogUI.AlarmDialog("※ 【ISBN】不符合標準規則");
                return;
            }
            RecordProductInfo();
            if(!Product_Model.isISBNExist(ProductInfo_Bean.getISBN(),false)){
                Task Task = InsertProductTask(ProductInfo_Bean, this);
                InsertProductProgressBar.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }else
                DialogUI.MessageDialog("※ 新增失敗，店內碼已存在!");
            //  Insert BidCategory
        }
    }
    private Task InsertProductTask(ProductInfo_Bean ProductInfo_Bean, ManageProductInfo_Controller ThisObj){
        return new Task() {
            @Override public Void call(){
                ComponentToolKit.setButtonDisable(InsertProduct_Button,true);
                Platform.runLater(()-> InsertProductStatusLabel.setText("寫入中"));
                ComponentToolKit.setProgressBarVisible(InsertProductProgressBar,true);
                if(Product_Model.insertProduct(ProductInfo_Bean,false,false)){
                    Platform.runLater(()-> {
                        ERPApplication.Logger.info("[新增商品成功] ↓ ↓ ↓ ");
                        ERPApplication.Logger.info("商品名稱：" + ProductInfo_Bean.getProductName());
                        ERPApplication.Logger.info("ISBN：" + ProductInfo_Bean.getISBN());
                        ERPApplication.Logger.info("ProductCode：" + ProductInfo_Bean.getProductCode());
                        ERPApplication.Logger.info("-------------------------------------------------------------");
                        InsertProductStatusLabel.setText("寫入完成");
                        DialogUI.MessageDialog("※ 新增商品成功");
                        initialComponent();
                        TableView.getItems().add(ProductInfo_Bean);
                        ThisObj.ProductInfo_Bean = new ProductInfo_Bean();
                    });
                }else{
                    Platform.runLater(()-> {
                        InsertProductStatusLabel.setText("寫入失敗");
                        DialogUI.MessageDialog("※ 新增商品失敗");
                    });
                }
                ComponentToolKit.setButtonDisable(InsertProduct_Button,false);
                ComponentToolKit.setProgressBarVisible(InsertProductProgressBar,false);
                return null;
            }
        };
    }
    private void createManageProductInfo_Bean(){ ProductInfo_Bean = new ProductInfo_Bean();  }
    public void SaveProduct(){
        if(isProductNameError()){
            DialogUI.MessageDialog("※ 請輸入品名");
            return;
        }else if(isPriceError()) {
            DialogUI.MessageDialog("※ 金額錯誤");
            return;
        }
        if(FirmCodeText.getText().equals("") && ProductInfo_Bean.isExistProductTag() && !DialogUI.ConfirmDialog("※ 未輸入商品碼! 將一併移除商品碼(標籤)，是否繼續 ?",true,false,0,0)){
            return;
        }
        if(!DialogUI.ConfirmDialog("確定要儲存店內碼 : " + ProductInfo_Bean.getISBN() + "  資訊 ?",true,false,0,0)){
            return;
        }
        ProductInfo_Bean.setInternationalCode(InternationalCodeText.getText());
        ProductInfo_Bean.setFirmCode(FirmCodeText.getText());
        ProductInfo_Bean.setProductName(ProductNameText.getText());
        ProductInfo_Bean.setUnit(UnitText.getText());
        ProductInfo_Bean.setSecondCategory(SecondCategoryIDText.getText());
        ProductInfo_Bean.setThirdCategory(ThirdCategoryIDText.getText());
        ProductInfo_Bean.setBatchPrice(BatchPriceText.getText());
        ProductInfo_Bean.setSinglePrice(SinglePriceText.getText());
        ProductInfo_Bean.setPricing(PricingText.getText());
        ProductInfo_Bean.setVipPrice1(VipPrice1Text.getText());
        ProductInfo_Bean.setVipPrice2(VipPrice2Text.getText());
        ProductInfo_Bean.setVipPrice3(VipPrice3Text.getText());
        ProductInfo_Bean.setSafetyStock(SafetyStockText.getText());
        ProductInfo_Bean.setDiscount(DiscountPercentageSpinner.getValue());
        ProductInfo_Bean.setRemark(RemarkText.getText());

        ProductInfo_Bean.setProductArea(ProductArea_ComboBox.getSelectionModel().getSelectedItem());
        ProductInfo_Bean.setProductFloor(ProductFloor_ComboBox.getSelectionModel().getSelectedItem());
        if(ProductInfo_Bean.getFirmCode().equals("")) {
            if(ProductInfo_Bean.getProductTag() != null)
                ProductInfo_Bean.getProductTag().clear();
        }else {
            ObservableList<String> productTagList = ProductInfo_Bean.getProductTag();
            if(productTagList == null) {
                ProductInfo_Bean.setProductTag(Product_Model.getProductTag(ProductInfo_Bean.getISBN(), ProductInfo_Bean.getFirmCode()));
                productTagList = ProductInfo_Bean.getProductTag();
            }
            productTagList.add(ProductInfo_Bean.getFirmCode());
        }
        if(Product_Model.modifyProduct(ProductInfo_Bean,false,false)){
            ERPApplication.Logger.info("[修改商品成功] ↓ ↓ ↓ ");
            ERPApplication.Logger.info("商品名稱：" + ProductInfo_Bean.getProductName());
            ERPApplication.Logger.info("ProductCode：" + ProductInfo_Bean.getProductCode());
            ERPApplication.Logger.info("-------------------------------------------------------------");
            ERPApplication.Logger.info("-------------------------------------------------------------");
            DialogUI.MessageDialog("※ 修改商品成功");
        }
        else   DialogUI.MessageDialog("※ 修改商品失敗");
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductInfo_Bean ProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
            if (ProductInfo_Bean != null) {
                if(ManageProductStatus == Product_Enum.ManageProductStatus.搜尋 && ProductInfo_Bean.isCheckBoxSelect()){
                    DialogUI.MessageDialog("已存在於待上架");
                    return;
                }
                if(Product_Model.isProductExistInOrder(ProductInfo_Bean.getISBN())){
                    DialogUI.AlarmDialog("※ 刪除被拒，已存在於貨單");
                    return;
                }
                if(DialogUI.ConfirmDialog("※ 確認刪除 ?",false,false,0,0)) {
                    if(Product_Model.deleteProduct(ProductInfo_Bean.getISBN())){
                        ComponentToolKit.removeManageProductTableViewItem(TableView, ProductInfo_Bean);
                        if (ManageProductStatus == Product_Enum.ManageProductStatus.搜尋) initialComponent();
                        ERPApplication.Logger.info("[刪除商品成功] ↓ ↓ ↓ ");
                        ERPApplication.Logger.info("商品名稱：" + ProductInfo_Bean.getProductName());
                        ERPApplication.Logger.info("ISBN：" + ProductInfo_Bean.getISBN());
                        ERPApplication.Logger.info("ProductCode：" + ProductInfo_Bean.getProductCode());
                        ERPApplication.Logger.info("-------------------------------------------------------------");
                        DialogUI.MessageDialog("※ 刪除商品成功");
                    }else
                        DialogUI.MessageDialog("※ 刪除商品失敗");
                }
            } else DialogUI.MessageDialog("※ 請選擇商品");
        }
    }
    /** Button Mouse Clicked - 查詢明細 */
    @FXML protected void SearchTransactionDetailMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductInfo_Bean ProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
            if (ProductInfo_Bean != null) {
                CallFXML.ShowTransactionDetail(MainStage,this,null);
            }
        }
    }
    public ProductInfo_Bean getSelectProductBean(){
        return ComponentToolKit.getManageProductTableViewSelectItem(TableView);
    }
    public void refreshProductInStockByTransactionDetail(int newInStock){
        ProductInfo_Bean ProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
        ProductInfo_Bean.setInStock(String.valueOf(newInStock));
        InStockText.setText(ProductInfo_Bean.getInStock());
        TableView.refresh();
    }
    /** Button Mouse Clicked - 標籤列印 */
    @FXML protected void PrintProductLabelMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductInfo_Bean ProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
            if (ProductInfo_Bean != null) {
                if (ProductInfo_Bean.getISBN().length() != 13) DialogUI.MessageDialog("※ ISBN錯誤!");
                else CallJAR.Label_Printer(ProductInfo_Bean.getISBN());
            }
        }
    }
    /** Button Mouse Clicked - 待上架 */
    @FXML protected void WaitingOnShelfMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ArrayList<ProductInfo_Bean> WaitingOnShelfList = new ArrayList<>();
            ObservableList<ProductInfo_Bean> ProductList = ComponentToolKit.getManageProductTableViewItemList(TableView);
            for (ProductInfo_Bean ProductInfo_Bean : ProductList) {
                if (!ProductInfo_Bean.isCheckBoxDisable() && ProductInfo_Bean.isCheckBoxSelect())
                    WaitingOnShelfList.add(ProductInfo_Bean);
            }
            if (WaitingOnShelfList.size() == 0) DialogUI.MessageDialog("※ 未選擇商品");
            else{
                ArrayList<ProductInfo_Bean> errorProductList = null;
                for(ProductInfo_Bean ProductInfo_Bean : WaitingOnShelfList){
                    if(!Product_Model.insertWaitingOnShelf(ProductInfo_Bean)) {
                        if (errorProductList == null) errorProductList = new ArrayList<>();
                        errorProductList.add(ProductInfo_Bean);
                        ProductInfo_Bean.setCheckBoxSelect(false);
                    }else
                        ProductInfo_Bean.setWaitingOnShelf(true);
                }
                if(errorProductList != null)    DialogUI.MessageDialog("失敗筆數 = " + errorProductList.size());
                else    DialogUI.MessageDialog("※ 待上架完成");
                for (ProductInfo_Bean ProductInfo_Bean : ProductList)
                    if (!ProductInfo_Bean.isCheckBoxDisable() && ProductInfo_Bean.isWaitingOnShelf())  ProductInfo_Bean.setCheckBoxDisable(true);
            }
        }
    }
    /** Button Mouse Clicked - 更新列表 */
    @FXML protected void RefreshTableViewMouseClicked(MouseEvent MouseEvent) throws Exception{    if(KeyPressed.isMouseLeftClicked(MouseEvent))    saveTableViewSetting();   }
    /** Button Mouse Clicked - 拍賣類別套用 */
    @FXML protected void ApplyBidCategoryMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ProductInfo_Bean selectProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
            if(selectProductInfo_Bean == null)
                DialogUI.MessageDialog("※ 請選擇商品");
            else if(selectProductInfo_Bean.getShopeeCategory() == null)
                DialogUI.MessageDialog("※ 「" + selectProductInfo_Bean.getISBN() + "  " + selectProductInfo_Bean.getProductName() + "」未設定拍賣類別");
            else{
                boolean isProductSelect = false;
                ObservableList<ProductInfo_Bean> productList = ComponentToolKit.getManageProductTableViewItemList(TableView);
                for(ProductInfo_Bean ProductInfo_Bean : productList){
                    if(!ProductInfo_Bean.isCheckBoxDisable() && ProductInfo_Bean.isCheckBoxSelect()){
                        isProductSelect = true;
                        break;
                    }
                }
                if(!isProductSelect)
                    DialogUI.MessageDialog("※ 請選擇欲套用的商品");
                else if(DialogUI.ConfirmDialog("是否將「" + selectProductInfo_Bean.getISBN() + "  " + selectProductInfo_Bean.getProductName() + "」拍賣類別套用到所有「勾選商品」?",true,false,0,0)){
                    for(ProductInfo_Bean ProductInfo_Bean : productList){
                        if(ProductInfo_Bean != selectProductInfo_Bean && !ProductInfo_Bean.isCheckBoxDisable() && ProductInfo_Bean.isCheckBoxSelect())
                            Product_Model.ApplyProductBidCategory(selectProductInfo_Bean, ProductInfo_Bean);
                    }
                    DialogUI.MessageDialog("※ 套用完成 !");
                }
            }
        }
    }
    /** Button Mouse Clicked - [圖片1] 載入 */
    @FXML protected void Picture1LoadMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                FileChooser FileChooser = ComponentToolKit.setFileChooser("載入圖片");
                File File = FileChooser.showOpenDialog(ComponentToolKit.setStage());
                if (File != null) {
                    String Base64 = ToolKit.generateBase64(File);
                    ProductInfo_Bean.setPicture1(Base64);
                    Picture1ImageView.setImage(ToolKit.decodeBase64ToImage(Base64));
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - [圖片1] 刪除 */
    @FXML protected void Picture1DeleteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductInfo_Bean.setPicture1("");
            Picture1ImageView.setImage(null);
        }
    }
    /** ImageView Mouse Clicked - [圖片1] 圖片放大 */
    @FXML protected void ShowPicture1MouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                String Picture;
                if (ProductInfo_Bean != null) {
                    Picture = ProductInfo_Bean.getPicture1();
                    if (isPictureExist(Picture))
                        CallFXML.ShowPicture(MainStage,null, getPictureList(Picture),null,this);
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - [圖片2] 載入 */
    @FXML protected void Picture2LoadMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                FileChooser FileChooser = ComponentToolKit.setFileChooser("載入圖片");
                File File = FileChooser.showOpenDialog(ComponentToolKit.setStage());
                if (File != null) {
                    String Base64 = ToolKit.generateBase64(File);
                    ProductInfo_Bean.setPicture2(Base64);
                    Picture2ImageView.setImage(ToolKit.decodeBase64ToImage(Base64));
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - [圖片2] 刪除 */
    @FXML protected void Picture2DeleteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductInfo_Bean.setPicture2("");
            Picture2ImageView.setImage(null);
        }
    }
    /** ImageView Mouse Clicked - [圖片2] 圖片放大 */
    @FXML protected void ShowPicture2MouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                String Picture;
                if (ProductInfo_Bean != null) {
                    Picture = ProductInfo_Bean.getPicture2();
                    if (isPictureExist(Picture))
                        CallFXML.ShowPicture(MainStage,null, getPictureList(Picture), null,this);
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - [圖片3] 載入 */
    @FXML protected void Picture3LoadMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                FileChooser FileChooser = ComponentToolKit.setFileChooser("載入圖片");
                File File = FileChooser.showOpenDialog(ComponentToolKit.setStage());
                if (File != null) {
                    String Base64 = ToolKit.generateBase64(File);
                    ProductInfo_Bean.setPicture3(Base64);
                    Picture3ImageView.setImage(ToolKit.decodeBase64ToImage(Base64));
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - [圖片3] 刪除 */
    @FXML protected void Picture3DeleteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductInfo_Bean.setPicture3("");
            Picture3ImageView.setImage(null);
        }
    }
    /** ImageView Mouse Clicked - [圖片3] 圖片放大 */
    @FXML protected void ShowPicture3MouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                String Picture;
                if (ProductInfo_Bean != null) {
                    Picture = ProductInfo_Bean.getPicture3();
                    if (isPictureExist(Picture))
                        CallFXML.ShowPicture(MainStage,null, getPictureList(Picture),null,this);
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        this.ProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
        if((KeyPressed.isUpKeyPressed(KeyEvent) || KeyPressed.isDownKeyPressed(KeyEvent)) && ProductInfo_Bean != null){
                setProductInfo(ProductInfo_Bean);
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(isManageProduct_Search() && KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.ProductInfo_Bean = ComponentToolKit.getManageProductTableViewSelectItem(TableView);
            if (ProductInfo_Bean != null) {
                setProductInfo(ProductInfo_Bean);
                setComponentDisable(false);
            }
        }
    }
    private void initialData(){
        ISBNText.setText(ProductInfo_Bean.getISBN());
        generateVendorInfoByISBN(ProductInfo_Bean.getISBN());
        ProductNameText.setText(ProductInfo_Bean.getProductName());
        BatchPriceText.setText(ProductInfo_Bean.getBatchPrice());
        SinglePricePercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getSinglePrice()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        SinglePriceText.setText(ProductInfo_Bean.getSinglePrice());
        PricingPercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getPricing()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        PricingText.setText(ProductInfo_Bean.getPricing());
        VipPrice1PercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getVipPrice1()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        VipPrice1Text.setText(ProductInfo_Bean.getVipPrice1());
        VipPrice2PercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getVipPrice2()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        VipPrice2Text.setText(ProductInfo_Bean.getVipPrice2());
        VipPrice3PercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getVipPrice3()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        VipPrice3Text.setText(ProductInfo_Bean.getVipPrice3());
        ComponentToolKit.setDoubleSpinnerDisable(SinglePricePercentageSpinner,false);
        ComponentToolKit.setTextFieldDisable(SinglePriceText,false);
        ComponentToolKit.setDoubleSpinnerDisable(PricingPercentageSpinner,false);
        ComponentToolKit.setTextFieldDisable(PricingText,false);
        ComponentToolKit.setDoubleSpinnerDisable(VipPrice1PercentageSpinner,false);
        ComponentToolKit.setTextFieldDisable(VipPrice1Text,false);
        ComponentToolKit.setDoubleSpinnerDisable(VipPrice2PercentageSpinner,false);
        ComponentToolKit.setTextFieldDisable(VipPrice2Text,false);
        ComponentToolKit.setDoubleSpinnerDisable(VipPrice3PercentageSpinner,false);
        ComponentToolKit.setTextFieldDisable(VipPrice3Text,false);

        if(ProductInfo_Bean.getPicture1() != null)  Picture1ImageView.setImage(ToolKit.decodeBase64ToImage(ProductInfo_Bean.getPicture1()));
        else Picture1ImageView.setImage(null);
    }
    private void setProductInfo(ProductInfo_Bean ProductInfo_Bean){
        ISBNText.setText(ProductInfo_Bean.getISBN());
        InternationalCodeText.setText(ProductInfo_Bean.getInternationalCode());
        FirmCodeText.setText(ProductInfo_Bean.getFirmCode());
        ProductCodeText.setText(ProductInfo_Bean.getProductCode());
        ProductNameText.setText(ProductInfo_Bean.getProductName());
        UnitText.setText(ProductInfo_Bean.getUnit());
        VendorCodeText.setText(ProductInfo_Bean.getVendorCode());
        VendorNameText.setText(ProductInfo_Bean.getVendorName());
        FirstCategoryIDText.setText(ProductInfo_Bean.getFirstCategory());
        FirstCategoryNameText.setText(Product_Model.getCategoryName(ProductInfo_Bean.getFirstCategory(), Product_Enum.CategoryLayer.大類別));
        SecondCategoryIDText.setText(ProductInfo_Bean.getSecondCategory());
        SecondCategoryNameText.setText(Product_Model.getCategoryName(ProductInfo_Bean.getSecondCategory(), Product_Enum.CategoryLayer.中類別));
        ThirdCategoryIDText.setText(ProductInfo_Bean.getThirdCategory());
        ThirdCategoryNameText.setText(Product_Model.getCategoryName(ProductInfo_Bean.getThirdCategory(), Product_Enum.CategoryLayer.小類別));
        BatchPriceText.setText(ProductInfo_Bean.getBatchPrice());
        SinglePricePercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getSinglePrice()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        SinglePriceText.setText(ProductInfo_Bean.getSinglePrice());
        PricingPercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getPricing()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        PricingText.setText(ProductInfo_Bean.getPricing());
        VipPrice1PercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getVipPrice1()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        VipPrice1Text.setText(ProductInfo_Bean.getVipPrice1());
        VipPrice2PercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getVipPrice2()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        VipPrice2Text.setText(ProductInfo_Bean.getVipPrice2());
        VipPrice3PercentageSpinner.getValueFactory().setValue(Double.parseDouble(ProductInfo_Bean.getVipPrice3()) / Double.parseDouble(ProductInfo_Bean.getBatchPrice()));
        VipPrice3Text.setText(ProductInfo_Bean.getVipPrice3());
        InStockText.setText(ProductInfo_Bean.getInStock());
        SafetyStockText.setText(ProductInfo_Bean.getSafetyStock());
        DiscountPercentageSpinner.getValueFactory().setValue(ProductInfo_Bean.getDiscount());
        InventoryDateText.setText(ProductInfo_Bean.getInventoryDate());
        PurchaseDateText.setText(ProductInfo_Bean.getPurchaseDate());
        SaleDateText.setText(ProductInfo_Bean.getSaleDate());
        ShipmentDateText.setText(ProductInfo_Bean.getShipmentDate());
        UpdateDateText.setText(ProductInfo_Bean.getUpdateDate());
        RemarkText.setText(ProductInfo_Bean.getRemark());

        if(ProductInfo_Bean.getProductArea() != null)   ProductArea_ComboBox.getSelectionModel().select(ProductInfo_Bean.getProductArea());
        else    ProductArea_ComboBox.getSelectionModel().selectFirst();
        if(ProductInfo_Bean.getProductFloor() != null)  ProductFloor_ComboBox.getSelectionModel().select(ProductInfo_Bean.getProductFloor());
        else    ProductFloor_ComboBox.getSelectionModel().selectFirst();
        if(ProductInfo_Bean.getPicture1() != null)  Picture1ImageView.setImage(ToolKit.decodeBase64ToImage(ProductInfo_Bean.getPicture1()));
        else Picture1ImageView.setImage(null);
        if(ProductInfo_Bean.getPicture2() != null)  Picture2ImageView.setImage(ToolKit.decodeBase64ToImage(ProductInfo_Bean.getPicture2()));
        else Picture2ImageView.setImage(null);
        if(ProductInfo_Bean.getPicture3() != null)  Picture3ImageView.setImage(ToolKit.decodeBase64ToImage(ProductInfo_Bean.getPicture3()));
        else Picture3ImageView.setImage(null);
    }
    private void RecordProductInfo(){
        ProductInfo_Bean.setISBN(ISBNText.getText());
        ProductInfo_Bean.setInternationalCode(InternationalCodeText.getText());
        ProductInfo_Bean.setFirmCode(FirmCodeText.getText());
        ProductInfo_Bean.setProductName(ProductNameText.getText());
        ProductInfo_Bean.setUnit(UnitText.getText());
        ProductInfo_Bean.setVendorCode(VendorCodeText.getText());
        ProductInfo_Bean.setVendorName(VendorNameText.getText());
        ProductInfo_Bean.setFirstCategory(FirstCategoryIDText.getText());
        ProductInfo_Bean.setSecondCategory(SecondCategoryIDText.getText());
        ProductInfo_Bean.setThirdCategory(ThirdCategoryIDText.getText());
        ProductInfo_Bean.setBatchPrice(BatchPriceText.getText());
        ProductInfo_Bean.setSinglePrice(SinglePriceText.getText());
        ProductInfo_Bean.setPricing(PricingText.getText());
        ProductInfo_Bean.setVipPrice1(VipPrice1Text.getText());
        ProductInfo_Bean.setVipPrice2(VipPrice2Text.getText());
        ProductInfo_Bean.setVipPrice3(VipPrice3Text.getText());
        ProductInfo_Bean.setInStock(InStockText.getText());
        ProductInfo_Bean.setSafetyStock(SafetyStockText.getText());
        ProductInfo_Bean.setDiscount(DiscountPercentageSpinner.getValue());
        ProductInfo_Bean.setInventoryDate(null);
        ProductInfo_Bean.setPurchaseDate(null);
        ProductInfo_Bean.setSaleDate(null);
        ProductInfo_Bean.setShipmentDate(null);
        ProductInfo_Bean.setUpdateDate(null);
        if(ProductInfo_Bean.getDescribe() == null)
            ProductInfo_Bean.setDescribe("");
        ProductInfo_Bean.setRemark(RemarkText.getText());

        ProductInfo_Bean.setProductArea(ProductArea_ComboBox.getSelectionModel().getSelectedItem());
        ProductInfo_Bean.setProductFloor(ProductFloor_ComboBox.getSelectionModel().getSelectedItem());

        if(ProductInfo_Bean.getRutenCategory() == null)
            ProductInfo_Bean.setRutenCategory("");
        if(ProductInfo_Bean.getYahooCategory() == null)
            ProductInfo_Bean.setYahooCategory("");
    }
    private void initialComponent(){
        ProductInfo_Bean = null;
        ISBNText.setText("");
        InternationalCodeText.setText("");
        FirmCodeText.setText("");
        ProductNameText.setText("");
        UnitText.setText("");
        VendorCodeText.setText("");
        VendorNameText.setText("");
        FirstCategoryIDText.setText("");
        FirstCategoryNameText.setText("");
        SecondCategoryIDText.setText("");
        SecondCategoryNameText.setText("");
        ThirdCategoryIDText.setText("");
        ThirdCategoryNameText.setText("");
        BatchPriceText.setText("");
        setSpinnerValueAndDisable(SinglePricePercentageSpinner,1.00,true);
        SinglePriceText.setText("");
        ComponentToolKit.setTextFieldDisable(SinglePriceText,true);
        setSpinnerValueAndDisable(PricingPercentageSpinner,1.00,true);
        PricingText.setText("");
        ComponentToolKit.setTextFieldDisable(PricingText,true);
        setSpinnerValueAndDisable(VipPrice1PercentageSpinner,1.00,true);
        VipPrice1Text.setText("");
        ComponentToolKit.setTextFieldDisable(VipPrice1Text,true);
        setSpinnerValueAndDisable(VipPrice2PercentageSpinner,1.00,true);
        VipPrice2Text.setText("");
        ComponentToolKit.setTextFieldDisable(VipPrice2Text,true);
        setSpinnerValueAndDisable(VipPrice3PercentageSpinner,1.00,true);
        VipPrice3Text.setText("");
        ComponentToolKit.setTextFieldDisable(VipPrice3Text,true);
        InStockText.setText("0");
        SafetyStockText.setText("0");
        ComponentToolKit.setSpinnerDoubleValue(DiscountPercentageSpinner,1.00);
        InventoryDateText.setText("");
        PurchaseDateText.setText("");
        SaleDateText.setText("");
        ShipmentDateText.setText("");
        UpdateDateText.setText("");
        RemarkText.setText("");
        Platform.runLater(() -> ProductArea_ComboBox.getSelectionModel().selectFirst());
        Platform.runLater(() -> ProductFloor_ComboBox.getSelectionModel().selectFirst());

        Picture1ImageView.setImage(null);
        Picture2ImageView.setImage(null);
        Picture3ImageView.setImage(null);
        if(ManageProductStatus == Product_Enum.ManageProductStatus.建立){
            InsertProductStatusLabel.setText("");
            ISBNText.requestFocus();
        }else if(ManageProductStatus == Product_Enum.ManageProductStatus.搜尋){
            Platform.runLater(() -> SaveProductPictureStatusLabel.setText(""));
            setComponentDisable(true);
        }
    }
    private void setComponentDisable(boolean Disable){
        ComponentToolKit.setButtonDisable(ProductTagSetting_Button,Disable);
//        ComponentToolKit.setButtonDisable(BidCategorySetting_Button,Disable);
        ComponentToolKit.setButtonDisable(ModifyProduct_Button,Disable);
        ComponentToolKit.setButtonDisable(SaveProductPicture_Button,Disable);
        ComponentToolKit.setButtonDisable(DeleteProduct_Button,Disable);
        ComponentToolKit.setButtonDisable(SearchTransactionDetail_Button,Disable);
        ComponentToolKit.setButtonDisable(PrintProductLabel_Button,Disable);
//        ComponentToolKit.setButtonDisable(Shelf_Button,Disable);
//        if(ComponentToolKit.getManageProductTableViewItemList(TableView).size() <= 1)   ComponentToolKit.setButtonDisable(ApplyBidCategory_Button,true);
//        else    ComponentToolKit.setButtonDisable(ApplyBidCategory_Button,false);
        ComponentToolKit.setButtonDisable(Picture1Load_Button,Disable);
        ComponentToolKit.setButtonDisable(Picture2Load_Button,Disable);
        ComponentToolKit.setButtonDisable(Picture3Load_Button,Disable);
        ComponentToolKit.setButtonDisable(Picture1Delete_Button,Disable);
        ComponentToolKit.setButtonDisable(Picture2Delete_Button,Disable);
        ComponentToolKit.setButtonDisable(Picture3Delete_Button,Disable);
        ComponentToolKit.setDoubleSpinnerDisable(SinglePricePercentageSpinner, Disable);
        ComponentToolKit.setTextFieldDisable(SinglePriceText,Disable);
        ComponentToolKit.setDoubleSpinnerDisable(PricingPercentageSpinner, Disable);
        ComponentToolKit.setTextFieldDisable(PricingText,Disable);
        ComponentToolKit.setDoubleSpinnerDisable(VipPrice1PercentageSpinner, Disable);
        ComponentToolKit.setTextFieldDisable(VipPrice1Text,Disable);
        ComponentToolKit.setDoubleSpinnerDisable(VipPrice2PercentageSpinner, Disable);
        ComponentToolKit.setTextFieldDisable(VipPrice2Text,Disable);
        ComponentToolKit.setDoubleSpinnerDisable(VipPrice3PercentageSpinner, Disable);
        ComponentToolKit.setTextFieldDisable(VipPrice3Text,Disable);
        ComponentToolKit.setCheckBoxDisable(InventoryDate_CheckBox,Disable);
        ComponentToolKit.setCheckBoxDisable(PurchaseDate_CheckBox,Disable);
        ComponentToolKit.setCheckBoxDisable(SaleDate_CheckBox,Disable);
        ComponentToolKit.setCheckBoxDisable(ShipmentDate_CheckBox,Disable);
        ComponentToolKit.setCheckBoxDisable(UpdateDate_CheckBox,Disable);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(Select_TableColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InternationalCodeColumn,"InternationalCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(FirmCodeColumn,"FirmCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductCodeColumn,"ProductCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(VendorCodeColumn,"VendorCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(VendorNameColumn,"VendorName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(FirstCategoryColumn,"FirstCategory", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SecondCategoryColumn,"SecondCategory", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ThirdCategoryColumn,"ThirdCategory", "CENTER-LEFT", "16",null);
        setStringPriceColumnMicrometerFormat(BatchPriceColumn,"BatchPrice", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(BatchPriceColumn);
        setStringPriceColumnMicrometerFormat(SinglePriceColumn,"SinglePrice", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(SinglePriceColumn);
        setStringPriceColumnMicrometerFormat(PricingColumn,"Pricing", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(PricingColumn);
        setStringPriceColumnMicrometerFormat(VipPrice1Column,"VipPrice1", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(VipPrice1Column);
        setStringPriceColumnMicrometerFormat(VipPrice2Column,"VipPrice2", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(VipPrice2Column);
        setStringPriceColumnMicrometerFormat(VipPrice3Column,"VipPrice3", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(VipPrice3Column);
        ComponentToolKit.setColumnCellValue(InStockColumn,"InStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SafetyStockColumn,"SafetyStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(DiscountColumn,"Discount", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InventoryDateColumn,"InventoryDate","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(PurchaseDateColumn,"PurchaseDate","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(SaleDateColumn,"SaleDate","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ShipmentDateColumn,"ShipmentDate","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(UpdateDateColumn,"UpdateDate","CENTER-LEFT","16",null);
    }
    private void saveTableViewSetting() throws Exception{
        JSONObject TableViewSettingMap = createJsonObject();
        CallConfig.setManageProductInfoJsonConfig(ManageProductInfoJsonConfigKey.商品查詢欄位, TableViewSettingMap);
        refreshTableView(TableViewSettingMap);
    }
    private JSONObject createJsonObject(){
        JSONObject JSONObject = new JSONObject(new LinkedHashMap<>());
        JSONObject.put(ManageProductInfoTableColumn.店內碼.name(),true);
        JSONObject.put(ManageProductInfoTableColumn.國際碼.name(),InternationalCode_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.商品碼.name(),FirmCode_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.ProductCode.name(),ProductCode_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.品名.name(),ProductName_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.單位.name(),Unit_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.廠商編號.name(),VendorCode_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.廠商名稱.name(),VendorName_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.大類別.name(),FirstCategory_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.中類別.name(),SecondCategory_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.小類別.name(),ThirdCategory_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.成本價.name(),BatchPrice_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.單價.name(),SinglePrice_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.市價.name(),Pricing_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.Vip金額1.name(),VipPrice1_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.Vip金額2.name(),VipPrice2_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.Vip金額3.name(),VipPrice3_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.存量.name(),InStock_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.安全存量.name(),SafetyStock_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.折扣數.name(),Discount_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.盤點日期.name(),InventoryDate_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.進貨日期.name(),PurchaseDate_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.銷售日期.name(),SaleDate_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.出貨日期.name(),ShipmentDate_CheckBox.isSelected());
        JSONObject.put(ManageProductInfoTableColumn.更新日期.name(),UpdateDate_CheckBox.isSelected());
        return JSONObject;
    }
    private void setTableColumnSort(){
        setTableColumnCheckBoxStatus();
        refreshTableView(TableViewSettingMap);
    }
    private void setTableColumnCheckBoxStatus(){
        InternationalCode_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.國際碼.name()));
        FirmCode_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.商品碼.name()));
        ProductCode_CheckBox.setSelected(TableViewSettingMap.containsKey(ManageProductInfoTableColumn.ProductCode.name()) ? TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.ProductCode.name()) : false);
        ProductName_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.品名.name()));
        Unit_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.單位.name()));
        VendorCode_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.廠商編號.name()));
        VendorName_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.廠商名稱.name()));
        FirstCategory_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.大類別.name()));
        SecondCategory_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.中類別.name()));
        ThirdCategory_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.小類別.name()));
        BatchPrice_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.成本價.name()));
        SinglePrice_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.單價.name()));
        Pricing_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.市價.name()));
        VipPrice1_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.Vip金額1.name()));
        VipPrice2_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.Vip金額2.name()));
        VipPrice3_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.Vip金額3.name()));
        InStock_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.存量.name()));
        SafetyStock_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.安全存量.name()));
        Discount_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.折扣數.name()));
        InventoryDate_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.盤點日期.name()));
        PurchaseDate_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.進貨日期.name()));
        SaleDate_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.銷售日期.name()));
        ShipmentDate_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.出貨日期.name()));
        UpdateDate_CheckBox.setSelected(TableViewSettingMap.getBoolean(ManageProductInfoTableColumn.更新日期.name()));
    }
    private void refreshTableView(JSONObject TableViewSettingMap){
        TableView.getColumns().clear();
        if(ManageProductStatus != Product_Enum.ManageProductStatus.建立)  TableView.getColumns().add(Select_TableColumn);
        for(String ColumnName : TableViewSettingMap.keySet()){
            if(TableViewSettingMap.getBoolean(ColumnName))  TableView.getColumns().add(getTableColumn(ManageProductInfoTableColumn.valueOf(ColumnName)));
        }
    }
    private TableColumn<ProductInfo_Bean,String> getTableColumn(ManageProductInfoTableColumn ManageProductInfoTableColumn){
        if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.選擇)  return Select_TableColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.店內碼)  return ISBNColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.國際碼)  return InternationalCodeColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.商品碼)  return FirmCodeColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.ProductCode)  return ProductCodeColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.品名)  return ProductNameColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.單位)  return UnitColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.廠商編號)  return VendorCodeColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.廠商名稱)  return VendorNameColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.大類別)  return FirstCategoryColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.中類別)  return SecondCategoryColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.小類別)  return ThirdCategoryColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.成本價)  return BatchPriceColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.單價)  return SinglePriceColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.市價)  return PricingColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.Vip金額1)  return VipPrice1Column;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.Vip金額2)  return VipPrice2Column;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.Vip金額3)  return VipPrice3Column;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.存量)  return InStockColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.安全存量)  return SafetyStockColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.折扣數)  return DiscountColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.盤點日期)  return InventoryDateColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.進貨日期)  return PurchaseDateColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.銷售日期)  return SaleDateColumn;
        else if(ManageProductInfoTableColumn == Product_Enum.ManageProductInfoTableColumn.出貨日期)  return ShipmentDateColumn;
        else return UpdateDateColumn;
    }
    private void ShowEstablishUI(){
        ComponentToolKit.setTextFieldEditable(ISBNText,true);
        ComponentToolKit.setHBoxVisible(establishProduct_describe_HBox, true);
        ComponentToolKit.setVBoxVisible(searchProduct_describeAndSpecification_VBox,false);
        ComponentToolKit.setHBoxVisible(establishProduct_HBox, true);
        ComponentToolKit.setVBoxVisible(searchProduct_VBox,false);
    }
    private void ShowSearchUI(){
        ComponentToolKit.setTextFieldEditable(ISBNText,false);
        ComponentToolKit.setHBoxVisible(establishProduct_describe_HBox, false);
        ComponentToolKit.setVBoxVisible(searchProduct_describeAndSpecification_VBox,true);
        ComponentToolKit.setHBoxVisible(establishProduct_HBox, false);
        ComponentToolKit.setVBoxVisible(searchProduct_VBox,true);
    }
    private void setSpinnerValueAndDisable(Spinner<Double> Spinner, Double Value, boolean Disable){
        ComponentToolKit.setSpinnerDoubleValue(Spinner,Value);
        ComponentToolKit.setDoubleSpinnerDisable(Spinner,Disable);
    }
    private boolean isManageProduct_Establish(){
        return ManageProductStatus == Product_Enum.ManageProductStatus.建立;
    }
    private boolean isManageProduct_Search(){
        return ManageProductStatus == Product_Enum.ManageProductStatus.搜尋;
    }
    private boolean isISBNError(){
        return ISBNText.getText().equals("") || ISBNText.getText().length() != 13;
    }
    private boolean isVendorError(){
        return VendorCodeText.getText().equals("") && VendorNameText.getText().equals("");
    }
    private boolean isFirstCategoryError(){
        return FirstCategoryIDText.getText().equals("") && FirstCategoryNameText.getText().equals("");
    }
    private boolean isProductNameError(){
        return ProductNameText.getText().equals("");
    }
    private boolean isPriceError(){
        return BatchPriceText.getText().equals("") || SinglePriceText.getText().equals("") || PricingText.getText().equals("") ||
                VipPrice1Text.getText().equals("") || VipPrice2Text.getText().equals("") || VipPrice3Text.getText().equals("");
    }
    private boolean isPictureExist(String Picture){
        return ProductInfo_Bean != null && Picture != null && !Picture.equals("");
    }
    private ArrayList<HashMap<Boolean,String>> getPictureList(String Picture){
        ArrayList<HashMap<Boolean,String>> PictureList = new ArrayList<>();
        HashMap<Boolean,String> PictureMap = new HashMap<>();
        PictureMap.put(true, Picture);
        PictureList.add(PictureMap);
        return PictureList;
    }
    private void setAdvancedSearchCheckBoxListener(CheckBox CheckBox){
        CheckBox.selectedProperty().addListener((ov, oldValue, newValue) -> {
            try{
                AdvancedSearchMap = createJsonObject(CheckBox);
                saveAdvancedSearch(AdvancedSearchMap);
            }catch (Exception Ex){
                Ex.printStackTrace();
            }
        });
    }
    private void saveAdvancedSearch(JSONObject AdvancedSearchMap) throws Exception{
        CallConfig.setManageProductInfoJsonConfig(Product_Enum.ManageProductInfoJsonConfigKey.進階搜尋欄位, AdvancedSearchMap);
    }
    private JSONObject createJsonObject(CheckBox CheckBox){
        JSONObject JSONObject = new JSONObject(new LinkedHashMap<>());
        for(Product_Enum.AdvancedSearchColumn AdvancedSearchColumn : Product_Enum.AdvancedSearchColumn.values()){
            if(CheckBox.getId().equals(AdvancedSearchColumn.name()))
                JSONObject.put(AdvancedSearchColumn.name(), CheckBox.isSelected());
            else
                JSONObject.put(AdvancedSearchColumn.name(), AdvancedSearchMap.getBoolean(AdvancedSearchColumn.name()));
        }
        return JSONObject;
    }

    private void setColumnCellValueAndCheckBox(TableColumn<ProductInfo_Bean, String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn, ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<ProductInfo_Bean, String> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                CheckBox checkBox = new CheckBox();
                setGraphic(checkBox);
                getTableView().getItems().get(getIndex()).setCheckBox(checkBox,false);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);

                ProductInfo_Bean selectProductInfo_Bean = getTableView().getItems().get(getIndex());
                selectProductInfo_Bean.setCheckBoxSelect(selectProductInfo_Bean.isWaitingOnShelf());
                selectProductInfo_Bean.setCheckBoxDisable(selectProductInfo_Bean.isWaitingOnShelf());
                checkBox.setOnAction(ActionEvent -> {
                    boolean isProductSelect = false;
                    ObservableList<ProductInfo_Bean> ProductList = ComponentToolKit.getManageProductTableViewItemList(TableView);
                    for(ProductInfo_Bean ProductInfo_Bean : ProductList){
                        if(!ProductInfo_Bean.isCheckBoxDisable() && ProductInfo_Bean.isCheckBoxSelect()) {
                            isProductSelect = true;
                            break;
                        }
                    }
                    ComponentToolKit.setButtonDisable(Shelf_Button,!isProductSelect);
                });
            }else   setGraphic(null);
        }
    }
    private void setStringPriceColumnMicrometerFormat(TableColumn<ProductInfo_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setStringPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setStringPriceColumnMicrometerFormat extends TableCell<ProductInfo_Bean, String> {
        String Alignment, FontSize;
        setStringPriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
            }
        }
    }
}
