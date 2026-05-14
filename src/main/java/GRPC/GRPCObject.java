/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GRPC;

import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Locale;

/**
 Nội dung
    Một dịch vụ gRPC JudgeService được triển khai trên server tại <Exam_IP>:2240.

    Yêu cầu: Viết chương trình Java (gRPC client) để giao tiếp với JudgeService và thực hiện các công việc sau:

    Gọi phương thức Request với student_code là mã sinh viên và question_alias là <question_alias trong đề bài>.

    Nhận về JudgeResponse chứa request_id là chuỗi định danh và data là chuỗi JSON mô tả sản phẩm, ví dụ:

    {"name":"ProductABC","price":150.0,"taxRate":10.0,"discount":15.0}
    Trong đó discount là giá trị tuyệt đối (số tiền chiết khấu, không phải %).

    Parse chuỗi JSON và tính giá cuối cùng theo công thức:

    finalPrice = price × (1 + taxRate / 100) - discount
    Gọi phương thức Submit với request_id là giá trị nhận được ở bước 1 và answer là giá trị finalPrice làm tròn 2 chữ số thập phân, dạng chuỗi, ví dụ "150.00".

    Trong lời gọi Submit, request_id phải là giá trị đã nhận được ở bước 1.

    Sai số cho phép: <= 0.01.

    Ví dụ: price=150.0, taxRate=10.0, discount=15.0 -> finalPrice = 150 × 1.1 - 15 = 150.0 -> answer = "150.00"

    Đóng kênh gRPC và kết thúc chương trình.

    IDL (Proto Contract)
    syntax = "proto3";
    package GRPC;
    option java_package = "GRPC";
    option java_multiple_files = true;

    service JudgeService {
      rpc Request (JudgeRequest) returns (JudgeResponse);
      rpc Submit  (SubmitRequest) returns (SubmitResponse);
    }

    message JudgeRequest {
      string student_code    = 1;
      string question_alias  = 2;
    }

    message JudgeResponse {
      string request_id = 1;
      string data       = 2;
    }

    message SubmitRequest {
      string student_code    = 1;
      string question_alias  = 2;
      string request_id      = 3;
      string answer          = 4;
    }

    message SubmitResponse {
      string status  = 1;
      string message = 2;
    }
    Field numbers phải giữ nguyên để đúng wire format protobuf. package GRPC và service name JudgeService phải đúng theo đặc tả.
 */
public class GRPCObject {
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
    public static void main(String[] args) throws Exception {
        String host = "36.50.135.242";
        int port = 2240;
        String studentCode = "B22DCCN444"; 
        String questionAlias = "9hUjIBJS";
        
        Gson gson = new Gson();

        // 1. Tạo kết nối với host và port
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        /** 2. Tạo Stub
         * Để kết nối 2 phía => gRPC tạo 1 stub (đường ống) thông để tiện trao đổi
         * Vì nó thông nên có thể gọi luôn hàm từ server như gọi hàm bình thường
        */
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

        // 3. Gọi phương thức Request để tạo request
        JudgeRequest request = JudgeRequest.newBuilder()
                .setStudentCode(studentCode)
                .setQuestionAlias(questionAlias)
                .build();
        
        // vì là gọi hàm kiểu trả về là JudgeResponse nên lấy được reponse nhưng phải gọi qua stub
        JudgeResponse response = stub.request(request);
        String requestId = response.getRequestId();
        String data = response.getData();

        System.out.println(requestId);
        System.out.println(data);

        // 4. Xử lý logic 
        // muốn biết kiểu dữ liệu của data => nhìn vào file .proto/JudgeResponse
        // bài này data là 1 json string => dùng gson xử lý
        Product product = gson.fromJson(data, Product.class);
        double finalPrice = product.getPrice() * (1 + product.getTaxRate() / 100) - product.getDiscount();
        String answer = String.format(Locale.US,"%.2f", finalPrice);

        // 5. Submit bằng cách gọi phương thức SubmitRequest
        SubmitRequest submitReq = SubmitRequest.newBuilder()
                .setStudentCode(studentCode)
                .setQuestionAlias(questionAlias)
                .setRequestId(requestId)
                .setAnswer(answer)
                .build();

        SubmitResponse submitRes = stub.submit(submitReq);

        // 6. Hiển thị kết quả
        System.out.println(submitRes.getStatus());
        System.out.println(submitRes.getMessage());

        channel.shutdown();
    }
}

