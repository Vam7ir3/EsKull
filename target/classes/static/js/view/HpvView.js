'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as HpvUI from "../ui/HpvUI.js";
import * as HpvController from "../controller/HpvController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(
        EndPoints.PERSON,
        ["pnr"],
        HpvUI.idSelectPerson,
    );

    if (!CommonUtil.hasAuthority("HPV_C")) {
        $(HpvUI.idBtnPopAddHpv).hide();
    } else {
        HpvUI.idBtnPopAddHpv.addEventListener("click", function (event) {
            $(HpvUI.idHpvShow).hide();
            $(HpvUI.idFormHpvUpdate).hide();
            $(HpvUI.idFormHpvImport).hide();
            $(HpvUI.idFormHpvAdd).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.PERSON, ["pnr"], HpvUI.hpvPersonAdd);
            HpvUI.modalHpvTitle.textContent = "Add Hpv";
            $(HpvUI.idModalHpv).modal("show");
        });

        // Hpv add form submission
        $(HpvUI.idFormHpvAdd).validate({
            rules: {
                name: "required",
            },
            messages: {
                name: "Name required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    personId: $(HpvUI.hpvPersonAdd).val(),
                    "name" : form.querySelector("input[name='name']").value,
                };
                HpvController.addHpv(form, requestBody, function () {
                    HpvController.listHpv(HpvUI.idTableHpv, EndPoints.PERSON_HPV);
                });
            }
        });
    }

    HpvController.listHpv(HpvUI.idTableHpv, EndPoints.PERSON_HPV);

    $(HpvUI.idTableHpv).on('draw.dt', function () {

        //Edit Event Listener
        let edits = HpvUI.idTableHpv.querySelectorAll(".hpvEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let hpv = event.currentTarget.dataset.hpv;
                console.log("Hpv data attribute:", hpv); // Log the Hpv data to ensure it's present
                if (!hpv) {
                    console.error("Hpv data is missing!");
                    return;
                }

                putValueInEditForm(JSON.parse(hpv));
                $(HpvUI.idHpvShow).hide();
                $(HpvUI.idFormHpvAdd).hide();
                $(HpvUI.idFormHpvImport).hide();
                $(HpvUI.idFormHpvUpdate).show();
                HpvUI.modalHpvTitle.textContent = "Edit Hpv";
                $(HpvUI.idModalHpv).modal("show");
            });
        });


        // Show event Listener
        let shows = HpvUI.idTableHpv.querySelectorAll(".hpvShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let hpv = JSON.parse(event.currentTarget.dataset.hpv);
                putValueInShow(hpv);
                $(HpvUI.idFormHpvAdd).hide();
                $(HpvUI.idFormHpvUpdate).hide();
                $(HpvUI.idFormHpvImport).hide();
                $(HpvUI.idHpvShow).show();
                if (HpvUI.idPersonShow) {
                    HpvUI.idPersonShow.textContent = hpv.personId ? hpv.personId.name : 'N/A';
                } else {
                    console.error("Element idPersonShow not found.");
                }
                if (HpvUI.modalHpvTitle) {
                    HpvUI.modalHpvTitle.textContent = "Hpv Detail";
                } else {
                    console.error("Element modalHpvTitle not found.");
                }
                $(HpvUI.idModalHpv).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = HpvUI.idTableHpv.querySelectorAll(".hpvDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                HpvUI.idBtnHpvDelete.dataset.hpv_id = event.currentTarget.dataset.hpv_id;
                $(HpvUI.idModalHpvDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (hpv) {
        console.log("Hpv data for edit:", hpv);

        HpvUI.id.value = hpv.id;
        HpvUI.nameUpdate.value = hpv.hpvRes.name;

        let personId = hpv.personRes && hpv.personRes.id ? hpv.personRes.id : null;

        SelectPickerUtil.populateSelectPicker(
            EndPoints.PERSON,
            ["pnr"],
            HpvUI.personUpdate,
            personId
        );

        try {
            $(HpvUI.personUpdate).selectpicker('refresh');
        } catch (error) {
            console.error("Error refreshing selectpicker:", error);
            console.log("HpvUI.personUpdate:", HpvUI.personUpdate);
            console.log("jQuery version:", $.fn.jquery);
            console.log("Bootstrap version:", $.fn.tooltip.Constructor.VERSION);
            console.log("Bootstrap-select version:", $.fn.selectpicker.Constructor.VERSION);
        }
    };



    $(HpvUI.idTableHpv).on('click', '.hpvShow', function(event) {
        let hpv = JSON.parse(event.currentTarget.dataset.hpv);
        console.log("Hpv data on show button click:", hpv); // Log data
        putValueInShow(hpv);
    });

    let putValueInShow = function (hpv) {
        console.log("Hpv data received:", hpv);  // Log the full Hpv data

        if (HpvUI.idPersonNameShow && HpvUI.idHpvNameShow) {
            // Person name
            let personName = hpv.personRes
                ? `${hpv.personRes.pnr}`
                : 'N/A';
            HpvUI.idPersonNameShow.textContent = personName;

            // Hpv name (adjusted to access HpvRes.name)
            let hpvName = hpv.hpvRes && hpv.hpvRes.name
                ? hpv.hpvRes.name
                : 'N/A';

            console.log("Hpv name:", hpvName);  // Log the Hpv name to ensure it's fetched correctly

            // Set the Hpv name
            HpvUI.idHpvNameShow.textContent = hpvName;
        } else {
            console.error("Element idPersonNameShow or idHpvNameShow not found");
        }
    };





    // Hpv Update for submission
    $(HpvUI.idFormHpvUpdate).validate({
        rules: {
            name: "required",
        },
        messages: {
            name: "Hpv Name required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                personId: $(HpvUI.personUpdate).val(), // Ensure this gets the updated value
                "name": form.querySelector("input[name='name']").value,
            };
            HpvController.updateHpv(form, requestBody, function () {
                HpvController.listHpv(HpvUI.idTableHpv, EndPoints.PERSON_HPV);
            });
        }
    });


    HpvUI.idHpvTemplate.addEventListener('click', function () {
        HpvController.exportHpvTemplate(HpvUI.idHpvTemplate, EndPoints.HPV_TEMPLATE);
    })
    HpvUI.idHpvExport.addEventListener('click', function () {
        HpvController.exportHpv(HpvUI.idHpvExport, EndPoints.HPV_EXPORT);
    })
    HpvUI.idHpvImport.addEventListener('click', function () {
        $(HpvUI.idFormHpvUpdate).hide();
        $(HpvUI.idFormHpvAdd).hide();
        $(HpvUI.idHpvShow).hide();
        $(HpvUI.idFormHpvImport).show();
        HpvUI.modalHpvTitle.textContent = "Import User Data";
        $(HpvUI.idModalHpv).modal("show");
    })
    $(HpvUI.idFormHpvImport).validate({
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

            HpvController.importHpv(form, requestBody, function () {
                HpvController.listHpv(HpvUI.idTableHpv);
            });
        }
    });



    $(HpvUI.idSelectPerson).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedPersonIds = $(this).val();
        HpvController.filterByPerson(HpvUI.idTableHpv, selectedPersonIds);
    });


    // Delete
    HpvUI.idBtnHpvDelete.addEventListener("click", function (event) {
        HpvController.deleteHpv(HpvUI.idModalHpvDelete, event.currentTarget.dataset.hpv_id, function () {
            HpvController.listHpv(HpvUI.idTableHpv, EndPoints.HPV);
        });
    });

    $(HpvUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(HpvUI.idModalHpv).modal("hide");
        $(HpvUI.idFormHpvAdd).trigger("reset");
        $(HpvUI.idFormHpvUpdate).trigger("reset");
        $(HpvUI.idModalHpvDelete).modal("hide");
        $(HpvUI.modalAlertBody).html("");
    });

});
