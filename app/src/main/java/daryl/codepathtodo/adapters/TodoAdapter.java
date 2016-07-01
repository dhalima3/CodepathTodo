package daryl.codepathtodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import daryl.codepathtodo.R;
import daryl.codepathtodo.models.Todo;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private static class ViewHolder {
        TextView text;
        TextView dueDate;
        TextView priority;
    }

    public TodoAdapter(Context context, ArrayList<Todo> todos) {
        super(context, R.layout.item_todo, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.tvPriority);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(todo.text);
        viewHolder.dueDate.setText(new SimpleDateFormat("mm/dd/yy").format(new Date(todo.dueDate)));
        setPriorityViewHolder(todo, viewHolder);
        return convertView;
    }

    private void setPriorityViewHolder(Todo todo, ViewHolder viewHolder) {
        if (todo.priority == 0) {
            viewHolder.priority.setText("LOW");
            viewHolder.priority.setTextColor(Color.GREEN);
        } else if (todo.priority == 1) {
            viewHolder.priority.setText("MEDIUM");
            viewHolder.priority.setTextColor(Color.YELLOW);
        } else if (todo.priority == 2) {
            viewHolder.priority.setText("HIGH");
            viewHolder.priority.setTextColor(Color.RED);
        }
    }
}
