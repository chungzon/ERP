package ERP.Bean.ManagePayableReceivable;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
/** [ERP.Bean] Manage bank info in manage payable or receivable system */
public class BankInfo_Bean {
    private SimpleObjectProperty<Integer> BankID;
    private SimpleStringProperty BankCode, BankNickName, BankName, ContactPerson1, ContactPerson2, Telephone1, Telephone2, Fax, Address, Remark;

    public BankInfo_Bean(Integer BankID){
        this.BankID = new SimpleObjectProperty<>();
        this.BankID.set(BankID);

        BankCode = new SimpleStringProperty();
        BankNickName = new SimpleStringProperty();
        BankName = new SimpleStringProperty();
        ContactPerson1 = new SimpleStringProperty();
        ContactPerson2 = new SimpleStringProperty();
        Telephone1 = new SimpleStringProperty();
        Telephone2 = new SimpleStringProperty();
        Fax = new SimpleStringProperty();
        Address = new SimpleStringProperty();
        Remark = new SimpleStringProperty();
    }
    public Integer getBankID() {    return BankID.get();    }
    public SimpleObjectProperty<Integer> bankIDProperty() { return BankID;  }
    public void setBankID(Integer bankID) { this.BankID.set(bankID);    }

    public String getBankCode() {   return BankCode.get();  }
    public SimpleStringProperty BankCodeProperty() {    return BankCode;    }
    public void setBankCode(String bankCode) {  this.BankCode.set(bankCode);    }

    public String getBankNickName() {   return BankNickName.get();  }
    public SimpleStringProperty BankNickNameProperty() {    return BankNickName;    }
    public void setBankNickName(String bankNickName) {  this.BankNickName.set(bankNickName);    }

    public String getBankName() {   return BankName.get();  }
    public SimpleStringProperty BankNameProperty() {    return BankName;    }
    public void setBankName(String bankName) {  this.BankName.set(bankName);    }

    public String getContactPerson1() { return ContactPerson1.get();    }
    public SimpleStringProperty ContactPerson1Property() {  return ContactPerson1;  }
    public void setContactPerson1(String contactPerson1) {  this.ContactPerson1.set(contactPerson1);    }

    public String getContactPerson2() { return ContactPerson2.get();    }
    public SimpleStringProperty ContactPerson2Property() {  return ContactPerson2;  }
    public void setContactPerson2(String contactPerson2) {  this.ContactPerson2.set(contactPerson2);    }

    public String getTelephone1() { return Telephone1.get();    }
    public SimpleStringProperty Telephone1Property() {  return Telephone1;  }
    public void setTelephone1(String telephone1) {  this.Telephone1.set(telephone1);    }

    public String getTelephone2() { return Telephone2.get();    }
    public SimpleStringProperty Telephone2Property() {  return Telephone2;  }
    public void setTelephone2(String telephone2) {  this.Telephone2.set(telephone2);    }

    public String getFax() {    return Fax.get();   }
    public SimpleStringProperty FaxProperty() { return Fax; }
    public void setFax(String fax) {    this.Fax.set(fax);  }

    public String getAddress() {    return Address.get();   }
    public SimpleStringProperty AddressProperty() { return Address; }
    public void setAddress(String address) {    this.Address.set(address);  }

    public String getRemark() { return Remark.get();    }
    public SimpleStringProperty RemarkProperty() {  return Remark;  }
    public void setRemark(String remark) {  this.Remark.set(remark);    }
}
