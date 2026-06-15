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
function changeCid(cid) {
    window.location.href = buildUrl({ filter: cid, page: 1 });
}

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
        stateSelect.value = (state && state.trim().toLowerCase() === 'inactive') ? 'inactive' : 'active';
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
    document.getElementById("right-content").style.filter = "none";
}

function openEditCat(id, name, state) {
    document.getElementById('edit-cat-id').value    = id;
    document.getElementById('edit-cat-name').value  = name;
    document.getElementById('edit-cat-state').value = state;
    const form = document.getElementById('form-edit-cat');
    if (form) {
        form.style.display = 'flex';
        document.getElementById('right-content').style.filter = 'blur(5px)';
    }
}

function closeEditCat() {
    const form = document.getElementById('form-edit-cat');
    if (form) form.style.display = 'none';
    document.getElementById('right-content').style.filter = 'none';
}

function toggleSelectAll(source) {
    let checkboxes = document.getElementsByName('productIds');
    for(let i=0, n=checkboxes.length; i<n; i++) {
        checkboxes[i].checked = source.checked;
    }
}

function toggleCheckedProducts() {
    const checkboxes = document.querySelectorAll('input[name="productIds"]:checked');
    if (checkboxes.length === 0) return;
    let ids = [];
    checkboxes.forEach(cb => ids.push(cb.value));
    document.getElementById('delete-action').value    = 'toggle_list';
    document.getElementById('delete-ids-multi').value = ids.join(",");
    document.getElementById('delete-form').submit();
}

// Xem thêm / Thu gọn danh sách loại
(function () {
    const rows = document.querySelectorAll('.cat-row');
    const btn = document.getElementById('btn-show-more-cat');
    if (rows.length > 5 && btn) btn.style.display = 'inline-block';
})();

let catShown = 5;

function showMoreCat() {
    const rows = document.querySelectorAll('.cat-row');
    const btn = document.getElementById('btn-show-more-cat');

    if (catShown >= rows.length) {
        for (let i = 5; i < rows.length; i++) rows[i].style.display = 'none';
        catShown = 5;
        btn.innerHTML = '<i class="fa-solid fa-chevron-down"></i> Xem thêm';
    } else {
        // Xem thêm
        const next = catShown + 5;
        for (let i = catShown; i < next && i < rows.length; i++) rows[i].style.display = '';
        catShown = next;
        if (catShown >= rows.length) {
            btn.innerHTML = '<i class="fa-solid fa-chevron-up"></i> Thu gọn';
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    // Scroll wheel trong form
    const formIds = ['form-add', 'form-remake', 'form-add-cat', 'form-edit-cat'];
    formIds.forEach(function (id) {
        const form = document.getElementById(id);
        if (!form) return;
        const scrollArea = form.querySelector('.main-form');
        if (!scrollArea) return;

        scrollArea.addEventListener('wheel', function (e) {
            const atTop    = scrollArea.scrollTop === 0;
            const atBottom = scrollArea.scrollTop + scrollArea.clientHeight >= scrollArea.scrollHeight;

            if ((atTop && e.deltaY < 0) || (atBottom && e.deltaY > 0)) return;

            e.stopPropagation();
        }, { passive: true });
    });

    // Page input
    const pageInput = document.getElementById('page-input');
    if (pageInput) {
        pageInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                let targetPage = parseInt(this.value, 10);
                let maxPage    = parseInt(this.getAttribute('data-max'), 10);
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