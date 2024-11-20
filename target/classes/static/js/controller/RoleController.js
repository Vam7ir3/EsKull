'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listRole = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        ajax: {
            url: EndPoints.ROLE,
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
                            title: item.title,
                            authorities: item.authorities,
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
            {
                "data": "title"
            },
            {
                "data": "authorities",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let authorities = "";
                    if (data) {
                        data.forEach(function (item) {
                            authorities += item.title + ", ";
                        });
                        authorities = authorities.substr(0, authorities.length - 2);
                    }
                    return authorities;
                }
            },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("ROLE_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "roleEdit btn color-orange");
                        spanEdit.setAttribute("data-role", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let updateRole = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ROLE, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addRole = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ROLE, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};