package com.goku.clinicmanagement2.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goku.clinicmanagement2.DoctorModel.DoctorDetailsForAdminData;
import com.goku.clinicmanagement2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllDoctorsAdapter extends RecyclerView.Adapter<ShowAllDoctorsAdapter.ViewHolder>{

    private static final Object TAG = "ShowAllDoctorsAdapter";
    ArrayList<DoctorDetailsForAdminData> dm= new ArrayList<>();
    Context context;

    public ShowAllDoctorsAdapter(Context context, ArrayList<DoctorDetailsForAdminData> dm) {
        this.context= context;
        this.dm= dm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_doctors_item_list_layout, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(dm.get(position).getImageUrl()).centerCrop().into(holder.cig);
        holder.mobile.setText(dm.get(position).getMobile());
        holder.schedule.setText(dm.get(position).getSchedule());
        holder.fees.setText(dm.get(position).getFee());
        holder.name.setText(dm.get(position).getName());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a1= new AlertDialog.Builder(context);
                a1.setIcon(R.drawable.delete);
                a1.setMessage("Are you sure you want to delete doctor " + dm.get(position).getName().replace("Name:","") + " from this app?");
                a1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pos= position;
                        dm.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, dm.size());
                        delete(dm.get(position).getName(), dm.get(position).getMobile(), dm.get(position).getFee());
                    }
                });
                a1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog a11= a1.create();
                a11.show();
            }
        });
    }

    private void delete(String name1, String mobile1, String fees1) {

        name1= name1.replace("Name:","").replace("\\s+","").trim();
        mobile1= mobile1.replace("Mobile:","").replace("\\s+","").trim();
        fees1= fees1.replace("Fees:","").replace("\\s+","").trim();
        String key= name1 + " " + mobile1 + " " + fees1;

        String specialization[]= {"Cardiologist", "Psychiatrist", "Dentist", "Surgeon", "EntSpecialist", "ChildSpecialist",
                                  "Urologist", "Veterenian", "Optometrist", "Chiropractor", "Dermatologist", "Gynaecologist", "Herbalist",
                                   "Paramedic"};

        String x= "Psychiatrist    Pikolo 8812055712 100";

        for (String s: specialization) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctor").child(s+ "    " + key);
            ref.removeValue();
        }

      //  String x= "Pikolo 8812055712 100";
        System.out.println("From delete() key is: " + key);

    }

    @Override
    public int getItemCount() {
        return dm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cig;

        ImageView delete;

        TextView name, fees, schedule, mobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cig= (CircleImageView) itemView.findViewById(R.id.doc_logo_id);
            delete= (ImageView) itemView.findViewById(R.id.dlete_id);
            name= (TextView) itemView.findViewById(R.id.name_id);
            fees= (TextView) itemView.findViewById(R.id.fees_id);
            schedule= (TextView) itemView.findViewById(R.id.schedule_id);
            mobile= (TextView) itemView.findViewById(R.id.mobile_id);
        }
    }
}