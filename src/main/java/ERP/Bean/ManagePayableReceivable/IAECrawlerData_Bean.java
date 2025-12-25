package ERP.Bean.ManagePayableReceivable;

import ERP.ERPApplication;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerSource;
import ERP.View.DialogUI;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerReviewStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class IAECrawlerData_Bean{
    private int rankey;
    private int id;
    private SimpleIntegerProperty serialNumber;
    private SimpleStringProperty belongName;
    private SimpleStringProperty summonsNumber;
    private SimpleStringProperty objectID, objectName, objectTaxID;
    private LocalDate payDate;
    private SimpleStringProperty payAmount;
    private SimpleIntegerProperty remittanceFee;
    private SimpleStringProperty bankAccount;
    private SimpleStringProperty invoiceContent;
    private LocalDate invoiceDate;
    private SimpleStringProperty invoiceNumber;
    private SimpleStringProperty invoiceAmount;
    private SimpleStringProperty projectCode;

    private SimpleStringProperty source;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyMMdd");
    private String accountName;
    private String invoice;

    private IAECrawlerStatus IAECrawlerStatus;
    private SimpleStringProperty status;

    private IAECrawlerReviewStatus IAECrawlerReviewStatus;
    private SimpleStringProperty isReview;

    private boolean isBindingOrder;
    private boolean isCheckBoxSelect;
    private String invoiceManufacturerNickName;
    private Integer invoice_manufacturerNickName_id;
    public IAECrawlerData_Bean(){
        isCheckBoxSelect = false;
        serialNumber = new SimpleIntegerProperty();
        belongName = new SimpleStringProperty();
        objectID = new SimpleStringProperty();
        objectName = new SimpleStringProperty();
        objectTaxID = new SimpleStringProperty();
        bankAccount = new SimpleStringProperty();
        invoiceNumber = new SimpleStringProperty();
        invoiceAmount = new SimpleStringProperty();
        remittanceFee = new SimpleIntegerProperty();
        payAmount = new SimpleStringProperty();
        summonsNumber = new SimpleStringProperty();
        invoiceContent = new SimpleStringProperty();
        projectCode = new SimpleStringProperty();
        status = new SimpleStringProperty();
        isReview = new SimpleStringProperty();

        source = new SimpleStringProperty();
    }

    public int getRankey() {    return rankey;  }
    @JsonProperty("RANKEY")
    public void setRankey(int rankey) { this.rankey = rankey;   }

    public boolean isCheckBoxSelect() {    return isCheckBoxSelect;    }
    public void setCheckBoxSelect(boolean isCheckBoxSelect) {   this.isCheckBoxSelect = isCheckBoxSelect;    }

    public boolean isBindingOrder() {   return isBindingOrder;  }
    public void setIsBindingOrder(boolean bindingOrder) { isBindingOrder = bindingOrder;  }

    public void setId(int id){  this.id = id;   }
    public int getId(){ return this.id; }

    public int getSerialNumber() {  return serialNumber.get();  }
    public SimpleIntegerProperty serialNumberProperty() {   return serialNumber;    }
    public void setSerialNumber(int serialNumber) { this.serialNumber.set(serialNumber);    }

    public String getBelongName() { return belongName.get();    }
    public SimpleStringProperty belongNameProperty() {  return belongName;  }
    public void setBelongName(String belongName) {  this.belongName.set(belongName);    }

    public String getObjectID() {   return objectID.get();  }
    public SimpleStringProperty objectIDProperty() {    return objectID;    }
    public void setObjectID(String objectID) {  this.objectID.set(objectID);    }

    public String getObjectName() { return objectName.get();    }
    public SimpleStringProperty objectNameProperty() {  return objectName;  }
    public void setObjectName(String objectName) {  this.objectName.set(objectName);    }

    public String getObjectTaxID() {    return objectTaxID.get();   }
    public SimpleStringProperty objectTaxIDProperty() { return objectTaxID; }
    public void setObjectTaxID(String objectTaxID) {    this.objectTaxID.set(objectTaxID);  }

    @JsonProperty("ACCNO")
    @JsonAlias("PEACCNO")
    public void setBankAccount(String bankAccount) {    this.bankAccount.set(bankAccount);  }
    public SimpleStringProperty bankAccountProperty() { return bankAccount; }
    public String getBankAccount() {    return this.bankAccount.get();    }

    public String getInvoice() {    return this.invoice;    }
    @JsonProperty("INVS")
    public void setInvoice(String invoice) {
        this.invoice = invoice;
        try{
            if(invoice.equals("") || !invoice.contains(":")){
                this.setInvoiceNumber(null);
                this.setInvoiceDate((LocalDate) null);
            }else {
                String invoiceNumber = invoice.substring(0,invoice.indexOf(":"));
                String invoiceDate = invoice.substring(invoice.indexOf(":")+1);
                if(!isInvoiceFormatError(invoiceNumber))    this.setInvoiceNumber(invoiceNumber);
                else    this.setInvoiceNumber(null);
                if(!invoiceDate.equals("") && invoiceDate.length() == 7)   this.setInvoiceDate(invoiceDate);
                else    this.setInvoiceDate((LocalDate)null);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }

    @JsonProperty("INVOICENO")
    public void setInvoiceNumber(String invoiceNumber) {    this.invoiceNumber.set(invoiceNumber);  }
    public SimpleStringProperty invoiceNumberProperty() {   return invoiceNumber;   }
    public String getInvoiceNumber() {  return this.invoiceNumber.get();  }

    @JsonProperty("IDATE")
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = this.parseTWDate(invoiceDate); }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public LocalDate getInvoiceDate() { return this.invoiceDate;    }


    @JsonProperty("HEXP")
    @JsonAlias("ATMFEE")
    public void setRemittanceFee(int remittanceFee) {    this.remittanceFee.set(remittanceFee);  }
    public SimpleIntegerProperty remittanceFeeProperty() {   return remittanceFee;   }
    public int getRemittanceFee() {  return this.remittanceFee.get();    }

    @JsonProperty("TSAMOUNT")
    public void setInvoiceAmount(String invoiceAmount) {    this.invoiceAmount.set(invoiceAmount);  }
    public SimpleStringProperty invoiceAmountProperty() {   return invoiceAmount;   }
    public String getInvoiceAmount() {  return this.invoiceAmount.get();  }

    @JsonProperty("NETPAY")
    @JsonAlias("AMOUNT")
    public void setPayAmount(String payAmount) {    this.payAmount.set(RoundingString(payAmount));  }
    public SimpleStringProperty payAmountProperty() {   return payAmount;   }
    public String getPayAmount() {  return this.payAmount.get();  }

    @JsonProperty("TSNO")
    @JsonAlias("VOUCH")
    public void setSummonsNumber(String summonsNumber) {    this.summonsNumber.set(summonsNumber); }
    public SimpleStringProperty summonsNumberProperty() {   return summonsNumber;   }
    public String getSummonsNumber() {  return this.summonsNumber.get();    }

    @JsonProperty("TSMEMO")
    @JsonAlias("abstract")
    public void setInvoiceContent(String invoiceContent) {  this.invoiceContent.set(invoiceContent);   }
    public SimpleStringProperty invoiceContentProperty() {  return invoiceContent;  }
    public String getInvoiceContent() { return this.invoiceContent.get(); }

    @JsonProperty("BUGETNO")
    @JsonAlias("APYNO")
    public void setProjectCode(String projectCode){ this.projectCode.set(projectCode); }
    public SimpleStringProperty projectCodeProperty() { return projectCode; }
    public String getProjectCode(){ return this.projectCode.get();    }

    @JsonProperty("ACCNAME")
    public void setAccountName(String accountName) {    this.accountName = accountName; }
    public String getAccountName() {    return this.accountName;    }

    @JsonProperty("PAYDATE")
    @JsonAlias("WDATE")
    public void setPayDate(String payDate) {    this.payDate = this.parseTWDate(payDate);   }
    public LocalDate getPayDate() { return this.payDate;    }


    private LocalDate parseTWDate(String payDate) {
        if(payDate.length() == 7)
            return LocalDate.parse(payDate, this.dateTimeFormatter).plusYears(1911L);
        else
            return LocalDate.parse(payDate);
    }

    public String getStatus() { return status.get();    }
    public SimpleStringProperty statusProperty() {  return status;  }
    private void setStatus(String status) { this.status.set(status);    }

    public void setIAECrawlerStatus(IAECrawlerStatus IAECrawlerStatus){
        this.IAECrawlerStatus = IAECrawlerStatus;
        setStatus(IAECrawlerStatus.name());
    }
    public IAECrawlerStatus getIAECrawlerStatus(){  return IAECrawlerStatus;    }

    public boolean isReview(){  return this.isReview.get().equals("是");  }
    public String getIsReview() {   return isReview.get();  }
    public SimpleStringProperty isReviewProperty() {    return isReview;    }
    private void setIsReview(boolean isReview) {
        if(isReview)    this.isReview.set("是");
        else    this.isReview.set("");
    }

    public IAECrawlerSource getSource() {   return PayableReceivable_Enum.IAECrawlerSource.valueOf(source.get());  }
    public SimpleStringProperty sourceProperty() {    return source;    }
    public void setSource(IAECrawlerSource source) {  this.source.set(source.name());    }

    public void setIAECrawlerReviewStatus(IAECrawlerReviewStatus IAECrawlerReviewStatus){
        this.IAECrawlerReviewStatus = IAECrawlerReviewStatus;
        setIsReview(IAECrawlerReviewStatus.isReview());
    }
    public IAECrawlerReviewStatus getIAECrawlerReviewStatus(){  return this.IAECrawlerReviewStatus; }

    public Integer getInvoice_manufacturerNickName_id() {   return invoice_manufacturerNickName_id; }
    public void setInvoice_manufacturerNickName_id(Integer invoice_manufacturerNickName_id) {   this.invoice_manufacturerNickName_id = invoice_manufacturerNickName_id; }

    public String getInvoiceManufacturerNickName() {    return invoiceManufacturerNickName; }
    public void setInvoiceManufacturerNickName(String invoiceManufacturerNickName) {    this.invoiceManufacturerNickName = invoiceManufacturerNickName; }

    private String RoundingString(String stringMath){
        if(stringMath.contains("-")){
            stringMath = String.valueOf(java.lang.Math.round(Double.parseDouble(stringMath)));
        }else if(stringMath.contains(".")){
            if(stringMath.contains(".0")) stringMath = stringMath.substring(0, stringMath.indexOf("."));
            else    stringMath = String.valueOf((int) (Double.parseDouble(stringMath) + 0.5));
        }
        return stringMath;
    }
    private boolean isInvoiceFormatError(String invoiceNumber){
        if(invoiceNumber.length() != 10)
            return true;
        String invoiceTitle = invoiceNumber.substring(0,2);
        String invoiceLast = invoiceNumber.substring(2,10);
        for(int index = 0 ; index < invoiceTitle.length() ; index++){
            if(isDigital(String.valueOf(invoiceTitle.charAt(index))))
                return true;
        }
        for(int index = 0 ; index < invoiceLast.length() ; index++){
            if(!isDigital(String.valueOf(invoiceLast.charAt(index))))
                return true;
        }
        return false;
    }
    private boolean isDigital(String Value){
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        return pattern.matcher(Value).matches();
    }
}

