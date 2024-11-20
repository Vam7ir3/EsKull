'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as ParishUI from "../ui/ParishUI.js";
import * as ParishController from "../controller/ParishController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.MUNICIPALITY, ["name"], ParishUI.idSelectMunicipalityId);

    if (!CommonUtil.hasAuthority("PARISH_C")) {
        $(ParishUI.idBtnPopAddParish).hide();
    } else {
        ParishUI.idBtnPopAddParish.addEventListener("click", function (event) {
            $(ParishUI.idParishShow).hide();
            $(ParishUI.idFormParishUpdate).hide();
            $(ParishUI.idFormParishImport).hide();
            $(ParishUI.idFormParishAdd).show();
            SelectPickerUtil.populateSelectPicker(Endpoints.MUNICIPALITY, ["name"], ParishUI.municipalityIdAdd);
            SelectPickerUtil.populateSelectPicker(Endpoints.COUNTY, ["name"], ParishUI.countyIdAdd);
            // SelectPickerUtil.populateSelectPicker(Endpoints.MUNICIPALITY, ["name"], ParishUI.municipalityIdUpdate);
            // SelectPickerUtil.populateSelectPicker(Endpoints.COUNTY, ["name"], ParishUI.countyIdUpdate);
            ParishUI.modalParishTitle.textContent = "Add Parish";
            $(ParishUI.idModalParish).modal("show");
        });

        // Parish add form submission
        $(ParishUI.idFormParishAdd).validate({
            rules: {
                name: "required"
            },
            messages: {
                name: "required"

            },
            submitHandler: function (form) {
                event.preventDefault();
                let requestBody = {
                    "name": form.querySelector("input[name='name']").value,
                    "registerDate": form.querySelector("input[name='registerDate']").value,
                    "dividedOtherCounty": form.querySelector("input[name='dividedOtherCounty']").value,
                    ...($(ParishUI.municipalityIdAdd).val() && {
                        municipalityId: $(ParishUI.municipalityIdAdd).val()
                    }),
                    ...($(ParishUI.countyIdAdd).val() && {
                        countyId: $(ParishUI.countyIdAdd).val()
                    }),
                };
                ParishController.addParish(form, requestBody, function (response) {
                    console.log('Add parish response:', response);
                    if (response.success) {
                        ParishController.listParish(ParishUI.idTableParish, Endpoints.PARISH);
                    }
                });
            }
        });
    }

    ParishController.listParish(ParishUI.idTableParish, Endpoints.PARISH);

    $(ParishUI.idTableParish).on('draw.dt', function () {
        //Edit Event Listener
        let edits = ParishUI.idTableParish.querySelectorAll(".parishEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                console.log("Edit button clicked");
                let parish = event.currentTarget.dataset.parish;
                console.log("Parish data:", parish);

                if (!parish) {
                    console.error("parish data is missing!");
                    return;
                }
                putValueInEditForm(JSON.parse(parish));
                $(ParishUI.idParishShow).hide();
                $(ParishUI.idFormParishAdd).hide();
                $(ParishUI.idFormParishImport).hide();
                $(ParishUI.idFormParishUpdate).show();
                ParishUI.modalParishTitle.textContent = "Edit Parish";
                $(ParishUI.idModalParish).modal("show");
            });
        });

        // Show event Listener
        let shows = ParishUI.idTableParish.querySelectorAll(".parishShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let parish = event.currentTarget.dataset.parish;
                putValueInShow(JSON.parse(parish));
                $(ParishUI.idFormParishAdd).hide();
                $(ParishUI.idFormParishUpdate).hide();
                $(ParishUI.idFormParishImport).hide();
                $(ParishUI.idParishShow).show();
                ParishUI.modalParishTitle.textContent = "Parish Detail";
                $(ParishUI.idModalParish).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = ParishUI.idTableParish.querySelectorAll(".parishDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                ParishUI.idBtnParishDelete.dataset.parish_id = event.currentTarget.dataset.parish_id;
                $(ParishUI.idModalParishDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (parish) {
        ParishUI.id.value = parish.id;
        ParishUI.name.value = parish.name;
        ParishUI.registerDate.value = parish.registerDate;
        ParishUI.dividedOtherCounty.value = parish.dividedOtherCounty;

        // let municipalityId = parish.municipalityId && parish.municipalityId.id ? parish.municipalityId.id : null;

        let municipalityId = null;
        if (parish.municipalityId) {
            // Direct ID reference
            municipalityId = typeof parish.municipalityId === 'object' ?
                parish.municipalityId.id : parish.municipalityId;
        } else if (parish.municipalityRes) {
            // Response object reference
            municipalityId = parish.municipalityRes.id;
        }

        console.log('Selected municipalityId:', municipalityId);

        SelectPickerUtil.populateSelectPicker(
            Endpoints.MUNICIPALITY,
            ["name"],
            ParishUI.municipalityIdUpdate,
            municipalityId
        );

        $(ParishUI.municipalityIdUpdate).selectpicker('refresh');

        let countyId = parish.countyId && parish.countyId.id ? parish.countyId.id : null;

        SelectPickerUtil.populateSelectPicker(
            Endpoints.COUNTY,
            ["name"],
            ParishUI.countyIdUpdate,
            countyId
        );
    };


    let putValueInShow = function (parish) {
        if (ParishUI.idNameShow) {
            ParishUI.idNameShow.textContent = parish.name;
        } else {
            console.error("Element idNameShow not found");
        }

        if (ParishUI.idRegisterDateShow) {
            ParishUI.idRegisterDateShow.textContent = parish.registerDate;
        } else {
            console.error("Element idRegisterDateShow not found");
        }

        if (ParishUI.idDividedOtherCountyShow) {
            ParishUI.idDividedOtherCountyShow.textContent = parish.dividedOtherCounty;
        } else {
            console.error("Element idDividedOtherCountyShow not found");
        }

        if (ParishUI.idMunicipalityIdShow && ParishUI.idCountyIdShow) {
            let municipalityId = parish.municipalityRes
                ? `${parish.municipalityRes.id}`
                : 'N/A';
            ParishUI.idMunicipalityIdShow.textContent = municipalityId;

            let countyId = parish.countyRes
                ? `${parish.countyRes.id}`
                : 'N/A';

            console.log("County:", countyId);

            ParishUI.idCountyIdShow.textContent = countyId;
        } else {
            console.error("Element idMunicipalityIdShow or idCountyIdShow not found");
        }
    };

    $(ParishUI.idFormParishUpdate).validate({
        rules: {
            name: "required",
        },
        message: {
            name: "Parish required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "name": form.querySelector("input[name='name']").value,
                "registerDate": form.querySelector("input[name='registerDate']").value,
                "dividedOtherCounty": form.querySelector("input[name='dividedOtherCounty']").value,
                municipalityId: $(ParishUI.municipalityIdUpdate).val(),
                countyId: $(ParishUI.countyIdUpdate).val(),
            };
            ParishController.updateParish(form, requestBody, function () {
                ParishController.listParish(ParishUI.idTableParish);
            });
        }
    });

    ParishUI.idParishTemplate.addEventListener('click', function () {
        ParishController.exportParishTemplate(ParishUI.idParishTemplate, Endpoints.PARISH_TEMPLATE);
    })
    ParishUI.idParishExport.addEventListener('click', function () {
        ParishController.exportParish(ParishUI.idParishExport, Endpoints.PARISH_EXPORT);
    })

    ParishUI.idParishImport.addEventListener('click', function () {
        $(ParishUI.idFormParishUpdate).hide();
        $(ParishUI.idFormParishAdd).hide();
        $(ParishUI.idParishShow).hide();
        $(ParishUI.idFormParishImport).show();
        ParishUI.modalParishTitle.textContent = "Import User Data";
        $(ParishUI.idModalParish).modal("show");
    })
    $(ParishUI.idFormParishImport).validate({
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

            ParishController.importParish(form, requestBody, function () {
                ParishController.listParish(ParishUI.idTableParish);
            });
        }
    });


    // Delete
    ParishUI.idBtnParishDelete.addEventListener("click", function (event) {
        ParishController.deleteParish(ParishUI.idModalParishDelete, event.currentTarget.dataset.parish_id, function () {
            ParishController.listParish(ParishUI.idTableParish);
        });
    });


    $(ParishUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(ParishUI.idModalParish).modal("hide");
        $(ParishUI.idFormParishAdd).trigger("reset");
        $(ParishUI.idFormParishUpdate).trigger("reset");
        $(ParishUI.idModalParishDelete).modal("hide");
        $(ParishUI.modalAlertBody).html("");
    });

});

