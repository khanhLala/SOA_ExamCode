package REST;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 Nội dung
    Mô tả
    Bạn cần chọn invoice hợp lệ từ phase 1 rồi gọi phase 2 với path param và query param đúng chuẩn.

    Giao thức
    Bước 1 — Lấy danh sách invoice (GET):

    GET /api/rest/path?studentCode=<mã_sv>&qCode=<qAlias trong đề>
    Bước 2 — Truy vấn theo path + query (GET):

    GET /api/rest/path/{invoiceId}?studentCode=<mã_sv>&qCode=<qAlias>&requestId=<requestId phase1>&currency=USD
    Ví dụ:

    GET /api/rest/path/2?studentCode=B22DCCN001&qCode=z3Np8Rk1&requestId=p4Ks7n2Q&currency=USD
    Yêu cầu
    invoiceId phải nằm trong danh sách phase 1.
    currency phải là USD.
    Endpoint phase 2 chỉ chấp nhận GET.
 */
public class RestPathNQueryValidation {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN444";
        String qCode = "Gp3TvGng"; 
        String baseUrl = "http://36.50.135.242:2230/api/rest/path";

        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();

        String step1Url = baseUrl + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpGet getReq1 = new HttpGet(step1Url);
        
        CloseableHttpResponse resp1 = client.execute(getReq1);
        String body1 = EntityUtils.toString(resp1.getEntity());
        System.out.println(body1);

        JsonObject json1 = gson.fromJson(body1, JsonObject.class);
        String requestId = json1.get("requestId").getAsString();
        
        // Lấy id của sản phẩm đầu tiên trong danh sách data
        JsonArray dataArray = json1.getAsJsonArray("data");
        int invoiceId = dataArray.get(0).getAsJsonObject().get("id").getAsInt();
        System.out.println(invoiceId);

        
        String step2Url = baseUrl + "/" + invoiceId
                        + "?studentCode=" + studentCode 
                        + "&qCode=" + qCode 
                        + "&requestId=" + requestId 
                        + "&currency=USD";
        
        System.out.println(step2Url);
        HttpGet getReq2 = new HttpGet(step2Url);

        CloseableHttpResponse resp2 = client.execute(getReq2);
        // đoạn này in ra KQ thôi, khi thi không cần code
        String finalResult = EntityUtils.toString(resp2.getEntity());
        System.out.println(finalResult);

        client.close();
    }
}
