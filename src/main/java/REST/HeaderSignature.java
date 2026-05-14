package REST;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 Nội dung
    Mô tả
    Bạn cần đọc chữ ký checksum từ response header ở phase 1 và gửi lại đúng trong request header ở phase 2.

    Giao thức
    Bước 1 — Lấy dữ liệu (GET):

    GET /api/rest/header?studentCode=<mã_sv>&qCode=<qAlias trong đề>
    Response header:

    X-Checksum: 8a3d8b4f9b4b77a90a0a9f9f43f2c43f2ce28f562e0b1726b5405c8c2512de67
    Bước 2 — Submit (POST):

    POST /api/rest/header/submit
    Header bắt buộc:

    X-Checksum: <giá trị đọc từ phase 1>
    Body JSON:

    {
      "studentCode": "B22DCCN001",
      "qCode": "k9T2uV5m",
      "requestId": "x2Q8p4Lm"
    }
    Yêu cầu
    Đọc đúng và gửi lại đúng header X-Checksum.
    Body phải có đủ studentCode, qCode, requestId.
 */
public class HeaderSignature {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN444";
        String qCode = "bI5DNjl5"; 
        String baseUrl = "http://36.50.135.242:2230/api/rest/header";

        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        String getURL = baseUrl + "?studentCode=" + studentCode + "&qCode=" + qCode;
        HttpGet getReq = new HttpGet(getURL);

        CloseableHttpResponse getResponse = client.execute(getReq);
        
        // Đọc giá trị X-Checksum từ Response Header
        Header checksumHeader = getResponse.getFirstHeader("X-Checksum");
        String checksumValue = checksumHeader.getValue();
        System.out.println(checksumValue);


        String respBody = EntityUtils.toString(getResponse.getEntity());
        JsonObject jsonBody = gson.fromJson(respBody, JsonObject.class);
        String requestId = jsonBody.get("requestId").getAsString();
        System.out.println(requestId);

        HttpPost postReq = new HttpPost(baseUrl + "/submit");
        
        // Thêm Header X-Checksum vào Request
        postReq.addHeader("X-Checksum", checksumValue);

        JsonObject jsonAnswer = new JsonObject();
        jsonAnswer.addProperty("studentCode", studentCode);
        jsonAnswer.addProperty("qCode", qCode);
        jsonAnswer.addProperty("requestId", requestId);

        StringEntity entity = new StringEntity(gson.toJson(jsonAnswer), ContentType.APPLICATION_JSON);
        postReq.setEntity(entity);

        CloseableHttpResponse postResponse = client.execute(postReq);
        String result = EntityUtils.toString(postResponse.getEntity());
        System.out.println("Kết quả Server: " + result);

        client.close();
    }
}
