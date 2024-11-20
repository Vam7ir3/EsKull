'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as MyUploadUI from "../ui/MyUploadUI.js";
import * as MyUploadController from "../controller/MyUploadController.js";
import * as EndPoints from "../controller/EndPoints.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";

    $(document).ready(function () {
        CommonUtil.initialSetup();
        SelectPickerUtil.populateSelectPicker(EndPoints.USER,["id","firstName","lastName"],MyUploadUI.idSelectUser,null,"All")
        $(MyUploadUI.idTableMainData).hide();
        $(MyUploadUI.idCloseIcon).hide();
        $(MyUploadUI.idSelectUserShare).hide();
        $(MyUploadUI.idTableMainUpload).show();
        MyUploadController.listMyUpload(MyUploadUI.idTableUpload);

        let sessionId;

        $(MyUploadUI.idTableUpload).on('draw.dt', function () {
            let myUploadShow = MyUploadUI.idTableUpload.querySelectorAll(".myUploadShow");
            Array.from(myUploadShow).forEach(function (element) {
                element.addEventListener('click', function (event) {
                    let data = event.currentTarget.dataset.all;
                    sessionId = JSON.parse(data).id;
                    console.log(sessionId)
                    $(MyUploadUI.idTableMainUpload).hide();
                    $(MyUploadUI.idTableMainData).show();
                    $(MyUploadUI.idSelectUserShare).show();
                    $(MyUploadUI.idCloseIcon).show();
                    MyUploadController.addUpload(MyUploadUI.idTableData,sessionId);
                });
            });
        });
        $(MyUploadUI.idCloseIcon).click(function(){
            $(MyUploadUI.idTableMainData).hide();
            $(MyUploadUI.idSelectUserShare).hide();
            $(MyUploadUI.idTableMainUpload).show();
        })
        MyUploadUI.idAddUserForShare.addEventListener('click',function(){
            let selected = $(MyUploadUI.idSelectUser).val();
            let api = "";

              let requestBody = {
                 sharedUsersIds : [selected], sharedStudiesIds :[sessionId],
              }
            MyUploadController.shareUser(MyUploadUI.idAddUserForShare,requestBody,api);
              $(MyUploadUI.idSelectUser).val(" ");
        })


        $(MyUploadUI.idModalAlert).on("hidden.bs.modal", function (e) {
            $(MyUploadUI.modalAlertBody).html("");
        });
    });


