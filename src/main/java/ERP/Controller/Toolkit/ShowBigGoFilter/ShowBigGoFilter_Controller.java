package ERP.Controller.Toolkit.ShowBigGoFilter;

import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum.BigGoFilterSource;
import ERP.Model.ProductWaitingConfirm.ProductWaitingConfirm_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.HashMap;

public class ShowBigGoFilter_Controller {
    @FXML Tab allStore_Tab, store_Tab, bid_Tab;

    @FXML TextField bid_storeName_textField, bid_linkName_textField;

    @FXML TableColumn<BigGoFilter_Bean,Boolean> allStore_select_tableColumn,store_select_tableColumn,bid_select_tableColumn;
    @FXML TableColumn<BigGoFilter_Bean,Integer> allStore_itemNumber_tableColumn, store_itemNumber_tableColumn, bid_itemNumber_tableColumn;
    @FXML TableColumn<BigGoFilter_Bean,String> allStore_storeName_tableColumn, allStore_linkName_tableColumn,
            store_storeName_tableColumn, store_linkName_tableColumn,
            bid_storeName_tableColumn, bid_linkName_tableColumn;
    @FXML TableView<BigGoFilter_Bean> allStore_tableView, store_tableView, bid_tableView;

    private ComponentToolKit componentToolKit;
    private KeyPressed keyPressed;
    private ProductWaitingConfirm_Model productWaitingConfirm_Model;

    private Stage stage;
    private HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap;
    private TableView<BigGoFilter_Bean> selectTableView;
    private HashMap<BigGoFilter_Bean,CheckBox> bigGoFilterCheckBoxMap = new HashMap<>();
    public ShowBigGoFilter_Controller(){
        ToolKit toolKit = ERPApplication.ToolKit;
        this.componentToolKit = toolKit.ComponentToolKit;
        this.keyPressed = toolKit.KeyPressed;

        this.productWaitingConfirm_Model = toolKit.ModelToolKit.getProductWaitingConfirmModel();
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setBigGoFilterMap(HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap){
        this.bigGoFilterMap = bigGoFilterMap;
    }
    public void setComponent(){
        initialTableView();
        this.selectTableView = allStore_tableView;
        refreshTableViewData(BigGoFilterSource.未篩選);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(allStore_select_tableColumn,"selectCheckBox", "CENTER", "16");
        componentToolKit.setColumnCellValue(allStore_itemNumber_tableColumn,"itemNumber", "CENTER", "16",null);
        componentToolKit.setColumnCellValue(allStore_select_tableColumn,"select", "CENTER-LEFT", "16",null);
        componentToolKit.setColumnCellValue(allStore_storeName_tableColumn,"storeName", "CENTER-LEFT", "16",null);
        componentToolKit.setColumnCellValue(allStore_linkName_tableColumn,"linkName", "CENTER-LEFT", "16",null);

        setColumnCellValueAndCheckBox(store_select_tableColumn,"selectCheckBox", "CENTER", "16");
        componentToolKit.setColumnCellValue(store_itemNumber_tableColumn,"itemNumber", "CENTER", "16",null);
        componentToolKit.setColumnCellValue(store_select_tableColumn,"select", "CENTER-LEFT", "16",null);
        componentToolKit.setColumnCellValue(store_storeName_tableColumn,"storeName", "CENTER-LEFT", "16",null);
        componentToolKit.setColumnCellValue(store_linkName_tableColumn,"linkName", "CENTER-LEFT", "16",null);

        setColumnCellValueAndCheckBox(bid_select_tableColumn,"selectCheckBox", "CENTER", "16");
        componentToolKit.setColumnCellValue(bid_itemNumber_tableColumn,"itemNumber", "CENTER", "16",null);
        componentToolKit.setColumnCellValue(bid_select_tableColumn,"select", "CENTER-LEFT", "16",null);
        componentToolKit.setColumnCellValue(bid_storeName_tableColumn,"storeName", "CENTER-LEFT", "16",null);
        bid_storeName_tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        componentToolKit.setColumnCellValue(bid_linkName_tableColumn,"linkName", "CENTER-LEFT", "16",null);
        bid_linkName_tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    @FXML protected void allStoreTabSelectChanged(){
        if(allStore_Tab.isSelected()){
            selectTableView = allStore_tableView;
            refreshTableViewData(BigGoFilterSource.未篩選);
        }
    }
    @FXML protected void storeTabSelectChanged(){
        if(store_Tab.isSelected()){
            selectTableView = store_tableView;
            refreshTableViewData(BigGoFilterSource.商城);
        }
    }
    @FXML protected void bidTabSelectChanged(){
        if(bid_Tab.isSelected()){
            selectTableView = bid_tableView;
            refreshTableViewData(BigGoFilterSource.拍賣);
        }
    }
    @FXML protected void allStore_addStore_mouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<BigGoFilter_Bean> bigGoFilterList = componentToolKit.getBigGoFilterSelectList(selectTableView);
            if(bigGoFilterList.size() == 0){
                DialogUI.MessageDialog("請選擇商店");
            }else{
                for(BigGoFilter_Bean bigGoFilter_Bean : bigGoFilterList){
                    if(productWaitingConfirm_Model.modifyBigGoFilterSource(bigGoFilter_Bean, BigGoFilterSource.商城)){
                        changeBigGoFilterSource(bigGoFilter_Bean, BigGoFilterSource.未篩選, BigGoFilterSource.商城);
                    }
                }
                refreshTableViewData(BigGoFilterSource.未篩選);
                DialogUI.MessageDialog("新增成功");
            }
        }
    }
    @FXML protected void updateOnlineStore_mouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            HashMap<String, BigGoFilter_Bean> filterMap = new HashMap<>();
            for(BigGoFilterSource bigGoFilterSource : bigGoFilterMap.keySet()){
                ObservableList<BigGoFilter_Bean> bigGoFilterList = bigGoFilterMap.get(bigGoFilterSource);
                for(BigGoFilter_Bean bigGoFilter_Bean : bigGoFilterList){
                    filterMap.put(bigGoFilter_Bean.getLinkName(),bigGoFilter_Bean);
                }
            }
            ObservableList<BigGoFilter_Bean> newBigGoFilterList = productWaitingConfirm_Model.refreshBigGoStore(filterMap);
            if(newBigGoFilterList != null) {
                bigGoFilterMap.get(BigGoFilterSource.未篩選).addAll(newBigGoFilterList);
                refreshTableViewData(BigGoFilterSource.未篩選);
                DialogUI.MessageDialog("更新完成");
            }else{
                DialogUI.MessageDialog("更新失敗");
            }
        }
    }
    @FXML protected void store_removeStore_mouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<BigGoFilter_Bean> bigGoFilterList = componentToolKit.getBigGoFilterSelectList(selectTableView);
            if(bigGoFilterList.size() == 0){
                DialogUI.MessageDialog("請選擇商店");
            }else{
                for(BigGoFilter_Bean bigGoFilter_Bean : bigGoFilterList){
                    if(productWaitingConfirm_Model.modifyBigGoFilterSource(bigGoFilter_Bean, BigGoFilterSource.未篩選)){
                        changeBigGoFilterSource(bigGoFilter_Bean, BigGoFilterSource.商城, BigGoFilterSource.未篩選);
                    }
                }
                refreshTableViewData(BigGoFilterSource.商城);
                DialogUI.MessageDialog("移除成功");
            }
        }
    }
    @FXML protected void bid_addBid_mouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            String storeName = bid_storeName_textField.getText(), linkName = bid_linkName_textField.getText();
            if(storeName == null || storeName.equals("")){
                DialogUI.MessageDialog("請輸入拍賣名稱");
                return;
            }else if(linkName == null || linkName.equals("")){
                DialogUI.MessageDialog("請輸入拍賣連結");
                return;
            }else if(productWaitingConfirm_Model.isBigGoFilterExist(linkName)){
                DialogUI.MessageDialog("拍賣連結已存在!");
                return;
            }
            BigGoFilter_Bean bigGoFilter_Bean = productWaitingConfirm_Model.insertBigGoStore(storeName,linkName,BigGoFilterSource.拍賣);
            if(bigGoFilter_Bean != null){
                bigGoFilterMap.get(BigGoFilterSource.拍賣).add(bigGoFilter_Bean);
                refreshTableViewData(BigGoFilterSource.拍賣);
                DialogUI.MessageDialog("新增成功");
            }else{
                DialogUI.MessageDialog("新增失敗!");
            }
        }
    }

    @FXML protected void bid_removeBid_MouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<BigGoFilter_Bean> bigGoFilterList = componentToolKit.getBigGoFilterSelectList(selectTableView);
            if(bigGoFilterList.size() == 0){
                DialogUI.MessageDialog("請選擇商店");
            }else if(DialogUI.ConfirmDialog("確定要移除所選拍賣 ?",true,false,0,0)){
                for(BigGoFilter_Bean bigGoFilter_Bean : bigGoFilterList){
                    if(productWaitingConfirm_Model.setBigGoFilterExistStatus(bigGoFilter_Bean.getId(),false)){
                        bigGoFilterMap.get(BigGoFilterSource.拍賣).remove(bigGoFilter_Bean);
                    }
                }
                refreshTableViewData(BigGoFilterSource.拍賣);
                DialogUI.MessageDialog("移除成功");
            }
        }
    }
    @FXML protected void bid_storeName_editCommit(TableColumn.CellEditEvent<BigGoFilter_Bean,String> storeNameOnEditCommit){
        BigGoFilter_Bean bigGoFilter_Bean = componentToolKit.getBigGoFilterTableViewSelectItem(selectTableView);
        if(DialogUI.ConfirmDialog("是否確定修改 ?",true,false,0,0)) {
            bigGoFilter_Bean.setStoreName(storeNameOnEditCommit.getNewValue());
            if(productWaitingConfirm_Model.updateBigGoFilter(bigGoFilter_Bean.getId(),bigGoFilter_Bean.getStoreName(),bigGoFilter_Bean.getLinkName()))
                  DialogUI.MessageDialog("※ 修改成功");
            else
                  DialogUI.MessageDialog("※ 修改失敗");
        }else
            bigGoFilter_Bean.setStoreName(storeNameOnEditCommit.getOldValue());
    }
    @FXML protected void bid_linkName_editCommit(TableColumn.CellEditEvent<BigGoFilter_Bean,String> storeNameOnEditCommit){
        BigGoFilter_Bean bigGoFilter_Bean = componentToolKit.getBigGoFilterTableViewSelectItem(selectTableView);
        if(DialogUI.ConfirmDialog("是否確定修改 ?",true,false,0,0)) {
            bigGoFilter_Bean.setLinkName(storeNameOnEditCommit.getNewValue());
            if(productWaitingConfirm_Model.updateBigGoFilter(bigGoFilter_Bean.getId(),bigGoFilter_Bean.getStoreName(),bigGoFilter_Bean.getLinkName()))
                DialogUI.MessageDialog("※ 修改成功");
            else
                DialogUI.MessageDialog("※ 修改失敗");
        }else
            bigGoFilter_Bean.setLinkName(storeNameOnEditCommit.getOldValue());
    }
    @FXML protected void close_mouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            componentToolKit.closeThisStage(stage);
        }
    }
    @FXML protected void allStoreTableViewKeyReleased(KeyEvent KeyEvent){
        if(keyPressed.isSpaceKeyPressed(KeyEvent)){
            BigGoFilter_Bean bigGoFilter_Bean = componentToolKit.getBigGoFilterTableViewSelectItem(selectTableView);
            if(bigGoFilter_Bean.isChoose())    bigGoFilter_Bean.setChoose(false);
            else if(!bigGoFilter_Bean.isChoose())  bigGoFilter_Bean.setChoose(true);
            bigGoFilterCheckBoxMap.get(bigGoFilter_Bean).setSelected(bigGoFilter_Bean.isChoose());
        }
    }
    @FXML protected void storeTableViewKeyReleased(KeyEvent KeyEvent){
        if(keyPressed.isSpaceKeyPressed(KeyEvent)){
            BigGoFilter_Bean bigGoFilter_Bean = componentToolKit.getBigGoFilterTableViewSelectItem(selectTableView);
            if(bigGoFilter_Bean.isChoose())    bigGoFilter_Bean.setChoose(false);
            else if(!bigGoFilter_Bean.isChoose())  bigGoFilter_Bean.setChoose(true);
            bigGoFilterCheckBoxMap.get(bigGoFilter_Bean).setSelected(bigGoFilter_Bean.isChoose());
        }
    }
    @FXML protected void bidTableViewKeyReleased(KeyEvent KeyEvent){
        if(keyPressed.isSpaceKeyPressed(KeyEvent)){
            BigGoFilter_Bean bigGoFilter_Bean = componentToolKit.getBigGoFilterTableViewSelectItem(selectTableView);
            if(bigGoFilter_Bean.isChoose())    bigGoFilter_Bean.setChoose(false);
            else if(!bigGoFilter_Bean.isChoose())  bigGoFilter_Bean.setChoose(true);
            bigGoFilterCheckBoxMap.get(bigGoFilter_Bean).setSelected(bigGoFilter_Bean.isChoose());
        }
    }
    private void changeBigGoFilterSource(BigGoFilter_Bean bigGoFilter_Bean, BigGoFilterSource oldSource, BigGoFilterSource newSource){
        bigGoFilter_Bean.setSource(newSource);
        bigGoFilterMap.get(newSource).add(bigGoFilter_Bean);
        bigGoFilterMap.get(oldSource).remove(bigGoFilter_Bean);
    }
    private void refreshTableViewData(BigGoFilterSource bigGoFilterSource){
        selectTableView.getItems().clear();
        if(bigGoFilterMap != null){
            ObservableList<BigGoFilter_Bean> bigGoFilterList = bigGoFilterMap.get(bigGoFilterSource);
            sortBigGoFilterById(bigGoFilterList);
            for(int index = 0; index < bigGoFilterList.size(); index++){
                BigGoFilter_Bean bigGoFilter_Bean = bigGoFilterList.get(index);
                bigGoFilter_Bean.setChoose(false);
                bigGoFilter_Bean.setItemNumber((index+1));
                selectTableView.getItems().add(bigGoFilter_Bean);
            }
        }
    }
    protected void sortBigGoFilterById(ObservableList<BigGoFilter_Bean> bigGoFilterList){
        Comparator<BigGoFilter_Bean> comparator = Comparator.comparing(BigGoFilter_Bean::getId);
        FXCollections.sort(bigGoFilterList, comparator);
    }
    private void setColumnCellValueAndCheckBox(TableColumn<BigGoFilter_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        componentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<BigGoFilter_Bean, Boolean> {
        final CheckBox CheckBox = new CheckBox();
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                setGraphic(CheckBox);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                BigGoFilter_Bean BigGoFilter_Bean = componentToolKit.getBigGoFilterTableViewSelectItem(selectTableView, getIndex());
                bigGoFilterCheckBoxMap.put(BigGoFilter_Bean,CheckBox);

                CheckBox.setSelected(BigGoFilter_Bean.isChoose());

                CheckBox.setOnAction(ActionEvent -> {
                    if(CheckBox.isSelected())   BigGoFilter_Bean.setChoose(true);
                    else    BigGoFilter_Bean.setChoose(false);
                });
            }else   setGraphic(null);
        }
    }
}
