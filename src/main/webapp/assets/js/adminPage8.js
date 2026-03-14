document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("form-promotion");
    const btnOpenAdd = document.getElementById("btn-open-add");
    const btnClose = document.getElementById("take-off");
    const content = document.getElementById("right-content");

    const mainForm = document.getElementById("main-form");
    const contextPath = mainForm ? mainForm.getAttribute("data-context") : "";

    const formTitle = document.getElementById("popup-title");
    const inputId = document.getElementById("input-id");

    const inputCode = document.getElementById("input-code");
    const inputDesc = document.getElementById("input-description");
    const inputMin = document.getElementById("input-min");
    const inputDiscount = document.getElementById("input-discount");
    const inputQuantity = document.getElementById("input-quantity");
    const inputStart = document.getElementById("input-start");
    const inputEnd = document.getElementById("input-end");
    const inputState = document.getElementById("input-state");
    const errorElement = document.querySelector(".form-error");

    function showModal() {
        if (modal) {
            modal.style.display = "block";
            modal.style.top = "50%";
            modal.style.left = "60%";
            modal.style.transform = "translate(-50%, -50%)";
            if (content) content.style.filter = "blur(5px)";
        }
    }

    function hideModal() {
        if (modal) {
            modal.style.display = "none";
            if (content) content.style.filter = "none";
        }
    }

    if (errorElement) {
        showModal();
        if (inputId && inputId.value) {
            if(mainForm) mainForm.action = contextPath + "/update";
            if(inputCode) {
                inputCode.setAttribute("readonly", true);
                inputCode.style.backgroundColor = "#e9ecef";
            }
        } else {
            if(mainForm) mainForm.action = contextPath + "/add";
        }
    }

    if (btnOpenAdd) {
        btnOpenAdd.addEventListener("click", function (e) {
            e.preventDefault();

            if(mainForm) mainForm.action = contextPath + "/add";

            if(formTitle) formTitle.innerText = "THÊM MÃ KHUYẾN MÃI";
            if(inputId) inputId.value = "";

            if(inputCode) {
                inputCode.value = "";
                inputCode.removeAttribute("readonly");
                inputCode.style.backgroundColor = "white";
                inputCode.style.cursor = "text";
            }

            if(inputDesc) inputDesc.value = "";
            if(inputMin) inputMin.value = "";
            if(inputDiscount) inputDiscount.value = "";
            if(inputQuantity) inputQuantity.value = "";
            if(inputStart) inputStart.value = "";
            if(inputEnd) inputEnd.value = "";
            if(inputState) inputState.value = "active";

            if(errorElement) errorElement.style.display = 'none';

            showModal();
        });
    }

    document.body.addEventListener("click", function (e) {
        const editBtn = e.target.closest(".btn-edit");

        if (editBtn) {
            e.preventDefault();
            const data = editBtn.dataset;

            if(mainForm) mainForm.action = contextPath + "/update";

            if(formTitle) formTitle.innerText = "CẬP NHẬT MÃ #" + data.id;
            if(inputId) inputId.value = data.id;

            if(inputCode) {
                inputCode.value = data.code;
                inputCode.setAttribute("readonly", true);
                inputCode.style.backgroundColor = "#e9ecef";
                inputCode.style.cursor = "not-allowed";
            }

            if(inputDesc) inputDesc.value = data.desc;
            if(inputMin) inputMin.value = data.min;
            if(inputDiscount) inputDiscount.value = data.discount;
            if(inputQuantity) inputQuantity.value = data.quantity;
            if(inputStart) inputStart.value = data.start;
            if(inputEnd) inputEnd.value = data.end;
            if(inputState) inputState.value = data.state;

            if(errorElement) errorElement.style.display = 'none';

            showModal();
        }
    });

    if (btnClose) {
        btnClose.addEventListener("click", function (e) {
            e.preventDefault();
            hideModal();
        });
    }
});