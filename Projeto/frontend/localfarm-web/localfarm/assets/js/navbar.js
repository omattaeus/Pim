// document.addEventListener("DOMContentLoaded", function () {
//     fetch("./header/navbar.html")
//         .then(response => response.text())
//         .then(data => {
//             document.getElementById("navbar").innerHTML = data;
//             document.querySelector('.px-6.flex.cursor-pointer').onclick = showLogoutPopup;
//         })
//         .catch(error => console.error("Erro ao carregar o navbar:", error));
// });

// function showLogoutPopup() {
//     document.getElementById("logoutPopup").classList.remove("hidden");
// }

// function closeLogoutPopup() {
//     document.getElementById("logoutPopup").classList.add("hidden");
// }

// function confirmLogout() {
//     localStorage.removeItem("jwtToken");
//     sessionStorage.removeItem("jwtToken");

//     window.location.href = "/index.html";
// }