$(document).ready(function () {
    const contentArea = $('#content-area');

    function loadContent(url) {
        contentArea.html('<div style="text-align:center; padding:50px; color:#666;">Đang tải dữ liệu... <i class="fas fa-spinner fa-spin"></i></div>');
        contentArea.load(url, function (response, status, xhr) {
            if (status == "error") {
                contentArea.html(`<div style="color:red; padding:20px; text-align:center;">Lỗi kết nối: ${xhr.status} ${xhr.statusText}</div>`);
            }
        });
    }

    $('.sidebar-link').on('click', function (e) {
        e.preventDefault();
        $('.sidebar-link').removeClass('active');
        $(this).addClass('active');
        var urlToLoad = $(this).attr('href');
        loadContent(urlToLoad);
    });

    $(document).on('submit', '#change-pass-form', function(e) {
        e.preventDefault();

        var form = $(this);
        var url = form.attr('action');
        var method = form.attr('method');

        var btn = form.find('button[type="submit"]');
        var originalBtnText = btn.text();
        btn.text('Đang xử lý...').prop('disabled', true);

        $.ajax({
            type: method,
            url: url,
            data: form.serialize(),
            success: function(response) {
                contentArea.html(response);
            },
            error: function(xhr) {
                alert('Có lỗi xảy ra: ' + xhr.statusText);
                btn.text(originalBtnText).prop('disabled', false);
            }
        });
    });

    $('.sidebar-link.active').trigger('click');
});