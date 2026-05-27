document.addEventListener('DOMContentLoaded', function () {
    const toast = document.getElementById('toast-notification');
    if (toast) {
        setTimeout(function () {
            toast.remove();
        }, 3000);
    }
});
