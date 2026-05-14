package REST;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 Nội dung
    Mô tả
    Bạn cần xử lý bài toán cập nhật trạng thái tài khoản theo mô hình 2 phase và gửi đủ thông tin audit.

    Giao thức
    Bước 1 — Lấy dữ liệu (GET):

    GET /api/rest/method?studentCode=<mã_sv>&qCode=<qAlias trong đề>
    Bước 2 — Cập nhật (PUT):

    PUT /api/rest/method/{requestId}
    Body JSON:

    {
      "studentCode": "B22DCCN001",
      "qCode": "a7Lm2Pq9",
      "answer": {
        "status": "ACTIVE",
        "activatedBy": "B22DCCN001",
        "auditNote": "manual-review-ok"
      }
    }
    Yêu cầu
    Phase 2 chỉ chấp nhận HTTP PUT.
    answer.status phải là ACTIVE.
    answer.activatedBy phải trùng với studentCode.
 */
public class PutWithAuditFields {
    public static void main(String[] args) throws Exception{
        String studentCode = "B22DCCN444";
        String qCode = "B6dqIyZZ";
        String baseUrl = "http://36.50.135.242:2230/api/rest/method";
        
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        
        //1. Lấy data        
        String getURL = baseUrl + "?studentCode=" + studentCode 
                                 + "&qCode=" + qCode;
        HttpGet getReq = new HttpGet(getURL);
        
        CloseableHttpResponse getResponse = client.execute(getReq);
        String respBody = EntityUtils.toString(getResponse.getEntity());
        System.out.println(respBody);
        
        JsonObject jsonBody = gson.fromJson(respBody, JsonObject.class);
        String requestId = jsonBody.get("requestId").getAsString();  
        
        //2. Gửi kết quả lên server
        JsonObject obj = new JsonObject();
        obj.addProperty("status", "ACTIVE");
        obj.addProperty("activatedBy", studentCode);
        obj.addProperty("auditNote", "manual-review-ok");
        
        String putUrl = baseUrl + "/" + requestId;
        
        JsonObject jsonAnswer = new JsonObject();
        jsonAnswer.addProperty("studentCode", studentCode);
        jsonAnswer.addProperty("qCode", qCode);
        // Vì JsonObject là kiểu DL của gson => add mà không parse
        // hàm add cho object, addProperty cho các key - value
        jsonAnswer.add("answer", obj);

        // Tạo PUT request
        HttpPut putReq = new HttpPut(putUrl);
        // Set body cho request (phải parse JsonAnswer từ Object sang Json)
        StringEntity entity = new StringEntity(gson.toJson(jsonAnswer), ContentType.APPLICATION_JSON);
        putReq.setEntity(entity);
        
        //Gửi KQ lên Server
        CloseableHttpResponse postResponse = client.execute(putReq);
        String result = EntityUtils.toString(postResponse.getEntity());
        System.out.println(result);
        
        client.close();
    }
}
