# 🚀 SOA Exam - gRPC Project

---

## 🛠️ Hướng dẫn khởi tạo Project

Để tạo một dự án tương tự, hãy thực hiện theo các bước sau trong môi trường IDE của bạn (Hiện tại dùng Netbeans):

### 1. Tạo dự án mới
- Chọn **File** -> **New Project**.
- Chọn danh mục: **Java with Maven**.
- Chọn loại dự án: **Java Application**.
- Nhấn **Next**, đặt tên dự án và nhấn **Finish**.

### 2. Cấu hình Maven
Sau khi dự án được tạo, bạn cần thiết lập các thư viện cần thiết (gRPC, Protobuf, Gson, Apache HttpClient) trong tệp cấu hình Maven (copy file pom.xml trong project này vào dự án của bạn)

👉 **[Xem chi tiết tệp pom.xml](pom.xml)**

### 3. Hướng dẫn lập trình

#### 🔹 Đối với phần REST
- Bạn có thể thực hiện lập trình bình thường bằng cách sử dụng các thư viện đã được cấu hình sẵn trong tệp `pom.xml` (như Gson, Apache HttpClient).

#### 🔹 Đối với phần gRPC
Để làm việc với gRPC, bạn cần thực hiện theo các bước sau để đảm bảo mã nguồn được sinh ra chính xác:

1.  **Tạo thư mục Proto**: Tìm đến thư mục lưu dự án trên máy tính (`File Explorer` -> ... -> `src` -> `main`, có thể click trái chuột vào mục dự án trong Netbeans, chọn **Properties** để xem đường dẫn đến dự án). Tại đây, tạo thêm một thư mục mới tên là **`proto`**.
2.  **Thao tác trong NetBeans**: Lúc này, quay lại giao diện NetBeans, bạn sẽ thấy xuất hiện mục **Other Sources**.
3.  **Tạo file định nghĩa**: Bên trong mục này, hãy tạo một file mới có đuôi `.proto` (ví dụ: `judge.proto`).
4.  **Cấu hình Proto**: Sao chép toàn bộ đoạn mã định nghĩa dịch vụ từ đề bài (bắt đầu từ dòng `syntax = "proto3";`...) và dán vào file vừa tạo.
5.  **Sinh mã nguồn**: Chuột phải vào dự án và chọn **Clean and Build**. Sau khi quá trình này hoàn tất, các class Java hỗ trợ gRPC sẽ được tự động tạo ra và bạn có thể bắt đầu viết code Client.
6.  **Lưu ý**: Mỗi lần code bài gRPC mới cần xoá file .proto của bài cũ đi để tránh xung đột do các phương thức thầy để đều giống nhau (mặc dù các file proto là giống nhau nên có thể không cần làm vậy, nhưng đi thi cứ nên làm cho chắc)

---

*Chúc bạn hoàn thành bài thi tốt!*
