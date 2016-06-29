package daryl.codepathtodo;

public class Todo {

    public Long _id;
    public String text;
    public Long dueDate;
    public int priority;

    public Todo() {
        this.text = "";
        this.dueDate = 0L;
        this.priority = 0;
    }

    public Todo(String text, Long dueDate, int priority) {
        this.text = text;
        this.dueDate = dueDate;
        this.priority = priority;
    }
}
