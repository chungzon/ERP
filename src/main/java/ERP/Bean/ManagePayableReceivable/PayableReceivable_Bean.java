package ERP.Bean.ManagePayableReceivable;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Enum.Order.Order_Enum.OrderObject;
import javafx.collections.ObservableList;

/** [ERP.Bean] Establish payable or receivable info in manage payable or receivable system */
public class PayableReceivable_Bean {
    private String OrderNumber, OrderDate, ObjectID, CheckNumber, CheckDueDate, ObjectBankBranch, ObjectBankAccount, ObjectAccountName, ObjectPerson;
    private Integer companyBankInfo_id;
    private String Cash, Deposit, OtherDiscount, remittanceFeeAndPostage, CashDiscount, CheckPrice, OffsetPrice, TotalPriceIncludeTax;
    private String InvoiceNumber, Remark;

    private BankInfo_Bean ObjectBankBean;
    private OrderObject OrderObject;
    private ObservableList<SearchOrder_Bean> OrderList;

    public OrderObject getOrderObject() {    return OrderObject; }
    public void setOrderObject(OrderObject OrderObject) {   this.OrderObject = OrderObject;  }

    public String getOrderNumber() {
        return OrderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getOrderDate() {  return OrderDate;   }
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getObjectID() {
        return ObjectID;
    }
    public void setObjectID(String ObjectID) {
        this.ObjectID = ObjectID;
    }

    public String getCheckNumber() {
        return CheckNumber;
    }
    public void setCheckNumber(String CheckNumber) {
        this.CheckNumber = CheckNumber;
    }

    public String getCheckDueDate() {
        return CheckDueDate;
    }
    public void setCheckDueDate(String CheckDueDate) {  this.CheckDueDate = CheckDueDate;   }

    public Integer getCompanyBankInfo_id() {    return companyBankInfo_id;  }
    public void setCompanyBankInfo_id(Integer companyBankInfo_id) { this.companyBankInfo_id = companyBankInfo_id;   }

    public BankInfo_Bean getObjectBankBean() {
        return ObjectBankBean;
    }
    public void setObjectBankBean(BankInfo_Bean ObjectBankBean) {    this.ObjectBankBean = ObjectBankBean;   }

    public String getObjectBankBranch() {
        return ObjectBankBranch;
    }
    public void setObjectBankBranch(String ObjectBankBranch) {  this.ObjectBankBranch = ObjectBankBranch;   }

    public String getObjectBankAccount() {
        return ObjectBankAccount;
    }
    public void setObjectBankAccount(String ObjectBankAccount) {
        this.ObjectBankAccount = ObjectBankAccount;
    }

    public String getObjectAccountName() {
        return ObjectAccountName;
    }
    public void setObjectAccountName(String ObjectAccountName) {
        this.ObjectAccountName = ObjectAccountName;
    }

    public String getObjectPerson(){    return ObjectPerson;    }
    public void setObjectPerson(String ObjectPerson){   this.ObjectPerson = ObjectPerson;   }

    public String getCash() {
        return Cash;
    }
    public void setCash(String Cash) {  this.Cash = Cash;   }

    public String getDeposit() {
        return Deposit;
    }
    public void setDeposit(String Deposit) {
        this.Deposit = Deposit;
    }

    public String getOtherDiscount() {
        return OtherDiscount;
    }
    public void setOtherDiscount(String OtherDiscount) {
        this.OtherDiscount = OtherDiscount;
    }

    public String getRemittanceFeeAndPostage() {    return remittanceFeeAndPostage; }
    public void setRemittanceFeeAndPostage(String remittanceFeeAndPostage) {    this.remittanceFeeAndPostage = remittanceFeeAndPostage; }

    public String getCashDiscount() {
        return CashDiscount;
    }
    public void setCashDiscount(String CashDiscount) {
        this.CashDiscount = CashDiscount;
    }

    public String getCheckPrice() {
        return CheckPrice;
    }
    public void setCheckPrice(String CheckPrice) {
        this.CheckPrice = CheckPrice;
    }

    public String getOffsetPrice() {
        return OffsetPrice;
    }
    public void setOffsetPrice(String OffsetPrice) {
        this.OffsetPrice = OffsetPrice;
    }

    public String getTotalPriceIncludeTax() {
        return TotalPriceIncludeTax;
    }
    public void setTotalPriceIncludeTax(String TotalPriceIncludeTax) {  this.TotalPriceIncludeTax = TotalPriceIncludeTax;   }

    public String getInvoiceNumber() {  return InvoiceNumber;   }
    public void setInvoiceNumber(String InvoiceNumber) {    this.InvoiceNumber = InvoiceNumber;  }

    public String getRemark() {
        return Remark;
    }
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public ObservableList<SearchOrder_Bean> getOrderList() {    return OrderList;   }
    public void setOrderList(ObservableList<SearchOrder_Bean> OrderList) {  this.OrderList = OrderList;  }
}
