package ERP.Controller.Toolkit.ShowEditOrderRemark;

import ERP.Bean.Order.Order_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderExist;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ShowEditOrderRemark_Controller {
    @FXML TextArea EditInfoText;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private Stage Stage;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private OrderExist OrderExist;
    private Order_Bean Order_Bean;

    private boolean isOrderRemark,isCashierRemark;
    public ShowEditOrderRemark_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){  this.EstablishOrder_Controller = EstablishOrder_Controller;  }
    public void setOrderExist(Order_Enum.OrderExist OrderExist){    this.OrderExist = OrderExist;   }
    public void setOrder_Bean(Order_Bean Order_Bean){    this.Order_Bean = Order_Bean; }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setRemarkStatus(boolean isOrderRemark, boolean isCashierRemark){
        this.isOrderRemark = isOrderRemark;
        this.isCashierRemark = isCashierRemark;
    }
    /** Set describe and remark of product */
    public void setEditInfo(){
        if(isOrderRemark && !isCashierRemark){
            if(Order_Bean.getOrderRemark() == null)
                Order_Bean.setOrderRemark("");
            EditInfoText.setText(Order_Bean.getOrderRemark());
        }else if(!isOrderRemark && isCashierRemark){
            if(Order_Bean.getCashierRemark() == null)
                Order_Bean.setCashierRemark("");
            EditInfoText.setText(Order_Bean.getCashierRemark());
        }
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void SaveEditInfoMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(OrderExist == Order_Enum.OrderExist.已存在){
                if((isOrderRemark && !isCashierRemark) && !EditInfoText.getText().equals(Order_Bean.getOrderRemark())){
                    if(DialogUI.AlarmDialog("※ 【貨單備註】已變更，請記得「修改貨單」!")){
                        Order_Bean.setOrderRemark(EditInfoText.getText());
                        EstablishOrder_Controller.setModifyOrderButtonStyle();
                    }
                }else if((!isOrderRemark && isCashierRemark) && !EditInfoText.getText().equals(Order_Bean.getCashierRemark())) {
                    if (DialogUI.AlarmDialog("※ 【出納備註】已變更，請記得「修改貨單」!")) {
                        Order_Bean.setCashierRemark(EditInfoText.getText());
                        EstablishOrder_Controller.setModifyOrderButtonStyle();
                    }
                }
            }else if(OrderExist == Order_Enum.OrderExist.未存在) {
                if(isOrderRemark && !isCashierRemark)
                    Order_Bean.setOrderRemark(EditInfoText.getText());
                else if(!isOrderRemark && isCashierRemark)
                    Order_Bean.setCashierRemark(EditInfoText.getText());
            }
            ComponentToolKit.closeThisStage(Stage);
        }
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    ComponentToolKit.closeThisStage(Stage);
    }
}
