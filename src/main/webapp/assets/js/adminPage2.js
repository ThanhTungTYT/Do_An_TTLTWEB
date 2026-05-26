function buildUrl(overrides) {
    const url = new URL(window.location.href);
    Object.keys(overrides).forEach(k => {
        if (overrides[k] === null) {
            url.searchParams.delete(k);
        } else {
            url.searchParams.set(k, overrides[k]);
        }
    });
    return url.toString();
}

function changePage(page) {
    window.location.href = buildUrl({ page: page });
}

document.addEventListener('DOMContentLoaded', function () {
    const pageInput = document.getElementById('page-input');
    if (pageInput) {
        pageInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                let targetPage = parseInt(this.value, 10);
                let maxPage = parseInt(this.getAttribute('data-max'), 10);
                if (isNaN(targetPage) || targetPage < 1) targetPage = 1;
                else if (targetPage > maxPage) targetPage = maxPage;
                changePage(targetPage);
            }
        });
        pageInput.addEventListener('blur', function () {
            this.value = this.defaultValue;
        });
    }
});

function previewImage(input) {
    const file = input.files[0];
    if (!file) return;

    if (file.size > 5 * 1024 * 1024) {
        alert("Ảnh không được vượt quá 5MB!");
        input.value = "";
        return;
    }

    const box = input.closest(".upload-box");
    const preview = box.querySelector(".upload-preview");

    const reader = new FileReader();
    reader.onload = function (e) {
        preview.src = e.target.result;
        box.classList.add("filled");
    };
    reader.readAsDataURL(file);
}

function setBoxImage(box, imgUrl) {
    const fileInput = box.querySelector("input[type='file']");
    const preview = box.querySelector(".upload-preview");
    fileInput.value = "";
    if (imgUrl && imgUrl.trim() !== "") {
        preview.src = imgUrl;
        box.classList.add("filled");
    } else {
        preview.src = "";
        box.classList.remove("filled");
    }
}
function addCat() {
    var popup = document.getElementById('form-add-cat');
    var content = document.getElementById('right-content');

    if (popup) {
        popup.style.display = 'block';
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
        if (content) content.style.filter = 'none';
    }
}
function deleteCategory(id) {
    if (confirm("CẢNH BÁO: Bạn có chắc chắn muốn xóa loại sản phẩm này?\n(Lưu ý: Nếu loại này đang chứa sản phẩm thì sẽ xóa các sản phẩm có loại này)")) {
        document.getElementById('input-cat-id').value = id;
        document.getElementById('form-delete-cat').submit();
    }
}

function openEditModal(button) {
    var id = button.getAttribute("data-id");
    var name = button.getAttribute("data-name");
    var category = button.getAttribute("data-category");
    var price = button.getAttribute("data-price");
    var stock = button.getAttribute("data-stock");
    var weight = button.getAttribute("data-weight");
    var state = button.getAttribute("data-state");
    var desc = button.getAttribute("data-desc");

    document.getElementById("edit-id-hidden").value = id;
    document.getElementById("edit-id-display").value = "#" + id;

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
            stateSelect.value = 'Active';
        }
    }

    var imgMain = button.getAttribute("data-img-main");
    var imgSub1 = button.getAttribute("data-img-sub1");
    var imgSub2 = button.getAttribute("data-img-sub2");
    setBoxImage(document.getElementById("edit-box-main"), imgMain);
    setBoxImage(document.getElementById("edit-box-sub1"), imgSub1);
    setBoxImage(document.getElementById("edit-box-sub2"), imgSub2);
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
