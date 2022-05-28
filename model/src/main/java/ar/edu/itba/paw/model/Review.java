package ar.edu.itba.paw.model;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

//    @Basic
//    @Column(name = "id_reviewer", nullable = false)
//    private int idReviewer;
//
//    @Basic
//    @Column(name = "id_reviewee", nullable = false)
//    private int idReviewee;

    @Basic
    @Column(name = "score", nullable = false)
    private int score;

    @Basic
    @Column(name = "comments", nullable = true, length = -1)
    private String comments;

    @ManyToOne
    @JoinColumn(name = "id_reviewer", referencedColumnName = "id", nullable = false)
    private User usersByIdReviewer;

    @ManyToOne
    @JoinColumn(name = "id_reviewee", referencedColumnName = "id", nullable = false)
    private User usersByIdReviewee;

    /* default */ Review() {

    }

    public Review(int idReviewer, int idReviewee, int score, String comments) {
//        this.idReviewer = idReviewer;
//        this.idReviewee = idReviewee;
        this.score = score;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getIdReviewer() {
//        return idReviewer;
//    }
//
//    public void setIdReviewer(int idReviewer) {
//        this.idReviewer = idReviewer;
//    }
//
//    public int getIdReviewee() {
//        return idReviewee;
//    }
//
//    public void setIdReviewee(int idReviewee) {
//        this.idReviewee = idReviewee;
//    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Review that = (Review) o;
//        return id == that.id && idReviewer == that.idReviewer && idReviewee == that.idReviewee && score == that.score && Objects.equals(comments, that.comments);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, idReviewer, idReviewee, score, comments);
//    }

    public User getUsersByIdReviewer() {
        return usersByIdReviewer;
    }

    public void setUsersByIdReviewer(User usersByIdReviewer) {
        this.usersByIdReviewer = usersByIdReviewer;
    }

    public User getUsersByIdReviewee() {
        return usersByIdReviewee;
    }

    public void setUsersByIdReviewee(User usersByIdReviewee) {
        this.usersByIdReviewee = usersByIdReviewee;
    }
}
