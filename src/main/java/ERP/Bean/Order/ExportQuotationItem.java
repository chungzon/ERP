package ERP.Bean.Order;

import ERP.ERPApplication;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ExportQuotationItem {
    private SplitMenuButton ExportQuotation_SplitMenuButton;
    private CustomMenuItem CustomMenuItem;
    private int id;
    private String itemName;
    private double priceDiscount, pricePercentage;

    private HBox HBox;
    private Label priceDiscountLabel;
    private CheckBox CheckBox;

    private ToolKit ToolKit;
    private KeyPressed KeyPressed;
    private Order_Model Order_Model;
    public ExportQuotationItem(SplitMenuButton ExportQuotation_SplitMenuButton, String itemName, Double pricePercentage, Double priceDiscount){
        this.ToolKit = ERPApplication.ToolKit;
        ComponentToolKit ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.ExportQuotation_SplitMenuButton = ExportQuotation_SplitMenuButton;

        this.itemName = itemName;
        if(pricePercentage == null && priceDiscount != null) {
            this.priceDiscount = priceDiscount;
            this.pricePercentage = priceDiscount * 100;
        }else if(pricePercentage != null && priceDiscount == null) {
            this.pricePercentage = pricePercentage;
            this.priceDiscount = pricePercentage / 100;
        }

        HBox = ComponentToolKit.setHBox(Pos.CENTER_LEFT,20,0,10,0,10);
        ObservableList<Node> HBoxChildList = HBox.getChildren();
        CheckBox = ComponentToolKit.initialCheckBox("",16);
        CheckBox.setSelected(true);
        setCheckBoxOnAction();
        HBoxChildList.addAll(CheckBox);

        Label itemNameLabel = ComponentToolKit.setLabel(itemName,-1,-1,18,null);
        HBoxChildList.addAll(itemNameLabel);

        priceDiscountLabel = ComponentToolKit.setLabel(ToolKit.RoundingString(true,this.pricePercentage) + " ％",-1,-1,18,null);
        HBoxChildList.addAll(priceDiscountLabel);

        Button modifyButton = ComponentToolKit.setButton("修改％數",120,15,16);
        setModifyButtonMouseEvent(modifyButton);
        HBoxChildList.addAll(modifyButton);

        Button removeButton = ComponentToolKit.setButton("移除",80,15,16);
        setDeleteButtonMouseEvent(removeButton);
        HBoxChildList.addAll(removeButton);
    }
    private void setCheckBoxOnAction(){
        CheckBox.setOnAction(ActionEvent -> {
            if(!Order_Model.updateExportQuotationItemDefaultSelect(id,CheckBox.isSelected()))
                DialogUI.AlarmDialog("※ 修改選擇預設值失敗");
        });
    }
    private void setModifyButtonMouseEvent(Button modifyButton){
        modifyButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                String wannaModifyPricePercentage;
                do {
                    wannaModifyPricePercentage = DialogUI.TextInputDialog("品項金額(未稅)", "品項金額為此報價單總額(未稅)的【0.1 ~ 99.9】％",null);
                    if (wannaModifyPricePercentage == null || (ToolKit.isDouble(wannaModifyPricePercentage) && Double.parseDouble(wannaModifyPricePercentage) > 0 && Double.parseDouble(wannaModifyPricePercentage) < 100))
                        break;
                    else
                        DialogUI.AlarmDialog("※ 請輸正確金額格式");
                } while (true);

                if (wannaModifyPricePercentage != null) {
                    if(Order_Model.modifyExportQuotationItemPriceDiscount(id,Double.parseDouble(wannaModifyPricePercentage))){
                        this.pricePercentage = Double.parseDouble(wannaModifyPricePercentage);
                        this.priceDiscount = this.pricePercentage / 100;
                        priceDiscountLabel.setText(this.pricePercentage + " ％");
                        DialogUI.MessageDialog("※ 修改成功");
                    }else
                        DialogUI.MessageDialog("※ 修改失敗");
                }
            }
        });
    }
    private void setDeleteButtonMouseEvent(Button removeButton){
        removeButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)){
                if(DialogUI.ConfirmDialog("※ 確定刪除此品項 ?",true,false,0,0)){
                    if(Order_Model.deleteExportQuotationItem(id)){
                        ExportQuotation_SplitMenuButton.getItems().remove(CustomMenuItem);
                        DialogUI.MessageDialog("※ 刪除成功");
                    }else
                        DialogUI.MessageDialog("※ 刪除失敗");
                }
            }
        });
    }

    public HBox getHBox() { return HBox;    }

    public void setCheckBoxSelect(boolean isSelect){   CheckBox.setSelected(isSelect); }
    public boolean isCheckBoxSelect(){  return CheckBox.isSelected();   }

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public String getItemName() {   return itemName;    }

    public double getPriceByDiscount() {  return priceDiscount;   }

    public CustomMenuItem getCustomMenuItem(){
        if(CustomMenuItem == null){
            CustomMenuItem = new CustomMenuItem(HBox);
            CustomMenuItem.setHideOnClick(false);
        }
        return CustomMenuItem;
    }
}
