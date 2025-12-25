package ERP.Model.ManagePayableReceivable;

import ERP.Bean.ManagePayableReceivable.PayContent_Bean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import okio.GzipSource;
import okio.Okio;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IAECrawler {
    private HashMap<String, String> header = new HashMap<>();
    private String destination = "http://140.133.78.37:81";
    private String account = "";
    private DateTimeFormatter twDateFormat = DateTimeFormatter.ofPattern("yyyMMdd");
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }};
    private static final SSLContext trustAllSslContext;
    public static final SSLSocketFactory trustAllSslSocketFactory;

    public IAECrawler() {
        this.header.put("Host", "140.133.78.37");
    }

    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    public void addSession(String session) {
        this.setHeader("Cookie", "ASP.NET_SessionId=" + session);
    }

    public void setDestination(String destination) throws MalformedURLException {
        this.destination = destination;
        URL url = new URL(destination);
        this.header.put("Host", url.getHost());
    }

    public boolean login(String account, String password) throws IOException {
        this.account = account;
        String data;
        Response indexPage = this.getPage(this.destination);
        if (indexPage.code() == 200 && indexPage.body() != null) {
            Document document = Jsoup.parse(indexPage.body().string());
            String session = this.getSession(indexPage.header("Set-Cookie"));
            if (!session.isEmpty() && !this.header.containsKey("Cookie")) {
                this.header.put("Cookie", session);
            }

            Response systemInit = this.postForm(this.destination + "/Services/SYSINIT.ashx", this.header, new HashMap<>());
            if (session.isEmpty()) {
                session = this.getSession(systemInit.header("Set-Cookie"));
            }

            if (!this.header.containsKey("Cookie")) {
                this.header.put("Cookie", session);
            }

            String customerNumber = "";
            if ("https://cashweb.nkust.edu.tw".equals(this.destination)) {
                customerNumber = this.getCustomerNumber();
                data = this.getLoginJson(account, password, customerNumber, document);
                HashMap<String, String> formData = new HashMap<>();
                formData.put("dtLOGIN", data);
                Response login = this.postForm(this.destination + "/Services/Login.ashx", this.header, formData);
                return login.code() == 200;
            } else if ("https://cash.nknu.edu.tw/".equals(this.destination)) {
                customerNumber = this.getCustomerNumber("");
                System.out.println("customerNumber: " + customerNumber);
                data = this.getLoginJson(account, password, customerNumber, document);
                HashMap<String, String> formData = new HashMap<>();
                String encodedData = encodeBase64(encodeURIComponent(data));
                formData.put("dtLOGIN", encodedData);
                Response login = this.postForm(this.destination + "/Services/Login.ashx", this.header, formData);
                return login.code() == 200;
            }

            return false;
        } else {
            return false;
        }
    }

    public String getPayListRaw(LocalDate startDate, LocalDate endDate, String invoiceNumber, IAECrawler.PayListFunction function) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        if ("https://cashweb.nkust.edu.tw".equals(this.destination)) {
            objectNode.put("xIDNO", this.account);
            objectNode.put("xVENDORID", this.account);
            objectNode.put("xID_RELEVANCE", this.account);
            objectNode.put("xVENDORID2", this.account);
        } else if ("https://cash.nknu.edu.tw/".equals(this.destination)) {
            ResponseBody response =
                    this.getForm(this.destination + "PayList.aspx", this.header, null).body();
            Element element = Jsoup.parse(response.string()).getElementById("xIDNO");
            //if (element != null) {
                String xIDNO = encodeBase64(encodeURIComponent(this.account));//element.val();
                objectNode.put("xIDNO", xIDNO);
                objectNode.put("xVENDORID", xIDNO);
                objectNode.put("xID_RELEVANCE", xIDNO);
            objectNode.put("xVENDORID2", xIDNO);
                objectNode.put("xEMAIL", "");
            //}
        }

        String start = startDate.minusYears(1911L).format(this.twDateFormat);
        String end = endDate.minusYears(1911L).format(this.twDateFormat);
        HashMap<String, String> formData = new HashMap<>();
        formData.put("funcName", function.name());

        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(objectNode);

        //objectNode.put("xVENDORID2", this.account);
        objectNode.put("SDATE", start);
        objectNode.put("EDATE", end);
        objectNode.put("INVNO", invoiceNumber);
        formData.put("FilterData", encodeURIComponent(arrayNode.toString()));
        formData.put("_search", "false");
        formData.put("nd", Long.toString(DateTime.now().getMillis()));
        formData.put("rows", "999");
        formData.put("page", "1");
        formData.put("sidx", "");
        formData.put("sord", "asc");
        formData.put("totalrows", "");
        ResponseBody response = this.postForm(this.destination + "/services/PAYLIST.ashx", this.header, formData).body();
        return response == null ? null : response.string();
    }

    public PayContent_Bean[] getPayList(LocalDate startDate, LocalDate endDate, String invoiceNumber, IAECrawler.PayListFunction function) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String aa = this.getPayListRaw(startDate, endDate, invoiceNumber, function);
        ArrayList<PayContent_Bean> list = new ArrayList<>();
        Iterator var8 = mapper.readTree(aa).iterator();

        while(var8.hasNext()) {
            JsonNode node = (JsonNode)var8.next();
            try {
                list.add(mapper.treeToValue(node, PayContent_Bean.class));
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }

        return list.toArray(new PayContent_Bean[0]);
    }

    private String getLoginJson(String account, String password, String customerNumber, Document document) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        ObjectNode objectNode = mapper.createObjectNode();
        node.add(objectNode);
        this.putValue(objectNode, document, "__VIEWSTATE");
        this.putValue(objectNode, document, "__VIEWSTATEGENERATOR");
        this.putValue(objectNode, document, "__EVENTVALIDATION");
        objectNode.put("LOGINID", account);
        objectNode.put("LOGINPW", password);
        objectNode.put("TOKENKEY", "");
        objectNode.put("CUSTNO", customerNumber);
        objectNode.put("MAINID", "");
        objectNode.put("MAINPW", "");
        objectNode.put("LOGINKIND", "1");
        return node.toString();
    }

    public String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encodeBase64(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());
        return new String(encodedBytes);
    }

    public String decodeBase64(String encoded) {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded.getBytes());
        return new String(decodedBytes);
    }

    private void putValue(ObjectNode node, Document document, String id) {
        node.put(id, document.getElementById(id).val());
    }

    private String getCustomerNumber() throws IOException {
        HashMap<String, String> formData = new HashMap<>();
        formData.put("funcName", "GETUNITVAR");
        formData.put("VARNAME", "CUSTNO");
        Response response = this.postForm(this.destination + "/Services/CASHFUNC.ashx", this.header, formData);
        if (!response.isSuccessful()) {
            throw new IOException("request customer number fail");
        } else {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.string().replaceAll("\"", "");
            } else {
                throw new IOException("get customer number fail");
            }
        }
    }

    private String getCustomerNumber(String mm) throws IOException {
        HashMap<String, String> formData = new HashMap<>();
        formData.put("funcName", "GETUNITVAR");
        formData.put("VARNAME", "Q1VTVE5P");
        Response response = this.postForm(this.destination + "/Services/CASHFUNC.ashx", this.header, formData);
        if (!response.isSuccessful()) {
            throw new IOException("request customer number fail");
        } else {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.string().replaceAll("\"", "");
            } else {
                throw new IOException("get customer number fail");
            }
        }
    }

    public Response getPage(String address) throws IOException {
        OkHttpClient client = trustAllSslClient((new OkHttpClient()).newBuilder().build());
        Request.Builder requestBuilder = (new Request.Builder()).url(address).get();
        Iterator var4 = this.header.keySet().iterator();

        while(var4.hasNext()) {
            String key = (String)var4.next();
            requestBuilder.addHeader(key, (String)this.header.get(key));
        }

        Request request = requestBuilder.build();
        return client.newCall(request).execute();
    }

    public Response postForm(String address, HashMap<String, String> headers, HashMap<String, String> formData) throws IOException {
        okhttp3.FormBody.Builder formBuilder = new okhttp3.FormBody.Builder();
        Iterator var5 = formData.keySet().iterator();

        while(var5.hasNext()) {
            String key = (String)var5.next();
            formBuilder.addEncoded(key, (String)formData.get(key));
        }

        FormBody requestBody = formBuilder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("Accept", "*/*");
        requestBuilder.addHeader("Accept-Encoding", "gzip, deflate, br");
        requestBuilder.addHeader("Connection", "keep-alive");
        requestBuilder.addHeader("Content-Length", Long.toString(requestBody.contentLength()));
        requestBuilder.addHeader("Content-Type", requestBody.contentType().toString());
        requestBuilder.addHeader("Cache-Control", "no-cache");
        Iterator var7 = headers.keySet().iterator();

        while(var7.hasNext()) {
            String key = (String)var7.next();
            requestBuilder.addHeader(key, (String)headers.get(key));
        }

        OkHttpClient client = trustAllSslClient((new OkHttpClient()).newBuilder().build());
        okhttp3.OkHttpClient.Builder httpClient = client.newBuilder();
        httpClient.addInterceptor(new UnzippingInterceptor());
        requestBuilder.url(address).post(requestBody);
        Request request = requestBuilder.build();
        return httpClient.build().newCall(request).execute();
    }

    public Response getForm(String address, HashMap<String, String> headers, HashMap<String, String> formData) throws IOException {
        okhttp3.FormBody.Builder formBuilder = new okhttp3.FormBody.Builder();
//        Iterator var5 = formData.keySet().iterator();
//
//        while(var5.hasNext()) {
//            String key = (String)var5.next();
//            formBuilder.addEncoded(key, (String)formData.get(key));
//        }

        FormBody requestBody = formBuilder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("Accept", "*/*");
        requestBuilder.addHeader("Accept-Encoding", "gzip, deflate, br");
        requestBuilder.addHeader("Connection", "keep-alive");
        requestBuilder.addHeader("Content-Length", Long.toString(requestBody.contentLength()));
        requestBuilder.addHeader("Content-Type", requestBody.contentType().toString());
        requestBuilder.addHeader("Cache-Control", "no-cache");
        Iterator var7 = headers.keySet().iterator();

        while(var7.hasNext()) {
            String key = (String)var7.next();
            requestBuilder.addHeader(key, (String)headers.get(key));
        }

        OkHttpClient client = trustAllSslClient((new OkHttpClient()).newBuilder().build());
        okhttp3.OkHttpClient.Builder httpClient = client.newBuilder();
        httpClient.addInterceptor(new UnzippingInterceptor());
        requestBuilder.url(address).get();
        Request request = requestBuilder.build();
        return httpClient.build().newCall(request).execute();
    }

    private String getSession(String cookies) {
        if (cookies == null) {
            return "";
        } else {
            String[] var2 = cookies.split(";");
            int var3 = var2.length;
            for (String cookie : var2) {
                String[] pair = cookie.split("=");
                String key = pair[0];
                if (key.equals("ASP.NET_SessionId")) {
                    return cookie;
                }
            }
            return "";
        }
    }

    public static OkHttpClient trustAllSslClient(OkHttpClient client) {
        okhttp3.OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        builder.hostnameVerifier((hostname, session) -> true);
        return builder.build();
    }

    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException var1) {
            throw new RuntimeException(var1);
        }

        trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();
    }

    private class UnzippingInterceptor implements Interceptor {
        private UnzippingInterceptor() {
        }

        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            return this.unzip(response);
        }

        private Response unzip(Response response) throws IOException {
            if (response.body() == null) {
                return response;
            } else {
                String contentEncoding = response.headers().get("Content-Encoding");
                if (contentEncoding != null && contentEncoding.equals("gzip")) {
                    Long contentLength = response.body().contentLength();
                    GzipSource responseBody = new GzipSource(response.body().source());
                    Headers strippedHeaders = response.headers().newBuilder().build();
                    return response.newBuilder().headers(strippedHeaders).body(new RealResponseBody(response.body().contentType().toString(), contentLength, Okio.buffer(responseBody))).build();
                } else {
                    return response;
                }
            }
        }
    }

    public static enum PayListFunction {
        CASHNET,
        CPS,
        NEWCASH,
        QCASH,
        PTCNET,
        PTC,
        QPTC,
        YEARGOT,
        PAYNET,
        GETINV;

        private PayListFunction() {
        }
    }
}
