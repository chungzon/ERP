package ERP.Bean.ToolKit.BidCategorySetting;
public class BidCategory_Bean {
    private String Category, CategoryName, CategoryValue;

    public BidCategory_Bean(String Category, String CategoryName, String CategoryValue){
        setCategory(Category);
        setCategoryName(CategoryName);
        setCategoryValue(CategoryValue);
    }
    private void setCategory(String Category){   this.Category = Category;   }
    public String getCategory(){  return Category;    }
    private void setCategoryName(String CategoryName){   this.CategoryName = CategoryName;   }
    public String getCategoryName(){    return CategoryName;    }
    private void setCategoryValue(String CategoryValue){ this.CategoryValue = CategoryValue; }
    public String getCategoryValue(){   return CategoryValue;   }
}
