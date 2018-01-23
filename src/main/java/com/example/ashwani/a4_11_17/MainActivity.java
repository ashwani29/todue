package com.example.ashwani.a4_11_17;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    RecyclerView rec;
    ArrayList<Todo> arrayList;
    TodoAdapter todoAdapter;
    FloatingActionButton fabn;
    AlertDialog alertDialog;
    private CoordinatorLayout coordinatorLayout;


    TodoDatabase todoDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         todoDatabase = new TodoDatabase( this, TodoDatabase.DB_NAME, null, TodoDatabase.DB_VERSION );




        rec = (RecyclerView) findViewById(R.id.rec);
        arrayList = todoDatabase.fetchALLTodos();
        todoAdapter = new TodoAdapter(this, arrayList, todoDatabase);
        fabn = (FloatingActionButton) findViewById(R.id.fabn);


        rec.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        rec.setAdapter(todoAdapter);

       // new ItemTouchHelper(SimpleItemTouchCallback).attachToRecyclerView(rec);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rec);

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Add ur ToDo")
                .setMessage("Add Title and Description")
                .setView(R.layout.fab)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText title = (EditText) alertDialog.findViewById(R.id.title);
                        EditText desc = (EditText) alertDialog.findViewById(R.id.desc);

                        String Title = title.getText().toString();
                        String Desc = desc.getText().toString();


                        Todo todo = new Todo(Title, Desc);
                        todoDatabase.insertTodo(todo);
                        arrayList.clear();
                        arrayList.addAll(todoDatabase.fetchALLTodos());
                        todoAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        Toast.makeText(MainActivity.this, "No Fields Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();

        fabn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "clicked");
                alertDialog.show();

                }
        });

        Intent  intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification builder = new Notification.Builder(this)
                                       .setContentTitle("Your Remaining Tasks")
                                       .setContentText("complete task")
                                       .setSmallIcon(R.mipmap.ic_launcher_write)
                                       .setContentIntent(pendingIntent)
                                       .setOngoing(true)
                                       .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.del) {
            arrayList.clear();
            arrayList.addAll(todoDatabase.fetchALLTodos());
            todoAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }



    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Log.e("TAG", "onMove called");
            return false;

        }

        @Override
        public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction ) {

            Log.e("TAG", "onSwiped called");
            // get the removed item name to display it in snack bar
            Todo todo = arrayList.get(viewHolder.getAdapterPosition());

            // backup of removed item for undo purpose
            final Todo deletedItem = arrayList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            todoAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(coordinatorLayout, todo.getDescription() + " removed from arrayList!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    todoAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            Log.e("TAG", "onSwiped ended");
        }


        @Override
        public void onChildDraw( Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // view the background view
            Log.e("TAG", "onChildDraw called");
        }


    };




}
