package android.ec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



/**
 * @author Jeremy Dagorn
 * @version 1.0
 * @date September 2011
 * 
 * This class manages the drawing/writing of appointments by the user on the screen, stored these appointments
 * as pictures on the sdcard, with the date of the appointment, and the date the picture is created as the picture's name
 *
 */
public class AppointmentCreation extends View{
	public static final String TAG = AppointmentCreation.class.getName();
	private Bitmap  mBitmap;
	private Canvas  mCanvas;
	private Path    mPath;
	private Paint   mPaint;
	private boolean existing = false;
	final static Logger ECLogger = Logger.getLogger(GeneratorCalendar.class.getName());
	static FileHandler fh = null;
	final static boolean append = true;
	private String fileLogging = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/actions.log";
	

	public AppointmentCreation(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(9);
		mPath = new Path();
		try {
			fh = new FileHandler(fileLogging, append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
	}
	public AppointmentCreation(Context con,AttributeSet atts)
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
		mPath = new Path();
		try {
			fh = new FileHandler(fileLogging, append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		ECLogger.addHandler(fh);
		
		
		

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h , Bitmap.Config.ALPHA_8);
		mCanvas = new Canvas(mBitmap);
		
	}


	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		canvas.drawPath(mPath, mPaint);
		invalidate(); 
	} 
	
	
	/**
	 * 
	 * @param dateAppointment
	 * @return a list containing all the appointments for the given date (filenames precisely) stored on the sdcard
	 */
	public ArrayList<String> checkExistingAppointment(String dateAppointment){
		ArrayList<String>listAppointments = new ArrayList<String>();
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/");
		Log.i(TAG, "checkExisting - getTag "+dateAppointment);
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

	
	
	/******
	 * 
	 * 
	 * 
	 * Managing of the drawing on canvas
	 * 
	 * 
	 */
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}
	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
		}
	}
	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	
	
	/**
	 * 
	 * @param dateAppointment
	 * Method to transform the writing to a JPEG file
	 */
	public void toJPEGFile(String dateAppointment){
		final String DATE_FORMAT_NOW = "yyyy-MM-dd";
		final String TIME_FORMAT_NOW = "HH-mm-ss";
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdfDateNow = new SimpleDateFormat(DATE_FORMAT_NOW);
		SimpleDateFormat sdfTimeNow = new SimpleDateFormat(TIME_FORMAT_NOW);
		String dateNow = sdfDateNow.format(cal.getTime());
		String timeNow = sdfTimeNow.format(cal.getTime());
		Log.i(TAG, "dateNow : "+dateNow);
		Log.i(TAG,"timeNow : "+timeNow);
		String nameFile = "date_"+dateAppointment+"acreated_"+dateNow+"_"+timeNow;
		Log.i(TAG, nameFile);
		ECLogger.fine("AppointmentCreationHandWriting:: Appointment creation for the day"+dateAppointment);
		fh.close();
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/");
		if(!folderAppointment.exists()) folderAppointment.mkdirs();
		
		

		try {
			this.setDrawingCacheEnabled(true);
			FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/"+nameFile));
			Bitmap bitmap = this.getDrawingCache();
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;


		}
		return true;
	}
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	public Canvas getmCanvas() {
		return mCanvas;
	}
	public void setmCanvas(Canvas mCanvas) {
		this.mCanvas = mCanvas;
	}
	public Path getmPath() {
		return mPath;
	}
	public void setmPath(Path mPath) {
		this.mPath = mPath;
	}
	public Paint getmPaint() {
		return mPaint;
	}
	public void setmPaint(Paint mPaint) {
		this.mPaint = mPaint;
	}

	public boolean getExisting(){
		return existing;
	}

	

} 
