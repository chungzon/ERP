package ERP.ToolKit;

import ERP.Bean.ManagePayableReceivable.IAECrawlerData_Bean;
import ERP.Bean.ManagePayableReceivable.PayableReceivable_Bean;
import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.*;
import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Product.ManageProductOnShelf_Bean;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.ProductWaitConfirm.WaitConfirmProductInfo_Bean;
import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.Bean.SystemSetting.Version_Bean;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.Bean.ToolKit.Installment.Installment_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowExportQuotationRecord.ExportQuotationRecord_Bean;
import ERP.Controller.ProductWaitingConfirm.SingleProductUpdate.*;
import ERP.Controller.SystemMenu_Controller;
import ERP.Controller.ManagePayableReceivable.EstablishPayableReceivable.EstablishPayableReceivable_Controller;
import ERP.Controller.ManagePayableReceivable.ManageBankInfo.ManageBankInfo_Controller;
import ERP.Controller.ManagePayableReceivable.ManageCompanyAccount.ManageCompanyAccount_Controller;
import ERP.Controller.ManagePayableReceivable.PaymentCompareSystem.PaymentCompareSystem_Controller;
import ERP.Controller.ManagePayableReceivable.SearchPayableReceivable.SearchPayableReceivable_Controller;
import ERP.Controller.ManageProductInfo.ManageProductOnShelf.ManageProductOnShelf_Controller;
import ERP.Controller.ManagePurchaseSystem.ManageManufacturerInfo.ManageManufacturerInfo_Controller;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.Controller.ManageShipmentSystem.ManageCustomerInfo.ManageCustomerInfo_Controller;
import ERP.Controller.ManageProductInfo.ManageProductCategory.ManageProductCategory_Controller;
import ERP.Controller.ManageProductInfo.ManageProductInfo.ManageProductInfo_Controller;
import ERP.Controller.Order.SearchNonePayReceive.SearchNonePayReceive_Controller;
import ERP.Controller.Order.SearchOrder.SearchOrder_Controller;
import ERP.Controller.Order.SearchOrderProgress.SearchOrderProgress_Controller;
import ERP.Controller.ProductWaitingConfirm.ProductWaitingConfirm.ProductWaitingConfirm_Controller;
import ERP.Controller.SelectDatabase_Controller;
import ERP.Controller.SystemSetting.QuotationTemplateFormatSetting_Controller;
import ERP.Controller.SystemSetting.SystemSetting_Controller;
import ERP.Controller.Toolkit.ShowBidCategorySetting.ShowBidCategorySetting_Controller;
import ERP.Controller.Toolkit.ShowBigGoFilter.ShowBigGoFilter_Controller;
import ERP.Controller.Toolkit.ShowBigGoPriceSetting.ShowBigGoPriceSetting_Controller;
import ERP.Controller.Toolkit.ShowCheckNumberSetting.ShowCheckNumberSetting_Controller;
import ERP.Controller.Toolkit.ShowCompareIAECrawlerData.ShowCompareIAECrawlerData_Controller;
import ERP.Controller.Toolkit.ShowEditOrderRemark.ShowEditOrderRemark_Controller;
import ERP.Controller.Toolkit.ShowEditOrderReviewStatusReason.ShowEditOrderReviewStatusReason_Controller;
import ERP.Controller.Toolkit.ShowEditProductDescribeOrRemark.ShowEditProductDescribeOrRemark_Controller;
import ERP.Controller.Toolkit.ShowExportQuotation.ShowExportQuotation_Controller;
import ERP.Controller.Toolkit.ShowExportQuotationRecord.ShowExportQuotationRecord_Controller;
import ERP.Controller.Toolkit.ShowHTMLEditor.ShowHTMLEditor_Controller;
import ERP.Controller.Toolkit.ShowIAECrawlerBelong.ShowIAECrawlerBelong_Controller;
import ERP.Controller.Toolkit.ShowCancelOrderProduct.ShowCancelOrderProduct_Controller;
import ERP.Controller.Toolkit.ShowInstallment.ShowInstallment_Controller;
import ERP.Controller.Toolkit.ShowModifyProduct.ShowModifyProduct_Controller;
import ERP.Controller.Toolkit.ShowModifySpecificationTemplate.ShowModifySpecificationTemplate_Controller;
import ERP.Controller.Toolkit.ShowNonePayReceiveDetails.ShowNonePayReceiveDetails_Controller;
import ERP.Controller.Toolkit.ShowObject.ShowObject_Controller;
import ERP.Controller.Toolkit.ShowOrderReferenceOverview.ShowOrderReferenceOverview_Controller;
import ERP.Controller.Toolkit.ShowOrderReviewStatusOverview.ShowOrderReviewStatusOverview_Controller;
import ERP.Controller.Toolkit.ShowPaymentCompareTableViewSetting.ShowPaymentCompareTableViewSetting_Controller;
import ERP.Controller.Toolkit.ShowPictureEditor.ShowPictureEditor_Controller;
import ERP.Controller.Toolkit.ShowProductConnectionFromGroup.ShowProductConnectionFromGroup_Controller;
import ERP.Controller.Toolkit.ShowProductGenerator.ShowProductGenerator_Controller;
import ERP.Controller.Toolkit.ShowProductGroup.ShowProductGroup_Controller;
import ERP.Controller.Toolkit.ShowProductOnShelfSetting.ShowProductOnShelfSetting_Controller;
import ERP.Controller.Toolkit.ShowProductSpecification.ShowProductSpecification_Controller;
import ERP.Controller.Toolkit.ShowExportSpecification.ShowExportSpecification_Controller;
import ERP.Controller.Toolkit.ShowProductTagSetting.ShowProductTagSetting_Controller;
import ERP.Controller.Toolkit.ShowExportOrder.ShowExportOrder_Controller;
import ERP.Controller.Toolkit.ShowPicture.ShowPicture_Controller;
import ERP.Controller.Toolkit.ShowProduct.ShowProduct_Controller;
import ERP.Controller.Toolkit.ShowPurchaserRecipient.ShowPurchaserRecipient_Controller;
import ERP.Controller.Toolkit.ShowReportGenerator.ShowReportGenerator_Controller;
import ERP.Controller.Toolkit.ShowSearchOrderOverview.ShowSearchOrderOverview_Controller;
import ERP.Controller.Toolkit.ShowSearchOrderTableViewSetting.ShowSearchOrderTableViewSetting_Controller;
import ERP.Controller.Toolkit.ShowSeparateOrder.ShowSeparateOrder_Controller;
import ERP.Controller.Toolkit.ShowSeparatePurchaseManufacturer.ShowSeparatePurchaseManufacturer_Controller;
import ERP.Controller.Toolkit.ShowSnapshotOrderPicture.ShowSnapshotOrderPicture_Controller;
import ERP.Controller.Toolkit.ShowTransactionDetail.ShowTransactionDetail_Controller;
import ERP.Controller.Toolkit.ShowControllerVersion.ShowControllerVersion_Controller;
import ERP.Controller.Toolkit.ShowVersion.ShowVersion_Controller;
import ERP.ERPApplication;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PayableReceivableStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PaymentCompareTabName;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.ShowObjectSource;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Enum.Order.Order_Enum.OrderExist;
import ERP.Enum.Order.Order_Enum.SpecificationColumn;
import ERP.Enum.Product.Product_Enum.ManageProductStatus;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum.SnapshotPictureLocation;
import ERP.Enum.Product.Product_Enum.BigGoFilterSource;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

/** Call FXML file */
public class CallFXML {

    private ComponentToolKit ComponentToolKit;
    public CallFXML(ComponentToolKit ComponentToolKit){
        this.ComponentToolKit = ComponentToolKit;
    }
    public void SelectDatabase(boolean restart){
        try {
            Stage Stage = ComponentToolKit.setStage("資料庫選擇", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent Parent = FXMLLoader.load();
            SelectDatabase_Controller DatabaseSelect_Controller = FXMLLoader.getController();
            DatabaseSelect_Controller.setStage(Stage);
            DatabaseSelect_Controller.setDataBaseButton(Parent);
            DatabaseSelect_Controller.setRestart(restart);

            Scene Scene = ComponentToolKit.setScene(Parent, 400, 275);
            Stage.setScene(Scene);
            Stage.setOnCloseRequest(event -> System.exit(0));
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void loadSystemMenu(Stage MainStage){
        try {
            Stage Stage = ComponentToolKit.setStage("系統主選單【" + SqlAdapter.getDbDisplayName() + "】", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/SystemMenu.fxml"));
            Parent Parent = FXMLLoader.load();
            SystemMenu_Controller SystemMenu_Controller = FXMLLoader.getController();
            SystemMenu_Controller.setStage(Stage);
            SystemMenu_Controller.setTitleReflection(Parent);
            SystemMenu_Controller.setMenuButton(Parent);
            SystemMenu_Controller.setComponent();

            Scene Scene = ComponentToolKit.setScene(Parent, 695, 630);
            Stage.setScene(Scene);
            Stage.show();
            MainStage.close();

            SystemMenu_Controller.versionSynchronize();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowVersion(){
        try {
            Stage Stage = ComponentToolKit.setStage("版本更新內容", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowVersion.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowVersion_Controller ShowVersion_Controller = FXMLLoader.getController();
            ShowVersion_Controller.setMainStage(Stage);
            ShowVersion_Controller.setComponent();
            Scene Scene = ComponentToolKit.setScene(Parent, 700, 600);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowControllerVersion(Stage MainStage, ObservableList<Version_Bean> allVersionList){
        try {
            Stage Stage = ComponentToolKit.setStage("版本控制", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowControllerVersion.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowControllerVersion_Controller ShowControllerVersion_Controller = FXMLLoader.getController();
            ShowControllerVersion_Controller.setAllVersionList(allVersionList);
            ShowControllerVersion_Controller.setComponent();
            Scene Scene = ComponentToolKit.setScene(Parent, 700, 500);
            Stage.setScene(Scene);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent ManageProductCategory(){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageProductInfo/ManageProductCategory.fxml"));
            Parent = FXMLLoader.load();
            ManageProductCategory_Controller Controller = FXMLLoader.getController();
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowProductTagSetting(Stage mainStage, String isbn, String firmCode, ObservableList<String> productTagList){
        try {
            Stage Stage = ComponentToolKit.setStage();
            Stage.setTitle("商品碼(標籤)設定");
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProductTagSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowProductTagSetting_Controller Controller = FXMLLoader.getController();
            Controller.setProductData(isbn, firmCode, productTagList);
            Controller.setStage(Stage);
            Stage.initOwner(mainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 500, 350);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowBidCategorySetting(Stage MainStage, ProductInfo_Bean ProductInfo_Bean, ManageProductInfo_Controller ManageProductInfo_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage();
            Stage.setTitle("拍賣類別設定");
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowBidCategorySetting.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowBidCategorySetting_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductInfo_Controller(ManageProductInfo_Controller);
            Controller.setComponent();
            Controller.setProductInfo_Bean(ProductInfo_Bean);
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 880, 480);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent ManageProductInfo(ManageProductStatus ManageProductStatus, Stage MainStage){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageProductInfo/ManageProductInfo.fxml"));
            Parent = FXMLLoader.load();
            ManageProductInfo_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductStatus(ManageProductStatus);
            Controller.setMainStage(MainStage);
            Controller.setComponent(null);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ManageProductInfo_NewStage(ManageProductStatus ManageProductStatus, Stage MainStage, ProductInfo_Bean ProductInfo_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("商品資料建立",true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageProductInfo/ManageProductInfo.fxml"));
            Parent Parent = FXMLLoader.load();
            ManageProductInfo_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductStatus(ManageProductStatus);
            Controller.setMainStage(MainStage);
            Controller.setComponent(ProductInfo_Bean);
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowHTMLEditor(Stage MainStage, ManageProductInfo_Controller ManageProductInfo_Controller, ProductInfo_Bean ProductInfo_Bean, WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("HTML編輯器", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowHTMLEditor.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 850, 600);
            ShowHTMLEditor_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductInfo_Controller(ManageProductInfo_Controller);
            Controller.setProductInfo_Bean(ProductInfo_Bean);
            Controller.setWaitConfirmProductInfo_Bean(WaitConfirmProductInfo_Bean);
            Controller.setHTMLText();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowProductSpecification(Stage MainStage, String ISBN){
        try {
            Stage Stage = ComponentToolKit.setStage("商品規格書", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProductSpecification.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1000, 700);
            ShowProductSpecification_Controller Controller = FXMLLoader.getController();
            Controller.setProductISBN(ISBN);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowExportQuotation(Stage MainStage, ExportQuotation_Bean exportQuotation_bean, Order_Bean order_bean, IAECrawlerAccount_Bean iAECrawlerAccount_bean){
        try {
            Stage Stage = ComponentToolKit.setStage("報價單匯出", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowExportQuotation.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 600, 600);
            ShowExportQuotation_Controller Controller = FXMLLoader.getController();
            Controller.setExportQuotation_Bean(exportQuotation_bean);
            Controller.setOrder_Bean(order_bean);
            Controller.setIAECrawlerAccount_Bean(iAECrawlerAccount_bean);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowExportQuotationRecord(Stage MainStage,ObservableList<ExportQuotationRecord_Bean> exportQuotationRecordList){
        try {
            Stage Stage = ComponentToolKit.setStage("匯出紀錄", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowExportQuotationRecord.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1050, 600);
            ShowExportQuotationRecord_Controller Controller = FXMLLoader.getController();
            Controller.setExportQuotationRecordList(exportQuotationRecordList);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowReportGenerator(Stage MainStage, Order_Bean Order_Bean, boolean isSelectProductGroup,String projectName, int totalPriceIncludeTax){
        try {
            Stage Stage = ComponentToolKit.setStage("採購文件", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowReportGenerator.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 900, 800);
            ShowReportGenerator_Controller Controller = FXMLLoader.getController();
            Controller.setStage(Stage);
            Controller.setOrderInfo(Order_Bean, isSelectProductGroup, projectName, totalPriceIncludeTax);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowExportSpecification(Stage MainStage, String projectName, ObservableList<OrderProduct_Bean> productList){
        try {
            Stage Stage = ComponentToolKit.setStage("匯出規格書", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowExportSpecification.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 970, 600);
            ShowExportSpecification_Controller Controller = FXMLLoader.getController();
            Controller.setStage(Stage);
            Controller.setProjectName(projectName);
            Controller.setProductList(productList);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowModifySpecificationTemplate(SpecificationColumn SpecificationColumn, Stage MainStage, ComboBox<SpecificationTemplate_Bean> templateComboBox){
        try {
            Stage Stage = ComponentToolKit.setStage("「" + SpecificationColumn.name() + "」 修改範本", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowModifySpecificationTemplate.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 900, 700);
            ShowModifySpecificationTemplate_Controller Controller = FXMLLoader.getController();
            Controller.setSpecificationColumn(SpecificationColumn);
            Controller.setExportTemplate_ComboBox(templateComboBox);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent ManageProductOnShelf(Stage MainStage){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageProductInfo/ManageProductOnShelf.fxml"));
            Parent = FXMLLoader.load();
            ManageProductOnShelf_Controller Controller = FXMLLoader.getController();
            Controller.setComponent();
            Controller.setMainStage(MainStage);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowProductOnShelfSetting(Stage MainStage, ManageProductOnShelf_Bean ManageProductOnShelf_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("商品設定", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProductOnShelfSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 800, 600);
            ShowProductOnShelfSetting_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductOnShelf_Bean(ManageProductOnShelf_Bean);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowObject(Stage MainStage, OrderObject OrderObject, ObservableList<ObjectInfo_Bean> ObjectList, boolean objectCanGet, ShowObjectSource ShowObjectSource, Object controllerSource){
        try {
            Stage Stage = ComponentToolKit.setStage("客戶查詢",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowObject.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1250, 400);
            Stage.setScene(Scene);
            ShowObject_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setObjectCanGet(objectCanGet);
            Controller.setControllerSource(controllerSource);
            Controller.setShowObjectSource(ShowObjectSource);
            Controller.setComponent();
            Controller.setTableViewItems(ObjectList);
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowProduct(Stage MainStage, OrderObject OrderObject, ObservableList<OrderProduct_Bean> ProductList, EstablishOrder_Controller EstablishOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("商品查詢", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProduct.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1750, 400);
            Stage.setScene(Scene);
            ShowProduct_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setOrderObject(OrderObject);
            Controller.setComponent();
            Controller.setTableViewItems(ProductList);
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowModifyProduct(Stage MainStage, OrderObject orderObject, ObjectInfo_Bean ObjectInfo_Bean, OrderProduct_Bean selectOrderProduct_Bean, OrderProduct_Bean storeOrderProduct_Bean, EstablishOrder_Controller EstablishOrder_Controller){
        try{
            Stage Stage = ComponentToolKit.setStage("【Store】商品修改", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowModifyProduct.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 780, 850);
            Stage.setScene(Scene);
            ShowModifyProduct_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(orderObject);
            Controller.setObjectInfo_Bean(ObjectInfo_Bean);
            Controller.setStoreOrderProduct_Bean(storeOrderProduct_Bean);
            Controller.setSelectOrderProduct_Bean(selectOrderProduct_Bean);
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setComponent();
            Controller.setStage(Stage);
            Controller.setMainStage(MainStage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowProductGenerator(Stage mainStage, OrderObject orderObject, String objectID, EstablishOrder_Controller establishOrder_controller){
        try {
            Stage Stage = ComponentToolKit.setStage("商品產生器", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProductGenerator.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 950, 850);
            ShowProductGenerator_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(establishOrder_controller);
            Controller.setOrderObject(orderObject);
            Controller.setObjectID(objectID);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(mainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowEditOrderReviewStatusReason(Stage MainStage, EstablishOrder_Controller EstablishOrder_Controller, Order_Enum.ReviewStatus ReviewStatus){
        try {
            Stage Stage = ComponentToolKit.setStage("【" + ReviewStatus.name() + "】" + (ReviewStatus == Order_Enum.ReviewStatus.待修改 ? "原因":"備註"), false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowEditOrderReviewStatusReason.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 950, 650);
            Stage.setScene(Scene);
            ShowEditOrderReviewStatusReason_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setReviewStatus(ReviewStatus);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowOrderReviewStatusOverview(Stage MainStage, boolean isTransferWaitingOrder,ObservableList<OrderReviewStatusRecord_Bean> orderReviewStatusRecordList){
        try {
            Stage Stage = ComponentToolKit.setStage("審查紀錄", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowOrderReviewStatusOverview.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 900, (int) Screen.getPrimary().getVisualBounds().getHeight()-50);
            Stage.setScene(Scene);
            ShowOrderReviewStatusOverview_Controller Controller = FXMLLoader.getController();
            Controller.setTransferWaitingOrder(isTransferWaitingOrder);
            Controller.setOrderReviewStatusRecordList(orderReviewStatusRecordList);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowOrderReferenceOverview(boolean canModifyOrderReference,Order_Bean Order_Bean, HashMap<OrderSource,HashMap<Integer,Boolean>> orderReferenceMap, ObjectInfo_Bean ObjectInfo_Bean, HashMap<String,Boolean> orderProductMap, EstablishOrder_Controller EstablishOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("參考貨單", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowOrderReferenceOverview.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1270, (int) Screen.getPrimary().getVisualBounds().getHeight()-50);
            Stage.setScene(Scene);
            ShowOrderReferenceOverview_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setCanModifyOrderReference(canModifyOrderReference);
            Controller.setOrder_Bean(Order_Bean);
            Controller.setOrderReferenceMap(orderReferenceMap);
            Controller.setObjectInfo_Bean(ObjectInfo_Bean);
            Controller.setOrderProductMap(orderProductMap);
            Controller.setComponent();
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowTransactionDetail(Stage MainStage, ManageProductInfo_Controller ManageProductInfo_Controller, EstablishOrder_Controller EstablishOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("商品交易明細", false,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowTransactionDetail.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1950, 800);
            Stage.setScene(Scene);
            ShowTransactionDetail_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductInfo_Controller(ManageProductInfo_Controller);
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowSearchOrderOverview(OrderObject OrderObject){
        try {
            Stage Stage = ComponentToolKit.setStage("貨單總覽", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowSearchOrderOverview.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1200, 800);
            Stage.setScene(Scene);
            ShowSearchOrderOverview_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowEditOrderRemark(Stage MainStage, EstablishOrder_Controller EstablishOrder_Controller, OrderExist OrderExist, Order_Bean Order_Bean, boolean isOrderRemark, boolean isCashierRemark){
        try {
            Stage Stage = ComponentToolKit.setStage("修改備註",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowEditOrderRemark.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowEditOrderRemark_Controller Controller = FXMLLoader.getController();
            Controller.setOrder_Bean(Order_Bean);
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setOrderExist(OrderExist);
            Controller.setRemarkStatus(isOrderRemark,isCashierRemark);
            Controller.setEditInfo();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 900, 500);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent ManageManufacturerInfo(Stage MainStage){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePurchaseSystem/ManageManufacturerInfo.fxml"));
            Parent = FXMLLoader.load();
            ManageManufacturerInfo_Controller Controller = FXMLLoader.getController();
            Controller.setStage(MainStage);
            Controller.setComponent(null);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ManageManufacturerInfo_NewStage(Stage MainStage, ObjectInfo_Bean objectInfo_bean){
        try {
            Stage Stage = ComponentToolKit.setStage("廠商資料管理",true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePurchaseSystem/ManageManufacturerInfo.fxml"));
            Parent Parent = FXMLLoader.load();
            ManageManufacturerInfo_Controller Controller = FXMLLoader.getController();
            Controller.setStage(MainStage);
            Controller.setComponent(objectInfo_bean);
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent ManageCustomerInfo(Stage MainStage){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageShipmentSystem/ManageCustomerInfo.fxml"));
            Parent = FXMLLoader.load();
            ManageCustomerInfo_Controller Controller = FXMLLoader.getController();
            Controller.setStage(MainStage);
            Controller.setComponent(null);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ManageCustomerInfo_NewStage(Stage mainStage, ObjectInfo_Bean objectInfo_bean){
        try {
            Stage Stage = ComponentToolKit.setStage("客戶資料管理",true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageShipmentSystem/ManageCustomerInfo.fxml"));
            Parent Parent = FXMLLoader.load();
            ManageCustomerInfo_Controller Controller = FXMLLoader.getController();
            Controller.setStage(mainStage);
            Controller.setComponent(objectInfo_bean);
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void EstablishOrder_NewStage(EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean) {
        try {
            Stage Stage = ComponentToolKit.setStage(establishOrder_NewStage_Bean.getOrderSource().name(), true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Order/EstablishOrder.fxml"));
            Parent Parent = FXMLLoader.load();
            EstablishOrder_Controller Controller = FXMLLoader.getController();
            Controller.setStage(Stage);
            Controller.initialComponentData(establishOrder_NewStage_Bean);
            boolean isOrderLost = Controller.loadOrderData();
            Controller.initialComponent();
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            if(!isOrderLost){
                Controller.setOrderData(false);
                Stage.show();
                Stage.setAlwaysOnTop(false);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent EstablishOrder(Stage MainStage, OrderSource OrderSource){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Order/EstablishOrder.fxml"));
            Parent = FXMLLoader.load();
            EstablishOrder_Controller Controller = FXMLLoader.getController();

            EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
            establishOrder_NewStage_Bean.setOrderSource(OrderSource);
            establishOrder_NewStage_Bean.setOrderExist(OrderExist.未存在);

            Controller.setStage(MainStage);
            if(!Controller.isOrderNumberLengthSetting()) {
                DialogUI.MessageDialog("※ [系統設定] 貨單號長度未設定");
                return null;
            }
            Controller.initialComponentData(establishOrder_NewStage_Bean);
            Controller.initialComponent();
            Controller.setOrderData(true);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowPurchaserRecipient(Stage MainStage, EstablishOrder_Controller EstablishOrder_Controller, OrderExist OrderExist, Order_Bean Order_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("訂購/收件資訊", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowPurchaserRecipient.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 850, 600);
            Stage.setScene(Scene);
            ShowPurchaserRecipient_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setOrderExist(OrderExist);
            Controller.setOrder_Bean(Order_Bean);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowProductGroup(Stage MainStage, Order_Bean Order_Bean, EstablishOrder_Controller EstablishOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("商品群組", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProductGroup.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1800, 950);
            Stage.setScene(Scene);
            ShowProductGroup_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setComponent();
            Controller.setOrder_Bean(Order_Bean);
            Controller.setStage(Stage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.initOwner(MainStage);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowProductConnectionFromGroup(Stage MainStage, EstablishOrder_Controller EstablishOrder_Controller, String ISBN, String objectID, ArrayList<ProductGroup_Bean> productGroupList){
        try {
            Stage Stage = ComponentToolKit.setStage("商品群組關聯", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowProductConnectionFromGroup.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1200, (int) Screen.getPrimary().getVisualBounds().getHeight()-50);
            Stage.setScene(Scene);
            ShowProductConnectionFromGroup_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setISBN(ISBN);
            Controller.setObjectID(objectID);
            Controller.setProductGroupList(productGroupList);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowCancelOrderProduct(Stage MainStage, Order_Bean Order_Bean, ObservableList<OrderProduct_Bean> canCancelOrderProductList, EstablishOrder_Controller EstablishOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("商品註銷", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowCancelOrderProduct.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent,1250,730);
            Stage.setScene(Scene);
            ShowCancelOrderProduct_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setComponent();
            Controller.setOrder_Bean(Order_Bean);
            Controller.setTableViewItems(canCancelOrderProductList);
            Controller.setStage(Stage);
            Controller.setMainStage(MainStage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent SearchOrder(Stage MainStage, OrderObject OrderObject, SearchOrderSource SearchOrderSource){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Order/SearchOrder.fxml"));
            Parent = FXMLLoader.load();
            SearchOrder_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setSearchOrderSource(SearchOrderSource);
            Controller.setMainStage(MainStage);
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowSearchOrderTableViewSetting(Stage MainStage, SearchOrderSource SearchOrderSource, SearchOrder_Controller SearchOrder_Controller, JSONObject TableViewSettingMap){
        try {
            Stage Stage = ComponentToolKit.setStage("視窗設定", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowSearchOrderTableViewSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 420, 700);
            Stage.setScene(Scene);
            ShowSearchOrderTableViewSetting_Controller Controller = FXMLLoader.getController();
            Controller.setSearchOrder_Controller(SearchOrder_Controller);
            Controller.setSearchOrderSource(SearchOrderSource);
            Controller.setTableViewSettingMap(TableViewSettingMap);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent SearchOrderProgress(Stage MainStage, OrderObject OrderObject){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Order/SearchOrderProgress.fxml"));
            Parent = FXMLLoader.load();
            SearchOrderProgress_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void SearchOrderProgress_NewStage(OrderObject OrderObject){
        try {
            Stage Stage = ComponentToolKit.setStage(OrderObject.name() + "貨單進度查詢",true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Order/SearchOrderProgress.fxml"));
            Parent Parent = FXMLLoader.load();
            SearchOrderProgress_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setStage(Stage);
            Controller.setComponent();
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent SearchNonePayReceive(Stage MainStage, OrderObject OrderObject){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/Order/SearchNonePayReceive.fxml"));
            Parent = FXMLLoader.load();
            SearchNonePayReceive_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setMainStage(MainStage);
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowNonePayReceiveDetails(Stage MainStage, Order_Enum.OrderObject OrderObject, ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean, SearchNonePayReceive_Bean SearchNonePayReceive_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage(OrderObject.name() + OrderObject.getPayableReceivableName()+"明細表", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowNonePayReceiveDetails.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1180, 600);
            Stage.setScene(Scene);
            ShowNonePayReceiveDetails_Controller Controller = FXMLLoader.getController();
            Controller.setConditionalPayReceiveSearch_bean(ConditionalPayReceiveSearch_Bean);
            Controller.setSearchNonePayReceive_bean(SearchNonePayReceive_Bean);
            Controller.setOrderObject(OrderObject);
            Controller.setMainStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowSeparateOrder(Stage MainStage, OrderObject OrderObject, OrderSource OrderSource, ObservableList<OrderProduct_Bean> ProductList, Order_Bean Order_Bean, SearchOrder_Controller SearchOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("子貨單建立", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowSeparateOrder.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1120, 730);
            Stage.setScene(Scene);
            ShowSeparateOrder_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setOrderSource(OrderSource);
            Controller.setSearchOrder_Controller(SearchOrder_Controller);
            Controller.setOrder_Bean(Order_Bean);
            Controller.setComponent();
            Controller.setTableViewItems(ProductList);
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowSeparatePurchaseManufacturer(Stage MainStage, String projectCode, String projectName, ObservableList<OrderProduct_Bean> ProductList, EstablishOrder_Controller EstablishOrder_Controller, SearchOrder_Controller SearchOrder_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("採購單建立", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowSeparatePurchaseManufacturer.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 1250, 730);
            Stage.setScene(Scene);
            ShowSeparatePurchaseManufacturer_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_Controller(EstablishOrder_Controller);
            Controller.setSearchOrder_Controller(SearchOrder_Controller);
            Controller.setComponent();
            Controller.setProjectCodeAndProjectName(projectCode, projectName);
            Controller.setTableViewItems(ProductList);
            Controller.setStage(Stage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.initOwner(MainStage);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowSnapshotOrderPicture(Stage MainStage, SnapshotPictureLocation SnapshotPictureLocation, Object sourceObject){
        try {
            Stage Stage = ComponentToolKit.setStage("貨單拍照",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowSnapshotOrderPicture.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowSnapshotOrderPicture_Controller Controller = FXMLLoader.getController();
            Controller.setSnapshotPictureLocationAndSourceObject(SnapshotPictureLocation, sourceObject);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 1000, 900);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowPicture(Stage MainStage, Order_Bean Order_Bean, ArrayList<HashMap<Boolean,String>> OrderPictureList, Order_Enum.OrderSource OrderSource, Object controller_object) {
        try {
            Stage Stage = ComponentToolKit.setStage("Picture",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowPicture.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowPicture_Controller Controller = FXMLLoader.getController();
            Controller.setSourceObject(controller_object);
            Controller.setOrder_Bean(Order_Bean);
            Controller.setOrderSource(OrderSource);
            Controller.setOrderPictureList(OrderPictureList);
            Controller.refreshAndSetImage();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 1400, 950);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowPictureEditor(Stage MainStage, OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean, Object sourceObject) {
        try {
            Stage Stage = ComponentToolKit.setStage("圖片編輯器",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowPictureEditor.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowPictureEditor_Controller Controller = FXMLLoader.getController();
            Controller.setSourceObject(sourceObject);
            Controller.setOrderReviewStatusPicture_Bean(orderReviewStatusPicture_Bean);
            Controller.setStage(Stage);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 1400, 950);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowExportOrder(Stage MainStage, String outputFilePath, Order_Bean Order_Bean, ObjectInfo_Bean ObjectInfo_Bean, ObservableList<ExportOrder_Bean> ProductList){
        try {
            Stage Stage = ComponentToolKit.setStage("匯出貨單",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowExportOrder.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowExportOrder_Controller Controller = FXMLLoader.getController();
            Controller.setOutPutFilePath(outputFilePath);
            Controller.setOrder_Bean(Order_Bean);
            Controller.setObjectInfo_Bean(ObjectInfo_Bean);
            Controller.setProductList(ProductList);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 750, 600);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowInstallment(Stage MainStage, EstablishOrder_Controller establishOrder_controller, Order_Bean order_bean, String orderPrice, ArrayList<Installment_Bean> installmentList){
        try {
            Stage Stage = ComponentToolKit.setStage("分期付/收款",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowInstallment.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowInstallment_Controller Controller = FXMLLoader.getController();
            Controller.setEstablishOrder_controller(establishOrder_controller);
            Controller.setOrder_bean(order_bean);
            Controller.setOrderPrice(orderPrice);
            Controller.setInstallmentList(installmentList);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 450, 500);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent EstablishPayableReceivable(Stage MainStage, OrderObject OrderObject){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePayableReceivable/EstablishPayableReceivable.fxml"));
            Parent = FXMLLoader.load();
            EstablishPayableReceivable_Controller Controller = FXMLLoader.getController();
            Controller.setPayableReceivableStatus(PayableReceivableStatus.建立);
            Controller.setOrderObject(OrderObject);
            Controller.setStage(MainStage);
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void EstablishPayableReceivable_NewStage(OrderObject OrderObject, PayableReceivableStatus PayableReceivableStatus, SearchPayableReceivable_Controller SearchPayableReceivable_Controller,PaymentCompareSystem_Controller PaymentCompareSystem_Controller, PayableReceivable_Bean PayableReceivable_Bean, ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage(OrderObject.name(), true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePayableReceivable/EstablishPayableReceivable.fxml"));
            Parent Parent = FXMLLoader.load();
            EstablishPayableReceivable_Controller Controller = FXMLLoader.getController();
            Controller.setPayableReceivableStatus(PayableReceivableStatus);
            Controller.setOrderObject(OrderObject);
            Controller.setSearchPayableReceivable_Controller(SearchPayableReceivable_Controller);
            Controller.setPaymentCompareSystem_Controller(PaymentCompareSystem_Controller);
            Controller.setPayableReceivable_Bean(PayableReceivable_Bean);
            Controller.setConditionalPayReceiveSearch_Bean(ConditionalPayReceiveSearch_Bean);
            Controller.setComponent();
            Controller.setStage(Stage);
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);

            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowCheckNumberSetting(Stage MainStage, SystemSettingConfig_Bean SystemSettingConfig_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("票據流水號設定",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowCheckNumberSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowCheckNumberSetting_Controller Controller = FXMLLoader.getController();
            Controller.setSystemSettingConfig_Bean(SystemSettingConfig_Bean);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 350, 200);
            Stage.setScene(Scene);
            Stage.show();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent SearchPayableReceivable(Stage MainStage, OrderObject OrderObject){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePayableReceivable/SearchPayableReceivable.fxml"));
            Parent = FXMLLoader.load();
            SearchPayableReceivable_Controller Controller = FXMLLoader.getController();
            Controller.setOrderObject(OrderObject);
            Controller.setMainStage(MainStage);
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public Parent ManageCompanyAccount(){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePayableReceivable/ManageCompanyAccount.fxml"));
            Parent = FXMLLoader.load();
            ManageCompanyAccount_Controller Controller = FXMLLoader.getController();
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public Parent ManageBankInfo(){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePayableReceivable/ManageBankInfo.fxml"));
            Parent = FXMLLoader.load();
            ManageBankInfo_Controller Controller = FXMLLoader.getController();
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public Parent PaymentCompareSystem(Stage MainStage){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManagePayableReceivable/PaymentCompareSystem.fxml"));
            Parent = FXMLLoader.load();
            PaymentCompareSystem_Controller Controller = FXMLLoader.getController();
            Controller.setMainStage(MainStage);
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowCompareIAECrawlerData(Stage MainStage, IAECrawlerAccount_Bean IAECrawlerAccount_Bean, HashMap<IAECrawlerData_Bean,IAECrawlerData_Bean> differentObjectMap){
        try {

            Stage Stage = ComponentToolKit.setStage(IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName() + "- 出納資訊更新結果", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowCompareIAECrawlerData.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowCompareIAECrawlerData_Controller Controller = FXMLLoader.getController();
            Controller.setStage(Stage);
            Controller.setIAECrawlerAccount_Bean(IAECrawlerAccount_Bean);
            Controller.setDifferentObjectMap(differentObjectMap);
            Controller.setComponent();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 800, 500);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowPaymentCompareTableViewSetting(Stage MainStage, PaymentCompareTabName PaymentCompareTabName, PaymentCompareSystem_Controller PaymentCompareSystem_Controller, JSONObject TableViewSettingMap){
        try {
            Stage Stage = ComponentToolKit.setStage("視窗設定", false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowPaymentCompareTableViewSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            Scene Scene = ComponentToolKit.setScene(Parent, 420, 700);
            Stage.setScene(Scene);
            ShowPaymentCompareTableViewSetting_Controller Controller = FXMLLoader.getController();
            Controller.setPaymentCompareSystem_Controller(PaymentCompareSystem_Controller);
            Controller.setPaymentCompareTabName(PaymentCompareTabName);
            Controller.setTableViewSettingMap(TableViewSettingMap);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public Parent ShowProductWaitConfirm(Stage MainStage){
        Parent Parent = null;
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/ProductWaitingConfirm.fxml"));
            Parent = FXMLLoader.load();
            ProductWaitingConfirm_Controller Controller = FXMLLoader.getController();
            Controller.setStage(MainStage);
            Controller.initialSingleUpdateWebDriver();
            Controller.setComponent();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return Parent;
    }
    public void ShowProductWaitConfirm_NewStage(Stage MainStage, OrderProduct_Bean OrderProduct_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("待確認商品", true,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/ProductWaitingConfirm.fxml"));
            Parent Parent = FXMLLoader.load();
            ProductWaitingConfirm_Controller Controller = FXMLLoader.getController();
            Controller.setOrderProduct_Bean(OrderProduct_Bean);
            Controller.setStage(Stage);
            Controller.initialSingleUpdateWebDriver();
            Controller.setComponent();
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowEditProductDescribeOrRemark(Stage MainStage, Product_Enum.EditProductDescribeOrRemark EditProductDescribeOrRemark, WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage(EditProductDescribeOrRemark.name(),false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowEditProductDescribeOrRemark.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowEditProductDescribeOrRemark_Controller Controller = FXMLLoader.getController();
            Controller.setEditProductDescribeOrRemark(EditProductDescribeOrRemark);
            Controller.setWaitConfirmProductInfo_Bean(WaitConfirmProductInfo_Bean);
            Controller.setEditInfo();
            Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 900, 500);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowBigGoStorePriceSetting(Stage mainStage, JSONObject storePriceParameterJson){
        try {
            Stage Stage = ComponentToolKit.setStage("金額設定",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowBigGoStorePriceSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowBigGoPriceSetting_Controller Controller = FXMLLoader.getController();
            Controller.setStorePriceParameterJson(storePriceParameterJson);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(mainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 800, 650);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowBigGoFilter(Stage mainStage, HashMap<BigGoFilterSource,ObservableList<BigGoFilter_Bean>> bigGoFilterMap){
        try {
            Stage Stage = ComponentToolKit.setStage("商店選擇",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowBigGoFilter.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowBigGoFilter_Controller Controller = FXMLLoader.getController();
            Controller.setBigGoFilterMap(bigGoFilterMap);
            Controller.setComponent();
            Controller.setStage(Stage);
            Stage.initOwner(mainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 600, 650);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowSingleProductUpdate(Stage MainStage, WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("單筆更新",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/SingleProductUpdate.fxml"));
            Parent Parent = FXMLLoader.load();
            SingleProductUpdate_Controller Controller = FXMLLoader.getController();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Controller.setSingleProductUpdate_Model(MainStage);
            Controller.setStage(Stage);
            Controller.setWaitConfirmProductInfo_Bean(WaitConfirmProductInfo_Bean);
            Scene Scene = ComponentToolKit.setScene(Parent, 880, 825);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowXanderLogin(Stage MainStage, Label UpdateStatusText){
        try {
            Stage Stage = ComponentToolKit.setStage(Product_Enum.Vendor.建達國際.name(),false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/XanderLogin.fxml"));
            Parent Parent = FXMLLoader.load();
            XanderLogin_Controller Controller = FXMLLoader.getController();
            Controller.setVendor(Product_Enum.Vendor.建達國際);
            Controller.setUpdateStatus(UpdateStatusText);
            Controller.setStage(Stage);
            Controller.setSingleProductUpdate_Model();
            Controller.setLoginInfo();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 320, 380);
            Stage.setScene(Scene);
            Stage.show();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowUnitechLogin(Stage MainStage, Label UpdateStatusText){
        try {
            Stage Stage = ComponentToolKit.setStage(Product_Enum.Vendor.精技電腦.name(),false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/UnitechLogin.fxml"));
            Parent Parent = FXMLLoader.load();
            UnitechLogin_Controller Controller = FXMLLoader.getController();
            Controller.setVendor(Product_Enum.Vendor.精技電腦);
            Controller.setUpdateStatus(UpdateStatusText);
            Controller.setStage(Stage);
            Controller.setSingleProductUpdate_Model();
            Controller.setLoginInfo();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 320, 380);
            Stage.setScene(Scene);

            Stage.show();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowJinghaoLogin(Stage MainStage, Label UpdateStatusText){
        try {
            Stage Stage = ComponentToolKit.setStage(Product_Enum.Vendor.精豪電腦.name(),false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/JinghaoLogin.fxml"));
            Parent Parent = FXMLLoader.load();
            JinghaoLogin_Controller Controller = FXMLLoader.getController();
            Controller.setVendor(Product_Enum.Vendor.精豪電腦);
            Controller.setUpdateStatus(UpdateStatusText);
            Controller.setStage(Stage);
            Controller.setSingleProductUpdate_Model();
            Controller.setLoginInfo();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 320, 380);
            Stage.setScene(Scene);
            Stage.show();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowWeblinkLogin(Stage MainStage, Label UpdateStatusText){
        try {
            Stage Stage = ComponentToolKit.setStage(Product_Enum.Vendor.展碁國際.name(),false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ProductWaitingConfirm/WeblinkLogin.fxml"));
            Parent Parent = FXMLLoader.load();
            WeblinkLogin_Controller Controller = FXMLLoader.getController();
            Controller.setVendor(Product_Enum.Vendor.展碁國際);
            Controller.setUpdateStatus(UpdateStatusText);
            Controller.setStage(Stage);
            Controller.setSingleProductUpdate_Model();
            Controller.setLoginInfo();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 300, 320);
            Stage.setScene(Scene);
            Stage.show();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowSystemSetting(Observer Observer){
        try {
            Stage Stage = ComponentToolKit.setStage("系統設定【" + SqlAdapter.getDbDisplayName() + "】", false,true);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/SystemSetting/SystemSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            SystemSetting_Controller Controller = FXMLLoader.getController();
            Controller.setStage(Stage);
            Controller.setComponents();
            Controller.addObserver(Observer);
            Scene Scene = ComponentToolKit.setScene(Parent);
            Stage.setScene(Scene);
            Stage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowTemplateFormatSetting(Stage MainStage, IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        try {
            Stage Stage = ComponentToolKit.setStage("範本格式",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/SystemSetting/QuotationTemplateFormatSetting.fxml"));
            Parent Parent = FXMLLoader.load();
            QuotationTemplateFormatSetting_Controller QuotationTemplateFormatSetting_Controller = FXMLLoader.getController();
            QuotationTemplateFormatSetting_Controller.setComponent(IAECrawlerAccount_Bean);
            QuotationTemplateFormatSetting_Controller.setStage(Stage);
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 430,800);
            Stage.setScene(Scene);
            Stage.show();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public void ShowIAECrawlerBelong(Stage MainStage,ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList, SystemSetting_Controller SystemSetting_Controller){
        try {
            Stage Stage = ComponentToolKit.setStage("所屬設定",false,false);
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ToolKit/ShowIAECrawlerBelong.fxml"));
            Parent Parent = FXMLLoader.load();
            ShowIAECrawlerBelong_Controller ShowIAECrawlerBelong_Controller = FXMLLoader.getController();
            Stage.initOwner(MainStage);
            Stage.initModality(Modality.WINDOW_MODAL);
            Scene Scene = ComponentToolKit.setScene(Parent, 900,500);
            Stage.setScene(Scene);
            ShowIAECrawlerBelong_Controller.setSystemSetting_Controller(SystemSetting_Controller);
            ShowIAECrawlerBelong_Controller.setIAECrawlerBelongList(IAECrawlerBelongList);
            ShowIAECrawlerBelong_Controller.setComponent();
            ShowIAECrawlerBelong_Controller.setStage(Stage);
            Stage.show();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
}
