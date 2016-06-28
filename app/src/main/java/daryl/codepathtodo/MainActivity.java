package daryl.codepathtodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import daryl.codepathtodo.EditTodoDialogFragment.EditTodoDialogListener;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements EditTodoDialogListener {

    private final int EDIT_REQUEST_CODE = 15;
    private final int ADD_REQUEST_CODE = 16;
    ArrayList<Todo> arrayOfTodos;
    TodoAdapter todoAdapter;
    ListView lvItems;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        arrayOfTodos = new ArrayList<>();
        TodoDatabaseHelper todoDatabaseHelper = new TodoDatabaseHelper(this);
        db = todoDatabaseHelper.getWritableDatabase();

        readItems();
        todoAdapter = new TodoAdapter(this, arrayOfTodos);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();
        setupEditItemListener();
    }

    public void onAddItem(View v) {
        Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String itemText = "";
        long itemPosition = 0;
        long itemDueDate = 0;

        if (resultCode == RESULT_OK) {
            itemText = data.getExtras().getString("itemText");
            itemPosition = data.getExtras().getLong("itemPosition");
            itemDueDate = data.getExtras().getLong("itemDueDate");

            if (requestCode == EDIT_REQUEST_CODE) {
                putItem(itemText, (int) itemPosition, itemDueDate);
            } else if (requestCode == ADD_REQUEST_CODE) {
                addItem(itemText, itemDueDate);
            }
        }
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

                FragmentManager fragmentManager = getSupportFragmentManager();
                EditTodoDialogFragment editTodoDialogFragment = EditTodoDialogFragment.
                        newInstance("Edit Todo", todoText, (int) position, dueDate);
                editTodoDialogFragment.show(fragmentManager, "fragment_edit_todo_dialog");
            }
        });
    }

    @Override
    public void onFinishEditTodoDialog(String todoText, int itemPosition, long dueDate) {
        Todo todo = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", arrayOfTodos.get(itemPosition).text).get();
        todo.text = todoText;
        todo.dueDate = dueDate;
        cupboard().withDatabase(db).put(todo);
        arrayOfTodos.set(itemPosition, todo);
        todoAdapter.notifyDataSetChanged();
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

    private void addItem(String itemText, long dueDate) {
        Todo todoToAdd = new Todo(itemText, dueDate);
        cupboard().withDatabase(db).put(todoToAdd);
        todoAdapter.add(todoToAdd);
    }

    private void removeItem(int position) {
        Todo todo = arrayOfTodos.remove(position);
        Todo todoToRemove = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", todo.text).get();
        cupboard().withDatabase(db).delete(todoToRemove);
        todoAdapter.notifyDataSetChanged();
    }

    private void putItem(String itemText, int itemPosition, long itemDueDate) {
        Todo todo = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", arrayOfTodos.get(itemPosition).text).get();
        todo.text = itemText;
        todo.dueDate = itemDueDate;
        cupboard().withDatabase(db).put(todo);
        arrayOfTodos.set(itemPosition, todo);
        todoAdapter.notifyDataSetChanged();
    }
}
