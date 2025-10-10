package com.rashmita.systemservice.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="access_group_type")
public class AccessGroupType extends AbstractEntity{
    @Column(unique = true, nullable = false)
    private String name;
}
