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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.widget.TextView.OnEditorActionListener;
import static com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment.OnDateSetListener;


public class AddTodoDialogFragment extends DialogFragment implements OnEditorActionListener,
        OnDateSetListener {

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private EditText etTodoName;
    private TextView tvAddTodoDueDate;
    private Spinner spAddPriority;

    public AddTodoDialogFragment() {
        // Required empty public constructor
    }

    public interface AddTodoDialogListener {
        void onFinishAddTodoDialog(String todoName, long dueDate, int priority);
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
        tvAddTodoDueDate = (TextView) view.findViewById(R.id.tvAddTodoDueDate);

        setUpSaveButton(view);
        setUpDatePickerButton(view);
        setUpPrioritySpinner(view);

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
        listener.onFinishAddTodoDialog(etTodoName.getText().toString(), toSaveDueDate(),
                spAddPriority.getSelectedItemPosition());
        dismiss();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        tvAddTodoDueDate.setText(getString(R.string.calendar_date_picker_result_values,
                year, monthOfYear + 1, dayOfMonth));
    }

    private long toSaveDueDate() {
        long dueDate = 0;
        try {
            dueDate = new SimpleDateFormat("mm/dd/yy").parse(tvAddTodoDueDate.getText().
                    toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dueDate;
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

    private void setUpDatePickerButton(View view) {
        Button btnEditDueDate = (Button) view.findViewById(R.id.btnAddDueDate);
        btnEditDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddTodoDialogFragment.this);
                cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });
    }

    private void setUpPrioritySpinner(View view) {
        spAddPriority = (Spinner) view.findViewById(R.id.spAddPriority);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAddPriority.setAdapter(spinnerAdapter);
    }
}
