'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as PersonUI from "../ui/PersonUI.js";
import * as PersonController from "../controller/PersonController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";



$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.PERSON, "isValidPNR", PersonUI.idSelectValidPnr);

    if (!CommonUtil.hasAuthority("PERSON_C")) {
        $(PersonUI.idBtnPopAddPerson).hide();
    } else {
        PersonUI.idBtnPopAddPerson.addEventListener("click", function (event) {
            $(PersonUI.idPersonShow).hide();
            $(PersonUI.idFormPersonUpdate).hide();
            $(PersonUI.idFormPersonImport).hide();
            $(PersonUI.idFormPersonAdd).show();
            PersonUI.modalPersonTitle.textContent = "Add Person";
            $(PersonUI.idModalPerson).modal("show");
        });

        // Person add form submission
        $(PersonUI.idFormPersonAdd).validate({
            rules: {
                pnr: "required"
            },
            messages: {
                pnr: "Pnr required"

            },
            submitHandler: function (form) {
                let requestBody = {
                    "pnr": form.querySelector("input[name='pnr']").value,
                    "dateOfBirth": form.querySelector("input[name='dateOfBirth']").value,
                    "isValidPNR": form.querySelector("input[name='isValidPNR']").checked,
                };
                PersonController.addPerson(form, requestBody, function () {
                    PersonController.listPerson(PersonUI.idTablePerson);
                });
            }
        });
    }

    PersonController.listPerson(PersonUI.idTablePerson);

    $(PersonUI.idTablePerson).on('draw.dt', function () {
        //Edit Event Listener
        let edits = PersonUI.idTablePerson.querySelectorAll(".personEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                console.log("Edit button clicked");
                let person = event.currentTarget.dataset.person;
                console.log("Person data:", person);
                try {
                    let parsedPerson = JSON.parse(person);
                    console.log("Parsed person data:", parsedPerson);
                    putValueInEditForm(parsedPerson);
                } catch (e) {
                    console.error("Error parsing person data:", e);
                }
                $(PersonUI.idPersonShow).hide();
                $(PersonUI.idFormPersonAdd).hide();
                $(PersonUI.idFormPersonImport).hide();
                $(PersonUI.idFormPersonUpdate).show();
                PersonUI.modalPersonTitle.textContent = "Edit Person";
                $(PersonUI.idModalPerson).modal("show");
            });
        });

        // Show event Listener
        let shows = PersonUI.idTablePerson.querySelectorAll(".personShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let person = event.currentTarget.dataset.person;
                putValueInShow(JSON.parse(person));
                $(PersonUI.idFormPersonAdd).hide();
                $(PersonUI.idFormPersonUpdate).hide();
                $(PersonUI.idFormPersonImport).hide();
                $(PersonUI.idPersonShow).show();
                PersonUI.modalPersonTitle.textContent = "Person Detail";
                $(PersonUI.idModalPerson).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = PersonUI.idTablePerson.querySelectorAll(".personDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                PersonUI.idBtnPersonDelete.dataset.person_id = event.currentTarget.dataset.person_id;
                $(PersonUI.idModalPersonDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (person) {
        PersonUI.id.value = person.id;
        PersonUI.pnrUpdate.value = person.pnr;
        PersonUI.dateOfBirthUpdate.value = person.dateOfBirth;
        PersonUI.isValidPNRUpdate.checked = person.isValidPNR;
    };


    let putValueInShow = function (person) {
        if (PersonUI.idPnrShow) {
            PersonUI.idPnrShow.textContent = person.pnr;
        } else {
            console.error("Element idPnrShow not found");
        }

        if (PersonUI.idDateOfBirthShow) {
            PersonUI.idDateOfBirthShow.textContent = person.dateOfBirth;
        } else {
            console.error("Element idDateOfBirthShow not found");
        }

        if (PersonUI.idIsValidPNRShow) {
            PersonUI.idIsValidPNRShow.textContent = person.isValidPNR;
        } else {
            console.error("Element idIsValidPNRShow not found");
        }
    };


    // Person Update for submission
    $(PersonUI.idFormPersonUpdate).validate({
        rules: {
            name: "required",
        },
        message: {
            name: "Person Pnr required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "pnr": form.querySelector("input[name='pnr']").value,
                "dateOfBirth": form.querySelector("input[name='dateOfBirth']").value,
                "isValidPNR": form.querySelector("input[name='isValidPNR']").checked,
            };
            PersonController.updatePerson(form, requestBody, function () {
                PersonController.listPerson(PersonUI.idTablePerson);
            });
        }
    });

    PersonUI.idPersonTemplate.addEventListener('click', function () {
        PersonController.exportPersonTemplate(PersonUI.idPersonTemplate, Endpoints.PERSON_TEMPLATE);
    })
    PersonUI.idPersonExport.addEventListener('click', function () {
        PersonController.exportPerson(PersonUI.idPersonExport, Endpoints.PERSON_EXPORT);
    })

    PersonUI.idPersonImport.addEventListener('click', function () {
        $(PersonUI.idFormPersonUpdate).hide();
        $(PersonUI.idFormPersonAdd).hide();
        $(PersonUI.idPersonShow).hide();
        $(PersonUI.idFormPersonImport).show();
        PersonUI.modalPersonTitle.textContent = "Import User Data";
        $(PersonUI.idModalPerson).modal("show");
    })
    $(PersonUI.idFormPersonImport).validate({
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

            PersonController.importPerson(form, requestBody, function () {
                PersonController.listPerson(PersonUI.idTablePerson);
            });
        }
    });


    // Delete
    PersonUI.idBtnPersonDelete.addEventListener("click", function (event) {
        PersonController.deletePerson(PersonUI.idModalPersonDelete, event.currentTarget.dataset.person_id, function () {
            PersonController.listPerson(PersonUI.idTablePerson);
        });
    });

    // PersonUI.idIsValidPNR.addEventListener('change', function () {
    //     let validPnrValue = $(PersonUI.idIsValidPNR).val();
    //     PersonController.filterByValidPnr(PersonUI.idTablePerson, validPnrValue);
    // });

    $(PersonUI.idSelectValidPnr).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedValue = $(this).val();
        let filterValue = null;

        if (selectedValue !== '') {
            filterValue = selectedValue === 'true';
        }

        PersonController.filterByValidPnr(PersonUI.idTablePerson, filterValue);
    });


    // PersonUI.idIsValidPNR.addEventListener('change', function (event) {
    //     PersonController.filterByValidPnr(PersonUI.idTablePerson, $(PersonUI.idIsValidPNR).val());
    // });
    $(PersonUI.idSelectValidPnr).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedValue = $(this).val();
        let filterValue = null;

        if (selectedValue === '') {
            filterValue = selectedValue === 'true';
        }
        PersonController.filterByValidPnr(PersonUI.idTablePerson, filterValue);
    });

    $(PersonUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(PersonUI.idModalPerson).modal("hide");
        $(PersonUI.idFormPersonAdd).trigger("reset");
        $(PersonUI.idFormPersonUpdate).trigger("reset");
        $(PersonUI.idModalPersonDelete).modal("hide");
        $(PersonUI.modalAlertBody).html("");
    });

});

