'use strict'

window.addEventListener('DOMContentLoaded', () => {
    const blockClient = document.querySelectorAll(".bs-example");

    const addListenerModify = (node) => {
        node.addEventListener("mouseover", e => {
            const btnModify = node.querySelector(".modify");
            btnModify.style.display = "inline";
        });

        node.addEventListener("mouseout", e => {
            const btnModify = node.querySelector(".modify");
            btnModify.style.display = "none";
        });

        node.addEventListener("click", e => {
            if (e.target.className === 'modify'){
                fetch(e.target.parentElement.getAttribute("href"));
            }
        });
    };

    blockClient.forEach(block => addListenerModify(block));
});
