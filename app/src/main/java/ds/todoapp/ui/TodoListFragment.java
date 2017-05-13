package ds.todoapp.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ds.todoapp.R;
import ds.todoapp.models.Todo;

public class TodoListFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = TodoListFragment.class.getName();
    private ListView todoListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout swipeRefreshLayoutEmptyView;

    private List<Todo> mTodoList = new ArrayList<>();
    private TodoListAdapter mTodoListAdapter;
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            //TODO
            final Todo item = (Todo) parent.getItemAtPosition(position);
            view.animate().setDuration(2000).alpha(0)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            //list.remove(item);
                            //TODO
                            mTodoListAdapter.notifyDataSetChanged();
                            view.setAlpha(1);
                        }
                    });
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        todoListView = (ListView) view.findViewById(R.id.todos_listview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayoutEmptyView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_emptyView);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayoutEmptyView.setOnRefreshListener(this);
        swipeRefreshLayoutEmptyView.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        todoListView.setEmptyView(swipeRefreshLayoutEmptyView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTodoListAdapter = new TodoListAdapter(this.getActivity(), R.layout.row);
        todoListView.setAdapter(mTodoListAdapter);
        todoListView.setOnItemClickListener(mItemClickListener);
    }


    @Override
    public void onRefresh() {
        //TODO
    }


    /////list adapter
    private class TodoListAdapter extends ArrayAdapter<Todo> {

        private final LayoutInflater mInflater;
        TextView titleText;
        TextView detailsText;
        CheckBox stateCheck;

        public TodoListAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewGroup view;

            if (convertView == null) {
                view = (ViewGroup) mInflater.inflate(R.layout.row, parent, false);
            } else {
                view = (ViewGroup) convertView;
            }
            Todo todo = getItem(position);
            titleText = (TextView) getActivity().findViewById(R.id.title);
            detailsText = (TextView) getActivity().findViewById(R.id.details);
            stateCheck = (CheckBox) getActivity().findViewById(R.id.checkbox);
            titleText.setText(todo.getTitle());
            detailsText.setText(todo.getDetails());
            stateCheck.setChecked((todo.getState()== Todo.TodoState.OPEN) ? false : true);
            return view;
        }
    }
}
