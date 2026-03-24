function togglePassword() {
    const input1 = document.getElementById('password');
    const input2 = document.getElementById('confirmpassword')
    const checkbox=document.getElementById('toggle-password')


    if (checkbox.checked) {
        input1.type = 'text';
        input2.type = 'text';
    } else {
        input1.type = 'password';
        input2.type = 'password';
    }
}