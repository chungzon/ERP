package ERP.Controller.SystemSetting;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.SystemSetting.QuotationTemplateFormatSetting_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.Enum.Order.Order_Enum.ExportQuotationFormat;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ExportQuotationTemplateTitle;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuotationTemplateFormatSetting_Controller {
    @FXML Button twoFormat_Button, threeFormat_Button, synchronizeTwoFormat_Button;
    @FXML TableColumn<QuotationTemplateFormatSetting_Bean,String> sheet_Column, itemRange_Column, sheetTitle_Column, sheetCell_Column;
    @FXML TableColumn<QuotationTemplateFormatSetting_Bean,Integer> sheetRow_Column, startIndex_Column, endIndex_Column;
    @FXML TableView<QuotationTemplateFormatSetting_Bean> tableView;

    private Stage Stage;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private JSONObject templateFormatSettingJsonObject;
    private ExportQuotationFormat exportQuotationFormat;

    public QuotationTemplateFormatSetting_Controller(){
        ToolKit toolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = toolKit.ComponentToolKit;
        this.KeyPressed = toolKit.KeyPressed;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }

    private IAECrawlerAccount_Bean IAECrawlerAccount_Bean;
    public void setComponent(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        this.IAECrawlerAccount_Bean = IAECrawlerAccount_Bean;
        if(IAECrawlerAccount_Bean.getTemplateFormatJsonObject() != null) {
            templateFormatSettingJsonObject = (JSONObject) IAECrawlerAccount_Bean.getTemplateFormatJsonObject().clone();
        }
        exportQuotationFormat = Order_Enum.ExportQuotationFormat.二聯;
        initialTableView();
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(sheet_Column, "sheet","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(itemRange_Column, "itemRange","CENTER-LEFT","16",null);

        setTitleColumnCellValueFactory(sheetTitle_Column, "sheetTitle","CENTER-LEFT","16");
        setRowColumnCellValueFactory(sheetRow_Column, "sheetRow","CENTER-LEFT","16");
        setCellColumnCellValueFactory(sheetCell_Column, "sheetCell","CENTER-LEFT","16");

        setStartIndexColumnCellValueFactory(startIndex_Column, "startIndex","CENTER-LEFT","16");
        setEndIndexColumnCellValueFactory(endIndex_Column, "endIndex","CENTER-LEFT","16");

        if(templateFormatSettingJsonObject == null){
            templateFormatSettingJsonObject = new JSONObject();
            for(ExportQuotationFormat exportQuotationFormat : ExportQuotationFormat.values()){
                JSONObject templateFormat = new JSONObject();
                for(ExportQuotationTemplateTitle exportQuotationTemplateTitle : ExportQuotationTemplateTitle.values()){
                    JSONObject templateTitle = new JSONObject();
                    if(exportQuotationTemplateTitle != ExportQuotationTemplateTitle.itemRange){
                        templateTitle.put("row","");
                        templateTitle.put("cell","");
                    }else{
                        templateTitle.put("startIndex",null);
                        templateTitle.put("endIndex",null);
                    }
                    templateFormat.put(exportQuotationTemplateTitle.name(), templateTitle);
                }
                templateFormatSettingJsonObject.put(exportQuotationFormat.name(), templateFormat);
            }
        }
        setTableViewData(exportQuotationFormat);
    }
    private void setTableViewData(ExportQuotationFormat exportQuotationFormat){
        ComponentToolKit.getQuotationTemplateFormatSettingTableViewItemList(tableView).clear();
        tableView.refresh();
        JSONObject templateFormat = templateFormatSettingJsonObject.getJSONObject(exportQuotationFormat.name());
        for(ExportQuotationTemplateTitle exportQuotationTemplateTitle : ExportQuotationTemplateTitle.values()){
            QuotationTemplateFormatSetting_Bean QuotationTemplateFormatSetting_Bean = new QuotationTemplateFormatSetting_Bean();
            QuotationTemplateFormatSetting_Bean.setExportQuotationTemplateTitle(exportQuotationTemplateTitle);
            if(templateFormat != null) {
                if(templateFormat.containsKey(exportQuotationTemplateTitle.name())){
                    if(exportQuotationTemplateTitle != ExportQuotationTemplateTitle.itemRange){
                        QuotationTemplateFormatSetting_Bean.setSheetRow(templateFormat.getJSONObject(exportQuotationTemplateTitle.name()).getInteger("row"));
                        QuotationTemplateFormatSetting_Bean.setSheetCell(templateFormat.getJSONObject(exportQuotationTemplateTitle.name()).getString("cell"));
                    }else{
                        QuotationTemplateFormatSetting_Bean.setSheetRow(null);
                        QuotationTemplateFormatSetting_Bean.setSheetCell("");
                        QuotationTemplateFormatSetting_Bean.setStartIndex(templateFormat.getJSONObject(exportQuotationTemplateTitle.name()).getInteger("startIndex"));
                        QuotationTemplateFormatSetting_Bean.setEndIndex(templateFormat.getJSONObject(exportQuotationTemplateTitle.name()).getInteger("endIndex"));
                    }
                }else{
                    QuotationTemplateFormatSetting_Bean.setSheetRow(null);
                    QuotationTemplateFormatSetting_Bean.setSheetCell("");

                    JSONObject templateTitle = new JSONObject();
                    if(exportQuotationTemplateTitle != ExportQuotationTemplateTitle.itemRange){
                        templateTitle.put("row","");
                        templateTitle.put("cell","");
                    }else{
                        templateTitle.put("startIndex",null);
                        templateTitle.put("endIndex",null);
                    }
                    templateFormat.put(exportQuotationTemplateTitle.name(), templateTitle);
                }
                tableView.getItems().add(QuotationTemplateFormatSetting_Bean);
            }
        }
    }
    @FXML protected void twoFormatMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            exportQuotationFormat = ExportQuotationFormat.二聯;
            ComponentToolKit.setButtonDisable(twoFormat_Button,true);
            ComponentToolKit.setButtonDisable(threeFormat_Button,false);
            ComponentToolKit.setButtonDisable(synchronizeTwoFormat_Button,true);

            setTableViewData(exportQuotationFormat);
        }
    }
    @FXML protected void threeFormatMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            exportQuotationFormat = ExportQuotationFormat.三聯;
            ComponentToolKit.setButtonDisable(twoFormat_Button,false);
            ComponentToolKit.setButtonDisable(threeFormat_Button,true);
            ComponentToolKit.setButtonDisable(synchronizeTwoFormat_Button,false);

            setTableViewData(exportQuotationFormat);
        }
    }
    @FXML protected void synchronizeTwoFormatMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            JSONObject twoFormat = templateFormatSettingJsonObject.getJSONObject(ExportQuotationFormat.二聯.name());
            JSONObject threeFormat = templateFormatSettingJsonObject.getJSONObject(ExportQuotationFormat.三聯.name());

            for (ExportQuotationTemplateTitle exportQuotationTemplateTitle : ExportQuotationTemplateTitle.values()){
                threeFormat.getJSONObject(exportQuotationTemplateTitle.name()).put("row",twoFormat.getJSONObject(exportQuotationTemplateTitle.name()).getString("row"));
                threeFormat.getJSONObject(exportQuotationTemplateTitle.name()).put("cell",twoFormat.getJSONObject(exportQuotationTemplateTitle.name()).getString("cell"));
                threeFormat.getJSONObject(exportQuotationTemplateTitle.name()).put("startIndex",twoFormat.getJSONObject(exportQuotationTemplateTitle.name()).getInteger("startIndex"));
                threeFormat.getJSONObject(exportQuotationTemplateTitle.name()).put("endIndex",twoFormat.getJSONObject(exportQuotationTemplateTitle.name()).getInteger("endIndex"));
            }
            setTableViewData(exportQuotationFormat);
        }
    }
    @FXML protected void saveMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(IAECrawlerAccount_Bean.getTemplateFormatJsonObject() != null)
                IAECrawlerAccount_Bean.getTemplateFormatJsonObject().clear();
            IAECrawlerAccount_Bean.setTemplateFormatJsonObject(templateFormatSettingJsonObject);
            DialogUI.MessageDialog("※ 儲存成功，請記得按【修改】");
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private void setTitleColumnCellValueFactory(TableColumn<QuotationTemplateFormatSetting_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setTitleNameColumnCellValueFactory(Alignment, FontSize));
    }
    private class setTitleNameColumnCellValueFactory extends TableCell<QuotationTemplateFormatSetting_Bean, String> {
        String Alignment, FontSize;
        setTitleNameColumnCellValueFactory(String Alignment, String FontSize){
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
                QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = getTableView().getItems().get(getIndex());
                if (quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.quotationNumber ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.quotationDate ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.customerName ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.contactPhone ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.taxIdNumber ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.faxIdNumber ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.projectName ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.projectCode ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.contactPerson ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.customerAddress ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.exportCount) {
                    setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;-fx-background-color:#F2F6BA");
                }else if (quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemRange ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemIndex ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.productName ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.quantity ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.unit ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_priceAmount ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.productRemark ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_priceAmount) {
                    setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;-fx-background-color:#F3BCD4");
                }else if (quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_tax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_totalPriceIncludeTax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_chineseFormat ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.remark ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_tax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.profit_includeTax ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.profit_noneTax) {
                    setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;-fx-background-color:#B3D7FF");
                }else if (quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.orderReference_customerID ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.orderReference_orderSource ||
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.orderReference_orderNumber) {
                    setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;-fx-background-color:#87F067");
                }
            }
        }
    }

    private void setRowColumnCellValueFactory(TableColumn<QuotationTemplateFormatSetting_Bean, Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setRowColumnCellValueFactory(Alignment, FontSize));
    }
    private class setRowColumnCellValueFactory extends TableCell<QuotationTemplateFormatSetting_Bean, Integer> {
        String Alignment, FontSize;
        TextField textField = new TextField();
        setRowColumnCellValueFactory(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
            ComponentToolKit.addTextFieldLimitDigital(textField,false);
            textField.setOnKeyPressed(KeyEvent -> {
                if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
                    commitEdit(textField.getText().equals("") ? null : Integer.parseInt(textField.getText()));
                }
            });
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if(getIndex() < 0 || getIndex() >= ExportQuotationTemplateTitle.values().length)
                return;
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = getTableView().getItems().get(getIndex());
            if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemRange ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemIndex ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.productName ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.quantity ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.unit ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_priceAmount ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.productRemark ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_priceAmount ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax) ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_tax) ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax) ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_tax) ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.orderReference_orderSource ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.orderReference_customerID ||
                    quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.orderReference_orderNumber) {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:red;");
                setText("--");
            }else {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;");
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (!isEmpty()) {
                QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
                if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.itemRange &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.itemIndex &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.productName &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.quantity &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.unit &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.singlePrice &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.singlePrice_priceAmount &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.productRemark &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.batchPrice &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.batchPrice_priceAmount &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax) &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_tax) &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax) &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_tax) &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.orderReference_orderSource &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.orderReference_customerID &&
                        quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.orderReference_orderNumber){
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    textField.setText(String.valueOf(quotationTemplateFormatSetting_Bean.getSheetRow()));
                    Platform.runLater(() -> textField.requestFocus());
                }else{
                    textField.setText("");
                }
            }
        }
        @Override
        public void commitEdit(Integer newValue) {
            super.commitEdit(newValue);
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
            quotationTemplateFormatSetting_Bean.setSheetRow(newValue);
            templateFormatSettingJsonObject.getJSONObject(exportQuotationFormat.name()).getJSONObject(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle().name()).put("row",newValue);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        @Override
        public void cancelEdit()
        {
            super.cancelEdit();
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    private void setCellColumnCellValueFactory(TableColumn<QuotationTemplateFormatSetting_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setCellColumnCellValueFactory(Alignment, FontSize));
    }
    private class setCellColumnCellValueFactory extends TableCell<QuotationTemplateFormatSetting_Bean, String> {
        String Alignment, FontSize;
        TextField textField = new TextField();
        setCellColumnCellValueFactory(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;

            ComponentToolKit.addTextFieldLimitEnglish(textField);
            ComponentToolKit.addKeyWordTextLimitLength(textField,1);

            textField.setOnKeyPressed(KeyEvent -> {
                if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
                    commitEdit(textField.getText().toUpperCase());
                }
            });
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(getIndex() < 0 || getIndex() >= ExportQuotationTemplateTitle.values().length)
                return;
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = getTableView().getItems().get(getIndex());
            if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemRange ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax) ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_tax) ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax) ||
                    (exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_tax)) {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:red;");
                setText("--");
            }else {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;");
                if (item == null || empty) {
                    setText("");
                } else {
                    setText(item);
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (!isEmpty()) {
                QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
                if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.itemRange &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax) &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.singlePrice_tax) &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax) &&
                        !(exportQuotationFormat == ExportQuotationFormat.二聯 && quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.batchPrice_tax)){
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    textField.setText(quotationTemplateFormatSetting_Bean.getSheetCell() == null ? "" : String.valueOf(quotationTemplateFormatSetting_Bean.getSheetCell()));
                    Platform.runLater(() -> textField.requestFocus());
                }
            }
        }
        @Override
        public void commitEdit(String newValue) {
            super.commitEdit(newValue);
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
            quotationTemplateFormatSetting_Bean.setSheetCell(newValue);
            templateFormatSettingJsonObject.getJSONObject(exportQuotationFormat.name()).getJSONObject(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle().name()).put("cell",newValue);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        @Override
        public void cancelEdit()
        {
            super.cancelEdit();
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    private void setStartIndexColumnCellValueFactory(TableColumn<QuotationTemplateFormatSetting_Bean, Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setStartIndexColumnCellValueFactory(Alignment, FontSize));
    }
    private class setStartIndexColumnCellValueFactory extends TableCell<QuotationTemplateFormatSetting_Bean, Integer> {
        String Alignment, FontSize;
        TextField textField = new TextField();
        setStartIndexColumnCellValueFactory(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;

            ComponentToolKit.addTextFieldLimitDigital(textField,false);

            textField.setOnKeyPressed(KeyEvent -> {
                if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
                    commitEdit(textField.getText().equals("") ? null : Integer.parseInt(textField.getText()));
                }
            });
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if(getIndex() < 0 || getIndex() >= ExportQuotationTemplateTitle.values().length)
                return;
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = getTableView().getItems().get(getIndex());
            if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.itemRange) {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:red;");
                setText("--");
            }else {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;");
                if (item == null || empty) {
                    setText("");
                } else {
                    setText(String.valueOf(item));
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (!isEmpty()) {
                QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
                if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemRange){
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    textField.setText(quotationTemplateFormatSetting_Bean.getStartIndex() == null ? "" : String.valueOf(quotationTemplateFormatSetting_Bean.getStartIndex()));
                    Platform.runLater(() -> textField.requestFocus());
                }
            }
        }
        @Override
        public void commitEdit(Integer newValue) {
            super.commitEdit(newValue);
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
            quotationTemplateFormatSetting_Bean.setStartIndex(newValue);
            templateFormatSettingJsonObject.getJSONObject(exportQuotationFormat.name()).getJSONObject(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle().name()).put("startIndex",newValue);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        @Override
        public void cancelEdit()
        {
            super.cancelEdit();
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }
    private void setEndIndexColumnCellValueFactory(TableColumn<QuotationTemplateFormatSetting_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setEndIndexColumnCellValueFactory(Alignment, FontSize));
    }
    private class setEndIndexColumnCellValueFactory extends TableCell<QuotationTemplateFormatSetting_Bean, Integer> {
        String Alignment, FontSize;
        TextField textField = new TextField();
        setEndIndexColumnCellValueFactory(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;

            ComponentToolKit.addTextFieldLimitDigital(textField,false);

            textField.setOnKeyPressed(KeyEvent -> {
                if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
                    commitEdit(textField.getText().equals("") ? null : Integer.parseInt(textField.getText()));
                }
            });
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if(getIndex() < 0 || getIndex() >= ExportQuotationTemplateTitle.values().length)
                return;
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = getTableView().getItems().get(getIndex());
            if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() != ExportQuotationTemplateTitle.itemRange) {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:red;");
                setText("--");
            }else {
                setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;-fx-text-fill:black;");
                if (item == null || empty) {
                    setText("");
                } else {
                    setText(String.valueOf(item));
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (!isEmpty()) {
                QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
                if(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle() == ExportQuotationTemplateTitle.itemRange){
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    textField.setText(quotationTemplateFormatSetting_Bean.getEndIndex() == null ? "" : String.valueOf(quotationTemplateFormatSetting_Bean.getEndIndex()));
                    Platform.runLater(() -> textField.requestFocus());
                }
            }
        }
        @Override
        public void commitEdit(Integer newValue) {
            super.commitEdit(newValue);
            QuotationTemplateFormatSetting_Bean quotationTemplateFormatSetting_Bean = ComponentToolKit.getQuotationTemplateFormatSettingTableViewSelectItem(tableView);
            quotationTemplateFormatSetting_Bean.setEndIndex(newValue);
            templateFormatSettingJsonObject.getJSONObject(exportQuotationFormat.name()).getJSONObject(quotationTemplateFormatSetting_Bean.getExportQuotationTemplateTitle().name()).put("endIndex",newValue);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        @Override
        public void cancelEdit()
        {
            super.cancelEdit();
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }
}