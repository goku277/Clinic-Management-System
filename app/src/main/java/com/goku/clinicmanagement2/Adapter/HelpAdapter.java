package com.goku.clinicmanagement2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goku.clinicmanagement2.DoctorModel.FetchCardiologistData;
import com.goku.clinicmanagement2.Patient.CollapsingToolbar;
import com.goku.clinicmanagement2.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HelpAdapter extends RecyclerView.Adapter {

    ArrayList<FetchCardiologistData> fcdList;

    Context context;

    public HelpAdapter(ArrayList<FetchCardiologistData> fcdList, Context context) {
        this.fcdList= fcdList;
        this.context= context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardiologist_item_layout, parent,false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        FetchCardiologistData fetchData= fcdList.get(position);

        viewHolder.name.setText("Name: \t" + fetchData.getName());
        viewHolder.fees.setText("Fees: \t" + fetchData.getFees());
        //   viewHolder.schedule.setText("Schedule: \n" + fetchData.getSchedule());
        //    viewHolder.mobile.setText("Mobile: \t"+ fetchData.getMobile());
        Glide.with(context).load(fetchData.getImageUrl()).centerCrop().into(viewHolder.cig);

        System.out.println("From onBindViewHolder() Name: " + fetchData.getName() + " Fees: " + fetchData.getFees() + " Mobile: " + fetchData.getMobile());

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicled on: " + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "" + fcdList.get(position).getName() + "\n" + fcdList.get(position).getMobile(), Toast.LENGTH_SHORT).show();

                Intent sendData= new Intent(context, CollapsingToolbar.class);
                sendData.putExtra("name", fcdList.get(position).getName());
                sendData.putExtra("fees", fcdList.get(position).getFees());
                sendData.putExtra("mobile", fcdList.get(position).getMobile());

                String schedule= fcdList.get(position).getSchedule();

                String schedules[]= schedule.split("\\  +");

                for (String s: schedules) {
                    System.out.println("From HelpAdapter schedules: " + s);
                }

                sendData.putExtra("schedule", schedule);
                sendData.putExtra("imageurl", fcdList.get(position).getImageUrl());
                sendData.putExtra("specialization", fcdList.get(position).getSpecialization());

                sendData.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(sendData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fcdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cig;

        TextView name, fees, schedule, mobile;

        CardView cv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.cardiologist_name_id);
            fees= itemView.findViewById(R.id.cardiologist_fees_id);
            //  schedule= itemView.findViewById(R.id.cardiologist_schedule_id);
            //  schedule.setMovementMethod(new ScrollingMovementMethod());
            //   mobile= itemView.findViewById(R.id.cardiologist_mobile_id);
            cig= (CircleImageView) itemView.findViewById(R.id.c_img_cardiologist_id);
            cv= (CardView) itemView.findViewById(R.id.cardview_1);
        }
    }
}