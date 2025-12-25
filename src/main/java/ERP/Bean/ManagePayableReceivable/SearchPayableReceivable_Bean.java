package ERP.Bean.ManagePayableReceivable;

import ERP.Bean.Order.SearchOrder_Bean;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
/** [ERP.Bean] Search payable or receivable info in manage payable or receivable system */
public class SearchPayableReceivable_Bean {
    private int PayableReceivableID;
    private SimpleBooleanProperty isExistWaitingOrder;
    private SimpleStringProperty OrderNumber, ObjectNickName, CheckTitle, CheckNumber, CheckDueDate, ObjectBankNickName, Remark;
    private SimpleIntegerProperty CheckPrice, Cash, Deposit, OtherDiscount,RemittanceFeeAndPostage, CashDiscount, OffsetPrice;

    private String OrderDate, ObjectID, ObjectBankBranch, ObjectBankAccount, ObjectAccountName, ObjectPerson, InvoiceTitle, InvoiceNumber;
    private Integer companyBankInfo_id;
    private Integer CheckDueDay;
    private BankInfo_Bean ObjectBankBean;
    private ObservableList<SearchOrder_Bean> OrderList;

    public SearchPayableReceivable_Bean(){
        this.isExistWaitingOrder = new SimpleBooleanProperty();
        this.OrderNumber = new SimpleStringProperty();
        this.ObjectNickName = new SimpleStringProperty();
        this.CheckTitle = new SimpleStringProperty();
        this.CheckNumber = new SimpleStringProperty();
        this.CheckDueDate = new SimpleStringProperty();
        this.ObjectBankNickName = new SimpleStringProperty();
        this.Remark = new SimpleStringProperty();
        this.CheckPrice = new SimpleIntegerProperty();
        this.Cash = new SimpleIntegerProperty();
        this.Deposit = new SimpleIntegerProperty();
        this.OtherDiscount = new SimpleIntegerProperty();
        this.RemittanceFeeAndPostage = new SimpleIntegerProperty();
        this.CashDiscount = new SimpleIntegerProperty();
        this.OffsetPrice = new SimpleIntegerProperty();
    }

    public int getPayableReceivableID() {
        return PayableReceivableID;
    }

    public void setPayableReceivableID(int payableReceivableID) {
        PayableReceivableID = payableReceivableID;
    }

    public boolean getIsExistWaitingOrder() {  return isExistWaitingOrder.get(); }
    public SimpleBooleanProperty isExistWaitingOrderProperty() {   return isExistWaitingOrder;   }
    public void setIsExistWaitingOrder(boolean isExistWaitingOrder) {    this.isExistWaitingOrder.set(isExistWaitingOrder);  }

    public String getOrderNumber() {    return OrderNumber.get();   }
    public SimpleStringProperty OrderNumberProperty() { return OrderNumber; }
    public void setOrderNumber(String OrderNumber) {    this.OrderNumber.set(OrderNumber);  }

    public String getOrderDate(){   return OrderDate;   }
    public void setOrderDate(String OrderDate){ this.OrderDate = OrderDate; }

    public String getObjectID() {   return ObjectID;    }
    public void setObjectID(String ObjectID) {  this.ObjectID = ObjectID;    }

    public String getObjectNickName() { return ObjectNickName.get();    }
    public SimpleStringProperty ObjectNickNameProperty() {  return ObjectNickName;  }
    public void setObjectNickName(String ObjectNickName) {  this.ObjectNickName.set(ObjectNickName);    }

    public String getCheckTitle() { return CheckTitle.get();    }
    public SimpleStringProperty CheckTitleProperty() {  return CheckTitle;  }
    public void setCheckTitle(String CheckTitle) {  this.CheckTitle.set(CheckTitle);    }

    public Integer getCheckDueDay() {    return CheckDueDay; }
    public void setCheckDueDay(Integer checkDueDay) {    CheckDueDay = checkDueDay;  }

    public String getCheckNumber() {    return CheckNumber.get();   }
    public SimpleStringProperty CheckNumberProperty() { return CheckNumber; }
    public void setCheckNumber(String CheckNumber) {    this.CheckNumber.set(CheckNumber);  }

    public String getCheckDueDate() {   return CheckDueDate.get();  }
    public SimpleStringProperty CheckDueDateProperty() {    return CheckDueDate;    }
    public void setCheckDueDate(String CheckDueDate) {  this.CheckDueDate.set(CheckDueDate);    }

    public String getObjectBankNickName() { return ObjectBankNickName.get();    }
    public SimpleStringProperty ObjectBankNickNameProperty() {  return ObjectBankNickName;  }
    public void setObjectBankNickName(String ObjectBankNickName) {  this.ObjectBankNickName.set(ObjectBankNickName);    }

    public Integer getCompanyBankInfo_id() {    return companyBankInfo_id;  }
    public void setCompanyBankInfo_id(Integer companyBankInfo_id) { this.companyBankInfo_id = companyBankInfo_id;   }

    public BankInfo_Bean getObjectBankBean(){   return ObjectBankBean;    }
    public void setObjectBankBean(BankInfo_Bean ObjectBankBean){  this.ObjectBankBean = ObjectBankBean;   }

    public String getObjectBankBranch() {   return ObjectBankBranch;    }
    public void setObjectBankBranch(String ObjectBankBranch) {  this.ObjectBankBranch = ObjectBankBranch;    }

    public String getObjectBankAccount() {  return ObjectBankAccount;   }
    public void setObjectBankAccount(String ObjectBankAccount) {    this.ObjectBankAccount = ObjectBankAccount;  }

    public String getObjectAccountName() {  return ObjectAccountName;   }
    public void setObjectAccountName(String ObjectAccountName) {    this.ObjectAccountName = ObjectAccountName;  }

    public String getObjectPerson(){  return ObjectPerson;    }
    public void setObjectPerson(String ObjectPerson){   this.ObjectPerson = ObjectPerson;   }

//    public String getObjectBankNickName() { return ObjectBankNickName.get();    }
//    public SimpleStringProperty ObjectBankNickNameProperty() {  return ObjectBankNickName;  }
//    public void setObjectBank(String ObjectBankNickName) {  this.ObjectBankNickName.set(ObjectBankNickName);    }

    public String getInvoiceTitle(){   return InvoiceTitle;   }
    public void setInvoiceTitle(String InvoiceTitle){ this.InvoiceTitle = InvoiceTitle; }

    public String getInvoiceNumber(){   return InvoiceNumber;   }
    public void setInvoiceNumber(String InvoiceNumber){ this.InvoiceNumber = InvoiceNumber; }

    public String getRemark() { return Remark.get();    }
    public SimpleStringProperty RemarkProperty() {  return Remark;  }
    public void setRemark(String Remark) {  this.Remark.set(Remark);    }

    public int getCheckPrice() {    return CheckPrice.get();    }
    public SimpleIntegerProperty CheckPriceProperty() { return CheckPrice;  }
    public void setCheckPrice(int CheckPrice) { this.CheckPrice.set(CheckPrice);    }

    public int getCash() {  return Cash.get();  }
    public SimpleIntegerProperty CashProperty() {   return Cash;    }
    public void setCash(int Cash) { this.Cash.set(Cash);    }

    public int getDeposit() {   return Deposit.get();   }
    public SimpleIntegerProperty DepositProperty() {    return Deposit; }
    public void setDeposit(int Deposit) {   this.Deposit.set(Deposit);  }

    public int getOtherDiscount() { return OtherDiscount.get(); }
    public SimpleIntegerProperty OtherDiscountProperty() {  return OtherDiscount;   }
    public void setOtherDiscount(int OtherDiscount) {   this.OtherDiscount.set(OtherDiscount);  }

    public int getRemittanceFeeAndPostage() {   return RemittanceFeeAndPostage.get();   }
    public SimpleIntegerProperty RemittanceFeeAndPostageProperty() {    return RemittanceFeeAndPostage; }
    public void setRemittanceFeeAndPostage(int remittanceFeeAndPostage) {   this.RemittanceFeeAndPostage.set(remittanceFeeAndPostage);  }

    public int getCashDiscount() {  return CashDiscount.get();  }
    public SimpleIntegerProperty cashDiscountProperty() {   return CashDiscount;    }
    public void setCashDiscount(int cashDiscount) { this.CashDiscount.set(cashDiscount);    }

    public int getOffsetPrice() {   return OffsetPrice.get();   }
    public SimpleIntegerProperty OffsetPriceProperty() {    return OffsetPrice; }
    public void setOffsetPrice(int OffsetPrice) {   this.OffsetPrice.set(OffsetPrice);  }

    public ObservableList<SearchOrder_Bean> getOrderList(){ return OrderList;   }
    public void setOrderList(ObservableList<SearchOrder_Bean> OrderList){   this.OrderList = OrderList; }
}
