fetch('/api/v1/quizzes')
    .then(response => response.json())
    .then(json => {
        makeMenu(json, 'quizzes', '/quiz-');
        makeMenu(json, 'ranking', '/ranking-quiz-');
    });

function makeMenu(json, menuOption, href) {
    let quizzesMenu = document.getElementById(menuOption)
    let submenu = document.createElement('ul');
    quizzesMenu.appendChild(submenu);
    let menuItem = document.createElement('li');

    for (let i = 0; i < json.length; ++i) {
        submenu.appendChild(menuItem);
        let a = document.createElement('a');
        a.textContent = json[i].name;
        a.setAttribute('href', href + json[i].id);
        menuItem.appendChild(a);
        menuItem = document.createElement('li');
    }
}