package ERP.Controller.Toolkit.ShowProductOnShelfSetting;

import ERP.Bean.Product.ManageProductOnShelf_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum.BlackCatShippingFee;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Model.Product.Product_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ShowProductOnShelfSetting_Controller {
    @FXML HBox blackCatSwitchButton_HBox, sevenElevenSwitchButton_HBox, familySwitchButton_HBox, hiLifeSwitchButton_HBox;
    @FXML HBox blackCatShippingFee_HBox, sevenElevenShippingFee_HBox, familyShippingFee_HBox, hiLifeShippingFee_HBox;
    @FXML RadioButton nonePrepareOrder_RadioButton, prepareOrder_RadioButton;
    @FXML CheckBox blackCatShippingFee_CheckBox, sevenElevenShippingFee_CheckBox, familyShippingFee_CheckBox, hiLifeShippingFee_CheckBox;
    @FXML ComboBox<BlackCatShippingFee> blackCatShippingFee_ComboBox;
    @FXML TextField packageLength_TextFiled, packageWidth_TextFiled, packageHeight_TextFiled, packageWeight_TextFiled,
            dayOfPrepare_TextField, defaultShippingFee_TextField,
            blackCatShippingFee_TextField, sevenElevenShippingFee_TextField, familyShippingFee_TextField, hiLifeShippingFee_TextField;

    private ToolKit ToolKit;
    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private Product_Model Product_Model;
    private SystemSetting_Model SystemSetting_Model;
    private ManageProductOnShelf_Bean ManageProductOnShelf_Bean;
    private Stage Stage;

    private ToggleSwitch blackCatSwitchButton, sevenElevenSwitchButton, familySwitchButton, hiLifeSwitchButton;
    public ShowProductOnShelfSetting_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.ComponentToolKit = ToolKit.ComponentToolKit;

        this.Product_Model = ToolKit.ModelToolKit.getProductModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setManageProductOnShelf_Bean(ManageProductOnShelf_Bean ManageProductOnShelf_Bean){
        this.ManageProductOnShelf_Bean = ManageProductOnShelf_Bean;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        setTextFieldLimitDigital();
        packageLength_TextFiled.setText(String.valueOf(ManageProductOnShelf_Bean.getPackageLength()));
        packageWidth_TextFiled.setText(String.valueOf(ManageProductOnShelf_Bean.getPackageWidth()));
        packageHeight_TextFiled.setText(String.valueOf(ManageProductOnShelf_Bean.getPackageHeight()));
        packageWeight_TextFiled.setText(String.valueOf(ManageProductOnShelf_Bean.getPackageWeight()));

        ComponentToolKit.setRadioButtonSelect(nonePrepareOrder_RadioButton, !ManageProductOnShelf_Bean.isPrepareOrder());
        ComponentToolKit.setRadioButtonSelect(prepareOrder_RadioButton, ManageProductOnShelf_Bean.isPrepareOrder());
        ComponentToolKit.setTextFieldDisable(dayOfPrepare_TextField, !ManageProductOnShelf_Bean.isPrepareOrder());
        dayOfPrepare_TextField.setText(String.valueOf(ManageProductOnShelf_Bean.getDayOfPrepare()));

        getDefaultShippingFee();

        ComponentToolKit.setCheckBoxSelect(blackCatShippingFee_CheckBox,ManageProductOnShelf_Bean.isBlackCatOwnExpense());
        ComponentToolKit.setBlackCatShippingFeeComboBoxObj(blackCatShippingFee_ComboBox);
        blackCatShippingFee_ComboBox.getItems().addAll(BlackCatShippingFee.values());
        if(ManageProductOnShelf_Bean.getBlackCatShippingFee() != null){
            for(BlackCatShippingFee BlackCatShippingFee : BlackCatShippingFee.values()){
                if(BlackCatShippingFee.getShippingFee() == ManageProductOnShelf_Bean.getBlackCatShippingFee()) {
                    blackCatShippingFee_ComboBox.getSelectionModel().select(BlackCatShippingFee);
                    blackCatShippingFee_TextField.setText(String.valueOf(BlackCatShippingFee.getShippingFee()));
                    break;
                }
            }
        }
        ComponentToolKit.setCheckBoxSelect(sevenElevenShippingFee_CheckBox,ManageProductOnShelf_Bean.isSevenElvenOwnExpense());
        ComponentToolKit.setTextFieldDisable(sevenElevenShippingFee_TextField,ManageProductOnShelf_Bean.isSevenElvenOwnExpense());
        sevenElevenShippingFee_TextField.setText(String.valueOf(ManageProductOnShelf_Bean.getSevenElevenShippingFee()));

        ComponentToolKit.setCheckBoxSelect(familyShippingFee_CheckBox,ManageProductOnShelf_Bean.isFamilyOwnExpense());
        ComponentToolKit.setTextFieldDisable(familyShippingFee_TextField,ManageProductOnShelf_Bean.isFamilyOwnExpense());
        familyShippingFee_TextField.setText(String.valueOf(ManageProductOnShelf_Bean.getFamilyShippingFee()));

        ComponentToolKit.setCheckBoxSelect(hiLifeShippingFee_CheckBox,ManageProductOnShelf_Bean.isHiLifeOwnExpense());
        ComponentToolKit.setTextFieldDisable(hiLifeShippingFee_TextField,ManageProductOnShelf_Bean.isHiLifeOwnExpense());
        hiLifeShippingFee_TextField.setText(String.valueOf(ManageProductOnShelf_Bean.getHiLifeShippingFee()));

        blackCatSwitchButton = createSwitchButton(blackCatSwitchButton_HBox, blackCatShippingFee_HBox, ManageProductOnShelf_Bean.isAllowBlackCat());
        sevenElevenSwitchButton = createSwitchButton(sevenElevenSwitchButton_HBox,sevenElevenShippingFee_HBox, ManageProductOnShelf_Bean.isAllowSevenEleven());
        familySwitchButton = createSwitchButton(familySwitchButton_HBox, familyShippingFee_HBox, ManageProductOnShelf_Bean.isAllowFamily());
        hiLifeSwitchButton = createSwitchButton(hiLifeSwitchButton_HBox, hiLifeShippingFee_HBox ,ManageProductOnShelf_Bean.isAllowHiLife());
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(packageLength_TextFiled, false);
        ComponentToolKit.addTextFieldLimitDigital(packageWidth_TextFiled, false);
        ComponentToolKit.addTextFieldLimitDigital(packageHeight_TextFiled, false);
        ComponentToolKit.addTextFieldLimitDouble(packageWeight_TextFiled);
        ComponentToolKit.addTextFieldLimitDigital(dayOfPrepare_TextField, false);
        ComponentToolKit.addTextFieldLimitDigital(defaultShippingFee_TextField, false);

        ComponentToolKit.addTextFieldLimitDigital(sevenElevenShippingFee_TextField, false);
        ComponentToolKit.addTextFieldLimitDigital(familyShippingFee_TextField, false);
        ComponentToolKit.addTextFieldLimitDigital(hiLifeShippingFee_TextField, false);
    }
    private ToggleSwitch createSwitchButton(HBox switchButton_HBox, HBox shippingFee_HBox, boolean defaultSwitched){
        ToggleSwitch toggle = new ToggleSwitch(shippingFee_HBox, defaultSwitched, ComponentToolKit);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        switchButton_HBox.getChildren().add(toggle);
        return toggle;
    }
    @FXML protected void packageLengthKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            packageWidth_TextFiled.requestFocus();
    }
    @FXML protected void packageWidthKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            packageHeight_TextFiled.requestFocus();
    }
    @FXML protected void packageHeightKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            packageWeight_TextFiled.requestFocus();
    }
    @FXML protected void packageWeightKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            dayOfPrepare_TextField.requestFocus();
    }
    @FXML protected void dayOfPrepareKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            defaultShippingFee_TextField.requestFocus();
    }
    @FXML protected void defaultShippingFeeKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            sevenElevenShippingFee_TextField.requestFocus();
    }
    @FXML protected void blackCatShippingFeeOnAction(){
        BlackCatShippingFee BlackCatShippingFee = ComponentToolKit.getBlackCatShippingFeeComboBoxSelectItem(blackCatShippingFee_ComboBox);
        if(BlackCatShippingFee != null){
            ComponentToolKit.setCheckBoxSelect(blackCatShippingFee_CheckBox,false);
            blackCatShippingFee_TextField.setText(String.valueOf(BlackCatShippingFee.getShippingFee()));
        }else
            blackCatShippingFee_TextField.setText("");
    }
    @FXML protected void sevenElevenShippingFeeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(sevenElevenShippingFee_CheckBox.isSelected())
                ComponentToolKit.setTextFieldDisable(sevenElevenShippingFee_TextField,true);
            else
                ComponentToolKit.setTextFieldDisable(sevenElevenShippingFee_TextField,false);
        }
    }
    @FXML protected void sevenElevenShippingFeeKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            familyShippingFee_TextField.requestFocus();
    }
    @FXML protected void familyShippingFeeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(familyShippingFee_CheckBox.isSelected())
                ComponentToolKit.setTextFieldDisable(familyShippingFee_TextField,true);
            else
                ComponentToolKit.setTextFieldDisable(familyShippingFee_TextField,false);
        }
    }
    @FXML protected void familyShippingFeeKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            hiLifeShippingFee_TextField.requestFocus();
    }
    @FXML protected void hiLifeShippingFeeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(hiLifeShippingFee_CheckBox.isSelected())
                ComponentToolKit.setTextFieldDisable(hiLifeShippingFee_TextField,true);
            else
                ComponentToolKit.setTextFieldDisable(hiLifeShippingFee_TextField,false);
        }
    }
    @FXML protected void hiLifeShippingFeeKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent))
            packageLength_TextFiled.requestFocus();
    }
    @FXML protected void nonePrepareOrderOnAction(){
        if(nonePrepareOrder_RadioButton.isSelected()) {
            ComponentToolKit.setTextFieldDisable(dayOfPrepare_TextField,true);
        }
    }
    @FXML protected void prepareOrderOnAction(){
        if(prepareOrder_RadioButton.isSelected())
            ComponentToolKit.setTextFieldDisable(dayOfPrepare_TextField, false);
    }
    @FXML protected void saveMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(prepareOrder_RadioButton.isSelected()) {
                String dayOfPrepare = dayOfPrepare_TextField.getText();
                if(dayOfPrepare == null || dayOfPrepare.equals("")) {
                    DialogUI.AlarmDialog("※ 請輸入預購天數!");
                    return;
                }else if(Integer.parseInt(dayOfPrepare) < 3) {
                    DialogUI.AlarmDialog("※ 預購天數至少3天!");
                    return;
                }
            }
            getDefaultShippingFee();
            String defaultShippingFee = defaultShippingFee_TextField.getText();
            if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.預設運費, defaultShippingFee)){
                recordProductOnShelfSetting(ManageProductOnShelf_Bean);
                if(Product_Model.updateWaitingOnShelfAndShippingFee(ManageProductOnShelf_Bean)) {
                    DialogUI.MessageDialog("※ 儲存成功");
                    closeMouseClicked();
                }else {
                    initialProductOnShelfSetting(ManageProductOnShelf_Bean);
                    DialogUI.MessageDialog("※ 儲存失敗");
                }
                DialogUI.MessageDialog("※ 儲存成功");
            }else
                DialogUI.MessageDialog("※ 「預設運費」儲存失敗");
        }
    }
    private void getDefaultShippingFee(){
        String defaultShippingFee;
        if(defaultShippingFee_TextField.getText() == null || defaultShippingFee_TextField.getText().equals("")){
            defaultShippingFee = SystemSetting_Model.getSpecificSystemSettingData(SystemSettingConfig.預設運費);
            if(defaultShippingFee == null || defaultShippingFee.equals(""))
                defaultShippingFee = "60";
            defaultShippingFee_TextField.setText(defaultShippingFee);
        }
    }
    private void recordProductOnShelfSetting(ManageProductOnShelf_Bean ManageProductOnShelf_Bean){
        if(packageLength_TextFiled.getText() == null || packageLength_TextFiled.getText().equals(""))   ManageProductOnShelf_Bean.setPackageLength(null);
        else    ManageProductOnShelf_Bean.setPackageLength(Integer.parseInt(packageLength_TextFiled.getText()));

        if(packageWidth_TextFiled.getText() == null || packageWidth_TextFiled.getText().equals("")) ManageProductOnShelf_Bean.setPackageWidth(null);
        else    ManageProductOnShelf_Bean.setPackageWidth(Integer.parseInt(packageWidth_TextFiled.getText()));

        if(packageHeight_TextFiled.getText() == null || packageHeight_TextFiled.getText().equals(""))   ManageProductOnShelf_Bean.setPackageHeight(null);
        else    ManageProductOnShelf_Bean.setPackageHeight(Integer.parseInt(packageHeight_TextFiled.getText()));

        if(packageWeight_TextFiled.getText() == null || packageWeight_TextFiled.getText().equals(""))   ManageProductOnShelf_Bean.setPackageWeight(0);
        else    ManageProductOnShelf_Bean.setPackageWeight(ToolKit.RoundingDouble(Double.parseDouble(packageWeight_TextFiled.getText())));

        ManageProductOnShelf_Bean.setPrepareOrder(prepareOrder_RadioButton.isSelected());
        String dayOfPrepare = dayOfPrepare_TextField.getText();
        if(dayOfPrepare == null || dayOfPrepare.equals("")) ManageProductOnShelf_Bean.setDayOfPrepare(0);
        else    ManageProductOnShelf_Bean.setDayOfPrepare(Integer.parseInt(dayOfPrepare));

        ManageProductOnShelf_Bean.setAllowBlackCat(blackCatSwitchButton.switchedOnProperty().get());
        ManageProductOnShelf_Bean.setBlackCatOwnExpense(blackCatShippingFee_CheckBox.isSelected());
        BlackCatShippingFee BlackCatShippingFee = ComponentToolKit.getBlackCatShippingFeeComboBoxSelectItem(blackCatShippingFee_ComboBox);
        if(BlackCatShippingFee != null)
            ManageProductOnShelf_Bean.setBlackCatShippingFee(BlackCatShippingFee.getShippingFee());
        else
            ManageProductOnShelf_Bean.setBlackCatShippingFee(null);

        ManageProductOnShelf_Bean.setAllowSevenEleven(sevenElevenSwitchButton.switchedOnProperty().get());
        ManageProductOnShelf_Bean.setSevenElvenOwnExpense(sevenElevenShippingFee_CheckBox.isSelected());
        String sevenElevenShippingFee = sevenElevenShippingFee_TextField.getText();
        if(sevenElevenShippingFee != null && !sevenElevenShippingFee.equals(""))
            ManageProductOnShelf_Bean.setSevenElevenShippingFee(Integer.parseInt(sevenElevenShippingFee));
        else
            ManageProductOnShelf_Bean.setSevenElevenShippingFee(Integer.parseInt(defaultShippingFee_TextField.getText()));

        ManageProductOnShelf_Bean.setAllowFamily(familySwitchButton.switchedOnProperty().get());
        ManageProductOnShelf_Bean.setFamilyOwnExpense(familyShippingFee_CheckBox.isSelected());
        String familyShippingFee = familyShippingFee_TextField.getText();
        if(familyShippingFee != null && !familyShippingFee.equals(""))
            ManageProductOnShelf_Bean.setFamilyShippingFee(Integer.parseInt(familyShippingFee));
        else
            ManageProductOnShelf_Bean.setFamilyShippingFee(Integer.parseInt(defaultShippingFee_TextField.getText()));

        ManageProductOnShelf_Bean.setAllowHiLife(hiLifeSwitchButton.switchedOnProperty().get());
        ManageProductOnShelf_Bean.setHiLifeOwnExpense(hiLifeShippingFee_CheckBox.isSelected());
        String hiLifeShippingFee = hiLifeShippingFee_TextField.getText();
        if(hiLifeShippingFee != null && !hiLifeShippingFee.equals(""))
            ManageProductOnShelf_Bean.setHiLifeShippingFee(Integer.parseInt(hiLifeShippingFee));
        else
            ManageProductOnShelf_Bean.setHiLifeShippingFee(Integer.parseInt(defaultShippingFee_TextField.getText()));
    }
    private void initialProductOnShelfSetting(ManageProductOnShelf_Bean ManageProductOnShelf_Bean){
        ManageProductOnShelf_Bean.setPackageLength(null);
        ManageProductOnShelf_Bean.setPackageWidth(null);
        ManageProductOnShelf_Bean.setPackageHeight(null);
        ManageProductOnShelf_Bean.setPackageWeight(0);
        ManageProductOnShelf_Bean.setPrepareOrder(false);
        ManageProductOnShelf_Bean.setDayOfPrepare(0);

        ManageProductOnShelf_Bean.setBlackCatOwnExpense(false);
        ManageProductOnShelf_Bean.setBlackCatShippingFee(null);

        ManageProductOnShelf_Bean.setSevenElvenOwnExpense(false);
        ManageProductOnShelf_Bean.setSevenElevenShippingFee(null);

        ManageProductOnShelf_Bean.setFamilyOwnExpense(false);
        ManageProductOnShelf_Bean.setFamilyShippingFee(null);

        ManageProductOnShelf_Bean.setHiLifeOwnExpense(false);
        ManageProductOnShelf_Bean.setHiLifeShippingFee(null);

        ManageProductOnShelf_Bean.setAllowBlackCat(true);
        ManageProductOnShelf_Bean.setAllowSevenEleven(true);
        ManageProductOnShelf_Bean.setAllowFamily(true);
        ManageProductOnShelf_Bean.setAllowHiLife(true);
    }
    @FXML protected void closeMouseClicked(){
        ComponentToolKit.closeThisStage(Stage);
    }
}
class ToggleSwitch extends Parent {
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    private HBox shippingFee_HBox;
    private ComponentToolKit ComponentToolKit;
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public ToggleSwitch(HBox shippingFee_HBox, boolean defaultSwitched, ComponentToolKit ComponentToolKit){
        this.shippingFee_HBox = shippingFee_HBox;
        this.ComponentToolKit = ComponentToolKit;
        Rectangle background = new Rectangle(60, 20);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(10);
        trigger.setCenterX(10);
        trigger.setCenterY(10);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);


        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            setSwitchedOnStatus(isOn);
        });
        setOnMouseClicked(MouseEvent -> switchedOn.set(!switchedOn.get()));
        switchedOn.set(defaultSwitched);
        if(!defaultSwitched) {
            setSwitchedOnStatus(false);
        }
    }
    private void setSwitchedOnStatus(boolean isOn){
        translateAnimation.setToX(isOn ? 100 - 60 : 0);
        fillAnimation.setFromValue(isOn ? Color.RED : Color.LIGHTGREEN);
        fillAnimation.setToValue(isOn ? Color.LIGHTGREEN : Color.RED);
        animation.play();
        ComponentToolKit.setHBoxDisable(shippingFee_HBox, !isOn);
    }
}
