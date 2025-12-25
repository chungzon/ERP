package ERP.Bean.ToolKit.ShowReportGenerator;

import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import javafx.collections.ObservableList;

public class ReportGenerator_Bean {
    private int id, orderOrSubBill_id;
    private OrderSource OrderSource;
    private int export_manufacturer_id;
    private String remark;
    private String insertDateTime;
    private ObservableList<ReportGenerator_Item_Bean> reportGenerator_itemList;

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public int getOrderOrSubBill_id() { return orderOrSubBill_id;   }
    public void setOrderOrSubBill_id(int orderOrSubBill_id) {   this.orderOrSubBill_id = orderOrSubBill_id; }

    public OrderSource getOrderSource() {    return OrderSource; }
    public void setOrderSource(Order_Enum.OrderSource orderSource) {    OrderSource = orderSource;  }

    public int getExport_manufacturer_id() {    return export_manufacturer_id;  }
    public void setExport_manufacturer_id(int export_manufacturer_id) { this.export_manufacturer_id = export_manufacturer_id;   }

    public String getRemark() { return remark;  }
    public void setRemark(String remark) {  this.remark = remark;   }

    public String getInsertDateTime() { return insertDateTime;  }
    public void setInsertDateTime(String insertDateTime) {  this.insertDateTime = insertDateTime;   }

    public ObservableList<ReportGenerator_Item_Bean> getReportGenerator_itemList() {    return reportGenerator_itemList;    }
   public void setReportGenerator_itemList(ObservableList<ReportGenerator_Item_Bean> reportGenerator_itemList) {   this.reportGenerator_itemList = reportGenerator_itemList;   }
}
