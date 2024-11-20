package se.ki.education.nkcx.util;

/* Created by: Binay Singh */

public class EmailMessage {

    private static String getHeader() {
        return "<tr>" +
                "<td></td>" +
                "<td><img height='100px' src='' alt='RISCC Logo'></td>" +
                "</tr>";
    }

    private static String getFooter() {
        return "<tr>" +
                "<td align='right'><img height='80px' src='' alt='NVS Icon'></td>" +
                "<td>" +
                "<p><span style='font-size: 1.3rem;'>Research by NVS</span><br>" +
                "<a href='#'>www.nvs-research.com</a><br>" +
                "<a href='#'>My Account</a> | <a href='#'>Notifications</a> | <a href='#'>Unsubscribe</a><br></p>" +
                "<p>&copy; 2019 STAV, Inc. All rights reserved.</p>" +
                "</td>" +
                "</tr>";
    }

    public static String signupVerificationCode(String verificationCode) {
        return "<table cellspacing='50px'>" +
                //getHeader() +

                //Body
                "<tr>" +
                //"<td>" +
                //"<img height='100px' src='' alt='Verify logo'>" +
                //"</td>" +
                "<td>" +
                "<p>Hej!<br><br>" +
                "Du får det här e-postmeddelandet eftersom vi har fått anmälningsförfrågan med din e-postadress.<br><br>" +
                "Registrera dig med denna verifieringskod: <b>" + verificationCode + "</b><br>" +
                "Om du inte gjorde en begäran om anmälan krävs ingen ytterligare åtgärd.<br><br>" +
                "Hälsningar,<br>" +
                "Support Desk<br>" +
                "RISK-BASED SCREENING FOR CERVICAL CANCER" +
                "</p>" +
                "</td>" +
                "</tr>" +

                //getFooter() +
                "</table>";
    }

    public static String passwordResetVerificationCode(String verificationCode) {
        return "<table cellspacing='50px'>" +
                //getHeader() +

                //Body
                "<tr>" +
                //"<td>" +
                //"<img height='100px' src='https://s3-us-west-1.amazonaws.com/community-common/verify_icon.png' alt='Verify logo'>" +
                //"</td>" +
                "<td>" +
                "<p>Hej!<br><br>" +
                "Du får detta e-postmeddelande eftersom vi har fått en begäran om återställning av lösenord för ditt konto.<br><br>" +
                "Återställ ditt lösenord med denna verifieringskod: <b>" + verificationCode + "</b><br>" +
                "Om du inte begärde återställning av lösenord krävs ingen ytterligare åtgärd.<br><br>" +
                "Hälsningar,<br>" +
                "Support Desk<br>" +
                "RISK-BASED SCREENING FOR CERVICAL CANCER" +
                "</p>" +
                "</td>" +
                "</tr>" +

                //getFooter() +
                "</table>";
    }

    public static String passwordReset() {
        return "<table cellspacing='50px'>" +
                //getHeader() +

                //Body
                "<tr>" +
                //"<td>" +
                //"<img height='100px' src='https://s3-us-west-1.amazonaws.com/community-common/password_icon.png' alt='Password logo'>" +
                //"</td>" +
                "<td>" +
                "<p>Hello!<br><br>" +
                "You are receiving this e-mail because you have successfully reset your password.<br><br>" +
                "Regards,<br>" +
                "Support Desk<br>" +
                "KI-COVID19" +
                "</p>" +
                "</td>" +
                "</tr>" +

                //getFooter() +
                "</table>";
    }

    public static String answerOptionMessage(String message, String fullName, String emailAddress, String mobileNumber) {
        return "<table cellspacing='50px'>" +
                //getHeader() +

                //Body
                "<tr>" +
                //"<td>" +
                //"<img height='100px' src='https://s3-us-west-1.amazonaws.com/community-common/verify_icon.png' alt='Verify logo'>" +
                //"</td>" +
                "<td>" +
                "<p>Hej!<br><br>" +
                message + "<br><br>" +
                (fullName != null ? "Answered by<br>Name: " + fullName + "<br>" : "") +
                (emailAddress != null ? "Email: " + emailAddress + "<br>" : "") +
                (mobileNumber != null ? "Phone: " + mobileNumber + "<br><br>" : "") +
                "Hälsningar,<br>" +
                "Support Desk<br>" +
                "RISK-BASED SCREENING FOR CERVICAL CANCER" +
                "</p>" +
                "</td>" +
                "</tr>" +

                //getFooter() +
                "</table>";
    }
}
