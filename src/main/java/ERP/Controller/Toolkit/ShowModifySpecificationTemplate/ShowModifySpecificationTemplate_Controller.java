package ERP.Controller.Toolkit.ShowModifySpecificationTemplate;

import ERP.Bean.Order.SpecificationTemplate_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.SpecificationColumn;
import ERP.Enum.ToolKit.ToolKit_Enum.ConvertMathToChinese;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.stage.Stage;

public class ShowModifySpecificationTemplate_Controller {
    @FXML Label templateFormat_Label;
    @FXML TextArea specificationTemplate_TextArea;
    @FXML ComboBox<SpecificationTemplate_Bean> specificationTemplate_ComboBox;

    private final KeyCombination KeyCombine_Ctrl1 = new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN);
    private final KeyCombination KeyCombine_Ctrl2 = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN);
    private final KeyCombination KeyCombine_Ctrl3 = new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN);

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private Stage Stage;
    private SpecificationColumn SpecificationColumn;
    private ComboBox<SpecificationTemplate_Bean> exportTemplate_ComboBox;

    private Order_Model Order_Model;
    public ShowModifySpecificationTemplate_Controller(){
        this.ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        this.KeyPressed = ERPApplication.ToolKit.KeyPressed;

        this.Order_Model = ERPApplication.ToolKit.ModelToolKit.getOrderModel();
    }
    public void setSpecificationColumn(SpecificationColumn SpecificationColumn){
        this.SpecificationColumn = SpecificationColumn;
    }
    public void setExportTemplate_ComboBox(ComboBox<SpecificationTemplate_Bean> exportTemplate_ComboBox){
        this.exportTemplate_ComboBox = exportTemplate_ComboBox;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        if(SpecificationColumn == Order_Enum.SpecificationColumn.基本需求)
            templateFormat_Label.setText("一、OOO：須能支援多...\n二、廠商須配合協助...\n三、此系統介接校內...\n四、除上述功能等...");
        else if(SpecificationColumn == Order_Enum.SpecificationColumn.保固維護與其他)
            templateFormat_Label.setText("A.本案履約期限為決標次日起15個日曆天。\nB.本案為「OOO」採購皆原廠保固一年。\nC.廠商須提供上述系統之SDK。\nD.廠商須提供所有系統相關安裝、設定。");
        else if(SpecificationColumn == Order_Enum.SpecificationColumn.教育訓練)
            templateFormat_Label.setText("未提供範本");

        ComponentToolKit.setSpecificationTemplateComboBoxObj(specificationTemplate_ComboBox);
        specificationTemplate_ComboBox.getItems().addAll(exportTemplate_ComboBox.getItems());
        specificationTemplate_ComboBox.getSelectionModel().selectFirst();
        SpecificationTemplate_Bean SpecificationTemplate_Bean = ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(specificationTemplate_ComboBox);
        specificationTemplate_TextArea.setText(SpecificationTemplate_Bean == null ? "" : SpecificationTemplate_Bean.getContent());
    }
    @FXML protected void specificationTemplateOnAction(){
        SpecificationTemplate_Bean SpecificationTemplate_Bean = ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(specificationTemplate_ComboBox);
        if(SpecificationTemplate_Bean != null)
            specificationTemplate_TextArea.setText(SpecificationTemplate_Bean.getContent());
    }
    @FXML protected void modifyTemplateNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){

            SpecificationTemplate_Bean SpecificationTemplate_Bean = ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(specificationTemplate_ComboBox);
            if(SpecificationTemplate_Bean != null){
                String newTemplateName = DialogUI.TextInputDialog("修改範本名稱","請輸入新範本名稱",null);
                if(newTemplateName != null && !newTemplateName.equals("")){
                    if(Order_Model.modifySpecificationName(SpecificationTemplate_Bean,newTemplateName)){
                        DialogUI.MessageDialog("※ 修改成功");
                        SpecificationTemplate_Bean.setTemplateName(newTemplateName);
                        refreshExportTemplate_ComboBox(SpecificationTemplate_Bean);
                    }else
                        DialogUI.MessageDialog("※ 修改失敗");
                }
            }else
                DialogUI.MessageDialog("※ 請選擇範本");
        }
    }
    private void refreshExportTemplate_ComboBox(SpecificationTemplate_Bean modifySpecificationTemplate_Bean){
        ObservableList<SpecificationTemplate_Bean> templateList = ComponentToolKit.getSpecificationTemplateComboBoxItemList(exportTemplate_ComboBox);
        for(SpecificationTemplate_Bean SpecificationTemplate_Bean : templateList){
            if(SpecificationTemplate_Bean == modifySpecificationTemplate_Bean){
                SpecificationTemplate_Bean.setTemplateName(modifySpecificationTemplate_Bean.getTemplateName());
                SpecificationTemplate_Bean.setContent(modifySpecificationTemplate_Bean.getContent());
                break;
            }
        }
        ComponentToolKit.setSpecificationTemplateComboBoxObj(specificationTemplate_ComboBox);
        ComponentToolKit.setSpecificationTemplateComboBoxObj(exportTemplate_ComboBox);
    }

    @FXML protected void deleteTemplateNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.ConfirmDialog("※ 確定是否刪除 ?",true,false,0,0)){
                SpecificationTemplate_Bean SpecificationTemplate_Bean = ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(specificationTemplate_ComboBox);
                if(SpecificationTemplate_Bean != null){
                    if(Order_Model.deleteSpecificationTemplate(SpecificationTemplate_Bean)){
                        DialogUI.MessageDialog("※ 刪除成功");

                        specificationTemplate_ComboBox.getItems().remove(SpecificationTemplate_Bean);
                        specificationTemplate_ComboBox.getSelectionModel().selectFirst();
                        exportTemplate_ComboBox.getItems().remove(SpecificationTemplate_Bean);
                    }else
                        DialogUI.MessageDialog("※ 刪除失敗");
                }else
                    DialogUI.MessageDialog("※ 請選擇範本");
            }
        }
    }
    @FXML protected void insertTemplateMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String content = specificationTemplate_TextArea.getText();
            if(content == null || content.equals("")){
                DialogUI.MessageDialog("※ 請輸入範本內容");
            }else{
                String templateName = DialogUI.TextInputDialog("範本名稱","請輸入範本名稱",null);
                if (templateName != null && !templateName.equals("")) {
                    SpecificationTemplate_Bean SpecificationTemplate_Bean = Order_Model.insertSpecificationTemplate(templateName,content,SpecificationColumn);
                    if(SpecificationTemplate_Bean != null){
                        DialogUI.MessageDialog("※ 新增成功");
                        specificationTemplate_ComboBox.getItems().add(SpecificationTemplate_Bean);
                        specificationTemplate_ComboBox.getSelectionModel().select(SpecificationTemplate_Bean);

                        exportTemplate_ComboBox.getItems().add(SpecificationTemplate_Bean);
                        exportTemplate_ComboBox.getSelectionModel().select(SpecificationTemplate_Bean);
                    }else
                        DialogUI.MessageDialog("※ 新增失敗");
                }
            }
        }
    }
    @FXML protected void saveTemplateMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){

            String content = specificationTemplate_TextArea.getText();
            if(content == null || content.equals("")){
                DialogUI.MessageDialog("※ 請輸入範本內容");
            }else{
                SpecificationTemplate_Bean SpecificationTemplate_Bean = ComponentToolKit.getSpecificationTemplateComboBoxSelectItem(specificationTemplate_ComboBox);
                if(SpecificationTemplate_Bean != null){
                    SpecificationTemplate_Bean.setContent(content);
                    if(Order_Model.modifySpecificationTemplate(SpecificationTemplate_Bean, content)){
                        DialogUI.MessageDialog("※ 儲存成功");
                        SpecificationTemplate_Bean.setContent(content);
                        refreshExportTemplate_ComboBox(SpecificationTemplate_Bean);
                    }else
                        DialogUI.MessageDialog("※ 儲存失敗");
                }else
                    DialogUI.MessageDialog("※ 請選擇範本");
            }
        }
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            ComponentToolKit.closeThisStage(Stage);
    }

    int ctrl1Level = 0,ctrl2Level = 0;
    @FXML protected void templateTextAreaKeyPressed(KeyEvent KeyEvent){
        if(KeyCombine_Ctrl1.match(KeyEvent)){
            generateCtrl1Number();
        }else if(KeyCombine_Ctrl2.match(KeyEvent)){
            generateCtrl2Number();
        }else if(KeyCombine_Ctrl3.match(KeyEvent)){
            generateCtrl3Number();
        }
    }
    private void generateCtrl1Number() {
        String specificationText = specificationTemplate_TextArea.getText();
        ctrl1Level = ctrl1Level + 1;
        if (ctrl1Level > 9) {
            DialogUI.MessageDialog("※ 編號已達上限，請手動輸入");
            return;
        }
        String chineseMath = ConvertMathToChinese.values()[ctrl1Level].name();
        inputTitleNumber(specificationText,ctrl1Level,chineseMath + "、");
    }
    private void generateCtrl2Number(){
        String specificationText = specificationTemplate_TextArea.getText();
        ctrl2Level = ctrl2Level + 1;
        inputTitleNumber(specificationText,ctrl2Level,ctrl2Level + ".");
    }
    private void inputTitleNumber(String specificationText, int ctrlLevel, String titleNumber){
        if ((specificationText == null || specificationText.equals("")) && ctrlLevel == 1)
            specificationTemplate_TextArea.appendText(titleNumber);
        else if (specificationText != null) {
            int cursorPosition = specificationTemplate_TextArea.getCaretPosition();
            String afterCursorText = specificationText.substring(cursorPosition);
            if (!afterCursorText.equals("")) {
                if (cursorPosition == 0)
                    specificationTemplate_TextArea.setText(titleNumber + specificationText);
                else {
                    for (; cursorPosition > 0; cursorPosition--) {
                        String everyCharacter = specificationText.substring(cursorPosition - 1, cursorPosition);
                        if (everyCharacter.equals("\n")) {
                            specificationTemplate_TextArea.setText(specificationText.substring(0, cursorPosition) + titleNumber + specificationText.substring(cursorPosition));
                            specificationTemplate_TextArea.positionCaret(cursorPosition + 1);
                            break;
                        } else if (cursorPosition == 1) {
                            specificationTemplate_TextArea.setText(titleNumber + specificationText);
                            specificationTemplate_TextArea.positionCaret(cursorPosition + 1);
                        }
                    }
                }
            } else
                specificationTemplate_TextArea.appendText("\n" + titleNumber);
        }
    }
    private void generateCtrl3Number(){
        String specificationText = specificationTemplate_TextArea.getText();
        int cursorPosition = specificationTemplate_TextArea.getCaretPosition();
        String afterCursorText = specificationText.substring(cursorPosition);
        if(!afterCursorText.equals("")) {
            specificationTemplate_TextArea.setText(specificationText.substring(0,cursorPosition) + "●" + afterCursorText);
            specificationTemplate_TextArea.positionCaret(cursorPosition+1);
        }else{
            specificationTemplate_TextArea.appendText("\n●");
        }
    }
}
