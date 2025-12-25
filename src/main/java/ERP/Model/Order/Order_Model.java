package ERP.Model.Order;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.*;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.ToolKit.Installment.Installment_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowExportQuotationRecord.ExportQuotationRecord_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Item_Bean;
import ERP.Bean.ToolKit.TransactionDetail.SearchTransactionDetail_Bean;
import ERP.Enum.Invoice.Invoice_Enum.InvoiceInvalid;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.CustomerSaleModel;
import ERP.Bean.ToolKit.ProductGenerator.ProductGenerator_Bean;
import ERP.Bean.ToolKit.TransactionDetail.TransactionDetail_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.WaitConfirmTable;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import ERP.Model.ManageCustomerInfo.ManageCustomerInfo_Model;
import ERP.Model.ManageManufacturerInfo.ManageManufacturerInfo_Model;
import ERP.Model.Product.Product_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Enum.Order.Order_Enum.OrderSearchColumn;
import ERP.Enum.Order.Order_Enum.ObjectSearchColumn;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.Order.Order_Enum.OrderBorrowed;
import ERP.Enum.Order.Order_Enum.OffsetOrderStatus;
import ERP.Enum.Order.Order_Enum.OrderTaxStatus;
import ERP.Enum.Order.Order_Enum.ProductSaleStatus;
import ERP.Enum.Order.Order_Enum.generateOrderNumberMethod;
import ERP.Enum.Order.Order_Enum.ProductSearchColumn;
import ERP.Enum.Order.Order_Enum.SpecificationColumn;
import ERP.Enum.Order.Order_Enum.ReviewStatus;
import ERP.Enum.Order.Order_Enum.ReviewObject;
import ERP.Enum.Order.Order_Enum.ExportQuotationVendor;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableView;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** [ERP.Model] Establish or search Order */

public class Order_Model {
    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private Product_Model product_model;
    private ManageManufacturerInfo_Model ManageManufacturerInfo_Model;
    private ManageCustomerInfo_Model ManageCustomerInfo_Model;
    private SystemSetting_Model SystemSetting_Model;
    public Order_Model(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.product_model = ToolKit.ModelToolKit.getProductModel();
        this.ManageManufacturerInfo_Model = ToolKit.ModelToolKit.getManageManufacturerInfoModel();
        this.ManageCustomerInfo_Model = ToolKit.ModelToolKit.getManageCustomerInfoModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public int getOrderNumberLength(){  return Integer.parseInt(SystemSetting_Model.getSpecificSystemSettingData(SystemSettingConfig.貨單號長度));   }
    private int getCustomerIDAreaLength(){  return Integer.parseInt(SystemSetting_Model.getSpecificSystemSettingData(SystemSettingConfig.地區客戶位數));   }
    /**
     * Generate order date by specific format
     * @param DateFormat the format of date
     */
    public String getOrderDateBySpecificFormat(String DateFormat) {
        if (DateFormat == null) return (Integer.parseInt(ToolKit.getToday("yyyy")) - 1911) + "/" + ToolKit.getToday("MM/dd");
        else if (DateFormat.contains("/")) {
            DateFormat = DateFormat.replace("/", "-");
            int Year = Integer.parseInt(DateFormat.substring(0, DateFormat.indexOf("-"))) + 1911;
            return Year + DateFormat.substring(DateFormat.indexOf("-"));
        } else {
            int year = Integer.parseInt(DateFormat.substring(0, 4)) - 1911;
            if (DateFormat.contains("-"))
                return year + "/" + DateFormat.substring(5, 7) + "/" + DateFormat.substring(8, 10);
            else return year + "/" + DateFormat.substring(4, 6) + "/" + DateFormat.substring(6, 8);
        }
    }

    /**
     * Generate newest order number of establish order
     *
     * @param OrderSource               the source of order
     * @param generateOrderNumberMethod the method of generate order number
     * @param InputText                 the text of order date or order number. depend on how to generate order number
     */
    public String generateNewestOrderNumberOfEstablishOrder(OrderSource OrderSource, generateOrderNumberMethod generateOrderNumberMethod, String InputText, boolean isTestDB) {
        long SearchNumber = 0;
        if (generateOrderNumberMethod == Order_Enum.generateOrderNumberMethod.日期)   SearchNumber = Long.parseLong(InputText.replaceAll("-", ""));
        else if (generateOrderNumberMethod == Order_Enum.generateOrderNumberMethod.單號) SearchNumber = Long.parseLong(InputText);
        String Query = "";
        boolean orderSourceStatus = OrderSource == Order_Enum.OrderSource.採購單 ||
                OrderSource == Order_Enum.OrderSource.待入倉單 ||
                (OrderSource == Order_Enum.OrderSource.進貨子貨單 && generateOrderNumberMethod == Order_Enum.generateOrderNumberMethod.日期) ||
                OrderSource == Order_Enum.OrderSource.報價單 ||
                OrderSource == Order_Enum.OrderSource.待出貨單 ||
                (OrderSource == Order_Enum.OrderSource.出貨子貨單 && generateOrderNumberMethod == Order_Enum.generateOrderNumberMethod.日期);
        if (orderSourceStatus)
            Query = "select Max(OrderNumber) from (select Max(OrderNumber) as OrderNumber from Orders where OrderNumber like ? union " +
                    "select Max(WaitingOrderNumber) as OrderNumber from Orders where WaitingOrderNumber like ? union " +
                    "select Max(AlreadyOrderNumber) as OrderNumber from Orders where AlreadyOrderNumber like ? union " +
                    "select Max(AlreadyOrderNumber) as OrderNumber from SubBill where AlreadyOrderNumber like ?) as OrderNumber";
        else if (OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
            Query = "select Max(OrderNumber) from SubBill where OrderNumber like ?";
        else if (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
            Query = "select MAX(OrderNumber) from ReturnOrder where OrderNumber like ?";

        long OrderNumber = 0;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setString(1,SearchNumber + "%");
            if(orderSourceStatus){
                PreparedStatement.setString(2, SearchNumber + "%");
                PreparedStatement.setString(3, SearchNumber + "%");
                PreparedStatement.setString(4, SearchNumber + "%");
            }
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                OrderNumber = Rs.getLong(1);
        } catch (Exception ex) {
            ERPApplication.Logger.catching(ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
        } finally {
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }

        if (OrderNumber == 0 && (OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.進貨子貨單 ||
                OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)) {
            int orderNumberLength = getOrderNumberLength();
            return SearchNumber + ToolKit.fillZero("1",orderNumberLength - String.valueOf(SearchNumber).length());
        }else if (OrderNumber == 0 && (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)) {
            int orderNumberLength = getOrderNumberLength();
            return SearchNumber + ToolKit.fillZero("1",orderNumberLength - String.valueOf(SearchNumber).length());
        }else
            return String.valueOf(OrderNumber + 1);
    }

    /**
     * Whether the order number is exist
     * @param OrderSource the source of order
     * @param OrderNumber the number of order
     */
    public boolean isOrderNumberExist(OrderSource OrderSource, String OrderNumber) {
        boolean isOrderNumberExist = false;
        String Query;
        if (OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
            Query = "select 1 from SubBill where OrderNumber = ? and OrderSource = ?";
        else if (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
            Query = "select 1 from ReturnOrder where OrderNumber = ? and OrderSource = ?";
        else
            Query = "select 1 from Orders A left join SubBill B on A.WaitingOrderNumber = B.WaitingOrderNumber where A.OrderNumber = ? or A.WaitingOrderNumber = ? or A.AlreadyOrderNumber = ? or B.AlreadyOrderNumber = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if (OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單 ||
                    OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單) {
                PreparedStatement.setString(1, OrderNumber);
                PreparedStatement.setInt(2, OrderSource.getOrderObject().ordinal());
            }else{
                PreparedStatement.setString(1, OrderNumber);
                PreparedStatement.setString(2, OrderNumber);
                PreparedStatement.setString(3, OrderNumber);
                PreparedStatement.setString(4, OrderNumber);
            }
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                isOrderNumberExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }
        return isOrderNumberExist;
    }
    public boolean isOrderTransferWaitingOrAlready(OrderSource OrderSource, int order_id, String OrderNumber){
        boolean isTransferWaitingOrAlready = false;
        String Query = "select 1 from Orders where OrderNumber = ? and " + OrderSource.getOrderNumberColumnName() + " is not null and id = ? ";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,OrderNumber);
            PreparedStatement.setInt(2,order_id);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                isTransferWaitingOrAlready = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }
        return isTransferWaitingOrAlready;
    }
    public String isSubBillOfMainOrderTransferAlready(OrderSource OrderSource, String WaitingOrderNumber){
        String alreadyOrderNumber = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(
                "select AlreadyOrderNumber from Orders where WaitingOrderNumber = ? and AlreadyOrderDate is not null and AlreadyOrderNumber is not null and OrderSource = ?");
            PreparedStatement.setString(1,WaitingOrderNumber);
            PreparedStatement.setInt(2,OrderSource.getOrderObject().ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                alreadyOrderNumber = Rs.getString(1);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }
        return alreadyOrderNumber;
    }
    public int getAllSubBillTotalPriceIncludeTax(OrderSource OrderSource, String waitingOrderNumber, boolean isTransferAlready, Boolean isCheckout){
        int totalPriceIncludeTax = 0;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "select TotalPriceIncludeTax from SubBill A inner join SubBill_Price B on A.id = B.SubBill_id where A.WaitingOrderNumber = ? and A.OrderSource = ? ";
            if(isTransferAlready)
                query = query + "and A.AlreadyOrderNumber is not null ";
            if(isCheckout != null)
                query = query + "and A.isCheckout = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,waitingOrderNumber);
            PreparedStatement.setInt(2,OrderSource.getOrderObject().ordinal());
            if(isCheckout != null)
                PreparedStatement.setBoolean(3, isCheckout);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())
                totalPriceIncludeTax = totalPriceIncludeTax + Rs.getInt(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }
        return totalPriceIncludeTax;
    }
    public boolean isProductInStockShortage(ObservableList<OrderProduct_Bean> ProductList){
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(OrderProduct_Bean.getInStock() - OrderProduct_Bean.getQuantity() < 0)
                return true;
        }
        return false;
    }
    /** Get object from search column
     *  @param OrderObject the source of order
     *  @param ObjectSearchColumn the object column of conditional search
     * @param ObjectColumnText the text of object column. depend on which column to search object
     * */
    public ObservableList<ObjectInfo_Bean> getObjectFromSearchColumn(OrderObject OrderObject, ObjectSearchColumn ObjectSearchColumn, String ObjectColumnText){
        ObservableList<ObjectInfo_Bean> ObjectList = FXCollections.observableArrayList();
        String Query = "";
        if(OrderObject == Order_Enum.OrderObject.廠商)
            Query = "select A.*,B.*,C.*,D.* from Manufacturer A inner join Manufacturer_Phone B on A.id = B.Manufacturer_id inner join Manufacturer_Address C on A.id = C.Manufacturer_id inner join Manufacturer_PayInfo D on A.id = D.Manufacturer_id";
        else if(OrderObject == Order_Enum.OrderObject.客戶)
            Query = "select A.*,B.*,C.*,D.* from Customer A inner join Customer_Phone B on A.id = B.Customer_id inner join Customer_Address C on A.id = C.Customer_id inner join Customer_ReceiveInfo D on A.id = D.Customer_id";

        if(ObjectSearchColumn != null && ObjectColumnText != null){
            if(ObjectSearchColumn == Order_Enum.ObjectSearchColumn.廠商編號 || ObjectSearchColumn == Order_Enum.ObjectSearchColumn.客戶編號)
                Query = Query + " where A." + ObjectSearchColumn.value() + " like '" + ObjectColumnText + "%'";
            else
                Query = Query + " where A." + ObjectSearchColumn.value() + " like '%" + ObjectColumnText + "%'";
        }

        Query = Query + " order by A.ObjectID";

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
                ObjectInfo_Bean.setId(Rs.getInt("id"));
                ObjectInfo_Bean.setObjectID(Rs.getString("ObjectID"));
                ObjectInfo_Bean.setObjectName(Rs.getString("ObjectName"));
                ObjectInfo_Bean.setObjectNickName(Rs.getString("ObjectNickName"));
                if(OrderObject == Order_Enum.OrderObject.廠商){
                    ObjectInfo_Bean.setDefaultPaymentMethod(ObjectInfo_Enum.DefaultPaymentMethod.values()[Rs.getInt("DefaultPaymentMethod")]);
                    ObjectInfo_Bean.setPayableReceivableDiscount(Rs.getDouble("PayableDiscount"));
                    ObjectInfo_Bean.setPayableDay(Rs.getInt("PayableDay"));
                    ObjectInfo_Bean.setCheckTitle(Rs.getString("CheckTitle"));
                    ObjectInfo_Bean.setCheckDueDay(Rs.getInt("CheckDueDay"));
                    ObjectInfo_Bean.setDiscountRemittanceFee(ObjectInfo_Enum.DiscountRemittanceFee.values()[Rs.getInt("DiscountRemittanceFee")]);
                    ObjectInfo_Bean.setRemittanceFee(Rs.getInt("RemittanceFee"));
                    ObjectInfo_Bean.setDiscountPostage(ObjectInfo_Enum.DiscountPostage.values()[Rs.getInt("DiscountPostage")]);
                    ObjectInfo_Bean.setPostage(Rs.getInt("Postage"));
                    ObjectInfo_Bean.setBankID(Rs.getInt("BankID") == 0 ? null : Rs.getInt("BankID"));
                    ObjectInfo_Bean.setBankBranch(Rs.getString("BankBranch"));
                    ObjectInfo_Bean.setAccountName(Rs.getString("AccountName"));
                    ObjectInfo_Bean.setBankAccount(Rs.getString("BankAccount"));
                }else if(OrderObject == Order_Enum.OrderObject.客戶){
                    ObjectInfo_Bean.setMemberID(Rs.getString("MemberID"));
                    ObjectInfo_Bean.setPayableReceivableDiscount(Rs.getDouble("ReceivableDiscount"));
                    ObjectInfo_Bean.setPrintPricing(ObjectInfo_Enum.ShipmentInvoicePrintPricing.values()[Rs.getInt("PrintPricing")]);
                    ObjectInfo_Bean.setSaleModel(CustomerSaleModel.values()[Rs.getInt("SaleModel")]);
                    ObjectInfo_Bean.setSaleDiscount(Rs.getDouble("SaleDiscount"));
                    ObjectInfo_Bean.setReceivableDay(Rs.getInt("ReceivableDay"));
                    ObjectInfo_Bean.setStoreCode(Rs.getString("StoreCode"));
                }
                ObjectInfo_Bean.setCheckoutByMonth(Rs.getBoolean("IsCheckoutByMonth"));
                ObjectInfo_Bean.setPersonInCharge(Rs.getString("PersonInCharge"));
                ObjectInfo_Bean.setContactPerson(Rs.getString("ContactPerson"));
                ObjectInfo_Bean.setTelephone1(Rs.getString("Telephone1"));
                ObjectInfo_Bean.setTelephone2(Rs.getString("Telephone2"));
                ObjectInfo_Bean.setCellPhone(Rs.getString("Cellphone"));
                ObjectInfo_Bean.setFax(Rs.getString("Fax"));
                ObjectInfo_Bean.setEmail(Rs.getString("Email"));
                ObjectInfo_Bean.setCompanyAddress(Rs.getString("CompanyAddress"));
                ObjectInfo_Bean.setDeliveryAddress(Rs.getString("DeliveryAddress"));
                ObjectInfo_Bean.setInvoiceTitle(Rs.getString("InvoiceTitle"));
                ObjectInfo_Bean.setTaxIDNumber(Rs.getString("TaxIDNumber"));
                ObjectInfo_Bean.setInvoiceAddress(Rs.getString("InvoiceAddress"));
                ObjectInfo_Bean.setOrderTax(ObjectInfo_Enum.OrderTax.values()[Rs.getInt("OrderTax")]);
                ObjectInfo_Bean.setRemark(Rs.getString("Remark"));
                ObjectList.add(ObjectInfo_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ObjectList;
    }
    public boolean insertOrderPicture(int OrderID, String Base64Format, OrderSource OrderSource) {
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            int ItemNumber = getOrderPictureNewIndex(OrderID, OrderSource);
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                query = query + "insert into SubBill_Picture (SubBill_id,ItemNumber,Picture,Source) values (?,?,?,?)";
            }else{
                query = query + "insert into Orders_Picture (Order_id,ItemNumber,Picture,Source) values (?,?,?,?)";
            }
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1, OrderID);
            preparedStatement.setInt(2, ItemNumber);
            preparedStatement.setString(3, Base64Format);
            preparedStatement.setInt(4, OrderSource.getOrderObject().ordinal());
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    /** Delete order picture into database
     *  @param OrderID the id of order
     * @param OrderSource the source of order
     * */
    public boolean deleteOrderPicture(int OrderID, OrderSource OrderSource, int deleteItemNumber){
        boolean status = false;
        PreparedStatement preparedStatement = null;
        try {
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                query = query + "delete SubBill_Picture where SubBill_id = ? and Source = ? and ItemNumber = ? ";
            }else{
                query = query + "delete Orders_Picture where Order_id = ? and Source = ? and ItemNumber = ? ";
            }
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1, OrderID);
            preparedStatement.setInt(2, OrderSource.getOrderObject().ordinal());
            preparedStatement.setInt(3, deleteItemNumber);
            preparedStatement.executeUpdate();
            status = true;
            refreshPictureItemNumber(OrderID, OrderSource, deleteItemNumber);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    private void refreshPictureItemNumber(int OrderID, OrderSource OrderSource, int deleteItemNumber){
        int PictureMaxItemNumber = getOrderPictureNewIndex(OrderID,OrderSource)-1;
        if(PictureMaxItemNumber > deleteItemNumber) {
            for(int index = (deleteItemNumber == 1) ? deleteItemNumber : (PictureMaxItemNumber - deleteItemNumber); index < PictureMaxItemNumber; index++){
                PreparedStatement preparedStatement = null;
                try {
                    String query = "BEGIN TRY BEGIN TRANSACTION ";
                    if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                        query = query + "update SubBill_Picture set ItemNumber = ? where SubBill_id = ? and ItemNumber = ? and Source = ? ";
                    }else{
                        query = query + "update Orders_Picture set ItemNumber = ? where Order_id = ? and ItemNumber = ? and Source = ? ";
                    }
                    query = query + "COMMIT TRANSACTION " +
                        "END TRY " +
                        "BEGIN CATCH " +
                        "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                        "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                        "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                        "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                        "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                        "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                        "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                        "ROLLBACK TRANSACTION " +
                        "END CATCH";
                    preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
                    preparedStatement.setInt(1, index);
                    preparedStatement.setInt(2, OrderID);
                    preparedStatement.setInt(3, (index+1));
                    preparedStatement.setInt(4, OrderSource.getOrderObject().ordinal());
                    preparedStatement.executeUpdate();
                } catch (Exception Ex) {
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }finally {
                    SqlAdapter.closeConnectParameter(preparedStatement,null);
                }
            }
        }
    }
    private boolean isOrderPictureExist(Order_Bean Order_Bean, OrderSource OrderSource){
        boolean Exist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query;
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                query = "select TOP 1 * from SubBill_Picture where SubBill_id = ? and Source = ? ";
            }else{
                query = "select TOP 1 * from Orders_Picture where Order_id = ? and Source = ? ";
            }
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,Order_Bean.getOrderID());
            PreparedStatement.setInt(2,OrderSource.getOrderObject().ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  Exist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return Exist;
    }
    private int getOrderPictureNewIndex(int OrderID, OrderSource OrderSource){
        int ItemNumber = 1;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query;
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                query = "select MAX(ItemNumber) from SubBill_Picture where SubBill_id = ? and Source = ?";
            }else{
                query = "select MAX(ItemNumber) from Orders_Picture where Order_id = ? and Source = ?";
            }
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,OrderID);
            PreparedStatement.setInt(2,OrderSource.getOrderObject().ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  ItemNumber = Rs.getInt(1) + 1;
        }catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ItemNumber;
    }
    /** Get order picture
     *  @param OrderID the id of order
     * @param OrderSource the source of order
     * */
    public ArrayList<HashMap<Boolean,String>> getOrderPicture(int OrderID, OrderSource OrderSource){
        ArrayList<HashMap<Boolean,String>> OrderPictureList = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query;
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                query = "select Picture from SubBill_Picture where SubBill_id= ? and Source = ? order by ItemNumber";
            }else{
                query = "select Picture from Orders_Picture where Order_id= ? and Source = ? order by ItemNumber";
            }
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,OrderID);
            PreparedStatement.setInt(2,OrderSource.getOrderObject().ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                if(OrderPictureList == null)
                    OrderPictureList = new ArrayList<>();
                HashMap<Boolean,String> PictureMap = new HashMap<>();
                if(OrderPictureList.size() == 0)    PictureMap.put(true, Rs.getString(1));
                else    PictureMap.put(false,Rs.getString(1));
                OrderPictureList.add(PictureMap);
            }
        }catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return OrderPictureList;
    }
    /** Get the default printer for order */
    public String getOrderPictureDefaultPrinter(){
        String PrinterName = "";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select ConfigValue from SystemSetting where ConfigName= ?");
            PreparedStatement.setString(1,SystemSettingConfig.預設列表機設定.getColumnName());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  PrinterName = Rs.getString("ConfigValue");
        }catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return PrinterName;
    }
    /** Print the image of order (目前為開啟 windows 內建圖片編輯器列印) */
    public void printOrderImage(ByteArrayInputStream ByteStream, String PrinterName) {
        try {
            // 設定列印格式，如果未確定型別，可選擇autosense
            DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
            // 設定列印引數
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new Copies(1)); //份數
//            aset.add(MediaSize.ISO.A4); //紙張
            aset.add(MediaSizeName.ISO_A4); //紙張

            // aset.add(Finishings.STAPLE);//裝訂
            aset.add(Sides.DUPLEX);//單雙面
            // 定位列印服務
            PrintService printService = null;
            if (PrinterName != null) {
                //獲得本臺電腦連線的所有印表機
                PrintService[] PrintService = PrintServiceLookup.lookupPrintServices(null,null);
                if(PrintService == null || PrintService.length == 0) {
                    DialogUI.AlarmDialog("※ 列印失敗，未找到可用印表機，請檢查。");
                    return ;
                }
                //匹配指定印表機
                for (javax.print.PrintService service : PrintService) {
                    if (service.getName().equals(PrinterName)) {
                        printService = service;
                        break;
                    }
                }
                if(printService==null){
                    DialogUI.AlarmDialog("※ 列印失敗，未找到名稱為" + PrinterName + "的印表機，請檢查。");
                    return ;
                }
            }
            Doc doc = new SimpleDoc(ByteStream, flavor, null);
            assert printService != null;
            DocPrintJob job = printService.createPrintJob(); // 建立列印作業
            job.print(doc, aset);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }

    public boolean modifyOrderStatus(OrderSource orderSource, int order_id, OrderStatus orderStatus){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            if(orderSource == Order_Enum.OrderSource.採購單 || orderSource == Order_Enum.OrderSource.報價單){
                query = query + "update Orders set status = ? where id = ? and OrderSource = ?";
            }else if(orderSource == Order_Enum.OrderSource.進貨子貨單 || orderSource == Order_Enum.OrderSource.出貨子貨單){
                query = query + "update SubBill set status = ? where id = ? and OrderSource = ?";
            }else if(orderSource == Order_Enum.OrderSource.進貨退貨單 || orderSource == Order_Enum.OrderSource.出貨退貨單){
                query = query + "update ReturnOrder set status = ? where id = ? and OrderSource = ?";
            }
            query = query + " COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, orderStatus.ordinal());
            PreparedStatement.setInt(2, order_id);
            PreparedStatement.setInt(3, orderSource.getOrderObject().ordinal());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }

    /** Insert order into database
     * @param OrderSource the source of order
     * @param Order_Bean the bean of order
     * */
    public boolean insertOrder(OrderSource OrderSource, Order_Bean Order_Bean, ObservableList<OrderProduct_Bean> wannaChangeSaleStatusProductList, boolean isHandleProductStatus, boolean isTestDB) {
        if (OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.報價單) return insertQuotation(Order_Bean, wannaChangeSaleStatusProductList, isHandleProductStatus, isTestDB);
        else if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單) return insertSubBill(Order_Bean, isTestDB);
        else if (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單) return insertReturnOrder(Order_Bean, wannaChangeSaleStatusProductList, isHandleProductStatus, isTestDB);
        return false;
    }
    private boolean insertQuotation(Order_Bean Order_Bean,ObservableList<OrderProduct_Bean> wannaChangeSaleStatusProductList,boolean isHandleProductStatus, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into Orders (OrderNumber,OrderDate,OrderSource, ObjectID, isCheckout, NumberOfItems,EstablishSource, isBorrowed, isOffset, WaitingOrderDate, WaitingOrderNumber, AlreadyOrderDate, AlreadyOrderNumber,Remark,CashierRemark) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "insert into Orders_ReviewStatus (order_id,review_status,review_object) values ((select IDENT_CURRENT('Orders')),?,?) " +
                    "insert into Orders_ReviewStatus (order_id,review_status,review_object) values ((select IDENT_CURRENT('Orders')),?,?) " +
                    "insert into Orders_Price (Order_id,OrderNumber,TotalPriceNoneTax,Tax,Discount,TotalPriceIncludeTax) values((select IDENT_CURRENT('Orders')),?,?,?,?,?) " +
                    "insert into Orders_ProjectInfo (Order_id,OrderNumber,ProjectCode,ProjectName,ProjectQuantity,ProjectUnit,ProjectPriceAmount,ProjectTotalPriceNoneTax,ProjectTax,ProjectTotalPriceIncludeTax,ProjectDifferentPrice) values((select IDENT_CURRENT('Orders')),?,?,?,?,?,?,?,?,?,?) " +
                    "insert into Orders_ShoppingInfo (Order_id,OrderNumber,PurchaserName,PurchaserTelephone,PurchaserCellphone,PurchaserAddress,RecipientName,RecipientTelephone,RecipientCellphone,RecipientAddress) values((select IDENT_CURRENT('Orders')),?,?,?,?,?,?,?,?,?) " +
                    getInsertOrderReferenceQuery(Order_Bean) +
                    getInsertOrderProductQuery(Order_Bean) +
                    getInsertOrderPictureQuery(Order_Bean);
            if(isHandleProductStatus)
                Query = Query + getInsertOrderProductSaleStatusQuery(Order_Bean.getOrderSource(), wannaChangeSaleStatusProductList);

            Query = Query + "SELECT id from Orders where OrderNumber = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";

            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setLong(1, Long.parseLong(Order_Bean.getNowOrderNumber()));
            PreparedStatement.setString(2, Order_Bean.getOrderDate());
            PreparedStatement.setInt(3, Order_Bean.getOrderSource().ordinal());
            PreparedStatement.setString(4, Order_Bean.getObjectID());
            PreparedStatement.setInt(5, Order_Bean.isCheckout().ordinal());
            PreparedStatement.setString(6, Order_Bean.getNumberOfItems());
            PreparedStatement.setInt(7, Order_Bean.getEstablishSource().ordinal());
            PreparedStatement.setInt(8, Order_Bean.isBorrowed().ordinal());
            PreparedStatement.setInt(9, Order_Bean.getOffsetOrderStatus().ordinal());
            PreparedStatement.setString(10, Order_Bean.getWaitingOrderDate());
            PreparedStatement.setString(11, Order_Bean.getWaitingOrderNumber());
            PreparedStatement.setString(12, Order_Bean.getAlreadyOrderDate());
            PreparedStatement.setString(13, Order_Bean.getAlreadyOrderNumber());
            PreparedStatement.setString(14, Order_Bean.getOrderRemark());
            PreparedStatement.setString(15, Order_Bean.getCashierRemark());

            PreparedStatement.setInt(16, Order_Bean.getReviewStatusMap().containsKey(ReviewObject.貨單商品) ? Order_Bean.getReviewStatusMap().get(ReviewObject.貨單商品).ordinal() : ReviewStatus.無.ordinal());
            PreparedStatement.setInt(17, ReviewObject.貨單商品.ordinal());
            PreparedStatement.setInt(18, Order_Bean.getReviewStatusMap().containsKey(ReviewObject.商品群組) ? Order_Bean.getReviewStatusMap().get(ReviewObject.商品群組).ordinal() : ReviewStatus.無.ordinal());
            PreparedStatement.setInt(19, ReviewObject.商品群組.ordinal());

            PreparedStatement.setString(20, Order_Bean.getNowOrderNumber());
            PreparedStatement.setInt(21, Integer.parseInt(Order_Bean.getTotalPriceNoneTax()));
            PreparedStatement.setInt(22, Integer.parseInt(Order_Bean.getTax()));
            PreparedStatement.setInt(23, Integer.parseInt(Order_Bean.getDiscount()));
            PreparedStatement.setInt(24, Integer.parseInt(Order_Bean.getTotalPriceIncludeTax()));

            PreparedStatement.setString(25, Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(26, Order_Bean.getProjectCode());
            PreparedStatement.setString(27, Order_Bean.getProjectName());
            PreparedStatement.setString(28, Order_Bean.getProjectQuantity());
            PreparedStatement.setString(29, Order_Bean.getProjectUnit());
            PreparedStatement.setString(30, Order_Bean.getProjectPriceAmount());
            PreparedStatement.setString(31, Order_Bean.getProjectTotalPriceNoneTax());
            PreparedStatement.setString(32, Order_Bean.getProjectTax());
            PreparedStatement.setString(33, Order_Bean.getProjectTotalPriceIncludeTax());
            PreparedStatement.setString(34, Order_Bean.getProjectDifferentPrice());

            PreparedStatement.setString(35, Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(36, Order_Bean.getPurchaserName());
            PreparedStatement.setString(37, Order_Bean.getPurchaserTelephone());
            PreparedStatement.setString(38, Order_Bean.getPurchaserCellphone());
            PreparedStatement.setString(39, Order_Bean.getPurchaserAddress());
            PreparedStatement.setString(40, Order_Bean.getRecipientName());
            PreparedStatement.setString(41, Order_Bean.getRecipientTelephone());
            PreparedStatement.setString(42, Order_Bean.getRecipientCellphone());
            PreparedStatement.setString(43, Order_Bean.getRecipientAddress());
            int productDBIndex = 44;
            ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
            for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                PreparedStatement.setString(productDBIndex, Order_Bean.getNowOrderNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getItemNumber());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getISBN());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getProductName());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getQuantity());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getUnit());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getBatchPrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getSinglePrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPricing());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPriceAmount());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getRemark());
                productDBIndex++;
            }
            ArrayList<String> orderPictureList = Order_Bean.getOrderPictureList();
            if(orderPictureList != null && orderPictureList.size() != 0){
                for(int index = 0; index < orderPictureList.size(); index++){
                    String base64Format = orderPictureList.get(index);
                    PreparedStatement.setInt(productDBIndex, (index+1));
                    productDBIndex++;
                    PreparedStatement.setString(productDBIndex, base64Format);
                    productDBIndex++;
                    PreparedStatement.setInt(productDBIndex, Order_Bean.getOrderSource().ordinal());
                    productDBIndex++;
                }
            }
            PreparedStatement.setLong(productDBIndex, Long.parseLong(Order_Bean.getNowOrderNumber()));
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                Order_Bean.setOrderID(Rs.getInt(1));
            status = true;
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
        }finally{
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    private String getInsertOrderReferenceQuery(Order_Bean Order_Bean){
        String query = "";
        HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = Order_Bean.getOrderReferenceMap();
        if(orderReferenceMap != null){
            HashMap<Integer,Boolean> mainOrderReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.報價單);
            HashMap<Integer,Boolean> subBillReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單);
            if(Order_Bean.getOrderSource() == OrderSource.報價單){
                if(mainOrderReferenceMap.size() != 0) {
                    for(int order_id : mainOrderReferenceMap.keySet()){
                        query = query + "insert into Orders_Reference (Order_id,Order_Reference_Id,SubBill_Reference_id) values ((select IDENT_CURRENT('Orders')),'" + order_id + "',null)";
                    }
                }
                if(subBillReferenceMap.size() != 0){
                    for(int subBill_id : subBillReferenceMap.keySet()){
                        query = query + "insert into Orders_Reference (Order_id,Order_Reference_Id,SubBill_Reference_id) values ((select IDENT_CURRENT('Orders')),null,'" + subBill_id + "')";
                    }
                }
            }else if(Order_Bean.getOrderSource() == OrderSource.出貨子貨單){
                if(mainOrderReferenceMap.size() != 0) {
                    for(int order_id : mainOrderReferenceMap.keySet()){
                        query = query + "insert into SubBill_Reference (SubBill_id,SubBill_Reference_Id,Order_Reference_id) values ((select IDENT_CURRENT('SubBill')),null,'" + order_id + "')";
                    }
                }
                if(subBillReferenceMap.size() != 0){
                    for(int subBill_id : subBillReferenceMap.keySet()){
                        query = query + "insert into SubBill_Reference (SubBill_id,SubBill_Reference_Id,Order_Reference_id) values ((select IDENT_CURRENT('SubBill')),'" + subBill_id + "',null)";
                    }
                }
            }
        }
        return query;
    }
    private String getInsertOrderProductQuery(Order_Bean Order_Bean){
        ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
        String Query = "";
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(Order_Bean.getOrderSource() == OrderSource.進貨退貨單 || Order_Bean.getOrderSource() == OrderSource.出貨退貨單) {
                Query = Query + "insert into " + Order_Bean.getOrderSource().getOrderItemsTableName() + " (ReturnOrder_id,OrderNumber,ItemNumber,ISBN,ProductName,Quantity,Unit,BatchPrice,SinglePrice,Pricing,PriceAmount,Remark) values ((select IDENT_CURRENT('ReturnOrder')),?,?,?,?,?,?,?,?,?,?,?)";
            }else if(Order_Bean.getOrderSource() != OrderSource.進貨子貨單 && Order_Bean.getOrderSource() != OrderSource.出貨子貨單) {
                Query = Query + "insert into " + Order_Bean.getOrderSource().getOrderItemsTableName() + " (Order_id,OrderNumber,ItemNumber,ISBN,ProductName,Quantity,Unit,BatchPrice,SinglePrice,Pricing,PriceAmount,Remark) values ((select IDENT_CURRENT('Orders')),?,?,?,?,?,?,?,?,?,?,?)";
            }else {
                ERPApplication.Logger.info("建立子貨單品項：【S】" + OrderProduct_Bean.getSeriesNumber() + " 【N】" + OrderProduct_Bean.getItemNumber() + " 【I】" + OrderProduct_Bean.getISBN() + " 【P】" + OrderProduct_Bean.getProductName());
                Query = Query + "insert into " + Order_Bean.getOrderSource().getOrderItemsTableName() + " (SubBill_id,OrderNumber,SeriesNumber,ItemNumber,ISBN,ProductName,Quantity,Unit,BatchPrice,SinglePrice,Pricing,PriceAmount,Remark) values ((select IDENT_CURRENT('SubBill')),?,?,?,?,?,?,?,?,?,?,?,?)";
            }
        }
        return Query;
    }
    private String getInsertOrderPictureQuery(Order_Bean Order_Bean){
        String query = "";
        ArrayList<String> orderPictureList = Order_Bean.getOrderPictureList();
        if(orderPictureList != null && orderPictureList.size() != 0){
            for(String base64Format : Order_Bean.getOrderPictureList()){
                query = query + "insert into Orders_Picture (Order_id,ItemNumber,Picture,Source) values ((select IDENT_CURRENT('Orders')),?,?,?) ";
            }
        }
        return query;
    }
    private String getInsertOrderProductSaleStatusQuery(OrderSource OrderSource,ObservableList<OrderProduct_Bean> productList){
        String Query = "";
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            if(OrderSource == Order_Enum.OrderSource.進貨退貨單)
                Query = Query + handleProductSaleStatus(OrderSource, ProductSaleStatus.刪除存量, OrderProduct_Bean);
            else if(OrderSource == Order_Enum.OrderSource.出貨退貨單)
                Query = Query + handleProductSaleStatus(OrderSource, ProductSaleStatus.新增存量, OrderProduct_Bean);
            else if(OrderSource == Order_Enum.OrderSource.採購單)
                Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.待出貨單, ProductSaleStatus.新增需量, OrderProduct_Bean);
        }
        return Query;
    }
    private boolean insertSubBill(Order_Bean Order_Bean, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into SubBill (Order_id,OrderNumber,OrderDate,OrderSource, ObjectID, isCheckout, NumberOfItems, isBorrowed, isOffset, WaitingOrderDate, WaitingOrderNumber,Remark,CashierRemark) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "insert into SubBill_Price (SubBill_id,OrderNumber,TotalPriceNoneTax,Tax,Discount,TotalPriceIncludeTax) values((select IDENT_CURRENT('SubBill')),?,?,?,?,?) " +
                    "insert into SubBill_ProjectInfo (SubBill_id,OrderNumber,ProjectCode,ProjectName,ProjectQuantity,ProjectUnit,ProjectPriceAmount,ProjectTotalPriceNoneTax,ProjectTax,ProjectTotalPriceIncludeTax,ProjectDifferentPrice) values((select IDENT_CURRENT('SubBill')),?,?,?,?,?,?,?,?,?,?) " +
                    "insert into SubBill_ShoppingInfo (SubBill_id,OrderNumber,PurchaserName,PurchaserTelephone,PurchaserCellphone,PurchaserAddress,RecipientName,RecipientTelephone,RecipientCellphone,RecipientAddress) values((select IDENT_CURRENT('SubBill')),?,?,?,?,?,?,?,?,?) " +
                    getInsertOrderReferenceQuery(Order_Bean) +
                    getInsertOrderProductQuery(Order_Bean) +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setInt(1, Order_Bean.getOrderID());
            PreparedStatement.setLong(2, Long.parseLong(Order_Bean.getNowOrderNumber()));
            PreparedStatement.setString(3, Order_Bean.getOrderDate());
            if(Order_Bean.getOrderSource() == OrderSource.進貨子貨單)
                PreparedStatement.setInt(4, OrderSource.採購單.ordinal());
            else if(Order_Bean.getOrderSource() == OrderSource.出貨子貨單)
                PreparedStatement.setInt(4, OrderSource.報價單.ordinal());
            PreparedStatement.setString(5, Order_Bean.getObjectID());
            PreparedStatement.setInt(6, Order_Bean.isCheckout().ordinal());
            PreparedStatement.setString(7, Order_Bean.getNumberOfItems());
            PreparedStatement.setInt(8, Order_Bean.isBorrowed().ordinal());
            PreparedStatement.setInt(9, Order_Bean.getOffsetOrderStatus().ordinal());
            PreparedStatement.setString(10, Order_Bean.getWaitingOrderDate());
            PreparedStatement.setString(11,Order_Bean.getWaitingOrderNumber());
            PreparedStatement.setString(12,Order_Bean.getOrderRemark());
            PreparedStatement.setString(13,Order_Bean.getCashierRemark());


            PreparedStatement.setString(14, Order_Bean.getNowOrderNumber());
            PreparedStatement.setInt(15, Integer.parseInt(Order_Bean.getTotalPriceNoneTax()));
            PreparedStatement.setInt(16, Integer.parseInt(Order_Bean.getTax()));
            PreparedStatement.setInt(17, Integer.parseInt(Order_Bean.getDiscount()));
            PreparedStatement.setInt(18, Integer.parseInt(Order_Bean.getTotalPriceIncludeTax()));

            PreparedStatement.setString(19, Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(20, Order_Bean.getProjectCode());
            PreparedStatement.setString(21, Order_Bean.getProjectName());
            PreparedStatement.setString(22, Order_Bean.getProjectQuantity());
            PreparedStatement.setString(23, Order_Bean.getProjectUnit());
            PreparedStatement.setString(24, Order_Bean.getProjectPriceAmount());
            PreparedStatement.setString(25, Order_Bean.getProjectTotalPriceNoneTax());
            PreparedStatement.setString(26, Order_Bean.getProjectTax());
            PreparedStatement.setString(27, Order_Bean.getProjectTotalPriceIncludeTax());
            PreparedStatement.setString(28, Order_Bean.getProjectDifferentPrice());

            PreparedStatement.setString(29, Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(30, Order_Bean.getPurchaserName());
            PreparedStatement.setString(31, Order_Bean.getPurchaserTelephone());
            PreparedStatement.setString(32, Order_Bean.getPurchaserCellphone());
            PreparedStatement.setString(33, Order_Bean.getPurchaserAddress());
            PreparedStatement.setString(34, Order_Bean.getRecipientName());
            PreparedStatement.setString(35, Order_Bean.getRecipientTelephone());
            PreparedStatement.setString(36, Order_Bean.getRecipientCellphone());
            PreparedStatement.setString(37, Order_Bean.getRecipientAddress());
            int productDBIndex = 38;
            ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
            for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                PreparedStatement.setString(productDBIndex, Order_Bean.getNowOrderNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getSeriesNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getItemNumber());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getISBN());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getProductName());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getQuantity());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getUnit());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getBatchPrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getSinglePrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPricing());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPriceAmount());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getRemark());
                productDBIndex++;
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
        }finally {
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    private boolean insertReturnOrder(Order_Bean Order_Bean, ObservableList<OrderProduct_Bean> wannaChangeSaleStatusProductList, boolean isHandleProductStatus, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into ReturnOrder (OrderNumber, OrderDate, OrderSource, ObjectID, isCheckout, NumberOfItems, isBorrowed, Remark) values (?,?,?,?,?,?,?,?)" +
                    "insert into ReturnOrder_Price (ReturnOrder_id,OrderNumber, TotalPriceNoneTax, Tax, Discount, TotalPriceIncludeTax) values ((select IDENT_CURRENT('ReturnOrder')),?,?,?,?,?)" +
                    "insert into ReturnOrder_ProjectInfo (ReturnOrder_id,OrderNumber, ProjectCode, ProjectName) values ((select IDENT_CURRENT('ReturnOrder')),?,?,?)" +
                    getInsertOrderProductQuery(Order_Bean);
            if(isHandleProductStatus)
                Query = Query + getInsertOrderProductSaleStatusQuery(Order_Bean.getOrderSource(), wannaChangeSaleStatusProductList);
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setLong(1, Long.parseLong(Order_Bean.getNowOrderNumber()));
            PreparedStatement.setString(2, Order_Bean.getOrderDate());
            PreparedStatement.setInt(3, Order_Bean.getOrderSource().getOrderObject().ordinal());
            PreparedStatement.setString(4, Order_Bean.getObjectID());
            PreparedStatement.setInt(5, Order_Bean.isCheckout().ordinal());
            PreparedStatement.setString(6, Order_Bean.getNumberOfItems());
            PreparedStatement.setInt(7, Order_Bean.isBorrowed().ordinal());
            PreparedStatement.setString(8, Order_Bean.getOrderRemark());

            PreparedStatement.setString(9, Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(10, Order_Bean.getTotalPriceNoneTax());
            PreparedStatement.setString(11, Order_Bean.getTax());
            PreparedStatement.setString(12, Order_Bean.getDiscount());
            PreparedStatement.setString(13, Order_Bean.getTotalPriceIncludeTax());

            PreparedStatement.setString(14, Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(15, Order_Bean.getProjectCode());
            PreparedStatement.setString(16, Order_Bean.getProjectName());
            int productDBIndex = 17;
            ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
            for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                PreparedStatement.setString(productDBIndex, Order_Bean.getNowOrderNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getItemNumber());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getISBN());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getProductName());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getQuantity());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getUnit());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getBatchPrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getSinglePrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPricing());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPriceAmount());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getRemark());
                productDBIndex++;
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
        }finally {
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteWaitingOrder(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductStatus){
        OrderSource OrderSource = SearchOrder_Bean.getOrderSource();
        int order_id = SearchOrder_Bean.getId();
        String waitingOrderNumber = SearchOrder_Bean.getOrderNumber();
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update " + OrderSource.getOrderTableName() + " set WaitingOrderDate = null, WaitingOrderNumber = null where " + OrderSource.getOrderNumberColumnName() + " = ? and id = ? ";
            if(isHandleProductStatus){
                ObservableList<OrderProduct_Bean> ProductList = getOrderItems(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId());
                for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                    if(SearchOrder_Bean.getOrderObject() == OrderObject.廠商)
                        Query = Query + handleDeleteWaitingPurchaseOrderSaleStatus(OrderProduct_Bean);
                    else if(SearchOrder_Bean.getOrderObject() == OrderObject.客戶)
                        Query = Query + handleDeleteWaitingShipmentOrderSaleStatus(OrderProduct_Bean);
                }
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, waitingOrderNumber);
            preparedStatement.setInt(2, order_id);
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    private String handleDeleteWaitingPurchaseOrderSaleStatus(OrderProduct_Bean OrderProduct_Bean){
        String query = "";
        query = query + handleProductSaleStatus(OrderSource.待入倉單, ProductSaleStatus.刪除待入倉量, OrderProduct_Bean);
        query = query + handleProductSaleStatus(OrderSource.待入倉單, ProductSaleStatus.刪除待入庫存, OrderProduct_Bean);
        query = query + handleProductSaleStatus(OrderSource.待入倉單, ProductSaleStatus.判斷需量, OrderProduct_Bean);
        return query;
    }
    private String handleDeleteWaitingShipmentOrderSaleStatus(OrderProduct_Bean OrderProduct_Bean){
        String query = "";
        query = query + handleProductSaleStatus(OrderSource.待出貨單, ProductSaleStatus.刪除待出貨量, OrderProduct_Bean);
        query = query + handleProductSaleStatus(OrderSource.待出貨單, ProductSaleStatus.刪除需量, OrderProduct_Bean);
        query = query + handleProductSaleStatus(OrderSource.待出貨單, ProductSaleStatus.新增待入庫存, OrderProduct_Bean);
        return query;
    }
    public boolean deleteAlreadyOrder(SearchOrder_Bean SearchOrder_Bean, ObservableList<OrderProduct_Bean> productList, boolean isHandleProductStatus){
        OrderSource OrderSource = SearchOrder_Bean.getOrderSource();
        int order_id = SearchOrder_Bean.getId();
        String alreadyOrderNumber = SearchOrder_Bean.getOrderNumber();
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update " + OrderSource.getOrderTableName() + " set AlreadyOrderDate = null, AlreadyOrderNumber = null where " + OrderSource.getOrderNumberColumnName() + " = ? and id = ? ";
            if(isHandleProductStatus){
                if(productList == null)
                    productList = getOrderItems(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId());
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(SearchOrder_Bean.getOrderObject() == OrderObject.廠商) {
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.進貨單, ProductSaleStatus.刪除存量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.進貨單, ProductSaleStatus.新增待入倉量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.進貨單, ProductSaleStatus.新增待入庫存, OrderProduct_Bean);
                    }else if(SearchOrder_Bean.getOrderObject() == OrderObject.客戶) {
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.出貨單, ProductSaleStatus.新增存量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.出貨單, ProductSaleStatus.新增待出貨量, OrderProduct_Bean);
                    }
                }
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, alreadyOrderNumber);
            preparedStatement.setInt(2, order_id);
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    /** Delete order in database
     * @param OrderSource the source of order
     * @param OrderNumber the number of order
     * */
    public boolean deleteOrder(OrderSource OrderSource, int order_id, String OrderNumber,boolean isHandleProductStatus){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "delete from " + OrderSource.getOrderTableName() + " where OrderNumber = ? and OrderSource = ? and id = ? ";
            if(isHandleProductStatus){
                ObservableList<OrderProduct_Bean> productList = getOrderItems(OrderSource, order_id);
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(OrderSource.getOrderObject() == OrderObject.廠商)
                        Query = Query + handleProductSaleStatus(OrderSource, ProductSaleStatus.新增存量, OrderProduct_Bean);
                    else if(OrderSource.getOrderObject() == OrderObject.客戶)
                        Query = Query + handleProductSaleStatus(OrderSource, ProductSaleStatus.刪除存量, OrderProduct_Bean);
                }
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, OrderNumber);
            preparedStatement.setInt(2, OrderSource.getOrderObject().ordinal());
            preparedStatement.setInt(3, order_id);
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    /** Transfer quotation to waiting order
     * @param WaitingOrderDate the date of waiting order
     * @param WaitingOrderNumber the number of waiting order
     * @param Order_Bean the bean of order
     * @param productList the products of order
     * */
    public boolean transferWaitingOrder(String WaitingOrderDate, String WaitingOrderNumber, Order_Bean Order_Bean, ObservableList<OrderProduct_Bean> productList){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            Query = Query + "update Orders set WaitingOrderDate = ?, WaitingOrderNumber = ?  where OrderNumber = ? and OrderSource = ? ";
            if(!Order_Bean.isOffset()) {
                if(Order_Bean.getEstablishSource() == Order_Enum.EstablishSource.系統建立)
                    refreshProductSaleStatus(Order_Bean.getProductList());
                for(OrderProduct_Bean OrderProduct_Bean : productList){
                    if(Order_Bean.getOrderSource().getOrderObject() == OrderObject.廠商) {
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.待入倉單, ProductSaleStatus.新增待入倉量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.待入倉單, ProductSaleStatus.判斷待入庫存, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.待入倉單, ProductSaleStatus.刪除需量, OrderProduct_Bean);
                    }else if(Order_Bean.getOrderSource().getOrderObject() == OrderObject.客戶) {
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.待出貨單, ProductSaleStatus.新增待出貨量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.待出貨單, ProductSaleStatus.判斷待入庫存, OrderProduct_Bean);
                    }
                }
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, WaitingOrderDate);
            preparedStatement.setString(2, WaitingOrderNumber);
            preparedStatement.setString(3, Order_Bean.getNowOrderNumber());
            preparedStatement.setInt(4, Order_Bean.getOrderSource().getOrderObject().ordinal());
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    public boolean transferAlreadyOrder(String AlreadyOrderDate, String AlreadyOrderNumber, Order_Bean Order_Bean, ObservableList<OrderProduct_Bean> productList, boolean isHandleProductSaleStatus){
        OrderSource OrderSource = Order_Bean.getOrderSource();
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.待出貨單)
                Query = Query + "update Orders set AlreadyOrderDate = ?, AlreadyOrderNumber = ?  where OrderNumber = ? and OrderSource = ? ";
            else if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
                Query = Query + "update SubBill set AlreadyOrderDate = ?, AlreadyOrderNumber = ? where OrderNumber = ? and OrderSource = ? ";
            if(isHandleProductSaleStatus) {
                for(OrderProduct_Bean OrderProduct_Bean : productList) {
                    if(OrderSource.getOrderObject() == OrderObject.廠商) {
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.進貨單, ProductSaleStatus.刪除待入倉量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.進貨單, ProductSaleStatus.新增存量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.進貨單, ProductSaleStatus.刪除待入庫存, OrderProduct_Bean);
                    }else if(OrderSource.getOrderObject() == OrderObject.客戶) {
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.出貨單, ProductSaleStatus.刪除待出貨量, OrderProduct_Bean);
                        Query = Query + handleProductSaleStatus(Order_Enum.OrderSource.出貨單, ProductSaleStatus.刪除存量, OrderProduct_Bean);
                    }
                }
            }

            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, AlreadyOrderDate);
            preparedStatement.setString(2, AlreadyOrderNumber);
            preparedStatement.setString(3, Order_Bean.getNowOrderNumber());
            preparedStatement.setInt(4, OrderSource.getOrderObject().ordinal());
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    /** Find all items of Sub bill for shipment
     * @param WaitingOrderNumber the number of waiting sub bill
     * @param isTransferAlreadyOrder Whether the sub bill transfer to already order
     * */
    public HashMap<Integer, Integer> findSubBill_AllItems(OrderSource OrderSource, String WaitingOrderNumber, boolean isTransferAlreadyOrder){
        HashMap<Integer, Integer> AllSubBillItemsMap = null;
        String Query = "select B.ItemNumber,B.Quantity from SubBill A left join SubBill_Items B on A.id = B.SubBill_id where A.WaitingOrderNumber = ? and OrderSource = ? and A.order_id is not null";
        if(isTransferAlreadyOrder)  Query += " and A.AlreadyOrderNumber is not null";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,WaitingOrderNumber);
            PreparedStatement.setInt(2,OrderSource.getOrderObject().ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(AllSubBillItemsMap == null)    AllSubBillItemsMap = new HashMap<>();
                int ItemNumber = Rs.getInt(1);
                int Quantity = Rs.getInt(2);
                if(!AllSubBillItemsMap.containsKey(ItemNumber)) AllSubBillItemsMap.put(ItemNumber,Quantity);
                else    AllSubBillItemsMap.put(ItemNumber, AllSubBillItemsMap.get(ItemNumber)+Quantity);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return AllSubBillItemsMap;
    }
    public boolean isMainOrderExistInstallment(OrderObject orderObject, String waitingOrderNumber){
        boolean isExist = false;
        String Query = "select 1 from Orders_Installment A inner join Orders B on A.order_id = B.id where B.OrderSource = ? and B.WaitingOrderNumber = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setInt(1, orderObject.ordinal());
            preparedStatement.setString(2, waitingOrderNumber);
            rs = preparedStatement.executeQuery();
            if (rs.next())
                isExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return isExist;
    }
    public boolean isMainOrderExistSubBill(OrderObject orderObject, String waitingOrderNumber, Boolean isTransferAlreadyOrder, Boolean isExistCheckout){
        boolean isExist = false;
        String query = "select 1 from SubBill where WaitingOrderNumber = ? and OrderSource = ?";
        if(isExistCheckout != null){
            query = query + " and isCheckout = ?";
        }
        if(isTransferAlreadyOrder != null){
            query = query + (isTransferAlreadyOrder ? " and AlreadyOrderDate is not null and AlreadyOrderNumber is not null" : " and AlreadyOrderDate is null and AlreadyOrderNumber is null");
        }
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setString(1, waitingOrderNumber);
            preparedStatement.setInt(2, orderObject.ordinal());
            if(isExistCheckout != null){
                preparedStatement.setBoolean(3, isExistCheckout);
            }
            rs = preparedStatement.executeQuery();
            if (rs.next())
                isExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return isExist;
    }
    /** Get sub bill of order
     * @param WaitingOrderNumber the number of waiting sub bill. waiting number of sub bill is the same as waiting number of quotation
     * */
    public ArrayList<Order_Bean> getSubBillOfOrder(OrderObject OrderObject, String WaitingOrderNumber){
        ArrayList<Order_Bean> SeparateOrderList = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.OrderNumber,A.ObjectID,B.ObjectName,A.isCheckout,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber " +
                    "from SubBill A inner join " + OrderObject.getTableName() + " B on A.ObjectID = B.ObjectID where A.WaitingOrderNumber = ? and OrderSource = ?");
            PreparedStatement.setString(1,WaitingOrderNumber);
            PreparedStatement.setInt(2,OrderObject.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(SeparateOrderList == null)   SeparateOrderList = new ArrayList<>();
                Order_Bean Order_Bean = new Order_Bean();
                Order_Bean.setNowOrderNumber(Rs.getString(1));
                Order_Bean.setObjectID(Rs.getString(2));
                Order_Bean.setObjectName(Rs.getString(3));
                Order_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(4)].value());
                Order_Bean.setWaitingOrderDate(Rs.getString(5));
                Order_Bean.setWaitingOrderNumber(Rs.getString(6));
                Order_Bean.setAlreadyOrderDate(Rs.getString(7));
                Order_Bean.setAlreadyOrderNumber(Rs.getString(8));
                SeparateOrderList.add(Order_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return SeparateOrderList;
    }
    /** Whether the order exist sub bill and sub bill is none transfer to already shipment
     * @param WaitingOrderNumber the number of waiting sub bill. waiting number of sub bill is the same as waiting number of quotation
     * */
    public ArrayList<Order_Bean> isOrderExistSubBillAndSubBillNoneTransferToAlreadyShipment(OrderObject OrderObject, String WaitingOrderNumber){
        ArrayList<Order_Bean> SubBillNoneTransferShipmentList = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select A.OrderNumber,A.ObjectID,B.ObjectName,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber " +
                    "from SubBill A inner join " + OrderObject.getTableName() + " B on A.ObjectID = B.ObjectID where A.WaitingOrderNumber = ? and A.AlreadyOrderNumber is null and A.OrderSource = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,WaitingOrderNumber);
            PreparedStatement.setInt(2,OrderObject.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(SubBillNoneTransferShipmentList == null)   SubBillNoneTransferShipmentList = new ArrayList<>();
                Order_Bean Order_Bean = new Order_Bean();
                Order_Bean.setNowOrderNumber(Rs.getString(1));
                Order_Bean.setObjectID(Rs.getString(2));
                Order_Bean.setObjectName(Rs.getString(3));
                Order_Bean.setWaitingOrderDate(Rs.getString(4));
                Order_Bean.setWaitingOrderNumber(Rs.getString(5));
                Order_Bean.setAlreadyOrderDate(Rs.getString(6));
                Order_Bean.setAlreadyOrderNumber(Rs.getString(7));
                SubBillNoneTransferShipmentList.add(Order_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return SubBillNoneTransferShipmentList;
    }
    /** Search order
     * @param OrderSource the source of order
     * @param OrderSearchColumn the column of search order
     * @param conditionalOrderSearch_Bean Conditional object of search order
     * */
    public ObservableList<SearchOrder_Bean> searchOrder(OrderSource OrderSource, OrderSearchColumn OrderSearchColumn, ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        OrderObject orderObject = conditionalOrderSearch_Bean.getOrderObject();
        SearchOrderSource searchOrderSource = conditionalOrderSearch_Bean.getSearchOrderSource();
        boolean isBorrowed = conditionalOrderSearch_Bean.isBorrowed();
        OrderStatus orderStatus = conditionalOrderSearch_Bean.getOrderStatus();
        String searchKeyWord = conditionalOrderSearch_Bean.getSearchKeyWord();
        OrderSearchMethod orderSearchMethod = conditionalOrderSearch_Bean.getOrderSearchMethod();
        String startDate = conditionalOrderSearch_Bean.getStartDate();
        String endDate = conditionalOrderSearch_Bean.getEndDate();
        HashMap<ReviewStatus,Boolean> searchOrderReviewStatus = conditionalOrderSearch_Bean.getSearchOrderReviewStatus();

        ObservableList<SearchOrder_Bean> OrderList = FXCollections.observableArrayList();
        String MainQuery = getOrderSourceQuery(OrderSource);
        String BorrowQuery = getIsBorrowedQuery(isBorrowed);
        String DateQuery = getOrderSearchDateQuery(searchOrderSource, OrderSource, orderSearchMethod, startDate, endDate);

        if(orderSearchMethod == Order_Enum.OrderSearchMethod.日期搜尋 && searchKeyWord.equals("")){
            if(!BorrowQuery.equals(""))
                MainQuery = MainQuery + " and " + BorrowQuery;
            MainQuery = MainQuery + " and " + DateQuery;
        }else{
            if((searchOrderSource == SearchOrderSource.待入倉與子貨單 && OrderSource == Order_Enum.OrderSource.待入倉單) || (searchOrderSource == SearchOrderSource.待出貨與子貨單 && OrderSource == Order_Enum.OrderSource.待出貨單))
                MainQuery = MainQuery + "and " + "A.WaitingOrderDate is not null and A.WaitingOrderNumber is not null ";
            else if(OrderSource == Order_Enum.OrderSource.進貨單 ||
                    OrderSource == Order_Enum.OrderSource.出貨單 ||
                    (searchOrderSource == Order_Enum.SearchOrderSource.進貨單 && OrderSource == Order_Enum.OrderSource.待入倉單) ||
                    (searchOrderSource == Order_Enum.SearchOrderSource.出貨單 && OrderSource == Order_Enum.OrderSource.待出貨單) ||
                    ((searchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單 || searchOrderSource == Order_Enum.SearchOrderSource.進貨單) && OrderSource == Order_Enum.OrderSource.進貨子貨單) ||
                    ((searchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單 || searchOrderSource == Order_Enum.SearchOrderSource.出貨單) && OrderSource == Order_Enum.OrderSource.出貨子貨單))
                MainQuery = MainQuery + "and " + "A.AlreadyOrderDate is not null and A.AlreadyOrderNumber is not null ";

            MainQuery = MainQuery + "and " + getOrderSearchColumnQuery(OrderSource.getOrderObject(), OrderSearchColumn, searchKeyWord, conditionalOrderSearch_Bean.isSearchTextByOr());
            if(!BorrowQuery.equals("")) MainQuery = MainQuery + " and " + BorrowQuery;
            if(!DateQuery.equals(""))  MainQuery = MainQuery + " and " + DateQuery;
        }
//        Query = Query + getSortByOrderNumber(OrderSource);

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(MainQuery);
            PreparedStatement.setInt(1,OrderSource.getOrderObject().ordinal());
            PreparedStatement.setInt(2,orderStatus.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
                SearchOrder_Bean.setCheckBoxSelect(false);
                SearchOrder_Bean.setOrderSource(OrderSource);
                SearchOrder_Bean.setOrderNumber(Rs.getString(1));
                SearchOrder_Bean.setOrderDate(Rs.getString(2));
                SearchOrder_Bean.setOrderObject(orderObject);
                SearchOrder_Bean.setObjectID(Rs.getString(3));
                SearchOrder_Bean.setObjectName(Rs.getString(4));
                SearchOrder_Bean.setPersonInCharge(Rs.getString(5));
                SearchOrder_Bean.setNumberOfItems(Rs.getInt(6));
                SearchOrder_Bean.setTotalPriceNoneTax(Rs.getInt(7));
                SearchOrder_Bean.setTax(Rs.getInt(8));
                SearchOrder_Bean.setDiscount(Rs.getInt(9));
                SearchOrder_Bean.setTotalPriceIncludeTax(Rs.getInt(10));

                SearchOrder_Bean.setProductGroupTotalPriceNoneTax(Rs.getObject(11) == null ? null : Rs.getInt(11));
                SearchOrder_Bean.setProductGroupTax(Rs.getObject(12) == null ? null : Rs.getInt(12));
                SearchOrder_Bean.setProductGroupDiscount(Rs.getObject(13) == null ? null : Rs.getInt(13));
                SearchOrder_Bean.setProductGroupTotalPriceIncludeTax(Rs.getObject(14) == null ? null : Rs.getInt(14));
                SearchOrder_Bean.setInvoiceNumber(Rs.getString(15));
                SearchOrder_Bean.setProjectCode(Rs.getString(16));
                SearchOrder_Bean.setProjectName(Rs.getString(17));
                SearchOrder_Bean.setProjectTotalPriceIncludeTax(Rs.getString(18));
                SearchOrder_Bean.setProjectDifferentPrice(Rs.getString(19));
                SearchOrder_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(20)].value());

                if (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單){
                    SearchOrder_Bean.setId(Rs.getInt(21));
                    SearchOrder_Bean.setOrderStatus(OrderStatus.values()[Rs.getInt(26)]);
                }else {
                    SearchOrder_Bean.setId(Rs.getInt(27));
                    SearchOrder_Bean.setCashierRemark(Rs.getString(28));
                    SearchOrder_Bean.setOrderStatus(OrderStatus.values()[Rs.getInt(33)]);
                }

                if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單) {
                    SearchOrder_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(21)]);
                    SearchOrder_Bean.setPictureCount(Rs.getObject(31) == null ? null : Rs.getInt(31));
                }
                if(OrderSource != Order_Enum.OrderSource.進貨子貨單 && OrderSource != Order_Enum.OrderSource.進貨退貨單 &&
                        OrderSource != Order_Enum.OrderSource.出貨子貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單) {
                    SearchOrder_Bean.setEstablishSource(Order_Enum.EstablishSource.values()[Rs.getInt(22)]);
                    SearchOrder_Bean.setReviewStatus(ReviewStatus.values()[Rs.getInt(29)],ReviewStatus.values()[Rs.getInt(30)]);
                }else
                    SearchOrder_Bean.setEstablishSource(Order_Enum.EstablishSource.人工建立);

                if(OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 ||
                        OrderSource == Order_Enum.OrderSource.出貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
                    SearchOrder_Bean.setExportQuotationManufacturerNickName(Rs.getString(32));

                if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.報價單){
                    SearchOrder_Bean.setQuotationNumber(Rs.getString(1));
                    SearchOrder_Bean.setQuotationDate(Rs.getString(2));
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(23));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(24));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(25));
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(26));
                }else if(OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.待出貨單){
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(1));
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(2));
                    SearchOrder_Bean.setQuotationDate(Rs.getString(23));
                    SearchOrder_Bean.setQuotationNumber(Rs.getString(24));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(25));
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(26));
                }else if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(23));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(24));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(25));
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(26));
                }else if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單){
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(1));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(2));
                    SearchOrder_Bean.setQuotationDate(Rs.getString(23));
                    SearchOrder_Bean.setQuotationNumber(Rs.getString(24));
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(25));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(26));
                }
                if((OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 ||
                        OrderSource == Order_Enum.OrderSource.出貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨單)
                        && SearchOrder_Bean.getInvoiceNumber() == null){
                    SearchOrder_Bean.setInvoiceNumber(getInvalidInvoice(OrderSource,SearchOrder_Bean.getId()));
                }
                if(SearchOrder_Bean.isCheckout())
                    SearchOrder_Bean.setBalance("0");
                else if(OrderSource == Order_Enum.OrderSource.出貨單){
                    int subBillTotalPriceIncludeTax = getAllSubBillTotalPriceIncludeTax(OrderSource,SearchOrder_Bean.getWaitingOrderNumber(),true,true);
                    SearchOrder_Bean.setBalance(String.valueOf(SearchOrder_Bean.getTotalPriceIncludeTax()-subBillTotalPriceIncludeTax));
                }else
                    SearchOrder_Bean.setBalance(String.valueOf(SearchOrder_Bean.getTotalPriceIncludeTax()));
                if(searchOrderReviewStatus != null &&
                        (searchOrderReviewStatus.get(ReviewStatus.待審查) || searchOrderReviewStatus.get(ReviewStatus.待修改) || searchOrderReviewStatus.get(ReviewStatus.審查通過))){
                    if((searchOrderReviewStatus.get(ReviewStatus.待審查) && (SearchOrder_Bean.getItemReviewStatus() == ReviewStatus.待審查 || SearchOrder_Bean.getGroupReviewStatus() == ReviewStatus.待審查)) ||
                            (searchOrderReviewStatus.get(ReviewStatus.待修改) && (SearchOrder_Bean.getItemReviewStatus() == ReviewStatus.待修改 || SearchOrder_Bean.getGroupReviewStatus() == ReviewStatus.待修改))||
                            (searchOrderReviewStatus.get(ReviewStatus.審查通過) && (SearchOrder_Bean.getItemReviewStatus() == ReviewStatus.審查通過 || SearchOrder_Bean.getGroupReviewStatus() == ReviewStatus.審查通過))){
                        OrderList.add(SearchOrder_Bean);
                    }
                }else{
                    OrderList.add(SearchOrder_Bean);
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return OrderList;
    }
    private String getOrderSourceQuery(OrderSource OrderSource){
        if(OrderSource == Order_Enum.OrderSource.採購單)
            return "select A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax, E.total_price_none_tax, E.tax, E.discount, E.total_price_include_tax , '', C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.EstablishSource, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber,A.id,A.CashierRemark," +
//                    "E.review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 0) as item_review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 1) as group_review_status," +
                    "case when EXISTS(select 1 from Orders_Picture where Order_id = A.id) then (select Count(*) from Orders_Picture where Order_id = A.id) else null end as PictureCount," +
                    "'',A.status " +
                    "from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_ProjectInfo C on A.id = C.Order_id inner join Orders_Price D on A.id = D.Order_id left join Orders_ProductGroup_Price E on A.id = E.order_id where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.待入倉單)
            return "select A.WaitingOrderNumber, A.WaitingOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax, E.total_price_none_tax, E.tax, E.discount, E.total_price_include_tax, '', C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.EstablishSource, A.OrderDate, A.OrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber,A.id,A.CashierRemark," +
//                    "E.review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 0) as item_review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 1) as group_review_status," +
                    "case when EXISTS(select 1 from Orders_Picture where Order_id = A.id) then (select Count(*) from Orders_Picture where Order_id = A.id) else null end as PictureCount," +
                    "'',A.status " +
                    "from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_ProjectInfo C on A.id = C.Order_id inner join Orders_Price D on A.id = D.Order_id left join Orders_ProductGroup_Price E on A.id = E.order_id where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.進貨子貨單)
            return "select A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax,null,null,null,null, '', C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset,'' , A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber,A.id,A.CashierRemark,'',''," +
                    "case when EXISTS(select 1 from SubBill_Picture where SubBill_id = A.id) then (select Count(*) from SubBill_Picture where SubBill_id = A.id) else null end as PictureCount," +
                    "'',A.Status from SubBill A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join SubBill_ProjectInfo C on A.id = C.SubBill_id inner join SubBill_Price D on A.id = D.SubBill_id left join Orders_InvoiceInfo E on A.id = E.SubBill_id and (E.Invalid != 1 and A.AlreadyOrderNumber is not null and E.SubBill_id is not null) where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.進貨單)
            return "select A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax, E.total_price_none_tax, E.tax, E.discount, E.total_price_include_tax, '', C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.EstablishSource, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber,A.id,A.CashierRemark," +
//                    "E.review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 0) as item_review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 1) as group_review_status," +
                    "case when EXISTS(select 1 from Orders_Picture where Order_id = A.id) then (select Count(*) from Orders_Picture where Order_id = A.id) else null end as PictureCount," +
                    "'',A.status " +
                    "from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_ProjectInfo C on A.id = C.Order_id inner join Orders_Price D on A.id = D.Order_id left join Orders_ProductGroup_Price E on A.id = E.order_id where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單)
            return "select A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax,null,null,null,null, '', C.ProjectCode, C.ProjectName, '', '', A.isCheckout,A.id,'','',null,'',A.status from ReturnOrder A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join ReturnOrder_ProjectInfo C on A.id = C.ReturnOrder_id inner join ReturnOrder_Price D on A.id = D.ReturnOrder_id where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.報價單)
            return "select A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax, F.total_price_none_tax, F.tax, F.discount, F.total_price_include_tax, E.InvoiceNumber, C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.EstablishSource, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber,A.id,A.CashierRemark," +
//                    "F.review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 0) as item_review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 1) as group_review_status," +
                    "case when EXISTS(select 1 from Orders_Picture where Order_id = A.id) then (select Count(*) from Orders_Picture where Order_id = A.id) else null end as PictureCount," +
                    "case when E.invoice_manufacturerNickName_id is null then " +
                    "(select T2.ManufacturerNickName from Orders_ExportQuotationRecord T1 inner join IAECrawlerAccount_ExportQuotation_Manufacturer T2 on T1.export_manufacturer_id=T2.id where order_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM Orders_ExportQuotationRecord where order_id = A.id)) else " +
                    "(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = E.invoice_manufacturerNickName_id) end," +
                    "A.status " +
                    " from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_Price D on A.id = D.Order_id left join Orders_InvoiceInfo E on A.id = E.Order_id and (E.Invalid != 1 and A.WaitingOrderNumber is not null and E.Order_id is not null) left join Orders_ProductGroup_Price F on A.id = F.order_id  where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.待出貨單)
            return "select A.WaitingOrderNumber, A.WaitingOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax, F.total_price_none_tax, F.tax, F.discount, F.total_price_include_tax, E.InvoiceNumber, C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.EstablishSource, A.OrderDate, A.OrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber,A.id,A.CashierRemark," +
//                    "F.review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 0) as item_review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 1) as group_review_status," +
                    "case when EXISTS(select 1 from Orders_Picture where Order_id = A.id) then (select Count(*) from Orders_Picture where Order_id = A.id) else null end as PictureCount," +
                    "case when E.invoice_manufacturerNickName_id is null then " +
                    "(select T2.ManufacturerNickName from Orders_ExportQuotationRecord T1 inner join IAECrawlerAccount_ExportQuotation_Manufacturer T2 on T1.export_manufacturer_id=T2.id where order_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM Orders_ExportQuotationRecord where order_id = A.id)) else " +
                    "(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = E.invoice_manufacturerNickName_id) end," +
                    "A.status " +
                    " from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_Price D on A.id = D.Order_id left join Orders_InvoiceInfo E on A.id = E.Order_id and (E.Invalid != 1 and A.WaitingOrderNumber is not null and E.Order_id is not null) left join Orders_ProductGroup_Price F on A.id = F.order_id where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)
            return "select A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax,null,null,null,null, E.InvoiceNumber, C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset,'' , A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber,A.id,A.CashierRemark,'',''," +
                    "case when EXISTS(select 1 from SubBill_Picture where SubBill_id = A.id) then (select Count(*) from SubBill_Picture where SubBill_id = A.id) else null end as PictureCount," +
                    "case when E.invoice_manufacturerNickName_id is null then " +
                    "(select T2.ManufacturerNickName from SubBill_ExportQuotationRecord T1 inner join IAECrawlerAccount_ExportQuotation_Manufacturer T2 on T1.export_manufacturer_id=T2.id where subBill_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM SubBill_ExportQuotationRecord where subBill_id = A.id)) else " +
                    "(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = E.invoice_manufacturerNickName_id) end," +
                    "A.status " +
                    " from SubBill A left join Customer B on A.ObjectID = B.ObjectID left join SubBill_ProjectInfo C on A.id = C.SubBill_id left join SubBill_Price D on A.id = D.SubBill_id left join Orders_InvoiceInfo E on A.id = E.SubBill_id and (E.Invalid != 1 and A.WaitingOrderNumber is not null and E.SubBill_id is not null) where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.出貨單)
            return "select A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax, F.total_price_none_tax, F.tax, F.discount, F.total_price_include_tax, E.InvoiceNumber, C.ProjectCode, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.EstablishSource, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber,A.id,A.CashierRemark," +
//                    "F.review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 0) as item_review_status," +
                    "(select review_status from Orders_ReviewStatus where order_id = A.id and review_object = 1) as group_review_status," +
                    "case when EXISTS(select 1 from Orders_Picture where Order_id = A.id) then (select Count(*) from Orders_Picture where Order_id = A.id) else null end as PictureCount," +
                    "case when E.invoice_manufacturerNickName_id is null then " +
                    "(select T2.ManufacturerNickName from Orders_ExportQuotationRecord T1 inner join IAECrawlerAccount_ExportQuotation_Manufacturer T2 on T1.export_manufacturer_id=T2.id where order_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM Orders_ExportQuotationRecord where order_id = A.id)) else " +
                    "(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = E.invoice_manufacturerNickName_id) end," +
                    "A.status " +
                    " from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_Price D on A.id = D.Order_id left join Orders_InvoiceInfo E on A.id = E.Order_id and (E.Invalid != 1  and A.WaitingOrderNumber is not null and E.Order_id is not null) left join Orders_ProductGroup_Price F on A.id = F.order_id where A.OrderSource = ? and A.status = ? ";
        else if(OrderSource == Order_Enum.OrderSource.出貨退貨單)
            return "select A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceNoneTax, D.Tax, D.Discount, D.TotalPriceIncludeTax,null,null,null,null, '', C.ProjectCode, C.ProjectName, '', '', A.isCheckout,A.id,'','',null,'',A.status from ReturnOrder A inner join Customer B on A.ObjectID = B.ObjectID inner join ReturnOrder_ProjectInfo C on A.id = C.ReturnOrder_id inner join ReturnOrder_Price D on A.id = D.ReturnOrder_id where A.OrderSource = ? and A.status = ? ";
        else    return "";
    }
    private String getOrderSearchColumnQuery(OrderObject orderObject, OrderSearchColumn orderSearchColumn, String searchKeyWord, boolean isSearchTextByOr){
        if(orderSearchColumn == Order_Enum.OrderSearchColumn.對象編號與名稱) {
            if(ToolKit.isDigital(searchKeyWord)) return "(A.ObjectID = '" +  searchKeyWord + "')";
            else    return "(A.ObjectID = '" +  searchKeyWord + "' or B.ObjectName like '%" + searchKeyWord + "%')";
        }else if(orderSearchColumn == Order_Enum.OrderSearchColumn.對象與專案名稱) {
            String query = "";
            if(searchKeyWord.contains(" ")){
                String[] keyWordArray = searchKeyWord.split(" ");
                query = getComplexSearchTextQuery(orderObject, isSearchTextByOr, keyWordArray);
            }else{
                query = query + "A.ObjectID = '" + searchKeyWord + "' or B.ObjectName like '%" + searchKeyWord + "%' or C.ProjectName like '%" + searchKeyWord + "%'";
            }
            query = "(" + query + ")";
            ERPApplication.Logger.info(query);
            return query;
        }else if(orderSearchColumn == Order_Enum.OrderSearchColumn.專案編號) {
            return "C.ProjectCode like '%" + searchKeyWord.substring(1) + "%'";
        }else if(orderSearchColumn == Order_Enum.OrderSearchColumn.發票號碼) {
            return "E.InvoiceNumber = '" + searchKeyWord + "'";
        }else
            return "A." + orderSearchColumn.value() + " = '" +  searchKeyWord + "'";
    }
    private String getComplexSearchTextQuery(OrderObject orderObject, boolean isSearchTextByOr, String[] keyWordArray){
        String query = "";
        String objectID = findSearchObjectIDString(orderObject, keyWordArray);
        for(int index = 0; index < keyWordArray.length; index++){
            String keyWord = keyWordArray[index];
            if(isSearchTextByOr){
                query = query + "A.ObjectID = '" + keyWord + "' or ";
                query = query + "B.ObjectName like '%" + keyWord + "%' or ";
                query = query + "C.ProjectName like '%" + keyWord + "%'";
                if(index != keyWordArray.length-1) {
                    query = query + " or ";
                }
            }else{
                if(objectID != null){
                    if(query.equals("")){
                        query = query + "A.ObjectID = '" + objectID + "' and (";
                    }
                    boolean needAnd = false;

                    if(index != keyWordArray.length-1){
                        if(index != 0){
                            if(!(keyWordArray[index+1].equals(objectID)) || objectID.equals(keyWord)){
                                needAnd = true;
                            }
                        }else if(!objectID.equals(keyWord)){
                            needAnd = true;
                        }
                    }
                    if(!objectID.equals(keyWord)){
                        query = query + "(B.ObjectName like '%" + keyWord + "%' or C.ProjectName like '%" + keyWord + "%')";
                    }
                    if(needAnd && (index+1 != keyWordArray.length) && (keyWordArray[index+1].equals(objectID))){
                        needAnd = false;
                    }

                    query = query + (needAnd ? " and " : "");
                    query = query + (index == keyWordArray.length-1 ? ")" : "");
                }else{
                    query = query + "(B.ObjectName like '%" + keyWord + "%' or C.ProjectName like '%" + keyWord + "%')";
                    if(index != keyWordArray.length-1) {
                        query = query + " and ";
                    }
                }
            }
        }
        return query;
    }
    private String findSearchObjectIDString(OrderObject orderObject, String[] keyWordArray){
        String objectID = null;
        for (String keyWord : keyWordArray) {
            if (isSearchObjectIDString(orderObject, keyWord)) {
                objectID = keyWord;
                break;
            }
        }
        return objectID;
    }
    private boolean isSearchObjectIDString(OrderObject orderObject, String keyWord){
        if(orderObject == OrderObject.客戶){
            return (ToolKit.isDigital(keyWord) && ManageCustomerInfo_Model.isCustomerIDExist(keyWord,false)) ||
                    ((keyWord.length()-1) == getCustomerIDAreaLength() && ManageCustomerInfo_Model.isCustomerIDExist(keyWord,false));
        }else return orderObject == OrderObject.廠商 && ToolKit.isDigital(keyWord) && ManageManufacturerInfo_Model.isManufacturerIDExist(keyWord,false);
    }
    private String getIsBorrowedQuery(boolean isBorrowed){
        if(isBorrowed)  return "A.isBorrowed = " + Order_Enum.OrderBorrowed.已借測.ordinal();
        return "";
    }
    private String getOrderSearchDateQuery(SearchOrderSource SearchOrderSource, OrderSource OrderSource, OrderSearchMethod OrderSearchMethod, String StartDate, String EndDate){
        if(OrderSearchMethod == Order_Enum.OrderSearchMethod.日期搜尋){
            if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.報價單)
                return "A.OrderDate between '" + StartDate + "' and '" + EndDate + "'";
            else if(OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.待出貨單)
                return "A.WaitingOrderDate between '" + StartDate + "' and '" + EndDate + "'";
            else if(OrderSource == Order_Enum.OrderSource.進貨子貨單) {
                if(SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單)   return "A.OrderDate between '" + StartDate + "' and '" + EndDate + "'";
                else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單)   return "A.AlreadyOrderDate between '" + StartDate + "' and '" + EndDate + "'";
            }else if(OrderSource == Order_Enum.OrderSource.出貨子貨單) {
                if(SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單)   return "A.OrderDate between '" + StartDate + "' and '" + EndDate + "'";
                else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單)   return "A.AlreadyOrderDate between '" + StartDate + "' and '" + EndDate + "'";
            }else if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單) {
                return "A.AlreadyOrderDate between '" + StartDate + "' and '" + EndDate + "'";
            }else if(OrderSource == Order_Enum.OrderSource.進貨退貨單 ||OrderSource == Order_Enum.OrderSource.出貨退貨單)
                return "A.OrderDate between '" + StartDate + "' and '" + EndDate + "'";
        }
        return "";
    }
    public String getInvalidInvoice(OrderSource OrderSource, int order_id){
        String invoiceNumber = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query;
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
                query = "select InvoiceNumber from Orders_InvoiceInfo where SubBill_id = ? and Invalid = ?";
            else
                query = "select InvoiceNumber from Orders_InvoiceInfo where Order_id = ? and Invalid = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, order_id);
            PreparedStatement.setInt(2, InvoiceInvalid.已作廢.ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                invoiceNumber = Rs.getString(1);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return invoiceNumber == null ? "" : "(廢)" + invoiceNumber;
    }
    private String getOrderProgressSearchDateQuery(OrderSearchMethod OrderSearchMethod, String StartDate, String EndDate){
        if(OrderSearchMethod == Order_Enum.OrderSearchMethod.日期搜尋)  return "A.OrderDate between '" + StartDate + "' and '" + EndDate + "'";
        return "";
    }
    private String getSortByOrderNumber(OrderSource OrderSource){
        if(OrderSource == Order_Enum.OrderSource.報價單)
            return " order by A.OrderNumber desc";
        else if(OrderSource == Order_Enum.OrderSource.待出貨單)
            return " order by A.WaitingOrderNumber desc";
        else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)
            return " order by A.OrderNumber desc";
        else if(OrderSource == Order_Enum.OrderSource.出貨單)
            return "order by A.AlreadyOrderNumber desc";
        else if(OrderSource == Order_Enum.OrderSource.出貨退貨單)
            return " order by A.OrderNumber desc";
        return "";
    }
    /** Search order progress
     * @param OrderSearchMethod the method of search order
     * @param OrderSearchColumn the column of search order
     * @param OrderObject the object of order
     * @param isBorrowed whether the order is borrowed
     * @param SearchKeyWord the key word of search column
     * @param StartDate the start date of conditional search
     * @param EndDate the end date of conditional search
     * */
    public ObservableList<SearchOrderProgress_Bean> searchOrderProgress(OrderSearchMethod OrderSearchMethod, OrderSearchColumn OrderSearchColumn, OrderObject OrderObject, boolean isBorrowed, String SearchKeyWord, String StartDate, String EndDate, boolean isSearchTextByOr){
        ObservableList<SearchOrderProgress_Bean> SearchOrderProgressList = FXCollections.observableArrayList();
        String MainQuery = "";
        if(OrderObject == Order_Enum.OrderObject.廠商) {
            MainQuery = "select A.ObjectID,B.ObjectName,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.ProjectCode,C.ProjectName,A.isBorrowed,A.isOffset,A.isCheckout,D.review_status,'' from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_ProjectInfo C on A.id = C.Order_id inner join Orders_ReviewStatus D on A.id = D.order_id and D.review_object = '0' where A.OrderSource = ? and A.status = '0' ";
        }else if(OrderObject == Order_Enum.OrderObject.客戶) {
            MainQuery = "select A.ObjectID,B.ObjectName,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.ProjectCode,C.ProjectName,A.isBorrowed,A.isOffset,A.isCheckout,D.review_status," +
                    "case when E.invoice_manufacturerNickName_id is null then " +
                    "(select T2.ManufacturerNickName from Orders_ExportQuotationRecord T1 inner join IAECrawlerAccount_ExportQuotation_Manufacturer T2 on T1.export_manufacturer_id=T2.id where order_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM Orders_ExportQuotationRecord where order_id = A.id)) else " +
                    "(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = E.invoice_manufacturerNickName_id) end " +
                    " from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_ProjectInfo C on A.id = C.Order_id inner join Orders_ReviewStatus D on A.id = D.order_id and D.review_object = '0' LEFT JOIN Orders_InvoiceInfo E on A.id = E.Order_id and E.Invalid = '0' where A.OrderSource = ? and A.status = '0' ";
        }
        String BorrowQuery = getIsBorrowedQuery(isBorrowed);
        String DateQuery = getOrderProgressSearchDateQuery(OrderSearchMethod, StartDate, EndDate);

        if(OrderSearchMethod == Order_Enum.OrderSearchMethod.日期搜尋 && SearchKeyWord.equals("")){
            if(!BorrowQuery.equals(""))   MainQuery = MainQuery + " and " + BorrowQuery + " and "+ DateQuery;
            else    MainQuery = MainQuery + "and " + DateQuery;
        }else{
            MainQuery = MainQuery + "and " + getOrderSearchColumnQuery(OrderObject, OrderSearchColumn, SearchKeyWord, isSearchTextByOr);
            if(!BorrowQuery.equals("")) MainQuery = MainQuery + " and " + BorrowQuery;
            if(!DateQuery.equals("")) MainQuery = MainQuery + " and " + DateQuery;
        }

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
//            ERPApplication.Logger.info(MainQuery);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(MainQuery);
            PreparedStatement.setInt(1,OrderObject.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                SearchOrderProgress_Bean SearchOrderProgress_Bean = new SearchOrderProgress_Bean();
                SearchOrderProgress_Bean.setObjectID(Rs.getString(1));
                SearchOrderProgress_Bean.setObjectName(Rs.getString(2));
                SearchOrderProgress_Bean.setQuotationDate(Rs.getString(3));
                SearchOrderProgress_Bean.setQuotationNumber(Rs.getString(4));
                SearchOrderProgress_Bean.setWaitingOrderDate(Rs.getString(5));
                SearchOrderProgress_Bean.setWaitingOrderNumber(Rs.getString(6));
                SearchOrderProgress_Bean.setAlreadyOrderDate(Rs.getString(7));
                SearchOrderProgress_Bean.setAlreadyOrderNumber(Rs.getString(8));
                SearchOrderProgress_Bean.setSubBillList(getSubBillList(OrderObject,SearchOrderProgress_Bean.getWaitingOrderNumber()));
                SearchOrderProgress_Bean.setProjectCode(Rs.getString(9));
                SearchOrderProgress_Bean.setProjectName(Rs.getString(10));
                SearchOrderProgress_Bean.setIsBorrowed(Order_Enum.OrderBorrowed.values()[Rs.getInt(11)].value());
                SearchOrderProgress_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(12)]);
                SearchOrderProgress_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(13)].value());
                SearchOrderProgress_Bean.setReviewStatus(ReviewStatus.values()[Rs.getInt(14)]);
                SearchOrderProgress_Bean.setExportQuotationManufacturerNickName(Rs.getString(15));
                SearchOrderProgressList.add(SearchOrderProgress_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return SearchOrderProgressList;
    }
    private ObservableList<Order_Bean> getSubBillList(OrderObject OrderObject, String WaitingOrderNumber){
        String Query = "select A.OrderNumber,A.ObjectID,B.ObjectName,A.isCheckout,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber from SubBill A left join Customer B on A.ObjectID = B.ObjectID where A.WaitingOrderNumber = ? and A.OrderSource = ?";
        ObservableList<Order_Bean> SubBillList = FXCollections.observableArrayList();
        if(WaitingOrderNumber == null)   return SubBillList;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
//            ERP.ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,WaitingOrderNumber);
            PreparedStatement.setInt(2,OrderObject.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                Order_Bean Order_Bean = new Order_Bean();
                Order_Bean.setNowOrderNumber(Rs.getString(1));
                Order_Bean.setObjectID(Rs.getString(2));
                Order_Bean.setObjectName(Rs.getString(3));
                Order_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(4)].value());
                Order_Bean.setWaitingOrderDate(Rs.getString(5));
                Order_Bean.setWaitingOrderNumber(Rs.getString(6));
                Order_Bean.setAlreadyOrderDate(Rs.getString(7));
                Order_Bean.setAlreadyOrderNumber(Rs.getString(8));
                SubBillList.add(Order_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return SubBillList;
    }
    public boolean updateExportQuotationVendor(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update IAECrawlerAccount_ExportQuotation_Manufacturer set DefaultSelect = '0' " +
                    "update IAECrawlerAccount_ExportQuotation_Manufacturer set DefaultSelect = '1' where Manufacturer_id = (select id from Manufacturer where ObjectID = ?)  and Account = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setString(2,IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean insertExportQuotationItem(ExportQuotationItem ExportQuotationItem){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into ExportQuotation_Item (ItemName,Discount,DefaultSelect) values (?,?,?) " +
                    "SELECT id from ExportQuotation_Item where ItemName = ? and Discount = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ExportQuotationItem.getItemName());
            PreparedStatement.setBigDecimal(2,BigDecimal.valueOf(ExportQuotationItem.getPriceByDiscount()));
            PreparedStatement.setBoolean(3, ExportQuotationItem.isCheckBoxSelect());
            PreparedStatement.setString(4, ExportQuotationItem.getItemName());
            PreparedStatement.setBigDecimal(5,BigDecimal.valueOf(ExportQuotationItem.getPriceByDiscount()));
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                ExportQuotationItem.setId(Rs.getInt(1));
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public boolean updateExportQuotationItemDefaultSelect(int id, boolean isSelect){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update ExportQuotation_Item set DefaultSelect = ? where id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setBoolean(1,isSelect);
            PreparedStatement.setInt(2,id);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public boolean modifyExportQuotationItemPriceDiscount(int id, double priceDiscount){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update ExportQuotation_Item set Discount = ? where id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setBigDecimal(1,BigDecimal.valueOf(priceDiscount/100));
            PreparedStatement.setInt(2,id);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteExportQuotationItem(int id){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from ExportQuotation_Item where id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,id);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public ObservableList<ExportQuotationItem> getExportQuotationItem(SplitMenuButton ExportQuotation_SplitMenuButton){
        ObservableList<ExportQuotationItem> exportQuotationItemList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from ExportQuotation_Item");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ExportQuotationItem ExportQuotationItem = new ExportQuotationItem(ExportQuotation_SplitMenuButton, Rs.getString("ItemName"),null,Rs.getDouble("Discount"));
                ExportQuotationItem.setId(Rs.getInt("id"));
                ExportQuotationItem.setCheckBoxSelect(Rs.getBoolean("DefaultSelect"));
                exportQuotationItemList.add(ExportQuotationItem);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return exportQuotationItemList;
    }
    public int getExportQuotationRecordCount(int orderOrSubBill_id, Order_Enum.OrderSource OrderSource){
        int count = 0;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query;
            if(OrderSource == Order_Enum.OrderSource.出貨子貨單) query = "select Count(*) from SubBill_ExportQuotationRecord A left join IAECrawlerAccount_ExportQuotation_Manufacturer B on A.export_manufacturer_id = B.id where subBill_id = ?";
            else    query = "select Count(*) from Orders_ExportQuotationRecord A left join IAECrawlerAccount_ExportQuotation_Manufacturer B on A.export_manufacturer_id = B.id where order_id = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,orderOrSubBill_id);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                count = Rs.getInt(1);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return count;
    }
    public ObservableList<ExportQuotationRecord_Bean> getExportQuotationRecord(int orderOrSubBill_id, Order_Enum.OrderSource OrderSource){
        ObservableList<ExportQuotationRecord_Bean> exportQuotationRecordList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            int itemNumber = 1;
            String query;
            if(OrderSource == Order_Enum.OrderSource.出貨子貨單) {
                query = "select A.*,B.ManufacturerNickName from SubBill_ExportQuotationRecord A left join IAECrawlerAccount_ExportQuotation_Manufacturer B on A.export_manufacturer_id = B.id where subBill_id = ? order by insert_dateTime desc";
            }else {
                query = "select A.*,B.ManufacturerNickName from Orders_ExportQuotationRecord A left join IAECrawlerAccount_ExportQuotation_Manufacturer B on A.export_manufacturer_id = B.id where order_id = ? order by insert_dateTime desc";
            }
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,orderOrSubBill_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ExportQuotationRecord_Bean ExportQuotationRecord_Bean = new ExportQuotationRecord_Bean();
                ExportQuotationRecord_Bean.setItemNumber(itemNumber);
                ExportQuotationRecord_Bean.setExportContent(Order_Enum.ExportQuotationContent.values()[Rs.getInt("export_content")].name());
                ExportQuotationRecord_Bean.setExportFormat(Order_Enum.ExportQuotationFormat.values()[Rs.getInt("export_format")].name());
                ExportQuotationRecord_Bean.setVendorName(Rs.getString("ManufacturerNickName") == null ? ExportQuotationVendor.noneInvoice.getDescribe() : Rs.getString("ManufacturerNickName"));
                ExportQuotationRecord_Bean.setExportStatus(Rs.getBoolean("status"));
                ExportQuotationRecord_Bean.setInsertDateTime(Rs.getString("insert_dateTime"));
                ExportQuotationRecord_Bean.setExportObject(ReviewObject.values()[Rs.getInt("export_object")]);
                ExportQuotationRecord_Bean.setTotalPriceIncludeTax(Rs.getString("export_totalPriceIncludeTax"));
                exportQuotationRecordList.add(ExportQuotationRecord_Bean);
                itemNumber = itemNumber+1;
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return exportQuotationRecordList;
    }
    public boolean insertExportQuotationRecord(int orderOrSubBill_id, int export_quotation_id, ExportQuotation_Bean exportQuotation_bean, boolean exportStatus){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";

            if(exportQuotation_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單) {
                Query = Query +
                        "insert into SubBill_ExportQuotationRecord (subBill_id,export_content,export_format,export_manufacturer_id,status,insert_dateTime,export_object,export_totalPriceIncludeTax) values (?,?,?,?,?,?,?,?) ";
            }else {
                Query = Query +
                        "insert into Orders_ExportQuotationRecord (order_id,export_content,export_format,export_manufacturer_id,status,insert_dateTime,export_object,export_totalPriceIncludeTax) values (?,?,?,?,?,?,?,?) ";
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,orderOrSubBill_id);
            PreparedStatement.setInt(2,exportQuotation_bean.getExportContent().ordinal());
            PreparedStatement.setInt(3,exportQuotation_bean.getExportFormat().ordinal());
            PreparedStatement.setObject(4,export_quotation_id == 0 ? null : export_quotation_id);
            PreparedStatement.setInt(5,exportStatus ? 1:0);
            PreparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            PreparedStatement.setInt(7, exportQuotation_bean.isExportGroup() ? ReviewObject.商品群組.ordinal() : ReviewObject.貨單商品.ordinal());
            PreparedStatement.setInt(8, exportQuotation_bean.getExport_TotalPriceIncludeTax());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    /** Get order info
     * @param OrderSource the source of order
     * @param Order_Bean the bean of order
     * */
    public Order_Bean getOrderInfo(OrderSource OrderSource, Order_Bean Order_Bean){
        String OrderNumber = "";
        if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.出貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
            OrderNumber = Order_Bean.getNowOrderNumber();
        else if(OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.待出貨單)
            OrderNumber = Order_Bean.getWaitingOrderNumber();
        else if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單)
            OrderNumber = Order_Bean.getAlreadyOrderNumber();
        String Query;

        if(OrderSource == Order_Enum.OrderSource.採購單 || OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.進貨單)
            Query = "select A.id,A.OrderNumber,A.OrderDate,A.isCheckout,A.isOffset,A.NumberOfItems,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax,F.total_price_none_tax,F.tax,F.discount,F.total_price_include_tax,A.isBorrowed,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.UpdateDateTime,A.Remark,A.CashierRemark,B.ProjectCode,B.ProjectName,B.ProjectQuantity,B.ProjectUnit,B.ProjectPriceAmount,B.ProjectTotalPriceNoneTax,B.ProjectTax,B.ProjectTotalPriceIncludeTax,B.ProjectDifferentPrice,D.PurchaserName,D.PurchaserTelephone,D.PurchaserCellphone,D.PurchaserAddress,D.RecipientName,D.RecipientTelephone,D.RecipientCellphone,D.RecipientAddress,null,null,null,E.ObjectID,E.ObjectName,A.Status,''," +
                    "case when exists(select 1 from Orders_Reference where Order_Id = A.id and (Order_Reference_Id is not null or SubBill_Reference_Id is not null)) then '1' else '0' end, " +
                    "case when exists(select 1 from Orders_Installment where order_id = A.id) then 1 else 0 END as 'isExistInstallment' " +
                    "from " + OrderSource.getOrderTableName() + " A inner join Orders_ProjectInfo B on A.id = B.Order_id inner join Orders_Price C on A.id = C.Order_id inner join Orders_ShoppingInfo D on A.id = D.Order_id inner join Manufacturer E on A.ObjectID = E.ObjectID left join Orders_ProductGroup_Price F on A.id = F.order_id where A." + OrderSource.getOrderNumberColumnName() + " = ?";
        else if(OrderSource == Order_Enum.OrderSource.進貨子貨單)
            Query = "select A.id,A.OrderNumber,A.OrderDate,A.isCheckout,A.isOffset,A.NumberOfItems,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax,null,null,null,null,A.isBorrowed,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.UpdateDateTime,A.Remark,A.CashierRemark,B.ProjectCode,B.ProjectName,B.ProjectQuantity,B.ProjectUnit,B.ProjectPriceAmount,B.ProjectTotalPriceNoneTax,B.ProjectTax,B.ProjectTotalPriceIncludeTax,B.ProjectDifferentPrice,D.PurchaserName,D.PurchaserTelephone,D.PurchaserCellphone,D.PurchaserAddress,D.RecipientName,D.RecipientTelephone,D.RecipientCellphone,D.RecipientAddress,null,null,null,E.ObjectID,E.ObjectName,A.Status,''," +
                    "case when exists(select 1 from SubBill_Reference where SubBill_Id = A.id and (SubBill_Reference_Id is not null or Order_Reference_Id is not null)) then '1' else '0' end,'0' " +
                    "from " + OrderSource.getOrderTableName() + " A inner join SubBill_ProjectInfo B on A.id = B.SubBill_id inner join SubBill_Price C on A.id = C.SubBill_id inner join SubBill_ShoppingInfo D on A.id = D.SubBill_id inner join Manufacturer E on A.ObjectID = E.ObjectID where A." + OrderSource.getOrderNumberColumnName() + " = ?";
        else if(OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 || OrderSource == Order_Enum.OrderSource.出貨單)
            Query = "select A.id,A.OrderNumber,A.OrderDate,A.isCheckout,A.isOffset,A.NumberOfItems,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax,H.total_price_none_tax,H.tax,H.discount,H.total_price_include_tax,A.isBorrowed,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.UpdateDateTime,A.Remark,A.CashierRemark,B.ProjectCode,B.ProjectName,B.ProjectQuantity,B.ProjectUnit,B.ProjectPriceAmount,B.ProjectTotalPriceNoneTax,B.ProjectTax,B.ProjectTotalPriceIncludeTax,B.ProjectDifferentPrice,D.PurchaserName,D.PurchaserTelephone,D.PurchaserCellphone,D.PurchaserAddress,D.RecipientName,D.RecipientTelephone,D.RecipientCellphone,D.RecipientAddress,F.InvoiceDate,F.InvoiceNumber,G.Account,E.ObjectID,E.ObjectName,A.Status," +
                    "(select export_manufacturer_id from Orders_ExportQuotationRecord where order_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM Orders_ExportQuotationRecord where order_id = A.id))," +
                    "case when exists(select 1 from Orders_Reference where Order_Id = A.id and (Order_Reference_Id is not null or SubBill_Reference_Id is not null)) then '1' else '0' end, " +
                    "case when exists(select 1 from Orders_Installment where order_id = A.id) then 1 else 0 END as 'isExistInstallment' " +
                    "from " + OrderSource.getOrderTableName() + " A inner join Orders_ProjectInfo B on A.id = B.Order_id inner join Orders_Price C on A.id = C.Order_id inner join Orders_ShoppingInfo D on A.id = D.Order_id inner join Customer E on A.ObjectID = E.ObjectID left join Orders_InvoiceInfo F on A.id = F.Order_id and F.Invalid != 1 left join IAECrawlerAccount_ExportQuotation_Manufacturer G on F.invoice_manufacturerNickName_id = G.id left join Orders_ProductGroup_Price H on A.id = H.order_id where A." + OrderSource.getOrderNumberColumnName() + " = ?";
        else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)
            Query = "select A.id,A.OrderNumber,A.OrderDate,A.isCheckout,A.isOffset,A.NumberOfItems,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax,null,null,null,null,A.isBorrowed,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.UpdateDateTime,A.Remark,A.CashierRemark,B.ProjectCode,B.ProjectName,B.ProjectQuantity,B.ProjectUnit,B.ProjectPriceAmount,B.ProjectTotalPriceNoneTax,B.ProjectTax,B.ProjectTotalPriceIncludeTax,B.ProjectDifferentPrice,D.PurchaserName,D.PurchaserTelephone,D.PurchaserCellphone,D.PurchaserAddress,D.RecipientName,D.RecipientTelephone,D.RecipientCellphone,D.RecipientAddress,F.InvoiceDate,F.InvoiceNumber,G.Account,E.ObjectID,E.ObjectName,A.Status," +
                    "(select export_manufacturer_id from SubBill_ExportQuotationRecord where subBill_id = A.id and insert_dateTime = (SELECT MAX(insert_dateTime) FROM SubBill_ExportQuotationRecord where subbill_id = A.id))," +
                    "case when exists(select 1 from SubBill_Reference where SubBill_Id = A.id and (SubBill_Reference_Id is not null or Order_Reference_Id is not null)) then '1' else '0' end,'0' " +
                    "from " + OrderSource.getOrderTableName() + " A inner join SubBill_ProjectInfo B on A.id = B.SubBill_id inner join SubBill_Price C on A.id = C.SubBill_id inner join SubBill_ShoppingInfo D on A.id = D.SubBill_id inner join Customer E on A.ObjectID = E.ObjectID left join Orders_InvoiceInfo F on A.id = F.SubBill_id and F.Invalid != 1 left join IAECrawlerAccount_ExportQuotation_Manufacturer G on F.invoice_manufacturerNickName_id = G.id where A." + OrderSource.getOrderNumberColumnName() + " = ?";
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單)
            Query = "select A.id,A.OrderNumber,A.OrderDate,A.isCheckout,'',A.NumberOfItems,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax,null,null,null,null,A.isBorrowed,'','','','',A.UpdateDateTime,A.Remark,'',B.ProjectCode,B.ProjectName,'','','','','','','','','','','','','','','',null,null,null,D.ObjectID,D.ObjectName,A.Status,'','','0' from " + OrderSource.getOrderTableName() + " A inner join ReturnOrder_ProjectInfo B on A.id = B.ReturnOrder_id inner join ReturnOrder_Price C on A.id = C.ReturnOrder_id inner join Manufacturer D on A.ObjectID = D.ObjectID where A." + OrderSource.getOrderNumberColumnName() + " = ?";
        else
            Query = "select A.id,A.OrderNumber,A.OrderDate,A.isCheckout,'',A.NumberOfItems,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax,null,null,null,null,A.isBorrowed,'','','','',A.UpdateDateTime,A.Remark,'',B.ProjectCode,B.ProjectName,'','','','','','','','','','','','','','','',null,null,null,D.ObjectID,D.ObjectName,A.Status,'','','0' from " + OrderSource.getOrderTableName() + " A inner join ReturnOrder_ProjectInfo B on A.id = B.ReturnOrder_id inner join ReturnOrder_Price C on A.id = C.ReturnOrder_id inner join Customer D on A.ObjectID = D.ObjectID where A." + OrderSource.getOrderNumberColumnName() + " = ?";
//        ERP.ERPApplication.Logger.info(Query);
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,OrderNumber);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                Order_Bean.setOrderID(Rs.getObject(1) == null ? null : Rs.getInt(1));
                Order_Bean.setOrderSource(OrderSource);
                Order_Bean.setNowOrderNumber(Rs.getString(2));
                if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.進貨子貨單 ||
                        OrderSource == Order_Enum.OrderSource.出貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)  Order_Bean.setOrderDate(Rs.getString("OrderDate"));
                else    Order_Bean.setOrderDate(Rs.getString(3));
                Order_Bean.setObjectID(Rs.getString(43));
                Order_Bean.setObjectName(Rs.getString(44));
                Order_Bean.setOrderStatus(OrderStatus.values()[Rs.getInt(45)]);
                Order_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(4)].value());
                if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單) Order_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.未沖帳);
                else    Order_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(5)]);
                Order_Bean.setNumberOfItems(Rs.getString(6));
                Order_Bean.setTotalPriceNoneTax(Rs.getString(7));
                Order_Bean.setTax(Rs.getString(8));
                Order_Bean.setDiscount(Rs.getString(9));
                Order_Bean.setTotalPriceIncludeTax(Rs.getString(10));

                Order_Bean.setProductGroupTotalPriceNoneTax(Rs.getObject(11) == null ? null : Rs.getString(11));
                Order_Bean.setProductGroupTax(Rs.getObject(12) == null ? null : Rs.getString(12));
                Order_Bean.setProductGroupDiscount(Rs.getObject(13) == null ? null : Rs.getString(13));
                Order_Bean.setProductGroupTotalPriceIncludeTax(Rs.getObject(14) == null ? null : Rs.getString(14));

                Order_Bean.setIsBorrowed(Order_Enum.OrderBorrowed.values()[Rs.getInt(15)].value());
                if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單){
                    Order_Bean.setWaitingOrderDate(Rs.getString(16));
                    Order_Bean.setWaitingOrderNumber(Rs.getString(17));
                    Order_Bean.setAlreadyOrderDate(Rs.getString(18));
                    Order_Bean.setAlreadyOrderNumber(Rs.getString(19));
                }

                Order_Bean.setUpdateDateTime(Rs.getString(20));
                Order_Bean.setOrderRemark(Rs.getString(21));
                Order_Bean.setCashierRemark(Rs.getString(22));

                Order_Bean.setProjectCode(Rs.getString(23));
                Order_Bean.setProjectName(Rs.getString(24));

                if(OrderSource == Order_Enum.OrderSource.報價單 || OrderSource == Order_Enum.OrderSource.待出貨單 ||
                        OrderSource == Order_Enum.OrderSource.出貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
                    Order_Bean.setExportQuotation_ManufacturerId(Rs.getObject(46) == null ? null : Rs.getInt(46));
                else
                    Order_Bean.setExportQuotation_ManufacturerId(null);
                if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單)
                    Order_Bean.setExistOrderReference(Rs.getInt(47) != 0);

                Order_Bean.setExistInstallment(Rs.getBoolean(48));

                if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單){
                    Order_Bean.setProjectQuantity(Rs.getString(25));
                    Order_Bean.setProjectUnit(Rs.getString(26));
                    Order_Bean.setProjectPriceAmount(Rs.getString(27));
                    Order_Bean.setProjectTotalPriceNoneTax(Rs.getString(28));
                    Order_Bean.setProjectTax(Rs.getString(29));
                    Order_Bean.setProjectTotalPriceIncludeTax(Rs.getString(30));
                    Order_Bean.setProjectDifferentPrice(Rs.getString(31));

                    Order_Bean.setPurchaserName(Rs.getString(32));
                    Order_Bean.setPurchaserTelephone(Rs.getString(33));
                    Order_Bean.setPurchaserCellphone(Rs.getString(34));
                    Order_Bean.setPurchaserAddress(Rs.getString(35));
                    Order_Bean.setRecipientName(Rs.getString(36));
                    Order_Bean.setRecipientTelephone(Rs.getString(37));
                    Order_Bean.setRecipientCellphone(Rs.getString(38));
                    Order_Bean.setRecipientAddress(Rs.getString(39));

                    if(Order_Bean.getWaitingOrderNumber() != null) {
                        Order_Bean.setInvoiceDate(Rs.getString(40));
                        Order_Bean.setInvoiceNumber(Rs.getString(41));
                        Order_Bean.setInvoice_exportQuotation_Account(Rs.getString(42));
                    }
                }else{
                    Order_Bean.setProjectQuantity("");
                    Order_Bean.setProjectUnit("");
                    Order_Bean.setProjectPriceAmount("");
                    Order_Bean.setProjectTotalPriceNoneTax("");
                    Order_Bean.setProjectTax("");
                    Order_Bean.setProjectTotalPriceIncludeTax("");
                    Order_Bean.setProjectDifferentPrice("");

                    Order_Bean.setPurchaserName("");
                    Order_Bean.setPurchaserTelephone("");
                    Order_Bean.setPurchaserCellphone("");
                    Order_Bean.setPurchaserAddress("");
                    Order_Bean.setRecipientName("");
                    Order_Bean.setRecipientTelephone("");
                    Order_Bean.setRecipientCellphone("");
                    Order_Bean.setRecipientAddress("");
                }
            }else   Order_Bean = null;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        if(Order_Bean != null){
            if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨退貨單){
                Order_Bean.setExistPicture(isOrderPictureExist(Order_Bean, OrderSource));
            }
            if(OrderSource != Order_Enum.OrderSource.進貨退貨單 && OrderSource != Order_Enum.OrderSource.進貨子貨單 &&
                    OrderSource != Order_Enum.OrderSource.出貨退貨單 && OrderSource != Order_Enum.OrderSource.出貨子貨單)
                getOrderReviewStatus(Order_Bean);
        }
        return Order_Bean;
    }
    private void getOrderReviewStatus(Order_Bean Order_Bean){
        String Query = "select review_object,review_status from Orders_ReviewStatus where order_id = ? ";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Order_Bean.getOrderID());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ReviewObject ReviewObject = Order_Enum.ReviewObject.values()[Rs.getInt(1)];
                ReviewStatus ReviewStatus = Order_Enum.ReviewStatus.values()[Rs.getInt(2)];
                Order_Bean.setReviewStatusMap(ReviewObject,ReviewStatus);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
    }
    /** Get order items
     * @param OrderSource the source of order
     * @param Order_id the id of quotation.
     * */
    public ObservableList<OrderProduct_Bean> getOrderItems(OrderSource OrderSource, int Order_id){
        ObservableList<OrderProduct_Bean> OrderItems = FXCollections.observableArrayList();
        String Query;

        if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單)
            Query = "select B.id,A.id,A.SeriesNumber, A.ISBN, B.InternationalCode, B.FirmCode, B.ProductCode, A.ProductName, A.Quantity, A.Unit, " +
                    "A.BatchPrice, A.SinglePrice, A.Pricing, E.VipPrice1, E.VipPrice2, E.VipPrice3, A.PriceAmount, A.Remark, B.InStock, B.SafetyStock, C.WaitingPurchaseQuantity, C.WaitingIntoInStock, C.NeededPurchaseQuantity, C.WaitingShipmentQuantity, B.SupplyStatus, F.NewFirstCategory " +
                    "from " + OrderSource.getOrderItemsTableName() + " A inner join Store B on A.ISBN = B.ISBN inner join ProductSaleStatus C on B.id = C.store_id inner join SubBill D on A.SubBill_id = D.id inner join Store_Price E on B.id = E.store_id inner join store_category F on B.id = F.store_id where A.SubBill_id = ?";
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
            Query = "select B.id,A.id,A.ItemNumber, A.ISBN, B.InternationalCode, B.FirmCode, B.ProductCode, A.ProductName, A.Quantity, A.Unit, " +
                    "A.BatchPrice, A.SinglePrice, A.Pricing, E.VipPrice1, E.VipPrice2, E.VipPrice3, A.PriceAmount, A.Remark, B.InStock, B.SafetyStock, C.WaitingPurchaseQuantity, C.WaitingIntoInStock, C.NeededPurchaseQuantity, C.WaitingShipmentQuantity ,B.SupplyStatus, F.NewFirstCategory " +
                    "from " + OrderSource.getOrderItemsTableName() + " A inner join Store B on A.ISBN = B.ISBN inner join ProductSaleStatus C on B.id = C.store_id inner join ReturnOrder D on A.ReturnOrder_id = D.id inner join Store_Price E on B.id = E.store_id inner join store_category F on B.id = F.store_id where A.ReturnOrder_id = ?";
        else
            Query = "select B.id,A.id,A.ItemNumber, A.ISBN, B.InternationalCode, B.FirmCode, B.ProductCode, A.ProductName, A.Quantity, A.Unit, " +
                    "A.BatchPrice, A.SinglePrice, A.Pricing, E.VipPrice1, E.VipPrice2, E.VipPrice3, A.PriceAmount, A.Remark, B.InStock, B.SafetyStock, C.WaitingPurchaseQuantity, C.WaitingIntoInStock, C.NeededPurchaseQuantity, C.WaitingShipmentQuantity ,B.SupplyStatus, F.NewFirstCategory " +
                    "from " + OrderSource.getOrderItemsTableName() + " A inner join Store B on A.ISBN = B.ISBN inner join ProductSaleStatus C on B.id = C.store_id inner join Orders D on A.Order_id = D.id inner join Store_Price E on B.id = E.store_id inner join store_category F on B.id = F.store_id where A.Order_id = ?";
        Query = Query + " order by A.ItemNumber";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Order_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setStore_id(Rs.getInt(1));
                OrderProduct_Bean.setItem_id(Rs.getObject(2) == null ? null : Rs.getInt(2));
                OrderProduct_Bean.setItemNumber(Rs.getInt(3));
                OrderProduct_Bean.setISBN(Rs.getString(4));
                OrderProduct_Bean.setInternationalCode(Rs.getString(5));
                OrderProduct_Bean.setFirmCode(Rs.getString(6));
                OrderProduct_Bean.setProductCode(Rs.getString(7));
                OrderProduct_Bean.setProductName(Rs.getString(8));
                OrderProduct_Bean.setQuantity(Rs.getInt(9));
                OrderProduct_Bean.setUnit(Rs.getString(10));
                OrderProduct_Bean.setBatchPrice(Rs.getDouble(11));
                OrderProduct_Bean.setSinglePrice(Rs.getDouble(12));
                OrderProduct_Bean.setPricing(Rs.getDouble(13));
                OrderProduct_Bean.setVipPrice1(Rs.getDouble(14));
                OrderProduct_Bean.setVipPrice2(Rs.getDouble(15));
                OrderProduct_Bean.setVipPrice3(Rs.getDouble(16));
                OrderProduct_Bean.setPriceAmount(Rs.getInt(17));
                OrderProduct_Bean.setRemark(Rs.getString(18));
                OrderProduct_Bean.setInStock(Rs.getInt(19));
                OrderProduct_Bean.setSafetyStock(Rs.getInt(20));
                OrderProduct_Bean.setWaitingPurchaseQuantity(Rs.getInt(21));
                OrderProduct_Bean.setWaitingIntoInStock(Rs.getInt(22));
                OrderProduct_Bean.setNeededPurchaseQuantity(Rs.getInt(23));
                OrderProduct_Bean.setWaitingShipmentQuantity(Rs.getInt(24));
                OrderProduct_Bean.setSupplyStatus(Rs.getString(25));
                OrderProduct_Bean.setFirstCategory(Rs.getString(26));
                OrderItems.add(OrderProduct_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return OrderItems;
    }
    public Integer refresh_Order_Item_Id(int order_id, OrderProduct_Bean OrderProduct_Bean){
        Integer item_id = null;
        String Query = "select id from Orders_Items where Order_id = ? and ItemNumber = ? and ISBN = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,order_id);
            PreparedStatement.setInt(2,OrderProduct_Bean.getItemNumber());
            PreparedStatement.setString(3,OrderProduct_Bean.getISBN());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                item_id = Rs.getObject(1) == null ? null : Rs.getInt(1);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return item_id;
    }
    public HashMap<Integer,HashMap<ProductGroup_Bean,LinkedHashMap<Integer,ItemGroup_Bean>>> getProductGroupByOrderId(int order_id){
        HashMap<Integer,HashMap<ProductGroup_Bean,LinkedHashMap<Integer,ItemGroup_Bean>>> productGroupMap = null;
        String query = "select B.id,B.ItemNumber,B.GroupName,B.Quantity,B.Unit,B.SinglePrice,B.PriceAmount,B.SmallQuantity,B.SmallSinglePrice,B.SmallPriceAmount,B.IsCombined,A.item_quantity*C.SinglePrice,A.id,A.item_id,A.item_quantity,C.ISBN,C.ProductName" +
                " from Orders_Items_Group A inner join ProductGroup B on A.group_id = B.id inner join Orders_Items C on A.item_id = C.id where C.Order_id = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,order_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(productGroupMap == null) productGroupMap = new HashMap<>();

                int group_id = Rs.getInt(1);
                String groupName = Rs.getString(3);
                int itemNumber = Rs.getInt(2);
                int quantity = Rs.getInt(4);
                String unit = Rs.getString(5);
                double singlePrice = Rs.getDouble(6);
                int priceAmount = Rs.getInt(7);
                Integer smallQuantity = Rs.getObject(8) == null ? null : Rs.getInt(8);
                Double smallSinglePrice = Rs.getObject(9) == null ? null : Rs.getDouble(9);
                Integer smallPriceAmount = Rs.getObject(10) == null ? null : Rs.getInt(10);
                boolean isCombined = Rs.getBoolean(11);
                double originalSinglePrice = Rs.getDouble(12);

                if(!productGroupMap.containsKey(itemNumber)) {
                    ProductGroup_Bean ProductGroup_Bean = new ProductGroup_Bean();
                    ProductGroup_Bean.setItemNumber(itemNumber);
                    ProductGroup_Bean.setId(group_id);
                    ProductGroup_Bean.setGroupName(groupName);
                    ProductGroup_Bean.setQuantity(quantity);
                    ProductGroup_Bean.setCombineGroup(isCombined);
                    ProductGroup_Bean.setUnit(unit);
                    ProductGroup_Bean.setSinglePrice(singlePrice);
                    ProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(originalSinglePrice));
                    ProductGroup_Bean.setPriceAmount(priceAmount);
                    ProductGroup_Bean.setSmallQuantity(smallQuantity);
                    ProductGroup_Bean.setSmallSinglePrice(smallSinglePrice == null ? null : ToolKit.RoundingDouble(smallSinglePrice));
                    ProductGroup_Bean.setSmallPriceAmount(smallPriceAmount);
                    productGroupMap.put(itemNumber, new HashMap<ProductGroup_Bean, LinkedHashMap<Integer,ItemGroup_Bean>>(){{put(ProductGroup_Bean, new LinkedHashMap<>());}});
                }else {
                    ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(itemNumber).keySet().iterator().next();
                    ProductGroup_Bean.setOriginalSinglePrice(ToolKit.RoundingDouble(ProductGroup_Bean.getOriginalSinglePrice() + ToolKit.RoundingDouble(originalSinglePrice)));
                }
                HashMap<ProductGroup_Bean,LinkedHashMap<Integer,ItemGroup_Bean>> itemGroupMap = productGroupMap.get(itemNumber);

                int orders_items_group_id = Rs.getInt(13);
                int item_id = Rs.getInt(14);
                int item_quantity = Rs.getInt(15);
                String isbn = Rs.getString(16);
                String productName = Rs.getString(17);

                ItemGroup_Bean ItemGroup_Bean = new ItemGroup_Bean(orders_items_group_id, item_id, item_quantity, group_id, isbn, productName);
                itemGroupMap.get(itemGroupMap.entrySet().iterator().next().getKey()).put(item_id,ItemGroup_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return productGroupMap;
    }
    public boolean updateItemsProductGroup(HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> originalProductGroupMap,
                                           HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> newProductGroupMap){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            if(originalProductGroupMap != null){
                for(Integer group_itemNumber : originalProductGroupMap.keySet()){
                    ProductGroup_Bean ProductGroup_Bean = originalProductGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                    HashMap<Integer, ItemGroup_Bean> itemMap = originalProductGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                    query = query + "delete from ProductGroup where id = '" + ProductGroup_Bean.getId() + "' ";
                    for(Integer item_id : itemMap.keySet()){
                        ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                        query = query + "delete from Orders_Items_Group where id = '" + ItemGroup_Bean.getId() + "' ";
                    }
                }
            }
            for(Integer group_itemNumber : newProductGroupMap.keySet()){
                ProductGroup_Bean ProductGroup_Bean = newProductGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                query = query + "insert into ProductGroup(ItemNumber,GroupName,Quantity,Unit,SinglePrice,PriceAmount,SmallQuantity,SmallSinglePrice,SmallPriceAmount,IsCombined) values (?,?,?,?,?,?,?,?,?,?) ";
                HashMap<Integer, ItemGroup_Bean> itemMap = newProductGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                for(Integer item_id : itemMap.keySet()){
                    ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                    query = query + "insert into Orders_Items_Group(item_id,item_quantity,group_id) values (?,?,(select IDENT_CURRENT('ProductGroup'))) ";
                }
            }
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            int index = 1;
            for(Integer group_itemNumber : newProductGroupMap.keySet()){
                ProductGroup_Bean ProductGroup_Bean = newProductGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                PreparedStatement.setInt(index,ProductGroup_Bean.getItemNumber());
                index = index + 1;
                PreparedStatement.setString(index,ProductGroup_Bean.getGroupName());
                index = index + 1;
                PreparedStatement.setInt(index,ProductGroup_Bean.getQuantity());
                index = index + 1;
                PreparedStatement.setString(index,ProductGroup_Bean.getUnit());
                index = index + 1;
                PreparedStatement.setDouble(index,ProductGroup_Bean.getSinglePrice());
                index = index + 1;
                PreparedStatement.setDouble(index,ProductGroup_Bean.getPriceAmount());
                index = index + 1;
                PreparedStatement.setObject(index,ProductGroup_Bean.getSmallQuantity());
                index = index + 1;
                PreparedStatement.setObject(index,ProductGroup_Bean.getSmallSinglePrice());
                index = index + 1;
                PreparedStatement.setObject(index,ProductGroup_Bean.getSmallPriceAmount());
                index = index + 1;
                PreparedStatement.setBoolean(index,ProductGroup_Bean.isCombineGroup());
                index = index + 1;
                HashMap<Integer, ItemGroup_Bean> itemMap = newProductGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                for(Integer item_id : itemMap.keySet()){
                    ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                    PreparedStatement.setInt(index,ItemGroup_Bean.getItem_id());
                    index = index + 1;
                    PreparedStatement.setInt(index,ItemGroup_Bean.getItem_quantity());
                    index = index + 1;
                }
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public ArrayList<ProductGroup_Bean> fineProductConnectionFromGroup(Integer order_id, String ISBN){
        ArrayList<ProductGroup_Bean> productGroupList = null;
        String query = "select A.id from ProductGroup A inner join Orders_items_Group B on A.id = B.group_id inner join Orders_Items C on B.item_id = C.id WHERE C.order_id IS NOT null and C.ISBN = ?";
        if(order_id != null){
            query = query + " and C.Order_id != ?";
        }
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,ISBN);
            if(order_id != null){
                PreparedStatement.setObject(2,order_id);
            }
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(productGroupList == null)
                    productGroupList = new ArrayList<>();
                int group_id = Rs.getInt(1);
                ProductGroup_Bean ProductGroup_Bean = getProductGroupByGroupId(group_id);
                if(ProductGroup_Bean != null){
                    productGroupList.add(ProductGroup_Bean);
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return productGroupList;
    }
    private ProductGroup_Bean getProductGroupByGroupId(int group_id){
        ProductGroup_Bean ProductGroup_Bean = null;
        String query = "select " +
                "A.id,A.ItemNumber,A.GroupName,A.Quantity,A.Unit,A.SinglePrice,A.PriceAmount,A.SmallQuantity," +
                "B.item_quantity," +
                "C.id,C.OrderNumber,C.ISBN,C.ProductName,C.Unit,C.BatchPrice,C.SinglePrice,C.Pricing " +
                "from ProductGroup A inner join Orders_Items_Group B on A.id = B.group_id inner join Orders_Items C on B.item_id = C.id where A.id = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            int product_itemNumber = 1;
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,group_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                int itemQuantity = Rs.getInt(8);
                if(ProductGroup_Bean == null) {
                    ProductGroup_Bean = new ProductGroup_Bean();
                    ProductGroup_Bean.setId(group_id);
                    ProductGroup_Bean.setOrderNumber(Rs.getString(10));
                    ProductGroup_Bean.setItemNumber(Rs.getInt(2));
                    ProductGroup_Bean.setGroupName(Rs.getString(3));
                    ProductGroup_Bean.setQuantity(Rs.getInt(4));
                    ProductGroup_Bean.setCombineGroup(ProductGroup_Bean.getQuantity() != 1);
                    ProductGroup_Bean.setUnit(Rs.getString(5));
                    ProductGroup_Bean.setSinglePrice(Rs.getDouble(6));
                    ProductGroup_Bean.setPriceAmount(Rs.getInt(7));
                    ProductGroup_Bean.setSmallQuantity(Rs.getObject(8) == null ? null : Rs.getInt(8));
                    ProductGroup_Bean.setProductList(FXCollections.observableArrayList());
                }
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setItem_id(Rs.getInt(10));
                OrderProduct_Bean.setISBN(Rs.getString(12));
                OrderProduct_Bean.setProductName(Rs.getString(13));
                OrderProduct_Bean.setItemNumber(product_itemNumber);
                OrderProduct_Bean.setQuantity(itemQuantity);
                OrderProduct_Bean.setUnit(Rs.getString(14));
                OrderProduct_Bean.setBatchPrice(Rs.getDouble(15));
                OrderProduct_Bean.setSinglePrice(Rs.getDouble(16));
                OrderProduct_Bean.setPricing(Rs.getDouble(17));
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
                ProductGroup_Bean.getProductList().add(OrderProduct_Bean);
                product_itemNumber++;
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ProductGroup_Bean;
    }
    public boolean isOrderExistsInstallment(int order_id, boolean isCheckout){
        boolean isExist = false;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String query = "select 1 from Orders_Installment where order_id = ? and ";
            query = query + (isCheckout ? "pay_receive_document_id is not null" : "pay_receive_document_id is null");
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1, order_id);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                isExist = true;
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return isExist;
    }
    public ArrayList<Installment_Bean> getInstallmentByOrderId(int order_id){
        ArrayList<Installment_Bean> installmentList = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String query = "select * from Orders_Installment where order_id = ? order by installment";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1, order_id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int pay_receive_document_id = rs.getInt("pay_receive_document_id");
                Installment_Bean installment_bean = new Installment_Bean(rs.getInt("installment"));
                installment_bean.setId(rs.getInt("id"));
                installment_bean.setOrder_id(order_id);
                installment_bean.setPayReceive_document_id(pay_receive_document_id == 0 ? null : pay_receive_document_id);
                installment_bean.setTotalPrice(rs.getInt("total_price"));
                if(installmentList == null){
                    installmentList = new ArrayList<>();
                }
                installmentList.add(installment_bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return installmentList;
    }
    public boolean findNoneCheckoutInstallment(SearchOrder_Bean searchOrder_bean){
        boolean isExistInstallment = false;
        String query = "select * from Orders_Installment where order_id = ? order by installment";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1, searchOrder_bean.getId());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                isExistInstallment = true;
                Integer pay_receive_document_id = (rs.getObject("pay_receive_document_id") == null ? null : rs.getInt("pay_receive_document_id"));
                if(pay_receive_document_id == null){
                    int installment = rs.getInt("installment");
                    int total_price = rs.getInt("total_price");
                    searchOrder_bean.setInstallment(installment+1);
                    searchOrder_bean.setTotalPriceIncludeTax(total_price);
                    break;
                }
            }
            if(isExistInstallment && searchOrder_bean.getInstallment() == null){
                searchOrder_bean.setInstallment(-1);
                searchOrder_bean.setTotalPriceIncludeTax(0);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return isExistInstallment;
    }
    public boolean isExistNextInstallmentAndCheckout(SearchOrder_Bean searchOrder_bean){
        boolean isExistInstallment = false;
        String query = "select 1 from Orders_Installment where order_id = ? and installment > ? and pay_receive_document_id is not null";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1, searchOrder_bean.getId());
            preparedStatement.setInt(2, (searchOrder_bean.getInstallment()-1));
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                isExistInstallment = true;
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return isExistInstallment;
    }
    public boolean updateInstallment(boolean isDelete, int order_id, ArrayList<Installment_Bean> installmentList){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            query += "delete from Orders_Installment where order_id = ? ";
            if(!isDelete){
                for(Installment_Bean installment_bean : installmentList){
                    query += "insert into Orders_Installment (order_id, pay_receive_document_id, installment, total_price) values (?,?,?,?) ";
                }
            }
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, order_id);
            if(!isDelete){
                int index = 2;
                for(Installment_Bean installment_bean : installmentList){
                    PreparedStatement.setInt(index++, order_id);
                    PreparedStatement.setObject(index++, installment_bean.getPayReceive_document_id());
                    PreparedStatement.setInt(index++, installment_bean.getInstallment());
                    PreparedStatement.setInt(index++, installment_bean.getTotalPrice());
                }
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Get the main order items of sub bill
     * @param WaitingOrderNumber the number of main waiting order
     * */
    public ObservableList<OrderProduct_Bean> getMainOrderItemsOfSubBill(OrderObject OrderObject, String WaitingOrderNumber){
        ObservableList<OrderProduct_Bean> OrderItems = FXCollections.observableArrayList();
        String Query = "select A.ItemNumber, A.ISBN, B.InternationalCode, B.FirmCode, A.ProductName, A.Quantity, A.Unit, " +
                "A.BatchPrice, A.SinglePrice, A.Pricing, A.PriceAmount, A.Remark, B.InStock, B.SafetyStock, C.WaitingPurchaseQuantity, C.WaitingIntoInStock, C.NeededPurchaseQuantity, C.WaitingShipmentQuantity, B.SupplyStatus, E.NewFirstCategory " +
                "from Orders_Items A left join Store B on A.ISBN = B.ISBN left join ProductSaleStatus C on A.ISBN = C.ISBN left join Orders D on A.Order_id = D.id LEFT JOIN store_category E on B.id = E.store_id where D.WaitingOrderNumber = ? and D.OrderSource = ? and D.status = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,WaitingOrderNumber);
            PreparedStatement.setInt(2,OrderObject.ordinal());
            PreparedStatement.setInt(3,OrderStatus.有效貨單.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setItemNumber(Rs.getInt(1));
                OrderProduct_Bean.setISBN(Rs.getString(2));
                OrderProduct_Bean.setInternationalCode(Rs.getString(3));
                OrderProduct_Bean.setFirmCode(Rs.getString(4));
                OrderProduct_Bean.setProductName(Rs.getString(5));
                OrderProduct_Bean.setQuantity(Rs.getInt(6));
                OrderProduct_Bean.setUnit(Rs.getString(7));
                OrderProduct_Bean.setBatchPrice(Rs.getDouble(8));
                OrderProduct_Bean.setSinglePrice(Rs.getDouble(9));
                OrderProduct_Bean.setPricing(Rs.getDouble(10));
                OrderProduct_Bean.setPriceAmount(Rs.getInt(11));
                OrderProduct_Bean.setRemark(Rs.getString(12));
                OrderProduct_Bean.setInStock(Rs.getInt(13));
                OrderProduct_Bean.setSafetyStock(Rs.getInt(14));
                OrderProduct_Bean.setWaitingPurchaseQuantity(Rs.getInt(15));
                OrderProduct_Bean.setWaitingIntoInStock(Rs.getInt(16));
                OrderProduct_Bean.setNeededPurchaseQuantity(Rs.getInt(17));
                OrderProduct_Bean.setWaitingShipmentQuantity(Rs.getInt(18));
                OrderProduct_Bean.setSupplyStatus(Rs.getString(19));
                OrderProduct_Bean.setFirstCategory(Rs.getString(20));
//                OrderProduct_Bean.setProductTag(getItemsProductTag(OrderProduct_Bean));
                OrderItems.add(OrderProduct_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return OrderItems;
    }
    public int getTotalPriceIncludeTaxFromMainOrderBySubBill(OrderSource OrderSource, String waitingOrderNumber){
        int totalPriceIncludeTax = 0;
        String Query = "select TotalPriceIncludeTax from Orders A inner join Orders_Price B on A.id = B.Order_id where A.WaitingOrderNumber = ? and A.OrderSource = ? and A.status = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, waitingOrderNumber);
            PreparedStatement.setInt(2, OrderSource.getOrderObject().ordinal());
            PreparedStatement.setInt(3, OrderStatus.有效貨單.ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                totalPriceIncludeTax = Rs.getInt(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return totalPriceIncludeTax;
    }
    public boolean insertReportGenerator(ReportGenerator_Bean ReportGenerator_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            if(ReportGenerator_Bean.getOrderSource() == OrderSource.出貨子貨單)
                query = query + "insert into SubBill_ReportGenerator (subBill_id,export_manufacturer_id,remark,insert_dateTime) values (?,?,?,?) ";
            else
                query = query + "insert into Orders_ReportGenerator (order_id,export_manufacturer_id,remark,insert_dateTime) values (?,?,?,?) ";
            ObservableList<ReportGenerator_Item_Bean> itemList = ReportGenerator_Bean.getReportGenerator_itemList();
            for(ReportGenerator_Item_Bean ReportGenerator_Item_Bean : itemList){
                if(ReportGenerator_Bean.getOrderSource() == OrderSource.出貨子貨單)
                    query = query + "insert into SubBill_ReportGenerator_item (report_generator_id,item_number,item_name) values ((SELECT IDENT_CURRENT('SubBill_ReportGenerator')),?,?) ";
                else
                    query = query + "insert into Orders_ReportGenerator_item (report_generator_id,item_number,item_name) values ((SELECT IDENT_CURRENT('Orders_ReportGenerator')),?,?) ";
            }
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, ReportGenerator_Bean.getOrderOrSubBill_id());
            PreparedStatement.setInt(2, ReportGenerator_Bean.getExport_manufacturer_id());
            PreparedStatement.setString(3, ReportGenerator_Bean.getRemark());
            PreparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            int index = 5;
            for(ReportGenerator_Item_Bean ReportGenerator_Item_Bean : itemList){
                PreparedStatement.setInt(index, ReportGenerator_Item_Bean.getItemNumber());
                index = index + 1;
                PreparedStatement.setString(index, ReportGenerator_Item_Bean.getItemName());
                index = index + 1;
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public ObservableList<ReportGenerator_Bean> getReportGenerator(OrderSource OrderSource, int orderOrSubBill_id){
        ObservableList<ReportGenerator_Bean> reportGeneratorList = FXCollections.observableArrayList();
        ReportGenerator_Bean ReportGenerator_Bean = null;
        String query;
        if(OrderSource != Order_Enum.OrderSource.出貨子貨單)
            query = "select A.id,A.export_manufacturer_id,A.remark,A.insert_dateTime,B.id,B.report_generator_id,B.item_number,B.item_name from Orders_ReportGenerator A inner join Orders_ReportGenerator_item B on A.id = B.report_generator_id where order_id = ? order by insert_dateTime desc";
        else
            query = "select A.id,A.export_manufacturer_id,A.remark,A.insert_dateTime,B.id,B.report_generator_id,B.item_number,B.item_name from SubBill_ReportGenerator A inner join SubBill_ReportGenerator_item B on A.id = B.report_generator_id where subBill_id = ? order by insert_dateTime desc";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, orderOrSubBill_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                int report_generator_id = Rs.getInt(1);
                ReportGenerator_Item_Bean ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
                ReportGenerator_Item_Bean.setId(Rs.getInt(5));
                ReportGenerator_Item_Bean.setReport_generator_id(Rs.getInt(6));
                ReportGenerator_Item_Bean.setItemNumber(Rs.getInt(7));
                ReportGenerator_Item_Bean.setItemName(Rs.getString(8));

                if(ReportGenerator_Bean == null || ReportGenerator_Bean.getId() != report_generator_id){
                    ReportGenerator_Bean = new ReportGenerator_Bean();
                    ReportGenerator_Bean.setId(report_generator_id);
                    ReportGenerator_Bean.setExport_manufacturer_id(Rs.getInt(2));
                    ReportGenerator_Bean.setRemark(Rs.getString(3));
                    ReportGenerator_Bean.setInsertDateTime(Rs.getString(4));
                    ReportGenerator_Bean.setReportGenerator_itemList(FXCollections.observableArrayList());
                    reportGeneratorList.add(ReportGenerator_Bean);
                }
                ReportGenerator_Bean.getReportGenerator_itemList().add(ReportGenerator_Item_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return reportGeneratorList;
    }
    public SpecificationTemplate_Bean insertSpecificationTemplate(String templateName, String content, SpecificationColumn SpecificationColumn){
        SpecificationTemplate_Bean SpecificationTemplate_Bean = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into SpecificationTemplate(Name,Content,Type) values (?,?,?) " +
                    "select id from SpecificationTemplate where Name = ? and Type = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,templateName);
            PreparedStatement.setString(2,content);
            PreparedStatement.setInt(3,SpecificationColumn.ordinal());
            PreparedStatement.setString(4,templateName);
            PreparedStatement.setInt(5,SpecificationColumn.ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                SpecificationTemplate_Bean = new SpecificationTemplate_Bean(Rs.getInt(1),templateName,content,SpecificationColumn);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return SpecificationTemplate_Bean;
    }
    public HashMap<SpecificationColumn,ObservableList<SpecificationTemplate_Bean>> getSpecificationTemplate(){
        HashMap<SpecificationColumn,ObservableList<SpecificationTemplate_Bean>> specificationTemplateMap =
                new HashMap<SpecificationColumn,ObservableList<SpecificationTemplate_Bean>>(){{
                    put(SpecificationColumn.基本需求,FXCollections.observableArrayList());
                    put(SpecificationColumn.保固維護與其他,FXCollections.observableArrayList());
                    put(SpecificationColumn.教育訓練,FXCollections.observableArrayList());}};
        String Query = "select * from SpecificationTemplate";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                int id = Rs.getInt("id");
                String templateName = Rs.getString("Name");
                String content = Rs.getString("Content");
                SpecificationColumn SpecificationColumn = Order_Enum.SpecificationColumn.values()[Rs.getInt("Type")];

                SpecificationTemplate_Bean SpecificationTemplate_Bean = new SpecificationTemplate_Bean(id,templateName,content,SpecificationColumn);
                specificationTemplateMap.get(SpecificationColumn).add(SpecificationTemplate_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return specificationTemplateMap;
    }
    public boolean modifySpecificationName(SpecificationTemplate_Bean SpecificationTemplate_Bean, String newTemplateName){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update SpecificationTemplate set Name = ? where Name = ? and Type = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,newTemplateName);
            PreparedStatement.setString(2,SpecificationTemplate_Bean.getTemplateName());
            PreparedStatement.setInt(3,SpecificationTemplate_Bean.getSpecificationColumn().ordinal());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean modifySpecificationTemplate(SpecificationTemplate_Bean SpecificationTemplate_Bean, String newContent){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update SpecificationTemplate set Content = ? where Name = ? and Type = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,newContent);
            PreparedStatement.setString(2,SpecificationTemplate_Bean.getTemplateName());
            PreparedStatement.setInt(3,SpecificationTemplate_Bean.getSpecificationColumn().ordinal());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteSpecificationTemplate(SpecificationTemplate_Bean SpecificationTemplate_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from SpecificationTemplate where Name = ? and Type = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,SpecificationTemplate_Bean.getTemplateName());
            PreparedStatement.setInt(2,SpecificationTemplate_Bean.getSpecificationColumn().ordinal());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public ObservableList<String> getItemsProductTag(OrderProduct_Bean OrderProduct_Bean){
        ObservableList<String> ProductTag = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from ProductTag where ISBN = ?");
            PreparedStatement.setString(1,OrderProduct_Bean.getISBN());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                String tag = Rs.getString("Tag");
                if(!tag.equals(OrderProduct_Bean.getFirmCode()))
                    ProductTag.add(Rs.getString("Tag"));
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ProductTag;
    }
    public boolean updateOrderReviewStatus(int order_id, ReviewObject ReviewObject, ReviewStatus ReviewStatus, OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Orders_ReviewStatus set review_status = ? where order_id = ? and review_object = ? ";
                    if(OrderReviewStatusRecord_Bean != null) {
                        Query = Query + "insert into Orders_ReviewStatusRecord (review_status_id,subject,record,record_time,review_status) values " +
                                "((select id from Orders_ReviewStatus where order_id = ? and review_object = ?),?,?,?,?) ";
                        if(OrderReviewStatusRecord_Bean.getPictureList() != null){
                            ArrayList<OrderReviewStatusPicture_Bean> pictureList = OrderReviewStatusRecord_Bean.getPictureList();
                            for(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean : pictureList){
                                Query = Query + "insert into Orders_ReviewStatusPicture (reviewStatus_record_id,picture) values ((select IDENT_CURRENT('Orders_ReviewStatusRecord')),?) ";
                            }
                        }
                    }
                    Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,ReviewStatus.ordinal());
            PreparedStatement.setInt(2,order_id);
            PreparedStatement.setInt(3,ReviewObject.ordinal());
            if(OrderReviewStatusRecord_Bean != null) {
                PreparedStatement.setInt(4, order_id);
                PreparedStatement.setInt(5, ReviewObject.ordinal());
                PreparedStatement.setString(6, OrderReviewStatusRecord_Bean.getSubject());
                PreparedStatement.setString(7, OrderReviewStatusRecord_Bean.getRecord());
                PreparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                PreparedStatement.setInt(9, ReviewStatus.ordinal());
                if(OrderReviewStatusRecord_Bean.getPictureList() != null){
                    int index = 10;
                    ArrayList<OrderReviewStatusPicture_Bean> pictureList = OrderReviewStatusRecord_Bean.getPictureList();
                    for(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean : pictureList){
                        PreparedStatement.setString(index, orderReviewStatusPicture_Bean.getBase64());
                        index = index + 1;
                    }
                }
            }
            PreparedStatement.executeUpdate();
            status =  true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean modifyOrderReviewStatusRecord(int id, int order_id, String subject, String record){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Orders_ReviewStatusRecord set subject = ?,record = ? where id = ? and review_status_id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,subject);
            PreparedStatement.setString(2,record);
            PreparedStatement.setInt(3,id);
            PreparedStatement.setInt(4,order_id);
            PreparedStatement.executeUpdate();
            status =  true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteOrderReviewStatusRecord(int id, int order_id){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from Orders_ReviewStatusRecord where id = ? and review_status_id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, id);
            PreparedStatement.setInt(2, order_id);
            PreparedStatement.executeUpdate();
            status =  true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public Integer insertOrderReviewStatusPicture(int reviewStatus_record_id, String base64){
        Integer pictureID = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into Orders_ReviewStatusPicture (reviewStatus_record_id, picture) values (?,?) " +
                    "select IDENT_CURRENT('Orders_ReviewStatusPicture') " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, reviewStatus_record_id);
            PreparedStatement.setString(2, base64);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                pictureID = Rs.getInt(1);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return pictureID;
    }
    public boolean updateOrderReviewStatusPicture(int id, int reviewStatus_record_id, String base64){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Orders_ReviewStatusPicture set picture = ? where id = ? and reviewStatus_record_id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, base64);
            PreparedStatement.setInt(2, id);
            PreparedStatement.setInt(3, reviewStatus_record_id);
            PreparedStatement.executeUpdate();
            status =  true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteOrderReviewStatusPicture(int id, int reviewStatus_record_id){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from Orders_ReviewStatusPicture where id = ? and reviewStatus_record_id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, id);
            PreparedStatement.setInt(2, reviewStatus_record_id);
            PreparedStatement.executeUpdate();
            status =  true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Update order info in database
     * @param Order_Bean the bean of order
     * */
    public boolean modifyOrder(Order_Bean Order_Bean,ObservableList<OrderProduct_Bean> wantCancelProductList,boolean isHandleProductStatus,boolean coverOrderReference) {
        //  如果已經轉待入倉or待出貨，則只能修改商品稅額(折讓)、專案資訊、訂購資訊
        if (Order_Bean.getOrderSource() == Order_Enum.OrderSource.採購單 || Order_Bean.getOrderSource() == Order_Enum.OrderSource.報價單){
            return modifyQuotation(Order_Bean,wantCancelProductList,isHandleProductStatus,coverOrderReference);
        }else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單){
            return modifyReturnOrder(Order_Bean);
        }else{
            if(Order_Bean.getOrderSource() == OrderSource.進貨子貨單 || Order_Bean.getOrderSource() == OrderSource.出貨子貨單){
                Order_Bean.setOrderSource(Order_Bean.getOrderSource() == OrderSource.進貨子貨單 ? Order_Enum.OrderSource.採購單 : Order_Enum.OrderSource.報價單);
                return modifySubBill(Order_Bean);
            }else {
                if(Order_Bean.getOrderSource() == OrderSource.待入倉單 || Order_Bean.getOrderSource() == OrderSource.進貨單)
                    Order_Bean.setOrderSource(Order_Enum.OrderSource.採購單);
                else if(Order_Bean.getOrderSource() == OrderSource.待出貨單 || Order_Bean.getOrderSource() == OrderSource.出貨單)
                    Order_Bean.setOrderSource(OrderSource.報價單);
                return modifyQuotation(Order_Bean,wantCancelProductList,isHandleProductStatus,false);
            }
        }
    }
    private boolean modifyQuotation(Order_Bean Order_Bean,ObservableList<OrderProduct_Bean> wantCancelProductList, boolean isHandleProductStatus, boolean coverOrderReference){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Orders set OrderNumber = ?,OrderDate = ?,ObjectID = ?,NumberOfItems = ?,isBorrowed = ?, isOffset = ?, Remark = ?, CashierRemark = ? where OrderNumber = ? and OrderSource = ? and id = ? and status = '0' " +
                    "update Orders_Price set OrderNumber = ?, TotalPriceNoneTax = ?,Tax = ?,Discount = ?,TotalPriceIncludeTax = ? from Orders A inner join Orders_Price B on A.id = B.Order_id where A.OrderNumber = ? and A.id = ? and A.status = '0' " +
                    "update Orders_ProjectInfo set OrderNumber = ?,ProjectCode = ?,ProjectName = ?,ProjectQuantity = ?,ProjectUnit = ?,ProjectPriceAmount = ?,ProjectTotalPriceNoneTax = ?,ProjectTax = ?,ProjectTotalPriceIncludeTax = ?,ProjectDifferentPrice = ? from Orders A inner join Orders_ProjectInfo B on A.id = B.Order_id where A.OrderNumber = ? and A.id = ? and A.status = '0' " +
                    "update Orders_ShoppingInfo set OrderNumber = ?,PurchaserName = ?,PurchaserTelephone = ?,PurchaserCellphone = ?,PurchaserAddress = ?,RecipientName = ?,RecipientTelephone = ?,RecipientCellphone = ?,RecipientAddress = ? from Orders A inner join Orders_ShoppingInfo B on A.id = B.Order_id where A.OrderNumber = ? and A.id = ? and A.status = '0' ";
            if(coverOrderReference)
                Query = Query + getModifyOrderReferenceQuery(Order_Bean);
            Query = Query + getModifyOrderProductQuery(Order_Bean);
            Query = Query + getModifyOrderProductGroupPrice(Order_Bean);

            if(isHandleProductStatus && wantCancelProductList != null){
                for(OrderProduct_Bean OrderProduct_Bean : wantCancelProductList){
                    if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.採購單)
                        Query = Query + handleDeleteWaitingPurchaseOrderSaleStatus(OrderProduct_Bean);
                    else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.報價單)
                        Query = Query + handleDeleteWaitingShipmentOrderSaleStatus(OrderProduct_Bean);
                }
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,Order_Bean.getNowOrderNumber());
            PreparedStatement.setDate(2, Date.valueOf(Order_Bean.getOrderDate()));
            PreparedStatement.setString(3,Order_Bean.getObjectID());
            PreparedStatement.setString(4,Order_Bean.getNumberOfItems());
            PreparedStatement.setInt(5,Order_Bean.isBorrowed().ordinal());
            PreparedStatement.setInt(6,Order_Bean.getOffsetOrderStatus().ordinal());
            PreparedStatement.setString(7,Order_Bean.getOrderRemark());
            PreparedStatement.setString(8,Order_Bean.getCashierRemark());
            PreparedStatement.setString(9,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(10,Order_Bean.getOrderSource().getOrderObject().ordinal());
            PreparedStatement.setInt(11,Order_Bean.getOrderID());

            PreparedStatement.setString(12,Order_Bean.getNowOrderNumber());
            PreparedStatement.setInt(13,Integer.parseInt(Order_Bean.getTotalPriceNoneTax()));
            PreparedStatement.setInt(14,Integer.parseInt(Order_Bean.getTax()));
            PreparedStatement.setInt(15,Integer.parseInt(Order_Bean.getDiscount()));
            PreparedStatement.setInt(16,Integer.parseInt(Order_Bean.getTotalPriceIncludeTax()));
            PreparedStatement.setString(17,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(18,Order_Bean.getOrderID());

            PreparedStatement.setString(19,Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(20,Order_Bean.getProjectCode());
            PreparedStatement.setString(21,Order_Bean.getProjectName());
            PreparedStatement.setString(22,Order_Bean.getProjectQuantity());
            PreparedStatement.setString(23,Order_Bean.getProjectUnit());
            PreparedStatement.setString(24,Order_Bean.getProjectPriceAmount());
            PreparedStatement.setString(25,Order_Bean.getProjectTotalPriceNoneTax());
            PreparedStatement.setString(26,Order_Bean.getProjectTax());
            PreparedStatement.setString(27,Order_Bean.getProjectTotalPriceIncludeTax());
            PreparedStatement.setString(28,Order_Bean.getProjectDifferentPrice());
            PreparedStatement.setString(29,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(30,Order_Bean.getOrderID());

            PreparedStatement.setString(31,Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(32,Order_Bean.getPurchaserName());
            PreparedStatement.setString(33,Order_Bean.getPurchaserTelephone());
            PreparedStatement.setString(34,Order_Bean.getPurchaserCellphone());
            PreparedStatement.setString(35,Order_Bean.getPurchaserAddress());
            PreparedStatement.setString(36,Order_Bean.getRecipientName());
            PreparedStatement.setString(37,Order_Bean.getRecipientTelephone());
            PreparedStatement.setString(38,Order_Bean.getRecipientCellphone());
            PreparedStatement.setString(39,Order_Bean.getRecipientAddress());
            PreparedStatement.setString(40,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(41,Order_Bean.getOrderID());

            HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
            HashMap<Integer,Integer> itemsGroupMap = null;
            if(productGroupMap != null){
                itemsGroupMap = new HashMap<>();
                for(Integer group_itemNumber : productGroupMap.keySet()){
                    ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                    HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                    for(Integer item_id : itemMap.keySet()){
                        ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                        itemsGroupMap.put(ItemGroup_Bean.getId(), ItemGroup_Bean.getItem_id());
                    }
                }
            }

            int productDBIndex = 42;
            PreparedStatement.setInt(productDBIndex,Order_Bean.getOrderID());
            productDBIndex++;
            ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
            for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                PreparedStatement.setInt(productDBIndex,Order_Bean.getOrderID());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, Order_Bean.getNowOrderNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getItemNumber());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getISBN());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getProductName());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getQuantity());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getUnit());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getBatchPrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getSinglePrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPricing());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPriceAmount());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getRemark());
                productDBIndex++;
                if(productGroupMap != null) {
                    for(Integer order_item_group_id : itemsGroupMap.keySet()){
                        int item_id = itemsGroupMap.get(order_item_group_id);
                        if(OrderProduct_Bean.getItem_id() != null && OrderProduct_Bean.getItem_id() == item_id){
                            PreparedStatement.setInt(productDBIndex++, order_item_group_id);
                        }
                    }
                }
            }
            if(productGroupMap == null) {
                PreparedStatement.setInt(productDBIndex++, Order_Bean.getOrderID());
            }else{
                PreparedStatement.setInt(productDBIndex++,Order_Bean.getOrderID());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupTotalPriceNoneTax());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupTax());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupDiscount());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupTotalPriceIncludeTax());
                PreparedStatement.setInt(productDBIndex++,Order_Bean.getOrderID());
                PreparedStatement.setInt(productDBIndex++,Order_Bean.getOrderID());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupTotalPriceNoneTax());    //#Error
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupTax());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupDiscount());
                PreparedStatement.setString(productDBIndex++,Order_Bean.getProductGroupTotalPriceIncludeTax());
            }
            PreparedStatement.executeUpdate();
            status =  true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    private String getModifyOrderReferenceQuery(Order_Bean Order_Bean){
        HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = Order_Bean.getOrderReferenceMap();
        HashMap<Integer,Boolean> mainOrderReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.報價單);;
        HashMap<Integer,Boolean> subBillReferenceMap = orderReferenceMap.get(Order_Enum.OrderSource.出貨子貨單);
        String query = "";
        if(Order_Bean.getOrderSource() == OrderSource.報價單){
            query = "delete from Orders_Reference where  Order_id = '" + Order_Bean.getOrderID() + "' ";
            if(mainOrderReferenceMap.size() != 0) {
                for(int order_id : mainOrderReferenceMap.keySet()){
                    query = query + "insert into Orders_Reference (Order_id,Order_Reference_Id,SubBill_Reference_id) values ('" + Order_Bean.getOrderID() + "','" + order_id + "',null)";
                }
            }
            if(subBillReferenceMap.size() != 0){
                for(int subBill_id : subBillReferenceMap.keySet()){
                    query = query + "insert into Orders_Reference (Order_id,Order_Reference_Id,SubBill_Reference_id) values ('" + Order_Bean.getOrderID() + "',null,'" + subBill_id + "')";
                }
            }
        }else if(Order_Bean.getOrderSource() == OrderSource.出貨子貨單){
            query = "delete from SubBill_Reference where SubBill_id = '" + Order_Bean.getOrderID() + "' ";
            if(mainOrderReferenceMap.size() != 0) {
                for(int order_id : mainOrderReferenceMap.keySet()){
                    query = query + "insert into SubBill_Reference (SubBill_id,SubBill_Reference_Id,Order_Reference_id) values ('" + Order_Bean.getOrderID() + "',null,'" + order_id + "')";
                }
            }
            if(subBillReferenceMap.size() != 0){
                for(int subBill_id : subBillReferenceMap.keySet()){
                    query = query + "insert into SubBill_Reference (SubBill_id,SubBill_Reference_Id,Order_Reference_id) values ('" + Order_Bean.getOrderID() + "','" + subBill_id + "',null)";
                }
            }
        }
        return query;
    }
    private String getModifyOrderProductQuery(Order_Bean Order_Bean){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
        HashMap<Integer,Integer> itemsGroupMap = null;
        if(productGroupMap != null){
            itemsGroupMap = new HashMap<>();
            for(Integer group_itemNumber : productGroupMap.keySet()){
                ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                for(Integer item_id : itemMap.keySet()){
                    ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                    itemsGroupMap.put(ItemGroup_Bean.getId(), ItemGroup_Bean.getItem_id());
                }
            }
        }

        ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
        String Query = "delete from Orders_Items where Order_id = ? ";
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            Query = Query + "insert into Orders_Items (Order_id,OrderNumber,ItemNumber,ISBN,ProductName,Quantity,Unit,BatchPrice,SinglePrice,Pricing,PriceAmount,Remark) values (?,?,?,?,?,?,?,?,?,?,?,?) ";
            if(productGroupMap != null) {
                for(Integer order_item_group_id : itemsGroupMap.keySet()){
                    int item_id = itemsGroupMap.get(order_item_group_id);
                    if(OrderProduct_Bean.getItem_id() != null && OrderProduct_Bean.getItem_id() == item_id){
                        Query = Query + "update Orders_Items_Group set item_id = (select IDENT_CURRENT('Orders_Items')) where id = ? ";
                    }
                }
            }
        }
        return Query;
    }
    private String getModifyOrderProductGroupPrice(Order_Bean order_bean){
        String query;
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = order_bean.getProductGroupMap();
        if(productGroupMap != null){
            query = "IF EXISTS (select 1 from Orders_ProductGroup_Price where order_id = ? ) BEGIN " +
                    "update Orders_ProductGroup_Price set total_price_none_tax = ?, tax = ?, discount = ?, total_price_include_tax = ? where order_id = ? " +
                    "END ELSE BEGIN " +
                    "insert into Orders_ProductGroup_Price (order_id,total_price_none_tax,tax,discount,total_price_include_tax) values (?,?,?,?,?) END ";
        }else{
            query = "delete from Orders_ProductGroup_Price where order_id = ? ";
        }
        return query;
    }
    public boolean deleteProductGroupPrice(int order_id){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from Orders_ProductGroup_Price where order_id = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, order_id);
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }

        //寫一個九九乘法表


        return status;
    }
    private boolean modifySubBill(Order_Bean Order_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update SubBill set OrderNumber = ?,OrderDate = ?,ObjectID = ?,NumberOfItems = ?,isBorrowed = ?, isOffset = ?, Remark = ?, CashierRemark = ? where OrderNumber = ? and OrderSource = ? and id = ? " +
                    "update SubBill_Price set OrderNumber = ?, TotalPriceNoneTax = ?,Tax = ?,Discount = ?,TotalPriceIncludeTax = ? from SubBill A inner join SubBill_Price B on A.id = B.SubBill_id where A.OrderNumber = ? and A.id = ? " +
                    "update SubBill_ProjectInfo set OrderNumber = ?,ProjectCode = ?,ProjectName = ?,ProjectQuantity = ?,ProjectUnit = ?,ProjectPriceAmount = ?,ProjectTotalPriceNoneTax = ?,ProjectTax = ?,ProjectTotalPriceIncludeTax = ?,ProjectDifferentPrice = ? from SubBill A inner join SubBill_ProjectInfo B on A.id = B.SubBill_id where A.OrderNumber = ? and A.id = ? " +
                    "update SubBill_ShoppingInfo set OrderNumber = ?,PurchaserName = ?,PurchaserTelephone = ?,PurchaserCellphone = ?,PurchaserAddress = ?,RecipientName = ?,RecipientTelephone = ?,RecipientCellphone = ?,RecipientAddress = ? from SubBill A inner join SubBill_ShoppingInfo B on A.id = B.SubBill_id where A.OrderNumber = ? and A.id = ? ";
            Query = Query + getModifySubBillQuery(Order_Bean);
            Query = Query + " COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(2,Order_Bean.getOrderDate());
            PreparedStatement.setString(3,Order_Bean.getObjectID());
            PreparedStatement.setInt(4,Integer.parseInt(Order_Bean.getNumberOfItems()));
            PreparedStatement.setInt(5,Order_Bean.isBorrowed().ordinal());
            PreparedStatement.setInt(6,Order_Bean.getOffsetOrderStatus().ordinal());
            PreparedStatement.setString(7,Order_Bean.getOrderRemark());
            PreparedStatement.setString(8,Order_Bean.getCashierRemark());
            PreparedStatement.setString(9,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(10,Order_Bean.getOrderSource().getOrderObject().ordinal());
            PreparedStatement.setInt(11,Order_Bean.getOrderID());

            PreparedStatement.setString(12,Order_Bean.getNowOrderNumber());
            PreparedStatement.setInt(13,Integer.parseInt(Order_Bean.getTotalPriceNoneTax()));
            PreparedStatement.setInt(14,Integer.parseInt(Order_Bean.getTax()));
            PreparedStatement.setInt(15,Integer.parseInt(Order_Bean.getDiscount()));
            PreparedStatement.setInt(16,Integer.parseInt(Order_Bean.getTotalPriceIncludeTax()));
            PreparedStatement.setString(17,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(18,Order_Bean.getOrderID());

            PreparedStatement.setString(19,Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(20,Order_Bean.getProjectCode());
            PreparedStatement.setString(21,Order_Bean.getProjectName());
            PreparedStatement.setString(22,Order_Bean.getProjectQuantity());
            PreparedStatement.setString(23,Order_Bean.getProjectUnit());
            PreparedStatement.setString(24,Order_Bean.getProjectPriceAmount());
            PreparedStatement.setString(25,Order_Bean.getProjectTotalPriceNoneTax());
            PreparedStatement.setString(26,Order_Bean.getProjectTax());
            PreparedStatement.setString(27,Order_Bean.getProjectTotalPriceIncludeTax());
            PreparedStatement.setString(28,Order_Bean.getProjectDifferentPrice());
            PreparedStatement.setString(29,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(30,Order_Bean.getOrderID());

            PreparedStatement.setString(31,Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(32,Order_Bean.getPurchaserName());
            PreparedStatement.setString(33,Order_Bean.getPurchaserTelephone());
            PreparedStatement.setString(34,Order_Bean.getPurchaserCellphone());
            PreparedStatement.setString(35,Order_Bean.getPurchaserAddress());
            PreparedStatement.setString(36,Order_Bean.getRecipientName());
            PreparedStatement.setString(37,Order_Bean.getRecipientTelephone());
            PreparedStatement.setString(38,Order_Bean.getRecipientCellphone());
            PreparedStatement.setString(39,Order_Bean.getRecipientAddress());
            PreparedStatement.setString(40,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(41,Order_Bean.getOrderID());

            int productDBIndex = 42;
            PreparedStatement.setInt(productDBIndex, Order_Bean.getOrderID());
            productDBIndex++;
            ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
            for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                PreparedStatement.setInt(productDBIndex,Order_Bean.getOrderID());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, Order_Bean.getNowOrderNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getSeriesNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getItemNumber());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getISBN());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getProductName());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getQuantity());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getUnit());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getBatchPrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getSinglePrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPricing());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPriceAmount());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getRemark());
                productDBIndex++;
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    private String getModifySubBillQuery(Order_Bean Order_Bean){
        ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
        String Query = "delete from SubBill_Items where SubBill_id = ? ";
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            Query = Query + "insert into SubBill_Items (SubBill_id,OrderNumber,SeriesNumber,ItemNumber,ISBN,ProductName,Quantity,Unit,BatchPrice,SinglePrice,Pricing,PriceAmount,Remark) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        return Query;
    }
    private boolean modifyReturnOrder(Order_Bean Order_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update ReturnOrder set ObjectID = ?, Remark = ? where OrderNumber = ? and OrderSource = ? and id = ? " +
                    "update ReturnOrder_Price set TotalPriceNoneTax = ?, Tax = ?, Discount = ?, TotalPriceIncludeTax = ? where ReturnOrder_id = ? " +
                    "update ReturnOrder_ProjectInfo set OrderNumber = ?,ProjectCode = ?,ProjectName = ? where ReturnOrder_id = ? ";
                    Query = Query + getModifyReturnOrderQuery(Order_Bean);
                    Query = Query + " COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,Order_Bean.getObjectID());
            PreparedStatement.setString(2,Order_Bean.getOrderRemark());
            PreparedStatement.setString(3,Order_Bean.getOldOrderNumber());
            PreparedStatement.setInt(4,Order_Bean.getOrderSource().getOrderObject().ordinal());
            PreparedStatement.setInt(5,Order_Bean.getOrderID());

            PreparedStatement.setString(6,Order_Bean.getTotalPriceNoneTax());
            PreparedStatement.setString(7,Order_Bean.getTax());
            PreparedStatement.setString(8,Order_Bean.getDiscount());
            PreparedStatement.setString(9,Order_Bean.getTotalPriceIncludeTax());
            PreparedStatement.setInt(10,Order_Bean.getOrderID());

            PreparedStatement.setString(11,Order_Bean.getNowOrderNumber());
            PreparedStatement.setString(12,Order_Bean.getProjectCode());
            PreparedStatement.setString(13,Order_Bean.getProjectName());
            PreparedStatement.setInt(14,Order_Bean.getOrderID());

            int productDBIndex = 15;
            PreparedStatement.setInt(productDBIndex, Order_Bean.getOrderID());
            productDBIndex++;
            ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
            for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                PreparedStatement.setInt(productDBIndex,Order_Bean.getOrderID());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, Order_Bean.getNowOrderNumber());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getItemNumber());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getISBN());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getProductName());
                productDBIndex++;
                PreparedStatement.setInt(productDBIndex, OrderProduct_Bean.getQuantity());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getUnit());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getBatchPrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getSinglePrice());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPricing());
                productDBIndex++;
                PreparedStatement.setDouble(productDBIndex, OrderProduct_Bean.getPriceAmount());
                productDBIndex++;
                PreparedStatement.setString(productDBIndex, OrderProduct_Bean.getRemark());
                productDBIndex++;
            }

            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    private String getModifyReturnOrderQuery(Order_Bean Order_Bean){
        ObservableList<OrderProduct_Bean> ProductList = Order_Bean.getProductList();
        String Query = "delete from ReturnOrder_Items where ReturnOrder_id = ? ";
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            Query = Query + "insert into ReturnOrder_Items (ReturnOrder_id,OrderNumber,ItemNumber,ISBN,ProductName,Quantity,Unit,BatchPrice,SinglePrice,Pricing,PriceAmount,Remark) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        return Query;
    }
    public void refreshProductSaleStatus(ObservableList<OrderProduct_Bean> orderProductList){
        for(OrderProduct_Bean OrderProduct_Bean : orderProductList){
            PreparedStatement PreparedStatement = null;
            ResultSet Rs = null;
            try {
                PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.InStock,A.SafetyStock,B.WaitingPurchaseQuantity,B.WaitingIntoInStock,B.NeededPurchaseQuantity,B.WaitingShipmentQuantity " +
                        "from Store A inner join ProductSaleStatus B on A.id = B.store_id where A.ISBN = ?");
                PreparedStatement.setString(1,OrderProduct_Bean.getISBN());
                Rs = PreparedStatement.executeQuery();
                if (Rs.next()) {
                    OrderProduct_Bean.setInStock(Rs.getInt(1));
                    OrderProduct_Bean.setSafetyStock(Rs.getInt(2));
                    OrderProduct_Bean.setWaitingPurchaseQuantity(Rs.getInt(3));
                    OrderProduct_Bean.setWaitingIntoInStock(Rs.getInt(4));
                    OrderProduct_Bean.setNeededPurchaseQuantity(Rs.getInt(5));
                    OrderProduct_Bean.setWaitingShipmentQuantity(Rs.getInt(6));
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }finally{
                SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
            }
        }
    }
    /** ※※※ very important. Handle product sale status in database when the order is modified
     * @param OrderSource the source of order
     * @param ProductSaleStatus the sale status of product
     * @param OrderProduct_Bean the bean of order product
     * */
    public String handleProductSaleStatus(OrderSource OrderSource, ProductSaleStatus ProductSaleStatus, OrderProduct_Bean OrderProduct_Bean){
        String query = null;
        if(OrderSource == Order_Enum.OrderSource.待入倉單){
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待入倉量)    query = addWaitingPurchaseQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入倉量)   query = deleteWaitingPurchaseQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.判斷待入庫存)   query = judgeWaitingIntoInStock(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入庫存)   query = deleteWaitingIntoInStock(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.判斷需量)    query = judgeNeededPurchaseQuantity(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除需量)    query = deleteNeededPurchaseQuantity(OrderSource, OrderProduct_Bean);
        }else if(OrderSource == Order_Enum.OrderSource.進貨單){
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待入倉量)    query = addWaitingPurchaseQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入倉量)    query = deleteWaitingPurchaseQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待入庫存)   query = addWaitingIntoInStock(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入庫存)   query = deleteWaitingIntoInStock(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增存量)  query = addInStock(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除存量)    query = deleteInStock(OrderProduct_Bean);
        }else if(OrderSource == Order_Enum.OrderSource.進貨退貨單){
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增存量)  query = addInStock(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除存量)  query = deleteInStock(OrderProduct_Bean);
        }else if(OrderSource == Order_Enum.OrderSource.待出貨單){
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待出貨量)  query = addWaitingShipmentQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待出貨量)   query = deleteWaitingShipmentQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增需量)   query = addNeededPurchaseQuantity(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除需量)   query = deleteNeededPurchaseQuantity(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.判斷待入庫存)   query = judgeWaitingIntoInStock(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待入庫存)   query = addWaitingIntoInStock(OrderSource, OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入庫存)   query = deleteWaitingIntoInStock(OrderSource, OrderProduct_Bean);
        }else if(OrderSource == Order_Enum.OrderSource.出貨單){
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待出貨量)   query = addWaitingShipmentQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待出貨量)   query = deleteWaitingShipmentQuantity(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增存量)   query = addInStock(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除存量)   query = deleteInStock(OrderProduct_Bean);
        }else if(OrderSource == Order_Enum.OrderSource.出貨退貨單){
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增存量)  query = addInStock(OrderProduct_Bean);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除存量)  query = deleteInStock(OrderProduct_Bean);
        }
        return query;
    }
    private String addInStock(OrderProduct_Bean OrderProduct_Bean) {
        int InStock = OrderProduct_Bean.getInStock() + OrderProduct_Bean.getQuantity();
        getNowProductSaleStatus(ProductSaleStatus.新增存量, OrderProduct_Bean.getISBN(), InStock,0,0,0,0);
        return updateProductInStock(OrderProduct_Bean.getISBN(), InStock);
    }
    private String deleteInStock(OrderProduct_Bean OrderProduct_Bean){
        int InStock = OrderProduct_Bean.getInStock() - OrderProduct_Bean.getQuantity();
        getNowProductSaleStatus(ProductSaleStatus.刪除存量, OrderProduct_Bean.getISBN(), InStock,0,0,0,0);
        return  updateProductInStock(OrderProduct_Bean.getISBN(), InStock);
    }
    private String addWaitingPurchaseQuantity(OrderProduct_Bean OrderProduct_Bean){
        int WaitingPurchaseQuantity = OrderProduct_Bean.getWaitingPurchaseQuantity() + OrderProduct_Bean.getQuantity();
        getNowProductSaleStatus(ProductSaleStatus.新增待入倉量, OrderProduct_Bean.getISBN(),0, WaitingPurchaseQuantity,0,0,0);
        return updateProductWaitingPurchaseQuantity(OrderProduct_Bean, WaitingPurchaseQuantity);
    }
    private String deleteWaitingPurchaseQuantity(OrderProduct_Bean OrderProduct_Bean){
        int WaitingPurchaseQuantity = OrderProduct_Bean.getWaitingPurchaseQuantity() - OrderProduct_Bean.getQuantity();
        getNowProductSaleStatus(ProductSaleStatus.刪除待入倉量, OrderProduct_Bean.getISBN(),0, WaitingPurchaseQuantity,0,0,0);
        return updateProductWaitingPurchaseQuantity(OrderProduct_Bean, WaitingPurchaseQuantity);
    }
    private String judgeWaitingIntoInStock(OrderSource OrderSource, OrderProduct_Bean OrderProduct_Bean){
        if(OrderSource == Order_Enum.OrderSource.待入倉單){
            int remainWaitingIntoInStock;
            if(OrderProduct_Bean.getNeededPurchaseQuantity() != 0){     //  代表有需量，須進貨
                remainWaitingIntoInStock = OrderProduct_Bean.getQuantity() - OrderProduct_Bean.getNeededPurchaseQuantity() + OrderProduct_Bean.getWaitingIntoInStock();
                if(remainWaitingIntoInStock < 0)    remainWaitingIntoInStock = 0;
            }else{  //  代表沒需量
                if(OrderProduct_Bean.getWaitingPurchaseQuantity() != 0){    //  有待入倉量：代表此時的「待入庫存」已經判斷「安全存量」
                    remainWaitingIntoInStock = OrderProduct_Bean.getQuantity() + OrderProduct_Bean.getWaitingIntoInStock();
                }else{      //  存量 < 安全存量：代表此時的「待入庫存」未判斷「安全存量」
                    if(OrderProduct_Bean.getInStock() < OrderProduct_Bean.getSafetyStock())
                        remainWaitingIntoInStock = OrderProduct_Bean.getQuantity() + OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock();
                    else
                        remainWaitingIntoInStock = OrderProduct_Bean.getQuantity() + OrderProduct_Bean.getWaitingIntoInStock();
                }
            }
            getNowProductSaleStatus(ProductSaleStatus.判斷待入庫存, OrderProduct_Bean.getISBN(),0,0, remainWaitingIntoInStock,0,0);
            return updateProductWaitingIntoInStock(OrderProduct_Bean, remainWaitingIntoInStock);
        }else if(OrderSource == Order_Enum.OrderSource.待出貨單){
            if(OrderProduct_Bean.getWaitingIntoInStock() != 0){
                int WaitingIntoInStock = 0;
                if(OrderProduct_Bean.getWaitingIntoInStock() > OrderProduct_Bean.getQuantity())
                    WaitingIntoInStock = OrderProduct_Bean.getWaitingIntoInStock() - OrderProduct_Bean.getQuantity();
                getNowProductSaleStatus(ProductSaleStatus.判斷待入庫存, OrderProduct_Bean.getISBN(),0,0, WaitingIntoInStock,0,0);
                return updateProductWaitingIntoInStock(OrderProduct_Bean, WaitingIntoInStock);
            }
        }
        return "";
    }
    private String addWaitingIntoInStock(OrderSource OrderSource, OrderProduct_Bean OrderProduct_Bean){
        int WaitingIntoInStock;
        if(OrderSource == Order_Enum.OrderSource.進貨單){
//                ※ 缺少需量的計算 ?
            if(OrderProduct_Bean.getWaitingShipmentQuantity() == 0){
                if(OrderProduct_Bean.getSafetyStock() == 0)
                    WaitingIntoInStock = OrderProduct_Bean.getWaitingIntoInStock() + OrderProduct_Bean.getQuantity();
                else{
                    if(OrderProduct_Bean.getInStock() - OrderProduct_Bean.getQuantity() <= OrderProduct_Bean.getSafetyStock() &&
                            OrderProduct_Bean.getWaitingPurchaseQuantity() == OrderProduct_Bean.getWaitingIntoInStock()) {
                        if(OrderProduct_Bean.getInStock() - OrderProduct_Bean.getQuantity() <= 0)
                            WaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() + OrderProduct_Bean.getQuantity() - OrderProduct_Bean.getSafetyStock();
                        else
                            WaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() + OrderProduct_Bean.getQuantity();

                    }else
                        WaitingIntoInStock = OrderProduct_Bean.getInStock() -OrderProduct_Bean.getQuantity() + OrderProduct_Bean.getWaitingPurchaseQuantity() - OrderProduct_Bean.getSafetyStock();
                }
            }else{
                WaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() + OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock() - OrderProduct_Bean.getWaitingShipmentQuantity();
                if(WaitingIntoInStock < 0)  WaitingIntoInStock = 0;
            }
            getNowProductSaleStatus(ProductSaleStatus.新增待入庫存, OrderProduct_Bean.getISBN(),0,0, WaitingIntoInStock,0,0);
            return updateProductWaitingIntoInStock(OrderProduct_Bean, WaitingIntoInStock);
        }else if(OrderSource == Order_Enum.OrderSource.待出貨單){
            if(OrderProduct_Bean.getWaitingIntoInStock() != 0){
                WaitingIntoInStock = OrderProduct_Bean.getWaitingIntoInStock() + OrderProduct_Bean.getQuantity();
                getNowProductSaleStatus(ProductSaleStatus.新增待入庫存, OrderProduct_Bean.getISBN(),0,0, WaitingIntoInStock,0,0);
                return updateProductWaitingIntoInStock(OrderProduct_Bean, WaitingIntoInStock);
            }else if(OrderProduct_Bean.getWaitingIntoInStock() == 0 && OrderProduct_Bean.getWaitingPurchaseQuantity() != 0 && OrderProduct_Bean.getWaitingShipmentQuantity() != 0){
                if(OrderProduct_Bean.getWaitingShipmentQuantity() - OrderProduct_Bean.getQuantity() == 0){
                    WaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() - OrderProduct_Bean.getSafetyStock();
                    getNowProductSaleStatus(ProductSaleStatus.新增待入庫存, OrderProduct_Bean.getISBN(),0,0, WaitingIntoInStock,0,0);
                    return updateProductWaitingIntoInStock(OrderProduct_Bean, WaitingIntoInStock);
                }else if(OrderProduct_Bean.getWaitingShipmentQuantity() - OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock()){
                    WaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() -
                            (OrderProduct_Bean.getWaitingShipmentQuantity() - OrderProduct_Bean.getQuantity() - (OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock()));
                    getNowProductSaleStatus(ProductSaleStatus.新增待入庫存, OrderProduct_Bean.getISBN(),0,0, WaitingIntoInStock,0,0);
                    return updateProductWaitingIntoInStock(OrderProduct_Bean, WaitingIntoInStock);
                }
            }
        }
        return "";
    }
    private String deleteWaitingIntoInStock(OrderSource OrderSource, OrderProduct_Bean OrderProduct_Bean){
        if(OrderSource == Order_Enum.OrderSource.待入倉單){
            int WaitingPurchaseQuantity = OrderProduct_Bean.getWaitingPurchaseQuantity() - OrderProduct_Bean.getQuantity();
            if(WaitingPurchaseQuantity == 0 && OrderProduct_Bean.getWaitingIntoInStock() != 0) {
                getNowProductSaleStatus(ProductSaleStatus.刪除待入庫存, OrderProduct_Bean.getISBN(),0,0,0,0,0);
                return updateProductWaitingIntoInStock(OrderProduct_Bean, 0);
            }else if(OrderProduct_Bean.getWaitingIntoInStock() != 0) {
                int WaitingIntoInStock = OrderProduct_Bean.getWaitingIntoInStock() - OrderProduct_Bean.getQuantity();
                if(WaitingIntoInStock < 0)  WaitingIntoInStock = 0;
                getNowProductSaleStatus(ProductSaleStatus.刪除待入庫存, OrderProduct_Bean.getISBN(),0,0, WaitingIntoInStock,0,0);
                return updateProductWaitingIntoInStock(OrderProduct_Bean, WaitingIntoInStock);
            }
        }else if(OrderSource == Order_Enum.OrderSource.進貨單){
            int remainWaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() - OrderProduct_Bean.getQuantity();
//                int remainWaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() + OrderProduct_Bean.getSafetyStock() - OrderProduct_Bean.getQuantity();
            if(remainWaitingIntoInStock < OrderProduct_Bean.getWaitingIntoInStock()) {
//                    remainWaitingIntoInStock = OrderProduct_Bean.getWaitingPurchaseQuantity() - OrderProduct_Bean.getQuantity();
                if(remainWaitingIntoInStock < 0) remainWaitingIntoInStock = 0;
                getNowProductSaleStatus(ProductSaleStatus.刪除待入庫存, OrderProduct_Bean.getISBN(),0,0, remainWaitingIntoInStock,0,0);
                return updateProductWaitingIntoInStock(OrderProduct_Bean, remainWaitingIntoInStock);
            }
        }else if(OrderSource == Order_Enum.OrderSource.待出貨單){
            getNowProductSaleStatus(ProductSaleStatus.刪除待入庫存, OrderProduct_Bean.getISBN(),0,0,0,0,0);
            return updateProductWaitingIntoInStock(OrderProduct_Bean,0);
        }
        return "";
    }
    private String judgeNeededPurchaseQuantity(OrderSource OrderSource, OrderProduct_Bean OrderProduct_Bean){
        int neededPurchaseQuantity;
        if(OrderSource == Order_Enum.OrderSource.待入倉單) {
            //  刪除待入倉單時，如果沒有待入庫存，並且有待出貨量：代表有出貨的商品，需量必須增加(一起判斷存量是否不足安全庫存)
            if(OrderProduct_Bean.getWaitingIntoInStock() != 0 && OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getWaitingIntoInStock()){
                if(OrderProduct_Bean.getWaitingShipmentQuantity() != 0) //  有待出貨，需量不能為0
                    neededPurchaseQuantity = OrderProduct_Bean.getQuantity() - OrderProduct_Bean.getWaitingIntoInStock();
                else    //  無待出貨所以清空需量，等需要出貨的時候在重新計算存量、安全存量、出貨的對應數量。
                    neededPurchaseQuantity = 0;
                getNowProductSaleStatus(ProductSaleStatus.新增需量, OrderProduct_Bean.getISBN(),0,0,0, neededPurchaseQuantity,0);
                return updateProductNeededPurchaseQuantity(OrderProduct_Bean, neededPurchaseQuantity);
            }else if(OrderProduct_Bean.getWaitingIntoInStock() == 0 && OrderProduct_Bean.getWaitingShipmentQuantity() != 0){
                neededPurchaseQuantity = OrderProduct_Bean.getNeededPurchaseQuantity() + OrderProduct_Bean.getQuantity();
                getNowProductSaleStatus(ProductSaleStatus.新增需量, OrderProduct_Bean.getISBN(),0,0,0, neededPurchaseQuantity,0);
                return updateProductNeededPurchaseQuantity(OrderProduct_Bean, neededPurchaseQuantity);
            }
        }
        return "";
    }
    private String addNeededPurchaseQuantity(OrderSource OrderSource, OrderProduct_Bean OrderProduct_Bean){
        int neededPurchaseQuantity;
        if(OrderSource == Order_Enum.OrderSource.待出貨單) {
            if(OrderProduct_Bean.getNeededPurchaseQuantity() != 0)  //  有需量：需量 = 需量 + 進貨數量
                neededPurchaseQuantity = OrderProduct_Bean.getNeededPurchaseQuantity() + OrderProduct_Bean.getQuantity();
            else    //  無需量：需量 = 進貨數量 - 庫存
                neededPurchaseQuantity = OrderProduct_Bean.getQuantity();
            getNowProductSaleStatus(ProductSaleStatus.新增需量, OrderProduct_Bean.getISBN(),0,0,0, neededPurchaseQuantity,0);
            return updateProductNeededPurchaseQuantity(OrderProduct_Bean, neededPurchaseQuantity);
        }
        return null;
    }
    private String deleteNeededPurchaseQuantity(OrderSource OrderSource, OrderProduct_Bean OrderProduct_Bean){
        int neededPurchaseQuantity;
        if(OrderSource == Order_Enum.OrderSource.待入倉單) {
            neededPurchaseQuantity = OrderProduct_Bean.getNeededPurchaseQuantity() - OrderProduct_Bean.getQuantity();
            if(neededPurchaseQuantity < 0)  neededPurchaseQuantity = 0;
            getNowProductSaleStatus(ProductSaleStatus.刪除需量, OrderProduct_Bean.getISBN(),0,0,0, neededPurchaseQuantity,0);
            return updateProductNeededPurchaseQuantity(OrderProduct_Bean, neededPurchaseQuantity);
        }else if(OrderSource == Order_Enum.OrderSource.待出貨單){
            if(OrderProduct_Bean.getNeededPurchaseQuantity() != 0) {
                neededPurchaseQuantity = OrderProduct_Bean.getNeededPurchaseQuantity() - OrderProduct_Bean.getQuantity();
                if(neededPurchaseQuantity < 0 || OrderProduct_Bean.getWaitingShipmentQuantity() == OrderProduct_Bean.getQuantity()) neededPurchaseQuantity = 0;
                getNowProductSaleStatus(ProductSaleStatus.刪除需量, OrderProduct_Bean.getISBN(),0,0,0, neededPurchaseQuantity,0);
                return updateProductNeededPurchaseQuantity(OrderProduct_Bean, neededPurchaseQuantity);
            }
        }
        return "";
    }
    private String addWaitingShipmentQuantity(OrderProduct_Bean OrderProduct_Bean){
        int WaitingShipmentQuantity = OrderProduct_Bean.getQuantity() + OrderProduct_Bean.getWaitingShipmentQuantity();
        getNowProductSaleStatus(ProductSaleStatus.新增待出貨量, OrderProduct_Bean.getISBN(),0,0,0,0, WaitingShipmentQuantity);
        return updateProductWaitingShipmentQuantity(OrderProduct_Bean, WaitingShipmentQuantity);

    }
    private String deleteWaitingShipmentQuantity(OrderProduct_Bean OrderProduct_Bean){
        int WaitingShipmentQuantity = OrderProduct_Bean.getWaitingShipmentQuantity() - OrderProduct_Bean.getQuantity();
        getNowProductSaleStatus(ProductSaleStatus.刪除待出貨量, OrderProduct_Bean.getISBN(),0,0,0,0, WaitingShipmentQuantity);
        return updateProductWaitingShipmentQuantity(OrderProduct_Bean, WaitingShipmentQuantity);
    }
    private String updateProductWaitingPurchaseQuantity(OrderProduct_Bean OrderProduct_Bean, int WaitingPurchaseQuantity){
        return "if EXISTS " +
                "(select 1 from ProductSaleStatus where store_id = '" + OrderProduct_Bean.getStore_id() + "')  " +
                "BEGIN " +
                "update ProductSaleStatus set WaitingPurchaseQuantity = '" + WaitingPurchaseQuantity + "' where store_id = '" + OrderProduct_Bean.getStore_id() + "' " +
                "END ELSE BEGIN " +
                "insert into ProductSaleStatus (store_id,ISBN,WaitingPurchaseQuantity,WaitingIntoInStock,NeededPurchaseQuantity,WaitingShipmentQuantity) values ((select id from store where ISBN = '" + OrderProduct_Bean.getISBN() + "'),'" + OrderProduct_Bean.getISBN() + "','" + WaitingPurchaseQuantity + "','0','0','0') " +
                "END ";
    }
    private String updateProductWaitingIntoInStock(OrderProduct_Bean OrderProduct_Bean, int WaitingIntoInStock){
        return "if EXISTS " +
                "(select 1 from ProductSaleStatus where store_id = '" + OrderProduct_Bean.getStore_id() + "')  " +
                "BEGIN " +
                "update ProductSaleStatus set WaitingIntoInStock = '" + WaitingIntoInStock + "' where store_id = '" + OrderProduct_Bean.getStore_id() + "' " +
                "END ELSE BEGIN " +
                "insert into ProductSaleStatus (store_id,ISBN,WaitingPurchaseQuantity,WaitingIntoInStock,NeededPurchaseQuantity,WaitingShipmentQuantity) values ((select id from store where ISBN = '" + OrderProduct_Bean.getISBN() + "'),'" + OrderProduct_Bean.getISBN() + "','0','" + WaitingIntoInStock + "','0','0')" +
                "END ";
    }
    private String updateProductNeededPurchaseQuantity(OrderProduct_Bean OrderProduct_Bean, int NeededPurchaseQuantity){
        return "if EXISTS " +
                "(select 1 from ProductSaleStatus where store_id = '" + OrderProduct_Bean.getStore_id() + "')  " +
                "BEGIN " +
                "update ProductSaleStatus set NeededPurchaseQuantity = '" + NeededPurchaseQuantity + "' where store_id = '" + OrderProduct_Bean.getStore_id() + "' " +
                "END ELSE BEGIN " +
                "insert into ProductSaleStatus (store_id,ISBN,WaitingPurchaseQuantity,WaitingIntoInStock,NeededPurchaseQuantity,WaitingShipmentQuantity) values ((select id from store where ISBN = '" + OrderProduct_Bean.getISBN() + "'),'" + OrderProduct_Bean.getISBN() + "','0','0','" + NeededPurchaseQuantity + "','0')" +
                "END ";
    }
    private String updateProductWaitingShipmentQuantity(OrderProduct_Bean OrderProduct_Bean, int WaitingShipmentQuantity){
        return "if EXISTS " +
                "(select 1 from ProductSaleStatus where store_id = '" + OrderProduct_Bean.getStore_id() + "')  " +
                "BEGIN " +
                "update ProductSaleStatus set WaitingShipmentQuantity = '" + WaitingShipmentQuantity + "' where store_id = '" + OrderProduct_Bean.getStore_id() + "' " +
                "END ELSE BEGIN " +
                "insert into ProductSaleStatus (store_id,ISBN,WaitingPurchaseQuantity,WaitingIntoInStock,NeededPurchaseQuantity,WaitingShipmentQuantity) values ((select id from store where ISBN = '" + OrderProduct_Bean.getISBN() + "'),'" + OrderProduct_Bean.getISBN() + "','0','0','0','" + WaitingShipmentQuantity + "')" +
                "END ";
    }
    private String updateProductInStock(String ISBN, int InStock){
        return "update Store set InStock = '" + InStock + "' where ISBN = '" + ISBN + "' ";
    }
    private void getNowProductSaleStatus(ProductSaleStatus ProductSaleStatus, String ISBN, int InStock, int WaitingPurchaseQuantity, int WaitingIntoInStock, int NeededPurchaseQuantity, int WaitingShipmentQuantity){
        String oldInStock = "0", oldSafetyStock = "0", oldWaitingPurchaseQuantity = "0", oldWaitingIntoInStock = "0", oldNeededPurchaseQuantity = "0", oldWaitingShipmentQuantity = "0";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.InStock,A.SafetyStock,B.WaitingPurchaseQuantity,B.WaitingIntoInStock,B.NeededPurchaseQuantity,B.WaitingShipmentQuantity from Store A inner join ProductSaleStatus B on A.id = B.store_id where A.ISBN = ?");
            PreparedStatement.setString(1,ISBN);
            Rs = PreparedStatement.executeQuery();
            if(Rs.next()){
                oldInStock = Rs.getString(1);
                oldSafetyStock = Rs.getString(2);
                oldWaitingPurchaseQuantity = Rs.getString(3);
                oldWaitingIntoInStock = Rs.getString(4);
                oldNeededPurchaseQuantity = Rs.getString(5);
                oldWaitingShipmentQuantity = Rs.getString(6);
            }
            if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增存量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + " → " + InStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除存量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + " → " + InStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待入倉量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + " → " + WaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入倉量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + " → " + WaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.判斷待入庫存)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + " → " + WaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待入庫存)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + " → " + WaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待入庫存)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + " → " + WaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增需量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + " → " + NeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除需量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + " → " + NeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.新增待出貨量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity + " → " + WaitingShipmentQuantity);
            else if(ProductSaleStatus == Order_Enum.ProductSaleStatus.刪除待出貨量)
                ERPApplication.Logger.info("※ [當前商品銷售狀態 → " + ProductSaleStatus.name() + "] ISBN : " + ISBN + "  存量:" + oldInStock + "  安全存量:" + oldSafetyStock + "  待入倉量:" + oldWaitingPurchaseQuantity + "  待入庫存:" + oldWaitingIntoInStock + "  需量:" + oldNeededPurchaseQuantity + "  待出貨量:" + oldWaitingShipmentQuantity + " → " + WaitingShipmentQuantity);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
    }
    /** Update product info in database
     * @param OrderProduct_Bean the bean of order product
     * */
    public boolean modifyProduct(OrderProduct_Bean OrderProduct_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Store set InternationalCode = ?, FirmCode = ?,ProductName = ?,Remark = ? where ISBN = ? " +
                    "update store_price set BatchPrice = ?, SinglePrice = ?, Pricing = ?,VipPrice1 = ?, VipPrice2 = ?, VipPrice3 = ? from Store A inner join store_price B on A.id = B.store_id where A.ISBN = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,OrderProduct_Bean.getInternationalCode());
            PreparedStatement.setString(2,OrderProduct_Bean.getFirmCode());
            PreparedStatement.setString(3,OrderProduct_Bean.getProductName());
            PreparedStatement.setString(4,OrderProduct_Bean.getRemark());
            PreparedStatement.setString(5,OrderProduct_Bean.getISBN());

            PreparedStatement.setBigDecimal(6, BigDecimal.valueOf(OrderProduct_Bean.getBatchPrice()));
            PreparedStatement.setBigDecimal(7, BigDecimal.valueOf(OrderProduct_Bean.getSinglePrice()));
            PreparedStatement.setBigDecimal(8, BigDecimal.valueOf(OrderProduct_Bean.getPricing()));
            PreparedStatement.setBigDecimal(9, BigDecimal.valueOf(OrderProduct_Bean.getVipPrice1()));
            PreparedStatement.setBigDecimal(10, BigDecimal.valueOf(OrderProduct_Bean.getVipPrice2()));
            PreparedStatement.setBigDecimal(11, BigDecimal.valueOf(OrderProduct_Bean.getVipPrice3()));
            PreparedStatement.setString(12,OrderProduct_Bean.getISBN());
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Get product info. The product price amount is according to object
     * @param OrderObject the object of order
     * @param ProductSearchColumn the column of search product
     * @param ObjectID the id of object
     * @param ProductColumnText the text of product column
     * */
    public ObservableList<OrderProduct_Bean> getProduct(OrderObject OrderObject, ProductSearchColumn ProductSearchColumn, String ObjectID, String ProductColumnText){
        ObservableList<OrderProduct_Bean> ProductList = FXCollections.observableArrayList();
        String Query = "";
        if(ProductSearchColumn == Order_Enum.ProductSearchColumn.品名) {
            Query = "select A.id,A.ISBN,A.InternationalCode,A.FirmCode,A.ProductCode,A.ProductName,A.Unit,C.BatchPrice,C.SinglePrice,C.Pricing,C.VipPrice1,C.VipPrice2,C.VipPrice3,A.SupplyStatus,A.InStock,A.SafetyStock,B.WaitingPurchaseQuantity,B.WaitingIntoInStock,B.NeededPurchaseQuantity,B.WaitingShipmentQuantity,A.Remark,D.NewFirstCategory,I.KeyInDate," +
                    "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL)," +
                    "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL)," +
                    "(SELECT Min(E.SinglePrice) FROM Orders_Items E inner join Orders F on E.Order_id = F.id where F.AlreadyOrderNumber is not null and F.OrderSource = '" + OrderObject.ordinal()  + "' and F.status = '0' and F.ObjectID = '" + ObjectID + "' and E.ISBN = A.ISBN)," +
                    "(select SUM(G.Quantity) from Orders_Items G inner join Orders H on G.Order_id = H.id where H.AlreadyOrderNumber is not null and H.OrderSource = '1' and H.status = '0' and G.ISBN=A.ISBN) " +
                    "from Store A inner join ProductSaleStatus B on A.id = B.store_id inner join store_price C on A.id = C.store_id inner join store_category D on A.id = D.store_id inner join store_date I on A.id = I.store_id " +
                    "where A." + ProductSearchColumn.value() + " like ?";
        }else if(ProductSearchColumn == Order_Enum.ProductSearchColumn.ISBN) {
            Query = "select A.id,A.ISBN,A.InternationalCode,A.FirmCode,A.ProductCode,A.ProductName,A.Unit,C.BatchPrice,C.SinglePrice,C.Pricing,C.VipPrice1,C.VipPrice2,C.VipPrice3,A.SupplyStatus,A.InStock,A.SafetyStock,B.WaitingPurchaseQuantity,B.WaitingIntoInStock,B.NeededPurchaseQuantity,B.WaitingShipmentQuantity,A.Remark,D.NewFirstCategory,I.KeyInDate," +
                    "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL)," +
                    "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL)," +
                    "(SELECT Min(E.SinglePrice) FROM Orders_Items E inner join Orders F on E.Order_id = F.id where F.AlreadyOrderNumber is not null and F.OrderSource = '" + OrderObject.ordinal()  + "' and F.status = '0' and F.ObjectID = '" + ObjectID + "' and E.ISBN = A.ISBN)," +
                    "(select SUM(G.Quantity) from Orders_Items G inner join Orders H on G.Order_id = H.id where H.AlreadyOrderNumber is not null and H.OrderSource = '1' and H.status = '0' and G.ISBN=A.ISBN) " +
                    "from Store A inner join ProductSaleStatus B on A.id = B.store_id inner join store_price C on A.id = C.store_id inner join store_category D on A.id = D.store_id inner join store_date I on A.id = I.store_id " +
                    "where A." + ProductSearchColumn.value() + " like ?";
        }else if(ProductSearchColumn == Order_Enum.ProductSearchColumn.標籤) {
            Query = "select A.id,A.ISBN,A.InternationalCode,A.FirmCode,A.ProductCode,A.ProductName,A.Unit,D.BatchPrice,D.SinglePrice,D.Pricing,D.VipPrice1,D.VipPrice2,D.VipPrice3,A.SupplyStatus,A.InStock,A.SafetyStock,C.WaitingPurchaseQuantity,C.WaitingIntoInStock,C.NeededPurchaseQuantity,C.WaitingShipmentQuantity,A.Remark,E.NewFirstCategory,I.KeyInDate," +
                    "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL)," +
                    "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL)," +
                    "(SELECT Min(E.SinglePrice) FROM Orders_Items E inner join Orders F on E.Order_id = F.id where F.AlreadyOrderNumber is not null and F.OrderSource = '" + OrderObject.ordinal()  + "' and F.status = '0' and F.ObjectID = '" + ObjectID + "' and E.ISBN = A.ISBN)," +
                    "(select SUM(G.Quantity) from Orders_Items G inner join Orders H on G.Order_id = H.id where H.AlreadyOrderNumber is not null and H.OrderSource = '1' and H.status = '0' and G.ISBN=A.ISBN) " +
                    "from ProductTag B inner join Store A on A.ISBN = B.ISBN inner join ProductSaleStatus C on A.id = C.Store_id inner join store_price D on A.id = D.store_id inner join store_category E on A.id = E.store_id inner join store_date I on A.id = I.store_id " +
                    "where B." + ProductSearchColumn.value() + " = ?";
        }
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            ERPApplication.Logger.info("search product：" + ProductSearchColumn.name() + "  " + ProductColumnText);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(ProductSearchColumn == Order_Enum.ProductSearchColumn.品名)    PreparedStatement.setString(1,"%" + ProductColumnText + "%");
            else if(ProductSearchColumn == Order_Enum.ProductSearchColumn.ISBN)    PreparedStatement.setString(1,ProductColumnText + "%");
            else if(ProductSearchColumn == Order_Enum.ProductSearchColumn.標籤)    PreparedStatement.setString(1,ProductColumnText);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setStore_id(Rs.getInt(1));
                OrderProduct_Bean.setISBN(Rs.getString(2));
                OrderProduct_Bean.setInternationalCode(Rs.getString(3));
                OrderProduct_Bean.setFirmCode(Rs.getString(4));
                OrderProduct_Bean.setProductCode(Rs.getString(5));
                OrderProduct_Bean.setProductName(Rs.getString(6));
                OrderProduct_Bean.setQuantity(1);
                OrderProduct_Bean.setUnit(Rs.getString(7));
                OrderProduct_Bean.setBatchPrice(ToolKit.RoundingDouble(Rs.getDouble(8)));
                OrderProduct_Bean.setSinglePrice(ToolKit.RoundingDouble(Rs.getDouble(9)));
                OrderProduct_Bean.setPricing(ToolKit.RoundingDouble(Rs.getDouble(10)));
                OrderProduct_Bean.setVipPrice1(ToolKit.RoundingDouble(Rs.getDouble(11)));
                OrderProduct_Bean.setVipPrice2(ToolKit.RoundingDouble(Rs.getDouble(12)));
                OrderProduct_Bean.setVipPrice3(ToolKit.RoundingDouble(Rs.getDouble(13)));
                OrderProduct_Bean.setSupplyStatus(Rs.getString(14));
                OrderProduct_Bean.setInStock(Rs.getInt(15));
                OrderProduct_Bean.setSafetyStock(Rs.getInt(16));
                OrderProduct_Bean.setWaitingPurchaseQuantity(Rs.getInt(17));
                OrderProduct_Bean.setWaitingIntoInStock(Rs.getInt(18));
                OrderProduct_Bean.setNeededPurchaseQuantity(Rs.getInt(19));
                OrderProduct_Bean.setWaitingShipmentQuantity(Rs.getInt(20));
                OrderProduct_Bean.setRemark(Rs.getString(21));
                OrderProduct_Bean.setFirstCategory(Rs.getString(22));
                OrderProduct_Bean.setKeyInDate(Rs.getString(23));
                OrderProduct_Bean.setPurchaseDate(Rs.getString(24));
                OrderProduct_Bean.setSaleDate(Rs.getString(25));
                OrderProduct_Bean.setMinimumPrice(Rs.getString(26));
                OrderProduct_Bean.setSaleQuantity(Rs.getString(27) == null ? 0 : Rs.getInt(27));
                ProductList.add(OrderProduct_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ProductList;
    }
    public void refreshProductSeriesNumber(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> OrderProductList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(int index = 0 ; index < OrderProductList.size() ; index++){
            OrderProduct_Bean OrderProduct_Bean = OrderProductList.get(index);
            OrderProduct_Bean.setSeriesNumber(index+1);
        }
    }
    public void refreshProductWannaAmount(OrderObject OrderObject, ObjectInfo_Bean ObjectInfo_Bean, OrderProduct_Bean OrderProduct_Bean){
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "BEGIN TRY BEGIN TRANSACTION ";
                query = query + "if exists " +
                    "(SELECT 1 FROM Orders_Items T1 INNER JOIN Orders T2 ON T1.Order_id = T2.id WHERE T2.AlreadyOrderNumber IS NOT NULL AND T2.ObjectID = ? and T2.OrderSource = ? and T2.status = ? AND T1.ISBN = ? union " +
                        "SELECT 1 FROM SubBill_Items T1 INNER JOIN SubBill T2 ON T1.SubBill_id = T2.id WHERE T2.AlreadyOrderNumber IS NOT NULL AND T2.ObjectID = ? and T2.OrderSource = ? and T2.status = ? AND T1.ISBN = ?) " +
                    "BEGIN ";
            query = query +
                    "select top 1 * from (SELECT 1 as 'exist'," + (OrderObject == Order_Enum.OrderObject.廠商 ? "BatchPrice" : "SinglePrice") + " as price,T4.max as AlreadyOrderNumber FROM Orders_Items T1 INNER JOIN (SELECT T2.id, MAX(T3.AlreadyOrderNumber) as max FROM Orders_Items T2 INNER JOIN Orders T3 ON T2.Order_id = T3.id WHERE T3.AlreadyOrderNumber IS NOT NULL AND T3.ObjectID = ? and T3.OrderSource = ? and T3.status = ? AND T2.ISBN = ? group by T2.id) " +
                    "T4 ON T1.id = T4.id union " +
                    "SELECT 1 as 'exist',SinglePrice as price,T4.max as AlreadyOrderNumber FROM SubBill_Items T1 INNER JOIN (SELECT T2.id, MAX(T3.AlreadyOrderNumber) as max FROM SubBill_Items T2 INNER JOIN SubBill T3 ON T2.SubBill_id = T3.id WHERE T3.AlreadyOrderNumber IS NOT NULL AND T3.ObjectID = ? and T3.OrderSource = ? and T3.status = ? AND T2.ISBN = ? group by T2.id) " +
                    "T4 ON T1.id = T4.id) TABLE_ALL order by AlreadyOrderNumber desc " +
                    "END ELSE BEGIN " +
                    (OrderObject == Order_Enum.OrderObject.廠商 ? "select '0'" : "select 0,SaleModel from Customer where ObjectID = ?") + " END ";
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,ObjectInfo_Bean.getObjectID());
            PreparedStatement.setInt(2,OrderObject.ordinal());
            PreparedStatement.setInt(3,OrderStatus.有效貨單.ordinal());
            PreparedStatement.setString(4,OrderProduct_Bean.getISBN());
            PreparedStatement.setString(5,ObjectInfo_Bean.getObjectID());
            PreparedStatement.setInt(6,OrderObject.ordinal());
            PreparedStatement.setInt(7,OrderStatus.有效貨單.ordinal());
            PreparedStatement.setString(8,OrderProduct_Bean.getISBN());
            PreparedStatement.setString(9,ObjectInfo_Bean.getObjectID());
            PreparedStatement.setInt(10,OrderObject.ordinal());
            PreparedStatement.setInt(11,OrderStatus.有效貨單.ordinal());
            PreparedStatement.setString(12,OrderProduct_Bean.getISBN());
            PreparedStatement.setString(13,ObjectInfo_Bean.getObjectID());
            PreparedStatement.setInt(14,OrderObject.ordinal());
            PreparedStatement.setInt(15,OrderStatus.有效貨單.ordinal());
            PreparedStatement.setString(16,OrderProduct_Bean.getISBN());
            if(OrderObject == Order_Enum.OrderObject.客戶){
                PreparedStatement.setString(17,ObjectInfo_Bean.getObjectID());
            }
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                boolean isExist = Rs.getBoolean(1);
                if(OrderObject == Order_Enum.OrderObject.廠商) {
                    if(isExist){
                        OrderProduct_Bean.setBatchPrice(Rs.getDouble(2));
                    }
                    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getBatchPrice()));
                }else if(OrderObject == Order_Enum.OrderObject.客戶){
                    if(isExist){
                        OrderProduct_Bean.setSinglePrice(Rs.getDouble(2));
                    }else{
                        CustomerSaleModel CustomerSaleModel = ObjectInfo_Enum.CustomerSaleModel.values()[Rs.getInt(2)];
                        if(CustomerSaleModel == ObjectInfo_Enum.CustomerSaleModel.成本價)
                            OrderProduct_Bean.setSinglePrice(OrderProduct_Bean.getBatchPrice());
                        else if(CustomerSaleModel == ObjectInfo_Enum.CustomerSaleModel.定價)
                            OrderProduct_Bean.setSinglePrice(OrderProduct_Bean.getPricing());
                        else if(CustomerSaleModel == ObjectInfo_Enum.CustomerSaleModel.VipPrice1)
                            OrderProduct_Bean.setSinglePrice(OrderProduct_Bean.getVipPrice1());
                        else if(CustomerSaleModel == ObjectInfo_Enum.CustomerSaleModel.VipPrice2)
                            OrderProduct_Bean.setSinglePrice(OrderProduct_Bean.getVipPrice2());
                        else if(CustomerSaleModel == ObjectInfo_Enum.CustomerSaleModel.VipPrice3)
                            OrderProduct_Bean.setSinglePrice(OrderProduct_Bean.getVipPrice3());
                    }
                    OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
    }
    public ObservableList<ProductCategory_Bean> getAllFirstCategoryOfProduct(CategoryLayer CategoryLayer){
        ObservableList<ProductCategory_Bean> ProductFirstCategoryList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select CategoryID,CategoryName from ProductCategory where CategoryLayer = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,CategoryLayer.getLayer());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
                String CategoryID = Rs.getString(1);
                String CategoryName = Rs.getString(2);
                CategoryID = ToolKit.fillZero(CategoryID,2);
                ProductCategory_Bean.setCategoryID(CategoryID);
                ProductCategory_Bean.setCategoryName(CategoryName);
                ProductCategory_Bean.setCategoryLayer(CategoryLayer);
                ProductFirstCategoryList.add(ProductCategory_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{   SqlAdapter.closeConnectParameter(PreparedStatement,Rs);    }
        return ProductFirstCategoryList;
    }
    public ObservableList<OrderProduct_Bean> getProductGenerator(String objectID, ProductGenerator_Bean productGenerator_bean){
        ObservableList<OrderProduct_Bean> ProductList = FXCollections.observableArrayList();
        int ProductQuantity = productGenerator_bean.getProductQuantity();
        int SinglePrice = productGenerator_bean.getSinglePrice();
        ArrayList<String> FirstCategoryIDList = productGenerator_bean.getFirstCategoryIDList();
        ObservableList<OrderProduct_Bean> SaveProductList = productGenerator_bean.getSaveProductList();

        if(SaveProductList != null)
            ProductQuantity = ProductQuantity - SaveProductList.size();

        String Query = "select TOP " + ProductQuantity + " * from (";
        String storeQuery = "select A.id,A.ISBN,A.InternationalCode,A.FirmCode,A.ProductName,A.ProductCode,A.Unit,C.BatchPrice,C.SinglePrice,C.Pricing,C.VipPrice1,C.VipPrice2,C.VipPrice3,A.SupplyStatus,A.InStock,A.SafetyStock,B.WaitingPurchaseQuantity,B.WaitingIntoInStock,B.NeededPurchaseQuantity,B.WaitingShipmentQuantity,E.NewFirstCategory,F.KeyInDate," +
                "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL) as 'PurchaseDate'," +
                "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL) as 'SaleDate'" +
                " from Store A inner join ProductSaleStatus B on A.ISBN = B.ISBN inner join Store_Price C on A.id = C.store_id inner join store_Category E on A.id = E.store_id INNER JOIN store_date F on A.id = F.store_id where C.SinglePrice <= ? and C.SinglePrice > 0";
        if(FirstCategoryIDList != null)
            storeQuery = storeQuery + getSelectedFirstCategoryByProductGenerator(WaitConfirmTable.進銷存商品, FirstCategoryIDList);
        if(SaveProductList != null)
            storeQuery = storeQuery + getSaveProductListByProductGenerator(SaveProductList);
        if(productGenerator_bean.isRemoveDuplicateProduct()){
            ArrayList<String> ISBNList = getDuplicateProduct(objectID, productGenerator_bean.getDuplicateProductDate());
            if(ISBNList != null){
                for(String ISBN : ISBNList)
                    storeQuery = storeQuery + " and A.ISBN != '" + ISBN + "'";
            }
        }
      Query = Query + storeQuery;

        if(productGenerator_bean.isIncludeWaitingConfirmProduct()){
            String checkStoreQuery = "select null as id,'' as ISBN, '' as InternationalCode,'' as FirmCode,ProductName,ProductCode,Unit,BatchPrice,SinglePrice,Pricing,VipPrice1,VipPrice2,VipPrice3,SupplyStatus,0 as Instock,0 as SafetyStock,0 as WaitingPurchaseQuantity," +
                    "0 as WaitingIntoInStock,0 as NeededPurchaseQuantity,0 as WaitingShipmentQuantity,NewFirstCategory,null as KeyInDate,null as PurchaseDate,null as SaleDate from checkstore where SinglePrice <= ? and SinglePrice > 0 and Status = ?";
            if(FirstCategoryIDList != null)
                checkStoreQuery = checkStoreQuery + getSelectedFirstCategoryByProductGenerator(WaitConfirmTable.待確認商品, FirstCategoryIDList);
            Query = Query + " union " + checkStoreQuery;
        }
        Query = Query + ") TABEL_ALL order by NEWID()";
//        ERPApplication.Logger.info(Query);
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ToolKit.RoundingString(true,SinglePrice/1.05));
            if(productGenerator_bean.isIncludeWaitingConfirmProduct()){
                PreparedStatement.setString(2,ToolKit.RoundingString(true,SinglePrice/1.05));
                PreparedStatement.setInt(3, Product_Enum.WaitConfirmStatus.新增.ordinal());
            }
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setStore_id(Rs.getInt(1));
                OrderProduct_Bean.setISBN(Rs.getString(2));
                OrderProduct_Bean.setInternationalCode(Rs.getString(3));
                OrderProduct_Bean.setFirmCode(Rs.getString(4));
                OrderProduct_Bean.setProductName(Rs.getString(5));
                OrderProduct_Bean.setProductCode(Rs.getString(6));
                OrderProduct_Bean.setQuantity(1);
                OrderProduct_Bean.setUnit(Rs.getString(7));
                OrderProduct_Bean.setBatchPrice(Rs.getDouble(9));
//                OrderProduct_Bean.setBatchPrice(Rs.getDouble(7));
                OrderProduct_Bean.setSinglePrice(Rs.getDouble(9));
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(Rs.getDouble(9)));
                OrderProduct_Bean.setPricing(Rs.getDouble(10));
                OrderProduct_Bean.setVipPrice1(Rs.getDouble(11));
                OrderProduct_Bean.setVipPrice2(Rs.getDouble(12));
                OrderProduct_Bean.setVipPrice3(Rs.getDouble(13));
                OrderProduct_Bean.setSupplyStatus(Rs.getString(14));
                OrderProduct_Bean.setInStock(Rs.getInt(15));
                OrderProduct_Bean.setSafetyStock(Rs.getInt(16));
                OrderProduct_Bean.setWaitingPurchaseQuantity(Rs.getInt(17));
                OrderProduct_Bean.setWaitingIntoInStock(Rs.getInt(18));
                OrderProduct_Bean.setNeededPurchaseQuantity(Rs.getInt(19));
                OrderProduct_Bean.setWaitingShipmentQuantity(Rs.getInt(20));
                OrderProduct_Bean.setFirstCategory(Rs.getString(21));
                OrderProduct_Bean.setRemark("");
                OrderProduct_Bean.setKeyInDate(Rs.getString(22));
                OrderProduct_Bean.setPurchaseDate(Rs.getString(23));
                OrderProduct_Bean.setSaleDate(Rs.getString(24));
                ProductList.add(OrderProduct_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }
        return ProductList;
    }
    private String getSelectedFirstCategoryByProductGenerator(WaitConfirmTable waitConfirmTable, ArrayList<String> FirstCategoryIDList){
        String Query = " and (";
        for(int index = 0; index < FirstCategoryIDList.size(); index++){
            String FirstCategoryID = FirstCategoryIDList.get(index);
            if(waitConfirmTable == WaitConfirmTable.待確認商品){
                Query = Query + " NewFirstCategory = '" + FirstCategoryID + "'";
            }else if(waitConfirmTable == WaitConfirmTable.進銷存商品){
                Query = Query + " E.NewFirstCategory = '" + FirstCategoryID + "'";
            }
            if(index != FirstCategoryIDList.size()-1)   Query = Query + " or ";
        }
        return Query + ")";
    }
    private String getSaveProductListByProductGenerator(ObservableList<OrderProduct_Bean> SaveProductList){
        String Query = "";
        for(OrderProduct_Bean OrderProduct_Bean : SaveProductList)
            Query = Query + " and A.ISBN != '" + OrderProduct_Bean.getISBN() + "'";
        return Query;
    }
    private ArrayList<String> getDuplicateProduct(String ObjectID, int duplicateProductDate){
        String Date = ToolKit.getTodayAddOrSubtractDay(-duplicateProductDate);
        ArrayList<String> ISBNList = null;
        String Query = "select DISTINCT A.ISBN from Store A left join Orders_Items B on A.ISBN = B.ISBN left join Orders C on B.Order_id = C.id where C.ObjectID = ? and C.AlreadyOrderDate >= ? and C.status = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ObjectID);
            PreparedStatement.setString(2,Date);
            PreparedStatement.setInt(3,OrderStatus.有效貨單.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(ISBNList == null)    ISBNList = new ArrayList<>();
                ISBNList.add(Rs.getString(1));
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ISBNList;
    }
    public OrderProduct_Bean insertCheckStoreToStore(OrderObject orderObject, String objectID, String productCode, double singlePrice){
        OrderProduct_Bean orderProduct_bean = null;
        String Query = "select * from CheckStore where ProductCode = ? and Status = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, productCode);
            preparedStatement.setInt(2, Product_Enum.WaitConfirmStatus.新增.ordinal());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String vendorCode = rs.getString("VendorCode");
                String newFirstCategory = rs.getString("NewFirstCategory");
                ProductInfo_Bean productInfo_bean = new ProductInfo_Bean();
                String ISBN = product_model.generateNewestISBN(vendorCode + newFirstCategory);
                productInfo_bean.setISBN(ISBN);
                productInfo_bean.setInternationalCode("");
                productInfo_bean.setFirmCode("");
                productInfo_bean.setProductCode(rs.getString("ProductCode"));
                productInfo_bean.setProductName(rs.getString("ProductName"));
                productInfo_bean.setUnit(rs.getString("Unit"));
                productInfo_bean.setBrand(rs.getString("Brand"));
                productInfo_bean.setDescribe(rs.getString("Describe"));
                productInfo_bean.setRemark(rs.getString("Remark"));
                productInfo_bean.setSupplyStatus(rs.getString("SupplyStatus"));
                productInfo_bean.setInStock("0");
                productInfo_bean.setSafetyStock("0");
                productInfo_bean.setInventoryQuantity(0);
                productInfo_bean.setVendorName(rs.getString("Vendor"));
                productInfo_bean.setVendorCode(vendorCode);
                productInfo_bean.setBatchPrice(ToolKit.RoundingString(true,rs.getDouble("BatchPrice")));
                double originalSinglePrice = rs.getDouble("SinglePrice");
                if(originalSinglePrice != singlePrice){
                    originalSinglePrice = singlePrice;
                }
                productInfo_bean.setSinglePrice(ToolKit.RoundingString(true,originalSinglePrice));
                productInfo_bean.setPricing(ToolKit.RoundingString(true,rs.getDouble("Pricing")));
                productInfo_bean.setVipPrice1(ToolKit.RoundingString(true,rs.getDouble("VipPrice1")));
                productInfo_bean.setVipPrice2(ToolKit.RoundingString(true,rs.getDouble("VipPrice2")));
                productInfo_bean.setVipPrice3(ToolKit.RoundingString(true,rs.getDouble("VipPrice3")));
                productInfo_bean.setDiscount(1.0);
                productInfo_bean.setVendorFirstCategory_id(rs.getObject("FirstCategory_Id") == null ? null : rs.getInt("FirstCategory_Id"));
                productInfo_bean.setVendorSecondCategory_id(rs.getObject("SecondCategory_Id") == null ? null : rs.getInt("SecondCategory_Id"));
                productInfo_bean.setVendorThirdCategory_id(rs.getObject("ThirdCategory_Id") == null ? null : rs.getInt("ThirdCategory_Id"));
                productInfo_bean.setFirstCategory(newFirstCategory);
                productInfo_bean.setSecondCategory("");
                productInfo_bean.setThirdCategory("");
                productInfo_bean.setProductArea("");
                productInfo_bean.setProductFloor("");

                productInfo_bean.setYahooCategory("");
                productInfo_bean.setRutenCategory("");
                productInfo_bean.setShopeeCategory(null);

                productInfo_bean.setPicture1(rs.getString("Picture1"));
                productInfo_bean.setPicture2(rs.getString("Picture2"));
                productInfo_bean.setPicture3(rs.getString("Picture3"));
                productInfo_bean.setProductTag(FXCollections.observableArrayList());
                if(product_model.insertProduct(productInfo_bean,true,false)){
                    orderProduct_bean = getProduct(orderObject, ProductSearchColumn.ISBN, objectID, ISBN).get(0);
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return orderProduct_bean;
    }
    /** Delete file of export quotation that file name is repeated
     * @param outputFilePath the path of picture
     * @param ExportQuotation_Bean the parameters of export quotation in bean
     * */
    public void deleteRepeatExportQuotationFileName(String outputFilePath, ExportQuotation_Bean ExportQuotation_Bean){
        File[] FileList = new File(outputFilePath).listFiles();
        if (FileList != null) {
            for(File File: FileList){
                if(File.isFile()){
                    if(File.getName().contains(ExportQuotation_Bean.getOrderNumber()) && File.getName().contains(ExportQuotation_Bean.getExportFormat().name()) && File.getName().contains(ExportQuotation_Bean.getVendorNickName()) && File.getName().contains(ExportQuotation_Bean.getOrderSource().name()))
                        File.delete();
                }
            }
        }
    }
    public ObservableList<OrderReviewStatusRecord_Bean> getOrderReviewStatusRecord(int order_id){
        ObservableList<OrderReviewStatusRecord_Bean> orderReviewStatusRecordList = FXCollections.observableArrayList();
        String query = "select A.review_object,B.*,CASE WHEN EXISTS(select 1 from Orders_ReviewStatusPicture where reviewStatus_record_id = B.id) THEN '1' ELSE '0' END as isExistPicture  from Orders_ReviewStatus A inner join Orders_ReviewStatusRecord B on A.id = B.review_status_id where A.order_id = ? ";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,order_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean = new OrderReviewStatusRecord_Bean();
                OrderReviewStatusRecord_Bean.setId(Rs.getInt("id"));
                OrderReviewStatusRecord_Bean.setReview_status_id(Rs.getInt("review_status_id"));
                OrderReviewStatusRecord_Bean.setReviewObject(ReviewObject.values()[Rs.getInt("review_object")]);
                OrderReviewStatusRecord_Bean.setSubject(Rs.getString("subject"));
                OrderReviewStatusRecord_Bean.setRecord(Rs.getString("record"));
                OrderReviewStatusRecord_Bean.setRecord_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Rs.getTimestamp("record_time")));
                OrderReviewStatusRecord_Bean.setExistPicture(Rs.getBoolean("isExistPicture"));
                OrderReviewStatusRecord_Bean.setReviewStatus(ReviewStatus.values()[Rs.getInt("review_status")]);
                OrderReviewStatusRecord_Bean.setPictureList(OrderReviewStatusRecord_Bean.isExistPicture() ? getOrderReviewStatusPicture(OrderReviewStatusRecord_Bean.getId()) : null);
                orderReviewStatusRecordList.add(OrderReviewStatusRecord_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return orderReviewStatusRecordList;
    }
    private ArrayList<OrderReviewStatusPicture_Bean> getOrderReviewStatusPicture(int reviewStatus_record_id){
        ArrayList<OrderReviewStatusPicture_Bean> pictureList = new ArrayList<>();
        String query = "select * from Orders_ReviewStatusPicture where reviewStatus_record_id = ? ";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,reviewStatus_record_id);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                OrderReviewStatusPicture_Bean OrderReviewStatusPicture_Bean = new OrderReviewStatusPicture_Bean();
                OrderReviewStatusPicture_Bean.setId(Rs.getInt("id"));
                OrderReviewStatusPicture_Bean.setReviewStatus_record_id(reviewStatus_record_id);
                OrderReviewStatusPicture_Bean.setBase64(Rs.getString("picture"));
                pictureList.add(OrderReviewStatusPicture_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return pictureList;
    }
    public HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> getOrderReferenceFromDatabase(Order_Bean Order_Bean){
        HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = null;
        HashMap<Integer,Boolean> mainOrderReferenceMap = new HashMap<>();
        HashMap<Integer,Boolean> subBillReferenceMap = new HashMap<>();
        String query = "";
        if(Order_Bean.getOrderSource() != OrderSource.出貨子貨單)
            query = "select * from Orders_Reference A inner join Orders B on A.Order_id = B.id where A.Order_id = ? ";
        else
            query = "select * from SubBill_Reference A inner join SubBill B on A.SubBill_id = B.id where A.SubBill_id = ? ";

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, Order_Bean.getOrderID());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())  {
                Integer order_Reference_id = Rs.getObject("Order_Reference_Id") == null ? null : Rs.getInt("Order_Reference_Id");
                Integer subBill_Reference_id = Rs.getObject("SubBill_Reference_id") == null ? null : Rs.getInt("SubBill_Reference_id");
                if(order_Reference_id != null && subBill_Reference_id == null)
                    mainOrderReferenceMap.put(order_Reference_id,true);
                else if(order_Reference_id == null && subBill_Reference_id != null)
                    subBillReferenceMap.put(subBill_Reference_id,true);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        if(mainOrderReferenceMap.size() != 0 || subBillReferenceMap.size() != 0) {
            orderReferenceMap = new HashMap<>();
            orderReferenceMap.put(OrderSource.報價單,mainOrderReferenceMap);
            orderReferenceMap.put(OrderSource.出貨子貨單,subBillReferenceMap);
        }
        return orderReferenceMap;
    }
    public ObservableList<Order_Bean> getOrderReferenceByOrderID(HashMap<Integer,Boolean> mainOrderReferenceMap, HashMap<Integer,Boolean> subBillReferenceMap){
        int index = 0;
        ObservableList<Order_Bean> orderReferenceList = FXCollections.observableArrayList();
        String query = "select * from (";
        String Shipment = "select '報價單' AS '貨單',A.OrderDate,A.OrderNumber,A.ObjectID,B.ObjectName,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.isCheckout,C.TotalPriceIncludeTax,D.ProjectName,A.id from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = Order_id inner join Orders_ProjectInfo D on A.id = D.Order_id where A.OrderSource = ? and ";
        String ShipmentSubBill = "select '出貨子貨單' AS '貨單',A.OrderDate,A.OrderNumber,A.ObjectID,B.ObjectName,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.isCheckout,C.TotalPriceIncludeTax,D.ProjectName,A.id from SubBill A inner join Customer B on A.ObjectID = B.ObjectID inner join SubBill_Price C on A.id = SubBill_id inner join SubBill_ProjectInfo D on A.id = D.SubBill_id where A.OrderSource = ? and ";

        if(mainOrderReferenceMap.size() == 0 && subBillReferenceMap.size() != 0)
            query = ShipmentSubBill + getReferenceOrderIDQuery(subBillReferenceMap) + " order by AlreadyOrderDate desc,OrderDate desc ";
        else if(mainOrderReferenceMap.size() != 0 && subBillReferenceMap.size() == 0)
            query = Shipment + getReferenceOrderIDQuery(mainOrderReferenceMap) + "order by AlreadyOrderDate desc,OrderDate desc ";
        else
            query = query.concat(Shipment).concat(getReferenceOrderIDQuery(mainOrderReferenceMap)).concat(" union ").
                    concat(ShipmentSubBill).concat(getReferenceOrderIDQuery(subBillReferenceMap)).concat(") TABLE_ALL order by AlreadyOrderDate desc,OrderDate desc");
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            if((mainOrderReferenceMap.size() == 0 && subBillReferenceMap.size() != 0) || (mainOrderReferenceMap.size() != 0 && subBillReferenceMap.size() == 0))
                PreparedStatement.setInt(1,OrderObject.客戶.ordinal());
            else {
                PreparedStatement.setInt(1, OrderObject.客戶.ordinal());
                PreparedStatement.setInt(2, OrderObject.客戶.ordinal());
            }
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())  {
                index = index + 1;
                Order_Bean Order_Bean = new Order_Bean();
                Order_Bean.setOrderSource(Order_Enum.OrderSource.valueOf(Rs.getString(1)));
                Order_Bean.setOrderDate(Rs.getString(2));
                Order_Bean.setNowOrderNumber(Rs.getString(3));
                Order_Bean.setOrderObject(OrderObject.客戶);
                Order_Bean.setObjectID(Rs.getString(4));
                Order_Bean.setObjectName(Rs.getString(5));
                Order_Bean.setWaitingOrderDate(Rs.getString(6));
                Order_Bean.setWaitingOrderNumber(Rs.getString(7));
                Order_Bean.setAlreadyOrderDate(Rs.getString(8));
                Order_Bean.setAlreadyOrderNumber(Rs.getString(9));
                Order_Bean.setIsCheckout(CheckoutStatus.values()[Rs.getInt(10)].value());
                Order_Bean.setTotalPriceIncludeTax(Rs.getString(11));
                Order_Bean.setProjectName(Rs.getString(12));
                Order_Bean.setOrderID(Rs.getObject(13) == null ? null : Rs.getInt(13));
                Order_Bean.setProductList(getOrderItems(Order_Bean.getOrderSource(), Order_Bean.getOrderID()));

//                Order_Bean.setPersonInCharge(Rs.getString(5));
//                Order_Bean.setNumberOfItems(Rs.getInt(6));
//                Order_Bean.setInvoiceNumber(Rs.getString(8));
//                Order_Bean.setProjectDifferentPrice(Rs.getString(11));

                if(Order_Bean.getOrderSource() == OrderSource.報價單){
                    if(Order_Bean.getAlreadyOrderDate() != null && Order_Bean.getAlreadyOrderNumber() != null)
                        Order_Bean.setOrderSource(OrderSource.出貨單);
                    else if(Order_Bean.getWaitingOrderDate() != null && Order_Bean.getWaitingOrderNumber() != null)
                        Order_Bean.setOrderSource(OrderSource.待出貨單);
                }

                orderReferenceList.add(Order_Bean);
/*
                OrderReference_Bean OrderReference_Bean = new OrderReference_Bean();
                OrderReference_Bean.setIndex(index);
                OrderReference_Bean.setOrderSource(Order_Enum.OrderSource.valueOf(Rs.getString(1)));
                OrderReference_Bean.setOrderDate(Rs.getString(2));
                OrderReference_Bean.setOrderNumber(Rs.getString(3));
                OrderReference_Bean.setObjectID(Rs.getString(4));
                OrderReference_Bean.setObjectName(Rs.getString(5));
                OrderReference_Bean.setWaitingOrderDate(Rs.getString(6));
                OrderReference_Bean.setWaitingOrderNumber(Rs.getString(7));
                OrderReference_Bean.setAlreadyOrderDate(Rs.getString(8));
                OrderReference_Bean.setAlreadyOrderNumber(Rs.getString(9));
                OrderReference_Bean.setTotalPriceIncludeTax(Rs.getDouble(11));
                OrderReference_Bean.setProjectName(Rs.getString(12));
                OrderReference_Bean.setOrder_id(Rs.getInt(13));

                if(OrderReference_Bean.getOrderSource() == OrderSource.報價單){
                    if(OrderReference_Bean.getAlreadyOrderDate() != null && OrderReference_Bean.getAlreadyOrderNumber() != null)
                        OrderReference_Bean.setOrderSource(OrderSource.出貨單);
                    else if(OrderReference_Bean.getWaitingOrderDate() != null && OrderReference_Bean.getWaitingOrderNumber() != null)
                        OrderReference_Bean.setOrderSource(OrderSource.待出貨單);
                }

                OrderReference_Bean.setIsCheckout(CheckoutStatus.values()[Rs.getInt(10)].value(),OrderReference_Bean.getOrderSource());

                if(OrderReference_Bean.getOrderSource() == OrderSource.出貨子貨單)
                    OrderReference_Bean.setOrderType("出貨(子)");
                else if(OrderReference_Bean.getOrderSource() == OrderSource.出貨單)
                    OrderReference_Bean.setOrderType("出貨");
                else if(OrderReference_Bean.getOrderSource() == OrderSource.待出貨單)
                    OrderReference_Bean.setOrderType("待出貨");
                else
                    OrderReference_Bean.setOrderType("報價");

                orderReferenceList.add(OrderReference_Bean);*/
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return orderReferenceList;
    }
    private String getReferenceOrderIDQuery(HashMap<Integer,Boolean> orderReferenceMap){
        int size = 0;
        String query = "(";
        for(int order_id : orderReferenceMap.keySet()){
            size = size + 1;
            query = query + "A.id = " + order_id;
            if(size != orderReferenceMap.size())
                query = query + " or ";
        }
        return query + ") ";
    }
    public ObservableList<Order_Bean> findSimilarOrderReference(OrderSource OrderSource,int order_id, String customerCode, String ISBN, boolean isShipmentOrderPriority){
        int index = 0;
        HashMap<String,Boolean> subBillWaitingOrderNumberMap = null;
        ObservableList<Order_Bean> similarOrderReferenceList = FXCollections.observableArrayList();
        String query = "select * from (";
        if(OrderSource != Order_Enum.OrderSource.出貨子貨單){
            query = query + "select '報價單' AS '貨單',B.OrderDate,B.OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderdate,B.WaitingOrderNumber,B.AlreadyOrderDate,B.AlreadyOrderNumber,B.isCheckout,D.TotalPriceIncludeTax,E.ProjectName,B.id from Orders_Items A inner join Orders B on A.Order_id = B.id inner join Customer C on B.ObjectID = C.ObjectID inner join Orders_Price D on B.id = D.Order_id inner join Orders_ProjectInfo E on B.id = E.Order_id where A.ISBN = ? and B.isOffset = ? and B.id != '" + order_id + "' and B.status = '0' union select '出貨子貨單' AS '貨單',B.OrderDate,B.OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderdate,B.WaitingOrderNumber,B.AlreadyOrderDate,B.AlreadyOrderNumber,B.isCheckout,D.TotalPriceIncludeTax,E.ProjectName,B.id from SubBill_Items A inner join SubBill B on A.SubBill_id = B.id inner join Customer C on B.ObjectID = C.ObjectID inner join SubBill_Price D on B.id = D.SubBill_id inner join SubBill_ProjectInfo E on B.id = E.SubBill_id where A.ISBN = ? and B.isOffset = ? and B.status = '0') TABLE_ALL";
            if(isShipmentOrderPriority) query = query + " order by AlreadyOrderDate ";
            else    query = query + " order by OrderDate ";
        }else{
            query = query + "select '報價單' AS '貨單',B.OrderDate,B.OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderdate,B.WaitingOrderNumber,B.AlreadyOrderDate,B.AlreadyOrderNumber,B.isCheckout,D.TotalPriceIncludeTax,E.ProjectName,B.id from Orders_Items A inner join Orders B on A.Order_id = B.id inner join Customer C on B.ObjectID = C.ObjectID inner join Orders_Price D on B.id = D.Order_id inner join Orders_ProjectInfo E on B.id = E.Order_id where A.ISBN = ? and B.isOffset = ? and B.status = '0' union " +
                    "select '出貨子貨單' AS '貨單',B.OrderDate,B.OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderdate,B.WaitingOrderNumber,B.AlreadyOrderDate,B.AlreadyOrderNumber,B.isCheckout,D.TotalPriceIncludeTax,E.ProjectName,B.id from SubBill_Items A inner join SubBill B on A.SubBill_id = B.id inner join Customer C on B.ObjectID = C.ObjectID inner join SubBill_Price D on B.id = D.SubBill_id inner join SubBill_ProjectInfo E on B.id = E.SubBill_id where A.ISBN = ? and B.isOffset = ? and B.id != '" + order_id + "' and B.status = '0') TABLE_ALL";
            if(isShipmentOrderPriority) query = query + " order by AlreadyOrderDate ";
            else    query = query + " order by OrderDate ";
        }
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,ISBN);
            PreparedStatement.setInt(2, OffsetOrderStatus.未沖帳.ordinal());
            PreparedStatement.setString(3,ISBN);
            PreparedStatement.setInt(4, OffsetOrderStatus.未沖帳.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())  {
                index = index + 1;
                Order_Bean Order_Bean = new Order_Bean();
                Order_Bean.setOrderSource(Order_Enum.OrderSource.valueOf(Rs.getString(1)));
                Order_Bean.setOrderDate(Rs.getString(2));
                Order_Bean.setNowOrderNumber(Rs.getString(3));
                Order_Bean.setObjectID(Rs.getString(4));
                Order_Bean.setObjectName(Rs.getString(5));
                Order_Bean.setWaitingOrderDate(Rs.getString(6));
                Order_Bean.setWaitingOrderNumber(Rs.getString(7));
                Order_Bean.setAlreadyOrderDate(Rs.getString(8));
                Order_Bean.setAlreadyOrderNumber(Rs.getString(9));
                Order_Bean.setIsCheckout(CheckoutStatus.values()[Rs.getInt(10)].value());
                Order_Bean.setTotalPriceIncludeTax(Rs.getString(11));
                Order_Bean.setProjectName(Rs.getString(12));
                Order_Bean.setOrderID(Rs.getObject(13) == null ? null : Rs.getInt(13));
                Order_Bean.setProductList(getOrderItems(Order_Bean.getOrderSource(), Order_Bean.getOrderID()));

                if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.報價單){
                    if(Order_Bean.getAlreadyOrderDate() != null && Order_Bean.getAlreadyOrderNumber() != null)
                        Order_Bean.setOrderSource(Order_Enum.OrderSource.出貨單);
                    else if(Order_Bean.getWaitingOrderDate() != null && Order_Bean.getWaitingOrderNumber() != null)
                        Order_Bean.setOrderSource(Order_Enum.OrderSource.待出貨單);
                }

                if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單) {
                    if(subBillWaitingOrderNumberMap == null)    subBillWaitingOrderNumberMap = new HashMap<>();
                    if(!subBillWaitingOrderNumberMap.containsKey(Order_Bean.getNowOrderNumber().substring(0, getOrderNumberLength())))
                        subBillWaitingOrderNumberMap.put(Order_Bean.getNowOrderNumber().substring(0, getOrderNumberLength()),true);
                }
                similarOrderReferenceList.add(Order_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        if(subBillWaitingOrderNumberMap != null){
            for(String waitingOrderNumber : subBillWaitingOrderNumberMap.keySet()){
                for(Order_Bean Order_Bean : similarOrderReferenceList){
                    if((Order_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || Order_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 ||
                            Order_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單) &&
                            Order_Bean.getWaitingOrderNumber().equals(waitingOrderNumber)){
                        similarOrderReferenceList.remove(Order_Bean);
                        break;
                    }
                }
            }
        }
        return similarOrderReferenceList;
    }
    public ObservableList<TransactionDetail_Bean> getTransactionDetail(SearchTransactionDetail_Bean transactionDetail_bean){
        OrderSearchMethod OrderSearchMethod = transactionDetail_bean.getOrderSearchMethod();
        String ISBN = transactionDetail_bean.getISBN();
        boolean isGetQuotation = transactionDetail_bean.isGetQuotation();
        boolean isGetWaitingOrder = transactionDetail_bean.isGetWaitingOrder();
        String objectID = transactionDetail_bean.getObjectID();
        String startDate = transactionDetail_bean.getStartDate();
        String endDate = transactionDetail_bean.getEndDate();

        HashMap<String,Boolean> subBillWaitingOrderNumberMap = null;
        ObservableList<TransactionDetail_Bean> TransactionDetailList = FXCollections.observableArrayList();

        String Query = "select * from (";
        String Purchase = "select '進貨單' AS '貨單',B.OrderDate as OrderDate,B.OrderNumber as OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderDate,B.WaitingOrderNumber,B.AlreadyOrderDate, B.AlreadyOrderNumber,A.ProductName,A.Quantity,A.BatchPrice,B.NumberOfItems,D.TotalPriceNoneTax,B.isCheckout,B.isBorrowed,B.isOffset from Orders_Items A INNER join Orders B on A.Order_id = B.id INNER join Manufacturer C on B.ObjectID = C.ObjectID INNER join Orders_Price D on B.id = D.Order_id where A.ISBN = ? " + getSelectWhichOrderNumberQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder) + " " + getSearchObjectIDQuery_TransactionDetail(objectID) + " and B.OrderSource = ? and B.status = '0'";
        String Shipment = "select '出貨單' AS '貨單',B.OrderDate as OrderDate,B.OrderNumber as OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderDate,B.WaitingOrderNumber,B.AlreadyOrderDate, B.AlreadyOrderNumber,A.ProductName,A.Quantity,A.SinglePrice,B.NumberOfItems,D.TotalPriceNoneTax,B.isCheckout,B.isBorrowed,B.isOffset from Orders_Items A INNER join Orders B on A.Order_id = B.id INNER join Customer C on B.ObjectID = C.ObjectID inner join Orders_Price D on B.id = D.Order_id where A.ISBN = ? " + getSelectWhichOrderNumberQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder) + " " + getSearchObjectIDQuery_TransactionDetail(objectID) + " and B.OrderSource = ? and B.status = '0'";
        String PurchaseSubBill = "select '進貨子貨單' AS '貨單',B.OrderDate as OrderDate,B.OrderNumber as OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderDate,B.WaitingOrderNumber,B.AlreadyOrderDate,B.AlreadyOrderNumber,A.ProductName,A.Quantity,A.BatchPrice,B.NumberOfItems,D.TotalPriceNoneTax,B.isCheckout,B.isBorrowed,B.isOffset from SubBill_Items A INNER join SubBill B on A.SubBill_id = B.id INNER join Manufacturer C on B.ObjectID = C.ObjectID INNER join SubBill_Price D on B.id = D.SubBill_id where A.ISBN = ? " + getSelectWhichOrderNumberQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder) + " " + getSearchObjectIDQuery_TransactionDetail(objectID) + " and B.OrderSource = ? and B.status = '0'";
        String ShipmentSubBill = "select '出貨子貨單' AS '貨單',B.OrderDate as OrderDate,B.OrderNumber as OrderNumber,B.ObjectID,C.ObjectName,B.WaitingOrderDate,B.WaitingOrderNumber,B.AlreadyOrderDate,B.AlreadyOrderNumber,A.ProductName,A.Quantity,A.SinglePrice,B.NumberOfItems,D.TotalPriceNoneTax,B.isCheckout,B.isBorrowed,B.isOffset from SubBill_Items A INNER join SubBill B on A.SubBill_id = B.id INNER join Customer C on B.ObjectID = C.ObjectID INNER join SubBill_Price D on B.id = D.SubBill_id where A.ISBN = ? " + getSelectWhichOrderNumberQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder) + " " + getSearchObjectIDQuery_TransactionDetail(objectID) + " and B.OrderSource = ? and B.status = '0'";
        String PurchaseReturn = "select '進貨退貨單' AS '貨單',B.OrderDate,B.OrderNumber,B.ObjectID,C.ObjectName,'','',B.OrderDate as OrderDate,B.OrderNumber as AlreadyOrderNumber,A.ProductName,A.Quantity,A.BatchPrice,B.NumberOfItems,D.TotalPriceNoneTax,B.isCheckout,'','' as OrderSource from ReturnOrder_Items A INNER join ReturnOrder B on A.ReturnOrder_id = B.id INNER join Manufacturer C on B.ObjectID = C.ObjectID INNER join ReturnOrder_Price D on B.id = D.ReturnOrder_id where A.ISBN = ? " + getSearchObjectIDQuery_TransactionDetail(objectID) + " and B.OrderSource = ? and B.status = '0'";
        String ShipmentReturn = "select '出貨退貨單' AS '貨單',B.OrderDate,B.OrderNumber,B.ObjectID,C.ObjectName,'','',B.OrderDate as OrderDate,B.OrderNumber as AlreadyOrderNumber,A.ProductName,A.Quantity,A.SinglePrice,B.NumberOfItems,D.TotalPriceNoneTax,B.isCheckout,'','' as OrderSource from ReturnOrder_Items A INNER join ReturnOrder B on A.ReturnOrder_id = B.id INNER join Customer C on B.ObjectID = C.ObjectID INNER join ReturnOrder_Price D on B.id = D.ReturnOrder_id where A.ISBN = ? " + getSearchObjectIDQuery_TransactionDetail(objectID) + " and B.OrderSource = ? and B.status = '0'";
        if(OrderSearchMethod == Order_Enum.OrderSearchMethod.日期搜尋){
            Purchase = Purchase + getSelectWhichOrderDateQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder, startDate, endDate);
            Shipment = Shipment + getSelectWhichOrderDateQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder, startDate, endDate);
            PurchaseSubBill = PurchaseSubBill + getSelectWhichOrderDateQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder, startDate, endDate);
            ShipmentSubBill = ShipmentSubBill + getSelectWhichOrderDateQuery_TransactionDetail(isGetQuotation, isGetWaitingOrder, startDate, endDate);
            PurchaseReturn = PurchaseReturn + " and B.OrderDate between '" + startDate + "' and '" + endDate + "'";
            ShipmentReturn = ShipmentReturn + " and B.OrderDate between '" + startDate + "' and '" + endDate + "'";
        }

        Query = Query.concat(Purchase).concat(" union ").concat(Shipment).concat(" union ").concat(PurchaseSubBill).concat(" union ").concat(ShipmentSubBill).concat(" union ").concat(PurchaseReturn).concat(" union ").concat(ShipmentReturn).concat(") TABLE_ALL order by AlreadyOrderNumber");

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ISBN);
            PreparedStatement.setInt(2,OrderSource.進貨單.getOrderObject().ordinal());
            PreparedStatement.setString(3,ISBN);
            PreparedStatement.setInt(4,OrderSource.出貨單.getOrderObject().ordinal());
            PreparedStatement.setString(5,ISBN);
            PreparedStatement.setInt(6,OrderSource.進貨子貨單.getOrderObject().ordinal());
            PreparedStatement.setString(7,ISBN);
            PreparedStatement.setInt(8,OrderSource.出貨子貨單.getOrderObject().ordinal());
            PreparedStatement.setString(9,ISBN);
            PreparedStatement.setInt(10,OrderSource.進貨退貨單.getOrderObject().ordinal());
            PreparedStatement.setString(11,ISBN);
            PreparedStatement.setInt(12,OrderSource.出貨退貨單.getOrderObject().ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())  {
                TransactionDetail_Bean TransactionDetail_Bean = new TransactionDetail_Bean();
                TransactionDetail_Bean.setOrderSource(Rs.getString(1));
                TransactionDetail_Bean.setObjectID(Rs.getString(4));
                TransactionDetail_Bean.setObjectName(Rs.getString(5));
                TransactionDetail_Bean.setProductName(Rs.getString(10));
                TransactionDetail_Bean.setQuantity(Rs.getInt(11));
                TransactionDetail_Bean.setItemPriceColumn(Rs.getDouble(12));
                TransactionDetail_Bean.setNumberOfItems(Rs.getInt(13));
                TransactionDetail_Bean.setTotalPriceNoneTax(Rs.getDouble(14));
                TransactionDetail_Bean.setIsBorrowed(OrderBorrowed.values()[Rs.getInt(16)].value());
                TransactionDetail_Bean.setOffsetOrderStatus(OffsetOrderStatus.values()[Rs.getInt(17)]);

                if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨單 || TransactionDetail_Bean.getOrderSource() == OrderSource.出貨單  ||
                        TransactionDetail_Bean.getOrderSource() == OrderSource.進貨子貨單 || TransactionDetail_Bean.getOrderSource() == OrderSource.出貨子貨單){
                    TransactionDetail_Bean.setOrderDate(Rs.getString(2));
                    TransactionDetail_Bean.setOrderNumber(Rs.getString(3));
                    TransactionDetail_Bean.setWaitingOrderDate(Rs.getString(6));
                    TransactionDetail_Bean.setWaitingOrderNumber(Rs.getString(7));
                    TransactionDetail_Bean.setAlreadyOrderDate(Rs.getString(8));
                    TransactionDetail_Bean.setAlreadyOrderNumber(Rs.getString(9));
                    if(TransactionDetail_Bean.getOrderSource() != OrderSource.進貨子貨單 && TransactionDetail_Bean.getOrderSource() != OrderSource.出貨子貨單 && TransactionDetail_Bean.getAlreadyOrderNumber() == null){
                        if(TransactionDetail_Bean.getWaitingOrderNumber() != null && TransactionDetail_Bean.getOrderSource() == OrderSource.進貨單)
                            TransactionDetail_Bean.setOrderSource("待入倉單");
                        else if(TransactionDetail_Bean.getWaitingOrderNumber() != null && TransactionDetail_Bean.getOrderSource() == OrderSource.出貨單)
                            TransactionDetail_Bean.setOrderSource("待出貨單");
                        else if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨單)
                            TransactionDetail_Bean.setOrderSource("採購單");
                        else
                            TransactionDetail_Bean.setOrderSource("報價單");
                    }
                }else if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨退貨單 || TransactionDetail_Bean.getOrderSource() == OrderSource.出貨退貨單){
                    TransactionDetail_Bean.setReturnOrderDate(Rs.getString(2));
                    TransactionDetail_Bean.setReturnOrderNumber(Rs.getString(3));
                }

                TransactionDetail_Bean.setIsCheckout(CheckoutStatus.values()[Rs.getInt(15)].value(),TransactionDetail_Bean.getOrderSource());
                if(TransactionDetail_Bean.getOrderSource() == OrderSource.採購單 || TransactionDetail_Bean.getOrderSource() == OrderSource.待入倉單){
                    TransactionDetail_Bean.setOrderType("採購");
                }else if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨單) {
                    if(TransactionDetail_Bean.isBorrowed().value())    TransactionDetail_Bean.setOrderType("進貨(借測)");
                    else if(TransactionDetail_Bean.isOffset())  TransactionDetail_Bean.setOrderType("進貨(" + TransactionDetail_Bean.getOffsetOrderStatus().getDescribe() + ")");
                    else    TransactionDetail_Bean.setOrderType("進貨");
                }else if(TransactionDetail_Bean.getOrderSource() == OrderSource.報價單 || TransactionDetail_Bean.getOrderSource() == OrderSource.待出貨單){
                    TransactionDetail_Bean.setOrderType("報價");
                }else if(TransactionDetail_Bean.getOrderSource() == OrderSource.出貨單) {
                    if(TransactionDetail_Bean.isBorrowed().value())    TransactionDetail_Bean.setOrderType("出貨(借測)");
                    else if(TransactionDetail_Bean.isOffset())   TransactionDetail_Bean.setOrderType("出貨(" + TransactionDetail_Bean.getOffsetOrderStatus().getDescribe() + ")");
                    else    TransactionDetail_Bean.setOrderType("出貨");
                }else if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨子貨單 || TransactionDetail_Bean.getOrderSource() == OrderSource.出貨子貨單) {
                    if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨子貨單)
                        TransactionDetail_Bean.setOrderType("子進貨");
                    else if(TransactionDetail_Bean.getOrderSource() == OrderSource.出貨子貨單)
                        TransactionDetail_Bean.setOrderType("子出貨");
                    if(TransactionDetail_Bean.getOffsetOrderStatus() != OffsetOrderStatus.部分沖帳){
                        if(subBillWaitingOrderNumberMap == null)    subBillWaitingOrderNumberMap = new HashMap<>();
                        String waitingOrderNumber = TransactionDetail_Bean.getOrderNumber().substring(0, getOrderNumberLength());
                        if(!subBillWaitingOrderNumberMap.containsKey(waitingOrderNumber))
                            subBillWaitingOrderNumberMap.put(waitingOrderNumber, true);
                    }
                    if(TransactionDetail_Bean.isOffset()){
                        TransactionDetail_Bean.setOrderType(TransactionDetail_Bean.getOrderType() + "(" + TransactionDetail_Bean.getOffsetOrderStatus().getDescribe() + ")");
                    }
                }else if(TransactionDetail_Bean.getOrderSource() == OrderSource.出貨退貨單)
                    TransactionDetail_Bean.setOrderType("出退貨");
                else if(TransactionDetail_Bean.getOrderSource() == OrderSource.進貨退貨單)
                    TransactionDetail_Bean.setOrderType("進退貨");
                TransactionDetailList.add(TransactionDetail_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        if(subBillWaitingOrderNumberMap != null){
            for(String waitingOrderNumber : subBillWaitingOrderNumberMap.keySet()){
                for(TransactionDetail_Bean TransactionDetail_Bean : TransactionDetailList){
                    if((TransactionDetail_Bean.getOrderSource() == OrderSource.待入倉單 || TransactionDetail_Bean.getOrderSource() == OrderSource.進貨單 ||
                            TransactionDetail_Bean.getOrderSource() == OrderSource.待出貨單 || TransactionDetail_Bean.getOrderSource() == OrderSource.出貨單) &&
                            TransactionDetail_Bean.getWaitingOrderNumber().equals(waitingOrderNumber)){
                        TransactionDetailList.remove(TransactionDetail_Bean);
                        break;
                    }
                }
            }
        }
        for(int index = 0 ; index < TransactionDetailList.size() ; index++){
            TransactionDetail_Bean TransactionDetail_Bean = TransactionDetailList.get(index);
            TransactionDetail_Bean.setIndex(index+1);
        }
        return TransactionDetailList;
    }
    private String getSearchObjectIDQuery_TransactionDetail(String objectID){
        return objectID == null ? "" : "and C.ObjectID = '" + objectID + "'";
    }
    private String getSelectWhichOrderNumberQuery_TransactionDetail(boolean isGetQuotation, boolean isGetWaitingOrder){
        if(isGetQuotation)  return "";
        else if(isGetWaitingOrder)  return "and B.WaitingOrderNumber is not null";
        else return "and B.AlreadyOrderNumber is not null";
    }
    private String getSelectWhichOrderDateQuery_TransactionDetail(boolean isGetQuotation, boolean isGetWaitingOrder, String startDate, String endDate){
        if(isGetQuotation)  return " and B.OrderDate between '" + startDate + "' and '" + endDate + "'";
        else if(isGetWaitingOrder)  return " and B.WaitingOrderDate between '" + startDate + "' and '" + endDate + "'";
        else    return " and B.AlreadyOrderDate between '" + startDate + "' and '" + endDate + "'";
    }
    /** Calculate total price which none tax in order
     * @param ProductList the list of product
     * */
    public String calculateOrderTotalPrice_NoneTax(boolean isTwoFormat, ObservableList<OrderProduct_Bean> ProductList){
        double totalPrice = 0;
        for(OrderProduct_Bean productInfo_bean : ProductList){
            if(isTwoFormat){
                int quantity = productInfo_bean.getQuantity();
                totalPrice = totalPrice + ToolKit.RoundingInteger(quantity*ToolKit.RoundingDouble(0,productInfo_bean.getSinglePrice()*1.05));
            }else{
                totalPrice = totalPrice + productInfo_bean.getPriceAmount();
            }
        }
        return ToolKit.RoundingString(totalPrice);
    }
    /** Calculate total price whether include tax in order
     * @param OrderTaxStatus the status of order that whether include tax
     * @param TotalPriceNoneTax the total price that none tax
     * */
    public int calculateOrderTotalPrice_IncludeTax(OrderTaxStatus OrderTaxStatus, int TotalPriceNoneTax){
        return (int)((OrderTaxStatus.ordinal() % 2 == 1 ? TotalPriceNoneTax * 1.05 : TotalPriceNoneTax)+0.5);
    }
    /** Calculate profit of order
     * @param OrderTaxStatus 貨單金額是否含稅
     * @param ProductList the product list of order
     * @param TotalPriceIncludeTax the total price that none tax
     * */
    public String calculateProfit(OrderTaxStatus OrderTaxStatus, ObservableList<OrderProduct_Bean> ProductList, String TotalPriceIncludeTax){
        double TotalBatchPrice = 0;
        for(OrderProduct_Bean ProductInfoBean : ProductList){
            TotalBatchPrice = TotalBatchPrice + (ProductInfoBean.getQuantity()*ProductInfoBean.getBatchPrice());
        }
        TotalBatchPrice = (OrderTaxStatus.ordinal() % 2 == 1 ? TotalBatchPrice * 1.05 : TotalBatchPrice);
        return ToolKit.RoundingString(Double.parseDouble(TotalPriceIncludeTax) - TotalBatchPrice);
    }
    public String calculateProductGroupTotalPrice_NoneTax(boolean isTwoFormat, HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap){
        double totalPrice = 0;
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean productGroup_bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            if(isTwoFormat){
                int quantity = (productGroup_bean.getSmallQuantity() != null && productGroup_bean.getSmallQuantity() != 0) ?
                        productGroup_bean.getSmallQuantity() : productGroup_bean.getQuantity();
                double singlePrice = (productGroup_bean.getSmallQuantity() != null && productGroup_bean.getSmallQuantity() != 0) ?
                        productGroup_bean.getSmallSinglePrice() : productGroup_bean.getSinglePrice();
                totalPrice = totalPrice + ToolKit.RoundingInteger(quantity*ToolKit.RoundingDouble(0,singlePrice*1.05));
            }else{
                boolean isExistSmallQuantity = productGroup_bean.getSmallQuantity() != null && productGroup_bean.getSmallQuantity() != 0;
                totalPrice = totalPrice + (isExistSmallQuantity ? productGroup_bean.getSmallPriceAmount() : productGroup_bean.getPriceAmount());
            }
        }
        return ToolKit.RoundingString(totalPrice);
    }
}
