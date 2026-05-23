const minus = document.getElementById("count-minus");
const add = document.getElementById("count-add");
const count = document.getElementById("num-count");

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