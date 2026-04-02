$(document).ready(function() {
    const host = "https://provinces.open-api.vn/api/v2/";

    function loadProvinces() {
        $.getJSON(host + "p/", function(data) {
            let options = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
            data.forEach(tinh => {
                options += `<option value="${tinh.code}" data-name="${tinh.name}">${tinh.name}</option>`;
            });
            $("#citySelect").html(options);
        }).fail(function() {
            alert("Lỗi tải dữ liệu Tỉnh/Thành từ hệ thống API.");
        });
    }

    function loadWards(provinceCode) {
        $.getJSON(host + "p/" + provinceCode + "?depth=2", function(data) {
            let options = '<option value="">-- Chọn Phường/Xã --</option>';
            if (data.wards) {
                data.wards.forEach(xa => {
                    options += `<option value="${xa.code}" data-name="${xa.name}">${xa.name}</option>`;
                });
            }
            $("#wardSelect").html(options);
        });
    }

    $("#citySelect").change(function () {
        let pCode = $(this).val();
        let pName = $(this).find("option:selected").data("name");

        if (pName) $("#hidden_city").val(pName);

        $("#wardSelect").html('<option value="">-- Đang tải... --</option>');
        $("#hidden_district").val("");

        if (pCode) {
            loadWards(pCode);
        } else {
            $("#wardSelect").html('<option value="">-- Chọn Phường/Xã --</option>');
        }
    });

    $("#wardSelect").change(function () {
        let wName = $(this).find("option:selected").data("name");

        if (wName) {
            $("#hidden_district").val(wName);
        } else {
            $("#hidden_district").val("");
        }
    });

    $('#btnToggle').on('click', function(e) {
        var btn = $(this);
        var editables = $('.info-form .editable');

        if (btn.attr('type') !== 'submit') {
            e.preventDefault();

            editables.filter('input').prop('readonly', false).removeClass('input-fixed');
            editables.filter('select').prop('disabled', false).removeClass('input-fixed');
            editables.first().focus();

            btn.text('Lưu thay đổi').addClass('btn-save-mode').attr('type', 'submit');

            if ($("#citySelect option").length <= 1) {
                $("#citySelect").html('<option value="">-- Đang tải dữ liệu... --</option>');
                loadProvinces();
                $("#wardSelect").html('<option value="">-- Chọn Phường/Xã --</option>');
            }
        }
    });

    $('#userForm').on('submit', function(e) {
        e.preventDefault();
        var form = $(this);

        if ($("#citySelect").val() === "" || $("#wardSelect").val() === "") {
            alert("Vui lòng chọn đầy đủ Tỉnh/Thành và Phường/Xã!");
            return;
        }

        $.ajax({
            type: "POST",
            url: form.attr('action'),
            data: form.serialize(),
            success: function(response) {
                $('#content-area').html(response);
            },
            error: function() {
                alert("Có lỗi xảy ra khi lưu thông tin. Vui lòng kiểm tra kết nối mạng.");
            }
        });
    });
});