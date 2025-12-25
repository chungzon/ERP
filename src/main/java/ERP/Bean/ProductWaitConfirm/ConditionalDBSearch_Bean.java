package ERP.Bean.ProductWaitConfirm;

import ERP.Enum.Product.Product_Enum;

/** [ERP.Bean] The conditional search for database in waiting confirm */
public class ConditionalDBSearch_Bean {
    private Product_Enum.WaitConfirmStatus WaitConfirmStatus;
    private Product_Enum.WaitConfirmTable WaitConfirmTable;
    private Vendor_Bean Vendor_Bean;
    private VendorCategory_Bean FirstCategory, SecondCategory, ThirdCategory;
    private String SinglePrice_Minus_BatchPrice, ProductName;
    boolean isPriceIncludeTax;
    boolean isSearchProductNameByOr;

    public Product_Enum.WaitConfirmStatus getWaitConfirmStatus() {   return WaitConfirmStatus;   }
    public void setWaitConfirmStatus(Product_Enum.WaitConfirmStatus waitConfirmStatus) { WaitConfirmStatus = waitConfirmStatus;  }
    public Product_Enum.WaitConfirmTable getWaitConfirmTable() { return WaitConfirmTable;    }
    public void setWaitConfirmTable(Product_Enum.WaitConfirmTable waitConfirmTable) {    WaitConfirmTable = waitConfirmTable;    }
    public Vendor_Bean getVendor() { return Vendor_Bean;  }
    public void setVendor(Vendor_Bean Vendor_Bean) {  this.Vendor_Bean = Vendor_Bean;    }
    public VendorCategory_Bean getFirstCategory() { return FirstCategory;   }
    public void setFirstCategory(VendorCategory_Bean firstCategory) {   FirstCategory = firstCategory;  }
    public VendorCategory_Bean getSecondCategory() {    return SecondCategory;  }
    public void setSecondCategory(VendorCategory_Bean secondCategory) { SecondCategory = secondCategory;    }
    public VendorCategory_Bean getThirdCategory() { return ThirdCategory;   }
    public void setThirdCategory(VendorCategory_Bean thirdCategory) {   ThirdCategory = thirdCategory;  }
    public String getSinglePrice_Minus_BatchPrice() {   return SinglePrice_Minus_BatchPrice;    }
    public void setSinglePrice_Minus_BatchPrice(String singlePrice_Minus_BatchPrice) {  SinglePrice_Minus_BatchPrice = singlePrice_Minus_BatchPrice;    }
    public String getProductName() {    return ProductName; }
    public void setProductName(String productName) {    ProductName = productName;  }
    public boolean isPriceIncludeTax() {    return isPriceIncludeTax;   }
    public void setPriceIncludeTax(boolean priceIncludeTax) {   isPriceIncludeTax = priceIncludeTax;    }
    public boolean isSearchProductNameByOr() {  return isSearchProductNameByOr; }
    public void setSearchProductNameByOr(boolean searchProductNameByOr) {   isSearchProductNameByOr = searchProductNameByOr;    }
}
