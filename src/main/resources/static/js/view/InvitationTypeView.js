'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as Endpoints from "../controller/EndPoints.js";
import * as InvitationTypeUI from "../ui/InvitationTypeUI.js";
import * as InvitationTypeController from "../controller/InvitationTypeController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    SelectPickerUtil.populateSelectPicker(Endpoints.INVITATIONTYPE);

    if (!CommonUtil.hasAuthority("INVITATIONTYPE_C")) {
        $(InvitationTypeUI.idBtnPopAddInvitationType).hide();
    } else {
        InvitationTypeUI.idBtnPopAddInvitationType.addEventListener("click", function (event) {
            $(InvitationTypeUI.idInvitationTypeShow).hide();
            $(InvitationTypeUI.idFormInvitationTypeUpdate).hide();
            $(InvitationTypeUI.idFormInvitationTypeImport).hide();
            $(InvitationTypeUI.idFormInvitationTypeAdd).show();
            InvitationTypeUI.modalInvitationTypeTitle.textContent = "Add InvitationType";
            $(InvitationTypeUI.idModalInvitationType).modal("show");
        });

        // InvitationType add form submission
        $(InvitationTypeUI.idFormInvitationTypeAdd).validate({
            rules: {
                type: "required"
            },
            messages: {
                type: "type required"
            },
            submitHandler: function (form) {
                let requestBody = {
                    "type": form.querySelector("input[name='type']").value,
                    "xtype": form.querySelector("input[name='xtype']").value,
                    "description": form.querySelector("input[name='description']").value,
                };
                InvitationTypeController.addInvitationType(form, requestBody, function () {
                    InvitationTypeController.listInvitationType(InvitationTypeUI.idTableInvitationType);
                });
            }
        });
    }

    InvitationTypeController.listInvitationType(InvitationTypeUI.idTableInvitationType);

    $(InvitationTypeUI.idTableInvitationType).on('draw.dt', function () {
        //Edit Event Listener
        let edits = InvitationTypeUI.idTableInvitationType.querySelectorAll(".invitationTypeEdit");
        console.log("Found edit buttons:", edits.length);

        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                console.log("Edit button clicked");
                try {
                    const invitationTypeData = event.currentTarget.getAttribute('data-invitation-type');
                    console.log("Raw InvitationType data:", invitationTypeData);

                    const parsedInvitationType = JSON.parse(invitationTypeData);
                    console.log("Parsed InvitationType data:", parsedInvitationType);
                    putValueInEditForm(parsedInvitationType);

                    $(InvitationTypeUI.idInvitationTypeShow).hide();
                    $(InvitationTypeUI.idFormInvitationTypeAdd).hide();
                    $(InvitationTypeUI.idFormInvitationTypeImport).hide();
                    $(InvitationTypeUI.idFormInvitationTypeUpdate).show();
                    InvitationTypeUI.modalInvitationTypeTitle.textContent = "Edit InvitationType";
                    $(InvitationTypeUI.idModalInvitationType).modal("show");
                } catch (e) {
                    console.error("Error processing InvitationType data:", e);
                    AlertMessageUtil.alertMessage({
                        success: false,
                        message: "Error loading invitation type data. Please try again."
                    });
                }
            });
        });

        // Show event Listener
        let shows = InvitationTypeUI.idTableInvitationType.querySelectorAll(".invitationTypeShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let invitationType = event.currentTarget.getAttribute('data-invitation-type');
                putValueInShow(JSON.parse(invitationType));
                $(InvitationTypeUI.idFormInvitationTypeAdd).hide();
                $(InvitationTypeUI.idFormInvitationTypeUpdate).hide();
                $(InvitationTypeUI.idFormInvitationTypeImport).hide();
                $(InvitationTypeUI.idInvitationTypeShow).show();
                InvitationTypeUI.modalInvitationTypeTitle.textContent = "InvitationType Detail";
                $(InvitationTypeUI.idModalInvitationType).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = InvitationTypeUI.idTableInvitationType.querySelectorAll(".invitationTypeDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                InvitationTypeUI.idBtnInvitationTypeDelete.dataset.invitationType_id = event.currentTarget.getAttribute('data-invitation-type_id');
                $(InvitationTypeUI.idModalInvitationTypeDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (invitationType) {
        InvitationTypeUI.id.value = invitationType.id;
        InvitationTypeUI.typeUpdate.value = invitationType.type;
        InvitationTypeUI.xTypeUpdate.value = invitationType.xtype;
        InvitationTypeUI.descriptionUpdate.value = invitationType.description;
    };

    let putValueInShow = function (invitationType) {
        if (InvitationTypeUI.idTypeShow) {
            InvitationTypeUI.idTypeShow.textContent = invitationType.type;
        } else {
            console.error("Element type not found");
        }
        if (InvitationTypeUI.idXtypeShow) {
            InvitationTypeUI.idXtypeShow.textContent = invitationType.xtype;
        } else {
            console.error("Element xtype not found");
        }
        if (InvitationTypeUI.idDescriptionShow) {
            InvitationTypeUI.idDescriptionShow.textContent = invitationType.description;
        } else {
            console.error("Element description not found");
        }
    };

    // InvitationType Update for submission
    $(InvitationTypeUI.idFormInvitationTypeUpdate).validate({
        rules: {
            type: "required",
        },
        messages: {
            type: "type required",
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "type": form.querySelector("input[name='type']").value,
                "xtype": form.querySelector("input[name='xtype']").value,
                "description": form.querySelector("input[name='description']").value,
            };
            InvitationTypeController.updateInvitationType(form, requestBody, function () {
                InvitationTypeController.listInvitationType(InvitationTypeUI.idTableInvitationType);
            });
        }
    });

    InvitationTypeUI.idInvitationTypeTemplate.addEventListener('click', function () {
        InvitationTypeController.exportInvitationTypeTemplate(InvitationTypeUI.idInvitationTypeTemplate, Endpoints.INVITATIONTYPE_TEMPLATE);
    });

    InvitationTypeUI.idInvitationTypeExport.addEventListener('click', function () {
        InvitationTypeController.exportInvitationType(InvitationTypeUI.idInvitationTypeExport, Endpoints.INVITATIONTYPE_EXPORT);
    });

    InvitationTypeUI.idInvitationTypeImport.addEventListener('click', function () {
        $(InvitationTypeUI.idFormInvitationTypeUpdate).hide();
        $(InvitationTypeUI.idFormInvitationTypeAdd).hide();
        $(InvitationTypeUI.idInvitationTypeShow).hide();
        $(InvitationTypeUI.idFormInvitationTypeImport).show();
        InvitationTypeUI.modalInvitationTypeTitle.textContent = "Import User Data";
        $(InvitationTypeUI.idModalInvitationType).modal("show");
    });

    $(InvitationTypeUI.idFormInvitationTypeImport).validate({
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

            InvitationTypeController.importInvitationType(form, requestBody, function () {
                InvitationTypeController.listInvitationType(InvitationTypeUI.idTableInvitationType);
            });
        }
    });

    // Delete
    InvitationTypeUI.idBtnInvitationTypeDelete.addEventListener("click", function (event) {
        InvitationTypeController.deleteInvitationType(InvitationTypeUI.idModalInvitationTypeDelete, event.currentTarget.dataset.invitationType_id, function () {
            InvitationTypeController.listInvitationType(InvitationTypeUI.idTableInvitationType);
        });
    });

    $(InvitationTypeUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(InvitationTypeUI.idModalInvitationType).modal("hide");
        $(InvitationTypeUI.idFormInvitationTypeAdd).trigger("reset");
        $(InvitationTypeUI.idFormInvitationTypeUpdate).trigger("reset");
        $(InvitationTypeUI.idModalInvitationTypeDelete).modal("hide");
        $(InvitationTypeUI.modalAlertBody).html("");
    });
});