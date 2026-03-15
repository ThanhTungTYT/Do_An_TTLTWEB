function showDetail(orderId) {
    document.querySelectorAll('.order-detail')
        .forEach(p => p.style.display = 'none');

    document.getElementById('detail-' + orderId).style.display = 'block';

    document.getElementById('right-content').style.filter = 'blur(5px)';
    document.getElementById('right-content').style.pointerEvents = 'none';
}

function closeDetail() {
    document.querySelectorAll('.order-detail')
        .forEach(p => p.style.display = 'none');

    document.getElementById('right-content').style.filter = 'none';
    document.getElementById('right-content').style.pointerEvents = 'auto';
}
