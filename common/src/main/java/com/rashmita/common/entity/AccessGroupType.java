package com.rashmita.common.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class AccessGroupType extends AbstractEntity {
    @Column(unique = true, nullable = false)
    private String name;
}
