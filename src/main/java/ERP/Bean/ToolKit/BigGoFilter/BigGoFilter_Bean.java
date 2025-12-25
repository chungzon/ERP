package ERP.Bean.ToolKit.BigGoFilter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ERP.Enum.Product.Product_Enum.BigGoFilterSource;
public class BigGoFilter_Bean {
    private int id;
    private SimpleIntegerProperty itemNumber;
    private SimpleStringProperty storeName, linkName;
    private boolean choose;
    private BigGoFilterSource bigGoFilterSource;

    public BigGoFilter_Bean(){
        choose = false;
        this.itemNumber = new SimpleIntegerProperty();
        this.storeName = new SimpleStringProperty();
        this.linkName = new SimpleStringProperty();
    }
    public boolean isChoose() {    return choose;    }
    public void setChoose(boolean choose) {   this.choose = choose;    }

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public int getItemNumber() {    return itemNumber.get();    }
    public SimpleIntegerProperty itemNumberProperty() { return itemNumber;  }
    public void setItemNumber(int itemNumber) { this.itemNumber.set(itemNumber);    }
    public String getStoreName() {  return storeName.get(); }
    public SimpleStringProperty StoreNameProperty() {   return storeName;   }
    public void setStoreName(String StoreName) {    this.storeName.set(StoreName);  }
    public String getLinkName() { return linkName.get();    }
    public SimpleStringProperty WebKeyWordProperty() {  return linkName;  }
    public void setLinkName(String WebKeyWord) {  this.linkName.set(WebKeyWord);    }

    public void setSource(BigGoFilterSource BigGoFilterSource){   this.bigGoFilterSource = BigGoFilterSource;  }
    public BigGoFilterSource getSource(){   return bigGoFilterSource; }
}
