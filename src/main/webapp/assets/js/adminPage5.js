
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


    document.getElementById('close-reply').addEventListener('click', closeFormReply);


    document.body.addEventListener('click', function (e) {
        const formReply = document.getElementById('form-reply');
        if (!formReply || formReply.style.display !== 'block') return;
        if (e.target.closest('#form-reply')
            || e.target.closest('.btn-reply')
            || e.target.closest('.btn-reply-from-detail')) return;
        closeFormReply();
    });


    const replyForm = document.getElementById('reply-form');
    if (replyForm) {
        replyForm.addEventListener('submit', function () {
            document.getElementById('reply-sending').style.display = 'block';
            const btn = document.getElementById('btn-send-reply');
            btn.disabled   = true;
            btn.innerHTML  = '<i class="fa-solid fa-spinner fa-spin"></i> Đang gửi...';
        });
    }

});



function autoResize(el) {
    el.style.height = 'auto';
    el.style.height = el.scrollHeight + 'px';
}

function closeFormReply() {
    document.getElementById('form-reply').style.display = 'none';
    const content = document.getElementById('right-content');
    if (content) content.style.filter = 'blur(0)';
}

function openReply(id, name, email) {
    currentContactId = id;

    document.getElementById('r-contact-id').value = id;
    document.getElementById('r-name').value        = name;
    document.getElementById('r-email').value       = email;

    document.getElementById('r-name-display').innerText  = name;
    document.getElementById('r-email-display').innerText = email;

    const ta = document.getElementById('reply-content');
    ta.value       = '';
    ta.style.height = 'auto';

    document.getElementById('detail-p').style.display = 'none';
    document.getElementById('form-reply').style.display = 'block';
    const content = document.getElementById('right-content');
    if (content) content.style.filter = 'blur(5px)';
}

// Mở phản hồi ngay từ trong popup chi tiết
function openReplyFromDetail() {
    const name  = document.getElementById('d-name').innerText;
    const email = document.getElementById('d-email').innerText;
    openReply(currentContactId, name, email);
}