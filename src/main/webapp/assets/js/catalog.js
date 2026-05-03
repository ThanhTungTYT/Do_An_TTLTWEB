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
    var keywordEl = document.getElementById("currentKeyword");
    var cidEl = document.getElementById("currentCid");
    var sortEl = document.getElementById("currentSort");

    var sort = (sortEl && sortEl.value) ? sortEl.value : "default";

    if (keywordEl) {
        var keyword = keywordEl.value;
        window.location.href = `search-product?search=${encodeURIComponent(keyword)}&sort=${sort}&page=${page}`;
    }
    else {
        var cid = (cidEl && cidEl.value) ? cidEl.value : "0";
        window.location.href = `catalog?cid=${cid}&sort=${sort}&page=${page}`;
    }
}

function changeSort(sortType) {
    var keywordEl = document.getElementById("currentKeyword");
    var cidEl = document.getElementById("currentCid");

    if (keywordEl) {
        var keyword = keywordEl.value;
        window.location.href = `search-product?search=${encodeURIComponent(keyword)}&sort=${sortType}&page=1`;
    } else {
        var cid = (cidEl && cidEl.value) ? cidEl.value : "0";
        window.location.href = `catalog?cid=${cid}&sort=${sortType}&page=1`;
    }
}