'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as ExtHpvUI from "../ui/ExtHpvUI.js";
import * as ExtHpvController from "../controller/ExtHpvController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(
        EndPoints.PERSON,
        ["pnr"],
        ExtHpvUI.idSelectPerson,
    );

    if (!CommonUtil.hasAuthority("EXTHPV_C")) {
        $(ExtHpvUI.idBtnPopAddExtHpv).hide();
    } else {
        ExtHpvUI.idBtnPopAddExtHpv.addEventListener("click", function (event) {
            $(ExtHpvUI.idExtHpvShow).hide();
            $(ExtHpvUI.idFormExtHpvUpdate).hide();
            $(ExtHpvUI.idFormExtHpvImport).hide();
            $(ExtHpvUI.idFormExtHpvAdd).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.PERSON, ["pnr"], ExtHpvUI.extHpvPersonAdd);
            ExtHpvUI.modalExtHpvTitle.textContent = "Add ExtHpv";
            $(ExtHpvUI.idModalExtHpv).modal("show");
        });

        // ExtHpv add form submission
        $(ExtHpvUI.idFormExtHpvAdd).validate({
            rules: {
                name: "required",
            },
            messages: {
                name: "Name required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    personId: $(ExtHpvUI.extHpvPersonAdd).val(),
                    "name" : form.querySelector("input[name='name']").value,
                };
                ExtHpvController.addExtHpv(form, requestBody, function () {
                    ExtHpvController.listExtHpv(ExtHpvUI.idTableExtHpv, EndPoints.PERSON_EXTHPV);
                });
            }
        });
    }

    ExtHpvController.listExtHpv(ExtHpvUI.idTableExtHpv, EndPoints.PERSON_EXTHPV);

    $(ExtHpvUI.idTableExtHpv).on('draw.dt', function () {

        //Edit Event Listener
        let edits = ExtHpvUI.idTableExtHpv.querySelectorAll(".extHpvEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let extHpv = event.currentTarget.dataset.extHpv;
                console.log("ExtHpv data attribute:", extHpv); // Log the ExtHpv data to ensure it's present
                if (!extHpv) {
                    console.error("ExtHpv data is missing!");
                    return;
                }

                putValueInEditForm(JSON.parse(extHpv));
                $(ExtHpvUI.idExtHpvShow).hide();
                $(ExtHpvUI.idFormExtHpvAdd).hide();
                $(ExtHpvUI.idFormExtHpvImport).hide();
                $(ExtHpvUI.idFormExtHpvUpdate).show();
                ExtHpvUI.modalExtHpvTitle.textContent = "Edit ExtHpv";
                $(ExtHpvUI.idModalExtHpv).modal("show");
            });
        });


        // Show event Listener
        let shows = ExtHpvUI.idTableExtHpv.querySelectorAll(".extHpvShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let extHpv = JSON.parse(event.currentTarget.dataset.extHpv);
                putValueInShow(extHpv);
                $(ExtHpvUI.idFormExtHpvAdd).hide();
                $(ExtHpvUI.idFormExtHpvUpdate).hide();
                $(ExtHpvUI.idFormExtHpvImport).hide();
                $(ExtHpvUI.idExtHpvShow).show();
                if (ExtHpvUI.idPersonShow) {
                    ExtHpvUI.idPersonShow.textContent = extHpv.personId ? extHpv.personId.name : 'N/A';
                } else {
                    console.error("Element idPersonShow not found.");
                }
                if (ExtHpvUI.modalExtHpvTitle) {
                    ExtHpvUI.modalExtHpvTitle.textContent = "ExtHpv Detail";
                } else {
                    console.error("Element modalExtHpvTitle not found.");
                }
                $(ExtHpvUI.idModalExtHpv).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = ExtHpvUI.idTableExtHpv.querySelectorAll(".extHpvDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                ExtHpvUI.idBtnExtHpvDelete.dataset.extHpv_id = event.currentTarget.dataset.extHpv_id;
                $(ExtHpvUI.idModalExtHpvDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (extHpv) {
        console.log("ExtHpv data for edit:", extHpv);

        ExtHpvUI.id.value = extHpv.id;
        ExtHpvUI.nameUpdate.value = extHpv.extHpvRes.name;

        let personId = extHpv.personRes && extHpv.personRes.id ? extHpv.personRes.id : null;

        SelectPickerUtil.populateSelectPicker(
            EndPoints.PERSON,
            ["pnr"],
            ExtHpvUI.personUpdate,
            personId
        );

        try {
            $(ExtHpvUI.personUpdate).selectpicker('refresh');
        } catch (error) {
            console.error("Error refreshing selectpicker:", error);
            console.log("ExtHpvUI.personUpdate:", ExtHpvUI.personUpdate);
            console.log("jQuery version:", $.fn.jquery);
            console.log("Bootstrap version:", $.fn.tooltip.Constructor.VERSION);
            console.log("Bootstrap-select version:", $.fn.selectpicker.Constructor.VERSION);
        }
    };



    $(ExtHpvUI.idTableExtHpv).on('click', '.extHpvShow', function(event) {
        let extHpv = JSON.parse(event.currentTarget.dataset.extHpv);
        console.log("ExtHpv data on show button click:", extHpv); // Log data
        putValueInShow(extHpv);
    });

    let putValueInShow = function (extHpv) {
        console.log("ExtHpv data received:", extHpv);  // Log the full ExtHpv data

        if (ExtHpvUI.idPersonNameShow && ExtHpvUI.idExtHpvNameShow) {
            // Person name
            let personName = extHpv.personRes
                ? `${extHpv.personRes.pnr}`
                : 'N/A';
            ExtHpvUI.idPersonNameShow.textContent = personName;

            // ExtHpv name (adjusted to access ExtHpvRes.name)
            let extHpvName = extHpv.extHpvRes && extHpv.extHpvRes.name
                ? extHpv.extHpvRes.name
                : 'N/A';

            console.log("ExtHpv name:", extHpvName);  // Log the ExtHpv name to ensure it's fetched correctly

            // Set the ExtHpv name
            ExtHpvUI.idExtHpvNameShow.textContent = extHpvName;
        } else {
            console.error("Element idPersonNameShow or idExtHpvNameShow not found");
        }
    };





    // ExtHpv Update for submission
    $(ExtHpvUI.idFormExtHpvUpdate).validate({
        rules: {
            name: "required",
        },
        messages: {
            name: "ExtHpv Name required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                personId: $(ExtHpvUI.personUpdate).val(), // Ensure this gets the updated value
                "name": form.querySelector("input[name='name']").value,
            };
            ExtHpvController.updateExtHpv(form, requestBody, function () {
                ExtHpvController.listExtHpv(ExtHpvUI.idTableExtHpv, EndPoints.PERSON_EXTHPV);
            });
        }
    });


    ExtHpvUI.idExtHpvTemplate.addEventListener('click', function () {
        ExtHpvController.exportExtHpvTemplate(ExtHpvUI.idExtHpvTemplate, EndPoints.EXTHPV_TEMPLATE);
    })
    ExtHpvUI.idExtHpvExport.addEventListener('click', function () {
        ExtHpvController.exportExtHpv(ExtHpvUI.idExtHpvExport, EndPoints.EXTHPV_EXPORT);
    })
    ExtHpvUI.idExtHpvImport.addEventListener('click', function () {
        $(ExtHpvUI.idFormExtHpvUpdate).hide();
        $(ExtHpvUI.idFormExtHpvAdd).hide();
        $(ExtHpvUI.idExtHpvShow).hide();
        $(ExtHpvUI.idFormExtHpvImport).show();
        ExtHpvUI.modalExtHpvTitle.textContent = "Import User Data";
        $(ExtHpvUI.idModalExtHpv).modal("show");
    })
    $(ExtHpvUI.idFormExtHpvImport).validate({
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

            ExtHpvController.importExtHpv(form, requestBody, function () {
                ExtHpvController.listExtHpv(ExtHpvUI.idTableExtHpv);
            });
        }
    });



    $(ExtHpvUI.idSelectPerson).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedPersonIds = $(this).val();
        ExtHpvController.filterByPerson(ExtHpvUI.idTableExtHpv, selectedPersonIds);
    });


    // Delete
    ExtHpvUI.idBtnExtHpvDelete.addEventListener("click", function (event) {
        ExtHpvController.deleteExtHpv(ExtHpvUI.idModalExtHpvDelete, event.currentTarget.dataset.extHpv_id, function () {
            ExtHpvController.listExtHpv(ExtHpvUI.idTableExtHpv, EndPoints.EXTHPV);
        });
    });

    $(ExtHpvUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(ExtHpvUI.idModalExtHpv).modal("hide");
        $(ExtHpvUI.idFormExtHpvAdd).trigger("reset");
        $(ExtHpvUI.idFormExtHpvUpdate).trigger("reset");
        $(ExtHpvUI.idModalExtHpvDelete).modal("hide");
        $(ExtHpvUI.modalAlertBody).html("");
    });

});
