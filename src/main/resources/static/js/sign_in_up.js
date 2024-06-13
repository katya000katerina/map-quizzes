let form = document.forms[0];
let formId = form.id;
document
    .getElementById(formId)
    .addEventListener('submit', function (evt) {
        evt.preventDefault();
        let formData = new FormData(form);
        let uri = "/api/v1/auth/" + (formId === 'sign-in-from' ? 'sign-in' : 'sign-up');
        let object = {};
        formData.forEach((value, key) => object[key] = value);
        let json = JSON.stringify(object);

        fetch(uri, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: json
        }).then(response => {
            if (response.ok){
                window.location.href = "/";
            }
        });
    });

