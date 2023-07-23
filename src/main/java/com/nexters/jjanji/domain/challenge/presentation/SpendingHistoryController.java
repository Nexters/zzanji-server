package com.nexters.jjanji.domain.challenge.presentation;

import com.nexters.jjanji.domain.challenge.application.SpendingHistoryService;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<SpendingDetailResponse> spendingList(@PathVariable Long planId,
                                                               @RequestParam(required = false) Long cursorId,
                                                               @RequestParam Integer size){
        SpendingDetailResponse spendingList = spendingHistoryService.findSpendingList(planId, cursorId, PageRequest.ofSize(size));
        return ResponseEntity.ok(spendingList);
    }

}
