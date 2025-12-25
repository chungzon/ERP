package ERP.Model.Product;

import ERP.Bean.Product.ShopeeCategory_Bean;
import ERP.Bean.ToolKit.BidCategorySetting.BidCategory_Bean;
import ERP.Enum.Product.Product_Enum;
import ERP.ERPApplication;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import javafx.application.Platform;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class BidCategorySetting_Model {

    public ArrayList<BidCategory_Bean> getRutenCategory(String Category, int Layer){
        return getBidCategory(Product_Enum.BidStore.露天.getTableName(), Category, Layer);
    }
    private ArrayList<BidCategory_Bean> getBidCategory(String TableName, String Category, int Layer){
        ArrayList<BidCategory_Bean> CategoryList = new ArrayList<>();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from " + TableName + " where category = '" + Category + "' and Layer = '" + Layer + "' order by value");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                BidCategory_Bean BidCategory_Bean = new BidCategory_Bean(Rs.getString("category"), Rs.getString("name"), Rs.getString("value"));
                CategoryList.add(BidCategory_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return CategoryList;
    }

    public void getYahooCategory(){

    }

    public HashMap<Integer, ShopeeCategory_Bean> getShopeeCategory(){
        HashMap<Integer,ShopeeCategory_Bean> categoryMap = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> categoryLevelMap = new HashMap<>();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.Id,A.ParentId,A.Name,B.Name from ShopeeCategory A left join ShopeeCategoryTree B on A.id = B.SId order by A.ParentId,A.Id");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ShopeeCategory_Bean ShopeeCategory_Bean = new ShopeeCategory_Bean();
                ShopeeCategory_Bean.setId(Rs.getInt(1));
                ShopeeCategory_Bean.setParentId(Rs.getInt(2));
                ShopeeCategory_Bean.setCategoryName(Rs.getString(3));
                ShopeeCategory_Bean.setCategoryTreeName(Rs.getString(4));
                if(ShopeeCategory_Bean.getParentId() == 0){  //  第一層
                    categoryMap.put(ShopeeCategory_Bean.getId(),ShopeeCategory_Bean);
                    if(!categoryLevelMap.containsKey(1)){
                        ArrayList<Integer> levelList = new ArrayList<>();
                        levelList.add(ShopeeCategory_Bean.getId());
                        categoryLevelMap.put(1,levelList);
                    }else
                        categoryLevelMap.get(1).add(ShopeeCategory_Bean.getId());
                }else{
                    if(categoryMap.containsKey(ShopeeCategory_Bean.getParentId())){  //  第二層
                        categoryMap.get(ShopeeCategory_Bean.getParentId()).getChildCategoryBean().put(ShopeeCategory_Bean.getId(),ShopeeCategory_Bean);
                        if(!categoryLevelMap.containsKey(2)){
                            ArrayList<Integer> levelList = new ArrayList<>();
                            levelList.add(ShopeeCategory_Bean.getId());
                            categoryLevelMap.put(2,levelList);
                        }else
                            categoryLevelMap.get(2).add(ShopeeCategory_Bean.getId());
                    }else{  //  第三or四層
                        combineShopeeCategory(categoryMap,categoryLevelMap,ShopeeCategory_Bean);
                    }
                }
            }
            categoryLevelMap.clear();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return categoryMap;
    }
    private void combineShopeeCategory(HashMap<Integer,ShopeeCategory_Bean> categoryMap, HashMap<Integer, ArrayList<Integer>> categoryLevelMap,ShopeeCategory_Bean ShopeeCategory_Bean){
        ArrayList<Integer> secondLevelList = categoryLevelMap.get(2);
        if(secondLevelList.contains(ShopeeCategory_Bean.getParentId())){    //  第三層
            for(Integer firstCategoryId : categoryMap.keySet()){
                ShopeeCategory_Bean firstCategory = categoryMap.get(firstCategoryId);
                HashMap<Integer,ShopeeCategory_Bean> secondCategoryMap = firstCategory.getChildCategoryBean();
                for(Integer secondCategoryId : secondCategoryMap.keySet()){
                    ShopeeCategory_Bean secondCategory = secondCategoryMap.get(secondCategoryId);
                    if(secondCategory.getId() == ShopeeCategory_Bean.getParentId()){
                        secondCategory.getChildCategoryBean().put(ShopeeCategory_Bean.getId(),ShopeeCategory_Bean);
                        break;
                    }
                }
            }
        }else{  //  第四層
            for(Integer firstCategoryId : categoryMap.keySet()){
                ShopeeCategory_Bean firstCategory = categoryMap.get(firstCategoryId);
                HashMap<Integer,ShopeeCategory_Bean> secondCategoryMap = firstCategory.getChildCategoryBean();
                for(Integer secondCategoryId : secondCategoryMap.keySet()){
                    ShopeeCategory_Bean secondCategory = secondCategoryMap.get(secondCategoryId);
                    HashMap<Integer,ShopeeCategory_Bean> thirdCategoryMap = secondCategory.getChildCategoryBean();
                    for(Integer thirdCategoryId : thirdCategoryMap.keySet()) {
                        ShopeeCategory_Bean thirdCategory = thirdCategoryMap.get(thirdCategoryId);
                        if(thirdCategory.getId() == ShopeeCategory_Bean.getParentId()) {
                            thirdCategory.getChildCategoryBean().put(ShopeeCategory_Bean.getId(), ShopeeCategory_Bean);
                            break;
                        }
                    }
                }
            }
        }
    }
}
