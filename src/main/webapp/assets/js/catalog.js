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
        const total = parseInt(rangeMin.max);
        let min = parseInt(rangeMin.value);
        let max = parseInt(rangeMax.value);

        if (min > max - 50000) {
            rangeMin.value = max - 50000;
            min = parseInt(rangeMin.value);
        }

        const leftPct  = (min / total) * 100;
        const rightPct = (max / total) * 100;

        fill.style.left  = leftPct + "%";
        fill.style.width = (rightPct - leftPct) + "%";

        labelMin.textContent = fmt(min);
        labelMax.textContent = fmt(max);
    }

    rangeMin.addEventListener("input", updateSlider);
    rangeMax.addEventListener("input", updateSlider);
    updateSlider();
})();

function getBaseUrl() {
    const path = window.location.pathname;
    if (path.includes('search-product')) {
        const keyword = document.getElementById("currentKeyword")?.value || "";
        return `search-product?search=${encodeURIComponent(keyword)}`;
    }
    const cid = document.getElementById("currentCid")?.value || "0";
    return `catalog?cid=${cid}`;
}

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

function changeSort(sort) {
    window.location.href = buildUrl({ sort: sort, page: 1 });
}
function changePrice(range) {
    if (range === 'all') {
        window.location.href = buildUrl({
            minPrice: null,
            maxPrice: null,
            page: 1
        });
    } else {
        const parts = range.split('-');
        window.location.href = buildUrl({
            minPrice: parts[0],
            maxPrice: parts[1],
            page: 1
        });
    }
}

function applyPriceFilter() {
    const rangeMin = document.getElementById("range-min");
    const rangeMax = document.getElementById("range-max");
    window.location.href = buildUrl({
        minPrice: rangeMin.value,
        maxPrice: rangeMax.value,
        page: 1
    });
}
function changeCid(cid) {
    const url = new URL(window.location.href);

    if (url.pathname.includes('search-product')) {
        url.searchParams.set('cid', cid);
        url.searchParams.set('page', 1); // Reset về trang 1 khi đổi bộ lọc
    } else {
        url.pathname = url.pathname.replace('search-product', 'catalog');
        url.searchParams.delete('search');
        url.searchParams.set('cid', cid);
        url.searchParams.set('page', 1);
    }
    window.location.href = url.toString();
}