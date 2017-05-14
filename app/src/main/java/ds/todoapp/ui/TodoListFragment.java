package ds.todoapp.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ds.todoapp.BusProvider;
import ds.todoapp.R;
import ds.todoapp.TodoApplication;
import ds.todoapp.events.BaseEvent;
import ds.todoapp.events.DeleteTaskResultEvent;
import ds.todoapp.events.GetTasksResultEvent;
import ds.todoapp.events.RefreshEvent;
import ds.todoapp.models.Todo;
import ds.todoapp.models.User;
import ds.todoapp.services.TodosService;
import ds.todoapp.util.PrefsUtil;

import static ds.todoapp.events.BaseEvent.Type.FAIL;
import static ds.todoapp.events.BaseEvent.Type.SUCCESS;

public class TodoListFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = TodoListFragment.class.getName();
    private ListView todoListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout swipeRefreshLayoutEmptyView;

    private TodosService mTodoService;
    private SharedPreferences mSharedPreferences;
    private User user;

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
                            //TODO
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
        mSharedPreferences = TodoApplication.getApplication(getActivity()).getSharedPreferences(TodoApplication.PREFS_NAME, 0);
        mTodoService = TodoApplication.getApplication(getActivity()).getTodosService();
        mTodoListAdapter = new TodoListAdapter(getActivity(), R.layout.row);
        todoListView.setAdapter(mTodoListAdapter);
        todoListView.setOnItemClickListener(mItemClickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BusProvider.getInstance().register(this);
        user = PrefsUtil.getUser(mSharedPreferences);
        if(user != null) {
            setLoading(true);
            mTodoService.getTasks(user);
            Log.d(TAG, "GET TASKS called");
        } else {
            mTodoList.clear();
            mTodoListAdapter.clear();
            mTodoListAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onRefreshEvent(RefreshEvent event) {
        startRefresh();
    }

    @Override
    public void onRefresh() {
        startRefresh();
    }

    private void startRefresh() {
        user = PrefsUtil.getUser(mSharedPreferences);
        if(user != null) {
            setLoading(true);
            mTodoService.getTasks(user);
            Log.d(TAG, "GET TASKS called in refresh");
        }
    }

    @Subscribe
    public void onGetTasksResultEvent(GetTasksResultEvent result) {
        if(result.getType() == SUCCESS) {
            Log.d(TAG, "GET TASKS SUCCESS!!!");
            mTodoList = result.getTasks();
            setLoading(false);
            Collections.sort(mTodoList);
            mTodoListAdapter.clear();
            mTodoListAdapter.addAll(mTodoList);
            mTodoListAdapter.notifyDataSetChanged();
        }  else if (result.getType() == BaseEvent.Type.FAIL) {
            Log.d(TAG, "GET TASKS FAIL!!!");
            Toast.makeText(getActivity(), R.string.tasks_retrieve_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setLoading(final boolean loading) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(loading);
            }
        });
        swipeRefreshLayoutEmptyView.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutEmptyView.setRefreshing(loading);
            }
        });
    }

    /////list adapter
    private class TodoListAdapter extends ArrayAdapter<Todo> {

        private final LayoutInflater mInflater;
        TextView titleText;
        TextView detailsText;
        CheckBox stateCheck;
        ImageButton edit;
        ImageButton delete;

        public TodoListAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ViewGroup view;

            if (convertView == null) {
                view = (ViewGroup) mInflater.inflate(R.layout.row, parent, false);
            } else {
                view = (ViewGroup) convertView;
            }
            final Todo todo = getItem(position);
            titleText = (TextView) view.findViewById(R.id.title);
            detailsText = (TextView) view.findViewById(R.id.details);
            stateCheck = (CheckBox) view.findViewById(R.id.checkbox);
            titleText.setText(todo.getTitle());
            detailsText.setText(todo.getDetails());
            stateCheck.setChecked((todo.getState()== Todo.TodoState.done) ? true : false);

            edit = (ImageButton) view.findViewById(R.id.edit_btn);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(),TodoDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("TASK", todo);
                    startActivity(intent);
                }
            });

            delete = (ImageButton) view.findViewById(R.id.delete_btn);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTodoService.delete(todo.get_id());
                }
            });

            stateCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stateCheck.isChecked()) {
                        todo.setState(Todo.TodoState.done);
                    } else {
                        todo.setState(Todo.TodoState.open);
                    }
                    mTodoService.update(todo);
                }
            });
            return view;
        }
    }

    @Subscribe
    public void onDeleteResultEvent(DeleteTaskResultEvent result) {
        if(result.getType() == SUCCESS) {
            Toast.makeText(getActivity(), R.string.deleted_task_success, Toast.LENGTH_SHORT).show();
            startRefresh();
        } else if (result.getType() == FAIL) {
            Toast.makeText(getActivity(), R.string.deleted_task_fail, Toast.LENGTH_SHORT).show();
            startRefresh();
        }

    }
}
