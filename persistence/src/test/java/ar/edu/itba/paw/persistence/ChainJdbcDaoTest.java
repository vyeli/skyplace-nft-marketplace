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
public class ChainJdbcDaoTest {

    private ChainJdbcDao chainJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        chainJdbcDao = new ChainJdbcDao(ds);
    }

    @Test
    public void testChain() {
        List<String> chains = chainJdbcDao.getChains();

        assertEquals(4, chains.size());
        assertTrue(chains.contains("Kovan"));
        assertFalse(chains.contains("Solana"));
    }
}
