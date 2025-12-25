package ERP.Bean.Product;

import java.util.HashMap;

public class ShopeeCategory_Bean {
    private Integer id,parentId;
    private String categoryName, categoryTreeName;
    private HashMap<Integer,ShopeeCategory_Bean> childCategoryMap;

    public Integer getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public int getParentId() {  return parentId;    }
    public void setParentId(int parentId) { this.parentId = parentId;   }

    public HashMap<Integer,ShopeeCategory_Bean> getChildCategoryBean() {
        if(childCategoryMap == null)   childCategoryMap = new HashMap<>();
        return childCategoryMap;
    }
    public void setChildCategoryBean(HashMap<Integer,ShopeeCategory_Bean> childCategoryBean) { this.childCategoryMap = childCategoryBean; }

    public String getCategoryName() {   return categoryName;    }
    public void setCategoryName(String categoryName) {  this.categoryName = categoryName;   }

    public String getCategoryTreeName() {   return categoryTreeName;    }
    public void setCategoryTreeName(String categoryTreeName) {  this.categoryTreeName = categoryTreeName;   }
}
