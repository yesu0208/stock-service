package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.StockInfo;
import arile.toy.stock_service.repository.StockInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockInfoService {

    private final StockInfoRepository stockInfoRepository;

    public String loadShortCodeByStockName(String stockName){
        return stockInfoRepository.findByStockName(stockName)
                .map(StockInfo::getShortCode)
                // Optional
                .orElseThrow(() -> new EntityNotFoundException("해당 이름의 종목이 없습니다 - stockName: " + stockName));
    }

    public List<String> loadStockNameList() {
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();

        return stockInfoList.stream()
                .map(StockInfo::getStockName)
                .toList();
    }
}
