'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as ErrorMessageUI from "../ui/ErrorMessageUI.js";
import * as ErrorMessageController from "../controller/ErrorMessageController.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    ErrorMessageController.listErrorMessage(ErrorMessageUI.idTableErrorMessage);
    $(ErrorMessageUI.idTableErrorMessage).on('draw.dt', function () {
        let errorMessageEdit = ErrorMessageUI.idTableErrorMessage.querySelectorAll(".errorMessageEdit");
        Array.from(errorMessageEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let error_message = event.currentTarget.dataset.error_message;
                putValueInEditForm(JSON.parse(error_message));
                $(ErrorMessageUI.idFormErrorMessageUpdate).show();
                ErrorMessageUI.modalErrorMessageTitle.textContent = "Edit Error Message";
                $(ErrorMessageUI.idModalErrorMessage).modal("show");
            });
        });
    });

    let putValueInEditForm = function (errorMessage) {
        ErrorMessageUI.id.value = errorMessage.id;
        ErrorMessageUI.codeUpdate.value = errorMessage.code;
        ErrorMessageUI.englishUpdate.value = errorMessage.english;
        if (errorMessage.swedish) {
            ErrorMessageUI.swedishUpdate.value = errorMessage.swedish;
        }
        if (errorMessage.spanish) {
            ErrorMessageUI.spanishUpdate.value = errorMessage.spanish;
        }
    };

    $(ErrorMessageUI.idFormErrorMessageUpdate).validate({
        rules: {},
        messages: {},
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements["id"].value,
            };
            if (form.elements["swedish"].value) {
                requestBody.swedish = form.elements["swedish"].value;
            }
            if (form.elements["spanish"].value) {
                requestBody.spanish = form.elements["spanish"].value;
            }

            ErrorMessageController.updateErrorMessage(form, requestBody, function () {
                ErrorMessageController.listErrorMessage(ErrorMessageUI.idTableErrorMessage);
            });
        }
    });

    $(ErrorMessageUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(ErrorMessageUI.idModalErrorMessage).modal("hide");
        $(ErrorMessageUI.idFormErrorMessageUpdate).trigger("reset");
        $(ErrorMessageUI.modalAlertBody).html("");
    });
});