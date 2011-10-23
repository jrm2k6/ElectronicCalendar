package android.ec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AppointmentAudioDisplaying extends Activity implements OnItemClickListener {
	final String TAG = AppointmentAudioDisplaying.class.getName();
	ArrayList<String> listOfFilesToPlay = new ArrayList<String>();
	MediaController recordController;
	ListView lvAudioAppointmentCurrentDay;
	MediaPlayer player;
	final static Logger ECLogger = Logger.getLogger(GeneratorCalendar.class.getName());
	static FileHandler fh = null;
	final static boolean append = true;
	private String fileLogging = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/actions.log";
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			fh = new FileHandler(fileLogging, append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
		setContentView(R.layout.audio_appointment_media_player);
		 lvAudioAppointmentCurrentDay = (ListView) findViewById(R.id.lv_audio_appointment_for_selected_day);
		recordController = (MediaController) findViewById(R.id.media_controller_audio_appointment_currently_playing);
		Intent currentIntent = this.getIntent();
		if(currentIntent.hasExtra("listOfAudioAppointments")) listOfFilesToPlay = currentIntent.getStringArrayListExtra("listOfAudioAppointments");
		AudioAppointmentListAdapter adapter = new AudioAppointmentListAdapter(this, R.layout.row_audio_appointment_list);
		for(String nameAudioFile : listOfFilesToPlay) adapter.add(nameAudioFile);
		
		lvAudioAppointmentCurrentDay.setAdapter(adapter);
		lvAudioAppointmentCurrentDay.setOnItemClickListener(this);
		player = new MediaPlayer();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String nameFile = (String) lvAudioAppointmentCurrentDay.getItemAtPosition(arg2);
		 player.reset();
         if(!player.isPlaying()){
                 File audioFile = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ECAudioAppointment/"+nameFile);
                 ECLogger.fine("AppointmentAudioDisplaying:: Listening of the appointment called :"+audioFile.getName() );
                 try {
					FileInputStream streamFromAudioFile = new FileInputStream(audioFile);
					player.setDataSource(streamFromAudioFile.getFD());
					player.prepare();
					player.start();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                       


        
         } else Log.i(TAG, "onItemClick : player already playing");

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(fh !=null) fh.close();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(fh != null) fh.close();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(fh != null) fh.close();
	}
	
	

}
