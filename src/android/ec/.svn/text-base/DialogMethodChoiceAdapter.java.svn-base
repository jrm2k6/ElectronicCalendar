/**
 * 
 */
package android.ec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;


public class DialogMethodChoiceAdapter extends ArrayAdapter<String> {

	public DialogMethodChoiceAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View resultView = convertView;
		String nameMethod = getItem(position);
		if(convertView== null){
			resultView = LayoutInflater.from(getContext()).inflate(R.layout.row_appointment_taking_choice, parent,false);
		}
		RadioButton rbCurrentRow = (RadioButton) resultView.findViewById(R.id.rb_row_choice_method_taking_appointment);
		rbCurrentRow.setText(nameMethod);
		return resultView;
	}
}


