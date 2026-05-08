document.addEventListener('DOMContentLoaded', function () {
    const pageInput = document.getElementById('page-input');

    if (pageInput) {
        pageInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();

                let targetPage = parseInt(this.value, 10);
                let maxPage = parseInt(this.getAttribute('data-max'), 10);

                if (isNaN(targetPage) || targetPage < 1) {
                    targetPage = 1;
                } else if (targetPage > maxPage) {
                    targetPage = maxPage;
                }

                changePage(targetPage);
            }
        });

        pageInput.addEventListener('blur', function () {
            this.value = this.defaultValue;
        });
    }
});

function changePage(page) {
    const urlParams = new URLSearchParams(window.location.search);

    const cid = document.getElementById("currentCid")?.value || urlParams.get('cid') || "0";
    const sort = document.getElementById("currentSort")?.value || urlParams.get('sort') || "default";
    const price = urlParams.get('price') || "all";
    const search = urlParams.get('search') || "";

    let newUrl = `catalog?cid=${cid}&sort=${sort}&price=${price}&page=${page}`;

    if (window.location.pathname.includes('search-product') || search !== "") {
        newUrl = `search-product?search=${encodeURIComponent(search)}&sort=${sort}&price=${price}&page=${page}`;
    }

    window.location.href = newUrl;
}
function changeSort(sortType) {
    const urlParams = new URLSearchParams(window.location.search);
    const cid = document.getElementById("currentCid")?.value || "0";
    const price = urlParams.get('price') || "all";
    const search = urlParams.get('search') || "";

    let newUrl = `catalog?cid=${cid}&sort=${sortType}&price=${price}&page=1`;

    if (search !== "") {
        newUrl = `search-product?search=${encodeURIComponent(search)}&sort=${sortType}&price=${price}&page=1`;
    }

    window.location.href = newUrl;
}

function changePrice(priceRange) {
    const urlParams = new URLSearchParams(window.location.search);
    const cid = document.getElementById("currentCid")?.value || "0";
    const sort = document.getElementById("currentSort")?.value || "default";
    const search = urlParams.get('search') || "";

    let newUrl = `catalog?cid=${cid}&sort=${sort}&price=${priceRange}&page=1`;

    if (search !== "") {
        newUrl = `search-product?search=${encodeURIComponent(search)}&sort=${sort}&price=${priceRange}&page=1`;
    }
    window.location.href = newUrl;
}