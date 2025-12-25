package ERP.Bean.ProductWaitConfirm;

/** [ERP.Bean] Vendor info in waiting confirm */
public class Vendor_Bean {
    private String VendorName;
    private int VendorCode;
    public Vendor_Bean(int VendorCode, String VendorName){
        this.VendorCode = VendorCode;
        this.VendorName = VendorName;
    }
    public String getVendorName(){  return this.VendorName; }
    public int getVendorCode(){ return VendorCode;  }
}
