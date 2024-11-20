'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as AllowedRegistrationUI from "../ui/AllowedRegistrationUI.js";
import * as AllowedRegistrationController from "../controller/AllowedRegistrationController.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("ALLOWED_REGISTRATION_C")) {
        $(AllowedRegistrationUI.idBtnPopAddAllowedRegistration).hide();
    } else {
        AllowedRegistrationUI.idBtnPopAddAllowedRegistration.addEventListener("click", function (event) {
            $(AllowedRegistrationUI.idFormAllowedRegistrationUpdate).hide();
            $(AllowedRegistrationUI.idFormAllowedRegistrationImport).hide();
            $(AllowedRegistrationUI.idFormAllowedRegistrationAdd).show();
            $(AllowedRegistrationUI.modalAllowedRegistrationTitle).textContent = "Add Allowed Registration";
            $(AllowedRegistrationUI.idModalAllowedRegistration).modal("show");
        });

        $(AllowedRegistrationUI.idFormAllowedRegistrationAdd).validate({
            rules: {
                email: "required"
            },
            messages: {
                email: "Email required."
            },
            submitHandler: function (form) {
                let requestBody = {
                    "email": form.querySelector("input[name='email']").value
                };

                AllowedRegistrationController.addAllowedRegistration(form, requestBody, function () {
                    AllowedRegistrationController.listAllowedRegistration(AllowedRegistrationUI.idTableAllowedRegistration);
                });
            }
        });
    }

    AllowedRegistrationController.listAllowedRegistration(AllowedRegistrationUI.idTableAllowedRegistration);
    $(AllowedRegistrationUI.idTableAllowedRegistration).on('draw.dt', function () {
        let allowedRegistrationEdit = AllowedRegistrationUI.idTableAllowedRegistration.querySelectorAll(".allowedRegistrationEdit");
        Array.from(allowedRegistrationEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let allowedRegistration = event.currentTarget.dataset.allowed_registration;
                putValueInEditForm(JSON.parse(allowedRegistration));
                $(AllowedRegistrationUI.idFormAllowedRegistrationAdd).hide();
                $(AllowedRegistrationUI.idFormAllowedRegistrationImport).hide();
                $(AllowedRegistrationUI.idFormAllowedRegistrationUpdate).show();
                AllowedRegistrationUI.modalAllowedRegistrationTitle.textContent = "Edit Allowed Registration";
                $(AllowedRegistrationUI.idModalAllowedRegistration).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = AllowedRegistrationUI.idTableAllowedRegistration.querySelectorAll(".allowedRegistrationDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                AllowedRegistrationUI.idBtnAllowedRegistrationDelete.dataset.allowed_registration_id = event.currentTarget.dataset.allowed_registration_id;
                $(AllowedRegistrationUI.idModalAllowedRegistrationDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (allowedRegistration) {
        AllowedRegistrationUI.id.value = allowedRegistration.id;
        AllowedRegistrationUI.emailUpdate.value = allowedRegistration.email;
    };

    $(AllowedRegistrationUI.idFormAllowedRegistrationUpdate).validate({
        rules: {
            email: "required"
        },
        messages: {
            email: "Email required."
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements["id"].value,
                "email": form.querySelector("input[name='email']").value
            };

            AllowedRegistrationController.updateAllowedRegistration(form, requestBody, function () {
                AllowedRegistrationController.listAllowedRegistration(AllowedRegistrationUI.idTableAllowedRegistration);
            });
        }
    });

    //Import Allowed Registration
    AllowedRegistrationUI.idBtnPopImportAllowedRegistration.addEventListener('click', function (event) {
        $(AllowedRegistrationUI.idFormAllowedRegistrationAdd).hide();
        $(AllowedRegistrationUI.idFormAllowedRegistrationUpdate).hide();
        $(AllowedRegistrationUI.idFormAllowedRegistrationImport).show();
        AllowedRegistrationUI.modalAllowedRegistrationTitle.textContent = "Import Allowed Registration Data";
        $(AllowedRegistrationUI.idModalAllowedRegistration).modal("show");
    });

    $(AllowedRegistrationUI.idFormAllowedRegistrationImport).validate({
        rules: {
            excelFile: "required"
        },
        messages: {
            excelFile: "Please select an excel file to import data."
        },
        submitHandler: function (form) {

            let requestBody = new FormData();
            let fileField = form.querySelector("input[type='file']");
            requestBody.append('excelFile', fileField.files[0]);

            AllowedRegistrationController.importData(form, requestBody, function () {
                AllowedRegistrationController.listAllowedRegistration(AllowedRegistrationUI.idTableAllowedRegistration);
            });
        }
    });

    //Delete
    AllowedRegistrationUI.idBtnAllowedRegistrationDelete.addEventListener("click", function (event) {
        AllowedRegistrationController.deleteAllowedRegistration(AllowedRegistrationUI.idModalAllowedRegistrationDelete, event.currentTarget.dataset.allowed_registration_id, function () {
            AllowedRegistrationController.listAllowedRegistration(AllowedRegistrationUI.idTableAllowedRegistration);
        });
    });

    $(AllowedRegistrationUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(AllowedRegistrationUI.idModalAllowedRegistration).modal("hide");
        $(AllowedRegistrationUI.idFormAllowedRegistrationAdd).trigger("reset");
        $(AllowedRegistrationUI.idFormAllowedRegistrationUpdate).trigger("reset");
        $(AllowedRegistrationUI.idModalAllowedRegistrationDelete).modal("hide");
        $(AllowedRegistrationUI.modalAlertBody).html("");
    });

    AllowedRegistrationUI.idBtnAllowedRegistrationTemplate.addEventListener('click', function (event) {
        AllowedRegistrationController.exportAllowedRegistrationTemplate(AllowedRegistrationUI.idBtnAllowedRegistrationTemplate, EndPoints.ALLOWED_REGISTRATION_TEMPLATE);
    });
});