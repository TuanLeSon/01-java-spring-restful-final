package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResSkillDTO {
    private long id;
    private String name;
    private Instant updatedAt;
    private Instant createdAt;
    private String createdBy;
    private String updatedBy;
}
