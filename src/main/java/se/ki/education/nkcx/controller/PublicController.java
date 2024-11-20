package se.ki.education.nkcx.controller;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.PasswordReq;
import se.ki.education.nkcx.dto.response.VerificationRes;
import se.ki.education.nkcx.dto.request.UserReq;
import se.ki.education.nkcx.service.AnonService;
import se.ki.education.nkcx.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/public")
@Api(tags = "Public", description = "APIs that can be accessed without authentication.")
public class PublicController {

    private final AnonService anonService;
    private final UserService userService;

    @Autowired
    public PublicController(AnonService anonService, UserService userService) {
        this.anonService = anonService;
        this.userService = userService;
    }


    @PostMapping(value = "/sign_up/verification_code")
    @ApiOperation(value = "Get verification code for sign up.")
    public ResponseEntity<ServiceRes> getSignUpVerificationCode(@RequestBody VerificationRes verification) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Verification code sent successfully.")
                        .addData("verification", anonService.sendVerificationCode(verification))
        );
    }

    @PostMapping(value = "/sign_up")
    @ApiOperation(value = "Sign Up a new User.")
    public ResponseEntity<ServiceRes> signUp(@RequestBody UserReq user) throws IOException {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Signed up successfully.")
                        .addData("user", userService.save(user))
        );
    }

    @GetMapping(value = "/password_reset/verification_code")
    @ApiOperation(value = "Get verification code in email or contact number to reset password.")
    public ResponseEntity<ServiceRes> getVerificationCode(
            @ApiParam(name = "identity", value = "Either Email address or contact number with country code prefixed with +")
            @RequestParam("identity") String identity) {
        String verificationCode = anonService.sendVerificationCode(identity);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage(identity.contains("@") ? "Verification code sent successfully to your email address." : "Verification code sent successfully to your mobile number.")
                        .addData("verificationCode", verificationCode)
        );
    }

    @PutMapping(value = "/reset_password")
    @ApiOperation(value = "Reset password.")
    public ResponseEntity<ServiceRes> resetPassword(@RequestBody PasswordReq resetPassword) {
        anonService.resetPassword(resetPassword.getVerificationCode(), resetPassword.getNewPassword());
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Password reset successful.")
        );
    }
//    @GetMapping(value = "/bio_bank_id")
//    @ApiOperation(value = "Get list of bio bank id.")
//    public ResponseEntity<ServiceRes> getBioBankId(){
//        return ResponseEntity.ok(new ServiceRes()
//        .setStatus(true)
//        .addData("list", anonService.getBioBankId())
//        );
//    }
}
