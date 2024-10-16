package vn.javaproject.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.javaproject.jobhunter.domain.Permission;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResRoleDTO {
    private long id;
    private String name;
    private String description;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<Permission> permissions;
}
