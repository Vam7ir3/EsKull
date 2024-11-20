'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";

export let listLog = function (tableElem, api, userId = null) {
    $(tableElem).DataTable({
        order: [[4, 'desc']],
        searching: true,
        destroy: true,
        ajax: {
            url: api ? api : EndPoints.LOG,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(tableElem);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "desc";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "timestamp";
                d.searchTerm = d.search.value;
                if (userId) {
                    d.userId = userId;
                }
                delete d.columns;
                delete d.order;
                delete d.search;
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
                    AlertMessageUtil.alertMessage(data);
                    return JSON.stringify({data: [], recordsTotal: 0, recordsFiltered: 0});
                }
                let logs = parsedData.data.logs;
                let counter = 1;

                let arr = [];
                if (logs && Array.isArray(logs)) {
                    logs.forEach(function (item) {
                        let user = item.userId; 
                        arr.push({
                            sn: counter++,
                            description: item.description || 'N/A',
                            operation: item.operation || 'N/A',
                            userId: user ? `${user.firstName} ${user.lastName} (${user.emailAddress})` : 'Unknown User',
                            timestamp: item.timestamp || 'N/A',
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr,
                    "recordsTotal": logs ? logs.length : 0,
                    "recordsFiltered": logs ? logs.length : 0
                };
                return JSON.stringify(filteredData);
            },
            error: function (xhr, error, code) {
                AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
            },
            complete: function () {
                LoaderUtil.hideLoader(tableElem);
            }
        },
        columns: [
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            {"data": "description"},
            {"data": "operation"},
            {"data": "userId"},
            {"data": "timestamp"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "logShow btn color-green");
                    spanShow.setAttribute("data-log", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    return spanShow.outerHTML;
                }
            }
        ]
    });
};

export let filterByEmail = function (tableElem, user_id) {
    listLog(tableElem, `${EndPoints.LOG_FILTER}${user_id}`);
};

export let exportLog = function (elem, api) {
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
            a.download = "log_export.xlsx"; // Set the desired filename

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



