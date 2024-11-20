package se.ki.education.nkcx.service;

import se.ki.education.nkcx.dto.response.VerificationRes;
import se.ki.education.nkcx.entity.VerificationEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.AllowedRegistrationRepo;
import se.ki.education.nkcx.repo.UserRepo;
import se.ki.education.nkcx.repo.VerificationRepo;
import se.ki.education.nkcx.util.EmailMessage;
import se.ki.education.nkcx.util.GeneralUtil;
import se.ki.education.nkcx.util.SMSUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnonService {

    private static final Logger LOG = LogManager.getLogger();

    private final EmailService emailService;
    private final UserRepo userRepo;
    private final VerificationRepo verificationRepo;
    private final SMSUtil smsUtil;
    private final AllowedRegistrationRepo allowedRegistrationRepo;

    @Autowired
    public AnonService(EmailService emailService, UserRepo userRepo, VerificationRepo verificationRepo, SMSUtil smsUtil, AllowedRegistrationRepo allowedRegistrationRepo) {
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.verificationRepo = verificationRepo;
        this.smsUtil = smsUtil;
        this.allowedRegistrationRepo = allowedRegistrationRepo;
    }


    public VerificationRes sendVerificationCode(VerificationRes verificationRes) {
        LOG.info("----- Saving verification. -----");
        if (verificationRes.getEmailAddress() == null && verificationRes.getMobileNumber() == null) {
            throw new IllegalArgumentException("Please provide either email address or mobile number.");
        }
        VerificationEntity verification;
        String verificationCode = GeneralUtil.generateRandomDigit();
        String encodedVerificationCode = GeneralUtil.sha256(verificationCode);
        verificationRes.setVerificationCode(verificationCode);

        if (verificationRes.getEmailAddress() != null && verificationRes.getMobileNumber() != null) {

            if (!allowedRegistrationRepo.findByEmail(verificationRes.getEmailAddress()).isPresent()) {
                throw new CustomException("VER006");
            }

            if (userRepo.findByEmailAddressAndMobileNumber(verificationRes.getEmailAddress(), verificationRes.getMobileNumber()) != null) {
                throw new CustomException("USR002");
            }
            if (userRepo.findByEmailAddress(verificationRes.getEmailAddress()) != null) {
                throw new CustomException("USR007");
            }
            if (userRepo.findByMobileNumber(verificationRes.getMobileNumber()) != null) {
                throw new CustomException("USR008");
            }

            verification = verificationRepo.findByEmailAddressAndMobileNumber(verificationRes.getEmailAddress(), verificationRes.getMobileNumber());
            if (verification == null) {
                verification = new VerificationEntity()
                        .setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setEmailAddress(verificationRes.getEmailAddress())
                        .setMobileNumber(verificationRes.getMobileNumber())
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(1);
                verificationRepo.save(verification);
            } else {
                verification.setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(verification.getVerificationCodeSentCount() + 1);
            }
            String body = EmailMessage.signupVerificationCode(verificationCode);
            //emailUtil.sendFormattedEmail(verificationRes.getEmailAddress(), "Signup Verification Code", body);
            //emailUtil.sendGmail(verificationRes.getEmailAddress(), "Signup Verification Code", body);
            emailService.sendFormattedEmail(verificationRes.getEmailAddress(), "Signup Verification Code", body);
            verificationRes.setEmailSent(true);

            try {
                if (smsUtil.sendSMS(verificationRes.getMobileNumber(), "Verification Code: " + verificationCode)) {
                    verificationRes.setSmsSent(true);
                } else {
                    throw new CustomException("SMS001");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (verificationRes.getEmailAddress() != null) {

            if (!allowedRegistrationRepo.findByEmail(verificationRes.getEmailAddress()).isPresent()) {
                throw new CustomException("VER006");
            }

            if (userRepo.findByEmailAddress(verificationRes.getEmailAddress()) != null) {
                throw new CustomException("USR002");
            }
            verification = verificationRepo.findByEmailAddress(verificationRes.getEmailAddress());
            if (verification == null) {
                verification = new VerificationEntity()
                        .setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setEmailAddress(verificationRes.getEmailAddress())
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(1);
                verificationRepo.save(verification);
            } else {
                verification.setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(verification.getVerificationCodeSentCount() + 1);
            }
            String body = EmailMessage.signupVerificationCode(verificationCode);
            //emailUtil.sendFormattedEmail(verificationRes.getEmailAddress(), "Signup Verification Code", body);
            //emailUtil.sendGmail(verificationRes.getEmailAddress(), "Signup Verification Code", body);
            emailService.sendFormattedEmail(verificationRes.getEmailAddress(), "Signup Verification Code", body);
            verificationRes.setEmailSent(true);
        } else {
            if (userRepo.findByMobileNumber(verificationRes.getMobileNumber()) != null) {
                throw new CustomException("USR002");
            }
            verification = verificationRepo.findByMobileNumber(verificationRes.getMobileNumber());
            if (verification == null) {
                verification = new VerificationEntity()
                        .setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setMobileNumber(verificationRes.getMobileNumber())
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(1);
                verificationRepo.save(verification);
            } else {
                verification.setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(verification.getVerificationCodeSentCount() + 1);
            }
            if (smsUtil.sendSMS(verificationRes.getMobileNumber(), "Verification Code: " + verificationCode)) {
                verificationRes.setSmsSent(true);
            } else {
                throw new CustomException("SMS001");
            }
        }

        return verificationRes.setId(verification.getId());
    }

    public String sendVerificationCode(String identity) {
        LOG.info("----- Sending verification code to the user. -----");
        if (identity == null || identity.isEmpty()) {
            throw new CustomException("VER007");
        }
        if (identity.contains("@")) {
            UserEntity userEntity = userRepo.findByEmailAddress(identity);
            if (userEntity == null) {
                throw new CustomException("USR001");
            }
            String verificationCode = GeneralUtil.generateRandomDigit();
            String encodedVerificationCode = GeneralUtil.sha256(verificationCode);

            userEntity.setVerificationCode(encodedVerificationCode);
            userEntity.setVerificationCodeDeadline(LocalDateTime.now().plusMinutes(30));

            String body = EmailMessage.passwordResetVerificationCode(verificationCode);
            //emailUtil.sendFormattedEmail(identity, "Password Reset Verification Code", body);
            //emailUtil.sendGmail(identity, "Password Reset Verification Code", body);
            emailService.sendFormattedEmail(identity, "Password Reset Verification Code", body);
            return verificationCode;

        } else if (identity.length() > 10) {
            UserEntity userEntity = userRepo.findByMobileNumber(identity);
            if (userEntity == null) {
                throw new CustomException("USR001");
            }
            String verificationCode = GeneralUtil.generateRandomDigit();
            String encodedVerificationCode = GeneralUtil.sha256(verificationCode);
            userEntity.setVerificationCode(encodedVerificationCode);
            userEntity.setVerificationCodeDeadline(LocalDateTime.now().plusMinutes(30));
            if (smsUtil.sendSMS(identity, "Verification Code: " + verificationCode)) {
                return verificationCode;
            } else {
                throw new CustomException("SMS001");
            }
        } else {
            throw new IllegalArgumentException("Please provide either email address or mobile number.");
        }
    }

    public void resetPassword(String verificationCode, String newPassword) {
        LOG.info("----- Resetting password. -----");

        UserEntity userEntity = userRepo.findByVerificationCodeAndVerificationCodeDeadlineAfter(GeneralUtil.sha256(verificationCode), LocalDateTime.now());
        if (userEntity == null) {
            throw new CustomException("VER002");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new CustomException("VER008");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
        userEntity.setPassword(encodedPassword);
        userEntity.setLastPasswordResetDate(LocalDateTime.now());
        userEntity.setVerificationCode("");
        if (userEntity.getEmailAddress() != null) {
            //emailUtil.sendEmail(userEntity.getEmailAddress(), "Password Reset.", "You have successfully reset your password.");
            //emailUtil.sendGmail(userEntity.getEmailAddress(), "Password Reset.", "You have successfully reset your password.");
            emailService.sendFormattedEmail(userEntity.getEmailAddress(), "Password Reset.", "You have successfully reset your password.");
        }
    }

}
