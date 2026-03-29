document.addEventListener("DOMContentLoaded", () => {

    const img_home = document.getElementById('img-home');
    const images = window.BANNERS || [];
    let currentIndex = 0;

    if (img_home && images.length > 0) {
        img_home.style.backgroundImage = `url("${images[0]}")`;

        setInterval(() => {
            currentIndex = (currentIndex + 1) % images.length;
            img_home.style.backgroundImage = `url("${images[currentIndex]}")`;
        }, 3000);
    }

    window.addEventListener("scroll", () => {
        document.querySelectorAll(".fade-in, .slide-up").forEach(el => {
            const rect = el.getBoundingClientRect();
            if (rect.top < window.innerHeight - 100) {
                el.classList.add("visible");
            }
        });
    });

    const products = document.querySelectorAll('.product');
    const preview = document.getElementById('productPreview');

    if (preview) {
        const img = document.getElementById('previewImg');
        const name = document.getElementById('previewName');
        const price = document.getElementById('previewPrice');
        const sold = document.getElementById('previewSold');
        const des = document.getElementById('previewDes');
        const rating = document.getElementById('previewRating');

        let hoverTimer;

        products.forEach(p => {
            p.addEventListener('mouseenter', () => {
                img.src = p.dataset.image;
                name.textContent = p.dataset.name;
                price.textContent = p.dataset.price;
                sold.textContent = `🔥 Đã bán: ${p.dataset.sold}`;
                des.textContent = p.dataset.des;
                rating.innerHTML = `<i class="fas fa-star" style="color:gold"></i><strong>${Number(p.dataset.avgr).toFixed(1)}</strong>/5`;

                hoverTimer = setTimeout(() => {
                    preview.classList.add('show');
                }, 120);
            });

            p.addEventListener('mouseleave', () => {
                clearTimeout(hoverTimer);
                preview.classList.remove('show');
            });
        });
    }

    const container = document.getElementById("product-list");
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

function scrollProduct(direction) {
    const container = document.getElementById("product-list");

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