/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SOAP;

import ptit.dblab.judge.network.server.soap.DataService;

import java.util.List;
import ptit.dblab.judge.network.server.soap.SoapDataService;

/**
 Nội dung
    Một dịch vụ SOAP DataService được triển khai trên server.

    WSDL: http://<Exam_IP>:2221/DataService?wsdl

    Yêu cầu: Viết chương trình Java (SOAP client) thực hiện các công việc sau:

    Tạo Web Service Client từ WSDL URL trên (dùng wsimport hoặc NetBeans/IntelliJ Web Service Client wizard).

    Gọi phương thức getData(studentCode, qCode) với qCode = "<qCode trong đề bài>" để nhận về danh sách số nguyên List<Integer> từ server.

    Ví dụ: Server trả về [12, 45, 88, 3, 210, 50].

    Tính tổng tất cả các phần tử trong danh sách nhận được.

    Gọi phương thức submitDataInt(studentCode, qCode, sum) để gửi kết quả tổng trở lại server.

    Ví dụ: Nếu danh sách là [1, 2, 3, 4, 5], tổng = 15 -> gọi submitDataInt("B21DCCN001", "<qCode trong đề bài>", 15).

    Kết thúc chương trình client. Server trả về status string ("AC" hoặc "WA").
 */
public class SOAPData {

    public static void main(String[] args) throws Exception{

        DataService service = new DataService();

        // 2. Lấy Port 
        SoapDataService port = service.getSoapDataServicePort();

        String studentCode = "B22DCCN444";
        String qCode = "oM5Ia4Hq";

        // 3. Lấy danh sách số nguyên từ server
        List<Integer> data = port.getData(studentCode, qCode);
        System.out.println("Danh sách nhận được: " + data);

        // 4. Tính tổng
        int sum = 0;

        for (Integer num : data) {
            sum += num;
        }

        System.out.println(sum);

        // 5. Gửi kết quả lên server
        // submitDataInt  trả về void nên không lấy được status
        port.submitDataInt(studentCode, qCode, sum);
    }
}
