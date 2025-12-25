package ERP.Bean.SystemSetting;

import javafx.beans.property.SimpleStringProperty;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ProductLevel;
/** [ERP.Bean] Product bookcase in system setting */
public class ProductBookCase_Bean {
    private SimpleStringProperty ProductArea,ProductFloor;
    private ProductLevel ProductLevel;
    public ProductBookCase_Bean(){
        this.ProductArea = new SimpleStringProperty();
        this.ProductFloor = new SimpleStringProperty();
    }

    public String getProductArea() { return ProductArea.get();    }
    public SimpleStringProperty ProductAreaProperty() {  return ProductArea;  }
    public void setProductArea(String ProductArea) {  this.ProductArea.set(ProductArea);    }

    public String getProductFloor() {   return ProductFloor.get();  }
    public SimpleStringProperty ProductFloorProperty() {    return ProductFloor;    }
    public void setProductFloor(String ProductFloor) {  this.ProductFloor.set(ProductFloor);    }

    public void setProductLevel(ProductLevel ProductLevel){    this.ProductLevel = ProductLevel; }
    public ProductLevel getProductLevel(){  return ProductLevel;    }
}
