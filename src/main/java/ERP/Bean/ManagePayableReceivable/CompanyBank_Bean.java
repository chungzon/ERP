package ERP.Bean.ManagePayableReceivable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/** [ERP.Bean] Manage company bank in manage payable or receivable system */
public class CompanyBank_Bean {
    private Integer companyBankInfo_id;

    private SimpleIntegerProperty BankID;
    private SimpleStringProperty AccountNickName, BankCode, BankName, BankBranch, BankAccountName, BankAccount, Remark;

    private String BankNickName;
    public CompanyBank_Bean(){
        this.BankID = new SimpleIntegerProperty();
        this.AccountNickName = new SimpleStringProperty();
        this.BankCode = new SimpleStringProperty();
        this.BankName = new SimpleStringProperty();
        this.BankBranch = new SimpleStringProperty();
        this.BankAccountName = new SimpleStringProperty();
        this.BankAccount = new SimpleStringProperty();
        this.Remark = new SimpleStringProperty();
    }

    public Integer getCompanyBankInfo_id() {    return companyBankInfo_id;  }
    public void setCompanyBankInfo_id(Integer companyBankInfo_id) { this.companyBankInfo_id = companyBankInfo_id;   }

    public int getBankID() {    return BankID.get(); }
    public SimpleIntegerProperty BankIDProperty() { return BankID; }
    public void setBankID(int BankID) {    this.BankID.set(BankID);  }

    public String getAccountNickName() {    return AccountNickName.get(); }
    public SimpleStringProperty AccountNickNameProperty() { return AccountNickName; }
    public void setAccountNickName(String AccountNickName) {    this.AccountNickName.set(AccountNickName);  }

    public String getBankCode() {   return BankCode.get();    }
    public SimpleStringProperty BankCodeProperty() {    return BankCode;    }
    public void setBankCode(String BankCode) {  this.BankCode.set(BankCode);    }

    public String getBankNickName() {   return BankNickName;    }
    public void setBankNickName(String BankNickName) {  this.BankNickName = BankNickName;    }


    public String getBankName() {   return BankName.get();    }
    public SimpleStringProperty BankNameProperty() {    return BankName;    }
    public void setBankName(String BankName) {  this.BankName.set(BankName);    }

    public String getBankBranch() { return BankBranch.get();  }
    public SimpleStringProperty BankBranchProperty() {  return BankBranch;  }
    public void setBankBranch(String BankBranch) {  this.BankBranch.set(BankBranch);    }

    public String getBankAccountName() {    return BankAccountName.get(); }
    public SimpleStringProperty BankAccountNameProperty() { return BankAccountName; }
    public void setBankAccountName(String BankAccountName) {    this.BankAccountName.set(BankAccountName);  }

    public String getBankAccount() {    return BankAccount.get(); }
    public SimpleStringProperty BankAccountProperty() { return BankAccount; }
    public void setBankAccount(String BankAccount) {    this.BankAccount.set(BankAccount);  }

    public String getRemark() {    return Remark.get(); }
    public SimpleStringProperty RemarkProperty() { return Remark; }
    public void setRemark(String Remark) {    this.Remark.set(Remark);  }
}
