package SOAP;

import ptit.dblab.judge.network.server.soap.ObjectService;
import ptit.dblab.judge.network.server.soap.SoapObjectService;
import ptit.dblab.judge.network.server.soap.ProductY;

/**
 * Nội dung 
    * Một dịch vụ SOAP ObjectService được triển khai trên server.
    *
    * WSDL: http://<Exam_IP>:2221/ObjectService?wsdl
    *
    * Yêu cầu: Viết chương trình Java (SOAP client) thực hiện các công việc sau:
    *
    * Tạo Web Service Client từ WSDL URL trên (dùng wsimport hoặc NetBeans/IntelliJ
    * Web Service Client wizard).
    *
    * Gọi phương thức requestProductY(studentCode, qCode) với qCode =
    * "<qCode trong đề bài>" để nhận về một đối tượng ProductY từ server, có các
    * thuộc tính name, price, taxRate, discount và finalPrice.
    *
    * Ví dụ: ProductY{name="Laptop", price=1000.0, taxRate=10.0, discount=5.0,
    * finalPrice=0.0}
    *
    * Tính giá cuối cùng theo công thức:
    *
    * finalPrice = price * (1 + taxRate / 100) * (1 - discount / 100) Ví dụ:
    * price=1000, taxRate=10, discount=5 -> finalPrice = 1000 * 1.10 * 0.95 =
    * 1045.0
    *
    * Gán finalPrice vào đối tượng ProductY, sau đó gọi submitProductY(studentCode,
    * qCode, productY) để gửi trở lại server.
    *
    * Ví dụ: submitProductY("B21DCCN001", "<qCode trong đề bài>", updatedProductY).
    *
    * Kết thúc chương trình client. Server trả về status string ("AC" hoặc "WA").
 */
public class SOAPObject {

    public static void main(String[] args) throws Exception {
        // Khởi tạo kết nối tới Service
        ObjectService service = new ObjectService();
        SoapObjectService port = service.getSoapObjectServicePort();

        String studentCode = "B22DCCN444"; 
        String qCode = "179kiz3i";

        //Gọi phương thức để nhận đối tượng ProductY từ server
        ProductY product = port.requestProductY(studentCode, qCode);

        float price = product.getPrice();
        float taxRate = product.getTaxRate();
        float discount = product.getDiscount();

        // Tính toán
        float finalPrice = price * (1 + taxRate / 100f) * (1 - discount / 100f);

        product.setFinalPrice(finalPrice);
        System.out.println(finalPrice);

        // Gửi đối tượng đã cập nhật trở lại server
        port.submitProductY(studentCode, qCode, product);

    }
}
