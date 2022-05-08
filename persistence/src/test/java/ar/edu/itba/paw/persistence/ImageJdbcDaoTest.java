package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.Collections;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJdbcDaoTest {

    private final MultipartFile image = new MockMultipartFile("image", new byte[1]);

    private static final String IMAGE_TABLE = "images";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert imageJdbcInsert;
    private ImageJdbcDao imageJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        imageJdbcDao = new ImageJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE)
                .usingGeneratedKeyColumns("id_image");
    }

    @Test
    public void testCreateImage() {
        imageJdbcDao.createImage(image);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
    }

    @Test
    public void testGetImageById() {
        int imageId = imageJdbcInsert.executeAndReturnKey(Collections.singletonMap("image", new byte[1])).intValue();

        Image image = imageJdbcDao.getImage(imageId);

        assertEquals(1,image.getImage().length);
        assertEquals(0,image.getImage()[0]);
    }
}
