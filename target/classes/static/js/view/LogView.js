'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as LogUI from "../ui/LogUI.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as LogController from "../controller/LogController.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(
        Endpoints.USER,
        "emailAddress",
        LogUI.idSelectUser,
    );


    LogUI.idSelectUser.addEventListener('change', function (event) {
        LogController.filterByEmail(LogUI.idTableLog, $(LogUI.idSelectUser).val());
    });

    LogController.listLog(LogUI.idTableLog);

    $(LogUI.idTableLog).on('draw.dt', function () {
        // Show event Listener
        let shows = LogUI.idTableLog.querySelectorAll(".logShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let log = event.currentTarget.dataset.log;
                putValueInShow(JSON.parse(log));
                $(LogUI.idLogShow).show();
                LogUI.modalLogTitle.textContent = "Log Detail";
                $(LogUI.idModalLog).modal("show");
            });
        });
    });


    let putValueInShow = function (log) {
        let user = log.userId;
        if (LogUI.idDescriptionShow) {
            LogUI.idDescriptionShow.textContent = log.description;
        } else {
            console.error("Element idDescriptionShow not found");
        }

        if (LogUI.idOperationShow) {
            LogUI.idOperationShow.textContent = log.operation;
        } else {
            console.error("Element idOperationShow not found");
        }

        if (LogUI.idFirstNameShow) {
            LogUI.idFirstNameShow.textContent = user.firstName;
        } else {
            console.error("Element idFirstNameShow not found");
        }

        if (LogUI.idLastNameShow) {
            LogUI.idLastNameShow.textContent = user.lastName;
        } else {
            console.error("Element idLastNameShow not found");
        }

        if (LogUI.idEmailAddressShow) {
            LogUI.idEmailAddressShow.textContent = user.emailAddress;
        } else {
            console.error("Element idEmailAddressShow not found");
        }

        if (LogUI.idTimeStampShow) {
            LogUI.idTimeStampShow.textContent = log.timestamp;
        } else {
            console.error("Element idTimeStampShow not found");
        }
    };

    LogUI.idLogExport.addEventListener('click', function () {
        LogController.exportLog(LogUI.idLogExport, Endpoints.LOG_EXPORT);
    })
});