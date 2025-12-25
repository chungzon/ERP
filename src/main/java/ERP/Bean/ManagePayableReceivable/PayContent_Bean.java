package ERP.Bean.ManagePayableReceivable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class PayContent_Bean {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyMMdd");
    private String accountName;
    private String account;
    private LocalDate payDate;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private double invoiceAmount;
    private double administrativeFee;
    private double amount;
    private String number;
    private String invoiceContent;
    private String invoice;

    public PayContent_Bean() {
    }

    @JsonProperty("ACCNAME")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return this.accountName;
    }

    @JsonProperty("ACCNO")
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("PAYDATE")
    public void setPayDate(String payDate) {
        this.payDate = this.parseTWDate(payDate);
    }

    private LocalDate parseTWDate(String payDate) {
        return LocalDate.parse(payDate, this.dateTimeFormatter).plusYears(1911L);
    }

    public String getInvoice() {
        return this.invoice;
    }

    @JsonProperty("INVS")
    public void setInvoice(String invoice) {
        this.invoice = invoice;
        if (!invoice.equals("") && invoice.contains(":")) {
            String invoiceNumber = invoice.substring(0, invoice.indexOf(":"));
            String invoiceDate = invoice.substring(invoice.indexOf(":") + 1);
            if (!this.isInvoiceFormatError(invoiceNumber)) {
                this.setInvoiceNumber(invoiceNumber);
            } else {
                this.setInvoiceNumber((String)null);
            }

            if (!invoiceDate.equals("") && invoiceDate.length() == 7) {
                this.setInvoiceDate(invoiceDate);
            } else {
                this.setInvoiceDate((LocalDate)null);
            }
        } else {
            this.setInvoiceNumber((String)null);
            this.setInvoiceDate((LocalDate)null);
        }

    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setInvoiceDate(Object invoiceDate) {
        if (invoiceDate instanceof String) {
            this.invoiceDate = this.parseTWDate((String)invoiceDate);
        } else if (invoiceDate instanceof LocalDate) {
            this.invoiceDate = (LocalDate)invoiceDate;
        }

    }

    @JsonProperty("TSAMOUNT")
    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    @JsonProperty("HEXP")
    public void setAdministrativeFee(double administrativeFee) {
        this.administrativeFee = administrativeFee;
    }

    @JsonProperty("NETPAY")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonProperty("TSNO")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("TSMEMO")
    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getAccount() {
        return this.account;
    }

    public LocalDate getPayDate() {
        return this.payDate;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public double getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public double getAdministrativeFee() {
        return this.administrativeFee;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getNumber() {
        return this.number;
    }

    public String getInvoiceContent() {
        return this.invoiceContent;
    }

    private boolean isInvoiceFormatError(String invoiceNumber) {
        if (invoiceNumber.length() != 10) {
            return true;
        } else {
            String invoiceTitle = invoiceNumber.substring(0, 2);
            String invoiceLast = invoiceNumber.substring(2, 10);

            int index;
            for(index = 0; index < invoiceTitle.length(); ++index) {
                if (this.isDigital(String.valueOf(invoiceTitle.charAt(index)))) {
                    return true;
                }
            }

            for(index = 0; index < invoiceLast.length(); ++index) {
                if (!this.isDigital(String.valueOf(invoiceLast.charAt(index)))) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean isDigital(String Value) {
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        return pattern.matcher(Value).matches();
    }
}
