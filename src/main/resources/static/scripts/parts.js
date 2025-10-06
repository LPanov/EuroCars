document.addEventListener('DOMContentLoaded', () => {
  const showMoreButton = document.getElementById('show-more-button');
  if (!showMoreButton) return;

  // Capture elements that are hidden at load time
  const initiallyHidden = Array.from(document.querySelectorAll('.hidden-manufacturer'));
  let expanded = false;

  const showAll = () => {
    initiallyHidden.forEach(el => el.classList.remove('hidden-manufacturer'));
    showMoreButton.textContent = 'Show less';
    expanded = true;
  };

  const hideAgain = () => {
    initiallyHidden.forEach(el => el.classList.add('hidden-manufacturer'));
    showMoreButton.textContent = 'Show more';
    expanded = false;
  };

  showMoreButton.addEventListener('click', () => {
    expanded ? hideAgain() : showAll();
  });
});