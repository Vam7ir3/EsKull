'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as DistrictUI from "../ui/DistrictUI.js";
import * as DistrictController from "../controller/DistrictController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import {districtNameUpdate, districtUpdate, idDistrictNameShow} from "../ui/DistrictUI.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.DISTRICT);

    if (!CommonUtil.hasAuthority("DISTRICT_C")) {
        $(DistrictUI.idBtnPopAddDistrict).hide();
    } else {
        DistrictUI.idBtnPopAddDistrict.addEventListener("click", function (event) {
            $(DistrictUI.idDistrictShow).hide();
            $(DistrictUI.idFormDistrictUpdate).hide();
            $(DistrictUI.idFormDistrictImport).hide();
            $(DistrictUI.idFormDistrictAdd).show();
            DistrictUI.modalDistrictTitle.textContent = "Add District";
            $(DistrictUI.idModalDistrict).modal("show");
        });

        // District add form submission
        $(DistrictUI.idFormDistrictAdd).validate({
            rules: {
                district: "required"
            },
            messages: {
                district: "district required"

            },
            submitHandler: function (form) {

                let requestBody = {
                    "district": form.querySelector("input[name='district']").value,
                    "districtName": form.querySelector("input[name='districtName']").value,
                };
                DistrictController.addDistrict(form, requestBody, function () {
                    DistrictController.listDistrict(DistrictUI.idTableDistrict);
                });
            }
        });
    }

    DistrictController.listDistrict(DistrictUI.idTableDistrict);

    $(DistrictUI.idTableDistrict).on('draw.dt', function () {
        //Edit Event Listener
        let edits = DistrictUI.idTableDistrict.querySelectorAll(".districtEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                console.log("Edit button clicked");
                let district = event.currentTarget.dataset.district;
                console.log("District data:", district);
                try {
                    let parsedDistrict = JSON.parse(district);
                    console.log("Parsed district data:", parsedDistrict);
                    putValueInEditForm(parsedDistrict);
                } catch (e) {
                    console.error("Error parsing District data:", e);
                }
                $(DistrictUI.idDistrictShow).hide();
                $(DistrictUI.idFormDistrictAdd).hide();
                $(DistrictUI.idFormDistrictImport).hide();
                $(DistrictUI.idFormDistrictUpdate).show();
                DistrictUI.modalDistrictTitle.textContent = "Edit District";
                $(DistrictUI.idModalDistrict).modal("show");
            });
        });

        // Show event Listener
        let shows = DistrictUI.idTableDistrict.querySelectorAll(".districtShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let district = event.currentTarget.dataset.district;
                putValueInShow(JSON.parse(district));
                $(DistrictUI.idFormDistrictAdd).hide();
                $(DistrictUI.idFormDistrictUpdate).hide();
                $(DistrictUI.idFormDistrictImport).hide();
                $(DistrictUI.idDistrictShow).show();
                DistrictUI.modalDistrictTitle.textContent = "District Detail";
                $(DistrictUI.idModalDistrict).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = DistrictUI.idTableDistrict.querySelectorAll(".districtDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                DistrictUI.idBtnDistrictDelete.dataset.district_id = event.currentTarget.dataset.district_id;
                $(DistrictUI.idModalDistrictDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (district) {
        DistrictUI.id.value = district.id;
        DistrictUI.districtUpdate.value = district.district;
        DistrictUI.districtNameUpdate.value = district.districtName;
    };


    let putValueInShow = function (district) {
        if (DistrictUI.idNameShow) {
            DistrictUI.idNameShow.textContent = district.district;
        } else {
            console.error("Element district not found");
        }
        if (DistrictUI.idDistrictNameShow) {
            DistrictUI.idDistrictNameShow.textContent = district.districtName;
        } else {
            console.error("Element districtName not found");
        }
    };


    // District Update for submission
    $(DistrictUI.idFormDistrictUpdate).validate({
        rules: {
            district: "required",
        },
        message: {
            district: "district required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "district": form.querySelector("input[name='district']").value,
                "districtName": form.querySelector("input[name='districtName']").value,
            };
            DistrictController.updateDistrict(form, requestBody, function () {
                DistrictController.listDistrict(DistrictUI.idTableDistrict);
            });
        }
    });

    DistrictUI.idDistrictTemplate.addEventListener('click', function () {
        DistrictController.exportDistrictTemplate(DistrictUI.idDistrictTemplate, Endpoints.DISTRICT_TEMPLATE);
    })
    DistrictUI.idDistrictExport.addEventListener('click', function () {
        DistrictController.exportDistrict(DistrictUI.idDistrictExport, Endpoints.DISTRICT_EXPORT);
    })

    DistrictUI.idDistrictImport.addEventListener('click', function () {
        $(DistrictUI.idFormDistrictUpdate).hide();
        $(DistrictUI.idFormDistrictAdd).hide();
        $(DistrictUI.idDistrictShow).hide();
        $(DistrictUI.idFormDistrictImport).show();
        DistrictUI.modalDistrictTitle.textContent = "Import User Data";
        $(DistrictUI.idModalDistrict).modal("show");
    })
    $(DistrictUI.idFormDistrictImport).validate({
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

            DistrictController.importDistrict(form, requestBody, function () {
                DistrictController.listDistrict(DistrictUI.idTableDistrict);
            });
        }
    });


    // Delete
    DistrictUI.idBtnDistrictDelete.addEventListener("click", function (event) {
        DistrictController.deleteDistrict(DistrictUI.idModalDistrictDelete, event.currentTarget.dataset.district_id, function () {
            DistrictController.listDistrict(DistrictUI.idTableDistrict);
        });
    });

    $(DistrictUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(DistrictUI.idModalDistrict).modal("hide");
        $(DistrictUI.idFormDistrictAdd).trigger("reset");
        $(DistrictUI.idFormDistrictUpdate).trigger("reset");
        $(DistrictUI.idModalDistrictDelete).modal("hide");
        $(DistrictUI.modalAlertBody).html("");
    });

});

