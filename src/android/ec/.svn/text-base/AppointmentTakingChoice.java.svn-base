package android.ec;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AppointmentTakingChoice extends Activity implements OnItemClickListener {

	private String sNumericMonth, currentMonth,  dateAppointment, currentDay;
	private int iNumericMonth;
	private final String TAG= Activity.class.getName();
	private final static Logger ECLogger = Logger.getLogger(GeneratorCalendar.class.getName());
	private FileHandler fh = null;
	private boolean append = true;
	private String fileLogging = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/actions.log";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			fh = new FileHandler(fileLogging,append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
		// TODO Auto-generated constructor stub
		
		this.setContentView(R.layout.dialog_appointment_taking_choice);
		ListView lvMethodChoice =  (ListView) findViewById(R.id.lv_dialog_appointment_taking_choice);
		DialogMethodChoiceAdapter adapter = new DialogMethodChoiceAdapter(this, R.layout.row_appointment_taking_choice);
		adapter.add("Handwriting method");
		adapter.add("Audio recording method");

		Intent currentIntent = this.getIntent();
		

		if(currentIntent.hasExtra("numericMonth")) {
			iNumericMonth = this.getIntent().getIntExtra("numericMonth", 13);
			if(iNumericMonth != 13) {
				sNumericMonth = String.valueOf(iNumericMonth);
				dateAppointment = currentIntent.getStringExtra("currentDay")+"_"+sNumericMonth+"_";
				Log.i(TAG, " dateAppointment transmitted : "+dateAppointment);

			}
			
			if(currentIntent.hasExtra("currentMonth")) currentMonth = currentIntent.getStringExtra("currentMonth");
			if(currentIntent.hasExtra("currentDay")) currentDay = currentIntent.getStringExtra("currentDay");
			
			lvMethodChoice.setOnItemClickListener(this);
			lvMethodChoice.setAdapter(adapter);

		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent newIntent = null;
		// TODO Auto-generated method stub
		switch(arg2){
		case 0:
			ECLogger.fine("Creation of appointment:: click on HandWriting method for day : "+currentDay+" of "+currentMonth);
			newIntent = new Intent(this, AppointmentManager.class);
			newIntent.putExtra("currentDay",currentDay);
			newIntent.putExtra("currentMonth", currentMonth);
			newIntent.putExtra("numericMonth", iNumericMonth);
			newIntent.putExtra("wantToCreateNewAppointment", true);
			break;
		case 1:
			ECLogger.fine("Creation of appointment:: click on AudioRecording method for day : "+currentDay+" of "+currentMonth);
			newIntent = new Intent(this, AppointmentAudioRecording.class);
			newIntent.putExtra("currentDay", currentDay);
			newIntent.putExtra("currentMonth", currentMonth);
			newIntent.putExtra("numericMonth", iNumericMonth);
			break;
		default:
			Log.d(TAG+" onClick ->",  "id not managed.");
			break;
		}
		
		if(newIntent !=null) {
			startActivity(newIntent);
			finish();
		}
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(fh != null) fh.close();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(fh!=null) fh.close();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(fh != null) fh.close();
	}
}
