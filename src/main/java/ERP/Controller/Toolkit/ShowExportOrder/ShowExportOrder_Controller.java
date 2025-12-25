package ERP.Controller.Toolkit.ShowExportOrder;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.ExportOrder_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.ERPApplication;
import ERP.Model.Order.ExportOrder_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Show export order */
public class ShowExportOrder_Controller {
    @FXML CheckBox showPrice_CheckBox;
    @FXML TableColumn<ExportOrder_Bean,String> ItemNumber_TableColumn, ISBN_TableColumn, ProductName_TableColumn;
    @FXML TableView<ExportOrder_Bean> TableView;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private Stage Stage;
    private String outputFilePath;
    private Order_Bean Order_Bean;
    private ObjectInfo_Bean ObjectInfo_Bean;
    private ObservableList<ExportOrder_Bean> ProductList;
    private ExportOrder_Model ExportOrder_Model;

    public ShowExportOrder_Controller(){
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setOutPutFilePath(String outputFilePath){   this.outputFilePath = outputFilePath;   }
    public void setOrder_Bean(Order_Bean Order_Bean){   this.Order_Bean = Order_Bean;   }
    public void setObjectInfo_Bean(ObjectInfo_Bean ObjectInfo_Bean){  this.ObjectInfo_Bean = ObjectInfo_Bean; }
    public void setProductList(ObservableList<ExportOrder_Bean> ProductList){   this.ProductList = ProductList; }
    /** Set component of show export order */
    public void setComponent(){
        ExportOrder_Model = new ExportOrder_Model(outputFilePath, Order_Bean, ObjectInfo_Bean, ProductList);
        initialTableView();
        TableView.setItems(ProductList);
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(ItemNumber_TableColumn,"ItemNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ISBN_TableColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductName_TableColumn,"ProductName", "CENTER-LEFT", "16",null);
        ProductName_TableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    @FXML protected void ProductNameOnEditCommit(TableColumn.CellEditEvent<ExportOrder_Bean,String> productNameOnEditCommit){
        ExportOrder_Bean ExportOrder_Bean = ComponentToolKit.getExportOrderTableViewSelectItem(TableView);
        ExportOrder_Bean.setProductName(productNameOnEditCommit.getNewValue());
    }
    /** Button Mouse Clicked - 匯出 */
    @FXML protected void ExportOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ExportOrder_Model.ExportOrder(showPrice_CheckBox.isSelected())){
                ComponentToolKit.closeThisStage(Stage);
                DialogUI.MessageDialog("※ 匯出成功!");
            }else    DialogUI.MessageDialog("※ 匯出失敗!");
        }
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrintOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ExportOrder_Model.PrintOrder(showPrice_CheckBox.isSelected())){
                ComponentToolKit.closeThisStage(Stage);
                DialogUI.MessageDialog("※ 列印成功!");
            }else    DialogUI.MessageDialog("※ 列印失敗!");
        }
    }
    /** Button Mouse Clicked - 取消 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){   if(KeyPressed.isMouseLeftClicked(MouseEvent))  ComponentToolKit.closeThisStage(this.Stage); }
}
