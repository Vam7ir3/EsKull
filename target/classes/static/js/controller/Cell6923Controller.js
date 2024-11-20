'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";


export let listCell6923 = function (tableElem, api, personId = null) {
    const endpoint = personId ? EndPoints.CELL6923 + "/filter/" + personId : (api ? api : EndPoints.CELL6923);

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
            $.ajax({
                url: endpoint,
                type: "GET",
                data: params,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                    LoaderUtil.showLoader(tableElem);
                },
                success: function (cell6923Data) {
                    console.log("Cell6923 response:", cell6923Data);

                    if (!cell6923Data.data || !Array.isArray(cell6923Data.data.list)) {
                        console.error("Unexpected Cell6923 response format:", cell6923Data);
                        AlertMessageUtil.alertMessage({message: "Invalid data format received from Cell6923 endpoint."});
                        return;
                    }

                    let cell6923List = cell6923Data.data.list;

                    let mergedData = cell6923List
                        .filter(data => !searchTerm ||
                            (data.snomed && data.snomed.toLowerCase().includes(searchTerm.toLowerCase())) ||
                            (data.xSnomed && data.xSnomed.toLowerCase().includes(searchTerm.toLowerCase()))
                        )
                        .map((data, index) => {
                            console.log("Cell6923 Data:", data);
                            console.log("personRes data checking:", data.personRes)
                            return {
                                sn: index + 1,
                                personId: data.personRes ? data.personRes.id : 'N/A',
                                laboratoryId: data.laboratoryRes ? data.laboratoryRes.id : 'N/A',
                                countyId: data.countyRes ? data.countyRes.id : 'N/A',
                                sampleDate: data.sampleDate,
                                sampleType: data.sampleType,
                                referralNumber: data.referralNumber,
                                referenceTypeId: data.referenceTypeRes ? data.referenceTypeRes.id: 'N/A',
                                referenceSite: data.referenceSite,
                                residc: data.residc,
                                residk: data.residk,
                                xSampleDate: data.xsampleDate,
                                xRegistrationDate: data.registrationDate,
                                xSnomed: data.xsnomed,
                                diagId: data.diagId,
                                ansClinic: data.ansClinic,
                                debClinic: data.debClinic,
                                remClinic: data.remClinic,
                                registrationDate: data.registrationDate,
                                scrType: data.scrType,
                                snomed: data.snomed,
                                responseDate: data.responseDate,
                                xResponseDate: data.xResponseDate,
                                diffDays: data.diffDays,
                                action: data,
                            };
                        });

                    let result = {
                        data: mergedData,
                        recordsTotal: cell6923Data.totalRecord || 0,
                        recordsFiltered: cell6923Data.totalRecord || 0
                    };

                    console.log('Merged Data:', result);
                    callback(result);
                },
                error: function (xhr, error, code) {
                    console.error("Cell6923 endpoint error:", xhr.responseText);
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
            {"data": "personId"},
            {"data": "laboratoryId"},
            {"data": "countyId"},
            {"data": "sampleDate"},
            {"data": "sampleType"},
            {"data": "referralNumber"},
            {"data": "referenceTypeId"},
            {"data": "referenceSite"},
            {"data": "residc"},
            {"data": "residk"},
            {"data": "xSampleDate"},
            {"data": "xRegistrationDate"},
            {"data": "xSnomed"},
            {"data": "diagId"},
            {"data": "ansClinic"},
            {"data": "debClinic"},
            {"data": "remClinic"},
            {"data": "registrationDate"},
            {"data": "scrType"},
            {"data": "snomed"},
            {"data": "responseDate"},
            {"data": "xResponseDate"},
            {"data": "diffDays"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";
                    if (CommonUtil.hasAuthority("CELL6923_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "cell6923Edit btn color-orange");

                        spanEdit.setAttribute("data-cell6923", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "cell6923Show btn color-green");
                    spanShow.setAttribute("data-cell6923", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("CELL6923_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "cell6923Delete btn color-red");
                        spanDelete.setAttribute("data-cell6923_id", data.id);
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


// export let filterByPerson = function (tableElem, personIds) {
//     let api = EndPoints.PERSON_SAMPLE + "/filter?";
//     if (personIds && personIds.length > 0) {
//         api += personIds.map(id => `personId=${encodeURIComponent(id)}`).join('&');
//     }
//     listSample(tableElem, api);
// };

export let addCell6923 = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.CELL6923, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteCell6923 = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.CELL6923 + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback == 'function') {
                callback();
            }
        });
};

export let updateCell6923 = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.CELL6923, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let exportCell6923 = function (elem, api) {
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
            a.download = "Cell6923_export.xlsx";
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

export let importCell6923 = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.CELL6923_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
}

export let exportCell6923Template = function (elem, api) {
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
            a.download = "Cell6923_template.xlsx";
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


