package ERP.Bean.SystemSetting;

import javafx.beans.property.SimpleStringProperty;

/** [ERP.Bean] Customer character in system setting */
public class CustomerIDCharacter_Bean {
    private SimpleStringProperty CustomerLocation,CustomerIDCharacter;

    public CustomerIDCharacter_Bean(){
        this.CustomerLocation = new SimpleStringProperty();
        this.CustomerIDCharacter = new SimpleStringProperty();
    }

    public String getCustomerIDCharacter() { return CustomerIDCharacter.get();    }
    public SimpleStringProperty CustomerIDCharacterProperty() {  return CustomerIDCharacter;  }
    public void setCustomerIDCharacter(String CustomerIDCharacter) {  this.CustomerIDCharacter.set(CustomerIDCharacter);    }

    public String getCustomerLocation() {   return CustomerLocation.get();  }
    public SimpleStringProperty customerLocationProperty() {    return CustomerLocation;    }
    public void setCustomerLocation(String customerLocation) {  this.CustomerLocation.set(customerLocation);    }
}
