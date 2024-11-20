'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as CountyLabUI from "../ui/CountyLabUI.js";
import * as CountyLabController from "../controller/CountyLabController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.LAB, ["name"], CountyLabUI.idSelectLab, null, "All");


    if (!CommonUtil.hasAuthority("COUNTYLAB_C")) {
        $(CountyLabUI.idAddLabForCounty).hide();
    } else {
        CountyLabUI.idAddLabForCounty.addEventListener("click", function (event) {
           event.preventDefault();

           const selectedLab = $(CountyLabUI.idSelectLab).val();
           const selectedCountyId = localStorage.getItem("selectedCountyId");

           const requestBody = {
               "countyId": selectedCountyId,
               "labId": selectedLab,
           };

           CountyLabController.addCountyLab(this, requestBody, function (){
               $(CountyLabUI.idSelectLab).val("All").selectpicker("refresh");

               CountyLabController.listCountyLab(CountyLabUI.idTableCountyLab);
           })

        });
        // County add form submission
        $(CountyLabUI.idAddLabForCounty).validate({
            rules: {
                labId: "required",
            },
            messages: {
                labId: "Lab Id required",
            },

            submitHandler: function (form) {
                let selected = $(CountyLabUI.idSelectLab).val();
                let selectedCountyId = localStorage.getItem("selectedCountyId");

                const requestBody = {
                    "countyId": selectedCountyId,
                    "labId": selected,
                };

                CountyLabController.addCountyLab(form, requestBody, function () {
                    $(CountyLabUI.idSelectLab).val("All").selectpicker("refresh");
                    CountyLabController.listCountyLab(CountyLabUI.idTableCountyLab);
                });
            }
        });
    }

    CountyLabController.listCountyLab(CountyLabUI.idTableCountyLab);

    $(CountyLabUI.idTableCountyLab).on('draw.dt', function () {
        //Edit Event Listener
        let edits = CountyLabUI.idTableCountyLab.querySelectorAll(".countyLabEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let countyLab = event.currentTarget.dataset.county;
                console.log("countyLab", countyLab);
                putValueInEditForm(countyLab);
                $(CountyLabUI.idCountyShow).hide();
                // $(CountyLabUI.idFormCountyAdd).hide();
                // $(CountyLabUI.idFormCountyImport).hide();
                $(CountyLabUI.idFormCountyLabUpdate).show();
                CountyLabUI.modalCountyLabTitle.textContent = "Edit County Lab";
                $(CountyLabUI.idModalCountyLab).modal("show");
            });
        });


        // Show event Listener
        let shows = CountyLabUI.idTableCountyLab.querySelectorAll(".countyLabShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let countyLab = event.currentTarget.dataset.countyLab;
                putValueInShow(JSON.parse(countyLab));
                // $(CountyLabUI.idFormCountyLabAdd).hide();
                $(CountyLabUI.idFormCountyLabUpdate).hide();
                // $(CountyLabUI.idFormCountyImport).hide();
                $(CountyLabUI.idCountyLabShow).show();
                CountyLabUI.modalCountyLabTitle.textContent = "County Lab Detail";
                $(CountyLabUI.idModalCountyLab).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = CountyLabUI.idTableCountyLab.querySelectorAll(".countyLabDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                CountyLabUI.idBtnCountyLabDelete.dataset.county_id = event.currentTarget.dataset.county_id;
                $(CountyLabUI.idModalCountyLabDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (countyLab) {
        const data = JSON.parse(countyLab)
        CountyLabUI.idCountyShow.value = countyLab.county;
        CountyLabUI.idLabShow.value = countyLab.lab;
        CountyLabUI.idIsInUseShow.value = countyLab.isInUse;
    };

    let putValueInShow = function (countyLab) {
        if (CountyLabUI.idCountyShow) {
            CountyLabUI.idCountyShow.textContent = countyLab.countyId.name;
        } else {
            console.error("Element County not found");
        }
        if (CountyLabUI.idLabShow) {
            CountyLabUI.idLabShow.textContent = countyLab.labRes.name;
        } else {
            console.error("Element lab not found");
        }
        if (CountyLabUI.idIsInUseShow) {
            CountyLabUI.idIsInUseShow.textContent = countyLab.labRes.isInUse;
        } else {
            console.error("Element isInuse not found");
        }
    };

    // County Update for submission
    // $(CountyLabUI.idFormCountyLabUpdate).validate({
    //     rules: {
    //         name: "required",
    //     },
    //     message: {
    //         name: "CountyLab Name required",
    //     },
    //     submitHandler: function (form) {
    //         let requestBody = {
    //             "id": form.querySelector("input[name='id']").value,
    //             "name": form.querySelector("input[name='name']").value,
    //         };
    //         CountyController.updateCounty(form, requestBody, function () {
    //             CountyController.listCounty(CountyLabUI.idTableCounty);
    //         });
    //     }
    // });

    // CountyLabUI.idCountyTemplate.addEventListener('click', function () {
    //     CountyController.exportCountyTemplate(CountyLabUI.idCountyTemplate, Endpoints.COUNTY_TEMPLATE);
    // })
    // CountyLabUI.idCountyExport.addEventListener('click', function () {
    //     CountyController.exportCounty(CountyLabUI.idCountyExport, Endpoints.COUNTY_EXPORT);
    // })

    // CountyLabUI.idCountyImport.addEventListener('click', function () {
    //     $(CountyLabUI.idFormCountyUpdate).hide();
    //     $(CountyLabUI.idFormCountyAdd).hide();
    //     $(CountyLabUI.idCountyShow).hide();
    //     $(CountyLabUI.idFormCountyImport).show();
    //     CountyLabUI.modalCountyTitle.textContent = "Import User Data";
    //     $(CountyLabUI.idModalCounty).modal("show");
    // })
    // $(CountyLabUI.idFormCountyImport).validate({
    //     rules: {
    //         excelFile: "required"
    //     },
    //     messages: {
    //         excelFile: "Please select an excel file to import data."
    //     },
    //     submitHandler: function (form) {
    //
    //         let requestBody = new FormData();
    //         let fileField = form.querySelector("input[type='file']");
    //         requestBody.append('excelFile', fileField.files[0]);
    //
    //         CountyController.importCounty(form, requestBody, function () {
    //             CountyController.listCounty(CountyLabUI.idTableCounty);
    //         });
    //     }
    // });

    // Delete
    CountyLabUI.idBtnCountyLabDelete.addEventListener("click", function (event) {
        CountyLabController.deleteCountyLab(CountyLabUI.idModalCountyLabDelete, event.currentTarget.dataset.county_id, function () {
            CountyLabController.listCountyLab(CountyLabUI.idTableCountyLab);
        });
    });


    $(CountyLabUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(CountyLabUI.idModalCountyLab).modal("hide");
        // $(CountyLabUI.idFormCountyAdd).trigger("reset");
        $(CountyLabUI.idFormCountyLabUpdate).trigger("reset");
        $(CountyLabUI.idModalCountyLabDelete).modal("hide");
        $(CountyLabUI.modalAlertBody).html("");
    });
});