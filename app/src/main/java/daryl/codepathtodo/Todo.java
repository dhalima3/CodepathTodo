package daryl.codepathtodo;

public class Todo {

    public Long _id;
    public String text;

    public Todo() {
        this.text = "";
    }

    public Todo(String text) {
        this.text = text;
    }
}
