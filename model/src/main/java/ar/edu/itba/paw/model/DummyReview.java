package ar.edu.itba.paw.model;

public class DummyReview {
    private User reviewer;

    private User reviewee;

    private int score;

    private String title;

    private String comments;

    public DummyReview(User reviewer, User reviewee, int score, String title, String comments) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.score = score;
        this.title = title;
        this.comments = comments;
    }

    public User getReviewer() {
        return reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getComments() {
        return comments;
    }
}
