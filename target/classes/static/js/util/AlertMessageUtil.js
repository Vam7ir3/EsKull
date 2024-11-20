import {BASE_URL} from "../controller/EndPoints.js";

export let alertMessage = function (data) {
    let alertModal = document.querySelector("#idModalAlert");
    let messageHolder = alertModal.querySelector(".modal-body p");

    if (data.message && data.message === 'You are not Authorized.') {
        localStorage.clear();
        window.location = BASE_URL;
    }

    if ((typeof data.status === 'boolean' && data.status)) {
        messageHolder.style.color = "green";
    } else {
        messageHolder.style.color = "red";
    }
    messageHolder.textContent = (typeof data.status === 'boolean' && data.status) ? "Success: " + data.message : "Failure: " + data.message;
    $(alertModal).modal("show");
};