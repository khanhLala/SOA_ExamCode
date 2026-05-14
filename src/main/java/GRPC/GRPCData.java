package GRPC;

import io.grpc.*;

public class GRPCData {
    // Thêm throws Exception ở đây
    public static void main(String[] args) throws Exception {
        String host = "36.50.135.242";
        int port = 2240;
        String studentCode = "B22DCCN444"; 
        String questionAlias = "PfA1YuGQ";

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
        long sum = 0;
        if (!data.isEmpty()) {
            String[] numbers = data.split(",");
            for (String numStr : numbers) {
                sum += Integer.parseInt(numStr.trim());
            }
        }
        String answer = String.valueOf(sum);
        System.out.println(answer);

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