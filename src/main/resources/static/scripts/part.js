function showTab(tabId) {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => tab.style.display = 'none');
    document.getElementById(tabId).style.display = 'block';

    const buttons = document.querySelectorAll('.tab-buttons button');
    buttons.forEach(button => button.classList.remove('active'));
    document.querySelector(`.tab-buttons button[onclick="showTab('${tabId}')"]`).classList.add('active');
}

// Show the first tab by default
showTab('parameters');

let plus = document.getElementById("+");
let minus = document.getElementById('-');
let qty = document.getElementById('qty');

plus.addEventListener('click', () => {
    let currentQty = Number(qty.value); 
    currentQty++; 
    qty.value = String(currentQty); 
});

minus.addEventListener('click', () => {
    let currentQty = Number(qty.value); 
    if (currentQty > 0) {
        currentQty--; 
        qty.value = String(currentQty); 
    }
    
});