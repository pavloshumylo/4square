package com.foursquare.dao;

import com.foursquare.dao.dao.impl.SearchDaoImpl;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SearchDaoImplTest {

    private SearchDao searchDao;

    @Before
    public void init() {
        searchDao = new SearchDaoImpl();
    }

    @Test(expected = NotImplementedException.class)
    public void testSearch_ShouldReturnException() {
        searchDao.search("testCity", "testPlace");
    }
}