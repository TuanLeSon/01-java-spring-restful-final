package vn.javaproject.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.javaproject.jobhunter.domain.Subscriber;
import vn.javaproject.jobhunter.service.SubscriberService;
import vn.javaproject.jobhunter.util.SecurityUtil;
import vn.javaproject.jobhunter.util.annotation.ApiMessage;
import vn.javaproject.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a new subscriber")
    public ResponseEntity<Subscriber> createNewSubscriber(@Valid @RequestBody Subscriber reqSubscriber)
            throws IdInvalidException {
        if (subscriberService.isEmailExist(reqSubscriber.getEmail())) {
            throw new IdInvalidException(
                    "Email " + reqSubscriber.getName() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        Subscriber subscriber = this.subscriberService.create(reqSubscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriber);
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateJob(@RequestBody Subscriber reqSubscriber) throws IdInvalidException {
        Subscriber subscriber = this.subscriberService.update(reqSubscriber);
        if (subscriber == null) {
            throw new IdInvalidException("Subcriber với id = " + reqSubscriber.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(subscriber);
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }

}
