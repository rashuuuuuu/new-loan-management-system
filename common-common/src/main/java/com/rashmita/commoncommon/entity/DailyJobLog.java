package com.rashmita.commoncommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_job_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyJobLog extends AbstractEntity {
    @Column(name="run_date")
    private LocalDate runDate;
    @Column(name="status")
    private String status;
    @Column(name="job_name")
    private String jobName;
}
