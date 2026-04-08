$(document).ready(function() {
    const host = "https://provinces.open-api.vn/api/v2/";

    function loadProvinces() {
        let savedCity = $("#hidden_city").val().trim();

        $.getJSON(host + "p/", function(data) {
            let options = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
            let matchedCode = "";

            data.forEach(tinh => {
                options += `<option value="${tinh.code}" data-name="${tinh.name}">${tinh.name}</option>`;
                if (tinh.name === savedCity) {
                    matchedCode = tinh.code;
                }
            });
            $("#citySelect").html(options);

            if (matchedCode !== "") {
                $("#citySelect").val(matchedCode);
                loadWards(matchedCode);
            }
        }).fail(function() {
            alert("Lỗi tải dữ liệu Tỉnh/Thành từ hệ thống API.");
        });
    }

    function loadWards(provinceCode) {
        let savedWard = $("#hidden_district").val().trim();

        $.getJSON(host + "p/" + provinceCode + "?depth=2", function(data) {
            let options = '<option value="">-- Chọn Phường/Xã --</option>';
            let matchedCode = "";

            if (data.wards) {
                data.wards.forEach(xa => {
                    options += `<option value="${xa.code}" data-name="${xa.name}">${xa.name}</option>`;
                    if (xa.name === savedWard) {
                        matchedCode = xa.code;
                    }
                });
            }
            $("#wardSelect").html(options);

            if (matchedCode !== "") {
                $("#wardSelect").val(matchedCode);
            }
        });
    }

    function validateLocation() {
        let isValid = true;

        if ($("#citySelect").val() === "") {
            showError("#citySelect", "#err-city", "Vui lòng chọn Tỉnh/Thành phố!");
            isValid = false;
        } else {
            clearError("#citySelect", "#err-city");
        }

        if ($("#wardSelect").val() === "") {
            showError("#wardSelect", "#err-ward", "Vui lòng chọn Phường/Xã!");
            isValid = false;
        } else {
            clearError("#wardSelect", "#err-ward");
        }

        return isValid;
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

        validateLocation();
    });

    $("#wardSelect").change(function () {
        let wName = $(this).find("option:selected").data("name");
        if (wName) { $("#hidden_district").val(wName); }
        else { $("#hidden_district").val(""); }

        validateLocation();
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
                $("#wardSelect").html('<option value="">-- Đang tải dữ liệu... --</option>');
                loadProvinces();
            }
        }
    });

    $('#userForm').on('submit', function(e) {
        e.preventDefault();

        let isNameValid = validateFullName();
        let isPhoneValid = validatePhone();
        let isAddrValid = validateAddress();

        if ($("#citySelect").val() === "" || $("#wardSelect").val() === "") {
            alert("Vui lòng chọn đầy đủ Tỉnh/Thành và Phường/Xã!");
            return;
        }

        if (!isNameValid || !isPhoneValid || !isAddrValid) {
            return;
        }

        var form = $(this);
        $.ajax({
            type: "POST",
            url: form.attr('action'),
            data: form.serialize(),
            success: function (response) {
                $('#content-area').html(response);
            },
            error: function () {
                alert("Có lỗi xảy ra khi lưu thông tin. Vui lòng kiểm tra kết nối mạng.");
            }
        })
    });

    function showError(inputId, errId, message) {
        $(inputId).addClass('input-error');
        $(errId).text(message).show();
    }

    function clearError(inputId, errId) {
        $(inputId).removeClass('input-error');
        $(errId).text("").hide();
    }

    function validateFullName() {
        let val = $("#fullname").val().trim();
        const regexName = /^[\p{L}\s]+$/u;

        if (val === "") {
            showError("#fullname", "#err-fullname", "Họ và tên không được để trống!");
            return false;
        } else if (!regexName.test(val)) {
            showError("#fullname", "#err-fullname", "Họ và tên không được chứa số hay kí tự đặc biệt!");
            return false;
        }
        clearError("#fullname", "#err-fullname");
        return true;
    }

    function validatePhone() {
        let val = $("#phone").val().trim();
        const regexPhone = /^(03|05|07|08|09)\d{8}$/;

        if (val === "") {
            showError("#phone", "#err-phone", "Số điện thoại không được để trống!");
            return false;
        } else if (!regexPhone.test(val)) {
            showError("#phone", "#err-phone", "Số điện thoại không hợp lệ (Phải gồm 10 số, bắt đầu bằng 03, 05, 07, 08 hoặc 09)!");
            return false;
        }
        clearError("#phone", "#err-phone");
        return true;
    }

    function validateAddress() {
        let val = $("#address").val().trim();
        const regexAddress = /^[\p{L}0-9\s,.\-\/]+$/u;

        if (val === "") {
            showError("#address", "#err-address", "Địa chỉ không được để trống!");
            return false;
        } else if (!regexAddress.test(val)) {
            showError("#address", "#err-address", "Địa chỉ chứa kí tự đặc biệt không hợp lệ!");
            return false;
        }
        clearError("#address", "#err-address");
        return true;
    }

    $("#fullname").on("input", validateFullName);
    $("#phone").on("input", validatePhone);
    $("#address").on("input", validateAddress);
});