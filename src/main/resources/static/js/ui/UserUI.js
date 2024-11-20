export let idModalAlert = document.querySelector("#idModalAlert");
export let modalAlertBody = idModalAlert.querySelector(".modal-body p");

export let idModalUser = document.querySelector("#idModalUser");
export let modalUserTitle = idModalUser.querySelector(".modal-title");

export let idBtnPopAddUser = document.querySelector("#idBtnPopAddUser");

export let idTableUser = document.querySelector("#idTableUser");

export let idSelectUserRole = document.querySelector("#idSelectUserRole");

//Add
export let idFormUserAdd = document.querySelector("#idFormUserAdd");
export let userRoleAdd = idFormUserAdd.querySelector("select[name='userRole']");

//Update
export let idFormUserUpdate = document.querySelector("#idFormUserUpdate");
export let id = idFormUserUpdate.querySelector("input[name='id']");
export let userRoleUpdate = idFormUserUpdate.querySelector("select[name='userRole']");
export let firstNameUpdate = idFormUserUpdate.querySelector("input[name='firstName']");
export let lastNameUpdate = idFormUserUpdate.querySelector("input[name='lastName']");
export let genderUpdate = idFormUserUpdate.querySelectorAll("input[name='gender']");
export let dateOfBirthUpdate = idFormUserUpdate.querySelector("input[name='dateOfBirth']");
export let mobileNumberUpdate = idFormUserUpdate.querySelector("input[name='mobileNumber']");
export let emailAddressUpdate = idFormUserUpdate.querySelector("input[name='emailAddress']");

//Show
export let idUserShow = document.querySelector("#idUserShow");
export let idRoleShow = document.querySelector("#idRoleShow");
export let idNameShow = document.querySelector("#idNameShow");
export let idEmailAddressShow = document.querySelector("#idEmailAddressShow");
export let idGenderShow = document.querySelector("#idGenderShow");
export let idDateOfBirthShow = document.querySelector("#idDateOfBirthShow");
export let idMobileNumberShow = document.querySelector("#idMobileNumberShow");

//Delete
export let idBtnUserDelete = document.querySelector("#idBtnUserDelete");
export let idModalUserDelete = document.querySelector("#idModalUserDelete");

//Delete Answers
export let idModalAnswerDelete = document.querySelector("#idModalAnswerDelete");
export let idBtnAnswerDelete = idModalAnswerDelete.querySelector("#idBtnAnswerDelete");