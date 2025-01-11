package com.templlo.service.program.kafka.message.review;

import java.util.UUID;

public record ReviewUpdateMessage(
        UUID programId,
        double oldRating,
        double newRating
) {
}
