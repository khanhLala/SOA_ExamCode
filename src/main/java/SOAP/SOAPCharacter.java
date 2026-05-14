package SOAP;

import ptit.dblab.judge.network.server.soap.CharacterService;
import ptit.dblab.judge.network.server.soap.SoapCharacterService;

/**
 Nội dung
    Một dịch vụ SOAP CharacterService được triển khai trên server.

    WSDL: http://<Exam_IP>:2221/CharacterService?wsdl

    Yêu cầu: Viết chương trình Java (SOAP client) thực hiện các công việc sau:

    Tạo Web Service Client từ WSDL URL trên (dùng wsimport hoặc NetBeans/IntelliJ Web Service Client wizard).

    Gọi phương thức requestString(studentCode, qCode) với qCode = "<qCode trong đề bài>" để nhận về một chuỗi ký tự từ server.

    Ví dụ: Server trả về "HelloWorld".

    Đảo ngược chuỗi ký tự nhận được.

    Ví dụ: "HelloWorld" -> "dlroWolleH".

    Gọi phương thức submitString(studentCode, qCode, reversedString) để gửi kết quả trở lại server.

    Ví dụ: submitString("B21DCCN001", "<qCode trong đề bài>", "dlroWolleH").

    Kết thúc chương trình client. Server trả về status string ("AC" hoặc "WA").
 */
public class SOAPCharacter {

    public static void main(String[] args) throws Exception{
        // 1. Khởi tạo Service và Port
        CharacterService service = new CharacterService();
        SoapCharacterService port = service.getSoapCharacterServicePort();

        String studentCode = "B22DCCN444";
        String qCode = "2FgYzq6b";

        // 2. Gọi phương thức requestString để nhận chuỗi từ server
        String originalString = port.requestString(studentCode, qCode);
        System.out.println(originalString);

        // 3. Đảo ngược chuỗi ký tự
        String reversedString = new StringBuilder(originalString).reverse().toString();
        System.out.println(reversedString);

        // 4. Gọi phương thức submitString để gửi kết quả trở lại server
        port.submitString(studentCode, qCode, reversedString);
    }
}
