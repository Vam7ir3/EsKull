'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as LabUI from "../ui/LabUI.js";
import * as LabController from "../controller/LabController.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("LAB_C")) {
        $(LabUI.idBtnPopAddLab).hide();
    } else {
        LabUI.idBtnPopAddLab.addEventListener("click", function (event) {
            $(LabUI.idLabShow).hide();
            $(LabUI.idFormLabUpdate).hide();
            $(LabUI.idFormLabImport).hide();
            $(LabUI.idFormLabAdd).show();
            LabUI.modalLabTitle.textContent = "Add Lab";
            $(LabUI.idModalLab).modal("show");
        });

        // Lab add form submission
        $(LabUI.idFormLabAdd).validate({
            rules: {
                name: "required",
            },
            messages: {
                name: "Name required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    "name" : form.querySelector("input[name='name']").value,
                    "isInUse" : form.querySelector("input[name='isInUse']").checked,
                };
                LabController.addLab(form, requestBody, function () {
                    LabController.listLab(LabUI.idTableLab);
                });
            }
        });
    }

    LabController.listLab(LabUI.idTableLab);

    $(LabUI.idTableLab).on('draw.dt', function () {

        //Edit Event Listener
        let edits = LabUI.idTableLab.querySelectorAll(".labEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let lab = event.currentTarget.dataset.lab;
                putValueInEditForm(JSON.parse(lab));
                $(LabUI.idLabShow).hide();
                $(LabUI.idFormLabAdd).hide();
                $(LabUI.idFormLabImport).hide();
                $(LabUI.idFormLabUpdate).show();
                LabUI.modalLabTitle.textContent = "Edit Lab";
                $(LabUI.idModalLab).modal("show");
            });
        });

        // Show event Listener
        let shows = LabUI.idTableLab.querySelectorAll(".labShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let lab = event.currentTarget.dataset.lab;
                putValueInShow(JSON.parse(lab));
                $(LabUI.idFormLabAdd).hide();
                $(LabUI.idFormLabUpdate).hide();
                $(LabUI.idFormLabImport).hide();
                $(LabUI.idLabShow).show();
                LabUI.modalLabTitle.textContent = "Lab Detail";
                $(LabUI.idModalLab).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = LabUI.idTableLab.querySelectorAll(".labDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                LabUI.idBtnLabDelete.dataset.lab_id = event.currentTarget.dataset.lab_id;
                $(LabUI.idModalLabDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (lab) {
        LabUI.id.value = lab.id;
        LabUI.nameUpdate.value = lab.name;
    };


    let putValueInShow = function (lab) {
        if (LabUI.idNameShow) {
            LabUI.idNameShow.textContent = lab.name;
        } else {
            console.error("Element idNameShow not found");
        }
        if (LabUI.idIsInUseShow) {
            LabUI.idIsInUseShow.textContent = lab.isInUse;
        } else {
            console.error("Element idIsInUseShow not found");
        }

    };


    // Lab Update for submission
    $(LabUI.idFormLabUpdate).validate({
        rules: {
            name: "required",
        },
        message: {
            name: "Lab Name required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "name": form.querySelector("input[name='name']").value,
                "isInUse": form.querySelector("input[name='isInUse']").value,
            };
            LabController.updateLab(form, requestBody, function () {
                LabController.listLab(LabUI.idTableLab);
            });
        }
    });

    LabUI.idLabTemplate.addEventListener('click', function () {
        LabController.exportLabTemplate(LabUI.idLabTemplate, Endpoints.LAB_TEMPLATE);
    })
    LabUI.idLabExport.addEventListener('click', function () {
        LabController.exportLab(LabUI.idLabExport, Endpoints.LAB_EXPORT);
    })
    LabUI.idLabImport.addEventListener('click', function () {
        $(LabUI.idFormLabUpdate).hide();
        $(LabUI.idFormLabAdd).hide();
        $(LabUI.idLabShow).hide();
        $(LabUI.idFormLabImport).show();
        LabUI.modalLabTitle.textContent = "Import User Data";
        $(LabUI.idModalLab).modal("show");
    })
    $(LabUI.idFormLabImport).validate({
        rules: {
            excelFile: "required"
        },
        messages: {
            excelFile: "Please select an excel file to import data."
        },
        submitHandler: function (form) {

            let requestBody = new FormData();
            let fileField = form.querySelector("input[type='file']");
            requestBody.append('excelFile', fileField.files[0]);

            LabController.importLab(form, requestBody, function () {
                LabController.listLab(LabUI.idTableLab);
            });
        }
    });


    // Delete
    LabUI.idBtnLabDelete.addEventListener("click", function (event) {
        LabController.deleteLab(LabUI.idModalLabDelete, event.currentTarget.dataset.lab_id, function () {
            LabController.listLab(LabUI.idTableLab);
        });
    });

    $(LabUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(LabUI.idModalLab).modal("hide");
        $(LabUI.idFormLabAdd).trigger("reset");
        $(LabUI.idFormLabUpdate).trigger("reset");
        $(LabUI.idModalLabDelete).modal("hide");
        $(LabUI.modalAlertBody).html("");
    });

});