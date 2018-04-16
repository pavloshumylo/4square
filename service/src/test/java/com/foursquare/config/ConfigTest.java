package com.foursquare.config;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.dao.impl.MockSearchDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTest {

    @Autowired
    private Config config;

    @Test
    public void testConfig_ShouldReturnMockSearchDaoImpl() {
        SearchDao searchDaoReturned = config.searchDao();
        assertTrue(searchDaoReturned instanceof MockSearchDaoImpl);
    }
}
