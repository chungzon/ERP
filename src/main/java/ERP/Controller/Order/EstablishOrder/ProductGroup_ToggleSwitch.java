package ERP.Controller.Order.EstablishOrder;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.ERPApplication;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProductGroup_ToggleSwitch  extends Parent {
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    private final String[] TableColumns = { "項號", "原項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量", "安全存量"};
    private final String[] TableColumnCellValue = new String[]{"SeriesNumber","ItemNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock","SafetyStock"};
    private final int[] ColumnsLength = { 45, 60, 130, 450, 85, 50, 80, 80, 80, 50, 80};

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private Order_Bean Order_Bean;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private Accordion accordion;
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public ProductGroup_ToggleSwitch(boolean defaultSwitched, Order_Bean Order_Bean, EstablishOrder_Controller EstablishOrder_Controller){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;

        this.Order_Bean = Order_Bean;
        this.EstablishOrder_Controller = EstablishOrder_Controller;
        accordion = EstablishOrder_Controller.ShipmentProductGroupAccordion;

        javafx.scene.shape.Rectangle background = new Rectangle(60, 20);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(javafx.scene.paint.Color.WHITE);
        background.setStroke(javafx.scene.paint.Color.LIGHTGRAY);

        Circle trigger = new Circle(10);
        trigger.setCenterX(10);
        trigger.setCenterY(10);
        trigger.setFill(javafx.scene.paint.Color.WHITE);
        trigger.setStroke(javafx.scene.paint.Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            setSwitchedOnStatus(isOn);
        });
        setOnMouseClicked(MouseEvent -> {
            if(Order_Bean.getProductGroupMap() == null){
                DialogUI.MessageDialog("※ 未設定商品群組");
                switchedOn.set(false);
            }else
                switchedOn.set(!switchedOn.get());
        });
        switchedOn.set(defaultSwitched);
        if(!defaultSwitched) {
            setSwitchedOnStatus(false);
        }
        refreshAccordion();
    }
    public void setSwitchedOnStatus(boolean isOn){
        switchedOn.set(isOn);
        translateAnimation.setToX(isOn ? 100 - 60 : 0);
        fillAnimation.setFromValue(isOn ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.LIGHTGREEN);
        fillAnimation.setToValue(isOn ? javafx.scene.paint.Color.LIGHTGREEN : Color.RED);
        animation.play();
        if(isOn){
            refreshAccordion();
            EstablishOrder_Controller.showProductGroupScrollerPane(true);
        }else{
            EstablishOrder_Controller.showProductGroupScrollerPane(false);
        }
    }
    public void refreshAccordion(){
        accordion.getPanes().clear();
        if(Order_Bean.getProductGroupMap() != null){
            generateAccordion();
        }
    }
    private void generateAccordion(){
        try{
            int index = 1;
            HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Bean.getProductGroupMap();
            for(Integer group_itemNumber : productGroupMap.keySet()){
                ProductGroup_Bean productGroup_bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(productGroup_bean);
                productGroup_bean.setBatchPrice(calculateProductGroupBatchPrice(itemMap, productGroup_bean.getSmallQuantity()));

                BorderPane BorderPane = createBorderPane(itemMap);
                TitledPane TitlePane = createTitlePane(index,productGroup_bean);
                TitlePane.setContent(BorderPane);
                accordion.getPanes().add(TitlePane);

                index = index + 1;
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    private double calculateProductGroupBatchPrice(HashMap<Integer,ItemGroup_Bean> itemMap, Integer smallQuantity){
        ObservableList<OrderProduct_Bean> productList = ComponentToolKit.getOrderProductTableViewItemList(EstablishOrder_Controller.MainTableView);
        double batchPrice = 0;

        for(Integer item_id : itemMap.keySet()){
            ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
            for(OrderProduct_Bean OrderProduct_Bean : productList) {
                if(item_id.equals(OrderProduct_Bean.getItem_id())){
                    if(smallQuantity == null || smallQuantity == 0){
                        batchPrice = batchPrice + OrderProduct_Bean.getBatchPrice()*ItemGroup_Bean.getItem_quantity();
                    }else{
                        batchPrice = batchPrice + (OrderProduct_Bean.getBatchPrice()*ItemGroup_Bean.getItem_quantity()/smallQuantity);
                    }
                    break;
                }
            }
        }
        return ToolKit.RoundingDouble(batchPrice);
    }
    private TitledPane createTitlePane(int index, ProductGroup_Bean ProductGroup_Bean){
        TitledPane TitledPane = new TitledPane();
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        HBoxChild.add(ComponentToolKit.setLabel(ProductGroup_Bean.getItemNumber() + "    ",-1, -1,18,""));
        HBoxChild.add(ComponentToolKit.setLabel(((ProductGroup_Bean.getSmallQuantity() != null) ?
                "【細項數量】" + (ProductGroup_Bean.getSmallQuantity()) : ("【數量】" + ProductGroup_Bean.getQuantity())),-1, -1,18,"-fx-text-fill:#7D79EB"));
        HBoxChild.add(ComponentToolKit.setLabel(("    【名稱】" + ProductGroup_Bean.getGroupName()),-1, -1,18,""));
        HBoxChild.add(ComponentToolKit.setLabel("    " + ((ProductGroup_Bean.getSmallPriceAmount() != null) ?
                ("【細項金額】" + ToolKit.fmtMicrometer(ProductGroup_Bean.getSmallPriceAmount())) : ("【金額】" + ToolKit.fmtMicrometer(ProductGroup_Bean.getPriceAmount()))),-1, -1,18,"-fx-text-fill:#C9700A"));
        HBoxChild.add(ComponentToolKit.setLabel("    【原始金額】" + ToolKit.fmtMicrometer(ToolKit.RoundingString(ProductGroup_Bean.getOriginalSinglePrice()*ProductGroup_Bean.getQuantity())),-1, -1,18,"-fx-text-fill:#0ABFA6"));

        double differentPrice = (ProductGroup_Bean.getSmallPriceAmount() != null ? ProductGroup_Bean.getSmallPriceAmount() : ProductGroup_Bean.getPriceAmount())
                - ProductGroup_Bean.getOriginalSinglePrice()*ProductGroup_Bean.getQuantity();
        if(differentPrice >= 0)
            HBoxChild.add(ComponentToolKit.setLabel("    【差額】" + ToolKit.RoundingString(differentPrice),-1, -1,18,"-fx-text-fill:blue"));
        else
            HBoxChild.add(ComponentToolKit.setLabel("    【差額】" + ToolKit.RoundingString(differentPrice),-1, -1,18,"-fx-text-fill:red"));
        TitledPane.setPrefWidth(1890);
        TitledPane.setGraphic(HBox);
        TitledPane.setExpanded(false);
        TitledPane.setId(String.valueOf(index));
        TitledPane.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)){
                ObservableList<TitledPane> titlePaneList = accordion.getPanes();
                for(TitledPane selectTitledPane : titlePaneList){
                    if(selectTitledPane == TitledPane){
                        if(TitledPane.isExpanded())
                            EstablishOrder_Controller.setProductInfoByAccordion(ProductGroup_Bean);
                        else
                            EstablishOrder_Controller.setProductInfoByAccordion(null);
                    }
                }
            }
        });
        return TitledPane;
    }
    private BorderPane createBorderPane(HashMap<Integer, ItemGroup_Bean> itemMap) throws Exception {
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        TableView.setMaxSize(1890,300);
        TableView.setEditable(true);
        for(int index = 0 ; index < TableColumns.length ; index++) {
            if(index == 0 || index == 1 || index == 4 || index == 6 || index == 7 || index == 8 || index == 9 || index == 10)
                TableView.getColumns().add(ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]));
            else {
                TableColumn<OrderProduct_Bean,String> TableColumn = ComponentToolKit.createOrderProductTableStringColumn(TableColumns[index], ColumnsLength[index], TableColumnCellValue[index]);
                TableView.getColumns().add(TableColumn);
            }
        }
        ObservableList<OrderProduct_Bean> mainProductList = Order_Bean.getProductList();
        int itemNumber = 1;
        for(Integer item_id : itemMap.keySet()){
            ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
            for(OrderProduct_Bean OrderProduct_Bean : mainProductList){
                if(item_id.equals(OrderProduct_Bean.getItem_id())){
                    OrderProduct_Bean copyOrderProduct_Bean = ToolKit.CopyProductBean(OrderProduct_Bean);
                    copyOrderProduct_Bean.setSeriesNumber(itemNumber);
                    copyOrderProduct_Bean.setQuantity(ItemGroup_Bean.getItem_quantity());
                    copyOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(copyOrderProduct_Bean.getQuantity()*copyOrderProduct_Bean.getSinglePrice()));
                    TableView.getItems().add(copyOrderProduct_Bean);
                    itemNumber = itemNumber + 1;
                }
            }
        }
        BorderPane BorderPane = ComponentToolKit.setBorderPane(5,5,5,5);
        BorderPane.setCenter(TableView);
        return BorderPane;
    }
}
