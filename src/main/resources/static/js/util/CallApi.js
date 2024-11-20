import * as LoaderUtil from "./LoaderUtil.js";
import * as AlertMessageUtil from "./AlertMessageUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

export let callBackend = async function (elem, api, method, requestBody = {}) {
    LoaderUtil.showLoader(elem);

    let headers = {'Content-Type': 'application/json'};
    if (localStorage.getItem("token")) {
        headers.Authorization = localStorage.getItem("token");
    }

    let options = {
        method: method,
        headers: headers
    };
    if (method !== "GET") {
        options.body = JSON.stringify(requestBody);
    }

    return await fetch(api, options)
        .then(function (response) {
            return response.json();
        }).catch((error) => {
            console.log('Error: ', error);
            AlertMessageUtil.alertMessage(error);
        }).finally(function () {
            LoaderUtil.hideLoader(elem);
        });
};

export let uploadFile = async function (elem, api, requestBody = {}, method) {
    LoaderUtil.showLoader(elem);

    let headers = {};
    if (localStorage.getItem("token")) {
        headers.Authorization = localStorage.getItem("token");
    }

    let options = {
        method: method || "POST",
        headers: headers,
        body: requestBody
    };

    return await fetch(api, options)
        .then(function (response) {
            return response.json();
        }).catch((error) => {
            console.log('Error: ', error);
            AlertMessageUtil.alertMessage(error);
        }).finally(function () {
            LoaderUtil.hideLoader(elem);
        });
};

export let downloadFile = async function (elem, api) {
    LoaderUtil.showLoader(elem);

    let headers = {};
    if (localStorage.getItem("token")) {
        headers.Authorization = localStorage.getItem("token");
        headers.timeZoneOffsetInMinute = new Date().getTimezoneOffset()
    }

    let options = {
        method: "GET",
        headers: headers
    };

    fetch(api, options).then(function (response) {
        return response.json();
    }).then(function (jsonData) {
        window.open(
            EndPoints.BASE_URL + jsonData.data.fileUrl,
            '_blank'
        );
    }).catch((error) => {
        console.log('Error: ', error);
        AlertMessageUtil.alertMessage(error);
    }).finally(function () {
        LoaderUtil.hideLoader(elem);
    });
};