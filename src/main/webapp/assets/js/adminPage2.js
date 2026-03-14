function addImageUrl() {
    var input = document.getElementById("urlInput");
    var url = input.value.trim();
    var countSpan = document.getElementById("countImg");
    var hiddenArea = document.getElementById("hidden-area");
    if (url === "") {
        alert("Vui lòng nhập link ảnh!");
        return;
    }
    var hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "image_urls";
    hiddenInput.value = url;
    hiddenArea.appendChild(hiddenInput);
    var currentCount = parseInt(countSpan.innerText);
    countSpan.innerText = currentCount + 1;
    input.value = "";
    alert("Đã thêm ảnh vào danh sách chờ!");
}
function addCat() {
    var popup = document.getElementById('form-add-cat');
    var content = document.getElementById('right-content');

    if (popup) {
        popup.style.display = 'block';
        // Làm mờ nền phía sau (nếu muốn)
        if (content) content.style.filter = 'blur(5px)';
    } else {
        console.error("Lỗi: Không tìm thấy ID form-add-cat");
    }
}

function dongFormThemLoai() {
    var popup = document.getElementById('form-add-cat');
    var content = document.getElementById('right-content');

    if (popup) {
        popup.style.display = 'none';
        // Bỏ làm mờ nền
        if (content) content.style.filter = 'none';
    }
}
function deleteCategory(id) {
    if (confirm("CẢNH BÁO: Bạn có chắc chắn muốn xóa loại sản phẩm này?\n(Lưu ý: Nếu loại này đang chứa sản phẩm thì sẽ xóa các sản phẩm có loại này)")) {
        // Điền ID vào form ẩn
        document.getElementById('input-cat-id').value = id;
        // Gửi form đi
        document.getElementById('form-delete-cat').submit();
    }
}

function openEditModal(button) {
    // 1. Lấy dữ liệu từ các thuộc tính data- của nút bấm
    var id = button.getAttribute("data-id");
    var name = button.getAttribute("data-name");
    var category = button.getAttribute("data-category");
    var price = button.getAttribute("data-price");
    var stock = button.getAttribute("data-stock");
    var weight = button.getAttribute("data-weight");
    var state = button.getAttribute("data-state");
    var desc = button.getAttribute("data-desc");

    // 2. Điền dữ liệu vào các ô input trong form (Dựa vào ID đã thêm ở Bước 1)
    document.getElementById("edit-id-hidden").value = id;        // Input ẩn gửi đi
    document.getElementById("edit-id-display").value = "#" + id; // Input hiện để xem

    document.getElementById("edit-name").value = name;
    document.getElementById("edit-price").value = price;
    document.getElementById("edit-stock").value = stock;
    document.getElementById("edit-weight").value = weight;
    document.getElementById("edit-desc").value = desc;

    var catSelect = document.getElementById("edit-category");
    if(catSelect) catSelect.value = category;

    var stateSelect = document.getElementById("edit-state");
    if (stateSelect) {
        if (state && state.trim() !== "") {
            stateSelect.value = state.trim().toLowerCase() === 'inactive'
                ? 'Inactive'
                : 'Active';
        } else {
            // DEFAULT – bắt buộc có
            stateSelect.value = 'Active';
        }
    }
}

function closeEditModal() {
    document.getElementById("form-remake").style.display = "none";
}

function toggleSelectAll(source) {
    let checkboxes = document.getElementsByName('productIds');
    for(let i=0, n=checkboxes.length; i<n; i++) {
        checkboxes[i].checked = source.checked;
    }
}

function deleteCheckedProducts() {
    const checkboxes = document.querySelectorAll('input[name="productIds"]:checked');

    if (checkboxes.length === 0) {
        alert("Vui lòng tích chọn vào ô vuông đầu dòng các sản phẩm cần xóa!");
        return;
    }

    if (confirm("Xóa " + checkboxes.length + " sản phẩm đã chọn?\n(Sản phẩm chưa bán sẽ xóa vĩnh viễn, đã bán sẽ bị ẩn)")) {
        let ids = [];
        checkboxes.forEach(cb => ids.push(cb.value));

        document.getElementById('delete-action').value = 'delete_list';
        document.getElementById('delete-ids-multi').value = ids.join(",");
        document.getElementById('delete-form').submit();
    }
}
