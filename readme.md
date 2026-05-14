# 🚀 SOA Exam - gRPC Project

## 📋 Yêu cầu hệ thống

*   **Java Development Kit (JDK):** Yêu cầu sử dụng **JDK 8** (cái này quan trọng vì JDK khác dễ lỗi khi code WSDL).
*   **Build Tool:** Maven (đã được tích hợp trong Netbeans/IntelliJ).

---

## ⚠️ Hướng dẫn xử lý JDK 8 (Nếu chưa đúng phiên bản)

Nếu máy bạn đang chạy JDK khác (11, 17, 21...), hãy thực hiện các bước sau để hạ về JDK 8 nhằm tránh lỗi WSDL:

### Bước 1: Kiểm tra Java Dependencies
- Mở tab **Projects**, kiểm tra mục **Java Dependencies**.
- Nếu thấy hiện JDK khác 8, hãy thực hiện các bước tiếp theo để cài đặt và cấu hình lại.

### Bước 2: Tải JDK 8 (LTS) qua NetBeans
- Vào **Tools** -> **Java Platforms** -> **Add Platform**.
- Chọn **Download OpenJDK**, nhấn **Next**.
- Kéo thanh trượt về **8 (LTS)**, chọn **Zulu** và nhấn **Next** để máy tự động tải về và cài đặt.

### Bước 3: Cấu hình Maven Runtime
- Vào **Tools** -> **Options**.
- Chọn thẻ **Java** -> tab **Maven**.
- Tại mục **JDK**, chọn đúng bản **JDK 1.8 (8 LTS)** vừa tải. Nhấn **OK**.

### Bước 4: Clean và Build
- Chuột phải vào Project -> chọn **Clean and Build**.

---

## 🛠️ Hướng dẫn khởi tạo Project

Để tạo một dự án tương tự, hãy thực hiện theo các bước sau trong môi trường IDE của bạn (Hiện tại dùng Netbeans):

### 1. Tạo dự án mới
- Chọn **File** -> **New Project**.
- Chọn danh mục: **Java with Maven**.
- Chọn loại dự án: **Java Application**.
- Nhấn **Next**, đặt tên dự án và nhấn **Finish**.

### 2. Cấu hình Maven
Sau khi dự án được tạo, bạn cần thiết lập các thư viện cần thiết (gRPC, Protobuf, Gson, Apache HttpClient) trong tệp cấu hình Maven (copy file pom.xml trong project này vào dự án của bạn). Sau đó Clean and Build để load thư viện.

👉 **[Xem chi tiết tệp pom.xml](pom.xml)**

### 3. Hướng dẫn lập trình

#### 🔹 Đối với phần REST
- Bạn có thể thực hiện lập trình bình thường bằng cách sử dụng các thư viện đã được cấu hình sẵn trong tệp `pom.xml` (như Gson, Apache HttpClient).


#### 🔹 Đối với phần gRPC
Để làm việc với gRPC, bạn cần thực hiện theo các bước sau để đảm bảo mã nguồn được sinh ra chính xác:

1.  **Tạo thư mục Proto**: Tìm đến thư mục lưu dự án trên máy tính (`File Explorer` -> ... -> `src` -> `main`, có thể click trái chuột vào mục dự án trong Netbeans, chọn **Properties** để xem đường dẫn đến dự án). Tại đây, tạo thêm một thư mục mới tên là **`proto`**.
2.  **Thao tác trong NetBeans**: Lúc này, quay lại giao diện NetBeans, bạn sẽ thấy xuất hiện mục **Other Sources**.
3.  **Tạo file định nghĩa**: Bên trong mục này, hãy tạo file `judge.proto`.
4.  **Cấu hình Proto**: Sao chép toàn bộ đoạn mã định nghĩa dịch vụ từ đề bài (bắt đầu từ dòng `syntax = "proto3";`...) và dán vào file vừa tạo.
5.  **Sinh mã nguồn**: Chuột phải vào dự án và chọn **Clean and Build**. Sau khi quá trình này hoàn tất, các class Java hỗ trợ gRPC sẽ được tự động tạo ra và bạn có thể bắt đầu viết code Client.
6.  **Lưu ý**: Mỗi lần code bài gRPC mới cần xoá file .proto của bài cũ đi để tránh xung đột do các phương thức thầy để đều giống nhau (mặc dù các file proto là giống nhau nên có thể không cần làm vậy, nhưng đi thi cứ nên làm cho chắc)

#### 🔹 Đối với phần SOAP
Để sử dụng Web Service (SOAP), thực hiện các bước sau:
1.  **Tạo Web Service Client**: Chuột phải vào dự án -> Chọn **New** -> Chọn **Web Service Client**. (Nếu không thấy, chọn **Other...** -> **Web Services** -> **Web Service Client**).
2.  **Cấu hình WSDL**: Tại ô **WSDL URL**, dán địa chỉ WSDL của đề bài cung cấp (ví dụ: `http://36.50.135.242:2221/CharacterService?wsdl`).
3.  **Lưu ý quan trọng**: **Không cần điền Package (để trống)** để IDE tự lấy theo Namespace của server, giúp tránh lỗi xung đột.
4.  **Hoàn tất**: Nhấn **Finish**. IDE sẽ tự động gọi Maven chạy `wsimport` để sinh code vào thư mục **Generated Sources (jaxws)**.

---

*Chúc bạn hoàn thành bài thi tốt!*
