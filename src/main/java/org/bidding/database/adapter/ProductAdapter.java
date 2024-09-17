package org.bidding.database.adapter;

import lombok.AllArgsConstructor;
import org.bidding.database.entity.ProductEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.database.repository.ProductRepository;
import org.bidding.dto.ProductDTO;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductAdapter {
    private final ProductRepository productRepository;
    private final EntityMapper entityMapper;

    public ProductDTO save(ProductDTO productDTO) {
        ProductEntity productEntity = entityMapper.toProductEntity(productDTO);
        return entityMapper.toProductDTO(productRepository.save(productEntity));
    }

    public ProductDTO findById(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        return entityMapper.toProductDTO(productEntity.orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    public List<ProductDTO> findAll() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream().map(entityMapper::toProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> findByCategory(String category) {
        List<ProductEntity> productEntities = productRepository.findByCategory(category);
        return productEntities.stream().map(entityMapper::toProductDTO).collect(Collectors.toList());
    }

    public Boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
