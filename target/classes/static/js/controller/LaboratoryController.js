'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listLaboratory = function (tableElem, api, isInUseFilter = null) {
    $(tableElem).DataTable({
        destroy: true,
        searching: true,
        ajax: function (data, callback, settings) {
            let searchTerm = data.search.value; // Capture the search term

            let apiUrl = api ? api : EndPoints.LABORATORY;
            if (isInUseFilter != null) {
                apiUrl += "?isInUse=" + isInUseFilter;
            }

            $.ajax({
                url: apiUrl,
                type: "GET",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (responseData) {
                    console.log("LABORATORY response:", responseData);

                    // Adapt the response data format
                    let parsedData = {
                        data: responseData.data.list || [],
                        totalRecord: responseData.totalRecord || 0
                    };

                    let filteredData = parsedData.data.filter(item =>
                        (!searchTerm || item.name.toLowerCase().includes(searchTerm.toLowerCase())) &&
                        (isInUseFilter === null || item.isInUse === isInUseFilter)
                    );

                    // Prepare the data for DataTable
                    let result = {
                        data: filteredData.map((item, index) => ({
                            sn: index + 1,
                            name: item.name,
                            isInUse: item.isInUse,
                            sosLab: item.sosLab,
                            sosLabName: item.sosLabName,
                            sosLongName: item.sosLongName,
                            region: item.region,
                            action: item // Preserve the action data as is
                        })),
                        recordsTotal: parsedData.totalRecord,
                        recordsFiltered: filteredData.length // Adjust if necessary
                    };

                    console.log('Processed Data:', result);
                    callback(result);
                },
                error: function (xhr, error, code) {
                    console.error("LABORATORY endpoint error:", xhr.responseText);
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
            { "data": "name" },
            { "data": "isInUse" },
            { "data": "sosLab" },
            { "data": "sosLabName" },
            { "data": "sosLongName" },
            { "data": "region" },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("LABORATORY_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "laboratoryEdit btn color-orange");
                        spanEdit.setAttribute("data-laboratory", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "laboratoryShow btn color-green");
                    spanShow.setAttribute("data-laboratory", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("LABORATORY_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "laboratoryDelete btn color-red");
                        spanDelete.setAttribute("data-laboratory_id", data.id);
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

export let filterByIsInUse = function (tableElem, isInUse) {
    listLaboratory(tableElem, EndPoints.LABORATORY, isInUse);
};



export let addLaboratory = async function(elem, requestBody = {} , callback) {
    CallApi.callBackend(elem, EndPoints.LABORATORY, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if(callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteLaboratory = async function(elem, id = {}, callback){
    CallApi.callBackend(elem, EndPoints.LABORATORY+ "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let updateLaboratory = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.LABORATORY, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if(callback && typeof callback === 'function'){
                callback(response);
            }
        });
};
export let exportLaboratory = function(elem,api){
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
            a.download = "laboratory_export.xlsx"; // Set the desired filename

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

export let importLaboratory = async function (elem,requestBody = {} ,callback){
    CallApi.uploadFile(elem,EndPoints.LABORATORY_IMPORT,requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });

}

export let exportLaboratoryTemplate = function (elem, api) {
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
            a.download = "laboratory_template.xlsx"; // Set the desired filename

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

