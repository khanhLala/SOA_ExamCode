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

/**
 * Mã câu hỏi: BjcYJml1
 * Exam Server: 36.50.135.242
 * 
 * Nội dung
    Mô tả
    Bài tập này yêu cầu bạn đọc một custom HTTP response header (X-Checksum) từ phản hồi Phase 1 và gửi lại giá trị đó trong request header ở Phase 2.

    Giao thức
    Bước 1 — Lấy dữ liệu (GET):

    GET /api/rest/header?studentCode=<mã_sv>&qCode=<qAlias>
    Phản hồi (body):

    {
      "requestId": "def56789",
      "data": [3421, 7890, 1234, 5678, 9012, 3456]
    }
    Phản hồi (header):

    X-Checksum: a3f2c1...  (SHA-256 của danh sách số)
    Bước 2 — Gửi đáp án (POST):

    POST /api/rest/header/submit
    Body JSON:

    {
      "studentCode": "<mã_sv>",
      "qCode": "<qAlias>",
      "requestId": "def56789"
    }
    Request header bắt buộc:

    X-Checksum: a3f2c1...  (giá trị đọc từ Phase 1)
    Phản hồi khi đúng:

    {"status": "AC", "message": "..."}
    Yêu cầu
    Đọc giá trị header X-Checksum từ phản hồi Phase 1.
    Gửi lại đúng giá trị đó trong request header X-Checksum ở Phase 2.
    Không cần tính SHA-256 thủ công — chỉ cần truyền lại giá trị đã nhận.
 */
public class RestHeader {

    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN444";
        String qCode = "BjcYJml1"; 
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