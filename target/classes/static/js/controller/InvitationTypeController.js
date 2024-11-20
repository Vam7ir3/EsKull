'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listInvitationType = function (tableElem, api) {
    $(tableElem).DataTable({
        serverSide: true,
        destroy: true,
        searching: true,
        processing: true,
        ajax: function (data, callback, settings) {
            let searchTerm = data.search.value;

            let params = {
                pageNumber: Math.floor(data.start / data.length),
                pageSize: data.length,
            };

            if (data.order && data.order.length > 0) {
                const columnIndex = data.order[0].column;
                const columnName = data.columns[columnIndex].data;
                params.sortBy = columnName;
                params.sortOrder = data.order[0].dir;
            }
            let apiUrl = api ? api : EndPoints.INVITATIONTYPE;

            $.ajax({
                url: apiUrl,
                type: "GET",
                data: params,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (responseData) {
                    console.log("InvitationType response:", responseData);

                    let result = {
                        data: (responseData.data.list || [])
                            .filter(item => {
                                let invitationTypeSearch = responseData.data.list.find(ps => ps.type === item.type);
                                return !searchTerm || invitationTypeSearch.type.toLowerCase().includes(searchTerm.toLowerCase());
                            })
                            .map((item, index) => ({
                                sn: data.start + index + 1,
                                type: item.type,
                                xtype: item.xtype,
                                description: item.description,
                                action: item
                            })),
                        recordsTotal: responseData.totalRecord || 0,
                        recordsFiltered: responseData.totalRecord || 0
                    };

                    console.log('Processed Data:', result);
                    callback(result);
                },
                error: function (xhr, error, code) {
                    console.error("InvitationType endpoint error:", xhr.responseText);
                    AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                },
                complete: function () {
                    LoaderUtil.hideLoader(tableElem);
                }
            });
        },
        columns: [
            {"data": "sn", "orderable": false, "searchable": false},
            {"data": "type", "searchable": true},
            {"data": "xtype"},
            {"data": "description"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("INVITATIONTYPE_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "invitationTypeEdit btn color-orange");

                        spanEdit.setAttribute("data-invitation-type", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "invitationTypeShow btn color-green");

                    spanShow.setAttribute("data-invitation-type", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("INVITATIONTYPE_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "invitationTypeDelete btn color-red");

                        spanDelete.setAttribute("data-invitation-type_id", data.id);
                        let iDelete = document.createElement("i");
                        iDelete.setAttribute("class", "fas fa-trash");
                        spanDelete.appendChild(iDelete);
                        returnValue += spanDelete.outerHTML;
                    }
                    return returnValue;
                }
            }
        ],
    });
};


export let addInvitationType = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.INVITATIONTYPE, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteInvitationType = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.INVITATIONTYPE + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let updateInvitationType = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.INVITATIONTYPE, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        }).catch(error => {
        AlertMessageUtil.alertMessage({
            success: false,
            message: "Failed to update InvitationType"
        });
        console.error("Update error:", error);
    });
};

export let exportInvitationType = function (elem, api) {
    LoaderUtil.showLoader(elem);

    let header = {};
    if (localStorage.getItem("token")) {
        header.Authorization = localStorage.getItem("token");
        header.timeZoneOffsetInMinute = new Date().getTimezoneOffset();
    }
    let options = {
        method: "GET",
        headers: header
    };
    fetch(api, options)
        .then(function (response) {

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(function (blob) {
            let fileURL = URL.createObjectURL(blob);
            let a = document.createElement("a");
            a.href = fileURL;
            a.download = "InvitationType_export.xlsx";

            a.click();

            URL.revokeObjectURL(fileURL);
        })
        .catch((error) => {
            console.log('Error: ', error);
            AlertMessageUtil.alertMessage(error);
        })
        .finally(function () {
            LoaderUtil.hideLoader(elem);
        });
}

export let importInvitationType = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.INVITATIONTYPE_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });

}

export let exportInvitationTypeTemplate = function (elem, api) {
    LoaderUtil.showLoader(elem);
    let headers = {};
    if (localStorage.getItem("token")) {
        headers.Authorization = localStorage.getItem("token");
    }

    let options = {
        method: "GET",
        headers: headers
    };

    fetch(api, options)
        .then(function (response) {
            console.log(response)
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(function (blob) {
            console.log(blob)
            let fileURL = URL.createObjectURL(blob);

            let a = document.createElement("a");
            a.href = fileURL;
            a.download = "InvitationType_template.xlsx";

            a.click();

            URL.revokeObjectURL(fileURL);
        })
        .catch((error) => {
            console.log('Error: ', error);
            AlertMessageUtil.alertMessage(error);
        })
        .finally(function () {
            LoaderUtil.hideLoader(elem);
        });
};