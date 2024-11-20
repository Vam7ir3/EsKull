'use strict';

import * as LoginController from '../controller/LoginController.js';
import {LoginModel} from "../model/LoginModel.js";
import * as IndexUI from "../ui/IndexUI.js";
import * as EndPoints from "../controller/EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";

$(document).ready(function () {

    if (localStorage.getItem("token")) {
        window.location = EndPoints.BASE_URL + "/home.html";
    }

    //Login
    $(IndexUI.formLogin).validate({
        rules: {
            username: "required",
            password: "required"
        },
        messages: {
            username: "Username required.",
            password: "Password required."
        },
        submitHandler: function (form) {
            LoginController.login(form, new LoginModel(form.querySelector("input[name='username']").value,
                form.querySelector("input[name='password']").value),
                function (response) {
                    if (typeof response.status === "boolean" && response.status) {
                        localStorage.setItem("token", response.data.token);
                        localStorage.setItem("token_date", new Date());
                        localStorage.setItem("expire_in", response.data.expireIn);//Minutes
                        localStorage.setItem("user", JSON.stringify(response.data.user));
                        localStorage.setItem("role", JSON.stringify(response.data.user.role));
                        localStorage.setItem("authorities", JSON.stringify(response.data.user.role.authorities));

                        window.location.href = EndPoints.BASE_URL + "/home.html";
                    } else {
                        AlertMessageUtil.alertMessage(response);
                    }
                }
            );
        }
    });

    //Password Verification Code
    $(IndexUI.idFormForgotPassword).validate({
        rules: {
            username: "required"
        },
        messages: {
            username: "Username required."
        },
        submitHandler: function (form) {
            LoginController.passwordVerificationCode(form, form.querySelector("input[name='username']").value);
        }
    });

    //Reset Password
    $(IndexUI.idFormResetPassword).validate({
        rules: {
            verificationCode: "required",
            newPassword: "required",
            confirmPassword: {
                required: true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            verificationCode: "Verification Code required",
            newPassword: "New Password required."
        },
        submitHandler: function (form) {
            let requestBody = {
                "verificationCode": form.querySelector("input[name='verificationCode']").value,
                "newPassword": form.querySelector("input[name='newPassword']").value
            };

            LoginController.resetPassword(form, requestBody);
        }
    });
});