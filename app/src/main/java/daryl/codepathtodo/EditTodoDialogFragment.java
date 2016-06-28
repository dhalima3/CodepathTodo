package daryl.codepathtodo;

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

import static android.widget.TextView.OnEditorActionListener;

public class EditTodoDialogFragment extends DialogFragment implements
        OnEditorActionListener {

    private EditText etTodoName;

    public EditTodoDialogFragment() {
        // Required empty public constructor
    }

    public interface EditTodoDialogListener {
        void onFinishEditTodoDialog(String todoName, int itemPosition, long dueDate);
    }

    public static EditTodoDialogFragment newInstance(String title, String todoText,
                                                     int itemPosition, long dueDate) {
        EditTodoDialogFragment fragment = new EditTodoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("todoText", todoText);
        args.putInt("itemPosition", itemPosition);
        args.putLong("dueDate", dueDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_todo_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTodoName = (EditText) view.findViewById(R.id.etEditTodoName);
        etTodoName.setOnEditorActionListener(this);

        setUpSaveButton(view);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        String todoText = getArguments().getString("todoText", "");
        etTodoName.setText(todoText);
        etTodoName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            onSave();
            return true;
        }
        return false;
    }

    public void onSave() {
        EditTodoDialogListener listener = (EditTodoDialogListener) getActivity();
        int itemPosition = getArguments().getInt("itemPosition");
        listener.onFinishEditTodoDialog(etTodoName.getText().toString(), itemPosition,
                new Date().getTime());
        dismiss();
    }

    private void setUpSaveButton(View view) {
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
    }
}
