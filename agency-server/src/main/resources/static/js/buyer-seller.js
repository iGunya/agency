'use strict'

window.addEventListener('DOMContentLoaded', () => {
    const blockClient = document.querySelectorAll(".card");

    const addListenerModify = (node) => {
        const btnModify = node.querySelector(".modify"),
            btnClose = node.querySelector(".close");

        node.addEventListener("mouseover", e => {
            btnModify.style.opacity = "1";
            btnClose.style.opacity = "1";
        });

        node.addEventListener("mouseout", e => {
            btnModify.style.opacity = "0";
            btnClose.style.opacity = "0";
        });

        node.addEventListener("click", e => {
            if (e.target.className === ('modify')){
                fetch(e.target.parentElement.getAttribute("href"));
            }
            if (e.target.className === ('close')){
                const verify = +confirm('Вы действительно хотите удалить клиента?');
                if (!verify) {
                    e.preventDefault();
                }
            }
        });
    };

    blockClient.forEach(block => addListenerModify(block));
});
