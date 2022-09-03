package kamenev.delivery.orderservice.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Coordinates {

    @NotEmpty
    public String latitude;

    @NotEmpty
    public String longitude;

}
