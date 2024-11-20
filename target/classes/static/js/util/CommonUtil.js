'use strict';

import * as EndPoints from "../controller/EndPoints.js";
import * as NavUtil from "./NavUtil.js";

export let initialSetup = function () {
    loginCheck();
    NavUtil.addNavMenu();
    NavUtil.addNavigationLink();
    NavUtil.addToggleFunctionality();
    loadProfile();
};

let loginCheck = function () {

    let accessToken = localStorage.getItem("token");
    let date = localStorage.getItem("token_date");
    let expireIn = localStorage.getItem("expire_in");//Minutes
    if (accessToken && date && expireIn) {
        let currentMillis = (new Date()).getTime();
        let expireMillis = new Date(date).getTime() + (expireIn * 60 * 1000);
        if (currentMillis > expireMillis) {
            localStorage.clear();
            window.location = EndPoints.BASE_URL;
        }
    } else {
        window.location = EndPoints.BASE_URL;
    }
};

let loadProfile = function () {
    let loggedInUser = document.querySelector("#loggedInUser");
    if (loggedInUser) {
        let user = JSON.parse(localStorage.getItem("user"));
        loggedInUser.textContent = user.firstName + " " + (user.middleName ? user.middleName + " " : "") + user.lastName;
    }
};

export let hasAuthority = function (authority) {
    let authorities = JSON.parse(localStorage.getItem("authorities"));
    for (let item of authorities) {
        if (item.title === authority) {
            return true;
        }
    }
};