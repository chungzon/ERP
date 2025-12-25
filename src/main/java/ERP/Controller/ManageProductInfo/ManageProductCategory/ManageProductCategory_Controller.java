package ERP.Controller.ManageProductInfo.ManageProductCategory;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Model.ManageProductCategory.ManageProductCategory_Model;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import javafx.scene.input.MouseEvent;

/** [Controller] Manage product category */
public class ManageProductCategory_Controller{
    @FXML private ComboBox<CategoryLayer> EstablishCategoryLayer_ComboBox, SearchCategoryLayer_ComboBox;
    @FXML private TextField CategoryIDTextField, CategoryNameTextField, StartCategoryIDTextField, EndCategoryIDTextField, CategoryNameText;
    @FXML private TableView<ProductCategory_Bean> TableView;
    @FXML private TableColumn<ProductCategory_Bean, String> CategoryIDColumn, CategoryNameColumn, CategoryLayerColumn, InStockColumn;

    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ManageProductCategory_Model ManageProductCategory_Model;
    public ManageProductCategory_Controller() {
        this.ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        this.KeyPressed = ERPApplication.ToolKit.KeyPressed;
        this.ManageProductCategory_Model = ERPApplication.ToolKit.ModelToolKit.getManageProductCategoryModel();
    }

    /** Set component of manage product category */
    public void setComponent(){
        EstablishCategoryLayer_ComboBox.getItems().addAll(CategoryLayer.values());
        SearchCategoryLayer_ComboBox.getItems().addAll(CategoryLayer.values());
        EstablishCategoryLayer_ComboBox.getSelectionModel().selectFirst();
        SearchCategoryLayer_ComboBox.getSelectionModel().selectFirst();

        CategoryLayer CategoryLayer = ComponentToolKit.getCategoryLayerComboBoxSelectItem(EstablishCategoryLayer_ComboBox);
        CategoryIDTextField.setText(ManageProductCategory_Model.getNewestProductCategoryID(CategoryLayer));
        StartCategoryIDTextField.setText("01");
        EndCategoryIDTextField.setText("99");
        InitialTableView();
        TableView.setItems(ManageProductCategory_Model.searchProductCategory(CategoryLayer, 0, 0));

        setKeyWordTextLimitDigital();
    }
    private void setKeyWordTextLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(CategoryIDTextField,false);
        ComponentToolKit.addTextFieldLimitDigital(StartCategoryIDTextField,false);
        ComponentToolKit.addTextFieldLimitDigital(EndCategoryIDTextField,false);
    }
    /** ComboBox On Action - [商品類別建立] 類別類型 */
    @FXML protected void EstablishCategoryLayerOnAction(){
        CategoryNameTextField.setText("");
        CategoryLayer CategoryLayer = ComponentToolKit.getCategoryLayerComboBoxSelectItem(EstablishCategoryLayer_ComboBox);
        CategoryIDTextField.setText(ManageProductCategory_Model.getNewestProductCategoryID(CategoryLayer));
        TableView.setItems(ManageProductCategory_Model.searchProductCategory(CategoryLayer, 0, 0));
    }
    /** ComboBox On Action - [商品類別搜尋] 類別類型 */
    @FXML protected void searchCategoryLayerOnAction(){
        CategoryLayer CategoryLayer = ComponentToolKit.getCategoryLayerComboBoxSelectItem(SearchCategoryLayer_ComboBox);
        if(CategoryLayer == Product_Enum.CategoryLayer.大類別 || CategoryLayer == Product_Enum.CategoryLayer.中類別){
            StartCategoryIDTextField.setText("01");
            EndCategoryIDTextField.setText("99");
        }else{
            StartCategoryIDTextField.setText("001");
            EndCategoryIDTextField.setText("999");
        }
    }
    /** Button Mouse Clicked - 寫入 */
    @FXML protected void InsertCategoryMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CategoryLayer CategoryLayer = ComponentToolKit.getCategoryLayerComboBoxSelectItem(EstablishCategoryLayer_ComboBox);
            if(ManageProductCategory_Model.isCategoryExist(CategoryIDTextField.getText(),CategoryLayer)){
                DialogUI.MessageDialog("※ 新增失敗，類別代碼已重複");
                return;
            }
            if((CategoryLayer == Product_Enum.CategoryLayer.大類別 || CategoryLayer == Product_Enum.CategoryLayer.中類別) && CategoryIDTextField.getText().length() != 2){
                DialogUI.MessageDialog("※ 新增失敗，類別代碼長度錯誤");
                return;
            }
            if(CategoryLayer == Product_Enum.CategoryLayer.小類別 && CategoryIDTextField.getText().length() != 3){
                DialogUI.MessageDialog("※ 新增失敗，類別代碼長度錯誤");
                return;
            }
            ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
            ProductCategory_Bean.setCategoryID(CategoryIDTextField.getText());
            ProductCategory_Bean.setCategoryName(CategoryNameTextField.getText());
            ProductCategory_Bean.setInStock(0);
            ProductCategory_Bean.setCategoryLayer(CategoryLayer);
            if(ManageProductCategory_Model.insertProductCategory(ProductCategory_Bean)) {
                DialogUI.MessageDialog("※ 類別新增成功");
                ComponentToolKit.getCategoryTableViewItemList(TableView).add(ProductCategory_Bean);
                CategoryNameTextField.setText("");
                CategoryIDTextField.setText(ManageProductCategory_Model.getNewestProductCategoryID(CategoryLayer));
            }else   DialogUI.MessageDialog("※ 類別新增失敗");
        }
    }
    /** Button Mouse Clicked - 查詢 */
    @FXML protected void SearchCategoryMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CategoryLayer CategoryLayer = ComponentToolKit.getCategoryLayerComboBoxSelectItem(SearchCategoryLayer_ComboBox);
            int StartCategoryID = Integer.parseInt(StartCategoryIDTextField.getText());
            int EndCategoryID = Integer.parseInt(EndCategoryIDTextField.getText());
            if(StartCategoryID <= EndCategoryID)    TableView.setItems(ManageProductCategory_Model.searchProductCategory(CategoryLayer, StartCategoryID, EndCategoryID));
            else    DialogUI.MessageDialog("※ 類別區間設定錯誤");
        }
    }
    /** Button Mouse Clicked - 修改名稱 */
    @FXML protected void ModifyCategoryMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ProductCategory_Bean ProductCategory_Bean = ComponentToolKit.getCategoryTableViewSelectItem(TableView);
            if(ProductCategory_Bean == null){
                DialogUI.MessageDialog("※ 請選擇類別");
                return;
            }
            if(DialogUI.ConfirmDialog("※ 確定是否修改?",true,false,0,0)){
                String CategoryName = CategoryNameText.getText();
                if(ManageProductCategory_Model.modifyProductCategory(CategoryName, ProductCategory_Bean)){
                    ProductCategory_Bean.setCategoryName(CategoryName);
                    DialogUI.MessageDialog("※ 名稱修改成功");
                }else   DialogUI.MessageDialog("※ 名稱修改失敗");
            }
        }
    }
    /** Button Mouse Clicked - 刪除類別 */
    @FXML protected void DeleteCategoryMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ProductCategory_Bean ProductCategory_Bean = ComponentToolKit.getCategoryTableViewSelectItem(TableView);
            if(ProductCategory_Bean != null){
                if(Integer.parseInt(ProductCategory_Bean.getInStock()) != 0){
                    DialogUI.MessageDialog("※ 刪除被拒，存量不為「0」");
                    return;
                }
                if(ManageProductCategory_Model.isCategoryExistProduct(ProductCategory_Bean)){
                    DialogUI.MessageDialog("※ 刪除被拒，此類別存在商品");
                    return;
                }
                if(DialogUI.ConfirmDialog("※ 確定是否刪除?",true,true,4,8)){
                    if(ManageProductCategory_Model.deleteProductCategory(ProductCategory_Bean)) {
                        DialogUI.MessageDialog("※ 類別刪除成功");
                        ComponentToolKit.removeCategoryTableViewItem(TableView, ProductCategory_Bean);
                    }else    DialogUI.MessageDialog("※ 類別刪除失敗");
                }
            }else   DialogUI.MessageDialog("※ 請選擇類別");
        }
    }
    /** Button Mouse Clicked - 列印表格 */
    @FXML protected void PrintTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<ProductCategory_Bean> ProductCategoryList = ComponentToolKit.getCategoryTableViewItemList(TableView);
            if(ProductCategoryList.size() != 0) ManageProductCategory_Model.printProductCategory(ProductCategoryList, StartCategoryIDTextField.getText(), EndCategoryIDTextField.getText());
            else    DialogUI.MessageDialog("無商品類別");
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ProductCategory_Bean ProductCategory_Bean = ComponentToolKit.getCategoryTableViewSelectItem(TableView);
            if(ProductCategory_Bean != null)
                CategoryNameText.setText(ProductCategory_Bean.getCategoryName());
        }
    }
    private void InitialTableView(){
        ComponentToolKit.setColumnCellValue(CategoryIDColumn,"CategoryID", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(CategoryNameColumn,"CategoryName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(CategoryLayerColumn,"CategoryLayer", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InStockColumn,"InStock", "CENTER-LEFT", "16",null);
    }
}
