'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as KlartextUI from "../ui/KlartextUI.js";
import * as KlartextController from "../controller/KlartextController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.KLARTEXT);

    if (!CommonUtil.hasAuthority("KLARTEXT_C")) {
        $(KlartextUI.idBtnPopAddKlartext).hide();
    } else {
        KlartextUI.idBtnPopAddKlartext.addEventListener("click", function (event) {
            $(KlartextUI.idKlartextShow).hide();
            $(KlartextUI.idFormKlartextUpdate).hide();
            $(KlartextUI.idFormKlartextImport).hide();
            $(KlartextUI.idFormKlartextAdd).show();
            KlartextUI.modalKlartextTitle.textContent = "Add Klartext";
            $(KlartextUI.idModalKlartext).modal("show");
        });

        // Klartext add form submission
        $(KlartextUI.idFormKlartextAdd).validate({
            rules: {
                snomedText: "required"
            },
            messages: {
                snomedText: "snomedText required"

            },
            submitHandler: function (form) {

                let requestBody = {
                    "snomedText": form.querySelector("input[name='snomedText']").value,
                };
                KlartextController.addKlartext(form, requestBody, function () {
                    KlartextController.listKlartext(KlartextUI.idTableKlartext);
                });
            }
        });
    }

    KlartextController.listKlartext(KlartextUI.idTableKlartext);

    $(KlartextUI.idTableKlartext).on('draw.dt', function () {
        //Edit Event Listener
        let edits = KlartextUI.idTableKlartext.querySelectorAll(".klartextEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                console.log("Edit button clicked");
                let klartext = event.currentTarget.dataset.klartext;
                console.log("Klartext data:", klartext);
                try {
                    let parsedKlartext = JSON.parse(klartext);
                    console.log("Parsed Klartext data:", parsedKlartext);
                    putValueInEditForm(parsedKlartext);
                } catch (e) {
                    console.error("Error parsing Klartext data:", e);
                }
                $(KlartextUI.idKlartextShow).hide();
                $(KlartextUI.idFormKlartextAdd).hide();
                $(KlartextUI.idFormKlartextImport).hide();
                $(KlartextUI.idFormKlartextUpdate).show();
                KlartextUI.modalKlartextTitle.textContent = "Edit Klartext";
                $(KlartextUI.idModalKlartext).modal("show");
            });
        });

        // Show event Listener
        let shows = KlartextUI.idTableKlartext.querySelectorAll(".klartextShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let klartext = event.currentTarget.dataset.klartext;
                putValueInShow(JSON.parse(klartext));
                $(KlartextUI.idFormKlartextAdd).hide();
                $(KlartextUI.idFormKlartextUpdate).hide();
                $(KlartextUI.idFormKlartextImport).hide();
                $(KlartextUI.idKlartextShow).show();
                KlartextUI.modalKlartextTitle.textContent = "Klartext Detail";
                $(KlartextUI.idModalKlartext).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = KlartextUI.idTableKlartext.querySelectorAll(".klartextDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                KlartextUI.idBtnKlartextDelete.dataset.klartext_id = event.currentTarget.dataset.klartext_id;
                $(KlartextUI.idModalKlartextDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (klartext) {
        KlartextUI.id.value = klartext.id;
        KlartextUI.snomedTextUpdate.value = klartext.snomedText;
    };


    let putValueInShow = function (klartext) {
        if (KlartextUI.idSnomedTextShow) {
            KlartextUI.idSnomedTextShow.textContent = klartext.snomedText;
        } else {
            console.error("Element snomedText not found");
        }
    };


    // Klartext Update for submission
    $(KlartextUI.idFormKlartextUpdate).validate({
        rules: {
            snomedText: "required",
        },
        message: {
            snomedText: "snomedText required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "snomedText": form.querySelector("input[name='snomedText']").value,
            };
            KlartextController.updateKlartext(form, requestBody, function () {
                KlartextController.listKlartext(KlartextUI.idTableKlartext);
            });
        }
    });

    KlartextUI.idKlartextTemplate.addEventListener('click', function () {
        KlartextController.exportKlartextTemplate(KlartextUI.idKlartextTemplate, Endpoints.KLARTEXT_TEMPLATE);
    })
    KlartextUI.idKlartextExport.addEventListener('click', function () {
        KlartextController.exportKlartext(KlartextUI.idKlartextExport, Endpoints.KLARTEXT_EXPORT);
    })

    KlartextUI.idKlartextImport.addEventListener('click', function () {
        $(KlartextUI.idFormKlartextUpdate).hide();
        $(KlartextUI.idFormKlartextAdd).hide();
        $(KlartextUI.idKlartextShow).hide();
        $(KlartextUI.idFormKlartextImport).show();
        KlartextUI.modalKlartextTitle.textContent = "Import User Data";
        $(KlartextUI.idModalKlartext).modal("show");
    })
    $(KlartextUI.idFormKlartextImport).validate({
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

            KlartextController.importKlartext(form, requestBody, function () {
                KlartextController.listKlartext(KlartextUI.idTableKlartext);
            });
        }
    });


    // Delete
    KlartextUI.idBtnKlartextDelete.addEventListener("click", function (event) {
        KlartextController.deleteKlartext(KlartextUI.idModalKlartextDelete, event.currentTarget.dataset.klartext_id, function () {
            KlartextController.listKlartext(KlartextUI.idTableKlartext);
        });
    });

    $(KlartextUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(KlartextUI.idModalKlartext).modal("hide");
        $(KlartextUI.idFormKlartextAdd).trigger("reset");
        $(KlartextUI.idFormKlartextUpdate).trigger("reset");
        $(KlartextUI.idModalKlartextDelete).modal("hide");
        $(KlartextUI.modalAlertBody).html("");
    });

});

