package ERP.Bean.Order.SearchNonePayReceive;

import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DefaultPaymentMethod;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

/** [ERP.Bean] Search none payable or receivable in manage purchase or shipment system */
public class SearchNonePayReceive_Bean {
    private CheckBox CheckBox;
    private SimpleStringProperty ObjectID, ObjectName, NonePayReceiveHistoryDate;
    private SimpleIntegerProperty OrderQuantity, ReturnOrderQuantity, OrderPriceAmount, ReturnOrderPriceAmount, Tax, Discount, PayReceivePrice, ActualPayReceivePrice;

    private String objectNickName,checkTitle;
    private DefaultPaymentMethod defaultPaymentMethod;
    private Integer checkDueDay;
    private boolean discountPostage;
    private Integer postage;

    private ObservableList<SearchOrder_Bean> OrderList;

    public SearchNonePayReceive_Bean(){
        this.ObjectID = new SimpleStringProperty();
        this.ObjectName = new SimpleStringProperty();
        this.OrderQuantity = new SimpleIntegerProperty();
        this.OrderPriceAmount = new SimpleIntegerProperty();
        this.ReturnOrderQuantity = new SimpleIntegerProperty();
        this.ReturnOrderPriceAmount = new SimpleIntegerProperty();
        this.Tax = new SimpleIntegerProperty();
        this.Discount = new SimpleIntegerProperty();
        this.PayReceivePrice = new SimpleIntegerProperty();
        this.ActualPayReceivePrice = new SimpleIntegerProperty();
        this.NonePayReceiveHistoryDate = new SimpleStringProperty();
    }
    public void setCheckBox(CheckBox CheckBox, boolean defaultSelect){
        this.CheckBox = CheckBox;
        this.CheckBox.setSelected(defaultSelect);
    }
    public CheckBox getCheckBox(){  return CheckBox;    }
    public void setCheckBoxSelect(Boolean select){  CheckBox.setSelected(select);    }
    public boolean isCheckBoxSelect(){  return CheckBox.isSelected();   }

    public void setOrderList(ObservableList<SearchOrder_Bean> OrderList){  this.OrderList = OrderList;   }
    public ObservableList<SearchOrder_Bean> getOrderList(){  return OrderList;    }

    public String getObjectID() {   return ObjectID.get();  }
    public SimpleStringProperty objectIDProperty() {    return ObjectID;    }
    public void setObjectID(String ObjectID) {  this.ObjectID.set(ObjectID);    }

    public String getObjectName() { return ObjectName.get();    }
    public SimpleStringProperty objectNameProperty() {  return ObjectName;  }
    public void setObjectName(String ObjectName) {  this.ObjectName.set(ObjectName);    }

    public int getOrderPriceAmount() {   return OrderPriceAmount.get();  }
    public SimpleIntegerProperty orderPriceAmountProperty() {    return OrderPriceAmount;    }
    public void setOrderPriceAmount(int OrderPriceAmount) {  this.OrderPriceAmount.set(OrderPriceAmount);    }

    public int getReturnOrderPriceAmount() { return ReturnOrderPriceAmount.get();    }
    public SimpleIntegerProperty returnOrderPriceAmountProperty() {  return ReturnOrderPriceAmount;  }
    public void setReturnOrderPriceAmount(int ReturnOrderPriceAmount) {      this.ReturnOrderPriceAmount.set(ReturnOrderPriceAmount);    }

    public int getTax() {    return Tax.get();   }
    public SimpleIntegerProperty taxProperty() { return Tax; }
    public void setTax(int Tax) {    this.Tax.set(Tax);  }

    public int getDiscount() {   return Discount.get();  }
    public SimpleIntegerProperty discountProperty() {    return Discount;    }
    public void setDiscount(int Discount) {  this.Discount.set(Discount);    }

    public int getPayReceivePrice() {   return PayReceivePrice.get();  }
    public SimpleIntegerProperty PayReceivePriceProperty() {    return PayReceivePrice;    }
    public void setPayReceivePrice(int PayReceivePrice) {  this.PayReceivePrice.set(PayReceivePrice);    }

    public int getActualPayReceivePrice() {  return ActualPayReceivePrice.get(); }
    public SimpleIntegerProperty ActualPayReceivePriceProperty() {   return ActualPayReceivePrice;   }
    public void setActualPayReceivePrice(int ActualPayReceivePrice) {    this.ActualPayReceivePrice.set(ActualPayReceivePrice);  }

    public String getNonePayReceiveHistoryDate() { return NonePayReceiveHistoryDate.get();    }
    public SimpleStringProperty NonePayReceiveHistoryDateProperty() {  return NonePayReceiveHistoryDate;  }
    public void setNonePayReceiveHistoryDate(String nonePayReceiveHistoryDate) {  this.NonePayReceiveHistoryDate.set(nonePayReceiveHistoryDate);    }

    public int getOrderQuantity() { return OrderQuantity.get(); }
    public SimpleIntegerProperty orderQuantityProperty() {  return OrderQuantity;   }
    public void setOrderQuantity(int OrderQuantity) {   this.OrderQuantity.set(OrderQuantity);  }

    public int getReturnOrderQuantity() {   return ReturnOrderQuantity.get();   }
    public SimpleIntegerProperty returnOrderQuantityProperty() {    return ReturnOrderQuantity; }
    public void setReturnOrderQuantity(int ReturnOrderQuantity) {   this.ReturnOrderQuantity.set(ReturnOrderQuantity);  }

    public String getObjectNickName() { return objectNickName;  }
    public void setObjectNickName(String objectNickName) {  this.objectNickName = objectNickName;   }

    public String getCheckTitle() { return checkTitle;  }
    public void setCheckTitle(String checkTitle) {  this.checkTitle = checkTitle;   }

    public DefaultPaymentMethod getDefaultPaymentMethod() { return defaultPaymentMethod;    }
    public void setDefaultPaymentMethod(DefaultPaymentMethod defaultPaymentMethod) {    this.defaultPaymentMethod = defaultPaymentMethod;   }

    public Integer getCheckDueDay() {   return checkDueDay; }
    public void setCheckDueDay(Integer checkDueDay) {   this.checkDueDay = checkDueDay; }

    public boolean isDiscountPostage() {    return discountPostage; }
    public void setDiscountPostage(boolean discountPostage) {   this.discountPostage = discountPostage; }

    public Integer getPostage() {   return postage; }
    public void setPostage(Integer postage) {   this.postage = postage; }
}
