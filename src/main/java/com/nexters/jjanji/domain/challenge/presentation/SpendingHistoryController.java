package com.nexters.jjanji.domain.challenge.presentation;

import com.nexters.jjanji.domain.challenge.application.SpendingHistoryService;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/challenge/plan")
@RequiredArgsConstructor
public class SpendingHistoryController {

    private final SpendingHistoryService spendingHistoryService;
    @PostMapping("/{planId}/spending")
    public ResponseEntity addSpending(@PathVariable Long planId,@RequestBody SpendingSaveDto dto){
        spendingHistoryService.addSpendingHistory(planId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{planId}/spending")
    public ResponseEntity<SpendingDetailResponse> spendingList(@PathVariable Long planId){
        SpendingDetailResponse spendingList = spendingHistoryService.findSpendingList(planId);
        return ResponseEntity.ok(spendingList);
    }

}
