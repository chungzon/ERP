package ERP.ToolKit;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.ExportOrder_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Model.ManageCustomerInfo.ManageCustomerInfo_Model;
import ERP.Model.ManageManufacturerInfo.ManageManufacturerInfo_Model;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.ManageProductCategory.ManageProductCategory_Model;
import ERP.Model.Order.ExportOrder_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Order.SearchNonePayReceive.ExportNonePayReceiveDocument;
import ERP.Model.Order.SearchNonePayReceive.SearchNonePayReceive_Model;
import ERP.Model.Order.OpenSpecificationWordDocument_Model;
import ERP.Model.Product.Product_Model;
import ERP.Model.ProductWaitingConfirm.ProductWaitingConfirm_Model;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate.*;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.Model.Product.BidCategorySetting_Model;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ModelToolKit {
    private Order_Model Order_Model;
    private ExportOrder_Model ExportOrder_Model;
    private Product_Model Product_Model;
    private OpenSpecificationWordDocument_Model OpenSpecificationWordDocument_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private SystemSetting_Model SystemSetting_Model;
    private ManageProductCategory_Model ManageProductCategory_Model;
    private BidCategorySetting_Model BidCategorySetting_Model;
    private ManageManufacturerInfo_Model ManageManufacturerInfo_Model;
    private ManageCustomerInfo_Model ManageCustomerInfo_Model;
    private ExportNonePayReceiveDocument ExportNonePayReceiveDocument;
    private SearchNonePayReceive_Model SearchNonePayReceive_Model;
    private SingleProductUpdate_Model SingleProductUpdate_Model;
    private UnitechGetWebInfo_Model UnitechGetWebInfo_Model;
    private JinghaoGetWebInfo_Model JinghaoGetWebInfo_Model;
    private WeblinkGetWebInfo_Model WeblinkGetWebInfo_Model;
    private XanderGetWebInfo_Model XanderGetWebInfo_Model;
    private KtnetGetWebInfo_Model KtnetGetWebInfo_Model;
    private Genb2bGetWebInfo_Model Genb2bGetWebInfo_Model;
    private SynnexGetWebInfo_Model SynnexGetWebInfo_Model;
    private SenaoGetWebInfo_Model SenaoGetWebInfo_Model;

    public ModelToolKit(CallFXML CallFXML){
        SystemSetting_Model = new SystemSetting_Model();
    }
    public SystemSetting_Model getSystemSettingModel(){
        if(SystemSetting_Model == null) SystemSetting_Model = new SystemSetting_Model();
        return SystemSetting_Model;
    }
    public Order_Model getOrderModel(){
        getSystemSettingModel();
        if(Order_Model == null) Order_Model = new Order_Model();
        return Order_Model;
    }
    public ExportOrder_Model getExportOrderModel(String outputFilePath, Order_Bean Order_Bean, ObjectInfo_Bean ObjectInfo_Bean, ObservableList<ExportOrder_Bean> productList){
        if(ExportOrder_Model == null)   ExportOrder_Model = new ExportOrder_Model(outputFilePath, Order_Bean, ObjectInfo_Bean, productList);
        return ExportOrder_Model;
    }
    public Product_Model getProductModel(){
        if(Product_Model == null)   Product_Model = new Product_Model();
        return Product_Model;
    }
    public OpenSpecificationWordDocument_Model getOpenSpecificationWordDocument_Model(){
        if(OpenSpecificationWordDocument_Model == null) OpenSpecificationWordDocument_Model = new OpenSpecificationWordDocument_Model();
        return OpenSpecificationWordDocument_Model;
    }
    public ManagePayableReceivable_Model getManagePayableReceivableModel(){
        if(ManagePayableReceivable_Model == null)   ManagePayableReceivable_Model = new ManagePayableReceivable_Model();
        return ManagePayableReceivable_Model;
    }
    public ProductWaitingConfirm_Model getProductWaitingConfirmModel(){
        if(ProductWaitingConfirm_Model == null) ProductWaitingConfirm_Model = new ProductWaitingConfirm_Model();
        return ProductWaitingConfirm_Model;
    }
    public ManageProductCategory_Model getManageProductCategoryModel(){
        if(ManageProductCategory_Model == null) ManageProductCategory_Model = new ManageProductCategory_Model();
        return ManageProductCategory_Model;
    }
    public BidCategorySetting_Model getBidCategorySettingModel(){
        if(BidCategorySetting_Model == null)    BidCategorySetting_Model = new BidCategorySetting_Model();
        return BidCategorySetting_Model;
    }
    public ManageManufacturerInfo_Model getManageManufacturerInfoModel(){
        if(ManageManufacturerInfo_Model == null)    ManageManufacturerInfo_Model = new ManageManufacturerInfo_Model();
        return ManageManufacturerInfo_Model;
    }
    public ManageCustomerInfo_Model getManageCustomerInfoModel(){
        if(ManageCustomerInfo_Model == null)    ManageCustomerInfo_Model = new ManageCustomerInfo_Model();
        return ManageCustomerInfo_Model;
    }
    public SearchNonePayReceive_Model getSearchNonePayReceiveModel(){
        if(SearchNonePayReceive_Model == null)  SearchNonePayReceive_Model = new SearchNonePayReceive_Model();
        return SearchNonePayReceive_Model;
    }
    public SingleProductUpdate_Model getSingleProductUpdateModel(Stage MainStage) {
        if(SingleProductUpdate_Model == null)   SingleProductUpdate_Model = new SingleProductUpdate_Model(MainStage);
        return SingleProductUpdate_Model;
    }
    public WeblinkGetWebInfo_Model getWeblinkGetWebInfoModel(){
        if(WeblinkGetWebInfo_Model == null) WeblinkGetWebInfo_Model = new WeblinkGetWebInfo_Model();
        return WeblinkGetWebInfo_Model;
    }
    public UnitechGetWebInfo_Model getUnitechGetWebInfoModel() {
        if(UnitechGetWebInfo_Model == null) UnitechGetWebInfo_Model = new UnitechGetWebInfo_Model();
        return UnitechGetWebInfo_Model;
    }
    public JinghaoGetWebInfo_Model getJinghaoGetWebInfo_Model(){
        if(JinghaoGetWebInfo_Model == null) JinghaoGetWebInfo_Model = new JinghaoGetWebInfo_Model();
        return JinghaoGetWebInfo_Model;
    }
    public XanderGetWebInfo_Model getXanderGetWebInfoModel(){
        if(XanderGetWebInfo_Model == null)  XanderGetWebInfo_Model = new XanderGetWebInfo_Model();
        return XanderGetWebInfo_Model;
    }
    public KtnetGetWebInfo_Model getKtnetGetWebInfoModel(){
        if(KtnetGetWebInfo_Model == null)   KtnetGetWebInfo_Model= new KtnetGetWebInfo_Model();
        return KtnetGetWebInfo_Model;
    }
    public Genb2bGetWebInfo_Model getGenb2bGetWebInfoModel(){
        if(Genb2bGetWebInfo_Model == null)  Genb2bGetWebInfo_Model = new Genb2bGetWebInfo_Model();
        return Genb2bGetWebInfo_Model;
    }
    public SynnexGetWebInfo_Model getSynnexGetWebInfoModel(){
        if(SynnexGetWebInfo_Model == null)  SynnexGetWebInfo_Model = new SynnexGetWebInfo_Model();
        return SynnexGetWebInfo_Model;
    }
    public SenaoGetWebInfo_Model getSenaoGetWebInfoModel(){
        if(SenaoGetWebInfo_Model == null)   SenaoGetWebInfo_Model = new SenaoGetWebInfo_Model();
        return SenaoGetWebInfo_Model;
    }
}
