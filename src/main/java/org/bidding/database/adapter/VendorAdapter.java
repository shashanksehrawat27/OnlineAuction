package org.bidding.database.adapter;

import lombok.AllArgsConstructor;
import org.bidding.database.entity.VendorEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.database.repository.VendorRepository;
import org.bidding.dto.VendorDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class VendorAdapter {
    private VendorRepository vendorRepository;
    private EntityMapper entityMapper;

    public VendorDTO save(VendorDTO VendorDTO) {
        VendorEntity vendorEntity = entityMapper.toVendorEntity(VendorDTO);
        return entityMapper.toVendorDTO(vendorRepository.save(vendorEntity));
    }

    public VendorDTO findById(Long id) {
        Optional<VendorEntity> vendorEntity = vendorRepository.findById(id);
        return entityMapper.toVendorDTO(vendorEntity.orElse(null));
    }

    public List<VendorDTO> findAll() {
        List<VendorEntity> productEntities = vendorRepository.findAll();
        return productEntities.stream().map(entityMapper::toVendorDTO).collect(Collectors.toList());
    }

    public Boolean existsById(Long id) {
        return vendorRepository.existsById(id);
    }

    public Boolean existsByEmailId(String emailId) {
        return vendorRepository.existsByEmailId(emailId);
    }

    public void deleteById(Long id) {
        vendorRepository.deleteById(id);
    }
}
