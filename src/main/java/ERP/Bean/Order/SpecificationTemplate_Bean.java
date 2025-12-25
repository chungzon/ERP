package ERP.Bean.Order;

import ERP.Enum.Order.Order_Enum.SpecificationColumn;

public class SpecificationTemplate_Bean {
    private int id;
    private String templateName, content;
    private SpecificationColumn SpecificationColumn;

    public SpecificationTemplate_Bean(int id, String templateName, String content, SpecificationColumn SpecificationColumn){
        this.id = id;
        this.templateName = templateName;
        this.content = content;
        this.SpecificationColumn = SpecificationColumn;
    }
    public int getId() {    return id;  }

    public void setTemplateName(String templateName){   this.templateName = templateName;   }
    public String getTemplateName() {   return templateName;    }

    public void setContent(String content){ this.content = content; }
    public String getContent() {    return content; }

    public SpecificationColumn getSpecificationColumn() {    return SpecificationColumn; }
}
