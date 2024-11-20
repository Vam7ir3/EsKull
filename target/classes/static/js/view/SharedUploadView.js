'use strict';
import * as CommonUtil from '../util/CommonUtil.js';
import * as SharedUploadUI from "../ui/SharedUploadUI.js";
import * as SharedUploadController from "../controller/SharedUploadController.js";
$(document).ready(function () {
    CommonUtil.initialSetup();
    $(SharedUploadUI.idTableMainData).hide();
    $(SharedUploadUI.idCloseIcon).hide();
    $(SharedUploadUI.idTableUserData).hide();
    $(SharedUploadUI.idTableMainUpload).show();
    SharedUploadController.listSharedUpload(SharedUploadUI.idTableSharedUpload);
})
$(SharedUploadUI.idTableSharedUpload).on('draw.dt', function () {
    let SharedUploadShow = SharedUploadUI.idTableSharedUpload.querySelectorAll(".SharedUploadShow");
    Array.from(SharedUploadShow).forEach(function (element) {
        element.addEventListener('click', function (event) {
            let dataset = event.currentTarget.dataset.all;
            let newData = JSON.parse(dataset);
            $(SharedUploadUI.idTableMainUpload).hide();
            $(SharedUploadUI.idTableUserData).hide();
            $(SharedUploadUI.idTableMainData).show();
            $(SharedUploadUI.idCloseIcon).show();
            SharedUploadController.addSharedUpload(SharedUploadUI.idTableSharedData,newData);
        });
    });
});
  SharedUploadUI.idBtnAnswerDelete.addEventListener("click",function(event){
      SharedUploadController.deleteAnswer(SharedUploadUI.idModalAnswerDelete,event.currentTarget.dataset.sessionId,function(){
         SharedUploadController.listSharedUpload(SharedUploadUI.idTableSharedUpload);
      })
})
$(SharedUploadUI.idCloseIcon).click(function(){
    $(SharedUploadUI.idTableMainData).hide();
    $(SharedUploadUI.idTableUserData).hide();
    $(SharedUploadUI.idTableMainUpload).show();
})

