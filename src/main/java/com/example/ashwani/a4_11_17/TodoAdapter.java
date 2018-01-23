package com.example.ashwani.a4_11_17;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


import static com.example.ashwani.a4_11_17.R.id.checkbox;

/**
 * Created by ashwani on 04-11-2017.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    Context context;
    ArrayList<Todo> arrayList;

    TodoDatabase todoDatabase ;
    int flag;

    public TodoAdapter(Context context, ArrayList<Todo> arrayList , TodoDatabase todoDatabase) {
        this.context = context;
        this.arrayList = arrayList;
        this.todoDatabase= todoDatabase;

    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        final Todo curentitem = arrayList.get(position);
        holder.id.setText(curentitem.getId() + "");
        holder.title.setText(curentitem.getTitle());
        holder.desc.setText(curentitem.getDescription());
        holder.checkBox.setChecked(false);
        holder.imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent  = new Intent(context, ReminderActivity.class);
               // intent.putExtra("ID", curentitem.getId());
                Bundle bundle = new Bundle();
                bundle.putInt("ID", curentitem.getId());
                bundle.putSerializable("DATABASE OBJECT", todoDatabase);
                intent.putExtra("VALUES", bundle);
                context.startActivity(intent);
                Log.e("TAG", "intent called");

            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    todoDatabase.deleteTodo(curentitem);
                   // buttonView.setChecked(false);

                    //MainActivity.rec.setAdapter(MainActivity.todoAdapter);
                  // Log.e("TAG", "todoAdapter notified");
//                    flag =1;
//                    Intent intent;
//                    intent = new Intent(context, MainActivity.class);
//                    intent.putExtra("value", flag);
//                    context.startActivity(intent);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void removeItem(int position) {
        arrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Todo item, int position) {
        arrayList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView id, title, desc ;
        CheckBox checkBox;
        ImageButton imagebtn;

        public TodoViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(checkbox);
            id = (TextView) itemView.findViewById(R.id.id);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            imagebtn = (ImageButton) itemView.findViewById(R.id.imgbtn);
        }
    }
}

