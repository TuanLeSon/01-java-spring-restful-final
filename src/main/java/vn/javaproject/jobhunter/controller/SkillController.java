package vn.javaproject.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.javaproject.jobhunter.domain.Skill;
import vn.javaproject.jobhunter.domain.response.ResultPaginationDTO;
import vn.javaproject.jobhunter.service.SkillService;
import vn.javaproject.jobhunter.util.annotation.ApiMessage;
import vn.javaproject.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    public final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill reqSkill) throws IdInvalidException {
        if (skillService.isNameExist(reqSkill.getName())) {
            throw new IdInvalidException("Skill " + reqSkill.getName() + " đã tồn tại, vui lòng sử dụng skill khác");
        }
        Skill skill = this.skillService.handleCreateSkill(reqSkill);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        if (skillService.fetchSkillById(id) == null) {
            throw new IdInvalidException("Skill với id = " + id + " không tồn tại");
        }
        this.skillService.handleDeleteSkill(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("fetch skill by id")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id) throws IdInvalidException {
        if (skillService.fetchSkillById(id) == null) {
            throw new IdInvalidException("Skill với id = " + id + " không tồn tại");
        }
        Skill skill = this.skillService.fetchSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable

    ) {

        ResultPaginationDTO skills = this.skillService.fetchAllSkill(spec, pageable);
        return ResponseEntity.ok(skills);
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill postManSkill) throws IdInvalidException {

        Skill currentSkill = this.skillService.fetchSkillById(postManSkill.getId());
        // check id
        if (currentSkill == null) {
            throw new IdInvalidException("Skill với id = " + postManSkill.getId() + " không tồn tại");
        }
        // check name
        if (currentSkill != null && this.skillService.isNameExist(currentSkill.getName())) {
            throw new IdInvalidException("Skill với name = " + postManSkill.getName() + " đã tồn tại");
        }

        Skill skill = this.skillService.handleUpdateSkill(postManSkill);
        return ResponseEntity.ok(skill);
    }

}
