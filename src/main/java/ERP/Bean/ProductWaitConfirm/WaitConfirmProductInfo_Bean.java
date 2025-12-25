package ERP.Bean.ProductWaitConfirm;

import ERP.Enum.Product.Product_Enum.WaitConfirmTable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/** [ERP.Bean] Product info in waiting confirm */
public class WaitConfirmProductInfo_Bean {
    private SimpleStringProperty ISBN, ProductName, NewFirstCategory, Vendor, KeyinDate, UpdateDate, ProductCode, SupplyStatus;
    private SimpleDoubleProperty Pricing, SinglePrice, BatchPrice, VipPrice1, VipPrice2, VipPrice3;

    private int PreviousStatus;
    private WaitConfirmTable WaitConfirmTable;

    private int VendorCode;
    private Integer FirstCategory_Id, SecondCategory_Id, ThirdCategory_Id;
    private String Unit, Remark, Describe, Brand, Picture1, Picture2, Picture3;

    private float StorePricing, StoreSinglePrice, StoreBatchPrice; //  Checked = 更新
    public WaitConfirmProductInfo_Bean(){
        this.Vendor = new SimpleStringProperty();
        this.KeyinDate = new SimpleStringProperty();
        this.UpdateDate = new SimpleStringProperty();
        this.ProductCode = new SimpleStringProperty();
        this.ISBN = new SimpleStringProperty();
        this.ProductName = new SimpleStringProperty();
        this.Pricing = new SimpleDoubleProperty();
        this.SinglePrice = new SimpleDoubleProperty();
        this.BatchPrice = new SimpleDoubleProperty();
        this.VipPrice1 = new SimpleDoubleProperty();
        this.VipPrice2 = new SimpleDoubleProperty();
        this.VipPrice3 = new SimpleDoubleProperty();
        this.NewFirstCategory = new SimpleStringProperty();
        this.SupplyStatus = new SimpleStringProperty();
    }
    public void setISBN(String ISBN){ this.ISBN.set(ISBN);  }
    public SimpleStringProperty ISBNProperty() {  return ISBN;  }
    public String getISBN(){ return ISBN.get(); }

    public void setProductName(String ProductName){ this.ProductName.set(ProductName);  }
    public SimpleStringProperty ProductNameProperty() {  return ProductName;  }
    public String getProductName(){ return ProductName.get(); }

    public void setPricing(double Pricing){  this.Pricing.set(Pricing); }                                           //  定價
    public SimpleDoubleProperty PricingProperty() {  return Pricing;  }
    public double getPricing(){  return Pricing.get(); }

    public void setSinglePrice(double SinglePrice){   this.SinglePrice.set(SinglePrice); }                          //  單價
    public SimpleDoubleProperty SinglePriceProperty() {  return SinglePrice;  }
    public double getSinglePrice(){  return SinglePrice.get(); }

    public void setNewFirstCategory(String NewFirstCategory){ this.NewFirstCategory.set(NewFirstCategory); }                   //  總類
    public SimpleStringProperty NewFirstCategoryProperty() {  return NewFirstCategory;  }
    public String getNewFirstCategory(){   return NewFirstCategory.get();   }

    public void setVendor(String Vendor){   this.Vendor.set(Vendor);   }
    public SimpleStringProperty VendorProperty() {  return Vendor;  }
    public String getVendor(){  return Vendor.get();  }

    public void setVendorCode(int VendorCode){  this.VendorCode = VendorCode;   }
    public int getVendorCode(){ return VendorCode;  }

    public void setSupplyStatus(String SupplyStatus){   this.SupplyStatus.set(SupplyStatus);   }
    public SimpleStringProperty SupplyStatusProperty() {  return SupplyStatus;  }
    public String getSupplyStatus(){    return SupplyStatus.get();    }

    public void setKeyInDate(String KeyInDate){ this.KeyinDate.set(KeyInDate); }
    public SimpleStringProperty KeyinDateProperty() {  return KeyinDate;  }
    public String getKeyInDate(){   return KeyinDate.get();   }

    public String getUpdateDate() { return UpdateDate.get();    }
    public SimpleStringProperty UpdateDateProperty() {  return UpdateDate;  }
    public void setUpdateDate(String updateDate) {  this.UpdateDate.set(updateDate);    }

    public void setBatchPrice(double BatchPrice){    this.BatchPrice.set(BatchPrice);   }
    public SimpleDoubleProperty BatchPriceProperty() {  return BatchPrice;  }
    public double getBatchPrice(){   return BatchPrice.get();  }

    public void setVipPrice1(double VipPrice1){  this.VipPrice1.set(VipPrice1); }
    public SimpleDoubleProperty VipPrice1Property() {  return VipPrice1;  }
    public double getVipPrice1(){    return VipPrice1.get();   }

    public void setVipPrice2(double VipPrice2){  this.VipPrice2.set(VipPrice2); }
    public SimpleDoubleProperty VipPrice2Property() {  return VipPrice2;  }
    public double getVipPrice2(){    return VipPrice2.get();   }

    public void setVipPrice3(double VipPrice3){  this.VipPrice3.set(VipPrice3); }
    public SimpleDoubleProperty VipPrice3Property() {  return VipPrice3;  }
    public double getVipPrice3(){    return VipPrice3.get();   }

    public void setProductCode(String ProductCode){ this.ProductCode.set(ProductCode); }
    public SimpleStringProperty ProductCodeProperty() {  return ProductCode;  }
    public String getProductCode(){ return ProductCode.get(); }

    public void setUnit(String Unit){   this.Unit = Unit;   }
    public String getUnit(){    return Unit;    }
    public void setPreviousStatus(int PreviousStatus){   this.PreviousStatus = PreviousStatus;   }
    public int getPreviousStatus(){    return PreviousStatus;    }
    public void setRemark(String Remark){  this.Remark = Remark;   }
    public String getRemark(){  return Remark;  }
    public void setDescribe(String Describe){   this.Describe = Describe;   }
    public String getDescribe(){    return Describe;    }
    public void setBrand(String Brand){ this.Brand = Brand; }
    public String getBrand(){   return Brand;   }

    public Integer getFirstCategory_Id() {  return FirstCategory_Id;    }
    public void setFirstCategory_Id(Integer firstCategory_Id) { FirstCategory_Id = firstCategory_Id;    }
    public Integer getSecondCategory_Id() { return SecondCategory_Id;   }
    public void setSecondCategory_Id(Integer secondCategory_Id) {   SecondCategory_Id = secondCategory_Id;  }
    public Integer getThirdCategory_Id() {  return ThirdCategory_Id;    }
    public void setThirdCategory_Id(Integer thirdCategory_Id) { ThirdCategory_Id = thirdCategory_Id;    }

    public void setPicture1(String Picture1){   this.Picture1 = Picture1;   }
    public String getPicture1(){    return Picture1;  }
    public void setPicture2(String Picture2){   this.Picture2 = Picture2;   }
    public String getPicture2(){    return Picture2;  }
    public void setPicture3(String Picture3){   this.Picture3 = Picture3;   }
    public String getPicture3(){    return Picture3;  }

    public void setWaitConfirmTable(WaitConfirmTable WaitConfirmTable){ this.WaitConfirmTable = WaitConfirmTable;   }
    public WaitConfirmTable getWaitConfirmTable(){  return WaitConfirmTable;    }

    //  ProductWaitConfirm Checked = 更新 : Column [Store].[定價、單價、成本價]
    public void setStorePricing(float StorePricing){    this.StorePricing = StorePricing;   }
    public Float getStorePricing(){ return StorePricing;    }
    public void setStoreSinglePrice(float StoreSinglePrice){    this.StoreSinglePrice = StoreSinglePrice;   }
    public Float getStoreSinglePrice(){ return StoreSinglePrice;    }
    public void setStoreBatchPrice(float StoreBatchPrice){  this.StoreBatchPrice = StoreBatchPrice; }
    public Float getStoreBatchPrice(){  return StoreBatchPrice; }
}
