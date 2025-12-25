package ERP.Controller.ProductWaitingConfirm.SingleProductUpdate;

import ERP.Bean.ProductWaitConfirm.WaitConfirmProductInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate.*;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Single product update */
public class SingleProductUpdate_Controller {

    private XanderGetWebInfo_Model XanderGetWebInfo_Model;
    private KtnetGetWebInfo_Model KtnetGetWebInfo_Model;
    private Genb2bGetWebInfo_Model Genb2bGetWebInfo_Model;
    private SynnexGetWebInfo_Model SynnexGetWebInfo_Model;
    private UnitechGetWebInfo_Model UnitechGetWebInfo_Model;
    private JinghaoGetWebInfo_Model JinghaoGetWebInfo_Model;
    private WeblinkGetWebInfo_Model WeblinkGetWebInfo_Model;
    private SenaoGetWebInfo_Model SenaoGetWebInfo_Model;
    private WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean;

    @FXML private Button UpdateButton;
    @FXML private Label OldProductName, OldProductBatchPrice, OldProductPricing, OldProductSupplyStatus;
    @FXML private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    @FXML private Label UpdateStatus;
    @FXML private TextField Vendor, ProductCode;
    @FXML private TextArea OldProductDescribe, OldProductRemark;
    @FXML private TextArea NewProductDescribe, NewProductRemark;
    @FXML private CheckBox ProductNameUpdate_CheckBox, PricingUpdate_CheckBox;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private SingleProductUpdate_Model SingleProductUpdate_Model;
    private Stage Stage;
    public SingleProductUpdate_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
    }
    /** Set object - [Model] SingleProductUpdate_Model */
    public void setSingleProductUpdate_Model(Stage MainStage){
        SingleProductUpdate_Model = ToolKit.ModelToolKit.getSingleProductUpdateModel(MainStage);

        XanderGetWebInfo_Model = ToolKit.ModelToolKit.getXanderGetWebInfoModel();
        KtnetGetWebInfo_Model = ToolKit.ModelToolKit.getKtnetGetWebInfoModel();
        Genb2bGetWebInfo_Model = ToolKit.ModelToolKit.getGenb2bGetWebInfoModel();
        SynnexGetWebInfo_Model = ToolKit.ModelToolKit.getSynnexGetWebInfoModel();
        UnitechGetWebInfo_Model = ToolKit.ModelToolKit.getUnitechGetWebInfoModel();
        JinghaoGetWebInfo_Model = ToolKit.ModelToolKit.getJinghaoGetWebInfo_Model();
        WeblinkGetWebInfo_Model = ToolKit.ModelToolKit.getWeblinkGetWebInfoModel();
        SenaoGetWebInfo_Model = ToolKit.ModelToolKit.getSenaoGetWebInfoModel();


        SingleProductUpdate_Model.setNewLabel(UpdateButton,NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe, NewProductRemark,UpdateStatus);

        XanderGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe, NewProductRemark);
        KtnetGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe);
        Genb2bGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe);
        SynnexGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe);
        UnitechGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe, NewProductRemark);
        JinghaoGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe, NewProductRemark);
        WeblinkGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe);
        SenaoGetWebInfo_Model.setNewLabel(NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus, NewProductDescribe, NewProductRemark);
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set object - [Bean] WaitConfirmProductInfo_Bean */
    public void setWaitConfirmProductInfo_Bean(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        this.WaitConfirmProductInfo_Bean = WaitConfirmProductInfo_Bean;
        InitialComponent();
        Vendor.setText(WaitConfirmProductInfo_Bean.getVendor());
        ProductCode.setText(WaitConfirmProductInfo_Bean.getProductCode());
        OldProductName.setText(WaitConfirmProductInfo_Bean.getProductName());
        OldProductBatchPrice.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getBatchPrice()));
        OldProductPricing.setText(ToolKit.RoundingString(true,WaitConfirmProductInfo_Bean.getPricing()));
        OldProductSupplyStatus.setText(WaitConfirmProductInfo_Bean.getSupplyStatus());
        OldProductDescribe.setText(WaitConfirmProductInfo_Bean.getDescribe());
        OldProductRemark.setText(WaitConfirmProductInfo_Bean.getRemark());
        Stage.show();
    }
    private void InitialComponent(){
        NewProductName.setText("");
        NewProductBatchPrice.setText("");
        NewProductPricing.setText("");
        NewProductSupplyStatus.setText("");
        NewProductDescribe.setText("");
        NewProductRemark.setText("");
        UpdateStatus.setText("");
        UpdateButton.setDisable(true);
    }
    /** Button Mouse Clicked - 執行 */
    @FXML protected void SearchUrlMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            InitialComponent();
            if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.建達國際.getVendorCode())
                SingleProductUpdate_Model.Xander(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.廣鐸企業.getVendorCode())
                SingleProductUpdate_Model.Ktnet(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.Genuine捷元.getVendorCode())
                SingleProductUpdate_Model.Genb2b(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.精技電腦.getVendorCode())
                SingleProductUpdate_Model.Unitech(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.精豪電腦.getVendorCode())
                SingleProductUpdate_Model.Jinghao(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.聯強國際.getVendorCode())
                SingleProductUpdate_Model.Synnex(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.展碁國際.getVendorCode())
                SingleProductUpdate_Model.Weblink(WaitConfirmProductInfo_Bean);
            else if(WaitConfirmProductInfo_Bean.getVendorCode() == Product_Enum.Vendor.神腦國際.getVendorCode()) {
                SingleProductUpdate_Model.Senao(WaitConfirmProductInfo_Bean);
            }
        }
    }
    /** Button Mouse Clicked - 更新 */
    @FXML protected void UpdateProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if (ProductNameUpdate_CheckBox.isSelected())
                WaitConfirmProductInfo_Bean.setProductName(NewProductName.getText());
            WaitConfirmProductInfo_Bean.setBatchPrice(ToolKit.RoundingDouble(Double.parseDouble(NewProductBatchPrice.getText())));
            if (PricingUpdate_CheckBox.isSelected())
                WaitConfirmProductInfo_Bean.setPricing(ToolKit.RoundingDouble(Double.parseDouble(NewProductPricing.getText())));
            WaitConfirmProductInfo_Bean.setSupplyStatus(NewProductSupplyStatus.getText());
            WaitConfirmProductInfo_Bean.setDescribe(NewProductDescribe.getText());
            WaitConfirmProductInfo_Bean.setRemark(NewProductRemark.getText());
            if (WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品)
                WaitConfirmProductInfo_Bean.setUpdateDate(ToolKit.getToday("yyyy-MM-dd"));
            if(SingleProductUpdate_Model.UpdateProduct(WaitConfirmProductInfo_Bean)){
                InitialComponent();
                DialogUI.MessageDialog("更新完成");
                ProductNameUpdate_CheckBox.setSelected(true);
                PricingUpdate_CheckBox.setSelected(true);
                ComponentToolKit.closeThisStage(Stage);
            }else{
                DialogUI.MessageDialog("更新失敗");
            }
        }
    }
    /** Button Mouse Clicked - 離開  */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){ if(KeyPressed.isMouseLeftClicked(MouseEvent))    ComponentToolKit.closeThisStage(Stage);  }
}
