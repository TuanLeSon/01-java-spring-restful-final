package vn.javaproject.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.javaproject.jobhunter.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
    Skill findByName(String name);

    boolean existsByName(String name);

    List<Skill> findByIdIn(List<Long> ids);
}
