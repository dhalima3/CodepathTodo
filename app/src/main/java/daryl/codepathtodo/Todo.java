package daryl.codepathtodo;

public class Todo {

    public Long _id;
    public String text;
    public Long dueDate;

    public Todo() {
        this.text = "";
        this.dueDate = 0L;
    }

    public Todo(String text, Long dueDate) {
        this.text = text;
        this.dueDate = dueDate;
    }
}
