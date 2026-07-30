package com.Soham.Lovable_Project.Entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProjectMemberId {

Long  projectId;
Long userId;


}
