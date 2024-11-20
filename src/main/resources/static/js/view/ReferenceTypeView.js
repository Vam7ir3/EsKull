'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as ReferenceTypeUI from "../ui/ReferenceTypeUI.js";
import * as ReferenceTypeController from "../controller/ReferenceTypeController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.REFERENCETYPE);

    if (!CommonUtil.hasAuthority("REFERENCETYPE_C")) {
        $(ReferenceTypeUI.idBtnPopAddReferenceType).hide();
    } else {
        ReferenceTypeUI.idBtnPopAddReferenceType.addEventListener("click", function (event) {
            $(ReferenceTypeUI.idReferenceTypeShow).hide();
            $(ReferenceTypeUI.idFormReferenceTypeUpdate).hide();
            $(ReferenceTypeUI.idFormReferenceTypeImport).hide();
            $(ReferenceTypeUI.idFormReferenceTypeAdd).show();
            ReferenceTypeUI.modalReferenceTypeTitle.textContent = "Add ReferenceType";
            $(ReferenceTypeUI.idModalReferenceType).modal("show");
        });

        // ReferenceType add form submission
        $(ReferenceTypeUI.idFormReferenceTypeAdd).validate({
            rules: {
                type: "required"
            },
            messages: {
                type: "type required"

            },
            submitHandler: function (form) {

                let requestBody = {
                    "type": form.querySelector("input[name='type']").value,
                };
                ReferenceTypeController.addReferenceType(form, requestBody, function () {
                    ReferenceTypeController.listReferenceType(ReferenceTypeUI.idTableReferenceType);
                });
            }
        });
    }

    ReferenceTypeController.listReferenceType(ReferenceTypeUI.idTableReferenceType);

    $(ReferenceTypeUI.idTableReferenceType).on('draw.dt', function () {
        //Edit Event Listener
        let edits = ReferenceTypeUI.idTableReferenceType.querySelectorAll(".referenceTypeEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let referenceTypeData = event.currentTarget.getAttribute('data-reference-type');
                putValueInEditForm(JSON.parse(referenceTypeData));
                $(ReferenceTypeUI.idReferenceTypeShow).hide();
                $(ReferenceTypeUI.idFormReferenceTypeAdd).hide();
                $(ReferenceTypeUI.idFormReferenceTypeImport).hide();
                $(ReferenceTypeUI.idFormReferenceTypeUpdate).show();
                ReferenceTypeUI.modalReferenceTypeTitle.textContent = "Edit ReferenceType";
                $(ReferenceTypeUI.idModalReferenceType).modal("show");
            });
        });

        // Show event Listener
        let shows = ReferenceTypeUI.idTableReferenceType.querySelectorAll(".referenceTypeShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let referenceType = event.currentTarget.getAttribute('data-reference-type');
                putValueInShow(JSON.parse(referenceType));
                $(ReferenceTypeUI.idFormReferenceTypeAdd).hide();
                $(ReferenceTypeUI.idFormReferenceTypeUpdate).hide();
                $(ReferenceTypeUI.idFormReferenceTypeImport).hide();
                $(ReferenceTypeUI.idReferenceTypeShow).show();
                ReferenceTypeUI.modalReferenceTypeTitle.textContent = "ReferenceType Detail";
                $(ReferenceTypeUI.idModalReferenceType).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = ReferenceTypeUI.idTableReferenceType.querySelectorAll(".referenceTypeDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                ReferenceTypeUI.idBtnReferenceTypeDelete.dataset.referenceType_id = event.currentTarget.getAttribute('data-reference-type-id');
                $(ReferenceTypeUI.idModalReferenceTypeDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (referenceType) {
        ReferenceTypeUI.id.value = referenceType.id;
        ReferenceTypeUI.typeUpdate.value = referenceType.type;
    };


    let putValueInShow = function (referenceType) {
        if (ReferenceTypeUI.idReferenceTypeShow) {
            ReferenceTypeUI.idReferenceTypeShow.textContent = referenceType.type;
        } else {
            console.error("Element type not found");
        }
    };


    // ReferenceType Update for submission
    $(ReferenceTypeUI.idFormReferenceTypeUpdate).validate({
        rules: {
            type: "required",
        },
        message: {
            type: "type required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "type": form.querySelector("input[name='type']").value,
            };
            ReferenceTypeController.updateReferenceType(form, requestBody, function () {
                ReferenceTypeController.listReferenceType(ReferenceTypeUI.idTableReferenceType);
            });
        }
    });

    ReferenceTypeUI.idReferenceTypeTemplate.addEventListener('click', function () {
        ReferenceTypeController.exportReferenceTypeTemplate(ReferenceTypeUI.idReferenceTypeTemplate, Endpoints.REFERENCETYPE_TEMPLATE);
    })
    ReferenceTypeUI.idReferenceTypeExport.addEventListener('click', function () {
        ReferenceTypeController.exportReferenceType(ReferenceTypeUI.idReferenceTypeExport, Endpoints.REFERENCETYPE_EXPORT);
    })

    ReferenceTypeUI.idReferenceTypeImport.addEventListener('click', function () {
        $(ReferenceTypeUI.idFormReferenceTypeUpdate).hide();
        $(ReferenceTypeUI.idFormReferenceTypeAdd).hide();
        $(ReferenceTypeUI.idReferenceTypeShow).hide();
        $(ReferenceTypeUI.idFormReferenceTypeImport).show();
        ReferenceTypeUI.modalReferenceTypeTitle.textContent = "Import User Data";
        $(ReferenceTypeUI.idModalReferenceType).modal("show");
    })
    $(ReferenceTypeUI.idFormReferenceTypeImport).validate({
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

            ReferenceTypeController.importReferenceType(form, requestBody, function () {
                ReferenceTypeController.listReferenceType(ReferenceTypeUI.idTableReferenceType);
            });
        }
    });


    // Delete
    ReferenceTypeUI.idBtnReferenceTypeDelete.addEventListener("click", function (event) {
        ReferenceTypeController.deleteReferenceType(ReferenceTypeUI.idModalReferenceTypeDelete, event.currentTarget.dataset.referenceType_id, function () {
            ReferenceTypeController.listReferenceType(ReferenceTypeUI.idTableReferenceType);
        });
    });

    $(ReferenceTypeUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(ReferenceTypeUI.idModalReferenceType).modal("hide");
        $(ReferenceTypeUI.idFormReferenceTypeAdd).trigger("reset");
        $(ReferenceTypeUI.idFormReferenceTypeUpdate).trigger("reset");
        $(ReferenceTypeUI.idModalReferenceTypeDelete).modal("hide");
        $(ReferenceTypeUI.modalAlertBody).html("");
    });

});

