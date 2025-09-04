package app.eurocars.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ChangedPasswordEvent {
    private String owner;
    private String email;
    private LocalDateTime changeDate;
}
