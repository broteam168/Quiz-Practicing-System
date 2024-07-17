
package Models;


public class SubjectDimension {
    private int dimension_id;
    private int type_id;
    private int subject_id;
    private String name;

    public SubjectDimension(int dimension_id, int type_id, int subject_id, String name) {
        this.dimension_id = dimension_id;
        this.type_id = type_id;
        this.subject_id = subject_id;
        this.name = name;
    }

    public int getDimension_id() {
        return dimension_id;
    }

    public void setDimension_id(int dimension_id) {
        this.dimension_id = dimension_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
