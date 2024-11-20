'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listPerson = function (tableElem, api, validPnrFilter = null) {
    let dataTable = $(tableElem).DataTable({
        serverSide: true,
        destroy: true,
        searching: true,
        stripeClasses: [],
        rowCallback: function(row, data, displayNum) {
            if (data.isByYear) {
                $(row).css('background-color', 'yellow');
            } else {
                $(row).css('background-color', '');
            }
        },
        drawCallback: function(settings) {
            const api = this.api();
            api.rows().every(function() {
                const data = this.data();
                const node = this.node();
                if (data.isByYear) {
                    $(node).css('background-color', 'yellow');
                }
            });
        },
        ajax: function (data, callback, settings) {
            let params = {
                pageNumber: Math.floor(data.start / data.length),
                pageSize: data.length,
                searchTerm: data.search.value
            };

            if (data.order && data.order.length > 0) {
                const columnIndex = data.order[0].column;
                const columnName = data.columns[columnIndex].data;
                params.sortBy = columnName;
                params.sortOrder = data.order[0].dir;
            }
            let apiUrl = api ? api : EndPoints.PERSON;
            if (validPnrFilter !== null) {
                apiUrl += (apiUrl.includes('?') ? '&' : '?') + "isValidPnr=" + validPnrFilter;
            }
            $.ajax({
                url: apiUrl,
                type: "GET",
                data: params,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (responseData) {
                    console.log("PERSON response:", responseData);

                    // let adaptedPersonData = {
                    //     data: {
                    //         list: Array.isArray(responseData.data) ? responseData.data : []
                    //     },
                    //     totalRecord : Array.isArray(responseData.totalRecord) ? responseData.totalRecord.length : 0
                    // };

                    let result = {
                        data: (responseData.data.list || []).map((item, index) => ({
                            sn: data.start + index + 1,
                            pnr: item.pnr,
                            dateOfBirth: item.dateOfBirth,
                            isValidPNR: item.isValidPNR,
                            isByYear: item.isByYear,
                            action: item
                        })),
                        recordsTotal: responseData.totalRecord || 0,
                        recordsFiltered: responseData.totalRecord || 0
                    };

                    console.log('Processed Data:', result);
                    callback(result);
                },
                error: function (xhr, error, code) {
                    console.error("PERSON endpoint error:", xhr.responseText);
                    AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                },
                complete: function () {
                    LoaderUtil.hideLoader(tableElem);
                }
            });
        },
        columns: [
            {"data": "sn", "orderable": false, "searchable": false},
            {"data": "pnr"},
            {"data": "dateOfBirth"},
            {"data": "isValidPNR"},
            {"data": "isByYear"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("PERSON_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "personEdit btn color-orange");
                        spanEdit.setAttribute("data-person", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "personShow btn color-green");
                    spanShow.setAttribute("data-person", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("PERSON_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "personDelete btn color-red");
                        spanDelete.setAttribute("data-person_id", data.id);
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
    dataTable.on('draw.dt', function() {
        $(tableElem).find('tr').each(function() {
            const rowData = dataTable.row(this).data();
            if (rowData && rowData.isByYear) {
                $(this).css('background-color', 'yellow');
            }
        });
    });
};


export let filterByValidPnr = function (tableElem, isValidPNR) {
    let api = EndPoints.PERSON_FILTER;

    if (isValidPNR !== null && isValidPNR !== undefined) {
        if (typeof isValidPNR === 'boolean') {
            api += `?isValidPNR=${isValidPNR}`;
        } else if (isValidPNR === 'true' || isValidPNR === 'false') {
            api += `?isValidPNR=${isValidPNR === 'true'}`;
        }
    }

    listPerson(tableElem, api);
};



export let addPerson = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.PERSON, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deletePerson = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.PERSON + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let updatePerson = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.PERSON, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let exportPerson = function (elem, api) {
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
            a.download = "person_export.xlsx"; // Set the desired filename

            // Programmatically click the anchor element to trigger the download
            a.click();

            // Cleanup: Revoke the URL to release resources
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

export let importPerson = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.PERSON_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });

}

export let exportPersonTemplate = function (elem, api) {
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
            // Check if the response is successful
            console.log(response)
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(function (blob) {
            // Create a URL for the blob
            console.log(blob)
            let fileURL = URL.createObjectURL(blob);

            // Create an anchor element and set the URL as its href
            let a = document.createElement("a");
            a.href = fileURL;
            a.download = "person_template.xlsx"; // Set the desired filename

            // Programmatically click the anchor element to trigger the download
            a.click();

            // Cleanup: Revoke the URL to release resources
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