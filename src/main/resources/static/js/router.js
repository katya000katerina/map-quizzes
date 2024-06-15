const route = (event) => {
    event = event || window.event;
    event.preventDefault();
    window.history.pushState({}, '', event.target.href);
    locationHandler();
};

const routes = {
    404: {
        page: '/pages/404.html',
        title: '404'
    },
    '/': {
        page: '/pages/home.html',
        title: 'Home',
    },
    '/sign-up': {
        page: '/pages/sign_up.html',
        title: 'Sign up',
        script: '/js/sign_in_up.js'
    },
    '/sign-in': {
        page: '/pages/sign_in.html',
        title: 'Sign in',
        script: '/js/sign_in_up.js'
    },
    '/quiz-1' :{
        page: '/pages/quiz_1.html',
        title: 'Mountains of the world',
        script: '/js/map_quiz.js'
    }
};

const locationHandler = async () => {
    let location = window.location.pathname;
    if (location.length === 0) {
        location = '/';
    }
    const route = routes[location] || routes['404'];
    const page = await fetch(route.page).then((response) => response.text());
    document.getElementById('content').innerHTML = page;
    document.title = route.title;
    if (route.script != null) {
        const script = document.createElement('script');
        script.type = 'module';
        script.src = route.script;
        const element = document.getElementsByTagName('script')[0];
        element.parentNode.insertBefore(script, element);
    }
};

window.onpopstate = locationHandler;
window.route = route;
locationHandler();


