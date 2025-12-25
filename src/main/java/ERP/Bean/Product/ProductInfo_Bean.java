package ERP.Bean.Product;

import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.StoreStatus;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

/** [ERP.Bean] Product info in manage product system */
public class ProductInfo_Bean {
    private CheckBox CheckBox;
    private SimpleStringProperty ISBN, InternationalCode, FirmCode, ProductCode, ProductName, Unit;
    private SimpleStringProperty VendorCode, VendorName;
    private SimpleStringProperty FirstCategory, SecondCategory, ThirdCategory;
    private SimpleStringProperty BatchPrice, SinglePrice, Pricing, VipPrice1, VipPrice2, VipPrice3;
    private SimpleStringProperty InStock, SafetyStock;
    private SimpleDoubleProperty Discount;

    private String Brand;
    private String KeyinDate;
    private SimpleStringProperty InventoryDate, UpdateDate, PurchaseDate, SaleDate, ShipmentDate;
    private String Describe, Remark, SupplyStatus;
    private int InventoryQuantity;
    private String ProductArea, ProductFloor;
    private String YahooCategory, RutenCategory;
    private ShopeeCategory_Bean ShopeeCategory;
    private String Picture1, Picture2, Picture3;
    private Integer vendorFirstCategory_id, vendorSecondCategory_id, vendorThirdCategory_id;
    private boolean isExistProductTag;
    private ObservableList<String> ProductTag;
    private boolean isWaitingOnShelf;
    private StoreStatus StoreStatus;

    public ProductInfo_Bean(){
        this.ISBN = new SimpleStringProperty();
        this.InternationalCode = new SimpleStringProperty();
        this.FirmCode = new SimpleStringProperty();
        this.ProductCode = new SimpleStringProperty();
        this.ProductName = new SimpleStringProperty();
        this.Unit = new SimpleStringProperty();
        this.VendorCode = new SimpleStringProperty();
        this.VendorName = new SimpleStringProperty();
        this.FirstCategory = new SimpleStringProperty();
        this.SecondCategory = new SimpleStringProperty();
        this.ThirdCategory = new SimpleStringProperty();
        this.BatchPrice = new SimpleStringProperty();
        this.SinglePrice = new SimpleStringProperty();
        this.Pricing = new SimpleStringProperty();
        this.VipPrice1 = new SimpleStringProperty();
        this.VipPrice2 = new SimpleStringProperty();
        this.VipPrice3 = new SimpleStringProperty();
        this.InStock = new SimpleStringProperty();
        this.SafetyStock = new SimpleStringProperty();
        this.Discount = new SimpleDoubleProperty();
        this.InventoryDate = new SimpleStringProperty();
        this.UpdateDate = new SimpleStringProperty();
        this.PurchaseDate = new SimpleStringProperty();
        this.SaleDate = new SimpleStringProperty();
        this.ShipmentDate = new SimpleStringProperty();
    }
    public void setCheckBox(CheckBox CheckBox,boolean defaultSelect){
        this.CheckBox = CheckBox;
        this.CheckBox.setSelected(defaultSelect);
    }
    public void setCheckBoxDisable(boolean disable){   this.CheckBox.setDisable(disable);  }
    public boolean isCheckBoxDisable(){   return CheckBox.isDisable();  }
    public void setCheckBoxSelect(Boolean select){  CheckBox.setSelected(select);    }
    public boolean isCheckBoxSelect(){  return CheckBox.isSelected();   }

    public void setISBN(String ISBN) {  this.ISBN.set(ISBN);    }
    public SimpleStringProperty ISBNProperty() {    return ISBN;    }
    public String getISBN() {   return ISBN.get();  }

    public void setInternationalCode(String InternationalCode) {    this.InternationalCode.set(InternationalCode);  }
    public SimpleStringProperty InternationalCodeProperty() {   return InternationalCode;   }
    public String getInternationalCode() {  return InternationalCode.get(); }

    public void setFirmCode(String FirmCode) {  this.FirmCode.set(FirmCode);    }
    public SimpleStringProperty FirmCodeProperty() {    return FirmCode;    }
    public String getFirmCode() {   return FirmCode.get();  }

    public void setProductName(String ProductName) {    this.ProductName.set(ProductName);  }
    public SimpleStringProperty ProductNameProperty() { return ProductName; }
    public String getProductName() {    return ProductName.get();   }

    public void setProductCode(String ProductCode){ this.ProductCode.set(ProductCode); }
    public SimpleStringProperty ProductCodeProperty() { return ProductCode; }
    public String getProductCode(){ return ProductCode.get(); }

    public void setUnit(String Unit) {  this.Unit.set(Unit);    }
    public SimpleStringProperty UnitProperty() {    return Unit;    }
    public String getUnit() {   return Unit.get();  }

    public void setVendorCode(String VendorCode) {  this.VendorCode.set(VendorCode);    }
    public SimpleStringProperty VendorCodeProperty() {  return VendorCode;  }
    public String getVendorCode() { return VendorCode.get();    }

    public void setVendorName(String VendorName){   this.VendorName.set(VendorName);    }
    public SimpleStringProperty setVendorNameProperty() {  return VendorName;  }
    public String getVendorName(){  return VendorName.get();  }

    public void setFirstCategory(String FirstCategory){ this.FirstCategory.set(FirstCategory); }
    public SimpleStringProperty setFirstCategoryProperty() {  return FirstCategory;  }
    public String getFirstCategory(){   return FirstCategory.get();   }

    public void setSecondCategory(String SecondCategory){   this.SecondCategory.set(SecondCategory);   }
    public SimpleStringProperty setSecondCategoryProperty() {  return SecondCategory;  }
    public String getSecondCategory(){  return SecondCategory.get();  }

    public void setThirdCategory(String ThirdCategory){ this.ThirdCategory.set(ThirdCategory); }
    public SimpleStringProperty setThirdCategoryProperty() {  return ThirdCategory;  }
    public String getThirdCategory(){   return ThirdCategory.get();   }

    public void setBatchPrice(String BatchPrice) {  this.BatchPrice.set(BatchPrice);    }
    public SimpleStringProperty BatchPriceProperty() {  return BatchPrice;  }
    public String getBatchPrice() { return BatchPrice.get();    }

    public void setSinglePrice(String SinglePrice) {    this.SinglePrice.set(SinglePrice);  }
    public SimpleStringProperty SinglePriceProperty() { return SinglePrice; }
    public String getSinglePrice() {    return SinglePrice.get();   }

    public void setPricing(String Pricing) {    this.Pricing.set(Pricing);  }
    public SimpleStringProperty PricingProperty() { return Pricing; }
    public String getPricing() {    return Pricing.get();   }

    public void setVipPrice1(String VipPrice1){ this.VipPrice1.set(VipPrice1); }
    public SimpleStringProperty setVipPrice1Property() {  return VipPrice1;  }
    public String getVipPrice1(){   return VipPrice1.get(); }

    public void setVipPrice2(String VipPrice2){ this.VipPrice2.set(VipPrice2); }
    public SimpleStringProperty setVipPrice2Property() {  return VipPrice2;  }
    public String getVipPrice2(){   return VipPrice2.get(); }

    public void setVipPrice3(String VipPrice3){ this.VipPrice3.set(VipPrice3); }
    public SimpleStringProperty setVipPrice3Property() {  return VipPrice3;  }
    public String getVipPrice3(){   return VipPrice3.get(); }

    public void setInStock(String InStock){    this.InStock.set(InStock); }
    public SimpleStringProperty setInStockProperty() {  return InStock;  }
    public String getInStock(){    return InStock.get(); }

    public void setSafetyStock(String SafetyStock){    this.SafetyStock.set(SafetyStock); }
    public SimpleStringProperty setSafetyStockProperty() {  return SafetyStock;  }
    public String getSafetyStock(){    return SafetyStock.get(); }

    public void setDiscount(Double Discount){    this.Discount.set(Discount);   }
    public SimpleDoubleProperty setDiscountProperty() {  return Discount;  }
    public Double getDiscount(){ return Discount.get();    }

    public void setBrand(String Brand){ this.Brand = Brand; }
    public String getBrand(){  return Brand;   }

    public void setInventoryDate(String InventoryDate){   this.InventoryDate.set(InventoryDate);    }
    public SimpleStringProperty InventoryDateProperty() {   return InventoryDate;   }
    public String getInventoryDate(){    return InventoryDate.get();    }

    public void setKeyinDate(String KeyinDate){ this.KeyinDate = KeyinDate; }
    public String getKeyinDate(){   return KeyinDate;   }

    public void setUpdateDate(String UpdateDate){   this.UpdateDate.set(UpdateDate);   }
    public SimpleStringProperty UpdateDateProperty() {  return UpdateDate;  }
    public String getUpdateDate(){    return UpdateDate.get();    }

    public void setPurchaseDate(String PurchaseDate){   this.PurchaseDate.set(PurchaseDate);   }
    public SimpleStringProperty PurchaseDateProperty() {    return PurchaseDate;    }
    public String getPurchaseDate(){    return PurchaseDate.get();    }

    public void setSaleDate(String SaleDate){   this.SaleDate.set(SaleDate);   }
    public SimpleStringProperty SaleDateProperty() {    return SaleDate;    }
    public String getSaleDate(){    return SaleDate.get();    }

    public void setShipmentDate(String ShipmentDate){   this.ShipmentDate.set(ShipmentDate);   }
    public SimpleStringProperty ShipmentDateProperty() {    return ShipmentDate;    }
    public String getShipmentDate(){    return ShipmentDate.get();    }

    public void setDescribe(String Describe){   this.Describe = Describe;   }
    public String getDescribe(){    return Describe;    }

    public void setRemark(String Remark){   this.Remark = Remark;   }
    public String getRemark(){  return Remark;  }

    public void setSupplyStatus(String SupplyStatus){  this.SupplyStatus = SupplyStatus;   }
    public String getSupplyStatus(){  return SupplyStatus;    }

    public void setInventoryQuantity(int InventoryQuantity){ this.InventoryQuantity = InventoryQuantity; }
    public int getInventoryQuantity(){  return InventoryQuantity;}

    public void setProductArea(String ProductArea){ this.ProductArea = ProductArea; }
    public String getProductArea(){ return ProductArea; }
    public void setProductFloor(String ProductFloor){   this.ProductFloor = ProductFloor;   }
    public String getProductFloor(){    return ProductFloor;    }

    public String getYahooCategory() {  return YahooCategory;   }
    public void setYahooCategory(String YahooCategory) {    this.YahooCategory = YahooCategory;  }
    public String getRutenCategory() {  return RutenCategory;   }
    public void setRutenCategory(String RutenCategory) {    this.RutenCategory = RutenCategory;  }

    public ShopeeCategory_Bean getShopeeCategory() {    return ShopeeCategory;  }
    public void setShopeeCategory(ShopeeCategory_Bean shopeeCategory) { ShopeeCategory = shopeeCategory;    }

    public void setPicture1(String Picture1){   this.Picture1 = Picture1;   }
    public String getPicture1(){  return  Picture1;   }
    public void setPicture2(String Picture2){   this.Picture2 = Picture2;   }
    public String getPicture2(){  return  Picture2;   }
    public void setPicture3(String Picture3){   this.Picture3 = Picture3;   }
    public String getPicture3(){  return  Picture3;   }

    public Integer getVendorFirstCategory_id() {    return vendorFirstCategory_id; }
    public void setVendorFirstCategory_id(Integer vendorFirstCategory_id) {    this.vendorFirstCategory_id = vendorFirstCategory_id; }
    public Integer getVendorSecondCategory_id() {   return vendorSecondCategory_id;    }
    public void setVendorSecondCategory_id(Integer vendorSecondCategory_id) {  this.vendorSecondCategory_id = vendorSecondCategory_id;   }
    public Integer getVendorThirdCategory_id() {    return vendorThirdCategory_id; }
    public void setVendorThirdCategory_id(Integer vendorThirdCategory_id) {    this.vendorThirdCategory_id = vendorThirdCategory_id; }

    public boolean isExistProductTag() {    return isExistProductTag;   }
    public void setExistProductTag(boolean existProductTag) {   isExistProductTag = existProductTag;    }
    public void setProductTag(ObservableList<String> ProductTag){   this.ProductTag = ProductTag;   }
    public ObservableList<String> getProductTag(){  return ProductTag;  }

    public boolean isWaitingOnShelf() { return isWaitingOnShelf;    }
    public void setWaitingOnShelf(boolean waitingOnShelf) { isWaitingOnShelf = waitingOnShelf;  }

    public Product_Enum.StoreStatus getStoreStatus() {
        return StoreStatus;
    }
    public void setStoreStatus(Product_Enum.StoreStatus storeStatus) {
        StoreStatus = storeStatus;
    }
}
