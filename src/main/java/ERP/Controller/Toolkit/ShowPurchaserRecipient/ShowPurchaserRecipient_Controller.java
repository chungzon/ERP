package ERP.Controller.Toolkit.ShowPurchaserRecipient;

import ERP.Bean.Order.Order_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderExist;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.ToolKit.TemplatePath;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logistic.document.excel.LogisticNoteBuilder;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

/** [Controller] Show purchase recipient */
public class ShowPurchaserRecipient_Controller {
    @FXML TextField PurchaserNameText, PurchaserTelephoneTitleText, PurchaserTelephoneNumberText, PurchaserTelephoneTransferText, PurchaserCellphoneTitleText, PurchaserCellphoneNumberText, PurchaserAddressText;
    @FXML TextField RecipientNameText, RecipientTelephoneTitleText, RecipientTelephoneNumberText, RecipientTelephoneTransferText, RecipientCellphoneTitleText, RecipientCellphoneNumberText, RecipientAddressText;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallConfig CallConfig;
    private Stage Stage;

    private EstablishOrder_Controller EstablishOrder_Controller;
    private OrderExist OrderExist;
    private Order_Bean Order_Bean;
    public ShowPurchaserRecipient_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallConfig = ToolKit.CallConfig;
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){ this.EstablishOrder_Controller = EstablishOrder_Controller; }
    /** Set object - [Enum] Order_Enum.OrderExist */
    public void setOrderExist(OrderExist OrderExist){    this.OrderExist = OrderExist;   }
    /** Set object - [Bean] Order_Bean */
    public void setOrder_Bean(Order_Bean Order_Bean){  this.Order_Bean = Order_Bean; }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set component of show purchase recipient */
    public void setComponent(){
        setKeyWordTextLimiter();
        setKeyWordTextDigital();
        if(OrderExist == Order_Enum.OrderExist.已存在) setPurchaserRecipientInfo();
        else    initialComponent();
    }
    private void initialComponent(){
        if(Order_Bean.getPurchaserName() == null)
            Order_Bean.setPurchaserName("");
        PurchaserNameText.setText(Order_Bean.getPurchaserName());

        if(Order_Bean.getPurchaserTelephone() == null){
            Order_Bean.setPurchaserTelephone("");
        }
        setTelephoneText(Order_Bean.getPurchaserTelephone(),PurchaserTelephoneTitleText,PurchaserTelephoneNumberText,PurchaserTelephoneTransferText);

        if(Order_Bean.getPurchaserCellphone() == null){
            Order_Bean.setPurchaserCellphone("");
        }
        setCellphoneText(Order_Bean.getPurchaserCellphone(),PurchaserCellphoneTitleText,PurchaserCellphoneNumberText);

        if(Order_Bean.getPurchaserAddress() == null){
            Order_Bean.setPurchaserAddress("");
        }
        PurchaserAddressText.setText(Order_Bean.getPurchaserAddress());

        if(Order_Bean.getRecipientName() == null)
            Order_Bean.setRecipientName("");
        RecipientNameText.setText(Order_Bean.getRecipientName());

        if(Order_Bean.getRecipientTelephone() == null){
            Order_Bean.setRecipientTelephone("");
        }
        setTelephoneText(Order_Bean.getRecipientTelephone(),RecipientTelephoneTitleText,RecipientTelephoneNumberText,RecipientTelephoneTransferText);

        if(Order_Bean.getRecipientCellphone() == null){
            Order_Bean.setRecipientCellphone("");
        }
        setCellphoneText(Order_Bean.getRecipientCellphone(),RecipientCellphoneTitleText,RecipientCellphoneNumberText);

        if(Order_Bean.getRecipientAddress() == null){
            Order_Bean.setRecipientAddress("");
        }
        RecipientAddressText.setText(Order_Bean.getRecipientAddress());
    }
    private void setKeyWordTextLimiter(){
        ComponentToolKit.addKeyWordTextLimitLength(PurchaserTelephoneTitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(PurchaserTelephoneNumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(PurchaserTelephoneTransferText, 5);
        ComponentToolKit.addKeyWordTextLimitLength(PurchaserCellphoneTitleText, 4);
        ComponentToolKit.addKeyWordTextLimitLength(PurchaserCellphoneNumberText, 6);
        ComponentToolKit.addKeyWordTextLimitLength(RecipientTelephoneTitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(RecipientTelephoneNumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(RecipientTelephoneTransferText, 5);
        ComponentToolKit.addKeyWordTextLimitLength(RecipientCellphoneTitleText, 4);
        ComponentToolKit.addKeyWordTextLimitLength(RecipientCellphoneNumberText, 6);
    }
    private void setKeyWordTextDigital(){
        ComponentToolKit.addTextFieldLimitDigital(PurchaserTelephoneTitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(PurchaserTelephoneNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(PurchaserTelephoneTransferText,false);
        ComponentToolKit.addTextFieldLimitDigital(PurchaserCellphoneTitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(PurchaserCellphoneNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(RecipientTelephoneTitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(RecipientTelephoneNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(RecipientTelephoneTransferText,false);
        ComponentToolKit.addTextFieldLimitDigital(RecipientCellphoneTitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(RecipientCellphoneNumberText,false);
    }
    private void setPurchaserRecipientInfo(){
        PurchaserNameText.setText(Order_Bean.getPurchaserName());
        setTelephoneText(Order_Bean.getPurchaserTelephone(), PurchaserTelephoneTitleText, PurchaserTelephoneNumberText, PurchaserTelephoneTransferText);
        setCellphoneText(Order_Bean.getPurchaserCellphone(), PurchaserCellphoneTitleText, PurchaserCellphoneNumberText);
        PurchaserAddressText.setText(Order_Bean.getPurchaserAddress());
        RecipientNameText.setText(Order_Bean.getRecipientName());
        setTelephoneText(Order_Bean.getRecipientTelephone(), RecipientTelephoneTitleText, RecipientTelephoneNumberText, RecipientTelephoneTransferText);
        setCellphoneText(Order_Bean.getRecipientCellphone(), RecipientCellphoneTitleText, RecipientCellphoneNumberText);
        RecipientAddressText.setText(Order_Bean.getRecipientAddress());
    }
    private void setTelephoneText(String Telephone, TextField TelephoneTitleText, TextField TelephoneNumberText, TextField TelephoneTransfer){
        if(Telephone.contains("#")) {
            TelephoneTitleText.setText(Telephone.substring(0,Telephone.indexOf("-")));
            TelephoneNumberText.setText(Telephone.substring(Telephone.indexOf("-")+1, Telephone.lastIndexOf("#")));
            TelephoneTransfer.setText(Telephone.substring(Telephone.lastIndexOf("#")+1));
        }else if(Telephone.contains("-")){
            TelephoneTitleText.setText(Telephone.substring(0,Telephone.indexOf("-")));
            TelephoneNumberText.setText(Telephone.substring(Telephone.indexOf("-")+1));
            TelephoneTransfer.setText("");
        }else{
            TelephoneTitleText.setText("");
            TelephoneNumberText.setText("");
            TelephoneTransfer.setText("");
        }
    }
    private void setCellphoneText(String Cellphone, TextField CellphoneTitleText, TextField CellphoneNumber){
        if(Cellphone.contains("-")){
            CellphoneTitleText.setText(Cellphone.substring(0,Cellphone.indexOf("-")));
            CellphoneNumber.setText(Cellphone.substring(Cellphone.indexOf("-")+1));
        }else{
            CellphoneTitleText.setText("");
            CellphoneNumber.setText("");
        }
    }
    /** TextField Key Released - 訂購人 電話開頭 */
    @FXML protected void PurchaserTelephoneTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String TelephoneTitle = PurchaserTelephoneTitleText.getText();
            if(!ToolKit.isDigital(TelephoneTitle))  PurchaserTelephoneTitleText.setText("");
            else if(TelephoneTitle.length() >= 2)   PurchaserTelephoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 訂購人 電話號碼 */
    @FXML protected void PurchaserTelephoneNumberKeyReleased(KeyEvent KeyEvent){
        String TelephoneNumber = PurchaserTelephoneNumberText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent))    PurchaserTelephoneTransferText.requestFocus();
        else if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (TelephoneNumber.equals("")) PurchaserTelephoneTitleText.requestFocus();
        }else if(!KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(TelephoneNumber))     PurchaserTelephoneNumberText.setText("");
            else if(TelephoneNumber.length() >= 8)   PurchaserTelephoneTransferText.requestFocus();
        }
    }
    /** TextField Key Released - 訂購人 電話轉接 */
    @FXML protected void PurchaserTelephoneTransferKeyReleased(KeyEvent KeyEvent){
        String TelephoneTransfer = PurchaserTelephoneTransferText.getText();
        if(KeyPressed.isBackSpaceKeyPressed(KeyEvent))    if (TelephoneTransfer.equals("")) PurchaserTelephoneNumberText.requestFocus();
        else if(KeyPressed.isDeleteKeyPressed(KeyEvent))  if(!ToolKit.isDigital(TelephoneTransfer))     PurchaserTelephoneTransferText.setText("");
    }
    /** TextField Key Released - 訂購人 手機開頭 */
    @FXML protected void PurchaserCellphoneTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String CellphoneTitle = PurchaserCellphoneTitleText.getText();
            if(!ToolKit.isDigital(CellphoneTitle))     PurchaserCellphoneTitleText.setText("");
            else if(CellphoneTitle.length() >= 4)    PurchaserCellphoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 訂購人 手機號碼 */
    @FXML protected void PurchaserCellphoneNumberKeyReleased(KeyEvent KeyEvent){
        String CellphoneNumber = PurchaserCellphoneNumberText.getText();
        if (KeyPressed.isBackSpaceKeyPressed(KeyEvent))
            if (CellphoneNumber.equals("")) PurchaserCellphoneTitleText.requestFocus();
            else if(!KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent))
                if(!ToolKit.isDigital(CellphoneNumber))     PurchaserCellphoneNumberText.setText("");
    }
    /** TextField Key Released - 收件人 電話開頭 */
    @FXML protected void RecipientTelephoneTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String TelephoneTitle = RecipientTelephoneTitleText.getText();
            if(!ToolKit.isDigital(TelephoneTitle))     RecipientTelephoneTitleText.setText("");
            else if(TelephoneTitle.length() >= 2)    RecipientTelephoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 收件人 電話號碼 */
    @FXML protected void RecipientTelephoneNumberKeyReleased(KeyEvent KeyEvent){
        String TelephoneNumber = RecipientTelephoneNumberText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent))    RecipientTelephoneTransferText.requestFocus();
        else if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (TelephoneNumber.equals("")) RecipientTelephoneTitleText.requestFocus();
        }else if(!KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(TelephoneNumber))     RecipientTelephoneNumberText.setText("");
            else if(TelephoneNumber.length() >= 8)   RecipientTelephoneTransferText.requestFocus();
        }
    }
    /** TextField Key Released - 收件人 電話轉接 */
    @FXML protected void RecipientTelephoneTransferKeyReleased(KeyEvent KeyEvent){
        String TelephoneTransfer = RecipientTelephoneTransferText.getText();
        if(KeyPressed.isBackSpaceKeyPressed(KeyEvent))    if (TelephoneTransfer.equals("")) RecipientTelephoneNumberText.requestFocus();
        else if(KeyPressed.isDeleteKeyPressed(KeyEvent))  if(!ToolKit.isDigital(TelephoneTransfer))     RecipientTelephoneTransferText.setText("");
    }
    /** TextField Key Released - 收件人 手機開頭 */
    @FXML protected void RecipientCellphoneTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String CellphoneTitle = RecipientCellphoneTitleText.getText();
            if(!ToolKit.isDigital(CellphoneTitle))     RecipientCellphoneTitleText.setText("");
            else if(CellphoneTitle.length() >= 4)    RecipientCellphoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 收件人 手機號碼 */
    @FXML protected void RecipientCellphoneNumberKeyReleased(KeyEvent KeyEvent){
        String CellphoneNumber = RecipientCellphoneNumberText.getText();
        if (KeyPressed.isBackSpaceKeyPressed(KeyEvent))
            if (CellphoneNumber.equals("")) RecipientCellphoneTitleText.requestFocus();
            else if(!KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent))
                if(!ToolKit.isDigital(CellphoneNumber))     RecipientCellphoneNumberText.setText("");
    }
    /** 大榮貨運    */
    @FXML protected void LogisticsNoteMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(PurchaserTelephoneTitleText.getText().equals("") || PurchaserTelephoneNumberText.getText().equals("")) {
                DialogUI.MessageDialog("※ 請輸入訂購人電話");
                return;
            }
            if(RecipientTelephoneTitleText.getText().equals("") || RecipientTelephoneNumberText.getText().equals("")) {
                DialogUI.MessageDialog("※ 請輸入收件人電話");
                return;
            }
            String PurchaseTelephone = PurchaserTelephoneTitleText.getText() + "-" + PurchaserTelephoneNumberText.getText();
            if(!PurchaserTelephoneTransferText.getText().equals(""))    PurchaseTelephone = PurchaseTelephone + "#" + PurchaserTelephoneTransferText.getText();
            String RecipientTelephone = RecipientTelephoneTitleText.getText() + "-" + RecipientTelephoneNumberText.getText();
            if(!RecipientTelephoneTransferText.getText().equals(""))    RecipientTelephone = RecipientTelephone + "#" + RecipientTelephoneTransferText.getText();

            LogisticNoteBuilder builder = new LogisticNoteBuilder(TemplatePath.Logistics_Note);
            builder.buildLogisticsNote(
                    LocalDate.now().plusDays(10),
                    RecipientNameText.getText(),
                    RecipientTelephone,
                    RecipientAddressText.getText(),
                    LocalDate.now(),
                    PurchaserNameText.getText(),
                    PurchaseTelephone,
                    Order_Bean.getObjectName(),
                    PurchaserAddressText.getText(),
                    ""
            );
            String outputFilePath = CallConfig.getFile_OutputPath();
            File file = builder.exportExcel(outputFilePath,Order_Bean.getObjectName() + " - 大榮託運");
            Desktop.getDesktop().open(file);
        }
    }
    /** Button Mouse Clicked - 確定 */
    @FXML protected void ConfirmMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(OrderExist == Order_Enum.OrderExist.已存在){
                if(isInfoModify()){
                    if(DialogUI.AlarmDialog("※ 訂購/收件資訊已變更，請記得「修改貨單」!")){
                        EstablishOrder_Controller.setModifyOrderButtonStyle();
                    }
                }
            }
            setOrderPurchaserInfoBean();
            setOrderRecipientInfoBean();
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    private boolean isInfoModify(){
        if(!PurchaserNameText.getText().equals(Order_Bean.getPurchaserName()) || !PurchaserAddressText.getText().equals(Order_Bean.getPurchaserAddress()))
            return true;
        if(PurchaserTelephoneTitleText.getText().equals("") && !Order_Bean.getPurchaserTelephone().equals(""))
            return true;
        else if(!PurchaserTelephoneTransferText.getText().equals("") && !Order_Bean.getPurchaserTelephone().equals(PurchaserTelephoneTitleText.getText() + "-" + PurchaserTelephoneNumberText.getText() + "#" + PurchaserTelephoneTransferText.getText()))
            return true;
        else if(!PurchaserTelephoneNumberText.getText().equals("") && !Order_Bean.getPurchaserTelephone().equals(PurchaserTelephoneTitleText.getText() + "-" + PurchaserTelephoneNumberText.getText()))
            return true;
        if(PurchaserCellphoneTitleText.getText().equals("") && !Order_Bean.getPurchaserCellphone().equals(""))
            return true;
        else if(!PurchaserCellphoneNumberText.getText().equals("") && !Order_Bean.getPurchaserCellphone().equals(PurchaserCellphoneTitleText.getText() + "-" + PurchaserCellphoneNumberText.getText()))
            return true;

        if(!RecipientNameText.getText().equals(Order_Bean.getRecipientName()) || !RecipientAddressText.getText().equals(Order_Bean.getRecipientAddress()))
            return true;
        if(RecipientTelephoneTitleText.getText().equals("") && !Order_Bean.getRecipientTelephone().equals(""))
            return true;
        else if(!RecipientTelephoneTransferText.getText().equals("") && !Order_Bean.getRecipientTelephone().equals(RecipientTelephoneTitleText.getText() + "-" + RecipientTelephoneNumberText.getText() + "#" + RecipientTelephoneTransferText.getText()))
            return true;
        else if(!RecipientTelephoneNumberText.getText().equals("") && !Order_Bean.getRecipientTelephone().equals(RecipientTelephoneTitleText.getText() + "-" + RecipientTelephoneNumberText.getText()))
            return true;
        if(RecipientCellphoneTitleText.getText().equals("") && !Order_Bean.getRecipientCellphone().equals(""))
            return true;
        else return !RecipientCellphoneNumberText.getText().equals("") && !Order_Bean.getRecipientCellphone().equals(RecipientCellphoneTitleText.getText() + "-" + RecipientCellphoneNumberText.getText());
    }
    private void setOrderPurchaserInfoBean(){
        Order_Bean.setPurchaserName(PurchaserNameText.getText());
        if(PurchaserTelephoneTitleText.getText().equals(""))    Order_Bean.setPurchaserTelephone("");
        else if(!PurchaserTelephoneTransferText.getText().equals(""))   Order_Bean.setPurchaserTelephone(PurchaserTelephoneTitleText.getText() + "-" + PurchaserTelephoneNumberText.getText() + "#" + PurchaserTelephoneTransferText.getText());
        else    Order_Bean.setPurchaserTelephone(PurchaserTelephoneTitleText.getText() + "-" + PurchaserTelephoneNumberText.getText());
        if(PurchaserCellphoneTitleText.getText().equals(""))    Order_Bean.setPurchaserCellphone("");
        else  Order_Bean.setPurchaserCellphone(PurchaserCellphoneTitleText.getText() + "-" + PurchaserCellphoneNumberText.getText());
        Order_Bean.setPurchaserAddress(PurchaserAddressText.getText());
    }
    private void setOrderRecipientInfoBean(){
        Order_Bean.setRecipientName(RecipientNameText.getText());
        if(RecipientTelephoneTitleText.getText().equals("")) Order_Bean.setRecipientTelephone("");
        else if(!RecipientTelephoneTransferText.getText().equals(""))    Order_Bean.setRecipientTelephone(RecipientTelephoneTitleText.getText() + "-" + RecipientTelephoneNumberText.getText() + "#" + RecipientTelephoneTransferText.getText());
        else    Order_Bean.setRecipientTelephone(RecipientTelephoneTitleText.getText() + "-" + RecipientTelephoneNumberText.getText());
        if(RecipientCellphoneTitleText.getText().equals(""))    Order_Bean.setRecipientCellphone("");
        else    Order_Bean.setRecipientCellphone(RecipientCellphoneTitleText.getText() + "-" + RecipientCellphoneNumberText.getText());
        Order_Bean.setRecipientAddress(RecipientAddressText.getText());
    }
    /** Button Mouse Clicked - 離開 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){   if(KeyPressed.isMouseLeftClicked(MouseEvent))  ComponentToolKit.closeThisStage(Stage);  }
}
