package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Member.InviteMemberRequest;
import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.DTOs.Member.UpdateMemberRequest;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Services.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>>getProjectMember(@PathVariable Long projectId){
        Long userId=1L;
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId,userId));
    }
    @PostMapping
    private ResponseEntity<MemberResponse> inviteMembers(@PathVariable Long projectId,
                                                         @RequestBody @Valid InviteMemberRequest request){
        Long userId=1L;
        return ResponseEntity.status((HttpStatus.CREATED)).body(
                projectMemberService.inviteMember(projectId,request,userId)
        );

    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                projectMemberService.updateMemberRole(
                        projectId, memberId, request, userId
                )
        );
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ) {
        Long userId = 1L;
        projectMemberService.removeProjectMember(
                projectId, memberId, userId
        );
        return ResponseEntity.noContent().build();


    }



}
