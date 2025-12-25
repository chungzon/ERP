package ERP.Controller.SystemSetting;

import ERP.Bean.DataBaseServer_Bean;
import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.IpCamInfo_Bean;
import ERP.Bean.SystemSetting.*;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ObjectStatus;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ExportCompanyFormat;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.ObjectSearchColumn;
import ERP.Enum.Order.Order_Enum.ShowObjectSource;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** [Controller] System setting */
public class SystemSetting_Controller extends Observable{
    @FXML ComboBox<String> DefaultPrinter_ComboBox, ConsignmentPrinter_ComboBox, LabelPrinter_ComboBox;
    @FXML ComboBox<String> CustomerIDCharacter_ComboBox;
    @FXML TextField ManufacturerIDLengthText, CustomerIDLengthText, CustomerIDAreaLengthText, orderNumberLengthText, projectCodeDefaultUrlText, outputFilePathText;
    @FXML Button insertProductArea_Button, modifyProductArea_Button, deleteProductArea_Button, insertProductFloor_Button, modifyProductFloor_Button, deleteProductFloor_Button;
    @FXML Button ManufacturerIDLength_Button, CustomerIDLength_Button, CustomerIDAreaLength_Button, orderNumberLength_Button;

    @FXML TextField testDriverText, testUrlText, testUserText, testPasswordText;

    @FXML RadioButton exportQuotationRadioButton_IAECrawler, noneExportQuotationRadioButton_IAECrawler;
    @FXML Button templateFormatSetting_Button, IAECrawlerAccount_InsertButton, IAECrawlerAccount_ModifyButton, IAECrawlerAccount_DeleteButton;
    @FXML TextField IAECrawler_ObjectIDText, IAECrawler_ObjectNameText, IAECrawler_AccountText, IAECrawler_PasswordText, IAECrawler_ManufacturerNickNameText, IAECrawler_ManufacturerAllNameText;
    @FXML SplitMenuButton IAECrawlerBelong_SplitMenuButton;

    @FXML TableColumn<IAECrawlerAccount_Bean,String> IAECrawler_ObjectIDColumn, IAECrawler_ObjectNameColumn, IAECrawler_AccountColumn, IAECrawler_PasswordColumn, IAECrawler_ExportQuotationColumn;
    @FXML TableColumn<IAECrawlerAccount_Bean,Boolean> IAECrawler_TheSameAsNoneInvoiceColumn;
    @FXML TableView<IAECrawlerAccount_Bean> IAECrawlerAccount_TableView;

    @FXML RadioButton IpCam_DefaultYes_RadioButton, IpCam_DefaultNo_RadioButton;
    @FXML TextField IpCam_NameText, IpCam_RTSPText;
    @FXML Button IpCam_InsertButton, IpCam_ModifyButton, IpCam_DeleteButton, IpCam_PreviewButton;
    @FXML TableColumn<IpCamInfo_Bean,Boolean> IpCam_DefaultPreview_Column;
    @FXML TableColumn<IpCamInfo_Bean,String> IpCam_Name_Column, IpCam_RTSP_Column;
    @FXML TableView<IpCamInfo_Bean> IpCam_TableView;
    @FXML ImageView IpCam_PreviewImageView;

    @FXML Label UpdateConfig_StatusLabel;
    @FXML ProgressBar UpdateConfig_ProgressBar;

    @FXML TableColumn<ProductBookCase_Bean,String> ProductArea_TableColumn, ProductFloor_TableColumn;
    @FXML TableView<ProductBookCase_Bean> ProductBookCase_TableView;

    @FXML TableColumn<CustomerIDCharacter_Bean,String> CustomerLocation_TableColumn, CustomerIDCharacter_TableColumn;
    @FXML TableView<CustomerIDCharacter_Bean> CustomerIDCharacter_TableView;

    @FXML TableColumn<ExportCompanyFormat_Bean,String> exportCompanyTitleFormat_Column,exportCompanyInfoFormat_Column;
    @FXML TableView<ExportCompanyFormat_Bean> exportCompanyFormat_TableView;

    @FXML TableColumn<ExportTemplateName_Bean,String> exportTemplateTitleName_Column, exportTemplateName_Column;
    @FXML TableView<ExportTemplateName_Bean> exportTemplateName_TableView;
    private Stage Stage;
    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallConfig CallConfig;
    private SystemSetting_Model SystemSetting_Model;
    private Order_Model Order_Model;
    private SystemSettingConfig_Bean SystemSettingConfig_Bean;

    private ObjectInfo_Bean IAECrawlerObjectInfo_Bean;

    private HashMap<IAECrawlerAccount_Bean,String> IAECrawlerAccount_PasswordMap = new HashMap<>();
    private HashMap<IAECrawlerBelong_Bean,CheckBox> IAECrawlerBelong_CheckBoxMap = new HashMap<>();
    public SystemSetting_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallConfig = ToolKit.CallConfig;
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){ this.Stage = Stage;  }
    /** Set component of system setting */
    public void setComponents(){
        this.SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();
        initialTableViewColumn();
        setProductBookCaseData();
        setPrinterSettingData();
        setOtherSettingData();
        setTestDBData();
        setIAECrawlerAccountData();
        setExportCompanyFormat();
        setExportTemplateName();
        setIpCameraData();
        setKeyWordTextLimiterLength();
        setTextFieldLimitDigital();
        if(SystemSetting_Model.isExistObject(ObjectStatus.純數字客戶))   ComponentToolKit.setButtonDisable(CustomerIDLength_Button,true);
        if(SystemSetting_Model.isExistObject(ObjectStatus.地區客戶))   ComponentToolKit.setButtonDisable(CustomerIDAreaLength_Button,true);
        if(SystemSetting_Model.isExistObject(ObjectStatus.純數字廠商))   ComponentToolKit.setButtonDisable(ManufacturerIDLength_Button,true);
        if(SystemSetting_Model.isExistOrder())   ComponentToolKit.setButtonDisable(orderNumberLength_Button,true);
        createAction();
    }
    private void createAction(){
        Stage.setOnCloseRequest(event -> {
            closeIpCamera();
            setChanged();
            notifyObservers();
            ComponentToolKit.closeThisStage(Stage);
        });
    }
    private void setKeyWordTextLimiterLength(){
        ComponentToolKit.addKeyWordTextLimitLength(CustomerIDLengthText,1);
        ComponentToolKit.addKeyWordTextLimitLength(CustomerIDAreaLengthText,1);
        ComponentToolKit.addKeyWordTextLimitLength(ManufacturerIDLengthText,1);
        ComponentToolKit.addKeyWordTextLimitLength(orderNumberLengthText,2);
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(CustomerIDLengthText, false);
        ComponentToolKit.addTextFieldLimitDigital(CustomerIDAreaLengthText, false);
        ComponentToolKit.addTextFieldLimitDigital(ManufacturerIDLengthText, false);
        ComponentToolKit.addTextFieldLimitDigital(orderNumberLengthText, false);
    }
    private void setProductBookCaseData(){
        ObservableList<ProductBookCase_Bean> ProductBookCaseList = SystemSetting_Model.getProductBookCase();
        ProductBookCase_TableView.getItems().addAll(ProductBookCaseList);
        if(ProductBookCaseList.size() == 0) initialButton();
    }
    private void initialButton(){
        ComponentToolKit.setButtonDisable(modifyProductArea_Button,true);
        ComponentToolKit.setButtonDisable(deleteProductArea_Button,true);
        ComponentToolKit.setButtonDisable(insertProductFloor_Button,true);
        ComponentToolKit.setButtonDisable(modifyProductFloor_Button,true);
        ComponentToolKit.setButtonDisable(deleteProductFloor_Button,true);
    }
    private void initialTableViewColumn(){
        ComponentToolKit.setColumnCellValue(ProductArea_TableColumn, "ProductArea","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ProductFloor_TableColumn, "ProductFloor","CENTER-LEFT","16",null);
        setColumnCellValueFactory(CustomerLocation_TableColumn,"CustomerLocation");
        setCustomerLocationColumnStyle(CustomerLocation_TableColumn);
        setColumnCellValueFactory(CustomerIDCharacter_TableColumn,"CustomerIDCharacter");
        setCustomerIDCharacterColumnStyle(CustomerIDCharacter_TableColumn);

        ComponentToolKit.setColumnCellValue(IAECrawler_ObjectIDColumn, "ObjectID","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(IAECrawler_ObjectNameColumn, "ObjectName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(IAECrawler_AccountColumn, "Account","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(IAECrawler_PasswordColumn, "Password","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(IAECrawler_ExportQuotationColumn, "ExportQuotation_ManufacturerNickName","CENTER-LEFT","16",null);
        setTheSameAsNoneInvoiceColumnCheckBox(IAECrawler_TheSameAsNoneInvoiceColumn, "theSameAsNoneInvoice","CENTER-LEFT","16");

        ComponentToolKit.setColumnCellValue(exportCompanyTitleFormat_Column, "exportCompanyTitleFormat","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(exportCompanyInfoFormat_Column, "exportCompanyInfoFormat","CENTER-LEFT","16",null);
        exportCompanyInfoFormat_Column.setCellFactory(TextFieldTableCell.forTableColumn());

        ComponentToolKit.setColumnCellValue(exportTemplateTitleName_Column, "exportTemplateTitleName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(exportTemplateName_Column, "exportTemplateName","CENTER-LEFT","16",null);
        exportTemplateName_Column.setCellFactory(TextFieldTableCell.forTableColumn());

        setIpCamDefaultColumnText(IpCam_DefaultPreview_Column, "DefaultPreview","CENTER-LEFT","16");
        ComponentToolKit.setColumnCellValue(IpCam_Name_Column, "Name","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(IpCam_RTSP_Column, "RTSP","CENTER-LEFT","16",null);
    }
    private void setPrinterSettingData(){
        ArrayList<String> PrinterList = SystemSetting_Model.getPrinterService();
        DefaultPrinter_ComboBox.getItems().addAll(PrinterList);
        if(SystemSettingConfig_Bean.getDefaultPrinter() == null)    DefaultPrinter_ComboBox.getSelectionModel().selectFirst();
        else    DefaultPrinter_ComboBox.getSelectionModel().select(SystemSettingConfig_Bean.getDefaultPrinter());

        ConsignmentPrinter_ComboBox.getItems().addAll(PrinterList);
        if(SystemSettingConfig_Bean.getConsignmentPrinter() == null)    ConsignmentPrinter_ComboBox.getSelectionModel().selectFirst();
        else    ConsignmentPrinter_ComboBox.getSelectionModel().select(SystemSettingConfig_Bean.getConsignmentPrinter());

        LabelPrinter_ComboBox.getItems().addAll(PrinterList);
        if(SystemSettingConfig_Bean.getLabelPrinter() == null)  LabelPrinter_ComboBox.getSelectionModel().selectFirst();
        else    LabelPrinter_ComboBox.getSelectionModel().select(SystemSettingConfig_Bean.getLabelPrinter());
    }
    private void setOtherSettingData(){
        for(int index = 0 ; index < SystemSetting_Enum.ObjectIDCharacter.values().length ; index ++ )
            CustomerIDCharacter_ComboBox.getItems().add(SystemSetting_Enum.ObjectIDCharacter.values()[index].Character());
        CustomerIDCharacter_TableView.setItems(getCustomerIDCharacterTableView());
        if(SystemSettingConfig_Bean.getCustomerIDCharacter() == null)      CustomerIDCharacter_ComboBox.getSelectionModel().selectFirst();
        else    CustomerIDCharacter_ComboBox.getSelectionModel().select(SystemSettingConfig_Bean.getCustomerIDCharacter());

        if(SystemSettingConfig_Bean.getCustomerIDLength() == null)   CustomerIDLengthText.setText("");
        else    CustomerIDLengthText.setText(SystemSettingConfig_Bean.getCustomerIDLength());
        if(SystemSettingConfig_Bean.getCustomerIDAreaLength() == null)   CustomerIDAreaLengthText.setText("");
        else    CustomerIDAreaLengthText.setText(SystemSettingConfig_Bean.getCustomerIDAreaLength());
        if(SystemSettingConfig_Bean.getManufacturerIDLength() == null)   ManufacturerIDLengthText.setText("");
        else    ManufacturerIDLengthText.setText(SystemSettingConfig_Bean.getManufacturerIDLength());

        if(SystemSettingConfig_Bean.getOrderNumberLength() == null)   orderNumberLengthText.setText("");
        else    orderNumberLengthText.setText(SystemSettingConfig_Bean.getOrderNumberLength());

        if(SystemSettingConfig_Bean.getProjectCodeDefaultUrl() == null)   projectCodeDefaultUrlText.setText("");
        else    projectCodeDefaultUrlText.setText(SystemSettingConfig_Bean.getProjectCodeDefaultUrl());

        outputFilePathText.setText(CallConfig.getFile_OutputPath());
    }
    private ObservableList<CustomerIDCharacter_Bean> getCustomerIDCharacterTableView(){
        ObservableList<CustomerIDCharacter_Bean> CustomerIDCharacterList = FXCollections.observableArrayList();
        for(int index = 0 ; index < SystemSetting_Enum.ObjectIDCharacter.values().length ; index++){
            CustomerIDCharacter_Bean CustomerIDCharacter_Bean = new CustomerIDCharacter_Bean();
            CustomerIDCharacter_Bean.setCustomerLocation(SystemSetting_Enum.ObjectIDCharacter.values()[index].name());
            CustomerIDCharacter_Bean.setCustomerIDCharacter(SystemSetting_Enum.ObjectIDCharacter.values()[index].Character());
            CustomerIDCharacterList.add(CustomerIDCharacter_Bean);
        }
        return CustomerIDCharacterList;
    }
    private void setTestDBData(){
        JSONObject testDatabase = SystemSettingConfig_Bean.getTestDatabase();
        if(testDatabase == null){
            testDatabase = new JSONObject();
            testDatabase.put("driver","");
            testDatabase.put("url","");
            testDatabase.put("user","");
            testDatabase.put("password","");
            SystemSettingConfig_Bean.setTestDatabase(testDatabase);
        }
        testDriverText.setText(testDatabase.getString("driver"));
        testUrlText.setText(testDatabase.getString("url"));
        testUserText.setText(testDatabase.getString("user"));
        testPasswordText.setText(ToolKit.hidePassword(testDatabase.getString("password")));
    }
    private void setIAECrawlerAccountData(){
        ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList = refreshIAECrawlerBelongItems();

        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAECrawlerAccount(IAECrawlerBelongList);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            IAECrawlerAccount_PasswordMap.put(IAECrawlerAccount_Bean,IAECrawlerAccount_Bean.getPassword());
            IAECrawlerAccount_Bean.setPassword(ToolKit.hidePassword(IAECrawlerAccount_Bean.getPassword()));
        }
        IAECrawlerAccount_TableView.setItems(IAECrawlerAccountList);
    }
    public ObservableList<IAECrawlerBelong_Bean> refreshIAECrawlerBelongItems(){
        IAECrawlerBelong_CheckBoxMap.clear();
        IAECrawlerBelong_SplitMenuButton.getItems().clear();
        ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList = SystemSetting_Model.getIAECrawlerBelong();
        for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelongList){
            CheckBox CheckBox = ComponentToolKit.initialCheckBox(IAECrawlerBelong_Bean.getBelongName(),18);
            CheckBox.setSelected(false);
            CustomMenuItem CustomMenuItem = new CustomMenuItem(CheckBox);
            CustomMenuItem.setHideOnClick(false);
            IAECrawlerBelong_SplitMenuButton.getItems().add(CustomMenuItem);
            IAECrawlerBelong_CheckBoxMap.put(IAECrawlerBelong_Bean,CheckBox);
        }
        return IAECrawlerBelongList;
    }
    private void setExportCompanyFormat(){
        JSONObject companyFormat = SystemSettingConfig_Bean.getExportCompanyFormat();
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.storeName,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.storeName.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.companyName,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.companyName.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.companyName_fontSize,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "0" : companyFormat.getString(ExportCompanyFormat.companyName_fontSize.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.companyAddress,SystemSettingConfig_Bean.getExportCompanyFormat() == null ?"" : companyFormat.getString(ExportCompanyFormat.companyAddress.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.companyAddress_fontSize,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "0" : companyFormat.getString(ExportCompanyFormat.companyAddress_fontSize.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.companyTelephone_fax,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.companyTelephone_fax.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.companyTelephone_fax_fontSize,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "0" : companyFormat.getString(ExportCompanyFormat.companyTelephone_fax_fontSize.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.item1,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.item1.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.item2,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.item2.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.item3,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.item3.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.item4,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.item4.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.item5,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.item5.name())));
        exportCompanyFormat_TableView.getItems().add(new ExportCompanyFormat_Bean(ExportCompanyFormat.productName,SystemSettingConfig_Bean.getExportCompanyFormat() == null ? "" : companyFormat.getString(ExportCompanyFormat.productName.name())));
    }
    private void setIpCameraData(){
        ComponentToolKit.getIpCamTableViewItemList(IpCam_TableView).addAll(SystemSetting_Model.getIpCamera());
    }
    private void setExportTemplateName(){
        JSONObject templateName = SystemSettingConfig_Bean.getExportTemplateName();
//        exportTemplateName_TableView.getItems().add(ExportTemplateName_Bean);
//        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.productCategoryAndOrderExport,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(SystemSetting_Enum.ExportTemplateName.productCategoryAndOrderExport.name())));
        /*exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.customerInfo,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.customerInfo.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.payableReceivableInvoice,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.payableReceivableInvoice.name())));
        /*exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.exportCheckFormat,SystemSettingConfig_Bean.getExportTemplateName() == null ?"" : templateName.getString(ExportTemplateName.exportCheckFormat.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.payableReceivableDetail,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.payableReceivableDetail.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.iAECrawlerPurchaseNote,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.iAECrawlerPurchaseNote.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.iAECrawlerInvoiceOverview,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.iAECrawlerInvoiceOverview.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.exportPayableDocument,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.exportPayableDocument.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.logistics_Note,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.logistics_Note.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.specificationDocument,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.specificationDocument.name())));
        exportTemplateName_TableView.getItems().add(new ExportTemplateName_Bean(ExportTemplateName.purchaseQuotationOrAskPriceDocument,SystemSettingConfig_Bean.getExportTemplateName() == null ? "" : templateName.getString(ExportTemplateName.purchaseQuotationOrAskPriceDocument.name())));*/
    }
    /** TableView Mouse Clicked - [商品儲存區設定] 表格事件 */
    @FXML protected void ProductBookCaseTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ProductBookCase_Bean ProductBookCase_Bean = ComponentToolKit.getProductBookCaseTableViewSelectItem(ProductBookCase_TableView);
            if(ProductBookCase_Bean != null){
                if(ProductBookCase_Bean.getProductFloor().equals("")) {
                    ComponentToolKit.setButtonDisable(modifyProductArea_Button,false);
                    ComponentToolKit.setButtonDisable(deleteProductArea_Button,false);
                    ComponentToolKit.setButtonDisable(insertProductFloor_Button,false);
                    ComponentToolKit.setButtonDisable(modifyProductFloor_Button,true);
                    ComponentToolKit.setButtonDisable(deleteProductFloor_Button,true);
                }else if(!ProductBookCase_Bean.getProductFloor().equals("")) {
                    ComponentToolKit.setButtonDisable(modifyProductArea_Button,true);
                    ComponentToolKit.setButtonDisable(deleteProductArea_Button,true);
                    ComponentToolKit.setButtonDisable(insertProductFloor_Button,true);
                    ComponentToolKit.setButtonDisable(modifyProductFloor_Button,false);
                    ComponentToolKit.setButtonDisable(deleteProductFloor_Button,false);
                }
            }
        }
    }
    /** Button Mouse Clicked - [商品儲存區設定] 新增儲存區 */
    @FXML protected void InsertProductAreaMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String ProductArea = DialogUI.TextInputDialog("新增儲存區", "儲存區名稱 : ",null);
            if (ProductArea != null && !ProductArea.equals("")) {
                ObservableList<ProductBookCase_Bean> ProductBookCasesList = ComponentToolKit.getProductBookCaseTableViewItemList(ProductBookCase_TableView);
                if (isProductAreaRepeat(ProductBookCasesList, ProductArea)) DialogUI.MessageDialog("※ 儲存區名稱重複");
                else {
                    ProductBookCase_Bean ProductBookCase_Bean = new ProductBookCase_Bean();
                    ProductBookCase_Bean.setProductArea(ProductArea);
                    ProductBookCase_Bean.setProductFloor("");
                    ProductBookCase_Bean.setProductLevel(SystemSetting_Enum.ProductLevel.商品儲存區);
                    if (SystemSetting_Model.insertAreaOrFloorOfBookCase(ProductBookCase_Bean)) {
                        SystemSetting_Model.sortProductBookCase(ProductBookCasesList, ProductBookCase_Bean);
                        DialogUI.MessageDialog("※ 新增成功");
                    } else DialogUI.MessageDialog("※ 新增失敗");
                }
            }
        }
    }
    private boolean isProductAreaRepeat(ObservableList<ProductBookCase_Bean> ProductBookCasesList, String ProductArea){
        for (ProductBookCase_Bean ProductBookCase_Bean : ProductBookCasesList) {
            if (ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區 && ProductBookCase_Bean.getProductArea().equals(ProductArea))
                return true;
        }
        return false;
    }
    /** Button Mouse Clicked - [商品儲存區設定] 修改儲存區 */
    @FXML protected void ModifyProductAreaMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if (DialogUI.ConfirmDialog("※ 將一併修改相關儲存層，是否繼續 ?",true, false, 0, 0)) {
                String ProductArea = DialogUI.TextInputDialog("修改儲存區", "儲存區名稱:",null);
                if (ProductArea != null && !ProductArea.equals("")) {
                    ProductBookCase_Bean ProductBookCase_Bean = ComponentToolKit.getProductBookCaseTableViewSelectItem(ProductBookCase_TableView);
                    if (SystemSetting_Model.modifyProductSave(ProductBookCase_Bean, ProductArea, "")) {
                        modifyExistProductArea(ProductArea);
                        DialogUI.MessageDialog("※ 修改成功");
                    } else DialogUI.MessageDialog("※ 修改失敗");
                }
            }
        }
    }
    private void modifyExistProductArea(String ProductArea){
        ObservableList<ProductBookCase_Bean> ProductBookCasesList = ComponentToolKit.getProductBookCaseTableViewItemList(ProductBookCase_TableView);
        for (ProductBookCase_Bean ProductBookCase_Bean : ProductBookCasesList) {
            if (ProductBookCase_Bean.getProductArea().equals(ProductBookCase_Bean.getProductArea())) {
                ProductBookCase_Bean.setProductArea(ProductArea);
            }
        }
    }
    /** Button Mouse Clicked - [商品儲存區設定] 刪除儲存區 */
    @FXML protected void DeleteProductAreaMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductBookCase_Bean ProductBookCase_Bean = ComponentToolKit.getProductBookCaseTableViewSelectItem(ProductBookCase_TableView);
            if (ProductBookCase_Bean == null || !ProductBookCase_Bean.getProductFloor().equals(""))
                DialogUI.MessageDialog("※ 請點選欲刪除儲存區");
            else if (DialogUI.ConfirmDialog("※ 將一併刪除相關儲存層，是否繼續 ?",true, false, 0, 0)) {
                if (SystemSetting_Model.deleteProductSave(ProductBookCase_Bean)) {
                    deleteExistProductFloor(ProductBookCase_Bean);
                    DialogUI.MessageDialog("※ 刪除成功");
                    initialButton();
                } else DialogUI.MessageDialog("※ 刪除失敗");
            }
        }
    }
    private void deleteExistProductFloor(ProductBookCase_Bean ProductBookCase_Bean){
        ObservableList<ProductBookCase_Bean> ProductBookCasesList = ComponentToolKit.getProductBookCaseTableViewItemList(ProductBookCase_TableView);
        ArrayList<ProductBookCase_Bean> DeleteIndexList = new ArrayList<>();
        for (ProductBookCase_Bean Data_Bean : ProductBookCasesList) {
            if (Data_Bean.getProductArea().equals(ProductBookCase_Bean.getProductArea()))
                DeleteIndexList.add(Data_Bean);
        }
        ProductBookCasesList.removeAll(DeleteIndexList);
    }
    /** Button Mouse Clicked - [商品儲存區設定] 新增儲存層 */
    @FXML protected void InsertProductFloorMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String ProductFloor = DialogUI.TextInputDialog("新增儲存層", "儲存層名稱 : ",null);
            if (ProductFloor != null && !ProductFloor.equals("")) {
                ObservableList<ProductBookCase_Bean> ProductBookCasesList = ComponentToolKit.getProductBookCaseTableViewItemList(ProductBookCase_TableView);
                ProductBookCase_Bean ProductBookCase_Bean = ComponentToolKit.getProductBookCaseTableViewSelectItem(ProductBookCase_TableView);
                if (isProductFloorRepeat(ProductBookCasesList, ProductBookCase_Bean.getProductArea(), ProductFloor))
                    DialogUI.MessageDialog("※ 儲存層名稱重複");
                else {
                    ProductBookCase_Bean newProductBookCase_Bean = new ProductBookCase_Bean();
                    newProductBookCase_Bean.setProductArea(ProductBookCase_Bean.getProductArea());
                    newProductBookCase_Bean.setProductFloor(ProductFloor);
                    newProductBookCase_Bean.setProductLevel(SystemSetting_Enum.ProductLevel.商品儲存層);
                    if (SystemSetting_Model.insertAreaOrFloorOfBookCase(newProductBookCase_Bean)) {
                        SystemSetting_Model.sortProductBookCase(ProductBookCasesList, newProductBookCase_Bean);
                        DialogUI.MessageDialog("※ 新增成功");
                    } else DialogUI.MessageDialog("※ 新增失敗");
                }
            }
        }
    }
    private boolean isProductFloorRepeat(ObservableList<ProductBookCase_Bean> ProductBookCasesList, String ProductArea, String ProductFloor){
        for (ProductBookCase_Bean ProductBookCase_Bean : ProductBookCasesList) {
            if (ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層 && ProductBookCase_Bean.getProductArea().equals(ProductArea) && ProductBookCase_Bean.getProductFloor().equals(ProductFloor))
                return true;
        }
        return false;
    }
    /** Button Mouse Clicked - [商品儲存區設定] 修改儲存層 */
    @FXML protected void ModifyProductFloorMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductBookCase_Bean ProductBookCase_Bean = ComponentToolKit.getProductBookCaseTableViewSelectItem(ProductBookCase_TableView);
            String ProductFloor = DialogUI.TextInputDialog("修改儲存層", "儲存層名稱:",null);
            if (ProductFloor != null && !ProductFloor.equals("")) {
                if (SystemSetting_Model.modifyProductSave(ProductBookCase_Bean, "", ProductFloor)) {
                    ProductBookCase_Bean.setProductFloor(ProductFloor);
                    DialogUI.MessageDialog("※ 修改成功");
                } else DialogUI.MessageDialog("※ 修改失敗");
            }
        }
    }
    /** Button Mouse Clicked - [商品儲存區設定] 刪除儲存層 */
    @FXML protected void DeleteProductFloorMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ProductBookCase_Bean ProductBookCase_Bean = ComponentToolKit.getProductBookCaseTableViewSelectItem(ProductBookCase_TableView);
            if (ProductBookCase_Bean == null || ProductBookCase_Bean.getProductFloor().equals(""))
                DialogUI.MessageDialog("※ 請點選欲刪除儲存層");
            else {
                if (SystemSetting_Model.deleteProductSave(ProductBookCase_Bean)) {
                    ObservableList<ProductBookCase_Bean> ProductBookCasesList = ComponentToolKit.getProductBookCaseTableViewItemList(ProductBookCase_TableView);
                    ProductBookCasesList.remove(ProductBookCase_Bean);
                    DialogUI.MessageDialog("※ 刪除成功");
                    initialButton();
                } else DialogUI.MessageDialog("※ 刪除失敗");
            }
        }
    }
    /** Button Mouse Clicked - [預設列表機設定] 儲存 */
    @FXML protected void SaveDefaultPrinterMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String PrinterName = DefaultPrinter_ComboBox.getSelectionModel().getSelectedItem();
            if (SystemSettingConfig_Bean.getDefaultPrinter() == null) {
                if (SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.預設列表機設定, PrinterName)) {
                    SystemSettingConfig_Bean.setDefaultPrinter(PrinterName);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            } else {
                if (SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.預設列表機設定, PrinterName)) {
                    SystemSettingConfig_Bean.setDefaultPrinter(PrinterName);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    /** Button Mouse Clicked - [託運單列表機設定] 儲存 */
    @FXML protected void SaveConsignmentPrinterMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String PrinterName = ConsignmentPrinter_ComboBox.getSelectionModel().getSelectedItem();
            if (SystemSettingConfig_Bean.getConsignmentPrinter() == null) {
                if (SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.託運單列表機設定, PrinterName)) {
                    SystemSettingConfig_Bean.setConsignmentPrinter(PrinterName);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            } else {
                if (SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.託運單列表機設定, PrinterName)) {
                    SystemSettingConfig_Bean.setConsignmentPrinter(PrinterName);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    /** Button Mouse Clicked - [標籤列表機設定] 儲存 */
    @FXML protected void SaveLabelPrinterMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String PrinterName = LabelPrinter_ComboBox.getSelectionModel().getSelectedItem();
            if (SystemSettingConfig_Bean.getLabelPrinter() == null) {
                if (SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.標籤列表機設定, PrinterName)) {
                    SystemSettingConfig_Bean.setLabelPrinter(PrinterName);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            } else {
                if (SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.標籤列表機設定, PrinterName)) {
                    SystemSettingConfig_Bean.setLabelPrinter(PrinterName);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    /** Button Mouse Clicked - [客戶編號起始字母] 更改設定 */
    @FXML protected void SaveCustomerIDCharacterMouseClicked(MouseEvent MouseEvent){
        if(ToolKit.KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String CustomerIDCharacter = CustomerIDCharacter_ComboBox.getSelectionModel().getSelectedItem();
            if (SystemSettingConfig_Bean.getDefaultPrinter() == null) {
                if (SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.客戶編號起始字母, CustomerIDCharacter)) {
                    SystemSettingConfig_Bean.setCustomerIDCharacter(CustomerIDCharacter);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            } else {
                if (SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.客戶編號起始字母, CustomerIDCharacter)) {
                    SystemSettingConfig_Bean.setCustomerIDCharacter(CustomerIDCharacter);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    /** Button Mouse Clicked - [客戶位數(無英文)] 更改設定 */
    @FXML protected void SaveCustomerIDLengthMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String CustomerIDLength = CustomerIDLengthText.getText();
            if(ToolKit.isDigital(CustomerIDLength) && !CustomerIDLength.equals("0")){
                if(SystemSettingConfig_Bean.getCustomerIDLength() == null){
                    if(SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.客戶位數, CustomerIDLength)){
                        SystemSettingConfig_Bean.setCustomerIDCharacter(CustomerIDLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }else{
                    if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.客戶位數, CustomerIDLength)){
                        SystemSettingConfig_Bean.setCustomerIDLength(CustomerIDLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }
            }else   DialogUI.MessageDialog("※ 格式錯誤");
        }
    }
    /** Button Mouse Clicked - [地區客戶位數] 更改設定 */
    @FXML protected void SaveCustomerIDAreaLengthMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String CustomerIDAreaLength = CustomerIDAreaLengthText.getText();
            if(ToolKit.isDigital(CustomerIDAreaLength) && !CustomerIDAreaLength.equals("0")){
                if(SystemSettingConfig_Bean.getCustomerIDAreaLength() == null){
                    if(SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.地區客戶位數, CustomerIDAreaLength)){
                        SystemSettingConfig_Bean.setCustomerIDAreaLength(CustomerIDAreaLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }else{
                    if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.地區客戶位數, CustomerIDAreaLength)){
                        SystemSettingConfig_Bean.setCustomerIDAreaLength(CustomerIDAreaLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }
            }else   DialogUI.MessageDialog("※ 格式錯誤");
        }
    }
    /** Button Mouse Clicked - [廠商位數(無英文)] 更改設定 */
    @FXML protected void SaveManufacturerIDLengthMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String ManufacturerIDLength = ManufacturerIDLengthText.getText();
            if(ToolKit.isDigital(ManufacturerIDLength) && !ManufacturerIDLength.equals("0")){
                if(SystemSettingConfig_Bean.getManufacturerIDLength() == null){
                    if(SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.廠商位數, ManufacturerIDLength)){
                        SystemSettingConfig_Bean.setManufacturerIDLength(ManufacturerIDLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }else{
                    if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.廠商位數, ManufacturerIDLength)){
                        SystemSettingConfig_Bean.setManufacturerIDLength(ManufacturerIDLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }
            }else   DialogUI.MessageDialog("※ 格式錯誤");
        }
    }
    @FXML protected void saveOrderNumberLengthMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String orderNumberLength = orderNumberLengthText.getText();
            if(!orderNumberLength.equals("0")){
                if(SystemSettingConfig_Bean.getOrderNumberLength() == null){
                    if(SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.貨單號長度, orderNumberLength)){
                        SystemSettingConfig_Bean.setOrderNumberLength(orderNumberLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }else{
                    if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.貨單號長度, orderNumberLength)){
                        SystemSettingConfig_Bean.setOrderNumberLength(orderNumberLength);
                        DialogUI.MessageDialog("※ 設定成功");
                    }else   DialogUI.MessageDialog("※ 設定失敗");
                }
            }else   DialogUI.MessageDialog("※ 長度不能為 0");
        }
    }
    @FXML protected void saveProjectCodeDefaultUrlMouseClicked(MouseEvent MouseEvent){
        if(ToolKit.KeyPressed.isMouseLeftClicked(MouseEvent)){
            String projectCodeDefaultUrl = projectCodeDefaultUrlText.getText();
            if(SystemSettingConfig_Bean.getProjectCodeDefaultUrl() == null){
                if(SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.專案編號預設網址, projectCodeDefaultUrl)){
                    SystemSettingConfig_Bean.setProjectCodeDefaultUrl(projectCodeDefaultUrl);
                    DialogUI.MessageDialog("※ 設定成功");
                }else   DialogUI.MessageDialog("※ 設定失敗");
            }else{
                if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.專案編號預設網址, projectCodeDefaultUrl)){
                    SystemSettingConfig_Bean.setProjectCodeDefaultUrl(projectCodeDefaultUrl);
                    DialogUI.MessageDialog("※ 設定成功");
                }else   DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    @FXML protected void saveOutputFilePathMouseClicked(MouseEvent MouseEvent){
        if(ToolKit.KeyPressed.isMouseLeftClicked(MouseEvent)){
            String outputFile = outputFilePathText.getText();
            if(SystemSetting_Model.saveFile_OutputPath(outputFile))
                DialogUI.MessageDialog("※ 設定成功");
            else
                DialogUI.MessageDialog("※ 設定失敗");
        }
    }

    @FXML protected void testDriverKeyReleased(KeyEvent KeyEvent){
        if(ToolKit.KeyPressed.isEnterKeyPressed(KeyEvent)){
            testUrlText.requestFocus();
        }
    }
    @FXML protected void testUrlKeyReleased(KeyEvent KeyEvent){
        if(ToolKit.KeyPressed.isEnterKeyPressed(KeyEvent)){
            testUserText.requestFocus();
        }
    }
    @FXML protected void testUserKeyReleased(KeyEvent KeyEvent){
        if(ToolKit.KeyPressed.isEnterKeyPressed(KeyEvent)){
            testPasswordText.requestFocus();
        }
    }
    @FXML protected void testPasswordKeyReleased(KeyEvent KeyEvent){
        if(ToolKit.KeyPressed.isEnterKeyPressed(KeyEvent)){
            testDriverText.requestFocus();
        }
    }
    /** 測試資料庫 - 連線測試 */
    @FXML protected void ConnectTestMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try{
                String driver = testDriverText.getText(), url = testUrlText.getText(), user = testUserText.getText(), password = testPasswordText.getText();
                if(isInputTextEmpty(driver)){
                    DialogUI.MessageDialog("請輸入 driver");
                    testDriverText.requestFocus();
                }else if(isInputTextEmpty(url)){
                    DialogUI.MessageDialog("請輸入 url");
                    testUrlText.requestFocus();
                }else if(isInputTextEmpty(user)){
                    DialogUI.MessageDialog("請輸入 user");
                    testUserText.requestFocus();
                }else if(isInputTextEmpty(password)){
                    DialogUI.MessageDialog("請輸入 password");
                    testPasswordText.requestFocus();
                }else{
                    DataBaseServer_Bean dataBaseServer_bean = new DataBaseServer_Bean();
                    dataBaseServer_bean.setDriver(driver);
                    dataBaseServer_bean.setUrl(url);
                    dataBaseServer_bean.setUser(user);
                    dataBaseServer_bean.setPassword(password);
                    boolean status = SqlAdapter.connectTest(dataBaseServer_bean);
                    if(status) {
                        testPasswordText.setText(ToolKit.hidePassword(password));
                        if(DialogUI.ConfirmDialog("※ 連線成功，是否儲存 ?",true,false,0,0)){
                            JSONObject database = new JSONObject();
                            database.put("driver",driver);
                            database.put("url",url);
                            database.put("user",user);
                            database.put("password",password);
                            if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.測試資料庫, database.toJSONString())){
                                SystemSettingConfig_Bean.setTestDatabase(database);
                                DialogUI.MessageDialog("※ 儲存成功");
                            }else{
                                DialogUI.MessageDialog("※ 儲存失敗");
                            }
                        }
                    }else{
                        DialogUI.AlarmDialog("※ 連線失敗");
                    }
                }
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    private boolean isInputTextEmpty(String input){
        return (input == null || input.equals(""));
    }
    @FXML protected void IAECrawler_ObjectIDKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String ObjectID = IAECrawler_ObjectIDText.getText();
            ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject.廠商, ObjectSearchColumn.廠商編號,ObjectID);
            if(ObjectList.size() == 1)  setIAECrawlerObjectInfo(ObjectList.get(0));
            else if(ObjectList.size() > 1) ToolKit.CallFXML.ShowObject(Stage, OrderObject.廠商, ObjectList,true, ShowObjectSource.出納帳戶,this);
            else{
                IAECrawler_ObjectIDText.setText("");
                IAECrawler_ObjectNameText.setText("");
                DialogUI.MessageDialog("※ 查無相關" + OrderObject.廠商);
            }
        }
    }
    @FXML protected void IAECrawler_ObjectNameKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String ObjectName = IAECrawler_ObjectNameText.getText();
            ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject.廠商, ObjectSearchColumn.廠商名稱,ObjectName);
            if(ObjectList.size() == 1)  setIAECrawlerObjectInfo(ObjectList.get(0));
            else if(ObjectList.size() > 1) ToolKit.CallFXML.ShowObject(Stage, OrderObject.廠商, ObjectList,true, ShowObjectSource.出納帳戶,this);
            else{
                IAECrawler_ObjectIDText.setText("");
                IAECrawler_ObjectNameText.setText("");
                DialogUI.MessageDialog("※ 查無相關" + OrderObject.廠商);
            }
        }
    }
    public void setIAECrawlerObjectInfo(ObjectInfo_Bean ObjectInfo_Bean){
        this.IAECrawlerObjectInfo_Bean = ObjectInfo_Bean;
        IAECrawler_ObjectIDText.setText(IAECrawlerObjectInfo_Bean.getObjectID());
        IAECrawler_ObjectNameText.setText(IAECrawlerObjectInfo_Bean.getObjectName());
        IAECrawler_AccountText.requestFocus();
    }
    @FXML protected void exportQuotationOnAction(){
        if(exportQuotationRadioButton_IAECrawler.isSelected()){
            ComponentToolKit.setButtonDisable(templateFormatSetting_Button, false);
        }
    }
    @FXML protected void noneExportQuotationOnAction(){
        if(noneExportQuotationRadioButton_IAECrawler.isSelected()){
            ComponentToolKit.setButtonDisable(templateFormatSetting_Button, true);
        }
    }
    @FXML protected void templateFormatSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerAccount_Bean selectIAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerAccountTableViewSelectItem(IAECrawlerAccount_TableView);
            if(selectIAECrawlerAccount_Bean == null) {
                DialogUI.MessageDialog("請選擇出納帳戶");
            }else{
                ToolKit.CallFXML.ShowTemplateFormatSetting(Stage, selectIAECrawlerAccount_Bean);
            }
        }
    }
    @FXML protected void IAECrawlerBelongMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList = FXCollections.observableArrayList();
            IAECrawlerBelongList.addAll(IAECrawlerBelong_CheckBoxMap.keySet());
            ToolKit.CallFXML.ShowIAECrawlerBelong(Stage,IAECrawlerBelongList,this);
        }
    }
    @FXML protected void insertPaymentAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String paymentAccount = IAECrawler_AccountText.getText();
            String paymentPassword = IAECrawler_PasswordText.getText();
            String manufacturerNickName = IAECrawler_ManufacturerNickNameText.getText();
            String manufacturerAllName = IAECrawler_ManufacturerAllNameText.getText();

            ArrayList<IAECrawlerBelong_Bean> IAECrawlerBelongList = getSelectIAECrawlerBelong();
            if(IAECrawlerObjectInfo_Bean == null)   DialogUI.MessageDialog("※ 請輸入客戶資訊");
            if(paymentAccount == null || paymentAccount.equals(""))  DialogUI.MessageDialog("※ 請輸入出納帳號");
            else if(paymentPassword == null || paymentPassword.equals("")) DialogUI.MessageDialog("※ 請輸入出納密碼");
            else if(manufacturerNickName == null || manufacturerNickName.equals(""))   DialogUI.MessageDialog("※ 請輸入廠商簡稱");
            else if(IAECrawlerBelongList == null)  DialogUI.MessageDialog("※ 請選擇所屬學校");
            else if(SystemSetting_Model.isIAECrawlerAccount_ExportQuotation_ManufacturerNickNameExist(paymentAccount , manufacturerNickName))
                DialogUI.MessageDialog("※ 廠商簡稱已重複!");
            else{
                IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
                IAECrawlerAccount_Bean.setObjectInfo_Bean(IAECrawlerObjectInfo_Bean);

                IAECrawlerAccount_Bean.setAccount(paymentAccount);
                IAECrawlerAccount_Bean.setPassword(paymentPassword);
                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName(manufacturerNickName);
                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName(manufacturerAllName);
                IAECrawlerAccount_Bean.setIAECrawlerBelongList(IAECrawlerBelongList);
                IAECrawlerAccount_Bean.setExportQuotation(exportQuotationRadioButton_IAECrawler.isSelected());
                IAECrawlerAccount_Bean.setExportQuotation_defaultSelect(false);

                if(SystemSetting_Model.isExistIAECrawlerAccount(null,IAECrawlerAccount_Bean.getAccount()))
                    DialogUI.MessageDialog("※ 新增失敗，出納帳號已存在");
                else if(SystemSetting_Model.insertIAECrawlerAccount(IAECrawlerAccount_Bean)){
                    DialogUI.MessageDialog("※ 新增成功");
                    IAECrawlerAccount_PasswordMap.put(IAECrawlerAccount_Bean,IAECrawlerAccount_Bean.getPassword());
                    IAECrawlerAccount_Bean.setPassword(ToolKit.hidePassword(IAECrawlerAccount_Bean.getPassword()));
                    ComponentToolKit.getIAECrawlerAccountTableViewItemList(IAECrawlerAccount_TableView).add(IAECrawlerAccount_Bean);
                    initialIAECrawlerSettingComponent();
                }else
                    DialogUI.MessageDialog("※ 新增失敗");
            }
        }
    }
    @FXML protected void clearPaymentAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            initialIAECrawlerSettingComponent();
        }
    }
    @FXML protected void modifyPaymentAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerAccount_Bean selectIAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerAccountTableViewSelectItem(IAECrawlerAccount_TableView);
            if(selectIAECrawlerAccount_Bean != null && DialogUI.ConfirmDialog("※ 是否確定修改 ?",true,false,0,0)){
                String paymentAccount = IAECrawler_AccountText.getText();
                String paymentPassword = IAECrawler_PasswordText.getText();
                String manufacturerNickName = IAECrawler_ManufacturerNickNameText.getText();
                String manufacturerAllName = IAECrawler_ManufacturerAllNameText.getText();

                ArrayList<IAECrawlerBelong_Bean> IAECrawlerBelongList = getSelectIAECrawlerBelong();
                if(paymentAccount == null || paymentAccount.equals(""))  DialogUI.MessageDialog("※ 請輸入出納帳號");
                else if(paymentPassword == null || paymentPassword.equals("")) DialogUI.MessageDialog("※ 請輸入出納密碼");
                else if(manufacturerNickName == null || manufacturerNickName.equals(""))   DialogUI.MessageDialog("※ 請輸入廠商簡稱");
                else if(IAECrawlerBelongList == null)  DialogUI.MessageDialog("※ 請選擇所屬學校");
                else if(SystemSetting_Model.isIAECrawlerAccount_ExportQuotation_ManufacturerNickNameExist(selectIAECrawlerAccount_Bean.getAccount(), manufacturerNickName))
                    DialogUI.MessageDialog("※ 廠商簡稱已重複!");
                else  if(SystemSetting_Model.isExistIAECrawlerAccount(selectIAECrawlerAccount_Bean.getId(), paymentAccount))
                    DialogUI.MessageDialog("※ 修改失敗，出納帳戶已存在");
                else{
                    selectIAECrawlerAccount_Bean.setObjectInfo_Bean(IAECrawlerObjectInfo_Bean);
                    selectIAECrawlerAccount_Bean.setAccount(IAECrawler_AccountText.getText());
                    if(IAECrawler_PasswordText.getText().contains("*")) selectIAECrawlerAccount_Bean.setPassword(IAECrawlerAccount_PasswordMap.get(selectIAECrawlerAccount_Bean));
                    else    selectIAECrawlerAccount_Bean.setPassword(IAECrawler_PasswordText.getText());
                    selectIAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName(manufacturerNickName);
                    selectIAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName(manufacturerAllName);
                    selectIAECrawlerAccount_Bean.setIAECrawlerBelongList(IAECrawlerBelongList);

                    selectIAECrawlerAccount_Bean.setExportQuotation(exportQuotationRadioButton_IAECrawler.isSelected());
                    selectIAECrawlerAccount_Bean.setExportQuotation_defaultSelect(false);


                    if(SystemSetting_Model.modifyIAECrawlerAccount(selectIAECrawlerAccount_Bean)) {
                        DialogUI.MessageDialog("※ 修改成功");
                        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountTableViewItemList(IAECrawlerAccount_TableView);
                        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
                            if(IAECrawlerAccount_Bean.getAccount().equals(selectIAECrawlerAccount_Bean.getAccount())){
                                IAECrawlerAccount_Bean.setExportQuotation(selectIAECrawlerAccount_Bean.isExportQuotation());
                                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName(selectIAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
                                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName(selectIAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
                            }
                        }
                        ObjectInfo_Bean objectInfo_bean = selectIAECrawlerAccount_Bean.getObjectInfo_Bean();
                        IAECrawler_ObjectIDText.setText(objectInfo_bean.getObjectID());
                        IAECrawler_ObjectNameText.setText(objectInfo_bean.getObjectName());
                        IAECrawlerAccount_PasswordMap.put(selectIAECrawlerAccount_Bean,selectIAECrawlerAccount_Bean.getPassword());
                        selectIAECrawlerAccount_Bean.setPassword(ToolKit.hidePassword(selectIAECrawlerAccount_Bean.getPassword()));
                        IAECrawler_PasswordText.setText(selectIAECrawlerAccount_Bean.getPassword());
                    }else
                        DialogUI.MessageDialog("※ 修改失敗");
                }
            }
        }
    }
    @FXML protected void deletePaymentAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerAccountTableViewSelectItem(IAECrawlerAccount_TableView);
            if(IAECrawlerAccount_Bean != null && DialogUI.ConfirmDialog("※ 是否確定刪除 ?",true,false,0,0)){
                if(SystemSetting_Model.isExistBindingInvoice(IAECrawlerAccount_Bean)) {
                    DialogUI.MessageDialog("※ 刪除失敗，此出納帳戶已有綁定貨單!");
                    return;
                }else if(SystemSetting_Model.isIAECrawlerAccountExistPayment(IAECrawlerAccount_Bean.getAccount())) {
                    DialogUI.MessageDialog("※ 刪除失敗，此出納帳戶已存在出納資料!");
                    return;
                }
                if(SystemSetting_Model.deleteIAECrawlerAccount(IAECrawlerAccount_Bean)) {
                    DialogUI.MessageDialog("※ 刪除成功");
                    IAECrawlerAccount_PasswordMap.remove(IAECrawlerAccount_Bean);
                    ComponentToolKit.getIAECrawlerAccountTableViewItemList(IAECrawlerAccount_TableView).remove(IAECrawlerAccount_Bean);
                    initialIAECrawlerSettingComponent();
                }else
                    DialogUI.MessageDialog("※ 刪除失敗");
            }
        }
    }
    @FXML protected void IAECrawlerTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerAccountTableViewSelectItem(IAECrawlerAccount_TableView);
            if(IAECrawlerAccount_Bean != null){
                ComponentToolKit.setButtonDisable(IAECrawlerAccount_InsertButton,true);
                ComponentToolKit.setButtonDisable(IAECrawlerAccount_ModifyButton,false);
                ComponentToolKit.setButtonDisable(IAECrawlerAccount_DeleteButton,false);

                IAECrawlerObjectInfo_Bean = IAECrawlerAccount_Bean.getObjectInfo_Bean();
                IAECrawler_ObjectIDText.setText(IAECrawlerObjectInfo_Bean.getObjectID());
                IAECrawler_ObjectNameText.setText(IAECrawlerObjectInfo_Bean.getObjectName());
                IAECrawler_AccountText.setText(IAECrawlerAccount_Bean.getAccount());
                IAECrawler_PasswordText.setText(IAECrawlerAccount_Bean.getPassword());
                IAECrawler_ManufacturerNickNameText.setText(IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
                IAECrawler_ManufacturerAllNameText.setText(IAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());

                for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelong_CheckBoxMap.keySet())
                    IAECrawlerBelong_CheckBoxMap.get(IAECrawlerBelong_Bean).setSelected(false);

                ArrayList<IAECrawlerBelong_Bean> IAECrawlerBelongList = IAECrawlerAccount_Bean.getIAECrawlerBelongList();
                for(IAECrawlerBelong_Bean selectIAECrawlerBelong_Bean : IAECrawlerBelongList){
                    for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelong_CheckBoxMap.keySet()){
                        if(selectIAECrawlerBelong_Bean.getId() == IAECrawlerBelong_Bean.getId()) {
                            IAECrawlerBelong_CheckBoxMap.get(IAECrawlerBelong_Bean).setSelected(true);
                            break;
                        }
                    }
                }
                ComponentToolKit.setButtonDisable(templateFormatSetting_Button, !IAECrawlerAccount_Bean.isExportQuotation());
                ComponentToolKit.setRadioButtonSelect(exportQuotationRadioButton_IAECrawler,IAECrawlerAccount_Bean.isExportQuotation());
                ComponentToolKit.setRadioButtonSelect(noneExportQuotationRadioButton_IAECrawler,!IAECrawlerAccount_Bean.isExportQuotation());
                ComponentToolKit.setTextFieldEditable(IAECrawler_AccountText,false);
            }
        }
    }
    private void initialIAECrawlerSettingComponent(){
        IAECrawler_ObjectIDText.setText("");
        IAECrawler_ObjectNameText.setText("");
        IAECrawler_AccountText.setText("");
        IAECrawler_PasswordText.setText("");
        IAECrawler_ManufacturerNickNameText.setText("");
        IAECrawler_ManufacturerAllNameText.setText("");
        ComponentToolKit.setRadioButtonSelect(exportQuotationRadioButton_IAECrawler,false);
        ComponentToolKit.setRadioButtonSelect(noneExportQuotationRadioButton_IAECrawler,true);
        for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelong_CheckBoxMap.keySet()){
            IAECrawlerBelong_CheckBoxMap.get(IAECrawlerBelong_Bean).setSelected(false);
        }
        IAECrawlerAccount_TableView.getSelectionModel().select(null);
        IAECrawlerObjectInfo_Bean = null;
        ComponentToolKit.setButtonDisable(IAECrawlerAccount_InsertButton,false);
        ComponentToolKit.setButtonDisable(IAECrawlerAccount_ModifyButton,true);
        ComponentToolKit.setButtonDisable(IAECrawlerAccount_DeleteButton,true);
        ComponentToolKit.setTextFieldEditable(IAECrawler_AccountText,true);
    }
    @FXML protected void saveExportCompanyFormatMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<ExportCompanyFormat_Bean> exportCompanyFormatList = ComponentToolKit.getExportCompanyFormatTableViewItemList(exportCompanyFormat_TableView);
            if(isExportCompanyFormatError(exportCompanyFormatList))
                return;

            JSONObject JSONObject = new JSONObject();
            for(ExportCompanyFormat_Bean ExportCompanyFormat_Bean : exportCompanyFormatList){
                JSONObject.put(ExportCompanyFormat_Bean.getExportCompanyFormat().name(),ExportCompanyFormat_Bean.getExportCompanyInfoFormat());
            }
            if (SystemSettingConfig_Bean.getExportCompanyFormat() == null) {
                if (SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.公司匯出格式, JSONObject.toString())) {
                    SystemSettingConfig_Bean.setExportCompanyFormat(JSONObject);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            } else {
                if (SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.公司匯出格式, JSONObject.toString())) {
                    SystemSettingConfig_Bean.setExportCompanyFormat(JSONObject);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    private boolean isExportCompanyFormatError(ObservableList<ExportCompanyFormat_Bean> exportCompanyFormatList){
        for(ExportCompanyFormat_Bean ExportCompanyFormat_Bean : exportCompanyFormatList){
            if(ExportCompanyFormat_Bean.getExportCompanyFormat() == ExportCompanyFormat.companyName_fontSize && !ToolKit.isDigital(ExportCompanyFormat_Bean.getExportCompanyInfoFormat())){
                DialogUI.MessageDialog("※ 公司名稱 字體大小 格式錯誤");
                return true;
            }else if(ExportCompanyFormat_Bean.getExportCompanyFormat() == ExportCompanyFormat.companyAddress_fontSize && !ToolKit.isDigital(ExportCompanyFormat_Bean.getExportCompanyInfoFormat())){
                DialogUI.MessageDialog("※ 公司地址 字體大小 格式錯誤");
                return true;
            }else if(ExportCompanyFormat_Bean.getExportCompanyFormat() == ExportCompanyFormat.companyTelephone_fax_fontSize && !ToolKit.isDigital(ExportCompanyFormat_Bean.getExportCompanyInfoFormat())){
                DialogUI.MessageDialog("※ 公司電話與傳真 字體大小 格式錯誤");
                return true;
            }
        }
        return false;
    }
    @FXML protected void exportCompanyInfoFormatOnEditCommit(TableColumn.CellEditEvent<ExportCompanyFormat_Bean,String> exportCompanyInfoFormatOnEditCommit){
        ExportCompanyFormat_Bean ExportCompanyFormat_Bean = ComponentToolKit.getExportCompanyFormatTableViewSelectItem(exportCompanyFormat_TableView);
        ExportCompanyFormat_Bean.setExportCompanyInfoFormat(exportCompanyInfoFormatOnEditCommit.getNewValue());
    }
    @FXML protected void saveExportTemplateMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            /*ObservableList<ExportCompanyFormat_Bean> exportCompanyFormatList = ComponentToolKit.getExportCompanyFormatTableViewItemList(exportCompanyFormat_TableView);
            if(isExportCompanyFormatError(exportCompanyFormatList))
                return;

            JSONObject JSONObject = new JSONObject();
            for(ExportCompanyFormat_Bean ExportCompanyFormat_Bean : exportCompanyFormatList){
                JSONObject.put(ExportCompanyFormat_Bean.getExportCompanyFormat().name(),ExportCompanyFormat_Bean.getExportCompanyInfoFormat());
            }
            if (SystemSettingConfig_Bean.getExportCompanyFormat() == null) {
                if (SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.公司匯出格式, JSONObject.toString())) {
                    SystemSettingConfig_Bean.setExportCompanyFormat(JSONObject);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            } else {
                if (SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.公司匯出格式, JSONObject.toString())) {
                    SystemSettingConfig_Bean.setExportCompanyFormat(JSONObject);
                    DialogUI.MessageDialog("※ 設定成功");
                } else DialogUI.MessageDialog("※ 設定失敗");
            }*/
        }
    }
    @FXML protected void templateNameOnEditCommit(TableColumn.CellEditEvent<ExportCompanyFormat_Bean,String> exportTemplateInfoNameOnEditCommit){
        ExportTemplateName_Bean ExportTemplateName_Bean = ComponentToolKit.getExportTemplateNameTableViewSelectItem(exportTemplateName_TableView);
        ExportTemplateName_Bean.setExportTemplateInfoName(exportTemplateInfoNameOnEditCommit.getNewValue());
    }
    private VideoCapture selectIpCam = null;
    private boolean stopPreviewCamera = false;
    private ScheduledExecutorService timer;
    @FXML protected void previewIpCamMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IpCamInfo_Bean IpCamInfo_Bean = ComponentToolKit.getIpCamTableViewSelectItem(IpCam_TableView);
            if(IpCamInfo_Bean != null){
                openIpCamera(IpCamInfo_Bean);
            }
        }
    }
    private void openIpCamera(IpCamInfo_Bean IpCamInfo_Bean){
        if(selectIpCam != null) closeIpCamera();
        selectIpCam = new VideoCapture();
        selectIpCam.open(IpCamInfo_Bean.getRTSP());
        if (selectIpCam.isOpened()){
            stopPreviewCamera = false;
            Runnable frameGrabber = () -> {
                Mat frame = new Mat();
                selectIpCam.read(frame);
                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".png", frame, byteMat);
                Image imageToShow = new Image(new ByteArrayInputStream(byteMat.toArray()));
                IpCam_PreviewImageView.setImage(stopPreviewCamera ? null : imageToShow);
            };
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
        }else
            DialogUI.MessageDialog("※ 影像串流開啟失敗");
    }
    private void closeIpCamera() {
        if (selectIpCam != null) {
            selectIpCam.release();
            selectIpCam = null;
            timer.shutdown();
            timer = null;
            stopPreviewCamera = true;
        }
    }
    @FXML protected void insertIpCamMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String name = IpCam_NameText.getText();
            String RTSP = IpCam_RTSPText.getText();
            if(name == null || name.equals(""))
                DialogUI.MessageDialog("※ 請輸入名稱");
            else if(RTSP == null || RTSP.equals(""))
                DialogUI.MessageDialog("※ 請輸入RTSP");
            else{
                IpCamInfo_Bean IpCamInfo_Bean = new IpCamInfo_Bean();
                IpCamInfo_Bean.setDefaultPreview(IpCam_DefaultYes_RadioButton.isSelected());
                IpCamInfo_Bean.setName(name);
                IpCamInfo_Bean.setRTSP(RTSP);
                if(SystemSetting_Model.insertIpCam(IpCamInfo_Bean)) {
                    DialogUI.MessageDialog("※ 新增成功");
                    if(IpCamInfo_Bean.getDefaultPreview()){
                        ObservableList<IpCamInfo_Bean> ipCamList = ComponentToolKit.getIpCamTableViewItemList(IpCam_TableView);
                        for(IpCamInfo_Bean selectIpCamInfo_Bean : ipCamList)
                            selectIpCamInfo_Bean.setDefaultPreview(false);
                    }
                    ComponentToolKit.getIpCamTableViewItemList(IpCam_TableView).add(IpCamInfo_Bean);
                    initialIpCamComponent();
                }else
                    DialogUI.MessageDialog("※ 新增失敗");
            }
        }
    }
    @FXML protected void clearIpCamMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            initialIpCamComponent();
        }
    }
    @FXML protected void modifyIpCamMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IpCamInfo_Bean selectIpCam_Info_Bean = ComponentToolKit.getIpCamTableViewSelectItem(IpCam_TableView);
            if(selectIpCam_Info_Bean != null){
                boolean defaultPreview = IpCam_DefaultYes_RadioButton.isSelected();
                String modifyName = IpCam_NameText.getText();
                String modifyRTSP = IpCam_RTSPText.getText();
                if(modifyName == null || modifyName.equals(""))
                    DialogUI.MessageDialog("※ 請輸入名稱");
                else if(modifyRTSP == null || modifyRTSP.equals(""))
                    DialogUI.MessageDialog("※ 請輸入RTSP");
                else{
                    if(DialogUI.ConfirmDialog("※ 是否確定修改 ?",true,false,0,0)){
                        if(SystemSetting_Model.modifyIpCam(selectIpCam_Info_Bean.getId(),defaultPreview,modifyName,modifyRTSP)){
                            DialogUI.MessageDialog("※ 修改成功");
                            if(defaultPreview){
                                ObservableList<IpCamInfo_Bean> ipCamList = ComponentToolKit.getIpCamTableViewItemList(IpCam_TableView);
                                for(IpCamInfo_Bean selectIpCamInfo_Bean : ipCamList)
                                    selectIpCamInfo_Bean.setDefaultPreview(false);
                            }
                            selectIpCam_Info_Bean.setDefaultPreview(defaultPreview);
                            selectIpCam_Info_Bean.setName(modifyName);
                            selectIpCam_Info_Bean.setRTSP(modifyRTSP);
                        }else
                            DialogUI.MessageDialog("※ 修改失敗");
                    }
                }
            }else
                DialogUI.MessageDialog("※ 請選擇攝影機");
        }
    }
    @FXML protected void deleteIpCamMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IpCamInfo_Bean IpCamInfo_Bean = ComponentToolKit.getIpCamTableViewSelectItem(IpCam_TableView);
            if(IpCamInfo_Bean != null && DialogUI.ConfirmDialog("※ 是否確定刪除 ?",true,false,0,0)){
                if(SystemSetting_Model.deleteIpCam(IpCamInfo_Bean)){
                    DialogUI.MessageDialog("※ 刪除成功");
                    ComponentToolKit.getIpCamTableViewItemList(IpCam_TableView).remove(IpCamInfo_Bean);
                    initialIpCamComponent();
                }else
                    DialogUI.MessageDialog("※ 刪除失敗");
            }else
                DialogUI.MessageDialog("※ 請選擇攝影機");
        }
    }
    @FXML protected void IpCamTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IpCamInfo_Bean IpCamInfo_Bean = ComponentToolKit.getIpCamTableViewSelectItem(IpCam_TableView);
            if(IpCamInfo_Bean != null){
                ComponentToolKit.setRadioButtonSelect(IpCam_DefaultYes_RadioButton,IpCamInfo_Bean.getDefaultPreview());
                ComponentToolKit.setRadioButtonSelect(IpCam_DefaultNo_RadioButton,!IpCamInfo_Bean.getDefaultPreview());
                ComponentToolKit.setButtonDisable(IpCam_PreviewButton,false);
                ComponentToolKit.setButtonDisable(IpCam_InsertButton,true);
                ComponentToolKit.setButtonDisable(IpCam_ModifyButton,false);
                ComponentToolKit.setButtonDisable(IpCam_DeleteButton,false);

                IpCam_NameText.setText(IpCamInfo_Bean.getName());
                IpCam_RTSPText.setText(IpCamInfo_Bean.getRTSP());
            }
        }
    }
    private void initialIpCamComponent(){
        IpCam_DefaultYes_RadioButton.setSelected(false);
        IpCam_DefaultNo_RadioButton.setSelected(true);
        IpCam_NameText.setText("");
        IpCam_RTSPText.setText("");
        ComponentToolKit.setButtonDisable(IpCam_PreviewButton,true);
        ComponentToolKit.setButtonDisable(IpCam_InsertButton,false);
        ComponentToolKit.setButtonDisable(IpCam_ModifyButton,true);
        ComponentToolKit.setButtonDisable(IpCam_DeleteButton,true);
        closeIpCamera();
    }

    /** 參數檔更新 - 更新檔案 */
    @FXML protected void UpdateConfigMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try{
                Task Task = updateConfigTask();
                UpdateConfig_ProgressBar.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    private Task updateConfigTask(){
        return new Task() {
            @Override public Void call(){
                try{
                    UpdateConfig_ProgressBar.setVisible(true);
                    Platform.runLater(() -> UpdateConfig_StatusLabel.setText("更新中..."));

                    JSONObject TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.報價單);
                    JSONObject newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.報價單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.待出貨與子貨單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.待出貨與子貨單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.出貨與出退貨單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.出貨與出退貨單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.出貨單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.出貨單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.採購單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.採購單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.待入倉與子貨單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.待入倉與子貨單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.進貨與進退貨單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.進貨與進退貨單,newJSONObject);

                    TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.進貨單);
                    newJSONObject = new JSONObject(new LinkedHashMap());
                    for(Order_Enum.SearchOrderTableColumn SearchOrderTableColumn : Order_Enum.SearchOrderTableColumn.values()){
                        if(TableViewSettingMap.containsKey(SearchOrderTableColumn.name())){
                            newJSONObject.put(SearchOrderTableColumn.name(),TableViewSettingMap.get(SearchOrderTableColumn.name()));
                        }else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量){
                            newJSONObject.put(SearchOrderTableColumn.name(),true);
                        }
                    }
                    CallConfig.setSearchOrderTableViewSettingJson(Order_Enum.SearchOrderSource.進貨單,newJSONObject);

                    Platform.runLater(() -> UpdateConfig_StatusLabel.setText("更新完成!!"));
                }catch (Exception Ex){
                    Platform.runLater(() -> UpdateConfig_StatusLabel.setText("更新失敗!!"));
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }finally {
                    UpdateConfig_ProgressBar.setVisible(false);
                }
                return null;
            }
        };
    }
    /** Button Mouse Clicked - 回主選單 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            closeIpCamera();
            setChanged();
            notifyObservers();
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private ArrayList<IAECrawlerBelong_Bean> getSelectIAECrawlerBelong(){
        ArrayList<IAECrawlerBelong_Bean> selectBelongList = null;
        for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelong_CheckBoxMap.keySet()){
            if(IAECrawlerBelong_CheckBoxMap.get(IAECrawlerBelong_Bean).isSelected()){
                if(selectBelongList == null)    selectBelongList = new ArrayList<>();
                selectBelongList.add(IAECrawlerBelong_Bean);
            }
        }
        return selectBelongList;
    }
    private void setColumnCellValueFactory(TableColumn<CustomerIDCharacter_Bean,String> TableColumn, String ColumnValue){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
    }
    private void setCustomerLocationColumnStyle(TableColumn<CustomerIDCharacter_Bean,String> TableColumn){
        TableColumn.setStyle("-fx-font-size: 16 px;-fx-alignment: CENTER-LEFT;-fx-text-fill: blue");
    }
    private void setCustomerIDCharacterColumnStyle(TableColumn<CustomerIDCharacter_Bean,String> TableColumn){
        TableColumn.setStyle("-fx-font-size: 16 px;-fx-alignment: CENTER-LEFT;-fx-text-fill: red");
    }
    private void setTheSameAsNoneInvoiceColumnCheckBox(TableColumn<IAECrawlerAccount_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setTheSameAsNoneInvoiceCheckBox(Alignment, FontSize));
    }
    private class setTheSameAsNoneInvoiceCheckBox extends TableCell<IAECrawlerAccount_Bean, Boolean> {
        String Alignment, FontSize;
        setTheSameAsNoneInvoiceCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
//            if(!empty){
//                setGraphic(getTableView().getItems().get(getIndex()).getTheSameANoneInvoice_CheckBox());
//                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
//            }else   setGraphic(null);

            if (item == null || empty) {
                setGraphic(null);
            } else {
                IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerAccountTableViewItemList(getTableView()).get(getIndex());
                if(IAECrawlerAccount_Bean != null){
//                    checkBox.setSelected(IAECrawlerAccount_Bean.isTheSameAsNoneInvoice());
                    CheckBox theSameANoneInvoice_CheckBox = IAECrawlerAccount_Bean.getTheSameANoneInvoice_CheckBox();
                    theSameANoneInvoice_CheckBox.setOnAction(ActionEvent -> {
                        if(SystemSetting_Model.updateExportQuotation_theSameAsNoneInvoiceVendor(IAECrawlerAccount_Bean.getExportQuotation_id(), theSameANoneInvoice_CheckBox.isSelected())){
                            ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountTableViewItemList(getTableView());
                            for(IAECrawlerAccount_Bean iAECrawlerAccount_Bean : IAECrawlerAccountList){
                                if(iAECrawlerAccount_Bean.getTheSameANoneInvoice_CheckBox() == theSameANoneInvoice_CheckBox){
//                                    iAECrawlerAccount_Bean.setTheSameAsNoneInvoice(theSameANoneInvoice_CheckBox.isSelected());
                                }else {
                                    iAECrawlerAccount_Bean.setTheSameAsNoneInvoice(false);
                                }
                            }
//                            IAECrawlerAccount_Bean.setTheSameAsNoneInvoice(theSameANoneInvoice_CheckBox.isSelected());
                            DialogUI.MessageDialog("切換成功");
                        }else{
                            System.out.println("失敗");
                            theSameANoneInvoice_CheckBox.setSelected(false);
                        }
                    });
                    setGraphic(theSameANoneInvoice_CheckBox);
                }
            }
        }
    }
    private void setIpCamDefaultColumnText(TableColumn<IpCamInfo_Bean,Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIpCamDefaultText(Alignment, FontSize));
    }
    private class setIpCamDefaultText extends TableCell<IpCamInfo_Bean, Boolean> {
        String Alignment, FontSize;
        setIpCamDefaultText(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                if(item){
                    setText("√");
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-text-fill:red");
                }else{
                    setText("");
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-text-fill:black");
                }
            }
        }
    }
}
