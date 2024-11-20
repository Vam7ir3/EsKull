'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listHpv = function (tableElem, api, personId = null) {
    const endpoint = personId ? EndPoints.PERSON_HPV_FILTER + personId : (api ? api : EndPoints.PERSON_HPV);

    $(tableElem).DataTable({
        serverSide: true,
        destroy: true,
        searching: true,
        ajax: function (data, callback, settings) {
            let searchTerm = data.search.value;

            let params = {
                pageNumber: Math.floor(data.start/ data.length),
                pageSize: data.length,
            };

            if (data.order && data.order.length > 0) {
                const columnIndex = data.order[0].column;
                const columnName = data.columns[columnIndex].data;
                params.sortBy = columnName;
                params.sortOrder = data.order[0].dir;
            }
            $.ajax({
                url: endpoint,
                type: "GET",
                data: params,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (hpvData) {
                    console.log("Hpv response:", hpvData);

                    // Adapt to the new response format
                    let adaptedHpvData = {
                        data: {
                            list: Array.isArray(hpvData) ? hpvData : []
                        },
                        totalRecord: Array.isArray(hpvData) ? hpvData.length : 0
                    };

                    $.ajax({
                        url: EndPoints.PERSON_HPV,
                        type: "GET",
                        data: params,
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                        },
                        success: function (personHpvData) {
                            console.log("PERSON_Hpv response:", personHpvData);

                            if (!personHpvData.data || !Array.isArray(personHpvData.data.list)) {
                                console.error("Unexpected PERSON_Hpv response format:", personHpvData);
                                AlertMessageUtil.alertMessage({message: "Invalid data format received from PERSON_Hpv endpoint."});
                                return;
                            }

                            // Merge and process data
                            let hpvList = personHpvData.data.list;
                            let personHpvList = personHpvData.data.list;

                            // Filter the data based on the search term in HpvRes.name
                            let mergedData = hpvList
                                .filter(hpv => {
                                    let relatedPersonHpv = personHpvList.find(ps => ps.hpvRes && ps.hpvRes.id === hpv.id);
                                    return !searchTerm || (relatedPersonHpv && relatedPersonHpv.hpvRes.name.toLowerCase().includes(searchTerm.toLowerCase()));
                                })
                                .map((hpv, index) => {
                                    let relatedPersonHpv = personHpvList.find(ps => ps.hpvRes && ps.hpvRes.id === hpv.id);

                                    return {
                                        sn: index + 1,
                                        personName: relatedPersonHpv ? `${relatedPersonHpv.personRes.pnr} ` : 'N/A',
                                        hpvName: relatedPersonHpv && relatedPersonHpv.hpvRes ? relatedPersonHpv.hpvRes.name : 'N/A',
                                        action: { ...hpv, personRes: relatedPersonHpv ? relatedPersonHpv.personRes : null }
                                    };
                                });

                            let result = {
                                data: mergedData,
                                recordsTotal: personHpvData.totalRecord || 0,
                                recordsFiltered: personHpvData.totalRecord || 0
                            };

                            console.log('Merged Data:', mergedData);
                            callback(result);
                        },
                        error: function (xhr, error, code) {
                            console.error("PERSON_Hpv endpoint error:", xhr.responseText);
                            AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                        }
                    });
                },
                error: function (xhr, error, code) {
                    console.error("Hpv endpoint error:", xhr.responseText);
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
            { "data": "personName" },
            { "data": "hpvName" },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";
                    if (CommonUtil.hasAuthority("HPV_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "hpvEdit btn color-orange");
                        spanEdit.setAttribute("data-hpv", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "hpvShow btn color-green");
                    spanShow.setAttribute("data-hpv", JSON.stringify(data)); // Ensure all data is included here
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("HPV_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "hpvDelete btn color-red");
                        spanDelete.setAttribute("data-hpv_id", data.id);
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
    let api = EndPoints.PERSON_HPV + "/filter?";
    if (personIds && personIds.length > 0) {
        api += personIds.map(id => `personId=${encodeURIComponent(id)}`).join('&');
    }
    listHpv(tableElem, api);
};

export let addHpv = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.HPV, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteHpv = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.HPV + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback == 'function') {
                callback();
            }
        });
};

export let updateHpv = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.HPV, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};
export let exportHpv = function(elem,api){
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
            a.download = "hpv_export.xlsx"; // Set the desired filename

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

export let importHpv = async function (elem,requestBody = {} ,callback){
    CallApi.uploadFile(elem,EndPoints.HPV_IMPORT,requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
}

export let exportHpvTemplate = function (elem, api) {
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
            a.download = "Hpv_template.xlsx"; // Set the desired filename

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