package REST;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Arrays;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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
 *
 * @author PC
 */

/**
    Mã câu hỏi: In6fGlQf
    Exam Server: 36.50.135.242
    * 
    Đề bài:
    * 
    Một dịch vụ REST được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/character để xử lý các bài toán về chuỗi và ký tự.

    Yêu cầu: Viết chương trình Java (REST client) để giao tiếp với CharacterService và thực hiện các công việc sau:

    Gửi HTTP GET request tới /api/rest/character?studentCode=<mã_sv>&qCode=<qCode trong đề bài> để nhận về một đối tượng JSON từ server.

    Response JSON có dạng:

    {
      "requestId": "x1y2z3w4",
      "data": "banana apple cherry date elderberry"
    }
    Tách chuỗi thành các từ dựa trên khoảng trắng, sau đó sắp xếp các từ theo thứ tự từ điển (alphabetical order, phân biệt hoa thường - case-sensitive).

    Gửi HTTP POST request tới /api/rest/character/submit với body JSON:

    {
      "studentCode": "B21DCCN001",
      "qCode": "<qCode trong đề bài>",
      "requestId": "x1y2z3w4",
      "answer": "apple banana cherry date elderberry"
    }
    Trong body JSON trên, trường "requestId": "x1y2z3w4" là requestId nhận được ở bước 1.

    Các từ nối lại bằng dấu cách đơn.

    Ví dụ 1: "banana apple cherry" -> sắp xếp -> "apple banana cherry"

    Ví dụ 2 (case-sensitive): "Cherry apple Banana" -> sắp xếp theo thứ tự từ điển -> "Banana Cherry apple" (chữ hoa đứng trước chữ thường trong ASCII)

    Kết thúc chương trình client. Server trả về {"status":"AC"} hoặc {"status":"WA"}.
*/

public class RestCharacter {
    public static void main(String[] args) throws Exception{
        String studentCode = "B22DCCN444";
        String qCode = "In6fGlQf";
        String baseUrl = "http://36.50.135.242:2230/api/rest/character";
        
        // Khai báo Gson để parse JSON và Client (luôn luôn)
        Gson gson = new Gson();
        CloseableHttpClient client = HttpClients.createDefault();
        
        //1. Lấy data 
        
        // Đường dẫn
        String getURL = baseUrl + "?studentCode=" + studentCode 
                                 + "&qCode=" + qCode;
        // Gọi phương thức GET đến URL
        HttpGet getReq = new HttpGet(getURL);
        
        // Lấy Response từ phương thức GET
        CloseableHttpResponse getResponse = client.execute(getReq);
        // Lấy phần Body của response
        String respBody = EntityUtils.toString(getResponse.getEntity());
        System.out.println(respBody);
        
        /** Parse JSON lấy data
        Phân biệt: Data lấy từ API về sẽ là 1 Json String
        Sau khi parse String bằng Gson (hàm fromJson) => từ json string thành kiểu JsonObject (kiểu DL của Gson)
        Muốn biến JsonObject thành Object tự định nghĩa => không thể ép kiểu
        Dùng hàm fromJson biến từ JsonObject thành Object của chúng ta
        => Life Cycle: Lấy về là json string => parse qua fromJson() ra JsonObject => parse tiếp qua fromJson ra Object chúng ta cần
        => Ngược lại: từ Object của chúng ta => toJsonTree() ra JsonObject => toJson ra Json string
        có 2 kiểu fromJson : từ json String -> JsonObject
        toJson: từ obj -> json
        param đầu là cái muốn chuyển, sau là KiểuDL muốn chuyển .class
        có đường tắt: từ Json String về Object tự định nghĩa: chỉ cần dùng fromJson, param thứ 2 để là KiểuDL.class là ok, vd Product.class
        tương tự có thể dùng toJson từ Object sang thẳng json string (k nên dùng vì bài này còn addProperty, trừ khi tạo thêm hẳn 1 class lưu Object đáp án post lên server)
        */
        JsonObject jsonBody = gson.fromJson(respBody, JsonObject.class);
        // lấy requestId
        String requestId = jsonBody.get("requestId").getAsString();
        // lấy data 
        String data = jsonBody.get("data").getAsString();
        System.out.println(data);
        /**
         * Giải thích một chút: khi nào getAs+KiểuDL luôn; khi nào get trước rồi mới getAs
         * Với dữ liệu đơn lẻ: 1 số long, 1 String => get trước rồi mới getAsKiểuDL
         * Với dữ liệu mảng: getAsJsonArray: getAsJsonKiểuDL
         * 
         * Để dễ hiểu hơn: Trong Gson tất cả đều là JsonElement lồng nhau
         * Ví dụ: nếu sau này trong data là 1 object, vd: User thì:
         *  Kiểu trả về là JsonObject, phương thức GetAsJsonObject
         *  Từ trong JsonObject lấy được có thể lấy getAsJsonArray, getAsLong,.. tuỳ DL, thậm chí là JsonObject nếu Object lồng nhau
        */
        
        //2. Xử lý
        String words[] = data.split("\\s+");
        Arrays.sort(words);
        String ans = String.join(" ", words);
                
        //3. Gửi kết quả lên server
        String postUrl = baseUrl + "/submit";
        
        // Tạo đối tượng để POST lên server
        // addProperty("key",value): thêm trường vào json
        JsonObject jsonAnswer = new JsonObject();
        jsonAnswer.addProperty("studentCode", studentCode);
        jsonAnswer.addProperty("qCode", qCode);
        jsonAnswer.addProperty("requestId", requestId);
        jsonAnswer.addProperty("answer", ans);
        
        // Tạo POST request
        HttpPost postReq = new HttpPost(postUrl);
        // Set body cho request (phải parse JsonAnswer từ Object sang Json)
        StringEntity entity = new StringEntity(gson.toJson(jsonAnswer), ContentType.APPLICATION_JSON);
        postReq.setEntity(entity);
        
        //Gửi KQ lên Server
        CloseableHttpResponse postResponse = client.execute(postReq);
        String result = EntityUtils.toString(postResponse.getEntity());
        System.out.println(result);
        
        client.close();
    }
}
