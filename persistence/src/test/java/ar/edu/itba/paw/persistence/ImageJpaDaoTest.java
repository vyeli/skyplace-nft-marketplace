package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {

    private final MultipartFile image = new MockMultipartFile("image", new byte[1]);
    public static final String IMAGE_TABLE = "images";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ImageJpaDao imageJpaDao;

    @Autowired
    private DataSource ds;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);
    }

    @Test
    public void testCreateImage() {
        imageJpaDao.createImage(image);

        em.flush();

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
    }

    @Test
    public void testGetImageById() {
        Image imageToInsert = new Image(new byte[]{0});
        em.persist(imageToInsert);

        Optional<Image> image = imageJpaDao.getImage(imageToInsert.getIdImage());

        assertTrue(image.isPresent());
        assertEquals(1,image.get().getImage().length);
        assertEquals(0,image.get().getImage()[0]);
    }

}
