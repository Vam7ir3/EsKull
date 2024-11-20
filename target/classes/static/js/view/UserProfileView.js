'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as UserController from "../controller/UserController.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";
import * as UserProfileUI from "../ui/UserProfileUI.js";
import * as LoginController from "../controller/LoginController.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    let showUser = function () {
        let user = JSON.parse(localStorage.getItem("user"));

        UserProfileUI.idRoleShow.textContent = user.role.title;
        UserProfileUI.idNameShow.textContent = user.firstName + " " + user.lastName;
        UserProfileUI.idEmailAddressShow.textContent = user.emailAddress;

        if (user.gender) {
            UserProfileUI.idGenderShow.textContent = user.gender;
        }
        if (user.dateOfBirth) {
            UserProfileUI.idDateOfBirthShow.textContent = DateTimeUtil.prepareDateForText(parseInt(user.dateOfBirth));
        }
        if (user.mobileNumber) {
            UserProfileUI.idMobileNumberShow.textContent = user.mobileNumber;
        }

        UserProfileUI.hiddenUserId.value = user.id;
    };
    showUser();

    //Edit Event Listener
    UserProfileUI.idBtnPopEditUser.addEventListener('click', function (event) {
        putValueInEditForm(JSON.parse(localStorage.getItem("user")));
        $(UserProfileUI.idFormUserUpdate).show();
        UserProfileUI.modalTitle.textContent = "Edit Profile";
        $(UserProfileUI.idModalUser).modal("show");
    });

    let putValueInEditForm = function (user) {
        UserProfileUI.id.value = user.id;
        UserProfileUI.firstNameUpdate.value = user.firstName;
        UserProfileUI.lastNameUpdate.value = user.lastName;
        UserProfileUI.emailAddressUpdate.value = user.emailAddress;

        if (user.gender) {
            UserProfileUI.genderUpdate.forEach(function (item) {
                if (item.value === user.gender) {
                    item.checked = true;
                }
            });
        }
        if (user.dateOfBirth) {
            UserProfileUI.dateOfBirthUpdate.value = DateTimeUtil.prepareDateForInput(parseInt(user.dateOfBirth));
        }
        if (user.mobileNumber) {
            UserProfileUI.mobileNumberUpdate.value = user.mobileNumber;
        }
    };

    //User Update Form submission
    $(UserProfileUI.idFormUserUpdate).validate({
        rules: {
            firstName: "required",
            lastName: "required",
            emailAddress: "required",
            mobileNumber: "required"
        },
        messages: {
            firstName: "First Name required",
            lastName: "Last Name required",
            emailAddress: "Email Address required",
            mobileNumber: "Mobile Number required."
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "firstName": form.querySelector("input[name='firstName']").value,
                "lastName": form.querySelector("input[name='lastName']").value,
                "emailAddress": form.querySelector("input[name='emailAddress']").value,
                "mobileNumber": form.querySelector("input[name='mobileNumber']").value
            };

            if (form.querySelector("input[name='gender']").value) {
                requestBody.gender = form.querySelector("input[name='gender']:checked").value;
            }
            if (form.querySelector("input[name='dateOfBirth']").value) {
                requestBody.dateOfBirth = new Date(form.querySelector("input[name='dateOfBirth']").value).valueOf();
            }

            UserController.updateUser(form, requestBody, function (response) {
                localStorage.setItem("user", JSON.stringify(response.data.user));
                showUser();
            });
        }
    });

    //Change password
    $(UserProfileUI.idFormUserPasswordUpdate).validate({
        rules: {
            oldPassword: "required",
            newPassword: "required",
            retypeNewPassword: {
                required: true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            oldPassword: "Old Password required.",
            newPassword: "New Password required.",
            retypeNewPassword: {
                required: "Please retype new Password.",
                equalTo: "Password does not match."
            }
        },
        submitHandler: function (form) {
            let requestBody = {
                "userId": form.elements["id"].value,
                "oldPassword": form.elements["oldPassword"].value,
                "newPassword": form.elements["newPassword"].value
            };
            UserController.changePassword(form, requestBody, function (response) {
                form.reset();
                if (response.status === true) {
                    LoginController.login(form, {
                        "username": (JSON.parse(localStorage.getItem("user"))).emailAddress,
                        "password": requestBody.newPassword
                    }, function (response) {
                        if (typeof response.status === "boolean" && response.status) {
                            localStorage.setItem("token", response.data.token);
                            localStorage.setItem("token_date", new Date());
                            localStorage.setItem("expire_in", response.data.expireIn);//Minutes
                            localStorage.setItem("user", JSON.stringify(response.data.user));
                            localStorage.setItem("role", JSON.stringify(response.data.user.role));
                            localStorage.setItem("authorities", JSON.stringify(response.data.user.role.authorities));
                        } else {
                            AlertMessageUtil.alertMessage(response);
                        }
                    });
                }
            });
        }
    });

    $(UserProfileUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(UserProfileUI.idModalUser).modal("hide");
        $(UserProfileUI.idFormUserUpdate).trigger("reset");
        $(UserProfileUI.modalAlertBody).html("");
    });
});