package com.mdb.api.controller;


import com.mdb.api.model.StockSymbol;
import com.mdb.api.repository.StockSymbolRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StockController {
    @Autowired
    StockSymbolRepository stockSymbolRepository;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/api/stocks")
    List<StockSymbol> all() {
        return stockSymbolRepository.findAll();
    }
    // end::get-aggregate-root[]

    @GetMapping("/api/stock/{symbol}")
    StockSymbol findOneBySymbol(@PathVariable String symbol) { return stockSymbolRepository.findOneBySymbol(symbol); }

    @PostMapping("/api/stock")
    StockSymbol newStockSymbol(@RequestBody StockSymbol stockSymbol) {
        return stockSymbolRepository.save(stockSymbol);
    }

    @PutMapping("/api/stock/{symbol}")
    StockSymbol replaceStockSymbol(@RequestBody StockSymbol updatedStockSymbol, @PathVariable String symbol) throws BadRequestException {
        return stockSymbolRepository.updateBySymbol(symbol, updatedStockSymbol);
    }


    //TODO: Add Update Aggregation & Nesting Collection


    @DeleteMapping("/employees/{id}")
    void deleteStockSymbol(@PathVariable String id) {
        stockSymbolRepository.delete(stockSymbolRepository.findOneById(id));
    }

}
