package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResJobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private Boolean active;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private List<String> skills;
}
