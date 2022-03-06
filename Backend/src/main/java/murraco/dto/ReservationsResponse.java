package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import murraco.model.AppUserRole;

import java.util.List;

@Data
@NoArgsConstructor
public class ReservationsResponse {
    @ApiModelProperty(position = 0)
    Integer ID;
    @ApiModelProperty(position = 1)
    Long movie_ID;
    @ApiModelProperty(position = 2)
    Integer user_ID;
    @ApiModelProperty(position = 3)
    Integer SeatsCount;
    @ApiModelProperty(position = 3)
    String MovieTitle;
    @ApiModelProperty(position = 3)
    String MovieDate;
}
