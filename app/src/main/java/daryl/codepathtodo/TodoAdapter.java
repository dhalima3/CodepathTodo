package daryl.codepathtodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private static class ViewHolder {
        TextView text;
        TextView dueDate;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(todo.text);
        viewHolder.dueDate.setText(new SimpleDateFormat("MM/dd/yy").format(new Date(todo.dueDate)));
        return convertView;
    }
}
