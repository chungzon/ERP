package ERP.Controller.Order.EstablishOrder;

import ERP.Bean.LineAPI_Bean;
import ERP.Bean.ManagePayableReceivable.BankInfo_Bean;
import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.ManagePayableReceivable.PayableReceivable_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.*;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.Bean.ToolKit.Installment.Installment_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowExportQuotationRecord.ExportQuotationRecord_Bean;
import ERP.Controller.Order.SearchOrder.SearchOrder_Controller;
import ERP.Controller.Order.SearchOrderProgress.SearchOrderProgress_Controller;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.CustomerSaleModel;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderTaxStatus;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderExist;
import ERP.Enum.Order.Order_Enum.OffsetOrderStatus;
import ERP.Enum.Order.Order_Enum.EstablishSource;
import ERP.Enum.Order.Order_Enum.ObjectSearchColumn;
import ERP.Enum.Order.Order_Enum.ShowObjectSource;
import ERP.Enum.Order.Order_Enum.ReviewStatus;
import ERP.Enum.Order.Order_Enum.ReviewObject;
import ERP.Enum.Order.Order_Enum.ExportQuotationVendor;
import ERP.Enum.Order.Order_Enum.ExportQuotationFormat;
import ERP.Enum.ToolKit.ToolKit_Enum.SnapshotPictureLocation;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_SearchFunction;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_PrintFunction;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_ExportPurchaseFunction;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.UploadPictureFunction;
import ERP.Enum.ToolKit.ToolKit_Enum.Authority;
import ERP.Enum.ToolKit.ToolKit_Enum.AuthorizedLocation;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PayableReceivableStatus;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.ManageProductInfoTableColumn;
import ERP.ERPApplication;
import ERP.Model.ManageCustomerInfo.ManageCustomerInfo_Model;
import ERP.Model.ManageManufacturerInfo.ManageManufacturerInfo_Model;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.ExportPurchaseQuotationDocument.ExportPurchaseQuotation_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.*;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** [Controller] Establish order */
public class EstablishOrder_Controller {
    @FXML HBox PurchaserType_HBox, ShipmentType_HBox;
    @FXML HBox TestDB_HBox, ChangeOrderType_HBox;
    @FXML SplitMenuButton OrderReviewStatus_SplitMenuButton, ExportQuotation_SplitMenuButton;
    @FXML RadioButton NoneOrderReviewStatus_RadioButton, WaitingOrderReviewStatus_RadioButton, WaitingOrderModifyStatus_RadioButton, OrderReviewFinished_RadioButton;

    @FXML SplitMenuButton SearchFunction_EstablishPurchase_SplitMenuButton, SearchFunction_ModifyPurchase_SplitMenuButton,
            SearchFunction_EstablishShipment_SplitMenuButton, SearchFunction_ModifyShipment_SplitMenuButton,
            SearchFunction_EstablishReturn_SplitMenuButton, SearchFunction_ModifyReturn_SplitMenuButton;

    @FXML SplitMenuButton UploadOrderPicture_SplitMenuButton, PrintFunction_ModifyPurchase_SplitMenuButton, PrintFunction_ModifyShipment_SplitMenuButton, PrintFunction_ModifyReturn_SplitMenuButton
            ,ExportPurchaseFunction_SplitMenuButton;
    @FXML RadioButton PastePicture_RadioButton, UploadLocation_RadioButton;

    private SplitMenuButton SearchFunction_SplitMenuButton, PrintFunction_SplitMenuButton;

    @FXML GridPane ExportQuotationGridPane, OrderInfoGridPane, PurchaseGridPane, ShipmentGridPane, ReturnGridPane, modifyPurchaseGridPane, modifyShipmentGridPane, shipmentOperationGridPane, purchaseOperationGridPane, returnOperationGridPane;
    @FXML HBox OrderReferenceButton_HBox, OrderPictureButton_HBox, Pricing_HBox, Profit_HBox, ProductGroupDifferencePrice_HBox, ProjectInfoHBox, DefaultProductConnectionAndSearchOrder_HBox;

    @FXML HBox UpdateDateTime_HBox;
    @FXML HBox establishPurchaseHBox, modifyPurchaseHBox, establishShipmentHBox, modifyShipmentHBox, establishReturnHBox, modifyReturnHBox;
    @FXML Button transferQuotationButton_PurchaseUI, transferQuotationButton_ShipmentUI;
    @FXML HBox transferQuotationHBox_ShipmentUI,transferQuotationHBox_PurchaseUI;
    @FXML HBox Offset_HBox, InvoiceInfo_HBox;

    @FXML ProgressBar SaveOrderPicture_ProgressBar;
    @FXML HBox ModifyShipmentProductGroup_HBox;
    @FXML Button AddOrderReference_Button, LoadOrderReference_Button, ShowOrderReference_Button, SnapshotOrderPicture_Button, ShowOrderPicture_Button, InvoicePaste_Button, CompletionReport_Button, ExportPayable_Button, establishPayable_button, establishReceivable_button, DeleteEstablishPurchaseOrderProduct_Button, DeleteModifyPurchaseOrderProduct_Button, DeleteEstablishShipmentOrderProduct_Button, DeleteModifyShipmentOrderProduct_Button, ModifyShipmentProductGroup_Button, TransferWaitingPurchaseOrder_Button, TransferPurchaseOrder_Button, TransferWaitingShipmentOrder_Button, TransferShipmentOrder_Button, cancelPurchaseOrderProduct_Button,cancelShipmentOrderProduct_Button;
    @FXML MenuButton establishPurchaseCheckout_menuButton, establishShipmentCheckout_menuButton;
    @FXML MenuItem purchaseInstallment_menuItem, shipmentInstallment_menuItem, establishPurchasePay_menuItem, establishShipmentReceive_menuItem;
    @FXML RadioButton OrderType_Purchase_RadioButton, OrderType_PurchaseReturn_RadioButton, OrderType_PurchaseSubBill_RadioButton, OrderType_Shipment_RadioButton, OrderType_ShipmentReturn_RadioButton, OrderType_ShipmentSubBill_RadioButton;
    @FXML RadioButton TwoFormat_RadioButton, ThreeFormat_RadioButton;
    @FXML ToggleGroup ExportContentToggleGroup, ExportFormatToggleGroup;
    @FXML ComboBox<IAECrawlerAccount_Bean> ExportQuotationVendor_ComboBox;

    @FXML Button ChangeQuotation_Button, ChangeWaitingOrder_Button, ChangeAlreadyOrder_Button;
    @FXML Label ObjectIDLabelText, ObjectNameLabelText;

    @FXML TextField ObjectIDText, ObjectNameText;
    @FXML TextField OrderNumberText;
    @FXML public TextField ISBNText, InternationalCodeText, FirmCodeText, ProductNameText, UnitText, QuantityText, BatchPriceText, SinglePriceText, PricingText, PriceAmountText, TotalPriceNoneTaxText, TaxText, DiscountText, TotalPriceIncludeTaxText, RemarkText, ProfitText, ProductGroupDifferentPriceText;
    @FXML Spinner<Double> PricePercentageSpinner;
    @FXML TextField ProjectCodeText, ProjectNameText, ProjectQuantityText, NumberOfItemsText, ProjectUnitText, ProjectPriceAmountText,ProjectTotalPriceNoneTaxText, ProjectTaxText, ProjectTotalPriceIncludeTaxText, ProjectDifferentPriceText;
    @FXML Label singlePrice_Label,UpdateDateTime_Label;
    @FXML TextField transferQuotation_ObjectCodeText_PurchaseUI,transferQuotation_ObjectNameText_PurchaseUI,transferQuotation_ObjectCodeText_ShipmentUI,transferQuotation_ObjectNameText_ShipmentUI;
    @FXML TextField InvoiceDateText,InvoiceNumberText;
    @FXML DatePicker OrderDate_DatePicker, WaitingPurchaseOrderDate_DatePicker, PurchaseOrderDate_DatePicker, WaitingShipmentOrderDate_DatePicker, ShipmentOrderDate_DatePicker;
    @FXML TextField WaitingPurchaseOrderNumberText, PurchaseOrderNumberText, WaitingShipmentOrderNumberText, ShipmentOrderNumberText;
    @FXML CheckBox ShowPricing_CheckBox, ShowProfit_CheckBox, OrderBorrowed_CheckBox, OffsetOrder_CheckBox;
    @FXML Button ProductGenerator_Button;

    @FXML Button OrderRemarkButton, CashierRemarkButton;
    @FXML Button PurchaserRecipientInfoButton_Shipment;
    @FXML Button modifyPurchaseOrderButton,modifyShipmentOrderButton,modifyReturnOrderButton;

    @FXML TableColumn<OrderProduct_Bean, Integer> ItemNumberColumn_Purchase,ItemNumberColumn_Shipment,ItemNumberColumn_ReturnOrder;
    @FXML TableColumn<OrderProduct_Bean, String> ProductCodeColumn_Purchase, ISBNColumn_Purchase, ProductNameColumn_Purchase, QuantityColumn_Purchase, UnitColumn_Purchase, InStockColumn_Purchase, SafetyStockColumn_Purchase, SupplyStatusColumn_Purchase, RemarkColumn_Purchase;
    @FXML TableColumn<OrderProduct_Bean, String> ProductCodeColumn_Shipment, ISBNColumn_Shipment, ProductNameColumn_Shipment, QuantityColumn_Shipment, UnitColumn_Shipment, InStockColumn_Shipment, SafetyStockColumn_Shipment, SupplyStatusColumn_Shipment, RemarkColumn_Shipment;
    @FXML TableColumn<OrderProduct_Bean, String> ProductCodeColumn_ReturnOrder,ISBNColumn_ReturnOrder, ProductNameColumn_ReturnOrder, QuantityColumn_ReturnOrder, UnitColumn_ReturnOrder, InStockColumn_ReturnOrder, SafetyStockColumn_ReturnOrder, SupplyStatusColumn_ReturnOrder, RemarkColumn_ReturnOrder;
    @FXML TableColumn<OrderProduct_Bean, Double> BatchPriceColumn_Purchase, SinglePriceColumn_Purchase, PricingColumn_Purchase, SinglePriceColumn_Shipment, BatchPriceColumn_Shipment, PricingColumn_Shipment, BatchPriceColumn_ReturnOrder, SinglePriceColumn_ReturnOrder, PricingColumn_ReturnOrder;
    @FXML TableColumn<OrderProduct_Bean, Integer> PriceAmountColumn_Purchase,PriceAmountColumn_Shipment,PriceAmountColumn_ReturnOrder;
    @FXML TableView<OrderProduct_Bean> PurchaseTableView, ShipmentTableView, ReturnTableView;
    @FXML ScrollPane ShipmentProductGroupScrollPane;
    @FXML Accordion ShipmentProductGroupAccordion;

    @FXML Label orderTooltip_Label, exportQuotationTooltip_Label;
    @FXML Tooltip orderTooltip, changeQuotationTooltip, changeWaitingOrderTooltip, changeAlreadyOrderTooltip, orderRemarkTooltip, cashierRemarkTooltip, exportQuotationTooltip;

    private final KeyCombination saveMoveKeyCombine = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    private final KeyCombination previousMoveKeyCombine = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);
    private final KeyCombination nextMoveKeyCombine = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);
    private final KeyCombination ProductConnectionKeyCombine = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.CallJAR CallJAR;
    private ERP.ToolKit.CallConfig CallConfig;
    private ERP.ToolKit.Tooltip Tooltip;

    private OrderStatus orderStatus;
    private OrderObject OrderObject;
    public ObjectInfo_Bean ObjectInfo_Bean;
    public ObjectInfo_Bean transferQuotation_ObjectInfoBean;
    private OrderSource OrderSource;
    private OrderExist OrderExist;
    public TableView<OrderProduct_Bean> MainTableView;
    private Order_Bean Order_Bean;
    public OrderTaxStatus OrderTaxStatus, ProjectTaxStatus;
    private Stage Stage;
    private Product_Model Product_Model;
    private ManageCustomerInfo_Model manageCustomerInfo_model;
    private ManageManufacturerInfo_Model manageManufacturerInfo_model;
    private Order_Model Order_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private SystemSetting_Model SystemSetting_Model;
    private SearchOrder_Controller SearchOrder_Controller;
    private SearchOrderProgress_Controller SearchOrderProgress_Controller;

    private boolean coverOrderReference = false;
    private ArrayList<String> differentOrderInfoList;
    private HashMap<Order_SearchFunction, RadioButton> searchFunctionRadioButtonMap = new HashMap<>();
    private HashMap<Order_PrintFunction, RadioButton> printFunctionRadioButtonMap = new HashMap<>();
    private HashMap<Order_ExportPurchaseFunction, RadioButton> exportPurchaseFunctionRadioButtonMap = new HashMap<>();

    private TransferQuotation_ToggleSwitch transferQuotation_toggleSwitch = null;
    private OffsetStatus_ToggleSwitch offsetStatus_toggleSwitch = null;
    public ProductGroup_ToggleSwitch shipmentProductGroup_toggleSwitch = null;
    private DefaultProductConnection_ToggleSwitch defaultProductConnection_toggleSwitch = null;

    public JSONObject SystemDefaultConfigJson;
    public EstablishOrder_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.CallJAR = ToolKit.CallJAR;
        this.CallConfig = ToolKit.CallConfig;
        this.Tooltip = ToolKit.Tooltip;
        this.Product_Model = ToolKit.ModelToolKit.getProductModel();
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
        this.manageManufacturerInfo_model = ToolKit.ModelToolKit.getManageManufacturerInfoModel();
        this.manageCustomerInfo_model = ToolKit.ModelToolKit.getManageCustomerInfoModel();
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    public void setOrderSource(OrderSource OrderSource){    this.OrderSource = OrderSource; }
    public void setOrderExist(OrderExist OrderExist){   this.OrderExist = OrderExist;   }
    private boolean isOrderExist(){ return OrderExist.value();  }
    public boolean isOrderNumberLengthSetting(){
        String orderNumberLength = SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.貨單號長度);
        return orderNumberLength != null && !orderNumberLength.equals("");
    }
    public void initialComponentData(EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean){
        setOrderSource(establishOrder_NewStage_Bean.getOrderSource());
        this.orderStatus = establishOrder_NewStage_Bean.getOrderStatus();
        this.OrderObject = OrderSource.getOrderObject();
        this.OrderExist = establishOrder_NewStage_Bean.getOrderExist();
        this.Order_Bean = establishOrder_NewStage_Bean.getOrder_Bean();
        this.differentOrderInfoList = establishOrder_NewStage_Bean.getDifferentOrderInfoList();
        this.SearchOrder_Controller = establishOrder_NewStage_Bean.getSearchOrder_Controller();
        this.SearchOrderProgress_Controller = establishOrder_NewStage_Bean.getSearchOrderProgress_Controller();
    }
    public void initialComponent(){
        if(Order_Bean != null)
            this.ObjectInfo_Bean = Order_Model.getObjectFromSearchColumn(OrderObject,
                    OrderObject == Order_Enum.OrderObject.廠商 ? ObjectSearchColumn.廠商編號 : ObjectSearchColumn.客戶編號,
                    Order_Bean.getObjectID()).get(0);

        setKeyWordTextLimiter();
        setTextFieldLimitDigital();
        setPriceSpinnerAndTextFill();
        setBatchPriceTextFill();
        setProductGroupDifferentPriceTextFill();
        setProjectDifferentPriceTextFill();
        initialPurchaseTableView();
        initialShipmentTableView();
        initialReturnTableView();
        setOrderUI();
        if(OrderObject == Order_Enum.OrderObject.客戶 && OrderSource != Order_Enum.OrderSource.出貨退貨單) {
            setExportQuotationVendorItems();
            setExportQuotationItems();
        }
        ComponentToolKit.setToolTips(orderTooltip_Label, orderTooltip, Tooltip.establishOrder());
        ComponentToolKit.setToolTips(exportQuotationTooltip_Label, exportQuotationTooltip, Tooltip.exportQuotation());
        ComponentToolKit.setToolTips(establishShipmentCheckout_menuButton, new Tooltip(), Tooltip.establishPayReceiveFromOrder());
        ComponentToolKit.setToolTips(establishPurchaseCheckout_menuButton, new Tooltip(), Tooltip.establishPayReceiveFromOrder());
        ComponentToolKit.setToolTips(establishPayable_button, new Tooltip(), Tooltip.establishPayReceiveFromSubBill());
        ComponentToolKit.setToolTips(establishReceivable_button, new Tooltip(), Tooltip.establishPayReceiveFromSubBill());
    }
    private void showCombinedOrderComponent(){
        ComponentToolKit.setHBoxDisable(OrderReferenceButton_HBox,true);
        setReviewStatusSplitMenuButton__Disable(true);
        ComponentToolKit.setHBoxDisable(OrderPictureButton_HBox,true);
        ComponentToolKit.setGridPaneDisable(ExportQuotationGridPane,true);
        ComponentToolKit.setHBoxDisable(modifyPurchaseHBox,true);
        ComponentToolKit.setGridPaneDisable(modifyPurchaseGridPane,true);
        ComponentToolKit.setHBoxDisable(Offset_HBox,true);
        ComponentToolKit.setHBoxDisable(InvoiceInfo_HBox,true);
        ComponentToolKit.setHBoxDisable(modifyShipmentHBox,true);
        ComponentToolKit.setGridPaneDisable(modifyShipmentGridPane,true);
        ComponentToolKit.setGridPaneDisable(returnOperationGridPane,true);
    }
    private void showCompareOrderComponent(){
        ComponentToolKit.setHBoxDisable(OrderReferenceButton_HBox,true);
        setReviewStatusSplitMenuButton__Disable(true);
        ComponentToolKit.setHBoxDisable(OrderPictureButton_HBox,true);
        ComponentToolKit.setGridPaneDisable(ExportQuotationGridPane,true);
        ComponentToolKit.setGridPaneDisable(purchaseOperationGridPane,true);
        ComponentToolKit.setGridPaneDisable(shipmentOperationGridPane,true);
        ComponentToolKit.setGridPaneDisable(returnOperationGridPane,true);

        setDifferentOrderInfoList(differentOrderInfoList);
    }
    private void setDifferentOrderInfoList(ArrayList<String> differentOrderInfoList){
        String textFieldRemarkStyle = "-fx-font-size:16px;-fx-background-color:" + ToolKit.getPink_BackgroundColor();
        String isBorrowedCheckBoxRemarkStyle = "-fx-font-size:18px;-fx-background-color:" + ToolKit.getPink_BackgroundColor();
        String isOffsetCheckBoxRemarkStyle = "-fx-font-size:28px;-fx-font-weight: bold;-fx-background-color:" + ToolKit.getPink_BackgroundColor();
        String buttonRemarkStyle = "-fx-background-color:" + ToolKit.getPink_BackgroundColor();
        this.differentOrderInfoList = differentOrderInfoList;
        ComponentToolKit.setHBoxVisible(UpdateDateTime_HBox,true);
        UpdateDateTime_Label.setText(Order_Bean.getUpdateDateTime());

        for (String differentColumn : differentOrderInfoList) {
            if (differentColumn.equals("ObjectID")) ComponentToolKit.setTextFieldStyle(ObjectIDText, textFieldRemarkStyle);
            if (differentColumn.equals("ObjectName")) ComponentToolKit.setTextFieldStyle(ObjectNameText, textFieldRemarkStyle);
            if (differentColumn.equals("NumberOfItems")) ComponentToolKit.setTextFieldStyle(NumberOfItemsText, textFieldRemarkStyle);
            if (differentColumn.equals("TotalPriceNoneTax")) ComponentToolKit.setTextFieldStyle(TotalPriceNoneTaxText, textFieldRemarkStyle);
            if (differentColumn.equals("Tax")) ComponentToolKit.setTextFieldStyle(TaxText, textFieldRemarkStyle);
            if (differentColumn.equals("Discount")) ComponentToolKit.setTextFieldStyle(DiscountText, textFieldRemarkStyle);
            if (differentColumn.equals("TotalPriceIncludeTax")) ComponentToolKit.setTextFieldStyle(TotalPriceIncludeTaxText, textFieldRemarkStyle);
            if (differentColumn.equals("InvoiceDate")) ComponentToolKit.setTextFieldStyle(InvoiceDateText, textFieldRemarkStyle);
            if (differentColumn.equals("InvoiceNumber")) ComponentToolKit.setTextFieldStyle(InvoiceNumberText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectCode")) ComponentToolKit.setTextFieldStyle(ProjectCodeText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectName")) ComponentToolKit.setTextFieldStyle(ProjectNameText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectQuantity")) ComponentToolKit.setTextFieldStyle(ProjectQuantityText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectUnit")) ComponentToolKit.setTextFieldStyle(ProjectUnitText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectPriceAmount")) ComponentToolKit.setTextFieldStyle(ProjectPriceAmountText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectTotalPriceNoneTax")) ComponentToolKit.setTextFieldStyle(ProjectTotalPriceNoneTaxText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectTax")) ComponentToolKit.setTextFieldStyle(ProjectTaxText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectTotalPriceIncludeTax"))  ComponentToolKit.setTextFieldStyle(ProjectTotalPriceIncludeTaxText, textFieldRemarkStyle);
            if (differentColumn.equals("ProjectDifferentPrice"))    ComponentToolKit.setTextFieldStyle(ProjectDifferentPriceText, textFieldRemarkStyle);

            if (differentColumn.equals("isBorrowed")) ComponentToolKit.setCheckBoxStyle(OrderBorrowed_CheckBox, isBorrowedCheckBoxRemarkStyle);
            if (differentColumn.equals("isOffsetOrder")) ComponentToolKit.setCheckBoxStyle(OffsetOrder_CheckBox, isOffsetCheckBoxRemarkStyle);
            if (differentColumn.equals("isExistOrderReference")) ComponentToolKit.setButtonStyle(LoadOrderReference_Button, buttonRemarkStyle);

            if (differentColumn.equals("WaitingOrderNumber")) {
                ComponentToolKit.setTextFieldStyle(WaitingPurchaseOrderNumberText, textFieldRemarkStyle);
                ComponentToolKit.setTextFieldStyle(WaitingShipmentOrderNumberText, textFieldRemarkStyle);
            }
            if (differentColumn.equals("AlreadyOrderNumber")) {
                ComponentToolKit.setTextFieldStyle(PurchaseOrderNumberText, textFieldRemarkStyle);
                ComponentToolKit.setTextFieldStyle(ShipmentOrderNumberText, textFieldRemarkStyle);
            }

            if(differentColumn.equals("PurchaserName") || differentColumn.equals("PurchaserTelephone") || differentColumn.equals("PurchaserCellphone") || differentColumn.equals("PurchaserAddress") ||
                    differentColumn.equals("RecipientName") || differentColumn.equals("RecipientTelephone") || differentColumn.equals("RecipientCellphone") || differentColumn.equals("RecipientAddress"))
            ComponentToolKit.setButtonStyle(PurchaserRecipientInfoButton_Shipment,buttonRemarkStyle);

            if (differentColumn.equals("OrderRemark"))  ComponentToolKit.setButtonStyle(OrderRemarkButton, buttonRemarkStyle);
            if(differentColumn.equals("CashierRemark"))   ComponentToolKit.setButtonStyle(CashierRemarkButton, buttonRemarkStyle);
        }
    }
    /** Initial component */
    public void setOrderData(boolean initialOrderInfoBean) throws Exception{
        if(!isOrderExist()){
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
                initialSubBillComponent();
            else{
                initialComponentExceptSubBill(initialOrderInfoBean);
                if(Order_Bean == null) Order_Bean = new Order_Bean();
                else    setOrderInfo(Order_Bean);
            }
        }else{
            setOrderInfo(Order_Bean);
        }
    }
    private void setDefaultSearchFunction(){
        if(SearchFunction_SplitMenuButton != null)
            SearchFunction_SplitMenuButton.getItems().clear();
        if(OrderObject == Order_Enum.OrderObject.廠商) {
            if (OrderSource == Order_Enum.OrderSource.進貨退貨單)
                this.SearchFunction_SplitMenuButton = !isOrderExist() ? SearchFunction_EstablishReturn_SplitMenuButton : SearchFunction_ModifyReturn_SplitMenuButton;
            else
                this.SearchFunction_SplitMenuButton = !isOrderExist() ? SearchFunction_EstablishPurchase_SplitMenuButton : SearchFunction_ModifyPurchase_SplitMenuButton;
        }else if(OrderObject == Order_Enum.OrderObject.客戶) {
            if (OrderSource == Order_Enum.OrderSource.出貨退貨單)
                this.SearchFunction_SplitMenuButton = !isOrderExist() ? SearchFunction_EstablishReturn_SplitMenuButton : SearchFunction_ModifyReturn_SplitMenuButton;
            else
                this.SearchFunction_SplitMenuButton = !isOrderExist() ? SearchFunction_EstablishShipment_SplitMenuButton : SearchFunction_ModifyShipment_SplitMenuButton;
        }
        Integer defaultConfig = SystemDefaultConfigJson.getInteger(SystemDefaultConfig_Enum.Order_SearchFunction.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = Order_SearchFunction.TransactionDetail.ordinal();
        ToggleGroup ToggleGroup = new ToggleGroup();
        for(SystemDefaultConfig_Enum.Order_SearchFunction Order_SearchFunction : SystemDefaultConfig_Enum.Order_SearchFunction.values()){
            RadioButton RadioButton = ComponentToolKit.setRadioButton(Order_SearchFunction.getDescribe(), -1,-1,16);
            RadioButton.setToggleGroup(ToggleGroup);
            if(SystemDefaultConfig_Enum.Order_SearchFunction.values()[defaultConfig] == Order_SearchFunction){
                ComponentToolKit.setRadioButtonSelect(RadioButton,true);
                SearchFunction_SplitMenuButton.setText(Order_SearchFunction.getDescribe());
            }else {
                ComponentToolKit.setRadioButtonSelect(RadioButton, false);
            }
            searchFunctionRadioButtonMap.put(Order_SearchFunction,RadioButton);
            RadioButton.setOnAction(ActionEvent -> {
                if(Order_SearchFunction == SystemDefaultConfig_Enum.Order_SearchFunction.TransactionDetail){
                    openTransactionDetail(Order_SearchFunction);
                }else if(Order_SearchFunction == SystemDefaultConfig_Enum.Order_SearchFunction.ProductWaitingConfirm){
                    openProductWaitingConfirm(Order_SearchFunction);
                }
            });

            CustomMenuItem CustomMenuItem = new CustomMenuItem(RadioButton);
            CustomMenuItem.setHideOnClick(true);
            SearchFunction_SplitMenuButton.getItems().add(CustomMenuItem);
        }
    }
    private void setDefaultUploadPictureFunction(){
        Integer defaultConfig = SystemDefaultConfigJson.getInteger(SystemDefaultConfig_Enum.UploadPictureFunction.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = UploadPictureFunction.PastePicture.ordinal();
        if(SystemDefaultConfig_Enum.UploadPictureFunction.values()[defaultConfig] == SystemDefaultConfig_Enum.UploadPictureFunction.PastePicture){
            PastePicture_RadioButton.setSelected(true);
        }else if(SystemDefaultConfig_Enum.UploadPictureFunction.values()[defaultConfig] == SystemDefaultConfig_Enum.UploadPictureFunction.UploadLocation) {
            UploadLocation_RadioButton.setSelected(true);
        }
    }
    private void setDefaultPrintFunction(){
        if(PrintFunction_SplitMenuButton != null)
            PrintFunction_SplitMenuButton.getItems().clear();
        if(OrderObject == Order_Enum.OrderObject.廠商 && isOrderExist()) {
            if (OrderSource == Order_Enum.OrderSource.進貨退貨單)
                this.PrintFunction_SplitMenuButton = PrintFunction_ModifyReturn_SplitMenuButton;
            else
                this.PrintFunction_SplitMenuButton = PrintFunction_ModifyPurchase_SplitMenuButton;
        }else if(OrderObject == Order_Enum.OrderObject.客戶 && isOrderExist()) {
            if (OrderSource == Order_Enum.OrderSource.出貨退貨單)
                this.PrintFunction_SplitMenuButton = PrintFunction_ModifyReturn_SplitMenuButton;
            else
                this.PrintFunction_SplitMenuButton = PrintFunction_ModifyShipment_SplitMenuButton;
        }
        Integer defaultConfig = SystemDefaultConfigJson.getInteger(Order_PrintFunction.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = Order_PrintFunction.PrintOrder.ordinal();
        ToggleGroup ToggleGroup = new ToggleGroup();
        for(SystemDefaultConfig_Enum.Order_PrintFunction Order_PrintFunction : SystemDefaultConfig_Enum.Order_PrintFunction.values()){
            RadioButton RadioButton = ComponentToolKit.setRadioButton(Order_PrintFunction.getDescribe(), -1,-1,16);
            RadioButton.setToggleGroup(ToggleGroup);
            if(SystemDefaultConfig_Enum.Order_PrintFunction.values()[defaultConfig] == Order_PrintFunction){
                ComponentToolKit.setRadioButtonSelect(RadioButton,true);
                PrintFunction_SplitMenuButton.setText(Order_PrintFunction.getDescribe());
            }else {
                ComponentToolKit.setRadioButtonSelect(RadioButton, false);
            }
            printFunctionRadioButtonMap.put(Order_PrintFunction,RadioButton);
            RadioButton.setOnAction(ActionEvent -> {
                if(Order_PrintFunction == SystemDefaultConfig_Enum.Order_PrintFunction.PrintOrder){
                    openPrintOrder(Order_PrintFunction);
                }else if(Order_PrintFunction == SystemDefaultConfig_Enum.Order_PrintFunction.PrintLabel){
                    openPrintLabel(Order_PrintFunction);
                }
            });

            CustomMenuItem CustomMenuItem = new CustomMenuItem(RadioButton);
            CustomMenuItem.setHideOnClick(true);
            PrintFunction_SplitMenuButton.getItems().add(CustomMenuItem);
        }
    }
    private void setDefaultExportPurchaseFunction(){
        if(ExportPurchaseFunction_SplitMenuButton != null)
            ExportPurchaseFunction_SplitMenuButton.getItems().clear();
        Integer defaultConfig = SystemDefaultConfigJson.getInteger(SystemDefaultConfig_Enum.Order_ExportPurchaseFunction.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = Order_ExportPurchaseFunction.PurchaseOrder.ordinal();
        ToggleGroup ToggleGroup = new ToggleGroup();
        for(SystemDefaultConfig_Enum.Order_ExportPurchaseFunction exportPurchaseFunction : SystemDefaultConfig_Enum.Order_ExportPurchaseFunction.values()){
            RadioButton RadioButton = ComponentToolKit.setRadioButton(exportPurchaseFunction.getDescribe(), -1,-1,16);
            RadioButton.setToggleGroup(ToggleGroup);
            if(SystemDefaultConfig_Enum.Order_ExportPurchaseFunction.values()[defaultConfig] == exportPurchaseFunction){
                ComponentToolKit.setRadioButtonSelect(RadioButton,true);
                ExportPurchaseFunction_SplitMenuButton.setText("匯出" + exportPurchaseFunction.getDescribe());
            }else {
                ComponentToolKit.setRadioButtonSelect(RadioButton, false);
            }
            exportPurchaseFunctionRadioButtonMap.put(exportPurchaseFunction,RadioButton);
            RadioButton.setOnAction(ActionEvent -> {
                SystemDefaultConfigJson.put(SystemDefaultConfig_Enum.Order_ExportPurchaseFunction.class.getSimpleName(), exportPurchaseFunction.ordinal());
                if(CallConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.Order_ExportPurchaseFunction, SystemDefaultConfigJson)){
                    ExportPurchaseFunction_SplitMenuButton.setText("匯出" + exportPurchaseFunction.getDescribe());
                    ExportPurchaseQuotation(exportPurchaseFunction);
                }else{
                    DialogUI.AlarmDialog("※ 【預設值】更新失敗");
                }
            });
            CustomMenuItem CustomMenuItem = new CustomMenuItem(RadioButton);
            CustomMenuItem.setHideOnClick(true);
            ExportPurchaseFunction_SplitMenuButton.getItems().add(CustomMenuItem);
        }
    }
    private void setExportQuotationVendorItems(){
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(false);
        ComponentToolKit.setIAECrawlerAccountComboBoxObj_ExportQuotation(ExportQuotationVendor_ComboBox);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            if(IAECrawlerAccount_Bean.isTheSameAsNoneInvoice()){
                IAECrawlerAccount_Bean noSealIAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
                noSealIAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName(ExportQuotationVendor.noneInvoice.getDescribe());
                noSealIAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName(ExportQuotationVendor.noneInvoice.getDescribe());
                noSealIAECrawlerAccount_Bean.setExportQuotation_defaultSelect(true);
                noSealIAECrawlerAccount_Bean.setExportQuotation(true);
                ExportQuotationVendor_ComboBox.getItems().add(0,noSealIAECrawlerAccount_Bean);
            }
            ExportQuotationVendor_ComboBox.getItems().add(IAECrawlerAccount_Bean);
            if(IAECrawlerAccount_Bean.isExportQuotation_defaultSelect()) {
                ExportQuotationVendor_ComboBox.getSelectionModel().select(IAECrawlerAccount_Bean);
            }
        }
    }
    private void setExportQuotationItems(){
        ObservableList<ExportQuotationItem> exportQuotationItemList = Order_Model.getExportQuotationItem(ExportQuotation_SplitMenuButton);
        for(ExportQuotationItem ExportQuotationItem : exportQuotationItemList){
            ExportQuotation_SplitMenuButton.getItems().add(ExportQuotation_SplitMenuButton.getItems().size()-1,
                    ExportQuotationItem.getCustomMenuItem());
        }
    }
    /** Whether the order was deleted */
    public boolean loadOrderData(){
        if(isOrderExist()){
            getOrderInfo(Order_Bean);
            if(Order_Bean == null) {
                DialogUI.AlarmDialog("※ 此貨單不存在，請重新搜尋!");
                return true;
            }
            if(orderStatus == Order_Enum.OrderStatus.併單)
                showCombinedOrderComponent();
            else if(orderStatus == Order_Enum.OrderStatus.比對貨單)
                showCompareOrderComponent();
        }
        return false;
    }
    private void getOrderInfo(Order_Bean Order_Bean){
        Order_Bean = Order_Model.getOrderInfo(OrderSource, Order_Bean);
        if(Order_Bean != null){
            Order_Bean.setProductList(Order_Model.getOrderItems(OrderSource, Order_Bean.getOrderID()));
            if(OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨單)
                Order_Bean.setProductGroupMap(Order_Model.getProductGroupByOrderId(Order_Bean.getOrderID()));
        }
    }

    private void setKeyWordTextLimiter(){
        ComponentToolKit.addKeyWordTextLimitLength(OrderNumberText, 14);
        ComponentToolKit.addKeyWordTextLimitLength(ISBNText, 13);
        ComponentToolKit.addKeyWordTextLimitLength(QuantityText,5);
        ComponentToolKit.addKeyWordTextLimitLength(InvoiceNumberText, 10);
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDouble(2,PricePercentageSpinner.getEditor());
        ComponentToolKit.addTextFieldLimitDigital(OrderNumberText, false);
        ComponentToolKit.addTextFieldLimitDouble(SinglePriceText);
        ComponentToolKit.addTextFieldLimitDigital(QuantityText,false);
        ComponentToolKit.addTextFieldLimitDigital(TaxText,false);
        ComponentToolKit.addTextFieldLimitDigital(DiscountText,false);
        ComponentToolKit.addTextFieldLimitDigital(ProjectQuantityText,false);
        ComponentToolKit.addTextFieldLimitDigital(ProjectPriceAmountText,false);
    }
    private void setBatchPriceTextFill(){
        BatchPriceText.setStyle("-fx-text-fill: red");
    }
    private void setProductGroupDifferentPriceTextFill(){
        ProductGroupDifferentPriceText.textProperty().addListener((ov, oldValue, newValue) -> {
            String productGroupDifferentPrice = ProductGroupDifferentPriceText.getText();
            if(!productGroupDifferentPrice.equals("")){
                if(Integer.parseInt(productGroupDifferentPrice) >= 0)  ProductGroupDifferentPriceText.setStyle("-fx-text-fill: #0089EB");
                else    ProductGroupDifferentPriceText.setStyle("-fx-text-fill: red");
            }
        });
    }
    private void setProjectDifferentPriceTextFill(){
        ProjectDifferentPriceText.textProperty().addListener((ov, oldValue, newValue) -> {
            String ProjectDifferentPrice = ProjectDifferentPriceText.getText();
            if(!ProjectDifferentPrice.equals("")){
                if(Integer.parseInt(ProjectDifferentPrice) >= 0)  ProjectDifferentPriceText.setStyle("-fx-text-fill: #0089EB");
                else    ProjectDifferentPriceText.setStyle("-fx-text-fill: red");
            }
        });
    }
    private void setPriceSpinnerAndTextFill(){
        ComponentToolKit.setDoubleSpinnerValueFactory(PricePercentageSpinner,0,100,0,0.01);
        TextField spinnerTextField = PricePercentageSpinner.getEditor();
        spinnerTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            double pricePercentage = Double.parseDouble(spinnerTextField.getText());
            if(pricePercentage > 1)
                spinnerTextField.setStyle("-fx-text-fill: red");
            else
                spinnerTextField.setStyle("-fx-text-fill: blue");
        });
    }
    private void initialPurchaseTableView(){
        setIsExistProductCodeColumnTextFill(ProductCodeColumn_Purchase,"ProductCode", "CENTER-LEFT", "16");
        setItemNumberColumnBackgroundColor(ItemNumberColumn_Purchase,"ItemNumber", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ISBNColumn_Purchase,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn_Purchase,"ProductName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuantityColumn_Purchase,"Quantity", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UnitColumn_Purchase,"Unit", "CENTER-LEFT", "16",null);
        setDoublePriceColumnMicrometerFormat(BatchPriceColumn_Purchase,"BatchPrice", "CENTER-LEFT", "16");
        setDoublePriceColumnMicrometerFormat(SinglePriceColumn_Purchase,"SinglePrice", "CENTER-LEFT", "16");
        setDoublePriceColumnMicrometerFormat(PricingColumn_Purchase,"Pricing", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(PriceAmountColumn_Purchase,"PriceAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(InStockColumn_Purchase,"InStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SafetyStockColumn_Purchase,"SafetyStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SupplyStatusColumn_Purchase, "SupplyStatus", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(RemarkColumn_Purchase,"Remark", "CENTER-LEFT", "16",null);
        setRemarkColumnTextFill(RemarkColumn_Purchase,"Remark", "CENTER-LEFT", "16");
    }
    private void initialShipmentTableView(){
        setIsExistProductCodeColumnTextFill(ProductCodeColumn_Shipment,"ProductCode", "CENTER-LEFT", "16");
        setItemNumberColumnBackgroundColor(ItemNumberColumn_Shipment,"ItemNumber", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ISBNColumn_Shipment,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn_Shipment,"ProductName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuantityColumn_Shipment,"Quantity", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UnitColumn_Shipment,"Unit", "CENTER-LEFT", "16",null);
        setDoublePriceColumnMicrometerFormat(BatchPriceColumn_Shipment,"BatchPrice", "CENTER-LEFT", "16");
        setDoublePriceColumnMicrometerFormat(SinglePriceColumn_Shipment,"SinglePrice", "CENTER-LEFT", "16");
        setDoublePriceColumnMicrometerFormat(PricingColumn_Shipment,"Pricing", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(PriceAmountColumn_Shipment,"PriceAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(InStockColumn_Shipment,"InStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SafetyStockColumn_Shipment,"SafetyStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SupplyStatusColumn_Shipment, "SupplyStatus", "CENTER-LEFT", "16",null);
        setRemarkColumnTextFill(RemarkColumn_Shipment,"Remark", "CENTER-LEFT", "16");
    }
    private void initialReturnTableView(){
        setIsExistProductCodeColumnTextFill(ProductCodeColumn_ReturnOrder,"ProductCode", "CENTER-LEFT", "16");
        setItemNumberColumnBackgroundColor(ItemNumberColumn_ReturnOrder,"ItemNumber", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ISBNColumn_ReturnOrder,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn_ReturnOrder,"ProductName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuantityColumn_ReturnOrder,"Quantity", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UnitColumn_ReturnOrder,"Unit", "CENTER-LEFT", "16",null);
        setDoublePriceColumnMicrometerFormat(BatchPriceColumn_ReturnOrder,"BatchPrice", "CENTER-LEFT", "16");
        setDoublePriceColumnMicrometerFormat(SinglePriceColumn_ReturnOrder,"SinglePrice", "CENTER-LEFT", "16");
        setDoublePriceColumnMicrometerFormat(PricingColumn_ReturnOrder,"Pricing", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(PriceAmountColumn_ReturnOrder,"PriceAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(InStockColumn_ReturnOrder,"InStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SafetyStockColumn_ReturnOrder,"SafetyStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SupplyStatusColumn_ReturnOrder, "SupplyStatus", "CENTER-LEFT", "16",null);
        setRemarkColumnTextFill(RemarkColumn_ReturnOrder,"Remark", "CENTER-LEFT", "16");
    }

    private void setOrderUI(){
        ObjectIDLabelText.setText(OrderObject.name() + "編號");
        ObjectNameLabelText.setText(OrderObject.name() + "名稱");
        if(orderStatus == Order_Enum.OrderStatus.比對貨單)
            Stage.setTitle(OrderSource.name() + "(貨單比對)");
        else if(orderStatus == Order_Enum.OrderStatus.併單)
            Stage.setTitle(OrderSource.name() + "(合併採購)");
        else
            Stage.setTitle(OrderSource.name());

        ComponentToolKit.setButtonStyle(ChangeQuotation_Button, "-fx-background-color:" + ToolKit.getQuotationBackgroundColor());
        ComponentToolKit.setButtonStyle(ChangeAlreadyOrder_Button, "-fx-background-color:" + ToolKit.getAlreadyOrderBackgroundColor());
        SystemDefaultConfigJson = CallConfig.getSystemDefaultConfigJson();
        setDefaultSearchFunction();
        setDefaultUploadPictureFunction();
        if(isOrderExist())
            setDefaultPrintFunction();
        if(OrderObject == Order_Enum.OrderObject.廠商){
            if(isOrderExist())
                setDefaultExportPurchaseFunction();
            ComponentToolKit.setButtonStyle(ChangeWaitingOrder_Button, "-fx-background-color:" + ToolKit.getWaitingPurchaseBackgroundColor());
            setManufacturerOrderTypeUI();
            setManufacturerUI();
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            ComponentToolKit.setButtonStyle(ChangeWaitingOrder_Button, "-fx-background-color:" + ToolKit.getWaitingShipmentBackgroundColor());
            setCustomerOrderTypeUI();
            setCustomerUI();
        }
    }
    private void setManufacturerOrderTypeUI(){
        ComponentToolKit.setHBoxVisible(PurchaserType_HBox, true);
        ComponentToolKit.setHBoxVisible(ShipmentType_HBox, false);

        ComponentToolKit.setHBoxVisible(TestDB_HBox, isOrderExist());

        if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.進貨退貨單 || !isOrderExist()){
            ComponentToolKit.setHBoxVisible(ChangeOrderType_HBox,false);
        }else{
            ComponentToolKit.setHBoxVisible(ChangeOrderType_HBox,true);
            ComponentToolKit.setToolTips(ChangeQuotation_Button, changeQuotationTooltip,Order_Enum.OrderSource.採購單.name());
            ComponentToolKit.setToolTips(ChangeWaitingOrder_Button, changeWaitingOrderTooltip,Order_Enum.OrderSource.待入倉單.name());
            ComponentToolKit.setToolTips(ChangeAlreadyOrder_Button, changeAlreadyOrderTooltip,Order_Enum.OrderSource.進貨單.name());
            ComponentToolKit.setButtonDisable(ChangeQuotation_Button,OrderSource == Order_Enum.OrderSource.採購單);
            ComponentToolKit.setButtonDisable(ChangeWaitingOrder_Button, !isTransferWaitingOrder() || OrderSource == Order_Enum.OrderSource.待入倉單);
            ComponentToolKit.setButtonDisable(ChangeAlreadyOrder_Button, !isTransferAlreadyOrder() || OrderSource == Order_Enum.OrderSource.進貨單);
        }
        if(isOrderExist()){
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.進貨退貨單)
                OrderType_Purchase_RadioButton.setText(Order_Enum.OrderSource.採購單.name());
            else
                OrderType_Purchase_RadioButton.setText(OrderSource.name());
        }

        if(OrderSource != Order_Enum.OrderSource.進貨退貨單){
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單) OrderType_PurchaseSubBill_RadioButton.setSelected(true);
            else    OrderType_Purchase_RadioButton.setSelected(true);
            MainTableView = PurchaseTableView;
        }else{
            OrderType_PurchaseReturn_RadioButton.setSelected(true);
            MainTableView = ReturnTableView;
        }
        if(isOrderExist())  setPurchaseRadioButtonDisable(true);
        else if(OrderSource == Order_Enum.OrderSource.進貨子貨單)   setPurchaseRadioButtonDisable(true);
        else   setPurchaseRadioButtonDisable(false);
    }
    private void setPurchaseRadioButtonDisable(boolean Disable){
        if(Disable){
            if(OrderSource == Order_Enum.OrderSource.進貨退貨單) {
                OrderType_Purchase_RadioButton.setDisable(true);
                OrderType_PurchaseSubBill_RadioButton.setDisable(true);
            }else if(OrderSource == Order_Enum.OrderSource.進貨子貨單) {
                OrderType_Purchase_RadioButton.setDisable(true);
                OrderType_PurchaseReturn_RadioButton.setDisable(true);
            }else{
                OrderType_PurchaseReturn_RadioButton.setDisable(true);
                OrderType_PurchaseSubBill_RadioButton.setDisable(true);
            }
        }else {
            OrderType_Purchase_RadioButton.setDisable(false);
            OrderType_PurchaseSubBill_RadioButton.setDisable(true);
            OrderType_PurchaseReturn_RadioButton.setDisable(false);
        }
    }
    private void setCustomerOrderTypeUI(){
        ComponentToolKit.setHBoxVisible(PurchaserType_HBox, false);
        ComponentToolKit.setHBoxVisible(ShipmentType_HBox, true);

        ComponentToolKit.setHBoxVisible(TestDB_HBox, isOrderExist());

        if(OrderSource == Order_Enum.OrderSource.出貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單 || !isOrderExist()){
            ComponentToolKit.setHBoxVisible(ChangeOrderType_HBox,false);
        }else{
            ComponentToolKit.setHBoxVisible(ChangeOrderType_HBox,true);
            ComponentToolKit.setToolTips(ChangeQuotation_Button, changeQuotationTooltip,Order_Enum.OrderSource.報價單.name());
            ComponentToolKit.setToolTips(ChangeWaitingOrder_Button, changeWaitingOrderTooltip,Order_Enum.OrderSource.待出貨單.name());
            ComponentToolKit.setToolTips(ChangeAlreadyOrder_Button, changeAlreadyOrderTooltip,Order_Enum.OrderSource.出貨單.name());
            ComponentToolKit.setButtonDisable(ChangeQuotation_Button, OrderSource == Order_Enum.OrderSource.報價單);
            ComponentToolKit.setButtonDisable(ChangeWaitingOrder_Button, !isTransferWaitingOrder() || OrderSource == Order_Enum.OrderSource.待出貨單);
            ComponentToolKit.setButtonDisable(ChangeAlreadyOrder_Button, !isTransferAlreadyOrder() || OrderSource == Order_Enum.OrderSource.出貨單);
        }
        if(isOrderExist()){
            if(OrderSource == Order_Enum.OrderSource.出貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
                OrderType_Shipment_RadioButton.setText(Order_Enum.OrderSource.報價單.name());
            else
                OrderType_Shipment_RadioButton.setText(OrderSource.name());
        }

        if(OrderSource != Order_Enum.OrderSource.出貨退貨單){
            if(OrderSource == Order_Enum.OrderSource.出貨子貨單) OrderType_ShipmentSubBill_RadioButton.setSelected(true);
            else    OrderType_Shipment_RadioButton.setSelected(true);
            MainTableView = ShipmentTableView;
        }else{
            OrderType_ShipmentReturn_RadioButton.setSelected(true);
            MainTableView = ReturnTableView;
        }
        if(isOrderExist()) {
            setShipmentRadioButtonDisable(true);
        }else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)   setShipmentRadioButtonDisable(true);
        else   setShipmentRadioButtonDisable(false);
    }
    private void setShipmentRadioButtonDisable(boolean Disable){
        if(Disable) {
            if(OrderSource == Order_Enum.OrderSource.出貨退貨單) {
                OrderType_Shipment_RadioButton.setDisable(true);
                OrderType_ShipmentSubBill_RadioButton.setDisable(true);
            }else if(OrderSource == Order_Enum.OrderSource.出貨子貨單) {
                OrderType_Shipment_RadioButton.setDisable(true);
                OrderType_ShipmentReturn_RadioButton.setDisable(true);
            }else{
                OrderType_ShipmentReturn_RadioButton.setDisable(true);
                OrderType_ShipmentSubBill_RadioButton.setDisable(true);
            }
        }else{
            OrderType_Shipment_RadioButton.setDisable(false);
            OrderType_ShipmentSubBill_RadioButton.setDisable(true);
            OrderType_ShipmentReturn_RadioButton.setDisable(false);
        }
    }

    private void setManufacturerUI(){
        if(transferQuotation_toggleSwitch == null)
            transferQuotation_toggleSwitch = createTransferQuotationSwitchButton(Order_Enum.OrderObject.廠商);

        ComponentToolKit.setCheckBoxDisable(ShowProfit_CheckBox,true);
        ComponentToolKit.setHBoxVisible(ProductGroupDifferencePrice_HBox,false);

        showPurchasePriceTableColumnVisible();
        ComponentToolKit.setHBoxVisible(OrderReferenceButton_HBox,false);
        if(OrderSource == Order_Enum.OrderSource.採購單) setPurchaseQuotationUI();
        else if(OrderSource == Order_Enum.OrderSource.待入倉單) setWaitingPurchaseOrderUI();
        else if(OrderSource == Order_Enum.OrderSource.進貨子貨單)    setSubBillUI();
        else if(OrderSource == Order_Enum.OrderSource.進貨單)   setPurchaseOrderUI();
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單) setReturnOrderUI();
    }
    private void setPurchaseQuotationUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
        showProjectInfo_HBox(true);
        showPurchase_TableViewGridPane();
        if(isOrderExist()) setExistPurchaseQuotationUI();
        else  setNoneExistPurchaseQuotationUI();
        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        ComponentToolKit.setButtonDisable(InvoicePaste_Button,true);
        ComponentToolKit.setButtonDisable(CompletionReport_Button,true);
        ComponentToolKit.setDatePickerDisable(PurchaseOrderDate_DatePicker, true);
        ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,false);
        ComponentToolKit.setButtonVisible(cancelPurchaseOrderProduct_Button,false);
        modifyPurchaseHBox.getChildren().remove(ExportPayable_Button);
        modifyPurchaseHBox.getChildren().remove(establishPurchaseCheckout_menuButton);
        modifyPurchaseHBox.getChildren().remove(establishPayable_button);
    }
    private void setExistPurchaseQuotationUI(){
        showOrderPicture_HBox(true);
        showModifyPurchase_GridPane();
        if(!modifyPurchaseHBox.getChildren().contains(ExportPurchaseFunction_SplitMenuButton))
            modifyPurchaseHBox.getChildren().add(ExportPurchaseFunction_SplitMenuButton);
        if(!isTransferWaitingOrder()) {
            setReviewStatusSplitMenuButton__Disable(false);
            ComponentToolKit.setHBoxDisable(transferQuotationHBox_PurchaseUI,false);
            ComponentToolKit.setDatePickerDisable(WaitingPurchaseOrderDate_DatePicker,false);
            ComponentToolKit.setButtonVisible(TransferWaitingPurchaseOrder_Button, true);
        }else{
            LockComponent();
            setReviewStatusSplitMenuButton__Disable(true);
            ComponentToolKit.setDatePickerDisable(WaitingPurchaseOrderDate_DatePicker,true);
            ComponentToolKit.setButtonVisible(TransferWaitingPurchaseOrder_Button, false);
        }
        if(!isTransferAlreadyOrder())   ComponentToolKit.setTextFieldDisable(PurchaseOrderNumberText, true);
        else    ComponentToolKit.setHBoxDisable(transferQuotationHBox_PurchaseUI,false);
    }
    private void setNoneExistPurchaseQuotationUI(){
        setReviewStatusSplitMenuButton__Disable(false);
        showOrderPicture_HBox(false);
        showEstablishPurchase_GridPane();
        ComponentToolKit.setHBoxDisable(transferQuotationHBox_PurchaseUI,true);
    }
    private void setWaitingPurchaseOrderUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
        setTransferPurchaseOrderUI();

        modifyPurchaseHBox.getChildren().remove(ExportPurchaseFunction_SplitMenuButton);
        boolean isMainOrderExistSubBill = Order_Model.isMainOrderExistSubBill(Order_Bean.getOrderSource().getOrderObject(), Order_Bean.getWaitingOrderNumber(),null,null);
        if(!isMainOrderExistSubBill) {
            if (!modifyPurchaseHBox.getChildren().contains(ExportPayable_Button))
                modifyPurchaseHBox.getChildren().add(ExportPayable_Button);
            ComponentToolKit.setButtonVisible(ExportPayable_Button,true);
        }else
            modifyPurchaseHBox.getChildren().remove(ExportPayable_Button);
        modifyPurchaseHBox.getChildren().remove(establishPayable_button);
        if (!modifyPurchaseHBox.getChildren().contains(establishPurchaseCheckout_menuButton))
            modifyPurchaseHBox.getChildren().add(establishPurchaseCheckout_menuButton);
        setEstablishPayReceiveMenuItemDisable();
//        ComponentToolKit.setMenuItemDisable(establishPurchasePay_menuItem, !((Order_Bean.isExistInstallment() && !isOrderCheckout())));

        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        ComponentToolKit.setHBoxDisable(transferQuotationHBox_PurchaseUI,true);
        if(!isTransferAlreadyOrder()){
            ComponentToolKit.setDatePickerDisable(PurchaseOrderDate_DatePicker,false);
            ComponentToolKit.setTextFieldDisable(PurchaseOrderNumberText, false);
            ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,true);
            ComponentToolKit.setButtonVisible(cancelPurchaseOrderProduct_Button,true);
        }else{
            ComponentToolKit.setDatePickerDisable(PurchaseOrderDate_DatePicker,true);
            ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,false);
            ComponentToolKit.setButtonVisible(cancelPurchaseOrderProduct_Button,false);
        }
    }
    private void setPurchaseOrderUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
        setTransferPurchaseOrderUI();
        ComponentToolKit.setDatePickerDisable(PurchaseOrderDate_DatePicker,true);
        ComponentToolKit.setTextFieldDisable(PurchaseOrderNumberText, false);
        ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,false);
        ComponentToolKit.setButtonVisible(cancelPurchaseOrderProduct_Button,false);
        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        ComponentToolKit.setHBoxDisable(transferQuotationHBox_PurchaseUI,false);

        modifyPurchaseHBox.getChildren().remove(ExportPurchaseFunction_SplitMenuButton);
        modifyPurchaseHBox.getChildren().remove(ExportPayable_Button);
        modifyPurchaseHBox.getChildren().remove(establishPayable_button);
        if(!modifyPurchaseHBox.getChildren().contains(establishPurchaseCheckout_menuButton))
            modifyPurchaseHBox.getChildren().add(establishPurchaseCheckout_menuButton);
        ComponentToolKit.setMenuItemDisable(establishPurchasePay_menuItem, isOrderCheckout());
    }
    private void setTransferPurchaseOrderUI(){
        showPurchase_TableViewGridPane();
        setReviewStatusSplitMenuButton__Disable(true);
        showProjectInfo_HBox(true);
        showOrderPicture_HBox(true);
        showModifyPurchase_GridPane();
        LockComponent();
        ComponentToolKit.setDatePickerDisable(WaitingPurchaseOrderDate_DatePicker,true);
        ComponentToolKit.setButtonVisible(TransferWaitingPurchaseOrder_Button, false);
    }
    private void setCustomerUI(){
        if(transferQuotation_toggleSwitch == null)
            transferQuotation_toggleSwitch = createTransferQuotationSwitchButton(Order_Enum.OrderObject.客戶);
        if(offsetStatus_toggleSwitch == null) {
            offsetStatus_toggleSwitch = createOffsetStatusSwitchButton();
        }

        if(defaultProductConnection_toggleSwitch == null) {
            boolean defaultProductConnection = SystemDefaultConfigJson.getBoolean(SystemDefaultConfig_Enum.Order_DefaultProductConnection.class.getSimpleName());
            defaultProductConnection_toggleSwitch = createDefaultProductConnectionSwitchButton(defaultProductConnection);
        }
        ComponentToolKit.setCheckBoxDisable(ShowProfit_CheckBox,false);
        ComponentToolKit.setHBoxVisible(ProductGroupDifferencePrice_HBox,true);
        showShipmentPriceTableColumnVisible();
        if(OrderSource == Order_Enum.OrderSource.報價單) setShipmentQuotationUI();
        else if(OrderSource == Order_Enum.OrderSource.待出貨單) setWaitingShipmentOrderUI();
        else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)    setSubBillUI();
        else if(OrderSource == Order_Enum.OrderSource.出貨單)   setShipmentOrderUI();
        else if(OrderSource == Order_Enum.OrderSource.出貨退貨單)    setReturnOrderUI();
    }
    private void setShipmentQuotationUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
        showShipment_TableViewGridPane();
        if(isOrderExist()) setExistShipmentQuotationUI();
        else  setNoneExistShipmentQuotationUI();
        ComponentToolKit.setButtonDisable(AddOrderReference_Button, !isOrderExist());
        ComponentToolKit.setButtonDisable(LoadOrderReference_Button,isOrderExist() && isTransferWaitingOrder());
        ComponentToolKit.setButtonDisable(ShowOrderReference_Button,!isOrderExist());
        ComponentToolKit.setButtonDisable(InvoicePaste_Button,true);
        ComponentToolKit.setButtonDisable(CompletionReport_Button,true);
        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        modifyShipmentHBox.getChildren().remove(establishShipmentCheckout_menuButton);
        modifyShipmentHBox.getChildren().remove(establishReceivable_button);
    }
    private void setNoneExistShipmentQuotationUI(){
        setReviewStatusSplitMenuButton__Disable(false);
        showOrderPicture_HBox(false);
        setExportQuotationGirdPane_Disable(true);
        showEstablishShipment_GridPane();
        ComponentToolKit.setHBoxDisable(transferQuotationHBox_ShipmentUI,true);
    }
    private void setExistShipmentQuotationUI(){
        showOrderPicture_HBox(true);
        setExportQuotationGirdPane_Disable(false);
        showModifyShipment_GridPane();
        ComponentToolKit.setDatePickerDisable(ShipmentOrderDate_DatePicker, true);
        ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,false);
        ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,false);

        if(!isTransferWaitingOrder()) {
            setReviewStatusSplitMenuButton__Disable(false);
            ComponentToolKit.setHBoxDisable(transferQuotationHBox_ShipmentUI,false);
            ComponentToolKit.setDatePickerDisable(WaitingShipmentOrderDate_DatePicker,false);
            ComponentToolKit.setButtonVisible(TransferWaitingShipmentOrder_Button, true);
        }else{
            LockComponent();
            setReviewStatusSplitMenuButton__Disable(true);
            ComponentToolKit.setDatePickerDisable(WaitingShipmentOrderDate_DatePicker,true);
            ComponentToolKit.setButtonVisible(TransferWaitingShipmentOrder_Button, false);
        }
        if(!isTransferAlreadyOrder())   ComponentToolKit.setTextFieldDisable(ShipmentOrderNumberText, true);
        else    ComponentToolKit.setHBoxDisable(transferQuotationHBox_ShipmentUI,false);
    }
    private void setWaitingShipmentOrderUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
        setTransfersShipmentOrderUI();
        ComponentToolKit.setButtonDisable(AddOrderReference_Button,false);
        ComponentToolKit.setButtonDisable(LoadOrderReference_Button,true);
        ComponentToolKit.setButtonDisable(ShowOrderReference_Button,false);
        ComponentToolKit.setButtonDisable(InvoicePaste_Button,false);
        ComponentToolKit.setButtonDisable(CompletionReport_Button,true);
        modifyShipmentHBox.getChildren().remove(establishReceivable_button);
        if (!modifyShipmentHBox.getChildren().contains(establishShipmentCheckout_menuButton))
            modifyShipmentHBox.getChildren().add(establishShipmentCheckout_menuButton);
        setEstablishPayReceiveMenuItemDisable();
        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        ComponentToolKit.setHBoxDisable(transferQuotationHBox_ShipmentUI,true);
        if(!isTransferAlreadyOrder()){
            ComponentToolKit.setDatePickerDisable(ShipmentOrderDate_DatePicker,false);
            ComponentToolKit.setTextFieldDisable(ShipmentOrderNumberText, false);
            ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,true);
            ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,true);
        }else{
            ComponentToolKit.setDatePickerDisable(ShipmentOrderDate_DatePicker,true);
            ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,false);
            ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,false);
        }
    }
    private void setSubBillUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getSubBillBackgroundColor());
        setReviewStatusSplitMenuButton__Disable(true);
        ComponentToolKit.setCheckBoxDisable(OrderBorrowed_CheckBox,true);
        ComponentToolKit.setHBoxDisable(Offset_HBox,true);
        ComponentToolKit.setButtonVisible(cancelPurchaseOrderProduct_Button,false);
        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        showOrderPicture_HBox(isOrderExist());
        if(OrderSource == Order_Enum.OrderSource.進貨子貨單) {
            modifyPurchaseHBox.getChildren().remove(establishPurchaseCheckout_menuButton);
            showPurchase_TableViewGridPane();
            ComponentToolKit.setHBoxDisable(transferQuotationHBox_PurchaseUI,true);
            if(isOrderExist()) setExistPurchaseSubBillUI();
            else  setNoneExistPurchaseSubBillUI();
        }else if(OrderSource == Order_Enum.OrderSource.出貨子貨單){
            modifyShipmentHBox.getChildren().remove(establishShipmentCheckout_menuButton);
            showShipment_TableViewGridPane();
            ComponentToolKit.setHBoxDisable(transferQuotationHBox_ShipmentUI,true);
            if(isOrderExist()) setExistShipmentSubBillUI();
            else  setNoneExistShipmentSubBillUI();
            ComponentToolKit.setButtonDisable(AddOrderReference_Button, !isTransferAlreadyOrder());
            ComponentToolKit.setButtonDisable(LoadOrderReference_Button, isOrderExist());
            ComponentToolKit.setButtonDisable(ShowOrderReference_Button,false);
        }
    }
    private void setExistPurchaseSubBillUI(){
        LockComponent();
        ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,true);

        showModifyPurchase_GridPane();
        ComponentToolKit.setButtonDisable(DeleteModifyPurchaseOrderProduct_Button,true);

        ComponentToolKit.setDatePickerDisable(WaitingPurchaseOrderDate_DatePicker,true);
        ComponentToolKit.setButtonVisible(TransferWaitingPurchaseOrder_Button, false);
        modifyPurchaseHBox.getChildren().remove(ExportPurchaseFunction_SplitMenuButton);
        modifyPurchaseHBox.getChildren().remove(ExportPayable_Button);
        if(!isTransferAlreadyOrder()){
            ComponentToolKit.setDatePickerDisable(PurchaseOrderDate_DatePicker,false);
            ComponentToolKit.setTextFieldDisable(PurchaseOrderNumberText, false);
            ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,true);
            ComponentToolKit.setButtonVisible(establishPayable_button,false);
        }else{
            ComponentToolKit.setDatePickerDisable(PurchaseOrderDate_DatePicker,true);
            ComponentToolKit.setButtonVisible(TransferPurchaseOrder_Button,false);
            ComponentToolKit.setButtonVisible(establishPayable_button, !isOrderCheckout());
        }
    }
    private void setNoneExistPurchaseSubBillUI(){
        showEstablishPurchase_GridPane();
        ComponentToolKit.setButtonDisable(DeleteModifyPurchaseOrderProduct_Button,true);

        ComponentToolKit.setTextFieldEditable(ISBNText,false);
        ComponentToolKit.setTextFieldEditable(ProductNameText,false);
        ComponentToolKit.setTextFieldEditable(QuantityText,false);
    }
    private void setExistShipmentSubBillUI(){
        LockComponent();
        ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,true);
        ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,true);
        setExportQuotationGirdPane_Disable(false);

        showModifyShipment_GridPane();

        ComponentToolKit.setDatePickerDisable(WaitingShipmentOrderDate_DatePicker,true);
        ComponentToolKit.setButtonVisible(TransferWaitingShipmentOrder_Button, false);
        ComponentToolKit.setButtonDisable(InvoicePaste_Button,false);
        if(!isTransferAlreadyOrder()){
            ComponentToolKit.setButtonDisable(CompletionReport_Button,true);
            ComponentToolKit.setDatePickerDisable(ShipmentOrderDate_DatePicker,false);
            ComponentToolKit.setTextFieldDisable(ShipmentOrderNumberText, false);
            ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,true);
            ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,true);
            ComponentToolKit.setButtonVisible(establishReceivable_button,false);
        }else{
            ComponentToolKit.setButtonDisable(CompletionReport_Button,false);
            ComponentToolKit.setDatePickerDisable(ShipmentOrderDate_DatePicker,true);
            ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,false);
            ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,false);

            ComponentToolKit.setButtonVisible(establishReceivable_button, !isOrderCheckout());
        }
    }
    private void setNoneExistShipmentSubBillUI(){
        setExportQuotationGirdPane_Disable(true);
        showEstablishShipment_GridPane();
        if(!isOffsetOrder()){
            ComponentToolKit.setButtonDisable(DeleteEstablishShipmentOrderProduct_Button,true);
            ComponentToolKit.setTextFieldEditable(ISBNText,false);
            ComponentToolKit.setTextFieldEditable(ProductNameText,false);
            ComponentToolKit.setTextFieldEditable(QuantityText,false);
        }
    }
    private void setShipmentOrderUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
        setTransfersShipmentOrderUI();
        ComponentToolKit.setButtonDisable(AddOrderReference_Button,false);
        ComponentToolKit.setButtonDisable(LoadOrderReference_Button, true);
        ComponentToolKit.setButtonDisable(ShowOrderReference_Button,false);
        ComponentToolKit.setButtonDisable(InvoicePaste_Button,false);
        ComponentToolKit.setButtonDisable(CompletionReport_Button,false);
        ComponentToolKit.setDatePickerDisable(ShipmentOrderDate_DatePicker,true);
        ComponentToolKit.setTextFieldDisable(ShipmentOrderNumberText, false);
        ComponentToolKit.setButtonVisible(TransferShipmentOrder_Button,false);
        ComponentToolKit.setButtonVisible(cancelShipmentOrderProduct_Button,false);
        ComponentToolKit.setButtonVisible(CashierRemarkButton,true);
        ComponentToolKit.setHBoxDisable(transferQuotationHBox_ShipmentUI,false);
        modifyShipmentHBox.getChildren().remove(establishReceivable_button);
        if (!modifyShipmentHBox.getChildren().contains(establishShipmentCheckout_menuButton))
            modifyShipmentHBox.getChildren().add(establishShipmentCheckout_menuButton);
        ComponentToolKit.setMenuItemDisable(establishShipmentReceive_menuItem, isOrderCheckout());
    }
    private void setTransfersShipmentOrderUI(){
        setReviewStatusSplitMenuButton__Disable(true);
        showShipment_TableViewGridPane();
        showProjectInfo_HBox(true);
        showOrderPicture_HBox(true);
        setExportQuotationGirdPane_Disable(false);
        showModifyShipment_GridPane();
        LockComponent();
        ComponentToolKit.setDatePickerDisable(WaitingShipmentOrderDate_DatePicker,true);
        ComponentToolKit.setButtonVisible(TransferWaitingShipmentOrder_Button, false);
    }
    private void setReturnOrderUI(){
        OrderInfoGridPane.setStyle("-fx-background-color: " + ToolKit.getReturnOrderBackgroundColor());
        setReviewStatusSplitMenuButton__Disable(true);
        ComponentToolKit.setButtonDisable(AddOrderReference_Button,true);
        ComponentToolKit.setButtonDisable(LoadOrderReference_Button, true);
        ComponentToolKit.setButtonDisable(ShowOrderReference_Button,true);
        showShipmentReturn_TableViewGridPane();
        showOrderPicture_HBox(false);
        showProjectInfo_HBox(false);
        ComponentToolKit.setButtonVisible(CashierRemarkButton,false);
        if(isOrderExist()) {
            showModifyReturn_GridPane();
            LockComponent();
        }
    }
    private void showPurchase_TableViewGridPane(){
        ComponentToolKit.setGridPaneVisible(PurchaseGridPane, true);
        ComponentToolKit.setGridPaneVisible(ShipmentGridPane, false);
        ComponentToolKit.setGridPaneVisible(ReturnGridPane, false);
    }
    private void showShipment_TableViewGridPane(){
        ComponentToolKit.setGridPaneVisible(PurchaseGridPane, false);
        ComponentToolKit.setGridPaneVisible(ShipmentGridPane, true);
        ComponentToolKit.setGridPaneVisible(ReturnGridPane, false);
    }
    private void showShipmentReturn_TableViewGridPane(){
        ComponentToolKit.setGridPaneVisible(PurchaseGridPane, false);
        ComponentToolKit.setGridPaneVisible(ShipmentGridPane, false);
        ComponentToolKit.setGridPaneVisible(ReturnGridPane, true);
    }
    private void setReviewStatusSplitMenuButton__Disable(boolean disable){
        ComponentToolKit.setSplitMenuButtonDisable(OrderReviewStatus_SplitMenuButton,disable);
    }
    private void showProjectInfo_HBox(boolean Visible){
        ComponentToolKit.setHBoxVisible(ProjectInfoHBox, Visible);
    }
    private void showOrderPicture_HBox(boolean Visible){
        ComponentToolKit.setHBoxVisible(OrderPictureButton_HBox, Visible);
    }
    private void setExportQuotationGirdPane_Disable(boolean Disable){
        ComponentToolKit.setGridPaneDisable(ExportQuotationGridPane, Disable);
    }
    private void showEstablishPurchase_GridPane(){
        ComponentToolKit.setHBoxVisible(establishPurchaseHBox,true);
        ComponentToolKit.setHBoxVisible(modifyPurchaseHBox,false);
        ComponentToolKit.setGridPaneVisible(modifyPurchaseGridPane, false);
    }
    private void showModifyPurchase_GridPane(){
        ComponentToolKit.setHBoxVisible(establishPurchaseHBox,false);
        ComponentToolKit.setHBoxVisible(modifyPurchaseHBox,true);
        ComponentToolKit.setGridPaneVisible(modifyPurchaseGridPane, true);
    }
    private void showEstablishShipment_GridPane(){
        ComponentToolKit.setHBoxVisible(establishShipmentHBox,true);
        ComponentToolKit.setHBoxVisible(modifyShipmentHBox,false);
        ComponentToolKit.setHBoxVisible(InvoiceInfo_HBox,false);
        ComponentToolKit.setGridPaneVisible(modifyShipmentGridPane, false);
    }
    private void showModifyShipment_GridPane(){
        ComponentToolKit.setHBoxVisible(establishShipmentHBox,false);
        ComponentToolKit.setHBoxVisible(modifyShipmentHBox,true);
        ComponentToolKit.setHBoxVisible(InvoiceInfo_HBox,true);
        ComponentToolKit.setGridPaneVisible(modifyShipmentGridPane, true);
    }
    private void showModifyReturn_GridPane(){
        ComponentToolKit.setHBoxVisible(establishReturnHBox,false);
        ComponentToolKit.setHBoxVisible(modifyReturnHBox,true);
    }
    @FXML protected void OrderKeyReleased(KeyEvent KeyEvent){
        if(isOrderExist() && saveMoveKeyCombine.match(KeyEvent)) {
            modifyOrder();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 採購 */
    @FXML protected void Purchase_RadioButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && !isOrderExist()) {
            setOrderSource(Order_Enum.OrderSource.採購單);
            setOrderUI();
            initialComponentExceptSubBill(true);
        }
    }
    /** RadioButton Mouse Clicked - [單別] 進退貨 */
    @FXML protected void PurchaseReturn_RadioButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && !isOrderExist()) {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                setOrderSource(Order_Enum.OrderSource.進貨退貨單);
                setOrderUI();
                initialComponentExceptSubBill(true);
            }
        }
    }
    /** RadioButton Mouse Clicked - [單別] 報價 */
    @FXML protected void Shipment_RadioButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && !isOrderExist()) {
            setOrderSource(Order_Enum.OrderSource.報價單);
            setOrderUI();
            initialComponentExceptSubBill(true);
        }
    }
    /** RadioButton Mouse Clicked - [單別] 出退貨 */
    @FXML protected void ShipmentReturn_RadioButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && !isOrderExist()) {
            setOrderSource(Order_Enum.OrderSource.出貨退貨單);
            setOrderUI();
            initialComponentExceptSubBill(true);
        }
    }
    @FXML protected void TestDBMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String database = SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.測試資料庫);
            if(database == null) {
                DialogUI.MessageDialog("未設定【測試資料庫】");
            }else{
                String projectCode = DialogUI.TextInputDialog("測試資料庫","請輸入ERP專案回報編號",null);
                Order_Bean order_bean = ToolKit.CopyOrderBean(Order_Bean);
                if(!isNoneExistObjectAndInsert(order_bean)){
                    DialogUI.AlarmDialog("【測試資料庫】客戶、廠商判斷異常!");
                    return;
                }else if(!isProductExist(order_bean.getProductList())){
                    DialogUI.AlarmDialog("【測試資料庫】商品資料判斷異常!");
                    return;
                }

                //  判斷廠商、對象、商品ISBN有沒有存在
                boolean isSubBill = false;
                if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                    order_bean.setOrderSource(OrderSource);
                    Order_Bean mainOrderInfo = Order_Model.getOrderInfo((OrderSource == Order_Enum.OrderSource.進貨子貨單 ? Order_Enum.OrderSource.待入倉單 : Order_Enum.OrderSource.待出貨單), ToolKit.CopyOrderBean(Order_Bean));
                    generateTestDBOrder(false, mainOrderInfo, "母貨單");
                    mainOrderInfo.setOrderSource((OrderSource == Order_Enum.OrderSource.待入倉單 ? Order_Enum.OrderSource.採購單 : Order_Enum.OrderSource.報價單));
                    boolean status = Order_Model.insertOrder(mainOrderInfo.getOrderSource(), mainOrderInfo,null,false,true);
                    if(status){
                        isSubBill = true;
                        order_bean.setOrderID(mainOrderInfo.getOrderID());
                        order_bean.setWaitingOrderNumber(mainOrderInfo.getWaitingOrderNumber());
                    }else{
                        DialogUI.AlarmDialog("【測試資料庫】母貨單寫入失敗");
                        return;
                    }
                }else{
                    if(OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.進貨單){
                        order_bean.setOrderSource(Order_Enum.OrderSource.採購單);
                    }else if(OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨單){
                        order_bean.setOrderSource(Order_Enum.OrderSource.報價單);
                    }else{
                        order_bean.setOrderSource(OrderSource);
                    }
                }
                generateTestDBOrder(isSubBill, order_bean, projectCode);
                boolean status = Order_Model.insertOrder(order_bean.getOrderSource(), order_bean,null,false,true);
                if(status){
                    if(order_bean.getProductGroupMap() != null){
                        status = insertTransferQuotationProductGroup(order_bean);
                    }
                    if(status){
                        DialogUI.MessageDialog("【測試資料庫】寫入成功");
                    }else{
                        DialogUI.AlarmDialog("【測試資料庫】寫入成功，但【商品群組】寫入失敗");
                    }
                }else{
                    DialogUI.AlarmDialog("【測試資料庫】寫入失敗");
                }
            }
        }
    }
    private boolean isNoneExistObjectAndInsert(Order_Bean order_bean){
        boolean status = true;
        if(OrderSource.getOrderObject() == Order_Enum.OrderObject.廠商 &&
                !manageManufacturerInfo_model.isManufacturerIDExist(order_bean.getObjectID(),true)){
            status = manageManufacturerInfo_model.insertManufacturer(ObjectInfo_Bean,true);
        }else if(OrderSource.getOrderObject() == Order_Enum.OrderObject.客戶 &&
                !manageCustomerInfo_model.isCustomerIDExist(order_bean.getObjectID(),true)){
            status = manageCustomerInfo_model.insertCustomer(ObjectInfo_Bean,true);
        }
        return status;
    }
    private boolean isProductExist(ObservableList<OrderProduct_Bean> productList){
        for(OrderProduct_Bean orderProduct_bean : productList){
            boolean status = !Product_Model.isISBNExist(orderProduct_bean.getISBN(),true) ?
                    Product_Model.insertProduct(generateProductInfoBean(orderProduct_bean),false,true):
                    Product_Model.modifyProduct(generateProductInfoBean(orderProduct_bean),false,true);
            if(!status){
                return false;
            }
        }
        return true;
    }
    private ProductInfo_Bean generateProductInfoBean(OrderProduct_Bean orderProduct_bean){
        ProductInfo_Bean productInfo_bean = new ProductInfo_Bean();
        productInfo_bean.setISBN(orderProduct_bean.getISBN());
        productInfo_bean.setInternationalCode(orderProduct_bean.getInternationalCode());
        productInfo_bean.setFirmCode(orderProduct_bean.getFirmCode());
        productInfo_bean.setProductCode(orderProduct_bean.getProductCode());
        productInfo_bean.setProductName(orderProduct_bean.getProductName());
        productInfo_bean.setUnit(orderProduct_bean.getUnit());
        productInfo_bean.setBrand("");
        productInfo_bean.setDescribe("");
        productInfo_bean.setRemark(orderProduct_bean.getRemark());
        productInfo_bean.setSupplyStatus(orderProduct_bean.getSupplyStatus());
        productInfo_bean.setInStock("0");
        productInfo_bean.setSafetyStock("0");
        productInfo_bean.setInventoryQuantity(0);
        productInfo_bean.setVendorName("");
        productInfo_bean.setVendorCode("");

        productInfo_bean.setBatchPrice(ToolKit.RoundingString(true, orderProduct_bean.getBatchPrice()));
        productInfo_bean.setSinglePrice(ToolKit.RoundingString(true, orderProduct_bean.getSinglePrice()));
        productInfo_bean.setPricing(ToolKit.RoundingString(true, orderProduct_bean.getPricing()));
        productInfo_bean.setVipPrice1(ToolKit.RoundingString(true, orderProduct_bean.getVipPrice1()));
        productInfo_bean.setVipPrice2(ToolKit.RoundingString(true, orderProduct_bean.getVipPrice2()));
        productInfo_bean.setVipPrice3(ToolKit.RoundingString(true, orderProduct_bean.getVipPrice3()));
        productInfo_bean.setDiscount(1.0);

        productInfo_bean.setVendorFirstCategory_id(null);
        productInfo_bean.setVendorSecondCategory_id(null);
        productInfo_bean.setVendorThirdCategory_id(null);

        productInfo_bean.setFirstCategory(orderProduct_bean.getFirstCategory());
        productInfo_bean.setSecondCategory("");
        productInfo_bean.setThirdCategory("");

        productInfo_bean.setProductArea("");
        productInfo_bean.setProductFloor("");

        productInfo_bean.setYahooCategory("");
        productInfo_bean.setRutenCategory("");
        productInfo_bean.setShopeeCategory(null);

        productInfo_bean.setPicture1("");
        productInfo_bean.setPicture2("");
        productInfo_bean.setPicture3("");

        productInfo_bean.setProductTag(FXCollections.observableArrayList());
        return productInfo_bean;
    }
    private void generateTestDBOrder(boolean isSubBill, Order_Bean order_bean, String projectCode){
        String orderDate = order_bean.getOrderDate(), waitingOrderDate = order_bean.getWaitingOrderDate(), alreadyOrderDate = order_bean.getAlreadyOrderDate();
        String orderNumber;
        if(isSubBill){
            orderNumber = Order_Model.generateNewestOrderNumberOfEstablishOrder(order_bean.getOrderSource(), Order_Enum.generateOrderNumberMethod.單號, order_bean.getWaitingOrderNumber(),true);
            int OrderNumberIndex = Integer.parseInt(orderNumber.substring(Integer.parseInt(SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.貨單號長度))));
            order_bean.setNowOrderNumber(order_bean.getWaitingOrderNumber() + ToolKit.fillZero(String.valueOf(OrderNumberIndex),2));
        }else{
            orderNumber = Order_Model.generateNewestOrderNumberOfEstablishOrder(order_bean.getOrderSource(), Order_Enum.generateOrderNumberMethod.日期, orderDate,true);
            order_bean.setNowOrderNumber(orderNumber);
        }
        if(!isSubBill && order_bean.getWaitingOrderNumber() != null){
            if(waitingOrderDate.equals(orderDate)) {
                order_bean.setWaitingOrderNumber(String.valueOf(Long.parseLong(orderNumber) + 1));
            }else{
                order_bean.setWaitingOrderNumber(String.valueOf(Long.parseLong(Order_Model.generateNewestOrderNumberOfEstablishOrder(order_bean.getOrderSource(), Order_Enum.generateOrderNumberMethod.日期, waitingOrderDate,true))));
            }
        }
        if(order_bean.getAlreadyOrderNumber() != null){
            if(alreadyOrderDate.equals(waitingOrderDate)){
                order_bean.setAlreadyOrderNumber(String.valueOf(Long.parseLong(order_bean.getWaitingOrderNumber())+1));
            }else if(alreadyOrderDate.equals(orderDate)){
                order_bean.setAlreadyOrderNumber(String.valueOf(Long.parseLong(orderNumber)+1));
            }else{
                order_bean.setAlreadyOrderNumber(String.valueOf(Long.parseLong(Order_Model.generateNewestOrderNumberOfEstablishOrder(order_bean.getOrderSource(), Order_Enum.generateOrderNumberMethod.日期, alreadyOrderDate,true))));
            }
        }
        order_bean.setProjectName("【測試_" + projectCode + "】" + order_bean.getProjectName());
    }
    @FXML protected void ChangeQuotationButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(OrderObject == Order_Enum.OrderObject.廠商)    this.OrderSource = Order_Enum.OrderSource.採購單;
            else if(OrderObject == Order_Enum.OrderObject.客戶)   this.OrderSource = Order_Enum.OrderSource.報價單;
            Order_Bean.setOrderSource(OrderSource);
            setOrderUI();
            setTransferOrderInfo(Order_Bean);
            setAlreadyOrderInfo(Order_Bean);
        }
    }
    @FXML protected void ChangeWaitingOrderButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(OrderObject == Order_Enum.OrderObject.廠商)    this.OrderSource = Order_Enum.OrderSource.待入倉單;
            else if(OrderObject == Order_Enum.OrderObject.客戶)   this.OrderSource = Order_Enum.OrderSource.待出貨單;
            Order_Bean.setOrderSource(OrderSource);
            setOrderUI();
            setTransferOrderInfo(Order_Bean);
            setAlreadyOrderInfo(Order_Bean);
        }
    }
    @FXML protected void ChangeAlreadyOrderButtonMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(OrderObject == Order_Enum.OrderObject.廠商)    this.OrderSource = Order_Enum.OrderSource.進貨單;
            else if(OrderObject == Order_Enum.OrderObject.客戶)   this.OrderSource = Order_Enum.OrderSource.出貨單;
            Order_Bean.setOrderSource(OrderSource);
            setOrderUI();
            setTransferOrderInfo(Order_Bean);
            setAlreadyOrderInfo(Order_Bean);
        }
    }

    @FXML protected void NoneOrderReviewStatusOnAction(){
        if(NoneOrderReviewStatus_RadioButton.isSelected()){
            boolean postLineAPI = false;
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請輸入" + OrderObject.name() + "編號");
            else{
                if(DialogUI.ConfirmDialog("※ 是否變更狀態為【" + ReviewStatus.無.name() + "】 ?",true,false,0,0)){
                    postLineAPI = modifyOrderReviewStatus(ReviewStatus.無,null);
                    if(postLineAPI) {
                        Order_Bean.getReviewStatusMap().put(getReviewObject(),ReviewStatus.無);
                        setReviewStatusRadioButtonSelect(ReviewStatus.無);
                    }
                }
            }
            if(!postLineAPI){
                if(WaitingOrderModifyStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,true);
                else if(OrderReviewFinished_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,true);
                else
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
                ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,false);
            }
        }else
            ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
    }
    @FXML protected void WaitingOrderReviewStatusOnAction() throws IOException {
        if(WaitingOrderReviewStatus_RadioButton.isSelected()){
            boolean postLineAPI = false;
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請輸入" + OrderObject.name() + "編號");
            else{
                if(DialogUI.ConfirmDialog("※ 是否變更狀態為【" + ReviewStatus.待審查.name() + "】 ?",true,false,0,0)){
                    postLineAPI = postOrderReviewStatusLineAPI(ReviewStatus.待審查,null);
                }
            }
            if(!postLineAPI || !isOrderExist()){
                if(WaitingOrderModifyStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,true);
                else if(OrderReviewFinished_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,true);
                else
                    ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
                ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,false);
                if(!isOrderExist())
                    setReviewStatusRadioButtonSelect(ReviewStatus.無);
            }
        }else
            ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
    }
    @FXML protected void WaitingOrderModifyStatusOnAction(){
        if(WaitingOrderModifyStatus_RadioButton.isSelected()){
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請輸入" + OrderObject.name() + "編號");
            else{
                if(DialogUI.ConfirmDialog("※ 是否變更狀態為【" + ReviewStatus.待修改.name() + "】 ?",true,false,0,0) &&
                    DialogUI.requestAuthorization(Authority.管理者, AuthorizedLocation.變更貨單審查狀態))
                        CallFXML.ShowEditOrderReviewStatusReason(Stage, this, ReviewStatus.待修改);
            }
            if(WaitingOrderReviewStatus_RadioButton.isSelected())
                ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
            else if(OrderReviewFinished_RadioButton.isSelected())
                ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,true);
            else
                ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,false);
        }else
            ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,true);
    }
    @FXML protected void OrderReviewFinishedOnAction() throws IOException {
        boolean postLineAPI = false;
        if(OrderReviewFinished_RadioButton.isSelected()){
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請輸入" + OrderObject.name() + "編號");
            else{
                if(DialogUI.ConfirmDialog("※ 是否變更狀態為【" + ReviewStatus.審查通過.name() + "】 ?",true,false,0,0) &&
                    DialogUI.requestAuthorization(Authority.管理者, AuthorizedLocation.變更貨單審查狀態)){
                    if(DialogUI.ConfirmDialog("是否輸入【審查通過】備註 ?",true,false,0,0))
                        CallFXML.ShowEditOrderReviewStatusReason(Stage, this, ReviewStatus.審查通過);
                    else
                        postLineAPI = postOrderReviewStatusLineAPI(ReviewStatus.審查通過,null);
                }
            }
            if(!postLineAPI){
                if(WaitingOrderReviewStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
                else if(WaitingOrderModifyStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,true);
                else
                    ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
                ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,false);
            }
        }else
            ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,true);
    }
    public boolean postOrderReviewStatusLineAPI(ReviewStatus ReviewStatus, OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean) throws IOException {
        boolean postLineAPI = modifyOrderReviewStatus(ReviewStatus, OrderReviewStatusRecord_Bean);
        if(postLineAPI){
            Order_Bean.getReviewStatusMap().put(getReviewObject(),ReviewStatus);
            setReviewStatusRadioButtonSelect(ReviewStatus);
            if(isPostLineAPISuccess(ReviewStatus,OrderReviewStatusRecord_Bean)) {
                DialogUI.MessageDialog("※ 【Line Notify】發送失敗!");
                ERPApplication.Logger.warn("※ 【Line Notify】發送失敗：" + ObjectInfo_Bean.getObjectID() + "  " + ComponentToolKit.getDatePickerValue(OrderDate_DatePicker,"yyyy-MM-dd") + "  " + OrderNumberText.getText());
            }else {
                DialogUI.MessageDialog("【Line Notify】發送成功");
                ERPApplication.Logger.info("※ 【Line Notify】發送成功：" + ObjectInfo_Bean.getObjectID() + "  " + ComponentToolKit.getDatePickerValue(OrderDate_DatePicker, "yyyy-MM-dd") + "  " + OrderNumberText.getText());

                if(getReviewObject() == ReviewObject.貨單商品 && ReviewStatus == Order_Enum.ReviewStatus.審查通過){
                    if(Order_Bean.getProductGroupMap() != null){
                        DialogUI.AlarmDialog("存在商品群組 - 審查狀態【" + Order_Bean.getReviewStatusMap().get(ReviewObject.商品群組) + "】");
                    }
                }
            }
        }
        if(!postLineAPI){
            if(ReviewStatus == Order_Enum.ReviewStatus.待審查){
                if(WaitingOrderModifyStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,true);
                else if(OrderReviewFinished_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,true);
                else
                    ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
                ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,false);
            }else if(ReviewStatus == Order_Enum.ReviewStatus.待修改){
                if(WaitingOrderReviewStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
                else if(OrderReviewFinished_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,true);
                else
                    ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
                ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,false);
            }else if(ReviewStatus == Order_Enum.ReviewStatus.審查通過){
                if(WaitingOrderReviewStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
                else if(WaitingOrderModifyStatus_RadioButton.isSelected())
                    ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,true);
                else
                    ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
                ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,false);
            }
        }
        return postLineAPI;
    }
    private boolean modifyOrderReviewStatus(ReviewStatus ReviewStatus, OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean){
        if(isOrderExist()){
            ReviewObject ReviewObject = getReviewObject();
            if(Order_Model.updateOrderReviewStatus(Order_Bean.getOrderID(), ReviewObject, ReviewStatus, OrderReviewStatusRecord_Bean))
                return true;
            else {
                DialogUI.MessageDialog("※ 審查狀態修改失敗!");
                return false;
            }
        }else
            return true;
    }
    @FXML protected void OrderReviewStatusMouseClicked(MouseEvent MouseEvent) throws IOException {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String orderReviewStatus = OrderReviewStatus_SplitMenuButton.getText();
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請輸入" + OrderObject.name() + "編號");
            else if(orderReviewStatus.equals(ReviewStatus.無.name()))
                DialogUI.MessageDialog("※ 當前無狀態");
            else{
                String confirmText;
                if(!isOrderExist())
                    confirmText = "※ 貨單未建立，是否發送當前資訊至【Line Notify】 ?";
                else
                    confirmText = "※ 是否重新發送【" + orderReviewStatus + "】狀態至【Line Notify】 ?";
                if(DialogUI.ConfirmDialog(confirmText,true,false,0,0)){
                    if(isPostLineAPISuccess(ReviewStatus.valueOf(OrderReviewStatus_SplitMenuButton.getText()),null))
                        DialogUI.MessageDialog("※ 【Line Notify】發送失敗!");
                    else
                        DialogUI.MessageDialog("【Line Notify】發送成功");
                }
            }
        }
    }
    private boolean isPostLineAPISuccess(ReviewStatus ReviewStatus, OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean) throws IOException {
        LineAPI_Bean LineAPI_Bean = CallConfig.getLineAPIProperties();
        URL restURL = new URL(LineAPI_Bean.getLineAPI());

        ToolKit.SSLAttack();
        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        PrintStream ps = new PrintStream(conn.getOutputStream(),true,"UTF-8");
        ps.print(generateApiPostJson(LineAPI_Bean,ReviewStatus,OrderReviewStatusRecord_Bean));
        ps.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String returnLine = br.readLine();
        br.close();
        if(returnLine != null && returnLine.equals("success"))
            return false;
        else {
            DialogUI.AlarmDialog("※ Line API 異常回復：" + returnLine);
            ERPApplication.Logger.warn("※ Line API 異常回復：" + returnLine);
            return true;
        }
    }
    private JSONObject generateApiPostJson(LineAPI_Bean LineAPI_Bean,ReviewStatus ReviewStatus,OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean){
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("text",generateApiPostString(ReviewStatus,OrderReviewStatusRecord_Bean));
        JSONObject.put("token",LineAPI_Bean.getToken());
        JSONObject.put("status","0");
        return JSONObject;
    }
    private String generateApiPostString(ReviewStatus ReviewStatus,OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean){
        String apiPostString = "";
        if(ReviewStatus == Order_Enum.ReviewStatus.待審查)
            apiPostString = "\uD83C\uDFF7  " + ReviewStatus.name() + "  \uD83C\uDFF7\n";
        else if(ReviewStatus == Order_Enum.ReviewStatus.待修改)
            apiPostString = "\uD83D\uDCCC  " + ReviewStatus.name() + "  \uD83D\uDCCC\n";
        else if(ReviewStatus == Order_Enum.ReviewStatus.審查通過)
            apiPostString = "\uD83D\uDCAF  " + ReviewStatus.name() + "  \uD83D\uDCAF\n";
        ReviewObject ReviewObject = getReviewObject();
        apiPostString = apiPostString +
                "來源：" + ReviewObject.name() + "\n" +
                "" + OrderSource.name() + "日期：" + ComponentToolKit.getDatePickerValue(OrderDate_DatePicker,"yyyy-MM-dd") + "\n" +
                "" + OrderSource.name() + "單號：" + OrderNumberText.getText() + "\n" +
                "" + OrderObject.name() + "編號：" + ObjectInfo_Bean.getObjectID() + "\n" +
                "" + OrderObject.name() + "名稱：" + ObjectInfo_Bean.getObjectName() + "\n" +
                "專案名稱：" + ProjectNameText.getText();
        if(OrderReviewStatusRecord_Bean != null) {
            if(ReviewStatus == Order_Enum.ReviewStatus.待修改)
                apiPostString = apiPostString + "\n\uD83D\uDCA2 待修改原因：\n" + OrderReviewStatusRecord_Bean.getRecord();
            else if(ReviewStatus == Order_Enum.ReviewStatus.審查通過)
                apiPostString = apiPostString + "\n✏️ 審查通過備註：\n" + OrderReviewStatusRecord_Bean.getRecord();
        }
        return apiPostString;
    }
    @FXML protected void ShowReviewStatusRecordMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(!isOrderExist())
                DialogUI.MessageDialog("※ 貨單未存在");
            else{
                ObservableList<OrderReviewStatusRecord_Bean> orderReviewStatusRecordList = Order_Model.getOrderReviewStatusRecord(Order_Bean.getOrderID());
                if(orderReviewStatusRecordList.size() == 0)
                    DialogUI.MessageDialog("※ 無審查紀錄");
                else
                    CallFXML.ShowOrderReviewStatusOverview(Stage, isTransferWaitingOrder(),orderReviewStatusRecordList);
            }
        }
    }
    @FXML protected void AddOrderReferenceMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(isOffsetOrder()) {
                DialogUI.MessageDialog("※ [沖帳單] 不納入貨單參考");
                return;
            }
            HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = CallConfig.getOrderReferenceJson();
            HashMap<Integer,Boolean> mainOrderReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.報價單);
            HashMap<Integer,Boolean> subBillReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單);
            if(OrderSource != Order_Enum.OrderSource.出貨子貨單) {
                if(mainOrderReferenceMap.containsKey(Order_Bean.getOrderID()))
                    DialogUI.AlarmDialog("※ 此貨單已被加入貨單參考");
                else
                    addShipmentOrderReference(mainOrderReferenceMap,subBillReferenceMap);
            }else {
                if(subBillReferenceMap.containsKey(Order_Bean.getOrderID()))
                    DialogUI.AlarmDialog("※ 此貨單已被加入貨單參考");
                else
                    addSubBillReference(mainOrderReferenceMap,subBillReferenceMap);
            }
        }
    }
    private void addShipmentOrderReference(HashMap<Integer,Boolean> mainOrderReferenceMap,HashMap<Integer,Boolean> subBillReferenceMap){
        //  如果是出貨單，要判斷是不是有子貨單被丟入參考
        boolean isMainOrderAddIntoReference = false;
        ArrayList<Order_Bean> allSubBillList = Order_Model.getSubBillOfOrder(Order_Enum.OrderObject.客戶,Order_Bean.getWaitingOrderNumber());
        if(allSubBillList != null) {
            for (Order_Bean Order_Bean : allSubBillList) {
                if (subBillReferenceMap.containsKey(Order_Bean.getOrderID())) {
                    isMainOrderAddIntoReference = true;
                    break;
                }
            }
        }
        if(isMainOrderAddIntoReference)
            DialogUI.MessageDialog("※ 母貨單已被加入貨單參考");
        else{
            mainOrderReferenceMap.put(Order_Bean.getOrderID(),true);
            if(CallConfig.setOrderReferenceJson(mainOrderReferenceMap,subBillReferenceMap))
                DialogUI.MessageDialog("※ 已加入貨單參考");
            else
                DialogUI.MessageDialog("※ 加入失敗");
        }
    }
    private void addSubBillReference(HashMap<Integer,Boolean> mainOrderReferenceMap,HashMap<Integer,Boolean> subBillReferenceMap) throws Exception {
        //  如果是出貨子貨單，要判斷是不是有母貨單被丟入參考
        Order_Bean mainOrderInfo = Order_Model.getOrderInfo(Order_Enum.OrderSource.待出貨單,ToolKit.CopyOrderBean(Order_Bean));
        if(mainOrderReferenceMap.containsKey(mainOrderInfo.getOrderID()))
            DialogUI.MessageDialog("※ 母貨單已被加入貨單參考");
        else{
            subBillReferenceMap.put(Order_Bean.getOrderID(),true);
            if(CallConfig.setOrderReferenceJson(mainOrderReferenceMap,subBillReferenceMap))
                DialogUI.MessageDialog("※ 已加入貨單參考");
            else
                DialogUI.MessageDialog("※ 加入失敗");
        }
    }
    @FXML protected void LoadOrderReferenceMouseClicked(MouseEvent MouseEvent) throws FileNotFoundException {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請先輸入客戶編號");
            else {
                coverOrderReference = true;
                HashMap<OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = CallConfig.getOrderReferenceJson();
                showOrderReference(true,orderReferenceMap);
            }
        }
    }
    @FXML protected void ShowOrderReferenceMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            HashMap<OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = Order_Model.getOrderReferenceFromDatabase(Order_Bean);
            if(orderReferenceMap == null)
                DialogUI.MessageDialog("※ 無加入任何參考貨單");
            else{
                showOrderReference(false, orderReferenceMap);
            }
        }
    }
    private void showOrderReference(boolean canModifyOrderReference,HashMap<OrderSource, HashMap<Integer, Boolean>> orderReferenceMap){
        HashMap<String,Boolean> orderProductMap = new HashMap<>();
        ObservableList<OrderProduct_Bean> orderProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        if(orderProductList != null){
            for(OrderProduct_Bean OrderProduct_Bean : orderProductList){
                orderProductMap.put(OrderProduct_Bean.getISBN(),false);
            }
        }
        OrderProduct_Bean selectOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        if(selectOrderProduct_Bean != null)
            orderProductMap.put(selectOrderProduct_Bean.getISBN(),true);
        CallFXML.ShowOrderReferenceOverview(canModifyOrderReference, Order_Bean, orderReferenceMap, ObjectInfo_Bean, orderProductMap, this);
    }
    /** CheckBox Mouse Clicked - 顯示定價 */
    @FXML protected void ShowPricingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if (ShowPricing_CheckBox.isSelected()) ComponentToolKit.setHBoxVisible(Pricing_HBox, true);
            else ComponentToolKit.setHBoxVisible(Pricing_HBox, false);
        }
    }
    /** CheckBox Mouse Clicked - 顯示利潤 */
    @FXML protected void ShowProfitOnAction(){
        showProfitHBox(ShowProfit_CheckBox.isSelected());
    }

    @FXML protected void PastePictureOnAction() throws Exception {
        if (PastePicture_RadioButton.isSelected()){
            updateDefaultUploadPictureFunction(SystemDefaultConfig_Enum.UploadPictureFunction.PastePicture);
            UploadOrderPicture(UploadPictureFunction.PastePicture);
        }
    }
    @FXML protected void UploadLocationOnAction() throws Exception {
        if(UploadLocation_RadioButton.isSelected()) {
            updateDefaultUploadPictureFunction(SystemDefaultConfig_Enum.UploadPictureFunction.UploadLocation);
            UploadOrderPicture(UploadPictureFunction.UploadLocation);
        }
    }
    private void updateDefaultUploadPictureFunction(SystemDefaultConfig_Enum.UploadPictureFunction UploadPictureFunction){
        SystemDefaultConfigJson.put(SystemDefaultConfig_Enum.UploadPictureFunction.class.getSimpleName(), UploadPictureFunction.ordinal());
        if(!CallConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.UploadPictureFunction, SystemDefaultConfigJson)){
            DialogUI.AlarmDialog("※ 【預設值】更新失敗");
        }
    }
    /** Button Mouse Clicked - 上傳圖片 */
    @FXML protected void UploadOrderPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try{
                if(PastePicture_RadioButton.isSelected()){
                    UploadOrderPicture(UploadPictureFunction.PastePicture);
                }else if(UploadLocation_RadioButton.isSelected()){
                    UploadOrderPicture(UploadPictureFunction.UploadLocation);
                }else{
                    DialogUI.MessageDialog("請選擇上傳方式");
                }
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    private void UploadOrderPicture(UploadPictureFunction UploadPictureFunction) throws Exception {
        if(UploadPictureFunction == SystemDefaultConfig_Enum.UploadPictureFunction.PastePicture){
            Clipboard cb = Clipboard.getSystemClipboard();
            ArrayList<String> imageList = getCopyImageBase64(cb);
            if(imageList != null){
                Task Task = SaveOrderPictureTask(imageList);
                SaveOrderPicture_ProgressBar.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }
        }else if(UploadPictureFunction == SystemDefaultConfig_Enum.UploadPictureFunction.UploadLocation){
            File File = ComponentToolKit.setFileChooser("載入圖片").showOpenDialog(ComponentToolKit.setStage());
            if (File != null) {
                String base64Format = ToolKit.generateBase64(File);
                Task Task = SaveOrderPictureTask(new ArrayList<String>(){{add(base64Format);}});
                SaveOrderPicture_ProgressBar.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }
        }
    }
    private ArrayList<String> getCopyImageBase64(Clipboard cb) throws Exception {
        ArrayList<String> imageList = null;
        if(cb.hasImage()){
            imageList = new ArrayList<>();
            BufferedImage image = SwingFXUtils.fromFXImage(cb.getImage(), null);
            imageList.add(ToolKit.generateBase64(ToolKit.fixImagePickBackgroundColor(image)));
        }else if(cb.hasFiles()) {
            ArrayList<String> errorFileExtensionList = null;
            List<File> copyFile = cb.getFiles();
            for(File file : copyFile){
                if(!file.getName().contains(".jpg") &&
                    !file.getName().contains(".png") &&
                    !file.getName().contains(".jpg")){
                    if(errorFileExtensionList == null){
                        errorFileExtensionList = new ArrayList<>();
                    }
                    errorFileExtensionList.add(file.getName());
                }
            }
            if(errorFileExtensionList != null){
                StringBuilder text = new StringBuilder("只允許【jpg、jpeg、png】圖片格式，錯誤檔名：");
                for(File file : copyFile){
                    text.append("\n").append(file.getName());
                }
                DialogUI.AlarmDialog(text.toString());
                return null;
            }
            imageList = new ArrayList<>();
            for(File file : copyFile){
                imageList.add(ToolKit.generateBase64(file));
            }
        }else{
            DialogUI.MessageDialog("未複製圖片");
        }
        return imageList;
    }
    private Task SaveOrderPictureTask(ArrayList<String> imageList){
        return new Task() {
            @Override public Void call(){
                try{
                    UploadOrderPicture_SplitMenuButton.setDisable(true);
                    SnapshotOrderPicture_Button.setDisable(true);
                    setShowOrderPictureButtonDisable(true);
                    SaveOrderPicture_ProgressBar.setVisible(true);
                    for(String base64Format : imageList){
                        if(saveSnapshotOrUploadPicture(base64Format))
                            Platform.runLater(() -> DialogUI.MessageDialog("※ 圖片上傳成功"));
                        else
                            Platform.runLater(() -> DialogUI.MessageDialog("※ 圖片上傳失敗"));
                    }
                    imageList.clear();
                    UploadOrderPicture_SplitMenuButton.setDisable(false);
                    SnapshotOrderPicture_Button.setDisable(false);
                    setShowOrderPictureButtonDisable(false);
                    SaveOrderPicture_ProgressBar.setVisible(false);
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
                return null;
            }
        };
    }
    @FXML protected void SnapshotOrderPictureMouseClicked(MouseEvent MouseEvent){
        if (KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowSnapshotOrderPicture(Stage,SnapshotPictureLocation.貨單, this);
        }
    }
    public boolean saveSnapshotOrUploadPicture(String base64Format){
        return Order_Model.insertOrderPicture(Order_Bean.getOrderID(), base64Format, Order_Bean.getOrderSource());
    }
    /** Button Mouse Clicked - 顯示圖片 */
    @FXML protected void ShowOrderPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ArrayList<HashMap<Boolean,String>> OrderPictureList = Order_Model.getOrderPicture(Order_Bean.getOrderID(), OrderSource);
            if(OrderPictureList == null)    DialogUI.MessageDialog("※ 無貨單圖片!");
            else    CallFXML.ShowPicture(Stage, Order_Bean, OrderPictureList,OrderSource,this);
        }
    }
    public void setShowOrderPictureButtonDisable(boolean disable){
        ComponentToolKit.setButtonDisable(ShowOrderPicture_Button, disable);
    }
    /** DatePicker On Action - 日期 */
    @FXML protected void OrderDateOnAction(){
        if(OrderSource != Order_Enum.OrderSource.進貨子貨單 && OrderSource != Order_Enum.OrderSource.出貨子貨單){
            String OrderDate = ComponentToolKit.getDatePickerValue(OrderDate_DatePicker, "yyyy-MM-dd");
            OrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, OrderDate,false));
        }
    }
    @FXML protected void ObjectIDKeyPressed(KeyEvent KeyEvent){
        if(isAllowModifyProduct(ObjectIDText,true)){
            String ObjectID = ObjectIDText.getText();
            if(KeyPressed.isEnterKeyPressed(KeyEvent) && !ObjectID.equals("")){
                searchOrderObject(OrderObject == Order_Enum.OrderObject.廠商 ? Order_Enum.ObjectSearchColumn.廠商編號 : Order_Enum.ObjectSearchColumn.客戶編號, ObjectID);
            }else if(KeyPressed.isF8KeyPressed(KeyEvent) && this.ObjectInfo_Bean != null){
                if(OrderObject == Order_Enum.OrderObject.廠商)
                    CallFXML.ManageManufacturerInfo_NewStage(Stage, this.ObjectInfo_Bean);
                else if(OrderObject == Order_Enum.OrderObject.客戶)
                    CallFXML.ManageCustomerInfo_NewStage(Stage, this.ObjectInfo_Bean);
            }
        }
    }
    @FXML protected void ObjectNameKeyPressed(KeyEvent KeyEvent){
        if(isAllowModifyProduct(ObjectNameText,true)) {
            String ObjectName = ObjectNameText.getText();
            if (KeyPressed.isEnterKeyPressed(KeyEvent) && !ObjectName.equals("")) {
                searchOrderObject(OrderObject == Order_Enum.OrderObject.廠商 ? Order_Enum.ObjectSearchColumn.廠商名稱 : Order_Enum.ObjectSearchColumn.客戶名稱, ObjectName);
            }else if(KeyPressed.isF8KeyPressed(KeyEvent) && this.ObjectInfo_Bean != null){
                if(OrderObject == Order_Enum.OrderObject.廠商)
                    CallFXML.ManageManufacturerInfo_NewStage(Stage, this.ObjectInfo_Bean);
                else if(OrderObject == Order_Enum.OrderObject.客戶)
                    CallFXML.ManageCustomerInfo_NewStage(Stage, this.ObjectInfo_Bean);
            }
        }
    }
    private void searchOrderObject(ObjectSearchColumn ObjectSearchColumn, String searchText){
        ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject, ObjectSearchColumn, searchText);
        if(ObjectList.size() == 1){
            setOrderObjectInfo(ObjectList.get(0));
            ISBNText.requestFocus();
        }else if(ObjectList.size() > 1)   CallFXML.ShowObject(Stage, OrderObject, ObjectList,true,ShowObjectSource.建立貨單,this);
        else{
            ObjectIDText.setText(this.ObjectInfo_Bean == null ? "" : this.ObjectInfo_Bean.getObjectID());
            ObjectNameText.setText(this.ObjectInfo_Bean == null ? "" : this.ObjectInfo_Bean.getObjectName());
            if(OrderObject == Order_Enum.OrderObject.客戶) {
                this.Order_Bean.setRecipientName(this.ObjectInfo_Bean == null ? "" : ObjectInfo_Bean.getContactPerson());
                this.Order_Bean.setRecipientAddress(this.ObjectInfo_Bean == null ? "" : ObjectInfo_Bean.getDeliveryAddress());
                this.Order_Bean.setRecipientTelephone(this.ObjectInfo_Bean == null ? "" : ObjectInfo_Bean.getTelephone1());
                this.Order_Bean.setRecipientCellphone(this.ObjectInfo_Bean == null ? "" : ObjectInfo_Bean.getCellPhone());
            }
            OrderTaxStatus = Order_Enum.OrderTaxStatus.未稅;
            DialogUI.MessageDialog("※ 查無相關" + OrderObject.name());
        }
    }
    public void setOrderObjectInfo(ObjectInfo_Bean ObjectInfo_Bean){
        if(isTransferWaitingOrder() || isTransferAlreadyOrder() ||
                (OrderSource == Order_Enum.OrderSource.進貨退貨單 && isOrderExist()) ||
                (OrderSource == Order_Enum.OrderSource.出貨退貨單 && isOrderExist())){
            if(!DialogUI.requestAuthorization(Authority.用戶, AuthorizedLocation.變更貨單對象)){
                ObjectIDText.setText(this.ObjectInfo_Bean.getObjectID());
                ObjectNameText.setText(this.ObjectInfo_Bean.getObjectName());
                return;
            }
        }
        this.ObjectInfo_Bean = ObjectInfo_Bean;
        ObjectIDText.setText(ObjectInfo_Bean.getObjectID());
        ObjectNameText.setText(ObjectInfo_Bean.getObjectName());
        if(OrderObject == Order_Enum.OrderObject.客戶) {
            this.Order_Bean.setRecipientName(ObjectInfo_Bean.getContactPerson());
            this.Order_Bean.setRecipientAddress(ObjectInfo_Bean.getDeliveryAddress());
            this.Order_Bean.setRecipientTelephone(ObjectInfo_Bean.getTelephone1());
            this.Order_Bean.setRecipientCellphone(ObjectInfo_Bean.getCellPhone());
        }
        OrderTaxStatus = Order_Enum.OrderTaxStatus.values()[ObjectInfo_Bean.getOrderTax().ordinal()];
    }
    /** CheckBox On Action - 借測單 */
    @FXML protected void OrderBorrowedCheckBoxOnAction(){   if(OrderBorrowed_CheckBox.isSelected()) OffsetOrder_CheckBox.setSelected(false);    }
    /** TextField Key Released - 主碼 */
    @FXML protected void ISBNKeyReleased(KeyEvent KeyEvent){
        HotKey(KeyEvent,true);
    }
    @FXML protected void ISBNKeyPressed(KeyEvent KeyEvent) throws Exception {
        if(isProductGroupModel()) {
            DialogUI.MessageDialog("※ 請切換回商品模式");
            return;
        }
        if(isAllowModifyProduct(ISBNText,false)){
            if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.報價單 || (OrderSource == Order_Enum.OrderSource.出貨子貨單 && !isOrderExist())||
                    (OrderSource == Order_Enum.OrderSource.進貨退貨單 && !isOrderExist()) ||
                    (OrderSource == Order_Enum.OrderSource.出貨退貨單 && !isOrderExist())){
                if(KeyPressed.isEnterKeyPressed(KeyEvent) || KeyPressed.isF2KeyPressed(KeyEvent) || KeyPressed.isF6KeyPressed(KeyEvent)){
                    String ObjectID = ObjectIDText.getText();
                    String ISBN = ISBNText.getText();
                    if(ObjectIDText.getText().equals("") && ObjectNameText.getText().equals(""))    DialogUI.MessageDialog("※ 請輸入" + OrderObject.name()+ "編號");
                    else if(KeyPressed.isEnterKeyPressed(KeyEvent) && !ISBN.equals("")){
                        if(ISBN.length() == 13) SearchProductISBN(ObjectID, ISBN);
                        else   SearchProductTag(ObjectID, ISBN);
                    }else if(KeyPressed.isF2KeyPressed(KeyEvent)){
                        OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
                        if(OrderProduct_Bean == null)    DialogUI.MessageDialog("※ 未選擇複製商品");
                        else{
                            copyOrderProduct();
                            UpdatePriceByProduct();
                        }
                    }else if(KeyPressed.isF6KeyPressed(KeyEvent) && !ISBN.equals(""))  SearchProductISBN(ObjectID, ISBN);
                }else if(KeyPressed.isF1KeyPressed(KeyEvent)){
                    CallFXML.ManageProductInfo_NewStage(Product_Enum.ManageProductStatus.建立, Stage, null);
                }
            }
        }
    }
    private void SearchProductISBN(String ObjectID, String ISBN){
        ObservableList<OrderProduct_Bean> ProductList = Order_Model.getProduct(OrderObject, Order_Enum.ProductSearchColumn.ISBN, ObjectID, ISBN);
        if(ProductList.size() == 1) setSelectProductBean(true, ProductList);
        else if(ProductList.size() > 1) CallFXML.ShowProduct(Stage, OrderObject, ProductList,this);
        else   DialogUI.MessageDialog("※ 查無相關主碼");
    }
    private void SearchProductTag(String ObjectID, String ISBN){
        ObservableList<OrderProduct_Bean> ProductList = Order_Model.getProduct(OrderObject, Order_Enum.ProductSearchColumn.標籤, ObjectID, ISBN);
        if(ProductList.size() != 0) CallFXML.ShowProduct(Stage, OrderObject, ProductList,this);
        else   DialogUI.MessageDialog("※ 查無相關標籤");
    }
    private void copyOrderProduct() throws Exception {
        OrderProduct_Bean OrderProduct_Bean = ToolKit.CopyProductBean(ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView));
        OrderProduct_Bean.setItemNumber(ComponentToolKit.getTableViewItemsSize(MainTableView) + 1);
        ComponentToolKit.getOrderProductTableViewItemList(MainTableView).add(OrderProduct_Bean);
    }
    /** TextField Key Released - 品名 */
    @FXML protected void ProductNameKeyReleased(KeyEvent KeyEvent){
        if(isProductGroupModel()) {
            DialogUI.MessageDialog("※ 請切換回商品模式");
            return;
        }
        HotKey(KeyEvent,false);
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(isAllowModifyProduct(ProductNameText,true)){
                if(ObjectIDText.getText().equals("") && ObjectNameText.getText().equals(""))
                    DialogUI.MessageDialog("※ 請輸入" + OrderObject.name() + "編號");
                else{
                    ObservableList<OrderProduct_Bean> ProductList = Order_Model.getProduct(OrderObject, Order_Enum.ProductSearchColumn.品名, ObjectIDText.getText(), ProductNameText.getText());
                    if(ProductList.size() != 0) CallFXML.ShowProduct(Stage, OrderObject, ProductList,this);
                    else   DialogUI.MessageDialog("※ 查無相關品名");
                }
            }
        }
    }
    @FXML protected void PricePercentageKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            calculateSinglePriceByPricePercentage();
    }
    @FXML protected void PricePercentageMouseClicked(){
            calculateSinglePriceByPricePercentage();
    }
    private void calculateSinglePriceByPricePercentage(){
        SinglePriceText.setText(ToolKit.RoundingString(true, Double.parseDouble(BatchPriceText.getText()) * PricePercentageSpinner.getValue()));
        OrderProduct_Bean orderProduct_bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        if(orderProduct_bean != null){
            ExportQuotationFormat exportQuotationFormat = isTwoFormatOrder() ? ExportQuotationFormat.二聯 : ExportQuotationFormat.三聯;
            refreshModifyBatchPriceOrSinglePrice(false, exportQuotationFormat, orderProduct_bean, SinglePriceText.getText());
        }
    }
    /** TextField Key Pressed - 成本 */
    @FXML protected void BatchPriceKeyReleased(KeyEvent KeyEvent){
        HotKey(KeyEvent,false);
    }
    @FXML protected void BatchPriceKeyPressed(KeyEvent KeyEvent){
        if(isProductGroupModel()) {
            DialogUI.MessageDialog("※ 請切換回商品模式");
            return;
        }
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            OrderProduct_Bean orderProduct_bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(orderProduct_bean != null){
                boolean isOffSetSubBill = (OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) && isOffsetOrder();
                if((isTransferWaitingOrder() || isTransferAlreadyOrder())){
                    if(!isOffSetSubBill  && !DialogUI.requestAuthorization(Authority.用戶, AuthorizedLocation.變更貨單金額)){
                        BatchPriceText.setText(ToolKit.RoundingString(true,orderProduct_bean.getBatchPrice()));
                        return;
                    }
                }
                String batchPrice;
                ExportQuotationFormat exportQuotationFormat = isTwoFormatOrder() ? ExportQuotationFormat.二聯 : ExportQuotationFormat.三聯;
                if(exportQuotationFormat == ExportQuotationFormat.二聯){
                    batchPrice = ToolKit.RoundingString(orderProduct_bean.getBatchPrice()*1.05);
                }else{
                    batchPrice = ToolKit.RoundingString(true, orderProduct_bean.getBatchPrice());
                }
                if(DialogUI.ConfirmDialog("※ 確定變更「" + exportQuotationFormat.name() + "」成本 " + batchPrice + " → " + BatchPriceText.getText() + " ?",true,false,0,0)){
                    refreshModifyBatchPriceOrSinglePrice(true, exportQuotationFormat, orderProduct_bean, BatchPriceText.getText());
                }else {
                    BatchPriceText.setText(ToolKit.RoundingString(true, orderProduct_bean.getBatchPrice()));
                }
                /*if(isTwoFormatOrder()){
                    batchPrice = ToolKit.RoundingString(OrderProduct_Bean.getBatchPrice()*1.05);
                    singlePrice = ToolKit.RoundingString(OrderProduct_Bean.getSinglePrice()*1.05);
                    pricing = ToolKit.RoundingString(OrderProduct_Bean.getPricing()*1.05);

                    priceAmount = ToolKit.RoundingString(OrderProduct_Bean.getQuantity()*ToolKit.RoundingDouble(singlePrice));
                }*/

                /*if(DialogUI.ConfirmDialog("※ 確定變更成本 " + ToolKit.RoundingString(true, orderProduct_bean.getBatchPrice()) +" → " + BatchPriceText.getText() + " ?",true,false,0,0)){
                    refreshModifyBatchPriceOrSinglePrice(true, orderProduct_bean, BatchPriceText.getText());
                }else
                    BatchPriceText.setText(ToolKit.RoundingString(true,orderProduct_bean.getBatchPrice()));*/
            }
        }
    }
    /** TextField Key Pressed - 單價 */
    @FXML protected void SinglePriceKeyReleased(KeyEvent KeyEvent){
        HotKey(KeyEvent,false);
    }
    @FXML protected void SinglePriceKeyPressed(KeyEvent KeyEvent){
        if(isProductGroupModel()) {
            DialogUI.MessageDialog("※ 請切換回商品模式");
            return;
        }
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(!SinglePriceText.isEditable())   return;
            OrderProduct_Bean orderProduct_bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(orderProduct_bean != null){
                boolean isOffSetSubBill = (OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) && isOffsetOrder();
                if((isTransferWaitingOrder() || isTransferAlreadyOrder())){
                    if(!isOffSetSubBill  && !DialogUI.requestAuthorization(Authority.用戶, AuthorizedLocation.變更貨單金額)){
                        SinglePriceText.setText(ToolKit.RoundingString(true,orderProduct_bean.getSinglePrice()));
                        return;
                    }
                }
                String singlePrice;
                ExportQuotationFormat exportQuotationFormat = isTwoFormatOrder() ? ExportQuotationFormat.二聯 : ExportQuotationFormat.三聯;
                if(exportQuotationFormat == ExportQuotationFormat.二聯){
                    singlePrice = ToolKit.RoundingString(orderProduct_bean.getSinglePrice()*1.05);
                }else{
                    singlePrice = ToolKit.RoundingString(true,orderProduct_bean.getSinglePrice());
                }
                if(DialogUI.ConfirmDialog("※ 確定變更「" + exportQuotationFormat.name() + "」單價 " + singlePrice + " → " + SinglePriceText.getText() + " ?",true,false,0,0)){
                    refreshModifyBatchPriceOrSinglePrice(false, exportQuotationFormat, orderProduct_bean, SinglePriceText.getText());
                }else {
                    SinglePriceText.setText(ToolKit.RoundingString(true, orderProduct_bean.getSinglePrice()));
                }
                /*if(isTwoFormatOrder()){
                    batchPrice = ToolKit.RoundingString(OrderProduct_Bean.getBatchPrice()*1.05);
                    singlePrice = ToolKit.RoundingString(OrderProduct_Bean.getSinglePrice()*1.05);
                    pricing = ToolKit.RoundingString(OrderProduct_Bean.getPricing()*1.05);

                    priceAmount = ToolKit.RoundingString(OrderProduct_Bean.getQuantity()*ToolKit.RoundingDouble(singlePrice));
                }*/
                /*
                if(DialogUI.ConfirmDialog("※ 確定變更單價 " + ToolKit.RoundingString(true,orderProduct_bean.getSinglePrice()) + " → " + SinglePriceText.getText() + " ?",true,false,0,0)){
                    refreshModifyBatchPriceOrSinglePrice(false, orderProduct_bean, SinglePriceText.getText());
                }else
                    SinglePriceText.setText(ToolKit.RoundingString(true,orderProduct_bean.getSinglePrice()));*/
            }
        }
    }
    private void refreshModifyBatchPriceOrSinglePrice(boolean isBatchPrice, ExportQuotationFormat exportQuotationFormat, OrderProduct_Bean orderProduct_bean, String price){
        if(isBatchPrice){
            double batchPrice = Double.parseDouble(price);
            if(exportQuotationFormat == ExportQuotationFormat.二聯){
                batchPrice = ToolKit.RoundingDouble(batchPrice/1.05);
            }
            orderProduct_bean.setBatchPrice(batchPrice);
            if(OrderObject == Order_Enum.OrderObject.廠商)
                orderProduct_bean.setPriceAmount(ToolKit.RoundingInteger(batchPrice * orderProduct_bean.getQuantity()));
        }else{
            double singlePrice = Double.parseDouble(price);
            if(exportQuotationFormat == ExportQuotationFormat.二聯){
                singlePrice = ToolKit.RoundingDouble(singlePrice/1.05);
            }
            orderProduct_bean.setSinglePrice(singlePrice);
            if(OrderObject == Order_Enum.OrderObject.客戶)
                orderProduct_bean.setPriceAmount(ToolKit.RoundingInteger(singlePrice) * orderProduct_bean.getQuantity());
        }
        UpdatePriceByProduct();
        calculatePricePercentage();
    }
    @FXML protected void SinglePriceMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            SinglePriceText.selectAll();
    }
    /** TextField Key Released - 數量 */
    @FXML protected void QuantityKeyReleased(KeyEvent KeyEvent){
        if(isProductGroupModel()) {
            DialogUI.MessageDialog("※ 請切換回商品模式");
            return;
        }
        moveOrderItems(KeyEvent,ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView), ComponentToolKit.getOrderProductTableViewSelectIndex(MainTableView));
        HotKey(KeyEvent,true);
        if(!KeyPressed.isF8KeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent) && isAllowModifyProduct(QuantityText,true)){
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(OrderProduct_Bean != null){
                String Quantity = QuantityText.getText();
                if(Quantity.equals("") || !isProductQuantityEnoughGroup(OrderProduct_Bean))
                    QuantityText.setText(String.valueOf(OrderProduct_Bean.getQuantity()));
                else{
                    OrderProduct_Bean.setQuantity(Integer.parseInt(Quantity));
                    if(OrderObject == Order_Enum.OrderObject.廠商)    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()* OrderProduct_Bean.getBatchPrice()));
                    else if(OrderObject == Order_Enum.OrderObject.客戶)   OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()* OrderProduct_Bean.getSinglePrice()));
                    PriceAmountText.setText(ToolKit.RoundingString(OrderProduct_Bean.getPriceAmount()));
                    UpdatePriceByProduct();
                }
            }
        }
    }
    private boolean isProductQuantityEnoughGroup(OrderProduct_Bean OrderProduct_Bean){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
        if(OrderProduct_Bean.getItem_id() == null || productGroupMap == null)
            return true;
        int productInGroupQuantity = 0;
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            LinkedHashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
            for(Integer item_id : itemMap.keySet()){
                ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                if(OrderProduct_Bean.getItem_id().equals(item_id))
                    productInGroupQuantity = productInGroupQuantity + ItemGroup_Bean.getItem_quantity()*ProductGroup_Bean.getQuantity();
            }
        }
        String Quantity = QuantityText.getText();
        if(ToolKit.RoundingInteger(Quantity) < productInGroupQuantity){
            DialogUI.AlarmDialog("※ 商品數量【" + productInGroupQuantity + "】已被分配群組");
            return false;
        }
        return true;
    }
    private void moveOrderItems(KeyEvent KeyEvent, OrderProduct_Bean OrderProduct_Bean, int nowIndex){
        if(OrderProduct_Bean == null || isTransferWaitingOrder())    return;
        if(previousMoveKeyCombine.match(KeyEvent)) {
            if(nowIndex != 0){
                ComponentToolKit.getOrderProductTableViewItemList(MainTableView).remove(nowIndex);
                OrderProduct_Bean previousOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView,nowIndex-1);
                previousOrderProduct_Bean.setItemNumber(previousOrderProduct_Bean.getItemNumber()+1);

                OrderProduct_Bean.setItemNumber(OrderProduct_Bean.getItemNumber()-1);
                ComponentToolKit.getOrderProductTableViewItemList(MainTableView).add(nowIndex - 1, OrderProduct_Bean);
                MainTableView.getSelectionModel().select(nowIndex);
            }
        }else if(nextMoveKeyCombine.match(KeyEvent)){
            if(nowIndex != ComponentToolKit.getOrderProductTableViewItemList(MainTableView).size()-1){
                ComponentToolKit.getOrderProductTableViewItemList(MainTableView).remove(nowIndex);
                OrderProduct_Bean nextOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView,nowIndex);
                nextOrderProduct_Bean.setItemNumber(nextOrderProduct_Bean.getItemNumber()-1);

                OrderProduct_Bean.setItemNumber(OrderProduct_Bean.getItemNumber()+1);
                ComponentToolKit.getOrderProductTableViewItemList(MainTableView).add(nowIndex + 1, OrderProduct_Bean);
                MainTableView.getSelectionModel().select(nowIndex);
            }
        }
    }
    private void HotKey(KeyEvent KeyEvent,boolean isAllowF8){
        if(KeyPressed.isDirectionKeyPressed(KeyEvent))  TableViewProductMove(KeyEvent);
        else if(KeyPressed.isF3KeyPressed(KeyEvent))  ISBNText.requestFocus();
        else if(KeyPressed.isF4KeyPressed(KeyEvent))  BatchPriceText.requestFocus();
        else if(KeyPressed.isF5KeyPressed(KeyEvent))  SinglePriceText.requestFocus();
        else if(isAllowF8 && KeyPressed.isF8KeyPressed(KeyEvent)) {
            OrderProduct_Bean selectOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(selectOrderProduct_Bean != null){
                ObservableList<OrderProduct_Bean> productList = Order_Model.getProduct(OrderObject, Order_Enum.ProductSearchColumn.ISBN, ObjectIDText.getText(), selectOrderProduct_Bean.getISBN());
                OrderProduct_Bean storeOrderProduct_Bean = productList.get(0);
                storeOrderProduct_Bean.setProductTag(Order_Model.getItemsProductTag(selectOrderProduct_Bean));
                CallFXML.ShowModifyProduct(Stage, OrderObject, this.ObjectInfo_Bean, selectOrderProduct_Bean, storeOrderProduct_Bean,this);
            }
        }else if(isAllowModifyProduct(ISBNText,false) && ProductConnectionKeyCombine.match(KeyEvent)){
            if(OrderObject == Order_Enum.OrderObject.客戶){
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
                if(OrderProduct_Bean != null){
                    openProductConnection(Order_Bean.getOrderID(), OrderProduct_Bean.getISBN());
                }else{
                    DialogUI.MessageDialog("※ 請選擇商品");
                }
            }else{  //  如果採購單要此功能：金額要變動
                DialogUI.MessageDialog("※ 採購單無此功能【商品群組關聯】");
            }
        }
    }
    private void openProductConnection(Integer order_id, String ISBN){
        ArrayList<ProductGroup_Bean> productGroupList = Order_Model.fineProductConnectionFromGroup(order_id, ISBN);
        if(productGroupList == null){
            DialogUI.MessageDialog("※ 查無任何群組關聯");
        }else {
            String objectID = ObjectIDText.getText();
            CallFXML.ShowProductConnectionFromGroup(Stage, this, ISBN, objectID, productGroupList);
        }
    }
    private void TableViewProductMove(KeyEvent KeyEvent){
        int SelectIndex = ComponentToolKit.getOrderProductTableViewSelectIndex(MainTableView);
        OrderProduct_Bean OrderProduct_Bean = null;
        if(KeyPressed.isUpKeyPressed(KeyEvent)){
            MainTableView.getSelectionModel().select(SelectIndex - 1);
            OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        }else if(KeyPressed.isDownKeyPressed(KeyEvent)){
            MainTableView.getSelectionModel().select(SelectIndex + 1);
            OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        }

        if(KeyPressed.isDownKeyPressed(KeyEvent) && ComponentToolKit.getOrderProductTableViewSelectIndex(MainTableView) == SelectIndex){
            if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.報價單 || (OrderSource == Order_Enum.OrderSource.出貨子貨單 && !isOrderExist())) {
                ProductNameText.requestFocus();
                initialProductInfoComponent(false);
            }
        }else if(OrderProduct_Bean != null){
            setProductInfoByTableView(OrderProduct_Bean,true);
            UpdatePriceByProduct();
            QuantityText.selectAll();
        }
    }
    public void refreshModifyProductInfoInTableView(OrderProduct_Bean OrderProduct_Bean,
                                                    CustomerSaleModel customerSaleModel,
                                                    HashMap<ManageProductInfoTableColumn,Boolean> modifyInfoMap){
        boolean isModifyProductName = modifyInfoMap.get(ManageProductInfoTableColumn.品名);
        boolean isModifyBatchPrice = modifyInfoMap.get(ManageProductInfoTableColumn.成本價);
        boolean isModifySinglePrice = modifyInfoMap.get(ManageProductInfoTableColumn.單價);
        boolean isModifyPricing = modifyInfoMap.get(ManageProductInfoTableColumn.市價);

        OrderProduct_Bean selectOrderProductBean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        if(isModifyProductName)
            selectOrderProductBean.setProductName(OrderProduct_Bean.getProductName());
        selectOrderProductBean.setInternationalCode(OrderProduct_Bean.getInternationalCode());
        selectOrderProductBean.setFirmCode(OrderProduct_Bean.getFirmCode());
        if(isModifyBatchPrice)
            selectOrderProductBean.setBatchPrice(OrderProduct_Bean.getBatchPrice());
        if(isModifySinglePrice)
            selectOrderProductBean.setSinglePrice(OrderProduct_Bean.getSinglePrice());
        if(isModifyPricing)
            selectOrderProductBean.setPricing(OrderProduct_Bean.getPricing());
        selectOrderProductBean.setVipPrice1(OrderProduct_Bean.getVipPrice1());
        selectOrderProductBean.setVipPrice2(OrderProduct_Bean.getVipPrice2());
        selectOrderProductBean.setVipPrice3(OrderProduct_Bean.getVipPrice3());

        ProductNameText.setText(selectOrderProductBean.getProductName());
        InternationalCodeText.setText(selectOrderProductBean.getInternationalCode());
        FirmCodeText.setText(selectOrderProductBean.getFirmCode());
        boolean refreshPriceAmount = false;
        if(this.OrderObject == Order_Enum.OrderObject.廠商 && isModifyBatchPrice) {
            selectOrderProductBean.setPriceAmount(ToolKit.RoundingInteger(selectOrderProductBean.getQuantity() * selectOrderProductBean.getBatchPrice()));
            BatchPriceText.setText(ToolKit.RoundingString(true,selectOrderProductBean.getBatchPrice()));
            refreshPriceAmount = true;
        }else if(this.OrderObject == Order_Enum.OrderObject.客戶) {
            if(customerSaleModel == CustomerSaleModel.成本價 && isModifyBatchPrice){
                selectOrderProductBean.setSinglePrice(selectOrderProductBean.getBatchPrice());
                refreshPriceAmount = true;
            }else if(customerSaleModel == CustomerSaleModel.單價 && isModifySinglePrice){
                selectOrderProductBean.setSinglePrice(selectOrderProductBean.getSinglePrice());
                refreshPriceAmount = true;
            }else if(customerSaleModel == CustomerSaleModel.定價 && isModifyPricing) {
                selectOrderProductBean.setSinglePrice(selectOrderProductBean.getPricing());
                refreshPriceAmount = true;
            }else if(customerSaleModel == CustomerSaleModel.VipPrice1){
                selectOrderProductBean.setSinglePrice(selectOrderProductBean.getVipPrice1());
                refreshPriceAmount = true;
            }else if(customerSaleModel == CustomerSaleModel.VipPrice2){
                selectOrderProductBean.setSinglePrice(selectOrderProductBean.getVipPrice2());
                refreshPriceAmount = true;
            }else if(customerSaleModel == CustomerSaleModel.VipPrice3){
                selectOrderProductBean.setSinglePrice(selectOrderProductBean.getVipPrice3());
                refreshPriceAmount = true;
            }
            if(refreshPriceAmount) {
                selectOrderProductBean.setPriceAmount(ToolKit.RoundingInteger(selectOrderProductBean.getQuantity() * selectOrderProductBean.getSinglePrice()));
                SinglePriceText.setText(ToolKit.RoundingString(true, selectOrderProductBean.getSinglePrice()));
            }
        }
        MainTableView.refresh();
        if(refreshPriceAmount) {
            PriceAmountText.setText(ToolKit.RoundingString(selectOrderProductBean.getPriceAmount()));
            UpdatePriceByProduct();
        }
        modifyInfoMap.clear();
    }
    private boolean isAllowModifyProduct(TextField TextField, boolean showMessageDialog){
        if(!TextField.isEditable()){
            if(showMessageDialog)
                DialogUI.MessageDialog("※ 不允許變更");
            return false;
        }
        return true;
    }
    @FXML protected void QuantityMouseClicked(MouseEvent MouseEvent) {
        if (KeyPressed.isMouseLeftClicked(MouseEvent))
            QuantityText.selectAll();
    }
    /** TextField Key Pressed - 稅額 */
    @FXML protected void TaxKeyReleased(KeyEvent KeyEvent){
        if(isTwoFormatOrder() &&
                (KeyPressed.isF7KeyPressed(KeyEvent) || KeyPressed.isF8KeyPressed(KeyEvent))){
            DialogUI.AlarmDialog("目前為【二聯】狀態，請切換至【三聯】");
        }
        if(ComponentToolKit.isTextFieldEditable(TaxText)) {
            if (KeyPressed.isF7KeyPressed(KeyEvent)) {
                OrderTaxStatus = Order_Enum.OrderTaxStatus.未稅;
                ProjectTaxStatus = Order_Enum.OrderTaxStatus.未稅;
            }else if (KeyPressed.isF8KeyPressed(KeyEvent)){
                OrderTaxStatus = Order_Enum.OrderTaxStatus.含稅;
                ProjectTaxStatus = Order_Enum.OrderTaxStatus.含稅;
            }
            CalculateOrderTotalPrice_Tax_Discount_Profit();
            refreshProductGroupDifferencePrice();
            CalculateProjectTax_TotalPrice();
        }
    }
    /** TextField Key Released - 折讓 */
    @FXML protected void DiscountKeyReleased(){
        String Discount = DiscountText.getText();
        if(ToolKit.isDigital(Discount)) CalculateOrderTotalPrice_Tax_Discount_Profit();
        else if(Discount.equals("")) DiscountText.setText("0");
        else{
            DiscountText.setText("0");
            DialogUI.MessageDialog("※ 折讓格式錯誤");
        }
    }
    /** TextField Key Released - 備註 */
    @FXML protected void RemarkKeyReleased(){
        OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        if(OrderProduct_Bean != null)    OrderProduct_Bean.setRemark(RemarkText.getText());
        else{
            RemarkText.setText("");
            DialogUI.MessageDialog("※ 請選擇商品");
        }
    }
    /** Add new product into order
     * @param isOpenProductConnection whether open product connection UI
     * @param SelectProductList list of selected products from show product
     * */
    public void setSelectProductBean(boolean isOpenProductConnection, ObservableList<OrderProduct_Bean> SelectProductList){
        for(OrderProduct_Bean OrderProduct_Bean : SelectProductList){
            Order_Model.refreshProductWannaAmount(OrderObject, ObjectInfo_Bean, OrderProduct_Bean);
            if(!ProductExist(OrderProduct_Bean)){
                OrderProduct_Bean.setItemNumber(ComponentToolKit.getTableViewItemsSize(MainTableView) + 1);
                ComponentToolKit.getOrderProductTableViewItemList(MainTableView).add(OrderProduct_Bean);
            }
        }
        UpdatePriceByProduct();
        NumberOfItemsText.setText(String.valueOf(ComponentToolKit.getTableViewItemsSize(MainTableView)));
        if(isOpenProductConnection){
            if(defaultProductConnection_toggleSwitch != null && defaultProductConnection_toggleSwitch.switchedOnProperty().get()) {
                for(OrderProduct_Bean OrderProduct_Bean : SelectProductList){
                    openProductConnection(Order_Bean.getOrderID(), OrderProduct_Bean.getISBN());
                }
            }
        }
    }
    private boolean ProductExist(OrderProduct_Bean ProductSelect_Bean){
        boolean ProductExist;
        OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        if(OrderProduct_Bean != null && OrderProduct_Bean.getISBN().equals(ProductSelect_Bean.getISBN())){
            addSelectProductQuantity(OrderProduct_Bean);
            ProductExist = true;
        }else   ProductExist = addExistProductQuantity(ProductSelect_Bean);
        return ProductExist;
    }
    private void addSelectProductQuantity(OrderProduct_Bean OrderProduct_Bean){
        OrderProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity()+1);
        if(OrderObject == Order_Enum.OrderObject.廠商)    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()* OrderProduct_Bean.getBatchPrice()));
        else if(OrderObject == Order_Enum.OrderObject.客戶)   OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()* OrderProduct_Bean.getSinglePrice()));
    }
    private boolean addExistProductQuantity(OrderProduct_Bean ProductSelect_Bean){
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(OrderProduct_Bean.getISBN().equals(ProductSelect_Bean.getISBN())){
                OrderProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity()+1);
                if(OrderObject == Order_Enum.OrderObject.廠商)    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()* OrderProduct_Bean.getBatchPrice()));
                else if(OrderObject == Order_Enum.OrderObject.客戶)   OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()* OrderProduct_Bean.getSinglePrice()));
                return true;
            }
        }
        return false;
    }
    private void UpdatePriceByProductGroup(){
        boolean isTwoFormat = isTwoFormatOrder();
        TotalPriceNoneTaxText.setText(Order_Model.calculateProductGroupTotalPrice_NoneTax(isTwoFormat, Order_Bean.getProductGroupMap()));
        DiscountText.setText(Order_Bean.getProductGroupDiscount() == null ? "0" : Order_Bean.getProductGroupDiscount());
        CalculateOrderTotalPrice_Tax_Discount_Profit();
        refreshProductGroupDifferencePrice();
    }
    public void refreshProductGroupDifferencePrice(){
        setExistProductGroupButtonStyle(Order_Bean.getProductGroupMap() != null);
        if(Order_Bean.getProductGroupMap() == null)
            ProductGroupDifferentPriceText.setText("");
        else {
            boolean isTwoFormat = isTwoFormatOrder();
            int discount = (Order_Bean.getDiscount() == null ? 0 : Integer.parseInt(Order_Bean.getDiscount())),
                productGroupDiscount = (Order_Bean.getProductGroupDiscount() == null ? 0 : Integer.parseInt(Order_Bean.getProductGroupDiscount()));

            String productTotalPriceNoneTax = Order_Model.calculateOrderTotalPrice_NoneTax(false, ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
            int productTotalPriceIncludeTax = Order_Model.calculateOrderTotalPrice_IncludeTax(OrderTaxStatus, ToolKit.RoundingInteger(productTotalPriceNoneTax)) - discount;
            String groupTotalPriceNoneTax = Order_Model.calculateProductGroupTotalPrice_NoneTax(false, Order_Bean.getProductGroupMap());
            int groupTotalPriceIncludeTax = Order_Model.calculateOrderTotalPrice_IncludeTax(OrderTaxStatus, ToolKit.RoundingInteger(groupTotalPriceNoneTax)) - productGroupDiscount;
            ProductGroupDifferentPriceText.setText(ToolKit.RoundingString(groupTotalPriceIncludeTax-productTotalPriceIncludeTax));
        }
    }
    private void setExistProductGroupButtonStyle(boolean isExist){
        ComponentToolKit.setButtonStyle(ModifyShipmentProductGroup_Button,isExist ? "-fx-border-color:#3DB52A; -fx-border-width:2" : "");
    }
    private void UpdatePriceByProduct(){
        boolean isTwoFormat = isTwoFormatOrder();
        TotalPriceNoneTaxText.setText(Order_Model.calculateOrderTotalPrice_NoneTax(isTwoFormat, ComponentToolKit.getOrderProductTableViewItemList(MainTableView)));
        DiscountText.setText(Order_Bean.getDiscount() == null ? "0" : Order_Bean.getDiscount());
        CalculateOrderTotalPrice_Tax_Discount_Profit();

        CalculateProjectTax_TotalPrice();
    }
    private void CalculateOrderTotalPrice_Tax_Discount_Profit(){
        int discount = Integer.parseInt(DiscountText.getText());
        int totalPriceNoneTax = Integer.parseInt(TotalPriceNoneTaxText.getText());

        boolean isTwoFormat = isTwoFormatOrder();
        int totalPriceIncludeTax = isTwoFormat ?
                totalPriceNoneTax : Order_Model.calculateOrderTotalPrice_IncludeTax(OrderTaxStatus, totalPriceNoneTax);

        if (totalPriceNoneTax > 0) {
            TotalPriceIncludeTaxText.setText("" + (totalPriceIncludeTax - discount));
            TaxText.setText(isTwoFormat ? "0" : ("" + (totalPriceIncludeTax - totalPriceNoneTax)));
        } else {
            TotalPriceIncludeTaxText.setText("0");
            TaxText.setText("0");
        }
        ProfitText.setText(Order_Model.calculateProfit(OrderTaxStatus,ComponentToolKit.getOrderProductTableViewItemList(MainTableView), TotalPriceIncludeTaxText.getText()));
    }
    @FXML protected void ProjectNameKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            ProjectQuantityText.requestFocus();
        }
    }
    /** TextField Key Released - 專案數量 */
    @FXML protected void ProjectQuantityKeyReleased(KeyEvent KeyEvent){
        if(!ProjectQuantityText.getText().equals("")){
            if(!ProjectPriceAmountText.getText().equals("")){
                String ProjectPriceAmount = String.valueOf(Integer.parseInt(ProjectQuantityText.getText())*Integer.parseInt(ProjectPriceAmountText.getText()));
                ProjectTotalPriceNoneTaxText.setText(ProjectPriceAmount);
                CalculateProjectTax_TotalPrice();
            }
        }
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            ProjectUnitText.requestFocus();
        }else if(KeyPressed.isSubtract(KeyEvent)){
            ProjectNameText.requestFocus();
        }
    }
    @FXML protected void ProjectUnitKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            ProjectPriceAmountText.requestFocus();
        }else if(KeyPressed.isSubtract(KeyEvent)){
            ProjectUnitText.setText(ProjectUnitText.getText().replace("-",""));
            ProjectQuantityText.requestFocus();
        }
    }
    /** TextField Key Released - 專案金額 */
    @FXML protected void ProjectPriceAmountKeyReleased(KeyEvent KeyEvent){
        String ProjectPrice = ProjectPriceAmountText.getText();
        if(ProjectPrice.equals("")){
            ProjectTotalPriceNoneTaxText.setText("");
            ProjectTaxText.setText("");
            ProjectTotalPriceIncludeTaxText.setText("");
            ProjectDifferentPriceText.setText("");
        }else if(!ProjectPrice.equals(ProjectTotalPriceNoneTaxText.getText())){
            if(ProjectQuantityText.getText().equals("")){
                DialogUI.MessageDialog("※ 請輸入數量");
                ProjectTotalPriceNoneTaxText.setText("");
                ProjectTaxText.setText("");
                ProjectTotalPriceIncludeTaxText.setText("");
                ProjectDifferentPriceText.setText("");
            }else{
                String ProjectPriceAmount = String.valueOf(Integer.parseInt(ProjectQuantityText.getText())*Integer.parseInt(ProjectPriceAmountText.getText()));
                ProjectTotalPriceNoneTaxText.setText(ProjectPriceAmount);
                CalculateProjectTax_TotalPrice();
            }
        }
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            ProjectTaxText.requestFocus();
        }else if(KeyPressed.isSubtract(KeyEvent)){
            ProjectUnitText.requestFocus();
        }
    }
    private String CalculateProjectDifferentPrice(){
        return String.valueOf(Integer.parseInt(ProjectTotalPriceIncludeTaxText.getText()) - Integer.parseInt(TotalPriceIncludeTaxText.getText()));
    }
    /** TextField Key Released - 專案稅額 */
    @FXML protected void ProjectTaxKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isF7KeyPressed(KeyEvent))   ProjectTaxStatus = Order_Enum.OrderTaxStatus.未稅;
        else if(KeyPressed.isF8KeyPressed(KeyEvent))  ProjectTaxStatus = Order_Enum.OrderTaxStatus.含稅;
        CalculateProjectTax_TotalPrice();
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            ProjectNameText.requestFocus();
        }
    }
    private void CalculateProjectTax_TotalPrice() {
        if(!ProjectTotalPriceNoneTaxText.getText().equals("")){
            int ProjectTotalPriceNoneTax = Integer.parseInt(ProjectTotalPriceNoneTaxText.getText());
            int ProjectTotalPriceIncludeTax = (int) ((ProjectTaxStatus.ordinal() % 2 == 1 ? ProjectTotalPriceNoneTax * 1.05 : ProjectTotalPriceNoneTax) + 0.5);
            int Tax = ProjectTotalPriceIncludeTax - ProjectTotalPriceNoneTax;
            ProjectTaxText.setText("" + Tax);
            ProjectTotalPriceIncludeTaxText.setText("" + (ProjectTotalPriceIncludeTax));
            ProjectDifferentPriceText.setText(CalculateProjectDifferentPrice());
        }
    }
    /** ComboBox On Action - 廠商 */
    @FXML protected void ExportQuotationVendorOnAction(){
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(ExportQuotationVendor_ComboBox);
        if(!IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName().equals(ExportQuotationVendor.noneInvoice.getDescribe()))
            Order_Model.updateExportQuotationVendor(IAECrawlerAccount_Bean);
    }
    @FXML protected void transferQuotation_ObjectCode_PurchaseUI_KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            boolean toggleSwitch = transferQuotation_toggleSwitch.switchedOnProperty().get();
            String objectCode = transferQuotation_ObjectCodeText_PurchaseUI.getText();
            if(objectCode.equals(""))    DialogUI.MessageDialog(toggleSwitch ? "請輸入客戶編號" : "請輸入廠商編號");
            else    searchTransferQuotationObject(toggleSwitch ? Order_Enum.OrderObject.客戶 : Order_Enum.OrderObject.廠商,
                    toggleSwitch ? ObjectSearchColumn.客戶編號 : ObjectSearchColumn.廠商編號,
                    objectCode);
        }
    }
    @FXML protected void transferQuotation_ObjectName_PurchaseUI_KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            boolean toggleSwitch = transferQuotation_toggleSwitch.switchedOnProperty().get();
            String objectName = transferQuotation_ObjectNameText_PurchaseUI.getText();
            if(objectName.equals(""))    DialogUI.MessageDialog(toggleSwitch ? "請輸入客戶名稱" : "請輸入廠商名稱");
            else searchTransferQuotationObject(toggleSwitch ? Order_Enum.OrderObject.客戶 : Order_Enum.OrderObject.廠商,
                    toggleSwitch ? ObjectSearchColumn.客戶名稱 : ObjectSearchColumn.廠商名稱,
                    objectName);
        }
    }
    @FXML protected void transferQuotation_ObjectCode_ShipmentUI_KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            boolean toggleSwitch = transferQuotation_toggleSwitch.switchedOnProperty().get();
            String objectCode = transferQuotation_ObjectCodeText_ShipmentUI.getText();
            if(objectCode.equals(""))    DialogUI.MessageDialog(toggleSwitch ? "請輸入客戶編號" : "請輸入廠商編號");
            else searchTransferQuotationObject(toggleSwitch ? Order_Enum.OrderObject.客戶 : Order_Enum.OrderObject.廠商,
                    toggleSwitch ? ObjectSearchColumn.客戶編號 : ObjectSearchColumn.廠商編號,
                    objectCode);
        }
    }
    @FXML protected void transferQuotation_ObjectName_ShipmentUI_KeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            boolean toggleSwitch = transferQuotation_toggleSwitch.switchedOnProperty().get();
            String objectName = transferQuotation_ObjectNameText_ShipmentUI.getText();
            if(objectName.equals(""))    DialogUI.MessageDialog(toggleSwitch ? "請輸入客戶名稱" : "請輸入廠商名稱");
            else searchTransferQuotationObject(toggleSwitch ? Order_Enum.OrderObject.客戶 : Order_Enum.OrderObject.廠商,
                    toggleSwitch ? ObjectSearchColumn.客戶名稱 : ObjectSearchColumn.廠商名稱,
                    objectName);
        }
    }
    @FXML protected void transferQuotationMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            boolean toggleSwitch = transferQuotation_toggleSwitch.switchedOnProperty().get();
            if(transferQuotation_ObjectInfoBean == null)    DialogUI.MessageDialog(toggleSwitch ? "※ 請輸入報價單客戶" : "※ 請輸入採購單客戶");
            else{
                boolean sameSinglePrice = false, sameOrderReferenceAndPicture;
                if((this.OrderObject == Order_Enum.OrderObject.客戶 && toggleSwitch)){
                    sameSinglePrice = DialogUI.ConfirmDialog("商品金額是否參照此貨單 ?",true,false,0,0);
                    sameOrderReferenceAndPicture = DialogUI.ConfirmDialog("【貨單參考】【圖片】是否參照此貨單 ?",true,false,0,0);
                }else{
                    sameOrderReferenceAndPicture = DialogUI.ConfirmDialog("【圖片】是否參照此貨單 ?",true,false,0,0);
                }
                Order_Bean Order_Bean = recordTransferQuotationBean(toggleSwitch ? Order_Enum.OrderSource.報價單 : Order_Enum.OrderSource.採購單,
                        toggleSwitch ? Order_Enum.OrderObject.客戶 : Order_Enum.OrderObject.廠商, sameSinglePrice, sameOrderReferenceAndPicture);
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean =
                        createEstablishOrderNewStageBean(OrderStatus.有效貨單, (toggleSwitch ?  Order_Enum.OrderSource.報價單 : Order_Enum.OrderSource.採購單), Order_Enum.OrderExist.未存在, Order_Bean,null,true);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }
    }
    private void searchTransferQuotationObject(OrderObject OrderObject, ObjectSearchColumn ObjectSearchColumn, String searchText){
        ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject, ObjectSearchColumn, searchText);
        if(ObjectList.size() == 1){
            setTransferQuotationObjectInfo(ObjectList.get(0));
        }else if(ObjectList.size() > 1)   CallFXML.ShowObject(Stage, OrderObject, ObjectList,true,ShowObjectSource.採購或報價單互轉,this);
        else{
            setTransferQuotationObjectInfo(null);
            DialogUI.MessageDialog("※ 查無相關" + OrderObject.name());
        }
    }
    public void setTransferQuotationObjectInfo(ObjectInfo_Bean ObjectInfo_Bean){
        if(ObjectInfo_Bean == null){
            if(this.OrderObject == Order_Enum.OrderObject.廠商){
                transferQuotation_ObjectCodeText_PurchaseUI.setText(this.transferQuotation_ObjectInfoBean == null ? "" : this.transferQuotation_ObjectInfoBean.getObjectID());
                transferQuotation_ObjectNameText_PurchaseUI.setText(this.transferQuotation_ObjectInfoBean == null ? "" : this.transferQuotation_ObjectInfoBean.getObjectName());
            }else{
                transferQuotation_ObjectCodeText_ShipmentUI.setText(this.transferQuotation_ObjectInfoBean == null ? "" : this.transferQuotation_ObjectInfoBean.getObjectID());
                transferQuotation_ObjectNameText_ShipmentUI.setText(this.transferQuotation_ObjectInfoBean == null ? "" : this.transferQuotation_ObjectInfoBean.getObjectName());
            }
            return;
        }
        this.transferQuotation_ObjectInfoBean = ObjectInfo_Bean;
        if(OrderObject == Order_Enum.OrderObject.廠商){
            transferQuotation_ObjectCodeText_PurchaseUI.setText(ObjectInfo_Bean.getObjectID());
            transferQuotation_ObjectNameText_PurchaseUI.setText(ObjectInfo_Bean.getObjectName());
        }else{
            transferQuotation_ObjectCodeText_ShipmentUI.setText(ObjectInfo_Bean.getObjectID());
            transferQuotation_ObjectNameText_ShipmentUI.setText(ObjectInfo_Bean.getObjectName());
        }
    }
    private Order_Bean recordTransferQuotationBean(OrderSource transferOrderSource, OrderObject transferOrderObject, boolean sameSinglePrice, boolean sameOrderReferenceAndPicture) throws Exception {
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderSource(transferOrderSource);
        Order_Bean.setOrderDate(ToolKit.getToday("yyyy-MM-dd"));
        Order_Bean.setNowOrderNumber(Order_Model.generateNewestOrderNumberOfEstablishOrder(transferOrderSource, Order_Enum.generateOrderNumberMethod.日期, Order_Bean.getOrderDate(),false));
        Order_Bean.setObjectID(transferQuotation_ObjectInfoBean.getObjectID());
        Order_Bean.setObjectName(transferQuotation_ObjectInfoBean.getObjectName());
        Order_Bean.setIsOffset(OffsetOrderStatus.未沖帳);
        Order_Bean.setIsBorrowed(Order_Enum.OrderBorrowed.未借測.value());
        Order_Bean.setExistPicture(false);
        Order_Bean.setExistInstallment(false);
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        ObservableList<OrderProduct_Bean> newProductList = FXCollections.observableArrayList();

        for(OrderProduct_Bean OrderProduct_Bean : productList){
            OrderProduct_Bean newOrderProduct_Bean;
            if(sameSinglePrice){
                newOrderProduct_Bean = ToolKit.CopyProductBean(OrderProduct_Bean);
                newOrderProduct_Bean.setItemNumber(newProductList.size()+1);
            }else{
                newOrderProduct_Bean = Order_Model.getProduct(transferOrderObject, Order_Enum.ProductSearchColumn.ISBN, Order_Bean.getObjectID(), OrderProduct_Bean.getISBN()).get(0);
                newOrderProduct_Bean.setItemNumber(newProductList.size()+1);
                newOrderProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity());

                //  ***報價轉採購，採購單的商品成本參考報價單
                if(this.OrderObject == Order_Enum.OrderObject.客戶 && transferOrderObject == Order_Enum.OrderObject.廠商)
                    newOrderProduct_Bean.setBatchPrice(OrderProduct_Bean.getBatchPrice());
                Order_Model.refreshProductWannaAmount(transferOrderObject, transferQuotation_ObjectInfoBean, newOrderProduct_Bean);
                newOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(transferOrderObject == Order_Enum.OrderObject.廠商 ?
                        newOrderProduct_Bean.getQuantity()*newOrderProduct_Bean.getBatchPrice() :
                        newOrderProduct_Bean.getQuantity()*newOrderProduct_Bean.getSinglePrice()));
            }
            newProductList.add(newOrderProduct_Bean);
        }

        Order_Bean.setProductList(newProductList);
        Order_Bean.setNumberOfItems(String.valueOf(newProductList.size()));
        Order_Bean.setProductGroupMap(this.Order_Bean.getProductGroupMap());

        // #9016, Modify by Zon
        // 計算商品群組的含稅總金額、折扣、稅額、未含稅總金額
        if(this.Order_Bean.getProductGroupMap() != null){
            Order_Bean.setProductGroupTotalPriceNoneTax(Order_Model.calculateProductGroupTotalPrice_NoneTax(false, this.Order_Bean.getProductGroupMap()));
            Order_Bean.setProductGroupDiscount(this.Order_Bean.getProductGroupDiscount() == null ? "0" :
                    this.Order_Bean.getProductGroupDiscount());
            Order_Bean.setProductGroupTotalPriceIncludeTax(
                    ToolKit.RoundingString(
                            (Order_Model.calculateOrderTotalPrice_IncludeTax(this.Order_Bean.getOrderTaxStatus(),
                                    ToolKit.RoundingInteger(Order_Bean.getProductGroupTotalPriceNoneTax()))-ToolKit.RoundingInteger(Order_Bean.getProductGroupDiscount()))
                    ));
            Order_Bean.setProductGroupTax(ToolKit.RoundingString(ToolKit.RoundingInteger(Order_Bean.getProductGroupTotalPriceIncludeTax()) - ToolKit.RoundingInteger(Order_Bean.getProductGroupTotalPriceNoneTax())));
        } else{
            Order_Bean.setProductGroupTotalPriceNoneTax("0");
            Order_Bean.setProductGroupDiscount("0");
            Order_Bean.setProductGroupTotalPriceIncludeTax("0");
            Order_Bean.setProductGroupTax("0");
        }

        Order_Bean.setTotalPriceNoneTax(Order_Model.calculateOrderTotalPrice_NoneTax(false, Order_Bean.getProductList()));
        Order_Bean.setTax("0");
        Order_Bean.setDiscount("0");
        Order_Bean.setTotalPriceIncludeTax(Order_Bean.getTotalPriceNoneTax());

        Order_Bean.setOrderRemark(this.Order_Bean.getOrderRemark());

        Order_Bean.setProjectCode(ProjectCodeText.getText());
        Order_Bean.setProjectName(ProjectNameText.getText());
        Order_Bean.setProjectQuantity("");
        Order_Bean.setProjectUnit("");
        Order_Bean.setProjectPriceAmount("");
        Order_Bean.setProjectTotalPriceNoneTax("");
        Order_Bean.setProjectTax("");
        Order_Bean.setProjectTotalPriceIncludeTax("");
        Order_Bean.setProjectDifferentPrice("");

        if(sameOrderReferenceAndPicture){
            HashMap<OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = Order_Model.getOrderReferenceFromDatabase(this.Order_Bean);
            Order_Bean.setOrderReferenceMap(orderReferenceMap);


            ArrayList<String> mainPictureList;
            ArrayList<HashMap<Boolean,String>> orderPictureList = Order_Model.getOrderPicture(this.Order_Bean.getOrderID(), OrderSource);
            if(orderPictureList != null){
                mainPictureList = new ArrayList<>();
                for(HashMap<Boolean,String> pictureMap : orderPictureList){
                    mainPictureList.add(pictureMap.entrySet().iterator().next().getValue());
                }
                Order_Bean.setOrderPictureList(mainPictureList);
            }
        }
        return Order_Bean;
    }
    @FXML protected void AddExportQuotationItemMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String itemName = DialogUI.TextInputDialog("品項名稱","請輸入欲新增品項名稱",null);
            if(itemName != null){
                String priceDiscount;
                do{
                    priceDiscount = DialogUI.TextInputDialog("品項金額(未稅)","品項金額為此報價單總額(未稅)的【0.1 ~ 99.9】％",null);
                    if(priceDiscount == null || (ToolKit.isDouble(priceDiscount) && Double.parseDouble(priceDiscount) > 0 && Double.parseDouble(priceDiscount) < 100))
                        break;
                    else
                    DialogUI.AlarmDialog("※ 請輸正確金額格式");
                }while(true);

                if(priceDiscount != null){
                    ExportQuotationItem ExportQuotationItem = new ExportQuotationItem(ExportQuotation_SplitMenuButton,itemName,Double.parseDouble(priceDiscount),null);
                    if(Order_Model.insertExportQuotationItem(ExportQuotationItem)){
                        ExportQuotation_SplitMenuButton.getItems().add(ExportQuotation_SplitMenuButton.getItems().size()-1,
                                ExportQuotationItem.getCustomMenuItem());
                        DialogUI.MessageDialog("※ 新增成功");
                    }else
                        DialogUI.MessageDialog("※ 新增失敗");
                }
            }
        }
    }
    /** Button Mouse Clicked - 採購、詢價單匯出 */
    @FXML protected void ExportPurchaseQuotationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            for(Order_ExportPurchaseFunction exportPurchaseFunction : exportPurchaseFunctionRadioButtonMap.keySet()){
                if(exportPurchaseFunctionRadioButtonMap.get(exportPurchaseFunction).isSelected()){
                    ExportPurchaseQuotation(exportPurchaseFunction);
                    break;
                }
            }
        }
    }
    private void ExportPurchaseQuotation(Order_ExportPurchaseFunction exportPurchaseFunction){
        try{
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            if(productList == null || productList.size() == 0) {
                DialogUI.MessageDialog("※ 未存在任何商品");
                return;
            }
            boolean isShowPrice = DialogUI.ConfirmDialog("是否顯示商品金額 ?",true,false,0,0);
            int pageSize = productList.size()/15;
            if(productList.size()%15 == 0)
                pageSize = pageSize - 1;
            for(int page = 0 ; page <= pageSize ; page++) {
                int startIndex = page*15;
                int endIndex;
                if(productList.size()%15 == 0 || page != pageSize){
                    endIndex = (page+1)*15;
                }else {
                    endIndex = page * 15 + productList.size() % 15;
                }
                ExportPurchaseQuotation_Model document = new ExportPurchaseQuotation_Model();
                document.readTemplate(TemplatePath.PurchaseQuotationOrAskPriceDocument);
                document.setData(exportPurchaseFunction,
                        Order_Bean.getOrderDate(),
                        Order_Bean.getNowOrderNumber(),
                        OrderTaxStatus,
                        productList.subList(startIndex,endIndex),
                        Order_Bean.getOrderRemark(),
                        isShowPrice);
                String outputName = exportPurchaseFunction.getDescribe() + "匯出_" + (page+1) + " - " + Order_Bean.getNowOrderNumber() + " - " + Order_Bean.getProjectName() + ".xlsx";
                document.build(CallConfig.getFile_OutputPath() + "\\" + outputName);
            }
            DialogUI.MessageDialog("※ 匯出成功");
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            DialogUI.ExceptionDialog(Ex);
        }
    }
    @FXML protected void TwoFormatOnAction(){
        if(isTwoFormatOrder()){
            ComponentToolKit.setTextFieldEditable(TaxText, false);
            if(isProductGroupModel()){
                UpdatePriceByProductGroup();
            }else{
                setProductInfoByTableView(ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView),false);
                UpdatePriceByProduct();
            }
        }
    }
    @FXML protected void ThreeFormatOnAction(){
        if(!isTwoFormatOrder()){
            ComponentToolKit.setTextFieldEditable(TaxText, true);
            if(isProductGroupModel()){
                UpdatePriceByProductGroup();
            }else{
                setProductInfoByTableView(ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView),false);
                UpdatePriceByProduct();
            }

        }
    }
    /** Button Mouse Clicked - 報價單匯出 */
    @FXML protected void ExportShipmentQuotationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            IAECrawlerAccount_Bean iAECrawlerAccount_bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(ExportQuotationVendor_ComboBox);
            if(!iAECrawlerAccount_bean.isExportQuotation()){
                DialogUI.MessageDialog("※ 該廠商不允許匯出報價單");
                return;
            }
            RadioButton ExportContent_RadioButton = (RadioButton) ExportContentToggleGroup.getSelectedToggle();
            String ExportContent = ExportContent_RadioButton.getText();
            RadioButton ExportFormat_RadioButton = (RadioButton) ExportFormatToggleGroup.getSelectedToggle();
            String ExportFormat = ExportFormat_RadioButton.getText();
            String VendorName = iAECrawlerAccount_bean.getExportQuotation_ManufacturerNickName();
            JSONObject templateFormat = iAECrawlerAccount_bean.getTemplateFormatJsonObject();
            if(iAECrawlerAccount_bean.getExportQuotation_ManufacturerNickName().equals(ExportQuotationVendor.noneInvoice.getDescribe())){
                ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountComboBoxItemList(ExportQuotationVendor_ComboBox);
                for(IAECrawlerAccount_Bean iAECrawlerAccount_Bean : IAECrawlerAccountList){
                    if(iAECrawlerAccount_Bean.isTheSameAsNoneInvoice()){
                        templateFormat = iAECrawlerAccount_Bean.getTemplateFormatJsonObject();
                        break;
                    }
                }
            }

            String OrderNumber = OrderNumberText.getText().trim();
            String ObjectID = ObjectIDText.getText().trim();
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            if("".equals(OrderNumber) || "".equals(ObjectID)) {
                DialogUI.AlarmDialog("※ 「貨單號」或「對象編號為空」。");
                return;
            }else if (productList.size() == 0) {
                DialogUI.AlarmDialog("※ 「無品項」!");
                return;
            }else if(ToolKit.isFileNameExistEscapeCharacter(ProjectNameText.getText())) {
                DialogUI.AlarmDialog("※ 檔案名稱不允許存在跳脫字元「/\\:*?\"><|」");
                return;
            }else{
                String Template = "Template/" + VendorName + "-估(" + Order_Enum.ExportQuotationFormat.valueOf(ExportFormat).name()+ ").xlsx";
                File file =new File(Template);
                if  (!file.exists() && !file.isDirectory()){
                    DialogUI.MessageDialog("※ 該廠商未存在範本!");
                    return;
                }
                if(templateFormat == null){
                    DialogUI.MessageDialog("※ 未設定匯出範本格式");
                    return;
                }
                if (Order_Enum.ExportQuotationContent.valueOf(ExportContent) == Order_Enum.ExportQuotationContent.專案) {
                    if (ProjectNameText.getText().equals("") || ToolKit.isFileNameExistEscapeCharacter(ProjectNameText.getText())) {
                        DialogUI.AlarmDialog("※ 專案名稱資料錯誤!");
                        return;
                    } else if (ProjectQuantityText.getText().equals("") || ProjectPriceAmountText.getText().equals("")) {
                        DialogUI.AlarmDialog("※ 專案數量或金額錯誤!");
                        return;
                    }
                }
            }
            boolean exportStatus = false;
            ExportQuotation_Bean exportQuotation_bean = new ExportQuotation_Bean();
            try{
                exportQuotation_bean.setOrderNumber(OrderNumber);
                exportQuotation_bean.setOrderDate(ComponentToolKit.getDatePickerValue(OrderDate_DatePicker,"yyyy/MM/dd"));
                exportQuotation_bean.setCustomerID(ObjectID);
                exportQuotation_bean.setShowProfit(ShowProfit_CheckBox.isSelected());
                exportQuotation_bean.setExportContent(Order_Enum.ExportQuotationContent.valueOf(ExportContent));
                exportQuotation_bean.setExportFormat(Order_Enum.ExportQuotationFormat.valueOf(ExportFormat));
                exportQuotation_bean.setVendorNickName(VendorName);
                exportQuotation_bean.setTemplateFormat(templateFormat);
                exportQuotation_bean.setOrderSource(OrderSource);
                exportQuotation_bean.setProjectCode(ProjectCodeText.getText());
                exportQuotation_bean.setProjectName(ProjectNameText.getText());
                exportQuotation_bean.setProjectQuantity(ProjectQuantityText.getText());
                exportQuotation_bean.setProjectUnit(ProjectUnitText.getText());
                exportQuotation_bean.setProjectPriceAmount(ProjectPriceAmountText.getText());
                exportQuotation_bean.setProjectTotalPriceNoneTax(ProjectTotalPriceNoneTaxText.getText());
                exportQuotation_bean.setProjectTax(ProjectTaxText.getText());
                exportQuotation_bean.setProjectTotalPriceIncludeTax(ProjectTotalPriceIncludeTaxText.getText());
                exportQuotation_bean.setProjectDifferencePrice(ProjectDifferentPriceText.getText());

                exportQuotation_bean.setExportGroup(shipmentProductGroup_toggleSwitch.switchedOnProperty().get());
                exportQuotation_bean.setProductGroup(Order_Bean.getProductGroupMap());

                exportQuotation_bean.setProductList(FXCollections.observableArrayList(productList));
                if(exportQuotation_bean.isExportGroup()){
                    exportQuotation_bean.setProductList(generateExportProductGroupItem(exportQuotation_bean.getProductGroup(), exportQuotation_bean.getProductList()));
                }else{
                    exportQuotation_bean.setProductList(generateSelectExportQuotationItem(exportQuotation_bean.getProductList()));
                }

                exportQuotation_bean.setOrderRemark(Order_Bean.getOrderRemark());

                HashMap<OrderSource, HashMap<Integer,Boolean>> orderReferenceMap = Order_Model.getOrderReferenceFromDatabase(Order_Bean);
                if(orderReferenceMap == null)
                    exportQuotation_bean.setOrderReferenceList(FXCollections.observableArrayList());
                else
                    exportQuotation_bean.setOrderReferenceList(Order_Model.getOrderReferenceByOrderID(orderReferenceMap.get(Order_Enum.OrderSource.報價單),orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單)));

                exportQuotation_bean.setExportCount(Order_Model.getExportQuotationRecordCount(Order_Bean.getOrderID(), OrderSource));

                CallFXML.ShowExportQuotation(Stage, exportQuotation_bean, Order_Bean, iAECrawlerAccount_bean);
            }catch (Exception ex){
                ERPApplication.Logger.catching(ex);
                DialogUI.ExceptionDialog(ex);
            }
        }
    }
    private ObservableList<OrderProduct_Bean> generateSelectExportQuotationItem(ObservableList<OrderProduct_Bean> orderProductList){
        String totalPriceNoneTax = TotalPriceNoneTaxText.getText();
        ObservableList<ExportQuotationItem> exportQuotationItemList = Order_Model.getExportQuotationItem(null);
        for(ExportQuotationItem ExportQuotationItem : exportQuotationItemList){
            if(ExportQuotationItem.isCheckBoxSelect()){
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setAddExportQuotationItem(true);
                OrderProduct_Bean.setItemNumber(orderProductList.size()+1);
                OrderProduct_Bean.setProductName(ExportQuotationItem.getItemName());
                OrderProduct_Bean.setUnit("PCS");
                OrderProduct_Bean.setQuantity(1);
                OrderProduct_Bean.setSinglePrice(ToolKit.RoundingInteger(Integer.parseInt(totalPriceNoneTax)*ExportQuotationItem.getPriceByDiscount()));
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
                orderProductList.add(OrderProduct_Bean);
            }
        }
        return orderProductList;
    }
    private ObservableList<OrderProduct_Bean> generateExportProductGroupItem(HashMap<Integer,
            HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap, ObservableList<OrderProduct_Bean> productList){
        ObservableList<OrderProduct_Bean> exportProductList = FXCollections.observableArrayList();
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            OrderProduct_Bean createOrderProduct_Bean = new OrderProduct_Bean();
            createOrderProduct_Bean.setAddExportQuotationItem(true);
            createOrderProduct_Bean.setItemNumber(ProductGroup_Bean.getItemNumber());
            createOrderProduct_Bean.setProductName(ProductGroup_Bean.getGroupName());
            createOrderProduct_Bean.setUnit(ProductGroup_Bean.getUnit());
            createOrderProduct_Bean.setQuantity(ProductGroup_Bean.getSmallQuantity() == null || ProductGroup_Bean.getSmallQuantity() == 0 ?
                    ProductGroup_Bean.getQuantity() : ProductGroup_Bean.getSmallQuantity());
            createOrderProduct_Bean.setSinglePrice(ProductGroup_Bean.getSmallSinglePrice() == null ? ProductGroup_Bean.getSinglePrice() : ProductGroup_Bean.getSmallSinglePrice());
            createOrderProduct_Bean.setPriceAmount(ProductGroup_Bean.getSmallPriceAmount() == null ? ToolKit.RoundingInteger(ProductGroup_Bean.getPriceAmount()) : ProductGroup_Bean.getSmallPriceAmount());

            double batchPrice = 0;
            HashMap<Integer,ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
            for(Integer item_id : itemMap.keySet()){
                ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                for(OrderProduct_Bean OrderProduct_Bean : productList) {
                    if(item_id.equals(OrderProduct_Bean.getItem_id())){
                        Integer smallQuantity = ProductGroup_Bean.getSmallQuantity();
                        if(smallQuantity == null || smallQuantity == 0){
                            batchPrice = batchPrice + OrderProduct_Bean.getBatchPrice()*ItemGroup_Bean.getItem_quantity();
                        }else{
                            batchPrice = batchPrice + (OrderProduct_Bean.getBatchPrice()*ItemGroup_Bean.getItem_quantity()/smallQuantity);
                        }
                        break;
                    }
                }
                createOrderProduct_Bean.setBatchPrice(ToolKit.RoundingDouble(batchPrice));
            }
            exportProductList.add(createOrderProduct_Bean);
        }
        return exportProductList;
    }
    /** Button Mouse Clicked - 匯出紀錄 */
    @FXML protected void ShowExportQuotationRecordMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ObservableList<ExportQuotationRecord_Bean> exportQuotationRecordList = Order_Model.getExportQuotationRecord(Order_Bean.getOrderID(), OrderSource);
            if(exportQuotationRecordList.size() == 0)   DialogUI.MessageDialog("※ 無匯出紀錄");
            else
                CallFXML.ShowExportQuotationRecord(Stage,exportQuotationRecordList);
        }
    }
    /** Button Mouse Clicked - 開啟資料夾 */
    @FXML protected void OpenExportQuotationFolderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                String FolderPath = CallConfig.getFile_OutputPath();
                if (FolderPath.equals("")) DialogUI.MessageDialog("※ 未設定路徑!");
                else Desktop.getDesktop().open(new File(FolderPath));
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - 採購文件匯出 */
    @FXML protected void ExportPurchaseDocumentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(ToolKit.isFileNameExistEscapeCharacter(ProjectNameText.getText()))
                DialogUI.AlarmDialog("※ 檔案名稱不允許存在跳脫字元「/\\:*?\"><|」");
            else
                CallFXML.ShowReportGenerator(Stage, Order_Bean, shipmentProductGroup_toggleSwitch.switchedOnProperty().get(), ProjectNameText.getText(), ToolKit.RoundingInteger(TotalPriceIncludeTaxText.getText()));
        }
    }
    /** Button Mouse Clicked - 規格書匯出 */
    @FXML protected void ExportSpecificationDocumentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String projectName = ProjectNameText.getText();
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            if(productList == null || productList.size() == 0)
                DialogUI.MessageDialog("※ 不存在商品");
            else if(ToolKit.isFileNameExistEscapeCharacter(projectName))
                DialogUI.AlarmDialog("※ 檔案名稱不允許存在跳脫字元「/\\:*?\"><|」");
            else
                CallFXML.ShowExportSpecification(Stage, projectName, productList);
        }
    }
    /** Button Mouse Clicked - 發票黏貼明細 */
    @FXML protected void InvoicePasteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            Integer OrderID = Order_Bean.getOrderID();
            String OrderNumber = Order_Bean.getAlreadyOrderNumber();
            String OrderType = "";
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(ExportQuotationVendor_ComboBox);
            if(IAECrawlerAccount_Bean == null)
                DialogUI.MessageDialog("※ 請選擇報價單廠商");
            else{
                String VendorName = IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();
                if(VendorName.equals(ExportQuotationVendor.noneInvoice.getDescribe()))
                    DialogUI.MessageDialog("※ 報價單廠商：無發票章");
                else{
                    if (OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨單) OrderType = "1";
                    else if (OrderSource == Order_Enum.OrderSource.報價單)    OrderType = "0";
                    else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)   OrderType = "3";
                    String args = String.format("%s %s %s %s %s", VendorName, OrderNumber, OrderType, Order_Bean.isCheckout().ordinal(), OrderID);
                    if (!args.equals("")) CallJAR.InvoiceBuilder(args);
                }
            }
        }
    }
    /** Button Mouse Clicked - 完工報告書 */
    @FXML protected void CompletionReportMouseClicked(MouseEvent MouseEvent){   if(KeyPressed.isMouseLeftClicked(MouseEvent))   CallJAR.FinishReportGenerator();    }
    /** CheckBox On Action - 沖帳 */
    @FXML protected void OffsetOrderCheckBox_OnAction(){
        if(OffsetOrder_CheckBox.isSelected()) {
            OrderBorrowed_CheckBox.setSelected(false);
        }
        if(ComponentToolKit.getOrderProductTableViewItemList(MainTableView).size() != 0){
            setOffsetPrice(OffsetOrder_CheckBox.isSelected());
            ProfitText.setText(Order_Model.calculateProfit(OrderTaxStatus,ComponentToolKit.getOrderProductTableViewItemList(MainTableView), TotalPriceIncludeTaxText.getText()));
        }
        ComponentToolKit.setButtonDisable(ProductGenerator_Button, !OffsetOrder_CheckBox.isSelected());
    }
    private void setOffsetPrice(boolean isOffsetOrder){
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            if(isOffsetOrder)
                OrderProduct_Bean.setBatchPrice(OrderProduct_Bean.getSinglePrice());
            else
                OrderProduct_Bean.setBatchPrice(Product_Model.getStoreBatchPriceByID(OrderProduct_Bean.getStore_id()));
        }
        OrderProduct_Bean selectOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        BatchPriceText.setText(ToolKit.RoundingString(true,selectOrderProduct_Bean.getBatchPrice()));
        calculatePricePercentage();
    }
    /** Button Mouse Clicked - 商品產生器 */
    @FXML protected void ProductGeneratorMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ObjectIDText.getText().equals("") && ObjectNameText.getText().equals(""))    DialogUI.MessageDialog("※ 請輸入" + OrderObject.name()+ "編號");
            else    CallFXML.ShowProductGenerator(Stage, OrderObject, ObjectIDText.getText(),this);
        }
    }
    @FXML protected void OrderRemarkMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowEditOrderRemark(Stage, this, OrderExist, Order_Bean,true,false);
        }
    }
    @FXML protected void OrderRemarkMouseMoved(){
        if(Order_Bean != null && Order_Bean.getOrderRemark() != null && !Order_Bean.getOrderRemark().equals(""))
            ComponentToolKit.setToolTips(OrderRemarkButton, orderRemarkTooltip, Order_Bean.getOrderRemark());
    }
    @FXML protected void CashierRemarkMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowEditOrderRemark(Stage, this, OrderExist, Order_Bean,false,true);
        }
    }
    @FXML protected void CashierRemarkMouseMoved(){
        if(Order_Bean != null && Order_Bean.getCashierRemark() != null && !Order_Bean.getCashierRemark().equals("")) {
            ComponentToolKit.setToolTips(CashierRemarkButton, cashierRemarkTooltip, Order_Bean.getCashierRemark());
        }
    }
    public void setModifyOrderButtonStyle(){
            ComponentToolKit.setButtonStyle(OrderRemarkButton,Order_Bean.getOrderRemark() != null && !Order_Bean.getOrderRemark().equals("") ? "-fx-background-color:" + ToolKit.getGreen_BackgroundColor() : "");
        ComponentToolKit.setButtonStyle(CashierRemarkButton,Order_Bean.getCashierRemark() != null && !Order_Bean.getCashierRemark().equals("") ? "-fx-background-color:" + ToolKit.getGreen_BackgroundColor() : "");
        orderRemarkTooltip.setText(Order_Bean.getOrderRemark());
        cashierRemarkTooltip.setText(Order_Bean.getCashierRemark());

        if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.進貨單)
            ComponentToolKit.setButtonStyle(modifyPurchaseOrderButton, "-fx-background-color:" + ToolKit.getPink_BackgroundColor());
        else if(OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
            ComponentToolKit.setButtonStyle(modifyShipmentOrderButton, "-fx-background-color:" + ToolKit.getPink_BackgroundColor());
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
            ComponentToolKit.setButtonStyle(modifyReturnOrderButton, "-fx-background-color:" + ToolKit.getPink_BackgroundColor());
    }
    @FXML protected void ProjectCodeHyperlinkMouseClicked(MouseEvent MouseEvent) throws URISyntaxException, IOException {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String projectCode = ProjectCodeText.getText();
            if(projectCode != null && !projectCode.equals("")){
                String projectCodeUrl = SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.專案編號預設網址) + projectCode;
                Desktop Desktop = java.awt.Desktop.getDesktop();
                Desktop.browse(new URI(projectCodeUrl)); //使用預設瀏覽器開啟超連結
            }else
                DialogUI.MessageDialog("※ 無專案編號");
        }
    }
    /** Button Mouse Clicked - 建立貨單 */
    @FXML protected void EstablishOrderMouseClicked(MouseEvent MouseEvent) throws FileNotFoundException {
        String projectName = ProjectNameText.getText();
        String orderNumber = OrderNumberText.getText();
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(ObjectInfo_Bean == null) DialogUI.MessageDialog("※ 未設定對象");
            else if(projectName.length() > 100)    DialogUI.MessageDialog("※ 專案名稱過長!");
            else if(ToolKit.isFileNameExistEscapeCharacter(projectName)) {  DialogUI.AlarmDialog("※ 檔案名稱不允許存在跳脫字元「/\\:*?\"><|」");   }
            else if(Order_Model.isOrderNumberExist(OrderSource, orderNumber))   DialogUI.MessageDialog("※ 貨單號重複!");
            else if(ComponentToolKit.getTableViewItemsSize(MainTableView) == 0) DialogUI.MessageDialog("※ 未新增商品");
            else if((OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) &&
                    Order_Model.isSubBillOfMainOrderTransferAlready(OrderSource,Order_Bean.getWaitingOrderNumber()) != null)
                DialogUI.MessageDialog("※ 子貨單建立失敗，主貨單已轉出貨");
            else if(OrderSource == Order_Enum.OrderSource.進貨退貨單 &&
                    Order_Model.isProductInStockShortage(ComponentToolKit.getOrderProductTableViewItemList(MainTableView)))
                    DialogUI.MessageDialog("※ 庫存不足以退貨給廠商!");
            else{
                if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                    int mainOrderTotalPriceIncludeTax = Order_Model.getTotalPriceIncludeTaxFromMainOrderBySubBill(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber());
                    int subBillTotalPriceIncludeTax = Order_Model.getAllSubBillTotalPriceIncludeTax(OrderSource, Order_Bean.getWaitingOrderNumber(),false,null);
                    if(mainOrderTotalPriceIncludeTax - subBillTotalPriceIncludeTax - Integer.parseInt(TotalPriceIncludeTaxText.getText()) < 0){
                        DialogUI.AlarmDialog("子貨單累計金額超過母貨單總金額!!\n剩餘 $" + (mainOrderTotalPriceIncludeTax-subBillTotalPriceIncludeTax));
                        return;
                    }
                }

                RecordThisOrderInfo();
                boolean status;
                if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
                    status = Order_Model.insertOrder(OrderSource, Order_Bean,Order_Bean.getProductList(),true,false);
                else
                    status = Order_Model.insertOrder(OrderSource, Order_Bean,null,false,false);
                if(status) {
                    if(OrderSource == Order_Enum.OrderSource.報價單 && Order_Bean.getProductGroupMap() != null){
                        status = insertTransferQuotationProductGroup(Order_Bean);
                    }
                    ObservableList<OrderProduct_Bean> productList = Order_Bean.getProductList();
                    if(!status){
                        ERPApplication.Logger.info("※ [成功] 建立「" + OrderSource.name() + " : " + Order_Bean.getNowOrderNumber() + "」；【轉報價】商品群組建立失敗 !");
                        ERPApplication.Logger.info("---------------------------------------------------");
                        DialogUI.AlarmDialog("【貨單】建立成功，但【商品群組】建立失敗 !");
                    }else{
                        ERPApplication.Logger.info("※ [成功] 建立「" + OrderSource.name() + " : " + Order_Bean.getNowOrderNumber() + "」");
                        ERPApplication.Logger.info("---------------------------------------------------");
                        DialogUI.MessageDialog("※ 建立成功");
                    }
                    refreshSearchOrderView();
                    if(coverOrderReference)
                        initialOrderReference();
                    if (OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) ComponentToolKit.closeThisStage(Stage);
                    else initialComponentExceptSubBill(true);
                }else {
                    ERPApplication.Logger.warn("※ [失敗] 建立「" + OrderSource.name() + " : " + Order_Bean.getNowOrderNumber() + "」");
                    ERPApplication.Logger.info("---------------------------------------------------");
                    DialogUI.MessageDialog("※ 建立失敗");
                }
            }
        }
    }
    private boolean insertTransferQuotationProductGroup(Order_Bean Order_Bean){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
        ObservableList<OrderProduct_Bean> productList = Order_Bean.getProductList();
        refreshOrderItemId(Order_Bean.getOrderID(), productList);
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
            for(Integer item_id : itemMap.keySet()){
                ItemGroup_Bean itemGroup_bean = itemMap.get(item_id);
                for(OrderProduct_Bean orderProduct_bean : productList){
                    if(itemGroup_bean.getIsbn().equals(orderProduct_bean.getISBN())){
                        itemGroup_bean.setItem_id(orderProduct_bean.getItem_id());
                        break;
                    }
                }
            }
        }
        return Order_Model.updateItemsProductGroup(null, productGroupMap);
    }
    /** Button Mouse Clicked - 修改貨單 */
    @FXML protected void ModifyOrderMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            modifyOrder();
        }
    }
    private void modifyOrder() {
        if(isTwoFormatOrder()){
            DialogUI.MessageDialog("※ 請切換回【三聯】狀態");
            return;
        }else if(ToolKit.isFileNameExistEscapeCharacter(ProjectNameText.getText())) {
            DialogUI.AlarmDialog("※ 檔案名稱不允許存在跳脫字元「/\\:*?\"><|」");
            return;
        }

        boolean continueModify;
        boolean isProductGroupModel = isProductGroupModel();
        if(isProductGroupModel) {
            continueModify = DialogUI.ConfirmDialog("此修改只會修改【商品群組 - 折讓】，是否繼續 ?",true, false, 0, 0);
        }else{
            continueModify = !notExistProductAndDeleteOrder();
            if(continueModify){
                if(OrderSource == Order_Enum.OrderSource.採購單 && Order_Bean.getEstablishSource() == EstablishSource.系統建立){
                    if(!DialogUI.ConfirmDialog("※ 此貨單為「待出貨」庫存不足轉「採購」，是否確定修改 ?",true,true,11,15))
                        return;
                }
                if(isOffsetOrder())
                    DialogUI.AlarmDialog("※ 此為【沖帳單】，不儲存參考貨單");
                else if(coverOrderReference) {
                    String inputText = DialogUI.TextInputDialog("參考貨單","是否儲存已載入的參考貨單 ? 請輸入【OK】來儲存",null);
                    coverOrderReference = inputText != null && inputText.equals("OK");
                }
            }
        }
        if(continueModify){
            try{
                RecordThisOrderInfo();
                if(!isProductGroupModel){
                    if(compareOrderAndOpen()){
                        return;
                    }
                    boolean isOrderItemsAndProductGroupMatch = isOrderItemsAndProductGroupMatch();
                    if((!isOrderItemsAndProductGroupMatch && !DialogUI.ConfirmDialog("※ 商品數量與群組不吻合，是否確定修改 ?",true,true,2,12)) ||
                            (isOrderItemsAndProductGroupMatch && !DialogUI.ConfirmDialog("※ 是否確定修改 ?",true,false,0,0)))
                        return;
                }
//                ObservableList<OrderProduct_Bean> dbOldOrderItems = Order_Model.getOrderItems(OrderSource, Order_Bean.getOrderID());
                if(Order_Model.modifyOrder(Order_Bean,null,false,coverOrderReference)){
//                    if(OrderSource == Order_Enum.OrderSource.採購單){
//                        showProductSaleLog(dbOldOrderItems);    //  -待出貨量 -需量
//                    }
                    DialogUI.MessageDialog("※ 修改成功");
                    ComponentToolKit.setButtonDisable(ShowOrderReference_Button, !Order_Bean.isExistOrderReference());
                    refreshSearchOrderView();
                    refreshOrderItemId(Order_Bean.getOrderID(), ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
                    refreshProductGroupDifferencePrice();
                    if(coverOrderReference)
                        initialOrderReference();
                    if(Order_Bean.getProductGroupMap() != null) {
                        Order_Bean.setProductGroupMap(Order_Model.getProductGroupByOrderId(Order_Bean.getOrderID()));
                        shipmentProductGroup_toggleSwitch.refreshAccordion();
                    }
                }else    DialogUI.MessageDialog("※ 貨單修改失敗");
            }catch (Exception Ex){
                DialogUI.ExceptionDialog(Ex);
                ERPApplication.Logger.catching(Ex);
            }
        }
    }
    private boolean notExistProductAndDeleteOrder(){
        String nowOrderNumber = OrderNumberText.getText();
        boolean status = false;
        if(ComponentToolKit.getTableViewItemsSize(MainTableView) == 0){
            status = true;
            if(DialogUI.ConfirmDialog("※ 貨單無商品，將一併刪除此貨單! 是否要繼續 ?",true,false,0,0)) {
                if(!Order_Model.getInvalidInvoice(OrderSource, Order_Bean.getOrderID()).equals("")){
                    DialogUI.AlarmDialog("※ 刪除被拒，此貨單存在「作廢發票」!");
                    return true;
                }
                boolean isHandleProductStatus = (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單);
                if(Order_Model.deleteOrder(OrderSource, Order_Bean.getOrderID(), nowOrderNumber, isHandleProductStatus)){
                    ComponentToolKit.closeThisStage(Stage);
                    ERPApplication.Logger.info("※ [成功] 刪除「" + OrderSource + " : " + nowOrderNumber + "」");
                    ERPApplication.Logger.info("---------------------------------------------------");
                    DialogUI.MessageDialog("※ 刪除成功");
                    refreshSearchOrderView();
                }else{
                    ERPApplication.Logger.warn("※ [失敗] 刪除「" + OrderSource + " : " + nowOrderNumber + "」");
                    ERPApplication.Logger.info("---------------------------------------------------");
                    DialogUI.MessageDialog("※ 刪除失敗");
                }
            }
        }
        return status;
    }
    private void showProductSaleLog(ObservableList<OrderProduct_Bean> dbOldOrderItems){
        HashMap<String, ModifyProductQuantity_Bean> ModifyProductQuantityMap = findModifyProduct(dbOldOrderItems);
        for(Map.Entry<String,ModifyProductQuantity_Bean> entry : ModifyProductQuantityMap.entrySet()){
            String ISBN = entry.getKey();
            OrderProduct_Bean oldOrderProduct_Bean = entry.getValue().getOldOrderProduct_Bean();
            OrderProduct_Bean newOrderProduct_Bean = entry.getValue().getNewOrderProduct_Bean();
            if(oldOrderProduct_Bean != null && newOrderProduct_Bean != null && oldOrderProduct_Bean.getQuantity() != newOrderProduct_Bean.getQuantity()){
                if(oldOrderProduct_Bean.getQuantity() < newOrderProduct_Bean.getQuantity()) {
                    //                                    待入倉量 = 總待入倉 - 舊 + 新
                    System.out.println("商品數量增加   ISBN = " + ISBN + "  " + oldOrderProduct_Bean.getQuantity() + " -> " + newOrderProduct_Bean.getQuantity());
                }else{
                    System.out.println("商品數量減少   ISBN = " + ISBN + "  " + oldOrderProduct_Bean.getQuantity() + " -> " + newOrderProduct_Bean.getQuantity());
                }
            }else if(oldOrderProduct_Bean == null && newOrderProduct_Bean != null)
                System.out.println("新增品項  ISBN = " + newOrderProduct_Bean.getISBN() + "  " + newOrderProduct_Bean.getQuantity());
            else if(oldOrderProduct_Bean != null)
                System.out.println("刪除品項   ISBN = " + oldOrderProduct_Bean.getISBN() + "  " + oldOrderProduct_Bean.getQuantity());
        }
    }
    private boolean isOrderItemsAndProductGroupMatch(){
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        HashMap<Integer,Integer> itemGroupMap = new HashMap<>();
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
        if(productGroupMap == null)
            return true;
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
            for(Integer item_id : itemMap.keySet()){
                ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                if(itemGroupMap.containsKey(item_id))
                    itemGroupMap.put(item_id,itemGroupMap.get(item_id)+ItemGroup_Bean.getItem_quantity()*ProductGroup_Bean.getQuantity());
                else
                    itemGroupMap.put(item_id,ItemGroup_Bean.getItem_quantity()*ProductGroup_Bean.getQuantity());
            }
        }
        for(Integer item_id : itemGroupMap.keySet()){   //  //  群組符合貨單商品
            boolean isMatch = false;
            for(OrderProduct_Bean OrderProduct_Bean : productList){
                if(OrderProduct_Bean.getItem_id() != null && OrderProduct_Bean.getItem_id().equals(item_id)) {
                    isMatch = true;
                    break;
                }
            }
            if(!isMatch)
                return false;
        }
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            if(OrderProduct_Bean.getItem_id() != null){
                if(!itemGroupMap.containsKey(OrderProduct_Bean.getItem_id()) || itemGroupMap.get(OrderProduct_Bean.getItem_id()) != OrderProduct_Bean.getQuantity())
                    return false;
            }else
                return true;
        }
        return true;
    }
    private void refreshOrderItemId(int order_id, ObservableList<OrderProduct_Bean> productList){
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            Integer item_id = Order_Model.refresh_Order_Item_Id(order_id, OrderProduct_Bean);
            if(item_id != null)
                OrderProduct_Bean.setItem_id(item_id);
        }
    }
    private void RecordThisOrderInfo() throws FileNotFoundException {
        if(isProductGroupModel()){
            Order_Bean.setProductGroupTotalPriceNoneTax(TotalPriceNoneTaxText.getText());
            Order_Bean.setProductGroupTax(TaxText.getText());
            Order_Bean.setProductGroupDiscount(DiscountText.getText());
            Order_Bean.setProductGroupTotalPriceIncludeTax(TotalPriceIncludeTaxText.getText());
            return;
        }
        Order_Bean.setNowOrderNumber(OrderNumberText.getText());
        Order_Bean.setOrderDate(ComponentToolKit.getDatePickerValue(OrderDate_DatePicker,"yyyy-MM-dd"));
        Order_Bean.setObjectID(ObjectIDText.getText());
        Order_Bean.setObjectName(ObjectNameText.getText());
        Order_Bean.setIsBorrowed(OrderBorrowed_CheckBox.isSelected());
        if(!OffsetOrder_CheckBox.isSelected()){
            Order_Bean.setIsOffset(OffsetOrderStatus.未沖帳);
        }else{
            Order_Bean.setIsOffset(offsetStatus_toggleSwitch.switchedOnProperty().get() ? OffsetOrderStatus.全沖帳 : OffsetOrderStatus.部分沖帳);
        }
        Order_Bean.setNumberOfItems(NumberOfItemsText.getText());
        Order_Bean.setTotalPriceNoneTax(TotalPriceNoneTaxText.getText());
        Order_Bean.setTax(TaxText.getText());
        Order_Bean.setDiscount(DiscountText.getText());
        Order_Bean.setTotalPriceIncludeTax(TotalPriceIncludeTaxText.getText());
        Order_Bean.setOrderTaxStatus(OrderTaxStatus);

        if(Order_Bean.getPurchaserName() == null)   Order_Bean.setPurchaserName("");
        if(Order_Bean.getPurchaserTelephone() == null)   Order_Bean.setPurchaserTelephone("");
        if(Order_Bean.getPurchaserCellphone() == null)   Order_Bean.setPurchaserCellphone("");
        if(Order_Bean.getPurchaserAddress() == null)   Order_Bean.setPurchaserAddress("");
        if(Order_Bean.getRecipientName() == null)   Order_Bean.setRecipientName("");
        if(Order_Bean.getRecipientTelephone() == null)   Order_Bean.setRecipientTelephone("");
        if(Order_Bean.getRecipientCellphone() == null)   Order_Bean.setRecipientCellphone("");
        if(Order_Bean.getRecipientAddress() == null)   Order_Bean.setRecipientAddress("");
        if(Order_Bean.getOrderRemark() == null) Order_Bean.setOrderRemark("");
        if(Order_Bean.getCashierRemark() == null) Order_Bean.setCashierRemark("");

        Order_Model.refreshProductSeriesNumber(MainTableView);
        Order_Bean.setProductList(ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
        //  避免貨單參考自己
        if(coverOrderReference){
            HashMap<OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = CallConfig.getOrderReferenceJson();
            if(Order_Bean.getOrderID() == null)
                Order_Bean.setOrderReferenceMap(orderReferenceMap);
            else{
                HashMap<Integer,Boolean> mainOrderReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.報價單);
                HashMap<Integer,Boolean> subBillReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單);
                if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單)
                    subBillReferenceMap.remove(Order_Bean.getOrderID());
                else
                    mainOrderReferenceMap.remove(Order_Bean.getOrderID());

                Order_Bean.setExistOrderReference(mainOrderReferenceMap.size() != 0 || subBillReferenceMap.size() != 0);
                Order_Bean.setOrderReferenceMap(orderReferenceMap);
            }
        }
        Order_Bean.setOrderSource(OrderSource);
        setOrderProjectInfo(Order_Bean);
        if(!isOrderExist()) Order_Bean.setIsCheckout(CheckoutStatus.未結帳.value());
        if((OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.報價單) && !isOrderExist()){
            Order_Bean.setEstablishSource(EstablishSource.人工建立);
        }
    }
    private void setOrderProjectInfo(Order_Bean Order_Bean){
        Order_Bean.setProjectCode(ProjectCodeText.getText());
        Order_Bean.setProjectName(ProjectNameText.getText());
        Order_Bean.setProjectQuantity(ProjectQuantityText.getText());
        Order_Bean.setProjectUnit(ProjectUnitText.getText());
        Order_Bean.setProjectPriceAmount(ProjectPriceAmountText.getText());
        Order_Bean.setProjectTotalPriceNoneTax(ProjectTotalPriceNoneTaxText.getText());
        Order_Bean.setProjectTax(ProjectTaxText.getText());
        Order_Bean.setProjectTotalPriceIncludeTax(ProjectTotalPriceIncludeTaxText.getText());
        Order_Bean.setProjectDifferentPrice(ProjectDifferentPriceText.getText());
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ComponentToolKit.getTableViewItemsSize(MainTableView) != 0){
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if (OrderProduct_Bean != null){
                setProductInfoByTableView(OrderProduct_Bean,false);
                QuantityText.requestFocus();
            }
        }
    }
    /** Button Mouse Clicked - 刪除商品 */
    @FXML protected void DeleteProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(isProductGroupModel()) {
                DialogUI.MessageDialog("※ 請切換回商品模式");
                return;
            }
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if (OrderProduct_Bean != null) {
                if(!isGroupExistThisProduct(OrderProduct_Bean)){
                    ComponentToolKit.removeOrderProductTableViewItem(MainTableView, OrderProduct_Bean);
                    ComponentToolKit.refreshTableViewItemNumber(ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
                    RefreshProductInfoByTableView();
                }else
                    DialogUI.AlarmDialog("※ 刪除被拒，此商品已被加入群組");
            } else DialogUI.MessageDialog("※ 請選擇商品");
        }
    }
    private boolean isGroupExistThisProduct(OrderProduct_Bean OrderProduct_Bean){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
        if(OrderProduct_Bean.getItem_id() == null || productGroupMap == null)
            return false;

        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
            for(Integer item_id : itemMap.keySet()){
                if(OrderProduct_Bean.getItem_id().equals(item_id))
                    return true;
            }
        }
        return false;
    }
    public void RefreshProductInfoByAccordion(){
        if(ComponentToolKit.getAccordionTitledPaneList(ShipmentProductGroupAccordion).size() != 0){
            setProductInfoByAccordion(null);
            UpdatePriceByProductGroup();
            NumberOfItemsText.setText(String.valueOf(ShipmentProductGroupAccordion.getPanes().size()));
        }else   initialProductInfoComponent(true);
    }
    private void RefreshProductInfoByTableView(){
        if(ComponentToolKit.getOrderProductTableViewItemList(MainTableView).size() != 0){
            setProductInfoByTableView(ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView),false);
            UpdatePriceByProduct();
            NumberOfItemsText.setText(String.valueOf(ComponentToolKit.getTableViewItemsSize(MainTableView)));
        }else   initialProductInfoComponent(true);
    }
    /** Button Mouse Clicked - 商品群組 */
    @FXML protected void ModifyProductGroupMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            if(productList.size() == 0)
                DialogUI.MessageDialog("※ 不存在任何商品");
            else {
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(OrderProduct_Bean.getItem_id() == null){
                        DialogUI.AlarmDialog("※ 貨單存在新商品，請先儲存貨單後再重新操作");
                        return;
                    }
                }
                CallFXML.ShowProductGroup(Stage, Order_Bean, this);
            }
        }
    }
    public void setProductInfoByAccordion(ProductGroup_Bean ProductGroup_Bean){
        ISBNText.setText("");
        InternationalCodeText.setText("");
        FirmCodeText.setText("");
        ProductNameText.setText(ProductGroup_Bean == null ? "" : ProductGroup_Bean.getGroupName());
        UnitText.setText(ProductGroup_Bean == null ? "" : ProductGroup_Bean.getUnit());
        PricePercentageSpinner.getValueFactory().setValue(0.0);

        String quantity = ProductGroup_Bean == null ?
                "" : (ProductGroup_Bean.getSmallQuantity() != null ? String.valueOf(ProductGroup_Bean.getSmallQuantity()) : String.valueOf(ProductGroup_Bean.getQuantity())),
                batchPrice = (ProductGroup_Bean == null ? "" : ToolKit.RoundingString(true, ProductGroup_Bean.getBatchPrice())),
                singlePrice = ProductGroup_Bean == null ?
                        "" : (ProductGroup_Bean.getSmallSinglePrice() != null ? String.valueOf(ProductGroup_Bean.getSmallSinglePrice()) : String.valueOf(ProductGroup_Bean.getSinglePrice())),
                pricing = "",
                priceAmount = ProductGroup_Bean == null ?
                        "" : (ProductGroup_Bean.getSmallPriceAmount() != null ? ToolKit.RoundingString(ProductGroup_Bean.getSmallPriceAmount()) : ToolKit.RoundingString(ProductGroup_Bean.getPriceAmount()));
        if(ProductGroup_Bean != null && isTwoFormatOrder()){
            batchPrice = ToolKit.RoundingString(ToolKit.RoundingDouble(batchPrice)*1.05);
            singlePrice = ToolKit.RoundingString(ToolKit.RoundingDouble(singlePrice)*1.05);

            priceAmount = ToolKit.RoundingString(ToolKit.RoundingInteger(quantity)*ToolKit.RoundingDouble(singlePrice));
        }
        QuantityText.setText(quantity);
        BatchPriceText.setText(batchPrice);
        SinglePriceText.setText(singlePrice);
        PricingText.setText(pricing);
        if(ProductGroup_Bean != null) {
            calculatePricePercentage();
        }
        PriceAmountText.setText(priceAmount);
        RemarkText.setText("");
    }
    private void setProductInfoByTableView(OrderProduct_Bean OrderProduct_Bean, boolean scrollTo){
        ISBNText.setText(OrderProduct_Bean.getISBN());
        InternationalCodeText.setText(OrderProduct_Bean.getInternationalCode());
        FirmCodeText.setText(OrderProduct_Bean.getFirmCode());
        ProductNameText.setText(OrderProduct_Bean.getProductName());
        UnitText.setText(OrderProduct_Bean.getUnit());
        String quantity = String.valueOf(OrderProduct_Bean.getQuantity()),
                batchPrice = ToolKit.RoundingString(true,OrderProduct_Bean.getBatchPrice()),
                singlePrice = ToolKit.RoundingString(true,OrderProduct_Bean.getSinglePrice()),
                pricing = ToolKit.RoundingString(true,OrderProduct_Bean.getPricing()),
                priceAmount = ToolKit.RoundingString(OrderProduct_Bean.getPriceAmount());

        if(isTwoFormatOrder()){
            batchPrice = ToolKit.RoundingString(OrderProduct_Bean.getBatchPrice()*1.05);
            singlePrice = ToolKit.RoundingString(OrderProduct_Bean.getSinglePrice()*1.05);
            pricing = ToolKit.RoundingString(OrderProduct_Bean.getPricing()*1.05);

            priceAmount = ToolKit.RoundingString(OrderProduct_Bean.getQuantity()*ToolKit.RoundingDouble(singlePrice));
        }
        QuantityText.setText(quantity);
        BatchPriceText.setText(batchPrice);
        SinglePriceText.setText(singlePrice);
        calculatePricePercentage();
        PricingText.setText(pricing);
        PriceAmountText.setText(priceAmount);
        RemarkText.setText(OrderProduct_Bean.getRemark());
        if(scrollTo){
            int itemNumber = OrderProduct_Bean.getItemNumber();
            if(itemNumber >= 9)
                MainTableView.scrollTo(itemNumber-9);
            else
                MainTableView.scrollTo(0);
        }
    }
    private void calculatePricePercentage(){
        try{
            if(ToolKit.RoundingInteger(BatchPriceText.getText()) == 0)
                PricePercentageSpinner.getValueFactory().setValue(0.0);
            else
                PricePercentageSpinner.getValueFactory().setValue(ToolKit.RoundingDouble(2,Double.parseDouble(SinglePriceText.getText())/Double.parseDouble(BatchPriceText.getText())));
        }catch (Exception Ex){
            DialogUI.AlarmDialog("成本：" + BatchPriceText.getText() + "  單價：" + SinglePriceText.getText());
            DialogUI.ExceptionDialog(Ex);
            ERPApplication.Logger.catching(Ex);
        }
    }
    private void initialProductInfoComponent(boolean clearNumberOfItemsAndTotalAmount){
        ISBNText.setText("");
        InternationalCodeText.setText("");
        FirmCodeText.setText("");
        ProductNameText.setText("");
        SinglePriceText.setText("");
        QuantityText.setText("");
        UnitText.setText("");
        PriceAmountText.setText("");
        BatchPriceText.setText("");
        PricingText.setText("");
        RemarkText.setText("");
        ProfitText.setText("");
        if(clearNumberOfItemsAndTotalAmount){
            NumberOfItemsText.setText("0");
            TotalPriceNoneTaxText.setText("0");
            TaxText.setText("0");
            DiscountText.setText("0");
            TotalPriceIncludeTaxText.setText("0");
        }
    }
    private void initialSubBillComponent() throws Exception{
        OrderTaxStatus = Order_Bean.getOrderTaxStatus();
        ProjectTaxStatus = Order_Bean.getOrderTaxStatus();

        ComponentToolKit.setDatePickerValue(OrderDate_DatePicker, Order_Bean.getOrderDate());
        OrderNumberText.setText(Order_Bean.getNowOrderNumber());
        ObjectIDText.setText(Order_Bean.getObjectID());
        ObjectNameText.setText(Order_Bean.getObjectName());
        ComponentToolKit.setCheckBoxSelect(OrderBorrowed_CheckBox, isBorrowedOrder());
        if(OrderObject == Order_Enum.OrderObject.客戶){
            setOffsetStatusToggleSwitch(Order_Bean.getOffsetOrderStatus());
        }
        ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
        for(OrderProduct_Bean OrderProduct_Bean : ProductList)  ComponentToolKit.getOrderProductTableViewItemList(MainTableView).add(ToolKit.CopyProductBean(OrderProduct_Bean));
        MainTableView.getSelectionModel().selectFirst();
        RefreshProductInfoByTableView();
    }
    private void initialComponentExceptSubBill(boolean initialOrderInfoBean){
        setReviewStatusRadioButtonSelect(ReviewStatus.無);
        if(initialOrderInfoBean)
            ObjectInfo_Bean = null;
        OrderTaxStatus = Order_Enum.OrderTaxStatus.未稅;
        ProjectTaxStatus = Order_Enum.OrderTaxStatus.未稅;

        ComponentToolKit.setDatePickerValue(OrderDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        OrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, ComponentToolKit.getDatePickerValue(OrderDate_DatePicker,"yyyy-MM-dd"),false));
        ObjectIDText.setText("");
        ObjectNameText.setText("");
        ComponentToolKit.setCheckBoxSelect(ShowPricing_CheckBox, false);
        ComponentToolKit.setHBoxVisible(Pricing_HBox, false);
        showProfitHBox(true);
        ComponentToolKit.setCheckBoxSelect(OrderBorrowed_CheckBox, false);
        ComponentToolKit.setCheckBoxSelect(OffsetOrder_CheckBox, false);
        ISBNText.setText("");
        InternationalCodeText.setText("");
        FirmCodeText.setText("");
        ProductNameText.setText("");
        NumberOfItemsText.setText("0");
        SinglePriceText.setText("");
        QuantityText.setText("");
        UnitText.setText("");
        PriceAmountText.setText("");
        TotalPriceNoneTaxText.setText("0");
        TaxText.setText("0");
        DiscountText.setText("0");
        TotalPriceIncludeTaxText.setText("0");
        BatchPriceText.setText("");
        PricingText.setText("");
        RemarkText.setText("");
        ProfitText.setText("");
        ProjectCodeText.setText("");
        ProjectNameText.setText("");
        ProjectQuantityText.setText("");
        ProjectUnitText.setText("");
        ProjectPriceAmountText.setText("");
        ProjectTotalPriceNoneTaxText.setText("");
        ProjectTaxText.setText("");
        ProjectTotalPriceIncludeTaxText.setText("");
        ProjectDifferentPriceText.setText("");
        if(OrderSource == Order_Enum.OrderSource.報價單) initialQuotationComponent();
        ComponentToolKit.getOrderProductTableViewItemList(MainTableView).clear();
    }
    private void initialQuotationComponent(){
        InvoiceDateText.setText("");
        InvoiceNumberText.setText("");
    }
    private void setOrderInfo(Order_Bean Order_Bean){
        ReviewStatus ReviewStatus = Order_Bean.getReviewStatusMap().get(Order_Enum.ReviewObject.貨單商品);
        setReviewStatusRadioButtonSelect(ReviewStatus);

        Order_Bean.setOldOrderNumber(Order_Bean.getNowOrderNumber());
        ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
        MainTableView.setItems(ProductList);
        MainTableView.getSelectionModel().selectFirst();
        if(ProductList.size() != 0)  setProductInfoByTableView(ProductList.get(0),false);

        if(Order_Bean.getTax() == null || Order_Bean.getTax().equals("") || Order_Bean.getTax().equals("0")) OrderTaxStatus = Order_Enum.OrderTaxStatus.未稅;
        else    OrderTaxStatus = Order_Enum.OrderTaxStatus.含稅;
        if(Order_Bean.getProjectTax() == null || Order_Bean.getProjectTax().equals("") || Order_Bean.getProjectTax().equals("0"))  ProjectTaxStatus = Order_Enum.OrderTaxStatus.未稅;
        else    ProjectTaxStatus = Order_Enum.OrderTaxStatus.含稅;
        Order_Bean.setOrderTaxStatus(OrderTaxStatus);

        ComponentToolKit.setDatePickerValue(OrderDate_DatePicker, Order_Bean.getOrderDate());
        OrderNumberText.setText(Order_Bean.getNowOrderNumber());
        ObjectIDText.setText(Order_Bean.getObjectID());
        ObjectNameText.setText(Order_Bean.getObjectName());
        setShowOrderPictureButtonDisable(!Order_Bean.isExistPicture());
        ComponentToolKit.setButtonDisable(ShowOrderReference_Button,!Order_Bean.isExistOrderReference());
        ComponentToolKit.setCheckBoxSelect(ShowPricing_CheckBox, false);
        ComponentToolKit.setHBoxVisible(Pricing_HBox, false);

        showProfitHBox(true);
        ComponentToolKit.setCheckBoxSelect(OrderBorrowed_CheckBox, Order_Bean.isBorrowed().value());
        NumberOfItemsText.setText(Order_Bean.getNumberOfItems());
        TotalPriceNoneTaxText.setText(Order_Bean.getTotalPriceNoneTax());
        TaxText.setText(Order_Bean.getTax());
        DiscountText.setText(Order_Bean.getDiscount() == null ? "0" : Order_Bean.getDiscount());
        TotalPriceIncludeTaxText.setText(Order_Bean.getTotalPriceIncludeTax());

        if(Order_Bean.getOrderRemark() != null && !Order_Bean.getOrderRemark().equals(""))
            ComponentToolKit.setButtonStyle(OrderRemarkButton,"-fx-background-color: " + ToolKit.getGreen_BackgroundColor());
        if(Order_Bean.getCashierRemark() != null && !Order_Bean.getCashierRemark().equals(""))
            ComponentToolKit.setButtonStyle(CashierRemarkButton,"-fx-background-color: " + ToolKit.getGreen_BackgroundColor());

        boolean isExistInvoiceManufacturer = false;
        if(Order_Bean.getInvoiceDate() != null && !Order_Bean.getInvoiceDate().equals("")) {
            InvoiceDateText.setText(Order_Bean.getInvoiceDate());
            InvoiceNumberText.setText(Order_Bean.getInvoiceNumber());
            ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountComboBoxItemList(ExportQuotationVendor_ComboBox);
            for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
                if(IAECrawlerAccount_Bean.getAccount() != null && IAECrawlerAccount_Bean.getAccount().equals(Order_Bean.getInvoice_exportQuotation_Account())){
                    ExportQuotationVendor_ComboBox.getSelectionModel().select(IAECrawlerAccount_Bean);
                    isExistInvoiceManufacturer = true;
                    break;
                }
            }
        }
        if(!isExistInvoiceManufacturer){
            if(Order_Bean.getExportQuotation_ManufacturerId() == null)
                ExportQuotationVendor_ComboBox.getSelectionModel().selectFirst();
            else{
                ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountComboBoxItemList(ExportQuotationVendor_ComboBox);
                for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
                    if(IAECrawlerAccount_Bean.getExportQuotation_id() == Order_Bean.getExportQuotation_ManufacturerId()){
                        ExportQuotationVendor_ComboBox.getSelectionModel().select(IAECrawlerAccount_Bean);
                        break;
                    }
                }
            }
        }
        ProfitText.setText(Order_Model.calculateProfit(OrderTaxStatus,ComponentToolKit.getOrderProductTableViewItemList(MainTableView),TotalPriceIncludeTaxText.getText()));
        ProjectCodeText.setText(Order_Bean.getProjectCode());
        ProjectNameText.setText(Order_Bean.getProjectName());
        ProjectQuantityText.setText(Order_Bean.getProjectQuantity());
        ProjectUnitText.setText(Order_Bean.getProjectUnit());
        ProjectPriceAmountText.setText(Order_Bean.getProjectPriceAmount());
        ProjectTotalPriceNoneTaxText.setText(Order_Bean.getProjectTotalPriceNoneTax());
        ProjectTaxText.setText(Order_Bean.getProjectTax());
        ProjectTotalPriceIncludeTaxText.setText(Order_Bean.getProjectTotalPriceIncludeTax());
        ProjectDifferentPriceText.setText(Order_Bean.getProjectDifferentPrice());

        if(OrderObject == Order_Enum.OrderObject.客戶) {
            if(shipmentProductGroup_toggleSwitch == null)
                shipmentProductGroup_toggleSwitch = createProductGroupSwitchButton(Order_Bean);
            if(Order_Bean.getProductGroupMap() != null){
                refreshProductGroupDifferencePrice();
            }
            setExistProductGroupButtonStyle(Order_Bean.getProductGroupMap() != null);
            setOffsetStatusToggleSwitch(Order_Bean.getOffsetOrderStatus());
        }

        if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單) {
            setTransferOrderInfo(Order_Bean);
            setAlreadyOrderInfo(Order_Bean);
        }
    }
    private void setTransferOrderInfo(Order_Bean Order_Bean){
        if(OrderObject == Order_Enum.OrderObject.廠商){
            if(isTransferWaitingOrder()){
                ComponentToolKit.setDatePickerValue(WaitingPurchaseOrderDate_DatePicker, Order_Bean.getWaitingOrderDate());
                WaitingPurchaseOrderNumberText.setText(Order_Bean.getWaitingOrderNumber());
            }else{
                ComponentToolKit.setDatePickerValue(WaitingPurchaseOrderDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
                WaitingPurchaseOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, ComponentToolKit.getDatePickerValue(WaitingPurchaseOrderDate_DatePicker,"yyyy-MM-dd"),false));
            }
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            if(isTransferWaitingOrder()){
                ComponentToolKit.setDatePickerValue(WaitingShipmentOrderDate_DatePicker, Order_Bean.getWaitingOrderDate());
                WaitingShipmentOrderNumberText.setText(Order_Bean.getWaitingOrderNumber());
            }else{
                ComponentToolKit.setDatePickerValue(WaitingShipmentOrderDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
                WaitingShipmentOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, ComponentToolKit.getDatePickerValue(WaitingShipmentOrderDate_DatePicker,"yyyyMMdd"),false));
            }
        }
    }
    private void setAlreadyOrderInfo(Order_Bean Order_Bean){
        if(OrderObject == Order_Enum.OrderObject.廠商){
            if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && isTransferAlreadyOrder()){
                ComponentToolKit.setDatePickerValue(PurchaseOrderDate_DatePicker, Order_Bean.getAlreadyOrderDate());
                PurchaseOrderNumberText.setText(Order_Bean.getAlreadyOrderNumber());
            }else if((OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.進貨子貨單) && !isTransferAlreadyOrder()){
                ComponentToolKit.setDatePickerValue(PurchaseOrderDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
                PurchaseOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, ComponentToolKit.getDatePickerValue(PurchaseOrderDate_DatePicker,"yyyy-MM-dd"),false));
            }
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            if(OrderSource != Order_Enum.OrderSource.出貨退貨單 && isTransferAlreadyOrder()){
                ComponentToolKit.setDatePickerValue(ShipmentOrderDate_DatePicker, Order_Bean.getAlreadyOrderDate());
                ShipmentOrderNumberText.setText(Order_Bean.getAlreadyOrderNumber());
            }else if((OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) && !isTransferAlreadyOrder()){
                ComponentToolKit.setDatePickerValue(ShipmentOrderDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
                ShipmentOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, ComponentToolKit.getDatePickerValue(ShipmentOrderDate_DatePicker,"yyyy-MM-dd"),false));
            }
        }
    }
    /** Button Mouse Clicked - 查詢功能 */
    @FXML protected void SearchFunctionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseRightClicked(MouseEvent))
            CallFXML.ShowSearchOrderOverview(OrderObject);
        else if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            for(Order_SearchFunction Order_SearchFunction : searchFunctionRadioButtonMap.keySet()){
                if(searchFunctionRadioButtonMap.get(Order_SearchFunction).isSelected()){
                    if(Order_SearchFunction == SystemDefaultConfig_Enum.Order_SearchFunction.TransactionDetail)
                        openTransactionDetail(Order_SearchFunction);
                    else if(Order_SearchFunction == SystemDefaultConfig_Enum.Order_SearchFunction.ProductWaitingConfirm)
                        openProductWaitingConfirm(Order_SearchFunction);
                    break;
                }
            }
        }
    }
    /** 查詢商品交易明細 */
    private void openTransactionDetail(Order_SearchFunction Order_SearchFunction){
        OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        if (OrderProduct_Bean != null){
            SystemDefaultConfigJson.put(SystemDefaultConfig_Enum.Order_SearchFunction.class.getSimpleName(), Order_SearchFunction.ordinal());
            if(CallConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.Order_SearchFunction, SystemDefaultConfigJson)){
                SearchFunction_SplitMenuButton.setText(Order_SearchFunction.getDescribe());
                CallFXML.ShowTransactionDetail(Stage,null,this);
            }else{
                returnSearchFunctionToDefaultConfig(Order_SearchFunction);
                DialogUI.AlarmDialog("※ 【預設值】更新失敗");
            }
        }else{
            returnSearchFunctionToDefaultConfig(Order_SearchFunction);
            DialogUI.MessageDialog("※ 請選擇商品");
        }
    }
    /** 待確認商品 */
    private void openProductWaitingConfirm(Order_SearchFunction Order_SearchFunction){
        SystemDefaultConfigJson.put(SystemDefaultConfig_Enum.Order_SearchFunction.class.getSimpleName(), Order_SearchFunction.ordinal());
        if(CallConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.Order_SearchFunction, SystemDefaultConfigJson)){
            SearchFunction_SplitMenuButton.setText(Order_SearchFunction.getDescribe());
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            CallFXML.ShowProductWaitConfirm_NewStage(Stage, OrderProduct_Bean);
        }else{
            returnSearchFunctionToDefaultConfig(Order_SearchFunction);
            DialogUI.AlarmDialog("※ 【預設值】更新失敗");
        }
    }
    private void returnSearchFunctionToDefaultConfig(Order_SearchFunction Order_SearchFunction){
        Integer defaultConfig = SystemSetting_Model.getSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.Order_SearchFunction);
        ComponentToolKit.setRadioButtonSelect(searchFunctionRadioButtonMap.get(Order_SearchFunction),false);
        if(defaultConfig != null)
            ComponentToolKit.setRadioButtonSelect(searchFunctionRadioButtonMap.get(SystemDefaultConfig_Enum.Order_SearchFunction.values()[defaultConfig]),true);
    }
    public OrderProduct_Bean getSelectProductBean(){
        return ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
    }
    public void refreshProductInStockByTransactionDetail(int newInStock){
        OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
        OrderProduct_Bean.setInStock(newInStock);
        MainTableView.refresh();
    }
    /** Button Mouse Clicked - 訂購/收件資訊 */
    @FXML protected void PurchaserRecipientInfoMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            CallFXML.ShowPurchaserRecipient(Stage, this, OrderExist, Order_Bean);
    }
    /** Button Mouse Clicked - 匯出/列印 */
    @FXML protected void PrintFunctionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            for(Order_PrintFunction Order_PrintFunction : printFunctionRadioButtonMap.keySet()){
                if(printFunctionRadioButtonMap.get(Order_PrintFunction).isSelected()){
                    if(Order_PrintFunction == SystemDefaultConfig_Enum.Order_PrintFunction.PrintOrder)
                        openPrintOrder(Order_PrintFunction);
                    else if(Order_PrintFunction == SystemDefaultConfig_Enum.Order_PrintFunction.PrintLabel)
                        openPrintLabel(Order_PrintFunction);
                    break;
                }
            }
        }
    }
    private void openPrintOrder(Order_PrintFunction Order_PrintFunction){
        String ProjectName = ProjectNameText.getText();
        String OrderNumber = OrderNumberText.getText();
        if(ToolKit.isFileNameExistEscapeCharacter(ProjectName)){
            DialogUI.AlarmDialog("※ 檔案名稱不允許存在跳脫字元「/\\:*?\"><|」");
            return;
        }
        SystemDefaultConfigJson.put(SystemDefaultConfig_Enum.Order_PrintFunction.class.getSimpleName(), Order_PrintFunction.ordinal());
        if(CallConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.Order_PrintFunction, SystemDefaultConfigJson)){
            PrintFunction_SplitMenuButton.setText(Order_PrintFunction.getDescribe());
            try {
                String FileName = OrderNumber + "-" + ProjectName + ".xls";
                String ExportPath = CallConfig.getFile_OutputPath();
                FileChooser FileChooser = ComponentToolKit.setFileChooser("選擇匯出位置");
                FileChooser.setInitialDirectory(new File(ExportPath));
                FileChooser.setInitialFileName(FileName);
                FileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xls", "*.xls"));
                File SavePath = FileChooser.showSaveDialog(Stage);
                String outputFilePath;
                if (SavePath != null) outputFilePath = SavePath.getAbsolutePath();
                else return;

                ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                if (ProductList.size() == 0) DialogUI.MessageDialog("※ 未新增商品");
                else {
                    ObservableList<ExportOrder_Bean> ExportOrderProductList = FXCollections.observableArrayList();
                    for (OrderProduct_Bean OrderProduct_Bean : ProductList) {
                        ExportOrder_Bean ExportOrder_Bean = new ExportOrder_Bean();
                        ExportOrder_Bean.setItemNumber(OrderProduct_Bean.getItemNumber());
                        ExportOrder_Bean.setISBN(OrderProduct_Bean.getISBN());
                        ExportOrder_Bean.setProductName(OrderProduct_Bean.getProductName());
                        ExportOrder_Bean.setQuantity(OrderProduct_Bean.getQuantity());
                        ExportOrder_Bean.setUnit(OrderProduct_Bean.getUnit());
                        if(OrderObject == Order_Enum.OrderObject.廠商)    ExportOrder_Bean.setSalePrice(OrderProduct_Bean.getBatchPrice());
                        else if(OrderObject == Order_Enum.OrderObject.客戶)   ExportOrder_Bean.setSalePrice(OrderProduct_Bean.getSinglePrice());
                        ExportOrder_Bean.setPriceAmount(OrderProduct_Bean.getPriceAmount());
                        ExportOrder_Bean.setRemark(OrderProduct_Bean.getRemark());
                        ExportOrderProductList.add(ExportOrder_Bean);
                    }
                    ObjectSearchColumn ObjectSearchColumn = null;
                    if(OrderObject == Order_Enum.OrderObject.廠商)    ObjectSearchColumn = Order_Enum.ObjectSearchColumn.廠商編號;
                    else if(OrderObject == Order_Enum.OrderObject.客戶)    ObjectSearchColumn = Order_Enum.ObjectSearchColumn.客戶編號;
                    ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject, ObjectSearchColumn, Order_Bean.getObjectID());
                    CallFXML.ShowExportOrder(Stage, outputFilePath, Order_Bean, ObjectList.get(0), ExportOrderProductList);
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                if(Ex.toString().contains("Folder parameter must be a valid folder"))   DialogUI.AlarmDialog("※ 匯出路徑設定錯誤 !");
                else    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }else{
            returnPrintFunctionToDefaultConfig(Order_PrintFunction);
            DialogUI.AlarmDialog("※ 【預設值】更新失敗");
        }
    }
    private void openPrintLabel(Order_PrintFunction Order_PrintFunction){
        SystemDefaultConfigJson.put(SystemDefaultConfig_Enum.Order_PrintFunction.class.getSimpleName(), Order_PrintFunction.ordinal());
        if(CallConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.Order_PrintFunction, SystemDefaultConfigJson)){
            PrintFunction_SplitMenuButton.setText(Order_PrintFunction.getDescribe());
            CallJAR.Label_Printer(ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView).getISBN());
        }else{
            returnPrintFunctionToDefaultConfig(Order_PrintFunction);
            DialogUI.AlarmDialog("※ 【預設值】更新失敗");
        }
    }
    private void returnPrintFunctionToDefaultConfig(Order_PrintFunction Order_PrintFunction){
        Integer defaultConfig = SystemSetting_Model.getSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.Order_PrintFunction);
        ComponentToolKit.setRadioButtonSelect(printFunctionRadioButtonMap.get(Order_PrintFunction),false);
        if(defaultConfig != null)
            ComponentToolKit.setRadioButtonSelect(printFunctionRadioButtonMap.get(SystemDefaultConfig_Enum.Order_PrintFunction.values()[defaultConfig]),true);
    }
    public void showProductGroupScrollerPane(boolean visible){
        MainTableView.setVisible(!visible);
        if(OrderObject == Order_Enum.OrderObject.客戶) {
            if(visible){    //  金額更新成群組
                RefreshProductInfoByAccordion();
                setReviewStatusRadioButtonSelect(Order_Bean.getReviewStatusMap().get(Order_Enum.ReviewObject.商品群組));
            }else{  //  金額更新成貨單
                RefreshProductInfoByTableView();
                setReviewStatusRadioButtonSelect(Order_Bean.getReviewStatusMap().get(Order_Enum.ReviewObject.貨單商品));
            }
            ComponentToolKit.setScrollerPaneVisible(ShipmentProductGroupScrollPane, visible);
        }
    }
    @FXML protected void ExportPayableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
            SearchOrder_Bean.setOrderSource(OrderSource);
            SearchOrder_Bean.setOrderNumber(WaitingPurchaseOrderNumberText.getText());
            SearchOrder_Bean.setOrderDate(ComponentToolKit.getDatePickerValue(WaitingPurchaseOrderDate_DatePicker,"yyyy-MM-dd"));
            SearchOrder_Bean.setTotalPriceIncludeTax(ToolKit.RoundingInteger(TotalPriceIncludeTaxText.getText()));
            SearchOrder_Bean.setProjectName(ProjectNameText.getText());

            ObservableList<SearchOrder_Bean> selectOrderList = FXCollections.observableArrayList();
            selectOrderList.add(SearchOrder_Bean);
            PayableReceivable_Bean PayableReceivable_Bean = createPayableReceivableBean(selectOrderList);
            String outputFile = ManagePayableReceivable_Model.exportPayableReceivable(OrderObject, ObjectInfo_Bean, PayableReceivable_Bean);
            if(outputFile != null) DialogUI.AlarmDialog("※ 匯出成功，檔名：" + outputFile);
            else    DialogUI.MessageDialog("※ 匯出失敗");
        }
    }
    private PayableReceivable_Bean createPayableReceivableBean(ObservableList<SearchOrder_Bean> selectOrderList){
        SystemSettingConfig_Bean SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();

        PayableReceivable_Bean PayableReceivable_Bean = new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderObject(OrderObject);
        PayableReceivable_Bean.setCheckNumber(SystemSettingConfig_Bean.getNowCheckNumber() != null ? SystemSettingConfig_Bean.getNowCheckNumber() : "");

        ObservableList<BankInfo_Bean> allBankList = ManagePayableReceivable_Model.getAllBankInfo(true);
        for(BankInfo_Bean BankInfo_Bean : allBankList){
            if(BankInfo_Bean.getBankID() != null && BankInfo_Bean.getBankID().equals(ObjectInfo_Bean.getBankID())){
                PayableReceivable_Bean.setObjectBankBean(BankInfo_Bean);
                break;
            }
        }
        PayableReceivable_Bean.setObjectBankBranch(ObjectInfo_Bean.getBankBranch());
        PayableReceivable_Bean.setObjectBankAccount(ObjectInfo_Bean.getBankAccount());
        PayableReceivable_Bean.setObjectAccountName(ObjectInfo_Bean.getAccountName());

        int TotalPriceIncludeTax = Integer.parseInt(TotalPriceIncludeTaxText.getText());
        int Cash = 0, CashDiscount = 0, CheckPrice = 0;
        int Deposit = 0, OtherDiscount = 0, Discount = 0;
        if(ObjectInfo_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.支票){
            if(ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣)
                Discount = ObjectInfo_Bean.getPostage();
            CheckPrice = TotalPriceIncludeTax - Discount;

            Cash = 0;
            CashDiscount = 0;
        }else if(ObjectInfo_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.現金){
            double CashDiscountPercentage = ObjectInfo_Bean.getPayableReceivableDiscount();
            CashDiscount = TotalPriceIncludeTax - ToolKit.RoundingInteger(CashDiscountPercentage * TotalPriceIncludeTax);
//            Cash = TotalPriceIncludeTax - CashDiscount;

            if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣 && ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣){
                if(Cash + Deposit + ObjectInfo_Bean.getRemittanceFee() + ObjectInfo_Bean.getPostage() + CashDiscount == TotalPriceIncludeTax) {
                    Discount = ObjectInfo_Bean.getRemittanceFee();
                    Cash = TotalPriceIncludeTax - CashDiscount - ObjectInfo_Bean.getRemittanceFee();
                }else if(Cash + Deposit + ObjectInfo_Bean.getRemittanceFee() + CashDiscount == TotalPriceIncludeTax)  Discount = ObjectInfo_Bean.getRemittanceFee();
                else    Discount = ObjectInfo_Bean.getRemittanceFee() + ObjectInfo_Bean.getPostage();
            }else if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣)    Discount = ObjectInfo_Bean.getRemittanceFee();
            else if(ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣 && Cash + Deposit + CashDiscount != TotalPriceIncludeTax)  Discount = ObjectInfo_Bean.getPostage();

            CheckPrice = 0;
        }
        PayableReceivable_Bean.setCash(ToolKit.RoundingString(Cash));
        PayableReceivable_Bean.setDeposit("0");
        PayableReceivable_Bean.setCashDiscount(ToolKit.RoundingString(CashDiscount));
        PayableReceivable_Bean.setCheckPrice(ToolKit.RoundingString(CheckPrice));
        PayableReceivable_Bean.setTotalPriceIncludeTax(ToolKit.RoundingString(TotalPriceIncludeTax));
        PayableReceivable_Bean.setOrderList(selectOrderList);
        return PayableReceivable_Bean;
    }
    @FXML protected void installmentOnAction(){
        if(!Order_Model.isMainOrderExistSubBill(OrderObject, Order_Bean.getWaitingOrderNumber(),true,true)){
            ArrayList<Installment_Bean> installmentList = Order_Model.getInstallmentByOrderId(Order_Bean.getOrderID());
            CallFXML.ShowInstallment(Stage,this, Order_Bean, TotalPriceIncludeTaxText.getText(), installmentList);
        }else{
            DialogUI.AlarmDialog("不允許建立，存在【已結帳】子貨單");
        }
    }
    public void setEstablishPayReceiveMenuItemDisable(){
//       (1) 待入倉(出貨)單如果有建立分期收/付款，不管有無子貨單，都可以【建立應收/付帳款】
//       (2) 待入倉(出貨)單如果沒有建立分期收/付款，則有建立子貨單(未轉進、出貨)會無法【建立應收/付帳款】
        boolean disable;
        if(isOrderCheckout()){
            disable = true;
        }else if(Order_Bean.isExistInstallment()){
            disable = false;
        }else{
            boolean isExist = Order_Model.isMainOrderExistSubBill(OrderObject, Order_Bean.getWaitingOrderNumber(),null,null);
            if(!isExist){
                disable = false;
            }else{
                isExist = Order_Model.isMainOrderExistSubBill(OrderObject, Order_Bean.getWaitingOrderNumber(),true,false);
                disable = !isExist;
            }
        }
        if(OrderObject == Order_Enum.OrderObject.廠商 && OrderSource == Order_Enum.OrderSource.待入倉單){
            ComponentToolKit.setMenuItemDisable(establishPurchasePay_menuItem, disable);
        }else if(OrderObject == Order_Enum.OrderObject.客戶 && OrderSource == Order_Enum.OrderSource.待出貨單){
            ComponentToolKit.setMenuItemDisable(establishShipmentReceive_menuItem, disable);
        }
    }
    @FXML protected void establishPayableReceivableOnAction(){
        establishPayableReceivable();
    }
    /** Button Mouse Clicked - 建立應收應付帳款 */
    @FXML protected void establishPayableReceivableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            establishPayableReceivable();
        }
    }
    private void establishPayableReceivable(){
        if(OrderSource != Order_Enum.OrderSource.採購單 && OrderSource != Order_Enum.OrderSource.報價單 &&
                OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單){
            if((OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) &&
                    Order_Model.isMainOrderExistInstallment(OrderObject, Order_Bean.getWaitingOrderNumber())){
                DialogUI.AlarmDialog("不允許建立，母貨單存在【 分期" + (OrderSource.getOrderObject() == Order_Enum.OrderObject.廠商 ? "付" : "收") + "款 】");
            }else{
                CallFXML.EstablishPayableReceivable_NewStage(OrderObject, PayableReceivableStatus.建立,null,null, setPayableReceivable_Bean(OrderSource),null);
            }
        }
    }
    private PayableReceivable_Bean setPayableReceivable_Bean(OrderSource orderSource){
        PayableReceivable_Bean PayableReceivable_Bean = new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderDate(Order_Bean.getAlreadyOrderDate() != null ? Order_Bean.getAlreadyOrderDate() : Order_Bean.getWaitingOrderDate());
        PayableReceivable_Bean.setOrderObject(orderSource.getOrderObject());
        PayableReceivable_Bean.setObjectID(Order_Bean.getObjectID());
        return PayableReceivable_Bean;
    }
    @FXML protected void searchPurchaseOrderOnAction(){
        CallFXML.SearchOrderProgress_NewStage(Order_Enum.OrderObject.廠商);
    }
    @FXML protected void searchShipmentOrderOnAction(){
        CallFXML.SearchOrderProgress_NewStage(Order_Enum.OrderObject.客戶);
    }
    /** DatePicker On Action - 轉待進/出貨 */
    @FXML protected void TransferWaitingOrderDateOnAction(){
        if(OrderObject == Order_Enum.OrderObject.廠商){
            String WaitingOrderDate = ComponentToolKit.getDatePickerValue(WaitingPurchaseOrderDate_DatePicker, "yyyy-MM-dd");
            WaitingPurchaseOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, WaitingOrderDate,false));
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            String WaitingOrderDate = ComponentToolKit.getDatePickerValue(WaitingShipmentOrderDate_DatePicker, "yyyy-MM-dd");
            WaitingShipmentOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, WaitingOrderDate,false));
        }
    }
    /** DatePicker On Action - 轉進/出貨 */
    @FXML protected void TransferAlreadyOrderDateOnAction(){
        if(OrderObject == Order_Enum.OrderObject.廠商){
            String AlreadyOrderDate = ComponentToolKit.getDatePickerValue(PurchaseOrderDate_DatePicker, "yyyy-MM-dd");
            PurchaseOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, AlreadyOrderDate,false));
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            String AlreadyOrderDate = ComponentToolKit.getDatePickerValue(ShipmentOrderDate_DatePicker, "yyyy-MM-dd");
            ShipmentOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, AlreadyOrderDate,false));
        }
    }
    /** Button Mouse Clicked - 轉待進/出貨 */
    @FXML protected void TransferWaitingOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        //  處理庫存量，轉採購 ....
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(OrderObject == Order_Enum.OrderObject.廠商 && Order_Model.isOrderTransferWaitingOrAlready(Order_Enum.OrderSource.待入倉單, Order_Bean.getOrderID(), Order_Bean.getNowOrderNumber())){
                DialogUI.MessageDialog("※ 此貨單已轉待入倉，請重新開啟!");
                return;
            }else if(OrderObject == Order_Enum.OrderObject.客戶 && Order_Model.isOrderTransferWaitingOrAlready(Order_Enum.OrderSource.待出貨單, Order_Bean.getOrderID(), Order_Bean.getNowOrderNumber())){
                DialogUI.MessageDialog("※ 此貨單已轉待出貨，請重新開啟!");
                return;
            }

            HashMap<ReviewObject,ReviewStatus> orderReviewStatusMap = Order_Bean.getReviewStatusMap();
            if(orderReviewStatusMap.get(ReviewObject.貨單商品) == ReviewStatus.待修改 || orderReviewStatusMap.get(ReviewObject.貨單商品) == ReviewStatus.待審查 ||
                    orderReviewStatusMap.get(ReviewObject.商品群組) == ReviewStatus.待修改 || orderReviewStatusMap.get(ReviewObject.商品群組) == ReviewStatus.待審查){
                if(OrderObject == Order_Enum.OrderObject.廠商)
                    DialogUI.MessageDialog("※ 當前【審查狀態】不允許轉待入倉");
                else if(OrderObject == Order_Enum.OrderObject.客戶)
                    DialogUI.MessageDialog("※ 當前【審查狀態】不允許轉待出貨");
                return;
            }
            if(OrderObject == Order_Enum.OrderObject.廠商){   //  直接轉待入倉 and 處理庫存
                String OrderDate = ComponentToolKit.getDatePickerValue(WaitingPurchaseOrderDate_DatePicker, "yyyy-MM-dd");
                String OrderNumber = WaitingPurchaseOrderNumberText.getText();
                if(Order_Model.isOrderNumberExist(Order_Enum.OrderSource.待入倉單, OrderNumber))
                    DialogUI.MessageDialog("※ 待入倉單號重複!");
                else
                    TransferWaitingPurchase(OrderDate, OrderNumber);
            }else if(OrderObject == Order_Enum.OrderObject.客戶){ //  判斷庫存
                if(!isOffsetOrder()){
                    if(isShipmentInStockEnough(ComponentToolKit.getOrderProductTableViewItemList(MainTableView)))
                        TransferWaitingShipment();
                    else if(DialogUI.AlarmDialog("※ 商品庫存不足!"))
                        generatePurchaseQuotation(ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
                }else {
                    //  先找進貨廠商，找不到就失敗
                    ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(Order_Enum.OrderObject.廠商, ObjectSearchColumn.廠商編號,"999");
                    if(ObjectList.size() == 0)
                        DialogUI.AlarmDialog("※ 轉待出貨失敗，未搜尋到沖帳採購單之廠商");
                    else {
                        TransferWaitingShipment();
                        establishPurchaseOffsetOrder(ObjectList.get(0), ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
                    }
                }
            }
        }
    }
    /**  刷新商品銷售狀態的目的：當報價單庫存不足轉採購時，如果沒有刷新銷售狀態就直接將採購單轉待入倉時會出問題 */
    private void TransferWaitingPurchase(String OrderDate, String OrderNumber){
        if (Order_Model.transferWaitingOrder(OrderDate, OrderNumber, Order_Bean, ComponentToolKit.getOrderProductTableViewItemList(MainTableView))) {
            ComponentToolKit.closeThisStage(Stage);
            ERPApplication.Logger.info("※ [成功] 轉「待入倉單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
            ERPApplication.Logger.info("---------------------------------------------------");
            DialogUI.MessageDialog("※ 轉待入倉成功!");
            refreshSearchOrderView();
        }else{
            ERPApplication.Logger.warn("※ [失敗] 轉「待入倉單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
            ERPApplication.Logger.info("---------------------------------------------------");
            DialogUI.MessageDialog("※ 轉待入倉失敗!");
        }
    }
    private boolean isShipmentInStockEnough(ObservableList<OrderProduct_Bean> ProductList) {
        for (OrderProduct_Bean OrderProduct_Bean : ProductList) {
            if(OrderSource == Order_Enum.OrderSource.報價單) {
                if (OrderProduct_Bean.getWaitingIntoInStock() != 0) { //  有待入庫存
                    if(OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getWaitingIntoInStock() + OrderProduct_Bean.getInStock())
                        return false;
//                    if (OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getWaitingIntoInStock() + OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock())
//                        return false;
                } else if (OrderProduct_Bean.getWaitingShipmentQuantity() != 0){   //  有待出貨並且待入庫存為0，判斷庫存夠不夠所有待出貨
                    if(OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock() - OrderProduct_Bean.getWaitingShipmentQuantity())
                        return false;
                }else if(OrderProduct_Bean.getInStock() <= OrderProduct_Bean.getSafetyStock())
                    return false;
                else if(OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock())
                    return false;
            }else if(OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                if (OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getInStock())
                    return false;
            }
        }
        return true;
    }
    public void refreshWaitingOrderNumber(){
        WaitingShipmentOrderNumberText.setText(Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.日期, ComponentToolKit.getDatePickerValue(WaitingShipmentOrderDate_DatePicker,"yyyyMMdd"),false));
    }
    public void TransferWaitingShipment(){
        String OrderDate = ComponentToolKit.getDatePickerValue(WaitingShipmentOrderDate_DatePicker, "yyyy-MM-dd");
        String OrderNumber = WaitingShipmentOrderNumberText.getText();
        if(Order_Model.isOrderNumberExist(Order_Enum.OrderSource.待出貨單, OrderNumber)) {
            DialogUI.MessageDialog("※ 待出貨單號重複!");
            return;
        }
        if(Order_Model.transferWaitingOrder(OrderDate, OrderNumber, Order_Bean, ComponentToolKit.getOrderProductTableViewItemList(MainTableView))) {
            ComponentToolKit.closeThisStage(Stage);
            ERPApplication.Logger.info("※ [成功] 轉「待出貨單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
            ERPApplication.Logger.info("---------------------------------------------------");
            DialogUI.MessageDialog("※ 轉待出貨成功!");
            refreshSearchOrderView();
        }else{
            ERPApplication.Logger.warn("※ [失敗] 轉「待出貨單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
            ERPApplication.Logger.info("---------------------------------------------------");
            DialogUI.MessageDialog("※ 轉待出貨失敗!");
        }
    }
    /**  建立採購單    */
    private void generatePurchaseQuotation(ObservableList<OrderProduct_Bean> ProductList) throws Exception{
        ObservableList<OrderProduct_Bean> PurchaseQuotationProductList = FXCollections.observableArrayList();

        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            boolean isInStockEnough = false;
            OrderProduct_Bean CopyProduct_Bean = ToolKit.CopyProductBean(OrderProduct_Bean);
            if(OrderProduct_Bean.getWaitingIntoInStock() != 0) {
                if(CopyProduct_Bean.getQuantity() > CopyProduct_Bean.getWaitingIntoInStock())
                    CopyProduct_Bean.setQuantity(CopyProduct_Bean.getQuantity() - CopyProduct_Bean.getWaitingIntoInStock());
                else
                    isInStockEnough = true;
            }else if(OrderProduct_Bean.getNeededPurchaseQuantity() != 0)  //  需量不等於0：代表如果存量不足，安全存量的數量已經補齊
                CopyProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity());
            else if(OrderProduct_Bean.getWaitingIntoInStock() == 0 && OrderProduct_Bean.getWaitingPurchaseQuantity() != 0)  //  已經叫貨，並補齊存量不足安全存量的數量
                CopyProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity());
            else if(OrderProduct_Bean.getWaitingShipmentQuantity() != 0){   //  此時有待出貨，沒有待入庫存，判斷存量是否足夠所有待出貨
                int canShipmentQuantity = OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock() - OrderProduct_Bean.getWaitingShipmentQuantity();
                if(canShipmentQuantity >= OrderProduct_Bean.getQuantity())
                    isInStockEnough = true;
                else
                    CopyProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity() - canShipmentQuantity);
            }else{   // 補齊存量不足安全存量的數量
                int canShipmentQuantity = OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock();
                if(canShipmentQuantity >= OrderProduct_Bean.getQuantity())
                    isInStockEnough = true;
                CopyProduct_Bean.setQuantity(OrderProduct_Bean.getQuantity() - canShipmentQuantity);
            }
            if(!isInStockEnough) {
                CopyProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(CopyProduct_Bean.getQuantity() * CopyProduct_Bean.getBatchPrice()));
                PurchaseQuotationProductList.add(CopyProduct_Bean);
            }
        }
        CallFXML.ShowSeparatePurchaseManufacturer(Stage, ProjectCodeText.getText(), ProjectNameText.getText(), PurchaseQuotationProductList, this, SearchOrder_Controller);
    }
    private void establishPurchaseOffsetOrder(ObjectInfo_Bean ObjectInfo_Bean, ObservableList<OrderProduct_Bean> ProductList){
        Order_Bean Order_Bean = RecordAlreadyPurchaseOrderInfo(ObjectInfo_Bean, ProductList);
        if (Order_Model.insertOrder(Order_Bean.getOrderSource(), Order_Bean,null,false,false)) {
            if(DialogUI.ConfirmDialog("※ 已建立沖帳用進貨單，是否開啟 ?",true,false,0,0)){
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(OrderStatus.有效貨單, Order_Bean.getOrderSource(), Order_Enum.OrderExist.已存在, Order_Bean,null,true);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        } else DialogUI.MessageDialog("※ 沖帳用進貨單建立失敗");
    }
    private Order_Bean RecordAlreadyPurchaseOrderInfo(ObjectInfo_Bean ObjectInfo_Bean, ObservableList<OrderProduct_Bean> ProductList){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderDate(ComponentToolKit.getDatePickerValue(WaitingShipmentOrderDate_DatePicker, "yyyy-MM-dd"));
        Order_Bean.setNowOrderNumber(Order_Model.generateNewestOrderNumberOfEstablishOrder(Order_Enum.OrderSource.採購單, Order_Enum.generateOrderNumberMethod.日期, Order_Bean.getOrderDate(),false));
        Order_Bean.setObjectID(ObjectInfo_Bean.getObjectID());
        Order_Bean.setObjectName(ObjectInfo_Bean.getObjectName());
        Order_Bean.setIsBorrowed(false);
        Order_Bean.setIsOffset(OffsetOrderStatus.全沖帳);
        Order_Bean.setNumberOfItems(String.valueOf(ProductList.size()));
        Order_Bean.setTotalPriceNoneTax(TotalPriceNoneTaxText.getText());
        Order_Bean.setTax(TaxText.getText());
        Order_Bean.setDiscount(DiscountText.getText());
        Order_Bean.setTotalPriceIncludeTax(TotalPriceIncludeTaxText.getText());
        Order_Bean.setProductList(ProductList);
        Order_Bean.setOrderSource(Order_Enum.OrderSource.採購單);
        Order_Bean.setProjectCode("");
        Order_Bean.setProjectName("※ " + ProjectNameText.getText());
        Order_Bean.setProjectQuantity("");
        Order_Bean.setProjectUnit("");
        Order_Bean.setProjectPriceAmount("");
        Order_Bean.setProjectTotalPriceNoneTax("");
        Order_Bean.setProjectTax("");
        Order_Bean.setProjectTotalPriceIncludeTax("");
        Order_Bean.setProjectDifferentPrice("");
        Order_Bean.setPurchaserName("");
        Order_Bean.setPurchaserTelephone("");
        Order_Bean.setPurchaserCellphone("");
        Order_Bean.setPurchaserAddress("");
        Order_Bean.setRecipientName("");
        Order_Bean.setRecipientTelephone("");
        Order_Bean.setRecipientCellphone("");
        Order_Bean.setRecipientAddress("");
        Order_Bean.setOrderRemark("");
        Order_Bean.setCashierRemark("");
        Order_Bean.setIsCheckout(CheckoutStatus.未結帳.value());
        Order_Bean.setWaitingOrderDate(Order_Bean.getOrderDate());
        Order_Bean.setWaitingOrderNumber(String.valueOf(Long.parseLong(Order_Bean.getNowOrderNumber())+1));
        Order_Bean.setAlreadyOrderDate(Order_Bean.getOrderDate());
        Order_Bean.setAlreadyOrderNumber(String.valueOf(Long.parseLong(Order_Bean.getNowOrderNumber())+2));
        Order_Bean.setEstablishSource(EstablishSource.系統建立);

        Order_Bean.setReviewStatusMap(Order_Enum.ReviewObject.貨單商品,Order_Enum.ReviewStatus.無);
        Order_Bean.setReviewStatusMap(Order_Enum.ReviewObject.商品群組,Order_Enum.ReviewStatus.無);

        HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = new HashMap<>();
        orderReferenceMap.put(Order_Enum.OrderSource.報價單,new HashMap<>());
        orderReferenceMap.put(Order_Enum.OrderSource.出貨子貨單,new HashMap<>());
        Order_Bean.setOrderReferenceMap(orderReferenceMap);
        return Order_Bean;
    }
    @FXML protected void cancelOrderProductMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<OrderProduct_Bean> canCancelOrderProductList = FXCollections.observableArrayList();

            ObservableList<OrderProduct_Bean> RemainProductList = getRemainProductList(ComponentToolKit.getOrderProductTableViewItemList(MainTableView), Order_Model.findSubBill_AllItems(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber(),false));
            if(RemainProductList != null){
                if(!isExistRemainProduct(RemainProductList)) {
                    DialogUI.MessageDialog("※ 無可註銷商品，請確認子貨單!");
                    return;
                }else{
                    for(OrderProduct_Bean OrderProduct_Bean : RemainProductList){
                        if(OrderProduct_Bean.getQuantity() != 0)    canCancelOrderProductList.add(OrderProduct_Bean);
                    }
                }
            }else{
                canCancelOrderProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            }
            CallFXML.ShowCancelOrderProduct(Stage,Order_Bean,canCancelOrderProductList,this);
        }
    }
    /** Button Mouse Clicked - 轉進/出貨 */
    @FXML protected void TransferAlreadyOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(OrderObject == Order_Enum.OrderObject.廠商 && Order_Model.isOrderTransferWaitingOrAlready(Order_Enum.OrderSource.進貨單, Order_Bean.getOrderID(), Order_Bean.getNowOrderNumber())){
                DialogUI.MessageDialog("※ 此貨單已轉進貨，請重新開啟!");
                return;
            }else if(OrderObject == Order_Enum.OrderObject.客戶 && Order_Model.isOrderTransferWaitingOrAlready(Order_Enum.OrderSource.出貨單, Order_Bean.getOrderID(), Order_Bean.getNowOrderNumber())){
                DialogUI.MessageDialog("※ 此貨單已轉出貨，請重新開啟!");
                return;
            }
            boolean isHandleProductSaleStatus = false;
            if(!isOffsetOrder())    isHandleProductSaleStatus = true;
            String OrderDate = "", OrderNumber = "";
            if(OrderObject == Order_Enum.OrderObject.廠商){
                OrderDate = ComponentToolKit.getDatePickerValue(PurchaseOrderDate_DatePicker,"yyyy-MM-dd");
                OrderNumber = PurchaseOrderNumberText.getText();
            }else if(OrderObject == Order_Enum.OrderObject.客戶){
                OrderDate = ComponentToolKit.getDatePickerValue(ShipmentOrderDate_DatePicker,"yyyy-MM-dd");
                OrderNumber = ShipmentOrderNumberText.getText();
            }


            if(OrderSource == Order_Enum.OrderSource.進貨子貨單){
                if(Order_Model.isOrderNumberExist(Order_Enum.OrderSource.進貨單, OrderNumber))
                    DialogUI.MessageDialog("※ 出貨單號重複!");
                else
                    handlePurchaseSubBillTransferToAlreadyOrder(OrderDate, OrderNumber, isHandleProductSaleStatus);
            }else if(OrderSource == Order_Enum.OrderSource.待入倉單) {
                if(Order_Model.isOrderNumberExist(Order_Enum.OrderSource.進貨單, OrderNumber))
                    DialogUI.MessageDialog("※ 進貨單號重複!");
                else
                    handleWaitingPurchaseTransferToAlreadyOrder(OrderDate, OrderNumber, isHandleProductSaleStatus);
            }else if (OrderSource == Order_Enum.OrderSource.出貨子貨單)
                if(Order_Model.isOrderNumberExist(Order_Enum.OrderSource.出貨單, OrderNumber))
                    DialogUI.MessageDialog("※ 出貨單號重複!");
                else
                    handleShipmentSubBillTransferToAlreadyOrder(OrderDate, OrderNumber, isHandleProductSaleStatus);
            else if (OrderSource == Order_Enum.OrderSource.待出貨單){
                if(Order_Model.isOrderNumberExist(Order_Enum.OrderSource.出貨單, OrderNumber))
                    DialogUI.MessageDialog("※ 出貨單號重複!");
                else
                    handleWaitingShipmentTransferToAlreadyOrder(OrderDate, OrderNumber, isHandleProductSaleStatus);
            }
        }
    }
    private void handleWaitingPurchaseTransferToAlreadyOrder(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus) throws Exception{
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        if (isBorrowedOrder() && DialogUI.AlarmDialog("※ 此為借測單，將直接轉進貨 !")) {
            TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
        }else if(ProductList.size() == 1 && ProductList.get(0).getQuantity() == 1 && DialogUI.AlarmDialog("※ 此為單一品數量貨單，將直接轉進貨 !")){
            TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
        }else{
            ObservableList<OrderProduct_Bean> RemainProductList = getRemainProductList(ComponentToolKit.getOrderProductTableViewItemList(MainTableView), Order_Model.findSubBill_AllItems(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber(),false));
            if (RemainProductList == null) {  //  詢問建立子貨單
                if (DialogUI.ConfirmDialog("※ 是否建立子貨單 ?",false,false,0,0))
                    establishSubBill(Order_Enum.OrderSource.進貨子貨單);
                else
                    TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
            }else if(isExistRemainProduct(RemainProductList)){  //  判斷主貨單有無剩餘品項未分配子貨單
                establishRemainProductOrder(Order_Enum.OrderSource.進貨子貨單, RemainProductList);
            }else{
                ArrayList<Order_Bean> SubBillList = Order_Model.isOrderExistSubBillAndSubBillNoneTransferToAlreadyShipment(OrderObject, Order_Bean.getWaitingOrderNumber());
                if (SubBillList != null) {
                    if (DialogUI.ConfirmDialog("※ 尚有未轉出貨之子貨單 ! 是否開啟 ?",true,true,4,8)) openNoneTransferToAlreadyOrder(Order_Enum.OrderSource.進貨子貨單, SubBillList);
                } else  TransferAlreadyOrder(OrderDate, OrderNumber,false);
            }
        }
    }
    private void handlePurchaseSubBillTransferToAlreadyOrder(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus) throws Exception{
        Order_Model.refreshProductSaleStatus(Order_Bean.getProductList());
        TransferAlreadyOrder(OrderDate, OrderNumber, isHandleProductSaleStatus);
        ObservableList<OrderProduct_Bean> MainOrderProductList = Order_Model.getMainOrderItemsOfSubBill(OrderObject, Order_Bean.getWaitingOrderNumber());
        ObservableList<OrderProduct_Bean> RemainProductList = getRemainProductList(MainOrderProductList, Order_Model.findSubBill_AllItems(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber(),true));
        if(!isExistRemainProduct(RemainProductList)){
            //     將待入倉轉進貨
            this.Order_Bean = Order_Model.getOrderInfo(Order_Enum.OrderSource.待入倉單, this.Order_Bean);
            String AlreadyShipmentOrder = String.valueOf(Long.parseLong(OrderNumber)+1);
            TransferAlreadyOrder(OrderDate, AlreadyShipmentOrder,false);
        }
    }
    private void handleShipmentSubBillTransferToAlreadyOrder(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus) throws Exception{
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        if(!isOffsetOrder() && !isShipmentInStockEnough(ProductList))   DialogUI.AlarmDialog("※ 轉出貨失敗，庫存不足 !");
        else{
            Order_Model.refreshProductSaleStatus(Order_Bean.getProductList());
            TransferAlreadyOrder(OrderDate, OrderNumber, isHandleProductSaleStatus);
            if(isOffsetOrder()){
                //  判斷金額，主動將母貨單轉出貨
                int mainOrderTotalPriceIncludeTax = Order_Model.getTotalPriceIncludeTaxFromMainOrderBySubBill(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber());
                int subBillTotalPriceIncludeTax = Order_Model.getAllSubBillTotalPriceIncludeTax(OrderSource, Order_Bean.getWaitingOrderNumber(),true,null);
                if(subBillTotalPriceIncludeTax == mainOrderTotalPriceIncludeTax){
                    //     將待出貨轉出貨
                    this.Order_Bean = Order_Model.getOrderInfo(Order_Enum.OrderSource.待出貨單, this.Order_Bean);
                    String AlreadyShipmentOrder = String.valueOf(Long.parseLong(OrderNumber)+1);
                    TransferAlreadyOrder(OrderDate, AlreadyShipmentOrder,false);
                }
            }else{
                ObservableList<OrderProduct_Bean> MainOrderProductList = Order_Model.getMainOrderItemsOfSubBill(OrderObject, Order_Bean.getWaitingOrderNumber());
                ObservableList<OrderProduct_Bean> RemainProductList = getRemainProductList(MainOrderProductList, Order_Model.findSubBill_AllItems(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber(),true));
                if(!isExistRemainProduct(RemainProductList)){
                    //     將待出貨轉出貨
                    this.Order_Bean = Order_Model.getOrderInfo(Order_Enum.OrderSource.待出貨單, this.Order_Bean);
                    String AlreadyShipmentOrder = String.valueOf(Long.parseLong(OrderNumber)+1);
                    TransferAlreadyOrder(OrderDate, AlreadyShipmentOrder,false);
                }
            }
        }
    }
    private void handleWaitingShipmentTransferToAlreadyOrder(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus) throws Exception{
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        if (isBorrowedOrder() && DialogUI.AlarmDialog("※ 此為借測單，將直接轉出貨 !")) {         // 借測單不會有沖帳
            if(!isShipmentInStockEnough(ProductList))   DialogUI.AlarmDialog("※ 轉出貨失敗，庫存不足 !");
            else    TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
        }else if(!isOffsetOrder() && ProductList.size() == 1 && ProductList.get(0).getQuantity() == 1 && DialogUI.AlarmDialog("※ 此為單一品數量貨單，將直接轉出貨 !")){
            if(!isShipmentInStockEnough(ProductList))   DialogUI.AlarmDialog("※ 轉出貨失敗，庫存不足 !");
            else    TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
        }else{
            if(isOffsetOrder())
                judgeSubBillByTotalPrice(OrderDate,OrderNumber,isHandleProductSaleStatus);
            else
                judgeSubBillByProduct(OrderDate,OrderNumber,ProductList,isHandleProductSaleStatus);
        }
    }
    private void judgeSubBillByTotalPrice(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus){
        boolean isMainOrderExistSubBill = Order_Model.isMainOrderExistSubBill(Order_Bean.getOrderSource().getOrderObject(), Order_Bean.getWaitingOrderNumber(),null,null);
        if(!isMainOrderExistSubBill){
            if (DialogUI.ConfirmDialog("※ 是否建立子貨單 ?",false,false,0,0))
                establishSubBill(Order_Enum.OrderSource.出貨子貨單);
            else
                TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
        }else{
            int subBillTotalPriceIncludeTax = Order_Model.getAllSubBillTotalPriceIncludeTax(OrderSource, Order_Bean.getWaitingOrderNumber(),false,null);
            if(subBillTotalPriceIncludeTax < ToolKit.RoundingInteger(Order_Bean.getTotalPriceIncludeTax()))
                establishRemainProductOrder(Order_Enum.OrderSource.出貨子貨單, ComponentToolKit.getOrderProductTableViewItemList(MainTableView));
            else{
                ArrayList<Order_Bean> SubBillList = Order_Model.isOrderExistSubBillAndSubBillNoneTransferToAlreadyShipment(OrderObject, Order_Bean.getWaitingOrderNumber());
                if (SubBillList != null) {
                    if (DialogUI.ConfirmDialog("※ 尚有未轉出貨之子貨單 ! 是否開啟 ?",true,true,4,8)) openNoneTransferToAlreadyOrder(Order_Enum.OrderSource.出貨子貨單, SubBillList);
                } else  TransferAlreadyOrder(OrderDate, OrderNumber,false);
            }
        }
    }
    private void judgeSubBillByProduct(String OrderDate, String OrderNumber, ObservableList<OrderProduct_Bean> ProductList, boolean isHandleProductSaleStatus) throws Exception {
        ObservableList<OrderProduct_Bean> RemainProductList = getRemainProductList(ComponentToolKit.getOrderProductTableViewItemList(MainTableView), Order_Model.findSubBill_AllItems(Order_Bean.getOrderSource(), Order_Bean.getWaitingOrderNumber(),false));
        if (RemainProductList == null) {  //  詢問建立子貨單
            if (DialogUI.ConfirmDialog("※ 是否建立子貨單 ?",false,false,0,0))
                establishSubBill(Order_Enum.OrderSource.出貨子貨單);
            else {
                if(!isOffsetOrder() && !isShipmentInStockEnough(ProductList))   DialogUI.AlarmDialog("※ 轉出貨失敗，庫存不足 !");
                else    TransferAlreadyOrder(OrderDate, OrderNumber,isHandleProductSaleStatus);
            }
        }else if(isExistRemainProduct(RemainProductList)){  //  判斷主貨單有無剩餘品項未分配子貨單
            establishRemainProductOrder(Order_Enum.OrderSource.出貨子貨單, RemainProductList);
        }else{
            ArrayList<Order_Bean> SubBillList = Order_Model.isOrderExistSubBillAndSubBillNoneTransferToAlreadyShipment(OrderObject, Order_Bean.getWaitingOrderNumber());
            if (SubBillList != null) {
                if (DialogUI.ConfirmDialog("※ 尚有未轉出貨之子貨單 ! 是否開啟 ?",true,true,4,8)) openNoneTransferToAlreadyOrder(Order_Enum.OrderSource.出貨子貨單, SubBillList);
            } else  TransferAlreadyOrder(OrderDate, OrderNumber,false);
        }
    }
    private boolean compareOrderAndOpen() throws Exception{
        Order_Bean DBOrder_Bean = ToolKit.CopyOrderBean(Order_Bean);
        getOrderInfo(DBOrder_Bean);
        ArrayList<String> differentOrderInfoList = new ArrayList<>();
        Field[] beforeFields = DBOrder_Bean.getClass().getDeclaredFields();
        Field[] afterFields = Order_Bean.getClass().getDeclaredFields();
        Field.setAccessible(beforeFields, true);
        Field.setAccessible(afterFields, true);
        if(beforeFields.length > 0){
            for(int i = 0; i < beforeFields.length; i++){
                Object beforeValue = beforeFields[i].get(DBOrder_Bean);
                Object afterValue = afterFields[i].get(Order_Bean);
//                if((beforeValue != null && afterValue == null) || (beforeValue == null && afterValue != null) || (beforeValue != null && afterValue != null && !beforeValue.equals(afterValue))){
                if((beforeValue != null && afterValue == null) || (beforeValue == null && afterValue != null) || (beforeValue != null && !beforeValue.equals(afterValue))){
                    if(beforeFields[i].getName().equals("ProductList")){
                        HashMap<String, ModifyProductQuantity_Bean> ModifyProductQuantityMap = findModifyProduct(DBOrder_Bean.getProductList());
                        if(ModifyProductQuantityMap.size() != 0)
                            differentOrderInfoList.add(beforeFields[i].getName());
                    }else if(!beforeFields[i].getName().equals("productGroupMap") && !beforeFields[i].getName().equals("UpdateDateTime")){
                        differentOrderInfoList.add(beforeFields[i].getName());
                    }
                }
            }
        }
        if(differentOrderInfoList.size() != 0 && DialogUI.ConfirmDialog("※ 貨單主要資訊與上次不符，是否開啟比對結果 ?",true,true,4,13)) {
            EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(OrderStatus.比對貨單, OrderSource, Order_Enum.OrderExist.已存在, DBOrder_Bean, differentOrderInfoList,false);
            CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            return true;
        }
        return false;
    }
    private HashMap<String, ModifyProductQuantity_Bean> findModifyProduct(ObservableList<OrderProduct_Bean> dbOldOrderItems){
        HashMap<String, ModifyProductQuantity_Bean> ModifyProductQuantityMap = new HashMap<>();
        ObservableList<OrderProduct_Bean> NowOrderItems = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        if(NowOrderItems.size() >= dbOldOrderItems.size()){
            for(OrderProduct_Bean newOrderProduct_Bean : NowOrderItems){
                boolean isProductExist = false;
                for(int index = 0 ; index < dbOldOrderItems.size() ; index++){   //     一直add 存在 remove
                    OrderProduct_Bean oldOrderProduct_Bean = dbOldOrderItems.get(index);
                    if(newOrderProduct_Bean.getISBN().equals(oldOrderProduct_Bean.getISBN())){ //  商品還存在於舊的 DB
                        isProductExist = true;
                        if(!newOrderProduct_Bean.getProductName().equals(oldOrderProduct_Bean.getProductName())){
                            ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,newOrderProduct_Bean));
                            break;
                        }else if(!newOrderProduct_Bean.getInternationalCode().equals(oldOrderProduct_Bean.getInternationalCode())){
                            ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,newOrderProduct_Bean));
                            break;
                        }else if(!newOrderProduct_Bean.getFirmCode().equals(oldOrderProduct_Bean.getFirmCode())){
                            ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,newOrderProduct_Bean));
                            break;
                        }else if(newOrderProduct_Bean.getQuantity() != oldOrderProduct_Bean.getQuantity()){
                            ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,newOrderProduct_Bean));
                            break;
                        }else if(newOrderProduct_Bean.getSinglePrice() != oldOrderProduct_Bean.getSinglePrice()){
                            ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,newOrderProduct_Bean));
                            break;
                        }
                    }
                    if(index == dbOldOrderItems.size()-1 && !isProductExist){     //  代表是新商品
                        ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),null,newOrderProduct_Bean));
                    }
                }
                if(!isProductExist && !ModifyProductQuantityMap.containsKey(newOrderProduct_Bean.getISBN())) {   //  商品不存在於舊的 DB
                    ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),newOrderProduct_Bean,null));
                }
            }
        }else{  // 品項減少
            for(OrderProduct_Bean oldOrderProduct_Bean : dbOldOrderItems){
                boolean isProductExist = false;
                for(OrderProduct_Bean newOrderProduct_Bean : NowOrderItems){   //     一直add 存在 remove
                    if(oldOrderProduct_Bean.getISBN().equals(newOrderProduct_Bean.getISBN())){ //  商品還存在於舊的 DB
                        isProductExist = true;
                        if(oldOrderProduct_Bean.getQuantity() != newOrderProduct_Bean.getQuantity()){
                            ModifyProductQuantityMap.put(newOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(newOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,newOrderProduct_Bean));
                            break;
                        }
                    }
                }
                if(!isProductExist && !ModifyProductQuantityMap.containsKey(oldOrderProduct_Bean.getISBN())) {   //  商品不存在於舊的 DB
                    ModifyProductQuantityMap.put(oldOrderProduct_Bean.getISBN(),new ModifyProductQuantity_Bean(oldOrderProduct_Bean.getISBN(),oldOrderProduct_Bean,null));
                }
            }
        }
        return ModifyProductQuantityMap;
    }
    private void establishSubBill(OrderSource OrderSource){
        CallFXML.ShowSeparateOrder(Stage, OrderObject, OrderSource, ComponentToolKit.getOrderProductTableViewItemList(MainTableView), Order_Bean, SearchOrder_Controller);
    }
    private void openNoneTransferToAlreadyOrder(OrderSource OrderSource, ArrayList<Order_Bean> SeparateOrderNoneTransferShipmentList){
        for (Order_Bean Order_Bean : SeparateOrderNoneTransferShipmentList) {
            EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(OrderStatus.有效貨單, OrderSource, Order_Enum.OrderExist.已存在, Order_Bean,null,true);
            CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
        }
    }
    private EstablishOrder_NewStage_Bean createEstablishOrderNewStageBean(OrderStatus orderStatus, OrderSource orderSource, OrderExist orderExist, Order_Bean order_bean, ArrayList<String> differentOrderInfoList, boolean setSearchController){
        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
        establishOrder_NewStage_Bean.setOrderStatus(orderStatus);
        establishOrder_NewStage_Bean.setOrderSource(orderSource);
        establishOrder_NewStage_Bean.setOrderExist(orderExist);
        establishOrder_NewStage_Bean.setOrder_Bean(order_bean);
        establishOrder_NewStage_Bean.setDifferentOrderInfoList(differentOrderInfoList);
        establishOrder_NewStage_Bean.setSearchOrder_Controller(setSearchController ? SearchOrder_Controller : null);
        establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(setSearchController ? SearchOrderProgress_Controller : null);
        return establishOrder_NewStage_Bean;
    }
    private void establishRemainProductOrder(OrderSource OrderSource, ObservableList<OrderProduct_Bean> RemainProductList){
        if (DialogUI.AlarmDialog("※ 尚有品項未分配給子貨單，請按「確定」來分配剩餘品項 !"))
            CallFXML.ShowSeparateOrder(Stage, OrderObject, OrderSource, RemainProductList, Order_Bean, SearchOrder_Controller);
    }
    private void TransferAlreadyOrder(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus){
        if(OrderObject == Order_Enum.OrderObject.廠商){
            if(TransferAlreadyPurchase(OrderDate, OrderNumber, isHandleProductSaleStatus)) {
                ERPApplication.Logger.info("※ [成功] " + Order_Bean.getOrderSource() + "轉「進貨單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 轉進貨成功!");
            }else{
                ERPApplication.Logger.warn("※ [失敗] " + Order_Bean.getOrderSource() + "轉「進貨單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 轉進貨失敗!");
            }
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            if(TransferAlreadyShipment(OrderDate, OrderNumber, isHandleProductSaleStatus)){
                ERPApplication.Logger.info("※ [成功] " + Order_Bean.getOrderSource() + "轉「出貨單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 轉出貨成功!");
            }else{
                ERPApplication.Logger.warn("※ [失敗] " + Order_Bean.getOrderSource() + "轉「出貨單 : " + OrderNumber + "(" + Order_Bean.getNowOrderNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 轉出貨失敗!");
            }
        }
    }
    private boolean TransferAlreadyPurchase(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus){
        if(Order_Model.transferAlreadyOrder(OrderDate, OrderNumber, Order_Bean, ComponentToolKit.getOrderProductTableViewItemList(MainTableView), isHandleProductSaleStatus)){
            ComponentToolKit.closeThisStage(Stage);
            refreshSearchOrderView();
            return true;
        }
        return false;
    }
    private boolean TransferAlreadyShipment(String OrderDate, String OrderNumber, boolean isHandleProductSaleStatus){
        if(Order_Model.transferAlreadyOrder(OrderDate, OrderNumber, Order_Bean, ComponentToolKit.getOrderProductTableViewItemList(MainTableView), isHandleProductSaleStatus)){
            ComponentToolKit.closeThisStage(Stage);
            refreshSearchOrderView();
            return true;
        }
        return false;
    }
    private ObservableList<OrderProduct_Bean> getRemainProductList(ObservableList<OrderProduct_Bean> ProductList, HashMap<Integer, Integer> SeparateOrderItemsMap) throws Exception{
        if(SeparateOrderItemsMap == null) return null;
        for(Integer itemNumber : SeparateOrderItemsMap.keySet()){
            ERPApplication.Logger.info("已存在子貨單項號:" + itemNumber + "  數量:" + SeparateOrderItemsMap.get(itemNumber));
        }
        ObservableList<OrderProduct_Bean> RemainProductList = FXCollections.observableArrayList();
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            int Remain;
            if(SeparateOrderItemsMap.containsKey(OrderProduct_Bean.getItemNumber()))   Remain = OrderProduct_Bean.getQuantity() - SeparateOrderItemsMap.get(OrderProduct_Bean.getItemNumber());
            else   Remain = OrderProduct_Bean.getQuantity();
            OrderProduct_Bean.setCheckBoxSelect(false);
            OrderProduct_Bean CopyProductBean = ToolKit.CopyProductBean(OrderProduct_Bean);
            CopyProductBean.setQuantity(Remain);
            RemainProductList.add(CopyProductBean);
        }
        return RemainProductList;
    }
    private boolean isExistRemainProduct(ObservableList<OrderProduct_Bean> RemainProductList){
        for(OrderProduct_Bean OrderProduct_Bean : RemainProductList)
            if(OrderProduct_Bean.getQuantity() != 0)    return true;
        return false;
    }
    public void refreshSearchOrderView(){
        if(SearchOrder_Controller != null && SearchOrderProgress_Controller == null)  SearchOrder_Controller.callOrderSearch();
        else if(SearchOrder_Controller == null && SearchOrderProgress_Controller != null)   SearchOrderProgress_Controller.callOrderSearch();
    }
    public void initialOrderReference(){
        if(!CallConfig.setOrderReferenceJson(new HashMap<>(),new HashMap<>()))
            DialogUI.MessageDialog("※ 貨單參考檔案初始化失敗");
        else
            coverOrderReference = false;
    }
    private void LockComponent(){
        ComponentToolKit.setDatePickerDisable(OrderDate_DatePicker,true);
        ComponentToolKit.setTextFieldEditable(ObjectIDText, Order_Bean.isCheckout() == CheckoutStatus.未結帳);
        ComponentToolKit.setTextFieldEditable(ObjectNameText,Order_Bean.isCheckout() == CheckoutStatus.未結帳);
        ComponentToolKit.setCheckBoxDisable(OrderBorrowed_CheckBox,true);
        ComponentToolKit.setHBoxDisable(Offset_HBox,true);
        ComponentToolKit.setTextFieldEditable(ISBNText,false);
        ComponentToolKit.setTextFieldEditable(InternationalCodeText,false);
        ComponentToolKit.setTextFieldEditable(FirmCodeText,false);
        ComponentToolKit.setTextFieldEditable(ProductNameText,false);
        ComponentToolKit.setTextFieldEditable(NumberOfItemsText,false);
        ComponentToolKit.setTextFieldEditable(SinglePriceText,Order_Bean.isCheckout() == CheckoutStatus.未結帳);
        ComponentToolKit.setTextFieldEditable(QuantityText,false);
        ComponentToolKit.setTextFieldEditable(UnitText,false);
        ComponentToolKit.setTextFieldEditable(TotalPriceIncludeTaxText,false);
        ComponentToolKit.setTextFieldEditable(RemarkText,Order_Bean.isCheckout() == CheckoutStatus.未結帳);
        if (this.OrderObject == Order_Enum.OrderObject.廠商 && (this.OrderSource != Order_Enum.OrderSource.採購單 || isTransferWaitingOrder())) {
            this.ComponentToolKit.setButtonDisable(this.DeleteModifyPurchaseOrderProduct_Button, true);
        } else if (this.OrderObject == Order_Enum.OrderObject.客戶 && (this.OrderSource != Order_Enum.OrderSource.報價單 || isTransferWaitingOrder())) {
            this.ComponentToolKit.setButtonDisable(this.DeleteModifyShipmentOrderProduct_Button, true);
            this.ComponentToolKit.setButtonDisable(this.ModifyShipmentProductGroup_Button, true);
        }
    }
    public boolean isTransferWaitingOrder(){
        return Order_Bean.getWaitingOrderDate() != null && Order_Bean.getWaitingOrderNumber() != null;
    }
    private boolean isTransferAlreadyOrder(){
        return Order_Bean.getAlreadyOrderDate() != null && Order_Bean.getAlreadyOrderNumber() != null;
    }
    private boolean isOrderCheckout(){
        return Order_Bean.isCheckout().value();
    }
    private boolean isNoneCheckoutOrder(){
        return !Order_Bean.isCheckout().value();
    }
    private boolean isBorrowedOrder(){  return Order_Bean.isBorrowed().value(); }
    private OffsetOrderStatus getOffsetOrderStatus(){   return Order_Bean.getOffsetOrderStatus();   }
    private boolean isOffsetOrder(){    return Order_Bean.getOffsetOrderStatus().value();   }
    private boolean isTwoFormatOrder(){
        return TwoFormat_RadioButton.isSelected();
    }
    private boolean isProductGroupModel(){
        if(shipmentProductGroup_toggleSwitch != null) {
            boolean isSwitch = shipmentProductGroup_toggleSwitch.switchedOnProperty().get();
            if(isSwitch){
                TitledPane TitledPane = ComponentToolKit.getAccordionTitledPaneExpandedPane(ShipmentProductGroupAccordion);
                if(TitledPane == null){
                    setProductInfoByAccordion(null);
                }else{
                    int index = 1;
                    HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
                    for(Integer group_itemNumber : productGroupMap.keySet()){
                        ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                        if(TitledPane.getId().equals(String.valueOf(index))) {
                            setProductInfoByAccordion(ProductGroup_Bean);
                            break;
                        }
                        index = index + 1;
                    }
                }
            }
            return isSwitch;
        }else
            return false;
    }
    private ReviewObject getReviewObject(){
        if(isOrderExist()){
            return OrderObject == Order_Enum.OrderObject.客戶 && shipmentProductGroup_toggleSwitch.switchedOnProperty().get() ?
                    Order_Enum.ReviewObject.商品群組 : Order_Enum.ReviewObject.貨單商品;
        }else
            return Order_Enum.ReviewObject.貨單商品;
    }
    private void setReviewStatusRadioButtonSelect(ReviewStatus ReviewStatus){
        if(ReviewStatus == Order_Enum.ReviewStatus.無){
            ComponentToolKit.setRadioButtonDisable(NoneOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(WaitingOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(WaitingOrderModifyStatus_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(OrderReviewFinished_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,true);
            OrderReviewStatus_SplitMenuButton.setText(ReviewStatus.name());
        }else if(ReviewStatus == Order_Enum.ReviewStatus.待審查){
            ComponentToolKit.setRadioButtonDisable(WaitingOrderReviewStatus_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(NoneOrderReviewStatus_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(WaitingOrderModifyStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(OrderReviewFinished_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,true);
            OrderReviewStatus_SplitMenuButton.setText(ReviewStatus.name());
        }else if(ReviewStatus == Order_Enum.ReviewStatus.待修改){
            ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(NoneOrderReviewStatus_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(WaitingOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(OrderReviewFinished_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton, true);
            OrderReviewStatus_SplitMenuButton.setText(ReviewStatus.name());
        }else if(ReviewStatus == Order_Enum.ReviewStatus.審查通過){
            ComponentToolKit.setRadioButtonDisable(OrderReviewFinished_RadioButton, true);
            ComponentToolKit.setRadioButtonSelect(NoneOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(NoneOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(WaitingOrderReviewStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonSelect(WaitingOrderModifyStatus_RadioButton,false);
            ComponentToolKit.setRadioButtonDisable(WaitingOrderModifyStatus_RadioButton,true);
            ComponentToolKit.setRadioButtonSelect(OrderReviewFinished_RadioButton, true);
            OrderReviewStatus_SplitMenuButton.setText(ReviewStatus.name());
        }
    }
    private void showPurchasePriceTableColumnVisible(){
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn_Purchase,true);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn_Purchase,isOrderExist() && Order_Bean.getEstablishSource() == EstablishSource.系統建立);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn_ReturnOrder,true);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn_ReturnOrder,false);
    }
    private void showShipmentPriceTableColumnVisible(){
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn_Shipment,false);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn_Shipment,true);
        ComponentToolKit.setTableColumnVisible(BatchPriceColumn_ReturnOrder,false);
        ComponentToolKit.setTableColumnVisible(SinglePriceColumn_ReturnOrder,true);
    }
    private void showProfitHBox(boolean visible){
        if(OrderObject == Order_Enum.OrderObject.廠商) {
            ComponentToolKit.setCheckBoxSelect(ShowProfit_CheckBox,false);
            ComponentToolKit.setHBoxVisible(Profit_HBox,false);
        }else if(OrderObject == Order_Enum.OrderObject.客戶) {
            ComponentToolKit.setCheckBoxSelect(ShowProfit_CheckBox,visible);
            ComponentToolKit.setHBoxVisible(Profit_HBox, visible);
        }
    }
    private void setIsExistProductCodeColumnTextFill(TableColumn<OrderProduct_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIsExistProductCodeTextFill(Alignment, FontSize));
    }
    private class setIsExistProductCodeTextFill extends TableCell<OrderProduct_Bean, String> {
        String Alignment, FontSize;
        setIsExistProductCodeTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String productCode, boolean empty) {
            super.updateItem(productCode, empty);
            if (productCode == null || empty) {
                setText(null);
                setStyle("-fx-background-color:white");
            } else {
                setText("");
                if(!productCode.equals(""))
                    setStyle("-fx-background-color:red");
                else
                    setStyle("-fx-background-color:white");
            }
        }
    }
    private void setItemNumberColumnBackgroundColor(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setItemNumberTextFill(Alignment, FontSize));
    }
    private class setItemNumberTextFill extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        setItemNumberTextFill(String Alignment, String FontSize){
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
                if(differentOrderInfoList != null && differentOrderInfoList.size() != 0){
                    for(String differentColumn : differentOrderInfoList){
                        if (differentColumn.equals("ProductList")){
                            setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: CENTER-LEFT;-fx-background-color:" + ToolKit.getPink_BackgroundColor());
                        }
                    }
                }
            }
        }
    }
    private void setIntegerPriceColumnMicrometerFormat(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIntegerPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setIntegerPriceColumnMicrometerFormat extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        setIntegerPriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
            }
        }
    }
    private void setDoublePriceColumnMicrometerFormat(TableColumn<OrderProduct_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setDoublePriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setDoublePriceColumnMicrometerFormat extends TableCell<OrderProduct_Bean, Double> {
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
    private void setRemarkColumnTextFill(TableColumn<OrderProduct_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setRemarkColumnCellFactory(Alignment, FontSize));
    }
    class setRemarkColumnCellFactory extends TableCell<OrderProduct_Bean, String> {
        String Alignment, FontSize;
        setRemarkColumnCellFactory(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String remark, boolean empty) {
            super.updateItem(remark, empty);
            if (remark == null || empty) {
                setText(null);
            } else {
                setText(remark);
                if(remark.contains("併單備註："))
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                else
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
            }
        }
    }

    private TransferQuotation_ToggleSwitch createTransferQuotationSwitchButton(OrderObject OrderObject){
        TransferQuotation_ToggleSwitch toggle;
        if(OrderObject == Order_Enum.OrderObject.廠商){
            toggle = new TransferQuotation_ToggleSwitch(true, OrderObject, this);
            toggle.setTranslateX(0);
            toggle.setTranslateY(0);
            transferQuotationHBox_PurchaseUI.getChildren().add(0,toggle);
        }else {
            toggle = new TransferQuotation_ToggleSwitch(false, OrderObject, this);
            toggle.setTranslateX(0);
            toggle.setTranslateY(0);
            transferQuotationHBox_ShipmentUI.getChildren().add(0,toggle);
        }
        ComponentToolKit.setToolTips(toggle, new Tooltip(), Tooltip.order_transferQuotation_switchButton());
        return toggle;
    }
    private OffsetStatus_ToggleSwitch createOffsetStatusSwitchButton(){
        OffsetStatus_ToggleSwitch toggle;
        toggle = new OffsetStatus_ToggleSwitch(true,this);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        Offset_HBox.getChildren().add(0,toggle);
        ComponentToolKit.setToolTips(toggle, new Tooltip(), Tooltip.order_offsetStatus_switchButton());
        return toggle;
    }
    private void setOffsetStatusToggleSwitch(OffsetOrderStatus offsetOrderStatus){
        boolean isOffset = offsetOrderStatus.value();
        ComponentToolKit.setCheckBoxSelect(OffsetOrder_CheckBox, isOffset);

        OffsetOrder_CheckBox.setText(isOffset ? offsetOrderStatus.name() : "全 沖 帳");
        offsetStatus_toggleSwitch.setSwitchedOnStatus(isOffset ? (offsetOrderStatus == OffsetOrderStatus.全沖帳) : OffsetOrderStatus.全沖帳.value());
    }
    private DefaultProductConnection_ToggleSwitch createDefaultProductConnectionSwitchButton(boolean defaultSwitched){
        DefaultProductConnection_ToggleSwitch toggle = new DefaultProductConnection_ToggleSwitch(defaultSwitched,this);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        DefaultProductConnectionAndSearchOrder_HBox.getChildren().add(0,toggle);
        ComponentToolKit.setToolTips(toggle, new Tooltip(), Tooltip.order_defaultProductConnection_switchButton());
        return toggle;
    }
    private ProductGroup_ToggleSwitch createProductGroupSwitchButton(Order_Bean Order_Bean){
        ProductGroup_ToggleSwitch toggle = new ProductGroup_ToggleSwitch(false, Order_Bean, this);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        ModifyShipmentProductGroup_HBox.getChildren().add(0, toggle);
        ComponentToolKit.setToolTips(toggle, new Tooltip(), Tooltip.order_productGroup_switchButton());
        return toggle;
    }
}
