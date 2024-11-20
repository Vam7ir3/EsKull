import * as EndPoints from "./EndPoints.js";
import * as CallApi from "../util/CallApi.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";

export let login = async function (elem, requestBody = {}, callback) {

    CallApi.callBackend(elem, EndPoints.AUTH, "POST", requestBody)
        .then(response => {
            if (callback && typeof callback === 'function') {
                callback(response);
            }}
        );
};

export let passwordVerificationCode = function (elem, username, callback) {
    CallApi.callBackend(elem, EndPoints.PASSWORD_VERIFICATION_CODE + username, "GET")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let resetPassword = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.RESET_PASSWORD, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};