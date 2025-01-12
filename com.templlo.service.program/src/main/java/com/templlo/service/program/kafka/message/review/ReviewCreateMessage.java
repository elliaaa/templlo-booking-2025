package com.templlo.service.program.kafka.message.review;

import java.util.UUID;

public record ReviewCreateMessage(
        UUID userId,
        UUID programId,
        double rating
) {
}
