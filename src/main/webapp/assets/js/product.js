document.addEventListener("DOMContentLoaded", function () {
    const toggleBtn = document.getElementById('readMoreBtn');
    const content = document.getElementById('contentToCollapse');
    const container = document.getElementById('productDescription');
    if (toggleBtn && content && container) {
        toggleBtn.addEventListener('click', function () {
            container.classList.toggle('is-expanded');
            if (container.classList.contains('is-expanded')) {
                content.style.maxHeight = content.scrollHeight + 'px';
                this.innerHTML = 'Thu gọn <span class="arrow"></span>';
            } else {
                content.style.maxHeight = null;
                this.innerHTML = 'Xem thêm <span class="arrow"></span>';
            }
        });
    } else {
        console.warn("Không tìm thấy các phần tử để thực hiện chức năng 'Xem thêm'.");
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const mainProductImage = document.getElementById('img-main');
    const thumbnails = document.querySelectorAll('.thumbnail-item');

    thumbnails.forEach(thumbnail => {
        thumbnail.addEventListener('click', function () {
            thumbnails.forEach(item => item.classList.remove('active'));
            this.classList.add('active');
            const fullImageUrl = this.getAttribute('data-full-image');
            mainProductImage.src = fullImageUrl;
        });
    });
});

function scrollProduct(direction) {
    const container = document.getElementById("product-catalog");
    const cardWidth = 270;
    const scrollAmount = cardWidth * 2;

    container.scrollBy({
        left: direction * scrollAmount,
        behavior: "smooth"
    });

    setTimeout(() => {
        const event = new Event("scroll");
        container.dispatchEvent(event);
    }, 300);
}

document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("product-catalog");
    const leftBtn = document.querySelector(".nav.left");
    const rightBtn = document.querySelector(".nav.right");

    setTimeout(() => {
        container.scrollLeft = 0;
    }, 50);

    if (container && leftBtn && rightBtn) {

        function updateButtons() {
            if (!container) return;
            const isAtStart = container.scrollLeft <= 1;
            const isAtEnd = container.scrollLeft + container.clientWidth >= container.scrollWidth - 1;
            leftBtn.style.display = isAtStart ? "none" : "flex";
            rightBtn.style.display = isAtEnd ? "none" : "flex";
        }

        container.addEventListener("scroll", updateButtons);
        setTimeout(updateButtons, 50);
    }
});

(function () {
    const numCount = document.getElementById("num-count");
    const qInput = document.getElementById("q");
    const addBtn = document.getElementById("count-add");
    const minusBtn = document.getElementById("count-minus");

    if (addBtn && minusBtn && numCount && qInput) {
        addBtn.onclick = () => {
            let v = parseInt(numCount.innerText) + 1;
            numCount.innerText = v;
            qInput.value = v;
        };
        minusBtn.onclick = () => {
            let v = Math.max(1, parseInt(numCount.innerText) - 1);
            numCount.innerText = v;
            qInput.value = v;
        };
    }
})();

function confirmDelete(rid, pid) {
    document.getElementById("delete-rid").value = rid;
    document.getElementById("delete-pid").value = pid;
    const overlay = document.getElementById("confirm-delete-overlay");
    overlay.style.display = "flex";
}

function closeConfirm() {
    document.getElementById("confirm-delete-overlay").style.display = "none";
}

function submitDelete() {
    document.getElementById("delete-review-form").submit();
}

(function () {
    const overlay = document.getElementById("confirm-delete-overlay");
    if (overlay) {
        overlay.onclick = function (e) {
            if (e.target === this) closeConfirm();
        };
    }
})();

function loadMoreReviews() {
    const hiddenReviews = document.querySelectorAll('.review-hidden');
    let count = 0;

    hiddenReviews.forEach(item => {
        if (count < 10) {
            item.style.display = 'block';
            item.classList.remove('review-hidden');
            count++;
        }
    });

    const remaining = document.querySelectorAll('.review-hidden').length;
    const hiddenCountSpan = document.getElementById('hidden-count');
    if (hiddenCountSpan) {
        hiddenCountSpan.textContent = remaining;
    }

    if (remaining === 0) {
        const wrapper = document.getElementById('load-more-reviews-wrapper');
        if (wrapper) {
            wrapper.style.display = 'none';
        }
    }
}

(function () {
    const form = document.getElementById('review-form');
    const ta = document.getElementById('review-comment');
    const counter = document.getElementById('review-counter');
    const err = document.getElementById('review-error');
    if (!form || !ta) return;

    const MAX = 500;
    const MIN = 1;

    function updateCounter() {
        const len = ta.value.length;
        counter.textContent = len + ' / ' + MAX + ' ký tự';
        counter.style.color = len > MAX ? '#dc3545' : (len >= MAX * 0.9 ? '#e67e22' : '#888');
    }
    ta.addEventListener('input', updateCounter);
    updateCounter();

    form.addEventListener('submit', function (e) {
        const val = ta.value.trim();
        if (val.length < MIN) {
            e.preventDefault();
            err.textContent = 'Đánh giá phải có ít nhất ' + MIN + ' ký tự.';
            err.style.display = 'block';
            ta.focus();
            return;
        }
        if (val.length > MAX) {
            e.preventDefault();
            err.textContent = 'Đánh giá không được quá ' + MAX + ' ký tự.';
            err.style.display = 'block';
            ta.focus();
            return;
        }
        err.style.display = 'none';
    });
})();