document.addEventListener("DOMContentLoaded", function () {

    const checkboxes      = document.querySelectorAll(".item-checkbox");
    const totalDisplay    = document.getElementById("cart-total");        // ẩn, giữ tương thích
    const selectAllBtn    = document.querySelector(".select-all-cart");   // link "Chọn tất cả" trên list
    const cartBadge       = document.getElementById("num-cart-label");

    const stickyBar          = document.getElementById("sticky-checkout-bar");
    const stickyTotal        = document.getElementById("sticky-total");
    const stickySelectAllCb  = document.getElementById("sticky-select-all-cb");
    const stickySelectedCount = document.getElementById("sticky-selected-count");
    const stickyCountBtn     = document.getElementById("sticky-count-btn");
    const stickyCheckoutBtn  = document.getElementById("sticky-checkout-btn");


    function formatVND(amount) {
        return amount.toLocaleString("vi-VN") + " VND";
    }

    function bumpTotal() {
        if (!stickyTotal) return;
        stickyTotal.classList.remove("bump");
        void stickyTotal.offsetWidth;
        stickyTotal.classList.add("bump");
        setTimeout(() => stickyTotal.classList.remove("bump"), 200);
    }


    function updateDisplayTotal() {
        let total        = 0;
        let checkedCount = 0;
        const allBoxes   = document.querySelectorAll(".item-checkbox");

        allBoxes.forEach(box => {
            if (box.checked) {
                total += parseFloat(box.dataset.subtotal) || 0;
                checkedCount++;
            }
        });

        if (totalDisplay) totalDisplay.textContent = formatVND(total);

        if (stickyTotal) {
            stickyTotal.textContent = formatVND(total);
            bumpTotal();
        }
        if (stickySelectedCount) {
            stickySelectedCount.textContent = checkedCount + " sản phẩm";
        }
        if (stickyCountBtn) {
            stickyCountBtn.textContent = "(" + checkedCount + ")";
        }
        if (stickyCheckoutBtn) {
            stickyCheckoutBtn.disabled = (checkedCount === 0);
        }

        if (stickySelectAllCb) {
            const allChecked = checkedCount === allBoxes.length && allBoxes.length > 0;
            stickySelectAllCb.checked       = allChecked;
            stickySelectAllCb.indeterminate = checkedCount > 0 && !allChecked;
        }

        if (selectAllBtn) {
            const allChecked = checkedCount === allBoxes.length && allBoxes.length > 0;
            selectAllBtn.textContent = allChecked ? "Bỏ chọn tất cả" : "Chọn tất cả";
        }
    }


    checkboxes.forEach(cb => {
        cb.addEventListener("change", updateDisplayTotal);
    });


    if (selectAllBtn) {
        selectAllBtn.addEventListener("click", function (e) {
            e.preventDefault();
            const allBoxes   = document.querySelectorAll(".item-checkbox");
            const allChecked = [...allBoxes].every(cb => cb.checked);
            allBoxes.forEach(cb => cb.checked = !allChecked);
            updateDisplayTotal();
        });
    }


    if (stickySelectAllCb) {
        stickySelectAllCb.addEventListener("change", function () {
            document.querySelectorAll(".item-checkbox").forEach(cb => {
                cb.checked = this.checked;
            });
            updateDisplayTotal();
        });
    }


    updateDisplayTotal();


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


    document.querySelectorAll(".btn-decrease").forEach(btn => {
        btn.addEventListener("click", function () {
            const pid       = this.dataset.pid;
            const inputEl   = this.nextElementSibling;
            const currentQty = parseInt(inputEl.value);
            if (currentQty > 1) {
                updateCartAjax(pid, currentQty - 1, inputEl);
            }
        });
    });


    document.querySelectorAll(".btn-increase").forEach(btn => {
        btn.addEventListener("click", function () {
            const pid       = this.dataset.pid;
            const inputEl   = this.previousElementSibling;
            const currentQty = parseInt(inputEl.value);
            updateCartAjax(pid, currentQty + 1, inputEl);
        });
    });


    document.querySelectorAll(".qty-input").forEach(input => {
        input.addEventListener("change", function () {
            const pid = this.dataset.pid;
            let qty   = parseInt(this.value);
            if (isNaN(qty) || qty < 1) qty = 1;
            updateCartAjax(pid, qty, this);
        });
    });

});