package ERP.Controller.Toolkit.ShowCompareIAECrawlerData;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.ManagePayableReceivable.IAECrawlerData_Bean;
import ERP.Bean.ToolKit.ShowCompareIAECrawlerData.ShowCompareIAECrawlerData_Bean;
import ERP.ERPApplication;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.HashMap;

public class ShowCompareIAECrawlerData_Controller {

    @FXML Label waitingUpdateCount_Label;
    @FXML TabPane waitingUpdateTabPane;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private HashMap<IAECrawlerData_Bean,IAECrawlerData_Bean> differentObjectMap;
    private IAECrawlerAccount_Bean IAECrawlerAccount_Bean;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private Stage Stage;

    private HashMap<Tab,HashMap<IAECrawlerData_Bean,IAECrawlerData_Bean>> recordIAECrawlerDataMap = new HashMap<>();

    public ShowCompareIAECrawlerData_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        ManagePayableReceivable_Model = ERPApplication.ToolKit.ModelToolKit.getManagePayableReceivableModel();
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setIAECrawlerAccount_Bean(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        this.IAECrawlerAccount_Bean = IAECrawlerAccount_Bean;
    }
    public void setDifferentObjectMap(HashMap<IAECrawlerData_Bean,IAECrawlerData_Bean> differentObjectMap){
        this.differentObjectMap = differentObjectMap;
    }
    public void setDifferentCount(int differentCount){
        waitingUpdateCount_Label.setText(String.valueOf(differentCount));
    }
    public void setComponent(){
        setDifferentCount(differentObjectMap.size());
        createTab();
    }
    private void createTab(){
        for(IAECrawlerData_Bean oldIAECrawlerData_Bean : differentObjectMap.keySet()){
            IAECrawlerData_Bean newIAECrawlerData_Bean = differentObjectMap.get(oldIAECrawlerData_Bean);
            TableView<ShowCompareIAECrawlerData_Bean> TableView = createTableView(oldIAECrawlerData_Bean, newIAECrawlerData_Bean);
            Tab Tab = ComponentToolKit.setTab("待更新 - " + oldIAECrawlerData_Bean.getInvoiceNumber());
            BorderPane BorderPane = ComponentToolKit.setBorderPane(10,10,0,10);
            BorderPane.setCenter(TableView);
            Tab.setContent(BorderPane);
            waitingUpdateTabPane.getTabs().add(Tab);
            waitingUpdateTabPane.getSelectionModel().select(Tab);
            newIAECrawlerData_Bean.setIAECrawlerReviewStatus(oldIAECrawlerData_Bean.getIAECrawlerReviewStatus());
            HashMap<IAECrawlerData_Bean,IAECrawlerData_Bean> differentDataMap = new HashMap<>();
            differentDataMap.put(oldIAECrawlerData_Bean,newIAECrawlerData_Bean);
            recordIAECrawlerDataMap.put(Tab,differentDataMap);
        }
    }
    private TableView<ShowCompareIAECrawlerData_Bean> createTableView(IAECrawlerData_Bean oldIAECrawlerData_Bean, IAECrawlerData_Bean newIAECrawlerData_Bean){
        TableView<ShowCompareIAECrawlerData_Bean> TableView = ComponentToolKit.createCompareIAECrawlerDataTableView();
        setTableViewItem(TableView,oldIAECrawlerData_Bean,newIAECrawlerData_Bean);
        String[] TableColumns = { "欄位名稱", "目前資訊", "待更新資訊"};
        String[] TableColumnCellValue = new String[]{"columnName","oldInfo","newInfo"};
        int[] ColumnsLength = { 80, 350, 350 };

        for(int index = 0 ; index < TableColumns.length ; index++) {
            TableColumn<ShowCompareIAECrawlerData_Bean,String> TableColumn = ComponentToolKit.createCompareIAECrawlerDataTableColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]);
            TableColumn.setSortable(false);
            TableColumn.setResizable(true);
            if(index == 2)  {
                TableColumn.setCellFactory(p -> new setTableColumnTextFill("CENTER-LEFT", "14"));
            }
            TableView.getColumns().add(TableColumn);
        }
        return TableView;
    }
    private void setTableViewItem(TableView<ShowCompareIAECrawlerData_Bean> TableView, IAECrawlerData_Bean originIAECrawlerData_Bean, IAECrawlerData_Bean IAECrawlerData_Bean){
        TableView.getItems().add(generateCompareDataBean("廠商名稱","(" + originIAECrawlerData_Bean.getInvoice_manufacturerNickName_id() + ")" + originIAECrawlerData_Bean.getInvoiceManufacturerNickName(),"(" + IAECrawlerData_Bean.getInvoice_manufacturerNickName_id() + ")" + IAECrawlerData_Bean.getInvoiceManufacturerNickName()));
        TableView.getItems().add(generateCompareDataBean("傳票編號",originIAECrawlerData_Bean.getSummonsNumber(),IAECrawlerData_Bean.getSummonsNumber()));
        TableView.getItems().add(generateCompareDataBean("付款日期",String.valueOf(originIAECrawlerData_Bean.getPayDate()),String.valueOf(IAECrawlerData_Bean.getPayDate())));
        TableView.getItems().add(generateCompareDataBean("付款金額",String.valueOf(originIAECrawlerData_Bean.getPayAmount()),String.valueOf(IAECrawlerData_Bean.getPayAmount())));
        TableView.getItems().add(generateCompareDataBean("匯費",String.valueOf(originIAECrawlerData_Bean.getRemittanceFee()),String.valueOf(IAECrawlerData_Bean.getRemittanceFee())));
        TableView.getItems().add(generateCompareDataBean("銀行帳號",originIAECrawlerData_Bean.getBankAccount(),IAECrawlerData_Bean.getBankAccount()));
        TableView.getItems().add(generateCompareDataBean("摘要",originIAECrawlerData_Bean.getInvoiceContent(),IAECrawlerData_Bean.getInvoiceContent()));
        TableView.getItems().add(generateCompareDataBean("發票日期",String.valueOf(originIAECrawlerData_Bean.getInvoiceDate()),String.valueOf(IAECrawlerData_Bean.getInvoiceDate())));
        TableView.getItems().add(generateCompareDataBean("發票號碼",originIAECrawlerData_Bean.getInvoiceNumber(),IAECrawlerData_Bean.getInvoiceNumber()));
        TableView.getItems().add(generateCompareDataBean("發票金額",originIAECrawlerData_Bean.getInvoiceAmount(),IAECrawlerData_Bean.getInvoiceAmount()));
        TableView.getItems().add(generateCompareDataBean("計畫代碼",originIAECrawlerData_Bean.getProjectCode(),IAECrawlerData_Bean.getProjectCode()));
    }
    private ShowCompareIAECrawlerData_Bean generateCompareDataBean(String columnName, String oldInfo, String newInfo){
        ShowCompareIAECrawlerData_Bean ShowCompareIAECrawlerData_Bean = new ShowCompareIAECrawlerData_Bean();
        ShowCompareIAECrawlerData_Bean.setColumnName(columnName);
        ShowCompareIAECrawlerData_Bean.setOldInfo(oldInfo);
        ShowCompareIAECrawlerData_Bean.setNewInfo(newInfo);
        return ShowCompareIAECrawlerData_Bean;
    }
    @FXML protected void updateMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Tab Tab = waitingUpdateTabPane.getSelectionModel().getSelectedItem();
            if(Tab != null){
                HashMap<IAECrawlerData_Bean, IAECrawlerData_Bean> differentDataMap = recordIAECrawlerDataMap.get(Tab);
                HashMap.Entry<IAECrawlerData_Bean,IAECrawlerData_Bean> entry = differentDataMap.entrySet().iterator().next();
                IAECrawlerData_Bean oldData_Bean = entry.getKey();
                IAECrawlerData_Bean newData_Bean = entry.getValue();

                boolean isReBinding = false;
                if(oldData_Bean.isBindingOrder()) {
                    isReBinding = true;
                    newData_Bean.setId(oldData_Bean.getId());
                }
                if(isReBinding && !DialogUI.ConfirmDialog("※ 更新後需重新綁定，是否繼續 ?",true,false,0,0))
                    return;
                else if(!isReBinding && !DialogUI.ConfirmDialog("※ 是否確定更新 ?",true,false,0,0))
                    return;
                if(ManagePayableReceivable_Model.updateIAECrawlerData(newData_Bean,true,isReBinding)){
                    waitingUpdateTabPane.getTabs().remove(Tab);
                    if(waitingUpdateTabPane.getTabs().size() == 0)
                        Stage.close();
                    else
                        waitingUpdateCount_Label.setText(String.valueOf(waitingUpdateTabPane.getTabs().size()));
                    DialogUI.MessageDialog("※ 更新成功");


                }else
                    DialogUI.MessageDialog("※ 更新失敗");
            }
        }
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            Stage.close();
    }

    private class setTableColumnTextFill extends TableCell<ShowCompareIAECrawlerData_Bean, String> {
        String Alignment, FontSize;
        setTableColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item);
                ShowCompareIAECrawlerData_Bean ShowCompareIAECrawlerData_Bean = getTableView().getItems().get(getIndex());
//                System.out.println(ShowCompareIAECrawlerData_Bean.getOldInfo() + "  " + ShowCompareIAECrawlerData_Bean.getNewInfo());
                if((ShowCompareIAECrawlerData_Bean.getOldInfo() == null && ShowCompareIAECrawlerData_Bean.getNewInfo() != null)||
                        (ShowCompareIAECrawlerData_Bean.getOldInfo() != null && ShowCompareIAECrawlerData_Bean.getNewInfo() == null)||
                        (!ShowCompareIAECrawlerData_Bean.getOldInfo().equals(ShowCompareIAECrawlerData_Bean.getNewInfo())))
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: red");
                else
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill: black");
            }
        }
    }
}
