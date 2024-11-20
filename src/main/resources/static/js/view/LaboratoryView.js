'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as LaboratoryUI from "../ui/LaboratoryUI.js";
import * as LaboratoryController from "../controller/LaboratoryController.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("LABORATORY_C")) {
        $(LaboratoryUI.idBtnPopAddLaboratory).hide();
    } else {
        LaboratoryUI.idBtnPopAddLaboratory.addEventListener("click", function (event) {
            $(LaboratoryUI.idLaboratoryShow).hide();
            $(LaboratoryUI.idFormLaboratoryUpdate).hide();
            $(LaboratoryUI.idFormLaboratoryImport).hide();
            $(LaboratoryUI.idFormLaboratoryAdd).show();
            LaboratoryUI.modalLaboratoryTitle.textContent = "Add Laboratory";
            $(LaboratoryUI.idModalLaboratory).modal("show");
        });

        // Laboratory add form submission
        $(LaboratoryUI.idFormLaboratoryAdd).validate({
            rules: {
                firstName: "required",
            },
            messages: {
                firstName: "First Name required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    "name" : form.querySelector("input[name='name']").value,
                    "isInUse": form.querySelector("input[name='isInUse']").checked,
                    "sosLab": form.querySelector("input[name='sosLab']").value,
                    "sosLabName": form.querySelector("input[name='sosLabName']").value,
                    "sosLongName": form.querySelector("input[name='sosLongName']").value,
                    "region": form.querySelector("input[name='region']").value,
                };
                LaboratoryController.addLaboratory(form, requestBody, function () {
                    LaboratoryController.listLaboratory(LaboratoryUI.idTableLaboratory);
                });
            }
        });
    }

    LaboratoryController.listLaboratory(LaboratoryUI.idTableLaboratory);

    $(LaboratoryUI.idTableLaboratory).on('draw.dt', function () {

        //Edit Event Listener
        let edits = LaboratoryUI.idTableLaboratory.querySelectorAll(".laboratoryEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let laboratory = event.currentTarget.dataset.laboratory;
                putValueInEditForm(JSON.parse(laboratory));
                $(LaboratoryUI.idLaboratoryShow).hide();
                $(LaboratoryUI.idFormLaboratoryAdd).hide();
                $(LaboratoryUI.idFormLaboratoryImport).hide();
                $(LaboratoryUI.idFormLaboratoryUpdate).show();
                LaboratoryUI.modalLaboratoryTitle.textContent = "Edit laboratory";
                $(LaboratoryUI.idModalLaboratory).modal("show");
            });
        });

        // Show event Listener
        let shows = LaboratoryUI.idTableLaboratory.querySelectorAll(".laboratoryShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let laboratory = event.currentTarget.dataset.laboratory;
                putValueInShow(JSON.parse(laboratory));
                $(LaboratoryUI.idFormLaboratoryAdd).hide();
                $(LaboratoryUI.idFormLaboratoryUpdate).hide();
                $(LaboratoryUI.idFormLaboratoryImport).hide();
                $(LaboratoryUI.idLaboratoryShow).show();
                LaboratoryUI.modalLaboratoryTitle.textContent = "Laboratory Detail";
                $(LaboratoryUI.idModalLaboratory).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = LaboratoryUI.idTableLaboratory.querySelectorAll(".laboratoryDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                LaboratoryUI.idBtnLaboratoryDelete.dataset.laboratory_id = event.currentTarget.dataset.laboratory_id;
                $(LaboratoryUI.idModalLaboratoryDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (laboratory) {
        LaboratoryUI.id.value = laboratory.id;
        LaboratoryUI.nameUpdate.value = laboratory.name;
        LaboratoryUI.isInUseUpdate.checked=laboratory.isInUse;
        LaboratoryUI.sosLabUpdate.value = laboratory.sosLab;
        LaboratoryUI.sosLabNameUpdate.value = laboratory.sosLabName;
        LaboratoryUI.sosLongNameUpdate.value= laboratory.sosLongName;
        LaboratoryUI.regionUpdate.value = laboratory.region;
    };


    let putValueInShow = function (laboratory) {
        if (LaboratoryUI.idNameShow) {
            LaboratoryUI.idNameShow.textContent = laboratory.name;
        } else {
            console.error("Element idNameShow not found");
        }
        if (LaboratoryUI.idIsInUseShow) {
            LaboratoryUI.idIsInUseShow.textContent = laboratory.isInUse;
        } else {
            console.error("Element idIsInUseShow not found");
        }

        if (LaboratoryUI.idSosLabShow) {
            LaboratoryUI.idSosLabShow.textContent = laboratory.sosLab;
        } else {
            console.error("Element idSosLabShow not found");
        }

        if (LaboratoryUI.idSosLabNameShow) {
            LaboratoryUI.idSosLabNameShow.textContent = laboratory.sosLabName;
        } else {
            console.error("Element idSosLabNameShow not found");
        }

        if (LaboratoryUI.idSosLongNameShow) {
            LaboratoryUI.idSosLongNameShow.textContent = laboratory.sosLongName;
        } else {
            console.error("Element idSosLongNameShow not found");
        }

        if (LaboratoryUI.idRegionShow) {
            LaboratoryUI.idRegionShow.textContent = laboratory.region;
        } else {
            console.error("Element idRegionShow not found");
        }


    };


    // laboratory Update for submission
    $(LaboratoryUI.idFormLaboratoryUpdate).validate({
        rules: {
            name: "required",
        },
        message: {
            name: "Laboratory Name required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "name": form.querySelector("input[name='name']").value,
                "isInUse": form.querySelector("input[name='isInUse']").checked,
                "sosLab": form.querySelector("input[name='sosLab']").value,
                "sosLabName": form.querySelector("input[name='sosLabName']").value,
                "sosLongName": form.querySelector("input[name='sosLongName'] ").value,
                "region": form.querySelector("input[name='region']").value,

            };
            LaboratoryController.updateLaboratory(form, requestBody, function () {
                LaboratoryController.listLaboratory(LaboratoryUI.idTableLaboratory);
            });
        }
    });

    LaboratoryUI.idLaboratoryTemplate.addEventListener('click', function () {
        LaboratoryController.exportLaboratoryTemplate(LaboratoryUI.idLaboratoryTemplate, Endpoints.LABORATORY_TEMPLATE);
    })
    LaboratoryUI.idLaboratoryExport.addEventListener('click', function () {
        LaboratoryController.exportLaboratory(LaboratoryUI.idLaboratoryExport, Endpoints.LABORATORY_EXPORT);
    })
    LaboratoryUI.idLaboratoryImport.addEventListener('click', function () {
        $(LaboratoryUI.idFormLaboratoryUpdate).hide();
        $(LaboratoryUI.idFormLaboratoryAdd).hide();
        $(LaboratoryUI.idLaboratoryShow).hide();
        $(LaboratoryUI.idFormLaboratoryImport).show();
        LaboratoryUI.modalLaboratoryTitle.textContent = "Import User Data";
        $(LaboratoryUI.idModalLaboratory).modal("show");
    })
    $(LaboratoryUI.idFormLaboratoryImport).validate({
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

            LaboratoryController.importLaboratory(form, requestBody, function () {
                LaboratoryController.listLaboratory(LaboratoryUI.idTableLaboratory);
            });
        }
    });

    // Add event listener for isInUse filter
    $(LaboratoryUI.idSelectIsInUse).on('change', function (event) {
        let isInUse = $(this).val();
        LaboratoryController.filterByIsInUse(LaboratoryUI.idTableLaboratory, isInUse);
    });

    // // Modify the search functionality
    // $(LaboratoryUI.idSearchInput).on('keyup', function () {
    //     let searchTerm = $(this).val();
    //     $(LaboratoryUI.idTableLaboratory).DataTable().search(searchTerm).draw();
    // });


    // Delete
    LaboratoryUI.idBtnLaboratoryDelete.addEventListener("click", function (event) {
        LaboratoryController.deleteLaboratory(LaboratoryUI.idModalLaboratoryDelete, event.currentTarget.dataset.laboratory_id, function () {
            LaboratoryController.listLaboratory(LaboratoryUI.idTableLaboratory);
        });
    });

    $(LaboratoryUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(LaboratoryUI.idModalLaboratory).modal("hide");
        $(LaboratoryUI.idFormLaboratoryAdd).trigger("reset");
        $(LaboratoryUI.idFormLaboratoryUpdate).trigger("reset");
        $(LaboratoryUI.idModalLaboratoryDelete).modal("hide");
        $(LaboratoryUI.modalAlertBody).html("");
    });

});

// Add event listener for isInUse filter
$(LaboratoryUI.idSelectIsInUse).on('change', function (event) {
    let isInUse = $(this).val(); // This will capture the selected value (true or false)
    LaboratoryController.filterByIsInUse(LaboratoryUI.idTableLaboratory, isInUse);
});