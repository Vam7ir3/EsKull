'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";


export let listSample = function (tableElem, api, personId = null) {
    const endpoint = personId ? EndPoints.PERSON_SAMPLE_FILTER + personId : (api ? api : EndPoints.PERSON_SAMPLE);

    $(tableElem).DataTable({
        serverSide: true,
        destroy: true,
        searching: true,
        processing: true,
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
                success: function (sampleData) {
                    console.log("SAMPLE response:", sampleData);

                    let adaptedSampleData = {
                        data: {
                            list: Array.isArray(sampleData) ? sampleData : []
                        },
                        totalRecord: Array.isArray(sampleData) ? sampleData.length : 0
                    };

                    $.ajax({
                        url: EndPoints.PERSON_SAMPLE,
                        type: "GET",
                        data: params,
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                        },
                        success: function (personSampleData) {
                            console.log("PERSON_SAMPLE response:", personSampleData);

                            if (!personSampleData.data || !Array.isArray(personSampleData.data.list)) {
                                console.error("Unexpected PERSON_SAMPLE response format:", personSampleData);
                                AlertMessageUtil.alertMessage({message: "Invalid data format received from PERSON_SAMPLE endpoint."});
                                return;
                            }

                            let sampleList =  personSampleData.data.list;
                            let personSampleList = personSampleData.data.list;

                            let mergedData = sampleList
                                .filter(sample => {
                                    let relatedPersonSample = personSampleList.find(ps => ps.sampleRes && ps.sampleRes.id === sample.id);
                                    return !searchTerm || (relatedPersonSample && relatedPersonSample.sampleRes.type.toLowerCase().includes(searchTerm.toLowerCase()));
                                })
                                .map((sample, index) => {
                                    let relatedPersonSample = personSampleList.find(ps => ps.sampleRes && ps.sampleRes.id === sample.id);

                                    return {
                                        sn: index + 1,
                                        personName: relatedPersonSample ? `${relatedPersonSample.personRes.pnr}` : 'N/A',
                                        sampleName: relatedPersonSample && relatedPersonSample.sampleRes ? relatedPersonSample.sampleRes.type : 'N/A',
                                        action: {
                                            ...sample,
                                            personRes: relatedPersonSample ? relatedPersonSample.personRes : null
                                        }
                                    };
                                });

                            let result = {
                                data: mergedData,
                                recordsTotal: personSampleData.totalRecord || 0,
                                recordsFiltered: personSampleData.totalRecord || 0
                            };

                            console.log('Merged Data:', result);
                            callback(result);
                        },
                        error: function (xhr, error, code) {
                            console.error("PERSON_SAMPLE endpoint error:", xhr.responseText);
                            AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
                        }
                    });
                },
                error: function (xhr, error, code) {
                    console.error("SAMPLE endpoint error:", xhr.responseText);
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
            {"data": "personName"},
            {"data": "sampleName"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";
                    if (CommonUtil.hasAuthority("SAMPLE_U")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "sampleEdit btn color-orange");
                        spanEdit.setAttribute("data-sample", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "sampleShow btn color-green");
                    spanShow.setAttribute("data-sample", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("SAMPLE_D")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "sampleDelete btn color-red");
                        spanDelete.setAttribute("data-sample_id", data.id);
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
    let api = EndPoints.PERSON_SAMPLE + "/filter?";
    if (personIds && personIds.length > 0) {
        api += personIds.map(id => `personId=${encodeURIComponent(id)}`).join('&');
    }
    listSample(tableElem, api);
};

export let addSample = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.SAMPLE, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteSample = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.SAMPLE + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback == 'function') {
                callback();
            }
        });
};

export let updateSample = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.SAMPLE, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};
export let exportSample = function (elem, api) {
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
            a.download = "sample_export.xlsx"; // Set the desired filename

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

export let importSample = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.SAMPLE_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
}

export let exportSampleTemplate = function (elem, api) {
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
            console.log(blob)
            let fileURL = URL.createObjectURL(blob);

            let a = document.createElement("a");
            a.href = fileURL;
            a.download = "sample_template.xlsx";

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


