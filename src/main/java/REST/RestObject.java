package REST;


import com.google.gson.Gson;
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
 * Mã câu hỏi: YTL9nQoh
 * Exam Server: 36.50.135.242
 * Nội dung
    Một dịch vụ REST được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/object để xử lý các bài toán với đối tượng.

    Yêu cầu: Viết chương trình Java (REST client) để giao tiếp với ObjectService và thực hiện các công việc sau:

    Gửi HTTP GET request tới /api/rest/object?studentCode=<mã_sv>&qCode=<qCode trong đề bài> để nhận về một đối tượng JSON từ server.

    Response JSON có dạng:

    {
      "requestId": "m1n2o3p4",
      "data": {
        "name": "Laptop Pro",
        "price": 100.0,
        "taxRate": 10.0,
        "discount": 5.0
      }
    }
    Trong đó discount là phần trăm chiết khấu (%).

    Tính toán giá cuối cùng finalPrice theo công thức:

    finalPrice = price × (1 + taxRate / 100) × (1 - discount / 100)
    Gửi HTTP POST request tới /api/rest/object/submit với body JSON:

    {
      "studentCode": "B21DCCN001",
      "qCode": "<qCode trong đề bài>",
      "requestId": "m1n2o3p4",
      "answer": {
        "name": "Laptop Pro",
        "price": 100.0,
        "taxRate": 10.0,
        "discount": 5.0,
        "finalPrice": 104.5
      }
    }
    Trong body JSON trên, trường "requestId": "m1n2o3p4" là requestId nhận được ở bước 1.

    Ví dụ: price=100.0, taxRate=10.0, discount=5.0 -> finalPrice = 100 × 1.1 × 0.95 = 104.5

    Sai số cho phép: <= 0.01.

    Kết thúc chương trình client. Server trả về {"status":"AC"} hoặc {"status":"WA"}.
 */
public class RestObject {
    
    // nên tách riêng 1 file cho tiện
    public static class Product{
        private String name;
        private double price;
        private double taxRate;
        private double discount;
        private double finalPrice;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(double taxRate) {
            this.taxRate = taxRate;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String studentCode = "B22DCCN444";
        String qCode = "YTL9nQoh";
        String baseUrl = "http://36.50.135.242:2230/api/rest/object";
        
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
        JsonObject data = jsonBody.getAsJsonObject("data");
        System.out.println(data);
        //Parse sang product
        Product product = gson.fromJson(data, Product.class);
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
        double finalPrice = product.getPrice() * (1 + product.getTaxRate() / 100) * (1 - product.getDiscount() / 100);
        product.setFinalPrice(finalPrice);
                
        //3. Gửi kết quả lên server
        String postUrl = baseUrl + "/submit";
        
        // Tạo đối tượng để POST lên server
        // addProperty("key",value): thêm trường vào json
        JsonObject jsonAnswer = new JsonObject();
        jsonAnswer.addProperty("studentCode", studentCode);
        jsonAnswer.addProperty("qCode", qCode);
        jsonAnswer.addProperty("requestId", requestId);
        // add object thì phải pasre sang JsonElement (khác Json string nên k dùng toJson thuần))
        jsonAnswer.add("answer", gson.toJsonTree(product));
        
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
