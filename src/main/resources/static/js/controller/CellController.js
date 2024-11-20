'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listCell = function (tableElem, api, personId = null) {
    const endpoint = personId ? EndPoints.PERSON_CELL_FILTER + personId : (api ? api : EndPoints.PERSON_CELL);

    $(tableElem).DataTable({
        destroy: true,
        searching: true,
        ajax: function (data, callback, settings) {
            let searchTerm = data.search.value; // Capture the search term

            $.ajax({
                url: endpoint,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (cellData) {
                    console.log("Cell response:", cellData);

                    // Adapt to the new response format
                    let adaptedCellData = {
                        data: {
                            list: Array.isArray(cellData) ? cellData : []
                        },
                        totalRecord: Array.isArray(cellData) ? cellData.length : 0
                    };

                    // Proceed with fetching PERSON_Cell data
                    $.ajax({
                        url: EndPoints.PERSON_CELL,
                        type: "GET",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                        },
                        success: function (personCellData) {
                            console.log("PERSON_Cell response:", personCellData);

                            if (!Array.isArray(personCellData)) {
                                console.error("Unexpected PERSON_Cell response format:", personCellData);
                                AlertMessageUtil.alertMessage({ message: "Invalid data format received from PERSON_Cell endpoint." });
                                return;
                            }

                            let cellList = adaptedCellData.data.list;
                            let personCellList = personCellData;

                            let mergedData = cellList
                                .filter(cell => {
                                    let relatedPersonCell = personCellList.find(ps => ps.cellRes && ps.cellRes.id === cell.id);
                                    return !searchTerm || (relatedPersonCell && relatedPersonCell.cellRes.name.toLowerCase().includes(searchTerm.toLowerCase()));
                                })
                                .map((cell, index) => {
                                    let relatedPersonCell = personCellList.find(ps => ps.cellRes && ps.cellRes.id === cell.id);
                                    return {
                                        sn: index + 1,
                                        personName: relatedPersonCell ? `${relatedPersonCell.personRes.pnr}` : 'N/A',
                                        cellName: relatedPersonCell && relatedPersonCell.cellRes ? relatedPersonCell.cellRes.name : 'N/A',
                                        action: { ...cell, personRes: relatedPersonCell ? relatedPersonCell.personRes : null }
                                    };
                                });

                            let result = {
                                data: mergedData,
                                recordsTotal: adaptedCellData.totalRecord,
                                recordsFiltered: mergedData.length // Update filtered records count
                            };

                            console.log('Merged Data:', mergedData);
                            callback(result);
                        },
                        error: function (xhr, error, code) {
                            console.error("PERSON_Cell endpoint error:", xhr.responseText);
                            AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                        }
                    });
                },
                error: function (xhr, error, code) {
                    console.error("Cell endpoint error:", xhr.responseText);
                    AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                },
                complete: function () {
                    LoaderUtil.hideLoader(tableElem);
                }
            });
        },
        columns: [
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            { "data": "personName" }, // Column for personName
            { "data": "cellName" }, // Column for cellName
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";
                    if (CommonUtil.hasAuthority("CELL_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "cellEdit btn color-orange");
                        spanEdit.setAttribute("data-cell", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "cellShow btn color-green");
                    spanShow.setAttribute("data-cell", JSON.stringify(data)); // Ensure all data is included here
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("CELL_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "cellDelete btn color-red");
                        spanDelete.setAttribute("data-cell_id", data.id);
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


export let filterByPerson = function (tableElem, personIds) {
    let api = EndPoints.PERSON_CELL + "/filter?";
    if (personIds && personIds.length > 0) {
        api += personIds.map(id => `personId=${encodeURIComponent(id)}`).join('&');
    }
    listCell(tableElem, api);
};

export let addCell = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.CELL, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteCell = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.CELL + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback == 'function') {
                callback();
            }
        });
};

export let updateCell = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.CELL, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};
export let exportCell = function(elem,api){
    LoaderUtil.showLoader(elem);

    let header = {};
    if(localStorage.getItem("token")){
        header.Authorization = localStorage.getItem("token");
        header.timeZoneOffsetInMinute = new Date().getTimezoneOffset();
    }
    let options = {
        method:"GET",
        headers:header
    };
    fetch(api,options)
        .then(function(response){

            if(!response.ok){
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(function(blob){
            let fileURL = URL.createObjectURL(blob);
            let a = document.createElement("a");
            a.href = fileURL;
            a.download = "Cell_export.xlsx"; // Set the desired filename

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

export let importCell = async function (elem,requestBody = {} ,callback){
    CallApi.uploadFile(elem,EndPoints.CELL_IMPORT,requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
}

export let exportCellTemplate = function (elem, api) {
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
            a.download = "Cell_template.xlsx"; // Set the desired filename

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