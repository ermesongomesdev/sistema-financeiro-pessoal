package br.com.veltrium.finance.dto.debt;

import br.com.veltrium.finance.model.enums.DebtStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DebtResponse(
        Long id,
        String description,
        String creditor,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        Integer installments,
        LocalDate firstDueDate,
        DebtStatus status,
        List<DebtInstallmentResponse> installmentList
) {}
