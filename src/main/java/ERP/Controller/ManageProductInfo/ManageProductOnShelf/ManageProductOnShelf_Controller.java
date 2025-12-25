package ERP.Controller.ManageProductInfo.ManageProductOnShelf;

import ERP.Bean.Product.ManageProductOnShelf_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.BidStore;
import ERP.Enum.Product.Product_Enum.BidStoreStatus;
import ERP.Model.Product.Product_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Manage product on shelf */
public class ManageProductOnShelf_Controller {
    @FXML Tab WaitingOnShelf_Tab, AlreadyOnShelf_Tab, AlreadyOffShelf_Tab, OnlineWaitingOffShelf_Tab , OnlineWaitingUpdate_Tab;

    @FXML Button clearWaitingOnShelf_Button, WebOnShelf_Button, YahooOnShelf_Button, RutenOnShelf_Button, ShopeeOnShelf_Button;

    @FXML Button AlreadyOnShelf_YahooButton, AlreadyOnShelf_RutenButton, AlreadyOnShelf_ShopeeButton, SearchProduct_Button, WebOffShelf_Button, YahooOffShelf_Button, RutenOffShelf_Button, refreshVisitors_Button;
    @FXML TextField SearchProductTextField;

    @FXML Button AlreadyOffShelf_YahooButton, AlreadyOffShelf_RutenButton, AlreadyOffShelf_ShopeeButton, AlreadyOffShelf_UnSell_Button, AlreadyOffShelf_AlreadySell_Button, ReOnShelf_Button, refreshData_Button;

    @FXML Button OnlineWaitingOffShelf_YahooButton, OnlineWaitingOffShelf_RutenButton, OnlineWaitingOffShelf_ShopeeButton, deleteProduct_Button;

    @FXML Button OnlineWaitingUpdate_YahooButton, OnlineWaitingUpdate_RutenButton, OnlineWaitingUpdate_ShopeeButton;

    @FXML CheckBox WaitingOnShelf_WebSelectAll_CheckBox, WaitingOnShelf_YahooSelectAll_CheckBox, WaitingOnShelf_RutenSelectAll_CheckBox, WaitingOnShelf_ShopeeSelectAll_CheckBox;
    @FXML CheckBox AlreadyOnShelf_SelectAll_CheckBox;
    @FXML CheckBox AlreadyOffShelf_SelectAll_CheckBox;
    @FXML CheckBox OnlineWaitingOffShelf_SelectAll_CheckBox;
    @FXML CheckBox OnlineWaitingUpdate_SelectAll_CheckBox;
    @FXML TableColumn<ManageProductOnShelf_Bean, CheckBox> WebCheckBoxColumn, YahooCheckBoxColumn, RutenCheckBoxColumn, ShopeeCheckBoxColumn;
    @FXML TableColumn<ManageProductOnShelf_Bean, String> ISBNColumn, InternationalCodeColumn, FirmCodeColumn, ProductNameColumn, UnitColumn, VisitorsColumn, FollowersColumn, SellingVolumeColumn, BidCodeColumn;
    @FXML TableColumn<ManageProductOnShelf_Bean, Double> BatchPriceColumn, SinglePriceColumn, PricingColumn, VipPrice1Column, VipPrice2Column, VipPrice3Column, SpecialOfferColumn;
    @FXML TableView<ManageProductOnShelf_Bean> TableView;

    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private CallFXML CallFXML;
    private Product_Model Product_Model;
    private SystemSetting_Model SystemSetting_Model;
    private Stage MainStage;

    private BidStoreStatus BidStoreStatus;
    public ManageProductOnShelf_Controller(){
        this.KeyPressed = ERPApplication.ToolKit.KeyPressed;
        this.ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        this.CallFXML = ERPApplication.ToolKit.CallFXML;
        this.Product_Model = ERPApplication.ToolKit.ModelToolKit.getProductModel();
        this.SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setMainStage(Stage Stage){
        this.MainStage = Stage;
    }
    /** Set component of manage product on shelf */
    public void setComponent(){
        initialTableView();

        BidStoreStatus = Product_Enum.BidStoreStatus.待上架;
        refreshUI(null);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(BidStore.網站, WebCheckBoxColumn,"WebCheckBox","CENTER", "16");
        setColumnCellValueAndCheckBox(BidStore.奇摩, YahooCheckBoxColumn,"YahooCheckBox","CENTER", "16");
        setColumnCellValueAndCheckBox(BidStore.露天, RutenCheckBoxColumn,"RutenCheckBox","CENTER", "16");
        setColumnCellValueAndCheckBox(BidStore.蝦皮, ShopeeCheckBoxColumn,"ShopeeCheckBox","CENTER", "16");

        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InternationalCodeColumn,"InternationalCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(FirmCodeColumn,"FirmCode", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(BatchPriceColumn,"BatchPrice", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SinglePriceColumn,"SinglePrice", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PricingColumn,"Pricing", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(VipPrice1Column,"VipPrice1", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(VipPrice2Column,"VipPrice2", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(VipPrice3Column,"VipPrice3", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SpecialOfferColumn,"SpecialOffer", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(VisitorsColumn,"Visitors", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(FollowersColumn,"Followers", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SellingVolumeColumn,"SellingVolume", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(BidCodeColumn,"BidCode", "CENTER-LEFT", "16",null);
    }
    private void refreshUI(BidStore BidStore){
        TableView.getItems().clear();
        if(BidStoreStatus == Product_Enum.BidStoreStatus.待上架)   setWaitingOnShelfUI();
        else if(BidStoreStatus == Product_Enum.BidStoreStatus.已上架)  setAlreadyOnShelfUI(BidStore);
        else if(BidStoreStatus == Product_Enum.BidStoreStatus.已下架)  setAlreadyOffShelfUI(BidStore);
        else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待下架)    setOnlineWaitingOffShelfUI(BidStore);
        else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待修改)    setOnlineWaitingUpdateUI(BidStore);
    }
    private void setWaitingOnShelfUI(){
        ComponentToolKit.setTableColumnVisible(WebCheckBoxColumn,true);
        ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
        ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
        ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn,true);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn,true);
        ComponentToolKit.setTableColumnVisible(PricingColumn,true);
        ComponentToolKit.setTableColumnVisible(VipPrice1Column,true);
        ComponentToolKit.setTableColumnVisible(VipPrice2Column,true);
        ComponentToolKit.setTableColumnVisible(VipPrice3Column,true);
        ComponentToolKit.setTableColumnVisible(SpecialOfferColumn,true);
        ComponentToolKit.setTableColumnVisible(VisitorsColumn,false);
        ComponentToolKit.setTableColumnVisible(FollowersColumn,false);
        ComponentToolKit.setTableColumnVisible(SellingVolumeColumn,false);
        ComponentToolKit.setTableColumnVisible(BidCodeColumn,false);

        ObservableList<ManageProductOnShelf_Bean> ProductWaitingOnShelfList = Product_Model.getProductWaitingOnShelf();
        TableView.setItems(ProductWaitingOnShelfList);
        if(ProductWaitingOnShelfList.size() == 0){
            ComponentToolKit.setButtonDisable(clearWaitingOnShelf_Button,true);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_WebSelectAll_CheckBox,false);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_YahooSelectAll_CheckBox,false);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_RutenSelectAll_CheckBox,false);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_ShopeeSelectAll_CheckBox,false);
        }else{
            ComponentToolKit.setButtonDisable(clearWaitingOnShelf_Button,false);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_WebSelectAll_CheckBox,true);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_YahooSelectAll_CheckBox,true);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_RutenSelectAll_CheckBox,true);
            ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_ShopeeSelectAll_CheckBox,true);
        }
    }
    private void setAlreadyOnShelfUI(BidStore BidStore){
        SearchProductTextField.setText("");
        AlreadyOnShelf_SelectAll_CheckBox.setSelected(false);
        if(BidStore == Product_Enum.BidStore.奇摩){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.露天){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.蝦皮) {
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }else{
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }
        ComponentToolKit.setTableColumnVisible(WebCheckBoxColumn,false);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn,true);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn,false);
        ComponentToolKit.setTableColumnVisible(PricingColumn,true);
        ComponentToolKit.setTableColumnVisible(VipPrice1Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice2Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice3Column,false);
        ComponentToolKit.setTableColumnVisible(SpecialOfferColumn,false);
        ComponentToolKit.setTableColumnVisible(VisitorsColumn,true);
        ComponentToolKit.setTableColumnVisible(FollowersColumn,true);
        ComponentToolKit.setTableColumnVisible(SellingVolumeColumn,true);
        ComponentToolKit.setTableColumnVisible(BidCodeColumn,true);
    }
    private void setAlreadyOffShelfUI(BidStore BidStore){
        AlreadyOffShelf_SelectAll_CheckBox.setSelected(false);
        if(BidStore == Product_Enum.BidStore.奇摩){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.露天){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.蝦皮){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }else{
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }
        ComponentToolKit.setTableColumnVisible(WebCheckBoxColumn,false);
        ComponentToolKit.setButtonDisable(AlreadyOffShelf_UnSell_Button,true);
        ComponentToolKit.setButtonDisable(AlreadyOffShelf_AlreadySell_Button,true);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn,true);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn,false);
        ComponentToolKit.setTableColumnVisible(PricingColumn,true);
        ComponentToolKit.setTableColumnVisible(VipPrice1Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice2Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice3Column,false);
        ComponentToolKit.setTableColumnVisible(SpecialOfferColumn,false);
        ComponentToolKit.setTableColumnVisible(VisitorsColumn,true);
        ComponentToolKit.setTableColumnVisible(FollowersColumn,true);
        ComponentToolKit.setTableColumnVisible(SellingVolumeColumn,true);
        ComponentToolKit.setTableColumnVisible(BidCodeColumn,true);
    }
    private void setOnlineWaitingOffShelfUI(BidStore BidStore){
        OnlineWaitingOffShelf_SelectAll_CheckBox.setSelected(false);
        if(BidStore == Product_Enum.BidStore.奇摩){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.露天){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.蝦皮){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }else{
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }
        ComponentToolKit.setTableColumnVisible(WebCheckBoxColumn,false);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn,true);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn,false);
        ComponentToolKit.setTableColumnVisible(PricingColumn,true);
        ComponentToolKit.setTableColumnVisible(VipPrice1Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice2Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice3Column,false);
        ComponentToolKit.setTableColumnVisible(SpecialOfferColumn,false);
        ComponentToolKit.setTableColumnVisible(VisitorsColumn,true);
        ComponentToolKit.setTableColumnVisible(FollowersColumn,true);
        ComponentToolKit.setTableColumnVisible(SellingVolumeColumn,true);
        ComponentToolKit.setTableColumnVisible(BidCodeColumn,true);
    }
    private void setOnlineWaitingUpdateUI(BidStore BidStore){
        OnlineWaitingUpdate_SelectAll_CheckBox.setSelected(false);
        if(BidStore == Product_Enum.BidStore.奇摩){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.露天){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,false);
        }else if(BidStore == Product_Enum.BidStore.蝦皮){
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,false);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }else{
            ComponentToolKit.setTableColumnVisible(YahooCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(RutenCheckBoxColumn,true);
            ComponentToolKit.setTableColumnVisible(ShopeeCheckBoxColumn,true);
        }
        ComponentToolKit.setTableColumnVisible(WebCheckBoxColumn,false);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn,false);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn,false);
        ComponentToolKit.setTableColumnVisible(PricingColumn,false);
        ComponentToolKit.setTableColumnVisible(VipPrice1Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice2Column,false);
        ComponentToolKit.setTableColumnVisible(VipPrice3Column,false);
        ComponentToolKit.setTableColumnVisible(SpecialOfferColumn,false);
        ComponentToolKit.setTableColumnVisible(VisitorsColumn,true);
        ComponentToolKit.setTableColumnVisible(FollowersColumn,true);
        ComponentToolKit.setTableColumnVisible(SellingVolumeColumn,false);
        ComponentToolKit.setTableColumnVisible(BidCodeColumn,true);
    }
    /** Tab On Selection Changed - 待上架 */
    @FXML protected void WaitingOnShelfTabOnSelectionChanged(){
        if(WaitingOnShelf_Tab.isSelected()){
            BidStoreStatus = Product_Enum.BidStoreStatus.待上架;
            if(TableView != null)   refreshUI(null);
        }

    }
    /** Tab On Selection Changed - 已上架 */
    @FXML protected void AlreadyOnShelfTabOnSelectionChanged(){
        if(AlreadyOnShelf_Tab.isSelected()){
            AlreadyOnShelf_YahooButton.setDefaultButton(false);
            AlreadyOnShelf_RutenButton.setDefaultButton(false);
            AlreadyOnShelf_ShopeeButton.setDefaultButton(false);
            BidStoreStatus = Product_Enum.BidStoreStatus.已上架;
            refreshUI(null);
        }
    }
    /** Tab On Selection Changed - 已下架 */
    @FXML protected void AlreadyOffShelfTabOnSelectionChanged(){
        if(AlreadyOffShelf_Tab.isSelected()){
            AlreadyOffShelf_YahooButton.setDefaultButton(false);
            AlreadyOffShelf_RutenButton.setDefaultButton(false);
            AlreadyOffShelf_ShopeeButton.setDefaultButton(false);
            BidStoreStatus = Product_Enum.BidStoreStatus.已下架;
            refreshUI(null);
        }
    }
    /** Tab On Selection Changed - 線上，待下架 */
    @FXML protected void OnlineWaitingOffShelfTabOnSelectionChanged(){
        if(OnlineWaitingOffShelf_Tab.isSelected()){
            OnlineWaitingOffShelf_YahooButton.setDefaultButton(false);
            OnlineWaitingOffShelf_RutenButton.setDefaultButton(false);
            OnlineWaitingOffShelf_ShopeeButton.setDefaultButton(false);
            BidStoreStatus = Product_Enum.BidStoreStatus.線上待下架;
            refreshUI(null);
        }
    }
    /** Tab On Selection Changed - 線上，待修改 */
    @FXML protected void OnlineWaitingUpdateTabOnSelectionChanged(){
        if(OnlineWaitingUpdate_Tab.isSelected()){
            OnlineWaitingUpdate_YahooButton.setDefaultButton(false);
            OnlineWaitingUpdate_RutenButton.setDefaultButton(false);
            OnlineWaitingUpdate_ShopeeButton.setDefaultButton(false);
            BidStoreStatus = Product_Enum.BidStoreStatus.線上待修改;
            refreshUI(null);
        }
    }
    /** Button Mouse Clicked - [待上架] 清空待上架商品 */
    @FXML protected void clearWaitingOnShelfMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.ConfirmDialog("※ 是否清空所有待上架商品 ?",true,false,0,0)){
                if(Product_Model.deleteWaitingOnShelf()){
                    TableView.getItems().clear();
                    ComponentToolKit.setButtonDisable(clearWaitingOnShelf_Button,true);
                    ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_WebSelectAll_CheckBox,false);
                    ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_YahooSelectAll_CheckBox,false);
                    ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_RutenSelectAll_CheckBox,false);
                    ComponentToolKit.setCheckBoxSelect(WaitingOnShelf_ShopeeSelectAll_CheckBox,false);
                    DialogUI.MessageDialog("※ 清空完成");
                }else    DialogUI.MessageDialog("※ 清空失敗");
            }
        }
    }
    /** CheckBox On Action - [待上架] 商品設定 */
    @FXML protected void ProductDefaultSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ManageProductOnShelf_Bean ManageProductOnShelf_Bean = ComponentToolKit.getManageProductOnShelfTableViewSelectItem(TableView);
            if(ManageProductOnShelf_Bean == null)
                DialogUI.MessageDialog("※ 請選擇商品");
            else
                CallFXML.ShowProductOnShelfSetting(MainStage, ManageProductOnShelf_Bean);
        }
    }
    /** CheckBox On Action - [待上架] 網站全選 */
    @FXML protected void WaitingOnShelf_WebSelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(WaitingOnShelfList.size() != 0)
            setTableViewItemsWebSelectStatus(WaitingOnShelfList, WaitingOnShelf_WebSelectAll_CheckBox.isSelected());
    }
    /** CheckBox On Action - [待上架] 奇摩全選 */
    @FXML protected void WaitingOnShelf_YahooSelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(WaitingOnShelfList.size() != 0)
            setTableViewItemsYahooSelectStatus(WaitingOnShelfList, WaitingOnShelf_YahooSelectAll_CheckBox.isSelected());
    }
    /** CheckBox On Action - [待上架] 露天全選 */
    @FXML protected void WaitingOnShelf_RutenSelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(WaitingOnShelfList.size() != 0)
            setTableViewItemsRutenSelectStatus(WaitingOnShelfList, WaitingOnShelf_RutenSelectAll_CheckBox.isSelected());
    }
    /** CheckBox On Action - [待上架] 蝦皮全選 */
    @FXML protected void WaitingOnShelf_ShopeeSelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(WaitingOnShelfList.size() != 0)
            setTableViewItemsShopeeSelectStatus(WaitingOnShelfList, WaitingOnShelf_ShopeeSelectAll_CheckBox.isSelected());
    }
    /** CheckBox On Action - [已上架] 網站全選 */
    @FXML protected void AlreadyOnShelf_SelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> AlreadyOnShelfList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(AlreadyOnShelfList.size() != 0) {
            if(AlreadyOnShelf_YahooButton.isDefaultButton())    setTableViewItemsYahooSelectStatus(AlreadyOnShelfList, AlreadyOnShelf_SelectAll_CheckBox.isSelected());
            else if(AlreadyOnShelf_RutenButton.isDefaultButton())   setTableViewItemsRutenSelectStatus(AlreadyOnShelfList, AlreadyOnShelf_SelectAll_CheckBox.isSelected());
            else if(AlreadyOnShelf_ShopeeButton.isDefaultButton())   setTableViewItemsShopeeSelectStatus(AlreadyOnShelfList, AlreadyOnShelf_SelectAll_CheckBox.isSelected());
        }
    }
    /** CheckBox On Action - [已下架] 網站全選 */
    @FXML protected void AlreadyOffShelf_SelectAllOnAction(){

    }
    /** CheckBox On Action - [線上，待下架] 網站全選 */
    @FXML protected void OnlineWaitingOffShelf_SelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> OnlineWaitingOffShelfList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(OnlineWaitingOffShelfList.size() != 0) {
            if(OnlineWaitingOffShelf_YahooButton.isDefaultButton())    setTableViewItemsYahooSelectStatus(OnlineWaitingOffShelfList, OnlineWaitingOffShelf_SelectAll_CheckBox.isSelected());
            else if(OnlineWaitingOffShelf_RutenButton.isDefaultButton())   setTableViewItemsRutenSelectStatus(OnlineWaitingOffShelfList, OnlineWaitingOffShelf_SelectAll_CheckBox.isSelected());
            else if(OnlineWaitingOffShelf_ShopeeButton.isDefaultButton())   setTableViewItemsShopeeSelectStatus(OnlineWaitingOffShelfList, OnlineWaitingOffShelf_SelectAll_CheckBox.isSelected());
        }
    }
    /** CheckBox On Action - [線上，待修改] 網站全選 */
    @FXML protected void OnlineWaitingUpdate_SelectAllOnAction(){
        ObservableList<ManageProductOnShelf_Bean> OnlineWaitingUpdateList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
        if(OnlineWaitingUpdateList.size() != 0) {
            if(OnlineWaitingUpdate_YahooButton.isDefaultButton())    setTableViewItemsYahooSelectStatus(OnlineWaitingUpdateList, OnlineWaitingUpdate_SelectAll_CheckBox.isSelected());
            else if(OnlineWaitingUpdate_RutenButton.isDefaultButton())   setTableViewItemsRutenSelectStatus(OnlineWaitingUpdateList, OnlineWaitingUpdate_SelectAll_CheckBox.isSelected());
            else if(OnlineWaitingUpdate_ShopeeButton.isDefaultButton())   setTableViewItemsShopeeSelectStatus(OnlineWaitingUpdateList, OnlineWaitingUpdate_SelectAll_CheckBox.isSelected());
        }
    }
    /** Button Mouse Clicked - [已上架] 搜尋 */
    @FXML protected void SearchProductOnShelfMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            DialogUI.MessageDialog("※ 暫無功能");
            /*
            if(AlreadyOnShelf_YahooButton.isDefaultButton())    searchProductOnShelf(BidStore.奇摩);
            else if(AlreadyOnShelf_RutenButton.isDefaultButton())   searchProductOnShelf(BidStore.露天);
            else if(AlreadyOnShelf_ShopeeButton.isDefaultButton())  searchProductOnShelf(BidStore.蝦皮);
            else    DialogUI.MessageDialog("※ 請選擇賣場");
             */
        }
    }
    /** TextField Key Pressed - [已上架] 搜尋 */
    @FXML protected void SearchProductTextKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            DialogUI.MessageDialog("※ 暫無功能");
            /*
            if(AlreadyOnShelf_YahooButton.isDefaultButton())    searchProductOnShelf(BidStore.奇摩);
            else if(AlreadyOnShelf_RutenButton.isDefaultButton())   searchProductOnShelf(BidStore.露天);
            else if(AlreadyOnShelf_ShopeeButton.isDefaultButton())  searchProductOnShelf(BidStore.蝦皮);
            else    DialogUI.MessageDialog("※ 請選擇賣場");
             */
        }
    }
    /** Button Mouse Clicked - [已下架] 未售出 */
    @FXML protected void UnSellMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            DialogUI.MessageDialog("※ 暫無功能");
//            ObservableList<ManageProductOnShelf_Bean> AlreadyOffShelfList = Product_Model.getProductAlreadyOffShelf();
//            if(AlreadyOffShelfList.size() != 0){
//                ComponentToolKit.setCheckBoxSelect(AlreadyOffShelf_SelectAll_CheckBox,true);
//                TableView.setItems(AlreadyOffShelfList);
//            }else{
//                ComponentToolKit.setCheckBoxSelect(AlreadyOffShelf_SelectAll_CheckBox,false);
//                DialogUI.MessageDialog("※ 查無商品");
//            }
        }
    }
    /** Button Mouse Clicked - [已下架] 已售出 */
    @FXML protected void AlreadySellMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            DialogUI.MessageDialog("※ 暫無功能");
        }
    }
    /** Button Mouse Clicked - 奇摩賣場 */
    @FXML protected void YahooMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(AlreadyOnShelf_Tab.isSelected()){
                AlreadyOnShelf_YahooButton.setDefaultButton(true);
                AlreadyOnShelf_RutenButton.setDefaultButton(false);
                AlreadyOnShelf_ShopeeButton.setDefaultButton(false);
            }else if(AlreadyOffShelf_Tab.isSelected()){
                AlreadyOffShelf_YahooButton.setDefaultButton(true);
                AlreadyOffShelf_RutenButton.setDefaultButton(false);
                AlreadyOffShelf_ShopeeButton.setDefaultButton(false);
            }else if(OnlineWaitingOffShelf_Tab.isSelected()){
                OnlineWaitingOffShelf_YahooButton.setDefaultButton(true);
                OnlineWaitingOffShelf_RutenButton.setDefaultButton(false);
                OnlineWaitingOffShelf_ShopeeButton.setDefaultButton(false);
            }else if(OnlineWaitingUpdate_Tab.isSelected()){
                OnlineWaitingUpdate_YahooButton.setDefaultButton(true);
                OnlineWaitingUpdate_RutenButton.setDefaultButton(false);
                OnlineWaitingUpdate_ShopeeButton.setDefaultButton(false);
            }
            refreshUI(BidStore.奇摩);
            /*if(BidStoreStatus == Product_Enum.BidStoreStatus.已上架)   searchProductOnShelf(BidStore.奇摩);
            else if(BidStoreStatus == Product_Enum.BidStoreStatus.已下架){
                ComponentToolKit.setButtonDisable(AlreadyOffShelf_UnSell_Button,false);
                ComponentToolKit.setButtonDisable(AlreadyOffShelf_AlreadySell_Button,false);
            }else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待下架)   searchProductOnlineWaitingOffShelf(BidStore.奇摩);
            else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待修改)   searchProductOnlineWaitingUpdate(BidStore.奇摩);*/
        }
    }
    /** Button Mouse Clicked - 露天賣場 */
    @FXML protected void RutenMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(AlreadyOnShelf_Tab.isSelected()){
                AlreadyOnShelf_YahooButton.setDefaultButton(false);
                AlreadyOnShelf_RutenButton.setDefaultButton(true);
                AlreadyOnShelf_ShopeeButton.setDefaultButton(false);
            }else if(AlreadyOffShelf_Tab.isSelected()){
                AlreadyOffShelf_YahooButton.setDefaultButton(false);
                AlreadyOffShelf_RutenButton.setDefaultButton(true);
                AlreadyOffShelf_ShopeeButton.setDefaultButton(false);
            }else if(OnlineWaitingOffShelf_Tab.isSelected()){
                OnlineWaitingOffShelf_YahooButton.setDefaultButton(false);
                OnlineWaitingOffShelf_RutenButton.setDefaultButton(true);
                OnlineWaitingOffShelf_ShopeeButton.setDefaultButton(false);
            }else if(OnlineWaitingUpdate_Tab.isSelected()){
                OnlineWaitingUpdate_YahooButton.setDefaultButton(false);
                OnlineWaitingUpdate_RutenButton.setDefaultButton(true);
                OnlineWaitingUpdate_ShopeeButton.setDefaultButton(false);
            }
            refreshUI(BidStore.露天);
            /*if(BidStoreStatus == Product_Enum.BidStoreStatus.已上架)   searchProductOnShelf(BidStore.露天);
            else if(BidStoreStatus == Product_Enum.BidStoreStatus.已下架){
                ComponentToolKit.setButtonDisable(AlreadyOffShelf_UnSell_Button,false);
                ComponentToolKit.setButtonDisable(AlreadyOffShelf_AlreadySell_Button,false);
            }else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待下架)   searchProductOnlineWaitingOffShelf(BidStore.露天);
            else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待修改)   searchProductOnlineWaitingUpdate(BidStore.露天);*/
        }
    }
    /** Button Mouse Clicked - 蝦皮賣場 */
    @FXML protected void ShopeeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(AlreadyOnShelf_Tab.isSelected()){
                AlreadyOnShelf_YahooButton.setDefaultButton(false);
                AlreadyOnShelf_RutenButton.setDefaultButton(false);
                AlreadyOnShelf_ShopeeButton.setDefaultButton(true);
            }else if(AlreadyOffShelf_Tab.isSelected()){
                AlreadyOffShelf_YahooButton.setDefaultButton(false);
                AlreadyOffShelf_RutenButton.setDefaultButton(false);
                AlreadyOffShelf_ShopeeButton.setDefaultButton(true);
            }else if(OnlineWaitingOffShelf_Tab.isSelected()){
                OnlineWaitingOffShelf_YahooButton.setDefaultButton(false);
                OnlineWaitingOffShelf_RutenButton.setDefaultButton(false);
                OnlineWaitingOffShelf_ShopeeButton.setDefaultButton(true);
            }else if(OnlineWaitingUpdate_Tab.isSelected()){
                OnlineWaitingUpdate_YahooButton.setDefaultButton(false);
                OnlineWaitingUpdate_RutenButton.setDefaultButton(false);
                OnlineWaitingUpdate_ShopeeButton.setDefaultButton(true);
            }
            refreshUI(BidStore.蝦皮);
            /*if(BidStoreStatus == Product_Enum.BidStoreStatus.已上架)   searchProductOnShelf(BidStore.蝦皮);
            else if(BidStoreStatus == Product_Enum.BidStoreStatus.已下架){
                ComponentToolKit.setButtonDisable(AlreadyOffShelf_UnSell_Button,false);
                ComponentToolKit.setButtonDisable(AlreadyOffShelf_AlreadySell_Button,false);
            }else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待下架)   searchProductOnlineWaitingOffShelf(BidStore.蝦皮);
            else if(BidStoreStatus == Product_Enum.BidStoreStatus.線上待修改)   searchProductOnlineWaitingUpdate(BidStore.蝦皮);*/
        }
    }
    private void searchProductOnShelf(BidStore BidStore){
        ObservableList<ManageProductOnShelf_Bean> AlreadyOnShelfList = Product_Model.getProductAlreadyOnShelf(BidStore, SearchProductTextField.getText());
        if(AlreadyOnShelfList.size() == 0){
            ComponentToolKit.setCheckBoxSelect(AlreadyOnShelf_SelectAll_CheckBox,false);
            DialogUI.MessageDialog("※ 查無商品");
        }else{
            ComponentToolKit.setCheckBoxSelect(AlreadyOnShelf_SelectAll_CheckBox,true);
            TableView.setItems(AlreadyOnShelfList);
        }
    }
    private void searchProductOnlineWaitingOffShelf(BidStore BidStore){
        ObservableList<ManageProductOnShelf_Bean> OnlineWaitingOffShelfList = Product_Model.getProductOnlineWaitingOffShelf(BidStore);
        if(OnlineWaitingOffShelfList.size() == 0){
            ComponentToolKit.setCheckBoxSelect(OnlineWaitingOffShelf_SelectAll_CheckBox,false);
            DialogUI.MessageDialog("※ 查無商品");
        }else{
            ComponentToolKit.setCheckBoxSelect(OnlineWaitingOffShelf_SelectAll_CheckBox,true);
            TableView.setItems(OnlineWaitingOffShelfList);
        }
    }
    private void searchProductOnlineWaitingUpdate(BidStore BidStore){
        ObservableList<ManageProductOnShelf_Bean> OnlineWaitingUpdateList = Product_Model.getProductOnlineWaitingUpdate(BidStore);
        if(OnlineWaitingUpdateList.size() == 0){
            ComponentToolKit.setCheckBoxSelect(OnlineWaitingUpdate_SelectAll_CheckBox,false);
            DialogUI.MessageDialog("※ 查無商品");
        }else{
            ComponentToolKit.setCheckBoxSelect(OnlineWaitingUpdate_SelectAll_CheckBox,true);
            TableView.setItems(OnlineWaitingUpdateList);
        }
    }
    private void setTableViewItemsWebSelectStatus(ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList, boolean selectStatus){
        for(ManageProductOnShelf_Bean ManageProductOnShelf_Bean : WaitingOnShelfList)
            ManageProductOnShelf_Bean.setWebCheckBoxSelect(selectStatus);
    }
    private void setTableViewItemsYahooSelectStatus(ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList, boolean selectStatus){
        for(ManageProductOnShelf_Bean ManageProductOnShelf_Bean : WaitingOnShelfList)
            ManageProductOnShelf_Bean.setYahooCheckBoxSelect(selectStatus);
    }
    private void setTableViewItemsRutenSelectStatus(ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList, boolean selectStatus){
        for(ManageProductOnShelf_Bean ManageProductOnShelf_Bean : WaitingOnShelfList)
            ManageProductOnShelf_Bean.setRutenCheckBoxSelect(selectStatus);
    }
    private void setTableViewItemsShopeeSelectStatus(ObservableList<ManageProductOnShelf_Bean> WaitingOnShelfList, boolean selectStatus){
        for(ManageProductOnShelf_Bean ManageProductOnShelf_Bean : WaitingOnShelfList)
            ManageProductOnShelf_Bean.setShopeeCheckBoxSelect(selectStatus);
    }
    private void setColumnCellValueAndCheckBox(BidStore BidStore, TableColumn<ManageProductOnShelf_Bean, CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn, ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(BidStore, Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<ManageProductOnShelf_Bean, CheckBox> {
        BidStore BidStore;
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(BidStore BidStore, String Alignment, String FontSize){
            this.BidStore = BidStore;
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBox, boolean empty) {
            super.updateItem(CheckBox, empty);
            if(!empty){
                CheckBox webCheckBox = getTableView().getItems().get(getIndex()).getWebCheckBox();
                setCheckBoxOnAction(webCheckBox, Product_Enum.BidStore.網站, WaitingOnShelf_WebSelectAll_CheckBox);
                CheckBox yahooCheckBox = getTableView().getItems().get(getIndex()).getYahooCheckBox();
                setCheckBoxOnAction(yahooCheckBox, Product_Enum.BidStore.奇摩, WaitingOnShelf_YahooSelectAll_CheckBox);
                CheckBox rutenCheckBox = getTableView().getItems().get(getIndex()).getRutenCheckBox();
                setCheckBoxOnAction(rutenCheckBox, Product_Enum.BidStore.露天, WaitingOnShelf_RutenSelectAll_CheckBox);
                CheckBox shopeeCheckBox = getTableView().getItems().get(getIndex()).getShopeeCheckBox();
                setCheckBoxOnAction(shopeeCheckBox, Product_Enum.BidStore.蝦皮, WaitingOnShelf_ShopeeSelectAll_CheckBox);
                if(BidStore == Product_Enum.BidStore.網站){
                    setGraphic(webCheckBox);
                }else if(BidStore == Product_Enum.BidStore.奇摩){
                    setGraphic(yahooCheckBox);
                }else if(BidStore == Product_Enum.BidStore.露天){
                    setGraphic(rutenCheckBox);
                }else if(BidStore == Product_Enum.BidStore.蝦皮){
                    setGraphic(shopeeCheckBox);
                }
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }else   setGraphic(null);
        }
    }
    private void setCheckBoxOnAction(CheckBox bidStoreCheckBox, BidStore BidStore, CheckBox selectAllCheckBox){
        bidStoreCheckBox.setOnAction(ActionEvent -> {
            boolean isAllProductSelect = true;
            ObservableList<ManageProductOnShelf_Bean> ProductList = ComponentToolKit.getManageProductOnShelfTableViewItemList(TableView);
            for(ManageProductOnShelf_Bean ManageProductOnShelf_Bean : ProductList){
                boolean isCheckBoxSelect = false;
                if(BidStore == Product_Enum.BidStore.網站)    isCheckBoxSelect = ManageProductOnShelf_Bean.getWebCheckBox().isSelected();
                else if(BidStore == Product_Enum.BidStore.奇摩)   isCheckBoxSelect = ManageProductOnShelf_Bean.getYahooCheckBox().isSelected();
                else if(BidStore == Product_Enum.BidStore.露天)   isCheckBoxSelect = ManageProductOnShelf_Bean.getRutenCheckBox().isSelected();
                else if(BidStore == Product_Enum.BidStore.蝦皮)   isCheckBoxSelect = ManageProductOnShelf_Bean.getShopeeCheckBox().isSelected();
                if(!isCheckBoxSelect) {
                    isAllProductSelect = false;
                    break;
                }
            }
            selectAllCheckBox.setSelected(isAllProductSelect);
        });
    }
}
