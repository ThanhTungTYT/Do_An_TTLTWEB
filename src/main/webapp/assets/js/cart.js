document.addEventListener("DOMContentLoaded", function () {

    const checkboxes = document.querySelectorAll(".item-checkbox");
    const totalDisplay = document.getElementById("cart-total");
    const selectAllBtn = document.querySelector(".select-all-cart");
    const cartBadge = document.getElementById("num-cart-label");

    function formatVND(amount) {
        return amount.toLocaleString("vi-VN") + " VND";
    }

    function updateDisplayTotal() {
        let total = 0;
        document.querySelectorAll(".item-checkbox:checked").forEach(box => {
            total += parseFloat(box.dataset.subtotal);
        });
        totalDisplay.textContent = formatVND(total);
    }

    checkboxes.forEach(cb => {
        cb.addEventListener("change", updateDisplayTotal);
    });

    if (selectAllBtn) {
        selectAllBtn.addEventListener("click", function (e) {
            e.preventDefault();
            const allChecked = [...document.querySelectorAll(".item-checkbox")].every(cb => cb.checked);
            document.querySelectorAll(".item-checkbox").forEach(cb => cb.checked = !allChecked);
            this.textContent = !allChecked ? "Bỏ chọn tất cả" : "Chọn tất cả";
            updateDisplayTotal();
        });
    }

    updateDisplayTotal();

    const btnDecreases = document.querySelectorAll(".btn-decrease");
    const btnIncreases = document.querySelectorAll(".btn-increase");
    const qtyInputs = document.querySelectorAll(".qty-input");

    function updateCartAjax(pid, quantity, inputEl) {
        if (quantity < 1) {
            quantity = 1;
            inputEl.value = 1;
        }

        fetch(`update-cart?pid=${pid}&q=${quantity}&ajax=true`)
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    inputEl.value = quantity;

                    const subtotalEl = document.getElementById(`subtotal-${pid}`);
                    if (subtotalEl) {
                        subtotalEl.textContent = formatVND(data.newSubtotal);
                    }

                    const checkboxEl = document.getElementById(`checkbox-${pid}`);
                    if (checkboxEl) {
                        checkboxEl.dataset.subtotal = data.newSubtotal;
                    }

                    updateDisplayTotal();

                    if (cartBadge) {
                        cartBadge.textContent = data.totalCartQuantity;
                    }
                }
            })
            .catch(error => console.error("Lỗi AJAX:", error));
    }

    btnDecreases.forEach(btn => {
        btn.addEventListener("click", function() {
            const pid = this.dataset.pid;
            const inputEl = this.nextElementSibling;
            let currentQty = parseInt(inputEl.value);
            if (currentQty > 1) {
                updateCartAjax(pid, currentQty - 1, inputEl);
            }
        });
    });

    // Bắt sự kiện nút tăng
    btnIncreases.forEach(btn => {
        btn.addEventListener("click", function() {
            const pid = this.dataset.pid;
            const inputEl = this.previousElementSibling;
            let currentQty = parseInt(inputEl.value);
            updateCartAjax(pid, currentQty + 1, inputEl);
        });
    });

    qtyInputs.forEach(input => {
        input.addEventListener("change", function() {
            const pid = this.dataset.pid;
            let currentQty = parseInt(this.value);
            if (isNaN(currentQty) || currentQty < 1) {
                currentQty = 1;
            }
            updateCartAjax(pid, currentQty, this);
        });
    });
});