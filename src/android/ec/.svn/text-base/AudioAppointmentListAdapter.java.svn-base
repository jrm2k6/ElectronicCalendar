package android.ec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AudioAppointmentListAdapter extends ArrayAdapter<String> {
	public AudioAppointmentListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View resultView = convertView;
		String nameFile = getItem(position);
		if(convertView== null){
			resultView = LayoutInflater.from(getContext()).inflate(R.layout.row_audio_appointment_list, parent,false);
		}
		TextView tvNameAudioAppointment = (TextView) resultView.findViewById(R.id.tv_name_file_audio);
		tvNameAudioAppointment.setText(nameFile);
		return resultView;
	}
}
