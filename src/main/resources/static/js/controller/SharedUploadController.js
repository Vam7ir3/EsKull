'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";

export let listSharedUpload = function(tableElem, api){
    $(tableElem).DataTable({
        ajax:{
            url: api ? api : EndPoints.STUDY_SHARE,
            type: "GET",
            "beforeSend": function(xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(tableElem);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                d.searchTerm = d.search.value;
                delete d.columns;
                delete d.order;
                delete d.search;
            },

            dataFilter: function(data, type){
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.list;
                console.log(reqData)
                let counter = parsedData.startPosition;


                let arr = [];
                if (Array.isArray(reqData)){
                    reqData.forEach(function (item) {
                        if (Array.isArray(item.sharedUsers) && item.sharedUsers.length > 0) {
                            const sharedUser = item.sharedUsers[0];
                            const firstName = sharedUser?.firstName || "Unknown";
                            const lastName = sharedUser?.lastName || "Unknown";
                            arr.push({
                                sn: (counter++),
                                id:item.uploader.firstName + " " + item.uploader.lastName,
                                name: firstName + " " + lastName,
                                action: item
                            });
                        }
                    });
                }

                let filteredData = {
                    "data": arr,
                    "recordsTotal": parsedData.totalRecord,
                    "recordsFiltered": parsedData.totalRecord
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
        columns:[
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            {"data":"id"},
            {"data": "name"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";
                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "Information");
                    spanShow.setAttribute("class", "SharedUploadShow btn color-green");
                    spanShow.setAttribute('sessionId',data.sessionId)
                    spanShow.setAttribute('data-all', JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue += spanShow.outerHTML;

                    let spanDelete = document.createElement("span");
                    spanDelete.setAttribute("title", "Delete");
                    spanDelete.setAttribute("class", "nursePatientDelete btn color-red");
                    spanDelete.setAttribute("sessionId", data.id);
                    let iDelete = document.createElement("i");
                    iDelete.setAttribute("class", "fas fa-trash");
                    spanDelete.appendChild(iDelete);
                    returnValue += spanDelete.outerHTML;

                    return returnValue;
                }
            }
        ],
    });
};
export let addSharedUpload = function(tableElem,newData) {
    $(tableElem).DataTable({
        ajax: {
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(tableElem);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                d.searchTerm = d.search.value;
                delete d.columns;
                delete d.order;
                delete d.search;
            },
            dataFilter: function () {
                let index = 0;
                let arr = [];
                if (newData) {
                    console.log(newData)
                        if(newData && Array.isArray(newData.sharedStudies)  ) {
                            newData.sharedStudies.forEach(function(sharedStudy,index)
                            {
                                arr.push({
                                    sn:index+1,
                                    name: sharedStudy.name,
                                    description: sharedStudy.description,
                                    contactName: sharedStudy.contactName,
                                    contactEmail: sharedStudy.contactEmail,
                                });
                            })
                        }
                }
                let filteredData = {
                    "data": arr,
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
            {"data": "name"},
            {"data": "description"},
            {"data": "contactName"},
            {"data": "contactEmail"},
        ]
    })
}

export function deleteAnswer(elem,sessionId,callBack){
    console.log(sessionId)
    CallApi.callBackend(elem,EndPoints.SAMPLE_SHARE + '/' + sessionId ,"DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callBack && typeof callBack === 'function'){
                callBack();
            }
        })
}
