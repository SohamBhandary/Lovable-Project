package com.Soham.Account_Service.DTOs.Subcription;

public record UsageTodayResponse(
        Integer tokensUsed,
        Integer tokenslimit,
        Integer preveiwsRunning,
        Integer previewsLimit

) {
}
