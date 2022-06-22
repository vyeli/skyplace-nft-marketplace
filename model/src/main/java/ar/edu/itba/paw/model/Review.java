package ar.edu.itba.paw.model;
import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "reviews_id_seq", name = "reviews_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "title")
    private String title;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reviewer", referencedColumnName = "id", nullable = false)
    private User usersByIdReviewer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reviewee", referencedColumnName = "id", nullable = false)
    private User usersByIdReviewee;

    /* default */ Review() {

    }

    public Review(User reviewer, User reviewee, int score, String title, String comments) {
        this.usersByIdReviewer = reviewer;
        this.usersByIdReviewee = reviewee;
        this.score = score;
        this.title = title;
        this.comments = comments;
    }

    public Review(int id, User reviewer, User reviewee, int score, String title, String comments) {
        this.id = id;
        this.usersByIdReviewer = reviewer;
        this.usersByIdReviewee = reviewee;
        this.score = score;
        this.title = title;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public User getUsersByIdReviewer() {
        return usersByIdReviewer;
    }

    public User getUsersByIdReviewee() {
        return usersByIdReviewee;
    }

}
