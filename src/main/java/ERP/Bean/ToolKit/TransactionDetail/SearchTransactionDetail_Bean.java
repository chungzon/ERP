package ERP.Bean.ToolKit.TransactionDetail;

import ERP.Enum.Order.Order_Enum.OrderSearchMethod;

public class SearchTransactionDetail_Bean {
    OrderSearchMethod orderSearchMethod;
    String ISBN;
    boolean isGetQuotation, isGetWaitingOrder;
    String objectID, startDate, endDate;

    public OrderSearchMethod getOrderSearchMethod() {   return orderSearchMethod;   }
    public void setOrderSearchMethod(OrderSearchMethod orderSearchMethod) { this.orderSearchMethod = orderSearchMethod; }

    public String getISBN() {   return ISBN;    }
    public void setISBN(String ISBN) {  this.ISBN = ISBN;   }

    public boolean isGetQuotation() {   return isGetQuotation;  }
    public void setGetQuotation(boolean getQuotation) { isGetQuotation = getQuotation;  }

    public boolean isGetWaitingOrder() { return isGetWaitingOrder;    }
    public void setGetWaitingOrder(boolean getWaitingOrder) { isGetWaitingOrder = getWaitingOrder;  }

    public String getObjectID() {   return objectID;    }
    public void setObjectID(String objectID) {  this.objectID = objectID;   }

    public String getStartDate() {  return startDate;   }
    public void setStartDate(String startDate) {    this.startDate = startDate; }

    public String getEndDate() {    return endDate; }
    public void setEndDate(String endDate) {    this.endDate = endDate; }
}
