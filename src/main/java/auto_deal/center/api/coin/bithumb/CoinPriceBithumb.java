package auto_deal.center.api.coin.bithumb;

import auto_deal.center.api.coin.CoinPrice;
import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

// 빗썸 API사용하기 위한 구현체
@Service
@Slf4j
public class CoinPriceBithumb implements CoinPrice {
    @Value("${bithumb.url}")
    private String bithumbUrl;

    private RestTemplate restTemplate;

    private CoinPriceBithumb(){
        restTemplate = new RestTemplate();
    }

    // 현재 코인의 모든 값을 받아온다.
    @Override
    public CoinApiRslt getPrices() {
        String coinNowPriceUrl = makeUrl("/public/ticker/ALL_KRW");

        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(coinNowPriceUrl, HttpMethod.GET, entity, Object.class);

        CoinApiRslt coinApiRslt = makeObjToCoin(response.getBody());
        log.info("CoinPriceBithumb.getPrice :::::::::: coinApiRslt ::::::::::::: {} ", coinApiRslt.toString());

        return coinApiRslt;
    }

    @Override
    public CoinOhlcvRslt getOhlcv(String ticker) {
        String coinNowPriceUrl = makeUrl("/public/candlestick/" + ticker + "_KRW/24h");

        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<CoinOhlcvRslt> response = restTemplate.exchange(coinNowPriceUrl, HttpMethod.GET, entity, CoinOhlcvRslt.class);
        return response.getBody();
    }

    public ResponseBody getNowPrices() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.bithumb.com/public/orderbook/ALL_KRW")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();

        return body;
    }

    @NotNull
    private String makeUrl(String nowPricePath) {
        String coinNowPriceUrl =  bithumbUrl + nowPricePath;
        return coinNowPriceUrl;
    }

    // Map으로 리턴받은 결과값을 자료객체로 변환한다.
    private CoinApiRslt makeObjToCoin(Object response) {
        ObjectMapper mapper = new ObjectMapper();

        Map coinApiRsltMap = Map.class.cast(response);
        Map coinApiRsltInData = Map.class.cast(coinApiRsltMap.get("data"));
        coinApiRsltMap.put("date", coinApiRsltInData.get("date").toString());
        coinApiRsltInData.remove("date");

        CoinApiRslt coinApiRslt = mapper.convertValue(coinApiRsltMap, CoinApiRslt.class);
        return coinApiRslt;
    }
}
