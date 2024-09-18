package org.bidding.database.mapper;
import org.apache.kafka.common.utils.Java;
import org.bidding.database.entity.BidEntity;
import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.UserEntity;
import org.bidding.database.entity.VendorEntity;
import org.bidding.domain.dto.BidDTO;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.dto.UserDTO;
import org.bidding.domain.dto.VendorDTO;
import org.bidding.domain.enums.ProductCategoryEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    VendorEntity toVendorEntity(VendorDTO vendorDTO);
    VendorDTO toVendorDTO(VendorEntity vendor);

    BidEntity toBidEntity(BidDTO bidDTO);
    BidDTO toBidDTO(BidEntity bid);

    UserEntity toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(UserEntity userEntity);

    ProductEntity toProductEntity(ProductDTO productDTO);

    ProductDTO toProductDTO(ProductEntity productEntity);


}