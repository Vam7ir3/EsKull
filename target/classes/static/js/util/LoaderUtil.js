export let showLoader = function (elem) {
    let div = document.createElement("div");
    div.setAttribute("id", "loaderContainer");
    let img = document.createElement("img");
    img.setAttribute("src", "/images/spinner_arrow.svg");
    div.appendChild(img);
    if (elem) {
        elem.appendChild(div);
    } else {
        document.querySelector("body").appendChild(div);
    }
};

export let hideLoader = function (elem) {
    let toRemove = document.querySelector("#loaderContainer");
    if (toRemove) {
        if (elem) {
            //elem.querySelector("#loaderContainer").remove();
            document.querySelector("#loaderContainer").remove();
        } else {
            document.querySelector("#loaderContainer").remove();
        }
    }
};