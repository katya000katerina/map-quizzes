let form = document.forms[0];
let formId = form.id;
document
    .getElementById(formId)
    .addEventListener('submit', function (evt) {
        evt.preventDefault();
        let formData = new FormData(form);
        let uri = '/api/v1/auth/' + (formId === 'sign-in-from' ? 'sign-in' : 'sign-up');
        let object = {};
        formData.forEach((value, key) => object[key] = value);
        let json = JSON.stringify(object);

        fetch(uri, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: json
        })
            .then(response => {
                if (response.ok){
                    window.location.href = '/my-profile';
                }

                const status = response.status;
                return response.json().then(json => ({json, status}));
            })
            .then(({json, status}) => {
                const errorContainer = document.getElementById('error-container');
                errorContainer.innerHTML = '';

                if (status === 400 || status === 401 || status === 403) {
                    errorContainer.style.display = 'block';
                    switch (status) {
                        case 400:
                            json.errors.forEach(error => {
                                const errorMessage = error.split(':')[1] || error;
                                addErrorMessage(errorContainer, errorMessage);
                            });
                            break;
                        case 401:
                            addErrorMessage(errorContainer,json.message);
                            break;

                    }
                }
            });
    });

function addErrorMessage(container, message) {
    const errorElement = document.createElement('p');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    container.appendChild(errorElement);
}

