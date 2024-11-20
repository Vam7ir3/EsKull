'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as CellUI from "../ui/CellUI.js";
import * as CellController from "../controller/CellController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(
        EndPoints.PERSON,
        ["pnr"],
        CellUI.idSelectPerson,
    );

    if (!CommonUtil.hasAuthority("CELL_C")) {
        $(CellUI.idBtnPopAddCell).hide();
    } else {
        CellUI.idBtnPopAddCell.addEventListener("click", function (event) {
            $(CellUI.idCellShow).hide();
            $(CellUI.idFormCellUpdate).hide();
            $(CellUI.idFormCellImport).hide();
            $(CellUI.idFormCellAdd).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.PERSON, ["pnr"], CellUI.cellPersonAdd);
            CellUI.modalCellTitle.textContent = "Add Cell";
            $(CellUI.idModalCell).modal("show");
        });

        // Cell add form submission
        $(CellUI.idFormCellAdd).validate({
            rules: {
                name: "required",
            },
            messages: {
                name: "Name required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    personId: $(CellUI.cellPersonAdd).val(),
                    "name" : form.querySelector("input[name='name']").value,
                };
                CellController.addCell(form, requestBody, function () {
                    CellController.listCell(CellUI.idTableCell, EndPoints.PERSON_CELL);
                });
            }
        });
    }

    CellController.listCell(CellUI.idTableCell, EndPoints.PERSON_CELL);

    $(CellUI.idTableCell).on('draw.dt', function () {

        //Edit Event Listener
        let edits = CellUI.idTableCell.querySelectorAll(".cellEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let cell = event.currentTarget.dataset.cell;
                console.log("Cell data attribute:", cell); // Log the cell data to ensure it's present
                if (!cell) {
                    console.error("Cell data is missing!");
                    return;
                }

                putValueInEditForm(JSON.parse(cell));
                $(CellUI.idCellShow).hide();
                $(CellUI.idFormCellAdd).hide();
                $(CellUI.idFormCellImport).hide();
                $(CellUI.idFormCellUpdate).show();
                CellUI.modalCellTitle.textContent = "Edit Cell";
                $(CellUI.idModalCell).modal("show");
            });
        });


        // Show event Listener
        let shows = CellUI.idTableCell.querySelectorAll(".cellShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let cell = JSON.parse(event.currentTarget.dataset.cell);
                putValueInShow(cell);
                $(CellUI.idFormCellAdd).hide();
                $(CellUI.idFormCellUpdate).hide();
                $(CellUI.idFormCellImport).hide();
                $(CellUI.idCellShow).show();
                if (CellUI.idPersonShow) {
                    CellUI.idPersonShow.textContent = cell.personId ? cell.personId.name : 'N/A';
                } else {
                    console.error("Element idPersonShow not found.");
                }
                if (CellUI.modalCellTitle) {
                    CellUI.modalCellTitle.textContent = "Cell Detail";
                } else {
                    console.error("Element modalCellTitle not found.");
                }
                $(CellUI.idModalCell).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = CellUI.idTableCell.querySelectorAll(".cellDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                CellUI.idBtnCellDelete.dataset.cell_id = event.currentTarget.dataset.cell_id;
                $(CellUI.idModalCellDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (cell) {
        console.log("Cell data for edit:", cell);

        CellUI.id.value = cell.id;
        CellUI.nameUpdate.value = cell.cellRes.name;

        let personId = cell.personRes && cell.personRes.id ? cell.personRes.id : null;

        SelectPickerUtil.populateSelectPicker(
            EndPoints.PERSON,
            ["pnr"],
            CellUI.personUpdate,
            personId
        );

        try {
            $(CellUI.personUpdate).selectpicker('refresh');
        } catch (error) {
            console.error("Error refreshing selectpicker:", error);
            console.log("CellUI.personUpdate:", CellUI.personUpdate);
            console.log("jQuery version:", $.fn.jquery);
            console.log("Bootstrap version:", $.fn.tooltip.Constructor.VERSION);
            console.log("Bootstrap-select version:", $.fn.selectpicker.Constructor.VERSION);
        }
    };



    $(CellUI.idTableCell).on('click', '.cellShow', function(event) {
        let cell = JSON.parse(event.currentTarget.dataset.cell);
        console.log("Cell data on show button click:", cell); // Log data
        putValueInShow(cell);
    });

    let putValueInShow = function (cell) {
        console.log("Cell data received:", cell);  // Log the full Cell data

        if (CellUI.idPersonNameShow && CellUI.idCellNameShow) {
            // Person name
            let personName = cell.personRes
                ? `${cell.personRes.pnr} `
                : 'N/A';
            CellUI.idPersonNameShow.textContent = personName;

            // Cell name (adjusted to access CellRes.name)
            let cellName = cell.cellRes && cell.cellRes.name
                ? cell.cellRes.name
                : 'N/A';

            console.log("Cell name:", cellName);  // Log the cell name to ensure it's fetched correctly

            // Set the cell name
            CellUI.idCellNameShow.textContent = cellName;
        } else {
            console.error("Element idPersonNameShow or idCellNameShow not found");
        }
    };





    // Cell Update for submission
    $(CellUI.idFormCellUpdate).validate({
        rules: {
            name: "required",
        },
        messages: {
            name: "Cell Name required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                personId: $(CellUI.personUpdate).val(), // Ensure this gets the updated value
                "name": form.querySelector("input[name='name']").value,
            };
            CellController.updateCell(form, requestBody, function () {
                CellController.listCell(CellUI.idTableCell, EndPoints.PERSON_CELL);
            });
        }
    });


    CellUI.idCellTemplate.addEventListener('click', function () {
        CellController.exportCellTemplate(CellUI.idCellTemplate, EndPoints.CELL_TEMPLATE);
    })
    CellUI.idCellExport.addEventListener('click', function () {
        CellController.exportCell(CellUI.idCellExport, EndPoints.CELL_EXPORT);
    })
    CellUI.idCellImport.addEventListener('click', function () {
        $(CellUI.idFormCellUpdate).hide();
        $(CellUI.idFormCellAdd).hide();
        $(CellUI.idCellShow).hide();
        $(CellUI.idFormCellImport).show();
        CellUI.modalCellTitle.textContent = "Import User Data";
        $(CellUI.idModalCell).modal("show");
    })
    $(CellUI.idFormCellImport).validate({
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

            CellController.importCell(form, requestBody, function () {
                CellController.listCell(CellUI.idTableCell);
            });
        }
    });



    $(CellUI.idSelectPerson).on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
        let selectedPersonIds = $(this).val();
        CellController.filterByPerson(CellUI.idTableCell, selectedPersonIds);
    });


    // Delete
    CellUI.idBtnCellDelete.addEventListener("click", function (event) {
        CellController.deleteCell(CellUI.idModalCellDelete, event.currentTarget.dataset.cell_id, function () {
            CellController.listCell(CellUI.idTableCell, EndPoints.PERSON_CELL);
        });
    });

    $(CellUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(CellUI.idModalCell).modal("hide");
        $(CellUI.idFormCellAdd).trigger("reset");
        $(CellUI.idFormCellUpdate).trigger("reset");
        $(CellUI.idModalCellDelete).modal("hide");
        $(CellUI.modalAlertBody).html("");
    });

});
