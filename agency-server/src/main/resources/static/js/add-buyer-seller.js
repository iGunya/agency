'use strict'

window.addEventListener('DOMContentLoaded', () => {
    const fieldPassport = document.querySelector("#passport"),
          where = document.location.pathname.toString();

    const getData = function (client) {
        return  fetch(`http://localhost:2203/managers/clients/${client}/${fieldPassport.value}`)
            .then(response => response.text());
    }

    fieldPassport.addEventListener("change", e => {
        e.defaultPrevented;

        let client = "seller"

        if (!where.includes(client)){
            client = "buyer";
        }

        getData(client)
            .then(text => {
                if (text === "not unique"){
                    const statusMessage = document.createElement('div');
                    statusMessage.classList.add('error');
                    statusMessage.style.color = "red";
                    statusMessage.textContent = "Такой номер уже существует";
                    fieldPassport.parentElement.append(statusMessage);

                    setTimeout(() => {
                        statusMessage.remove();
                    }, 5000);
                }
            });
    });
});