package daryl.codepathtodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.R.attr.data;

public class EditItemActivity extends AppCompatActivity {

    String itemText;
    long itemPosition;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etNewItem = (EditText) findViewById(R.id.etNewItem);

        initializeNewItemEditText();
    }

    public void onSave(View v) {
        itemText = etNewItem.getText().toString();
        Intent data = new Intent();
        data.putExtra("itemText", itemText);
        data.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK, data);
        this.finish();
    }

    private void initializeNewItemEditText() {
        itemText = getIntent().getStringExtra("itemText");
        itemPosition = getIntent().getLongExtra("itemPosition", 0);
        etNewItem.setText("");
        etNewItem.append(itemText);
    }
}
