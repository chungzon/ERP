package ERP.Bean.Order;

import ERP.Enum.Order.Order_Enum;

import java.util.ArrayList;

public class OrderReviewStatusRecord_Bean {
    private int id, review_status_id;
    private Order_Enum.ReviewObject ReviewObject;
    private String subject;
    private String record;
    private String record_time;
    private boolean isExistPicture;
    private Order_Enum.ReviewStatus ReviewStatus;
    private ArrayList<OrderReviewStatusPicture_Bean> pictureList;

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public int getReview_status_id() {  return review_status_id;    }
    public void setReview_status_id(int review_status_id) { this.review_status_id = review_status_id;   }

    public Order_Enum.ReviewObject getReviewObject() {  return ReviewObject;    }
    public void setReviewObject(Order_Enum.ReviewObject reviewObject) { ReviewObject = reviewObject;    }

    public String getSubject() {    return subject; }
    public void setSubject(String subject) {    this.subject = subject; }

    public String getRecord() { return record;  }
    public void setRecord(String record) {  this.record = record;   }

    public String getRecord_time() {    return record_time; }
    public void setRecord_time(String record_time) {    this.record_time = record_time; }

    public boolean isExistPicture() {   return isExistPicture;  }
    public void setExistPicture(boolean existPicture) { isExistPicture = existPicture;  }

    public Order_Enum.ReviewStatus getReviewStatus() {  return ReviewStatus;    }
    public void setReviewStatus(Order_Enum.ReviewStatus reviewStatus) { ReviewStatus = reviewStatus;    }

    public ArrayList<OrderReviewStatusPicture_Bean> getPictureList() { return pictureList; }
    public void setPictureList(ArrayList<OrderReviewStatusPicture_Bean> pictureList) { this.pictureList = pictureList; }
}
