package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public Subscriber create(Subscriber subscriber) {
        List<Long> ids = subscriber.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
        List<Skill> skills = this.skillRepository.findAllById(ids);
        subscriber.setSkills(skills);
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber update(Subscriber reqSubscriber) {
        Optional<Subscriber> subscriberOptional = this.subscriberRepository.findById(reqSubscriber.getId());
        if (subscriberOptional.isPresent()) {
            Subscriber currentSubscriber = subscriberOptional.get();
            if (reqSubscriber.getSkills() != null) {
                List<Long> ids = reqSubscriber.getSkills().stream().map(item -> item.getId())
                        .collect(Collectors.toList());
                List<Skill> skills = this.skillRepository.findAllById(ids);
                currentSubscriber.setSkills(skills);
            }
            currentSubscriber = this.subscriberRepository.save(currentSubscriber);
            return currentSubscriber;
        } else {
            return null;
        }
    }

    public boolean isEmailExist(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }
}
