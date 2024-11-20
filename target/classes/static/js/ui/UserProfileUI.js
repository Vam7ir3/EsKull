export let idModalAlert = document.querySelector("#idModalAlert");
export let modalAlertBody = idModalAlert.querySelector(".modal-body p");

export let idModalUser = document.querySelector("#idModalUser");
export let modalTitle = idModalUser.querySelector(".modal-title");

//Show
export let idUserShow = document.querySelector("#idUserShow");
export let idRoleShow = document.querySelector("#idRoleShow");
export let idNameShow = document.querySelector("#idNameShow");
export let idGenderShow = document.querySelector("#idGenderShow");
export let idDateOfBirthShow = document.querySelector("#idDateOfBirthShow");
export let idMobileNumberShow = document.querySelector("#idMobileNumberShow");
export let idEmailAddressShow = document.querySelector("#idEmailAddressShow");

//Update
export let idBtnPopEditUser = document.querySelector("#idBtnPopEditUser");
export let idFormUserUpdate = document.querySelector("#idFormUserUpdate");
export let id = idFormUserUpdate.querySelector("input[name='id']");
export let firstNameUpdate = idFormUserUpdate.querySelector("input[name='firstName']");
export let lastNameUpdate = idFormUserUpdate.querySelector("input[name='lastName']");
export let genderUpdate = idFormUserUpdate.querySelectorAll("input[name='gender']");
export let dateOfBirthUpdate = idFormUserUpdate.querySelector("input[name='dateOfBirth']");
export let mobileNumberUpdate = idFormUserUpdate.querySelector("input[name='mobileNumber']");
export let emailAddressUpdate = idFormUserUpdate.querySelector("input[name='emailAddress']");

//Change Password
export let idFormUserPasswordUpdate = document.querySelector("#idFormUserPasswordUpdate");
export let hiddenUserId = idFormUserPasswordUpdate.querySelector("input[name='id']");
export let oldPassword = idFormUserPasswordUpdate.querySelector("input[name='oldPassword']");
export let newPassword = idFormUserPasswordUpdate.querySelector("input[name='newPassword']");