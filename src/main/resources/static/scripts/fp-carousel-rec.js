const slidePartsWrapper = document.querySelector('.parts-carousel-wrapper');
const carouselItems = document.querySelectorAll('.parts-carousel-item');
const butttonWrapper = document.querySelector('.carousel-nav-part');
const navButtons = document.querySelectorAll('.carousel-nav-part button');
const itemWidth = carouselItems[0].offsetWidth;

let current = 0;
let interval;
let isDragging = false;
let startX = 0;
let scrollLeft = 0;

function updateNavButtons(index) {
    navButtons.forEach((button, i) => {
        button.classList.toggle('active', i === index);
    });
}

function moveToSlide(index, smooth = true) {
    if (index < 0) {
        index = 0;
    } else if (index >= carouselItems.length) {
        index = 0;
    }

    current = index;
    const scrollLeft = carouselItems[index].offsetLeft;

    slidePartsWrapper.scrollTo({
        left: scrollLeft,
        behavior: smooth ? 'smooth' : 'auto'
    });
    updateNavButtons(index);
}

function nextSlide() {
    moveToSlide(current + 1);
}


function startCarousel() {
    interval = setInterval(nextSlide, 10000);
}

function stopCarousel() {
    clearInterval(interval);
}

slidePartsWrapper.addEventListener('mouseenter', stopCarousel);
slidePartsWrapper.addEventListener('mouseleave', startCarousel);

navButtons.forEach((button, index) => {
    button.addEventListener('click', () => {
        moveToSlide(index);
        stopCarousel();
        startCarousel();
    });
});



slidePartsWrapper.addEventListener('scroll', () => {
    if (!isDragging) {
        let closestIndex = 0;
        let minDistance = Infinity;
        carouselItems.forEach((item, index) => {
            const distance = Math.abs(item.offsetLeft - slidePartsWrapper.scrollLeft);
            if (distance < minDistance) {
                minDistance = distance;
                closestIndex = index;
            }
        });
        current = closestIndex;
        updateNavButtons(current);
    }
});

moveToSlide(current, false);
startCarousel();



