package ar.edu.itba.paw.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class CategoryJdbcDaoTest {

    private CategoryJdbcDao categoryJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        categoryJdbcDao = new CategoryJdbcDao(ds);
    }

    @Test
    public void testCategory() {

        List<String> categories = categoryJdbcDao.getCategories();

        assertEquals(5, categories.size());
        assertTrue(categories.contains("Art"));
        assertFalse(categories.contains("Gaming"));
    }
}
