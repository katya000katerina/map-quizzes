const pageSize = 5;
const quizId = new URLSearchParams(window.location.search).get('quiz')
let curPage = 0;
let lastPage = 0;

const nextButton = document.getElementById('nextButton');
nextButton.addEventListener('click', nextPage, false);

const prevButton = document.getElementById('prevButton');
prevButton.addEventListener('click', previousPage, false);

fetch('/api/v1/quizzes/' + quizId)
    .then(response => response.json())
    .then(json => {
        document.getElementById('quiz-name')
            .textContent = json.name;
    });

showPage(0, pageSize);

function showPage(pageNum) {
    let url = '/api/v1/ranking?quiz-id=' + quizId + '&page=' + pageNum + '&size=' + pageSize + '&sort=timeInMillis,asc';
    fetch(url)
        .then(response => response.json())
        .then(json => makeTable(json));
}

function previousPage() {
    if (curPage > 0) {
        curPage--;
        showPage(curPage);
    }
}

function nextPage() {
    if (curPage < lastPage) {
        curPage++;
        showPage(curPage);
    }
}

function makeTable(json) {
    lastPage = json.totalPages - 1;

    if (curPage === 0) {
        prevButton.style.visibility = "hidden";
    } else {
        prevButton.style.visibility = "visible";
    }

    if (curPage === lastPage) {
        nextButton.style.visibility = "hidden";
    } else {
        nextButton.style.visibility = "visible";
    }

    let table = document.getElementById('data');
    let content = json.content;
    let offset = json.pageable.offset;
    let data = '';

    for (let i = 0; i < content.length; i++) {
        data += '<tr>';
        data += '<td>' + (offset + i + 1) + '</td>'
        data += '<td>' + content[i].username + '</td>'
        data += '<td>' + getFormattedTime(content[i].timeInMillis) + '</td>'
        data += '</tr>';
    }
    table.innerHTML = data;
}

function getFormattedTime(millis) {
    let date = new Date(millis);
    return date.getMinutes() + ' min ' + date.getSeconds() + ' sec ' + date.getMilliseconds() + ' ms';
}