import {fetchWithAuth} from './refresh_token.js';

class Access {
    static all = new Access();
    static authorized = new Access();
    static anonymous = new Access();
}

const route = (event) => {
    event = event || window.event;
    event.preventDefault();
    window.history.pushState({}, '', event.target.href);
    locationHandler();
};

const routes = {
    404: {
        page: '/pages/404.html',
        title: '404',
        access: Access.all
    },
    '/': {
        page: '/pages/home.html',
        title: 'Home',
        access: Access.all
    },
    '/sign-up': {
        page: '/pages/sign_up.html',
        title: 'Sign up',
        script: '/js/sign_in_up.js',
        access: Access.anonymous
    },
    '/sign-in': {
        page: '/pages/sign_in.html',
        title: 'Sign in',
        script: '/js/sign_in_up.js',
        access: Access.anonymous
    },
    '/quiz-1': {
        page: '/pages/quiz_1.html',
        title: 'Mountains of the world',
        script: '/js/map_quiz.js',
        access: Access.all
    },
    '/quiz-2': {
        page: '/pages/quiz_2.html',
        title: 'Volcanoes of the world',
        script: '/js/map_quiz.js',
        access: Access.all
    },
    '/ranking': {
        page: '/pages/ranking.html',
        title: 'ranking',
        script: '/js/ranking.js',
        access: Access.all
    },
    '/my-profile': {
        page: '/pages/my_profile.html',
        title: 'My profile',
        script: '/js/my_profile.js',
        access: Access.authorized
    },
    '/settings': {
    page: '/pages/settings.html',
        title: 'My profile',
        script: '/js/settings.js',
        access: Access.authorized}

};

const locationHandler = async () => {
    let location = window.location.pathname;
    if (location.length === 0) {
        location = '/';
    }

    let access;
    const mistakeQuiz = '/mistakes';
    if (location.endsWith(mistakeQuiz)) {
        location = location.endsWith(mistakeQuiz) ?
            location.substring(0, location.length - mistakeQuiz.length) : location;
        access = Access.authorized;
    }

    const route = routes[location] || routes['404'];
    access = access != null ? access : route.access;
    const isUserAuth = (await fetchWithAuth('/api/v1/users/current')).ok;

    if (Object.is(access, Access.authorized) && !isUserAuth) {
        window.location.href = '/sign-in';
    } else if (Object.is(access, Access.anonymous) && isUserAuth) {
        window.location.href = '/my-profile';
    }


    const page = await fetch(route.page).then((response) => response.text());
    document.getElementById('content').innerHTML = page;
    document.title = route.title;

    const pageScript = route.script;
    if (pageScript != null) {
        const script = document.createElement('script');
        script.type = 'module';
        script.src = pageScript;
        const element = document.getElementsByTagName('script')[0];
        element.parentNode.insertBefore(script, element);
    }
};

window.onpopstate = locationHandler;
window.route = route;
locationHandler();