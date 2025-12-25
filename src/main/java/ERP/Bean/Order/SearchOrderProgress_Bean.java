package ERP.Bean.Order;

import ERP.Enum.Order.Order_Enum;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
/** [ERP.Bean] Search order progress info */
public class SearchOrderProgress_Bean {
    private SimpleStringProperty ObjectID, ObjectName, QuotationDate, QuotationNumber, WaitingOrderDate, WaitingOrderNumber, AlreadyOrderDate, AlreadyOrderNumber, ProjectCode, ProjectName, ExportQuotationManufacturerNickName, ReviewStatus, isBorrowed, isOffset, isCheckout;
    private ObservableList<Order_Bean> SubBillList;
    public SearchOrderProgress_Bean(){
        ObjectID = new SimpleStringProperty();
        ObjectName = new SimpleStringProperty();
        QuotationDate = new SimpleStringProperty();
        QuotationNumber = new SimpleStringProperty();
        WaitingOrderDate = new SimpleStringProperty();
        WaitingOrderNumber = new SimpleStringProperty();
        AlreadyOrderDate = new SimpleStringProperty();
        AlreadyOrderNumber = new SimpleStringProperty();
        ProjectCode = new SimpleStringProperty();
        ProjectName = new SimpleStringProperty();
        ExportQuotationManufacturerNickName = new SimpleStringProperty();
        ReviewStatus = new SimpleStringProperty();
        isBorrowed = new SimpleStringProperty();
        isOffset = new SimpleStringProperty();
        isCheckout = new SimpleStringProperty();
    }
    public String getObjectID() {   return ObjectID.get();  }
    public SimpleStringProperty ObjectIDProperty() {    return ObjectID;    }
    public void setObjectID(String ObjectID) {  this.ObjectID.set(ObjectID);    }

    public String getObjectName() { return ObjectName.get();    }
    public SimpleStringProperty ObjectNameProperty() {  return ObjectName;  }
    public void setObjectName(String ObjectName) {  this.ObjectName.set(ObjectName);    }

    public String getQuotationDate() {  return QuotationDate.get(); }
    public SimpleStringProperty QuotationDateProperty() {   return QuotationDate;   }
    public void setQuotationDate(String QuotationDate) {    this.QuotationDate.set(QuotationDate);  }

    public String getQuotationNumber() {  return QuotationNumber.get(); }
    public SimpleStringProperty QuotationNumberProperty() {   return QuotationNumber;   }
    public void setQuotationNumber(String QuotationNumber) {    this.QuotationNumber.set(QuotationNumber);  }

    public String getWaitingOrderDate() {   return WaitingOrderDate.get();  }
    public SimpleStringProperty WaitingOrderDateProperty() {    return WaitingOrderDate;    }
    public void setWaitingOrderDate(String WaitingOrderDate) {  this.WaitingOrderDate.set(WaitingOrderDate);    }

    public String getWaitingOrderNumber() {   return WaitingOrderNumber.get();  }
    public SimpleStringProperty WaitingOrderNumberProperty() {    return WaitingOrderNumber;    }
    public void setWaitingOrderNumber(String WaitingOrderNumber) {  this.WaitingOrderNumber.set(WaitingOrderNumber);    }

    public String getAlreadyOrderDate() {   return AlreadyOrderDate.get();  }
    public SimpleStringProperty AlreadyOrderDateProperty() {    return AlreadyOrderDate;    }
    public void setAlreadyOrderDate(String AlreadyOrderDate) {  this.AlreadyOrderDate.set(AlreadyOrderDate);    }

    public String getAlreadyOrderNumber() {   return AlreadyOrderNumber.get();  }
    public SimpleStringProperty AlreadyOrderNumberProperty() {    return AlreadyOrderNumber;    }
    public void setAlreadyOrderNumber(String AlreadyOrderNumber) {  this.AlreadyOrderNumber.set(AlreadyOrderNumber);    }

    public void setSubBillList(ObservableList<Order_Bean> SubBillList){ this.SubBillList = SubBillList; }
    public ObservableList<Order_Bean> getSubBillList(){   return SubBillList;   }

    public String getProjectCode() {    return ProjectCode.get();   }
    public SimpleStringProperty ProjectCodeProperty() { return ProjectCode; }
    public void setProjectCode(String projectCode) {    this.ProjectCode.set(projectCode);  }

    public String getProjectName() {   return ProjectName.get();  }
    public SimpleStringProperty ProjectNameProperty() {    return ProjectName;    }
    public void setProjectName(String ProjectName) {  this.ProjectName.set(ProjectName);    }

    public String getExportQuotationManufacturerNickName() {    return ExportQuotationManufacturerNickName.get();   }
    public SimpleStringProperty ExportQuotationManufacturerNickNameProperty() { return ExportQuotationManufacturerNickName; }
    public void setExportQuotationManufacturerNickName(String exportQuotationManufacturerNickName) {    this.ExportQuotationManufacturerNickName.set(exportQuotationManufacturerNickName);  }

    public Order_Enum.ReviewStatus getReviewStatus() {  return Order_Enum.ReviewStatus.valueOf(ReviewStatus.get());  }
    public SimpleStringProperty ReviewStatusProperty() {    return ReviewStatus;    }
    public void setReviewStatus(Order_Enum.ReviewStatus ReviewStatus) {
        if(ReviewStatus != Order_Enum.ReviewStatus.無)
            this.ReviewStatus.set(ReviewStatus.name());
    }

    public boolean isBorrowed() {
        return isBorrowed.get().equals("是");
    }
    public SimpleStringProperty isBorrowedProperty() { return isBorrowed;  }
    public void setIsBorrowed(boolean isBorrowed) {
        if(isBorrowed)  this.isBorrowed.set("是");
        else this.isBorrowed.set("否");
    }

    public boolean isOffset() { return isOffset.get().contains("沖");    }
    public boolean getOffsetOrderStatus() {
        return isOffset.get().contains("沖");
    }
    public SimpleStringProperty isOffsetProperty() { return isOffset;  }
    public void setIsOffset(Order_Enum.OffsetOrderStatus offsetOrderStatus) {
        if(!offsetOrderStatus.value()){
            this.isOffset.set("");
        }else{
            this.isOffset.set(offsetOrderStatus.getDescribe());
        }
    }

    public boolean isCheckout() {   return isCheckout.get().equals("已結");    }
    public SimpleStringProperty isCheckoutProperty() { return isCheckout;  }
    public void setIsCheckout(boolean isCheckout) {
        if(isCheckout)  this.isCheckout.set("已結");
        else this.isCheckout.set("未結");
    }
}
