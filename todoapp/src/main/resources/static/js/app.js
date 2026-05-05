// app.js — minimal client-side enhancement

document.addEventListener('DOMContentLoaded', () => {
    // Auto-focus the title input so the user can type immediately
    const titleInput = document.querySelector('input[name="title"]');
    if (titleInput) titleInput.focus();
});
