package vn.hoidanit.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Table(name = "companies")
@Getter
@Setter
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "ten cong ty ko duoc de trong")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;
    private Instant createAt;
    private Instant updateAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.setCreatedBy("createdBy");
        this.createAt = Instant.now();

    }
}
