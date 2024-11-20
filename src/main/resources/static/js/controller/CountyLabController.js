'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as CountyLabUI from "../ui/CountyLabUI.js";


export let listCountyLab = function (tableElem, api) {
    $(tableElem).DataTable({
        destroy: true,
        searching: true,
        ajax: function (data, callback, settings) {
            let params = {
                pageNumber: Math.floor(data.start / data.length),
                pageSize: data.length,
                searchTerm: data.search.value
            };

            // Add sorting parameters if present
            if (data.order && data.order.length > 0) {
                const columnIndex = data.order[0].column;
                const columnName = data.columns[columnIndex].data;
                params.sortBy = columnName;
                params.sortOrder = data.order[0].dir;
            }

            let apiUrl = api ? api : EndPoints.COUNTYLAB;
            const selectedCountyId = localStorage.getItem("selectedCountyId");
            console.log(selectedCountyId);

            $.ajax({
                url: apiUrl,
                type: "GET",
                data: params,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (responseData) {
                    console.log("API Response:", responseData);

                    // Ensure responseData is wrapped in an array if it's not
                    const dataArray = Array.isArray(responseData) ? responseData : [responseData];

                    // Filter data based on selected county
                    const filteredData = dataArray.filter(item =>
                        item.countyRes && item.countyRes.id &&
                        item.countyRes.id.toString() === selectedCountyId.toString()
                    );

                    const result = {
                        data: filteredData.map((item, index) => ({
                            sn: index + 1,
                            county: item.countyRes ? item.countyRes.name : '',
                            lab: item.labRes ? item.labRes.name : '',
                            isInUse: item.labRes && item.labRes.isInUse ? 'Yes' : 'No',
                            action: item
                        })),
                        recordsTotal: filteredData.length,
                        recordsFiltered: filteredData.length
                    };
                    callback(result);
                },
                error: function (xhr, error, code) {
                    console.error("COUNTY endpoint error:", xhr.responseText);
                    AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                    callback({
                        data: [],
                        recordsTotal: 0,
                        recordsFiltered: 0
                    });
                },
                complete: function () {
                    LoaderUtil.hideLoader(tableElem);
                }
            });
        },
        columns: [
            { "data": "sn", "orderable": false, "searchable": false },
            { "data": "county" },
            { "data": "lab" },
            { "data": "isInUse" },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("COUNTYLAB_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "countyLabEdit btn color-orange");

                        spanEdit.setAttribute("data-county_lab", JSON.stringify({
                            county: data.countyRes ? data.countyRes.name : '',
                            lab: data.labRes ? data.labRes.name : '',
                            isInUse: data.labRes ? data.labRes.isInUse: false,
                            is:data.id
                        }));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "showCountyLab btn color-green");
                    spanShow.setAttribute("data-user", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("COUNTYLAB_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "countyLabDelete btn color-red");
                        spanDelete.setAttribute("data-county_b_id", data.id);
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


export let addCountyLab = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.COUNTYLAB, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteCountyLab = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.COUNTYLABv + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let updateCountyLab = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.COUNTYLAB, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};
export let exportCounty = function (elem, api) {
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
            a.download = "County_export.xlsx"; // Set the desired filename

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

export let importCounty = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.COUNTY_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });

}

export let exportCountyTemplate = function (elem, api) {
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
            a.download = "County_template.xlsx"; // Set the desired filename

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
            }
        );
};