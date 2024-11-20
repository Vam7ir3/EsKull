import * as EndPoints from "../controller/EndPoints.js";
export let addNavMenu = function () {
    let navContainer = document.querySelector("#sidebar");
    navContainer.innerHTML =
        '<div class="sidebar-header">' +
        ' <h4><a href="' + EndPoints.BASE_URL + '#" id="linkDashboard"><img alt="NKCX Icon" height="100px" id="covid19Icon" src="/images/logo.png"></a></h4>' +
        '</div>' +

        '<ul class="list-unstyled components">' +
        '            <p class="btn btn-link"><i class="fas fa-user fa-2x"></i> <a href="#" id="linkProfile"><span' +
        '                    id="loggedInUser"></span></a></p>' +


        '            <li id="linkHomeContainer">' +
        '              <a href="#" id="linkHome">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-home\'></i></div>' +
        '                    <div class="col-10">Home</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkPersonContainer">' +
        '              <a href="#" id="linkPerson">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-user\'></i></div>' +
        '                    <div class="col-10">Person</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +


        '            <li id="linkSampleContainer">' +
        '              <a href="#" id="linkSample">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-vial\'></i></div>' +
        '                    <div class="col-10">Sample</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkHpvContainer">' +
        '              <a href="#" id="linkHpv">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-microscope\'></i></div>' +
        '                    <div class="col-10">Hpv</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkExtHpvContainer">' +
        '              <a href="#" id="linkExtHpv">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-microscope\'></i></div>' +
        '                    <div class="col-10">Ext Hpv</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +


        '            <li id="linkCellContainer">' +
        '              <a href="#" id="linkCell">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-dna\'></i></div>' +
        '                    <div class="col-10">Cell</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkCell6923Container">' +
        '              <a href="#" id="linkCell6923">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-dna\'></i></div>' +
        '                    <div class="col-10">Cell6923</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkMunicipalityContainer">' +
        '              <a href="#" id="linkMunicipality">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-building\'></i></div>' +
        '                    <div class="col-10">Municipality</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +



        '            <li id="linkDistrictContainer">' +
        '              <a href="#" id="linkDistrict">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-map-marker-alt\'></i></div>' +
        '                    <div class="col-10">District</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkInvitationTypeContainer">' +
        '              <a href="#" id="linkInvitationType">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-envelope\'></i></div>' +
        '                    <div class="col-10">Invitation Type</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkReferenceTypeContainer">' +
        '              <a href="#" id="linkReferenceType">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-building\'></i></div>' +
        '                    <div class="col-10">ReferenceType</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkKlartextContainer">' +
        '              <a href="#" id="linkKlartext">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-file-alt\'></i></div>' +
        '                    <div class="col-10">Klartext</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkParishContainer">' +
        '              <a href="#" id="linkParish">' +
        '                <div class="row">' +
        '                    <div class="col-2"><i class=\'fas fa-church\'></i></div>' +
        '                    <div class="col-10">Parish</div>' +
        '                </div>' +
        '              </a>' +
        '            </li>' +

        '            <li id="linkUserContainer">' +
        '                <a href="#" id="linkUser">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-users-cog\'></i></div>' +
        '                        <div class="col-10">User</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        //Utility Sub Menu dropdown
        '            <li id="utilitySubMenuContainer">' +
        '                <a aria-expanded="false" class="dropdown-toggle" data-toggle="collapse" href="#utilitySubMenu">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-tools\'></i></div>' +
        '                        <div class="col-10">Utility</div>' +
        '                    </div>' +
        '                </a>' +
        '               <ul class="collapse list-unstyled" id="utilitySubMenu">' +
        '                   <li id="linkRoleContainer">' +
        '                       <a href="#" id="linkRole">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-user-lock\'></i></div>' +
        '                               <div class="col-10">Role</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkAuthorityContainer">' +
        '                       <a href="#" id="linkAuthority">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-lock\'></i></div>' +
        '                               <div class="col-10">Authority</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkAllowedRegistrationContainer">' +
        '                       <a href="#" id="linkAllowedRegistration">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-lock\'></i></div>' +
        '                               <div class="col-10">Allowed Registration</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkErrorMessageContainer">' +
        '                       <a href="#" id="linkErrorMessage">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-language\'></i></div>' +
        '                               <div class="col-10">Error Message</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +

        '                   <li id="linkLaboratoryContainer">' +
        '                       <a href="#" id="linkLaboratory">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-flask\'></i></div>' +
        '                               <div class="col-10">Laboratory</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +

        '                   <li id="linkCountyContainer">' +
        '                       <a href="#" id="linkCounty">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-city\'></i></div>' +
        '                               <div class="col-10">County</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +

        '                   <li id="linkLabContainer">' +
        '                       <a href="#" id="linkLab">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-flask\'></i></div>' +
        '                               <div class="col-10">Lab</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +

        '                   <li id="linkLogContainer">' +
        '                       <a href="#" id="linkLog">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-book\'></i></div>' +
        '                               <div class="col-10">Log</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '               </ul>' +
        '            </li>' +
        '        </ul>';
};
const role =JSON.parse(localStorage.getItem("role"));
console.log(role)

if (role.title === "User") {
    $("#linkSharedContainer").hide();
    $("#utilitySubMenuContainer").hide();
    $("#linkUserContainer").hide();
} else {
    $("#linkUserSharedContainer").hide();
}

export let addNavigationLink = function () {
    let linkHomeContainer = document.querySelector("#linkHomeContainer");
    let userContainer = document.querySelector("#linkUserContainer");
    let roleContainer = document.querySelector("#linkRoleContainer");
    let authorityContainer = document.querySelector("#linkAuthorityContainer");
    let allowedRegistrationContainer = document.querySelector("#linkAllowedRegistrationContainer");
    let errorMessageContainer = document.querySelector("#linkErrorMessageContainer");
    let linkPersonContainer = document.querySelector("#linkPersonContainer");
    let linkSampleContainer = document.querySelector("#linkSampleContainer");
    let linkHpvContainer = document.querySelector("#linkHpvContainer");
    let linkExtHpvContainer = document.querySelector("#linkExtHpvContainer");
    let linkCellContainer = document.querySelector("#linkCellContainer");
    let linkCell6923Container = document.querySelector("#linkCell6923Container");
    let linkMunicipalityContainer = document.querySelector("#linkMunicipalityContainer");
    let linkLogContainer = document.querySelector("#linkLogContainer");
    let linkLaboratoryContainer = document.querySelector("#linkLaboratoryContainer");
    let linkCountyContainer = document.querySelector("#linkCountyContainer");
    let linkLabContainer = document.querySelector("#linkLabContainer");
    let linkParishContainer = document.querySelector("#linkParishContainer");
    let linkDistrictContainer = document.querySelector("#linkDistrictContainer");
    let linkInvitationTypeContainer = document.querySelector("#linkInvitationTypeContainer");
    let linkReferenceTypeContainer = document.querySelector("#linkReferenceTypeContainer");
    let linkKlartextContainer = document.querySelector("#linkKlartextContainer");


    //Hide all the links
    linkHomeContainer.hidden = false;
    userContainer.hidden = true;
    roleContainer.hidden = false;
    authorityContainer.hidden = true;
    allowedRegistrationContainer.hidden = true;
    errorMessageContainer.hidden = true;
    linkPersonContainer.hidden = true;
    linkSampleContainer.hidden = true;
    linkHpvContainer.hidden = true;
    linkExtHpvContainer.hidden = true;
    linkCellContainer.hidden = true;
    linkMunicipalityContainer.hidden= true;
    linkReferenceTypeContainer.hidden= true;
    linkLogContainer.hidden = true;
    linkLaboratoryContainer.hidden = true;
    linkCountyContainer.hidden = true;
    linkLabContainer.hidden = true;
    linkParishContainer.hidden = true;
    linkDistrictContainer.hiddn = true;
    linkInvitationTypeContainer.hiddn = true;
    linkKlartextContainer.hiddn = true;
    linkCell6923Container.hidden = true;



    let authorities = JSON.parse(localStorage.getItem("authorities"));
    if (authorities) {
        for (let item of authorities) {
           if (item.title === "USER") {
                userContainer.querySelector("#linkUser").href = EndPoints.BASE_URL + "/user.html";
                userContainer.hidden = false;
            } else if (item.title === "ROLE") {
                roleContainer.querySelector("#linkRole").href = EndPoints.BASE_URL + "/role.html";
                roleContainer.hidden = false;
            } else if (item.title === "AUTHORITY") {
                authorityContainer.querySelector("#linkAuthority").href = EndPoints.BASE_URL + "/authority.html";
                authorityContainer.hidden = false;
            } else if (item.title === "ALLOWED_REGISTRATION") {
                allowedRegistrationContainer.querySelector("#linkAllowedRegistration").href = EndPoints.BASE_URL + "/allowed_registration.html";
                allowedRegistrationContainer.hidden = false;
            } else if (item.title === "ERROR_MESSAGE") {
                errorMessageContainer.querySelector("#linkErrorMessage").href = EndPoints.BASE_URL + "/error_message.html";
                errorMessageContainer.hidden = false;
            } else if (item.title === "HOME") {
               linkHomeContainer.querySelector("#linkHome").href = EndPoints.BASE_URL + "/home.html";
               linkHomeContainer.hidden = false;
            } else if (item.title === "PERSON") {
                linkPersonContainer.querySelector("#linkPerson").href = EndPoints.BASE_URL + "/person.html";
                linkPersonContainer.hidden = false;
            } else if (item.title === "SAMPLE") {
               linkSampleContainer.querySelector("#linkSample").href = EndPoints.BASE_URL + "/sample.html";
               linkSampleContainer.hidden = false;
           } else if (item.title === "HPV") {
               linkHpvContainer.querySelector("#linkHpv").href = EndPoints.BASE_URL + "/hpv.html";
               linkHpvContainer.hidden = false;
           } else if (item.title === "EXTHPV") {
               linkExtHpvContainer.querySelector("#linkExtHpv").href = EndPoints.BASE_URL + "/extHpv.html";
               linkExtHpvContainer.hidden = false;
           } else if (item.title === "CELL") {
               linkCellContainer.querySelector("#linkCell").href = EndPoints.BASE_URL + "/cell.html";
               linkCellContainer.hidden = false;
           } else if (item.title === "MUNICIPALITY") {
               linkMunicipalityContainer.querySelector("#linkMunicipality").href = EndPoints.BASE_URL + "/municipality.html";
               linkMunicipalityContainer.hidden = false;
           }
           else if (item.title === "REFERENCETYPE") {
               linkReferenceTypeContainer.querySelector("#linkReferenceType").href = EndPoints.BASE_URL + "/referenceType.html";
               linkReferenceTypeContainer.hidden = false;
           }
           else if (item.title === "LOG") {
               linkLogContainer.querySelector("#linkLog").href = EndPoints.BASE_URL + "/log.html";
               linkLogContainer.hidden = false;
           } else if (item.title === "LABORATORY") {
               linkLaboratoryContainer.querySelector("#linkLaboratory").href = EndPoints.BASE_URL + "/laboratory.html";
               linkLaboratoryContainer.hidden = false;
           } else if (item.title === "COUNTY") {
               linkCountyContainer.querySelector("#linkCounty").href = EndPoints.BASE_URL + "/county.html";
               linkCountyContainer.hidden = false;
           }else if (item.title === "LAB") {
               linkLabContainer.querySelector("#linkLab").href = EndPoints.BASE_URL + "/lab.html";
               linkLabContainer.hidden = false;
           }else if (item.title === "PARISH") {
               linkParishContainer.querySelector("#linkParish").href = EndPoints.BASE_URL + "/parish.html";
               linkParishContainer.hidden = false;
           } else if (item.title === "DISTRICT") {
               linkDistrictContainer.querySelector("#linkDistrict").href = EndPoints.BASE_URL + "/district.html";
               linkDistrictContainer.hidden = false;
           } else if (item.title === "INVITATIONTYPE") {
               linkInvitationTypeContainer.querySelector("#linkInvitationType").href = EndPoints.BASE_URL + "/invitationType.html";
               linkInvitationTypeContainer.hidden = false;
           } else if (item.title === "KLARTEXT") {
               linkKlartextContainer.querySelector("#linkKlartext").href = EndPoints.BASE_URL + "/klartext.html";
               linkKlartextContainer.hidden = false;
           } else if (item.title === "CELL6923") {
               linkCell6923Container.querySelector("#linkCell6923").href = EndPoints.BASE_URL + "/cell6923.html";
               linkCell6923Container.hidden = false;
           }
        }
    }


    document.querySelector("#linkProfile").href = EndPoints.BASE_URL + "/user_profile.html";
    let url = window.location;
    $('ul li a[href="' + url + '"]').parent().addClass('active');
    $('ul li a').filter(function () {
        return this.href === url.toString();
    }).parent().addClass('active');
};
export let addToggleFunctionality = function () {
    $("#sidebar").mCustomScrollbar({
        theme: "minimal"
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar, #content').toggleClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });
};
document.querySelector("#idButtonLogout").addEventListener("click", function (event) {
    localStorage.clear();
    window.location = EndPoints.BASE_URL;
});