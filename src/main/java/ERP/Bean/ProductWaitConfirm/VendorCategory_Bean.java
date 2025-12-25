package ERP.Bean.ProductWaitConfirm;

/** [ERP.Bean] Vendor category in waiting confirm */
public class VendorCategory_Bean {
    private Integer id;
    private String CategoryName, CategoryValue;

    public VendorCategory_Bean(Integer id, String CategoryName, String CategoryCode){
        this.id = id;
        this.CategoryName = CategoryName;
        this.CategoryValue = CategoryCode;
    }

    public Integer getId() {    return id;  }
    public String getCategoryName(){    return CategoryName;    }
    public String getCategoryValue(){    return  CategoryValue;   }
}
