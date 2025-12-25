package ERP.Controller.Toolkit.ShowEditProductDescribeOrRemark;

import ERP.Bean.ProductWaitConfirm.WaitConfirmProductInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Model.ProductWaitingConfirm.ProductWaitingConfirm_Model;
import ERP.Enum.Product.Product_Enum.EditProductDescribeOrRemark;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Show edit product describe or remark */
public class ShowEditProductDescribeOrRemark_Controller {
    @FXML TextArea EditInfoText;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private Stage Stage;
    private EditProductDescribeOrRemark EditProductDescribeOrRemark;
    private WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    public ShowEditProductDescribeOrRemark_Controller(){
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        ProductWaitingConfirm_Model = ERPApplication.ToolKit.ModelToolKit.getProductWaitingConfirmModel();
    }
    /** Set object - [Enum] Product_Enum.EditProductDescribeOrRemark */
    public void setEditProductDescribeOrRemark(EditProductDescribeOrRemark EditProductDescribeOrRemark){    this.EditProductDescribeOrRemark = EditProductDescribeOrRemark; }
    /** Set object - [Bean] WaitConfirmProductInfo_Bean */
    public void setWaitConfirmProductInfo_Bean(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){    this.WaitConfirmProductInfo_Bean = WaitConfirmProductInfo_Bean; }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set describe and remark of product */
    public void setEditInfo(){
        if(EditProductDescribeOrRemark == Product_Enum.EditProductDescribeOrRemark.編輯商品描述)   EditInfoText.setText(WaitConfirmProductInfo_Bean.getDescribe());
        else if(EditProductDescribeOrRemark == Product_Enum.EditProductDescribeOrRemark.編輯商品備註)   EditInfoText.setText(WaitConfirmProductInfo_Bean.getRemark());
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void SaveEditInfoMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ProductWaitingConfirm_Model.saveEditProductDescribeOrRemark(EditProductDescribeOrRemark, EditInfoText.getText(), WaitConfirmProductInfo_Bean)) {
                if(EditProductDescribeOrRemark == Product_Enum.EditProductDescribeOrRemark.編輯商品描述)   WaitConfirmProductInfo_Bean.setDescribe(EditInfoText.getText());
                else if(EditProductDescribeOrRemark == Product_Enum.EditProductDescribeOrRemark.編輯商品備註)   WaitConfirmProductInfo_Bean.setRemark(EditInfoText.getText());
                DialogUI.MessageDialog("※ 儲存成功 !");
                ComponentToolKit.closeThisStage(Stage);
            }else   DialogUI.MessageDialog("※ 儲存失敗 !");
        }
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    ComponentToolKit.closeThisStage(Stage);
    }
}
