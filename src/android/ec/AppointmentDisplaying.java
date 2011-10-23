package android.ec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;





/**
 * @author Jeremy Dagorn
 * @version 1.0
 * @date September 2011
 *
 */
public class AppointmentDisplaying extends View{
	public static final String TAG = AppointmentDisplaying.class.getName();
	private Paint   mPaint;
	private String date;
	private ArrayList<String>picturesToDisplayed;
	final static Logger ECLogger = Logger.getLogger(GeneratorCalendar.class.getName());
	static FileHandler fh = null;
	final static boolean append = true;
	private String fileLogging = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/actions.log";

	public AppointmentDisplaying(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(9);
		picturesToDisplayed = getExistingAppointment(this.getDate());
		try {
			fh = new FileHandler(fileLogging, append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
		ECLogger.fine("AppointmentDisplaying:: Consultation of appointments for the date "+getDate());
		Log.i(TAG," getDate -> "+this.getDate());
		Log.d(TAG, " Verification audio appointment "+String.valueOf(AudioRecordingForSelectedDayExists(this.getDate())));


	}

	public AppointmentDisplaying(Context con,AttributeSet atts)
	{
		super(con,atts);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(9);
		
		picturesToDisplayed = getExistingAppointment(this.getDate());
		try {
			fh = new FileHandler(fileLogging, append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
		ECLogger.fine("AppointmentDisplaying:: Consultation of appointments for the date "+getDate());
		fh.close();
		Log.i(TAG," getDate -> "+this.getDate());
		invalidate();





	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i(TAG, "onSizeChanged -> getDate ->"+this.getDate());
		picturesToDisplayed = getExistingAppointment(this.getDate());


	}


	@Override
	public void onDraw(Canvas canvas) {
		int heightToDisplay = 0;
		canvas.drawColor(Color.WHITE);
		Log.i(TAG, "onDraw - size picturesToDisplayed "+ String.valueOf(picturesToDisplayed.size()));
		// We get the list containing pictures names to display, and display the first one.
		if(picturesToDisplayed.size()>0){
			Bitmap firstAppointmentBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/"+picturesToDisplayed.get(0));
			firstAppointmentBitmap = Bitmap.createScaledBitmap(firstAppointmentBitmap, getWidth() , 100, false);
			heightToDisplay+=100;
			canvas.drawBitmap(firstAppointmentBitmap, 0,0, mPaint);
			// We remove the first one, who is now displayed on the screen
			picturesToDisplayed.remove(0);
			// If there is some others pictures
			if(picturesToDisplayed.size()>0){
				// We display them, with the height of 100 px, until there is nothing to display
				for(int i =0; i< picturesToDisplayed.size(); i++){
					Bitmap pictureNextAppointment = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/"+picturesToDisplayed.get(i));
					pictureNextAppointment = Bitmap.createScaledBitmap(pictureNextAppointment, getWidth(),100 , false);
					canvas.drawBitmap(pictureNextAppointment, 0,heightToDisplay, mPaint);
					heightToDisplay+=100;
				}
			}
		}
	} 

	/**
	 * 
	 * @param dateAppointment
	 * @return a list containing appointment pictures to display for the dateAppointment in parameter
	 */
	public ArrayList<String> getExistingAppointment(String dateAppointment){
		ArrayList<String>listAppointments = new ArrayList<String>();
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/");
		if (folderAppointment.exists()){
			String[] contentFolder = folderAppointment.list();
			if(contentFolder.length > 0){
				String expToFind = "date_"+dateAppointment;
				for(String file : contentFolder){
					Log.d(TAG, "checkExistingAppoitment nameFile  : "+file);
					if(file.contains(expToFind)){
						listAppointments.add(file);
						Log.i(TAG, "checkExistingAppointment "+file+" added to the list");
					}
				}
				return listAppointments;
			}else Log.w(TAG, "checkExistingAppointment : ElectronicCalendar folder empty");
		}else Log.w(TAG, "checkExistingAppointment : ElectronicCalendar not existing" );
		return null;
	}



	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}


	public boolean AudioRecordingForSelectedDayExists(String dateAppointment){
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ECAudioAppointment/");
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
			}else Log.w(TAG, "checkExistingAppointment : ElectronicCalendar/ECAudioAppointment folder empty");
		}else Log.w(TAG, "checkExistingAppointment : ElectronicCalendar/ECAudioAppointment/ not exist" );
		return false;
	}





} 
