'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
export let listMyUpload = function(tableElem, api){
    $(tableElem).DataTable({
        ajax:{
            url: api ? api : EndPoints.STUDY_SESSION,
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
                console.log(parsedData);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.list;
                let counter = parsedData.startPosition;
                console.log(reqData);


                let arr = [];
                if (Array.isArray(reqData)){
                    reqData.forEach(function (item) {
                            arr.push({
                                sn: (counter++),
                                id: item.id,
                                action: item
                            });

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
            {"data": "id"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";
                        let spanShow = document.createElement("span");
                        spanShow.setAttribute("title", "Information");
                        spanShow.setAttribute("class", "myUploadShow btn color-green");
                        spanShow.setAttribute('sessionId',data.id)
                        spanShow.setAttribute('data-all', JSON.stringify(data));
                        let iShow = document.createElement("i");
                        iShow.setAttribute("class", "fas fa-eye");
                        spanShow.appendChild(iShow);
                        returnValue += spanShow.outerHTML;
                        return returnValue;
                    }
            }
        ],
           });
};

export let addUpload = function(tableElem,sessionId) {
    $(tableElem).DataTable({
        ajax: {
            url: EndPoints.MY_UPLOAD + "/" + sessionId,
            type: "POST",
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
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                console.log(parsedData)
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.list;
                let counter = parsedData.startPosition;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            sn: (counter++),
                            name: item.name,
                            description: item.description,
                            contactName: item.contactName,
                            contactEmail: item.contactEmail,
                        });
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
export let shareUser = function(elem,requestBody,callback){
    CallApi.callBackend(elem,EndPoints.STUDY_SHARE,"POST",requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function'){
                callback(response);
            }
        })
        .catch(error =>{
            console.log("error sharing data",error)
        });
};