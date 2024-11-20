package se.ki.education.nkcx.enums;

public enum ErrorMessage {

    //Authority
    AUT001("Authority already exists."),
    AUT002("Authority does not exist."),

    //About Us
    ABT001("About Use does not exist."),

    //Cell
    CELL001("Cell does not exist."),
    CELL002("Cell already exists."),

    //City
    CIT001("City already exists."),
    CIT002("City does not exist."),
    CIT003("There are addresses associated with this city."),

    //Country
    COU001("Country does not exist."),
    COU002("Country already exists."),

    //County
    COUNTY001("County does not exist."),
    COUNTY002("County already exists."),

    //District
    DISTRICT001("District does not exist."),
    DISTRICT002("District already exists."),

    //General Error
    ERR001("Something went wrong"),
    ERR002("Username and/or password is wrong."),
    ERR003("You are not Authorized."),
    ERR004("Invalid token."),
    ERR005("Super Admin cannot be deleted."),

    //ExtHpv
    EXTHPV001("ExtHpv does not exist."),
    EXTHPV002("ExtHpv already exists."),

    //Group
    GRP001("Group already exists."),
    GRP002("Group does not exist."),

    //Hpv
    HPV001("Hpv does not exist."),
    HPV002("Hpv already exists."),

    INS002("Sample does not exist."),

    LAO001("Lab Analysis Output does not exist."),
    LAO002("Lab Analysis Format does not exist."),
    LAO003("Lab Analysis Technical platform does not exist."),

    //InvitationType
    INVITATIONTYPE001("InvitationType does not exist."),
    INVITATIONTYPE002("InvitationType already exists."),

    //Klartext
    KLARTEXT001("Klartext does not exist."),
    KLARTEXT002("Klartext already exists."),

    //Laboratory
    LABORATORY001("Laboratory does not exist."),
    LABORATORY002("Laboratory already exists."),

    //Lab
    LAB001("Lab does not exist."),
    LAB002("Lab already exists."),

    //Municipality
    MUNICIPALITY001("Municipality does not exist."),
    MUNICIPALITY002("Municipality already exists."),

    //Parish
    PARISH001("Parish already exists."),
    PARISH002("Parish does not exist."),

    //Person
    PERSON001("Person already exists."),
    PERSON002("Person does not exist."),

    //Questionnaire
    QUE001("Questionnaire does not exist."),

    //Role
    ROL001("Role does not exist."),
    ROL002("Role already exists."),
    ROL003("Restricted Role."),
    ROL004("Role Id not provided."),

    //Sample
    SAMPLE001("Sample already exists."),
    SAMPLE002("Sample does not exist."),

    //State
    STA001("State already exists."),
    STA002("State does not exist."),

    //User
    USR001("User does not exist."),
    USR002("User already exists."),
    USR003("You don't have permission to change the password."),
    USR004("Email Address is missing."),
    USR005("Email Address already exists."),
    USR006("Mobile Number already exists."),
    USR007("User with this email address already exists."),
    USR008("User with this mobile number already exists."),

    //Verification
    VER001("Verification details are missing."),
    VER002("Verification code is wrong or expired."),
    VER003("Email address did not match."),
    VER004("Mobile number did not match."),
    VER005("Email Address or Mobile Number is missing."),
    VER006("Your Email Address has not been added to the allowed registration list. Please contact the support team.");



    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
