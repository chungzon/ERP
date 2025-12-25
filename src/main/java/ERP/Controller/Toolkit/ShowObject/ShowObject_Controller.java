package ERP.Controller.Toolkit.ShowObject;

import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.Controller.SystemSetting.SystemSetting_Controller;
import ERP.Controller.Toolkit.ShowBigGoPriceSetting.ShowBigGoPriceSetting_Controller;
import ERP.Controller.Toolkit.ShowSeparatePurchaseManufacturer.ShowSeparatePurchaseManufacturer_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.ShowObjectSource;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Controller.ManagePayableReceivable.EstablishPayableReceivable.EstablishPayableReceivable_Controller;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Show object */
public class ShowObject_Controller {

    @FXML Button objectCanGet_Button;
    @FXML TableColumn<ObjectInfo_Bean,String> ObjectIDColumn, ObjectNameColumn, ContactPersonColumn, TelephoneColumn, TaxIDNumberColumn, CompanyAddressColumn, ReceivableDiscountColumn, PrintPricingColumn, SaleModelColumn;
    @FXML TableView<ObjectInfo_Bean> TableView;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private OrderObject OrderObject;
    private boolean objectCanGet;
    private Object controllerSource;
    private ShowObjectSource ShowObjectSource;
    private Stage Stage;
    public ShowObject_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
    }
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    public void setObjectCanGet(boolean objectCanGet){  this.objectCanGet = objectCanGet;   }
    public void setControllerSource(Object controllerSource){   this.controllerSource = controllerSource;   }
    public void setShowObjectSource(ShowObjectSource ShowObjectSource){  this.ShowObjectSource = ShowObjectSource;   }
    public void setTableViewItems(ObservableList<ObjectInfo_Bean> ObjectList){   TableView.getItems().addAll(ObjectList);  }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setComponent(){
        InitialTableView();
        if(OrderObject == Order_Enum.OrderObject.廠商) {
            ComponentToolKit.setTableColumnVisible(ReceivableDiscountColumn, false);
            ComponentToolKit.setTableColumnVisible(PrintPricingColumn, false);
            ComponentToolKit.setTableColumnVisible(SaleModelColumn, false);
        }
        ComponentToolKit.setButtonDisable(objectCanGet_Button, !objectCanGet);
    }
    private void InitialTableView(){
        ComponentToolKit.setColumnCellValue(ObjectIDColumn,"ObjectID","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(ObjectNameColumn,"ObjectName","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(ContactPersonColumn,"ContactPerson","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(TelephoneColumn,"Telephone1","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(TaxIDNumberColumn,"TaxIDNumber","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(CompanyAddressColumn,"CompanyAddress","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(ReceivableDiscountColumn,"ReceivableDiscount","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(PrintPricingColumn,"PrintPricing","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(SaleModelColumn,"SaleModel","CENTER-LEFT","14",null);
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(objectCanGet){
            ObjectInfo_Bean ObjectInfo_Bean = ComponentToolKit.getObjectTableViewSelectItem(TableView);
            if (MouseEvent.getClickCount() == 2 && ObjectInfo_Bean != null) {
                if(ShowObjectSource == Order_Enum.ShowObjectSource.建立貨單)    setEstablishOrderInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.採購或報價單互轉)   setTransferQuotationObjectInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.待出貨庫存不足轉採購) setShowSeparatePurchaseManufacturerInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.建立應收應付帳款)   setEstablishPayableReceivableInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.出納帳戶)   setSystemSettingInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.待確認_Store參數)   setWaitConfirmManufacturerInfo(ObjectInfo_Bean);
                ComponentToolKit.closeThisStage(Stage);
            }
        }
    }
    /** TableView Key Released - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && objectCanGet){
            ObjectInfo_Bean ObjectInfo_Bean = ComponentToolKit.getObjectTableViewSelectItem(TableView);
            if(ObjectInfo_Bean != null) {
                if(ShowObjectSource == Order_Enum.ShowObjectSource.建立貨單)    setEstablishOrderInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.採購或報價單互轉)   setTransferQuotationObjectInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.待出貨庫存不足轉採購) setShowSeparatePurchaseManufacturerInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.建立應收應付帳款)   setEstablishPayableReceivableInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.出納帳戶)   setSystemSettingInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.待確認_Store參數)   setWaitConfirmManufacturerInfo(ObjectInfo_Bean);
                ComponentToolKit.closeThisStage(Stage);
            }
        }
    }
    /** Button Mouse Clicked - 取得 */
    @FXML protected void GetObjectMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObjectInfo_Bean ObjectInfo_Bean = TableView.getSelectionModel().getSelectedItem();
            if(ObjectInfo_Bean != null) {
                if(ShowObjectSource == Order_Enum.ShowObjectSource.建立貨單)    setEstablishOrderInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.採購或報價單互轉)   setTransferQuotationObjectInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.待出貨庫存不足轉採購) setShowSeparatePurchaseManufacturerInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.建立應收應付帳款)   setEstablishPayableReceivableInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.出納帳戶)   setSystemSettingInfo(ObjectInfo_Bean);
                else if(ShowObjectSource == Order_Enum.ShowObjectSource.待確認_Store參數)   setWaitConfirmManufacturerInfo(ObjectInfo_Bean);
                ComponentToolKit.closeThisStage(Stage);
            }else DialogUI.MessageDialog("※ 請選擇對象");
        }
    }
    private void setEstablishOrderInfo(ObjectInfo_Bean ObjectInfo_Bean){
        EstablishOrder_Controller EstablishOrder_Controller = (EstablishOrder_Controller) controllerSource;
        EstablishOrder_Controller.setOrderObjectInfo(ObjectInfo_Bean);
    }
    private void setTransferQuotationObjectInfo(ObjectInfo_Bean ObjectInfo_Bean){
        EstablishOrder_Controller EstablishOrder_Controller = (EstablishOrder_Controller) controllerSource;
        EstablishOrder_Controller.setTransferQuotationObjectInfo(ObjectInfo_Bean);
    }
    private void setShowSeparatePurchaseManufacturerInfo(ObjectInfo_Bean ObjectInfo_Bean){
        ShowSeparatePurchaseManufacturer_Controller ShowSeparatePurchaseManufacturer_Controller = (ShowSeparatePurchaseManufacturer_Controller) controllerSource;
        ShowSeparatePurchaseManufacturer_Controller.setManufacturerInfo(ObjectInfo_Bean);
    }
    private void setEstablishPayableReceivableInfo(ObjectInfo_Bean ObjectInfo_Bean){
        EstablishPayableReceivable_Controller EstablishPayableReceivable_Controller = (EstablishPayableReceivable_Controller) controllerSource;
        EstablishPayableReceivable_Controller.initialComponent();
        if(OrderObject == Order_Enum.OrderObject.廠商)    EstablishPayableReceivable_Controller.setManufacturerInfo(ObjectInfo_Bean);
        else if(OrderObject == Order_Enum.OrderObject.客戶)   EstablishPayableReceivable_Controller.setCustomerInfo(ObjectInfo_Bean);
    }
    private void setSystemSettingInfo(ObjectInfo_Bean ObjectInfo_Bean){
        SystemSetting_Controller SystemSetting_Controller = (SystemSetting_Controller) controllerSource;
        SystemSetting_Controller.setIAECrawlerObjectInfo(ObjectInfo_Bean);
    }
    private void setWaitConfirmManufacturerInfo(ObjectInfo_Bean ObjectInfo_Bean){
        ShowBigGoPriceSetting_Controller ShowBigGoPriceSetting_Controller = (ShowBigGoPriceSetting_Controller) controllerSource;
        ShowBigGoPriceSetting_Controller.setManufacturerInfo(ObjectInfo_Bean);
    }
    /** Button Mouse Clicked - 取得 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){  if(KeyPressed.isMouseLeftClicked(MouseEvent))   ComponentToolKit.closeThisStage(Stage);  }
}
