import * as EndPoints from "./EndPoints.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as CallApi from "../util/CallApi.js";

export let listErrorMessage = function (elemTable) {
    $(elemTable).DataTable({
        ajax: {
            url: EndPoints.ERROR_MESSAGE,
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
                            code: item.code,
                            english: item.english,
                            swedish: item.swedish || "",
                            spanish: item.spanish || "",
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
            {"data": "code"},
            {"data": "english"},
            {"data": "swedish"},
            {"data": "spanish"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("MULTI_LANG_MESSAGE_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "errorMessageEdit btn color-orange");
                        spanEdit.setAttribute("data-error_message", JSON.stringify(data));
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

export let updateErrorMessage = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ERROR_MESSAGE, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};