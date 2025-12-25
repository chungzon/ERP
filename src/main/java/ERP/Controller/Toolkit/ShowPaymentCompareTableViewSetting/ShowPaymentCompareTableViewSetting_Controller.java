package ERP.Controller.Toolkit.ShowPaymentCompareTableViewSetting;

import ERP.Bean.ToolKit.ShowTableViewSetting.TableViewSetting_Bean;
import ERP.Controller.ManagePayableReceivable.PaymentCompareSystem.PaymentCompareSystem_Controller;
import ERP.ERPApplication;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PaymentCompareTabName;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum.OrderKeyCombination;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class ShowPaymentCompareTableViewSetting_Controller {
    @FXML TableColumn<TableViewSetting_Bean,Boolean> showTableColumn;
    @FXML TableColumn<TableViewSetting_Bean,String> ColumnName, previousMoveColumn, nextMoveColumn;
    @FXML TableView<TableViewSetting_Bean> TableView;

    private final KeyCombination previousMoveKeyCombine = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);
    private final KeyCombination nextMoveKeyCombine = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallConfig CallConfig;
    private PaymentCompareSystem_Controller PaymentCompareSystem_Controller;
    private PaymentCompareTabName PaymentCompareTabName;
    private Stage Stage;
    private JSONObject TableViewSettingMap;

    public ShowPaymentCompareTableViewSetting_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        CallConfig = ERPApplication.ToolKit.CallConfig;
    }
    /** Set object - [Controller] SearchOrder_Controller */
    public void setPaymentCompareSystem_Controller(PaymentCompareSystem_Controller PaymentCompareSystem_Controller){   this.PaymentCompareSystem_Controller = PaymentCompareSystem_Controller;   }
    /** Set object - [Enum] Order_Enum.SearchOrderSource */
    public void setPaymentCompareTabName(PaymentCompareTabName PaymentCompareTabName){  this.PaymentCompareTabName = PaymentCompareTabName; }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set object - [JsonObject]
     * @param TableViewSettingMap the sorting of TableColumn in TableView
     * */
    public void setTableViewSettingMap(JSONObject TableViewSettingMap){   this.TableViewSettingMap = TableViewSettingMap;}
    /** Set component of show search order TableView setting */
    public void setComponent(){ initialTableView(); }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(showTableColumn,"showTableColumn", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ColumnName,"ColumnName", "CENTER-LEFT", "16",null);
        setColumnCellValueAndButton(previousMoveColumn, OrderKeyCombination.上移,"previousMove", "CENTER-LEFT", "16");
        setColumnCellValueAndButton(nextMoveColumn, OrderKeyCombination.下移,"nextMove", "CENTER-LEFT", "16");

        ObservableList< TableViewSetting_Bean> TableColumnList = FXCollections.observableArrayList();
        for (String ColumnName : TableViewSettingMap.keySet()) {
            TableColumnList.add(new  TableViewSetting_Bean(TableViewSettingMap.getBoolean(ColumnName), ColumnName));
        }
        TableView.setItems(TableColumnList);
    }
    /** TableView Key Pressed - 表格事件 */
    @FXML protected void TableViewKeyPressed(KeyEvent KeyEvent) throws Exception{
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) saveTableViewSetting();
        else if(previousMoveKeyCombine.match(KeyEvent))
            moveTableColumn(OrderKeyCombination.上移, ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView), ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectIndex(TableView));
        else if(nextMoveKeyCombine.match(KeyEvent))
            moveTableColumn(OrderKeyCombination.下移, ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView), ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectIndex(TableView));
    }
    /** TableView Key Released - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
             TableViewSetting_Bean  TableViewSetting_Bean = ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView);
            if( TableViewSetting_Bean != null){
                if( TableViewSetting_Bean.isCheckBoxSelect())  TableViewSetting_Bean.setCheckBoxSelect(false);
                else if(! TableViewSetting_Bean.isCheckBoxSelect())    TableViewSetting_Bean.setCheckBoxSelect(true);
            }
        }
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void SaveMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    saveTableViewSetting();
    }
    private void saveTableViewSetting() throws Exception{
        JSONObject TableViewSettingMap = createJsonObject();
        CallConfig.setPaymentCompareTableViewSettingJson(PaymentCompareTabName,TableViewSettingMap);
        PaymentCompareSystem_Controller.refreshTableView(TableViewSettingMap);
        CloseMouseClicked(null);
    }
    private JSONObject createJsonObject(){
        JSONObject JSONObject = new JSONObject(new LinkedHashMap<>());
        ObservableList< TableViewSetting_Bean> TableViewSettingList = ComponentToolKit.getSearchOrderTableViewSettingList(TableView);
        for( TableViewSetting_Bean  TableViewSetting_Bean : TableViewSettingList){
            JSONObject.put( TableViewSetting_Bean.getColumnName(), TableViewSetting_Bean.isCheckBoxSelect());
        }
        return JSONObject;
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){   if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent))               ComponentToolKit.closeThisStage(Stage);  }

    private void setColumnCellValueAndCheckBox(TableColumn< TableViewSetting_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell< TableViewSetting_Bean, Boolean> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                 TableViewSetting_Bean  TableViewSetting_Bean = ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView, getIndex());
                CheckBox CheckBox =  TableViewSetting_Bean.getCheckBox();
                setGraphic(CheckBox);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }else   setGraphic(null);
        }
    }
    private void setColumnCellValueAndButton(TableColumn< TableViewSetting_Bean, String> TableColumn, OrderKeyCombination OrderKeyCombination, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndButton(OrderKeyCombination, Alignment, FontSize));
    }
    private class setColumnCellValueAndButton extends TableCell< TableViewSetting_Bean, String> {
        final javafx.scene.control.Button Button = new Button();
        String Alignment, FontSize;
        setColumnCellValueAndButton(OrderKeyCombination OrderKeyCombination, String Alignment, String FontSize){
            Button.setText(OrderKeyCombination.name());
            Button.setId(OrderKeyCombination.name());
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String ButtonText, boolean empty) {
            super.updateItem(ButtonText, empty);
            if(!empty){
                setGraphic(Button);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                Button.setOnAction(ActionEvent -> {
                    moveTableColumn(OrderKeyCombination.valueOf(Button.getId()), ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView, getIndex()), getIndex());
                });
            }else   setGraphic(null);
        }
    }
    private void moveTableColumn(OrderKeyCombination OrderKeyCombination, TableViewSetting_Bean  TableViewSetting_Bean, int nowIndex){
        if( TableViewSetting_Bean == null)    return;
        if(OrderKeyCombination == ToolKit_Enum.OrderKeyCombination.上移){
            if(nowIndex != 0){
                TableView.getItems().remove(nowIndex);
                TableView.getItems().add(nowIndex - 1,  TableViewSetting_Bean);
                TableView.getSelectionModel().select(nowIndex);
            }
        }else if(OrderKeyCombination == ToolKit_Enum.OrderKeyCombination.下移){
            if(nowIndex != ComponentToolKit.getSearchOrderTableViewSettingList(TableView).size()-1){
                TableView.getItems().remove(nowIndex);
                TableView.getItems().add(nowIndex + 1,  TableViewSetting_Bean);
                TableView.getSelectionModel().select(nowIndex);
            }
        }
    }
}
