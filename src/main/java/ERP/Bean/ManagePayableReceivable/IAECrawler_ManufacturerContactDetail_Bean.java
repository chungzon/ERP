package ERP.Bean.ManagePayableReceivable;

import ERP.Enum.PayableReceivable.PayableReceivable_Enum.ManufacturerContactDetailSource;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;

public class IAECrawler_ManufacturerContactDetail_Bean {
    private SimpleObjectProperty<Integer> payment_id;
    private Integer order_id,subBill_id;
    private CheckBox selectCheckBox;
    private Button deleteCheckoutButton;
    private MenuButton deleteCheckoutMenuButton;

    private SimpleIntegerProperty serialNumber;
    private SimpleStringProperty orderDate;
    private SimpleObjectProperty<Integer> alreadyInvoiceAmount;
    private SimpleObjectProperty<Integer> noneInvoiceAmount;
    private SimpleStringProperty projectName;
    private SimpleStringProperty invoiceNumber;
    private SimpleStringProperty invoiceType;
    private SimpleObjectProperty<Integer> schoolAlreadyAmount;
    private SimpleObjectProperty<Integer> schoolNoneAmount;
    private SimpleStringProperty cashierRemark;
    private SimpleStringProperty CheckoutStatus;
    private String IAECrawlerAccount_BelongName;
    private Integer remittanceFee;
    private ManufacturerContactDetailSource ManufacturerContactDetailSource;
    private boolean isCheckBoxSelect;
    public IAECrawler_ManufacturerContactDetail_Bean(){
        payment_id = new SimpleObjectProperty<>();
        serialNumber = new SimpleIntegerProperty();
        orderDate = new SimpleStringProperty();
        this.alreadyInvoiceAmount = new SimpleObjectProperty<>();
        noneInvoiceAmount = new SimpleObjectProperty<>();
        projectName = new SimpleStringProperty();
        invoiceNumber = new SimpleStringProperty();
        invoiceType = new SimpleStringProperty();
        schoolAlreadyAmount = new SimpleObjectProperty<>();
        schoolNoneAmount = new SimpleObjectProperty<>();
        cashierRemark = new SimpleStringProperty();
        CheckoutStatus = new SimpleStringProperty();
        isCheckBoxSelect = false;
    }

    public Integer getPayment_id() {    return payment_id.get();    }
    public SimpleObjectProperty<Integer> payment_idProperty() { return payment_id;  }
    public void setPayment_id(Integer payment_id) { this.payment_id.set(payment_id);    }

    public Integer getOrder_id() {  return order_id;    }
    public void setOrder_id(Integer order_id) { this.order_id = order_id;   }

    public Integer getSubBill_id() {    return subBill_id;  }
    public void setSubBill_id(Integer subBill_id) { this.subBill_id = subBill_id;   }

    public boolean isCheckBoxSelect() { return isCheckBoxSelect;    }
    public void setCheckBoxSelect(boolean isCheckBoxSelect) {
        this.isCheckBoxSelect = isCheckBoxSelect;
        selectCheckBox.setSelected(isCheckBoxSelect);
    }
    public CheckBox getSelectCheckBox() {    return selectCheckBox;    }
    public void setSelectCheckBox(CheckBox selectCheckBox) {   this.selectCheckBox = selectCheckBox;    }

    public CheckoutStatus_ManufacturerContactDetail getCheckoutStatus_ManufacturerContactDetail(){
        if(CheckoutStatus.get().equals("已結清"))
            return CheckoutStatus_ManufacturerContactDetail.全部;
        else if(CheckoutStatus.get().equals("已開發票"))
            return CheckoutStatus_ManufacturerContactDetail.已開發票;
        else if(CheckoutStatus.get().equals("已結貨款"))
            return CheckoutStatus_ManufacturerContactDetail.已給付貨款金額;
        else
            return null;
    }
    public SimpleStringProperty checkoutStatusProperty() {    return CheckoutStatus;    }
    public void setCheckoutStatus_ManufacturerContactDetail(CheckoutStatus_ManufacturerContactDetail checkStatus) {
        if(checkStatus == CheckoutStatus_ManufacturerContactDetail.全部)
            this.CheckoutStatus.set("已結清");
        else if(checkStatus == CheckoutStatus_ManufacturerContactDetail.已開發票)
            this.CheckoutStatus.set("已開發票");
        else if(checkStatus == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額)
            this.CheckoutStatus.set("已結貨款");
        else this.CheckoutStatus.set("");
    }

    public Button getDeleteCheckoutButton() {   return deleteCheckoutButton;    }
    public void setDeleteCheckoutButton(Button deleteCheckoutButton) {  this.deleteCheckoutButton = deleteCheckoutButton;   }

    public MenuButton getDeleteCheckoutMenuButton() {   return deleteCheckoutMenuButton;    }
    public void setDeleteCheckoutMenuButton(MenuButton deleteCheckoutMenuButton) {  this.deleteCheckoutMenuButton = deleteCheckoutMenuButton;   }

    public int getSerialNumber() {  return serialNumber.get();  }

    public SimpleIntegerProperty serialNumberProperty() {   return serialNumber;    }

    public void setSerialNumber(int serialNumber) { this.serialNumber.set(serialNumber);    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public SimpleStringProperty orderDateProperty() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate.set(orderDate);
    }

    public Integer getAlreadyInvoiceAmount() {
        return alreadyInvoiceAmount.get();
    }

    public SimpleObjectProperty<Integer> alreadyInvoiceAmountProperty() { return alreadyInvoiceAmount;  }

    public void setAlreadyInvoiceAmount(Integer alreadyInvoiceAmount) { this.alreadyInvoiceAmount.set(alreadyInvoiceAmount);    }

    public Integer getNoneInvoiceAmount() {
        return noneInvoiceAmount.get();
    }

    public SimpleObjectProperty<Integer> noneInvoiceAmountProperty() {
        return noneInvoiceAmount;
    }

    public void setNoneInvoiceAmount(Integer noneInvoiceAmount) {
        this.noneInvoiceAmount.set(noneInvoiceAmount);
    }

    public String getProjectName() {
        return projectName.get();
    }

    public SimpleStringProperty projectNameProperty() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName.set(projectName);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public SimpleStringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public String getInvoiceType() {
        return invoiceType.get();
    }

    public SimpleStringProperty invoiceTypeProperty() {
        return invoiceType;
    }

    public void setInvoiceType(String InvoiceType) {
        this.invoiceType.set(InvoiceType);
    }

    public Integer getSchoolAlreadyAmount() {
        return schoolAlreadyAmount.get();
    }

    public SimpleObjectProperty<Integer> schoolAlreadyAmountProperty() {
        return schoolAlreadyAmount;
    }

    public void setSchoolAlreadyAmount(Integer schoolAlreadyAmount) {
        this.schoolAlreadyAmount.set(schoolAlreadyAmount);
    }

    public Integer getSchoolNoneAmount() {
        return schoolNoneAmount.get();
    }

    public SimpleObjectProperty<Integer> schoolNoneAmountProperty() {
        return schoolNoneAmount;
    }

    public void setSchoolNoneAmount(Integer schoolNoneAmount) {
        this.schoolNoneAmount.set(schoolNoneAmount);
    }

    public String getCashierRemark() {
        return cashierRemark.get();
    }

    public SimpleStringProperty cashierRemarkProperty() {
        return cashierRemark;
    }

    public void setCashierRemark(String cashierRemark) {
        this.cashierRemark.set(cashierRemark);
    }

    public String getIAECrawlerAccount_BelongName() {   return IAECrawlerAccount_BelongName;    }
    public void setIAECrawlerAccount_BelongName(String IAECrawlerAccount_BelongName) {  this.IAECrawlerAccount_BelongName = IAECrawlerAccount_BelongName;   }

    public Integer getRemittanceFee() { return remittanceFee;   }
    public void setRemittanceFee(Integer remittanceFee) {   this.remittanceFee = remittanceFee; }

    public ManufacturerContactDetailSource getManufacturerContactDetailSource() {    return ManufacturerContactDetailSource; }
    public void setManufacturerContactDetailSource(ManufacturerContactDetailSource manufacturerContactDetailSource) {    ManufacturerContactDetailSource = manufacturerContactDetailSource;  }
}
