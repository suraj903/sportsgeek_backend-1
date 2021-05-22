package com.project.sportsgeek.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(description = "Contest Model")
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {
    private String message;
}
