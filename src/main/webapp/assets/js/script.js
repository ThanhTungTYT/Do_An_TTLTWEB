const num_cart = document.querySelector('#num-cart-label');
const btn = document.getElementById("slide-top");

if (num_cart) {
    if (parseInt(num_cart.textContent) > 0) {
        num_cart.style.display = 'block';
    } else {
        num_cart.style.display = 'none';
    }
}

if (btn) {
    window.addEventListener("scroll", () => {
        if (window.scrollY > 20) {
            btn.style.display = "block";
        } else {
            btn.style.display = "none";
        }
    });

    btn.addEventListener("click", () => {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    });
}
