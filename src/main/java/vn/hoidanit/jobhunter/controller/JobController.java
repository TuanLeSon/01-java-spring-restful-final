package vn.hoidanit.jobhunter.controller;

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
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    public final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a new job")
    public ResponseEntity<ResCreateJobDTO> createNewJob(@Valid @RequestBody Job reqJob) throws IdInvalidException {
        // if (jobService.isNameExist(reqJob.getName())) {
        // throw new IdInvalidException("Job " + reqJob.getName() + " đã tồn tại, vui
        // lòng sử dụng job khác");
        // }
        Job job = this.jobService.handleCreateJob(reqJob);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.convertToResCreateJobDTO(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        if (jobService.fetchJobById(id) == null) {
            throw new IdInvalidException("Job với id = " + id + " không tồn tại");
        }
        this.jobService.handleDeleteJob(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("fetch job by id")
    public ResponseEntity<ResJobDTO> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        if (jobService.fetchJobById(id) == null) {
            throw new IdInvalidException("Job với id = " + id + " không tồn tại");
        }
        Job job = this.jobService.fetchJobById(id);
        return ResponseEntity.ok(this.jobService.convertToResJobDTO(job));
    }

    @GetMapping("/jobs")
    @ApiMessage("fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable) {

        ResultPaginationDTO jobs = this.jobService.fetchAllJob(spec, pageable);
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job postManJob) throws IdInvalidException {

        Job job = this.jobService.handleUpdateJob(postManJob);
        if (job == null) {
            throw new IdInvalidException("Job với id = " + postManJob.getId() + " không tồn tại");
        }

        return ResponseEntity.ok(this.jobService.convertToResUpdateJobDTO(job));
    }

}
