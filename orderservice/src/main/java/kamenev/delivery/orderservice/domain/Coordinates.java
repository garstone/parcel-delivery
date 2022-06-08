package kamenev.delivery.orderservice.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

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
