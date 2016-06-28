package daryl.codepathtodo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import static android.widget.TextView.*;


public class AddTodoDialogFragment extends DialogFragment implements OnEditorActionListener{

    private EditText etTodoName;

    public AddTodoDialogFragment() {
        // Required empty public constructor
    }

    public interface AddTodoDialogListener {
        void onFinishAddTodoDialog(String todoName, long dueDate);
    }

    public static AddTodoDialogFragment newInstance(String title) {
        AddTodoDialogFragment fragment = new AddTodoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_todo_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTodoName = (EditText) view.findViewById(R.id.etAddItemName);
        etTodoName.setOnEditorActionListener(this);

        setUpSaveButton(view);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        etTodoName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            onAddTodo();
            return true;
        }
        return false;
    }

    public void onAddTodo() {
        AddTodoDialogListener listener = (AddTodoDialogListener) getActivity();
        listener.onFinishAddTodoDialog(etTodoName.getText().toString(), new Date().getTime());
        dismiss();
    }

    private void setUpSaveButton(View view) {
        Button btnSave = (Button) view.findViewById(R.id.btnAddItem);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddTodo();
            }
        });
    }
}
