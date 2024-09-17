package org.bidding.database.mapper;
import org.bidding.database.entity.BidEntity;
import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.UserEntity;
import org.bidding.database.entity.VendorEntity;
import org.bidding.dto.BidDTO;
import org.bidding.dto.ProductDTO;
import org.bidding.dto.UserDTO;
import org.bidding.dto.VendorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    @Mapping(target = "name", source = "name")
    VendorEntity toVendorEntity(VendorDTO vendorDTO);
    VendorDTO toVendorDTO(VendorEntity vendor);

    BidEntity toBidEntity(BidDTO bidDTO);
    BidDTO toBidDTO(BidEntity bid);

    UserEntity toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(UserEntity userEntity);

    ProductEntity toProductEntity(ProductDTO productDTO);
    ProductDTO toProductDTO(ProductEntity productEntity);

}