package ERP.Controller.Toolkit.ShowPictureEditor;

import ERP.Bean.Order.OrderReviewStatusPicture_Bean;
import ERP.Controller.Toolkit.ShowEditOrderReviewStatusReason.ShowEditOrderReviewStatusReason_Controller;
import ERP.Controller.Toolkit.ShowOrderReviewStatusOverview.ShowOrderReviewStatusOverview_Controller;
import ERP.ERPApplication;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.*;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class ShowPictureEditor_Controller {
    @FXML ImageView PictureImageView;
    @FXML Canvas PictureCanvas;
    @FXML Button saveDrawing_Button;
    @FXML Label tooltip_Label;
    @FXML Tooltip tooltip;

    private ToolKit ToolKit;
    private KeyPressed KeyPressed;
    private CallConfig CallConfig;
    private CallJAR CallJAR;
    private ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.Tooltip Tooltip;
    private Order_Model Order_Model;

    private Stage Stage;
    private ShowOrderReviewStatusOverview_Controller ShowOrderReviewStatusOverview_Controller;
    private ShowEditOrderReviewStatusReason_Controller ShowEditOrderReviewStatusReason_Controller;
    private OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean;

    private GraphicsContext gc;
    private ArrayList<ArrayList<double[]>> drawPathList = new ArrayList<>();
    public ShowPictureEditor_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        Tooltip = ToolKit.Tooltip;
        CallJAR = ToolKit.CallJAR;
        CallConfig = ToolKit.CallConfig;

        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setSourceObject(Object sourceObject){
        if(sourceObject instanceof ShowOrderReviewStatusOverview_Controller){
            this.ShowOrderReviewStatusOverview_Controller = (ShowOrderReviewStatusOverview_Controller)sourceObject;
        }else if(sourceObject instanceof ShowEditOrderReviewStatusReason_Controller){
            this.ShowEditOrderReviewStatusReason_Controller = (ShowEditOrderReviewStatusReason_Controller) sourceObject;
        }
    }
    public void setOrderReviewStatusPicture_Bean(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean){
        this.orderReviewStatusPicture_Bean = orderReviewStatusPicture_Bean;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.showPictureEditor());

        gc = PictureCanvas.getGraphicsContext2D();
        initDraw();

        loadImagePane();
    }
    private void initDraw(){
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
    }
    private void loadImagePane() {
        Image image = ToolKit.decodeBase64ToImage(orderReviewStatusPicture_Bean.getBase64());
        PictureCanvas.setHeight(image.getHeight());
        PictureCanvas.setWidth(image.getWidth());
        PictureImageView.setImage(image);
    }
    @FXML protected void PictureCanvasMousePressed(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
//            System.out.println("left pressed:" + MouseEvent.getX() + " - " + MouseEvent.getY());
            gc.beginPath();
            gc.moveTo(MouseEvent.getX(), MouseEvent.getY());
            gc.stroke();

            double[] position = new double[]{ MouseEvent.getX(), MouseEvent.getY()};
            drawPathList.add(new ArrayList<double[]>(){{add(position);}});

            ComponentToolKit.setButtonDisable(saveDrawing_Button,false);
        }else if(KeyPressed.isMouseRightDoubleClicked(MouseEvent)){
//            System.out.println("right pressed:" + MouseEvent.getX() + " - " + MouseEvent.getY());
            double positionX = MouseEvent.getX(), positionY = MouseEvent.getY();
            for(int index = 0 ; index < drawPathList.size() ; index++){
                ArrayList<double[]> drawList = drawPathList.get(index);
                for(double[] recordPosition : drawList){
                    if((Math.abs(recordPosition[0]-positionX) <= 4 && Math.abs(recordPosition[1]-positionY) <= 4)){
//                        System.out.println("第幾個圖：" + (index+1) + "  " + recordPosition[0] + "-" + recordPosition[1]);
                        removeAndReDraw(index);
                        //  移除這組圖
                        break;
                    }
                }
            }
        }
    }
    private void removeAndReDraw(int removeDrawIndex){
        drawPathList.remove(removeDrawIndex);
        gc.clearRect(0,0,PictureCanvas.getWidth(),PictureCanvas.getHeight());
        for (ArrayList<double[]> drawList : drawPathList) {
            for (int j = 0; j < drawList.size(); j++) {
                double[] recordPosition = drawList.get(j);
                if (j == 0) {
                    gc.beginPath();
                    gc.moveTo(recordPosition[0], recordPosition[1]);
                } else {
                    gc.lineTo(recordPosition[0], recordPosition[1]);
                }
                gc.stroke();
            }
        }
    }
    @FXML protected void PictureCanvasMouseDragged(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
//            System.out.println("left pressed:" + MouseEvent.getX() + " - " + MouseEvent.getY());
            gc.lineTo(MouseEvent.getX(), MouseEvent.getY());
            gc.stroke();

            double[] position = new double[]{ MouseEvent.getX(), MouseEvent.getY()};
            drawPathList.get(drawPathList.size()-1).add(position);
        }else if(KeyPressed.isMouseRightClicked(MouseEvent)){
            double mouse_X = MouseEvent.getX(), mouse_Y = MouseEvent.getY();
            gc.clearRect(mouse_X,mouse_Y,8, 8);
        }
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrinterPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                String PicturePath = CallConfig.getFile_OutputPath() + "\\列印.jpg";
                BufferedImage newImage = generateNewImage();
                String newImageBase64 = ToolKit.generateBase64(newImage);
                byte[] ImageByte = ToolKit.decodeBase64ToByteArray(newImageBase64);
//                byte[] ImageByte = Base64.decodeBase64(orderReviewStatusPicture_Bean.getBase64());
                OutputStream out = new FileOutputStream(PicturePath);
                out.write(ImageByte);
                out.flush();
                out.close();
                if (CallJAR.CallPicture(PicturePath)) {
                    if (DialogUI.AlarmDialog("※ 按下確定刪除 「 列印.jpg 」"))
                        ToolKit.deleteExportQuotationPicture(PicturePath);
                } else DialogUI.MessageDialog("※ 開啟失敗!");
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    /** Button Mouse Clicked - 下載 */
    @FXML protected void DownloadPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try {
                String PicturePath = CallConfig.getFile_OutputPath() + "\\圖片下載.jpg";
                BufferedImage newImage = generateNewImage();
                String newImageBase64 = ToolKit.generateBase64(newImage);
                byte[] ImageByte = ToolKit.decodeBase64ToByteArray(newImageBase64);
//                byte[] ImageByte = ToolKit.decodeBase64ToByteArray(orderReviewStatusPicture_Bean.getBase64());
                OutputStream out = new FileOutputStream(PicturePath);
                out.write(ImageByte);
                out.flush();
                out.close();
                DialogUI.MessageDialog("※ 下載成功");
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                DialogUI.MessageDialog("※ 下載失敗");
            }
        }
    }
    @FXML protected void SavePictureMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            boolean isKeepOriginPicture = ShowEditOrderReviewStatusReason_Controller != null ||
                    DialogUI.ConfirmDialog("是否保留原圖 ?", true,false,0,0);
            BufferedImage newImage = generateNewImage();
            String newImageBase64 = ToolKit.generateBase64(newImage);
            if(isKeepOriginPicture){    //  新增照片
                if(ShowEditOrderReviewStatusReason_Controller != null) {    //  建立審查時
                    ShowEditOrderReviewStatusReason_Controller.recordEditPicture(newImageBase64);
                    DialogUI.MessageDialog("※ 儲存成功");
                    ComponentToolKit.closeThisStage(this.Stage);
                }else if(ShowOrderReviewStatusOverview_Controller != null){ //  檢視審查時
                    Integer pictureID = Order_Model.insertOrderReviewStatusPicture(orderReviewStatusPicture_Bean.getReviewStatus_record_id(), newImageBase64);
                    if(pictureID != null){
                        DialogUI.MessageDialog("※ 儲存成功");

                        OrderReviewStatusPicture_Bean newOrderReviewStatusPicture_Bean = new OrderReviewStatusPicture_Bean();
                        newOrderReviewStatusPicture_Bean.setId(pictureID);
                        newOrderReviewStatusPicture_Bean.setReviewStatus_record_id(orderReviewStatusPicture_Bean.getReviewStatus_record_id());
                        newOrderReviewStatusPicture_Bean.setBase64(newImageBase64);

                        ShowOrderReviewStatusOverview_Controller.insertPictureStackPane(orderReviewStatusPicture_Bean, newOrderReviewStatusPicture_Bean);
                        ComponentToolKit.closeThisStage(this.Stage);
                    }else{
                        DialogUI.MessageDialog("※ 儲存失敗");
                    }
                }
            }else{
                if(Order_Model.updateOrderReviewStatusPicture(orderReviewStatusPicture_Bean.getId(), orderReviewStatusPicture_Bean.getReviewStatus_record_id(), newImageBase64)){
                    DialogUI.MessageDialog("※ 儲存成功");
                    orderReviewStatusPicture_Bean.setBase64(newImageBase64);
                    ShowOrderReviewStatusOverview_Controller.refreshPictureStackPane(orderReviewStatusPicture_Bean);
                    ComponentToolKit.closeThisStage(this.Stage);
                }else{
                    DialogUI.MessageDialog("※ 儲存失敗");
                }
            }
        }
    }
    private BufferedImage generateNewImage(){
        WritableImage wi = new WritableImage((int)PictureCanvas.getWidth(),(int)PictureCanvas.getHeight());
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        BufferedImage canvasImage = SwingFXUtils.fromFXImage(PictureCanvas.snapshot(sp, wi), null);
        BufferedImage image = ToolKit.decodeBase64ToBufferedImage(orderReviewStatusPicture_Bean.getBase64());
        canvasImage = resizeImage(canvasImage, image.getWidth(), image.getHeight());
        return combineImage(image, canvasImage);
    }
    private BufferedImage combineImage(BufferedImage image1, BufferedImage image2) {
        Graphics2D g = image1.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));		// 更改透明參數
        g.drawImage(image2, 0, 0, null);
        return ToolKit.fixImagePickBackgroundColor(image1);
    }
    private BufferedImage resizeImage(BufferedImage resizeImage, int width, int height){
        BufferedImage newImage = new BufferedImage(width,height,resizeImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(resizeImage, 0,0,width,height,null);
        g.dispose();
        return newImage;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeletePictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(ShowEditOrderReviewStatusReason_Controller != null) {
                if(DialogUI.ConfirmDialog("【是】刪除所有照片(含原照)\n【否】刪除所有已編輯照片",true,false,0,0))
                    ShowEditOrderReviewStatusReason_Controller.deleteEditPicture(true);
                else
                    ShowEditOrderReviewStatusReason_Controller.deleteEditPicture(false);
                DialogUI.MessageDialog("※ 刪除成功");
                ComponentToolKit.closeThisStage(this.Stage);
            }else if(ShowOrderReviewStatusOverview_Controller != null){
                if(!DialogUI.ConfirmDialog("確定刪除該影像 ?",true,false,0,0))
                    return;
                if(Order_Model.deleteOrderReviewStatusPicture(orderReviewStatusPicture_Bean.getId(),orderReviewStatusPicture_Bean.getReviewStatus_record_id())){
                    DialogUI.MessageDialog("※ 刪除成功");
                    ShowOrderReviewStatusOverview_Controller.deletePictureStackPane(orderReviewStatusPicture_Bean);
                    ComponentToolKit.closeThisStage(this.Stage);
                }else
                    DialogUI.MessageDialog("※ 刪除失敗");
            }
        }
    }
    @FXML
    protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            ComponentToolKit.closeThisStage(this.Stage);
    }
}
