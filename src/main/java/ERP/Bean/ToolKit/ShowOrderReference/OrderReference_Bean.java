package ERP.Bean.ToolKit.ShowOrderReference;

import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderReference_Bean {
    private SimpleIntegerProperty Index;
    private SimpleStringProperty OrderType, ObjectID, orderDate, orderNumber, AlreadyOrderDate, AlreadyOrderNumber, ProjectName, isCheckout;
    private SimpleDoubleProperty TotalPriceIncludeTax;

    private int order_id;
    private OrderSource OrderSource;
    private String WaitingOrderDate, WaitingOrderNumber, ObjectName;
    public OrderReference_Bean(){
        this.Index = new SimpleIntegerProperty();
        this.OrderType = new SimpleStringProperty();
        this.ObjectID = new SimpleStringProperty();
        this.orderDate = new SimpleStringProperty();
        this.orderNumber = new SimpleStringProperty();
        this.AlreadyOrderDate = new SimpleStringProperty();
        this.AlreadyOrderNumber = new SimpleStringProperty();
        this.ProjectName = new SimpleStringProperty();
        this.isCheckout = new SimpleStringProperty();
        this.TotalPriceIncludeTax = new SimpleDoubleProperty();
    }

    public int getIndex() { return Index.get(); }
    public SimpleIntegerProperty IndexProperty() {  return Index;   }
    public void setIndex(int Index) {   this.Index.set(Index);  }

    public String getOrderType() {    return OrderType.get();   }
    public SimpleStringProperty OrderTypeProperty() { return OrderType; }
    public void setOrderType(String OrderType) {    this.OrderType.set(OrderType);  }

    public String getObjectID() { return ObjectID.get();    }
    public SimpleStringProperty ObjectIDProperty() {  return ObjectID;  }
    public void setObjectID(String ObjectID) {  this.ObjectID.set(ObjectID);    }

    public String getOrderDate() {  return orderDate.get(); }
    public SimpleStringProperty orderDateProperty() {   return orderDate;   }
    public void setOrderDate(String orderDate) {    this.orderDate.set(orderDate);  }

    public String getOrderNumber(){ return orderNumber.get(); }
    public SimpleStringProperty orderNumberProperty() { return orderNumber; }
    public void setOrderNumber(String orderNumber){ this.orderNumber.set(orderNumber); }

    public String getAlreadyOrderDate() {    return AlreadyOrderDate.get();   }
    public SimpleStringProperty AlreadyOrderDateProperty() { return AlreadyOrderDate; }
    public void setAlreadyOrderDate(String AlreadyOrderDate) {    this.AlreadyOrderDate.set(AlreadyOrderDate);  }

    public String getAlreadyOrderNumber() {  return AlreadyOrderNumber.get(); }
    public SimpleStringProperty AlreadyOrderNumberProperty() {   return AlreadyOrderNumber;   }
    public void setAlreadyOrderNumber(String AlreadyOrderNumber) {    this.AlreadyOrderNumber.set(AlreadyOrderNumber);  }

    public String getProjectName() {  return ProjectName.get(); }
    public SimpleStringProperty ProjectNameProperty() {   return ProjectName;   }
    public void setProjectName(String ProjectName) {    this.ProjectName.set(ProjectName);  }

    public boolean getIsCheckout() {   return isCheckout.get().equals("已結");  }
    public SimpleStringProperty isCheckoutProperty() {    return isCheckout;    }
    public void setIsCheckout(boolean isCheckout, OrderSource OrderSource) {
        if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單){
            if(isCheckout)  this.isCheckout.set("已結");
            else this.isCheckout.set("未結");
        }else   this.isCheckout.set("-");
    }

    public double getTotalPriceIncludeTax() {   return TotalPriceIncludeTax.get();  }
    public SimpleDoubleProperty TotalPriceIncludeTaxProperty() {    return TotalPriceIncludeTax;    }
    public void setTotalPriceIncludeTax(double TotalPriceIncludeTax) {  this.TotalPriceIncludeTax.set(TotalPriceIncludeTax);    }

    public int getOrder_id() {  return order_id;    }
    public void setOrder_id(int order_id) { this.order_id = order_id;   }

    public OrderSource getOrderSource(){   return OrderSource;   }
    public void setOrderSource(OrderSource OrderSource){ this.OrderSource = OrderSource; }

    public String getObjectName() { return ObjectName;  }
    public void setObjectName(String objectName) {  ObjectName = objectName;    }

    public String getWaitingOrderDate(){    return WaitingOrderDate;    }
    public void setWaitingOrderDate(String WaitingOrderDate){   this.WaitingOrderDate = WaitingOrderDate;  }

    public String getWaitingOrderNumber(){  return WaitingOrderNumber;  }
    public void setWaitingOrderNumber(String WaitingOrderNumber){   this.WaitingOrderNumber = WaitingOrderNumber;   }
}
