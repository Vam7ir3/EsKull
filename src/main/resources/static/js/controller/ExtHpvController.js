'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listExtHpv = function (tableElem, api, personId = null) {
    const endpoint = personId ? EndPoints.PERSON_EXTHPV_FILTER + personId : (api ? api : EndPoints.PERSON_EXTHPV);

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
                success: function (extHpvData) {
                    console.log("ExtHpv response:", extHpvData);

                    if (!Array.isArray(extHpvData)) {
                        console.error("ExtHpvData is not in expected array format:", extHpvData);
                        AlertMessageUtil.alertMessage({ message: "Unexpected data format from ExtHPV endpoint." });
                        return;
                    }

                    // Adapt to the new response format
                    let adaptedExtHPVData = {
                        data: {
                            list: Array.isArray(extHpvData) ? extHpvData : []
                        },
                        totalRecord: Array.isArray(extHpvData) ? extHpvData.length : 0
                    };

                    // Proceed with fetching PERSON_ExtHPV data
                    $.ajax({
                        url: EndPoints.PERSON_EXTHPV,
                        type: "GET",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                        },
                        success: function (personExtHPVData) {
                            console.log("PERSON_ExtHPV response:", personExtHPVData);

                            // Merge and process data
                            let extHpvList = adaptedExtHPVData.data.list;
                            let personExtHPVList = personExtHPVData;

                            // Filter the data based on the search term in ExtHPVRes.name
                            let mergedData = extHpvList
                                .filter(extHpv => {
                                    let relatedPersonExtHPV = personExtHPVList.find(ps => ps.extHpvRes && ps.extHpvRes.id === extHpv.id);
                                    return !searchTerm || (relatedPersonExtHPV && relatedPersonExtHPV.extHpvRes.name.toLowerCase().includes(searchTerm.toLowerCase()));
                                })
                                .map((extHpv, index) => {
                                    // Match the extHpv with the personExtHPV data
                                    let relatedPersonExtHPV = personExtHPVList.find(ps => ps.extHpvRes && ps.extHpvRes.id === extHpv.id);

                                    // Return the merged data object
                                    return {
                                        sn: index + 1,
                                        personName: relatedPersonExtHPV ? `${relatedPersonExtHPV.personRes.pnr}` : 'N/A',
                                        extHpvName: relatedPersonExtHPV && relatedPersonExtHPV.extHpvRes ? relatedPersonExtHPV.extHpvRes.name : 'N/A',
                                        action: { ...extHpv, personRes: relatedPersonExtHPV ? relatedPersonExtHPV.personRes : null }
                                    };
                                });

                            let result = {
                                data: mergedData,
                                recordsTotal: adaptedExtHPVData.totalRecord,
                                recordsFiltered: mergedData.length // Update filtered records count
                            };

                            console.log('Merged Data:', mergedData);
                            callback(result);
                        },
                        error: function (xhr, error, code) {
                            console.error("PERSON_ExtHPV endpoint error:", xhr.responseText);
                            AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                        }
                    });
                },
                error: function (xhr, error, code) {
                    console.error("ExtHPV endpoint error:", xhr.responseText);
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
            { "data": "extHpvName" }, // Column for ExtHPVName
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";
                    if (CommonUtil.hasAuthority("EXTHPV_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "extHpvEdit btn color-orange");
                        spanEdit.setAttribute("data-extHpv", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "extHpvShow btn color-green");
                    spanShow.setAttribute("data-extHpv", JSON.stringify(data)); // Ensure all data is included here
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("EXTHPV_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "extHpvDelete btn color-red");
                        spanDelete.setAttribute("data-extHpv_id", data.id);
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
    let api = EndPoints.PERSON_EXTHPV + "/filter?";
    if (personIds && personIds.length > 0) {
        api += personIds.map(id => `personId=${encodeURIComponent(id)}`).join('&');
    }
    listExtHpv(tableElem, api);
};

export let addExtHpv = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.EXTHPV, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteExtHpv = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.EXTHPV + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback == 'function') {
                callback();
            }
        });
};

export let updateExtHpv = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.EXTHPV, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};
export let exportExtHpv = function(elem,api){
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
            a.download = "ExtHPV_export.xlsx"; // Set the desired filename

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

export let importExtHpv = async function (elem,requestBody = {} ,callback){
    CallApi.uploadFile(elem,EndPoints.EXTHPV_IMPORT,requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
}

export let exportExtHpvTemplate = function (elem, api) {
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
            a.download = "ExtHPV_template.xlsx"; // Set the desired filename

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