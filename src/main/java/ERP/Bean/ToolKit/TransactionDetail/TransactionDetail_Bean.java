package ERP.Bean.ToolKit.TransactionDetail;

import ERP.Enum.Order.Order_Enum;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderBorrowed;
import ERP.Enum.Order.Order_Enum.OffsetOrderStatus;

public class TransactionDetail_Bean {
    private SimpleIntegerProperty index, quantity, numberOfItems;
    private SimpleStringProperty orderType, objectID, orderDate, orderNumber, waitingOrderDate, waitingOrderNumber, alreadyOrderDate, alreadyOrderNumber, returnOrderDate, returnOrderNumber, productName, firmCode, isCheckout;
    private SimpleDoubleProperty itemPriceColumn, TotalPriceNoneTax;

    private String ObjectName;
    private OrderSource OrderSource;
    private OrderBorrowed isBorrowed;
    private OffsetOrderStatus offsetOrderStatus;
    public TransactionDetail_Bean(){
        this.index = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.numberOfItems = new SimpleIntegerProperty();
        this.orderType = new SimpleStringProperty();
        this.objectID = new SimpleStringProperty();
        this.orderDate = new SimpleStringProperty();
        this.orderNumber = new SimpleStringProperty();
        this.waitingOrderDate = new SimpleStringProperty();
        this.waitingOrderNumber = new SimpleStringProperty();
        this.alreadyOrderDate = new SimpleStringProperty();
        this.alreadyOrderNumber = new SimpleStringProperty();
        this.returnOrderDate = new SimpleStringProperty();
        this.returnOrderNumber = new SimpleStringProperty();
        this.productName = new SimpleStringProperty();
        this.firmCode = new SimpleStringProperty();
        this.isCheckout = new SimpleStringProperty();
        this.itemPriceColumn = new SimpleDoubleProperty();
        this.TotalPriceNoneTax = new SimpleDoubleProperty();
    }

    public int getIndex() { return index.get(); }
    public SimpleIntegerProperty indexProperty() {  return index;   }
    public void setIndex(int Index) {   this.index.set(Index);  }

    public int getQuantity() {    return quantity.get();    }
    public SimpleIntegerProperty quantityProperty() { return quantity;  }
    public void setQuantity(int Quantity) { this.quantity.set(Quantity);    }

    public int getNumberOfItems() {   return numberOfItems.get();   }
    public SimpleIntegerProperty numberOfItemsProperty() {    return numberOfItems; }
    public void setNumberOfItems(int NumberOfItems) {   this.numberOfItems.set(NumberOfItems);  }

    public String getOrderType() {    return orderType.get();   }
    public SimpleStringProperty orderTypeProperty() { return orderType; }
    public void setOrderType(String OrderType) {    this.orderType.set(OrderType);  }

    public String getObjectID() { return objectID.get();    }
    public SimpleStringProperty objectIDProperty() {  return objectID;  }
    public void setObjectID(String ObjectID) {  this.objectID.set(ObjectID);    }

    public String getOrderDate() {  return orderDate.get(); }
    public SimpleStringProperty orderDateProperty() {   return orderDate;   }
    public void setOrderDate(String orderDate) {    this.orderDate.set(orderDate);  }

    public String getOrderNumber(){ return orderNumber.get(); }
    public SimpleStringProperty orderNumberProperty() { return orderNumber; }
    public void setOrderNumber(String orderNumber){ this.orderNumber.set(orderNumber); }

    public String getWaitingOrderDate(){    return waitingOrderDate.get();    }
    public SimpleStringProperty waitingOrderDateProperty() {  return waitingOrderDate;  }
    public void setWaitingOrderDate(String waitingOrderDate){   this.waitingOrderDate.set(waitingOrderDate);  }

    public String getWaitingOrderNumber(){ return waitingOrderNumber.get(); }
    public SimpleStringProperty waitingOrderNumberProperty() {    return waitingOrderNumber;    }
    public void setWaitingOrderNumber(String waitingOrderNumber){ this.waitingOrderNumber.set(waitingOrderNumber); }

    public String getAlreadyOrderDate() {    return alreadyOrderDate.get();   }
    public SimpleStringProperty alreadyOrderDateProperty() { return alreadyOrderDate; }
    public void setAlreadyOrderDate(String AlreadyOrderDate) {    this.alreadyOrderDate.set(AlreadyOrderDate);  }

    public String getAlreadyOrderNumber() {  return alreadyOrderNumber.get(); }
    public SimpleStringProperty alreadyOrderNumberProperty() {   return alreadyOrderNumber;   }
    public void setAlreadyOrderNumber(String AlreadyOrderNumber) {    this.alreadyOrderNumber.set(AlreadyOrderNumber);  }

    public String getReturnOrderDate() {    return returnOrderDate.get();   }
    public SimpleStringProperty returnOrderDateProperty() { return returnOrderDate; }
    public void setReturnOrderDate(String returnOrderDate) {    this.returnOrderDate.set(returnOrderDate);  }

    public String getReturnOrderNumber() {  return returnOrderNumber.get(); }
    public SimpleStringProperty returnOrderNumberProperty() {   return returnOrderNumber;   }
    public void setReturnOrderNumber(String returnOrderNumber) {    this.returnOrderNumber.set(returnOrderNumber);  }

    public String getProductName() {  return productName.get(); }
    public SimpleStringProperty productNameProperty() {   return productName;   }
    public void setProductName(String ProductName) {    this.productName.set(ProductName);  }

    public String getFirmCode() { return firmCode.get();    }
    public SimpleStringProperty firmCodeProperty() {  return firmCode;  }
    public void setFirmCode(String FirmCode) {  this.firmCode.set(FirmCode);    }

    public boolean getIsCheckout() {   return isCheckout.get().equals("已結");  }
    public SimpleStringProperty isCheckoutProperty() {    return isCheckout;    }
    public void setIsCheckout(boolean isCheckout, OrderSource OrderSource) {
        if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單){
            if(isCheckout)  this.isCheckout.set("已結");
            else this.isCheckout.set("未結");
        }else   this.isCheckout.set("-");
    }

    public double getItemPriceColumn() {  return itemPriceColumn.get(); }
    public SimpleDoubleProperty itemPriceProperty() {   return itemPriceColumn;   }
    public void setItemPriceColumn(double PriceAmount) {    this.itemPriceColumn.set(PriceAmount);  }

    public double getTotalPriceNoneTax() {   return TotalPriceNoneTax.get();  }
    public SimpleDoubleProperty TotalPriceNoneTaxProperty() {    return TotalPriceNoneTax;    }
    public void setTotalPriceNoneTax(double TotalPriceNoneTax) {  this.TotalPriceNoneTax.set(TotalPriceNoneTax);    }

    public String getObjectName(){    return ObjectName;    }
    public void setObjectName(String ObjectName){   this.ObjectName = ObjectName;   }

    public OrderSource getOrderSource(){   return OrderSource;   }
    public void setOrderSource(String OrderSource){ this.OrderSource = Order_Enum.OrderSource.valueOf(OrderSource); }

    public OrderBorrowed isBorrowed() { return isBorrowed;  }
    public void setIsBorrowed(boolean isBorrowed) {
        if(isBorrowed)      this.isBorrowed = OrderBorrowed.已借測;
        else    this.isBorrowed = OrderBorrowed.未借測;
    }
    public OffsetOrderStatus getOffsetOrderStatus() { return offsetOrderStatus;  }
    public boolean isOffset() { return offsetOrderStatus.value();  }
    public void setOffsetOrderStatus(OffsetOrderStatus offsetOrderStatus) {
        this.offsetOrderStatus = offsetOrderStatus;
    }
}
