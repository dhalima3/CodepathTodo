package daryl.codepathtodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        TodoDatabaseHelper todoDatabaseHelper = new TodoDatabaseHelper(this);
        db = todoDatabaseHelper.getWritableDatabase();

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupEditItemListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        addItem(itemText);
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String itemText = "";
        long itemPosition = 0;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            itemText = data.getExtras().getString("itemText");
            itemPosition = data.getExtras().getLong("itemPosition");
        }
        putItem(itemText, (int) itemPosition);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("itemText", items.get((int) l));
                intent.putExtra("itemPosition", l);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        items = new ArrayList<String>();
        Cursor todos = cupboard().withDatabase(db).query(Todo.class).getCursor();
        try {
            QueryResultIterable<Todo> itr = cupboard().withCursor(todos).iterate(Todo.class);
            for (Todo todo : itr) {
                items.add(todo.text);
            }
        } finally {
            todos.close();
        }
    }

    private void addItem(String itemText) {
        cupboard().withDatabase(db).put(new Todo(itemText));
        itemsAdapter.add(itemText);
    }

    private void removeItem(int position) {
        String todoText = items.remove(position);
        Todo todoToRemove = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", todoText).get();
        cupboard().withDatabase(db).delete(todoToRemove);
        itemsAdapter.notifyDataSetChanged();
    }

    private void putItem(String itemText, int itemPosition) {
        Todo todo = cupboard().withDatabase(db).query(Todo.class).
                withSelection("text = ?", items.get(itemPosition)).get();
        todo.text = itemText;
        cupboard().withDatabase(db).put(todo);
        items.set(itemPosition, itemText);
        itemsAdapter.notifyDataSetChanged();
    }
}
