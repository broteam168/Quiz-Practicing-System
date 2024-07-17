
package Models;

import DAOs.PracticeDAO;
import Models.Group;
import java.util.List;

public class Lesson {

    private int lesson_id;
    private int lesson_type;
    private String name;
    private int topic_id;
    private int subject_id;
    private int status;
    private String back_link;
    private String content;
    private int order;
    private String description;
    private int quiz_id;

    public Lesson() {
    }

    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public int getLesson_type() {
        return lesson_type;
    }

    public void setLesson_type(int lesson_type) {
        this.lesson_type = lesson_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBack_link() {
        return back_link;
    }

    public void setBack_link(String back_link) {
        this.back_link = back_link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }
    
    public String getTopic(){
        List<Group> topics = new PracticeDAO().getTopicsBySubjectId(subject_id);
        for (Group topic : topics) {
            if (topic.getId() == topic_id) {
                return topic.getName();
            }
        }
        return "";
    }
    
}
