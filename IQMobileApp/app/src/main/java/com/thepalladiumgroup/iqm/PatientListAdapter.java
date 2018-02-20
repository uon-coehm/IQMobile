package com.thepalladiumgroup.iqm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.Patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/19]
 */
public class PatientListAdapter extends ArrayAdapter<Patient> {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(PatientListAdapter.class);
    Patient selectedPatient;
    private List<Patient> countryList;
    private Activity activity;

    public PatientListAdapter(Context context, int textViewResourceId, List<Patient> countryList) {
        super(context, textViewResourceId, countryList);
        this.countryList = countryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        selectedPatient = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.patient_list, parent, false);

            viewHolder.clientcode = (TextView) convertView.findViewById(R.id.textViewlvclientcode);
            viewHolder.fullname = (TextView) convertView.findViewById(R.id.textViewlvfullname);
            viewHolder.sex = (TextView) convertView.findViewById(R.id.textViewlvsex);
            viewHolder.age = (TextView) convertView.findViewById(R.id.textViewlvage);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // Populate the data into the template view using the data object
        viewHolder.clientcode.setText(String.valueOf(selectedPatient.getClientcode()));
        viewHolder.fullname.setText(selectedPatient.getFullName());
        viewHolder.sex.setText(selectedPatient.getGender());
        viewHolder.age.setText(String.valueOf(selectedPatient.getCurrentAgeString()));
        // Return the completed view to render on screen


        return convertView;
    }

    private class ViewHolder {
        TextView clientcode;
        TextView fullname;
        TextView sex;
        TextView age;
    }
}


