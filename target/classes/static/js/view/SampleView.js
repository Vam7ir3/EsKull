'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as SampleUI from "../ui/SampleUI.js";
import * as SampleController from "../controller/SampleController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(
        EndPoints.PERSON,
        ["pnr"],
        SampleUI.idSelectPerson,
    );

    if (!CommonUtil.hasAuthority("SAMPLE_C")) {
        $(SampleUI.idBtnPopAddSample).hide();
    } else {
        SampleUI.idBtnPopAddSample.addEventListener("click", function (event) {
            $(SampleUI.idSampleShow).hide();
            $(SampleUI.idFormSampleUpdate).hide();
            $(SampleUI.idFormSampleImport).hide();
            $(SampleUI.idFormSampleAdd).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.PERSON, ["pnr"], SampleUI.samplePersonAdd);
            SampleUI.modalSampleTitle.textContent = "Add Sample";
            $(SampleUI.idModalSample).modal("show");
        });

        // Sample add form submission
        $(SampleUI.idFormSampleAdd).validate({
            rules: {
                type: "required",
            },
            messages: {
                type: "type required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    personId: $(SampleUI.samplePersonAdd).val(),
                    "type" : form.querySelector("input[name='type']").value,
                };
                SampleController.addSample(form, requestBody, function () {
                    SampleController.listSample(SampleUI.idTableSample, EndPoints.PERSON_SAMPLE);
                });
            }
        });
    }

    SampleController.listSample(SampleUI.idTableSample, EndPoints.PERSON_SAMPLE);

    $(SampleUI.idTableSample).on('draw.dt', function () {

        //Edit Event Listener
        let edits = SampleUI.idTableSample.querySelectorAll(".sampleEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let sample = event.currentTarget.dataset.sample;
                console.log("Sample data attribute:", sample);
                if (!sample) {
                    console.error("Sample data is missing!");
                    return;
                }

                putValueInEditForm(JSON.parse(sample));
                $(SampleUI.idSampleShow).hide();
                $(SampleUI.idFormSampleAdd).hide();
                $(SampleUI.idFormSampleImport).hide();
                $(SampleUI.idFormSampleUpdate).show();
                SampleUI.modalSampleTitle.textContent = "Edit Sample";
                $(SampleUI.idModalSample).modal("show");
            });
        });

        // Show event Listener
        let shows = SampleUI.idTableSample.querySelectorAll(".sampleShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let sample = JSON.parse(event.currentTarget.dataset.sample);
                putValueInShow(sample);
                $(SampleUI.idFormSampleAdd).hide();
                $(SampleUI.idFormSampleUpdate).hide();
                $(SampleUI.idFormSampleImport).hide();
                $(SampleUI.idSampleShow).show();
                if (SampleUI.idPersonShow) {
                    SampleUI.idPersonShow.textContent = sample.personId ? sample.personId.name : 'N/A';
                } else {
                    console.error("Element idPersonShow not found.");
                }
                if (SampleUI.modalSampleTitle) {
                    SampleUI.modalSampleTitle.textContent = "Sample Detail";
                } else {
                    console.error("Element modalSampleTitle not found.");
                }
                $(SampleUI.idModalSample).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = SampleUI.idTableSample.querySelectorAll(".sampleDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                SampleUI.idBtnSampleDelete.dataset.sample_id = event.currentTarget.dataset.sample_id;
                $(SampleUI.idModalSampleDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (sample) {
        console.log("Sample data for edit:", sample);

        SampleUI.id.value = sample.id;
        SampleUI.typeUpdate.value = sample.sampleRes.type;

        let personId = sample.personRes && sample.personRes.id ? sample.personRes.id : null;

        SelectPickerUtil.populateSelectPicker(
            EndPoints.PERSON,
            ["pnr"],
            SampleUI.personUpdate,
            personId
        );

        try {
            $(SampleUI.personUpdate).selectpicker('refresh');
        } catch (error) {
            console.error("Error refreshing selectpicker:", error);
            console.log("SampleUI.personUpdate:", SampleUI.personUpdate);
            console.log("jQuery version:", $.fn.jquery);
            console.log("Bootstrap version:", $.fn.tooltip.Constructor.VERSION);
            console.log("Bootstrap-select version:", $.fn.selectpicker.Constructor.VERSION);
        }
    };



    $(SampleUI.idTableSample).on('click', '.sampleShow', function(event) {
        let sample = JSON.parse(event.currentTarget.dataset.sample);
        console.log("Sample data on show button click:", sample); // Log data
        putValueInShow(sample);
    });


    let putValueInShow = function (sample) {
        console.log("Sample data received:", sample);

        if (SampleUI.idPersonPnrShow && SampleUI.idSampletypeShow) {
            // Person name
            let personName = sample.personRes
                ? `${sample.personRes.pnr}`
                : 'N/A';
            SampleUI.idPersonPnrShow.textContent = personName;

            // Sample name (adjusted to access sampleRes.name)
            let sampleName = sample.sampleRes && sample.sampleRes.type
                ? sample.sampleRes.type
                : 'N/A';

            console.log("Sample type:", sampleName);

            // Set the sample type
            SampleUI.idSampleTypeShow.textContent = sampleName;
        } else {
            console.error("Element idPersonPnrShow or idSampleTypeShow not found");
        }
    };
    


    // Sample Update for submission
    $(SampleUI.idFormSampleUpdate).validate({
        rules: {
            type: "required",
        },
        messages: {
            type: "Sample Type required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                personId: $(SampleUI.personUpdate).val(), // Ensure this gets the updated value
                "type": form.querySelector("input[name='type']").value,
            };
            SampleController.updateSample(form, requestBody, function () {
                SampleController.listSample(SampleUI.idTableSample);
            });
        }
    });

    SampleUI.idSampleTemplate.addEventListener('click', function () {
        SampleController.exportSampleTemplate(SampleUI.idSampleTemplate, EndPoints.SAMPLE_TEMPLATE);
    })
    SampleUI.idSampleExport.addEventListener('click', function () {
        SampleController.exportSample(SampleUI.idSampleExport, EndPoints.SAMPLE_EXPORT);
    })
    SampleUI.idSampleImport.addEventListener('click', function () {
        $(SampleUI.idFormSampleUpdate).hide();
        $(SampleUI.idFormSampleAdd).hide();
        $(SampleUI.idSampleShow).hide();
        $(SampleUI.idFormSampleImport).show();
        SampleUI.modalSampleTitle.textContent = "Import User Data";
        $(SampleUI.idModalSample).modal("show");
    })
    $(SampleUI.idFormSampleImport).validate({
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

            SampleController.importSample(form, requestBody, function () {
                SampleController.listSample(SampleUI.idTableSample);
            });
        }
    });


    $(SampleUI.idSelectPerson).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedPersonIds = $(this).val();
        SampleController.filterByPerson(SampleUI.idTableSample, selectedPersonIds);
    });


    // Delete
    SampleUI.idBtnSampleDelete.addEventListener("click", function (event) {
        SampleController.deleteSample(SampleUI.idModalSampleDelete, event.currentTarget.dataset.sample_id, function () {
            SampleController.listSample(SampleUI.idTableSample, EndPoints.PERSON_SAMPLE);
        });
    });

    $(SampleUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(SampleUI.idModalSample).modal("hide");
        $(SampleUI.idFormSampleAdd).trigger("reset");
        $(SampleUI.idFormSampleUpdate).trigger("reset");
        $(SampleUI.idModalSampleDelete).modal("hide");
        $(SampleUI.modalAlertBody).html("");
    });

});
