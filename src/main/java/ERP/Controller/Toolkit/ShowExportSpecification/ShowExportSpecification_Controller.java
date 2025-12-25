package ERP.Controller.Toolkit.ShowExportSpecification;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.SpecificationTemplate_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum.SpecificationColumn;
import ERP.Model.Order.OpenSpecificationWordDocument_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.HashMap;

public class ShowExportSpecification_Controller {
    @FXML RadioButton Equipment_RadioButton, BasicNeed_RadioButton, WarrantyAndOther_RadioButton, EducationTraining_RadioButton;
    @FXML ComboBox<SpecificationTemplate_Bean> BasicNeed_ComboBox, WarrantyAndOther_ComboBox, EducationTraining_ComboBox;
    @FXML TableColumn<OrderProduct_Bean,String> ItemNumberColumn, ProductNameColumn, QuantityColumn, UnitColumn, ExistSpecificationContentColumn;
    @FXML TableColumn<OrderProduct_Bean, ComboBox<String>> WannaExportProductNameColumn;
    @FXML TableView<OrderProduct_Bean> TableView;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;
    private Stage Stage;


    private String projectName;
    private ObservableList<OrderProduct_Bean> productList;
    private Order_Model Order_Model;
    private Product_Model Product_Model;
    private OpenSpecificationWordDocument_Model OpenSpecificationWordDocument_Model;
    public ShowExportSpecification_Controller(){
        this.ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        this.KeyPressed = ERPApplication.ToolKit.KeyPressed;
        this.CallFXML = ERPApplication.ToolKit.CallFXML;
        this.Order_Model = ERPApplication.ToolKit.ModelToolKit.getOrderModel();
        this.Product_Model = ERPApplication.ToolKit.ModelToolKit.getProductModel();
        OpenSpecificationWordDocument_Model = ERPApplication.ToolKit.ModelToolKit.getOpenSpecificationWordDocument_Model();
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    public void setProductList(ObservableList<OrderProduct_Bean> productList){
        this.productList = productList;
    }
    public void setComponent(){
        initialTableView();
        ComponentToolKit.getOrderProductTableViewItemList(TableView).addAll(productList);

        ComponentToolKit.setSpecificationTemplateComboBoxObj(BasicNeed_ComboBox);
        ComponentToolKit.setSpecificationTemplateComboBoxObj(WarrantyAndOther_ComboBox);
        ComponentToolKit.setSpecificationTemplateComboBoxObj(EducationTraining_ComboBox);
        ComponentToolKit.setComboBoxDisable(BasicNeed_ComboBox,!BasicNeed_RadioButton.isSelected());
        ComponentToolKit.setComboBoxDisable(WarrantyAndOther_ComboBox,!WarrantyAndOther_RadioButton.isSelected());
        ComponentToolKit.setComboBoxDisable(EducationTraining_ComboBox,!EducationTraining_RadioButton.isSelected());

        HashMap<SpecificationColumn,ObservableList<SpecificationTemplate_Bean>> specificationTemplateMap = Order_Model.getSpecificationTemplate();
        BasicNeed_ComboBox.getItems().addAll(specificationTemplateMap.get(SpecificationColumn.基本需求));
        WarrantyAndOther_ComboBox.getItems().addAll(specificationTemplateMap.get(SpecificationColumn.保固維護與其他));
        EducationTraining_ComboBox.getItems().addAll(specificationTemplateMap.get(SpecificationColumn.教育訓練));
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(ItemNumberColumn,"ItemNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuantityColumn,"Quantity", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit", "CENTER-LEFT", "16",null);
        setColumnCellValueAndExistSpecificationContent(ExistSpecificationContentColumn,"ExistSpecificationContent", "CENTER-LEFT", "16",null);
        setColumnCellValueAndComboBox(WannaExportProductNameColumn,"WannaExportProductName","CENTER-LEFT","16",null);
    }
    @FXML protected void BasicNeedOnAction(){
        if(!BasicNeed_RadioButton.isSelected())
            BasicNeed_RadioButton.setSelected(true);
    }
    @FXML protected void EquipmentOnAction(){
        if(!Equipment_RadioButton.isSelected())
            Equipment_RadioButton.setSelected(true);
    }
    @FXML protected void WarrantyAndOtherOnAction(){
        ComponentToolKit.setComboBoxDisable(WarrantyAndOther_ComboBox, !WarrantyAndOther_RadioButton.isSelected());
    }
    @FXML protected void EducationTrainingOnAction(){
        ComponentToolKit.setComboBoxDisable(EducationTraining_ComboBox, !EducationTraining_RadioButton.isSelected());
    }
    @FXML protected void basicNeedMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowModifySpecificationTemplate(SpecificationColumn.基本需求, Stage, BasicNeed_ComboBox);
        }
    }
    @FXML protected void warrantyAndOtherMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowModifySpecificationTemplate(SpecificationColumn.保固維護與其他, Stage, WarrantyAndOther_ComboBox);
        }
    }
    @FXML protected void educationTrainingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowModifySpecificationTemplate(SpecificationColumn.教育訓練, Stage, EducationTraining_ComboBox);
        }
    }
    @FXML protected void exportMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(noneSelectExportColumn())    DialogUI.MessageDialog("※ 請選擇匯出欄位");
            else if(noneSelectSpecificationProductName())    DialogUI.MessageDialog("※ 未選擇欲匯出商品名稱");
            else if(noneSelectBasicNeedTemplate())    DialogUI.MessageDialog("※ 請選擇「基本需求」範本");
            else if(noneSelectWarrantyAndOtherTemplate())    DialogUI.MessageDialog("※ 請選擇「保固維護與其他」範本");
            else if(noneSelectEducationTrainingTemplate())    DialogUI.MessageDialog("※ 請選擇「教育訓練」範本");
            else{
                if(OpenSpecificationWordDocument_Model.generateSpecificationDocument(
                        projectName,
                        BasicNeed_RadioButton.isSelected() ? ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(BasicNeed_ComboBox).getContent() : null,
                        Equipment_RadioButton.isSelected() ? productList : null,
                        WarrantyAndOther_RadioButton.isSelected() ? ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(WarrantyAndOther_ComboBox).getContent() : null,
                        EducationTraining_RadioButton.isSelected() ? ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(EducationTraining_ComboBox).getContent() : null
                )){
                    DialogUI.MessageDialog("※ 匯出成功");
                }else
                    DialogUI.MessageDialog("※ 匯出失敗");
            }
        }
    }
    private boolean noneSelectExportColumn(){
        return !Equipment_RadioButton.isSelected() && !BasicNeed_RadioButton.isSelected() && !WarrantyAndOther_RadioButton.isSelected();
    }
    private boolean noneSelectSpecificationProductName(){
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            if(Equipment_RadioButton.isSelected() && OrderProduct_Bean.getSpecificationProductName() == null){
                return true;
            }
        }
        return false;
    }
    private boolean noneSelectBasicNeedTemplate(){
        return BasicNeed_RadioButton.isSelected() && ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(BasicNeed_ComboBox) == null;
    }
    private boolean noneSelectWarrantyAndOtherTemplate(){
        return WarrantyAndOther_RadioButton.isSelected() && ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(WarrantyAndOther_ComboBox) == null;
    }
    private boolean noneSelectEducationTrainingTemplate(){
        return EducationTraining_RadioButton.isSelected() && ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(EducationTraining_ComboBox) == null;
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private void setColumnCellValueAndExistSpecificationContent(TableColumn<OrderProduct_Bean, String> TableColumn, String ColumnValue, String Alignment, String FontSize, String BackgroundColor){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize, BackgroundColor);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndExistSpecificationContent(Alignment, FontSize));
    }
    private class setColumnCellValueAndExistSpecificationContent extends TableCell<OrderProduct_Bean, String> {
        String Alignment, FontSize;
        setColumnCellValueAndExistSpecificationContent(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";");
            if(!empty){
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView, getIndex());
                String specificationContent = Product_Model.getSpecificationContent(OrderProduct_Bean.getISBN());
                OrderProduct_Bean.setSpecificationContent(specificationContent);
                if(specificationContent == null || specificationContent.equals("")) {
                    setText("X");
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill:RED;");
                }else {
                    setText("O");
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill:black;");
                }
            }
        }
    }
    private void setColumnCellValueAndComboBox(TableColumn<OrderProduct_Bean, ComboBox<String>> TableColumn, String ColumnValue, String Alignment, String FontSize, String BackgroundColor){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize, BackgroundColor);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, ComboBox<String>> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(ComboBox<String> ComboBox, boolean empty) {
            super.updateItem(ComboBox, empty);
            setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";");
            if(!empty){
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView, getIndex());
                OrderProduct_Bean.setSpecificationProductName(null);
                ObservableList<String> wannaSpecificationProductNameList = Product_Model.getSpecificationProductName(OrderProduct_Bean.getISBN());
                if(wannaSpecificationProductNameList.size() != 0){
                    if(ComboBox == null)    ComboBox = new ComboBox<>();
                    ComboBox<String> finalComboBox = ComboBox;
                    finalComboBox.setPrefSize(360, Control.USE_COMPUTED_SIZE);
                    finalComboBox.setItems(wannaSpecificationProductNameList);
                    setGraphic(finalComboBox);
                    finalComboBox.setOnAction(ActionEvent -> {
                        OrderProduct_Bean.setSpecificationProductName(ComponentToolKit.getComboBoxSelectItemStringFormat(finalComboBox));
                    });
                }else {
                    OrderProduct_Bean.setSpecificationProductName(OrderProduct_Bean.getProductName());
                    setText(OrderProduct_Bean.getProductName());
                }
            }else   setGraphic(null);
        }
    }
}
