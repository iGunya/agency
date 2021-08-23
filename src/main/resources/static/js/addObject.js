'use strict'

window.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector("form"),
          btn = form.querySelector("button");

    const postData = async (url,data) => {
        return  await fetch(url, {
            method: "POST",
            body: data
        });

    };

    const createBodyRequest = () => {
        const inputs = form.querySelectorAll("input, select, textarea"),
              formData = new FormData(),
              object = {};

        inputs.forEach(fild => {
            if (fild.name === "fileName") {
                for (const file of fild.files) {
                    formData.append('fileName[]', file, file.name);
                }
                // formData.append("fileName", fild.files);
            } else {
                object[fild.name] = fild.value;
            }
        })
        const jsonObj = new File([JSON.stringify(object)], 'object.json',{ type : "application/json"});
        formData.append('object', jsonObj, jsonObj.name);
        return formData
    }

    const veiwErrorValidate = arrError => {
        const arrErorsVeiw = [];
        arrError.forEach(error => {
            const statusMessage = document.createElement('div'),
                  feldError = document.querySelector(`[name="${error.field}"]`);
            statusMessage.classList.add('error');
            statusMessage.style.color = "red";
            statusMessage.textContent = error.defaultMessage;
            feldError.parentElement.append(statusMessage)
            arrErorsVeiw.push(statusMessage);
        });

        setTimeout(() => {
            arrErorsVeiw.forEach(messageError => messageError.remove())
        }, 5000);
    }

    const veiwSuccess = (text) => {
        const statusMessage = document.createElement("div");
        btn.style.display = "none";
        btn.parentElement.append(statusMessage);
        statusMessage.textContent = text;
        statusMessage.style.color = "red";
        setTimeout(() => {
            statusMessage.remove();
            btn.style.display = "inline";
        }, 5000);
    }

    btn.addEventListener('click', ev => {
        ev.defaultPrevented;

        const formData = createBodyRequest();

        postData("http://localhost:8080/managers/objects/add", formData)
            .then(responce =>responce.text())
            .then( (body) => {
                if (body === "Объект добавлен"){
                    veiwSuccess(body)
                } else {
                    veiwErrorValidate(JSON.parse(body));
                }
            }).catch(
            () => {
                console.log("Ошибка");
            })
    })
});