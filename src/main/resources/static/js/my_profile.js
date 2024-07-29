import {fetchWithAuth} from './refresh_token.js';

document.getElementById('upload-image').addEventListener('change', function (event) {
    const file = event.target.files[0];
    const formData = new FormData();
    formData.append('image', file);

    fetch('/api/v1/images', {
        method: 'POST',
        body: formData
    })
        .then(response => response.blob())
        .then(blob => {
            const imageUrl = URL.createObjectURL(blob);
            document.getElementById('user-image').src = imageUrl;
        });
});

fetchWithAuth('/api/v1/users/current')
    .then(response => response.json())
    .then(json => {
        document
            .getElementById('username')
            .textContent = 'Hello, ' + json.username + '!';
    });

fetchWithAuth('/api/v1/mistakes/current')
    .then(response => response.json())
    .then(json => createMistakesLists(json));

fetchWithAuth('/api/v1/ranking/current')
    .then(response => response.json())
    .then(json => createRankingList(json));

function createRankingList(json) {
    if (Object.keys(json).length === 0) {
        document.getElementById('my-ranking').textContent = 'Complete at least one test to see your ranking!';
        document.getElementById('ranking-list').style.visibility = 'hidden';
        document.getElementById('download-certificate').style.visibility = 'hidden';
        return;
    }

    const listContainer = document.getElementById('ranking-list');
    json.forEach(quiz => {
        const listItem = document.createElement('li');
        listItem.textContent = `${quiz.quizName}: ${quiz.rank}`;
        listContainer.appendChild(listItem);
    });
}

function createMistakesLists(json) {
    if (Object.keys(json).length === 0) {
        document.getElementById('mistakes-list').style.visibility = 'hidden';
        return;
    }

    const container = document.getElementById('mistakes-container');
    json.forEach(quiz => {
        const btn = document.createElement('button');
        btn.className = 'collapsible';
        btn.innerHTML = `${quiz.quizName} &#x25B8;`;

        const content = document.createElement('div');
        content.className = 'content';
        quiz.mistakes.forEach((mistake, index) => {
            const p = document.createElement('p');
            p.textContent = `${index + 1}. ${mistake.question} (Mistakes: ${mistake.numberOfMistakes})`;
            content.appendChild(p);
        });

        const link = document.createElement('a');
        link.href = `/quiz-${quiz.quizId}/mistakes`;
        link.textContent = 'Take a quiz on these mistakes';
        content.appendChild(link);

        btn.addEventListener('click', () => {
            btn.classList.toggle('active');
            const isActive = btn.classList.contains('active');
            btn.innerHTML = `${quiz.quizName} ${isActive ? '&#x25BE;' : '&#x25B8;'}`;
            content.style.display = isActive ? 'block' : 'none';
        });

        container.appendChild(btn);
        container.appendChild(content);
    });
}