const sidebarLinks = document.querySelectorAll('.sidebar ul li a');
const contents = document.querySelectorAll('.content');

for (let link of sidebarLinks) {
    link.addEventListener('click', function(e) {
        e.preventDefault()

        // Remove 'active' class from all links
        sidebarLinks.forEach(link => link.parentElement.classList.remove('active'));
        contents.forEach(content => content.style.display = 'none');

        // Add 'active' class to the clicked link's parent <li>
        this.parentElement.classList.add('active');
        if (this.parentElement.id === 'cars-btn') {
            document.getElementById('cars').style.display = 'block';
        }
        else if (this.parentElement.id === 'vans-btn') {
            document.getElementById('vans').style.display = 'block';
        }
        else if (this.parentElement.id === 'trucks-btn') {
            document.getElementById('trucks').style.display = 'block';
        }
        else if (this.parentElement.id === 'motorcycles-btn') {
            document.getElementById('motorcycles').style.display = 'block';
        }
        else if (this.parentElement.id === 'vin-btn') {
            document.getElementById('vin-category').style.display = 'block';
        }

        // Get the ID of the clicked list item (li)
        const clickedLiId = this.parentElement.id;
    });
}

// Select the search bar and list
let inputs = document.querySelectorAll('.search-bar');


for (let searchBar of inputs) {
    const listItems = searchBar.parentElement.querySelectorAll('ul li');
    // Add event listener for input changes
    searchBar.addEventListener('input', function() {
        const searchTerm = searchBar.value.toUpperCase(); // Get the input value and convert to lowercase

        // Loop through all list items
        listItems.forEach(item => {
            const text = item.textContent.toUpperCase(); // Get the text content of the <li>
            if (text.includes(searchTerm)) {
                item.style.display = ''; // Show the item if it matches
            } else {
                item.style.display = 'none'; // Hide the item if it doesn't match
            }
        });
    });
}





