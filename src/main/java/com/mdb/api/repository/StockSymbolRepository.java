package com.mdb.api.repository;

import com.mdb.api.model.StockSymbol;
import com.mongodb.client.result.DeleteResult;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.UpdateResult;

import java.util.List;
import java.util.Objects;

@Component
public class StockSymbolRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    public void updateSymbolLastSale(String symbol, float lastSalePrice) {
        Query query = new Query(Criteria.where("symbol").is(symbol));
        Update update = new Update();
        update.set("quantity", lastSalePrice);

        UpdateResult result = mongoTemplate.updateFirst(query, update, StockSymbol.class);

        if(result == null)
            System.out.println("No documents updated");
        else
            System.out.println(result.getModifiedCount() + " document(s) updated..");

    }

    public List<StockSymbol> findAll() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        return mongoTemplate.find(query, StockSymbol.class);
    }

    public StockSymbol findOneBySymbol(String symbol) {
        Query query = new Query(Criteria.where("symbol").is(symbol));
        return mongoTemplate.findOne(query, StockSymbol.class);
    }

    public StockSymbol findOneById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, StockSymbol.class);
    }

    public StockSymbol save(StockSymbol stockSymbol) {
        return mongoTemplate.save(stockSymbol);
    }

    public StockSymbol updateBySymbol(String symbol, StockSymbol stockSymbol) throws BadRequestException {
        System.out.println("repository enter for symbol: " + stockSymbol.getSymbol());
        if (!Objects.equals(symbol, stockSymbol.getSymbol())) {
            System.out.println("Symbol dies not match the Object symbol given: '" + stockSymbol.getSymbol() + "' - Smybol '" + symbol + "'");
            throw new BadRequestException("Symbol dies not match the Object symbol given.");
        }
        System.out.println("performing PERSIST for symbol: " + stockSymbol.getSymbol());
        return mongoTemplate.save(stockSymbol);
    }

    public DeleteResult delete(StockSymbol stockSymbol) {
        return mongoTemplate.remove(stockSymbol);
    }
}
