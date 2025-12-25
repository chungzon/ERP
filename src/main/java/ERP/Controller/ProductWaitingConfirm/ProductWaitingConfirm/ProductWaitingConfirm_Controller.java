package ERP.Controller.ProductWaitingConfirm.ProductWaitingConfirm;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.ProductWaitConfirm.*;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum.UpOrDownFunction;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.WaitConfirmStatus;
import ERP.Enum.Product.Product_Enum.WaitConfirmTable;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import ERP.Enum.Product.Product_Enum.BigGoFilterSource;
import ERP.Enum.Product.Product_Enum.BigGoPriceSettingColumn;
import ERP.Enum.Product.Product_Enum.EditProductDescribeOrRemark;
import ERP.Enum.Product.Product_Enum.TableViewButtonStatus;
import ERP.ERPApplication;
import ERP.Model.ManageProductCategory.ManageProductCategory_Model;
import ERP.Model.Product.Product_Model;
import ERP.Model.ProductWaitingConfirm.ProductWaitingConfirm_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/** [Controller] Product Waiting Confirm */
public class ProductWaitingConfirm_Controller {
    @FXML HBox ConditionalSearch_HBox;
    @FXML ComboBox<WaitConfirmStatus> WaitConfirmStatus_ComboBox;
    @FXML ComboBox<WaitConfirmTable> WhichTable_ComboBox;
    @FXML ComboBox<Vendor_Bean> Vendor_ComboBox;
    @FXML ComboBox<VendorCategory_Bean> FirstCategory_ComboBox, SecondCategory_ComboBox, ThirdCategory_ComboBox;
    @FXML TextField SinglePrice_Minus_BatchPrice_TextField,ProductName_SearchTextField;
    @FXML CheckBox BatchPriceIncludeTax_CheckBox;
    @FXML Button Search_Button;

    @FXML ImageView PictureImageView;
    @FXML TextField ProductNameText, BatchPriceText, SinglePriceText, PricingText, VipPrice1Text, VipPrice2Text, VipPrice3Text, LowestProfitText_ProductInfo, SupplyStatusText;
    @FXML Label OldSinglePriceText, OldPricingText, OldVipPrice1Text, OldVipPrice2Text, OldVipPrice3Text;
    @FXML Button PicturePrevious_Button, PictureNext_Button, DownloadPicture_Button;
    @FXML Button EditProductRemark_Button, EditProductDescribe_Button;
    @FXML Button SinglePriceNoneTax_Button, PricingNoneTax_Button, VipPrice1NoneTax_Button, VipPrice2NoneTax_Button, VipPrice3NoneTax_Button;
    @FXML Button HandleStoreProduct_Button, SingleProductUpdate_Button;

    @FXML TextField LowestProfitText_ComparePriceInfo, BigGoHyperlinkText, StoreOrBidHyperlinkText, BigGoSearchText;
    @FXML CheckBox GreaterThanBatchPrice_CheckBox, SortByLargeToSmall_CheckBox;
    @FXML TextField GreaterThanBatchPrice_TextField;
    @FXML Button BigGoSearch_Button, ImportComparePrice_Button, BigGoPreviousPage_Button, BigGoNextPage_Button;
    @FXML ComboBox<Integer> BigGoPage_ComboBox;

    @FXML TabPane TabPane;
    @FXML Tab allStoreFilterTab, StoreFilterTab, BidFilterTab;
    @FXML VBox allStoreFilter_VBox, StoreFilter_VBox, BidFilter_VBox;
    @FXML ScrollPane allStoreFilter_ScrollPane, StoreFilter_ScrollPane, BidFilter_ScrollPane;

    @FXML TableColumn<WaitConfirmProductInfo_Bean, String> Vendor_TableColumn, KeyInDate_TableColumn, UpdateDate_TableColumn, ProductCode_TableColumn, ISBN_TableColumn, FirmCode_TableColumn, ProductName_TableColumn, NewFirstCategory_TableColumn, SupplyStatus_TableColumn, StatusButton_TableColumn, DeleteButton_TableColumn, IgnoreButton_TableColumn;
    @FXML TableColumn<WaitConfirmProductInfo_Bean, Double> Pricing_TableColumn, SinglePrice_TableColumn, BatchPrice_TableColumn, VipPrice1_TableColumn, VipPrice2_TableColumn, VipPrice3_TableColumn;
    @FXML TableView<WaitConfirmProductInfo_Bean> TableView;
    @FXML ProgressIndicator ProgressIndicator;
    @FXML ProgressBar ProgressBar;

    @FXML private Label waitConfirmTooltip_Label, comparePriceTooltip_Label;
    @FXML private Tooltip waitConfirm_tooltip, comparePrice_tooltip;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.CallConfig CallConfig;
    private ERP.ToolKit.Tooltip Tooltip;
    private Stage Stage;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private ManageProductCategory_Model ManageProductCategory_Model;
    private SystemSetting_Model SystemSetting_Model;
    private Product_Model Product_Model;
    private OrderProduct_Bean OrderProduct_Bean;

    private ConditionalDBSearch_Bean ConditionalDBSearch_Bean;
    private WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean;

    private HashMap<BigGoFilterSource, BigGoSearchResult_Bean> BigGoSearchResultMap = new HashMap<>();

    private ArrayList<HashMap<Boolean,String>> ProductPictureList = new ArrayList<>();
    private HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap;

    public AtomicBoolean stopBigGoSearch = new AtomicBoolean(false), stillBigGoSearch = new AtomicBoolean(false);

    private Task BigGoTask = null;
    private DefaultSearchProductName_ToggleSwitch defaultSearchProductName_ToggleSwitch = null;
    public JSONObject systemDefaultConfigJson, storePriceParameterJson = null;
    public ProductWaitingConfirm_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallConfig = ToolKit.CallConfig;
        CallFXML = ToolKit.CallFXML;
        Tooltip = ToolKit.Tooltip;
        ProductWaitingConfirm_Model = ToolKit.ModelToolKit.getProductWaitingConfirmModel();
        Product_Model = ToolKit.ModelToolKit.getProductModel();
        ManageProductCategory_Model = ToolKit.ModelToolKit.getManageProductCategoryModel();
        SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void initialSingleUpdateWebDriver(){
        ERPApplication.ToolKit.ModelToolKit.getSingleProductUpdateModel(Stage).initialWebDriver();
    }
    /** Set object - [Bean] OrderProduct_Bean */
    public void setOrderProduct_Bean(OrderProduct_Bean OrderProduct_Bean){  this.OrderProduct_Bean = OrderProduct_Bean; }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set compoent of product waiting confirm */
    public void setComponent(){
        String storeParameter = SystemSetting_Model.getSpecificSystemSettingData(SystemSettingConfig.待確認_Store參數);
        if(storeParameter != null) {
            storePriceParameterJson = JSONObject.parseObject(storeParameter);
        }
        systemDefaultConfigJson = CallConfig.getSystemDefaultConfigJson();
        createDefaultSearchProductNameFunction();
        setTextFieldLimitDigital();
        ComponentToolKit.getWaitConfirmStatusComboBoxItems(WaitConfirmStatus_ComboBox).addAll(WaitConfirmStatus.values());
        WaitConfirmStatus_ComboBox.getSelectionModel().selectFirst();
        WhichTable_ComboBox.getItems().addAll(WaitConfirmTable.values());
        WhichTable_ComboBox.getSelectionModel().selectFirst();

        initialTableView();
        ObservableList<Vendor_Bean> VendorList = ProductWaitingConfirm_Model.getCheckStoreVendor();
        Vendor_ComboBox.getItems().addAll(VendorList);
        Vendor_ComboBox.getSelectionModel().selectFirst();
        ComponentToolKit.setVendorComboBoxObj(Vendor_ComboBox);
        bigGoFilterMap = ProductWaitingConfirm_Model.getBigGoFilter();

        if(OrderProduct_Bean != null){
            ProductName_SearchTextField.setText(OrderProduct_Bean.getProductName());
            ConditionalSearch();
        }
        BigGoSearchResultMap.put(BigGoFilterSource.未篩選, new BigGoSearchResult_Bean(allStoreFilter_VBox));
        BigGoSearchResultMap.put(BigGoFilterSource.商城, new BigGoSearchResult_Bean(StoreFilter_VBox));
        BigGoSearchResultMap.put(BigGoFilterSource.拍賣, new BigGoSearchResult_Bean(BidFilter_VBox));


        ComponentToolKit.setToolTips(waitConfirmTooltip_Label, waitConfirm_tooltip, Tooltip.waitingConfirm());
        ComponentToolKit.setToolTips(comparePriceTooltip_Label, comparePrice_tooltip, Tooltip.waitingConfirm_comparePrice());

    }
    private void createDefaultSearchProductNameFunction(){
        Integer defaultConfig = systemDefaultConfigJson.getInteger(SystemDefaultConfig_Enum.WaitingConfirm_DefaultSearchProductNameFunction.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = SystemDefaultConfig_Enum.WaitingConfirm_DefaultSearchProductNameFunction.Or.ordinal();
        defaultSearchProductName_ToggleSwitch = new DefaultSearchProductName_ToggleSwitch(defaultConfig != 0,this);
        ConditionalSearch_HBox.getChildren().add(9,defaultSearchProductName_ToggleSwitch);

        ComponentToolKit.setToolTips(defaultSearchProductName_ToggleSwitch, new Tooltip(), Tooltip.waitingConfirm_ConditionalSearch());
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(GreaterThanBatchPrice_TextField,false);
        ComponentToolKit.addTextFieldLimitDouble(BatchPriceText);
        ComponentToolKit.addTextFieldLimitDouble(SinglePriceText);
        ComponentToolKit.addTextFieldLimitDouble(PricingText);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice1Text);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice2Text);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice3Text);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice3Text);
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(Vendor_TableColumn,"Vendor", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(KeyInDate_TableColumn,"KeyInDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UpdateDate_TableColumn,"UpdateDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductCode_TableColumn,"ProductCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ISBN_TableColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(FirmCode_TableColumn,"FirmCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductName_TableColumn,"ProductName", "CENTER-LEFT", "16",null);
        setPricingColumnTextFill(Pricing_TableColumn,"Pricing", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(Pricing_TableColumn);
        setSinglePriceColumnTextFill(SinglePrice_TableColumn,"SinglePrice", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(SinglePrice_TableColumn);
        setBatchPriceColumnTextFill(BatchPrice_TableColumn,"BatchPrice", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(BatchPrice_TableColumn);
        setDoublePriceColumnMicrometerFormat(VipPrice1_TableColumn,"VipPrice1", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(VipPrice1_TableColumn);
        setDoublePriceColumnMicrometerFormat(VipPrice2_TableColumn,"VipPrice2", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(VipPrice2_TableColumn);
        setDoublePriceColumnMicrometerFormat(VipPrice3_TableColumn,"VipPrice3", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(VipPrice3_TableColumn);
        ComponentToolKit.setColumnCellValue(NewFirstCategory_TableColumn,"NewFirstCategory", "CENTER-LEFT", "16",null);
        setSupplyStatusColumnTextFill(SupplyStatus_TableColumn,"SupplyStatus", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(StatusButton_TableColumn,"StatusButton", "CENTER-LEFT", "16",null);
        setStatusButton();
        ComponentToolKit.setColumnCellValue(DeleteButton_TableColumn,"DeleteButton", "CENTER-LEFT", "16",null);
        setDeleteButton();
        ComponentToolKit.setColumnCellValue(IgnoreButton_TableColumn,"IgnoreButton", "CENTER-LEFT", "16",null);
        setIgnoreButton();
    }
    /** ComboBox On Action - 待確認狀態 */
    @FXML protected void WaitConfirmStatusComboBoxOnAction(){
        WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
        if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.更新) {
            WhichTable_ComboBox.getSelectionModel().select(WaitConfirmTable.待確認商品);
            ComponentToolKit.setComboBoxDisable(WhichTable_ComboBox,true);
        }else{
            WhichTable_ComboBox.getSelectionModel().select(WaitConfirmTable.全部);
            ComponentToolKit.setComboBoxDisable(WhichTable_ComboBox,false);
        }
        Vendor_ComboBox.getSelectionModel().selectFirst();
        FirstCategory_ComboBox.getSelectionModel().selectFirst();
    }
    /** ComboBox On Action - 廠商 */
    @FXML protected void VendorComboBoxOnAction(){
        Vendor_Bean VendorSelectItem = ComponentToolKit.getVendorComboBoxSelectItem(Vendor_ComboBox);
        if (ComponentToolKit.getVendorComboBoxSelectIndex(Vendor_ComboBox) > 0){
            FirstCategory_ComboBox.getItems().clear();
            ObservableList<VendorCategory_Bean> CategoryBeanList = ProductWaitingConfirm_Model.getVendorCategory(VendorSelectItem,CategoryLayer.大類別,null,null);
            FirstCategory_ComboBox.getItems().addAll(CategoryBeanList);
            ComponentToolKit.setVendorCategoryComboBoxObj(FirstCategory_ComboBox);
            ComponentToolKit.setComboBoxDisable(FirstCategory_ComboBox,false);
        }else {
            FirstCategory_ComboBox.getSelectionModel().selectFirst();
            ComponentToolKit.setComboBoxDisable(FirstCategory_ComboBox,true);
        }
    }
    /** ComboBox On Action - 大類別 */
    @FXML protected void FirstCategoryComboBoxOnAction(){
        Vendor_Bean VendorSelectItem = ComponentToolKit.getVendorComboBoxSelectItem(Vendor_ComboBox);
        VendorCategory_Bean FirstCategorySelectItem = ComponentToolKit.getVendorCategoryComboBoxSelectItem(FirstCategory_ComboBox);
        if(ComponentToolKit.getVendorCategoryComboBoxSelectIndex(FirstCategory_ComboBox) > 0) {
            SecondCategory_ComboBox.getItems().clear();
            ObservableList<VendorCategory_Bean> CategoryBeanList = ProductWaitingConfirm_Model.getVendorCategory(VendorSelectItem, CategoryLayer.中類別, FirstCategorySelectItem,null);
            SecondCategory_ComboBox.getItems().addAll(CategoryBeanList);
            ComponentToolKit.setVendorCategoryComboBoxObj(SecondCategory_ComboBox);
            ComponentToolKit.setComboBoxDisable(SecondCategory_ComboBox,false);
        }else {
            SecondCategory_ComboBox.getSelectionModel().selectFirst();
            ComponentToolKit.setComboBoxDisable(SecondCategory_ComboBox,true);
        }
    }
    /** ComboBox On Action - 中類別 */
    @FXML protected void SecondCategoryComboBoxOnAction(){
        Vendor_Bean VendorSelectItem = ComponentToolKit.getVendorComboBoxSelectItem(Vendor_ComboBox);
        VendorCategory_Bean FirstCategorySelectItem = ComponentToolKit.getVendorCategoryComboBoxSelectItem(FirstCategory_ComboBox);
        VendorCategory_Bean SecondCategorySelectItem = ComponentToolKit.getVendorCategoryComboBoxSelectItem(SecondCategory_ComboBox);
        if(ComponentToolKit.getVendorCategoryComboBoxSelectIndex(SecondCategory_ComboBox) > 0) {
            ThirdCategory_ComboBox.getItems().clear();
            ObservableList<VendorCategory_Bean> CategoryBeanList = ProductWaitingConfirm_Model.getVendorCategory(VendorSelectItem, CategoryLayer.小類別, FirstCategorySelectItem, SecondCategorySelectItem);
            ThirdCategory_ComboBox.getItems().addAll(CategoryBeanList);
            ComponentToolKit.setVendorCategoryComboBoxObj(ThirdCategory_ComboBox);
            ComponentToolKit.setComboBoxDisable(ThirdCategory_ComboBox,false);
        }else {
            ThirdCategory_ComboBox.getSelectionModel().selectFirst();
            ComponentToolKit.setComboBoxDisable(ThirdCategory_ComboBox,true);
        }
    }
    /** CheckBox On Action - 經銷商含稅 */
    @FXML protected void BatchPriceIncludeTaxCheckBoxOnAction(){
        if(BatchPriceIncludeTax_CheckBox.isSelected()) {
            setPriceIncludeTax(true);
            BatchPriceIncludeTax_CheckBox.setStyle("-fx-text-fill: red");
        }else{
            setPriceIncludeTax(false);
            BatchPriceIncludeTax_CheckBox.setStyle("-fx-text-fill: black");
        }
    }
    private void setPriceIncludeTax(Boolean SelectStatus){
        ObservableList<WaitConfirmProductInfo_Bean> WaitConfirmProductList = ComponentToolKit.getWaitConfirmTableViewList(TableView);
        for (WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean : WaitConfirmProductList) {
            if(WaitConfirmProductInfo_Bean.getPricing() != 999999)
                WaitConfirmProductInfo_Bean.setPricing(ToolKit.RoundingDouble(SelectStatus ? WaitConfirmProductInfo_Bean.getPricing()*1.05 : WaitConfirmProductInfo_Bean.getPricing()/1.05));
            if(WaitConfirmProductInfo_Bean.getSinglePrice() != 999999)
                WaitConfirmProductInfo_Bean.setSinglePrice(ToolKit.RoundingDouble(SelectStatus ? WaitConfirmProductInfo_Bean.getSinglePrice()*1.05 : WaitConfirmProductInfo_Bean.getSinglePrice()/1.05));
            if(WaitConfirmProductInfo_Bean.getBatchPrice() != 999999)
                WaitConfirmProductInfo_Bean.setBatchPrice(ToolKit.RoundingDouble(SelectStatus ? WaitConfirmProductInfo_Bean.getBatchPrice()*1.05 : WaitConfirmProductInfo_Bean.getBatchPrice()/1.05));
            if(WaitConfirmProductInfo_Bean.getVipPrice1() != 999999)
                WaitConfirmProductInfo_Bean.setVipPrice1(ToolKit.RoundingDouble(SelectStatus ? WaitConfirmProductInfo_Bean.getVipPrice1()*1.05 : WaitConfirmProductInfo_Bean.getVipPrice1()/1.05));
            if(WaitConfirmProductInfo_Bean.getVipPrice2() != 999999)
                WaitConfirmProductInfo_Bean.setVipPrice2(ToolKit.RoundingDouble(SelectStatus ? WaitConfirmProductInfo_Bean.getVipPrice2()*1.05 : WaitConfirmProductInfo_Bean.getVipPrice2()/1.05));
            if(WaitConfirmProductInfo_Bean.getVipPrice3() != 999999)
                WaitConfirmProductInfo_Bean.setVipPrice3(ToolKit.RoundingDouble(SelectStatus ? WaitConfirmProductInfo_Bean.getVipPrice3()*1.05 : WaitConfirmProductInfo_Bean.getVipPrice3()/1.05));
        }
    }
    /** TextField Key Released - 單價減成本 */
    @FXML protected void SinglePrice_Minus_BatchPriceKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) ConditionalSearch();
    }
    /** TextField Key Released - 品名 */
    @FXML protected void ProductNameKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) ConditionalSearch();
    }
    /** Button Mouse Clicked - 查詢 */
    @FXML protected void SearchMouseClicked(){
        ConditionalSearch();
    }
    private void ConditionalSearch(){
        this.ConditionalDBSearch_Bean = new ConditionalDBSearch_Bean();
        ConditionalDBSearch_Bean.setWaitConfirmStatus(ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox));
        ConditionalDBSearch_Bean.setWaitConfirmTable(ComponentToolKit.getWaitConfirmTableComboBoxSelectItem(WhichTable_ComboBox));

        ConditionalDBSearch_Bean.setVendor(ComponentToolKit.getVendorComboBoxSelectItem(Vendor_ComboBox));
        ConditionalDBSearch_Bean.setFirstCategory(ComponentToolKit.getVendorCategoryComboBoxSelectItem(FirstCategory_ComboBox));
        ConditionalDBSearch_Bean.setSecondCategory(ComponentToolKit.getVendorCategoryComboBoxSelectItem(SecondCategory_ComboBox));
        ConditionalDBSearch_Bean.setThirdCategory(ComponentToolKit.getVendorCategoryComboBoxSelectItem(ThirdCategory_ComboBox));
        ConditionalDBSearch_Bean.setSinglePrice_Minus_BatchPrice(SinglePrice_Minus_BatchPrice_TextField.getText());
        ConditionalDBSearch_Bean.setProductName(ProductName_SearchTextField.getText());
        ConditionalDBSearch_Bean.setPriceIncludeTax(BatchPriceIncludeTax_CheckBox.isSelected());
        ConditionalDBSearch_Bean.setSearchProductNameByOr(!defaultSearchProductName_ToggleSwitch.switchedOnProperty().get());
        setStatusButton();

        Task Task = ConditionalTask(ConditionalDBSearch_Bean);
        ProgressIndicator.progressProperty().bind(Task.progressProperty());
        new Thread(Task).start();
    }
    private Task ConditionalTask(ConditionalDBSearch_Bean ConditionalSearch_Bean){
        return new Task() {
            @Override public Void call(){
                ComponentToolKit.setButtonDisable(Search_Button,true);
                ProgressIndicator.setVisible(true);
                Platform.runLater(()-> setHandleStoreProductButtonText(ConditionalSearch_Bean.getWaitConfirmTable()));
                initialComponent();
                ObservableList<WaitConfirmProductInfo_Bean> WaitConfirmProductList = ProductWaitingConfirm_Model.ProductConditionalSearch(ConditionalSearch_Bean);
                Platform.runLater(()-> ProgressIndicator.setVisible(false));
                if(WaitConfirmProductList.size() == 0) Platform.runLater(()-> DialogUI.MessageDialog("※ 無資料!"));
                else{
                    TableView.getItems().addAll(WaitConfirmProductList);
                    if(ConditionalSearch_Bean.isPriceIncludeTax())
                        setPriceIncludeTax(true);
                }
                ComponentToolKit.setButtonDisable(Search_Button,false);
                return null;
            }
        };
    }
    private void setHandleStoreProductButtonText(WaitConfirmTable WaitConfirmTable){
        if(WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品)    HandleStoreProduct_Button.setText("修改進銷存");
        else HandleStoreProduct_Button.setText("新增至進銷存");
    }
    /** Tab On Selection Changed - 所有商品 */
    @FXML protected void allStoreFilterTabOnSelectionChanged(){
        if(allStoreFilterTab.isSelected()){
            BigGoPage_ComboBox.getItems().clear();
            BigGoSearchResult_Bean BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.未篩選);
            if(BigGoSearchResult_Bean != null){
                if(BigGoSearchResult_Bean.getBigGoPageList() != null)
                    BigGoPage_ComboBox.getItems().addAll(BigGoSearchResult_Bean.getBigGoPageList());
                BigGoHyperlinkText.setText(BigGoSearchResult_Bean.getHyperlink());
                Platform.runLater(()-> setBigGoSearchResultUI(BigGoSearchResult_Bean));
            }
        }
    }
    /** Tab On Selection Changed - 商城篩選 */
    @FXML protected void StoreFilterTabOnSelectionChanged(){
        if(StoreFilterTab.isSelected()){
            BigGoPage_ComboBox.getItems().clear();
            BigGoSearchResult_Bean BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.商城);
            if(BigGoSearchResult_Bean != null){
                if(BigGoSearchResult_Bean.getBigGoPageList() != null)
                    BigGoPage_ComboBox.getItems().addAll(BigGoSearchResult_Bean.getBigGoPageList());
                BigGoHyperlinkText.setText(BigGoSearchResult_Bean.getHyperlink());
                Platform.runLater(()-> setBigGoSearchResultUI(BigGoSearchResult_Bean));
            }
        }
    }
    /** Tab On Selection Changed - 拍賣篩選 */
    @FXML protected void BidFilterTabOnSelectionChanged(){
        if(BidFilterTab.isSelected()){
            BigGoPage_ComboBox.getItems().clear();
            BigGoSearchResult_Bean BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.拍賣);
            if(BigGoSearchResult_Bean != null){
                if(BigGoSearchResult_Bean.getBigGoPageList() != null)
                    BigGoPage_ComboBox.getItems().addAll(BigGoSearchResult_Bean.getBigGoPageList());
                BigGoHyperlinkText.setText(BigGoSearchResult_Bean.getHyperlink());
                Platform.runLater(()-> setBigGoSearchResultUI(BigGoSearchResult_Bean));
            }
        }
    }
    /** Button Mouse Clicked - 商店篩選 */
    @FXML protected void BigGoFilterMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CallFXML.ShowBigGoFilter(Stage, bigGoFilterMap);
        }
    }
    /** Button Mouse Clicked - 金額設定 */
    @FXML protected void storePriceMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CallFXML.ShowBigGoStorePriceSetting(Stage, storePriceParameterJson);
        }
    }
    /** Button Mouse Clicked - [拍賣/商城超連結] 前往 */
    @FXML protected void StoreOrBidHyperlinkMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && !StoreOrBidHyperlinkText.getText().equals("")){
            try {
                Desktop Desktop = java.awt.Desktop.getDesktop();
                Desktop.browse(new URI(StoreOrBidHyperlinkText.getText())); //使用預設瀏覽器開啟超連結
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - [BigGo超連結] 前往 */
    @FXML protected void BigGoHyperlinkMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && !BigGoHyperlinkText.getText().equals("")){
            try {
                Desktop Desktop = java.awt.Desktop.getDesktop();
                Desktop.browse(new URI(BigGoHyperlinkText.getText())); //使用預設瀏覽器開啟超連結
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Key Pressed - 搜尋 */
    @FXML protected void BigGoSearchTextKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(WaitConfirmProductInfo_Bean != null)
                setProductInfo();
            BigGoSearch();
        }
    }
    /** Button Mouse Clicked - 搜尋 */
    @FXML protected void BigGoSearchMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(WaitConfirmProductInfo_Bean != null)
                setProductInfo();
            BigGoSearch();
        }
    }
    @FXML protected void BigGoPageOnAction(){
        Integer selectPage = ComponentToolKit.getComboBoxSelectItemIntegerFormat(BigGoPage_ComboBox);
        if(selectPage != null){
            BigGoSearchResult_Bean BigGoSearchResult_Bean;
            if(StoreFilterTab.isSelected()){
                BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.商城);
                StoreFilter_ScrollPane.setVvalue(0);
            }else if(BidFilterTab.isSelected()){
                BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.拍賣);
                BidFilter_ScrollPane.setVvalue(0);
            }else{
                BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.未篩選);
                allStoreFilter_ScrollPane.setVvalue(0);
            }
            BigGoSearchResult_Bean.setNowPage(selectPage);
            Platform.runLater(()-> setBigGoSearchResultUI(BigGoSearchResult_Bean));
            if(selectPage == 0 || selectPage == 1)
                ComponentToolKit.setButtonDisable(BigGoPreviousPage_Button,true);
            else
                ComponentToolKit.setButtonDisable(BigGoPreviousPage_Button,false);
            ComponentToolKit.setButtonDisable(BigGoNextPage_Button,selectPage == BigGoSearchResult_Bean.getTotalPage());
        }
    }
    @FXML protected void BigGoPreviousPageMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            setBigGoResultNowPage(UpOrDownFunction.previous);
        }
    }
    @FXML protected void BigGoNextPageMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            setBigGoResultNowPage(UpOrDownFunction.next);
        }
    }
    private void setBigGoResultNowPage(UpOrDownFunction UpOrDownFunction){
        allStoreFilter_VBox.getChildren().clear();
        StoreFilter_VBox.getChildren().clear();
        BidFilter_VBox.getChildren().clear();
        BigGoSearchResult_Bean BigGoSearchResult_Bean;
        if(allStoreFilterTab.isSelected()) {
            BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.未篩選);
            allStoreFilter_ScrollPane.setVvalue(0);
        }else if(StoreFilterTab.isSelected()) {
            BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.商城);
            StoreFilter_ScrollPane.setVvalue(0);
        }else {
            BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.拍賣);
            BidFilter_ScrollPane.setVvalue(0);
        }
        if(UpOrDownFunction == ToolKit_Enum.UpOrDownFunction.previous)
            BigGoSearchResult_Bean.setNowPage(BigGoSearchResult_Bean.getNowPage()-1);
        else if(UpOrDownFunction == ToolKit_Enum.UpOrDownFunction.next)
            BigGoSearchResult_Bean.setNowPage(BigGoSearchResult_Bean.getNowPage()+1);
        setBigGoSearchResultUI(BigGoSearchResult_Bean);
    }
    /** Button Mouse Clicked - 匯入比價金額 */
    @FXML protected void ImportComparePriceMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ArrayList<BigGoProductInfo_Bean> storeProductList = BigGoSearchResultMap.get(BigGoFilterSource.商城).getProductList();
            ArrayList<BigGoProductInfo_Bean> bidProductList = BigGoSearchResultMap.get(BigGoFilterSource.拍賣).getProductList();
            if(storeProductList != null && bidProductList != null &&
                    storeProductList.size() != 0 && bidProductList.size() != 0){
                SynchronizeStorePrice();
                SynchronizeBidPrice();
                CalculateLowestProfit();
            }else{
                DialogUI.MessageDialog("無任何BigGo商品");
            }
        }
    }
    private void CalculateLowestProfit(){
        if(Float.parseFloat(SinglePriceText.getText()) >= Float.parseFloat(VipPrice3Text.getText()))
            LowestProfitText_ProductInfo.setText(ToolKit.RoundingString(true,Float.parseFloat(VipPrice3Text.getText()) - Float.parseFloat(BatchPriceText.getText())));
        else
            LowestProfitText_ProductInfo.setText(ToolKit.RoundingString(true,Float.parseFloat(SinglePriceText.getText()) - Float.parseFloat(BatchPriceText.getText())));
    }
    private void SynchronizeStorePrice(){
        BigGoSearchResult_Bean BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.商城);
        if(BigGoSearchResult_Bean != null && BigGoSearchResult_Bean.getProductList() != null){
            double[] SynchronizePrice = FindBigGoProductMinAndMaxPrice(BigGoSearchResult_Bean.getProductList());
            if (SynchronizePrice[0] != 0){
                SinglePriceText.setText(ToolKit.RoundingString(true,SynchronizePrice[0]));
                OldSinglePriceText.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getSinglePrice()));
                setPriceTextColor(SinglePriceText, SynchronizePrice[0], WaitConfirmProductInfo_Bean.getSinglePrice());
                SinglePriceNoneTax_Button.setText(ToolKit.RoundingString(true,SynchronizePrice[0]/1.05)+" (未稅)");
                ComponentToolKit.setButtonVisible(SinglePriceNoneTax_Button,true);
            }
            if (SynchronizePrice[1] != 0){
                PricingText.setText(ToolKit.RoundingString(true,SynchronizePrice[1]));
                OldPricingText.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getPricing()));
                setPriceTextColor(PricingText, SynchronizePrice[1], WaitConfirmProductInfo_Bean.getPricing());
                PricingNoneTax_Button.setText(ToolKit.RoundingString(true,SynchronizePrice[1]/1.05)+" (未稅)");
                ComponentToolKit.setButtonVisible(PricingNoneTax_Button,true);
            }
        }
    }
    private void SynchronizeBidPrice(){
        BigGoSearchResult_Bean BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.拍賣);
        if(BigGoSearchResult_Bean != null && BigGoSearchResult_Bean.getProductList() != null){
            double[] SynchronizePrice = FindBigGoProductMinAndMaxPrice(BigGoSearchResult_Bean.getProductList());
            if (SynchronizePrice[0] != 0){
                VipPrice3Text.setText(ToolKit.RoundingString(true,SynchronizePrice[0]));
                OldVipPrice3Text.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice3()));
                setPriceTextColor(VipPrice3Text, SynchronizePrice[0], WaitConfirmProductInfo_Bean.getVipPrice3());
                SynchronizePrice[0] = SynchronizePrice[0]/(float)1.05;
                VipPrice3NoneTax_Button.setText(ToolKit.RoundingString(true,SynchronizePrice[0])+" (未稅)");
                ComponentToolKit.setButtonVisible(VipPrice3NoneTax_Button,true);
            }
            if (SynchronizePrice[1] != 0){
                VipPrice1Text.setText(ToolKit.RoundingString(true,SynchronizePrice[1]));
                OldVipPrice1Text.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice1()));
                setPriceTextColor(VipPrice1Text, SynchronizePrice[1], WaitConfirmProductInfo_Bean.getVipPrice1());
                SynchronizePrice[1] = SynchronizePrice[1]/(float)1.05;
                VipPrice1NoneTax_Button.setText(ToolKit.RoundingString(true,SynchronizePrice[1])+" (未稅)");
                ComponentToolKit.setButtonVisible(VipPrice1NoneTax_Button,true);
            }
            float VipPrice2 = (Float.parseFloat(VipPrice3Text.getText()) + Float.parseFloat(VipPrice1Text.getText())) / 2;
            String NewPrice = ToolKit.RoundingString(true,VipPrice2);
            VipPrice2Text.setText(NewPrice);
            OldVipPrice2Text.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice2()));
            setPriceTextColor(VipPrice2Text, VipPrice2, WaitConfirmProductInfo_Bean.getVipPrice2());
            VipPrice2NoneTax_Button.setText(ToolKit.RoundingString(true,VipPrice2/1.05)+" (未稅)");
            ComponentToolKit.setButtonVisible(VipPrice2NoneTax_Button,true);
        }
    }
    private double[] FindBigGoProductMinAndMaxPrice(ArrayList<BigGoProductInfo_Bean> BigGoStoreProductList){
        double MaxPrice = 0, MinPrice = 0;
        double[] SynchronizePrice = {MinPrice, MaxPrice};
        for (BigGoProductInfo_Bean BigGoProductInfo_Bean : BigGoStoreProductList) {
            MaxPrice = Double.parseDouble(BigGoProductInfo_Bean.getProductPrice().replace("$", "").replace(",", ""));
            if(MinPrice == 0) {
                MinPrice = Double.parseDouble(BigGoProductInfo_Bean.getProductPrice().replace("$", "").replace(",", ""));
            }
        }
        if(MinPrice > MaxPrice){
            double temp = MinPrice;
            MinPrice = MaxPrice;
            MaxPrice = temp;
        }
        SynchronizePrice[0] = MinPrice;
        SynchronizePrice[1] = MaxPrice;
        return SynchronizePrice;
    }
    private void setPriceTextColor(TextField TextField, double NewPrice, double OldPrice){
        if(NewPrice > OldPrice)    TextField.setStyle("-fx-text-fill: red");
        else if(NewPrice == OldPrice)   TextField.setStyle("-fx-text-fill: black");
        else if(NewPrice < OldPrice)   TextField.setStyle("-fx-text-fill: blue");
    }
    /** Button Mouse Clicked - 單價未稅 */
    @FXML protected void SinglePriceNoneTaxMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NoneTaxPrice = SinglePriceNoneTax_Button.getText().substring(0,SinglePriceNoneTax_Button.getText().indexOf(" "));
            SinglePriceText.setText(NoneTaxPrice);
            setPriceTextColor(SinglePriceText, Double.parseDouble(NoneTaxPrice), WaitConfirmProductInfo_Bean.getSinglePrice());
        }
    }
    /** Button Mouse Clicked - 定價未稅 */
    @FXML protected void PricingNoneTaxMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NoneTaxPrice = PricingNoneTax_Button.getText().substring(0,PricingNoneTax_Button.getText().indexOf(" "));
            PricingText.setText(NoneTaxPrice);
            setPriceTextColor(PricingText, Double.parseDouble(NoneTaxPrice), WaitConfirmProductInfo_Bean.getPricing());
        }
    }
    /** Button Mouse Clicked - VipPrice1未稅 */
    @FXML protected void VipPrice1NoneTaxMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NoneTaxPrice = VipPrice1NoneTax_Button.getText().substring(0,VipPrice1NoneTax_Button.getText().indexOf(" "));
            VipPrice1Text.setText(NoneTaxPrice);
            setPriceTextColor(VipPrice1Text, Double.parseDouble(NoneTaxPrice), WaitConfirmProductInfo_Bean.getVipPrice1());
        }
    }
    /** Button Mouse Clicked - VipPrice2未稅 */
    @FXML protected void VipPrice2NoneTaxMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NoneTaxPrice = VipPrice2NoneTax_Button.getText().substring(0,VipPrice2NoneTax_Button.getText().indexOf(" "));
            VipPrice2Text.setText(NoneTaxPrice);
            setPriceTextColor(VipPrice2Text, Float.parseFloat(NoneTaxPrice), WaitConfirmProductInfo_Bean.getVipPrice2());
        }
    }
    /** Button Mouse Clicked - VipPrice3未稅 */
    @FXML protected void VipPrice3NoneTaxMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NoneTaxPrice = VipPrice3NoneTax_Button.getText().substring(0,VipPrice3NoneTax_Button.getText().indexOf(" "));
            VipPrice3Text.setText(NoneTaxPrice);
            setPriceTextColor(VipPrice3Text, Float.parseFloat(NoneTaxPrice), WaitConfirmProductInfo_Bean.getVipPrice3());
        }
    }
    /** Button Mouse Clicked - 上一張 */
    @FXML protected void PicturePreviousMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setButtonDisable(PictureNext_Button,false);
            for(int index = 0; index < ProductPictureList.size(); index++ ){
                if(ProductPictureList.get(index).containsKey(true) && (index-1) >= 0){
                    ProductPictureList.get(index).put(false,ProductPictureList.get(index).get(true));
                    ProductPictureList.get(index).remove(true);
                    ProductPictureList.get(index-1).put(true, ProductPictureList.get(index-1).get(false));
                    ProductPictureList.get(index-1).remove(false);
                    setProductImage(ProductPictureList.get(index-1).get(true));
                    if((index-1) == 0)
                        PicturePrevious_Button.setDisable(true);
                }
            }
        }
    }
    /** Button Mouse Clicked - 下一張 */
    @FXML protected void PictureNextMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setButtonDisable(PicturePrevious_Button,false);
            for(int index = 0; index < ProductPictureList.size(); index++ ){
                if(ProductPictureList.get(index).containsKey(true) && (index+1) <= (ProductPictureList.size()-1)){
                    ProductPictureList.get(index).put(false,ProductPictureList.get(index).get(true));
                    ProductPictureList.get(index).remove(true);
                    ProductPictureList.get(index+1).put(true, ProductPictureList.get(index+1).get(false));
                    ProductPictureList.get(index+1).remove(false);
                    setProductImage(ProductPictureList.get(index+1).get(true));
                    if((index+1) == (ProductPictureList.size()-1))  PictureNext_Button.setDisable(true);
                    break;
                }
            }
        }
    }
    @FXML protected void DownloadPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ProductWaitingConfirm_Model.downloadPicture(PictureImageView.getImage(),CallConfig.getFile_OutputPath()))   DialogUI.MessageDialog("※ 下載成功");
            else    DialogUI.MessageDialog("※ 下載失敗，請確認匯出路徑!");
        }
    }

    /** Button Mouse Clicked - 編輯商品描述 */
    @FXML protected void EditProductDescribeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CallFXML.ShowHTMLEditor(Stage,null,null, WaitConfirmProductInfo_Bean);
        }
    }
    /** Button Mouse Clicked - 編輯商品備註 */
    @FXML protected void EditProductRemarkMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))   CallFXML.ShowEditProductDescribeOrRemark(Stage, EditProductDescribeOrRemark.編輯商品備註, WaitConfirmProductInfo_Bean);
    }
    /** Button Mouse Clicked - 代理商超連結 */
    @FXML protected void ProductHyperlinkMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try {
                if(WaitConfirmProductInfo_Bean != null) {
                    if(WaitConfirmProductInfo_Bean.getProductCode() == null || WaitConfirmProductInfo_Bean.getProductCode().equals("")) {
                        DialogUI.MessageDialog("※ 無商品編號");
                        return;
                    }
                    String Url = ProductWaitingConfirm_Model.getProductUrl(WaitConfirmProductInfo_Bean.getProductCode());
                    if(Url != null){
                        Desktop Desktop = java.awt.Desktop.getDesktop();
                        Desktop.browse(new URI(Url)); //使用預設瀏覽器開啟超連結
                    }else
                        DialogUI.MessageDialog("※ 找不到相對應連結");
                }
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - 進銷存新增/修改 */
    @FXML protected void HandleStoreProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            WaitConfirmProductInfo_Bean.setProductName(ProductNameText.getText());
            WaitConfirmProductInfo_Bean.setSinglePrice(ToolKit.RoundingDouble(Double.parseDouble(SinglePriceText.getText())));
            WaitConfirmProductInfo_Bean.setPricing(ToolKit.RoundingDouble(Double.parseDouble(PricingText.getText())));
            WaitConfirmProductInfo_Bean.setVipPrice1(ToolKit.RoundingDouble(Double.parseDouble(VipPrice1Text.getText())));
            WaitConfirmProductInfo_Bean.setVipPrice2(ToolKit.RoundingDouble(Double.parseDouble(VipPrice2Text.getText())));
            WaitConfirmProductInfo_Bean.setVipPrice3(ToolKit.RoundingDouble(Double.parseDouble(VipPrice3Text.getText())));
            if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == WaitConfirmTable.待確認商品){
                if(WaitConfirmProductInfo_Bean.getNewFirstCategory() == null) {
                    DialogUI.MessageDialog("※ 未設定總類");
                }else if(WaitConfirmProductInfo_Bean.getNewFirstCategory().length() != 2){
                    DialogUI.MessageDialog("※ 總類長度為兩位數，請按「F8」來修改");
                }else{
                    if(!Product_Model.isProductCodeExist(WaitConfirmProductInfo_Bean.getProductCode())){
                        if(Product_Model.insertProduct(generateInsertProductInfoBean(WaitConfirmProductInfo_Bean),true,false)){
                            ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                            DialogUI.MessageDialog("※ 新增成功");
                        }else    DialogUI.MessageDialog("※ 新增失敗");
                    }else
                        DialogUI.MessageDialog("※ 新增失敗，ProductCode 已存在!");
                }
            }else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == WaitConfirmTable.進銷存商品){
                if(Product_Model.modifyProduct(generateModifyProductInfoBean(WaitConfirmProductInfo_Bean),true,false)){
                    ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                    DialogUI.MessageDialog("※ 修改成功");
                }else   DialogUI.MessageDialog("※ 修改失敗");
            }
        }
    }
    private ProductInfo_Bean generateInsertProductInfoBean(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        String ISBN = Product_Model.generateNewestISBN(WaitConfirmProductInfo_Bean.getVendorCode() + WaitConfirmProductInfo_Bean.getNewFirstCategory());

        ProductInfo_Bean ProductInfo_Bean = new ProductInfo_Bean();
        ProductInfo_Bean.setISBN(ISBN);
        ProductInfo_Bean.setInternationalCode("");
        ProductInfo_Bean.setFirmCode("");
        ProductInfo_Bean.setProductCode(WaitConfirmProductInfo_Bean.getProductCode());
        ProductInfo_Bean.setProductName(WaitConfirmProductInfo_Bean.getProductName());
        ProductInfo_Bean.setUnit(WaitConfirmProductInfo_Bean.getUnit());
        ProductInfo_Bean.setBrand(WaitConfirmProductInfo_Bean.getBrand());
        ProductInfo_Bean.setDescribe(WaitConfirmProductInfo_Bean.getDescribe());
        ProductInfo_Bean.setRemark(WaitConfirmProductInfo_Bean.getRemark());
        ProductInfo_Bean.setSupplyStatus(WaitConfirmProductInfo_Bean.getSupplyStatus());
        ProductInfo_Bean.setInStock("0");
        ProductInfo_Bean.setSafetyStock("0");
        ProductInfo_Bean.setInventoryQuantity(0);
        ProductInfo_Bean.setVendorName(WaitConfirmProductInfo_Bean.getVendor());
        ProductInfo_Bean.setVendorCode(String.valueOf(WaitConfirmProductInfo_Bean.getVendorCode()));

        ProductInfo_Bean.setBatchPrice(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getBatchPrice()));
        ProductInfo_Bean.setSinglePrice(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getSinglePrice()));
        ProductInfo_Bean.setPricing(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getPricing()));
        ProductInfo_Bean.setVipPrice1(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice1()));
        ProductInfo_Bean.setVipPrice2(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice2()));
        ProductInfo_Bean.setVipPrice3(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice3()));
        ProductInfo_Bean.setDiscount(1.0);

        ProductInfo_Bean.setVendorFirstCategory_id(WaitConfirmProductInfo_Bean.getFirstCategory_Id());
        ProductInfo_Bean.setVendorSecondCategory_id(WaitConfirmProductInfo_Bean.getSecondCategory_Id());
        ProductInfo_Bean.setVendorThirdCategory_id(WaitConfirmProductInfo_Bean.getThirdCategory_Id());

        ProductInfo_Bean.setFirstCategory(WaitConfirmProductInfo_Bean.getNewFirstCategory());
        ProductInfo_Bean.setSecondCategory("");
        ProductInfo_Bean.setThirdCategory("");

        ProductInfo_Bean.setProductArea("");
        ProductInfo_Bean.setProductFloor("");

        ProductInfo_Bean.setYahooCategory("");
        ProductInfo_Bean.setRutenCategory("");
        ProductInfo_Bean.setShopeeCategory(null);

        ProductInfo_Bean.setPicture1(WaitConfirmProductInfo_Bean.getPicture1());
        ProductInfo_Bean.setPicture2(WaitConfirmProductInfo_Bean.getPicture2());
        ProductInfo_Bean.setPicture3(WaitConfirmProductInfo_Bean.getPicture3());

        ProductInfo_Bean.setProductTag(FXCollections.observableArrayList());
        return ProductInfo_Bean;
    }
    private ProductInfo_Bean generateModifyProductInfoBean(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        ProductInfo_Bean ProductInfo_Bean = null;
        JSONObject AdvancedSearchMap = createJsonObject();
        ObservableList<ProductInfo_Bean> ProductList = Product_Model.SearchProduct(WaitConfirmProductInfo_Bean.getISBN(), AdvancedSearchMap, false,"");
        if(ProductList.size() != 0){
            ProductInfo_Bean = ProductList.get(0);
            ProductInfo_Bean.setProductName(WaitConfirmProductInfo_Bean.getProductName());
            ProductInfo_Bean.setBatchPrice(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getBatchPrice()));
            ProductInfo_Bean.setSinglePrice(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getSinglePrice()));
            ProductInfo_Bean.setPricing(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getPricing()));
            ProductInfo_Bean.setVipPrice1(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice1()));
            ProductInfo_Bean.setVipPrice2(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice2()));
            ProductInfo_Bean.setVipPrice3(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice3()));
            ProductInfo_Bean.setVendorFirstCategory_id(WaitConfirmProductInfo_Bean.getFirstCategory_Id());
            ProductInfo_Bean.setVendorSecondCategory_id(WaitConfirmProductInfo_Bean.getSecondCategory_Id());
            ProductInfo_Bean.setVendorThirdCategory_id(WaitConfirmProductInfo_Bean.getThirdCategory_Id());
            ProductInfo_Bean.setDescribe(WaitConfirmProductInfo_Bean.getDescribe());
            ProductInfo_Bean.setRemark(WaitConfirmProductInfo_Bean.getRemark());
            ProductInfo_Bean.setSupplyStatus(WaitConfirmProductInfo_Bean.getSupplyStatus());
        }
        return ProductInfo_Bean;
    }
    private JSONObject createJsonObject(){
        JSONObject JSONObject = new JSONObject(new LinkedHashMap<>());
        for(Product_Enum.AdvancedSearchColumn AdvancedSearchColumn : Product_Enum.AdvancedSearchColumn.values()){
            if(AdvancedSearchColumn == Product_Enum.AdvancedSearchColumn.ISBN)
                JSONObject.put(AdvancedSearchColumn.name(), true);
            else
                JSONObject.put(AdvancedSearchColumn.name(), false);
        }
        return JSONObject;
    }
    /** Button Mouse Clicked - 單筆更新 */
    @FXML protected void SingleProductUpdateMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
              CallFXML.ShowSingleProductUpdate(Stage, WaitConfirmProductInfo_Bean);
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isF8KeyPressed(KeyEvent)){
            if(this.WaitConfirmProductInfo_Bean.getWaitConfirmTable() == WaitConfirmTable.待確認商品){
                String newFirstCategory = DialogUI.TextInputDialog("總類變更","請輸入欲變更「總類」編號",null);
                if(newFirstCategory != null && !newFirstCategory.equals("")){
                    if(!ToolKit.isDigital(newFirstCategory)){
                        DialogUI.AlarmDialog("只允許輸入數字");
                    }else if(!ManageProductCategory_Model.isCategoryExist(newFirstCategory, CategoryLayer.大類別)){
                        DialogUI.AlarmDialog("總類編號不存在");
                    }else{
                        if(DialogUI.ConfirmDialog("確定將總類變更為【" + newFirstCategory + "】",true,false,0,0)){
                            if(ProductWaitingConfirm_Model.updateCheckStoreNewFirstCategory(this.WaitConfirmProductInfo_Bean.getProductCode(), newFirstCategory)){
                                this.WaitConfirmProductInfo_Bean.setNewFirstCategory(newFirstCategory);
                                DialogUI.MessageDialog("更新成功");
                            }else{
                                DialogUI.MessageDialog("更新失敗");
                            }
                        }
                    }
                }
            }else{
                DialogUI.MessageDialog("只允許變更「待確認商品之總類」");
            }
        }
    }
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ComponentToolKit.getTableViewItemsSize(TableView) != 0){
            this.WaitConfirmProductInfo_Bean = ComponentToolKit.getWaitConfirmTableViewSelectItem(TableView);
            if(WaitConfirmProductInfo_Bean != null) {
                if(ConditionalDBSearch_Bean.getWaitConfirmTable() == WaitConfirmTable.全部)
                    setHandleStoreProductButtonText(WaitConfirmProductInfo_Bean.getWaitConfirmTable());
                setProductInfo();
                setComparePriceInfo();
                BigGoSearch();
            }
        }
    }
    private void setProductInfo(){
        ComponentToolKit.setButtonDisable(SingleProductUpdate_Button,false);
        ComponentToolKit.setButtonDisable(EditProductDescribe_Button,false);
        ComponentToolKit.setButtonDisable(EditProductRemark_Button,false);
        ComponentToolKit.setButtonDisable(HandleStoreProduct_Button,false);
        ProductNameText.setText(WaitConfirmProductInfo_Bean.getProductName());
        BatchPriceText.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getBatchPrice()));
        LowestProfitText_ProductInfo.setText("");
        PricingText.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getPricing()));
        PricingText.setStyle("-fx-text-fill: black");
        SinglePriceText.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getSinglePrice()));
        SinglePriceText.setStyle("-fx-text-fill: black");
        VipPrice1Text.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice1()));
        VipPrice1Text.setStyle("-fx-text-fill: black");
        VipPrice2Text.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice2()));
        VipPrice2Text.setStyle("-fx-text-fill: black");
        VipPrice3Text.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getVipPrice3()));
        VipPrice3Text.setStyle("-fx-text-fill: black");
        OldPricingText.setText("");
        OldSinglePriceText.setText("");
        OldVipPrice1Text.setText("");
        OldVipPrice2Text.setText("");
        OldVipPrice3Text.setText("");
        ComponentToolKit.setButtonVisible(SinglePriceNoneTax_Button,false);
        ComponentToolKit.setButtonVisible(PricingNoneTax_Button,false);
        ComponentToolKit.setButtonVisible(VipPrice1NoneTax_Button,false);
        ComponentToolKit.setButtonVisible(VipPrice2NoneTax_Button,false);
        ComponentToolKit.setButtonVisible(VipPrice3NoneTax_Button,false);
        setProductSupplyStatus();
        setProductPictureList();
        if(BigGoSearchResultMap.get(BigGoFilterSource.商城) != null && BigGoSearchResultMap.get(BigGoFilterSource.商城).getProductList() != null)
            BigGoSearchResultMap.get(BigGoFilterSource.商城).getProductList().clear();
        if(BigGoSearchResultMap.get(BigGoFilterSource.拍賣) != null && BigGoSearchResultMap.get(BigGoFilterSource.拍賣).getProductList() != null)
            BigGoSearchResultMap.get(BigGoFilterSource.拍賣).getProductList().clear();
    }
    private void setComparePriceInfo(){
        GreaterThanBatchPrice_TextField.setText(ToolKit.RoundingString(WaitConfirmProductInfo_Bean.getBatchPrice()));
        BigGoSearchText.setText(WaitConfirmProductInfo_Bean.getProductName());
        LowestProfitText_ComparePriceInfo.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getSinglePrice() - WaitConfirmProductInfo_Bean.getBatchPrice()));
    }
    private void setProductSupplyStatus(){
        if(WaitConfirmProductInfo_Bean.getSupplyStatus().equals("")){
            SupplyStatusText.setText("網站未提供");
            SupplyStatusText.setStyle("-fx-background-color:#bebebe; -fx-text-fill: black");
        }else {
            SupplyStatusText.setText(WaitConfirmProductInfo_Bean.getSupplyStatus());
            if(!WaitConfirmProductInfo_Bean.getSupplyStatus().contains("供貨中") && !WaitConfirmProductInfo_Bean.getSupplyStatus().equals("庫存充足") && !WaitConfirmProductInfo_Bean.getSupplyStatus().equals("貨物充足"))
                SupplyStatusText.setStyle("-fx-background-color:#bebebe; -fx-text-fill: red");
            else    SupplyStatusText.setStyle("-fx-background-color:#bebebe; -fx-text-fill: black");
        }
    }
    private void setProductPictureList(){
        ProductWaitingConfirm_Model.getProductPicture(WaitConfirmProductInfo_Bean);
        ProductPictureList.clear();
        if(WaitConfirmProductInfo_Bean.getPicture1() != null && !WaitConfirmProductInfo_Bean.getPicture1().equals("")) {
            HashMap<Boolean,String> PictureMap = new HashMap<>();
            PictureMap.put(true, WaitConfirmProductInfo_Bean.getPicture1());
            ProductPictureList.add(PictureMap);
            setProductImage(WaitConfirmProductInfo_Bean.getPicture1());
        }
        if(WaitConfirmProductInfo_Bean.getPicture2() != null && !WaitConfirmProductInfo_Bean.getPicture2().equals("")){
            HashMap<Boolean,String> PictureMap = new HashMap<>();
            PictureMap.put(false, WaitConfirmProductInfo_Bean.getPicture2());
            ProductPictureList.add(PictureMap);
        }
        if(WaitConfirmProductInfo_Bean.getPicture3() != null && !WaitConfirmProductInfo_Bean.getPicture3().equals("")){
            HashMap<Boolean,String> PictureMap = new HashMap<>();
            PictureMap.put(false, WaitConfirmProductInfo_Bean.getPicture3());
            ProductPictureList.add(PictureMap);
        }
        PicturePrevious_Button.setDisable(true);
        if(ProductPictureList.size() == 0) {
            setProductImage("");
            PictureNext_Button.setDisable(true);
            DownloadPicture_Button.setDisable(true);
        }else{
            DownloadPicture_Button.setDisable(false);
            if(ProductPictureList.size() == 1)    PictureNext_Button.setDisable(true);
            else    PictureNext_Button.setDisable(false);
        }
    }
    private void setProductImage(String Base64String) {   PictureImageView.setImage(ToolKit.decodeBase64ToImage(Base64String));  }
    private void BigGoSearch(){
        ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean = getBigGoSearch_Bean();
        if(ConditionalBigGoSearch_Bean != null){
            try{
                while(true){
                    if(!stopBigGoSearch.get() && stillBigGoSearch.get()){
                        stopBigGoSearch.set(true);
                    }
                    if(!stopBigGoSearch.get()){
                        clearBigGoSearchInfo();
                        BigGoTask = BigGoTask(this, ConditionalBigGoSearch_Bean);
                        ProgressBar.progressProperty().bind(BigGoTask.progressProperty());
                        new Thread(BigGoTask).start();
                        break;
                    }

                }
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                DialogUI.ExceptionDialog(Ex);
            }
        }
    }
    public void stopBigGoSearch(){
        stillBigGoSearch.set(false);
        stopBigGoSearch.set(false);
    }
    private void clearBigGoSearchInfo(){
        ComponentToolKit.setTabDisable(allStoreFilterTab,true);
        allStoreFilter_VBox.getChildren().clear();
        ComponentToolKit.setTabDisable(StoreFilterTab,true);
        StoreFilter_VBox.getChildren().clear();
        ComponentToolKit.setTabDisable(BidFilterTab,true);
        BidFilter_VBox.getChildren().clear();
        BigGoPage_ComboBox.getItems().clear();
        ComponentToolKit.setButtonDisable(BigGoPreviousPage_Button,true);
        ComponentToolKit.setButtonDisable(BigGoNextPage_Button,true);
    }
    private ConditionalBigGoSearch_Bean getBigGoSearch_Bean(){
        if(BigGoSearchText.getText() == null || BigGoSearchText.getText().equals("")){
            DialogUI.MessageDialog("請輸入搜尋關鍵字");
            return null;
        }else if(GreaterThanBatchPrice_CheckBox.isSelected() &&
                (GreaterThanBatchPrice_TextField.getText() == null || GreaterThanBatchPrice_TextField.getText().equals(""))){
            DialogUI.MessageDialog("請輸入經銷價");
            return null;
        }else if(bigGoFilterMap.get(BigGoFilterSource.商城).size() == 0 && bigGoFilterMap.get(BigGoFilterSource.拍賣).size() == 0){
            DialogUI.MessageDialog("請選擇至少一間 BigGo 商店");
            return null;
        }
        ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean = new ConditionalBigGoSearch_Bean();
        ConditionalBigGoSearch_Bean.setBigGoSearchText(BigGoSearchText.getText());
        ConditionalBigGoSearch_Bean.setGreaterThanBatchPrice(GreaterThanBatchPrice_CheckBox.isSelected());
        if(ConditionalBigGoSearch_Bean.getGreaterThanBatchPrice()){
            if(GreaterThanBatchPrice_TextField.getText() == null || GreaterThanBatchPrice_TextField.getText().equals(""))
                GreaterThanBatchPrice_TextField.setText(ToolKit.RoundingString(WaitConfirmProductInfo_Bean.getBatchPrice()));
            ConditionalBigGoSearch_Bean.setBatchPrice(ToolKit.RoundingInteger(GreaterThanBatchPrice_TextField.getText()));
        }
        ConditionalBigGoSearch_Bean.setSortByLargeToSmall(SortByLargeToSmall_CheckBox.isSelected());
        ConditionalBigGoSearch_Bean.setBigGoFilterMap(bigGoFilterMap);
        return ConditionalBigGoSearch_Bean;
    }
    private Task BigGoTask(ProductWaitingConfirm_Controller productWaitingConfirm_Controller, ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean){
        return new Task() {

            @Override public Void call(){
                try{
                    ProgressBar.setVisible(true);
                    initialBigGoSearchResultMap(BigGoFilterSource.商城, StoreFilterTab);
                    initialBigGoSearchResultMap(BigGoFilterSource.拍賣, BidFilterTab);
                    initialBigGoSearchResultMap(BigGoFilterSource.未篩選, allStoreFilterTab);
                    stillBigGoSearch.set(true);
                    ProductWaitingConfirm_Model.ConditionalSearchForBigGo(
                            productWaitingConfirm_Controller,
                            BigGoSearchResultMap,
                            ConditionalBigGoSearch_Bean);
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }finally {
                    ProgressBar.setVisible(false);
                }
                return null;
            }
        };
    }
    private void initialBigGoSearchResultMap(BigGoFilterSource bigGoFilterSource, Tab tab){
        BigGoSearchResult_Bean bigGoSearchResult_Bean = BigGoSearchResultMap.get(bigGoFilterSource);
        bigGoSearchResult_Bean.setNowPage(0);
        bigGoSearchResult_Bean.setTotalPage(0);
        bigGoSearchResult_Bean.setHyperlink("");
        if(bigGoSearchResult_Bean.getProductList() != null)
            bigGoSearchResult_Bean.getProductList().clear();
        if(bigGoSearchResult_Bean.getBigGoPageList() != null)
            bigGoSearchResult_Bean.getBigGoPageList().clear();
        ComponentToolKit.setTabDisable(tab,false);
    }
    public void refreshBigGoPageComponent(){
        BigGoSearchResult_Bean BigGoSearchResult_Bean;
        if(allStoreFilterTab.isSelected()){
            BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.未篩選);
        }else if(StoreFilterTab.isSelected()){
            BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.商城);
        }else{
            BigGoSearchResult_Bean = BigGoSearchResultMap.get(BigGoFilterSource.拍賣);
        }
        Integer selectPage = ComponentToolKit.getComboBoxSelectItemIntegerFormat(BigGoPage_ComboBox);
        if(selectPage == null) {
            selectPage = 1;
            BigGoPage_ComboBox.getItems().add(selectPage);
        }

        ObservableList<Integer> bigGoPageList = ComponentToolKit.getComboBoxItemsIntegerFormat(BigGoPage_ComboBox);
        if(BigGoSearchResult_Bean.getBigGoPageList() != null && bigGoPageList.size() != BigGoSearchResult_Bean.getBigGoPageList().size())
            BigGoPage_ComboBox.getItems().add(BigGoSearchResult_Bean.getBigGoPageList().size());
        BigGoPage_ComboBox.getSelectionModel().select(selectPage);
    }
    public void setBigGoSearchResultUI(BigGoSearchResult_Bean BigGoSearchResult_Bean){
        int nowPage = BigGoSearchResult_Bean.getNowPage(), totalPage = BigGoSearchResult_Bean.getTotalPage();
        ArrayList<BigGoProductInfo_Bean> BigGoProductList = BigGoSearchResult_Bean.getProductList();
        if(nowPage == 0 || nowPage == 1)
            ComponentToolKit.setButtonDisable(BigGoPreviousPage_Button,true);
        else
            ComponentToolKit.setButtonDisable(BigGoPreviousPage_Button,false);
        ComponentToolKit.setButtonDisable(BigGoNextPage_Button,nowPage == totalPage);

        if(BigGoProductList == null || BigGoProductList.size() == 0)
            return;
        BigGoPage_ComboBox.getSelectionModel().select(nowPage-1);
        ObservableList<Node> VBoxChild = BigGoSearchResult_Bean.getVBox().getChildren();
        VBoxChild.clear();
        for(int index = (nowPage-1)*10; index < (nowPage*10); index++){
            if(index >= BigGoProductList.size())
                break;
            BigGoProductInfo_Bean BigGoProductInfo_Bean = BigGoProductList.get(index);
            GridPane GridPane = generateBigGoProductGridPane(BigGoProductInfo_Bean);
            VBoxChild.add(GridPane);
        }
    }
    public GridPane generateBigGoProductGridPane(BigGoProductInfo_Bean BigGoProductInfo_Bean){
        GridPane GridPane = ComponentToolKit.setGridPane(Pos.TOP_LEFT,5,0,0,0,10);
        GridPane.setStyle("-fx-background-color: #F5E3AB");
        GridPane.setPrefSize(450,250);
        GridPane.setOnMouseClicked(ActionEvent -> StoreOrBidHyperlinkText.setText(BigGoProductInfo_Bean.getStoreHyperlink()));
        GridPane.add(ComponentToolKit.setLabel(BigGoProductInfo_Bean.getProductName(),350,30,13,"-fx-text-fill: blue"),0,0);

        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER_LEFT,10,15,10,5,10);
        ObservableList<Node> HBoxChild = HBox.getChildren();
        HBoxChild.add(ComponentToolKit.setLabel(BigGoProductInfo_Bean.getProductPrice(),100,30,13,"-fx-text-fill: blue"));
        HBoxChild.add(ComponentToolKit.setLabel(BigGoProductInfo_Bean.getStoreName(),250,30,13,"-fx-text-fill: blue"));
        GridPane.add(HBox,0,1);
        Button insertStoreButton = ComponentToolKit.setButton("新增進銷存",100,20,13);
        insertStoreButton.setOnMouseClicked(ActionEvent ->{
            if(storePriceParameterJson == null){
                DialogUI.AlarmDialog("BigGo 商店金額未設定!");
                return;
            }
            ArrayList<BigGoProductInfo_Bean> storeProductList = BigGoSearchResultMap.get(BigGoFilterSource.商城).getProductList();
            ArrayList<BigGoProductInfo_Bean> bidProductList = BigGoSearchResultMap.get(BigGoFilterSource.拍賣).getProductList();
            double[] singlePriceAndPricingArray = null, vipPrice1To3Array = null;
            if(storeProductList != null && storeProductList.size() > 0){
                singlePriceAndPricingArray = FindBigGoProductMinAndMaxPrice(storeProductList);
            }
            if(bidProductList != null && bidProductList.size() > 0){
                vipPrice1To3Array = FindBigGoProductMinAndMaxPrice(bidProductList);
            }
            if(storeProductList == null || storeProductList.size() == 0)  singlePriceAndPricingArray = vipPrice1To3Array;
            if(bidProductList == null || bidProductList.size() == 0)   vipPrice1To3Array = singlePriceAndPricingArray;

            String manufacturerCode = storePriceParameterJson.getString(Product_Enum.BigGoPriceSettingColumn.manufacturerCode.name());
            boolean taxStatus = storePriceParameterJson.getBoolean(Product_Enum.BigGoPriceSettingColumn.taxStatus.name());

            ProductInfo_Bean ProductInfo_Bean = new ProductInfo_Bean();
            ProductInfo_Bean.setISBN(manufacturerCode == null ? "" : manufacturerCode);
            ProductInfo_Bean.setProductName(BigGoProductInfo_Bean.getProductName());
            ProductInfo_Bean.setPicture1(BigGoProductInfo_Bean.getProductPictureBase64());

            ProductInfo_Bean.setBatchPrice(BigGoProductInfo_Bean.getProductPrice().replace("$","").replace(",","") + ".0");
            ProductInfo_Bean.setSinglePrice(generateStoreParameterPrice(taxStatus, ProductInfo_Bean.getBatchPrice(), storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.singlePrice.name()), singlePriceAndPricingArray));
            ProductInfo_Bean.setPricing(generateStoreParameterPrice(taxStatus, ProductInfo_Bean.getBatchPrice(), storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.pricing.name()), singlePriceAndPricingArray));
            ProductInfo_Bean.setVipPrice1(generateStoreParameterPrice(taxStatus, ProductInfo_Bean.getBatchPrice(), storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.vipPrice1.name()), vipPrice1To3Array));
            ProductInfo_Bean.setVipPrice2(generateStoreParameterPrice(taxStatus, ProductInfo_Bean.getBatchPrice(), storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.vipPrice2.name()), vipPrice1To3Array));
            ProductInfo_Bean.setVipPrice3(generateStoreParameterPrice(taxStatus, ProductInfo_Bean.getBatchPrice(), storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.vipPrice3.name()), vipPrice1To3Array));
            ProductInfo_Bean.setBatchPrice(taxStatus ? ProductInfo_Bean.getBatchPrice() : ToolKit.RoundingString(true,ToolKit.RoundingInteger(ProductInfo_Bean.getBatchPrice())/1.05));
            CallFXML.ManageProductInfo_NewStage(Product_Enum.ManageProductStatus.建立, Stage, ProductInfo_Bean);
            /*
            ArrayList<BigGoProductInfo_Bean> storeProductList = BigGoSearchResultMap.get(BigGoFilterSource.商城).getProductList();
            ArrayList<BigGoProductInfo_Bean> bidProductList = BigGoSearchResultMap.get(BigGoFilterSource.拍賣).getProductList();

            boolean isIncludeTax = DialogUI.ConfirmDialog("欲匯入金額是否含稅 ? \n【是】：匯入當前金額(含稅)\n【否】：當前金額/1.05(未稅)",
                    true,false,0,0);
            double[] singlePriceAndPricingArray = null, vipPrice1To3Array = null;
            if(storeProductList != null && storeProductList.size() > 0){
                singlePriceAndPricingArray = FindBigGoProductMinAndMaxPrice(storeProductList);
                singlePriceAndPricingArray[0] = isIncludeTax ? singlePriceAndPricingArray[0] : singlePriceAndPricingArray[0]/1.05;
                singlePriceAndPricingArray[1] = isIncludeTax ? singlePriceAndPricingArray[1] : singlePriceAndPricingArray[1]/1.05;
            }
            if(bidProductList != null && bidProductList.size() > 0){
                vipPrice1To3Array = FindBigGoProductMinAndMaxPrice(bidProductList);
                vipPrice1To3Array[0] = isIncludeTax ? vipPrice1To3Array[0] : vipPrice1To3Array[0]/1.05;
                vipPrice1To3Array[1] = isIncludeTax ? vipPrice1To3Array[1] : vipPrice1To3Array[1]/1.05;
            }
            if(storeProductList == null || storeProductList.size() == 0)  singlePriceAndPricingArray = vipPrice1To3Array;
            if(bidProductList == null || bidProductList.size() == 0)   vipPrice1To3Array = singlePriceAndPricingArray;
            ProductInfo_Bean ProductInfo_Bean = new ProductInfo_Bean();
            ProductInfo_Bean.setISBN("999");
            ProductInfo_Bean.setProductName(BigGoProductInfo_Bean.getProductName());
            ProductInfo_Bean.setBatchPrice(BigGoProductInfo_Bean.getProductPrice().replace("$","").replace(",","") + ".0");
            ProductInfo_Bean.setPicture1(BigGoProductInfo_Bean.getProductPictureBase64());
            ProductInfo_Bean.setSinglePrice(ToolKit.RoundingString(2,singlePriceAndPricingArray[0]));
            ProductInfo_Bean.setPricing(ToolKit.RoundingString(2,singlePriceAndPricingArray[1]));
            ProductInfo_Bean.setVipPrice1(ToolKit.RoundingString(2,vipPrice1To3Array[1]));
            ProductInfo_Bean.setVipPrice2(ToolKit.RoundingString(2,(vipPrice1To3Array[0]+vipPrice1To3Array[1])/2));
            ProductInfo_Bean.setVipPrice3(ToolKit.RoundingString(2,vipPrice1To3Array[0]));
            CallFXML.ManageProductInfo_NewStage(Product_Enum.ManageProductStatus.建立, Stage, ProductInfo_Bean);*/
        });
        GridPane.add(insertStoreButton,0,2);
        GridPane.add(ComponentToolKit.setPictureStackPane(BigGoProductInfo_Bean.getProductPicture(),250,120),0,3,2,1);
        return GridPane;
    }
    private String generateStoreParameterPrice(boolean taxStatus, String batchPrice,JSONObject jsonObject, double[] priceArray){
        BigGoPriceSettingColumn.priceKey priceKey = BigGoPriceSettingColumn.priceKey.valueOf(jsonObject.keySet().iterator().next());
        double returnPrice;
        if(priceKey == BigGoPriceSettingColumn.priceKey.max){
            returnPrice = priceArray[0];
        }else if(priceKey == BigGoPriceSettingColumn.priceKey.min){
            returnPrice = priceArray[1];
        }else if(priceKey == BigGoPriceSettingColumn.priceKey.average){
            returnPrice = (priceArray[0]+priceArray[1])/2;
        }else{
            double percentage = jsonObject.getDouble(priceKey.name());
            returnPrice = ToolKit.RoundingInteger(batchPrice)*percentage;
        }
        return taxStatus ? ToolKit.RoundingString(true,returnPrice) : ToolKit.RoundingString(true,returnPrice/1.05);
    }
    private void initialComponent(){
        ProductNameText.setText("");
        BatchPriceText.setText("");
        LowestProfitText_ProductInfo.setText("");
        PricingText.setText("");
        SinglePriceText.setText("");
        VipPrice1Text.setText("");
        VipPrice2Text.setText("");
        VipPrice3Text.setText("");
        OldSinglePriceText.setText("");
        OldPricingText.setText("");
        OldVipPrice1Text.setText("");
        OldVipPrice2Text.setText("");
        OldVipPrice3Text.setText("");
        SupplyStatusText.setText("");
        PictureImageView.setImage(null);
        ComponentToolKit.setButtonDisable(SingleProductUpdate_Button,true);
        ComponentToolKit.setButtonDisable(EditProductDescribe_Button,true);
        ComponentToolKit.setButtonDisable(EditProductRemark_Button,true);
        ComponentToolKit.setButtonDisable(HandleStoreProduct_Button,true);

        LowestProfitText_ComparePriceInfo.setText("");
        BigGoSearchText.setText("");
        StoreOrBidHyperlinkText.setText("");
        BigGoHyperlinkText.setText("");
        ComponentToolKit.setCheckBoxSelect(GreaterThanBatchPrice_CheckBox,false);
        ComponentToolKit.setCheckBoxSelect(SortByLargeToSmall_CheckBox,false);
        Platform.runLater(this::clearBigGoSearchInfo);
        ComponentToolKit.getWaitConfirmTableViewList(TableView).clear();
    }
    private void setDoublePriceColumnMicrometerFormat(TableColumn<WaitConfirmProductInfo_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setDoublePriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setDoublePriceColumnMicrometerFormat extends TableCell<WaitConfirmProductInfo_Bean, Double> {
        String Alignment, FontSize;
        setDoublePriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
            }
        }
    }
    private void setBatchPriceColumnTextFill(TableColumn<WaitConfirmProductInfo_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setBatchPriceTextFill(Alignment, FontSize));
    }
    private class setBatchPriceTextFill extends TableCell<WaitConfirmProductInfo_Bean, Double>{
        String Alignment, FontSize;
        setBatchPriceTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
                WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
                if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.更新){
                    WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = getTableView().getItems().get(getIndex());
                    double CheckStorePrice = WaitConfirmProductInfo_Bean.getBatchPrice();
                    double StorePrice = WaitConfirmProductInfo_Bean.getStoreBatchPrice();
                    if (CheckStorePrice == StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: black");
                    else if (CheckStorePrice > StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: red");
                    else if (CheckStorePrice < StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: blue");
                }
            }
        }
    }
    private void setPricingColumnTextFill(TableColumn<WaitConfirmProductInfo_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setPricingTextFill(Alignment, FontSize));
    }
    private class setPricingTextFill extends TableCell<WaitConfirmProductInfo_Bean, Double>{
        String Alignment, FontSize;
        setPricingTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
                WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
                if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.更新){
                    WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = getTableView().getItems().get(getIndex());
                    double CheckStorePrice = WaitConfirmProductInfo_Bean.getPricing();
                    double StorePrice = WaitConfirmProductInfo_Bean.getStorePricing();
                    if (CheckStorePrice == StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: black");
                    else if (CheckStorePrice > StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: red");
                    else if (CheckStorePrice < StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: blue");
                }
            }
        }
    }
    private void setSinglePriceColumnTextFill(TableColumn<WaitConfirmProductInfo_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setSinglePriceTextFill(Alignment, FontSize));
    }
    private class setSinglePriceTextFill extends TableCell<WaitConfirmProductInfo_Bean, Double>{
        String Alignment, FontSize;
        setSinglePriceTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
                WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
                if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.更新){
                    WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = getTableView().getItems().get(getIndex());
                    double CheckStorePrice = WaitConfirmProductInfo_Bean.getSinglePrice();
                    double StorePrice = WaitConfirmProductInfo_Bean.getStoreSinglePrice();
                    if (CheckStorePrice == StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: black");
                    else if (CheckStorePrice > StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: red");
                    else if (CheckStorePrice < StorePrice)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: blue");
                }
            }
        }
    }
    private void setSupplyStatusColumnTextFill(TableColumn<WaitConfirmProductInfo_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setSupplyStatusTextFill(Alignment, FontSize));
    }
    private class setSupplyStatusTextFill extends TableCell<WaitConfirmProductInfo_Bean, String> {
        String Alignment, FontSize;
        setSupplyStatusTextFill(String Alignment, String FontSize){
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
                WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = getTableView().getItems().get(getIndex());
                String SupplyStatus = WaitConfirmProductInfo_Bean.getSupplyStatus();
                if(!SupplyStatus.contains("供貨中") && !SupplyStatus.equals("庫存充足") && !SupplyStatus.equals("貨物充足"))   setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: CENTER-LEFT;-fx-text-fill: red");
                else    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: CENTER-LEFT;-fx-text-fill: black");
            }
        }
    }

    private void setStatusButton(){
        WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
        if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.新增)
            StatusButton_TableColumn.setCellFactory(p -> new CreateButton(TableViewButtonStatus.新增));
        else if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.更新)
            StatusButton_TableColumn.setCellFactory(p -> new CreateButton(TableViewButtonStatus.更新));
        else if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.忽略)
            StatusButton_TableColumn.setCellFactory(p -> new CreateButton(TableViewButtonStatus.復原));
        else if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.封存)
            StatusButton_TableColumn.setCellFactory(p -> new CreateButton(TableViewButtonStatus.封存));
    }
    private void setDeleteButton(){
        DeleteButton_TableColumn.setCellFactory(p -> new CreateButton(TableViewButtonStatus.刪除));
    }
    private void setIgnoreButton(){
        IgnoreButton_TableColumn.setCellFactory(p -> new CreateButton(TableViewButtonStatus.忽略));
    }
    private class CreateButton extends TableCell<WaitConfirmProductInfo_Bean, String> {
        TableViewButtonStatus TableViewButtonStatus;
        Button Button;
        CreateButton(TableViewButtonStatus TableViewButtonStatus) {
            this.TableViewButtonStatus = TableViewButtonStatus;
            Button = new Button(TableViewButtonStatus.name());
            Button.setPrefWidth(65);
            if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.新增)
                Button.setDisable(true);

            if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.刪除)
                Button.setStyle("-fx-background-color:#FFCDC8;");
            else if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.忽略)
                Button.setStyle("-fx-background-color:#FFE153;");

            Button.setOnAction(e -> {
                WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = TableView.getItems().get(getIndex());
                if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.更新)
                    if(Product_Model.modifyProduct(generateModifyProductInfoBean(WaitConfirmProductInfo_Bean),true,false)){
                        ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                        DialogUI.MessageDialog("※ 更新成功");
                    }else   DialogUI.MessageDialog("※ 更新失敗");
                else if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.封存)
                    if(ProductWaitingConfirm_Model.archiveCheckStoreProduct(WaitConfirmProductInfo_Bean.getProductCode())){
                        ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                        DialogUI.MessageDialog("※ 封存成功");
                    }else   DialogUI.MessageDialog("※ 封存失敗");
                else if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.刪除)
                    if(ProductWaitingConfirm_Model.deleteCheckStoreProduct(WaitConfirmProductInfo_Bean.getProductCode())){
                        ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                        DialogUI.MessageDialog("※ 刪除成功");
                    }else   DialogUI.MessageDialog("※ 刪除失敗");
                else if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.忽略){
                    WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
                    if(ProductWaitingConfirm_Model.ignoreCheckStoreProduct(WaitConfirmProductInfo_Bean.getProductCode(), WaitConfirmStatus)){
                        ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                        DialogUI.MessageDialog("※ 忽略成功");
                    }else   DialogUI.MessageDialog("※ 忽略失敗");
                }else if(TableViewButtonStatus == Product_Enum.TableViewButtonStatus.復原)
                    if(ProductWaitingConfirm_Model.recoverCheckStoreProduct(WaitConfirmProductInfo_Bean.getProductCode(),WaitConfirmProductInfo_Bean.getPreviousStatus())){
                        ComponentToolKit.getWaitConfirmTableViewList(TableView).remove(WaitConfirmProductInfo_Bean);
                        DialogUI.MessageDialog("※ 復原成功");
                    }else   DialogUI.MessageDialog("※ 復原失敗");
            });
        }
        @Override
        protected void updateItem(String WaitConfirmProductInfo_Bean, boolean empty) {
            super.updateItem(WaitConfirmProductInfo_Bean, empty);
            if (empty)
                setGraphic(null);
            else{
                setGraphic(Button);
                WaitConfirmStatus WaitConfirmStatus = ComponentToolKit.getWaitConfirmStatusComboBoxSelectItem(WaitConfirmStatus_ComboBox);
                WaitConfirmProductInfo_Bean ProductInfoBean = getTableView().getItems().get(getIndex());
                if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.新增 && TableViewButtonStatus == Product_Enum.TableViewButtonStatus.新增)
                    Button.setDisable(true);
                else if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.忽略 && TableViewButtonStatus == Product_Enum.TableViewButtonStatus.忽略)
                    Button.setDisable(true);
                else if (ProductInfoBean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品)
                    Button.setDisable(true);
                else if(ProductInfoBean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品)
                    Button.setDisable(false);
            }
            setText(null);
        }
    }
}