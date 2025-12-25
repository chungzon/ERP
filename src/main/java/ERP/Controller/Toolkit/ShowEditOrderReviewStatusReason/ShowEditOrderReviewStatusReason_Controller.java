package ERP.Controller.Toolkit.ShowEditOrderReviewStatusReason;

import ERP.Bean.Order.OrderReviewStatusPicture_Bean;
import ERP.Bean.Order.OrderReviewStatusRecord_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.ReviewStatus;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.UploadPictureFunction;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowEditOrderReviewStatusReason_Controller {
    @FXML RadioButton PastePicture_RadioButton, UploadLocation_RadioButton;
    @FXML TextField SubjectText;
    @FXML TextArea RecordText;
    @FXML Button EditPictureButton;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private CallFXML CallFXML;
    private KeyPressed KeyPressed;
    private Stage Stage;

    private EstablishOrder_Controller EstablishOrder_Controller;
    private SystemSetting_Model SystemSetting_Model;
    private ReviewStatus ReviewStatus;
    private OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean = new OrderReviewStatusRecord_Bean();

    private final KeyCombination savePictureMoveKeyCombine = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
    public ShowEditOrderReviewStatusReason_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        this.SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setReviewStatus(ReviewStatus ReviewStatus){
        this.ReviewStatus = ReviewStatus;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){
        this.EstablishOrder_Controller = EstablishOrder_Controller;
    }
    public void setComponent(){
        getDefaultUploadPictureFunction();
    }
    private void getDefaultUploadPictureFunction(){
        Integer defaultConfig = SystemSetting_Model.getSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.UploadPictureFunction);
        if(defaultConfig != null) {
            if(SystemDefaultConfig_Enum.UploadPictureFunction.values()[defaultConfig] == SystemDefaultConfig_Enum.UploadPictureFunction.PastePicture){
                PastePicture_RadioButton.setSelected(true);
            }else if(SystemDefaultConfig_Enum.UploadPictureFunction.values()[defaultConfig] == SystemDefaultConfig_Enum.UploadPictureFunction.UploadLocation) {
                UploadLocation_RadioButton.setSelected(true);
            }
        }
    }
    private void setDefaultUploadPictureFunction(UploadPictureFunction UploadPictureFunction){
        if(!SystemSetting_Model.updateSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.UploadPictureFunction.name(),
                UploadPictureFunction.ordinal())) {
            DialogUI.AlarmDialog("※ 【預設值】更新失敗");
        }
    }
    @FXML protected void SubjectTextKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            RecordText.requestFocus();
        }
    }
    @FXML protected void PastePictureOnAction() throws Exception {
        if(PastePicture_RadioButton.isSelected()) {
            setDefaultUploadPictureFunction(UploadPictureFunction.PastePicture);
            Boolean uploadStats = UploadPicture(UploadPictureFunction.PastePicture);
            if (uploadStats != null) {
                if(uploadStats){
                    DialogUI.MessageDialog("※ 上傳成功");
                }else{
                    DialogUI.MessageDialog("未複製圖片");
                }
            }
        }
    }
    @FXML protected void UploadLocationOnAction() throws Exception {
        if(UploadLocation_RadioButton.isSelected()) {
            setDefaultUploadPictureFunction(UploadPictureFunction.UploadLocation);
            Boolean uploadStats = UploadPicture(UploadPictureFunction.UploadLocation);
            if (uploadStats != null && uploadStats) {
                DialogUI.MessageDialog("※ 上傳成功");
            }
        }
    }
    @FXML protected void UploadPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try{
                Boolean uploadStats;
                if(PastePicture_RadioButton.isSelected()){
                    uploadStats = UploadPicture(UploadPictureFunction.PastePicture);
                    if(uploadStats != null){
                        if(uploadStats){
                            DialogUI.MessageDialog("※ 上傳成功");
                        }else{
                            DialogUI.MessageDialog("未複製圖片");
                        }
                    }
                }else if(UploadLocation_RadioButton.isSelected()){
                    uploadStats = UploadPicture(UploadPictureFunction.UploadLocation);
                    if (uploadStats != null && uploadStats) {
                        DialogUI.MessageDialog("※ 上傳成功");
                    }
                }else{
                    DialogUI.MessageDialog("請選擇上傳方式");
                }
            }catch (Exception Ex){
                DialogUI.MessageDialog("※ 上傳失敗");
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    private Boolean UploadPicture(UploadPictureFunction UploadPictureFunction) throws Exception {
        boolean isExistUploadPicture = false;
        ArrayList<OrderReviewStatusPicture_Bean> pictureList = OrderReviewStatusRecord_Bean.getPictureList();
        if(pictureList != null && pictureList.size() >=1){
            isExistUploadPicture = true;
        }
        if(UploadPictureFunction == SystemDefaultConfig_Enum.UploadPictureFunction.PastePicture){
            Clipboard cb = Clipboard.getSystemClipboard();
            String base64Format = getCopyImageBase64(cb);
            if(base64Format != null){
                if(isExistUploadPicture && !DialogUI.ConfirmDialog("【存在圖片】將覆蓋當前圖片，是否繼續 ?",true,false,0,0)){
                    return null;
                }
                recordUploadOrSnapshotPicture(base64Format);
                return true;
            }
        }else if(UploadPictureFunction == SystemDefaultConfig_Enum.UploadPictureFunction.UploadLocation){
            if(isExistUploadPicture && !DialogUI.ConfirmDialog("【存在圖片】將覆蓋當前圖片，是否繼續 ?",true,false,0,0)){
                return null;
            }
            File File = ComponentToolKit.setFileChooser("載入圖片").showOpenDialog(ComponentToolKit.setStage());
            if (File != null) {
                String base64Format = ToolKit.generateBase64(File);
                recordUploadOrSnapshotPicture(base64Format);
                return true;
            }
        }
        return false;
    }
    private String getCopyImageBase64(Clipboard cb) throws Exception {
        if(cb.hasImage()){
            BufferedImage image = SwingFXUtils.fromFXImage(cb.getImage(), null);
            return ToolKit.generateBase64(ToolKit.fixImagePickBackgroundColor(image));
        }else if(cb.hasFiles()) {
            List<File> copyFile = cb.getFiles();
            if(copyFile.size() > 1){
                StringBuilder text = new StringBuilder("【只允許上傳一張圖片】已複製：");
                for(File file : copyFile){
                    text.append("\n").append(file.getName());
                }
                DialogUI.AlarmDialog(text.toString());
            }else if(!copyFile.get(0).getName().contains(".jpg") && !copyFile.get(0).getName().contains(".png")){
                DialogUI.AlarmDialog("只允許【jpg、png】圖片格式，錯誤檔名：" + copyFile.get(0).getName());
            }else{
                return ToolKit.generateBase64(copyFile.get(0));
            }
        }
        return null;
    }
    @FXML protected void SnapshotPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowSnapshotOrderPicture(Stage, ToolKit_Enum.SnapshotPictureLocation.審查紀錄, this);
        }
    }
    public boolean saveSnapshotPicture(String base64Format){
        recordUploadOrSnapshotPicture(base64Format);
        return true;
    }
    private void recordUploadOrSnapshotPicture(String base64Format){
        ArrayList<OrderReviewStatusPicture_Bean> pictureList;
        if(OrderReviewStatusRecord_Bean.getPictureList() == null) {
            pictureList = new ArrayList<>();
            OrderReviewStatusRecord_Bean.setPictureList(pictureList);
        }
        pictureList = OrderReviewStatusRecord_Bean.getPictureList();
        OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean;
        if(pictureList.size() != 0) {
            pictureList.get(0).setBase64(base64Format);
        }else {
            orderReviewStatusPicture_Bean = new OrderReviewStatusPicture_Bean();
            orderReviewStatusPicture_Bean.setBase64(base64Format);
            pictureList.add(orderReviewStatusPicture_Bean);
        }
        EditPictureButton.setText("圖片編輯(" + pictureList.size() + ")");
        ComponentToolKit.setButtonDisable(EditPictureButton, false);
    }
    @FXML protected void EditPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowPictureEditor(Stage, OrderReviewStatusRecord_Bean.getPictureList().get(0), this);
        }
    }
    @FXML protected void RecordTextAreaKeyReleased(KeyEvent KeyEvent) throws Exception {
        if(savePictureMoveKeyCombine.match(KeyEvent)) {
            Boolean uploadStats = UploadPicture(UploadPictureFunction.PastePicture);
            if (uploadStats != null && uploadStats) {
                DialogUI.MessageDialog("※ 上傳成功");
            }
        }
    }
    public void recordEditPicture(String base64Format){
        ArrayList<OrderReviewStatusPicture_Bean> pictureList = OrderReviewStatusRecord_Bean.getPictureList();
        OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean = new OrderReviewStatusPicture_Bean();
        orderReviewStatusPicture_Bean.setBase64(base64Format);
        pictureList.add(orderReviewStatusPicture_Bean);
        EditPictureButton.setText("圖片編輯(" + pictureList.size() + ")");
    }
    public void deleteEditPicture(boolean isDeleteAll){
        if(isDeleteAll) {
            OrderReviewStatusRecord_Bean.getPictureList().clear();
            ComponentToolKit.setButtonDisable(EditPictureButton, true);
            EditPictureButton.setText("圖片編輯");
        }else{
            Iterator<OrderReviewStatusPicture_Bean> pictureList = OrderReviewStatusRecord_Bean.getPictureList().iterator();
            if(pictureList.hasNext())
                pictureList.next();
            while (pictureList.hasNext()) {
                pictureList.next();
                pictureList.remove();
            }
            EditPictureButton.setText("圖片編輯(1)");
        }
    }
    @FXML protected void SaveMouseClicked(MouseEvent MouseEvent) throws IOException {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String subjectText = SubjectText.getText();
            String recordText = RecordText.getText();
            if(recordText == null || recordText.equals(""))
                DialogUI.MessageDialog("※ 請輸入【" + ReviewStatus.name()+ "】" + (ReviewStatus == Order_Enum.ReviewStatus.待修改 ? "原因":"備註"));
            else{
                boolean stillSave = true;
                if((subjectText == null || subjectText.equals("")))
                    stillSave = DialogUI.ConfirmDialog("※ 主旨為空，仍要儲存 ?", true, false, 0, 0);
                if(stillSave) {
                    OrderReviewStatusRecord_Bean.setSubject(subjectText == null ? "" : subjectText);
                    OrderReviewStatusRecord_Bean.setRecord(recordText);
                    if(EstablishOrder_Controller.postOrderReviewStatusLineAPI(ReviewStatus, OrderReviewStatusRecord_Bean))
                        Stage.close();
                }
            }
        }
    }
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Stage.close();
        }
    }
}
