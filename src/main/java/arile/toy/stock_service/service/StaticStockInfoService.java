package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.StaticStockInfo;
import arile.toy.stock_service.repository.StaticStockInfoRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("해당 이름의 종목이 없습니다 - stockName: " + stockName));
    }

    public List<String> loadStockNameList() {
        List<StaticStockInfo> staticStockInfoList = staticStockInfoRepository.findAll();

        return staticStockInfoList.stream()
                .map(StaticStockInfo::getStockName)
                .toList();
    }
}
