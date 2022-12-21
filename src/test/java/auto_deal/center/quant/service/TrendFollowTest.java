package auto_deal.center.quant.service;

import auto_deal.center.coin.service.CoinRdbSyncManager;
import auto_deal.center.quant.model.QuantModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TrendFollowTest {

    @Autowired
    public TrendFollow trendFollow;

    @Autowired
    private CoinRdbSyncManager coinRdbSyncManager;

    @Test
    @DisplayName( "추세상승 구매 테스트한다.")
    public void getTest(){
        //given
        String bitcoinTicker = "BTC";

        //when
        QuantModel quantModel = trendFollow.get(bitcoinTicker);

        //then
        Assertions.assertThat(quantModel.toRsltStr()).isNotNull();
    }

    @Test
    @DisplayName( "추세상승 구매 전체 가져오기 테스트한다.")
    public void getAllTest(){
        //given
        coinRdbSyncManager.updateCoinToDb();

        //when
        List<QuantModel> models = trendFollow.getAll();

        //then

        Assertions.assertThat(models).isNotNull();
    }

}