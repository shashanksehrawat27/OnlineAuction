package org.bidding.database.adapter;

import lombok.AllArgsConstructor;
import org.bidding.database.entity.BidEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.database.repository.BidRepository;
import org.bidding.domain.dto.BidDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BidAdapter {

    private final BidRepository bidRepository;
    private final EntityMapper entityMapper;

    public BidDTO save(BidDTO bidDTO) {
        BidEntity bidEntity = entityMapper.toBidEntity(bidDTO);
        return entityMapper.toBidDTO(bidRepository.save(bidEntity));
    }

    public BidDTO findById(Long id) {
        Optional<BidEntity> bidEntity = bidRepository.findById(id);
        return entityMapper.toBidDTO(bidEntity.orElse(null));
    }

    public List<BidDTO> findAll() {
        List<BidEntity> bidEntities = bidRepository.findAll();
        return bidEntities.stream().map(entityMapper::toBidDTO).collect(Collectors.toList());
    }

    public Boolean existsById(Long id) {
        return bidRepository.existsById(id);
    }

    public BidDTO findTopByProductIdOrderByAmountDesc(Long productId) {
        BidEntity bidEntity = bidRepository.findTopByProductIdOrderByAmountDesc(productId);
        return entityMapper.toBidDTO(bidEntity);
    }

    public List<BidDTO> findByProductId(Long productId) {
        List<BidEntity> bidEntities = bidRepository.findByProductId(productId);
        return bidEntities.stream().map(entityMapper::toBidDTO).collect(Collectors.toList());
    }


}