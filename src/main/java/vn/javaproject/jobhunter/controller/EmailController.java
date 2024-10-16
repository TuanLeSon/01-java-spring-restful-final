package vn.javaproject.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.javaproject.jobhunter.service.EmailService;
import vn.javaproject.jobhunter.service.SubscriberService;
import vn.javaproject.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")

public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    // @Scheduled(cron = "*/20 * * * * *")
    // @Transactional
    public String sendSimpleEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
}
