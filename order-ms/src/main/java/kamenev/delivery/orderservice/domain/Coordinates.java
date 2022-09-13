package kamenev.delivery.orderservice.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Coordinates implements Serializable {

    @NotEmpty
    public String latitude;

    @NotEmpty
    public String longitude;

}
