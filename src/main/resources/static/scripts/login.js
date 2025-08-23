document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission for demo

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (username && password) {
        window.location.href = "front-page.html";
    } else {
        alert('Please fill in both fields.');
    }
});
