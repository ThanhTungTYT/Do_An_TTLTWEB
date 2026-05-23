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

(function () {
    const rangeMin = document.getElementById("range-min");
    const rangeMax = document.getElementById("range-max");
    const fill     = document.getElementById("slider-fill");
    const labelMin = document.getElementById("price-min-label");
    const labelMax = document.getElementById("price-max-label");

    if (!rangeMin || !rangeMax) return;

    function fmt(val) {
        return parseInt(val).toLocaleString("vi-VN") + "đ";
    }

    function updateSlider() {
        const min   = parseInt(rangeMin.value);
        const max   = parseInt(rangeMax.value);
        const total = parseInt(rangeMin.max);

        if (min > max - 50000) rangeMin.value = max - 50000;

        const leftPct  = (parseInt(rangeMin.value) / total) * 100;
        const rightPct = (parseInt(rangeMax.value) / total) * 100;

        fill.style.left  = leftPct + "%";
        fill.style.width = (rightPct - leftPct) + "%";

        labelMin.textContent = fmt(rangeMin.value);
        labelMax.textContent = fmt(rangeMax.value);
    }

    rangeMin.addEventListener("input", updateSlider);
    rangeMax.addEventListener("input", updateSlider);
    updateSlider();
})();

function applyPriceFilter() {
    const rangeMin = document.getElementById("range-min");
    const rangeMax = document.getElementById("range-max");
    const url = new URL(window.location.href);
    url.searchParams.set("minPrice", rangeMin.value);
    url.searchParams.set("maxPrice", rangeMax.value);
    url.searchParams.set("page", "1");
    window.location.href = url.toString();
}

function changePage(page) {
    const url = new URL(window.location.href);
    url.searchParams.set("page", page);
    window.location.href = url.toString();
}

function changeSort(sort) {
    const url = new URL(window.location.href);
    url.searchParams.set("sort", sort);
    url.searchParams.set("page", "1");
    window.location.href = url.toString();
}
function changePrice(range) {
    const url = new URL(window.location.href);
    url.searchParams.set("page", "1");

    if (range === 'all') {
        url.searchParams.delete("minPrice");
        url.searchParams.delete("maxPrice");
    } else {
        const parts = range.split('-');
        url.searchParams.set("minPrice", parts[0]);
        url.searchParams.set("maxPrice", parts[1]);
    }

    window.location.href = url.toString();
}