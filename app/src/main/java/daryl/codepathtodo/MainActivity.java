package daryl.codepathtodo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import daryl.codepathtodo.AddTodoDialogFragment.AddTodoDialogListener;
import daryl.codepathtodo.EditTodoDialogFragment.EditTodoDialogListener;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements EditTodoDialogListener,
        AddTodoDialogListener {

    ArrayList<Todo> arrayOfTodos;
    TodoAdapter todoAdapter;
    ListView lvItems;
    FloatingActionButton fabAddTodo;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        setUpFabAddTodo();
        arrayOfTodos = new ArrayList<>();
        TodoDatabaseHelper todoDatabaseHelper = new TodoDatabaseHelper(this);
        db = todoDatabaseHelper.getWritableDatabase();

        readItems();
        todoAdapter = new TodoAdapter(this, arrayOfTodos);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();
        setupEditItemListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
                                           long l) {
                removeItem(position);
                return true;
            }
        });
    }

    public void setupEditItemListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long position) {
                String todoText = arrayOfTodos.get((int) position).text;
                long dueDate = arrayOfTodos.get((int) position).dueDate;
                int priority = arrayOfTodos.get((int) position).priority;

                FragmentManager fragmentManager = getSupportFragmentManager();
                EditTodoDialogFragment editTodoDialogFragment = EditTodoDialogFragment.
                        newInstance("Edit Todo", todoText, (int) position, dueDate, priority);
                editTodoDialogFragment.show(fragmentManager, "fragment_edit_todo_dialog");
            }
        });
    }

    @Override
    public void onFinishEditTodoDialog(String todoText, int itemPosition, long dueDate,
                                       int priority) {
        Todo todo = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", arrayOfTodos.get(itemPosition).text).get();
        todo.text = todoText;
        todo.dueDate = dueDate;
        todo.priority = priority;
        cupboard().withDatabase(db).put(todo);
        arrayOfTodos.set(itemPosition, todo);
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishAddTodoDialog(String todoName, long dueDate, int priority) {
        Todo todoToAdd = new Todo(todoName, dueDate, priority);
        cupboard().withDatabase(db).put(todoToAdd);
        todoAdapter.add(todoToAdd);
    }

    private void setUpFabAddTodo() {
        FloatingActionButton fabAddTodo = (FloatingActionButton) findViewById(R.id.fabAddButton);
        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddTodoDialogFragment addTodoDialogFragment = AddTodoDialogFragment.newInstance("Add Todo");
                addTodoDialogFragment.show(fragmentManager, "fragment_add_todo_dialog");
            }
        });
    }

    private void readItems() {
        arrayOfTodos = new ArrayList<>();
        Cursor todos = cupboard().withDatabase(db).query(Todo.class).getCursor();
        try {
            QueryResultIterable<Todo> itr = cupboard().withCursor(todos).iterate(Todo.class);
            for (Todo todo : itr) {
                arrayOfTodos.add(todo);
            }
        } finally {
            todos.close();
        }
    }

    private void removeItem(int position) {
        Todo todo = arrayOfTodos.remove(position);
        Todo todoToRemove = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", todo.text).get();
        cupboard().withDatabase(db).delete(todoToRemove);
        todoAdapter.notifyDataSetChanged();
    }
}
