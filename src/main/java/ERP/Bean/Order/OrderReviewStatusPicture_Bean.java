package ERP.Bean.Order;

import javafx.scene.layout.StackPane;

public class OrderReviewStatusPicture_Bean {
    private int id;
    private int reviewStatus_record_id;
    private String base64;
    private StackPane stackPane;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewStatus_record_id() {
        return reviewStatus_record_id;
    }

    public void setReviewStatus_record_id(int reviewStatus_record_id) {
        this.reviewStatus_record_id = reviewStatus_record_id;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }
}
