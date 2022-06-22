package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewJpaDaoTest {

    private final static String MAIL_USER1 = "test1@test.test";
    private final static String USERNAME_USER1 = "test1";
    private final static String WALLET_USER1 = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";
    private final static Chain WALLETCHAIN_USER1 = Chain.Ethereum;
    private final static String PASSWORD_USER1 = "PASSUSER1";
    private final static Role ROLE_USER1 = Role.User;
    private final static String LOCALE_USER1 = "en";

    private final static String MAIL_USER2 = "test2@test.test";
    private final static String USERNAME_USER2 = "test2";
    private final static String WALLET_USER2 = "0xE5Da717E6d0186bE40eEa87De0A5aC9d5c4e5666";
    private final static Chain WALLETCHAIN_USER2 = Chain.Ethereum;
    private final static String PASSWORD_USER2 = "PASSUSER2";
    private final static Role ROLE_USER2 = Role.User;
    private final static String LOCALE_USER2 = "en";

    private final static int REVIEW_ID = 1;
    private final static int REVIEW_SCORE = 3;
    private final static String REVIEW_TITLE = "MY_REVIEW_TITLE";
    private final static String REVIEW_DESC = "MY_REVIEW_DESC";
    private final static String REVIEW_SORT = "MY_REVIEW_SORT";

    private final static String REVIEW_TABLE = "reviews";

    private final static int PAGE_NUM = 1;
    private final static int PAGE_SIZE = 5;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReviewJpaDao reviewJpaDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, REVIEW_TABLE);
    }


    @Test
    public void testAddReview(){
        User reviewee = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User reviewer = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);

        em.persist(reviewee);
        em.persist(reviewer);

        reviewJpaDao.addReview(reviewer, reviewee, REVIEW_SCORE, REVIEW_TITLE, REVIEW_DESC);

        em.flush();

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REVIEW_TABLE));
    }

    @Test
    public void testGetUserReviews(){
        User reviewee = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        em.persist(reviewee);

        for (int i = 0; i < 5; i++) {
            User reviewer = new User(USERNAME_USER1 + i, WALLET_USER1, MAIL_USER1 + i, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
            em.persist(reviewer);

            Review review = new Review(reviewer, reviewee, REVIEW_SCORE, REVIEW_TITLE, REVIEW_DESC);
            em.persist(review);
        }

        List<Review> userReviews = reviewJpaDao.getUserReviews(PAGE_NUM, reviewee.getId(), PAGE_SIZE);

        assertEquals(5, userReviews.size());
    }

    @Test
    public void testHasNoReviewByUser(){
        User reviewee = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User reviewer = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);

        em.persist(reviewee);
        em.persist(reviewer);

        boolean result = reviewJpaDao.hasReviewByUser(reviewer.getId(), reviewee.getId());

        assertFalse(result);
    }

    @Test
    public void testHasReviewByUser(){
        User reviewee = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User reviewer = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Review review = new Review(reviewer, reviewee, REVIEW_SCORE, REVIEW_TITLE, REVIEW_DESC);

        em.persist(reviewee);
        em.persist(reviewer);
        em.persist(review);

        boolean result = reviewJpaDao.hasReviewByUser(reviewer.getId(), reviewee.getId());

        assertTrue(result);
    }

    @Test
    public void testGetUserReviewsAmount(){
        User reviewee = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        em.persist(reviewee);

        for (int i = 0; i < 15; i++) {
            User reviewer = new User(USERNAME_USER1 + i, WALLET_USER1, MAIL_USER1 + i, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
            em.persist(reviewer);

            Review review = new Review(reviewer, reviewee, REVIEW_SCORE, REVIEW_TITLE, REVIEW_DESC);
            em.persist(review);
        }

        long reviewAmount = reviewJpaDao.getUserReviewsAmount(reviewee.getId());

        assertEquals(15, reviewAmount);
    }

}
