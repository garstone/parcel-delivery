package kamenev.delivery.tokens.mapper;

import kamenev.delivery.common.security.UserDetails;
import kamenev.delivery.tokens.model.CreateTokensRequest;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    UserDetails fromCreateTokensRequest(CreateTokensRequest request);


}
