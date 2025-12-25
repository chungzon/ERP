package ERP.Model.Order.SearchNonePayReceive;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.SearchNonePayReceive.*;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
/** [ERP.Model] Search none payable or receivable */
public class SearchNonePayReceive_Model {

    private ToolKit ToolKit;
    private Order_Model order_model;
    public SearchNonePayReceive_Model(){
        ToolKit = ERPApplication.ToolKit;
        order_model = ToolKit.ModelToolKit.getOrderModel();
    }
    /** Get all object info
     * @param OrderObject the object of order
     * */
    public ObservableList<ObjectInfo_Bean> getAllObjectInfo(OrderObject OrderObject){
        ObservableList<ObjectInfo_Bean> ObjectInfoList = FXCollections.observableArrayList();
        ObjectInfoList.add(new ObjectInfo_Bean());

        String Query = "select * from " + OrderObject.getTableName() + " order by ObjectID";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
                ObjectInfo_Bean.setObjectID(Rs.getString("ObjectID"));
                ObjectInfo_Bean.setObjectNickName(Rs.getString("ObjectNickName"));
                ObjectInfoList.add(ObjectInfo_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ObjectInfoList;
    }
    /** Get none payable or receivable order
     * @param orderObject the object of order
     * @param conditionalPayReceiveSearch_bean the bean of conditional search
     * */
    public ObservableList<SearchNonePayReceive_Bean> getNonePayableReceivableOrderInfo(OrderObject orderObject, ConditionalPayReceiveSearch_Bean conditionalPayReceiveSearch_bean){
        HashMap<String, ObservableList<SearchOrder_Bean>> orderInfoMap = new HashMap<>();
        if(orderObject == Order_Enum.OrderObject.廠商) {
            getPayableReceivableDetails(orderObject, OrderSource.待入倉單, orderInfoMap, conditionalPayReceiveSearch_bean);
            getPayableReceivableDetails(orderObject, OrderSource.進貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
            getPayableReceivableDetails(orderObject, OrderSource.進貨子貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
            getPayableReceivableDetails(orderObject, OrderSource.進貨退貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
        }else if(orderObject == Order_Enum.OrderObject.客戶) {
            getPayableReceivableDetails(orderObject, OrderSource.待出貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
            getPayableReceivableDetails(orderObject, OrderSource.出貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
            getPayableReceivableDetails(orderObject, OrderSource.出貨子貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
            getPayableReceivableDetails(orderObject, OrderSource.出貨退貨單, orderInfoMap, conditionalPayReceiveSearch_bean);
        }
        return getNonePayableReceivable(orderInfoMap, conditionalPayReceiveSearch_bean);
    }
    private void getPayableReceivableDetails(OrderObject orderObject, OrderSource orderSource, HashMap<String, ObservableList<SearchOrder_Bean>> orderInfoMap, ConditionalPayReceiveSearch_Bean conditionalPayReceiveSearch_bean){
        String query = "";
        if(orderSource == Order_Enum.OrderSource.待入倉單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.WaitingOrderDate,A.WaitingOrderNumber,A.OrderDate,A.OrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.WaitingOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ? and A.AlreadyOrderDate is null and A.AlreadyOrderNumber is null" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.WaitingOrderDate,A.WaitingOrderNumber,A.OrderDate,A.OrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.WaitingOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ? and A.AlreadyOrderDate is null and A.AlreadyOrderNumber is null" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.進貨單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.進貨子貨單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from SubBill A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join SubBill_Price C on A.id = C.SubBill_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from SubBill A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join SubBill_Price C on A.id = C.SubBill_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.進貨退貨單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.OrderDate,A.OrderNumber,'','','','',C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from ReturnOrder A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join ReturnOrder_Price C on A.id = C.ReturnOrder_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.OrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.PayableDiscount,A.OrderDate,A.OrderNumber,'','','','',C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from ReturnOrder A inner join Manufacturer B on A.ObjectID = B.ObjectID inner join ReturnOrder_Price C on A.id = C.ReturnOrder_id inner join Manufacturer_PayInfo D on B.id = D.Manufacturer_id where D.IsCheckoutByMonth = ? and A.OrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.待出貨單){
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.WaitingOrderDate,A.WaitingOrderNumber,A.OrderDate,A.OrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.WaitingOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ? and A.AlreadyOrderDate is null and A.AlreadyOrderNumber is null" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.WaitingOrderDate,A.WaitingOrderNumber,A.OrderDate,A.OrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.WaitingOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ? and A.AlreadyOrderDate is null and A.AlreadyOrderNumber is null" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.出貨單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.AlreadyOrderDate,A.AlreadyOrderNumber,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_Price C on A.id = C.Order_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.出貨子貨單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from SubBill A inner join Customer B on A.ObjectID = B.ObjectID inner join SubBill_Price C on A.id = C.SubBill_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.OrderDate,A.OrderNumber,A.WaitingOrderDate,A.WaitingOrderNumber,A.AlreadyOrderDate,A.AlreadyOrderNumber,C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from SubBill A inner join Customer B on A.ObjectID = B.ObjectID inner join SubBill_Price C on A.id = C.SubBill_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.AlreadyOrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }else if(orderSource == Order_Enum.OrderSource.出貨退貨單) {
            query = "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.OrderDate,A.OrderNumber,'','','','',C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from ReturnOrder A inner join Customer B on A.ObjectID = B.ObjectID inner join ReturnOrder_Price C on A.id = C.ReturnOrder_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.OrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean) + " union " +
                    "select A.id,A.ObjectID,B.ObjectNickName,B.ObjectName,B.ReceivableDiscount,A.OrderDate,A.OrderNumber,'','','','',C.TotalPriceNoneTax,C.Tax,C.Discount,C.TotalPriceIncludeTax from ReturnOrder A inner join Customer B on A.ObjectID = B.ObjectID inner join ReturnOrder_Price C on A.id = C.ReturnOrder_id inner join Customer_ReceiveInfo D on B.id = D.Customer_id where D.IsCheckoutByMonth = ? and A.OrderDate between ? and ? and A.isCheckout = ? and A.status = '0' and OrderSource = ?" + getSearchNonePayReceiveQuery(orderObject, conditionalPayReceiveSearch_bean);
        }
//        ERPApplication.Logger.info(Query);
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            int queryIndex = 1;
            String startDate, endDate;
            for(ObjectInfo_Enum.CheckoutByMonth checkoutByMonth : ObjectInfo_Enum.CheckoutByMonth.values()){
                if(checkoutByMonth == ObjectInfo_Enum.CheckoutByMonth.月結){
                    startDate = conditionalPayReceiveSearch_bean.getCheckoutByMonthStartDate();
                    endDate = conditionalPayReceiveSearch_bean.getCheckoutByMonthEndDate();
                }else{
                    startDate = conditionalPayReceiveSearch_bean.getNoneCheckoutByMonthStartDate();
                    endDate = conditionalPayReceiveSearch_bean.getNoneCheckoutByMonthEndDate();
                }
                preparedStatement.setInt(queryIndex, checkoutByMonth.ordinal());
                queryIndex = queryIndex + 1;
                preparedStatement.setString(queryIndex, startDate);
                queryIndex = queryIndex + 1;
                preparedStatement.setString(queryIndex, endDate);
                queryIndex = queryIndex + 1;
                preparedStatement.setInt(queryIndex,Order_Enum.CheckoutStatus.未結帳.ordinal());
                queryIndex = queryIndex + 1;
                preparedStatement.setInt(queryIndex,orderSource.getOrderObject().ordinal());
                queryIndex = queryIndex + 1;
            }
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SearchOrder_Bean searchOrder_bean = new SearchOrder_Bean();
                searchOrder_bean.setOrderSource(orderSource);
                searchOrder_bean.setId(rs.getInt(1));
                searchOrder_bean.setObjectID(rs.getString(2));
                searchOrder_bean.setObjectNickName(rs.getString(3));
                searchOrder_bean.setObjectName(rs.getString(4));
                searchOrder_bean.setPayableReceivableDiscount(rs.getFloat(5));
                searchOrder_bean.setOrderDate(rs.getString(6));
                searchOrder_bean.setOrderNumber(rs.getString(7));
                if(orderSource == Order_Enum.OrderSource.待入倉單 || orderSource == Order_Enum.OrderSource.待出貨單 ||
                        orderSource == Order_Enum.OrderSource.進貨單 || orderSource == Order_Enum.OrderSource.出貨單){
                    searchOrder_bean.setQuotationDate(rs.getString(8));
                    searchOrder_bean.setQuotationNumber(rs.getString(9));
                    if(orderSource == Order_Enum.OrderSource.待入倉單 || orderSource == Order_Enum.OrderSource.待出貨單){
                        searchOrder_bean.setWaitingOrderDate(rs.getString(6));
                        searchOrder_bean.setWaitingOrderNumber(rs.getString(7));
                        searchOrder_bean.setAlreadyOrderDate(rs.getString(10));
                        searchOrder_bean.setAlreadyOrderNumber(rs.getString(11));
                    }else{
                        searchOrder_bean.setWaitingOrderDate(rs.getString(10));
                        searchOrder_bean.setWaitingOrderNumber(rs.getString(11));
                        searchOrder_bean.setAlreadyOrderDate(rs.getString(6));
                        searchOrder_bean.setAlreadyOrderNumber(rs.getString(7));
                    }
                }else if(orderSource == Order_Enum.OrderSource.進貨子貨單 || orderSource == Order_Enum.OrderSource.出貨子貨單){
                    searchOrder_bean.setWaitingOrderDate(rs.getString(8));
                    searchOrder_bean.setWaitingOrderNumber(rs.getString(9));
                    searchOrder_bean.setAlreadyOrderDate(rs.getString(10));
                    searchOrder_bean.setAlreadyOrderNumber(rs.getString(11));
                }
                searchOrder_bean.setTotalPriceNoneTax(rs.getInt(12));
                searchOrder_bean.setTax(rs.getInt(13));
                searchOrder_bean.setDiscount(rs.getInt(14));
                searchOrder_bean.setTotalPriceIncludeTax(rs.getInt(15));
                searchOrder_bean.setIsCheckout(CheckoutStatus.未結帳.value());
                /*
                母貨單：if 有建立分期收/付款、則優先權最高  else  只搜尋沒有建立子貨單的母貨單
                子貨單：if 母貨單有建立分期收/付款、則不搜尋 else 搜尋
                **/
                boolean isMainOrderExistInstallment = false;
                if(orderSource == OrderSource.待入倉單 || orderSource == OrderSource.待出貨單 ||
                        orderSource == OrderSource.進貨單 || orderSource == OrderSource.出貨單){
                    isMainOrderExistInstallment = order_model.findNoneCheckoutInstallment(searchOrder_bean);
                    if(!isMainOrderExistInstallment){
                        if(order_model.isMainOrderExistSubBill(orderObject, searchOrder_bean.getWaitingOrderNumber(),null,null)){
                            continue;
                        }
                    }
                }else if(orderSource == OrderSource.進貨子貨單 || orderSource == OrderSource.出貨子貨單){
                    if(order_model.isMainOrderExistInstallment(orderObject, searchOrder_bean.getWaitingOrderNumber())){
                        continue;
                    }
                }
                if((orderSource == OrderSource.待入倉單 || orderSource == OrderSource.待出貨單) && !isMainOrderExistInstallment){
                    continue;
                }
                if(!orderInfoMap.containsKey(searchOrder_bean.getObjectID())){
                    ObservableList<SearchOrder_Bean> PayableReceivableDetailsFormList = FXCollections.observableArrayList();
                    PayableReceivableDetailsFormList.add(searchOrder_bean);
                    orderInfoMap.put(searchOrder_bean.getObjectID(), PayableReceivableDetailsFormList);
                }else   orderInfoMap.get(searchOrder_bean.getObjectID()).add(searchOrder_bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
    }
    private String getSearchNonePayReceiveQuery(OrderObject OrderObject, ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean){
        String query = "";
        if(!ConditionalPayReceiveSearch_Bean.getObjectName().equals("")) {
            if(OrderObject == Order_Enum.OrderObject.廠商)    query = query + " and B.ObjectName like '%" + ConditionalPayReceiveSearch_Bean.getObjectName() + "%'";
            else if(OrderObject == Order_Enum.OrderObject.客戶)   query = query + " and B.ObjectName like '%" + ConditionalPayReceiveSearch_Bean.getObjectName() + "%'";
        }else if(ConditionalPayReceiveSearch_Bean.getStartObjectID() != null && ConditionalPayReceiveSearch_Bean.getEndObjectID() != null)
            query = query + " and A.ObjectID between '" + ConditionalPayReceiveSearch_Bean.getStartObjectID() + "' and '" + ConditionalPayReceiveSearch_Bean.getEndObjectID() + "'";
        return query;
    }
    private ObservableList<SearchNonePayReceive_Bean> getNonePayableReceivable(HashMap<String, ObservableList<SearchOrder_Bean>> orderInfoMap, ConditionalPayReceiveSearch_Bean conditionalPayReceiveSearch_bean){
        OrderObject orderObject = null;
        ObservableList<SearchNonePayReceive_Bean> nonePayReceiveList = FXCollections.observableArrayList();
        for(String ObjectID : orderInfoMap.keySet()){
            ObservableList<SearchOrder_Bean> orderList = orderInfoMap.get(ObjectID);
            SearchNonePayReceive_Bean searchNonePayReceive_bean = new SearchNonePayReceive_Bean();
            searchNonePayReceive_bean.setCheckBox(new CheckBox(),true);
            searchNonePayReceive_bean.setObjectID(ObjectID);
            for (SearchOrder_Bean searchOrder_bean : orderList) {
                orderObject = searchOrder_bean.getOrderSource().getOrderObject();
                searchNonePayReceive_bean.setObjectName(searchOrder_bean.getObjectNickName());
                if (searchOrder_bean.getOrderSource() == OrderSource.待入倉單 || searchOrder_bean.getOrderSource() == OrderSource.進貨子貨單 || searchOrder_bean.getOrderSource() == OrderSource.進貨單 ||
                        searchOrder_bean.getOrderSource() == OrderSource.待出貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨子貨單) {
                    searchNonePayReceive_bean.setOrderQuantity(searchNonePayReceive_bean.getOrderQuantity() + 1);
                    searchNonePayReceive_bean.setOrderPriceAmount(searchNonePayReceive_bean.getOrderPriceAmount() + searchOrder_bean.getTotalPriceNoneTax());
                    searchNonePayReceive_bean.setTax(searchNonePayReceive_bean.getTax() + searchOrder_bean.getTax());
                    searchNonePayReceive_bean.setDiscount(searchNonePayReceive_bean.getDiscount() + searchOrder_bean.getDiscount());
                    searchNonePayReceive_bean.setPayReceivePrice(searchNonePayReceive_bean.getPayReceivePrice() + searchOrder_bean.getTotalPriceNoneTax());
                    searchNonePayReceive_bean.setActualPayReceivePrice(searchNonePayReceive_bean.getActualPayReceivePrice() + searchOrder_bean.getTotalPriceIncludeTax());
                } else if (searchOrder_bean.getOrderSource() == OrderSource.進貨退貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨退貨單) {
                    searchNonePayReceive_bean.setReturnOrderQuantity(searchNonePayReceive_bean.getReturnOrderQuantity() + 1);
                    searchNonePayReceive_bean.setReturnOrderPriceAmount(searchNonePayReceive_bean.getReturnOrderPriceAmount() + searchOrder_bean.getTotalPriceNoneTax());
                    searchNonePayReceive_bean.setTax(searchNonePayReceive_bean.getTax() - searchOrder_bean.getTax());
                    searchNonePayReceive_bean.setDiscount(searchNonePayReceive_bean.getDiscount() - searchOrder_bean.getDiscount());
                    searchNonePayReceive_bean.setPayReceivePrice(searchNonePayReceive_bean.getPayReceivePrice() - searchOrder_bean.getTotalPriceNoneTax());
                    searchNonePayReceive_bean.setActualPayReceivePrice(searchNonePayReceive_bean.getActualPayReceivePrice() - searchOrder_bean.getTotalPriceIncludeTax());
                }
                searchNonePayReceive_bean.setOrderList(orderList);
            }
            if(orderObject != null)
                searchNonePayReceive_bean.setNonePayReceiveHistoryDate(findNonePayReceiveHistoryAlreadyDate(ObjectID, orderObject, conditionalPayReceiveSearch_bean));
            nonePayReceiveList.add(searchNonePayReceive_bean);
        }
        ToolKit.sortNonePayReceiveOrderByObjectID(nonePayReceiveList);
        return nonePayReceiveList;
    }
    private String findNonePayReceiveHistoryAlreadyDate(String objectID, OrderObject orderObject, ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean){
        String nonePayReceiveHistoryDate = null;
        String query =
                "select TOP 1 AlreadyOrderNumber from (";
        if(orderObject == OrderObject.廠商){
            query = query +
                "select AlreadyOrderDate as AlreadyOrderNumber from Orders A INNER JOIN Manufacturer B ON A.ObjectID = B.ObjectID INNER JOIN Manufacturer_PayInfo C ON B.id = C.Manufacturer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +
                " union " +
                "select AlreadyOrderDate as AlreadyOrderNumber from Orders A INNER JOIN Manufacturer B ON A.ObjectID = B.ObjectID INNER JOIN Manufacturer_PayInfo C ON B.id = C.Manufacturer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +

                " union " +

                "select AlreadyOrderDate as AlreadyOrderNumber from SubBill A INNER JOIN Manufacturer B ON A.ObjectID = B.ObjectID INNER JOIN Manufacturer_PayInfo C ON B.id = C.Manufacturer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and  A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +
                " union " +
                "select AlreadyOrderDate as AlreadyOrderNumber from SubBill A INNER JOIN Manufacturer B ON A.ObjectID = B.ObjectID INNER JOIN Manufacturer_PayInfo C ON B.id = C.Manufacturer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and  A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +

                " union " +

                "select OrderDate as AlreadyOrderNumber from ReturnOrder A INNER JOIN Manufacturer B ON A.ObjectID = B.ObjectID INNER JOIN Manufacturer_PayInfo C ON B.id = C.Manufacturer_id " +
                "WHERE C.IsCheckoutByMonth = ? and OrderSource = ? and A.ObjectID = ? and A.OrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +
                " union " +
                "select OrderDate as AlreadyOrderNumber from ReturnOrder A INNER JOIN Manufacturer B ON A.ObjectID = B.ObjectID INNER JOIN Manufacturer_PayInfo C ON B.id = C.Manufacturer_id " +
                "WHERE C.IsCheckoutByMonth = ? and OrderSource = ? and A.ObjectID = ? and A.OrderDate < ? AND A.isCheckout = ? AND A.Status = ?";
        }else{
            query = query +
                "select AlreadyOrderDate as AlreadyOrderNumber from Orders A INNER JOIN Customer B ON A.ObjectID = B.ObjectID INNER JOIN Customer_ReceiveInfo C ON B.id = C.Customer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +
                " union " +
                "select AlreadyOrderDate as AlreadyOrderNumber from Orders A INNER JOIN Customer B ON A.ObjectID = B.ObjectID INNER JOIN Customer_ReceiveInfo C ON B.id = C.Customer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +

                " union " +

                "select AlreadyOrderDate as AlreadyOrderNumber from SubBill A INNER JOIN Customer B ON A.ObjectID = B.ObjectID INNER JOIN Customer_ReceiveInfo C ON B.id = C.Customer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and  A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +
                " union " +
                "select AlreadyOrderDate as AlreadyOrderNumber from SubBill A INNER JOIN Customer B ON A.ObjectID = B.ObjectID INNER JOIN Customer_ReceiveInfo C ON B.id = C.Customer_id " +
                "WHERE C.IsCheckoutByMonth = ? and A.OrderSource = ? and  A.ObjectID = ? and A.AlreadyOrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +

                " union " +

                "select OrderDate as AlreadyOrderNumber from ReturnOrder A INNER JOIN Customer B ON A.ObjectID = B.ObjectID INNER JOIN Customer_ReceiveInfo C ON B.id = C.Customer_id " +
                "WHERE C.IsCheckoutByMonth = ? and OrderSource = ? and A.ObjectID = ? and A.OrderDate < ? AND A.isCheckout = ? AND A.Status = ?" +
                " union " +
                "select OrderDate as AlreadyOrderNumber from ReturnOrder A INNER JOIN Customer B ON A.ObjectID = B.ObjectID INNER JOIN Customer_ReceiveInfo C ON B.id = C.Customer_id " +
                "WHERE C.IsCheckoutByMonth = ? and OrderSource = ? and A.ObjectID = ? and A.OrderDate < ? AND A.isCheckout = ? AND A.Status = ?";
        }

        query = query + ") TABLE_ALL order by AlreadyOrderNumber";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            int queryIndex = 1;
            for(int index = 0; index < 3; index++){
                for(ObjectInfo_Enum.CheckoutByMonth checkoutByMonth : ObjectInfo_Enum.CheckoutByMonth.values()){
                    String startDate;
                    if(checkoutByMonth == ObjectInfo_Enum.CheckoutByMonth.月結){
                        startDate = ConditionalPayReceiveSearch_Bean.getCheckoutByMonthStartDate();
                    }else{
                        startDate = ConditionalPayReceiveSearch_Bean.getNoneCheckoutByMonthStartDate();
                    }
                    PreparedStatement.setInt(queryIndex, checkoutByMonth.ordinal());
                    queryIndex = queryIndex + 1;
                    PreparedStatement.setInt(queryIndex, orderObject.ordinal());
                    queryIndex = queryIndex + 1;
                    PreparedStatement.setString(queryIndex, objectID);
                    queryIndex = queryIndex + 1;
                    PreparedStatement.setString(queryIndex, startDate);
                    queryIndex = queryIndex + 1;
                    PreparedStatement.setInt(queryIndex, Order_Enum.CheckoutStatus.未結帳.ordinal());
                    queryIndex = queryIndex + 1;
                    PreparedStatement.setInt(queryIndex, Order_Enum.OrderStatus.有效貨單.ordinal());
                    queryIndex = queryIndex + 1;
                }
            }
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                nonePayReceiveHistoryDate = Rs.getString(1);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return nonePayReceiveHistoryDate;
    }
    public HashMap<LocalDate,Integer> getAlreadyPayReceiveCheckInfo(OrderObject OrderObject){
        HashMap<LocalDate,Integer> checkDueDateAndCheckPriceMap = null;
        String query = "select A.OrderNumber,A.OrderDate,A.ObjectID,A.CheckDueDate,B.CheckPrice from PayableReceivable A inner join PayableReceivable_Price B on A.id = B.PayableReceivable_id where A.CheckDueDate >= ? and A.OrderObject = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,ToolKit.getThisMonthSpecifyDay(25));
            PreparedStatement.setInt(2,OrderObject.ordinal());
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(checkDueDateAndCheckPriceMap == null) checkDueDateAndCheckPriceMap = new HashMap<>();
                LocalDate checkDueDate = Rs.getDate(4) == null ? null : Rs.getDate(4).toLocalDate();
                int checkPrice = Rs.getInt(5);
                if(!checkDueDateAndCheckPriceMap.containsKey(checkDueDate))
                    checkDueDateAndCheckPriceMap.put(checkDueDate,checkPrice);
                else
                    checkDueDateAndCheckPriceMap.put(checkDueDate,checkDueDateAndCheckPriceMap.get(checkDueDate)+checkPrice);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return checkDueDateAndCheckPriceMap;
    }
    public void getManufacturerInfo(SearchNonePayReceive_Bean SearchNonePayReceive_Bean){
        String Query = "select A.ObjectNickName,B.CheckTitle,A.DefaultPaymentMethod,B.CheckDueDay,B.DiscountPostage,B.Postage from Manufacturer A inner join Manufacturer_PayInfo B on A.id = B.Manufacturer_id where A.ObjectID = ?";
//        ERP.ERPApplication.Logger.info(Query);
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,SearchNonePayReceive_Bean.getObjectID());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                SearchNonePayReceive_Bean.setObjectNickName(Rs.getString(1));
                SearchNonePayReceive_Bean.setCheckTitle(Rs.getString(2));
                SearchNonePayReceive_Bean.setDefaultPaymentMethod(ObjectInfo_Enum.DefaultPaymentMethod.values()[Rs.getInt(3)]);
                SearchNonePayReceive_Bean.setCheckDueDay(Rs.getObject(4) == null ? null : Rs.getInt(4));
                SearchNonePayReceive_Bean.setDiscountPostage(Rs.getBoolean(5));
                SearchNonePayReceive_Bean.setPostage(Rs.getInt(6));
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
    }
}
