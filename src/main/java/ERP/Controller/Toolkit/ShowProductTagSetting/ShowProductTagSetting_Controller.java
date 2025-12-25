package ERP.Controller.Toolkit.ShowProductTagSetting;

import ERP.ERPApplication;
import ERP.Model.Product.Product_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Show product tag setting */
public class ShowProductTagSetting_Controller {
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private Product_Model product_model;
    private Stage Stage;
    private String isbn, firmCode;
    private ObservableList<String> productTagList;
    @FXML protected Button InsertButton, ModifyButton, DeleteButton;
    @FXML protected ComboBox<String> ProductTag_ComboBox;
    @FXML protected TextField ProductTagName, InsertProductTagName;

    public ShowProductTagSetting_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;

        product_model = ERPApplication.ToolKit.ModelToolKit.getProductModel();
    }
    public void setProductData(String isbn, String firmCode, ObservableList<String> productTagList){
        this.isbn = isbn;
        this.firmCode = firmCode;
        this.productTagList = productTagList;
        ProductTag_ComboBox.getItems().addAll(this.productTagList);
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** ComboBox On Action */
    @FXML protected void ProductTagOnAction(){
        ObservableList<String> ProductTagList = ComponentToolKit.getComboBoxItemsStringFormat(ProductTag_ComboBox);
        if(ProductTagList.size() != 0)  {
            String SelectTag = ProductTag_ComboBox.getSelectionModel().getSelectedItem();
            ProductTagName.setText(SelectTag);
            ModifyButton.setDisable(false);
            DeleteButton.setDisable(false);
        }else   ProductTagName.setText("");
    }
    /** Button Mouse Clicked - 新增 */
    @FXML protected void InsertProductTagMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ObservableList<String> ProductTagList = ProductTag_ComboBox.getItems();
            if(InsertProductTagName.getText().equals(""))
                DialogUI.MessageDialog("※ 請輸入標籤");
            else if(InsertProductTagName.getText().equals(firmCode))
                DialogUI.MessageDialog("※ 與商品碼重複，請重新輸入");
            else if (!ProductTagList.contains(InsertProductTagName.getText())) {
                this.productTagList.add(InsertProductTagName.getText());
                ProductTagList.add(InsertProductTagName.getText());
                ProductTagName.setText(InsertProductTagName.getText());
                ProductTag_ComboBox.getSelectionModel().select(InsertProductTagName.getText());
                InsertProductTagName.setText("");
                DialogUI.MessageDialog("※ 新增成功");
            } else {
                InsertProductTagName.setText("");
                DialogUI.MessageDialog("※ 已重複");
            }
        }
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void ModifyProductTagMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String selectProductTag = ComponentToolKit.getComboBoxSelectItemStringFormat(ProductTag_ComboBox);
            String modifyProductTag = ProductTagName.getText();
            if(DialogUI.ConfirmDialog("確定要將【" + selectProductTag+ "】修改成【" + modifyProductTag + "】?",true,false,0,0)){
                ObservableList<String> ProductTagList = ComponentToolKit.getComboBoxItemsStringFormat(ProductTag_ComboBox);
                this.productTagList.remove(selectProductTag);
                ProductTagList.remove(selectProductTag);
                this.productTagList.add(modifyProductTag);
                ProductTagList.add(modifyProductTag);
                ProductTag_ComboBox.getSelectionModel().select(modifyProductTag);
                ProductTagName.setText(modifyProductTag);
                DialogUI.MessageDialog("※ 修改成功");
            }
        }
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteProductTagMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            String selectProductTag = ComponentToolKit.getComboBoxSelectItemStringFormat(ProductTag_ComboBox);
            if(DialogUI.ConfirmDialog("確定要刪除【" + selectProductTag+ "】",true,false,0,0)){
                ObservableList<String> ProductTagList = ComponentToolKit.getComboBoxItemsStringFormat(ProductTag_ComboBox);
                this.productTagList.remove(selectProductTag);
                ProductTagList.remove(selectProductTag);
                ProductTag_ComboBox.getSelectionModel().selectFirst();
                if (ProductTagList.size() != 0) ProductTagName.setText(ProductTagList.get(0));
                else ProductTagName.setText("");
                DialogUI.MessageDialog("※ 刪除成功");
            }
        }
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void saveMouseClicked(MouseEvent  mouseEvent){
        if(KeyPressed.isMouseLeftClicked(mouseEvent)){
            ObservableList<String> productTagList = ComponentToolKit.getComboBoxItemsStringFormat(ProductTag_ComboBox);
            productTagList.add(firmCode);
            if(product_model.updateProductTag(isbn, productTagList)){
                ComponentToolKit.closeThisStage(Stage);
                DialogUI.MessageDialog("儲存成功");
            }else{
                DialogUI.MessageDialog("儲存失敗");
            }
            this.productTagList.remove(firmCode);
            productTagList.remove(firmCode);
        }
    }

    /** Button Mouse Clicked - 離開 */
    @FXML protected void closeMouseClicked(MouseEvent mouseEvent){
        if(KeyPressed.isMouseLeftClicked(mouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }
}
