package vn.javaproject.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.javaproject.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResCreateJobDTO {
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
    private List<String> skills;

}
