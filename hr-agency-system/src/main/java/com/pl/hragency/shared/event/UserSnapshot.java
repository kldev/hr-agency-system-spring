package com.pl.hragency.shared.event;

import java.util.UUID;

public record UserSnapshot(UUID id, String fullName, String email) {
}
