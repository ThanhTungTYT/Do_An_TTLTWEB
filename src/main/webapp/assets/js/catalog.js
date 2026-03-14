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