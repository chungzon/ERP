package ERP.Controller.Toolkit.ShowSearchOrderTableViewSetting;

import ERP.Bean.ToolKit.ShowTableViewSetting.TableViewSetting_Bean;
import ERP.Controller.Order.SearchOrder.SearchOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Enum.ToolKit.ToolKit_Enum.OrderKeyCombination;
import ERP.Enum.ToolKit.ToolKit_Enum;
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

/** [Controller] Show search order TableView setting */
public class ShowSearchOrderTableViewSetting_Controller {
    @FXML TableColumn<TableViewSetting_Bean,Boolean> showTableColumn;
    @FXML TableColumn<TableViewSetting_Bean,String> ColumnName, previousMoveColumn, nextMoveColumn;
    @FXML TableView<TableViewSetting_Bean> TableView;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallConfig CallConfig;
    private final KeyCombination previousMoveKeyCombine = new KeyCodeCombination(KeyCode.UP, KeyCombination.SHIFT_DOWN);
    private final KeyCombination nextMoveKeyCombine = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN);

    private SearchOrder_Controller SearchOrder_Controller;
    private SearchOrderSource SearchOrderSource;
    private Stage Stage;
    private JSONObject TableViewSettingMap;

    public ShowSearchOrderTableViewSetting_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        CallConfig = ERPApplication.ToolKit.CallConfig;
    }
    /** Set object - [Controller] SearchOrder_Controller */
    public void setSearchOrder_Controller(SearchOrder_Controller SearchOrder_Controller){   this.SearchOrder_Controller = SearchOrder_Controller;   }
    /** Set object - [Enum] Order_Enum.SearchOrderSource */
    public void setSearchOrderSource(SearchOrderSource SearchOrderSource){  this.SearchOrderSource = SearchOrderSource; }
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
        setColumnCellValueAndButton(previousMoveColumn,OrderKeyCombination.上移,"previousMove", "CENTER-LEFT", "16");
        setColumnCellValueAndButton(nextMoveColumn,OrderKeyCombination.下移,"nextMove", "CENTER-LEFT", "16");

        ObservableList<TableViewSetting_Bean> TableColumnList = FXCollections.observableArrayList();
        for (String ColumnName : TableViewSettingMap.keySet()) {
            if(ColumnName.equals(Order_Enum.SearchOrderTableColumn.對象名稱.name())) {
                if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單 || SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.進貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.進貨退貨單)
                    TableColumnList.add(new TableViewSetting_Bean(TableViewSettingMap.getBoolean(ColumnName), "廠商名稱"));
                else if(SearchOrderSource == Order_Enum.SearchOrderSource.報價單 || SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.出貨退貨單)
                    TableColumnList.add(new TableViewSetting_Bean(TableViewSettingMap.getBoolean(ColumnName), "客戶名稱"));
            }else if(ColumnName.equals(Order_Enum.SearchOrderTableColumn.對象編號.name())) {
                if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單 || SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.進貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.進貨退貨單)
                    TableColumnList.add(new TableViewSetting_Bean(TableViewSettingMap.getBoolean(ColumnName), "廠商編號"));
                else if(SearchOrderSource == Order_Enum.SearchOrderSource.報價單 || SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.出貨退貨單)
                    TableColumnList.add(new TableViewSetting_Bean(TableViewSettingMap.getBoolean(ColumnName), "客戶編號"));
            }else
                TableColumnList.add(new TableViewSetting_Bean(TableViewSettingMap.getBoolean(ColumnName), ColumnName));
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
            TableViewSetting_Bean TableViewSetting_Bean = ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView);
            if(TableViewSetting_Bean != null){
                if(TableViewSetting_Bean.isCheckBoxSelect()) TableViewSetting_Bean.setCheckBoxSelect(false);
                else if(!TableViewSetting_Bean.isCheckBoxSelect())   TableViewSetting_Bean.setCheckBoxSelect(true);
            }
        }
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void SaveMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    saveTableViewSetting();
    }
    private void saveTableViewSetting() throws Exception{
        JSONObject TableViewSettingMap = createJsonObject();
        CallConfig.setSearchOrderTableViewSettingJson(SearchOrderSource,TableViewSettingMap);
        SearchOrder_Controller.refreshTableView(TableViewSettingMap);
        CloseMouseClicked(null);
    }
    private JSONObject createJsonObject(){
        JSONObject JSONObject = new JSONObject(new LinkedHashMap());
        ObservableList<TableViewSetting_Bean> TableViewSettingList = ComponentToolKit.getSearchOrderTableViewSettingList(TableView);
        for(TableViewSetting_Bean TableViewSetting_Bean : TableViewSettingList){
            if(TableViewSetting_Bean.getColumnName().equals("廠商編號") || TableViewSetting_Bean.getColumnName().equals("客戶編號"))
                JSONObject.put(Order_Enum.SearchOrderTableColumn.對象編號.name(), TableViewSetting_Bean.isCheckBoxSelect());
            else if(TableViewSetting_Bean.getColumnName().equals("廠商名稱") || TableViewSetting_Bean.getColumnName().equals("客戶名稱"))
                JSONObject.put(Order_Enum.SearchOrderTableColumn.對象名稱.name(), TableViewSetting_Bean.isCheckBoxSelect());
            else
                JSONObject.put(TableViewSetting_Bean.getColumnName(), TableViewSetting_Bean.isCheckBoxSelect());
        }
        return JSONObject;
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){   if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent))               ComponentToolKit.closeThisStage(Stage);  }

    private void setColumnCellValueAndCheckBox(TableColumn<TableViewSetting_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<TableViewSetting_Bean, Boolean> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                TableViewSetting_Bean TableViewSetting_Bean = ComponentToolKit.getSearchOrderTableViewSettingTableViewSelectItem(TableView, getIndex());
                CheckBox CheckBox = TableViewSetting_Bean.getCheckBox();
                setGraphic(CheckBox);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }else   setGraphic(null);
        }
    }
    private void setColumnCellValueAndButton(TableColumn<TableViewSetting_Bean, String> TableColumn, OrderKeyCombination OrderKeyCombination, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndButton(OrderKeyCombination, Alignment, FontSize));
    }
    private class setColumnCellValueAndButton extends TableCell<TableViewSetting_Bean, String> {
        final Button Button = new Button();
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
    private void moveTableColumn(OrderKeyCombination OrderKeyCombination, TableViewSetting_Bean TableViewSetting_Bean, int nowIndex){
        if(TableViewSetting_Bean == null)    return;
        if(OrderKeyCombination == ToolKit_Enum.OrderKeyCombination.上移){
            if(nowIndex != 0){
                TableView.getItems().remove(nowIndex);
                TableView.getItems().add(nowIndex - 1, TableViewSetting_Bean);
                TableView.getSelectionModel().select(nowIndex);
            }
        }else if(OrderKeyCombination == ToolKit_Enum.OrderKeyCombination.下移){
            if(nowIndex != ComponentToolKit.getSearchOrderTableViewSettingList(TableView).size()-1){
                TableView.getItems().remove(nowIndex);
                TableView.getItems().add(nowIndex + 1, TableViewSetting_Bean);
                TableView.getSelectionModel().select(nowIndex);
            }
        }
    }
}
