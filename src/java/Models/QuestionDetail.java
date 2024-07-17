package Models;

public class QuestionDetail {
    private int questionId;
    private int subjectId;
    private int dimensionId;
    private int lessonId;
    private int status;
    private String content;
    private String expaination;
    private String media;
    private int typeId;
    private int level;
    private String mediaType;
    public QuestionDetail()
    {
    
    }
    public QuestionDetail(int questionId, int subjectId, int dimensionId, int lessonId, int status, String content, String expaination, String media, int typeId, int level) {
        this.questionId = questionId;
        this.subjectId = subjectId;
        this.dimensionId = dimensionId;
        this.lessonId = lessonId;
        this.status = status;
        this.content = content;
        this.expaination = expaination;
        this.media = media;
        this.typeId = typeId;
        this.level = level;
    }

    public QuestionDetail(int questionId, int subjectId, int dimensionId, int lessonId, int status, String content, String expaination, String media, int typeId, int level, String mediaType) {
        this.questionId = questionId;
        this.subjectId = subjectId;
        this.dimensionId = dimensionId;
        this.lessonId = lessonId;
        this.status = status;
        this.content = content;
        this.expaination = expaination;
        this.media = media;
        this.typeId = typeId;
        this.level = level;
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpaination() {
        return expaination;
    }

    public void setExpaination(String expaination) {
        this.expaination = expaination;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

 
  
    
    
    
}
