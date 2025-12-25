package ERP.Controller.Toolkit.ShowSnapshotOrderPicture;

import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.Controller.Toolkit.ShowEditOrderReviewStatusReason.ShowEditOrderReviewStatusReason_Controller;
import ERP.ERPApplication;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.IpCamInfo_Bean;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.WebCamInfo_Bean;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_SnapshotCamera;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import ERP.Enum.ToolKit.ToolKit_Enum.SnapshotPictureLocation;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShowSnapshotOrderPicture_Controller extends ToolKit implements Initializable {
    @FXML TabPane TabPane;
    @FXML Tab IpCamTab, WebCamTab;
    @FXML ComboBox<IpCamInfo_Bean> selectIpCam_ComboBox;
    @FXML ComboBox<WebCamInfo_Bean> selectWebCam_ComboBox;
    @FXML Button showCamera_Button, snapshot_Button, uploadSnapshot_Button;
    @FXML GridPane camera_GridPane;
    @FXML ImageView snapshot_ImageView;
    @FXML ProgressIndicator ProgressIndicator;
    private SwingNode swingNode;
    private Webcam selectWebcam;

    private Order_SnapshotCamera Order_SnapshotCamera;
    private SnapshotPictureLocation SnapshotPictureLocation;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private ShowEditOrderReviewStatusReason_Controller ShowEditOrderReviewStatusReason_Controller;
    private SystemSetting_Model SystemSetting_Model;
    private Stage Stage;

    private ToolKit ToolKit;
    public ShowSnapshotOrderPicture_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setSnapshotPictureLocationAndSourceObject(SnapshotPictureLocation SnapshotPictureLocation, Object sourceObject){
        this.SnapshotPictureLocation = SnapshotPictureLocation;
        if(SnapshotPictureLocation == ToolKit_Enum.SnapshotPictureLocation.貨單)
            EstablishOrder_Controller = (EstablishOrder_Controller)sourceObject;
        else if(SnapshotPictureLocation == ToolKit_Enum.SnapshotPictureLocation.審查紀錄)
            ShowEditOrderReviewStatusReason_Controller = (ShowEditOrderReviewStatusReason_Controller)sourceObject;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        ComponentToolKit.setIpCamInfoBeanComboBoxObj(selectIpCam_ComboBox);
        ComponentToolKit.setWebCamInfoBeanComboBoxObj(selectWebCam_ComboBox);
        swingNode = new SwingNode();
        camera_GridPane.getChildren().add(swingNode);

        getDefaultSelectTab();
        setDefaultPreviewCamera();
        createAction();
    }
    private void getDefaultSelectTab(){
        Integer defaultConfig = SystemSetting_Model.getSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.Order_SnapshotCamera);
        if(defaultConfig == null) {
            Order_SnapshotCamera = SystemDefaultConfig_Enum.Order_SnapshotCamera.IPCamera;
            TabPane.getSelectionModel().select(IpCamTab);
        }else{
            Order_SnapshotCamera = SystemDefaultConfig_Enum.Order_SnapshotCamera.values()[defaultConfig];
            if(Order_SnapshotCamera == SystemDefaultConfig_Enum.Order_SnapshotCamera.IPCamera)
                TabPane.getSelectionModel().select(IpCamTab);
            else
                TabPane.getSelectionModel().select(WebCamTab);
        }
    }
    private void setDefaultPreviewCamera(){
        if(Order_SnapshotCamera == SystemDefaultConfig_Enum.Order_SnapshotCamera.IPCamera){
            //  根據回報 #6934：不要欲先載入 IPcam
//            previewIPCamera();
        }else if(Order_SnapshotCamera == SystemDefaultConfig_Enum.Order_SnapshotCamera.WebCamera){
            ObservableList<WebCamInfo_Bean> webCameraList = ComponentToolKit.getWebCamComboBoxItemList(selectWebCam_ComboBox);
            if(webCameraList.size() != 0)
                previewWebCamera(0);
        }
    }
    private void createAction(){
        Stage.setOnCloseRequest(event -> {
            closeIpCamera();
            closeWebCamera();
            Stage.close();
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectIpCam_ComboBox.setItems(SystemSetting_Model.getIpCamera());

        ObservableList<WebCamInfo_Bean> options = FXCollections.observableArrayList();
        int webCamCounter = 0;
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo_Bean WebCamInfo_Bean = new WebCamInfo_Bean();
            WebCamInfo_Bean.setWebCamIndex(webCamCounter);
            WebCamInfo_Bean.setWebCamName(webcam.getName());
            options.add(WebCamInfo_Bean);
            webCamCounter++;
        }
        selectWebCam_ComboBox.setItems(options);
    }
    @FXML protected void IpCamTabChanged(){
        if(IpCamTab.isSelected()){
            closeWebCamera();
            if(selectWebCam_ComboBox != null)
                selectWebCam_ComboBox.getSelectionModel().select(null);
            if(swingNode != null)
                swingNode.setVisible(false);
            if(snapshot_ImageView != null) {
                snapshot_ImageView.setImage(null);
                snapshot_ImageView.setVisible(true);
            }
            ComponentToolKit.setButtonDisable(showCamera_Button,true);
            ComponentToolKit.setButtonDisable(snapshot_Button,true);
            ComponentToolKit.setButtonDisable(uploadSnapshot_Button,true);
            if(Order_SnapshotCamera != null) {
                this.Order_SnapshotCamera = SystemDefaultConfig_Enum.Order_SnapshotCamera.IPCamera;
                updateSystemDefaultConfig();
                setDefaultPreviewCamera();
            }
        }
    }
    private void previewIPCamera(){
        ObservableList<IpCamInfo_Bean> ipCamList = ComponentToolKit.getIpCamComboBoxItems(selectIpCam_ComboBox);
        for(IpCamInfo_Bean IpCamInfo_Bean : ipCamList){
            if(IpCamInfo_Bean.getDefaultPreview()){
                selectIpCam_ComboBox.getSelectionModel().select(IpCamInfo_Bean);
                openIpCam(IpCamInfo_Bean);
                break;
            }
        }
    }
    @FXML protected void WebCamTabChanged(){
        if(WebCamTab.isSelected()){
            closeIpCamera();
            if(selectWebCam_ComboBox != null)
                selectIpCam_ComboBox.getSelectionModel().select(null);
            if(swingNode != null)
                swingNode.setVisible(true);
            if(snapshot_ImageView != null) {
                snapshot_ImageView.setImage(null);
                snapshot_ImageView.setVisible(false);
            }
            ComponentToolKit.setButtonDisable(showCamera_Button,true);
            ComponentToolKit.setButtonDisable(snapshot_Button,true);
            ComponentToolKit.setButtonDisable(uploadSnapshot_Button,true);
            if(Order_SnapshotCamera != null) {
                this.Order_SnapshotCamera = SystemDefaultConfig_Enum.Order_SnapshotCamera.WebCamera;
                updateSystemDefaultConfig();
                setDefaultPreviewCamera();
            }
        }
    }

    private VideoCapture selectIpCam;
    private ScheduledExecutorService timer;
    private double rotateDegree = 0;
    @FXML protected void selectIpCameraOnAction(){
        IpCamInfo_Bean IpCamInfo_Bean = ComponentToolKit.getIpCamComboBoxSelectItem(selectIpCam_ComboBox);
        if(IpCamInfo_Bean != null){
            openIpCam(IpCamInfo_Bean);
        }
    }
    @FXML protected void reConnectIpCamMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IpCamInfo_Bean IpCamInfo_Bean = ComponentToolKit.getIpCamComboBoxSelectItem(selectIpCam_ComboBox);
            if(IpCamInfo_Bean == null)  DialogUI.MessageDialog("※ 請選擇攝影機");
            else
                openIpCam(IpCamInfo_Bean);
        }
    }
    protected void openIpCam(IpCamInfo_Bean IpCamInfo_Bean){
        if(selectIpCam != null) closeIpCamera();
        selectIpCam = new VideoCapture();
        selectIpCam.open(IpCamInfo_Bean.getRTSP());
        if (selectIpCam.isOpened()){
            Runnable frameGrabber = () -> {
                Mat frame = new Mat();
                selectIpCam.read(frame);
                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".jpg", frame, byteMat);

                Image imageToShow = new Image(new ByteArrayInputStream(byteMat.toArray()));
                snapshot_ImageView.setImage(imageToShow);
            };
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

            ComponentToolKit.setButtonDisable(uploadSnapshot_Button,false);
        }else
            DialogUI.MessageDialog("※ 影像串流開啟失敗");
    }
    @FXML protected void rotateIpCamImageMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            snapshot_ImageView.setRotate(snapshot_ImageView.getRotate()+90);
            rotateDegree = snapshot_ImageView.getRotate();
            if(rotateDegree%180 == 0){  //  16:9
                snapshot_ImageView.setFitWidth(960);
                snapshot_ImageView.setFitHeight(540);
            }else { //  9:16
                snapshot_ImageView.setFitWidth(640);
                snapshot_ImageView.setFitHeight(360);
            }
        }
    }
    @FXML protected void selectWebCameraOnAction(){
        WebCamInfo_Bean WebCamInfo_Bean = ComponentToolKit.getWebCamComboBoxSelectItem(selectWebCam_ComboBox);
        if(WebCamInfo_Bean != null){
            previewWebCamera(WebCamInfo_Bean.getWebCamIndex());
        }
    }
    protected void previewWebCamera(final int webCamIndex) {
        Task<Void> webCamIntilizer = new Task<Void>() {
            @Override
            protected Void call() {
                if (selectWebcam != null)   closeWebCamera();
                selectWebcam = Webcam.getWebcams().get(webCamIndex);
                Dimension[] nonStandardResolutions = new Dimension[] { WebcamResolution.PAL.getSize(),
                        WebcamResolution.HD.getSize(), new Dimension(2000, 1000), new Dimension(1000, 500), };
                selectWebcam.setCustomViewSizes(nonStandardResolutions);
                selectWebcam.setViewSize(WebcamResolution.HD.getSize());

                Platform.runLater(() -> startWebCamStream());
                return null;
            }
        };
        new Thread(webCamIntilizer).start();
    }
    protected void startWebCamStream() {
        snapshot_ImageView.setVisible(false);
        snapshot_Button.setDisable(false);
        WebcamPanel webcamPanel = new WebcamPanel(selectWebcam);
        webcamPanel.setFPSDisplayed(true);

        SwingUtilities.invokeLater(() -> {
            swingNode.setContent(webcamPanel);
            webcamPanel.start();
        });
    }
    @FXML protected void showCameraMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            swingNode.setVisible(true);
            snapshot_ImageView.setVisible(false);
            ComponentToolKit.setButtonDisable(showCamera_Button,true);
            ComponentToolKit.setButtonDisable(snapshot_Button,false);
            ComponentToolKit.setButtonDisable(uploadSnapshot_Button,true);
        }
    }
    @FXML protected void snapshotMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            swingNode.setVisible(false);
            javafx.scene.image.Image image = SwingFXUtils.toFXImage(selectWebcam.getImage(), null);
            snapshot_ImageView.setImage(image);
            snapshot_ImageView.setVisible(true);
            ComponentToolKit.setButtonDisable(showCamera_Button,false);
            ComponentToolKit.setButtonDisable(snapshot_Button,true);
            ComponentToolKit.setButtonDisable(uploadSnapshot_Button,false);
        }
    }
    @FXML protected void uploadSnapshotMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Image snapshotImage = snapshot_ImageView.getImage();
            if(snapshotImage != null){
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshotImage,null);
                if(rotateDegree%360 != 0){
                    bufferedImage = createRotateImage(bufferedImage, (int) rotateDegree);
                }
                bufferedImage = ToolKit.fixImagePickBackgroundColor(bufferedImage);
                String base64Format = generateBase64(bufferedImage);
                Task Task = SaveOrderPictureTask(base64Format);
                ProgressIndicator.progressProperty().bind(Task.progressProperty());
                new Thread(Task).start();
            }
        }
    }

    private BufferedImage createRotateImage(BufferedImage oldBufferedImage, int rotateDegree){
        try{
            double rads = Math.toRadians(rotateDegree);
            double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
            int w = oldBufferedImage.getWidth();
            int h = oldBufferedImage.getHeight();
            int newWidth = (int) Math.floor(w * cos + h * sin);
            int newHeight = (int) Math.floor(h * cos + w * sin);

            BufferedImage img = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - w) / 2, (newHeight - h) / 2);

            int x = w / 2;
            int y = h / 2;
            at.rotate(rads, x, y);
            g2d.setTransform(at);
            g2d.drawImage(oldBufferedImage, 0, 0, null);
            if(rotateDegree%270 == 0)
                g2d.drawRect(0, 0, newWidth, newHeight);
            else
                g2d.drawRect(0, 0, newWidth-1, newHeight-1);
            g2d.dispose();
            return fixImagePickBackgroundColor(img);
        }catch (Exception Ex){
            DialogUI.ExceptionDialog(Ex);
            ERPApplication.Logger.catching(Ex);
        }
        return null;
    }
    private Task SaveOrderPictureTask(String base64Format){
        return new Task() {
            @Override public Void call(){
                try{
                    ProgressIndicator.setVisible(true);
                    uploadSnapshot_Button.setDisable(true);
                    showCamera_Button.setDisable(true);
                    if(SnapshotPictureLocation == ToolKit_Enum.SnapshotPictureLocation.貨單){
                        if(EstablishOrder_Controller.saveSnapshotOrUploadPicture(base64Format)) {
                            EstablishOrder_Controller.setShowOrderPictureButtonDisable(false);
                            Platform.runLater(() -> DialogUI.MessageDialog("※ 圖片上傳成功"));
                        }else
                            Platform.runLater(() -> DialogUI.MessageDialog("※ 圖片上傳失敗"));
                    }else if(SnapshotPictureLocation == ToolKit_Enum.SnapshotPictureLocation.審查紀錄){
                        if(ShowEditOrderReviewStatusReason_Controller.saveSnapshotPicture(base64Format))
                            Platform.runLater(() -> DialogUI.MessageDialog("※ 圖片上傳成功"));
                        else
                            Platform.runLater(() -> DialogUI.MessageDialog("※ 圖片上傳失敗"));
                    }
                    ProgressIndicator.setVisible(false);
                    if(IpCamTab.isSelected()){
                        uploadSnapshot_Button.setDisable(false);
                    }else if(WebCamTab.isSelected()){
                        uploadSnapshot_Button.setDisable(false);
                        showCamera_Button.setDisable(false);
                    }
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
                return null;
            }
        };
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            closeIpCamera();
            closeWebCamera();
            Stage.close();
        }
    }
    private void closeIpCamera() {
        if (selectIpCam != null) {
            selectIpCam.release();
            selectIpCam = null;
        }
        if(timer != null){
            timer.shutdown();
            timer = null;
        }
    }
    private void closeWebCamera() {
        if (selectWebcam != null) {
            selectWebcam.close();
        }
    }
    private void updateSystemDefaultConfig(){
        if(!SystemSetting_Model.updateSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.Order_SnapshotCamera.name(),Order_SnapshotCamera.ordinal())){
            DialogUI.AlarmDialog("※ 【預設值】更新失敗");
        }
    }
}
