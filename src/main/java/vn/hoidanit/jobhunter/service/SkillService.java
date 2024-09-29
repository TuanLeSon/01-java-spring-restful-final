package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResSkillDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) {

        return this.skillRepository.save(skill);
    }

    public void handleDeleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        // delete skill
        this.skillRepository.deleteById(id);
    }

    public Skill fetchSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllSkill(
            Specification<Skill> spec, Pageable pageable
    // Pageable pageable
    ) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);
        List<ResSkillDTO> listSkill = pageSkill.getContent()
                .stream().map(item -> new ResSkillDTO(
                        item.getId(),
                        item.getName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy()))
                .collect(Collectors.toList());
        rs.setResult(listSkill);
        return rs;
    }

    public Skill handleUpdateSkill(Skill reqSkill) {
        Optional<Skill> skillOptional = this.skillRepository.findById(reqSkill.getId());
        if (skillOptional.isPresent()) {
            Skill currentSkill = skillOptional.get();
            currentSkill.setName(reqSkill.getName());
            // update
            currentSkill = this.skillRepository.save(currentSkill);
            return currentSkill;
        } else {
            return null;
        }
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill handleGetSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

}
