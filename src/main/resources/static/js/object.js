'use strict'

window.addEventListener('DOMContentLoaded', () => {
    const blockContent = document.querySelector(".objective-content"),
          nowContent = [];

    const viewPhoto = function (photos) {
        let html = '';
        photos.forEach((item,i) =>{
            html +=
`                                    <div class="carousel-item ${i===0?'active':''}">
                                        <img src="https://storage.yandexcloud.net/okm3agency/${item.url_photo}" class="d-block w-100" alt="да"/>
                                    </div>
`

        });
        return html
    }

    const viewSliderImg = function (json) {
        const html =
        `                    <div class="col-4">
                                <div class="row">
                                    <div id="sliderBigImages${json.idObject}" class="carousel slide" data-ride="carousel" style="width: 270px">
                                        <div class="carousel-inner">
        ${viewPhoto(json.photos)}       
                                        </div>
                                        <a class="carousel-control-prev" href="#sliderBigImages${json.idObject}" role="button" data-slide="prev">
                                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                            <span class="sr-only">Previous</span>
                                        </a>
                                        <a class="carousel-control-next" href="#sliderBigImages${json.idObject}" role="button" data-slide="next">
                                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                            <span class="sr-only">Next</span>
                                        </a>
                                    </div>
                                    <a href="/managers/objects/delete/${json.idObject}" >
                                        <img src="/image/delete.png" class="close" alt="удоли"/>
                                    </a>
                                </div>
                            </div>
        `
        return html;
    };

    const viewInfoObject = function (json) {
        const html =
`                            <div class = "col-8">
                                <div class="row">
                                    <div class="col-4 price">
                                        <h4 >${json.price}</h4>
                                    </div>
                                    <div class="col-8 info">
                                        <h6>${json.countRoom} к, ${json.square} м2</h6>
                                        <p>${json.countFloor} этаж</p>
                                        <p>${json.adress}</p>
                                    </div>
                                </div>
                                <div class="desc">
                                     ${json.description} шаблон описания очнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много буквочнень много
                                </div>
                            </div>
`
        return html;
    };

    const viewObject = function (json) {
         const object = document.createElement('div');
        const html = `
                    <a href="/managers/objects/update/${json.idObject}">
                        <div class = "row my_card">
         ${viewSliderImg(json)}
 ${viewInfoObject(json)}
                        </div>
                    </a>
        `;
        object.innerHTML = html;
        nowContent.push(object);
        blockContent.append(object);
    };

    const getData = function (filters = {}) {
        const url = new URL('http://localhost:8080/managers/objects/filter');
        url.search = new URLSearchParams(filters).toString();

        return  fetch(url)
            .then(response => response.json())
            .then(json => {for (let object in json) {
                // console.log(json[object]);
                viewObject(json[object]);
                addListenerRemove(nowContent);
            }

            });
    }

    getData();

    const countRoom = document.querySelector('input[name="countRoom"]'),
          maxPrice = document.querySelector('input[name="maxPrice"]'),
          minPrice = document.querySelector('input[name="minPrice"]'),
          typeObject = document.querySelector('select[name="typeObject"]'),
          typeMove = document.querySelector('select[name="typeMove"]');

    const listFilters = [countRoom, maxPrice, minPrice, typeObject, typeMove],
          filter = {};

    const addListener = function (filterParam) {
        filterParam.addEventListener("change", e => {
            e.defaultPrevented;

            filter[filterParam.name] = filterParam.value;

            nowContent.forEach(node => node.remove());

            getData(filter);
        });
    }

    listFilters.forEach(fild => addListener(fild));

    //удаление объекта

    const addListenerRemove = (listNode) => {
        listNode.forEach(node => node.addEventListener("click", e => {
            if (e.target.className === 'close'){
                e.preventDefault();
                const verify = confirm('Вы действительно хотите удалить объект?');
                if (verify) {
                    fetch(e.target.parentElement.getAttribute("href"))
                        .then(response => node.remove())
                }
            }
        }));
    };

});

// onclick="return confirm('Вы действительно хотите удалить объект?')"