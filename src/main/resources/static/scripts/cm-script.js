const sidebarLinks = document.querySelectorAll('.sidebar ul li a');
const contents = document.querySelectorAll('.content');

for (let link of sidebarLinks) {
    link.addEventListener('click', function(e) {
        e.preventDefault()

        // Remove 'active' class from all links
        sidebarLinks.forEach(link => link.parentElement.classList.remove('active'));
        models.forEach(model => {
            let modelBox = model.parentElement.parentElement;
            modelBox.style.display = 'none';
        });
        engines.forEach(engine => {
            let engineBox = engine.parentElement.parentElement;
            engineBox.style.display = 'none';
        });
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

        sendCategoryData(clickedLiId)
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

let brands = document.querySelectorAll('.content ul li');
let models = document.querySelectorAll('.models ul li');

//Loop through all brands and add event listener
brands.forEach(brand => brand.addEventListener('click', function(e) {

    // Loop through all model and hide the shown one
    models.forEach(model => {
        let modelBox = model.parentElement.parentElement;
        modelBox.style.display = 'none';
    });   

    //Loop through all models
    models.forEach(model => {
        let modelBox = model.parentElement.parentElement; //get the .model container
        let brandBox = brand.parentElement.parentElement; //get the .content container

        //Checks if the models are corresponding to the choosen brand
        if (modelBox.classList.contains(brand.classList[0])) {
            brandBox.style.display = 'none'; //Hide the brands
            modelBox.style.display = 'block'; //Show the models
        }
    });
}));

let previousBtns = document.querySelectorAll('.previous');

previousBtns.forEach(previousBtn => previousBtn.addEventListener('click', function(e) {
    e.preventDefault();

    let modelBox = previousBtn.parentElement; //get the .model container

    brands.forEach(brand => {
        let brandBox = brand.parentElement.parentElement; //get the brand container

        if (modelBox.classList.contains(brand.classList[0])) {
            brandBox.style.display = 'block'; //Hide the brands
            modelBox.style.display = 'none'; //Show the models
        }
    })
    
}));

let engines = document.querySelectorAll('.engines ul li');

models.forEach(model => model.addEventListener('click', function(e) {
    e.preventDefault();

    // Loop through all model and hide the shown one
    engines.forEach(engine => {
        let engineBox = engine.parentElement.parentElement;
        engineBox.style.display = 'none';
    });  
    
    //Loop through all models
    engines.forEach(engine => {
        let modelBox = model.parentElement.parentElement; //get the .models container
        let engineBox = engine.parentElement.parentElement; //get the .engines container
        
        //Checks if the engines are corresponding to the choosen model
        if (engineBox.classList.contains(model.classList[0])) {
            modelBox.style.display = 'none'; //Hide the brands
            engineBox.style.display = 'block'; //Show the models
        }
    });
}));

let backBtns = document.querySelectorAll('.previous-en');

backBtns.forEach(back => back.addEventListener('click', function(e) {
    e.preventDefault();

    let engineBox = back.parentElement; //get the .engine container

    models.forEach(model => {
        let modelBox = model.parentElement.parentElement; //get the model container

        if (engineBox.classList.contains(model.classList[0])) {
            modelBox.style.display = 'block'; //Hide models
            engineBox.style.display = 'none'; //Show engines
        }
    })
    
}));

function sendCategoryData(clickedLiId) {
    fetch(`/catalogue`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ category: clickedLiId })
    })
        .then(response => {
            if (!response.ok) {
                console.error('Failed to log click on the server.');
            } else {
                console.log('Category click logged successfully!');
            }
        })
        .catch(error => console.error('Error sending click data:', error));
}

document.addEventListener('DOMContentLoaded', (event) => {
    // Find the default link and "click" it
    const defaultLink = document.getElementById('cars-btn');
    if (defaultLink) {
        defaultLink.classList.add('active'); // Add the active class
        document.getElementById('cars').style.display = 'block'; // Show the default content
    }

    // Directly call the function to send the data for the default category
    sendCategoryData('cars-btn');
});




