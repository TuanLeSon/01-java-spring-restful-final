package vn.javaproject.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.javaproject.jobhunter.domain.Company;
import vn.javaproject.jobhunter.domain.Job;
import vn.javaproject.jobhunter.domain.Skill;
import vn.javaproject.jobhunter.domain.response.ResCreateJobDTO;
import vn.javaproject.jobhunter.domain.response.ResUpdateJobDTO;
import vn.javaproject.jobhunter.domain.response.ResultPaginationDTO;
import vn.javaproject.jobhunter.repository.JobRepository;
import vn.javaproject.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyService companyService;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyService = companyService;
    }

    public Job handleCreateJob(Job job) {
        List<Long> ids = job.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
        List<Skill> skills = this.skillRepository.findAllById(ids);
        job.setSkills(skills);
        if (job.getCompany() != null) {
            Company company = this.companyService.fetchCompanyById(job.getCompany().getId());
            if (company != null) {
                job.setCompany(company);
            }
        }
        return this.jobRepository.save(job);
    }

    public void handleDeleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public Job fetchJobById(long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllJob(
            Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());
        return rs;
    }

    public Job handleUpdateJob(Job reqJob) {
        Optional<Job> jobOptional = this.jobRepository.findById(reqJob.getId());
        if (jobOptional.isPresent()) {
            Job currentJob = jobOptional.get();
            currentJob.setName(reqJob.getName());
            currentJob.setLocation(reqJob.getLocation());
            currentJob.setSalary(reqJob.getSalary());
            currentJob.setQuantity(reqJob.getQuantity());
            currentJob.setLevel(reqJob.getLevel());
            currentJob.setStartDate(reqJob.getStartDate());
            currentJob.setEndDate(reqJob.getEndDate());
            currentJob.setActive(reqJob.getActive());
            if (reqJob.getSkills() != null) {
                List<Long> ids = reqJob.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
                List<Skill> skills = this.skillRepository.findAllById(ids);
                currentJob.setSkills(skills);
            }
            if (reqJob.getCompany() != null) {
                Company company = this.companyService.fetchCompanyById(reqJob.getCompany().getId());
                if (company != null) {
                    currentJob.setCompany(company);
                }
            }
            // update
            currentJob = this.jobRepository.save(currentJob);
            return currentJob;
        } else {
            return null;
        }
    }

    public boolean isNameExist(String name) {
        return this.jobRepository.existsByName(name);
    }

    public Job handleGetJobById(long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        }
        return null;
    }

    public ResCreateJobDTO convertToResCreateJobDTO(Job job) {
        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.getActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setCreatedBy(job.getCreatedBy());
        if (job.getSkills().size() != 0) {
            List<String> skills = job.getSkills().stream().map(item -> item.getName()).collect(Collectors.toList());
            res.setSkills(skills);
        }
        return res;
    }

    public ResUpdateJobDTO convertToResUpdateJobDTO(Job job) {
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.getActive());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setUpdatedBy(job.getUpdatedBy());
        if (job.getSkills().size() != 0) {
            List<String> skills = job.getSkills().stream().map(item -> item.getName()).collect(Collectors.toList());
            res.setSkills(skills);
        }
        return res;
    }

    // public ResJobDTO convertToResJobDTO(Job job) {
    // ResJobDTO res = new ResJobDTO();
    // res.setId(job.getId());
    // res.setName(job.getName());
    // res.setLocation(job.getLocation());
    // res.setSalary(job.getSalary());
    // res.setQuantity(job.getQuantity());
    // res.setLevel(job.getLevel());
    // res.setStartDate(job.getStartDate());
    // res.setEndDate(job.getEndDate());
    // res.setActive(job.getActive());
    // res.setCreatedAt(job.getCreatedAt());
    // res.setCreatedBy(job.getCreatedBy());
    // res.setUpdatedAt(job.getUpdatedAt());
    // res.setUpdatedBy(job.getUpdatedBy());
    // if (job.getSkills().size() != 0) {
    // List<String> skills = job.getSkills().stream().map(item ->
    // item.getName()).collect(Collectors.toList());
    // res.setSkills(skills);
    // }
    // return res;
    // }
}
