'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as MunicipalityUI from "../ui/MunicipalityUI.js";
import * as MunicipalityController from "../controller/MunicipalityController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.MUNICIPALITY);

    if (!CommonUtil.hasAuthority("MUNICIPALITY_C")) {
        $(MunicipalityUI.idBtnPopAddMunicipality).hide();
    } else {
        MunicipalityUI.idBtnPopAddMunicipality.addEventListener("click", function (event) {
            $(MunicipalityUI.idMunicipalityShow).hide();
            $(MunicipalityUI.idFormMunicipalityUpdate).hide();
            $(MunicipalityUI.idFormMunicipalityImport).hide();
            $(MunicipalityUI.idFormMunicipalityAdd).show();
            MunicipalityUI.modalMunicipalityTitle.textContent = "Add Municipality";
            $(MunicipalityUI.idModalMunicipality).modal("show");
        });

        // Municipality add form submission
        $(MunicipalityUI.idFormMunicipalityAdd).validate({
            rules: {
                name: "required"
            },
            messages: {
                year: "Year required"
            },
            submitHandler: function (form) {
                let requestBody = {
                    "name": form.querySelector("input[name='name']").value,
                    "year": form.querySelector("input[name='year']").value,
                };
                MunicipalityController.addMunicipality(form, requestBody, function () {
                    MunicipalityController.listMunicipality(MunicipalityUI.idTableMunicipality);
                });
            }
        });
    }

    MunicipalityController.listMunicipality(MunicipalityUI.idTableMunicipality);

    $(MunicipalityUI.idTableMunicipality).on('draw.dt', function () {
        //Edit Event Listener
        let edits = MunicipalityUI.idTableMunicipality.querySelectorAll(".municipalityEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                console.log("Edit button clicked");
                let municipality = event.currentTarget.dataset.municipality;
                console.log("Municipality data:", municipality);
                try {
                    let parsedMunicipality = JSON.parse(municipality);
                    console.log("Parsed municipality data:", parsedMunicipality);
                    putValueInEditForm(parsedMunicipality);
                } catch (e) {
                    console.error("Error parsing Municipality data:", e);
                }
                $(MunicipalityUI.idMunicipalityShow).hide();
                $(MunicipalityUI.idFormMunicipalityAdd).hide();
                $(MunicipalityUI.idFormMunicipalityImport).hide();
                $(MunicipalityUI.idFormMunicipalityUpdate).show();
                MunicipalityUI.modalMunicipalityTitle.textContent = "Edit Municipality";
                $(MunicipalityUI.idModalMunicipality).modal("show");
            });
        });

        // Show event Listener
        let shows = MunicipalityUI.idTableMunicipality.querySelectorAll(".municipalityShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let municipality = event.currentTarget.dataset.municipality;
                putValueInShow(JSON.parse(municipality));
                $(MunicipalityUI.idFormMunicipalityAdd).hide();
                $(MunicipalityUI.idFormMunicipalityUpdate).hide();
                $(MunicipalityUI.idFormMunicipalityImport).hide();
                $(MunicipalityUI.idMunicipalityShow).show();
                MunicipalityUI.modalMunicipalityTitle.textContent = "Municipality Detail";
                $(MunicipalityUI.idModalMunicipality).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = MunicipalityUI.idTableMunicipality.querySelectorAll(".municipalityDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                MunicipalityUI.idBtnMunicipalityDelete.dataset.municipality_id = event.currentTarget.dataset.municipality_id;
                $(MunicipalityUI.idModalMunicipalityDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (municipality) {
        MunicipalityUI.id.value = municipality.id;
        MunicipalityUI.nameUpdate.value = municipality.name;
        MunicipalityUI.yearUpdate.value = municipality.year;
    };

    let putValueInShow = function (municipality) {
        if (MunicipalityUI.idNameShow) {
            MunicipalityUI.idNameShow.textContent = municipality.name;
        } else {
            console.error("Element name not found");
        }

        if (MunicipalityUI.idYearShow) {
            MunicipalityUI.idYearShow.textContent = municipality.year;
        } else {
            console.error("Element year not found");
        }
    };


    // Municipality Update for submission
    $(MunicipalityUI.idFormMunicipalityUpdate).validate({
        rules: {
            name: "required",
        },
        message: {
            year: "Municipality Year required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "name": form.querySelector("input[name='name']").value,
                "year": form.querySelector("input[name='year']").value,
            };
            MunicipalityController.updateMunicipality(form, requestBody, function () {
                MunicipalityController.listMunicipality(MunicipalityUI.idTableMunicipality);
            });
        }
    });

    MunicipalityUI.idMunicipalityTemplate.addEventListener('click', function () {
        MunicipalityController.exportMunicipalityTemplate(MunicipalityUI.idMunicipalityTemplate, Endpoints.MUNICIPALITY_TEMPLATE);
    })
    MunicipalityUI.idMunicipalityExport.addEventListener('click', function () {
        MunicipalityController.exportMunicipality(MunicipalityUI.idMunicipalityExport, Endpoints.MUNICIPALITY_EXPORT);
    })

    MunicipalityUI.idMunicipalityImport.addEventListener('click', function () {
        $(MunicipalityUI.idFormMunicipalityUpdate).hide();
        $(MunicipalityUI.idFormMunicipalityAdd).hide();
        $(MunicipalityUI.idMunicipalityShow).hide();
        $(MunicipalityUI.idFormMunicipalityImport).show();
        MunicipalityUI.modalMunicipalityTitle.textContent = "Import User Data";
        $(MunicipalityUI.idModalMunicipality).modal("show");
    })
    $(MunicipalityUI.idFormMunicipalityImport).validate({
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

            MunicipalityController.importMunicipality(form, requestBody, function () {
                MunicipalityController.listMunicipality(MunicipalityUI.idTableMunicipality);
            });
        }
    });


    // Delete
    MunicipalityUI.idBtnMunicipalityDelete.addEventListener("click", function (event) {
        MunicipalityController.deleteMunicipality(MunicipalityUI.idModalMunicipalityDelete, event.currentTarget.dataset.municipality_id, function () {
            MunicipalityController.listMunicipality(MunicipalityUI.idTableMunicipality);
        });
    });

    $(MunicipalityUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(MunicipalityUI.idModalMunicipality).modal("hide");
        $(MunicipalityUI.idFormMunicipalityAdd).trigger("reset");
        $(MunicipalityUI.idFormMunicipalityUpdate).trigger("reset");
        $(MunicipalityUI.idModalMunicipalityDelete).modal("hide");
        $(MunicipalityUI.modalAlertBody).html("");
    });

});

