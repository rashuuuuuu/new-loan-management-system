package com.rashmita.systemservice.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name="status")
public class Status extends AbstractEntity {
    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="icon")
    private String icon;

}
