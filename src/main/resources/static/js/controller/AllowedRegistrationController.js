import * as EndPoints from "./EndPoints.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";

export let listAllowedRegistration = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        ajax: {
            url: EndPoints.ALLOWED_REGISTRATION,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                delete d.columns;
                delete d.order;
                delete d.search;
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.list;
                let counter = parsedData.startPosition;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            sn: (counter++),
                            email: item.email,
                            createdDateTime: DateTimeUtil.prepareDateTimeForText(parseInt(item.createdDateTime)),
                            registeredDateTime: item.registeredDateTime ? DateTimeUtil.prepareDateTimeForText(parseInt(item.registeredDateTime)) : "",
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr,
                    "recordsTotal": parsedData.totalRecord,
                    "recordsFiltered": parsedData.totalRecord
                };
                return JSON.stringify(filteredData);
            },
            error: function (xhr, error, code) {
                AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
            },
            complete: function () {
                LoaderUtil.hideLoader(elemTable);
            }
        },

        columns: [
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            {"data": "email"},
            {"data": "createdDateTime"},
            {"data": "registeredDateTime"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("ALLOWED_REGISTRATION_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "allowedRegistrationEdit btn color-orange");
                        spanEdit.setAttribute("data-allowed_registration", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("ALLOWED_REGISTRATION_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "allowedRegistrationDelete btn color-red");
                        spanDelete.setAttribute("data-allowed_registration_id", data.id);
                        let iDelete = document.createElement("i");
                        iDelete.setAttribute("class", "fas fa-trash");
                        spanDelete.appendChild(iDelete);
                        returnValue += spanDelete.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let addAllowedRegistration = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ALLOWED_REGISTRATION, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let updateAllowedRegistration = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ALLOWED_REGISTRATION, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let importData = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.ALLOWED_REGISTRATION_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteAllowedRegistration = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ALLOWED_REGISTRATION + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let exportAllowedRegistrationTemplate = function (elem, api) {
    CallApi.downloadFile(elem, api);
};