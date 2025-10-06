package com.rashmita.systemservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name="access_group")
public class AccessGroup extends AbstractEntity {
    @Column(name="name")
    private String name;
    @Column(name="recorded_date")
    private Date recordedDate;
    @Column(name="created_date")
    private Date createdDate;
    @Column(name="updated_date")
    private Date updatedDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
}
