$(document).ready(function() {
    // Context path để fetch endpoint GHN nội bộ (info.jsp set window.appCtx)
    const ctx = window.appCtx || '';

    function loadProvinces() {
        const savedProvince = ($("#hidden_province").val() || "").trim();

        $.getJSON(ctx + '/api/ghn/provinces', function(data) {
            let options = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
            let matchedId = "";

            data.forEach(p => {
                options += `<option value="${p.ProvinceID}" data-name="${p.ProvinceName}">${p.ProvinceName}</option>`;
                if (p.ProvinceName === savedProvince) matchedId = p.ProvinceID;
            });
            $("#citySelect").html(options);

            if (matchedId !== "") {
                $("#citySelect").val(matchedId);
                loadDistricts(matchedId);
            }
        }).fail(function() {
            alert("Lỗi tải danh sách Tỉnh/Thành.");
        });
    }

    function loadDistricts(provinceId) {
        const savedDistrict = ($("#hidden_district").val() || "").trim();

        $.getJSON(ctx + '/api/ghn/districts?province_id=' + provinceId, function(data) {
            let options = '<option value="">-- Chọn Quận/Huyện --</option>';
            let matchedId = "";

            data.forEach(d => {
                options += `<option value="${d.DistrictID}" data-name="${d.DistrictName}">${d.DistrictName}</option>`;
                if (d.DistrictName === savedDistrict) matchedId = d.DistrictID;
            });
            $("#districtSelect").html(options);

            if (matchedId !== "") {
                $("#districtSelect").val(matchedId);
                loadWards(matchedId);
            } else {
                $("#wardSelect").html('<option value="">-- Chọn Phường/Xã --</option>');
            }
        }).fail(function() {
            alert("Lỗi tải danh sách Quận/Huyện.");
        });
    }

    function loadWards(districtId) {
        const savedWard = ($("#hidden_ward").val() || "").trim();

        $.getJSON(ctx + '/api/ghn/wards?district_id=' + districtId, function(data) {
            let options = '<option value="">-- Chọn Phường/Xã --</option>';
            let matchedCode = "";

            data.forEach(w => {
                options += `<option value="${w.WardCode}" data-name="${w.WardName}">${w.WardName}</option>`;
                if (w.WardName === savedWard) matchedCode = w.WardCode;
            });
            $("#wardSelect").html(options);

            if (matchedCode !== "") $("#wardSelect").val(matchedCode);
        }).fail(function() {
            alert("Lỗi tải danh sách Phường/Xã.");
        });
    }

    function validateLocation() {
        let isValid = true;

        if ($("#citySelect").val() === "") {
            showError("#citySelect", "#err-city", "Vui lòng chọn Tỉnh/Thành phố!");
            isValid = false;
        } else clearError("#citySelect", "#err-city");

        if ($("#districtSelect").val() === "") {
            showError("#districtSelect", "#err-district", "Vui lòng chọn Quận/Huyện!");
            isValid = false;
        } else clearError("#districtSelect", "#err-district");

        if ($("#wardSelect").val() === "") {
            showError("#wardSelect", "#err-ward", "Vui lòng chọn Phường/Xã!");
            isValid = false;
        } else clearError("#wardSelect", "#err-ward");

        return isValid;
    }

    $("#citySelect").change(function () {
        const id = $(this).val();
        const name = $(this).find("option:selected").data("name") || "";
        $("#hidden_province").val(name);
        // Reset cấp dưới
        $("#hidden_district").val("");
        $("#hidden_ward").val("");
        $("#districtSelect").html('<option value="">-- Đang tải... --</option>');
        $("#wardSelect").html('<option value="">-- Chọn Phường/Xã --</option>');

        if (id) loadDistricts(id);
        else $("#districtSelect").html('<option value="">-- Chọn Quận/Huyện --</option>');

        validateLocation();
    });

    $("#districtSelect").change(function () {
        const id = $(this).val();
        const name = $(this).find("option:selected").data("name") || "";
        $("#hidden_district").val(name);
        $("#hidden_ward").val("");
        $("#wardSelect").html('<option value="">-- Đang tải... --</option>');

        if (id) loadWards(id);
        else $("#wardSelect").html('<option value="">-- Chọn Phường/Xã --</option>');

        validateLocation();
    });

    $("#wardSelect").change(function () {
        const name = $(this).find("option:selected").data("name") || "";
        $("#hidden_ward").val(name);
        validateLocation();
    });

    // Lưu snapshot giá trị ban đầu để có thể restore khi Hủy
    let originalValues = {};

    function snapshotForm() {
        originalValues = {
            fullname:        $("#fullname").val(),
            phone:           $("#phone").val(),
            address:         $("#address").val(),
            hiddenProvince:  $("#hidden_province").val(),
            hiddenDistrict:  $("#hidden_district").val(),
            hiddenWard:      $("#hidden_ward").val(),
            citySelected:    $("#citySelect").val(),
            districtSelected:$("#districtSelect").val(),
            wardSelected:    $("#wardSelect").val()
        };
    }

    function restoreForm() {
        $("#fullname").val(originalValues.fullname);
        $("#phone").val(originalValues.phone);
        $("#address").val(originalValues.address);
        $("#hidden_province").val(originalValues.hiddenProvince);
        $("#hidden_district").val(originalValues.hiddenDistrict);
        $("#hidden_ward").val(originalValues.hiddenWard);
        if (originalValues.citySelected     !== undefined) $("#citySelect").val(originalValues.citySelected);
        if (originalValues.districtSelected !== undefined) $("#districtSelect").val(originalValues.districtSelected);
        if (originalValues.wardSelected     !== undefined) $("#wardSelect").val(originalValues.wardSelected);
        $(".info-form .editable").removeClass("input-error");
        $(".error-msg").text("").hide();
    }

    function exitEditMode() {
        $('.info-form .editable').filter('input').prop('readonly', true).addClass('input-fixed');
        $('.info-form .editable').filter('select').prop('disabled', true).addClass('input-fixed');
        $('#btnToggle').text('Cập nhật thông tin').removeClass('btn-save-mode').attr('type', 'button');
        $('#btnCancel').hide();
    }

    $('#btnToggle').on('click', function(e) {
        var btn = $(this);
        var editables = $('.info-form .editable');

        if (btn.attr('type') !== 'submit') {
            e.preventDefault();

            snapshotForm();

            editables.filter('input').prop('readonly', false).removeClass('input-fixed');
            editables.filter('select').prop('disabled', false).removeClass('input-fixed');
            editables.first().focus();

            btn.text('Lưu thay đổi').addClass('btn-save-mode').attr('type', 'submit');
            $('#btnCancel').show();

            if ($("#citySelect option").length <= 1) {
                $("#citySelect").html('<option value="">-- Đang tải dữ liệu... --</option>');
                $("#districtSelect").html('<option value="">-- Đang tải dữ liệu... --</option>');
                $("#wardSelect").html('<option value="">-- Đang tải dữ liệu... --</option>');
                loadProvinces();
            }
        }
    });

    $('#btnCancel').on('click', function() {
        restoreForm();
        exitEditMode();
    });

    $('#userForm').on('submit', function(e) {
        e.preventDefault();

        let isNameValid = validateFullName();
        let isPhoneValid = validatePhone();
        let isAddrValid = validateAddress();

        if ($("#citySelect").val() === "" || $("#districtSelect").val() === "" || $("#wardSelect").val() === "") {
            alert("Vui lòng chọn đầy đủ Tỉnh/Thành, Quận/Huyện và Phường/Xã!");
            return;
        }

        if (!isNameValid || !isPhoneValid || !isAddrValid) return;

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
        });
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
