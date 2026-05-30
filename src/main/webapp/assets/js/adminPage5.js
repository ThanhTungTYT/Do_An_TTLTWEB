
'use strict';
let currentContactId = null;

document.addEventListener('DOMContentLoaded', function () {

    document.addEventListener('click', function (e) {
        const btn = e.target.closest('.detail');
        if (!btn) return;
        currentContactId = btn.dataset.id;
        document.getElementById('d-name').innerText  = btn.dataset.name;
        document.getElementById('d-email').innerText = btn.dataset.email;
        document.getElementById('d-msg').innerText   = btn.dataset.message;
    }, true);


    const closeReplyBtn = document.getElementById('close-reply');
    if (closeReplyBtn) {
        closeReplyBtn.addEventListener('click', function () {
            closeFormReply();
        });
    }


    document.body.addEventListener('click', function (e) {
        const formReply = document.getElementById('form-reply');
        if (!formReply || formReply.style.display !== 'block') return;
        if (e.target.closest('#form-reply') || e.target.closest('.btn-reply')) return;
        closeFormReply();
    });


    const replyForm = document.getElementById('reply-form');
    if (replyForm) {
        replyForm.addEventListener('submit', function () {
            document.getElementById('reply-sending').style.display = 'block';
            document.getElementById('btn-send-reply').disabled    = true;
            document.getElementById('btn-send-reply').innerText   = 'Đang gửi...';
        });
    }

});



function closeFormReply() {
    const formReply = document.getElementById('form-reply');
    const content   = document.getElementById('right-content');
    if (formReply) formReply.style.display = 'none';
    if (content)   content.style.filter    = 'blur(0)';
}

function openReply(id, name, email) {
    currentContactId = id;
    document.getElementById('r-contact-id').value = id;
    document.getElementById('r-name').value        = name;
    document.getElementById('r-email').value       = email;
    document.getElementById('form-reply').style.display = 'block';
    document.getElementById('detail-p').style.display   = 'none';
    const content = document.getElementById('right-content');
    if (content) content.style.filter = 'blur(5px)';
}

function openReplyFromDetail() {
    const name  = document.getElementById('d-name').innerText;
    const email = document.getElementById('d-email').innerText;
    openReply(currentContactId, name, email);
}

function deleteSingle(id) {
    if (!confirm('Bạn có chắc muốn xóa liên hệ #' + id + ' không?')) return;
    document.getElementById('delete-single-id').value = id;
    document.getElementById('delete-single-form').submit();
}

function toggleAll(source) {
    document.querySelectorAll('.row-check').forEach(cb => cb.checked = source.checked);
    updateDeleteBtn();
}

function updateDeleteBtn() {
    const checked  = document.querySelectorAll('.row-check:checked');
    const all      = document.querySelectorAll('.row-check');
    const btn      = document.getElementById('btn-delete-selected');
    const checkAll = document.getElementById('check-all');
    document.getElementById('selected-count').innerText = checked.length;
    btn.style.display      = checked.length > 0 ? 'inline-flex' : 'none';
    checkAll.indeterminate = checked.length > 0 && checked.length < all.length;
    checkAll.checked       = all.length > 0 && checked.length === all.length;
}

function deleteSelected() {
    const checked = document.querySelectorAll('.row-check:checked');
    if (checked.length === 0) return;
    if (!confirm('Bạn có chắc muốn xóa ' + checked.length + ' liên hệ đã chọn không?')) return;
    document.getElementById('bulk-delete-form').submit();
}