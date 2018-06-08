package com.foursquare.repository;

import com.foursquare.entity.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category firstCategoryExpected, secondCategoryExpected;

    @Before
    public void init() {
        firstCategoryExpected = initializeEntity();
        secondCategoryExpected = initializeEntity();
    }

    @Test
    public void testSave_ShouldSaveAndReturnExpectedCategory() {
        Category firstCategoryActual = categoryRepository.save(firstCategoryExpected);
        Category secondCategoryActual = categoryRepository.save(secondCategoryExpected);
        long countActual = categoryRepository.count();

        assertEquals(firstCategoryExpected, firstCategoryActual);
        assertEquals(secondCategoryExpected, secondCategoryActual);
        assertEquals(2, countActual);
    }

    @Test
    public void testGet_ShouldReturnProperCategoryById() {
        entityManager.persist(firstCategoryExpected);
        entityManager.persist(secondCategoryExpected);

        Category firstCategoryActual = categoryRepository.getOne(firstCategoryExpected.getId());
        Category secondCategoryActual = categoryRepository.getOne(secondCategoryExpected.getId());

        assertEquals(firstCategoryExpected, firstCategoryActual);
        assertEquals(secondCategoryExpected, secondCategoryActual);
    }

    @Test
    public void testFindAll_ShouldReturnAllCategories() {
        entityManager.persist(firstCategoryExpected);
        entityManager.persist(secondCategoryExpected);

        List<Category> categoriesExpected = categoryRepository.findAll();

        assertEquals(categoriesExpected, Arrays.asList(firstCategoryExpected, secondCategoryExpected));
    }

    @Test
    public void testDelete_ShouldDeleteProperCategory() {
        entityManager.persist(firstCategoryExpected);
        entityManager.persist(secondCategoryExpected);

        categoryRepository.delete(firstCategoryExpected);
        categoryRepository.delete(secondCategoryExpected);
        assertThat(categoryRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindByFsId_ShouldReturnProperCategoryByFsId() {
        entityManager.persist(firstCategoryExpected);
        Category categoryActual = categoryRepository.findByFsId(firstCategoryExpected.getFsId());

        assertEquals(firstCategoryExpected, categoryActual);
    }

    @Test
    public void testFindByFsId_notExistingCategory_ShouldReturnNull() {
        entityManager.persist(firstCategoryExpected);
        Category categoryActual = categoryRepository.findByFsId("notExistingCategoryFsId");

        assertNull(categoryActual);
    }

    private Category initializeEntity() {
        Category category = new Category();
        category.setFsId("fourSquareId");
        category.setName("categoryName");
        return category;
    }
}