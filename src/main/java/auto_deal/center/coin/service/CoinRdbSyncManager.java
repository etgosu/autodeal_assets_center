package auto_deal.center.coin.service;

public interface CoinRdbSyncManager {

    void updateCoinToDb();

    void initCoinName();

    void init3MAvgPrice();

    Boolean initNowPrices();
}
