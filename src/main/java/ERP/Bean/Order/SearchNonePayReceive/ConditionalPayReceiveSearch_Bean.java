package ERP.Bean.Order.SearchNonePayReceive;

/** [ERP.Bean] The conditional search for none payable or receivable in manage purchase or shipment system */
public class ConditionalPayReceiveSearch_Bean {

    private String CheckoutByMonthStartDate, CheckoutByMonthEndDate, NoneCheckoutByMonthStartDate, NoneCheckoutByMonthEndDate;
    private String ObjectName;
    private String StartObjectID, EndObjectID;

    public String getCheckoutByMonthStartDate() {   return CheckoutByMonthStartDate;    }
    public void setCheckoutByMonthStartDate(String StartDate) {  this.CheckoutByMonthStartDate = StartDate;    }

    public String getCheckoutByMonthEndDate() { return CheckoutByMonthEndDate;  }
    public void setCheckoutByMonthEndDate(String EndDate) {  this.CheckoutByMonthEndDate = EndDate;    }

    public String getNoneCheckoutByMonthStartDate() {   return NoneCheckoutByMonthStartDate;    }
    public void setNoneCheckoutByMonthStartDate(String noneCheckoutByMonthStartDate) {  NoneCheckoutByMonthStartDate = noneCheckoutByMonthStartDate;    }

    public String getNoneCheckoutByMonthEndDate() { return NoneCheckoutByMonthEndDate;  }
    public void setNoneCheckoutByMonthEndDate(String noneCheckoutByMonthEndDate) {  NoneCheckoutByMonthEndDate = noneCheckoutByMonthEndDate;    }

    public String getObjectName() { return ObjectName;  }
    public void setObjectName(String ObjectName) {  this.ObjectName = ObjectName;    }

    public String getStartObjectID() {  return StartObjectID;   }
    public void setStartObjectID(String StartObjectID) {    this.StartObjectID = StartObjectID;  }

    public String getEndObjectID() {    return EndObjectID; }
    public void setEndObjectID(String EndObjectID) {    this.EndObjectID = EndObjectID;  }
}
