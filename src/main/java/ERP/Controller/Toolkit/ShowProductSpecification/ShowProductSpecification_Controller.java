package ERP.Controller.Toolkit.ShowProductSpecification;

import ERP.ERPApplication;
import ERP.Model.Product.Product_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import ERP.Enum.ToolKit.ToolKit_Enum.ConvertMathToUpperEnglish;
import ERP.Enum.ToolKit.ToolKit_Enum.ConvertMathToLowerEnglish;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;

public class ShowProductSpecification_Controller{
    @FXML TextArea textSpecificationTextArea;
    @FXML ComboBox<String> productNameComboBox;
    @FXML TextField productNameTextField, firstLevelText, secondLevelText, thirdLevelText;

    private final KeyCombination KeyCombine_Ctrl1 = new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN);
    private final KeyCombination KeyCombine_Ctrl2 = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN);
    private final KeyCombination KeyCombine_Ctrl3 = new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN);
    private final KeyCombination KeyCombine_Ctrl4 = new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_DOWN);

    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private Product_Model Product_Model;
    private String ISBN;
    private Stage Stage;
    public ShowProductSpecification_Controller(){
        ToolKit ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.Product_Model = ToolKit.ModelToolKit.getProductModel();
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setProductISBN(String ISBN){
        this.ISBN = ISBN;
    }
    public void setComponent(){
        setKeyWordTextLimiter();
        setTextFieldLimitDigital();
        textSpecificationTextArea.setText(Product_Model.getSpecificationContent(ISBN));
        productNameComboBox.getItems().addAll(Product_Model.getSpecificationProductName(ISBN));
        productNameComboBox.getSelectionModel().selectFirst();

        firstLevelText.setText("1");
        secondLevelText.setText("A");
        thirdLevelText.setText("a");
    }
    private void setKeyWordTextLimiter(){
        ComponentToolKit.addKeyWordTextLimitLength(firstLevelText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(secondLevelText, 1);
        ComponentToolKit.addKeyWordTextLimitLength(thirdLevelText,1);
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(firstLevelText, false);
        ComponentToolKit.addTextFieldLimitUpperCaseEnglish(secondLevelText);
        ComponentToolKit.addTextFieldLimitLowerCaseEnglish(thirdLevelText);
    }
    @FXML protected void insertProductNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String wannaProductName = productNameTextField.getText();
            if(wannaProductName.equals(""))
                DialogUI.MessageDialog("※ 請輸入商品名稱");
            else if(isExistProductName(wannaProductName))
                DialogUI.MessageDialog("※ 已存在商品名稱");
            else{
                if(Product_Model.insertSpecificationProductName(ISBN, wannaProductName)) {
                    DialogUI.MessageDialog("※ 新增成功");
                    ComponentToolKit.getComboBoxItemsStringFormat(productNameComboBox).add(wannaProductName);
                    productNameTextField.setText("");
                }else
                    DialogUI.MessageDialog("※ 新增失敗");
            }
        }
    }
    private boolean isExistProductName(String wannaProductName){
        boolean isExist = false;
        ObservableList<String> productNameList = ComponentToolKit.getComboBoxItemsStringFormat(productNameComboBox);
        for(String originProductName : productNameList){
            if(originProductName.equals(wannaProductName)){
                isExist = true;
                break;
            }
        }
        return isExist;
    }
    @FXML protected void modifyProductNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String selectProductName = ComponentToolKit.getComboBoxSelectItemStringFormat(productNameComboBox);
            String wannaChangeProductName = productNameTextField.getText();
            if(wannaChangeProductName.equals(""))
                DialogUI.MessageDialog("※ 請輸入商品名稱");
            else if(selectProductName == null)
                DialogUI.MessageDialog("※ 請選擇欲修改的商品名稱");
            else if(DialogUI.ConfirmDialog("確定將「" + selectProductName + "」修改成「" + wannaChangeProductName + "」 ?",true,false,0,0)){
                if(Product_Model.modifySpecificationProductName(ISBN, selectProductName, wannaChangeProductName)){
                    DialogUI.MessageDialog("※ 修改成功");
                    int selectIndex = ComponentToolKit.getComboBoxSelectItemIndex(productNameComboBox);
                    ComponentToolKit.getComboBoxItemsStringFormat(productNameComboBox).remove(selectProductName);
                    ComponentToolKit.getComboBoxItemsStringFormat(productNameComboBox).add(selectIndex,wannaChangeProductName);
                    productNameComboBox.getSelectionModel().select(wannaChangeProductName);
                    productNameTextField.setText("");
                }else
                    DialogUI.MessageDialog("※ 修改失敗");
            }
        }
    }
    @FXML protected void deleteProductNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String selectProductName = ComponentToolKit.getComboBoxSelectItemStringFormat(productNameComboBox);
            if(selectProductName == null)
                DialogUI.MessageDialog("※ 請選擇欲刪除的商品名稱");
            else if(DialogUI.ConfirmDialog("確定刪除「" + selectProductName + "」 ?",true,false,0,0)){
                if(Product_Model.deleteSpecificationProductName(ISBN, selectProductName)){
                    DialogUI.MessageDialog("※ 刪除成功");
                    ComponentToolKit.getComboBoxItemsStringFormat(productNameComboBox).remove(selectProductName);
                    productNameTextField.setText("");
                }else
                    DialogUI.MessageDialog("※ 刪除失敗");
            }
        }
    }
    @FXML protected void saveSpecificationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String specificationContent = textSpecificationTextArea.getText();
            if(Product_Model.saveSpecificationContent(ISBN, specificationContent == null ? "" : specificationContent))
                DialogUI.MessageDialog("※ 儲存成功");
            else
                DialogUI.MessageDialog("※ 儲存失敗");
        }
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    int firstLevel = 0;
    int secondLevel = 0;
    int thirdLevel = 0;
    @FXML protected void textSpecificationKeyPressed(KeyEvent KeyEvent){
        if(KeyCombine_Ctrl1.match(KeyEvent)){
            firstLevel = firstLevel + 1;
            if(firstLevel >= 100)
                DialogUI.MessageDialog("※ 「1、2...」編號已達上限");
            else{
                if(setTitleNumber(firstLevel, firstLevel)) {
                    if(firstLevel < 99)
                        firstLevelText.setText(String.valueOf((firstLevel + 1)));
                }
            }
        }else if(KeyCombine_Ctrl2.match(KeyEvent)){
            if(secondLevel >= 26)
                DialogUI.MessageDialog("※ 「A、B...」編號已達上限");
            else {
                if(setTitleNumber(secondLevel, ConvertMathToUpperEnglish.values()[secondLevel])) {
                    if(secondLevel < 25)
                        secondLevelText.setText(ConvertMathToUpperEnglish.values()[secondLevel + 1].name());
                    secondLevel = secondLevel + 1;
                }
            }
        }else if(KeyCombine_Ctrl3.match(KeyEvent)){
            if(thirdLevel >= 26)
                DialogUI.MessageDialog("※ 「a、b...」編號已達上限");
            else {
                if(setTitleNumber(thirdLevel, ConvertMathToLowerEnglish.values()[thirdLevel])) {
                    if(thirdLevel < 25)
                        thirdLevelText.setText(ConvertMathToLowerEnglish.values()[thirdLevel + 1].name());
                    thirdLevel = thirdLevel + 1;
                }
            }
        }else if(KeyCombine_Ctrl4.match(KeyEvent)) {
            String specificationText = textSpecificationTextArea.getText();
            int cursorPosition = textSpecificationTextArea.getCaretPosition();
            String afterCursorText = specificationText.substring(cursorPosition);
            if(!afterCursorText.equals("")) {
                textSpecificationTextArea.setText(specificationText.substring(0,cursorPosition) + "●" + afterCursorText);
                textSpecificationTextArea.positionCaret(cursorPosition+1);
            }else{
                textSpecificationTextArea.appendText("\n●");
            }
        }
    }
    private boolean setTitleNumber(int level,Object titleNumber){
        String specificationText = textSpecificationTextArea.getText();
        if((specificationText == null || specificationText.equals("")) && level == 1) {
            textSpecificationTextArea.appendText(titleNumber + ".");
            return true;
        }else if(specificationText != null){
            int cursorPosition = textSpecificationTextArea.getCaretPosition();
            String afterCursorText = specificationText.substring(cursorPosition);
            if(!afterCursorText.equals("")){
                if(cursorPosition == 0) {
                    textSpecificationTextArea.setText(titleNumber + "." + specificationText);
                    return true;
                }else{
                    for(; cursorPosition > 0 ; cursorPosition--){
                        String everyCharacter = specificationText.substring(cursorPosition-1,cursorPosition);
                        if(everyCharacter.equals("\n")){
                            textSpecificationTextArea.setText(specificationText.substring(0,cursorPosition) + titleNumber + "." + specificationText.substring(cursorPosition));
                            textSpecificationTextArea.positionCaret(cursorPosition+1);
                            return true;
                        }else if(cursorPosition == 1){
                            textSpecificationTextArea.setText(titleNumber + "." + specificationText);
                            textSpecificationTextArea.positionCaret(cursorPosition+1);
                            return true;
                        }
                    }
                }
            }else {
                textSpecificationTextArea.appendText("\n" + titleNumber + ".");
                return true;
            }
        }
        return false;
    }
    @FXML protected void firstLevelSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String firstLevelText = this.firstLevelText.getText();
            if(firstLevelText == null || firstLevelText.equals(""))
                DialogUI.MessageDialog("※ 請輸入數字");
            else {
                this.firstLevel = Integer.parseInt(firstLevelText)-1;
                DialogUI.MessageDialog("※ 設定成功【編號1】");
            }
        }
    }
    @FXML protected void firstLevelInitialMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.firstLevel = 0;
            firstLevelText.setText("1");
            DialogUI.MessageDialog("※ 已初始化【編號1】");
        }
    }
    @FXML protected void secondLevelSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String secondLevelText = this.secondLevelText.getText();
            if(secondLevelText == null || secondLevelText.equals(""))
                DialogUI.MessageDialog("※ 請輸入大寫英文");
            else {
                this.secondLevel = ConvertMathToUpperEnglish.valueOf(secondLevelText).ordinal();
                DialogUI.MessageDialog("※ 設定成功【編號2】");
            }
        }
    }
    @FXML protected void secondLevelInitialMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.secondLevel = 0;
            secondLevelText.setText("A");
            DialogUI.MessageDialog("※ 已初始化【編號2】");
        }
    }

    @FXML protected void thirdLevelSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String thirdLevelText = this.thirdLevelText.getText();
            if(thirdLevelText == null || thirdLevelText.equals(""))
                DialogUI.MessageDialog("※ 請輸入大寫英文");
            else {
                this.thirdLevel = ConvertMathToLowerEnglish.valueOf(thirdLevelText).ordinal();
                DialogUI.MessageDialog("※ 設定成功【編號3】");
            }
        }
    }
    @FXML protected void thirdLevelInitialMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.thirdLevel = 0;
            thirdLevelText.setText("a");
            DialogUI.MessageDialog("※ 已初始化【編號3】");
        }
    }
}