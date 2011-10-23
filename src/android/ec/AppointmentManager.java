package android.ec;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;




/**
 * @author Jeremy Dagorn
 * @version 1.0
 * @date September 2011
 * 
 * This class displays appointments for the chosen days if there is some, at the launching of the application.
 * It could also displays a canvas where the user could write his appointments and stored them on the sd card
 * A picture is taken each time you create a new appointment
 *
 */
public class AppointmentManager extends Activity implements OnClickListener
{
	public static final String TAG = AppointmentManager.class.getName();
	AppointmentCreation drawview;
	static Camera mCamera;
	SurfaceView mSurfaceView;
	Parameters mCameraParameters;
	SurfaceHolder mSurfaceHolder;
	Button btAppointment;
	Button btListenAudioAppointment;
	String dateAppointment, sNumericMonth, currentMonth, currentDay;
	ArrayList<String> todaysAppointment, listOfAudioAppointmentsToPlay;
	PictureCallback mCall;
	AppointmentDisplaying viewdayAppointment;
	int numericMonth;
	boolean wantToCreateNewAppointment;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas_view);
		Intent currentIntent = this.getIntent();
		if(currentIntent.hasExtra("currentMonth") && currentIntent.hasExtra("currentDay")){
			currentMonth = currentIntent.getStringExtra("currentMonth");
			currentDay = currentIntent.getStringExtra("currentDay");
			this.setTitle(currentDay +" of "+currentMonth);
		}

		if(this.getIntent().hasExtra("numericMonth")) {
			numericMonth = this.getIntent().getIntExtra("numericMonth", 13);
			if(numericMonth != 13) {
				sNumericMonth = String.valueOf(numericMonth);
				dateAppointment = currentDay+"_"+sNumericMonth+"_";

			}
		}

		if(this.getIntent().hasExtra("wantToCreateNewAppointment")){
			wantToCreateNewAppointment = this.getIntent().getBooleanExtra("wantToCreateNewAppointment", false);
		}
		Log.i(TAG,"onCreate - existing appointment for selected day -> "+String.valueOf(hasAppointmentForSelectedDate(dateAppointment)));



		drawview = (AppointmentCreation) findViewById(R.id.DrawViewId);
		viewdayAppointment = (AppointmentDisplaying) findViewById(R.id.DrawDayAppointmentId);
		btAppointment = (Button) findViewById(R.id.bt_Appointment);
		btListenAudioAppointment = (Button) findViewById(R.id.bt_ListenAudioAppointment);
		mSurfaceView = (SurfaceView)findViewById(R.id.sfv_canvasview_snapshot);
		mSurfaceHolder = mSurfaceView.getHolder();
		if ( mSurfaceHolder == null)Log.i(TAG, "getHolder null");
		else Log.i(TAG, "getHolder doesn't return null");

		// If there is an appointment we display it, and modify the button action to create a new appointment 
		// (call to an instance of DrawView) 

		if(hasAudioAppointmentForSelectedDate(dateAppointment)){
			btListenAudioAppointment.setVisibility(0);
			listOfAudioAppointmentsToPlay = getNameFilesAudioAppointmentForSelectedDate(dateAppointment);
			btListenAudioAppointment.setOnClickListener(this);
		}
		if(hasAppointmentForSelectedDate(dateAppointment) && !wantToCreateNewAppointment) {
			viewdayAppointment.setDate(dateAppointment);
			drawview.setVisibility(8);
			btAppointment.setText("Create new appointment");
			Log.i(TAG, "OnCreate -> DrawViewAppointment displayed");
		}else{
			viewdayAppointment.setVisibility(8);
			Log.i(TAG, "OnCreate -> DrawView displayed");
		}

		btAppointment.setOnClickListener(this);


	}

	public void onClick(View v)
	{

		switch (v.getId()) {
		case R.id.bt_Appointment:
			if(dateAppointment != null){
				if(drawview.isShown()){
					drawview.toJPEGFile(dateAppointment);
					Log.i(TAG, "number of cameras "+String.valueOf(Camera.getNumberOfCameras()));
					/** Handling of the camera,**/
					if (mCamera != null) {
						mCamera.release();
						mCamera = null;
					}
					mCamera = getCameraFront();
					if(mCamera != null){
						mCamera.startPreview();
						// Creation of a new picture callback, actions to do when the phone takes a picture
						mCall = new PictureCallback() {

							@Override
							public void onPictureTaken(byte[] data, Camera camera) {
								// TODO Auto-generated method stub
								final String DATE_FORMAT_NOW = "yyyy-MM-dd";
								final String TIME_FORMAT_NOW = "HH-mm-ss";
								SimpleDateFormat sdfDateNow = new SimpleDateFormat(DATE_FORMAT_NOW);
								SimpleDateFormat sdfTimeNow = new SimpleDateFormat(TIME_FORMAT_NOW);
								String dateNow = sdfDateNow.format(Calendar.getInstance().getTime());
								String timeNow = sdfTimeNow.format(Calendar.getInstance().getTime());

								File folderPicturesFaces = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ecSnapshots/");
								if (!folderPicturesFaces.exists()) folderPicturesFaces.mkdirs();
								File picture = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ecSnapshots/"+dateNow+"_"+timeNow);
								FileOutputStream out;
								try {
									out = new FileOutputStream(picture);

									Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length); 
									if(out==null) Log.i(TAG, "onPictureTaken : out is null");
									if(data == null) Log.i(TAG, "data is null");
									Log.i(TAG, data.toString());
									if(data.length == 0) Log.i(TAG, "data is empty");
									bmp.compress(CompressFormat.JPEG, 50, out);
									out.flush();
									out.close();
								}catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}
						};
					}

					// We take the picture using the pictureCallBack (others are null -> because we don't need them)
					mCamera.takePicture(null, null, mCall);
					// CAUTION : don't release the camera here -> crash of the application when you want to take a new picture
					this.finish();
				}else {
					viewdayAppointment.setVisibility(8);
					drawview.setVisibility(0);
					btAppointment.setText("Create");
					Intent choiceMethodIntent = new Intent(this, AppointmentTakingChoice.class);
					choiceMethodIntent.putExtra("currentDay", currentDay);
					choiceMethodIntent.putExtra("currentMonth", currentMonth);
					choiceMethodIntent.putExtra("numericMonth", numericMonth);
					startActivity(choiceMethodIntent);

				}
			}
			else Log.i(TAG, "onClick :  dateAppointment is null - no picture created");
			break;
		case R.id.bt_ListenAudioAppointment:
			Intent playerAudioAppointment = new Intent(this, AppointmentAudioDisplaying.class);
			playerAudioAppointment.putStringArrayListExtra("listOfAudioAppointments", listOfAudioAppointmentsToPlay);
			startActivity(playerAudioAppointment);
			break;
		}
	}


	/**
	 * 
	 * @param dateAppointment
	 * @return true if there is an appointment for the given date 
	 */
	public boolean hasAppointmentForSelectedDate(String dateAppointment){
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/");
		if (folderAppointment.exists()){
			String[] contentFolder = folderAppointment.list();
			if(contentFolder.length > 0){
				String expToFind = "date_"+dateAppointment;
				for(String file : contentFolder){
					Log.d(TAG, "checkExistingAppoitment nameFile  : "+file);
					if(file.contains(expToFind)){
						Log.i(TAG, "checkExistingAppointment -> true");
						return true;
					}
				}
			}else Log.w(TAG, "checkExistingAppointment : ElectronicCalendar folder empty");
		}else Log.w(TAG, "checkExistingAppointment : ElectronicCalendar not existing" );
		return false;
	}

	public boolean hasAudioAppointmentForSelectedDate(String dateAppointment){
		Log.i(TAG, "date to check : "+dateAppointment);
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ECAudioAppointment/");
		if (folderAppointment.exists()){
			String[] contentFolder = folderAppointment.list();
			if(contentFolder.length > 0){
				String expToFind = "date_"+dateAppointment;
				for(String file : contentFolder){
					Log.i(TAG, "hasAudioAppointmentForSelectedDate nameFile  : "+file);
					if(file.contains(expToFind)){
						Log.i(TAG, "hasAudioAppointmentForSelectedDate -> true");
						return true;
					}
				}
			}else Log.w(TAG, "hasAudioAppointmentForSelectedDate : ElectronicCalendar/ECAudioAppointment folder empty");
		}else Log.w(TAG, "hasAudioAppointmentForSelectedDate : ElectronicCalendar/ECAudioAppointment not existing" );
		return false;
	}

	public ArrayList<String> getNameFilesAudioAppointmentForSelectedDate(String dateAppointment){
		ArrayList<String >listOfAudioAppointmentFile= new ArrayList<String>();
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ECAudioAppointment/");
		if (folderAppointment.exists()){
			String[] contentFolder = folderAppointment.list();
			if(contentFolder.length > 0){
				String expToFind = "date_"+dateAppointment;
				for(String file : contentFolder){
					Log.i(TAG, "hasAudioAppointmentForSelectedDate nameFile  : "+file);
					if(file.contains(expToFind)){
						Log.i(TAG, "hasAudioAppointmentForSelectedDate -> "+file+" added to the list");
						listOfAudioAppointmentFile.add(file);
					}
				}
			}else Log.w(TAG, "hasAudioAppointmentForSelectedDate : ElectronicCalendar/ECAudioAppointment folder empty");
		}else Log.w(TAG, "hasAudioAppointmentForSelectedDate : ElectronicCalendar/ECAudioAppointment not existing" );
		return listOfAudioAppointmentFile;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	private Camera getCameraFront(){
		Camera cam = null;
		int numberCamera = Camera.getNumberOfCameras();
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		for ( int indCurrentCam = 0; indCurrentCam < numberCamera; indCurrentCam++ ) {
			Camera.getCameraInfo( indCurrentCam, cameraInfo );
			if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
				try {
					cam = Camera.open( indCurrentCam );
				} catch (RuntimeException e) {
					Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		return cam;
	}


} 
