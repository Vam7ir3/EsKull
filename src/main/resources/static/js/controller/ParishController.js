'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listParish = function (tableElem, api, municipalityId = null) {
    const endpoint = municipalityId ? EndPoints.PARISH + "/filter" + municipalityId : (api ? api : EndPoints.PARISH);

    $(tableElem).DataTable({
        serverSide: true,
        destroy: true,
        searching: true,
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
            $.ajax({
                url: endpoint,
                type: "GET",
                data: params,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (responseData) {
                    console.log("PARISH response:", responseData);

                    if(!responseData.data || !Array.isArray(responseData.data.list)){
                        responseData = {
                            data: {
                                list: Array.isArray(responseData) ? responseData : [],
                                totalRecord: Array.isArray(responseData) ? responseData.length : 0
                            }
                        }
                    }

                    let processData = responseData.data.list
                        .filter(item => {
                            return !searchTerm ||
                                item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                (item.municipalityRes && item.municipalityRes.name.toLowerCase().includes(searchTerm.toLowerCase()));
                        })
                        .map((item, index) => ({
                            sn: index + 1,
                            name: item.name,
                            registerDate: item.registerDate,
                            dividedOtherCounty: item.dividedOtherCounty,
                            municipalityId: item.municipalityRes ? item.municipalityRes.id : "N/A",
                            countyId: item.countyRes ? item.countyRes.id : "N/A",
                            action: item
                        }))

                    let result = {
                        data:processData,
                        recordsTotal: responseData.data.totalRecord || processData.length,
                        recordsFiltered: responseData.totalRecord || 0
                    };

                    console.log('Processed Data:', processData);
                    callback(result);
                },
                error: function (xhr, error, code) {
                    console.error("PARISH endpoint error:", xhr.responseText);
                    AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                },
                complete: function () {
                    LoaderUtil.hideLoader(tableElem);
                }
            });
        },
        columns: [
            {"data": "sn", "orderable": false, "searchable": false},
            {"data": "name"},
            {"data": "registerDate"},
            {"data": "dividedOtherCounty"},
            {"data": "municipalityId"},
            {"data": "countyId"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("PARISH_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "parishEdit btn color-orange");

                        spanEdit.setAttribute("data-parish", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "parishShow btn color-green");
                    spanShow.setAttribute("data-parish", JSON.stringify(data));

                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("PARISH_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "parishDelete btn color-red");
                        spanDelete.setAttribute("data-parish_id", data.id);
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


export let filterByMunicipality = function (tableElem, municipalityIds) {
    let api = EndPoints.PARISH + "/filter?";
    if (municipalityIds && municipalityIds.length > 0) {
        api += municipalityIds.map(id => `municipalityId=${encodeURIComponent(id)}`).join('&');
    }
    listParish(tableElem, api);
};

const validateParishData = (data) => {
    const errors = [];

    if (!data.name || typeof data.name !== 'string' || data.name.trim() === '') {
        errors.push('Name is required');
    }

    if (!data.registerDate) {
        errors.push('Register date is required');
    }

    if (data.municipalityId && !Number.isInteger(parseInt(data.municipalityId))) {
        errors.push('Municipality ID must be a valid integer if provided');
    }

    if (data.countyId && !Number.isInteger(parseInt(data.countyId))) {
        errors.push('County ID must be a valid integer if provided');
    }

    return errors;
};

export let addParish = async function (elem, requestBody = {}, callback) {
    try {
        const validationErrors = validateParishData(requestBody);
        if (validationErrors.length > 0) {
            AlertMessageUtil.alertMessage({
                success: false,
                message: `Validation failed: ${validationErrors.join(', ')}`
            });
            return;
        }

        const formattedData = {
            name: requestBody.name,
            registerDate: requestBody.registerDate,
            dividedOtherCounty: requestBody.dividedOtherCounty || '',
            ...(requestBody.municipalityId && { municipalityId: parseInt(requestBody.municipalityId) }),
            ...(requestBody.countyId && { countyId: parseInt(requestBody.countyId) })
        };

        LoaderUtil.showLoader(elem);

        const response = await CallApi.callBackend(elem, EndPoints.PARISH, "POST", formattedData);

        if (response.success) {
            AlertMessageUtil.alertMessage({
                success: true,
                message: 'Parish added successfully'
            });

            if (callback && typeof callback === 'function') {
                callback(response);
            }
        } else {
            AlertMessageUtil.alertMessage({
                success: false,
                message: response.message || 'Failed to add parish'
            });
        }
    } catch (error) {
        console.error('Error adding parish:', error);
        AlertMessageUtil.alertMessage({
            success: false,
            message: error.message || 'An unexpected error occurred'
        });
    } finally {
        LoaderUtil.hideLoader(elem);
    }
};


// export let addParish = async function (elem, requestBody = {}, callback) {
//     CallApi.callBackend(elem, EndPoints.PARISH, "POST", requestBody)
//         .then(response => {
//             AlertMessageUtil.alertMessage(response);
//             if (callback && typeof callback === 'function') {
//                 callback(response);
//             }
//         });
// };

export let deleteParish = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.PARISH + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let updateParish = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.PARISH, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let exportParish = function (elem, api) {
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
            a.download = "parish_export.xlsx"; // Set the desired filename

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

export let importParish = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.PARISH_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });

}

export let exportParishTemplate = function (elem, api) {
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
            a.download = "parish_template.xlsx"; // Set the desired filename

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