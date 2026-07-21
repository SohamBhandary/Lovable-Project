package com.Soham.Account_Service.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String stripePriceId;

    private Integer maxProjects;
    private Integer maxTokensPerDay;
    private Integer maxPreviews; // max preview allowed
    private Boolean unlimitedAi;  // unlimited access to llm ignore maxtokenperday
    private Boolean active;
}