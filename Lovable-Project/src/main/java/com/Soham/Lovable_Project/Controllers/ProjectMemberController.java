package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Member.InviteMemberRequest;
import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Services.ProjectMemberService;
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
    public ResponseEntity<List<ProjectMember>>getProjectMember(@PathVariable Long projectId){
        Long userId=1L;
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId,userId));
    }
    @PostMapping
    private ResponseEntity<MemberResponse> inviteMembers(@PathVariable Long projectId,
                                                         @RequestBody InviteMemberRequest request){
        Long userId=1L;
        return ResponseEntity.status((HttpStatus.CREATED)).body(
                projectMemberService.inviteMember(projectId,request,userId)
        );

    }
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestBody InviteMemberRequest request
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                projectMemberService.updateMemberRole(
                        projectId, memberId, request, userId
                )
        );
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponse> deleteMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                projectMemberService.deleteProjectMember(
                        projectId, memberId, userId
                )
        );
    }



}
