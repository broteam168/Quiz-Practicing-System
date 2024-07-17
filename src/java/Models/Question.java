package Models;

public class Question {

    private int questionId;
    private String content;
    private String subject;
    private String lesson;
    private String dimension;
    private String level;
    private int status;

    public Question(int questionId, String content, String subject, String lesson, String dimension, String level, int status) {
        this.questionId = questionId;
        this.content = content;
        this.subject = subject;
        this.lesson = lesson;
        this.dimension = dimension;
        this.level = level;
        this.status = status;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
