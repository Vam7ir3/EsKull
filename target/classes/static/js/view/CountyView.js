'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as CountyUI from "../ui/CountyUI.js";
import * as CountyController from "../controller/CountyController.js";

$(document).ready(function () {
    CommonUtil.initialSetup();
    if (!CommonUtil.hasAuthority("COUNTY_C")) {
        $(CountyUI.idBtnPopAddCounty).hide();
    } else {
        CountyUI.idBtnPopAddCounty.addEventListener("click", function (event) {
            $(CountyUI.idCountyShow).hide();
            $(CountyUI.idFormCountyUpdate).hide();
            $(CountyUI.idFormCountyImport).hide();
            $(CountyUI.idFormCountyAdd).show();
            CountyUI.modalCountyTitle.textContent = "Add County";
            $(CountyUI.idModalCounty).modal("show");
        });

        $(CountyUI.idFormCountyAdd).validate({
            rules: {
                firstName: "required",
            },
            messages: {
                firstName: "First Name required",
            },
            submitHandler: function (form) {
                let requestBody = {
                    "name": form.querySelector("input[name='name']").value,
                };
                CountyController.addCounty(form, requestBody, function () {
                    CountyController.listCounty(CountyUI.idTableCounty);
                });
            }
        });
    }

    CountyController.listCounty(CountyUI.idTableCounty);

    $(CountyUI.idTableCounty).on('draw.dt', function () {
        //Edit Event Listener
        let edits = CountyUI.idTableCounty.querySelectorAll(".countyEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let county = event.currentTarget.dataset.county;
                putValueInEditForm(JSON.parse(county));
                $(CountyUI.idCountyShow).hide();
                $(CountyUI.idFormCountyAdd).hide();
                $(CountyUI.idFormCountyImport).hide();
                $(CountyUI.idFormCountyUpdate).show();
                CountyUI.modalCountyTitle.textContent = "Edit County";
                $(CountyUI.idModalCounty).modal("show");
            });
        });

        // Show event Listener
        let shows = CountyUI.idTableCounty.querySelectorAll(".countyShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let county = event.currentTarget.dataset.county;
                putValueInShow(JSON.parse(county));
                $(CountyUI.idFormCountyAdd).hide();
                $(CountyUI.idFormCountyUpdate).hide();
                $(CountyUI.idFormCountyImport).hide();
                $(CountyUI.idCountyShow).show();
                CountyUI.modalCountyTitle.textContent = "County Detail";
                $(CountyUI.idModalCounty).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = CountyUI.idTableCounty.querySelectorAll(".countyDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                CountyUI.idBtnCountyDelete.dataset.county_id = event.currentTarget.dataset.county_id;
                $(CountyUI.idModalCountyDelete).modal("show");
            });
        });

        let navigates = CountyUI.idTableCounty.querySelectorAll(".countyLabAdd");
        Array.from(navigates).forEach(function (element) {
            element.addEventListener('click', function (event) {
                const countyId = event.currentTarget.dataset.county_id;
                localStorage.setItem("selectedCountyId", (JSON.parse(countyId)));
                localStorage.setItem("countyDetails",(JSON.parse(countyId)).name );
                window.location = Endpoints.BASE_URL + "/countyLab.html";
            });
        });
    });

    let putValueInEditForm = function (county) {
        ``
        CountyUI.id.value = county.id;
        CountyUI.nameUpdate.value = county.name;
    };

    let putValueInShow = function (county) {
        if (CountyUI.idNameShow) {
            CountyUI.idNameShow.textContent = county.name;
        } else {
            console.error("Element idNameShow not found");
        }
    };

    // County Update for submission
    $(CountyUI.idFormCountyUpdate).validate({
        rules: {
            name: "required",
        },
        message: {
            name: "County Name required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "name": form.querySelector("input[name='name']").value,
            };
            CountyController.updateCounty(form, requestBody, function () {
                CountyController.listCounty(CountyUI.idTableCounty);
            });
        }
    });

    CountyUI.idCountyTemplate.addEventListener('click', function () {
        CountyController.exportCountyTemplate(CountyUI.idCountyTemplate, Endpoints.COUNTY_TEMPLATE);
    })
    CountyUI.idCountyExport.addEventListener('click', function () {
        CountyController.exportCounty(CountyUI.idCountyExport, Endpoints.COUNTY_EXPORT);
    })
    CountyUI.idCountyImport.addEventListener('click', function () {
        $(CountyUI.idFormCountyUpdate).hide();
        $(CountyUI.idFormCountyAdd).hide();
        $(CountyUI.idCountyShow).hide();
        $(CountyUI.idFormCountyImport).show();
        CountyUI.modalCountyTitle.textContent = "Import User Data";
        $(CountyUI.idModalCounty).modal("show");
    })
    $(CountyUI.idFormCountyImport).validate({
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

            CountyController.importCounty(form, requestBody, function () {
                CountyController.listCounty(CountyUI.idTableCounty);
            });
        }
    });


    // Delete
    CountyUI.idBtnCountyDelete.addEventListener("click", function (event) {
        CountyController.deleteCounty(CountyUI.idModalCountyDelete, event.currentTarget.dataset.county_id, function () {
            CountyController.listCounty(CountyUI.idTableCounty);
        });
    });

    $(CountyUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(CountyUI.idModalCounty).modal("hide");
        $(CountyUI.idFormCountyAdd).trigger("reset");
        $(CountyUI.idFormCountyUpdate).trigger("reset");
        $(CountyUI.idModalCountyDelete).modal("hide");
        $(CountyUI.modalAlertBody).html("");
    });

});