package REST;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * Mã câu hỏi: jdnbr1oR
 * Exam Server: 36.50.135.242
 * 
 * Nội dung
    Mô tả
    Bài tập này yêu cầu bạn sử dụng path parameter và query parameter để truy vấn một tài nguyên cụ thể trong danh sách sản phẩm.

    Giao thức
    Bước 1 — Lấy danh sách sản phẩm (GET):

    GET /api/rest/path?studentCode=<mã_sv>&qCode=<qAlias>
    Phản hồi:

    {
      "requestId": "ghi01234",
      "data": [
        {"id": 1, "name": "Laptop", "priceVND": 15000000},
        {"id": 2, "name": "Smartphone", "priceVND": 8500000},
        {"id": 3, "name": "Tablet", "priceVND": 6200000}
      ]
    }
    Bước 2 — Truy vấn sản phẩm theo ID (GET):

    GET /api/rest/path/{productId}?studentCode=<mã_sv>&qCode=<qAlias>&requestId=ghi01234&currency=USD
    Ví dụ: truy vấn sản phẩm có id=2:

    GET /api/rest/path/2?studentCode=B22DCCN001&qCode=<qAlias>&requestId=ghi01234&currency=USD
    Phản hồi khi đúng:

    {"status": "AC", "message": "..."}
    Yêu cầu
    Chọn bất kỳ id hợp lệ từ danh sách Phase 1 rồi đưa vào path.
    Truyền requestId từ Phase 1 qua query parameter.
    Truyền currency=USD qua query parameter.
    Endpoint Phase 2 chỉ chấp nhận phương thức GET.
 */
public class RestPath {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN444";
        String qCode = "jdnbr1oR"; 
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
        int productId = dataArray.get(0).getAsJsonObject().get("id").getAsInt();
        System.out.println("Chọn Product ID: " + productId);

        
        String step2Url = baseUrl + "/" + productId 
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