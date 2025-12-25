package ERP.ToolKit;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Order.SearchOrderProgress_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum.ConvertMathToChinese;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Pattern;

/** All toolkit */
public class ToolKit {

    public CallConfig CallConfig;
    public KeyPressed KeyPressed;
    public ComponentToolKit ComponentToolKit;
    public CallFXML CallFXML;
    public CallJAR CallJAR;
    public ModelToolKit ModelToolKit;
    public Tooltip Tooltip;
    public ToolKit(){
        CallConfig = new CallConfig();
        KeyPressed = new KeyPressed();
        CallJAR = new CallJAR();
        ComponentToolKit = new ComponentToolKit(this);
        CallFXML = new CallFXML(ComponentToolKit);
        ModelToolKit = new ModelToolKit(CallFXML);
        Tooltip = new Tooltip();
    }
    /** Quotation background color */
    public String getQuotationBackgroundColor(){ return "#90CAF9";   }
    /** Waiting purchase background color */
    public String getWaitingPurchaseBackgroundColor(){   return "#F2B980";   }
    /** Waiting shipment background color */
    public String getWaitingShipmentBackgroundColor(){  return "#41C7D1"; }
    /** Sub bill background color */
    public String getSubBillBackgroundColor(){  return "#FFCDC8";   }
    /** Already order background color */
    public String getAlreadyOrderBackgroundColor(){ return "#F48FB1";   }
    /** Return order background color */
    public String getReturnOrderBackgroundColor(){   return "#C5E1A5";   }
    /** Already order and Return order background color mix */
    public String getAlreadyOrderAndReturnOrderBackgroundColorMix(){ return "#DCB8AB"; }
    /** Search none payable or receivable background color */
    public String getSearchNonePayableReceivableBackgroundColor(){    return "#ECD76F";   }
    /** Payable background color */
    public String getPayableBackgroundColor(){   return "#D0BEE1";  }
    /** Receivable background color */
    public String dsasf(){
        return "#C5E1A5";
    }
    public String getReceivableBackgroundColor(){    return "#C5E1A5";  }
    public String getGreen_BackgroundColor(){  return "#7CE883";  }
    public String getGray_BackgroundColor(){    return "#C5C5C5";    }
    public String getPink_BackgroundColor(){    return "#F692C7";    }
    /** Judge parameter is digital
     * @param Value the parameter being judged
     * */
    public boolean isDigital(String Value){
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        return pattern.matcher(Value).matches();
    }
    protected boolean isPositiveDigital(String Value){
        Pattern pattern = Pattern.compile("^\\+?[1-9][0-9]*$");
        return pattern.matcher(Value).matches();
    }
    public boolean isDouble(String value){
        String RePattern = "^(([1-9]{1}\\d{0,6})|(0{1}))(\\.\\d{0,1})?$";
//        String RePattern = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{0,2})?$";
        Pattern pattern = Pattern.compile(RePattern);
//        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,2})?$");
        return pattern.matcher(value).matches();
    }
    public boolean isDouble(int decimalPlace, String value){
        String RePattern = "^(([1-9]{1}\\d{0,6})|(0{1}))(\\.\\d{0," + decimalPlace+ "})?$";
        Pattern pattern = Pattern.compile(RePattern);
        return pattern.matcher(value).matches();
    }
    public boolean isEnglish(String text){
        Pattern pattern = Pattern.compile("^[a-zA-Z]{1}$");
        return pattern.matcher(text).matches();
    }
    public boolean isUpperCaseEnglish(String value){
        Pattern pattern = Pattern.compile("[A-Z]");
        return pattern.matcher(value).matches();
    }
    public boolean isLowerCaseEnglish(String value){
        Pattern pattern = Pattern.compile("[a-z]");
        return pattern.matcher(value).matches();
    }
    public boolean isChineseCount(String value){
        try{
            ConvertMathToChinese.valueOf(value.substring(0,1));
            return true;
        }catch (Exception Ex){
            return false;
        }
    }
    public boolean isFileNameExistEscapeCharacter(String fileName){
        return fileName.contains("/") || fileName.contains("\\") || fileName.contains(":") || fileName.contains("*") ||
                fileName.contains("?") || fileName.contains("\"") || fileName.contains(">") || fileName.contains("<") || fileName.contains("|");
    }
    /** Judge date wrong range
     * @param StartDatePicker the start date
     * @param EndDatePicker the end date
     * */
    public boolean isDateRangeError(DatePicker StartDatePicker, DatePicker EndDatePicker) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = simpleDateFormat.parse(ComponentToolKit.getDatePickerValue(StartDatePicker,"yyyy-MM-dd"));
            Date endDate = simpleDateFormat.parse(ComponentToolKit.getDatePickerValue(EndDatePicker,"yyyy-MM-dd"));
            return startDate.after(endDate);
        }catch (Exception ex){
            DialogUI.ExceptionDialog(ex);
        }
        return true;
    }
    protected boolean isDateFormatError(String format, String Date){
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat(format);
        try {
            SimpleDateFormat.setLenient(false);
            SimpleDateFormat.parse(Date);
            return false;
        } catch (ParseException Ex) {
            return true;
        }
    }
    public boolean isInvoiceFormatError(String invoiceNumber){
        if(invoiceNumber.length() != 10)
            return true;
        String invoiceTitle = invoiceNumber.substring(0,2);
        String invoiceLast = invoiceNumber.substring(2,10);
        for(int index = 0 ; index < invoiceTitle.length() ; index++){
            if(isDigital(String.valueOf(invoiceTitle.charAt(index))))
                return true;
        }
        for(int index = 0 ; index < invoiceLast.length() ; index++){
            if(!isDigital(String.valueOf(invoiceLast.charAt(index))))
                return true;
        }
        return false;
    }
    public Integer RoundingInteger(String stringMath){
        return BigDecimal.valueOf(Double.parseDouble(stringMath)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }
    public Integer RoundingInteger(double doubleMath){
        return BigDecimal.valueOf(doubleMath).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }
    public String RoundingString(String stringMath){
        return String.valueOf(BigDecimal.valueOf(Double.parseDouble(stringMath)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
    }
    public String RoundingString(double doubleMath){
        return String.valueOf(BigDecimal.valueOf(doubleMath).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
    }
    public double RoundingDouble(String doubleMath){
        return BigDecimal.valueOf(Double.parseDouble(doubleMath)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public double RoundingDouble(double doubleMath){
        return BigDecimal.valueOf(doubleMath).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public double RoundingDouble(int decimalPlace, double doubleMath){
        return BigDecimal.valueOf(doubleMath).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public String RoundingString(boolean isDecimal,double doubleMath){
        if(isDecimal){
            DecimalFormat df;
            String priceText = String.valueOf(doubleMath);
            if (priceText.indexOf(".") > 0) {
                if (priceText.length() - priceText.indexOf(".") - 1 == 0)
                    df = new DecimalFormat("#0.");
                else
                    df = new DecimalFormat("#0.0");
            } else
                df = new DecimalFormat("#0");
            return df.format(Double.parseDouble(priceText));
        }else{
            return String.valueOf(BigDecimal.valueOf(doubleMath).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
        }
    }
    public String fmtMicrometer(Object input) {
        if(input.equals(""))
            return null;
        String text;
        if(input instanceof Integer || input instanceof Double)
            text = String.valueOf(input);
        else
            text = (String) input;
        DecimalFormat df;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0)
                df = new DecimalFormat("###,##0.");
            else
                df = new DecimalFormat("###,##0.0");
        } else
            df = new DecimalFormat("###,##0");
        return df.format(Double.parseDouble(text));
    }
    /** Fill up the string by zero
     * @param Str the parameter being filled
     * @param Digit the number of zero
     * */
    public String fillZero(Object Str, int Digit) {
        for (int index = String.valueOf(Str).length(); index < Digit ; index++)    Str = "0" + Str;
        return String.valueOf(Str);
    }
    public String fillSpecificCharacter(Object Str, int digit) {
        String text = "";
        for(int index = 0 ; index < digit ; index++){
            text = text + Str;
        }
        return text;
    }
    public int getEnglishIndex(boolean upperCase, String text){
        return text.charAt(0) - (upperCase ? 'A' : 'a');
    }
    //	計算國字數字
    public String CalculateChinesePrice(int totalPrice) {
        String TotalPrice = String.valueOf(totalPrice);
        StringBuilder Chinese = new StringBuilder();
        for (int Index = 0; Index < TotalPrice.length(); Index++) {
            String Number = String.valueOf(TotalPrice.charAt(Index));
            Chinese.append(ConvertMathToChinese.values()[Integer.parseInt(Number)].getChineseMath()).append("　");
            Chinese.append(ConvertMathToChinese.values()[TotalPrice.length() - 1 - Index].getChineseUnit()).append("　");

        }
        return Chinese.substring(0, Chinese.lastIndexOf("　"));
    }
    public String hidePassword(String password){
        StringBuilder hidePassword = new StringBuilder();
        for(int index = 0 ; index < password.length() ; index++){
            if(index != 0 && index != password.length()-1)
                hidePassword.append("*");
            else
                hidePassword.append(password.charAt(index));
        }
        return hidePassword.toString();
    }

    /** Copy object of OrderProduct_Bean
     * @param OrderProduct_Bean the object of product
     * */
    public OrderProduct_Bean CopyProductBean(OrderProduct_Bean OrderProduct_Bean) throws Exception{
        OrderProduct_Bean orderProduct_bean = new OrderProduct_Bean();
        BeanUtils.copyProperties(orderProduct_bean, OrderProduct_Bean);
        return orderProduct_bean;
    }
    /** Copy object of product group
     * @param productGroup_bean the object of product
     * */
    public ProductGroup_Bean CopyProductGroupBean(ProductGroup_Bean productGroup_bean) throws Exception{
        ProductGroup_Bean productGroup = new ProductGroup_Bean();
        BeanUtils.copyProperties(productGroup, productGroup_bean);
        productGroup.setPriceAmount(productGroup_bean.getPriceAmount());
        productGroup.setSmallQuantity(productGroup_bean.getSmallQuantity());
        productGroup.setSmallSinglePrice(productGroup_bean.getSmallSinglePrice());
        productGroup.setSmallPriceAmount(productGroup_bean.getSmallPriceAmount());
        return productGroup;
    }
    /** Copy object of Order_Bean
     * @param Order_Bean the object of order
     * */
    public Order_Bean CopyOrderBean(Order_Bean Order_Bean) throws Exception{
        Order_Bean newOrderBean = new Order_Bean();
        BeanUtils.copyProperties(newOrderBean, Order_Bean);
        newOrderBean.setOrderObject(Order_Bean.getOrderObject());
        newOrderBean.setIsCheckout(Order_Bean.isCheckout().value());
        newOrderBean.setIsBorrowed(Order_Bean.isBorrowed().value());
        newOrderBean.setIsOffset(Order_Bean.getOffsetOrderStatus());
        return newOrderBean;
    }
    /** Sort the order number of order TableView
     * @param SearchOrderList the order list of search order
     * */
    public void sortSearchOrderNumber(ObservableList<SearchOrder_Bean> SearchOrderList){
        Comparator<SearchOrder_Bean> comparator = Comparator.comparing(SearchOrder_Bean::getOrderNumber);
        comparator = comparator.reversed();
        FXCollections.sort(SearchOrderList, comparator);
//        ShipmentOrderList.sort(Comparator.comparing(SearchOrder_Bean::getOrderNumber));
    }
    public void sortSearchOrderProgress(ObservableList<SearchOrderProgress_Bean> SearchOrderProgressList){
        Comparator<SearchOrderProgress_Bean> comparator = Comparator.comparing(SearchOrderProgress_Bean::getQuotationNumber);
        comparator = comparator.reversed();
        FXCollections.sort(SearchOrderProgressList, comparator);
    }
    /** Sort the sub bill number of waiting order TableView
     * @param ShipmentOrderList the order list of waiting shipment order
     * @param SubBillList  the order list of sub bill
     * */
    public void sortSubBillFromWaitingShipment(ObservableList<SearchOrder_Bean> ShipmentOrderList, ObservableList<SearchOrder_Bean> SubBillList, int orderNumberLength){
        if(SubBillList.size() != 0){
            for (SearchOrder_Bean SubBillOrder_Bean : SubBillList) {
                String FatherNumber = SubBillOrder_Bean.getOrderNumber().substring(0, orderNumberLength);
                for (int j = ShipmentOrderList.size() - 1; j >= 0; j--) {
                    if (ShipmentOrderList.get(j).getOrderNumber().substring(0, orderNumberLength).equals(FatherNumber)) {
                        ShipmentOrderList.add(j + 1, SubBillOrder_Bean);
                        break;
                    }
                }
            }
        }
    }
    /** Sort the sub bill number of search already order TableView
     * @param ShipmentOrderList the order list of already shipment order
     * @param SubBillList  the order list of sub bill
     * */
    public void sortSubBillFromAlreadyOrder(ObservableList<SearchOrder_Bean> ShipmentOrderList, ObservableList<SearchOrder_Bean> SubBillList, int orderNumberLength){
        if(SubBillList.size() != 0){
            if(ShipmentOrderList.size() == 0)   ShipmentOrderList.addAll(SubBillList);
            else{
                for (SearchOrder_Bean SubBillOrder_Bean : SubBillList) {
                    String FatherNumber = SubBillOrder_Bean.getWaitingOrderNumber().substring(0, orderNumberLength);
                    boolean isExistMainOrder = false;
                    for (int index = ShipmentOrderList.size() - 1; index >= 0; index--) {
                        if(ShipmentOrderList.get(index).getOrderSource() != Order_Enum.OrderSource.進貨退貨單 && ShipmentOrderList.get(index).getOrderSource() != Order_Enum.OrderSource.出貨退貨單) {
                            if (ShipmentOrderList.get(index).getWaitingOrderNumber().substring(0, orderNumberLength).equals(FatherNumber)) {
                                ShipmentOrderList.add(index + 1, SubBillOrder_Bean);
                                isExistMainOrder = true;
                                break;
                            }
                        }
                    }
                    if(!isExistMainOrder){
                        FatherNumber = SubBillOrder_Bean.getAlreadyOrderNumber();
                        for(int index = 0 ; index < ShipmentOrderList.size() ; index++){
                            if(ShipmentOrderList.get(index).getOrderSource() != Order_Enum.OrderSource.進貨退貨單 && ShipmentOrderList.get(index).getOrderSource() != Order_Enum.OrderSource.出貨退貨單) {
                                if(Long.parseLong(FatherNumber) > Long.parseLong(ShipmentOrderList.get(index).getAlreadyOrderNumber())){
                                    ShipmentOrderList.add(index, SubBillOrder_Bean);
                                    break;
                                }
                            }
                            if(index == ShipmentOrderList.size()-1){
                                ShipmentOrderList.add(index, SubBillOrder_Bean);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    public void sortNonePayReceiveOrderByObjectID(ObservableList<SearchNonePayReceive_Bean> nonePayReceiveList){
        Comparator<SearchNonePayReceive_Bean> comparator = Comparator.comparing(SearchNonePayReceive_Bean::getObjectID);
        FXCollections.sort(nonePayReceiveList, comparator);
    }
    /** Convert picture to Base64 format
     * @param file picture Url file
     * */
    public String generateBase64(File file) throws Exception {
        BufferedImage input = ImageIO.read(file);
        if(!file.getName().contains(".jpg") && !file.getName().contains(".jpeg")){
            return generateBase64(fixImagePickBackgroundColor(input));
        }
        ByteArrayOutputStream ByteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(input, "jpg", ByteArrayOutputStream);
        byte[] imageBytes = ByteArrayOutputStream.toByteArray();
        return new String(Base64.encodeBase64(imageBytes));
    }
    public String generateBase64(BufferedImage bufferedImage) throws Exception {
        ByteArrayOutputStream ByteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", ByteArrayOutputStream);
        byte[] imageBytes = ByteArrayOutputStream.toByteArray();
        return new String(Base64.encodeBase64(imageBytes));
    }

    /**
     Decode Base64 format to image
     * @param base64String the Base64 format of picture
     * */
    public byte[] decodeBase64ToByteArray(String base64String){
        return Base64.decodeBase64(base64String);
    }
    public byte[] convertBufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
    public Image decodeBase64ToImage(String base64String){
        Image Image = null;
        try {
            byte[] ImageByte = decodeBase64ToByteArray(base64String);
            ByteArrayInputStream ByteStream = new ByteArrayInputStream(ImageByte);
            Image = new Image(ByteStream);
            ByteStream.close();
        }catch (Exception Ex){
            Ex.printStackTrace();
        }
        return Image;
    }
    public BufferedImage decodeBase64ToBufferedImage(String base64String){
        BufferedImage Image = null;
        try {
            byte[] ImageByte = decodeBase64ToByteArray(base64String);
            ByteArrayInputStream ByteStream = new ByteArrayInputStream(ImageByte);
            Image = ImageIO.read(ByteStream);
            ByteStream.close();
        }catch (Exception Ex){
            Ex.printStackTrace();
        }
        return Image;
    }
    public BufferedImage fixImagePickBackgroundColor(BufferedImage image){
        try{
            int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
            ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

            PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
            pg.grabPixels();
            int width = pg.getWidth(), height = pg.getHeight();

            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
            return new BufferedImage(RGB_OPAQUE, raster, false, null);
        }catch (Exception Ex){
            DialogUI.ExceptionDialog(Ex);
        }
        return null;
    }
    /** Today's date
     * @param Format date format
     * */
    public String getToday(String Format) {
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat(Format);
        return DateFormat.format(new java.util.Date());
    }
    /** This month first date */
    public String getMonthFirstDate(){
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.DATE, FirstDate.getActualMinimum(Calendar.DATE));
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(FirstDate.getTime());
    }
    /** A specific day of last month
     * @param SpecificDay specific day
     * */
    public String getLastMonthSpecificDay(int SpecificDay){
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.MONTH, FirstDate.get(Calendar.MONTH)-1);
        FirstDate.set(Calendar.DAY_OF_MONTH, SpecificDay);
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(FirstDate.getTime());
    }
    /** A specific day of this month
     * @param SpecificDay specific day
     * */
    public String getThisMonthSpecifyDay(int SpecificDay){
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.MONTH, FirstDate.get(Calendar.MONTH));
        FirstDate.set(Calendar.DAY_OF_MONTH, SpecificDay);
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(FirstDate.getTime());
    }
    /** A specific day of next month
     * @param SpecificDay specific day
     * */
    public String getNextMonthSpecificDay(int SpecificDay){
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.MONTH, FirstDate.get(Calendar.MONTH)+1);
        FirstDate.set(Calendar.DAY_OF_MONTH, SpecificDay);
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(FirstDate.getTime());
    }
    /** A specific day of specific month
     * @param SpecificMonth specific month
     * @param SpecificDay specific day
     * */
    public String getSpecificMonthSpecificDay(int SpecificMonth, int SpecificDay){
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.MONTH, FirstDate.get(Calendar.MONTH)+SpecificMonth);
        FirstDate.set(Calendar.DAY_OF_MONTH, SpecificDay);
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(FirstDate.getTime());
    }
    public String getSpecificMonthLastDay(int specificMonth){
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.MONTH, FirstDate.get(Calendar.MONTH) + 1 + specificMonth);
        int lastDay = FirstDate.getMinimum(Calendar.DATE);
        FirstDate.set(Calendar.DAY_OF_MONTH, lastDay-1);
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(FirstDate.getTime());
    }
    public String getTodayAddOrSubtractDay(int Day){
        Calendar Calendar = java.util.Calendar.getInstance();
        Calendar.setTime(new Date());
        Calendar.add(java.util.Calendar.DAY_OF_WEEK, Day);
        SimpleDateFormat DateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return DateFormat.format(Calendar.getTime());
    }
    public String encodeURIComponent(String component) {
        String result;
        try {
            result = URLEncoder.encode(component, "UTF-8").replaceAll("\\%28", "(").replaceAll("\\%29", ")")
                    .replaceAll("\\+", "%20").replaceAll("\\%27", "'").replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~");
        } catch (Exception e) {
            result = component;
        }
        return result;
    }
    //	攻擊加密憑證
    public void SSLAttack(){
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  return null;    }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {  }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
    }
    /** Delete the picture file in windows
     * @param PicturePath the path of picture
     * */
    public void deleteExportQuotationPicture(String PicturePath){
        File File = new File(PicturePath);
        File.delete();
    }
}
