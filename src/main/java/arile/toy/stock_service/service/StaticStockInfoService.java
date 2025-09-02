package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.StaticStockInfo;
import arile.toy.stock_service.exception.entity.StaticStockInfoNotFoundException;
import arile.toy.stock_service.repository.StaticStockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StaticStockInfoService {

    private final StaticStockInfoRepository staticStockInfoRepository;

    public String loadShortCodeByStockName(String stockName){
        return staticStockInfoRepository.findByStockName(stockName)
                .map(StaticStockInfo::getShortCode)
                // Optional
                .orElseThrow(() -> new StaticStockInfoNotFoundException(stockName));
    }

    public List<String> loadStockNameList() {
        List<StaticStockInfo> staticStockInfoList = staticStockInfoRepository.findAll();

        return staticStockInfoList.stream()
                .map(StaticStockInfo::getStockName)
                .toList();
    }
}
