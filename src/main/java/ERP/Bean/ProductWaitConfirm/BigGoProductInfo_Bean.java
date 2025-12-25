package ERP.Bean.ProductWaitConfirm;

import javafx.scene.image.Image;

/** [ERP.Bean] BigGo product info in waiting confirm */
public class BigGoProductInfo_Bean {
    private Image ProductPicture;
    private String ProductPictureBase64;
    private String ProductName, ProductPrice, StoreName, StoreHyperlink;

    public void setProductName(String ProductName){ this.ProductName = ProductName; }
    public String getProductName(){ return ProductName; }
    public void setProductPrice(String ProductPrice){   this.ProductPrice = ProductPrice;   }
    public String getProductPrice(){   return ProductPrice;   }
    public String getProductPictureBase64() {   return ProductPictureBase64;    }
    public void setProductPictureBase64(String productPictureBase64) {  ProductPictureBase64 = productPictureBase64;    }
    public void setProductPicture(Image ProductPicture){   this.ProductPicture = ProductPicture;   }
    public Image getProductPicture(){    return ProductPicture; }
    public void setStoreName(String StoreName){ this.StoreName = StoreName; }
    public String getStoreName(){   return StoreName;   }
    public void setStoreHyperlink(String StoreHyperlink){    this.StoreHyperlink = StoreHyperlink;   }
    public String getStoreHyperlink(){  return StoreHyperlink;  }
}
