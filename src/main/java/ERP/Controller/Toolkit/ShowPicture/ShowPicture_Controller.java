package ERP.Controller.Toolkit.ShowPicture;

import ERP.Bean.Order.Order_Bean;
import ERP.Controller.ManageProductInfo.ManageProductInfo.ManageProductInfo_Controller;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.ERPApplication;
import ERP.Model.Order.Order_Model;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/** [Controller] Show picture image */
public class ShowPicture_Controller {
    @FXML BorderPane BorderPane;
    private Stage Stage;
    @FXML ImageView PictureImageView;
    @FXML Button PrinterPictureButton, PreviousPictureButton, NextPictureButton, DeletePictureButton;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallConfig CallConfig;
    private ERP.ToolKit.CallJAR CallJAR;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;

    private ManageProductInfo_Controller ManageProductInfo_Controller;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private Order_Bean Order_Bean;
    private OrderSource OrderSource;
    private ArrayList<HashMap<Boolean,String>> OrderPictureList;
    private Order_Model Order_Model;
    public ShowPicture_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallJAR = ToolKit.CallJAR;
        CallConfig = ToolKit.CallConfig;
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setSourceObject(Object controller_object){
        if(controller_object instanceof EstablishOrder_Controller){
            setButtonDisable(false);
            ManageProductInfo_Controller = null;
            EstablishOrder_Controller = (EstablishOrder_Controller) controller_object;
        }else if(controller_object instanceof ManageProductInfo_Controller){
            setButtonDisable(true);
            ManageProductInfo_Controller = (ManageProductInfo_Controller) controller_object;
            EstablishOrder_Controller = null;
        }
    }
    /** Set print and delete button disable  */
    private void setButtonDisable(boolean Disable){
        ComponentToolKit.setButtonDisable(PrinterPictureButton, Disable);
        ComponentToolKit.setButtonDisable(DeletePictureButton, Disable);
    }
    /** Set order number  */
    public void setOrder_Bean(Order_Bean Order_Bean){ this.Order_Bean = Order_Bean; }
    /** Set object - [Enum] Order_Enum.OrderSource  */
    public void setOrderSource(OrderSource OrderSource){ this.OrderSource = OrderSource; }
    /** Set object - Picture list */
    public void setOrderPictureList(ArrayList<HashMap<Boolean,String>> OrderPictureList){   this.OrderPictureList = OrderPictureList;   }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){ this.Stage = Stage; }
    /** Refresh image pane and set image */
    public void refreshAndSetImage(){
        LoadImagePane(OrderPictureList.get(0).get(true));
        PreviousPictureButton.setDisable(true);
        if(OrderPictureList.size() == 0) {
            LoadImagePane("");
            NextPictureButton.setDisable(true);
        }else if(OrderPictureList.size() == 1)    NextPictureButton.setDisable(true);
        else    NextPictureButton.setDisable(false);
    }

    double imageWidth;
    double imageHeight;
    private void LoadImagePane(String Base64String) {
        Image image = ToolKit.decodeBase64ToImage(Base64String);
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        PictureImageView.setImage(image);
    }

    int frameFitWidth = 1350;
    final double scale = 5;
    @FXML protected void StackPaneOnScroll(ScrollEvent scrollEvent){
        double rate;
        if (scrollEvent.getDeltaY() > 0) {
            rate = 0.05;
        } else {
            rate = -0.05;
        }
        double newWidth = PictureImageView.getFitWidth() + imageWidth * rate;
        double newHeight = PictureImageView.getFitHeight() + imageHeight * rate;
        if (newWidth <= frameFitWidth || newWidth > scale * frameFitWidth) {
            return;
        }
        Point2D eventPoint = new Point2D(scrollEvent.getSceneX(), scrollEvent.getSceneY());
        ComponentToolKit.setStackPaneScroll(BorderPane, PictureImageView, scale, eventPoint, frameFitWidth, newHeight, newWidth);
    }

    /** Button Mouse Clicked - 下載 */
    @FXML protected void DownloadPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String Base64String = "";
            for (HashMap<Boolean, String> booleanStringHashMap : OrderPictureList) {
                if (booleanStringHashMap.containsKey(true)) {
                    Base64String = booleanStringHashMap.get(true);
                    break;
                }
            }
            try {
                String PicturePath = CallConfig.getFile_OutputPath() + "\\商品圖片下載.jpg";
                byte[] ImageByte = Base64.decodeBase64(Base64String);
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
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrinterPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String Base64String = "";
            for (HashMap<Boolean, String> booleanStringHashMap : OrderPictureList) {
                if (booleanStringHashMap.containsKey(true)) {
                    Base64String = booleanStringHashMap.get(true);
                    break;
                }
            }
            try {
                String PicturePath = CallConfig.getFile_OutputPath() + "\\列印.jpg";
                byte[] ImageByte = Base64.decodeBase64(Base64String);
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
    /** Button Mouse Clicked - 上一張 */
    @FXML protected void PreviousPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            NextPictureButton.setDisable(false);
            for(int Index = 0; Index < OrderPictureList.size(); Index++ ){
                if(OrderPictureList.get(Index).containsKey(true) && (Index-1) >= 0){
                    OrderPictureList.get(Index).put(false,OrderPictureList.get(Index).get(true));
                    OrderPictureList.get(Index).remove(true);
                    OrderPictureList.get(Index-1).put(true, OrderPictureList.get(Index-1).get(false));
                    OrderPictureList.get(Index-1).remove(false);
                    LoadImagePane(OrderPictureList.get(Index-1).get(true));
                    if((Index-1) == 0)  PreviousPictureButton.setDisable(true);
                }
            }
        }
    }
    /** Button Mouse Clicked - 下一張 */
    @FXML protected void NextPictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            PreviousPictureButton.setDisable(false);
            for (int Index = 0; Index < OrderPictureList.size(); Index++) {
                if (OrderPictureList.get(Index).containsKey(true) && (Index + 1) <= (OrderPictureList.size() - 1)) {
                    OrderPictureList.get(Index).put(false, OrderPictureList.get(Index).get(true));
                    OrderPictureList.get(Index).remove(true);
                    OrderPictureList.get(Index + 1).put(true, OrderPictureList.get(Index + 1).get(false));
                    OrderPictureList.get(Index + 1).remove(false);
                    LoadImagePane(OrderPictureList.get(Index + 1).get(true));
                    if ((Index + 1) == (OrderPictureList.size() - 1)) NextPictureButton.setDisable(true);
                    break;
                }
            }
        }
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeletePictureMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            for (int Index = 0; Index < OrderPictureList.size(); Index++) {
                if (OrderPictureList.get(Index).containsKey(true)) {
                    if (Order_Model.deleteOrderPicture(Order_Bean.getOrderID(), OrderSource, (Index+1))) {
                        DialogUI.MessageDialog("※ 刪除成功");
                        OrderPictureList.remove(Index);
                        if (OrderPictureList.size() == 0) {
                            DeletePictureButton.setDisable(true);
                            PrinterPictureButton.setDisable(true);
                            LoadImagePane("");
                            if(EstablishOrder_Controller != null){
                                EstablishOrder_Controller.setShowOrderPictureButtonDisable(true);
                            }
                            ComponentToolKit.closeThisStage(this.Stage);
                        } else {
                            OrderPictureList.get(0).put(true, OrderPictureList.get(0).get(false));
                            OrderPictureList.get(0).remove(false);
                            refreshAndSetImage();
                        }
                    } else DialogUI.MessageDialog("※ 刪除失敗");
                }
            }
        }
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){  if(KeyPressed.isMouseLeftClicked(MouseEvent))   ComponentToolKit.closeThisStage(this.Stage); }
}
