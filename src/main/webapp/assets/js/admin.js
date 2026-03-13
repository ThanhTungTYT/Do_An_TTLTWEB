document.addEventListener('DOMContentLoaded', () => {

    // 1. LẤY CÁC PHẦN TỬ CỐ ĐỊNH
    const menu = document.getElementById("left-menu");
    const content = document.getElementById("right-content");
    const btn_slider_top = document.getElementById("slide-top");

    // Xử lý nút Back to Top
    if (btn_slider_top) {
        window.addEventListener("scroll", () => {
            btn_slider_top.style.display = window.scrollY > 20 ? "block" : "none";
        });
        btn_slider_top.addEventListener("click", () => {
            window.scrollTo({top: 0, behavior: "smooth"});
        });
    }

    // 2. XỬ LÝ SỰ KIỆN CLICK TOÀN TRANG (Event Delegation)
    document.body.addEventListener('click', e => {

        // Khai báo các Modal (Lấy mới mỗi lần click để tránh lỗi null)
        const form_add = document.getElementById("form-add");           // Modal Thêm SP
        const form_remake = document.getElementById("form-remake");     // Modal Sửa SP
        const form_add_cat = document.getElementById("form-add-cat");   // Modal Thêm Loại
        const detail = document.getElementById("detail-p");             // Modal Chi tiết

        // --- A. XỬ LÝ MENU TRÁI ---
        if (e.target.closest('#slider-menu')) {
            if (getComputedStyle(menu).display !== "none") {
                menu.style.display = "none";
                content.style.width = "100%";
                // Căn giữa lại các form
                if(form_add) form_add.style.left = "50%";
                if(form_remake) form_remake.style.left = "50%";
                if(form_add_cat) form_add_cat.style.left = "50%";
                if(detail) detail.style.left = "50%";
            } else {
                menu.style.display = "";
                content.style.width = "80%";
                if(form_add) form_add.style.left = "60%";
                if(form_remake) form_remake.style.left = "60%";
                if(form_add_cat) form_add_cat.style.left = "60%";
                if(detail) detail.style.left = "60%";
            }
        }

        // --- B. FORM THÊM SẢN PHẨM (Nút #add) ---
        if (e.target.closest('#add')) {
            if (form_add) {
                form_add.style.display = 'block';
                if(content) content.style.filter = 'blur(5px)';
            }
        }
        if (e.target.closest('#take-off')) { // Nút đóng form thêm SP
            if (form_add) {
                form_add.style.display = 'none';
                if(content) content.style.filter = 'blur(0)';
            }
        }

        // --- C. FORM SỬA SẢN PHẨM (Nút class .remake) ---
        if (e.target.closest('.remake')) {
            if (form_remake) {
                form_remake.style.display = 'block';
                if(content) content.style.filter = 'blur(5px)';
            }
        }
        if (e.target.closest('#close-remake')) { // Nút đóng form sửa SP
            if (form_remake) {
                form_remake.style.display = 'none';
                if(content) content.style.filter = 'blur(0)';
            }
        }

        // --- D. FORM THÊM LOẠI SẢN PHẨM (Nút #add-cat) ---
        // Quan trọng: Đã khớp với ID add-cat bạn vừa sửa ở Bước 1
        if (e.target.closest('#add-cat')) {
            if (form_add_cat) {
                form_add_cat.style.display = 'block';
                if(content) content.style.filter = 'blur(5px)';
                console.log("Mở form loại thành công!");
            } else {
                console.error("Lỗi: Không tìm thấy div có id='form-add-cat'");
            }
        }
        if (e.target.closest('#close-cat')) { // Nút đóng form loại SP
            if (form_add_cat) {
                form_add_cat.style.display = 'none';
                if(content) content.style.filter = 'blur(0)';
            }
        }

        // --- E. FORM CHI TIẾT ---
        if (e.target.closest('.detail')) {
            if (detail) {
                detail.style.display = 'flex';
                if(content) content.style.filter = 'blur(5px)';
            }
        }
        // Nút đóng chung cho chi tiết (nếu có id="close")
        if (e.target.closest('#close')) {
            if (detail) {
                detail.style.display = 'none';
                if(content) content.style.filter = 'blur(0)';
            }
        }

    });
});