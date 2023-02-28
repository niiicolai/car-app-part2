package dat3.car.application.dto;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty
    private HttpStatusCode status;

    @JsonProperty
    private String message;
}
