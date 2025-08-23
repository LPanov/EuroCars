//Show/hide the menu
document.addEventListener('DOMContentLoaded', function() {
    const catalogueButton = document.getElementById('catalogueButton'); // Target the <a> tag by its ID
    const catalogueMenu = document.getElementById('catalogueMenu');

    // Add functionality to the CATALOGUE button
    catalogueButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent the link from navigating
        catalogueMenu.classList.toggle('show');
        catalogueButton.style.color ='rgba(92,140,170, 255)';
        catalogueButton.style.backgroundColor = 'white';
        catalogueButton.style.padding = '5px';
        catalogueButton.style.borderRadius = '5px';
    });

    // Close the menu if the user clicks outside of it
    document.addEventListener('click', function(event) {
        if (!catalogueButton.contains(event.target) && !catalogueMenu.contains(event.target)) {
            catalogueMenu.classList.remove('show');
            catalogueButton.style.color = 'white';
            catalogueButton.style.backgroundColor = 'rgba(92,140,170, 255)';
            catalogueButton.style.padding = '5px';
            catalogueButton.style.borderRadius = '5px';
        }
    });
});

//Show the notifications box
document.addEventListener('DOMContentLoaded', function() {
    const bellIcon = document.getElementById('bell');
    const notificationBox = document.getElementById('notificationBox');
    
    //Add event listener to the bell icon
    bellIcon.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent the default link behavior if needed
        notificationBox.classList.toggle('show');
        bellIcon.style.backgroundColor = 'white';
        bellIcon.style.padding = '5px';
        bellIcon.style.color = 'rgba(92,140,170, 255)';
        bellIcon.style.borderRadius = '5px';
        
    });

    // Close the notification box if the user clicks outside of it
    document.addEventListener('click', function(event) {
        if (!bellIcon.contains(event.target) && !notificationBox.contains(event.target)) {
        notificationBox.classList.remove('show');
        bellIcon.style.backgroundColor = 'rgba(92,140,170, 255)';
        bellIcon.style.padding = '5px';
        bellIcon.style.color = 'white';
        bellIcon.style.borderRadius = '5px';
        }
    });
});

// Show/hide the button based on scroll position
window.addEventListener('scroll', function() {
    const button = document.querySelector('.scroll-to-top');
    if (window.scrollY > 10) { // Show button after scrolling 200px
        button.classList.add('visible');
    } else {
        button.classList.remove('visible');
    }
});

function openModalFilter() {
    document.getElementById('searchModalFilter').style.display = 'flex';
  }

  function closeModalFilter() {
    document.getElementById('searchModalFilter').style.display = 'none';
  }

  function clearForm() {
    document.getElementById('productIndex').value = '';
    document.getElementById('techDescription').value = '';
    document.getElementById('category').value = '';
    document.getElementById('manufacturer').value = 'any';
    document.getElementById('logisticPath').checked = false;
  }

  function submitForm() {
    // Add your search logic here
    alert('Search submitted!');
    closeModal();
  }

  // Close modal when clicking outside of it
  window.onclick = function(event) {
    const modal = document.getElementById('searchModalFilter');
    if (event.target === modal) {
      closeModalFilter();
    }
  }

// Smooth scroll to top
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

