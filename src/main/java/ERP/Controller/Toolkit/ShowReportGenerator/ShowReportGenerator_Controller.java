package ERP.Controller.Toolkit.ShowReportGenerator;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Item_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Model.Order.Order_Model;
import ERP.Model.Order.ExportReportGenerator.ExportProcurementDocument_Model;
import ERP.Model.Order.ExportReportGenerator.ResponsibilityGuarantee_Model;
import ERP.Model.Order.ExportReportGenerator.SelfPurchasedApplicationForm_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.*;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ShowReportGenerator_Controller {
    @FXML TabPane TabPane;
    @FXML Tab InsertReportGenerator_Tab, SearchReportGenerator_Tab;
    @FXML Accordion accordion;
    @FXML ComboBox<IAECrawlerAccount_Bean> ExportVendor_ComboBox;
    @FXML TextField ItemName_TextField;
    @FXML TextArea Remark_TextArea;
    @FXML Button InsertItem_Button, DeleteItem_Button;
    @FXML TableColumn<ReportGenerator_Item_Bean,Integer> itemNumberColumn;
    @FXML TableColumn<ReportGenerator_Item_Bean,String> itemNameColumn;
    @FXML TableView<ReportGenerator_Item_Bean> TableView;

    private final KeyCombination previousMoveKeyCombine = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);
    private final KeyCombination nextMoveKeyCombine = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);

    private final String[] TableColumns = { "項號", "名稱" };
    private final String[] TableColumnCellValue = new String[]{"itemNumber","itemName" };
    private final int[] ColumnsLength = { 45, 800 };

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallConfig CallConfig;

    private Order_Model Order_Model;
    private SystemSetting_Model SystemSetting_Model;

    private Stage Stage;
    private Order_Bean Order_Bean;
    private boolean isSelectProductGroup;
    private String projectName;
    private int totalPriceIncludeTax;
    private HashMap<TitledPane,Button> TitlePaneAndButtonMap = new HashMap<>();
    public ShowReportGenerator_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallConfig = ToolKit.CallConfig;

        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setOrderInfo(Order_Bean Order_Bean, boolean isSelectProductGroup, String projectName, int totalPriceIncludeTax){
        this.Order_Bean = Order_Bean;
        this.isSelectProductGroup = isSelectProductGroup;
        this.projectName = projectName;
        this.totalPriceIncludeTax = totalPriceIncludeTax;
    }
    public void setComponent(){
        initialTableView();
        setExportQuotationVendorItems();
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(itemNumberColumn,"itemNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(itemNameColumn,"itemName", "CENTER-LEFT", "16",null);
        itemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    private void setExportQuotationVendorItems(){
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(false);
        ComponentToolKit.setIAECrawlerAccountComboBoxObj_ExportQuotation(ExportVendor_ComboBox);

        ExportVendor_ComboBox.getItems().addAll(IAECrawlerAccountList);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            if(IAECrawlerAccount_Bean.isExportQuotation_defaultSelect()) {
                ExportVendor_ComboBox.getSelectionModel().select(IAECrawlerAccount_Bean);
                break;
            }
        }
    }
    @FXML protected void SearchReportGeneratorSelectionChanged(){
        initialComponent();
        if(SearchReportGenerator_Tab.isSelected()){
            TitlePaneAndButtonMap.clear();
            accordion.getPanes().clear();
            ObservableList<ReportGenerator_Bean> reportGeneratorList = Order_Model.getReportGenerator(Order_Bean.getOrderSource(),Order_Bean.getOrderID());
            for(ReportGenerator_Bean ReportGenerator_Bean : reportGeneratorList){
                TitledPane TitlePane = createTitlePane(ReportGenerator_Bean);
                BorderPane BorderPane = createBorderPane(ReportGenerator_Bean);
                TitlePane.setContent(BorderPane);
                accordion.getPanes().add(TitlePane);
            }
        }
    }
    @FXML protected void ItemNameOnEditCommit(TableColumn.CellEditEvent<ReportGenerator_Item_Bean,String> itemNameOnEditCommit){
        ReportGenerator_Item_Bean ReportGenerator_Item_Bean = ComponentToolKit.getReportGeneratorItemTableViewSelectItem(TableView);
        if(ReportGenerator_Item_Bean != null)
            ReportGenerator_Item_Bean.setItemName(itemNameOnEditCommit.getNewValue());
    }
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ReportGenerator_Item_Bean ReportGenerator_Item_Bean = ComponentToolKit.getReportGeneratorItemTableViewSelectItem(TableView);
            ComponentToolKit.setButtonDisable(DeleteItem_Button,ReportGenerator_Item_Bean == null);
        }
    }
    @FXML protected void TableViewKeyPressed(KeyEvent KeyEvent){
        moveItems(KeyEvent,ComponentToolKit.getReportGeneratorItemTableViewSelectItem(TableView), ComponentToolKit.getReportGeneratorItemTableViewSelectIndex(TableView));
    }
    private void moveItems(KeyEvent KeyEvent, ReportGenerator_Item_Bean ReportGenerator_Item_Bean, int nowIndex){
        if(ReportGenerator_Item_Bean == null)    return;
        if(previousMoveKeyCombine.match(KeyEvent)) {
            if(nowIndex != 0){
                ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).remove(nowIndex);
                ReportGenerator_Item_Bean previousReportGenerator_Item_Bean = ComponentToolKit.getReportGeneratorItemTableViewSelectItem(TableView,nowIndex-1);
                previousReportGenerator_Item_Bean.setItemNumber(previousReportGenerator_Item_Bean.getItemNumber()+1);

                ReportGenerator_Item_Bean.setItemNumber(ReportGenerator_Item_Bean.getItemNumber()-1);
                ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).add(nowIndex - 1, ReportGenerator_Item_Bean);
                TableView.getSelectionModel().select(nowIndex);
            }
        }else if(nextMoveKeyCombine.match(KeyEvent)){
            if(nowIndex != ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).size()-1){
                ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).remove(nowIndex);
                ReportGenerator_Item_Bean nextReportGenerator_Item_Bean = ComponentToolKit.getReportGeneratorItemTableViewSelectItem(TableView,nowIndex);
                nextReportGenerator_Item_Bean.setItemNumber(nextReportGenerator_Item_Bean.getItemNumber()-1);

                ReportGenerator_Item_Bean.setItemNumber(ReportGenerator_Item_Bean.getItemNumber()+1);
                ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).add(nowIndex + 1, ReportGenerator_Item_Bean);
                TableView.getSelectionModel().select(nowIndex);
            }
        }
    }
    @FXML protected void ItemNameKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            insertItem(ItemName_TextField.getText());
    }
    @FXML protected void InsertItemMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            insertItem(ItemName_TextField.getText());
    }
    private void insertItem(String itemName){
        if(itemName == null || itemName.equals(""))
            DialogUI.MessageDialog("※ 請輸入名稱");
        else{
            if(isItemNameExist(itemName))
                DialogUI.MessageDialog("※ 商品名稱已存在");
            else{
                ObservableList<ReportGenerator_Item_Bean> itemList = ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView);

                ReportGenerator_Item_Bean ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
                ReportGenerator_Item_Bean.setItemNumber(itemList.size()+1);
                ReportGenerator_Item_Bean.setItemName(itemName);
                itemList.add(ReportGenerator_Item_Bean);
                ItemName_TextField.setText("");
            }
        }
    }
    private boolean isItemNameExist(String newItemName){
        ObservableList<ReportGenerator_Item_Bean> itemList = ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView);
        for(ReportGenerator_Item_Bean ReportGenerator_Item_Bean : itemList){
            if(ReportGenerator_Item_Bean.getItemName().equals(newItemName))
                return true;
        }
        return false;
    }
    @FXML protected void DeleteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ReportGenerator_Item_Bean ReportGenerator_Item_Bean = ComponentToolKit.getReportGeneratorItemTableViewSelectItem(TableView);
            if(ReportGenerator_Item_Bean == null)
                DialogUI.MessageDialog("※ 請選擇品項");
            else {
                ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).remove(ReportGenerator_Item_Bean);
                refreshItemNumber();
            }
        }
    }
    private void refreshItemNumber(){
        ObservableList<ReportGenerator_Item_Bean> itemList = ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView);
        for(int index = 0 ; index < itemList.size() ; index++){
            ReportGenerator_Item_Bean ReportGenerator_Item_Bean = itemList.get(index);
            ReportGenerator_Item_Bean.setItemNumber(index+1);
        }
    }
    @FXML protected void ImportProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(isSelectProductGroup){
                HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
                for (Integer group_itemNumber : productGroupMap.keySet()) {
                    ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                    if(!isItemNameExist(ProductGroup_Bean.getGroupName())){
                        ReportGenerator_Item_Bean ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
                        ReportGenerator_Item_Bean.setItemNumber(ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).size()+1);
                        ReportGenerator_Item_Bean.setItemName(ProductGroup_Bean.getGroupName());
                        ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).add(ReportGenerator_Item_Bean);
                    }
                }
                DialogUI.MessageDialog("※ 匯入完成");
            }else{
                ObservableList<OrderProduct_Bean> orderProductList = Order_Bean.getProductList();
                for(OrderProduct_Bean OrderProduct_Bean : orderProductList){
                    boolean isItemNameExist = isItemNameExist(OrderProduct_Bean.getProductName());
                    if(!isItemNameExist) {
                        ReportGenerator_Item_Bean ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
                        ReportGenerator_Item_Bean.setItemNumber(ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).size()+1);
                        ReportGenerator_Item_Bean.setItemName(OrderProduct_Bean.getProductName());
                        ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).add(ReportGenerator_Item_Bean);
                    }
                }
                DialogUI.MessageDialog("※ 匯入完成");
            }
        }
    }
    @FXML protected void SaveMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<ReportGenerator_Item_Bean> itemList = ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView);
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(ExportVendor_ComboBox);
            if(itemList.size() == 0)
                DialogUI.MessageDialog("※ 不存在任何品項");
            else if(IAECrawlerAccount_Bean == null)
                DialogUI.MessageDialog("※ 請選擇匯出廠商");
            else if(!IAECrawlerAccount_Bean.isExportQuotation())
                DialogUI.MessageDialog("※ 該廠商不允許匯出");
            else{
                boolean isStillSave = true;
                if(!isExistTemplate(IAECrawlerAccount_Bean))
                    isStillSave = DialogUI.ConfirmDialog("※ 該廠商未存在範本，仍要儲存 ?",true,false,0,0);
                if(isStillSave){
                    ReportGenerator_Bean ReportGenerator_Bean = new ReportGenerator_Bean();
                    ReportGenerator_Bean.setOrderOrSubBill_id(Order_Bean.getOrderID());
                    ReportGenerator_Bean.setExport_manufacturer_id(IAECrawlerAccount_Bean.getExportQuotation_id());
                    ReportGenerator_Bean.setOrderSource(Order_Bean.getOrderSource());
                    ReportGenerator_Bean.setRemark(Remark_TextArea.getText() == null ? "" : Remark_TextArea.getText());
                    ReportGenerator_Bean.setReportGenerator_itemList(itemList);
                    if(Order_Model.insertReportGenerator(ReportGenerator_Bean)){
                        initialComponent();
                        DialogUI.MessageDialog("※ 儲存成功");
                    }else
                        DialogUI.MessageDialog("※ 儲存失敗");
                }
            }
        }
    }
    private boolean isExistTemplate(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        String VendorName = IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();
        String Template = "Template/採購文件/" + VendorName + "-規格表.docx";
        File file =new File(Template);
        return file.exists() || file.isDirectory();
    }
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private void initialComponent(){
        ExportVendor_ComboBox.getSelectionModel().select(null);
        Remark_TextArea.setText("");
        ItemName_TextField.setText("");
        ComponentToolKit.setButtonDisable(DeleteItem_Button,true);
        ComponentToolKit.getReportGeneratorItemTableViewItemList(TableView).clear();
    }
    private TitledPane createTitlePane(ReportGenerator_Bean ReportGenerator_Bean){
        TitledPane TitledPane = new TitledPane();
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        String vendorName = "";
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountComboBoxItemList(ExportVendor_ComboBox);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            if(IAECrawlerAccount_Bean.getExportQuotation_id() == ReportGenerator_Bean.getExport_manufacturer_id()){
                vendorName = IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();
                break;
            }
        }
        HBoxChild.add(ComponentToolKit.setLabel(ReportGenerator_Bean.getInsertDateTime() + "  " + vendorName,-1, -1,18,""));
        Button export_Button = ComponentToolKit.setButton("匯出",120,-1,16);
        export_Button.setDisable(true);
        export_Button.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        HBoxChild.add(export_Button);

        Button getInfo_Button = ComponentToolKit.setButton("編輯紀錄",120,-1,16);
        getInfo_Button.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        HBoxChild.add(getInfo_Button);

        TitledPane.setPrefWidth(870);
        TitledPane.setGraphic(HBox);
        TitledPane.setExpanded(false);
        TitlePaneAndButtonMap.put(TitledPane,export_Button);

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
        export_Button.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                IAECrawlerAccount_Bean thisIAECrawlerAccount_Bean = null;
                for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
                    if(IAECrawlerAccount_Bean.getExportQuotation_id() == ReportGenerator_Bean.getExport_manufacturer_id()){
                        thisIAECrawlerAccount_Bean = IAECrawlerAccount_Bean;
                        break;
                    }
                }
                if(thisIAECrawlerAccount_Bean == null)
                    DialogUI.MessageDialog("※ 不存在廠商");
                else if(!isExistTemplate(thisIAECrawlerAccount_Bean))
                    DialogUI.MessageDialog("※ 該廠商未存在範本");
                else{
                    try{
                        exportProcurementDocument(ReportGenerator_Bean, thisIAECrawlerAccount_Bean);
                        exportResponsibilityGuarantee(thisIAECrawlerAccount_Bean);
                        exportSelfPurchasedApplicationForm(ReportGenerator_Bean, thisIAECrawlerAccount_Bean);

                        DialogUI.MessageDialog("※ 匯出成功");
                    }catch (Exception Ex){
                        DialogUI.MessageDialog("※ 匯出失敗");
                        ERPApplication.Logger.catching(Ex);
                        DialogUI.ExceptionDialog(Ex);
                    }
                }
            }
        });
        getInfo_Button.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                setReportGeneratorInfo(ReportGenerator_Bean);
            }
        });
        return TitledPane;
    }
    private void exportProcurementDocument(ReportGenerator_Bean ReportGenerator_Bean, IAECrawlerAccount_Bean thisIAECrawlerAccount_Bean) throws IOException {
        String vendorNickName = thisIAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();

        List<String> productList = new ArrayList<>();
        ObservableList<ReportGenerator_Item_Bean> reportGeneratorItemList = ReportGenerator_Bean.getReportGenerator_itemList();
        for(ReportGenerator_Item_Bean ReportGenerator_Item_Bean : reportGeneratorItemList){
            productList.add(ReportGenerator_Item_Bean.getItemName());
        }
        ExportProcurementDocument_Model export = ExportProcurementDocument_Model.createTemplate(TemplatePath.ProcurementDocument_ProductList(vendorNickName))
                .setProductName(projectName)
                .setProductList(productList);
        switch (vendorNickName){
            case "協新":
                export.buildSheiSin();
                break;
            case "啟廉":
                export.buildChiLen();
                break;
            case "展源浩":
                export.buildZhanyh();
                break;
            case "皇佳":
                export.buildHuangGia();
                break;
            case "福大興":
                export.buildFuDaXing();
                break;
        }
        export.export(CallConfig.getFile_OutputPath() + "\\" + vendorNickName+"_" + projectName + "_設備產品規格表.docx");
    }
    private void exportResponsibilityGuarantee(IAECrawlerAccount_Bean thisIAECrawlerAccount_Bean) throws IOException {
        String vendorNickName = thisIAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();

        ResponsibilityGuarantee_Model ResponsibilityGuarantee = ResponsibilityGuarantee_Model.createTemplate(TemplatePath.ProcurementDocument_ResponsibilityGuarantee(vendorNickName));
        ResponsibilityGuarantee.build(projectName,
                LocalDate.parse(ToolKit.getToday("yyyy-MM-dd")),
                thisIAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
        ResponsibilityGuarantee.export(CallConfig.getFile_OutputPath() + "\\" + vendorNickName+"_" + projectName + "_軟體及設備採購權利及責任切結書.docx");
    }
    private void exportSelfPurchasedApplicationForm(ReportGenerator_Bean ReportGenerator_Bean, IAECrawlerAccount_Bean thisIAECrawlerAccount_Bean) throws IOException {
        String vendorNickName = thisIAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();

        SelfPurchasedApplicationForm_Model SelfPurchasedApplicationForm = SelfPurchasedApplicationForm_Model.createTemplate(TemplatePath.SelfPurchaseApplicationFormTemplate);

        SelfPurchasedApplicationForm.setDate(Order_Bean.getAlreadyOrderDate() == null ? LocalDate.parse(Order_Bean.getOrderDate()) : LocalDate.parse(Order_Bean.getAlreadyOrderDate()));

        ObjectInfo_Bean ObjectInfo_Bean = Order_Model.getObjectFromSearchColumn(Order_Enum.OrderObject.客戶, Order_Enum.ObjectSearchColumn.客戶編號, Order_Bean.getObjectID()).get(0);
        SelfPurchasedApplicationForm.setCustomer(ObjectInfo_Bean.getPersonInCharge());
        if(!ObjectInfo_Bean.getTelephone1().equals("") && ObjectInfo_Bean.getTelephone1().contains("#"))
            SelfPurchasedApplicationForm.setPhoneBranch(ObjectInfo_Bean.getTelephone1().substring(ObjectInfo_Bean.getTelephone1().lastIndexOf("#")+1));
        else
            SelfPurchasedApplicationForm.setPhoneBranch("");

        SelfPurchasedApplicationForm.setProductName(projectName);

        String allProduct = "";
        ObservableList<ReportGenerator_Item_Bean> reportGeneratorItemList = ReportGenerator_Bean.getReportGenerator_itemList();
        for(ReportGenerator_Item_Bean ReportGenerator_Item_Bean : reportGeneratorItemList){
            if(allProduct.equals(""))
                allProduct = ReportGenerator_Item_Bean.getItemName();
            else
                allProduct = allProduct + "、" + ReportGenerator_Item_Bean.getItemName();
        }
        SelfPurchasedApplicationForm.setAllProduct(allProduct);
        SelfPurchasedApplicationForm.setProductPrice(totalPriceIncludeTax);
        SelfPurchasedApplicationForm.setCommit(ReportGenerator_Bean.getRemark());

        SelfPurchasedApplicationForm.build();
        SelfPurchasedApplicationForm.export(CallConfig.getFile_OutputPath() + "\\" + vendorNickName+"_" + projectName + "_不利用「共同供應契約」自行採購財物及採購非環保指定項目商品申請書.docx");
    }
    private void setReportGeneratorInfo(ReportGenerator_Bean ReportGenerator_Bean){
        TabPane.getSelectionModel().select(InsertReportGenerator_Tab);
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = ComponentToolKit.getIAECrawlerAccountComboBoxItemList(ExportVendor_ComboBox);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            if(IAECrawlerAccount_Bean.getExportQuotation_id() == ReportGenerator_Bean.getExport_manufacturer_id()){
                ExportVendor_ComboBox.getSelectionModel().select(IAECrawlerAccount_Bean);
                break;
            }
        }
        Remark_TextArea.setText(ReportGenerator_Bean.getRemark());
        TableView.getItems().addAll(ReportGenerator_Bean.getReportGenerator_itemList());
    }
    private BorderPane createBorderPane(ReportGenerator_Bean ReportGenerator_Bean){
        TableView<ReportGenerator_Item_Bean> TableView = new TableView<>();
        TableView.setMaxSize(870,320);
        TableView.setEditable(true);
        for(int index = 0 ; index < TableColumns.length ; index++) {
            if(index == 0) {
                TableColumn<ReportGenerator_Item_Bean,Integer> TableColumn = new TableColumn<>();
                TableColumn.setText(TableColumns[index]);
                TableColumn.setPrefWidth(ColumnsLength[index]);
                ComponentToolKit.setColumnCellValue(TableColumn, TableColumnCellValue[index], "CENTER-LEFT", "14",null);
                TableColumn.setResizable(false);
                TableColumn.setSortable(false);
                TableView.getColumns().add(TableColumn);
            }else {
                TableColumn<ReportGenerator_Item_Bean,String> TableColumn = new TableColumn<>();
                TableColumn.setText(TableColumns[index]);
                TableColumn.setPrefWidth(ColumnsLength[index]);
                ComponentToolKit.setColumnCellValue(TableColumn, TableColumnCellValue[index], "CENTER-LEFT", "14",null);
                TableColumn.setResizable(false);
                TableColumn.setSortable(false);
                TableView.getColumns().add(TableColumn);
            }
        }
        TableView.getItems().addAll(ReportGenerator_Bean.getReportGenerator_itemList());
        BorderPane BorderPane = ComponentToolKit.setBorderPane(5,5,5,5);
        BorderPane.setCenter(TableView);
        return BorderPane;
    }
}
