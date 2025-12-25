package ERP.Bean.ManagePayableReceivable;

import ERP.Enum.Invoice.Invoice_Enum.InvoiceType;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class IAECrawler_ExportPurchaseNote_Bean {

    private SimpleIntegerProperty serialNumber;
    private SimpleStringProperty checkoutStatus;
    private SimpleStringProperty schoolPayDate;
    private SimpleObjectProperty<Integer> schoolPayAmount;

    private SimpleStringProperty manufacturerName;
    private SimpleStringProperty invoiceDate;
    private SimpleStringProperty invoiceType;
    private SimpleIntegerProperty invoiceAmount;
    private SimpleStringProperty invoiceNumber;
    private SimpleStringProperty customerID;
    private SimpleStringProperty orderDate;
    private SimpleStringProperty orderNumber;
    private SimpleStringProperty projectName;
    private SimpleIntegerProperty projectTotalPriceIncludeTax;
    private SimpleStringProperty cashierRemark;

    public IAECrawler_ExportPurchaseNote_Bean(){
        serialNumber = new SimpleIntegerProperty();
        checkoutStatus = new SimpleStringProperty();
        schoolPayDate = new SimpleStringProperty();
        schoolPayAmount = new SimpleObjectProperty<Integer>();
        manufacturerName = new SimpleStringProperty();
        invoiceDate = new SimpleStringProperty();
        invoiceType = new SimpleStringProperty();
        invoiceAmount = new SimpleIntegerProperty();
        invoiceNumber = new SimpleStringProperty();
        customerID = new SimpleStringProperty();
        orderDate = new SimpleStringProperty();
        orderNumber = new SimpleStringProperty();
        projectName = new SimpleStringProperty();
        projectTotalPriceIncludeTax = new SimpleIntegerProperty();
        cashierRemark = new SimpleStringProperty();
    }

    public int getSerialNumber() {
        return serialNumber.get();
    }

    public SimpleIntegerProperty serialNumberProperty() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public String getCheckoutStatus() {
        return checkoutStatus.get();
    }

    public SimpleStringProperty checkoutStatusProperty() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(CheckoutStatus CheckoutStatus) {
        if(CheckoutStatus == Order_Enum.CheckoutStatus.未結帳) this.checkoutStatus.set("否");
        else this.checkoutStatus.set("是");
    }

    public String getSchoolPayDate() {
        return schoolPayDate.get();
    }

    public SimpleStringProperty schoolPayDateProperty() {
        return schoolPayDate;
    }

    public void setSchoolPayDate(String schoolPayDate) {
        this.schoolPayDate.set(schoolPayDate);
    }

    public Integer getSchoolPayAmount() {
        return schoolPayAmount.get();
    }

    public SimpleObjectProperty<Integer> schoolPayAmountProperty() {
        return schoolPayAmount;
    }

    public void setSchoolPayAmount(Integer schoolPayAmount) {
        this.schoolPayAmount.set(schoolPayAmount);
    }

    public String getManufacturerName() {
        return manufacturerName.get();
    }

    public SimpleStringProperty manufacturerNameProperty() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName.set(manufacturerName);
    }

    public String getInvoiceDate() {
        return invoiceDate.get();
    }

    public SimpleStringProperty invoiceDateProperty() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate.set(invoiceDate);
    }

    public String getInvoiceType() {
        return invoiceType.get();
    }

    public SimpleStringProperty invoiceTypeProperty() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType InvoiceType) {
        this.invoiceType.set(InvoiceType.name());
    }

    public int getInvoiceAmount() {
        return invoiceAmount.get();
    }

    public SimpleIntegerProperty invoiceAmountProperty() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(int invoiceAmount) {
        this.invoiceAmount.set(invoiceAmount);
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

    public String getCustomerID() {
        return customerID.get();
    }

    public SimpleStringProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID.set(customerID);
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public SimpleStringProperty orderDateProperty() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate.set(orderDate);
    }

    public String getOrderNumber() {
        return orderNumber.get();
    }

    public SimpleStringProperty orderNumberProperty() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber.set(orderNumber);
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

    public int getProjectTotalPriceIncludeTax() {
        return projectTotalPriceIncludeTax.get();
    }

    public SimpleIntegerProperty projectTotalPriceIncludeTaxProperty() {
        return projectTotalPriceIncludeTax;
    }

    public void setProjectTotalPriceIncludeTax(int projectTotalPriceIncludeTax) {
        this.projectTotalPriceIncludeTax.set(projectTotalPriceIncludeTax);
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
}
