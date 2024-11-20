'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";

export let listUser = function (tableElem, api) {
    $(tableElem).DataTable({
        dom:'lBfrtip',
        buttons: [
            {
                extend: 'csv',
                className: 'btn btn-primary',
                text: '<i class="fas fa-file-csv"></i> CSV',
                exportOptions: {
                    columns: ':visible'
                }
            },
            {
                extend: 'print',
                text: '<i class="fas fa-print"></i> Print',
                exportOptions: {
                    columns: ':visible'
                }
            },
        ],
        ajax: {
            url: api ? api : EndPoints.USER,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(tableElem);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                d.searchTerm = d.search.value;
                delete d.columns;
                delete d.order;
                delete d.search;
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
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
                            name: item.firstName + " " + item.lastName,
                            mobileNumber: item.mobileNumber,
                            emailAddress: item.emailAddress,
                            role: item.role.title,
                            registeredDateTime: DateTimeUtil.prepareDateTimeForText(parseInt(item.createdDateTime)),
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
                LoaderUtil.hideLoader(tableElem);
            }
        },

        columns: [
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            {"data": "name"},
            {"data": "mobileNumber"},
            {"data": "emailAddress"},
            {"data": "role"},
            {
                "data": "registeredDateTime",
                "orderable": false,
                "searchable": false
            },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("USER_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "userEdit btn color-orange");
                        spanEdit.setAttribute("data-user", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "userShow btn color-green");
                    spanShow.setAttribute("data-user", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("Answer (Delete)")) {
                        let spanAnswerDelete = document.createElement("span");
                        spanAnswerDelete.setAttribute("title", "Delete Answers");
                        spanAnswerDelete.setAttribute("class", "answerDelete btn color-red");
                        spanAnswerDelete.setAttribute("data-user_id", data.id);
                        let iAnswerDelete = document.createElement("i");
                        iAnswerDelete.setAttribute("class", "fas fa-redo");
                        spanAnswerDelete.appendChild(iAnswerDelete);
                        returnValue += spanAnswerDelete.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("USER_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "userDelete btn color-red");
                        spanDelete.setAttribute("data-user_id", data.id);
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

export let filterByRole = function (tableElem, roleId) {
    listUser(tableElem, EndPoints.USER_BY_ROLE + roleId);
};

export let updateUser = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.USER, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addUser = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.USER, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let deleteUser = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.USER + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let changePassword = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.USER_CHANGE_PASSWORD, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteAnswer = async function (elem, userId = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ANSWER_BY_USER + "/" + userId, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let getUserWithLimitedFieldData = async function (elem, pageNumber, pageSize, callback) {
    CallApi.callBackend(elem, EndPoints.USER_FIELDS + "pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&", "GET")
        .then(response => {
            //AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};