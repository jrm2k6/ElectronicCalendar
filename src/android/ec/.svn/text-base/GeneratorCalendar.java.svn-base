package android.ec;

import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Jeremy Dagorn
 * @version 1.0
 * @date September 2011
 */

public class GeneratorCalendar extends Activity implements OnClickListener  {
	private final static Logger ECLogger = Logger.getLogger(GeneratorCalendar.class.getName());
	public final static boolean append = true;
	public static FileHandler fh = null;
	public static int NB_HOURS_ON_THIS_PROJECT = 1; // NEED TO BE INCREASED
	GridView calendar;
	TextView tvCurrentMonth;
	RelativeLayout rlNavigationMonths;
	LinearLayout llDays;
	TextView day1,day2,day3,day4,day5,day6,day7;
	Button btNext,btPrevious;
	ArrayList<Integer>listDays;
	GridCellAdapter adapter;
	GregorianCalendar gcalendar;
	int valueMonth, currentMonth, interval=0;
	static final String TAG = GeneratorCalendar.class.getSimpleName();
	static final String fileLogging = Environment.getExternalStorageDirectory()+"/ElectronicCalendar/actions.log";;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			fh = new FileHandler(fileLogging, append);
			fh.setFormatter(new SimpleFormatter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ECLogger.setLevel(Level.ALL);
		ECLogger.addHandler(fh);
		ECLogger.fine("ElectronicCalendar launched");
		setContentView(R.layout.main);
		// Display metrics is useful to get differents size information about the screen
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		calendar = (GridView) findViewById(R.id.gv_calendar);
		rlNavigationMonths = (RelativeLayout) findViewById(R.id.rlRowButtonChoiceMonth);
		// the textview for displaying the current month is twice larger than buttons to navigate
		int widthRl = metrics.widthPixels/4;
		tvCurrentMonth = (TextView)findViewById(R.id.tv_current_month);
		tvCurrentMonth.setWidth(2*widthRl);
		btNext = (Button)findViewById(R.id.bt_next_month);
		btNext.setWidth(widthRl);
		btNext.setId(2);
		btPrevious = (Button)findViewById(R.id.bt_previous_month);
		btPrevious.setWidth(widthRl);
		btPrevious.setId(1);
		gcalendar = new GregorianCalendar();
		currentMonth = gcalendar.get(GregorianCalendar.MONTH);
		tvCurrentMonth.setText(getMonthForInt(currentMonth).toUpperCase());
		listDays = listOfDaysForMonth(gcalendar.get(GregorianCalendar.MONTH));
		valueMonth = currentMonth;
		Log.i(TAG+"OnCreate value month", String.valueOf(valueMonth) );
		llDays = (LinearLayout) findViewById(R.id.llRowDay);

		// We divide the screen by 7, to write days names.
		int width_ll = metrics.widthPixels/7;
		day1 = (TextView) findViewById(R.id.tv_day_1);
		day2 = (TextView) findViewById(R.id.tv_day_2);
		day3 = (TextView) findViewById(R.id.tv_day_3);
		day4 = (TextView) findViewById(R.id.tv_day_4);
		day5 = (TextView) findViewById(R.id.tv_day_5);
		day6 = (TextView) findViewById(R.id.tv_day_6);
		day7 = (TextView) findViewById(R.id.tv_day_7);

		day1.setWidth(width_ll);
		day2.setWidth(width_ll);
		day3.setWidth(width_ll);
		day4.setWidth(width_ll);
		day5.setWidth(width_ll);
		day6.setWidth(width_ll);
		day7.setWidth(width_ll);

		adapter = new GridCellAdapter(this,listDays);
		adapter.notifyDataSetChanged();
		calendar.setAdapter(adapter);
		btNext.setOnClickListener(this);
		btPrevious.setOnClickListener(this);
		

		if(hasAppointmentForCurrentDay()){
			ECLogger.fine("Display of appointment for currentDay:: day "+currentMonth+" month "+currentMonth);
			// If there is an appointment for the current day at the application launching, we display current days appointments by calling the DrawActivity intent
			Intent i = new Intent(this,AppointmentManager.class);
			i.putExtra("currentMonth", getMonthForInt((gcalendar.get(GregorianCalendar.MONTH))));
			i.putExtra("currentDay", String.valueOf(gcalendar.get(GregorianCalendar.DAY_OF_MONTH)));
			i.putExtra("numericMonth",gcalendar.get(GregorianCalendar.MONTH)+1 );
			startActivity(i);
		}

		

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG,"GeneratorCalender onPause is called");
		fh.close();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "GeneratorCalendar onResume is called");
		setGridCellAdapterWhenNewAppointment();
		try {
			if(fh==null){
				
			
			fh = new FileHandler(fileLogging,append);
			fh.setFormatter(new SimpleFormatter());
			ECLogger.addHandler(fh);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			Log.i(TAG," GeneratorCalendar has focus");
			Log.i(TAG,"Value month of the view "+ String.valueOf(valueMonth));
			setGridCellAdapterToSelectedMonth(listOfDaysForMonth(valueMonth));
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "GeneratorCalendar onStop is called");
		if(fh != null) fh.close();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG,"GeneratorCalendar onDestroy is called");
		if(fh != null) fh.close();
	}
	
	
	
	public void setGridCellAdapterToSelectedMonth(ArrayList<Integer>listOfDaysMonth){
		
		adapter = new GridCellAdapter(getApplicationContext(), listOfDaysMonth);
		adapter.notifyDataSetChanged();
		calendar.setAdapter(adapter);
	}
	
	
	public void setGridCellAdapterWhenNewAppointment(){
		//adapter.notifyDataSetInvalidated();
		
	}



	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
		private static final String tag = "GridCellAdapter";
		private Context context;
		private ArrayList<Integer> days;
		public String currentDate;
		public Button bt;
		public GregorianCalendar gregorianCalendarInstance = new GregorianCalendar();

		// Days in Current Month
		public GridCellAdapter(Context context,ArrayList<Integer> listDays)
		{
			super();
			this.context = context;
			this.days = listDays;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent newIntentToLaunch = null;
			String currentDay = String.valueOf(v.getId()-getInterval()+1);
			String extraCurrentMonth = String.valueOf(tvCurrentMonth.getText());
			int numericMonth = valueMonth+1;
			ECLogger.fine("Click on the day "+currentDay+" of  month "+numericMonth);
			if(v.getId() >= getInterval()){
				if(hasAudioAppointmentRecordedForSelectedDate(currentDay) || hasAppointmentForSelectedDate(currentDay)) 
				{
					newIntentToLaunch= new Intent(this.context, AppointmentManager.class);
					newIntentToLaunch.putExtra("currentDay",currentDay);
					newIntentToLaunch.putExtra("currentMonth", extraCurrentMonth);
					newIntentToLaunch.putExtra("numericMonth", numericMonth);
				} else 
				{
					newIntentToLaunch = new Intent(this.context, AppointmentTakingChoice.class);
					newIntentToLaunch.putExtra("currentDay", currentDay);
					newIntentToLaunch.putExtra("currentMonth", extraCurrentMonth);
					newIntentToLaunch.putExtra("numericMonth", numericMonth);
				}
				startActivity(newIntentToLaunch);
			}





		}

		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return days.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return days.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}


		/** Method to know if a day is a sunday or not **/
		public boolean dayIsASunday(ArrayList<Integer> sundaysList, int day){
			return (sundaysList.contains(day) && day > getInterval());
		}

		public boolean dayIsToday(int day){
			return (day == (gregorianCalendarInstance.get(GregorianCalendar.DAY_OF_MONTH)+getInterval()-1) && valueMonth == currentMonth);
		}

		public int hasToDisplayPictureAppointment(String currentDate){
			
			int value;
			value = (hasAppointmentForSelectedDate(currentDate)) ? 1 : 0;
			return value;

		}

		public int hasToDisplayPictureAudioAppointment(String currentDate){
			int value;
			value = hasAudioAppointmentRecordedForSelectedDate(currentDate) ? 2 : 0;
			return value;
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			// It is important to know where are the sundays on the grid 
			// CAUTION : impossible to just get the first column of the gridcell in android -> explain why we're using this arrayList
			ArrayList<Integer> positionSundays = new ArrayList<Integer>();
			positionSundays.add(0);
			positionSundays.add(7);
			positionSundays.add(14);
			positionSundays.add(21);
			positionSundays.add(28);
			positionSundays.add(35);
			View v;
			if(convertView==null){

				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.cell_calendar, null);
				bt = (Button)v.findViewById(R.id.bt_daycell);
				bt.setText(String.valueOf(days.get(position)));
				bt.setId(position);

				currentDate = new String(String.valueOf(days.get(position)));
				bt.setOnClickListener(this);
				// If the day is a sunday 
				if(bt.getId() < getInterval()){
					bt.setBackgroundResource(R.drawable.grey_button);
					bt.setTextColor(Color.WHITE);

				}
				else{
					int backgroundDeterminationValue = hasToDisplayPictureAppointment(currentDate)+hasToDisplayPictureAudioAppointment(currentDate);
					if(dayIsASunday(positionSundays, position) && position > getInterval()){
						switch (backgroundDeterminationValue) {
						case 0:
							bt.setBackgroundResource(R.drawable.blue_button);
							break;
						case 1:
							bt.setBackgroundResource(R.drawable.blue_button_writing_appointment);
							break;
						case 2:
							bt.setBackgroundResource(R.drawable.blue_button_audio_appointment);
							break;
						case 3:
							bt.setBackgroundResource(R.drawable.blue_button_audio_writing_appointment);
							break;
						default:
							break;
						}
					}else {

						if(dayIsToday(position)){
							switch (backgroundDeterminationValue) {
							case 0:
								bt.setBackgroundResource(R.drawable.red_button);
								break;
							case 1:
								bt.setBackgroundResource(R.drawable.red_button_writing_appointment);
								break;
							case 2:
								bt.setBackgroundResource(R.drawable.red_button_audio_appointment);
								break;
							case 3:
								bt.setBackgroundResource(R.drawable.red_button_audio_writing_appointment);
								break;

							default:
								break;
							}
						} else {
							switch (backgroundDeterminationValue) {
							case 0:
								bt.setBackgroundResource(R.drawable.black_button);
								break;
							case 1:
								bt.setBackgroundResource(R.drawable.black_button_writing_appointment);
								break;
							case 2:
								bt.setBackgroundResource(R.drawable.black_button_audio_appointment);
								break;
							case 3:
								bt.setBackgroundResource(R.drawable.black_button_audio_writing_appointment);
								break;

							default:
								break;
							}
						}
					}
				}
			}
			else
			{
				v = convertView;
			}
			return v;
		}

	}




	/**
	 * @name getWeekday
	 * @param dateWritten 
	 * @return the weekday corresponding to the parameter dateWritten
	 */
	public String getWeekday(String dateWritten){
		Integer[] valueMonths = {0,3,3,6,1,4,6,2,5,0,3,5};
		Integer[] valueMonthsLeap = {6,2,3,6,1,4,6,2,5,0,3,5 };
		String[] remainderForDay = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		String dateToCheck = dateWritten;
		if(dateToCheck != "") {
			if(dateToCheck.contains("/")){
				String[] date = dateToCheck.split("/");
				int day = Integer.valueOf(date[0]);
				int month = Integer.valueOf(date[1]);
				String year = date[2];
				char[] array = new char[2];
				year.getChars(2, 4, array, 0);
				String yearLastDigits = String.valueOf(array);
				boolean isLeap = yearIsLeap(Integer.valueOf(year));	
				Log.i(TAG," getWeekdays -> "+ String.valueOf(isLeap));
				// Operation 
				int fractionPart = 0;
				if(Integer.valueOf(yearLastDigits) > 6) fractionPart = Integer.valueOf(yearLastDigits)/4;
				int valueCurrentMonth;
				if (isLeap)  valueCurrentMonth = valueMonthsLeap[month-1];
				else valueCurrentMonth = valueMonths[month-1];
				Log.i(TAG, " getWeekdays -> Value month is : "+String.valueOf(valueCurrentMonth));
				int sum = 6+valueCurrentMonth+fractionPart+Integer.valueOf(yearLastDigits)+day;
				Log.i(TAG, " getWeekdays -> Value sum is : "+String.valueOf(sum));
				int remainder = sum % 7;
				Log.i(TAG, " getWeekdays -> Value remainder is : "+String.valueOf(remainder));
				String valueWeekDay = remainderForDay[remainder]; 
				Log.i(TAG, "getWeekdays -> Value weekday is : "+valueWeekDay);
				return valueWeekDay;
			}
		}
		return null;
	}


	/**
	 * 
	 * @param year
	 * @return is the year is a leap year or not, as a boolean
	 */
	public boolean yearIsLeap(int year){
		if(year%4 == 0){
			if((year%400 == 0) || (year%100 !=0)){
				return true;
			}
		}
		return false;

	}


	/**
	 * 
	 * @param interval
	 */
	public void setInterval(int interval){
		this.interval = interval;
	}


	/**
	 * 
	 * @return the value of the field interval
	 */
	public int getInterval(){
		return interval;
	}



	/**
	 * 
	 * @param month
	 * @param year
	 * @return a list containing the integer corresponding to the day of the previous beginning the new one
	 */
	public ArrayList<Integer> getTrailingDays(int month, int year){
		int numDay;
		Log.i(TAG+" getTrailingDays values", "month "+String.valueOf(month)+" year "+String.valueOf(year));
		String firstday = getWeekday("01/"+String.valueOf(month)+"/"+String.valueOf(year));
		if(firstday.toLowerCase().equals("sunday")) numDay = 0;
		else if (firstday.toLowerCase().equals("monday")) numDay = 1;
		else if (firstday.toLowerCase().equals("tuesday")) numDay = 2;
		else if (firstday.toLowerCase().equals("wednesday")) numDay = 3;
		else if (firstday.toLowerCase().equals("thursday")) numDay = 4;
		else if (firstday.toLowerCase().equals("friday")) numDay = 5;
		else if (firstday.toLowerCase().equals("saturday")) numDay = 6;
		else numDay = 10;

		if(numDay<10 && numDay > 0){
			ArrayList<Integer> listTrailingDays = new ArrayList<Integer>();
			for(int i=0; i<numDay;i++){
				listTrailingDays.add(31-i);


			}
			Collections.reverse(listTrailingDays);
			setInterval(listTrailingDays.size());
			return listTrailingDays;
		}
		return null;

	}


	/**
	 * 
	 * @param month
	 * @return an integer list corresponding to the days to display in the calendar gridcell
	 */
	public ArrayList<Integer> listOfDaysForMonth(int month){
		int NB_MAX_DAYS;
		Integer[] numberDaysPerMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
		ArrayList<Integer>trailingdays;
		Calendar calendar = new GregorianCalendar();
		Log.i(TAG+ "listOfDaysForMonth ->  valueMonth",String.valueOf(valueMonth));
		Log.i(TAG+ "listOfDaysForMonth  ->  int month",String.valueOf(month));
		if(month<=11) {
			trailingdays = getTrailingDays(month+1,calendar.get(Calendar.YEAR));
			if(month==1 && yearIsLeap(calendar.get(Calendar.YEAR))) NB_MAX_DAYS = 29;
			else NB_MAX_DAYS = numberDaysPerMonth[month];
			Log.i(TAG, "listOfDaysForMonth -> Month "+String.valueOf(month)+ " nb_days "+String.valueOf(NB_MAX_DAYS));
			for(int i=1; i<NB_MAX_DAYS+1; i++) trailingdays.add(i);
			return trailingdays;
		}
		return null;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// To get previous month calendar
		case 1: 
			// We could only go 2 months back 
			if (valueMonth>currentMonth-1){
				if (!btNext.isEnabled()) btNext.setEnabled(true);
				valueMonth--;
				ArrayList<Integer>newList = listOfDaysForMonth(valueMonth);
				Log.d(TAG, "onClick method:: "+String.valueOf(valueMonth));
				// Regeneration of the grid thanks to the adapter
				setGridCellAdapterToSelectedMonth(newList);
				tvCurrentMonth.setText(getMonthForInt(valueMonth).toUpperCase());
			} else btPrevious.setEnabled(false);

			break;

		case 2 : 
			// To get next month calendar
			valueMonth++;
			if(valueMonth == 11) btNext.setEnabled(false);
			if(!btPrevious.isEnabled()) btPrevious.setEnabled(true);
			ArrayList<Integer>newList2 = listOfDaysForMonth(valueMonth);
			setGridCellAdapterToSelectedMonth(newList2);
			tvCurrentMonth.setText(getMonthForInt(valueMonth).toUpperCase());

		default:
			break;
		}
	}



	/**
	 * 
	 * @param m
	 * @return the String value of the month integer given in parameter
	 */
	public String getMonthForInt(int m) {
		String month = "invalid";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (m >= 0 && m <= 11 ) {
			month = months[m];
		}
		return month;
	}


	/**
	 * 
	 * @return true if there is an appointment for today
	 */
	public boolean hasAppointmentForCurrentDay(){
		String dateOfToday = String.valueOf(gcalendar.get(GregorianCalendar.DAY_OF_MONTH))+"_"+String.valueOf(gcalendar.get(GregorianCalendar.MONTH)+1);
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/");
		// We check if the application folder exists
		if(folderAppointment.exists()){
			// We check if there is some pictures in the folder which match the today's date
			String[] contentFolder = folderAppointment.list();
			for(String file : contentFolder){
				if(file.contains("date_"+dateOfToday)) return true;
			}
		}
		return false;
	}


	/**
	 * 
	 * @param day
	 * @return true if there is an appointment for the given day
	 */
	public boolean hasAppointmentForSelectedDate(String day){
		String selectedMonth;
		String selectedDate;

		selectedMonth = String.valueOf(valueMonth+1);
		selectedDate = day+"_"+selectedMonth;

		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/");
		// We check if the application folder exists
		if(folderAppointment.exists()){
			// We check if there is a file matching the date given in parameter, meaning there is an appointment planned for this day
			String[] contentFolder = folderAppointment.list();
			for (String file : contentFolder){
				if(file.contains("date_"+selectedDate)){
					return true;
				}
			}
		}
		return false;
	}


	public boolean hasAudioAppointmentRecordedForSelectedDate(String day){
		String selectedMonth;
		String selectedDate;

		selectedMonth = String.valueOf(valueMonth+1);
		selectedDate = day+"_"+selectedMonth;

		Log.i(TAG, "hasApppointmentForSelectedDate -> "+selectedDate);
		File folderAppointment = new File(Environment.getExternalStorageDirectory()+"/ElectronicCalendar/ECAudioAppointment/");
		// We check if the application folder exists
		if(folderAppointment.exists()){
			// We check if there is a file matching the date given in parameter, meaning there is an appointment planned for this day
			String[] contentFolder = folderAppointment.list();
			for (String file : contentFolder){
				if(file.contains("date_"+selectedDate)){
					return true;
				}
			}
		}
		return false;

	}


}
