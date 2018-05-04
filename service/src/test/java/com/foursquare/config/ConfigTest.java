package com.foursquare.config;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.impl.SearchDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ConfigTest {

    @InjectMocks
    private Config config;

    @Test
    public void testConfig_ShouldReturnSearchDaoImpl() {
        SearchDao searchDaoReturned = config.searchDao();
        assertTrue(searchDaoReturned instanceof SearchDaoImpl);
    }
}
