package ERP.ToolKit;

import ERP.Bean.ManagePayableReceivable.*;
import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.*;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.Product.ManageProductOnShelf_Bean;
import ERP.Bean.ProductWaitConfirm.VendorCategory_Bean;
import ERP.Bean.ProductWaitConfirm.Vendor_Bean;
import ERP.Bean.ProductWaitConfirm.WaitConfirmProductInfo_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Item_Bean;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.IpCamInfo_Bean;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.WebCamInfo_Bean;
import ERP.Bean.SystemSetting.*;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.Bean.ToolKit.ShowCompareIAECrawlerData.ShowCompareIAECrawlerData_Bean;
import ERP.Bean.ToolKit.ShowOrderReference.OrderReference_Bean;
import ERP.Bean.ToolKit.ShowTableViewSetting.TableViewSetting_Bean;
import ERP.Bean.ToolKit.TransactionDetail.TransactionDetail_Bean;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/** All component toolkit */
public class ComponentToolKit{
    private ToolKit ToolKit;
    ComponentToolKit(ToolKit ToolKit){
        this.ToolKit = ToolKit;
    }
    public Stage setStage(){
        return new Stage();
    }
    public Stage setStage(String Title, boolean setMaximized, boolean Resizable){
        Stage Stage = new Stage();
        Stage.setTitle(Title);
        Stage.setMaximized(setMaximized);
        Stage.setResizable(Resizable);
        return Stage;
    }
    /** Initial GridPane */
    public GridPane setGridPane(Pos Alignment, int top, int right, int bottom, int left, int Hgap){
        GridPane GridPane = new GridPane();
        GridPane.setPadding(new Insets(top, right, bottom, left));
        GridPane.setAlignment(Alignment);
        GridPane.setHgap(Hgap);
        return GridPane;
    }
    /** Set GridPane disable
     * @param Disable GirdPane disable status
     * */
    public void setGridPaneDisable(GridPane GridPane, boolean Disable){ GridPane.setDisable(Disable);   }
    /** Set GridPane visible
     * @param Visible GridPane visible status
     * */
    public void setGridPaneVisible(GridPane GridPane, boolean Visible){ GridPane.setVisible(Visible);   }
    /** Close Stage */
    public void closeThisStage(Stage Stage){  Stage.close();  }
    /** Initial BorderPane */
    public BorderPane setBorderPane(int top, int right, int bottom, int left){
        BorderPane BorderPane = new BorderPane();
        BorderPane.setPadding(new Insets(top, right, bottom, left));
        return BorderPane;
    }
    /** Initial TableView */
    public TableView<OrderProduct_Bean> createOrderProductTableView(){ return new TableView<>();   }
    /** Initial TableColumn */
    public TableColumn<OrderProduct_Bean,CheckBox> createOrderProductTableCheckBoxColumn(String Text, int ColumnLength, String ColumnValue){
        TableColumn<OrderProduct_Bean,CheckBox> TableColumn = new TableColumn<>();
        TableColumn.setText(Text);
        TableColumn.setPrefWidth(ColumnLength);
        setColumnCellValue(TableColumn, ColumnValue, "CENTER", "14",null);
        TableColumn.impl_setReorderable(false);
        TableColumn.setResizable(false);
        TableColumn.setSortable(false);
        return TableColumn;
    }
    public TableColumn<OrderProduct_Bean,Integer> createOrderProductTableIntegerColumn(String Text, int ColumnLength, String ColumnValue){
        TableColumn<OrderProduct_Bean,Integer> TableColumn = new TableColumn<>();
        TableColumn.setText(Text);
        TableColumn.setPrefWidth(ColumnLength);
        setColumnCellValue(TableColumn, ColumnValue, "CENTER-LEFT", "14",null);
        TableColumn.impl_setReorderable(false);
        TableColumn.setResizable(false);
        TableColumn.setSortable(false);
        return TableColumn;
    }
    public TableColumn<OrderProduct_Bean,Double> createOrderProductTableDoubleColumn(String Text, int ColumnLength, String ColumnValue){
        TableColumn<OrderProduct_Bean,Double> TableColumn = new TableColumn<>();
        TableColumn.setText(Text);
        TableColumn.setPrefWidth(ColumnLength);
        setColumnCellValue(TableColumn, ColumnValue, "CENTER-LEFT", "14",null);
        TableColumn.impl_setReorderable(false);
        TableColumn.setResizable(false);
        TableColumn.setSortable(false);
        return TableColumn;
    }
    public TableColumn<OrderProduct_Bean,String> createOrderProductTableStringColumn(String Text, int ColumnLength, String ColumnValue){
        TableColumn<OrderProduct_Bean,String> TableColumn = new TableColumn<>();
        TableColumn.setText(Text);
        TableColumn.setPrefWidth(ColumnLength);
        setColumnCellValue(TableColumn, ColumnValue, "CENTER-LEFT", "14",null);
        TableColumn.impl_setReorderable(false);
        TableColumn.setResizable(false);
        TableColumn.setSortable(false);
        return TableColumn;
    }
    public TableView<ProductGroup_Bean> createProductGroupTableView(){ return new TableView<>();   }
    public TableColumn<ProductGroup_Bean,Integer> createProductGroupTableIntegerColumn(String Text, int ColumnLength, String ColumnValue){
        TableColumn<ProductGroup_Bean,Integer> TableColumn = new TableColumn<>();
        TableColumn.setText(Text);
        TableColumn.setPrefWidth(ColumnLength);
        setColumnCellValue(TableColumn, ColumnValue, "CENTER-LEFT", "14",null);
        TableColumn.impl_setReorderable(false);
        TableColumn.setResizable(false);
        TableColumn.setSortable(false);
        return TableColumn;
    }

    public TableView<ShowCompareIAECrawlerData_Bean> createCompareIAECrawlerDataTableView(){    return new TableView<>();}
    public TableColumn<ShowCompareIAECrawlerData_Bean,String> createCompareIAECrawlerDataTableColumn(String Text, int ColumnLength, String ColumnValue){
        TableColumn<ShowCompareIAECrawlerData_Bean,String> TableColumn = new TableColumn<>();
        TableColumn.setText(Text);
        TableColumn.setPrefWidth(ColumnLength);
        setColumnCellValue(TableColumn, ColumnValue, "CENTER-LEFT", "14",null);
        TableColumn.impl_setReorderable(false);
        TableColumn.setResizable(false);
        return TableColumn;
    }
    /** Set TableColumn visible
     * @param Visible TableColumn visible status
     * */
    public void setTableColumnVisible(TableColumn TableColumn , boolean Visible){   TableColumn.setVisible(Visible);    }
    /** Initial Tab */
    public Tab setTab(String Text){
        return new Tab(Text);
    }
    /** Initial Label */
    public Label setLabel(String Title, int prefWidth, int prefHeight, int FontSize, String Style){
        Label Label = new Label(Title);
        Label.setPrefSize(prefWidth,prefHeight);
        Label.setFont(new Font("微軟正黑體", FontSize));
        Label.setStyle(Style);
        return Label;
    }
    public void setLabelStyle(Label Label, String style){   Label.setStyle(style);  }
    public void setLabelVisible(Label Label, boolean Visible){
        Label.setVisible(Visible);
    }
    public TextArea setTextArea(String text, int prefWidth, int prefHeight, int fontSize){
        TextArea TextArea = new TextArea();
        TextArea.setText(text);
        TextArea.setPrefSize(prefWidth,prefHeight);
        TextArea.setFont(new Font("微軟正黑體", fontSize));
        return TextArea;
    }
    /** Initial Button */
    public Button setButton(String Title, int prefWidth, int prefHeight, int FontSize){
        Button Button = new Button(Title);
        Button.setFont(new Font("微軟正黑體", FontSize));
        Button.setPrefSize(prefWidth,prefHeight);
        return Button;
    }
    /** Set Button disable
     * @param Disable Button disable status
     * */
    public void setButtonDisable(Button Button, boolean Disable){   Button.setDisable(Disable); }
    public void setSplitMenuButtonDisable(SplitMenuButton SplitMenuButton, boolean Disable){   SplitMenuButton.setDisable(Disable); }
    public void setSplitMenuButtonVisible(SplitMenuButton SplitMenuButton, boolean visible){   SplitMenuButton.setVisible(visible); }
    public RadioButton getSplitMenuButton_RadioButtonSelected(SplitMenuButton SplitMenuButton){
        ObservableList<MenuItem> menuItems = SplitMenuButton.getItems();
        for(MenuItem menuItem : menuItems){
            RadioButton radioButton = (RadioButton)((CustomMenuItem) menuItem).getContent();
            if(radioButton.isSelected())
                return radioButton;
        }
        return null;
    }
    /** Set Button visible
     * @param Visible Button visible status
     * */
    public void setButtonVisible(Button Button, boolean Visible){   Button.setVisible(Visible); }
    public void setButtonStyle(Button Button, String Style){    Button.setStyle(Style); }

    public MenuButton setMenuButton(String Title, int prefWidth, int prefHeight, int FontSize){
        MenuButton MenuButton = new MenuButton(Title);
        MenuButton.setFont(new Font("微軟正黑體", FontSize));
        MenuButton.setPrefSize(prefWidth,prefHeight);
        return MenuButton;
    }
    public void setMenuButtonVisible(MenuButton MenuButton, boolean visible){   MenuButton.setVisible(visible); }
    public void setMenuButtonDisable(MenuButton MenuButton, boolean disable){   MenuButton.setDisable(disable); }
    public MenuItem setMenuItem(String Title, String id){
        MenuItem MenuItem = new MenuItem(Title);
        MenuItem.setId(id);
        return MenuItem;
    }
    public void setMenuItemDisable(MenuItem MenuItem, boolean disable){ MenuItem.setDisable(disable);   }

    public RadioButton setRadioButton(String text, int prefWidth, int prefHeight, int FontSize){
        RadioButton RadioButton = new RadioButton();
        RadioButton.setText(text);
        RadioButton.setFont(new Font("微軟正黑體", FontSize));
        RadioButton.setPrefSize(prefWidth,prefHeight);
        return RadioButton;
    }
    public void setRadioButtonVisible(RadioButton RadioButton, boolean Visible){    RadioButton.setVisible(Visible);    }
    public void setRadioButtonDisable(RadioButton RadioButton, boolean disable){    RadioButton.setDisable(disable);    }
    public void setRadioButtonSelect(RadioButton RadioButton, boolean select){  RadioButton.setSelected(select);    }

    public CheckBox initialCheckBox(String Text, int fontSize){
        CheckBox CheckBox = new CheckBox();
        CheckBox.setText(Text);
        CheckBox.setId(CheckBox.getText());
        CheckBox.setFont(new Font("微軟正黑體", fontSize));
        return CheckBox;
    }
    /** Set CheckBox selected
     * @param Select CheckBox select status
     * */
    public void setCheckBoxSelect(CheckBox CheckBox, boolean Select){   CheckBox.setSelected(Select);   }
    /** Set CheckBox disable
     * @param Disable CheckBox disable status
     * */
    public void setCheckBoxDisable(CheckBox CheckBox, boolean Disable){
        CheckBox.setDisable(Disable);
    }
    public void setCheckBoxVisible(CheckBox CheckBox, boolean visible){
        CheckBox.setVisible(visible);
    }

    public void setCheckBoxStyle(CheckBox CheckBox, String Style){
        CheckBox.setStyle(Style);
    }
    /** Set ComboBox disable
     * @param Disable ComboBox disable status
     * */
    public void setComboBoxDisable(ComboBox ComboBox, boolean Disable){ ComboBox.setDisable(Disable);   }
    /** Initial HBox */
    public HBox setHBox(Pos Alignment, int Spacing, int PaddingTop, int PaddingRight, int PaddingBottom, int PaddingLeft){
        HBox HBox = new HBox();
        HBox.setAlignment(Alignment);
        HBox.setPadding(new Insets(PaddingTop, PaddingRight, PaddingBottom, PaddingLeft));
        HBox.setSpacing(Spacing);
        return HBox;
    }
    public ObservableList<Node> getHBoxChildren(HBox hBox){ return hBox.getChildren();  }
    /** Set HBox disable
     * @param Disable HBox disable status
     * */
    public void setHBoxDisable(HBox HBox, boolean Disable){
        HBox.setDisable(Disable);
    }
    /** Set HBox visible
     * @param Visible HBox visible status
     * */
    public void setHBoxVisible(HBox HBox, boolean Visible){ HBox.setVisible(Visible);   }
    /** Set VBox visible
     * @param Visible VBox disable status
     * */
    public void setVBoxVisible(VBox VBox, boolean Visible){ VBox.setVisible(Visible);   }
    public int getVBoxSize(VBox vBox){  return vBox.getChildren().size();   }
    public ObservableList<Node> getVBoxChildren(VBox vBox){ return vBox.getChildren();  }
    /** Initial StackPane */
    public StackPane setPictureStackPane(Image Image, int prefWidth, int prefHeight){
        StackPane StackPane = new StackPane();
        StackPane.setPrefSize(prefWidth,prefHeight);
        ImageView PictureView = new ImageView();
        PictureView.setImage(Image);
        PictureView.setFitWidth(prefWidth);
        PictureView.setFitHeight(prefHeight);
        StackPane.getChildren().add(PictureView);
        return StackPane;
    }
    public void setProgressBarVisible(ProgressBar ProgressBar, boolean Visible){    ProgressBar.setVisible(Visible);    }
    public void setProgressIndicatorVisible(ProgressIndicator ProgressIndicator,boolean visible){   ProgressIndicator.setVisible(visible);  }
    /** Initial Scene */
    public Scene setScene(Parent Parent){
        return new Scene(Parent);
    }
    /** Initial Scene include parameters */
    public Scene setScene(Parent Parent, int width, int height){
        return new Scene(Parent, width, height);
    }
    /** Initial ComboBox Object - [ERP.Bean] ObjectInfo_Bean */
    public void setObjectInfoBeanComboBoxObj(ComboBox<ObjectInfo_Bean> ComboBox){
        ComboBox.setButtonCell(new ObjectInfoBeanComboBoxObj());
        ComboBox.setCellFactory(Obj -> new ObjectInfoBeanComboBoxObj());
    }
    private class ObjectInfoBeanComboBoxObj extends ListCell<ObjectInfo_Bean> {
        @Override
        protected void updateItem(ObjectInfo_Bean ObjectInfo_Bean, boolean empty) {
            super.updateItem(ObjectInfo_Bean, empty);
            if (ObjectInfo_Bean != null){
                if(ObjectInfo_Bean.getObjectID() == null && ObjectInfo_Bean.getObjectNickName() == null)    setText("");
                else    setText(ObjectInfo_Bean.getObjectID() + "  " + ObjectInfo_Bean.getObjectNickName());
            }
        }
    }
    public void setIpCamInfoBeanComboBoxObj(ComboBox<IpCamInfo_Bean> ComboBox){
        ComboBox.setButtonCell(new IpCamInfoBeanComboBoxObj());
        ComboBox.setCellFactory(Obj -> new IpCamInfoBeanComboBoxObj());
    }
    private class IpCamInfoBeanComboBoxObj extends ListCell<IpCamInfo_Bean> {
        @Override
        protected void updateItem(IpCamInfo_Bean IpCamInfo_Bean, boolean empty) {
            super.updateItem(IpCamInfo_Bean, empty);
            if (IpCamInfo_Bean != null){
                setText(IpCamInfo_Bean.getName());
            }
        }
    }
    public void setWebCamInfoBeanComboBoxObj(ComboBox<WebCamInfo_Bean> ComboBox){
        ComboBox.setButtonCell(new WebCamInfoBeanComboBoxObj());
        ComboBox.setCellFactory(Obj -> new WebCamInfoBeanComboBoxObj());
    }
    private class WebCamInfoBeanComboBoxObj extends ListCell<WebCamInfo_Bean> {
        @Override
        protected void updateItem(WebCamInfo_Bean WebCamInfo_Bean, boolean empty) {
            super.updateItem(WebCamInfo_Bean, empty);
            if (WebCamInfo_Bean != null){
                setText(WebCamInfo_Bean.getWebCamName());
            }
        }
    }

    /** Initial ComboBox Object - [ERP.Bean] CompanyBank_Bean */
    public void setCompanyBankInfoBeanComboBoxObj(ComboBox<CompanyBank_Bean> ComboBox){
        ComboBox.setButtonCell(new CompanyBankInfoBeanComboBoxObj());
        ComboBox.setCellFactory(Obj -> new CompanyBankInfoBeanComboBoxObj());
    }
    private class CompanyBankInfoBeanComboBoxObj extends ListCell<CompanyBank_Bean> {
        @Override
        protected void updateItem(CompanyBank_Bean CompanyBank_Bean, boolean empty) {
            super.updateItem(CompanyBank_Bean, empty);
            if (CompanyBank_Bean != null)   setText(CompanyBank_Bean.getAccountNickName());
            else    setText("");
        }
    }
    /** Initial ComboBox Object - [ERP.Bean] BankInfo_Bean */
    public void setAllBankBeanComboBoxObj(ComboBox<BankInfo_Bean> ComboBox){
        ComboBox.setButtonCell(new AllBankBeanComboBoxObj());
        ComboBox.setCellFactory(Obj -> new AllBankBeanComboBoxObj());
    }
    private class AllBankBeanComboBoxObj extends ListCell<BankInfo_Bean> {
        @Override
        protected void updateItem(BankInfo_Bean BankInfo_Bean, boolean empty) {
            super.updateItem(BankInfo_Bean, empty);
            if (BankInfo_Bean != null && BankInfo_Bean.getBankCode() != null && BankInfo_Bean.getBankNickName() != null)
                setText(BankInfo_Bean.getBankCode() + "  " + BankInfo_Bean.getBankNickName());
            else    setText("");
        }
    }
    /** Initial ComboBox Object - [ERP.Bean] Vendor_Bean */
    public void setVendorComboBoxObj(ComboBox<Vendor_Bean> ComboBox){
        ComboBox.setButtonCell(new VendorComboBoxObj());
        ComboBox.setCellFactory(Obj -> new VendorComboBoxObj());
    }
    private class VendorComboBoxObj extends ListCell<Vendor_Bean> {
        @Override
        protected void updateItem(Vendor_Bean Vendor_Bean, boolean empty) {
            super.updateItem(Vendor_Bean, empty);
            if (Vendor_Bean != null)  setText(Vendor_Bean.getVendorName());
        }
    }
    /** Initial ComboBox Object - [ERP.Bean] VendorCategory_Bean */
    public void setVendorCategoryComboBoxObj(ComboBox<VendorCategory_Bean> ComboBox){
        ComboBox.setButtonCell(new VendorCategoryComboBoxObj());
        ComboBox.setCellFactory(Obj -> new VendorCategoryComboBoxObj());
    }
    private class VendorCategoryComboBoxObj extends ListCell<VendorCategory_Bean> {
        @Override
        protected void updateItem(VendorCategory_Bean VendorCategory_Bean, boolean empty) {
            super.updateItem(VendorCategory_Bean, empty);
            if (VendorCategory_Bean != null)  setText(VendorCategory_Bean.getCategoryName());
        }
    }
    public void setCheckoutStatusBeanComboBoxObj(ComboBox<CheckoutStatus_Bean> ComboBox){
        ComboBox.setButtonCell(new CheckoutStatusBeanComboBoxObj());
        ComboBox.setCellFactory(Obj -> new CheckoutStatusBeanComboBoxObj());
    }
    private class CheckoutStatusBeanComboBoxObj extends ListCell<CheckoutStatus_Bean> {
        @Override
        protected void updateItem(CheckoutStatus_Bean CheckoutStatus_Bean, boolean empty) {
            super.updateItem(CheckoutStatus_Bean, empty);
            if (CheckoutStatus_Bean != null){
                setText(CheckoutStatus_Bean.getCheckStatus());
            }
        }
    }
    public void setIAECrawlerAccountComboBoxObj_ExportQuotation(ComboBox<IAECrawlerAccount_Bean> ComboBox){
        ComboBox.setButtonCell(new IAECrawlerAccountComboBoxObj());
        ComboBox.setCellFactory(Obj -> new IAECrawlerAccountComboBoxObj());
    }
    public class IAECrawlerAccountComboBoxObj extends ListCell<IAECrawlerAccount_Bean>{
        @Override
        protected void updateItem(IAECrawlerAccount_Bean IAECrawlerAccount_Bean, boolean empty) {
            super.updateItem(IAECrawlerAccount_Bean, empty);
            if (IAECrawlerAccount_Bean != null){
                if(IAECrawlerAccount_Bean.isExportQuotation())
                    setText("(√)  " + IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
                else
                    setText("(X)  " + IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
            }
        }
    }
    public void setIAECrawlerAccountComboBoxObj_PurchaseNote(ComboBox<IAECrawlerAccount_Bean> ComboBox){
        ComboBox.setButtonCell(new setIAECrawlerAccountComboBoxObj_PurchaseNote());
        ComboBox.setCellFactory(Obj -> new setIAECrawlerAccountComboBoxObj_PurchaseNote());
    }
    public class setIAECrawlerAccountComboBoxObj_PurchaseNote extends ListCell<IAECrawlerAccount_Bean>{
        @Override
        protected void updateItem(IAECrawlerAccount_Bean IAECrawlerAccount_Bean, boolean empty) {
            super.updateItem(IAECrawlerAccount_Bean, empty);
            if (IAECrawlerAccount_Bean != null){
                if(IAECrawlerAccount_Bean.getObjectInfo_Bean() == null)
                    setText("所有廠商");
                else
                    setText(IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID() + " " + IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectNickName());
            }
        }
    }
    public void setIAECrawlerAccountComboBoxObj_ManufacturerContactDetail(ComboBox<IAECrawlerAccount_Bean> ComboBox){
        ComboBox.setButtonCell(new setIAECrawlerAccountComboBoxObj_ObjectIDAndObjectNickName());
        ComboBox.setCellFactory(Obj -> new setIAECrawlerAccountComboBoxObj_ObjectIDAndObjectNickName());
    }
    public class setIAECrawlerAccountComboBoxObj_ObjectIDAndObjectNickName extends ListCell<IAECrawlerAccount_Bean>{
        @Override
        protected void updateItem(IAECrawlerAccount_Bean IAECrawlerAccount_Bean, boolean empty) {
            super.updateItem(IAECrawlerAccount_Bean, empty);
            if (IAECrawlerAccount_Bean != null)  setText(IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID() + " " + IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectNickName());
        }
    }

    /** Initial ComboBox Object - [ERP.Bean] SearchOrder_Bean */
    public void setSearchOrderComboBoxObj(ComboBox<Order_Bean> ComboBox){
        ComboBox.setButtonCell(new SearchOrderComboBoxObj());
        ComboBox.setCellFactory(Obj -> new SearchOrderComboBoxObj());
    }
    private class SearchOrderComboBoxObj extends ListCell<Order_Bean> {
        @Override
        protected void updateItem(Order_Bean Order_Bean, boolean empty) {
            super.updateItem(Order_Bean, empty);

            if (Order_Bean != null){
                if(Order_Bean.getAlreadyOrderNumber() != null)
                    setText(Order_Bean.getNowOrderNumber() + " (已轉)");
                else
                    setText(Order_Bean.getNowOrderNumber());
            }
        }
    }
    public void setBlackCatShippingFeeComboBoxObj(ComboBox<Product_Enum.BlackCatShippingFee> ComboBox){
        ComboBox.setButtonCell(new setBlackCatShippingFeeComboBoxObj());
        ComboBox.setCellFactory(Obj -> new setBlackCatShippingFeeComboBoxObj());
    }
    private class setBlackCatShippingFeeComboBoxObj extends ListCell<Product_Enum.BlackCatShippingFee> {
        @Override
        protected void updateItem(Product_Enum.BlackCatShippingFee BlackCatShippingFee, boolean empty) {
            super.updateItem(BlackCatShippingFee, empty);
            if (BlackCatShippingFee != null){
                setText(BlackCatShippingFee.getName());
            }
        }
    }
    public void setSpecificationTemplateComboBoxObj(ComboBox<SpecificationTemplate_Bean> ComboBox){
        ComboBox.setButtonCell(new setSpecificationTemplateComboBoxObj());
        ComboBox.setCellFactory(Obj -> new setSpecificationTemplateComboBoxObj());
    }
    private class setSpecificationTemplateComboBoxObj extends ListCell<SpecificationTemplate_Bean> {
        @Override
        protected void updateItem(SpecificationTemplate_Bean SpecificationTemplate_Bean, boolean empty) {
            super.updateItem(SpecificationTemplate_Bean, empty);
            if (SpecificationTemplate_Bean != null){
                setText(SpecificationTemplate_Bean.getTemplateName());
            }
        }
    }
    /** Get ComboBox items - [String] */
    public ObservableList<String> getComboBoxItemsStringFormat(ComboBox<String> ComboBox){ return ComboBox.getItems(); }
    public String getComboBoxSelectItemStringFormat(ComboBox<String> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem(); }
    public int getComboBoxSelectItemIndex(ComboBox<String> ComboBox){ return ComboBox.getSelectionModel().getSelectedIndex(); }
    public ObservableList<Integer> getComboBoxItemsIntegerFormat(ComboBox<Integer> ComboBox){ return ComboBox.getItems(); }
    public Integer getComboBoxSelectItemIntegerFormat(ComboBox<Integer> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem(); }

    public ObservableList<Version_Bean> getVersionComboBoxItems(ComboBox<Version_Bean> ComboBox){ return ComboBox.getItems(); }
    public Version_Bean getVersionComboBoxSelectItem(ComboBox<Version_Bean> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem(); }
    public void setVersionComboBoxObj(ComboBox<Version_Bean> ComboBox){
        ComboBox.setButtonCell(new setVersionComboBoxObj());
        ComboBox.setCellFactory(Obj -> new setVersionComboBoxObj());
    }
    private class setVersionComboBoxObj extends ListCell<Version_Bean>{
        @Override
        protected void updateItem(Version_Bean Version_Bean, boolean empty) {
            super.updateItem(Version_Bean, empty);
            if (Version_Bean != null)  setText(Version_Bean.getVersion());
        }
    }

    /** Set all bank into ComboBox
     * @param AllBank_ComboBox all bank ComboBox
     * @param BankID all bank which selected
     * */
    public void setAllBankComboBox(ComboBox<BankInfo_Bean> AllBank_ComboBox, Integer BankID){
        if(BankID == null)
            AllBank_ComboBox.getSelectionModel().selectFirst();
        else{
            ObservableList<BankInfo_Bean> AllBankList = getAllBankComboBoxItems(AllBank_ComboBox);
            for(BankInfo_Bean BankInfo_Bean : AllBankList){
                if(BankInfo_Bean.getBankID() != null && BankInfo_Bean.getBankID().equals(BankID)){
                    AllBank_ComboBox.getSelectionModel().select(BankInfo_Bean);
                    break;
                }
            }
        }
    }
    /** Get ComboBox items - [ERP.Bean] CompanyBank_Bean */
    public ObservableList<CompanyBank_Bean> getCompanyBankInfoComboBoxItems(ComboBox<CompanyBank_Bean> ComboBox){   return ComboBox.getItems();    }
    /** Get ComboBox select item - [ERP.Bean] CompanyBank_Bean */
    public CompanyBank_Bean getCompanyBankInfoComboBoxSelectItem(ComboBox<CompanyBank_Bean> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    /** Get ComboBox items - [ERP.Bean] BankInfo_Bean */
    public ObservableList<BankInfo_Bean> getAllBankComboBoxItems(ComboBox<BankInfo_Bean> ComboBox){   return ComboBox.getItems();    }
    /** Get ComboBox select item - [ERP.Bean] BankInfo_Bean */
    public BankInfo_Bean getAllBankComboBoxSelectItem(ComboBox<BankInfo_Bean> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }

    /** Get ComboBox select item - [ERP.Enum] SystemSetting_Enum.ObjectIDCharacter */
    public SystemSetting_Enum.ObjectIDCharacter getCustomerAreaTitleComboBoxSelectItem(ComboBox<SystemSetting_Enum.ObjectIDCharacter> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    /** Get ComboBox items - [ERP.Bean] ObjectInfo_Bean */
    public ObservableList<ObjectInfo_Bean> getObjectIDComboBoxItems(ComboBox<ObjectInfo_Bean> ComboBox){   return ComboBox.getItems();    }
    /** Get ComboBox select item - [ERP.Bean] ObjectInfo_Bean */
    public ObjectInfo_Bean getObjectIDComboBoxSelectItem(ComboBox<ObjectInfo_Bean> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    /** Get ComboBox select index - [ERP.Bean] ObjectInfo_Bean */
    public int getObjectIDComboBoxSelectIndex(ComboBox<ObjectInfo_Bean> ComboBox){  return ComboBox.getSelectionModel().getSelectedIndex();  }
    public IpCamInfo_Bean getIpCamComboBoxSelectItem(ComboBox<IpCamInfo_Bean> ComboBox){   return ComboBox.getSelectionModel().getSelectedItem();    }
    public ObservableList<IpCamInfo_Bean> getIpCamComboBoxItems(ComboBox<IpCamInfo_Bean> ComboBox){   return ComboBox.getItems();    }
    public WebCamInfo_Bean getWebCamComboBoxSelectItem(ComboBox<WebCamInfo_Bean> ComboBox){   return ComboBox.getSelectionModel().getSelectedItem();    }
    public ObservableList<WebCamInfo_Bean> getWebCamComboBoxItemList(ComboBox<WebCamInfo_Bean> ComboBox){   return ComboBox.getItems();    }
    /** Get ComboBox select item - [ERP.Enum] Product_Enum.CategoryLayer */
    public Product_Enum.CategoryLayer getCategoryLayerComboBoxSelectItem(ComboBox<Product_Enum.CategoryLayer> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    /** Get ComboBox select item - [Enum] Product_Enum.BlackCatShippingFee */
    public Product_Enum.BlackCatShippingFee getBlackCatShippingFeeComboBoxSelectItem(ComboBox<Product_Enum.BlackCatShippingFee> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    public SpecificationTemplate_Bean getSpecificationTemplateComboBoxSelectItem(ComboBox<SpecificationTemplate_Bean> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    public ObservableList<SpecificationTemplate_Bean> getSpecificationTemplateComboBoxItemList(ComboBox<SpecificationTemplate_Bean> ComboBox){  return ComboBox.getItems();  }
    /** Get ComboBox select item - [ERP.Enum] Order_Enum.ExportQuotationVendor */
    public IAECrawlerAccount_Bean getIAECrawlerComboBoxSelectItem(ComboBox<IAECrawlerAccount_Bean> ComboBox){  return ComboBox.getSelectionModel().getSelectedItem();  }
    public ObservableList<IAECrawlerAccount_Bean> getIAECrawlerAccountComboBoxItemList(ComboBox<IAECrawlerAccount_Bean> ComboBox){  return ComboBox.getItems();  }
    public ObservableList<CheckoutStatus_Bean> getCheckoutStatusComboBoxItemList(ComboBox<CheckoutStatus_Bean> ComboBox){   return ComboBox.getItems(); }
    public CheckoutStatus_Bean getCheckoutStatusComboBoxSelectItem(ComboBox<CheckoutStatus_Bean> ComboBox){   return ComboBox.getSelectionModel().getSelectedItem(); }

    /** Get ComboBox select item - [ERP.Bean] Vendor_Bean */
    public Vendor_Bean getVendorComboBoxSelectItem(ComboBox<Vendor_Bean> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem();   }
    /** Get ComboBox select index - [ERP.Bean] Vendor_Bean */
    public int getVendorComboBoxSelectIndex(ComboBox<Vendor_Bean> ComboBox){ return ComboBox.getSelectionModel().getSelectedIndex();   }
    /** Get ComboBox select items - [ERP.Enum] Product_Enum.WaitConfirmStatus */
    public ObservableList<Product_Enum.WaitConfirmStatus> getWaitConfirmStatusComboBoxItems(ComboBox<Product_Enum.WaitConfirmStatus> ComboBox){   return ComboBox.getItems(); }
    /** Get ComboBox select item - [ERP.Enum] Product_Enum.WaitConfirmStatus */
    public Product_Enum.WaitConfirmStatus getWaitConfirmStatusComboBoxSelectItem(ComboBox<Product_Enum.WaitConfirmStatus> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem();   }
    /** Get ComboBox select item - [ERP.Enum] Product_Enum.WaitConfirmTable */
    public Product_Enum.WaitConfirmTable getWaitConfirmTableComboBoxSelectItem(ComboBox<Product_Enum.WaitConfirmTable> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem();  }
    /** Get ComboBox select item - [ERP.Bean] VendorCategory_Bean */
    public VendorCategory_Bean getVendorCategoryComboBoxSelectItem(ComboBox<VendorCategory_Bean> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem();   }
    /** Get ComboBox select index - [ERP.Bean] VendorCategory_Bean */
    public int getVendorCategoryComboBoxSelectIndex(ComboBox<VendorCategory_Bean> ComboBox){ return ComboBox.getSelectionModel().getSelectedIndex();   }

    public IAECrawlerBelong_Bean getIAECrawlerBelongComboBoxSelectItem(ComboBox<IAECrawlerBelong_Bean> ComboBox){ return ComboBox.getSelectionModel().getSelectedItem();   }
    public void setIAECrawlerBelongComboBoxSelectItem(ComboBox<IAECrawlerBelong_Bean> ComboBox, IAECrawlerBelong_Bean IAECrawlerBelong_Bean){
        ComboBox.getSelectionModel().select(IAECrawlerBelong_Bean);
    }

    /** Set date into DatePicker
     * @param DatePicker object
     * @param Date Date
     * */
    public void setDatePickerValue(DatePicker DatePicker, String Date){
        if(Date == null)    DatePicker.setValue(null);
        else    DatePicker.setValue(LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
    /** Get DatePicker in format
     * @param Format DatePicker format
     * */
    public String getDatePickerValue(DatePicker DatePicker, String Format){  return DatePicker.getValue().format(DateTimeFormatter.ofPattern(Format)); }
    /** Set DatePicker disable
     * @param Disable DatePicker disable status
     * */
    public void setDatePickerDisable(DatePicker DatePicker, boolean Disable){
        DatePicker.setDisable(Disable);
    }

    public Spinner<Integer> setIntegerSpinner(int min, int max, int initialValue, int amountToStepBy, boolean isEditable, int fontSize, int width){
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setEditable(isEditable);
        spinner.setPrefWidth(width);
        spinner.setStyle("-fx-font-size:" + fontSize + "px");
        setIntegerSpinnerValueFactory(spinner,min, max, initialValue, amountToStepBy);
        return spinner;
    }
    public Spinner<Double> setDoubleSpinner(double min, double max, double initialValue, double amountToStepBy, boolean isEditable, int fontSize, int width){
        Spinner<Double> spinner = new Spinner<>();
        spinner.setEditable(isEditable);
        spinner.setPrefWidth(width);
        spinner.setStyle("-fx-font-size:" + fontSize + "px");
        setDoubleSpinnerValueFactory(spinner, min, max, initialValue, amountToStepBy);
        return spinner;
    }
    /** Set Spinner disable
     * @param Disable Spinner disable status
     * */
    public void setDoubleSpinnerDisable(Spinner<Double> Spinner, Boolean Disable){    Spinner.setDisable(Disable);    }
    public void setIntegerSpinnerDisable(Spinner<Integer> Spinner, Boolean Disable){    Spinner.setDisable(Disable);    }

//    public void setSpinnerDisable(Spinner<Integer> Spinner, Boolean Disable){    Spinner.setDisable(Disable);    }
    /** Initial Spinner include parameters - [Integer] */
    public void setIntegerSpinnerValueFactory(Spinner<Integer> Spinner, int Min, int Max, int InitialValue, int amountToStepBy){
        Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Min, Max, InitialValue, amountToStepBy));
    }
    /** Set Spinner parameters - [Integer] */
    public void setSpinnerIntegerValue(Spinner<Integer> Spinner, int Value){    Spinner.getValueFactory().setValue(Value);  }

    /** Initial Spinner include parameters - [Double] */
    public void setDoubleSpinnerValueFactory(Spinner<Double> Spinner, double Min, double Max, double InitialValue, double amountToStepBy){
        Spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(Min, Max, InitialValue, amountToStepBy));
    }
    /** Set Spinner parameters - [Double] */
    public void setSpinnerDoubleValue(Spinner<Double> Spinner, Double Value){ Spinner.getValueFactory().setValue(Value);  }

    public void setDatePickerStyle(DatePicker DatePicker, String Style){
        DatePicker.setStyle(Style);
    }
    /** Set TableColumn cell value factory and style */
    public void setColumnCellValue(TableColumn TableColumn, String ColumnValue, String Alignment, String FontSize, String BackgroundColor){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        if(BackgroundColor == null) TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        else    TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px; -fx-background-color:" + BackgroundColor + ";");
    }
    /** Sort TableColumn in Integer */
    public void setTableColumnDoubleSort(TableColumn TableColumn){
        TableColumn.setComparator((Comparator<Object>) (o1, o2) -> {
            Double obj1, obj2;
            if(o1 instanceof Double && o2 instanceof Double){
                obj1 = (Double) o1;
                obj2 = (Double) o2;
            }else{
                obj1 = Double.valueOf(String.valueOf(o1));
                obj2 = Double.valueOf(String.valueOf(o2));
            }
            return obj1.compareTo(obj2);
        });
    }
    /** Initial FileChooser
     * @param Title FileChooser title name
     * */
    public FileChooser setFileChooser(String Title){
        FileChooser FileChooser = new FileChooser();
        FileChooser.setTitle(Title);
        return FileChooser;
    }

    public void setTextAreaEditable(TextArea TextArea, boolean editable){ TextArea.setEditable(editable);   }
    public TextField setTextFiled(int fontSize, int width, Object text){
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.setFont(new Font("微軟正黑體", fontSize));
        textField.setText("" + text);
        return textField;
    }
    /** Set TextField style
     * @param Style TextField style format
     * */
    public void setTextFieldStyle(TextField TextField, String Style){   TextField.setStyle(Style);  }
    /** Set TextField editable
     * @param Editable TextField editable status
     * */
    public void setTextFieldEditable(TextField TextField, boolean Editable){  TextField.setEditable(Editable);  }
    /** Judge TextField editable */
    public boolean isTextFieldEditable(TextField TextField){   return TextField.isEditable();   }
    /** Set TextField disable
     * @param Disable TextField disable status
     * */
    public void setTextFieldDisable(TextField TextField, boolean Disable){  TextField.setDisable(Disable);  }
    public void setTextFieldVisible(TextField TextField, boolean visible){  TextField.setVisible(visible);  }
    /** Limit TextFiled max length */
    public void addKeyWordTextLimitLength(final TextField TextField, final int maxLength) {
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (TextField.getText().length() > maxLength) {
                String KeyWord = TextField.getText().substring(0, maxLength);
                TextField.setText(KeyWord);
            }
        });
    }
    /** Limit TextFiled only be entered in digital
     * @param Minus judge if it is negative
     * */
    public void addTextFieldLimitDigital(final TextField TextField, boolean Minus){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(Minus){
                if(!Text.equals("")){
                    if(!String.valueOf(Text.charAt(0)).equals("-") && !ToolKit.isDigital(Text)){
                        String KeyWord = "";
                        for(int index = 0 ; index < Text.length() ; index++){
                            String indexString = String.valueOf(Text.charAt(index));
                            if(ToolKit.isDigital(indexString))
                                KeyWord = KeyWord + indexString;
                        }
                        TextField.setText(KeyWord);
                    }else if(String.valueOf(Text.charAt(0)).equals("-") && Text.length() >= 2 && !ToolKit.isDigital(Text))
                        TextField.setText(Text.substring(1));
                }
            }else{
                if(!Text.equals("") && !ToolKit.isPositiveDigital(Text)){
                    String KeyWord = "";
                    for(int index = 0 ; index < Text.length() ; index++){
                        String indexString = String.valueOf(Text.charAt(index));
                        if(ToolKit.isDigital(indexString))
                            KeyWord = KeyWord + indexString;
                    }
                    TextField.setText(KeyWord);
                }
            }
        });
    }
    public void addTextFieldLimitDouble(final TextField TextField){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(!Text.equals("") && !ToolKit.isDouble(Text)){
                String KeyWord = TextField.getText().substring(0, TextField.getText().length()-1);
                TextField.setText(KeyWord);
            }
        });
    }
    public void addTextFieldLimitDouble(int decimalPlace, final TextField TextField){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(!Text.equals("") && !ToolKit.isDouble(decimalPlace,Text)){
                String KeyWord = TextField.getText().substring(0, TextField.getText().length()-1);
                TextField.setText(KeyWord);
            }
        });
    }
    public void addTextFieldLimitEnglish(final TextField TextField){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(!Text.equals("") && !ToolKit.isEnglish(Text)){
                String KeyWord = TextField.getText().substring(0, TextField.getText().length()-1);
                TextField.setText(KeyWord);
            }
        });
    }
    public void addTextFieldLimitUpperCaseEnglish(final TextField TextField){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(!Text.equals("") && !ToolKit.isUpperCaseEnglish(Text)){
                String KeyWord = TextField.getText().substring(0, TextField.getText().length()-1);
                TextField.setText(KeyWord);
            }
        });
    }
    public void addTextFieldLimitLowerCaseEnglish(final TextField TextField){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(!Text.equals("") && !ToolKit.isLowerCaseEnglish(Text)){
                String KeyWord = TextField.getText().substring(0, TextField.getText().length()-1);
                TextField.setText(KeyWord);
            }
        });
    }
    public void addTextFiledLimitByBankAccount(final TextField TextField){
        TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = TextField.getText();
            if(!Text.equals("")){
                for(int index = 0 ; index < Text.length() ; index++){
                    String indexString = String.valueOf(Text.charAt(index));
                    if(!ToolKit.isDigital(indexString) && !indexString.equals("-")){
                        String title = Text.substring(0,index);
                        String end = Text.substring(index+1);
                        TextField.setText(title + end);
                        break;
                    }
                }
            }
        });
    }
    public void addSpinnerLimitDouble(final Spinner<Double> Spinner, int decimalPlace){
        Spinner.getEditor().textProperty().addListener((ov, oldValue, newValue) -> {
            String Text = Spinner.getEditor().getText();
            if(!Text.equals("") && !ToolKit.isDouble(decimalPlace,Text)){
                String KeyWord = Spinner.getEditor().getText().substring(0,Spinner.getEditor().getText().length()-1);
                Spinner.getEditor().setText(KeyWord);
            }
        });
    }
    public Text createText(String text,int fontSize, String textFillColor){
        Text Text = new Text();
        if(textFillColor == null)
            Text.setStyle("-fx-font-size:" + fontSize + "px;");
        else
            Text.setStyle("-fx-font-size:" + fontSize + "px; -fx-fill:" + textFillColor + ";-fx-font-weight: bold");
        Text.setText(text);
        return Text;
    }

    public void setScrollerPaneVisible(ScrollPane scrollerPane, boolean visible){   scrollerPane.setVisible(visible);  }
    public ObservableList<TitledPane> getAccordionTitledPaneList(Accordion accordion){   return accordion.getPanes();    }
    public TitledPane getAccordionTitledPaneExpandedPane(Accordion accordion){   return accordion.getExpandedPane();    }
    public ObservableList<TitledPane> getOrderReferenceAccordionList(Accordion accordion){    return accordion.getPanes();    }

    /** Get TableView select item - [ERP.Bean] ProductBookCase_Bean */
    public ProductBookCase_Bean getProductBookCaseTableViewSelectItem(TableView<ProductBookCase_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView items - [ERP.Bean] ProductBookCase_Bean */
    public ObservableList<ProductBookCase_Bean> getProductBookCaseTableViewItemList(TableView<ProductBookCase_Bean> TableView){  return TableView.getItems();    }

    public QuotationTemplateFormatSetting_Bean getQuotationTemplateFormatSettingTableViewSelectItem(TableView<QuotationTemplateFormatSetting_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    public ObservableList<QuotationTemplateFormatSetting_Bean> getQuotationTemplateFormatSettingTableViewItemList(TableView<QuotationTemplateFormatSetting_Bean> TableView){   return TableView.getItems(); }

    public IAECrawlerAccount_Bean getIAECrawlerAccountTableViewSelectItem(TableView<IAECrawlerAccount_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    public ObservableList<IAECrawlerAccount_Bean> getIAECrawlerAccountTableViewItemList(TableView<IAECrawlerAccount_Bean> TableView){   return TableView.getItems(); }
    public IAECrawlerBelong_Bean getIAECrawlerBelongTableViewSelectItem(TableView<IAECrawlerBelong_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    public ObservableList<IAECrawlerBelong_Bean> getIAECrawlerBelongTableViewItemList(TableView<IAECrawlerBelong_Bean> TableView){   return TableView.getItems(); }
    public IAECrawlerData_Bean getIAECrawlerDataTableViewSelectItem(TableView<IAECrawlerData_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    public ObservableList<IAECrawlerData_Bean> getIAECrawlerDataTableViewItemList(TableView<IAECrawlerData_Bean> TableView){   return TableView.getItems(); }
    public IAECrawlerData_Bean getIAECrawlerDataTableViewSelectItem(TableView<IAECrawlerData_Bean> TableView, int index){ return TableView.getItems().get(index); }
    public ObservableList<IAECrawler_ManufacturerContactDetail_Bean> getIAECrawlerContactDetailTableViewItemList(TableView<IAECrawler_ManufacturerContactDetail_Bean> TableView){   return TableView.getItems(); }
    public IAECrawler_ManufacturerContactDetail_Bean getIAECrawlerContactDetailTableViewSelectItem(TableView<IAECrawler_ManufacturerContactDetail_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    public IAECrawler_ManufacturerContactDetail_Bean getIAECrawlerContactDetailTableViewSelectItem(TableView<IAECrawler_ManufacturerContactDetail_Bean> TableView, int index){ return TableView.getItems().get(index); }

    public ObservableList<IAECrawler_ExportPurchaseNote_Bean> getIAECrawlerPurchaseNoteTableViewItemList(TableView<IAECrawler_ExportPurchaseNote_Bean> TableView){   return TableView.getItems(); }
    public IAECrawler_ExportPurchaseNote_Bean getIAECrawlerPurchaseNoteTableViewSelectItem(TableView<IAECrawler_ExportPurchaseNote_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }

    public ObservableList<ExportCompanyFormat_Bean> getExportCompanyFormatTableViewItemList(TableView<ExportCompanyFormat_Bean> TableView){   return TableView.getItems(); }
    public ExportCompanyFormat_Bean getExportCompanyFormatTableViewSelectItem(TableView<ExportCompanyFormat_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }

    public ObservableList<ExportTemplateName_Bean> getExportTemplateNameTableViewItemList(TableView<ExportTemplateName_Bean> TableView){   return TableView.getItems(); }
    public ExportTemplateName_Bean getExportTemplateNameTableViewSelectItem(TableView<ExportTemplateName_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }

    public ObservableList<IpCamInfo_Bean> getIpCamTableViewItemList(TableView<IpCamInfo_Bean> TableView){   return TableView.getItems(); }
    public IpCamInfo_Bean getIpCamTableViewSelectItem(TableView<IpCamInfo_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }


    /** Get TableView select item - [ERP.Bean] ObjectInfo_Bean */
    public ObjectInfo_Bean getObjectTableViewSelectItem(TableView<ObjectInfo_Bean> TableView){    return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select item - [ERP.Bean] ManageProductInfo_Bean */
    public ProductInfo_Bean getManageProductTableViewSelectItem(TableView<ProductInfo_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView items - [ERP.Bean] ManageProductInfo_Bean */
    public ObservableList<ProductInfo_Bean> getManageProductTableViewItemList(TableView<ProductInfo_Bean> TableView){   return TableView.getItems();    }
    /** Remove TableView item - [ERP.Bean] ManageProductInfo_Bean */
    public void removeManageProductTableViewItem(TableView<ProductInfo_Bean> TableView, ProductInfo_Bean ProductInfo_Bean){   TableView.getItems().remove(ProductInfo_Bean);    }
    /** Get TableView items - [ERP.Bean] ManageProductOnShelf_Bean */
    public ObservableList<ManageProductOnShelf_Bean> getManageProductOnShelfTableViewItemList(TableView<ManageProductOnShelf_Bean> TableView){   return TableView.getItems();    }
    public ManageProductOnShelf_Bean getManageProductOnShelfTableViewSelectItem(TableView<ManageProductOnShelf_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem();    }
    /** Get TableView select index - [ERP.Bean] OrderProduct_Bean */
    public int getOrderProductTableViewSelectIndex(TableView<OrderProduct_Bean> TableView){   return TableView.getSelectionModel().getSelectedIndex();  }
    /** Get TableView select item - [ERP.Bean] OrderProduct_Bean */
    public OrderProduct_Bean getOrderProductTableViewSelectItem(TableView<OrderProduct_Bean> TableView){    return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select item - [ERP.Bean] OrderProduct_Bean */
    public OrderProduct_Bean getOrderProductTableViewSelectItem(TableView<OrderProduct_Bean> TableView, int index){   return TableView.getItems().get(index);  }
    /** Get TableView items - [ERP.Bean] OrderProduct_Bean */
    public ObservableList<OrderProduct_Bean> getOrderProductTableViewItemList(TableView<OrderProduct_Bean> TableView){  return TableView.getItems();    }
    /** Remove TableView item - [ERP.Bean] OrderProduct_Bean */
    public void removeOrderProductTableViewItem(TableView<OrderProduct_Bean> TableView, OrderProduct_Bean OrderProduct_Bean){  TableView.getItems().remove(OrderProduct_Bean); }
    public ProductGroup_Bean getProductGroupTableViewSelectItem(TableView<ProductGroup_Bean> TableView){    return TableView.getSelectionModel().getSelectedItem(); }
    public ProductGroup_Bean getProductGroupTableViewSelectItem(TableView<ProductGroup_Bean> TableView, int index){   return TableView.getItems().get(index);  }
    public ObservableList<ProductGroup_Bean> getProductGroupTableViewItemList(TableView<ProductGroup_Bean> TableView){  return TableView.getItems();    }
    /** Get TableView select item - [ERP.Bean] ProductCategory_Bean */
    public ProductCategory_Bean getCategoryTableViewSelectItem(TableView<ProductCategory_Bean> TableView){  return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView items - [ERP.Bean] ProductCategory_Bean */
    public ObservableList<ProductCategory_Bean> getCategoryTableViewItemList(TableView<ProductCategory_Bean> TableView){    return TableView.getItems();    }
    /** Remove TableView item - [ERP.Bean] ProductCategory_Bean */
    public void removeCategoryTableViewItem(TableView<ProductCategory_Bean> TableView, ProductCategory_Bean ProductCategory_Bean){    TableView.getItems().remove(ProductCategory_Bean);  }

    /** Get TableView select item - [ERP.Bean] ExportOrder_Bean */
    public ExportOrder_Bean getExportOrderTableViewSelectItem(TableView<ExportOrder_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }

    public ReportGenerator_Item_Bean getReportGeneratorItemTableViewSelectItem(TableView<ReportGenerator_Item_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    public ReportGenerator_Item_Bean getReportGeneratorItemTableViewSelectItem(TableView<ReportGenerator_Item_Bean> TableView, int index){   return TableView.getItems().get(index); }
    public int getReportGeneratorItemTableViewSelectIndex(TableView<ReportGenerator_Item_Bean> TableView){   return TableView.getSelectionModel().getSelectedIndex(); }
    public ObservableList<ReportGenerator_Item_Bean> getReportGeneratorItemTableViewItemList(TableView<ReportGenerator_Item_Bean> TableView){   return TableView.getItems(); }
    /** Get TableView select item - [ERP.Bean] SearchOrder_Bean */
    public SearchOrder_Bean getSearchOrderTableViewSelectItem(TableView<SearchOrder_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView items - [ERP.Bean] SearchOrder_Bean */
    public ObservableList<SearchOrder_Bean> getSearchOrderTableViewItemList(TableView<SearchOrder_Bean> TableView){ return TableView.getItems();    }
    public int getSearchOrderTableViewItemSize(TableView<SearchOrder_Bean> TableView){ return TableView.getItems().size();    }
    /** Get TableView select item - [ERP.Bean] SearchOrder_Bean */
    public SearchOrder_Bean getSearchOrderTableViewSelectItem(TableView<SearchOrder_Bean> TableView, int index){   return TableView.getItems().get(index); }
    /** Get TableView select item - [ERP.Bean] SearchOrderProgress_Bean */
    public SearchOrderProgress_Bean getSearchOrderProgressTableViewSelectItem(TableView<SearchOrderProgress_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView items - [ERP.Bean] SearchOrderProgress_Bean */
    public ObservableList<SearchOrderProgress_Bean> getSearchOrderProgressTableViewItemList(TableView<SearchOrderProgress_Bean> TableView){ return TableView.getItems();    }
    /** Get TableView select item - [ERP.Bean] SearchOrderProgress_Bean */
    public SearchOrderProgress_Bean getSearchOrderProgressTableViewSelectItem(TableView<SearchOrderProgress_Bean> TableView, int index){   return TableView.getItems().get(index); }
    /** Get TableView select item - [ERP.Bean] SearchOrderTableViewSetting_Bean */
    public TableViewSetting_Bean getSearchOrderTableViewSettingTableViewSelectItem(TableView<TableViewSetting_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select item - [ERP.Bean] SearchOrderTableViewSetting_Bean */
    public TableViewSetting_Bean getSearchOrderTableViewSettingTableViewSelectItem(TableView<TableViewSetting_Bean> TableView, int index){ return TableView.getItems().get(index); }
    /** Get TableView select index - [ERP.Bean] SearchOrderTableViewSetting_Bean */
    public int getSearchOrderTableViewSettingTableViewSelectIndex(TableView<TableViewSetting_Bean> TableView){ return TableView.getSelectionModel().getSelectedIndex(); }
    /** Get TableView items - [ERP.Bean] SearchOrderTableViewSetting_Bean */
    public ObservableList<TableViewSetting_Bean> getSearchOrderTableViewSettingList(TableView<TableViewSetting_Bean> TableView){   return TableView.getItems();    }

    /** Get TableView select item - [ERP.Bean] SearchNonePayReceive_Bean */
    public SearchNonePayReceive_Bean getSearchNonePayReceiveTableViewSelectItem(TableView<SearchNonePayReceive_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select item - [ERP.Bean] SearchNonePayReceive_Bean */
    public SearchNonePayReceive_Bean getSearchNonePayReceiveTableViewSelectItem(TableView<SearchNonePayReceive_Bean> TableView, int index){   return TableView.getItems().get(index); }
    /** Get TableView items - [ERP.Bean] SearchNonePayReceive_Bean */
    public ObservableList<SearchNonePayReceive_Bean> getSearchNonePayReceiveTableViewItemList(TableView<SearchNonePayReceive_Bean> TableView){ return TableView.getItems();    }
    /** Get TableView select item - [ERP.Bean] SearchOrder_Bean */
    public SearchOrder_Bean getNonePayableReceivableDetailsTableViewSelectItem(TableView<SearchOrder_Bean> TableView){   return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView items - [ERP.Bean] SearchOrder_Bean */
    public ObservableList<SearchOrder_Bean> getNonePayableReceivableDetailsTableViewItemList(TableView<SearchOrder_Bean> TableView){ return TableView.getItems();    }
    public OrderReference_Bean getOrderReferenceTableViewSelectItem(TableView<OrderReference_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    public ObservableList<OrderReference_Bean> getOrderReferenceTableViewList(TableView<OrderReference_Bean> TableView){   return TableView.getItems();    }
    /** Get TableView select item - [ERP.Bean] TransactionDetail_Bean */
    public TransactionDetail_Bean getTransactionDetailTableViewSelectItem(TableView<TransactionDetail_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select index - [ERP.Bean] TransactionDetail_Bean */
    public int getTransactionDetailTableViewSelectIndex(TableView<TransactionDetail_Bean> TableView){ return TableView.getSelectionModel().getSelectedIndex(); }
    /** Get TableView items - [ERP.Bean] TransactionDetail_Bean */
    public ObservableList<TransactionDetail_Bean> getTransactionDetailTableViewList(TableView<TransactionDetail_Bean> TableView){   return TableView.getItems();    }
    /** Get TableView select item - [ERP.Bean] SearchPayableReceivable_Bean */
    public SearchPayableReceivable_Bean getSearchPayableReceivableTableViewSelectItem(TableView<SearchPayableReceivable_Bean> TableView){  return TableView.getSelectionModel().getSelectedItem();    }
    /** Get TableView select items - [ERP.Bean] SearchPayableReceivable_Bean */
    public ObservableList<SearchPayableReceivable_Bean> getSearchPayableReceivableTableViewList(TableView<SearchPayableReceivable_Bean> TableView){  return TableView.getItems();    }
    /** Get TableView select select item - [ERP.Bean] CompanyBank_Bean */
    public CompanyBank_Bean getCompanyBankTableViewSelectItem(TableView<CompanyBank_Bean> TableView){  return TableView.getSelectionModel().getSelectedItem();    }
    /** Get TableView select select item - [ERP.Bean] BankInfo_Bean */
    public BankInfo_Bean getAllBankTableViewSelectItem(TableView<BankInfo_Bean> TableView){  return TableView.getSelectionModel().getSelectedItem();    }
    /** Get TableView select select item - [ERP.Bean] WaitConfirmProductInfo_Bean */
    public WaitConfirmProductInfo_Bean getWaitConfirmTableViewSelectItem(TableView<WaitConfirmProductInfo_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select item - [ERP.Bean] WaitConfirmProductInfo_Bean */
    public ObservableList<WaitConfirmProductInfo_Bean> getWaitConfirmTableViewList(TableView<WaitConfirmProductInfo_Bean> TableView){   return TableView.getItems();    }

    public ObservableList<BigGoFilter_Bean> getBigGoFilterTableViewItemList(TableView<BigGoFilter_Bean> TableView){ return TableView.getItems(); }
    /** Get TableView select item - [ERP.Bean] BigGoFilter_Bean */
    public BigGoFilter_Bean getBigGoFilterTableViewSelectItem(TableView<BigGoFilter_Bean> TableView){ return TableView.getSelectionModel().getSelectedItem(); }
    /** Get TableView select item - [ERP.Bean] BigGoFilter_Bean */
    public BigGoFilter_Bean getBigGoFilterTableViewSelectItem(TableView<BigGoFilter_Bean> TableView, int index){ return TableView.getItems().get(index); }
    /** Get TableView items - [ERP.Bean] BigGoFilter_Bean */
    public ObservableList<BigGoFilter_Bean> getBigGoFilterTableViewList(TableView<BigGoFilter_Bean> TableView){   return TableView.getItems();    }
    /** Get bigGo TableView items which selected - [ERP.Bean] BigGoFilter_Bean */
    public ObservableList<BigGoFilter_Bean> getBigGoFilterSelectList(TableView<BigGoFilter_Bean> TableView){
        ObservableList<BigGoFilter_Bean> bigGoFilterList = getBigGoFilterTableViewItemList(TableView);
        ObservableList<BigGoFilter_Bean> selectBigGoFilterList = FXCollections.observableArrayList();
        for(BigGoFilter_Bean bigGoFilter_Bean : bigGoFilterList){
            if(bigGoFilter_Bean.isChoose())
                selectBigGoFilterList.add(bigGoFilter_Bean);
        }
        return selectBigGoFilterList;
    }
    /** Get TableView items sizes */
    public int getTableViewItemsSize(TableView TableView){  return TableView.getItems().size(); }

    /** Get order TableView items which selected - [ERP.Bean] SearchOrder_Bean */
    public ObservableList<SearchOrder_Bean> getSearchOrderSelectList(TableView<SearchOrder_Bean> TableView){
        ObservableList<SearchOrder_Bean> SearchOrderList = getSearchOrderTableViewItemList(TableView);
        ObservableList<SearchOrder_Bean> SelectOrderList = FXCollections.observableArrayList();
        for(SearchOrder_Bean searchOrder_Bean : SearchOrderList){
            if(searchOrder_Bean.isCheckBoxSelect() != null && searchOrder_Bean.isCheckBoxSelect())
                SelectOrderList.add(searchOrder_Bean);
        }
        return SelectOrderList;
    }
    /** Get product TableView items which selected - [ERP.Bean] SearchOrder_Bean */
    public ObservableList<OrderProduct_Bean> getSelectProductList(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> ProductList = getOrderProductTableViewItemList(TableView);
        ObservableList<OrderProduct_Bean> SelectProductList = FXCollections.observableArrayList();
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(OrderProduct_Bean.isCheckBoxSelect())
                SelectProductList.add(OrderProduct_Bean);
        }
        return SelectProductList;
    }
    /** Get product TableView items which none selected - [ERP.Bean] SearchOrder_Bean */
    public ObservableList<OrderProduct_Bean> getNoneSelectProductList(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> productList = getOrderProductTableViewItemList(TableView);
        ObservableList<OrderProduct_Bean> noneSelectProductList = FXCollections.observableArrayList();
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            if(!OrderProduct_Bean.isCheckBoxSelect())
                noneSelectProductList.add(OrderProduct_Bean);
        }
        return noneSelectProductList;
    }
    /** Refresh TableView item number - [ERP.Bean] OrderProduct_Bean */
    public void refreshTableViewItemNumber(ObservableList<OrderProduct_Bean> ProductList){
        for(int index = 0 ; index < ProductList.size() ; index++){
            OrderProduct_Bean OrderProduct_Bean = ProductList.get(index);
            if(OrderProduct_Bean.getItemNumber() != (index+1)){
                OrderProduct_Bean.setItemNumber(index+1);
            }
        }
    }
    /** Set Tab disable
     * @param Disable Tab disable status
     * */
    public void setTabDisable(Tab Tab, boolean Disable){ Tab.setDisable(Disable);    }
    /** Get TabPan which Tab selected */
    public Tab getTabPaneSelectTab(TabPane TabPane){    return TabPane.getSelectionModel().getSelectedItem();   }
    /** Get TabPan Tabs */
    public ObservableList<Tab> getTabPaneList(TabPane TabPane){ return TabPane.getTabs();   }
    /** Get TabPan which Tab selected */
    public int getTabPaneSelectTabIndex(TabPane TabPane){   return TabPane.getSelectionModel().getSelectedIndex();  }
    /** Delete TabPan which Tab selected */
    public void deleteSelectTab(TabPane TabPane){   TabPane.getTabs().remove(TabPane.getSelectionModel().getSelectedItem());    }

    public void setToolTips(Node node, javafx.scene.control.Tooltip Tooltip, String tooltips){
        Tooltip.setText(tooltips);
        setTooltipStyle(Tooltip);
        bindTooltip(node, Tooltip);
    }
    public void setTooltipStyle(Tooltip tooltip){
        tooltip.setStyle("-fx-base: #AE3522;-fx-text-fill:orange;-fx-font: normal bold 24 Langdon");
    }
    private void bindTooltip(final Node node, final Tooltip tooltip){
        node.setOnMouseMoved(event -> tooltip.show(node, event.getScreenX()+10, event.getScreenY()+7));
        node.setOnMouseExited(event -> tooltip.hide());
    }
    public void setStackPaneScroll(BorderPane borderPane, ImageView pictureImageView, double scale, Point2D eventPoint, double frameFitWidth, double newHeight, double newWidth){
        Point2D imagePoint = borderPane.localToScene(new Point2D(pictureImageView.getX(), pictureImageView.getY()));
        Rectangle2D imageRect = new Rectangle2D(imagePoint.getX(), imagePoint.getY(), pictureImageView.getFitWidth(), pictureImageView.getFitHeight());
        Point2D ratePoint;
        Point2D eventPointDistance;
        if (newWidth > scale / 4 * frameFitWidth && imageRect.contains(eventPoint)) {
            ratePoint = eventPoint.subtract(imagePoint);
            ratePoint = new Point2D(ratePoint.getX() / pictureImageView.getFitWidth(), ratePoint.getY() / pictureImageView.getFitHeight());
            eventPointDistance = borderPane.sceneToLocal(eventPoint);
        } else {
            ratePoint = new Point2D(0.5, 0.5);
            eventPointDistance = new Point2D(borderPane.getWidth() / 2,borderPane.getHeight() / 2);
        }

        pictureImageView.setX(eventPointDistance.getX() - newWidth * ratePoint.getX());
        pictureImageView.setY(eventPointDistance.getY() - newHeight * ratePoint.getY());
        pictureImageView.setFitWidth(newWidth);
        pictureImageView.setFitHeight(newHeight);
    }
}
