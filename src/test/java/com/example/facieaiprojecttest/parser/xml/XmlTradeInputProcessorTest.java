package com.example.facieaiprojecttest.parser.xml;

import com.example.facieaiprojecttest.model.Trade;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class XmlTradeInputProcessorTest {

    @InjectMocks
    private XmlTradeInputProcessor processor;

    private Trade trade;

    @BeforeEach
    void setup() {
        trade = new Trade();
        trade.setDate("20240226");
        trade.setCurrency("USD");
        trade.setPrice(100.0);
        trade.setProductId("123");
    }

    @Test
    void testParseSuccess() throws Exception {
        List<Trade> trades = new ArrayList<>();
        trades.add(trade);
        String inputData = "<trades><trade><date>20240226</date><currency>USD</currency><price>100.0</price><productId>123</productId></trade></trades>";

        XmlMapper realXmlMapper = new XmlMapper();
        XmlTradeInputProcessor processorForTest = new XmlTradeInputProcessor(realXmlMapper);

        List<Trade> result = processorForTest.parse(inputData);

        assertEquals(trades, result);
    }

    @Test
    void testParseError() throws Exception {
        String inputData = "<invalidXml></invalidXml>";

        XmlMapper realXmlMapper = new XmlMapper();
        XmlTradeInputProcessor processorForTest = new XmlTradeInputProcessor(realXmlMapper);

        assertThrows(Exception.class, () -> processorForTest.parse(inputData));
    }

    @Test
    void testGetSupportedFormat() {
        String format = processor.getSupportedFormat();

        assertEquals("xml", format);
    }
}


