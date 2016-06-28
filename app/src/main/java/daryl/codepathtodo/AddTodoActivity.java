package daryl.codepathtodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class AddTodoActivity extends AppCompatActivity {

    String itemText;
    long itemPosition;
    long itemDueDate;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        etNewItem = (EditText) findViewById(R.id.etAddItemName);
    }

    public void onAddTodo(View v) {
        itemText = etNewItem.getText().toString();
        itemDueDate = new Date().getTime();
        Intent data = new Intent();
        data.putExtra("itemText", itemText);
        data.putExtra("itemDueDate", itemDueDate);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
