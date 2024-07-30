import {fetchWithAuth} from './refresh_token.js';

const usernameForm = document.getElementById('username-form');
const passwordForm = document.getElementById('password-form');
const deleteAccountBtn = document.getElementById('delete-account');
const popup = document.getElementById('popup');
const popupMessage = document.getElementById('popup-message');
const popupClose = document.getElementById('popup-close');
const popupConfirm = document.getElementById('popup-confirm');

usernameForm.addEventListener('submit', handleUsernameChange);
passwordForm.addEventListener('submit', handlePasswordChange);
deleteAccountBtn.addEventListener('click', showDeleteConfirmation);
popupClose.addEventListener('click', closePopup);
popupConfirm.addEventListener('click', handleDeleteAccount);

function handleUsernameChange(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    sendRequest('/api/v1/users/username', {username});
}

function handlePasswordChange(event) {
    event.preventDefault();
    const password = document.getElementById('password').value;
    sendRequest('/api/v1/users/password', {password});
}

function sendRequest(url, data) {
    fetchWithAuth(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw err;
                });
            } else {
                showPopup('Changes saved successfully');
            }
        })
        .catch(handleError);
}

function handleError(error) {
    if (error.httpStatus === 'BAD_REQUEST' && error.message === 'Validation error') {
        error.errors.forEach(err => {
            const [field, message] = err.split(':');
            const errorElement = document.getElementById(`${field}-error`);
            if (errorElement) {
                errorElement.textContent = message;
            }
        });
    } else {
        showPopup('Server error. Please try again later.');
    }
}

function showDeleteConfirmation() {
    popupMessage.textContent = 'Are you sure you want to delete your account? This action cannot be undone.';
    popupConfirm.style.display = 'inline-block';
    popup.style.display = 'block';
}

function handleDeleteAccount() {
    fetchWithAuth('/api/v1/users/current', {method: 'DELETE'})
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete account');
            }
            window.location.href = '/sign-in';
        })
        .catch(() => {
            showPopup('Failed to delete account. Please try again later.');
        });
}

function showPopup(message) {
    popupMessage.textContent = message;
    popupConfirm.style.display = 'none';
    popup.style.display = 'block';
}

function closePopup() {
    popup.style.display = 'none';
}