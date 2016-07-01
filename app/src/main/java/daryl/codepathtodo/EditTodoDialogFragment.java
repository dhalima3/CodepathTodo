package daryl.codepathtodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
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
import java.util.Date;

import static android.widget.TextView.OnEditorActionListener;
import static com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment.OnDateSetListener;

public class EditTodoDialogFragment extends DialogFragment implements
        OnEditorActionListener, OnDateSetListener {

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private EditText etTodoName;
    private TextView tvEditTodoDueDate;
    private Spinner spEditPriority;

    public EditTodoDialogFragment() {
        // Required empty public constructor
    }

    public interface EditTodoDialogListener {
        void onFinishEditTodoDialog(String todoName, int itemPosition, long dueDate,
                                    int priority);
    }

    public static EditTodoDialogFragment newInstance(String title, String todoText,
                                                     int itemPosition, long dueDate,
                                                     int priority) {
        EditTodoDialogFragment fragment = new EditTodoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("todoText", todoText);
        args.putInt("itemPosition", itemPosition);
        args.putLong("dueDate", dueDate);
        args.putInt("priority", priority);
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
        setUpPrioritySpinner(view);
        setUpDatePickerEditText(view);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        tvEditTodoDueDate = (TextView) view.findViewById(R.id.etEditTodoDueDate);
        long todoDueDate = getArguments().getLong("dueDate", 0);
        tvEditTodoDueDate.setText(new SimpleDateFormat("mm/dd/yy").format(new Date(todoDueDate)));

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
                toSaveDueDate(), spEditPriority.getSelectedItemPosition());
        dismiss();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

        tvEditTodoDueDate.setText(getString(R.string.calendar_date_picker_result_values,
                year, monthOfYear + 1, dayOfMonth));
    }

    private long toSaveDueDate() {
        long dueDate = 0;
        try {
            dueDate = new SimpleDateFormat("mm/dd/yy").parse(tvEditTodoDueDate.getText().
                    toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dueDate;
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

    private void setUpPrioritySpinner(View view) {
        spEditPriority = (Spinner) view.findViewById(R.id.spEditPriority);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEditPriority.setAdapter(spinnerAdapter);
        spEditPriority.setSelection(getArguments().getInt("priority", 1));
    }

    private void setUpDatePickerEditText(View view) {
        EditText etEditTodoDueDate = (EditText) view.findViewById(R.id.etEditTodoDueDate);
        etEditTodoDueDate.setInputType(InputType.TYPE_NULL);
        etEditTodoDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(EditTodoDialogFragment.this);
                cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        etEditTodoDueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                            .setOnDateSetListener(EditTodoDialogFragment.this);
                    cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
                }
            }
        });
    }
}
