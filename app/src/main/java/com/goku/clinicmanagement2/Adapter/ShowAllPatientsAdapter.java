package com.goku.clinicmanagement2.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goku.clinicmanagement2.Database.PatientProfiledb;
import com.goku.clinicmanagement2.DoctorModel.PatientDetailsForAdminData;
import com.goku.clinicmanagement2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllPatientsAdapter extends RecyclerView.Adapter<ShowAllPatientsAdapter.ViewHolder> {

    PatientProfiledb ppdb;

    Context context;
    ArrayList<PatientDetailsForAdminData> pd= new ArrayList<>();

    public ShowAllPatientsAdapter(Context context, ArrayList<PatientDetailsForAdminData> pd) {
        this.context= context;
        this.pd= pd;
        ppdb= new PatientProfiledb(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_patients_item_list_layout, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(pd.get(position).getImageurl()).centerCrop().into(holder.cig);
        holder.mobile.setText(pd.get(position).getMoile());
        holder.name.setText(pd.get(position).getName());
        holder.age.setText(pd.get(position).getAge());
        holder.gender.setText(pd.get(position).getGender());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a1= new AlertDialog.Builder(context);
                a1.setIcon(R.drawable.delete);
                a1.setMessage("Are you sure you want to delete patient " + pd.get(position).getName().replace("Name:","") + " from this app?");
                a1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  try {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Patient").child(pd.get(position).getName().replace("Name:", "").replace("\\s+", "").trim() + " " +
                                    pd.get(position).getAge().replace("Mobile:", "").replace("\\s+", "").trim());
                            ref.removeValue();
                            ppdb.delete();
                            Toast.makeText(context, "Patient " + pd.get(position).getName().replace("Name:", "").replace("\\s+", "").trim() + " successfully", Toast.LENGTH_SHORT).show();
                      //  } catch (Exception e){}
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

                System.out.println("From ShowAllPatientsAdapter name is: " + pd.get(position).getName().replace("Name:","").replace("\\s+","").trim() +
                        " mobile is: " + pd.get(position).getAge().replace("Mobile:", "").replace("\\s+","").trim());

             //   int pos= position;
             //   pd.remove(pos);
             //   notifyItemRemoved(pos);
             //   notifyItemRangeChanged(pos, pd.size());
                //delete(pd.get(position).getName(), pd.get(position).getMobile(), pd.get(position).getFee())

            }
        });
    }

    @Override
    public int getItemCount() {
        return pd.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cig;
        ImageView delete;
        TextView name, gender, age, mobile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cig= (CircleImageView) itemView.findViewById(R.id.pat_logo_id);
            delete= (ImageView) itemView.findViewById(R.id.delete_id);
            name= (TextView) itemView.findViewById(R.id.name_id);
            gender= (TextView) itemView.findViewById(R.id.gender_id);
            age= (TextView) itemView.findViewById(R.id.age_id);
            mobile= (TextView) itemView.findViewById(R.id.mobile_id);
        }
    }
}
