package REST;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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
    Bài tập này yêu cầu bạn sử dụng phương thức HTTP PUT để cập nhật dữ liệu một tài nguyên. Bạn sẽ nhận dữ liệu ban đầu qua GET và gửi cập nhật qua PUT.

    Giao thức
    Bước 1 — Lấy dữ liệu (GET):

    GET /api/rest/method?studentCode=<mã_sv>&qCode=<qAlias>
    Phản hồi:

    {
      "requestId": "abc12345",
      "data": {
        "id": 512,
        "title": "Update task #7",
        "status": "pending"
      }
    }
    Bước 2 — Gửi cập nhật (PUT):

    PUT /api/rest/method/{requestId}
    Body JSON:

    {
      "studentCode": "<mã_sv>",
      "qCode": "<qAlias>",
      "answer": {
        "status": "done"
      }
    }
    Phản hồi khi đúng:

    {"status": "AC", "message": "..."}
    Yêu cầu
    Endpoint Phase 2 chỉ chấp nhận phương thức PUT — gửi GET/POST sẽ nhận lỗi 405.
    Trường answer.status phải có giá trị "done".
 */
public class RestMethodPut {
    public static void main(String[] args) throws Exception{
        String studentCode = "B22DCCN444";
        String qCode = "eZWUoUOp";
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
        obj.addProperty("status", "done");
        
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
