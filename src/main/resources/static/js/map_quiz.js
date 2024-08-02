import {fetchWithAuth} from './refresh_token.js';

let questions = [];
let url = window.location.pathname;
const mistakeQuizPath = "/mistakes";
const isMistakesQuiz = url.endsWith(mistakeQuizPath);
const isUserAuth = isMistakesQuiz ? true : (await fetchWithAuth('/api/v1/users/current')).ok;
const quizId = isMistakesQuiz ?
    url.substring(url.length - mistakeQuizPath.length - 1, url.length - mistakeQuizPath.length) :
    url.substring(url.length - 1);
let quizName;
let mistakesCount = 0; //a number of mistakes a user did while answering the current question
let notFirstTryAnswersCount = 0; //a number of questions a user could not give an answer to on the first try
let numberOfQuestions = 0;
let wasWrongAnswer = false; //whether there were wrong answers to the current question
let randomNumber;
const start = new Date();

uploadMap();

addOnClick();

fetchQuestions()

async function fetchQuestions() {
    const path = '/api/v1/quizzes-questions/' + quizId;
    let response;
    if (isMistakesQuiz) {
        response = await fetchWithAuth(path + mistakeQuizPath);
    } else {
        response = await fetch(path);
    }
    const json = await response.json();
    quizName = json.name;
    questions = json.questions;
    numberOfQuestions = questions.length;
    changeQuestion();
}

function addOnClick() {
    let elements = document.getElementsByClassName('question')
    for (let i = 0; i < elements.length; i++) {
        let element = elements[i];
        element.onclick = function () {
            let answer = element.getAttribute('id')
            console.log('answer in addOnClick(): ' + answer);
            document.getElementById('check').textContent = checkAnswer(answer);
        };
    }
}

function uploadMap() {
    let img = document.createElement('img');
    img.src = '../images/map.png'; //image from https://avatars.mds.yandex.net/i?id=c72f1825d44bc830eb5b09492ca871f9_l-5450752-images-thumbs&n=13
    let map = document.getElementById('map');
    map.appendChild(img);
}

function changeQuestion() {
    if (questions.length > 0) {
        randomNumber = Math.floor(Math.random() * questions.length);
        document.getElementById('currentQuestion')
            .textContent = questions[randomNumber].question;
    }
}

function removeQuestion(question) {
    let lastElement = questions[questions.length - 1];
    questions[questions.length - 1] = question;
    questions[randomNumber] = lastElement;
    questions.pop();
}

function changeColor(answer) {
    let color;
    if (mistakesCount === 0) {
        color = '#36c010';
    } else if (mistakesCount > 0 && mistakesCount < 3) {
        color = '#ffe31e';
    } else if (mistakesCount >= 3 && mistakesCount < 6) {
        color = '#ff8a08';
    } else {
        color = '#dc1616';
    }
    document.getElementById(answer).style.fill = color;
}

function help(question) {
    document.getElementById(question).style.fill = '#22289d';
}


function getQuizCompletedMessage(firstTryAnswersCount) {
    const message = 'You scored ' + firstTryAnswersCount + ' out of ' + numberOfQuestions + '.';
    if (isMistakesQuiz) {
        return message;
    }

    let end = new Date();
    let timeInMillis = end.getTime() - start.getTime();
    let millis = timeInMillis % 1000;
    let seconds = ((timeInMillis - millis) % 60000) / 1000;
    let minutes = (timeInMillis - timeInMillis % 60000) / 60000;
    if (isUserAuth) {
        updateTime(timeInMillis);
    }

    return message + ' Time: ' + minutes + ' min ' + seconds + ' sec ' + millis + ' ms';
}

function checkAnswer(answer) {
    if (questions[randomNumber].question === answer) {
        if (wasWrongAnswer) {
            if (isUserAuth) {
                saveOrupdateMistakes();
            }
            ++notFirstTryAnswersCount;
            wasWrongAnswer = false;
        } else if (isMistakesQuiz) {
            removeMistake();
        }

        changeColor(answer);
        mistakesCount = 0;
        removeQuestion(answer);

        if (questions.length !== 0) {
            changeQuestion();
            return 'You are right! Where is it located?';
        } else {
            let firstTryAnswersCount = numberOfQuestions - notFirstTryAnswersCount;
            return getQuizCompletedMessage(firstTryAnswersCount);
        }
    } else {
        wasWrongAnswer = true;
        ++mistakesCount;
        if (mistakesCount > 7) {
            help(questions[randomNumber].question);
        }
        return 'You are wrong. Try again!';
    }
}

function saveOrupdateMistakes() {
    const path = '/api/v1/mistakes';
    const mistake = {questionId: questions[randomNumber].id, numberOfMistakes: mistakesCount};
    const requestInit = {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(mistake)
    };
    fetchWithAuth(path, requestInit);
}

function removeMistake() {
    const path = '/api/v1/mistakes?question-id=' + questions[randomNumber].id;
    const requestInit = {method: 'DELETE'};
    fetchWithAuth(path, requestInit);
}

function updateTime(millis) {
    const path = '/api/v1/fastest-time';
    const time = {quizId: quizId, timeInMillis: millis};
    const requestInit = {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(time)
    }
    fetchWithAuth(path, requestInit);
}


