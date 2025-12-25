package ERP.Bean.ManageProductCategory;

import ERP.Enum.Product.Product_Enum;
import javafx.beans.property.SimpleStringProperty;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
/** [ERP.Bean] Manage product category in manage product system */
public class ProductCategory_Bean {
    private SimpleStringProperty CategoryID, CategoryName, CategoryLayer, InStock;

    public ProductCategory_Bean(){
        this.CategoryID = new SimpleStringProperty();
        this.CategoryName = new SimpleStringProperty();
        this.CategoryLayer = new SimpleStringProperty();
        this.InStock = new SimpleStringProperty();
    }
    public void setCategoryID(String CategoryID) {  this.CategoryID.set(CategoryID);    }
    public SimpleStringProperty CategoryIDProperty() {    return CategoryID;    }
    public String getCategoryID() {   return CategoryID.get();  }

    public void setCategoryName(String CategoryName) {    this.CategoryName.set(CategoryName);  }
    public SimpleStringProperty CategoryNameProperty() {   return CategoryName;   }
    public String getCategoryName() {  return CategoryName.get(); }

    public void setInStock(int InStock) {  this.InStock.set(String.valueOf(InStock));    }
    public SimpleStringProperty InStockProperty() {    return InStock;    }
    public String getInStock() {   return InStock.get();  }

    public void setCategoryLayer(CategoryLayer CategoryLayer) {  this.CategoryLayer.set(CategoryLayer.name());    }
    public SimpleStringProperty CategoryLayerProperty() {    return CategoryLayer;    }
    public CategoryLayer getCategoryLayer() {   return Product_Enum.CategoryLayer.valueOf(CategoryLayer.get());  }
}
