package com.thepalladiumgroup.iqm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.Patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/19]
 */
public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.PatientViewHolder> {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(PatientRecyclerAdapter.class);
    private final OnItemClickListener listener;
    private final OnItemDeleteListener listenerDelete;
    private List<Patient> patientList;
    private LayoutInflater mInflater;


    public PatientRecyclerAdapter(Context context, List<Patient> patientList, OnItemClickListener listener, OnItemDeleteListener listenerDelete) {
        this.patientList = patientList;
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.listenerDelete = listenerDelete;

    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.patient_list_item, parent, false);
        PatientViewHolder holder = new PatientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        Patient selectedPatient = patientList.get(position);
        holder.setData(selectedPatient, position, listener, listenerDelete);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Patient actviePatient);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Patient actviePatient);
    }

    class PatientViewHolder extends RecyclerView.ViewHolder {

        Patient selectedPatient;
        int position;
        TextView clientcode, fullname, sex, age;
        ImageButton delete;

        public PatientViewHolder(View itemView) {
            super(itemView);
            clientcode = (TextView) itemView.findViewById(R.id.textViewlvclientcode);
            fullname = (TextView) itemView.findViewById(R.id.textViewlvfullname);
            sex = (TextView) itemView.findViewById(R.id.textViewlvsex);
            age = (TextView) itemView.findViewById(R.id.textViewlvage);
            itemView.setClickable(true);
            delete = (ImageButton) itemView.findViewById(R.id.imageButtonDelete);
        }

        public void setData(final Patient selectedPatient, int position, final OnItemClickListener listener, final OnItemDeleteListener listenerDelete) {
            clientcode.setText(String.valueOf(selectedPatient.getClientcode()));
            fullname.setText(selectedPatient.getFullName());
            sex.setText(selectedPatient.getGender());
            age.setText(String.valueOf(selectedPatient.getCurrentAgeString()));

            this.selectedPatient = selectedPatient;
            this.position = position;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(selectedPatient);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerDelete.onItemDelete(selectedPatient);
                }
            });

        }
    }
}


