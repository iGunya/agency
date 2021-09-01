'use strict'

window.addEventListener('DOMContentLoaded', () => {
    const btnsSubmit = document.querySelectorAll("button")

    const postData = async (url,data) => {
      return  await fetch(url, {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: data
      });

    };

    const message = {
        success: "Измененно",
        bad: "Логин занят",
        load: "image/spinner.svg",
        catchError: "Что-то не так"
    };

    const addListener = function(btnSubmit)  {
        btnSubmit.addEventListener("click", e => {
            e.defaultPrevented;

            const forms = btnSubmit.parentElement.parentElement.querySelector("form"),
                 formData = new FormData(forms);

            const obj = {};
            formData.forEach((value, key)=> {
                obj[key] = value;
            });

            const json = JSON.stringify(obj);

            const statusMessage = document.createElement('div');
            statusMessage.classList.add('status');
            statusMessage.style.color = "red";
            btnSubmit.style.display = "none";
            btnSubmit.parentElement.append(statusMessage);
            // forms.append(statusMessage);

            postData("http://localhost:2203/only_for_admins/grand", json)
                .then( (data) => {
                    console.log(data);
                    if(data.status === 200) {
                        statusMessage.textContent = message.success;
                    } else {
                        statusMessage.textContent = message.bad;
                    }
                }).catch(
                () => {
                    statusMessage.textContent = message.catchError;
                })

            setTimeout(() => {
                statusMessage.remove();
                btnSubmit.style.display = "inline";
            }, 5000);
        });
    }

    btnsSubmit.forEach(e => addListener(e));
});
