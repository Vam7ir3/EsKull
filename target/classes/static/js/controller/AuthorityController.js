'use strict';

import * as EndPoints from "./EndPoints.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";

export let listAuthority = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        ajax: {
            url: EndPoints.AUTHORITY,
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
                            title: item.description,
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
            }/*,
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    if (CommonUtil.hasAuthority("Authority (Update)")) {
                        return "<span class='authorityEdit btn color-orange' data-authority='" + JSON.stringify(data) + "'><i class='fas fa-edit'></i></span>";
                    } else {
                        return "";
                    }
                }
            }*/
        ]
    });
};

export let updateAuthority = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.AUTHORITY, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addAuthority = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.AUTHORITY, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};