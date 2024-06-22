import {fetchWithAuth} from './refresh_token.js';

fetchWithAuth("/api/v1/users/current")
    .then(response => makeUserMenu(response));

fetch('/api/v1/quizzes')
    .then(response => response.json())
    .then(json => {
        makeMenuOptions(json, 'quizzes', '/quiz-');
        makeMenuOptions(json, 'ranking', '/ranking?quiz=');
    });

function makeMenuOptions(json, menuOption, href) {
    let quizzesMenu = document.getElementById(menuOption)
    let submenu = document.createElement('ul');
    quizzesMenu.appendChild(submenu);
    for (let i = 0; i < json.length; i++) {
        let link = href + json[i].id;
        makeLink(submenu, json[i].name, link, link.substring(1, link.length));
    }
}

function makeUserMenu(response) {
    let userMenu = document.getElementById('sign-in');
    if (!response.ok) {
        let a = document.createElement('a');
        a.textContent = 'Sign in';
        a.setAttribute('href', '/sign-in');
        userMenu.appendChild(a);
    } else {
        let submenu = document.createElement('ul');
        userMenu.appendChild(submenu);
        let items = new Map();
        items.set('My profile', '/my-profile');
        items.set('Settings', '/settings');
        items.set('Log out', '/logout');
        for (let [key, value] of items) {
            makeLink(submenu, key, value, value.substring(1, value.length));
        }
        document.getElementById('logout').onclick = function () {
            fetchWithAuth("/api/v1/auth/logout")
                .then(response => {
                    if (response.ok) {
                        window.location.href = "/";
                    }
                });
        }
    }
}

function makeLink(submenu, textContent, link, id) {
    let menuItem = document.createElement('li');
    submenu.appendChild(menuItem);
    let a = document.createElement('a');
    a.textContent = textContent;
    a.setAttribute('href', link);
    a.setAttribute('id', id);
    menuItem.appendChild(a);
}