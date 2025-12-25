package ERP.Controller.Toolkit.ShowHTMLEditor;

import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.ProductWaitConfirm.WaitConfirmProductInfo_Bean;
import ERP.Controller.ManageProductInfo.ManageProductInfo.ManageProductInfo_Controller;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.KeyPressed;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class ShowHTMLEditor_Controller {

    @FXML private HTMLEditor HTMLEditor;
    private KeyPressed KeyPressed;
    private ManageProductInfo_Controller ManageProductInfo_Controller;
    private ProductInfo_Bean ProductInfo_Bean;
    private WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean;

    public ShowHTMLEditor_Controller(){
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
    }
    private Stage Stage;
    public void setManageProductInfo_Controller(ManageProductInfo_Controller ManageProductInfo_Controller){
        this.ManageProductInfo_Controller = ManageProductInfo_Controller;
    }
    /** Set object - [Bean] ProductInfo_Bean */
    public void setProductInfo_Bean(ProductInfo_Bean ProductInfo_Bean){ this.ProductInfo_Bean = ProductInfo_Bean;   }
    public void setWaitConfirmProductInfo_Bean(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        this.WaitConfirmProductInfo_Bean = WaitConfirmProductInfo_Bean;
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set HTML Text - the HTML format of product's describe */
    public void setHTMLText(){
        if(ProductInfo_Bean != null && WaitConfirmProductInfo_Bean == null)
            HTMLEditor.setHtmlText(ProductInfo_Bean.getDescribe());
        else if(ProductInfo_Bean == null && WaitConfirmProductInfo_Bean != null)
            HTMLEditor.setHtmlText(WaitConfirmProductInfo_Bean.getDescribe());
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void saveMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ProductInfo_Bean != null && WaitConfirmProductInfo_Bean == null) {
                ProductInfo_Bean.setDescribe(HTMLEditor.getHtmlText());
                if(ManageProductInfo_Controller.ManageProductStatus == Product_Enum.ManageProductStatus.搜尋)
                    ManageProductInfo_Controller.SaveProduct();
            }else if(ProductInfo_Bean == null && WaitConfirmProductInfo_Bean != null)
                WaitConfirmProductInfo_Bean.setDescribe(HTMLEditor.getHtmlText());
            Stage.close();
        }
    }
    /** Button Mouse Clicked - 離開 */
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))  Stage.close();
    }
}
