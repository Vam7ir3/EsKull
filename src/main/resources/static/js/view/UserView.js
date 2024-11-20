'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as UserController from "../controller/UserController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";
import * as UserUI from "../ui/UserUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(EndPoints.ROLE, "title", UserUI.idSelectUserRole);

    if (!CommonUtil.hasAuthority("USER_C")) {
        $(UserUI.idBtnPopAddUser).hide();
    } else {
        UserUI.idBtnPopAddUser.addEventListener("click", function (event) {
            $(UserUI.idUserShow).hide();
            $(UserUI.idFormUserUpdate).hide();
            $(UserUI.idFormUserAdd).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.ROLE, "title", UserUI.userRoleAdd);
            UserUI.modalUserTitle.textContent = "Add User";
            $(UserUI.idModalUser).modal("show");
        });

        //User add form submission
        $(UserUI.idFormUserAdd).validate({
            rules: {
                userRole: "required",
                firstName: "required",
                lastName: "required",
                emailAddress: "required",
                mobileNumber: "required",
                password: "required"
            },
            messages: {
                userRole: "User Role required.",
                firstName: "First Name required",
                lastName: "Last Name required",
                emailAddress: "Email Address required",
                mobileNumber: "Mobile Number required.",
                password: "Password required"
            },
            submitHandler: function (form) {
                let requestBody = {
                    "roleId": $(UserUI.userRoleAdd).val(),
                    "firstName": form.querySelector("input[name='firstName']").value,
                    "lastName": form.querySelector("input[name='lastName']").value,
                    "emailAddress": form.querySelector("input[name='emailAddress']").value,
                    "mobileNumber": form.querySelector("input[name='mobileNumber']").value,
                    "password": form.querySelector("input[name='password']").value
                };
                if (form.querySelector("input[name='gender']:checked")) {
                    requestBody.gender = form.querySelector("input[name='gender']:checked").value;
                }
                if (form.querySelector("input[name='dateOfBirth']").value) {
                    requestBody.dateOfBirth = new Date(form.querySelector("input[name='dateOfBirth']").value).valueOf();
                }

                UserController.addUser(form, requestBody, function () {
                    UserController.listUser(UserUI.idTableUser);
                });
            }
        });
    }

    UserController.listUser(UserUI.idTableUser);

    $(UserUI.idTableUser).on('draw.dt', function () {

        //Edit Event Listener
        let edits = UserUI.idTableUser.querySelectorAll(".userEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let user = event.currentTarget.dataset.user;
                putValueInEditForm(JSON.parse(user));
                $(UserUI.idUserShow).hide();
                $(UserUI.idFormUserAdd).hide();
                $(UserUI.idFormUserUpdate).show();
                UserUI.modalUserTitle.textContent = "Edit User";
                $(UserUI.idModalUser).modal("show");
            });
        });

        //Show Event Listener
        let shows = UserUI.idTableUser.querySelectorAll(".userShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let user = event.currentTarget.dataset.user;
                putValueInShow(JSON.parse(user));
                $(UserUI.idFormUserAdd).hide();
                $(UserUI.idFormUserUpdate).hide();
                $(UserUI.idUserShow).show();
                UserUI.modalUserTitle.textContent = "User Detail";
                $(UserUI.idModalUser).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = UserUI.idTableUser.querySelectorAll(".userDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                UserUI.idBtnUserDelete.dataset.user_id = event.currentTarget.dataset.user_id;
                $(UserUI.idModalUserDelete).modal("show");
            });
        });

        // Delete Event Listener for Answers
        let answerDeletes = UserUI.idTableUser.querySelectorAll(".answerDelete");
        Array.from(answerDeletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                UserUI.idBtnAnswerDelete.dataset.user_id = event.currentTarget.dataset.user_id;
                $(UserUI.idModalAnswerDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (user) {
        UserUI.id.value = user.id;
        SelectPickerUtil.populateSelectPicker(EndPoints.ROLE, "title", UserUI.userRoleUpdate, user.role.id);
        UserUI.firstNameUpdate.value = user.firstName;
        UserUI.lastNameUpdate.value = user.lastName;
        UserUI.emailAddressUpdate.value = user.emailAddress;

        if (user.gender) {
            UserUI.genderUpdate.forEach(function (item) {
                if (item.value === user.gender) {
                    item.checked = true;
                }
            });
        }
        if (user.dateOfBirth) {
            UserUI.dateOfBirthUpdate.value = DateTimeUtil.prepareDateForInput(parseInt(user.dateOfBirth));
        }
        if (user.mobileNumber) {
            UserUI.mobileNumberUpdate.value = user.mobileNumber;
        }
    };

    let putValueInShow = function (user) {
        UserUI.idRoleShow.textContent = user.role.title;
        UserUI.idNameShow.textContent = user.firstName + " " + user.lastName;
        UserUI.idEmailAddressShow.textContent = user.emailAddress;

        if (user.gender) {
            UserUI.idGenderShow.textContent = user.gender;
        }
        if (user.dateOfBirth) {
            UserUI.idDateOfBirthShow.textContent = DateTimeUtil.prepareDateForText(parseInt(user.dateOfBirth));
        }
        if (user.mobileNumber) {
            UserUI.idMobileNumberShow.textContent = user.mobileNumber;
        }
    };


    //User Update Form submission
    $(UserUI.idFormUserUpdate).validate({
        rules: {
            userRole: "required",
            firstName: "required",
            lastName: "required",
            emailAddress: "required",
            mobileNumber: "required"
        },
        messages: {
            userRole: "User Role required.",
            firstName: "First Name required",
            lastName: "Last Name required",
            emailAddress: "Email Address required",
            mobileNumber: "Mobile Number required."
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "roleId": $(UserUI.userRoleAdd).val(),
                "firstName": form.querySelector("input[name='firstName']").value,
                "lastName": form.querySelector("input[name='lastName']").value,
                "emailAddress": form.querySelector("input[name='emailAddress']").value,
                "mobileNumber": form.querySelector("input[name='mobileNumber']").value
            };
            if (form.querySelector("input[name='gender']:checked")) {
                requestBody.gender = form.querySelector("input[name='gender']:checked").value;
            }
            if (form.querySelector("input[name='dateOfBirth']").value) {
                requestBody.dateOfBirth = new Date(form.querySelector("input[name='dateOfBirth']").value).valueOf();
            }

            UserController.updateUser(form, requestBody, function () {
                UserController.listUser(UserUI.idTableUser);
            });
        }
    });


    //User filter
    UserUI.idSelectUserRole.addEventListener('change', function (event) {
        UserController.filterByRole(UserUI.idTableUser, $(UserUI.idSelectUserRole).val());
    });


    //Delete
    UserUI.idBtnUserDelete.addEventListener("click", function (event) {
        UserController.deleteUser(UserUI.idModalUserDelete, event.currentTarget.dataset.user_id, function () {
            UserController.listUser(UserUI.idTableUser);
        });
    });

    //Delete Answer
    UserUI.idBtnAnswerDelete.addEventListener("click", function (event) {
        UserController.deleteAnswer(UserUI.idModalAnswerDelete, event.currentTarget.dataset.user_id, function () {
            UserController.listUser(UserUI.idTableUser);
        });
    });

    $(UserUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(UserUI.idModalUser).modal("hide");
        $(UserUI.idFormUserAdd).trigger("reset");
        $(UserUI.idFormUserUpdate).trigger("reset");
        $(UserUI.idModalUserDelete).modal("hide");
        $(UserUI.idModalAnswerDelete).modal("hide");
        $(UserUI.modalAlertBody).html("");
    });
});