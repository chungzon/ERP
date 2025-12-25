package ERP.Controller.Toolkit.ShowBidCategorySetting;

import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.Product.ShopeeCategory_Bean;
import ERP.Bean.ToolKit.BidCategorySetting.BidCategory_Bean;
import ERP.Controller.ManageProductInfo.ManageProductInfo.ManageProductInfo_Controller;
import ERP.ERPApplication;
import ERP.Model.Product.BidCategorySetting_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;

/** [Controller] Bid category setting */
public class ShowBidCategorySetting_Controller {
    @FXML private TextField YahooCategoryText, RutenCategoryText, ShopeeCategoryText;
    @FXML private ComboBox<BidCategory_Bean> RutenFirstCategory_ComboBox, RutenSecondCategory_ComboBox, RutenThirdCategory_ComboBox, RutenFourthCategory_ComboBox, RutenFifthCategory_ComboBox, RutenSixthCategory_ComboBox;
    @FXML private ComboBox<ShopeeCategory_Bean> ShopeeFirstCategory_ComboBox, ShopeeSecondCategory_ComboBox, ShopeeThirdCategory_ComboBox, ShopeeFourthCategory_ComboBox;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private Stage Stage;
    private ManageProductInfo_Controller ManageProductInfo_Controller;
    private BidCategorySetting_Model BidCategorySetting_Model;
    private ProductInfo_Bean ProductInfo_Bean;
    private HashMap<Integer, ShopeeCategory_Bean> shopeeCategoryMap;
    public ShowBidCategorySetting_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        BidCategorySetting_Model = ERPApplication.ToolKit.ModelToolKit.getBidCategorySettingModel();
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set object - [Controller] ManageProductInfo_Controller */
    public void setManageProductInfo_Controller(ManageProductInfo_Controller ManageProductInfo_Controller){ this.ManageProductInfo_Controller = ManageProductInfo_Controller;   }
    /** Set component of bid category setting */
    public void setComponent(){
        shopeeCategoryMap = BidCategorySetting_Model.getShopeeCategory();
    }
    /** Set object - [Bean] ManageProductInfo_Bean */
    public void setProductInfo_Bean(ProductInfo_Bean ProductInfo_Bean){
        this.ProductInfo_Bean = ProductInfo_Bean;
//        setRutenCategory(ProductInfo_Bean.getRutenCategory());
        setShopeeCategory(ProductInfo_Bean.getShopeeCategory());
    }
    private void setRutenCategory(String RutenCategory){
        RutenCategoryText.setText(RutenCategory);
        ArrayList<BidCategory_Bean> FirstCategoryList = BidCategorySetting_Model.getRutenCategory("1",1);
        FirstCategoryList.add(0, new BidCategory_Bean("","",""));
        for (BidCategory_Bean BidCategory_Bean : FirstCategoryList) RutenFirstCategory_ComboBox.getItems().addAll(BidCategory_Bean);
        setComboBoxObj(RutenFirstCategory_ComboBox);

        if(RutenCategory != null && !RutenCategory.equals("")){
            String Value = RutenCategory.substring(0, 4);
            for (BidCategory_Bean BidCategory_Bean : FirstCategoryList) if (BidCategory_Bean.getCategoryValue().equals(Value))   RutenFirstCategory_ComboBox.getSelectionModel().select(BidCategory_Bean);
            int Layer = RutenCategory.length()/4;
            for(int index = 1 ; index < Layer ; index++){
                String Category = RutenCategory.substring(0, (index-1)*4+4);
                Value = RutenCategory.substring(0, index*4+4);
                if(index == 1)  getRutenCategory(RutenSecondCategory_ComboBox, index+1, Category, Value);
                if(index == 2)  getRutenCategory(RutenThirdCategory_ComboBox, index+1, Category, Value);
                if(index == 3)  getRutenCategory(RutenFourthCategory_ComboBox, index+1, Category, Value);
                if(index == 4)  getRutenCategory(RutenFifthCategory_ComboBox, index+1, Category, Value);
                if(index == 5)  getRutenCategory(RutenSixthCategory_ComboBox, index+1, Category, Value);
            }
        }
    }
    private void getRutenCategory(ComboBox<BidCategory_Bean> RutenLayer_ComboBox, int Layer, String Category, String Value){
        ArrayList<BidCategory_Bean> CategoryList = BidCategorySetting_Model.getRutenCategory(Category, Layer);
        for (BidCategory_Bean BidCategory_Bean : CategoryList) {
            RutenLayer_ComboBox.getItems().addAll(BidCategory_Bean);
            if(BidCategory_Bean.getCategoryValue().equals(Value))   RutenLayer_ComboBox.getSelectionModel().select(BidCategory_Bean);
        }
        setComboBoxObj(RutenLayer_ComboBox);
        RutenLayer_ComboBox.setVisible(true);
    }
    /** ComboBox On Action - 露天第一類別 */
    @FXML protected void RutenFirstCategoryOnAction(){
        BidCategory_Bean RutenFirstCategoryBean = RutenFirstCategory_ComboBox.getSelectionModel().getSelectedItem();
        RutenCategoryText.setText(RutenFirstCategoryBean.getCategoryValue());
        ArrayList<BidCategory_Bean> SecondCategoryList = BidCategorySetting_Model.getRutenCategory(RutenFirstCategoryBean.getCategoryValue(),2);
        initialRutenComboBox(RutenSecondCategory_ComboBox);
        initialRutenComboBox(RutenThirdCategory_ComboBox);
        initialRutenComboBox(RutenFourthCategory_ComboBox);
        initialRutenComboBox(RutenFifthCategory_ComboBox);
        initialRutenComboBox(RutenSixthCategory_ComboBox);
        if(SecondCategoryList.size() != 0){
            for (BidCategory_Bean BidCategory_Bean : SecondCategoryList) RutenSecondCategory_ComboBox.getItems().addAll(BidCategory_Bean);
            setComboBoxObj(RutenSecondCategory_ComboBox);
            RutenSecondCategory_ComboBox.setVisible(true);
        }
    }
    /** ComboBox On Action - 露天第二類別 */
    @FXML protected void RutenSecondCategoryOnAction(){
        BidCategory_Bean RutenSecondCategoryBean = RutenSecondCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(RutenSecondCategoryBean != null){
            RutenCategoryText.setText(RutenSecondCategoryBean.getCategoryValue());
            ArrayList<BidCategory_Bean> ThirdCategoryList = BidCategorySetting_Model.getRutenCategory(RutenSecondCategoryBean.getCategoryValue(),3);
            initialRutenComboBox(RutenThirdCategory_ComboBox);
            initialRutenComboBox(RutenFourthCategory_ComboBox);
            initialRutenComboBox(RutenFifthCategory_ComboBox);
            initialRutenComboBox(RutenSixthCategory_ComboBox);
            if(ThirdCategoryList.size() != 0) {
                for (BidCategory_Bean BidCategory_Bean : ThirdCategoryList) RutenThirdCategory_ComboBox.getItems().addAll(BidCategory_Bean);
                setComboBoxObj(RutenThirdCategory_ComboBox);
                RutenThirdCategory_ComboBox.setVisible(true);
            }
        }
    }
    /** ComboBox On Action - 露天第三類別 */
    @FXML protected void RutenThirdCategoryOnAction(){
        BidCategory_Bean RutenThirdCategoryBean = RutenThirdCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(RutenThirdCategoryBean != null){
            RutenCategoryText.setText(RutenThirdCategoryBean.getCategoryValue());
            ArrayList<BidCategory_Bean> FourthCategoryList = BidCategorySetting_Model.getRutenCategory(RutenThirdCategoryBean.getCategoryValue(),4);
            initialRutenComboBox(RutenFourthCategory_ComboBox);
            initialRutenComboBox(RutenFifthCategory_ComboBox);
            initialRutenComboBox(RutenSixthCategory_ComboBox);
            if(FourthCategoryList.size() != 0){
                for (BidCategory_Bean BidCategory_Bean : FourthCategoryList) RutenFourthCategory_ComboBox.getItems().addAll(BidCategory_Bean);
                setComboBoxObj(RutenFourthCategory_ComboBox);
                RutenFourthCategory_ComboBox.setVisible(true);
            }
        }
    }
    /** ComboBox On Action - 露天第四類別 */
    @FXML protected void RutenFourthCategoryOnAction(){
        BidCategory_Bean RutenFourthCategoryBean = RutenFourthCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(RutenFourthCategoryBean != null){
            RutenCategoryText.setText(RutenFourthCategoryBean.getCategoryValue());
            ArrayList<BidCategory_Bean> FifthCategoryList = BidCategorySetting_Model.getRutenCategory(RutenFourthCategoryBean.getCategoryValue(),5);
            initialRutenComboBox(RutenFifthCategory_ComboBox);
            initialRutenComboBox(RutenSixthCategory_ComboBox);
            if(FifthCategoryList.size() != 0){
                for (BidCategory_Bean BidCategory_Bean : FifthCategoryList) RutenFifthCategory_ComboBox.getItems().addAll(BidCategory_Bean);
                setComboBoxObj(RutenFifthCategory_ComboBox);
                RutenFifthCategory_ComboBox.setVisible(true);
            }
        }
    }
    /** ComboBox On Action - 露天第五類別 */
    @FXML protected void RutenFifthCategoryOnAction(){
        BidCategory_Bean RutenFifthCategoryBean = RutenFifthCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(RutenFifthCategoryBean != null){
            RutenCategoryText.setText(RutenFifthCategoryBean.getCategoryValue());
            ArrayList<BidCategory_Bean> SixthCategoryList = BidCategorySetting_Model.getRutenCategory(RutenFifthCategoryBean.getCategoryValue(),6);
            initialRutenComboBox(RutenSixthCategory_ComboBox);
            if(SixthCategoryList.size() != 0){
                for (BidCategory_Bean BidCategory_Bean : SixthCategoryList) RutenSixthCategory_ComboBox.getItems().addAll(BidCategory_Bean);
                setComboBoxObj(RutenSixthCategory_ComboBox);
                RutenSixthCategory_ComboBox.setVisible(true);
            }
        }
    }
    /** ComboBox On Action - 露天第六類別 */
    @FXML protected void RutenSixthCategoryOnAction(){
        BidCategory_Bean RutenSixthCategoryBean = RutenSixthCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(RutenSixthCategoryBean != null) RutenCategoryText.setText(RutenSixthCategoryBean.getCategoryValue());
    }
    private void setShopeeCategory(ShopeeCategory_Bean ShopeeCategory_Bean){
        setShopeeComboBoxObj(ShopeeFirstCategory_ComboBox);
        setShopeeComboBoxObj(ShopeeSecondCategory_ComboBox);
        setShopeeComboBoxObj(ShopeeThirdCategory_ComboBox);
        setShopeeComboBoxObj(ShopeeFourthCategory_ComboBox);
        for(Integer firstCategoryId : shopeeCategoryMap.keySet()){
            ShopeeCategory_Bean firstCategory = shopeeCategoryMap.get(firstCategoryId);
            if(ShopeeFirstCategory_ComboBox.getItems().size() == 0)
                ShopeeFirstCategory_ComboBox.getItems().add(new ShopeeCategory_Bean());
            ShopeeFirstCategory_ComboBox.getItems().add(firstCategory);
            if(ShopeeCategory_Bean != null)
                setShopeeSecondCategory(firstCategory,firstCategory.getChildCategoryBean(), ShopeeCategory_Bean.getId());
        }
    }
    private void setShopeeSecondCategory(ShopeeCategory_Bean firstCategory, HashMap<Integer,ShopeeCategory_Bean> secondCategoryMap, int shopeeFinalCategoryId){
        for(Integer secondCategoryId : secondCategoryMap.keySet()){
            ShopeeCategory_Bean secondCategory = secondCategoryMap.get(secondCategoryId);
            ShopeeSecondCategory_ComboBox.getItems().add(secondCategory);
            if(setShopeeThirdCategory(secondCategory.getChildCategoryBean(), shopeeFinalCategoryId)){
                ShopeeFirstCategory_ComboBox.getSelectionModel().select(firstCategory);
                ShopeeSecondCategory_ComboBox.getSelectionModel().select(secondCategory);
                ShopeeSecondCategory_ComboBox.setVisible(true);
            }
        }
    }
    private boolean setShopeeThirdCategory(HashMap<Integer,ShopeeCategory_Bean> thirdCategoryMap, int shopeeFinalCategoryId){
        boolean findCorrectCategory = false;
        for(Integer thirdCategoryId : thirdCategoryMap.keySet()) {
            ShopeeCategory_Bean thirdCategory = thirdCategoryMap.get(thirdCategoryId);
            ShopeeThirdCategory_ComboBox.getItems().add(thirdCategory);
            if(!findCorrectCategory && thirdCategoryId.equals(shopeeFinalCategoryId)){
                findCorrectCategory = true;
                ShopeeCategoryText.setText(thirdCategory.getCategoryTreeName());
                ShopeeThirdCategory_ComboBox.getSelectionModel().select(thirdCategory);
                ShopeeThirdCategory_ComboBox.setVisible(true);
            }
            setShopeeFourthCategory(thirdCategory.getChildCategoryBean(), findCorrectCategory, shopeeFinalCategoryId);
        }
        return findCorrectCategory;
    }
    private void setShopeeFourthCategory(HashMap<Integer,ShopeeCategory_Bean> fourthCategoryMap, boolean findCorrectCategory, int shopeeFinalCategoryId){
        for(Integer fourthCategoryId : fourthCategoryMap.keySet()) {
            ShopeeCategory_Bean fourthCategory = fourthCategoryMap.get(fourthCategoryId);
            ShopeeFourthCategory_ComboBox.getItems().add(fourthCategory);
            if(!findCorrectCategory && fourthCategoryId.equals(shopeeFinalCategoryId)){
                findCorrectCategory = true;
                ShopeeCategoryText.setText(fourthCategory.getCategoryTreeName());
                ShopeeFourthCategory_ComboBox.getSelectionModel().select(fourthCategory);
                ShopeeThirdCategory_ComboBox.setVisible(true);
                ShopeeFourthCategory_ComboBox.setVisible(true);
            }
        }
    }
    /** ComboBox On Action - 蝦皮第一類別 */
    @FXML protected void ShopeeFirstCategoryOnAction(){
        ShopeeCategory_Bean firstCategory = ShopeeFirstCategory_ComboBox.getSelectionModel().getSelectedItem();
        ShopeeCategoryText.setText("");
        initialShopeeComboBox(ShopeeSecondCategory_ComboBox);
        initialShopeeComboBox(ShopeeThirdCategory_ComboBox);
        initialShopeeComboBox(ShopeeFourthCategory_ComboBox);
        if(firstCategory != null){
            HashMap<Integer,ShopeeCategory_Bean> childCategoryMap = firstCategory.getChildCategoryBean();
            for(Integer secondCategoryId : childCategoryMap.keySet()){
                ShopeeSecondCategory_ComboBox.getItems().add(childCategoryMap.get(secondCategoryId));
            }
            if(ShopeeSecondCategory_ComboBox.getItems().size() > 0)
                ShopeeSecondCategory_ComboBox.setVisible(true);
        }
    }
    /** ComboBox On Action - 蝦皮第二類別 */
    @FXML protected void ShopeeSecondCategoryOnAction(){
        ShopeeCategory_Bean secondCategory = ShopeeSecondCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(secondCategory != null){
            ShopeeCategoryText.setText("");
            initialShopeeComboBox(ShopeeThirdCategory_ComboBox);
            initialShopeeComboBox(ShopeeFourthCategory_ComboBox);
            HashMap<Integer,ShopeeCategory_Bean> childCategoryMap = secondCategory.getChildCategoryBean();
            for(Integer thirdCategoryId : childCategoryMap.keySet()){
                ShopeeThirdCategory_ComboBox.getItems().add(childCategoryMap.get(thirdCategoryId));
            }
            ShopeeThirdCategory_ComboBox.setVisible(true);
        }
    }
    /** ComboBox On Action - 蝦皮第三類別 */
    @FXML protected void ShopeeThirdCategoryOnAction(){
        ShopeeCategory_Bean thirdCategory = ShopeeThirdCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(thirdCategory != null){
            ShopeeCategoryText.setText(thirdCategory.getCategoryTreeName() == null ? "" : thirdCategory.getCategoryTreeName());
            initialShopeeComboBox(ShopeeFourthCategory_ComboBox);
            HashMap<Integer,ShopeeCategory_Bean> childCategoryMap = thirdCategory.getChildCategoryBean();
            if(childCategoryMap.size() != 0){
                for(Integer fourthCategoryId : childCategoryMap.keySet()){
                    ShopeeFourthCategory_ComboBox.getItems().add(childCategoryMap.get(fourthCategoryId));
                }
                ShopeeFourthCategory_ComboBox.setVisible(true);
            }else
                ShopeeCategoryText.setText(thirdCategory.getCategoryTreeName());
        }
    }
    /** ComboBox On Action - 蝦皮第四類別 */
    @FXML protected void ShopeeFourthCategoryOnAction(){
        ShopeeCategory_Bean fourthCategory = ShopeeFourthCategory_ComboBox.getSelectionModel().getSelectedItem();
        if(fourthCategory != null) ShopeeCategoryText.setText(fourthCategory.getCategoryTreeName() == null ? "" : fourthCategory.getCategoryTreeName());
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void SaveBidCategoryMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ShopeeCategory_Bean shopeeFirstCategory_Bean = ShopeeFirstCategory_ComboBox.getSelectionModel().getSelectedItem();
            if(shopeeFirstCategory_Bean != null && shopeeFirstCategory_Bean.getId() != null && ShopeeCategoryText.getText().equals("")){
                DialogUI.MessageDialog("※ 請選擇正確的「蝦皮」類別");
                return;
            }
            if(ShopeeFourthCategory_ComboBox.isVisible())
                ProductInfo_Bean.setShopeeCategory(ShopeeFourthCategory_ComboBox.getSelectionModel().getSelectedItem());
            else if(ShopeeThirdCategory_ComboBox.isVisible())
                ProductInfo_Bean.setShopeeCategory(ShopeeThirdCategory_ComboBox.getSelectionModel().getSelectedItem());
            else if(shopeeFirstCategory_Bean != null && shopeeFirstCategory_Bean.getId() == null)
                ProductInfo_Bean.setShopeeCategory(null);
//            ProductInfo_Bean.setRutenCategory(RutenCategoryText.getText());
            StageClose(null);
        }
    }
    /** Button Mouse Clicked - 離開 */
    @FXML protected void StageClose(MouseEvent MouseEvent){  if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent))   ComponentToolKit.closeThisStage(Stage);  }
    private void initialRutenComboBox(ComboBox<BidCategory_Bean> ComboBox){
        ComboBox.getItems().clear();
        ComboBox.setVisible(false);
    }
    private void initialShopeeComboBox(ComboBox<ShopeeCategory_Bean> ComboBox){
        ComboBox.getItems().clear();
        ComboBox.setVisible(false);
    }
    private void setComboBoxObj(ComboBox<BidCategory_Bean> ComboBox){
        ComboBox.setButtonCell(new ComboBoxObj());
        ComboBox.setCellFactory(Obj -> new ComboBoxObj());
    }
    class ComboBoxObj extends ListCell<BidCategory_Bean> {
        @Override
        protected void updateItem(BidCategory_Bean BidCategory_Bean, boolean empty) {
            super.updateItem(BidCategory_Bean, empty);
            if (BidCategory_Bean != null) {
                setText(BidCategory_Bean.getCategoryName());
            }
        }
    }
    private void setShopeeComboBoxObj(ComboBox<ShopeeCategory_Bean> ComboBox){
        ComboBox.setButtonCell(new setShopeeComboBoxObj());
        ComboBox.setCellFactory(Obj -> new setShopeeComboBoxObj());
    }
    class setShopeeComboBoxObj extends ListCell<ShopeeCategory_Bean> {
        @Override
        protected void updateItem(ShopeeCategory_Bean ShopeeCategory_Bean, boolean empty) {
            super.updateItem(ShopeeCategory_Bean, empty);
            if (ShopeeCategory_Bean != null) {
                setText(ShopeeCategory_Bean.getCategoryName());
            }
        }
    }
}
