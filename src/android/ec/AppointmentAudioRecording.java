package android.ec;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AppointmentAudioRecording extends Activity implements android.view.View.OnClickListener{

	static final String TAG = Activity.class.getName();
	static final String pathToAudioAppointmentFolder = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ECAudioAppointment/";;
	private MediaRecorder recorder;
	private boolean isRecording;
	private String appointmentMonth;
	private String appointmentDay;
	private int iNumericMonthAppointment;
	private TextView tvRecordingInProgress;
	private ProgressBar pbRecordingInProgress;
	final static Logger ECLogger = Logger.getLogger(GeneratorCalendar.class.getName());
	static FileHandler fh = null;
	final static boolean append = true;
	private String fileLogging = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/actions.log";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			fh = new FileHandler(fileLogging,append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
		setContentView(R.layout.dialog_appointment_audio_recording);
		Intent currentIntent = this.getIntent();
		appointmentMonth = currentIntent.getStringExtra("currentMonth");
		appointmentDay = currentIntent.getStringExtra("currentDay");
		iNumericMonthAppointment = currentIntent.getIntExtra("numericMonth", 1000);
		ToggleButton btRecording = (ToggleButton) findViewById(R.id.bt_on_off_recording);
		tvRecordingInProgress = (TextView) findViewById(R.id.tv_record_in_progress);
		pbRecordingInProgress = (ProgressBar) findViewById(R.id.progressBar1);
		btRecording.setOnClickListener(this);
		btRecording.setId(1);
		isRecording = false;
		recorder = new MediaRecorder();
		if (!checkIfAudioAppointmentFolderExists(pathToAudioAppointmentFolder)) {
			Log.d(TAG, "checks if appointment audio recodings exists - false");
			createAudioAppointmentFolderIfNotExisting(pathToAudioAppointmentFolder);
		}


	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case 1: 
			if(!isRecording){
				pbRecordingInProgress.setVisibility(0);
				tvRecordingInProgress.setVisibility(0);
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				String nameFileTocreate = generateNameFileAudioAppointment(appointmentDay, String.valueOf(iNumericMonthAppointment));
				ECLogger.fine("AppointmentAudioRecording:: Recording of an appointment called : "+nameFileTocreate);
				ECLogger.finer("Recording for the date - day "+appointmentDay+" month "+appointmentMonth);
				recorder.setOutputFile(pathToAudioAppointmentFolder+nameFileTocreate);
				try {
					recorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recorder.start();   // Recording is now started
				isRecording = true;


			}
			else{
				recorder.stop();
				recorder.reset();
				isRecording=false;
				finish();
			}
			break;
		default:
			break;
		}

	}

	public boolean checkIfAudioAppointmentFolderExists(String pathToFolder){
		return (new File(pathToFolder).exists());
	}


	public void createAudioAppointmentFolderIfNotExisting(String pathToFolder){
		File folderAudioAppointment = new File(pathToFolder);
		folderAudioAppointment.mkdirs();
	}

	
	public String generateNameFileAudioAppointment(String appointmentDay, String appointmentMonth){
		String nameFileToCreate = null;
		final String DATE_FORMAT_NOW = "yyyy-MM-dd";
		final String TIME_FORMAT_NOW = "HH-mm-ss";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdfDateNow = new SimpleDateFormat(DATE_FORMAT_NOW);
		SimpleDateFormat sdfTimeNow = new SimpleDateFormat(TIME_FORMAT_NOW);
		String dateNow = sdfDateNow.format(cal.getTime());
		String timeNow = sdfTimeNow.format(cal.getTime());
		Log.i(TAG, "dateNow : "+dateNow);
		Log.i(TAG,"timeNow : "+timeNow);
		nameFileToCreate = "date_"+appointmentDay+"_"+appointmentMonth+"_acreated_"+dateNow+"_"+timeNow;
		return nameFileToCreate;
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(fh!= null) fh.close();
	}




}
