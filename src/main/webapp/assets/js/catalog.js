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
    var cid = document.getElementById("currentCid").value;
    var sort = document.getElementById("currentSort").value;
    if (!sort) sort = "default";
    if (!cid) cid = "0";
    window.location.href = `catalog?cid=${cid}&sort=${sort}&page=${page}`;
}

function changeSort(sortType) {
    var cid = document.getElementById("currentCid").value;
    if (!cid) cid = "0";
    window.location.href = `catalog?cid=${cid}&sort=${sortType}&page=1`;
}