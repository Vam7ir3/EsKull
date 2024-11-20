'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as AuthorityController from "../controller/AuthorityController.js";
import {AuthorityModel} from "../model/AuthorityModel.js";
import * as AuthorityUI from "../ui/AuthorityUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();
    //
    // if (!CommonUtil.hasAuthority("Authority (Create)")) {
    //     $(AuthorityUI.idBtnPopAddAuthority).hide();
    // } else {
    //     AuthorityUI.idBtnPopAddAuthority.addEventListener("click", function (event) {
    //         $(AuthorityUI.idFormAuthorityUpdate).hide();
    //         $(AuthorityUI.idFormAuthorityAdd).show();
    //         AuthorityUI.modalAuthorityTitle.textContent = "Add Authority";
    //         $(AuthorityUI.idModalAuthority).modal("show");
    //     });
    //
    //     $(AuthorityUI.idFormAuthorityAdd).validate({
    //         rules: {
    //             title: "required"
    //         },
    //         messages: {
    //             title: "Title required."
    //         },
    //         submitHandler: function (form) {
    //             AuthorityController.addAuthority(form,
    //                 new AuthorityModel(null, form.querySelector("input[name='title']").value), function () {
    //                     AuthorityController.listAuthority(AuthorityUI.idTableAuthority);
    //                 });
    //         }
    //     });
    // }

    AuthorityController.listAuthority(AuthorityUI.idTableAuthority);
    $(AuthorityUI.idTableAuthority).on('draw.dt', function () {
        let authorityEdit = AuthorityUI.idTableAuthority.querySelectorAll(".authorityEdit");
        Array.from(authorityEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let authority = event.currentTarget.dataset.authority;
                putValueInEditForm(JSON.parse(authority));
                $(AuthorityUI.idFormAuthorityAdd).hide();
                $(AuthorityUI.idFormAuthorityUpdate).show();
                AuthorityUI.modalAuthorityTitle.textContent = "Edit Authority";
                $(AuthorityUI.idModalAuthority).modal("show");
            });
        });
    });

    let putValueInEditForm = function (role) {
        AuthorityUI.id.value = role.id;
        AuthorityUI.titleUpdate.value = role.description;
    };

    $(AuthorityUI.idFormAuthorityUpdate).validate({
        rules: {
            title: "required"
        },
        messages: {
            title: "Title required."
        },
        submitHandler: function (form) {
            AuthorityController.updateAuthority(form,
                new AuthorityModel(form.querySelector("input[name='id']").value, AuthorityUI.titleUpdate.value),
                function () {
                    AuthorityController.listAuthority(AuthorityUI.idTableAuthority);
                }
            );
        }
    });

    $(AuthorityUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(AuthorityUI.idModalAuthority).modal("hide");
        $(AuthorityUI.idFormAuthorityAdd).trigger("reset");
        $(AuthorityUI.idFormAuthorityUpdate).trigger("reset");
        $(AuthorityUI.modalAlertBody).html("");
    });
});