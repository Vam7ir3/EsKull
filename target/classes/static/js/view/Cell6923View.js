'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Cell6923UI from "../ui/Cell6923UI.js";
import * as Cell6923Controller from "../controller/Cell6923Controller.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(
        EndPoints.PERSON,
        ["pnr"],
        Cell6923UI.idSelectPerson,
    );

    if (!CommonUtil.hasAuthority("CELL_C")) {
        $(Cell6923UI.idBtnPopAddCell6923).hide();
    } else {
        Cell6923UI.idBtnPopAddCell6923.addEventListener("click", function (event) {
            $(Cell6923UI.idCell6923Show).hide();
            $(Cell6923UI.idFormCell6923Update).hide();
            $(Cell6923UI.idFormCell6923Import).hide();
            $(Cell6923UI.idFormCell6923Add).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.PERSON, ["pnr"], Cell6923UI.cell6923PersonAdd);
            SelectPickerUtil.populateSelectPicker(EndPoints.LABORATORY, ["name"], Cell6923UI.cell6923LaboratoryAdd);
            SelectPickerUtil.populateSelectPicker(EndPoints.COUNTY, ["name"], Cell6923UI.cell6923CountyAdd);
            SelectPickerUtil.populateSelectPicker(EndPoints.REFERENCETYPE, ["type"], Cell6923UI.cell6923ReferralTypeAdd);
            Cell6923UI.modalCell6923Title.textContent = "Add Cell6923";
            $(Cell6923UI.idModalCell6923).modal("show");
        });

        // Cell6923 add form submission
        $(Cell6923UI.idFormCell6923Add).validate({
            rules: {
                personId: "required",
            },
            messages: {
                personId: "personId required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    personId: $(Cell6923UI.cell6923PersonAdd).val(),
                    laboratoryId: $(Cell6923UI.cell6923LaboratoryAdd).val(),
                    countyId: $(Cell6923UI.cell6923CountyAdd).val(),
                    "sampleDate": form.querySelector("input[name='sampleDate']").value,
                    "sampleType": form.querySelector("input[name='sampleType']").value,
                    "referralNumber": form.querySelector("input[name='referralNumber']").value,
                    referenceTypeId: $(Cell6923UI.cell6923ReferralTypeAdd).val(),
                    "referenceSite": form.querySelector("input[name='referenceSite']").value,
                    "residc": form.querySelector("input[name='residc']").value,
                    "residk": form.querySelector("input[name='residk']").value,
                    "xSampleDate": form.querySelector("input[name='xSampleDate']").value,
                    "xRegistrationDate": form.querySelector("input[name='xRegistrationDate']").value,
                    "xSnomed": form.querySelector("input[name='xSnomed']").value,
                    "diagId": form.querySelector("input[name='diagId']").value,
                    "ansClinic": form.querySelector("input[name='ansClinic']").value,
                    "debClinic": form.querySelector("input[name='debClinic']").value,
                    "remClinic": form.querySelector("input[name='remClinic']").value,
                    "registrationDate": form.querySelector("input[name='registrationDate']").value,
                    "scrType": form.querySelector("input[name='scrType']").value,
                    "snomed": form.querySelector("input[name='snomed']").value,
                    "responseDate": form.querySelector("input[name='responseDate']").value,
                    "xResponseDate": form.querySelector("input[name='xresponseDate']").value,
                    "diffDays": form.querySelector("input[name='diffDays']").value,

                };

                console.log("this is for testing:", $(Cell6923UI.cell6923ReferralTypeAdd).val(),)
                Cell6923Controller.addCell6923(form, requestBody, function () {
                    Cell6923Controller.listCell6923(Cell6923UI.idTableCell6923, EndPoints.CELL6923);
                });
            }
        });
    }

    Cell6923Controller.listCell6923(Cell6923UI.idTableCell6923, EndPoints.CELL6923);

    $(Cell6923UI.idTableCell6923).on('draw.dt', function () {
        //Edit Event Listener
        let edits = Cell6923UI.idTableCell6923.querySelectorAll(".cell6923Edit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let cell6923 = event.currentTarget.dataset.cell6923;
                console.log("Cell6923 data attribute:", cell6923);
                if (!cell6923) {
                    console.error("Cell6923 data is missing!");
                    return;
                }

                putValueInEditForm(JSON.parse(cell6923));
                $(Cell6923UI.idCell6923Show).hide();
                $(Cell6923UI.idFormCell6923Add).hide();
                $(Cell6923UI.idFormCell6923Import).hide();
                $(Cell6923UI.idFormCell6923Update).show();
                Cell6923UI.modalCell6923Title.textContent = "Edit Cell6923";
                $(Cell6923UI.idModalCell6923).modal("show");
            });
        });

        // Show event Listener
        let shows = Cell6923UI.idTableCell6923.querySelectorAll(".cell6923Show");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let cell6923 = JSON.parse(event.currentTarget.dataset.cell6923);
                putValueInShow(cell6923);
                $(Cell6923UI.idFormCell6923Add).hide();
                $(Cell6923UI.idFormCell6923Update).hide();
                $(Cell6923UI.idFormCell6923Import).hide();
                $(Cell6923UI.idCell6923Show).show();
                if (Cell6923UI.idPersonIdShow) {
                    Cell6923UI.idPersonIdShow.textContent = cell6923.personId ? cell6923.personId.pnr : 'N/A';
                } else {
                    console.error("Element idPersonIdShow not found.");
                }
                if (Cell6923UI.idLaboratoryIdShow) {
                    Cell6923UI.idLaboratoryIdShow.textContent = cell6923.laboratoryId ? cell6923.laboratoryId.name : 'N/A';
                } else {
                    console.error("Element idLaboratoryIdShow not found.");
                }
                if (Cell6923UI.idCountyShow) {
                    Cell6923UI.idCountyShow.textContent = cell6923.countyId ? cell6923.countyId.name : 'N/A';
                } else {
                    console.error("Element idPersonShow not found.");
                }
                if (Cell6923UI.modalCell6923Title) {
                    Cell6923UI.modalCell6923Title.textContent = "Cell6923 Detail";
                } else {
                    console.error("Element modalCell6923Title not found.");
                }
                $(Cell6923UI.idModalCell6923).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = Cell6923UI.idTableCell6923.querySelectorAll(".cell6923Delete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                Cell6923UI.idBtnCell6923Delete.dataset.cell6923_id = event.currentTarget.dataset.cell6923_id;
                $(Cell6923UI.idModalCell6923Delete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (cell6923) {
        console.log("Cell6923 data for edit:", cell6923);

        Cell6923UI.id.value = cell6923.id;
        Cell6923UI.sampleDateUpdate.value = cell6923.sampleDate;
        Cell6923UI.sampleTypeUpdate.value = cell6923.sampleType;
        Cell6923UI.referralNumberUpdate = cell6923.referralNumber;
        Cell6923UI.referenceSiteUpdate = cell6923.referenceSite;
        Cell6923UI.residcUpdate = cell6923.residc;
        Cell6923UI.residkUpdate = cell6923.residk;
        Cell6923UI.xSampleDateUpdate = cell6923.xSampleDate;
        Cell6923UI.xRegistrationDateUpdate = cell6923.xRegistrationDate;
        Cell6923UI.xSnomedUpdate = cell6923.xSnomed;
        Cell6923UI.diagIdUpdate = cell6923.diagId;
        Cell6923UI.ansClinicUpdate = cell6923.ansClinic;
        Cell6923UI.debClinicUpdate = cell6923.debClinic;
        Cell6923UI.remClinicUpdate = cell6923.remClinic;
        Cell6923UI.registrationDateUpdate = cell6923.registrationDate;
        Cell6923UI.scrTypeUpdate = cell6923.scrType;
        Cell6923UI.snomedUpdate = cell6923.snomed;
        Cell6923UI.responseDateUpdate = cell6923.responseDate;
        Cell6923UI.xResponseDateUpdate = cell6923.xResponseDate;
        Cell6923UI.diffDaysUpdate = cell6923.diffDays;

        let personId = cell6923.personId && cell6923.personId.id ? cell6923.personId.id : null;
        let laboratoryId = cell6923.laboratoryRes && cell6923.laboratoryRes.id ? cell6923.laboratoryRes.id : null;
        let countyId = cell6923.countyRes && cell6923.countyRes.id ? cell6923.countyRes.id : null;
        let referenceType = cell6923.referenceType ? cell6923.referenceType : null;


        SelectPickerUtil.populateSelectPicker(
            EndPoints.PERSON,
            ["pnr"],
            Cell6923UI.cell6923PersonUpdate,
            personId
        );
        SelectPickerUtil.populateSelectPicker(
            EndPoints.LABORATORY,
            ["name"],
            Cell6923UI.cell6923LaboratoryUpdate,
            laboratoryId
        );
        SelectPickerUtil.populateSelectPicker(
            EndPoints.COUNTY,
            ["name"],
            Cell6923UI.cell6923CountyUpdate,
            countyId
        );
        SelectPickerUtil.populateSelectPicker(
            EndPoints.REFERENCETYPE,
            ["type"],
            Cell6923UI.cell6923ReferralTypeUpdate,
            referenceType
        );

        // try {
        //     $(Cell6923UI.personUpdate).selectpicker('refresh');
        // } catch (error) {
        //     console.error("Error refreshing selectpicker:", error);
        //     console.log("SampleUI.personUpdate:", SampleUI.personUpdate);
        //     console.log("jQuery version:", $.fn.jquery);
        //     console.log("Bootstrap version:", $.fn.tooltip.Constructor.VERSION);
        //     console.log("Bootstrap-select version:", $.fn.selectpicker.Constructor.VERSION);
        // }
    };


    $(Cell6923UI.idTableCell6923).on('click', '.cell6923Show', function (event) {
        let cell6923 = JSON.parse(event.currentTarget.dataset.cell6923);
        console.log("Cell6923 data on show button click:", cell6923); // Log data
        putValueInShow(cell6923);
    });


    let putValueInShow = function (cell6923) {
        console.log("Cell6923 data received:", cell6923);
        console.log("person data checking", cell6923.personId);

        Cell6923UI.idPersonIdShow.textContent = cell6923.personId ? cell6923.personId.pnr : 'N/A';
        Cell6923UI.idLaboratoryIdShow.textContent = cell6923.laboratoryId ? cell6923.laboratoryId.name : 'N/A';
        Cell6923UI.idCountyShow.textContent = cell6923.countyId ? cell6923.countyId.name : 'N/A';
        Cell6923UI.idSampleDateShow.textContent = cell6923.sampleDate;
        Cell6923UI.idSampleTypeShow.textContent = cell6923.sampleType;
        Cell6923UI.idReferralNumberShow.textContent = cell6923.referralNumber;
        Cell6923UI.idReferralTypeShow.textContent = cell6923.referenceTypeRes ? cell6923.referenceTypeRes.type : 'N/A';
        Cell6923UI.idReferenceSiteShow.textContent = cell6923.referenceSite;
        Cell6923UI.idResidcShow.textContent = cell6923.residc;
        Cell6923UI.idResidkShow.textContent = cell6923.residk;
        Cell6923UI.idXSampleDateShow.textContent = cell6923.xSampleDate;
        Cell6923UI.idXResponseDateShow.textContent = cell6923.xResponseDate;
        Cell6923UI.idXSnomedShow.textContent = cell6923.xSnomed;
        Cell6923UI.idDiagIdShow.textContent = cell6923.diagId;
        Cell6923UI.idAnsClinicShow.textContent = cell6923.ansClinic;
        Cell6923UI.idDebClinicShow.textContent = cell6923.debClinic;
        Cell6923UI.idRemClinicShow.textContent = cell6923.remClinic;
        Cell6923UI.idRegistrationDateShow.textContent = cell6923.registrationDate;
        Cell6923UI.idScrTypeShow.textContent = cell6923.scrType;
        Cell6923UI.idSnomedShow.textContent = cell6923.snomed;
        Cell6923UI.idResponseDateShow.textContent = cell6923.responseDate;
        Cell6923UI.idXResponseDateShow.textContent = cell6923.xResponseDate;
        Cell6923UI.idDiffDaysShow.textContent = cell6923.diffDays;
    };


    // Cell6923 Update for submission
    $(Cell6923UI.idFormCell6923Update).validate({
        rules: {
            personId: "required",
        },
        messages: {
            personId: "Person required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                personId: $(Cell6923UI.cell6923PersonUpdate).val(),
                laboratoryId: $(Cell6923UI.cell6923LaboratoryUpdate).val(),
                countyId: $(Cell6923UI.cell6923CountyUpdate).val(),
                "sampleDate": form.querySelector("input[name='sampleDate']").value,
                "sampleType": form.querySelector("input[name='sampleType']").value,
                "referralNumber": form.querySelector("input[name='referralNumber']").value,
                referenceTypeId: $(Cell6923UI.cell6923ReferralTypeAdd).val(),
                "referenceSite": form.querySelector("input[name='referenceSite']").value,
                "residc": form.querySelector("input[name='residc']").value,
                "residk": form.querySelector("input[name='residk']").value,
                "xSampleDate": form.querySelector("input[name='xSampleDate']").value,
                "xRegistrationDate": form.querySelector("input[name='xRegistrationDate']").value,
                "diagId": form.querySelector("input[name='diagId']").value,
                "ansClinic": form.querySelector("input[name='ansClinic']").value,
                "debClinic": form.querySelector("input[name='debClinic']").value,
                "remClinic": form.querySelector("input[name='remClinic']").value,
                "registrationDate": form.querySelector("input[name='registrationDate']").value,
                "scrType": form.querySelector("input[name='scrType']").value,
                "snomed": form.querySelector("input[name='snomed']").value,
                "responseDate": form.querySelector("input[name='responseDate']").value,
                "xResponseDate": form.querySelector("input[name='xResponseDate']").value,
                "diffDays": form.querySelector("input[name='diffDays']").value,

            };
            Cell6923Controller.updateCell6923(form, requestBody, function () {
                Cell6923Controller.listCell6923(Cell6923UI.idTableCell6923);
            });
        }
    });

    Cell6923UI.idCell6923Template.addEventListener('click', function () {
        Cell6923Controller.exportCell6923Template(Cell6923UI.idCell6923Template, EndPoints.CELL6923_TEMPLATE);
    })
    Cell6923UI.idCell6923Export.addEventListener('click', function () {
        Cell6923Controller.exportCell6923(Cell6923UI.idCell6923Export, EndPoints.CELL6923_EXPORT);
    })
    Cell6923UI.idCell6923Import.addEventListener('click', function () {
        $(Cell6923UI.idFormCell6923Update).hide();
        $(Cell6923UI.idFormCell6923Add).hide();
        $(Cell6923UI.idCell6923Show).hide();
        $(Cell6923UI.idFormCell6923Import).show();
        Cell6923UI.modalCell6923Title.textContent = "Import User Data";
        $(Cell6923UI.idModalCell6923).modal("show");
    })
    $(Cell6923UI.idFormCell6923Import).validate({
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

            Cell6923Controller.importCell6923(form, requestBody, function () {
                Cell6923Controller.listCell6923(Cell6923UI.idTableCell6923);
            });
        }
    });


    $(Cell6923UI.idSelectPerson).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedPersonIds = $(this).val();
        Cell6923Controller.filterByPerson(Cell6923UI.idTableCell6923, selectedPersonIds);
    });


    // Delete
    Cell6923UI.idBtnCell6923Delete.addEventListener("click", function (event) {
        Cell6923Controller.deleteCell6923(Cell6923UI.idModalCell6923Delete, event.currentTarget.dataset.cell6923_id, function () {
            Cell6923Controller.listCell6923(Cell6923UI.idTableCell6923, EndPoints.CELL6923);
        });
    });

    $(Cell6923UI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(Cell6923UI.idModalCell6923).modal("hide");
        $(Cell6923UI.idFormCell6923Add).trigger("reset");
        $(Cell6923UI.idFormCell6923Update).trigger("reset");
        $(Cell6923UI.idModalCell6923Delete).modal("hide");
        $(Cell6923UI.modalAlertBody).html("");
    });
});
