package org.bidding.service;

import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.enums.ProductCategoryEnum;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAllProducts();
    ProductDTO findProductByProductId(Long id);
    List<ProductDTO> getProductsByCategory(String category);
    ProductDTO addProduct(ProductDTO productDTO);
    ProductDTO updateProductDetail(Long id, ProductDTO productDTO);
    boolean deleteProduct(Long id);
}