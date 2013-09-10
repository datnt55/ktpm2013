package datNT.example.quanlythuchi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.Toast;
import datNT.example.quanlythuchi.inits.BalanceObject;
import datNT.example.quanlythuchi.inits.DBAdapter;
import datNT.example.quanlythuchi.inits.MyInits;
import datNT.example.quanlythuchi.inits.NoteDetail;
import datNT.example.quanlythuchi.inits.PaymentObject;
import datNT.example.quanlythuchi.inits.PaymentVayObject;
import datNT.example.quanlythuchi.views.MyArrayAdapter;
import datNT.example.quanlythuchi.views.MyArrayAdapterBalance;
import datNT.example.quanlythuchi.views.MyArrayAdapterCustomNoteDetail;

public class IndexActivity extends Activity implements OnClickListener {
	private LinearLayout layoutBalance;
	private ImageButton buttonBackDate, buttonNextDate, buttonShowHidden,
			buttonNewNote, buttonFilter, buttonSetting;
	private ImageButton buttonBackMonth, buttonNextMonth, btnnextBalance,
			btnbackBalance;
	private ImageButton buttonBackYear, buttonNextYear;
	private Button buttonShowDate, buttonShowMonth, buttonShowYear;
	private ListView listViewShowInfor, listViewNoteMonth, listViewNoteYear,
			listviewBalance;
	private Calendar calendar;
	private DBAdapter database;
	private String dateRequest, dateModify,
			contactname;
	private int month, year, date, dateOfWeek, count = 0, count_income = 0, position, countmonth = 0, countyear = 0,
			countincome_month = 0, countincome_year = 0, positionincome,
			positionvay = 0;
	private TabHost tab;
	private MyArrayAdapter adapter_lv, adapter_lvmonth, adapter_lvyear;
	private PopupMenu popup;
	private ArrayList<PaymentObject> array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_main);
		layoutBalance = (LinearLayout) findViewById(R.id.layoutbalance);
		database = new DBAdapter(this);
		getBundle();
		getBundleResult();
		loadTabs();
		myInitComponents();
		loadListView();
		myInitsTabMonth();
		myInitsTabYear();
		tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				Log.i("tab", tabId);
				if (tabId.equals("t1")) {
					myInitsTabYear();
					showListViewBalanceYear();
					buttonShowYear.setText(year + " ");
					btnnextBalance
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									showBalanceVayNoYear();
								}
							});
					btnbackBalance
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									showListViewBalanceYear();
								}
							});
				}
				if (tabId.equals("t3")) {
					loadListView();
					showBalance();
					buttonShowDate.setText(date + " - "
							+ MyInits.getMonthAsString(month) + " - " + year
							+ "(" + MyInits.getDaysOfWeekAsString(dateOfWeek)
							+ ")");
				}
				if (tabId.equals("t2")) {
					myInitsTabMonth();
					showListViewBalanceMonth();
					buttonShowMonth.setText(MyInits.getMonthAsString(month)
							+ " - " + year);
					btnnextBalance
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									showBalanceVayNoMonth();

								}
							});
					btnbackBalance
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									showListViewBalanceMonth();

								}
							});

				}

			}
		});

	}

	private void getBundle() {
		try {
			Bundle bundle = getIntent().getBundleExtra("ACCOUNT");
			contactname = bundle.getString("NAME");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

	}

	private void getBundleResult() {
		try {
			Bundle bundle1 = getIntent().getBundleExtra(
					MyInits.REQUEST_CODE_CONTACT);
			contactname = bundle1.getString("namecontact");
			Log.i("namecontact", contactname);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	private void myInitComponents() {
		// ---------------------------- Add control in tab
		// 1--------------------------------
		calendar = Calendar.getInstance(Locale.getDefault());
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		date = calendar.get(Calendar.DAY_OF_MONTH);
		dateOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Log.i("dateofweek", dateOfWeek + "");
		dateRequest = date + "/" + month + "/" + year;

		buttonBackYear = (ImageButton) findViewById(R.id.imgbtnbackYear);
		buttonBackYear.setOnClickListener(this);

		buttonNextYear = (ImageButton) findViewById(R.id.imgbtnnextYear);
		buttonNextYear.setOnClickListener(this);

		buttonShowYear = (Button) this.findViewById(R.id.btnshowYear);

		buttonShowYear.setText(year + " ");
		buttonShowYear.setOnClickListener(this);

		// -------------------------- Add control in tab
		// 2----------------------------------

		buttonBackMonth = (ImageButton) findViewById(R.id.imgbtnbackmonth);
		buttonBackMonth.setOnClickListener(this);

		buttonNextMonth = (ImageButton) findViewById(R.id.imgbtnnextmonth);
		buttonNextMonth.setOnClickListener(this);

		buttonShowMonth = (Button) this.findViewById(R.id.btnshowMonth);
		buttonShowMonth.setText(MyInits.getMonthAsString(month) + " - " + year);
		buttonShowMonth.setOnClickListener(this);
		// --------Add control in tab4---------------------------------

		buttonBackDate = (ImageButton) findViewById(R.id.imgbtn_back_day);
		buttonBackDate.setOnClickListener(this);

		buttonNextDate = (ImageButton) findViewById(R.id.imgbtn_next_day);
		buttonNextDate.setOnClickListener(this);

		buttonShowDate = (Button) this.findViewById(R.id.btnshow_Date);
		buttonShowDate.setText(date + " - " + MyInits.getMonthAsString(month)
				+ " - " + year + "( " + dateOfWeek + " )");
		buttonShowDate.setOnClickListener(this);

		// ---------------------------------------------------------------------------------

		buttonShowHidden = (ImageButton) findViewById(R.id.imgshowhidden);
		buttonShowHidden.setOnClickListener(this);

		buttonNewNote = (ImageButton) findViewById(R.id.imgaddnote);
		buttonNewNote.setOnClickListener(this);

		buttonSetting = (ImageButton) findViewById(R.id.imgsetting);
		buttonSetting.setOnClickListener(this);

		buttonFilter = (ImageButton) findViewById(R.id.imgfilter);
		buttonFilter.setOnClickListener(this);
		listViewShowInfor = (ListView) findViewById(R.id.listViewNoteDay);
		listviewBalance = (ListView) findViewById(R.id.listviewBalance);
		LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listViewShowInfor.setLayoutParams(Params1);
		btnnextBalance = (ImageButton) findViewById(R.id.nextbalance);
		btnnextBalance.setOnClickListener(this);
		btnbackBalance = (ImageButton) findViewById(R.id.backbalance);
		btnbackBalance.setOnClickListener(this);
		popup = new PopupMenu(this, buttonSetting);
		popup.inflate(R.menu.popup_menu);
	}

	private void loadTabs() {
		tab = (TabHost) findViewById(android.R.id.tabhost);
		tab.setup();
		TabHost.TabSpec spec;
		spec = tab.newTabSpec("t1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Năm");
		tab.addTab(spec);
		spec = tab.newTabSpec("t2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Tháng");
		tab.addTab(spec);

		spec = tab.newTabSpec("t3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Ngày");
		tab.addTab(spec);
		tab.setCurrentTab(2);
	}

	// -----------------------------------------------------------------------------------------
	@Override
	public void onClick(View arg0) {
		// -------------------------------------------
		if (arg0.getId() == R.id.imgbtn_back_day) {
			getBackDate();
			showBalance();
		}
		if (arg0.getId() == R.id.imgbtn_next_day) {
			getNextDate();
			showBalance();
		}
		// -------------------------------------------
		if (arg0.getId() == R.id.imgbtnbackmonth) {
			getBackMonth();
			showListViewBalanceMonth();
		}
		if (arg0.getId() == R.id.imgbtnnextmonth) {
			getNextMonth();
			showListViewBalanceMonth();
		}
		// -------------------------------------------
		if (arg0.getId() == R.id.imgbtnbackYear) {
			getBackYear();
			showListViewBalanceYear();
		}
		if (arg0.getId() == R.id.imgbtnnextYear) {
			getNextYear();
			showListViewBalanceYear();
		}
		// -------------------------------------------
		if (arg0.getId() == R.id.imgaddnote) {
			Intent intent = new Intent(IndexActivity.this,
					Create_note_Activity.class);
			Bundle bundle = new Bundle();
			bundle.putString("namecontact", contactname);
			intent.putExtra(MyInits.REQUEST_CODE_CONTACT, bundle);
			startActivity(intent);
		}
		if (arg0.getId() == R.id.imgshowhidden) {
			setListViewBalance();
		}
		if (arg0.getId() == R.id.nextbalance) {
			showBalanceVayNo();
		}
		if (arg0.getId() == R.id.backbalance) {
			showBalance();
		}
		if (arg0.getId() == R.id.imgsetting) {
			showPopupMenus();
		}
	}

	// -----------------------------------------------------------------------------------------
	private void getBackDate() {
		if (date <= 1) {
			if (month <= 1) {
				month = 12;
				year--;
				date = MyInits.getDateinMonth(month);
			} else {
				month--;
				date = MyInits.getDateinMonth(month);
			}
		} else {
			date--;
		}
		if (dateOfWeek <= 1) {
			dateOfWeek = 7;
		} else
			dateOfWeek--;
		Log.i("dateofweek", dateOfWeek + "");
		Log.i("dateofweek", MyInits.getDaysOfWeekAsString(dateOfWeek));
		buttonShowDate.setText(date + " - " + MyInits.getMonthAsString(month)
				+ " - " + year + "("
				+ MyInits.getDaysOfWeekAsString(dateOfWeek) + ")");
		dateRequest = date + "/" + month + "/" + year;
		loadListView();
	}

	private void getNextDate() {
		if (date == MyInits.getDateinMonth(month)) {
			if (month > 11) {
				month = 1;
				year++;
				date = 1;
			} else {
				month++;
				date = 1;
			}
		} else {
			date++;
		}
		if (dateOfWeek >= 7) {
			dateOfWeek = 1;
		} else
			dateOfWeek++;
		dateRequest = date + "/" + month + "/" + year;
		buttonShowDate.setText(date + " - " + MyInits.getMonthAsString(month)
				+ " - " + year + "("
				+ MyInits.getDaysOfWeekAsString(dateOfWeek) + ")");
		loadListView();
	}

	private void getNextMonth() {
		if (month > 11) {
			month = 1;
			year++;
		} else {
			month++;
		}
		buttonShowMonth.setText(MyInits.getMonthAsString(month) + " - " + year);
		dateRequest = date + "/" + month + "/" + year;
		myInitsTabMonth();
	}

	private void getBackMonth() {
		if (month <= 1) {
			month = 12;
			year--;
		} else {
			month--;
		}
		buttonShowMonth.setText(MyInits.getMonthAsString(month) + " - " + year);
		dateRequest = date + "/" + month + "/" + year;
		myInitsTabMonth();
	}

	private void getBackYear() {
		year--;
		buttonShowYear.setText(year + " ");
		dateRequest = date + "/" + month + "/" + year;
		myInitsTabYear();
	}

	private void getNextYear() {
		year++;
		buttonShowYear.setText(year + " ");
		dateRequest = date + "/" + month + "/" + year;
		myInitsTabYear();
	}

	// -----------------------------------------------------------------------------------------
	private void loadListView() {
		count = 0;
		count_income = 0;
		database.open();
		String date_db = "";
		array = new ArrayList<PaymentObject>();
		Cursor mcursor = database.getAllPaymentInDate(dateRequest, contactname);
		if (mcursor.moveToFirst()) {
			do {
				String category = mcursor.getString(0).toString();
				String money = mcursor.getString(1).toString();
				date_db = mcursor.getString(2).toString();
				String account = mcursor.getString(3).toString();
				int icon = Integer.parseInt(mcursor.getString(4));
				PaymentObject po = new PaymentObject(money, category, null,
						dateRequest, null, account, null, null, null, null,
						icon);
				array.add(po);
				count++;
			} while (mcursor.moveToNext());
		}
		String date_income_db = "";
		Cursor incomecursor = database.getAllPaymentInDateIncome(dateRequest,
				contactname);
		int counttmp = 0;
		if (incomecursor.moveToFirst()) {
			do {
				String incomecategory = incomecursor.getString(0).toString();
				String incomemoney = incomecursor.getString(1).toString();
				date_income_db = incomecursor.getString(2).toString();
				String incomeaccount = incomecursor.getString(3).toString();
				int incomeicon = Integer.parseInt(incomecursor.getString(4));
				PaymentObject po = new PaymentObject(incomemoney,
						incomecategory, null, dateRequest, null, incomeaccount,
						null, null, null, null, incomeicon);
				array.add(po);
				counttmp++;
			} while (incomecursor.moveToNext());
			Log.i("counttmp", counttmp + "");
		}
		count_income = count + counttmp;
		String date_vay_db = "";
		Cursor vaycursor = database.getAllPaymentInDateVay(dateRequest,
				contactname);
		if (vaycursor.moveToFirst()) {
			do {
				String vayperson = vaycursor.getString(0).toString();
				String vaymoney = vaycursor.getString(1).toString();
				date_vay_db = vaycursor.getString(2).toString();
				String vayaccount = vaycursor.getString(3).toString();
				int vayicon = Integer.parseInt(vaycursor.getString(4));
				String vaycategory = vaycursor.getString(5).toString();
				PaymentObject po = new PaymentObject(vaymoney, vayperson,
						vaycategory, dateRequest, null, vayaccount, null, null,
						null, null, vayicon);
				array.add(po);
			} while (vaycursor.moveToNext());
		}
		database.close();
		adapter_lv = null;
		if (dateRequest.equals(date_db) || (dateRequest.equals(date_income_db))
				|| (dateRequest.equals(date_vay_db))) {
			adapter_lv = new MyArrayAdapter(this,
					R.layout.layout_custom_listview_thu_tieu, array, count,
					count_income);
			listViewShowInfor.setAdapter(adapter_lv);
			showBalance();
			listViewShowInfor
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								final int arg2, long arg3) {
							ArrayList<PaymentObject> arraydetail = new ArrayList<PaymentObject>();
							ArrayList<Integer> arrayId = new ArrayList<Integer>();
							ArrayList<PaymentVayObject> arrayvay = new ArrayList<PaymentVayObject>();
							ArrayList<Integer> arrayvayId = new ArrayList<Integer>();
							ArrayList<PaymentObject> arraydetailincome = new ArrayList<PaymentObject>();
							ArrayList<Integer> arrayIdincome = new ArrayList<Integer>();
							database.open();
							Cursor mcusor = database.getPaymentDetail(
									dateRequest, contactname);
							if (mcusor.moveToFirst()) {
								do {
									String date = mcusor.getString(0)
											.toString();
									dateModify = date;
									String money = mcusor.getString(1)
											.toString();
									String category = mcusor.getString(2)
											.toString();
									int icon = Integer.parseInt(mcusor
											.getString(3).toString());
									String project = mcusor.getString(4)
											.toString();
									String count = mcusor.getString(5)
											.toString();
									String enddate = mcusor.getString(6)
											.toString();
									String note = mcusor.getString(7)
											.toString();
									String account = mcusor.getString(8)
											.toString();
									int id = mcusor.getInt(9);
									arrayId.add(id);
									PaymentObject pm = new PaymentObject(money,
											category, note, date, null,
											account, null, count, enddate,
											project, icon);
									arraydetail.add(pm);
								} while (mcusor.moveToNext());
							}
							Cursor vaycursor = database.getPaymentDetailVay(
									dateRequest, contactname);
							if (vaycursor.moveToFirst()) {
								do {
									String vaycategory = vaycursor.getString(0)
											.toString();
									String vayperson = vaycursor.getString(1)
											.toString();
									String vaystartdate = vaycursor
											.getString(2).toString();
									dateModify = vaystartdate;
									String vayenddate = vaycursor.getString(3)
											.toString();
									int vaycategoryicon = vaycursor.getInt(4);
									String vaylai = vaycursor.getString(5)
											.toString();
									String vaylaitg = vaycursor.getString(6)
											.toString();
									String vaylaitysuat = vaycursor
											.getString(7).toString();
									String vaylaikieu = vaycursor.getString(8)
											.toString();
									String vayaccount = vaycursor.getString(9)
											.toString();
									int vayid = vaycursor.getInt(10);
									arrayvayId.add(vayid);
									String vaynote = vaycursor.getString(11)
											.toString();
									String vaymoney = vaycursor.getString(12)
											.toString();
									PaymentVayObject pmv = new PaymentVayObject(
											vaymoney, vaycategory, vayperson,
											vaystartdate, vayenddate,
											contactname, vayaccount, vaylai,
											vaylaikieu, vaylaitg, vaylaitysuat,
											vaycategoryicon, vaynote);
									arrayvay.add(pmv);
								} while (vaycursor.moveToNext());
							}
							Cursor incomecusor = database
									.getPaymentDetailIncome(dateRequest,
											contactname);
							if (incomecusor.moveToFirst()) {
								do {
									String date = incomecusor.getString(0)
											.toString();
									dateModify = date;
									String money = incomecusor.getString(1)
											.toString();
									String category = incomecusor.getString(2)
											.toString();
									int icon = Integer.parseInt(incomecusor
											.getString(3).toString());
									String project = incomecusor.getString(4)
											.toString();
									String count = incomecusor.getString(5)
											.toString();
									String enddate = incomecusor.getString(6)
											.toString();
									String note = incomecusor.getString(7)
											.toString();
									String account = incomecusor.getString(8)
											.toString();
									int id = incomecusor.getInt(9);
									arrayIdincome.add(id);
									PaymentObject pm = new PaymentObject(money,
											category, note, date, null,
											account, null, count, enddate,
											project, icon);
									arraydetailincome.add(pm);
								} while (incomecusor.moveToNext());
							}

							database.close();
							final Dialog dialog = new Dialog(
									IndexActivity.this,
									android.R.style.Theme_DeviceDefault_Dialog);
							dialog.setContentView(R.layout.dialog_note_detail);
							dialog.setTitle("Chi tiết");
							if (arg2 < count) {
								PaymentObject pmdetail = arraydetail.get(arg2);
								final int id = arrayId.get(arg2);
								position = arg2;
								ImageView imgdetailIcon = (ImageView) dialog
										.findViewById(R.id.notedetailimgview);
								ListView detailLv = (ListView) dialog
										.findViewById(R.id.notedetailLv);
								Button notedetaldel = (Button) dialog
										.findViewById(R.id.notedetaildelete);
								Button notedetalmodifi = (Button) dialog
										.findViewById(R.id.notedetailmodifi);
								Button notedetalcancel = (Button) dialog
										.findViewById(R.id.notedetailcancel);
								ArrayList<NoteDetail> arraydetailLv = new ArrayList<NoteDetail>();
								NoteDetail n1 = new NoteDetail("Ngày", pmdetail
										.getDate());
								NoteDetail n2 = new NoteDetail("Số tiền",
										pmdetail.getMoney());
								NoteDetail n3 = new NoteDetail("Thể loại",
										pmdetail.getCategory());
								NoteDetail n4 = new NoteDetail(
										"Loại Tài khoản", pmdetail
												.getAccount_type());
								NoteDetail n5 = new NoteDetail("Dự án",
										pmdetail.getProject());
								NoteDetail n7 = new NoteDetail("Ghi chú",
										pmdetail.getNote());
								arraydetailLv.add(n1);
								arraydetailLv.add(n2);
								arraydetailLv.add(n3);
								arraydetailLv.add(n4);
								arraydetailLv.add(n5);
								if (pmdetail.getPeriodic_count().equals(
										pmdetail.getPeriodic_enddate())) {
									NoteDetail n6 = new NoteDetail("Lặp",
											pmdetail.getPeriodic_count());
									arraydetailLv.add(n6);
								} else {
									NoteDetail n6 = new NoteDetail(
											"Lặp",
											pmdetail.getPeriodic_count()
													+ " | "
													+ pmdetail
															.getPeriodic_enddate());
									arraydetailLv.add(n6);
								}
								arraydetailLv.add(n7);
								imgdetailIcon.setImageResource(pmdetail
										.getCategory_icon());
								MyArrayAdapterCustomNoteDetail adapterdetail = new MyArrayAdapterCustomNoteDetail(
										IndexActivity.this,
										R.layout.customer_note_detail_layout,
										arraydetailLv);
								detailLv.setAdapter(adapterdetail);
								notedetaldel
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View arg0) {
												database.open();
												database.deletePayment(id);
												database.close();
												array.remove(position);
												count -= 1;
												count_income -= 1;
												Log.i("count", count + "");
												Log.i("countincome",
														count_income + "");
												for (PaymentObject p : array) {
													Log.i("category",
															p.getCategory());
													Log.i("money", p.getMoney());
												}
												Log.i("vi tri", position + "");
												adapter_lv = new MyArrayAdapter(
														IndexActivity.this,
														R.layout.layout_custom_listview_thu_tieu,
														array, count,
														count_income);
												listViewShowInfor
														.setAdapter(null);
												listViewShowInfor
														.setAdapter(adapter_lv);
												showBalance();
												dialog.dismiss();
											}
										});
								notedetalcancel
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View arg0) {
												dialog.dismiss();

											}
										});
								notedetalmodifi
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												gotoModify();

											}
										});
							} else {
								if (arg2 >= count_income) {
									ImageView imgdetailIcon = (ImageView) dialog
											.findViewById(R.id.notedetailimgview);
									ListView detailLv = (ListView) dialog
											.findViewById(R.id.notedetailLv);
									Button notedetaldel = (Button) dialog
											.findViewById(R.id.notedetaildelete);
									Button notedetalmodifi = (Button) dialog
											.findViewById(R.id.notedetailmodifi);
									Button notedetalcancel = (Button) dialog
											.findViewById(R.id.notedetailcancel);

									notedetalcancel.setText("Đã trả");
									ArrayList<NoteDetail> arraydetailVay = new ArrayList<NoteDetail>();
									final PaymentVayObject pmv = arrayvay
											.get(arg2 - count_income);
									final int id = arrayvayId.get(arg2
											- count_income);
									positionvay = arg2;
									NoteDetail n0 = new NoteDetail("Giao dịch",
											pmv.getCategory());
									final NoteDetail n01 = new NoteDetail(
											"Số tiền", pmv.getMoney());
									NoteDetail n1 = new NoteDetail(
											"Người vay/nợ", pmv.getPerson());
									NoteDetail n2 = new NoteDetail(
											"Ngày bắt đầu", pmv.getStartdate());
									NoteDetail n3 = new NoteDetail(
											"Ngày kết thúc", pmv.getEnddate());
									NoteDetail n4 = new NoteDetail(
											"Loại Tài khoản", pmv
													.getAccount_type());
									arraydetailVay.add(n0);
									arraydetailVay.add(n01);
									arraydetailVay.add(n1);
									arraydetailVay.add(n2);
									arraydetailVay.add(n3);
									arraydetailVay.add(n4);
									if (pmv.getLai().equals("Không")) {
										NoteDetail n5 = new NoteDetail(
												"Lãi suất", pmv.getLai());
										arraydetailVay.add(n5);
									} else {
										NoteDetail n5 = new NoteDetail(
												"Lãi suất", pmv.getKieu_lai()
														+ "|"
														+ pmv.getTysuatlai());
										arraydetailVay.add(n5);
										NoteDetail n6 = new NoteDetail(
												"Lãi suất kỳ hạn", pmv
														.getTansuatlai());
										arraydetailVay.add(n6);
									}
									NoteDetail n6 = new NoteDetail("Ghi chú",
											pmv.getNote());
									arraydetailVay.add(n6);
									MyArrayAdapterCustomNoteDetail adapterdetail = new MyArrayAdapterCustomNoteDetail(
											IndexActivity.this,
											R.layout.customer_note_detail_layout,
											arraydetailVay);
									detailLv.setAdapter(adapterdetail);
									notedetaldel
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View arg0) {
													database.open();
													database.deletePaymentVay(id);
													database.close();
													array.remove(positionvay);
													adapter_lv = new MyArrayAdapter(
															IndexActivity.this,
															R.layout.layout_custom_listview_thu_tieu,
															array, count,
															count_income);
													listViewShowInfor
															.setAdapter(null);
													listViewShowInfor
															.setAdapter(adapter_lv);

													showBalance();
													dialog.dismiss();
												}
											});
									notedetalcancel
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View arg0) {
													final Dialog dialog_tra = new Dialog(
															IndexActivity.this,
															android.R.style.Theme_Holo_Dialog);
													dialog_tra
															.setContentView(R.layout.note_add_dialog);
													dialog_tra
															.setTitle("Đã trả");
													final EditText edittext_note_dialog = (EditText) dialog_tra
															.findViewById(R.id.note_edittext_dialog);
													Button btn_Ok_note_dialog = (Button) dialog_tra
															.findViewById(R.id.button_note_ok);
													Button btn_Cancel_note_dialog = (Button) dialog_tra
															.findViewById(R.id.button_note_cancel);
													btn_Ok_note_dialog
															.setOnClickListener(new View.OnClickListener() {

																@Override
																public void onClick(
																		View arg0) {
																	int moneytra = Integer
																			.parseInt(edittext_note_dialog
																					.getText()
																					.toString());
																	int money = Integer
																			.parseInt(pmv
																					.getMoney());
																	pmv.setMoney(money
																			- moneytra
																			+ "");
																	database.open();
																	database.updatePaymentVay(
																			pmv,
																			id);
																	database.close();
																	PaymentObject pmo = array
																			.get(arg2);
																	pmo.setMoney(pmv
																			.getMoney());
																	adapter_lv = new MyArrayAdapter(
																			IndexActivity.this,
																			R.layout.layout_custom_listview_thu_tieu,
																			array,
																			count,
																			count_income);
																	listViewShowInfor
																			.setAdapter(null);
																	listViewShowInfor
																			.setAdapter(adapter_lv);
																	dialog_tra
																			.dismiss();
																}
															});
													btn_Cancel_note_dialog
															.setOnClickListener(new View.OnClickListener() {

																@Override
																public void onClick(
																		View arg0) {
																	dialog_tra
																			.dismiss();
																}
															});
													dialog_tra.show();
													dialog.dismiss();

												}
											});
									notedetalmodifi
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View v) {
													Log.i("datemodifi",
															dateModify);
													gotoModifyVay();

												}
											});

								} else {
									PaymentObject pmdetail = arraydetailincome
											.get(arg2 - count);
									Log.i("Position", arg2 - count + "");
									final int id = arrayIdincome.get(arg2
											- count);
									positionincome = arg2;
									ImageView imgdetailIcon = (ImageView) dialog
											.findViewById(R.id.notedetailimgview);
									ListView detailLv = (ListView) dialog
											.findViewById(R.id.notedetailLv);
									Button notedetaldel = (Button) dialog
											.findViewById(R.id.notedetaildelete);
									Button notedetalmodifi = (Button) dialog
											.findViewById(R.id.notedetailmodifi);
									Button notedetalcancel = (Button) dialog
											.findViewById(R.id.notedetailcancel);
									ArrayList<NoteDetail> arraydetailLv = new ArrayList<NoteDetail>();
									NoteDetail n1 = new NoteDetail("Ngày",
											pmdetail.getDate());
									NoteDetail n2 = new NoteDetail("Số tiền",
											pmdetail.getMoney());
									NoteDetail n3 = new NoteDetail("Thể loại",
											pmdetail.getCategory());
									NoteDetail n4 = new NoteDetail(
											"Loại Tài khoản", pmdetail
													.getAccount_type());
									NoteDetail n5 = new NoteDetail("Dự án",
											pmdetail.getProject());
									NoteDetail n7 = new NoteDetail("Ghi chú",
											pmdetail.getNote());
									arraydetailLv.add(n1);
									arraydetailLv.add(n2);
									arraydetailLv.add(n3);
									arraydetailLv.add(n4);
									arraydetailLv.add(n5);
									if (pmdetail.getPeriodic_count().equals(
											pmdetail.getPeriodic_enddate())) {
										NoteDetail n6 = new NoteDetail("Lặp",
												pmdetail.getPeriodic_count());
										arraydetailLv.add(n6);
									} else {
										NoteDetail n6 = new NoteDetail(
												"Lặp",
												pmdetail.getPeriodic_count()
														+ " | "
														+ pmdetail
																.getPeriodic_enddate());
										arraydetailLv.add(n6);
									}
									arraydetailLv.add(n7);
									imgdetailIcon.setImageResource(pmdetail
											.getCategory_icon());
									MyArrayAdapterCustomNoteDetail adapterdetail = new MyArrayAdapterCustomNoteDetail(
											IndexActivity.this,
											R.layout.customer_note_detail_layout,
											arraydetailLv);
									detailLv.setAdapter(adapterdetail);
									notedetaldel
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View arg0) {
													database.open();
													database.deletePaymentIncome(id);
													database.close();
													array.remove(positionincome);
													count_income -= 1;
													adapter_lv = new MyArrayAdapter(
															IndexActivity.this,
															R.layout.layout_custom_listview_thu_tieu,
															array, count,
															count_income);
													listViewShowInfor
															.setAdapter(null);
													listViewShowInfor
															.setAdapter(adapter_lv);

													showBalance();
													dialog.dismiss();
												}
											});
									notedetalcancel
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View arg0) {
													dialog.dismiss();

												}
											});
									notedetalmodifi
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View v) {
													gotoModifyIncome();

												}
											});
								}
							}
							dialog.show();
						}
					});
		} else {
			String[] array_null = { "Không có dữ liệu" };
			ArrayAdapter<String> adapter_null = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, array_null);
			listViewShowInfor.setAdapter(adapter_null);
			listViewShowInfor
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Toast.makeText(IndexActivity.this,
									"Không có dữ liệu", Toast.LENGTH_SHORT)
									.show();

						}
					});
		}
	}

	private void myInitsTabMonth() {
		listViewNoteMonth = (ListView) findViewById(R.id.listViewNoteMonth);
		ArrayList<PaymentObject> arraymonth = new ArrayList<PaymentObject>();
		countmonth = 0;
		database.open();
		String date_db = "";
		String[] timedb = null;
		String[] timerq = null;
		Cursor mcursor = database.getAllPayment(contactname);
		if (mcursor.moveToFirst()) {
			do {
				String category = mcursor.getString(0).toString();
				String money = mcursor.getString(1).toString();
				date_db = mcursor.getString(2).toString();
				String account = mcursor.getString(3).toString();
				int icon = Integer.parseInt(mcursor.getString(4));
				timedb = date_db.split("/");
				timerq = dateRequest.split("/");

				if ((timedb[1].equals(timerq[1]))
						&& (timedb[2].equals(timerq[2]))) {
					PaymentObject po = new PaymentObject(money, category, null,
							date_db, null, account, null, null, null, null,
							icon);
					arraymonth.add(po);
					countmonth++;
				}

				Log.i("countmonth", countmonth + "");
			} while (mcursor.moveToNext());
		}
		countincome_month = 0;
		int countmonthtmp = 0;
		String date_income_db = "";
		String[] timedb_income = null;
		String[] timerq_income = null;
		Cursor incomecursor = database.getAllPaymentIncome(contactname);
		if (incomecursor.moveToFirst()) {
			do {
				String incomecategory = incomecursor.getString(0).toString();
				String incomemoney = incomecursor.getString(1).toString();
				date_income_db = incomecursor.getString(2).toString();
				String incomeaccount = incomecursor.getString(3).toString();
				int incomeicon = Integer.parseInt(incomecursor.getString(4));
				timedb_income = date_income_db.split("/");
				timerq_income = dateRequest.split("/");
				if ((timedb_income[1].equals(timerq_income[1]))
						&& (timedb_income[2].equals(timerq_income[2]))) {
					PaymentObject po = new PaymentObject(incomemoney,
							incomecategory, null, date_income_db, null,
							incomeaccount, null, null, null, null, incomeicon);
					arraymonth.add(po);
					countmonthtmp++;
				}
			} while (incomecursor.moveToNext());
		}
		countincome_month = countmonthtmp + countmonth;
		Log.i("countincome_month", countincome_month + "");
		String date_vay_db = "";

		String[] timedb_vay = null;
		String[] timerq_vay = null;
		Cursor vaycursor = database.getAllPaymentVay(contactname);
		if (vaycursor.moveToFirst()) {
			do {
				String vayperson = vaycursor.getString(0).toString();
				String vaymoney = vaycursor.getString(1).toString();
				date_vay_db = vaycursor.getString(2).toString();
				String vayaccount = vaycursor.getString(3).toString();
				int vayicon = Integer.parseInt(vaycursor.getString(4));
				String vaycategory = vaycursor.getString(5).toString();
				timedb_vay = date_vay_db.split("/");
				timerq_vay = dateRequest.split("/");
				if ((timedb_vay[1].equals(timerq_vay[1]))
						&& (timedb_vay[2].equals(timerq_vay[2]))) {
					PaymentObject po = new PaymentObject(vaymoney, vayperson,
							vaycategory, date_income_db, null, vayaccount,
							null, null, null, null, vayicon);
					arraymonth.add(po);
				}
			} while (vaycursor.moveToNext());
		}
		database.close();

		adapter_lvmonth = new MyArrayAdapter(this,
				R.layout.layout_custom_listview_thu_tieu, arraymonth,
				countmonth, countincome_month);
		listViewNoteMonth.setAdapter(adapter_lvmonth);
	}

	private void myInitsTabYear() {
		listViewNoteYear = (ListView) findViewById(R.id.listViewNoteYear);
		ArrayList<PaymentObject> arrayyear = new ArrayList<PaymentObject>();
		countyear = 0;
		database.open();
		String date_db = "";
		String[] timedb = null;
		String[] timerq = null;
		Cursor mcursor = database.getAllPayment(contactname);
		if (mcursor.moveToFirst()) {
			do {
				String category = mcursor.getString(0).toString();
				String money = mcursor.getString(1).toString();
				date_db = mcursor.getString(2).toString();
				String account = mcursor.getString(3).toString();
				int icon = Integer.parseInt(mcursor.getString(4));
				timedb = date_db.split("/");
				timerq = dateRequest.split("/");
				if (timedb[2].equals(timerq[2])) {
					PaymentObject po = new PaymentObject(money, category, null,
							date_db, null, account, null, null, null, null,
							icon);
					arrayyear.add(po);
					countyear++;
				}

			} while (mcursor.moveToNext());
		}
		countincome_year = 0;
		int countyeartmp = 0;
		String date_income_db = "";
		String[] timedb_income = null;
		String[] timerq_income = null;
		Cursor incomecursor = database.getAllPaymentIncome(contactname);
		if (incomecursor.moveToFirst()) {
			do {
				String incomecategory = incomecursor.getString(0).toString();
				String incomemoney = incomecursor.getString(1).toString();
				date_income_db = incomecursor.getString(2).toString();
				String incomeaccount = incomecursor.getString(3).toString();
				int incomeicon = Integer.parseInt(incomecursor.getString(4));
				timedb_income = date_income_db.split("/");
				timerq_income = dateRequest.split("/");
				if (timedb_income[2].equals(timerq_income[2])) {
					PaymentObject po = new PaymentObject(incomemoney,
							incomecategory, null, date_income_db, null,
							incomeaccount, null, null, null, null, incomeicon);
					arrayyear.add(po);
					countyeartmp++;
				}
			} while (incomecursor.moveToNext());
		}
		countincome_year = countyear + countyeartmp;
		String date_vay_db = "";

		String[] timedb_vay = null;
		String[] timerq_vay = null;
		Cursor vaycursor = database.getAllPaymentVay(contactname);
		if (vaycursor.moveToFirst()) {
			do {
				String vayperson = vaycursor.getString(0).toString();
				String vaymoney = vaycursor.getString(1).toString();
				date_vay_db = vaycursor.getString(2).toString();
				String vayaccount = vaycursor.getString(3).toString();
				int vayicon = Integer.parseInt(vaycursor.getString(4));
				String vaycategory = vaycursor.getString(5).toString();
				timedb_vay = date_vay_db.split("/");
				timerq_vay = dateRequest.split("/");
				if (timedb_vay[2].equals(timerq_vay[2])) {
					PaymentObject po = new PaymentObject(vaymoney, vayperson,
							vaycategory, date_income_db, null, vayaccount,
							null, null, null, null, vayicon);
					arrayyear.add(po);
				}
			} while (vaycursor.moveToNext());
		}

		database.close();

		adapter_lvyear = new MyArrayAdapter(this,
				R.layout.layout_custom_listview_thu_tieu, arrayyear, countyear,
				countincome_year);
		listViewNoteYear.setAdapter(adapter_lvyear);
	}

	// ---------------------------------------------------------------------------------
	private void gotoModify() {
		Intent intent = new Intent(IndexActivity.this, ModifyNoteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("DATE", dateModify);
		bundle.putInt("POSITION", position);
		bundle.putString("namecontact", contactname);
		intent.putExtra(MyInits.REQUEST_CODE_CONTACT, bundle);
		startActivity(intent);
	}

	private void gotoModifyIncome() {
		Intent intent = new Intent(IndexActivity.this,
				ModifyNoteActivityIncome.class);
		Bundle bundle = new Bundle();
		bundle.putString("DATE", dateModify);
		bundle.putInt("POSITION", positionincome - count);
		bundle.putString("namecontact", contactname);
		intent.putExtra(MyInits.REQUEST_CODE_CONTACT, bundle);
		startActivity(intent);
	}

	private void gotoModifyVay() {
		Intent intent = new Intent(IndexActivity.this,
				ModifyNoteActivityVay.class);
		Bundle bundle = new Bundle();
		bundle.putString("DATE", dateModify);
		bundle.putInt("POSITION", positionvay - count_income);
		Log.i("position", positionincome + "");
		Log.i("position", count_income + "");
		bundle.putString("namecontact", contactname);
		intent.putExtra(MyInits.REQUEST_CODE_CONTACT, bundle);
		startActivity(intent);
	}

	private void showBalance() {
		database.open();
		Cursor cursor = database.getAllPaymentInDate(dateRequest, contactname);
		int moneythu = 0;
		if (cursor.moveToFirst()) {
			do {
				int money = 0;
				try {
					money = Integer.parseInt(cursor.getString(1).toString());
				} catch (NumberFormatException nfe) {
					Toast.makeText(IndexActivity.this, "Sai số",
							Toast.LENGTH_SHORT).show();
				}
				moneythu += money;
			} while (cursor.moveToNext());
		}
		Cursor incomecursor = database.getAllPaymentInDateIncome(dateRequest,
				contactname);
		int moneyincome = 0;
		if (incomecursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(incomecursor.getString(1)
						.toString());
				moneyincome += money;
			} while (incomecursor.moveToNext());
		}
		database.close();
		ArrayList<BalanceObject> arraybalance = new ArrayList<BalanceObject>();
		BalanceObject b1 = new BalanceObject("Thu nhập", moneyincome);
		BalanceObject b2 = new BalanceObject("Chi tiêu", moneythu);
		BalanceObject b3 = new BalanceObject("Cân bằng", moneyincome - moneythu);
		arraybalance.add(b1);
		arraybalance.add(b2);
		arraybalance.add(b3);
		MyArrayAdapterBalance adapterbalance = new MyArrayAdapterBalance(
				IndexActivity.this, R.layout.custom_listview_balance,
				arraybalance);
		listviewBalance.setAdapter(adapterbalance);
		listviewBalance.setDivider(null);
		listviewBalance.setDividerHeight(0);
	}

	private void setListViewBalance() {

		if (listviewBalance.getVisibility() == View.INVISIBLE) {
			buttonShowHidden.setImageResource(R.drawable.up);
			listviewBalance.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 100);
			layoutBalance.setLayoutParams(Params1);
			showBalance();
		} else {
			if (listviewBalance.getVisibility() == View.VISIBLE) {
				buttonShowHidden.setImageResource(R.drawable.down);
				listviewBalance.setVisibility(View.INVISIBLE);
				layoutBalance.getLayoutParams().height = 0;
				LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				listViewShowInfor.setLayoutParams(Params1);
				listViewNoteMonth.setLayoutParams(Params1);
				listViewNoteYear.setLayoutParams(Params1);
			}
		}
	}

	private void showListViewBalanceYear() {
		ArrayList<PaymentObject> arrayyear = new ArrayList<PaymentObject>();
		database.open();
		String date_db = "";
		String[] timedb = null;
		String[] timerq = null;
		int money_thu_year = 0;
		Cursor mcursor = database.getAllPayment(contactname);
		if (mcursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(mcursor.getString(1).toString());
				date_db = mcursor.getString(2).toString();
				timedb = date_db.split("/");
				timerq = dateRequest.split("/");
				if (timedb[2].equals(timerq[2])) {
					money_thu_year += money;
				}
			} while (mcursor.moveToNext());
		}
		String date_income_db = "";
		String[] timedb_income = null;
		String[] timerq_income = null;
		int money_income_year = 0;
		Cursor incomecursor = database.getAllPaymentIncome(contactname);
		if (incomecursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(incomecursor.getString(1)
						.toString());
				date_income_db = incomecursor.getString(2).toString();
				timedb_income = date_db.split("/");
				timerq_income = dateRequest.split("/");
				if (timedb_income[2].equals(timerq_income[2])) {
					money_income_year += money;
				}
			} while (incomecursor.moveToNext());
		}
		database.close();
		ArrayList<BalanceObject> arraybalance = new ArrayList<BalanceObject>();
		BalanceObject b1 = new BalanceObject(getResources().getString(R.string.thunhap), money_income_year);
		BalanceObject b2 = new BalanceObject("Chi tiêu", money_thu_year);
		BalanceObject b3 = new BalanceObject("Cân bằng", money_income_year
				- money_thu_year);
		arraybalance.add(b1);
		arraybalance.add(b2);
		arraybalance.add(b3);
		MyArrayAdapterBalance adapterbalance = new MyArrayAdapterBalance(
				IndexActivity.this, R.layout.custom_listview_balance,
				arraybalance);
		listviewBalance.setAdapter(adapterbalance);
		listviewBalance.setDivider(null);
		listviewBalance.setDividerHeight(0);
	}

	private void showListViewBalanceMonth() {
		database.open();
		String date_db = "";
		String[] timedb = null;
		String[] timerq = null;
		int money_chi_month = 0;
		Cursor mcursor = database.getAllPayment(contactname);
		if (mcursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(mcursor.getString(1).toString());
				date_db = mcursor.getString(2).toString();
				timedb = date_db.split("/");
				timerq = dateRequest.split("/");
				if ((timedb[1].equals(timerq[1]))
						&& (timedb[2].equals(timerq[2]))) {
					money_chi_month += money;
				}
			} while (mcursor.moveToNext());
		}
		String date_db_income = "";
		String[] timedb_income = null;
		String[] timerq_income = null;
		int money_income_month = 0;
		Cursor incomecursor = database.getAllPaymentIncome(contactname);
		if (incomecursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(incomecursor.getString(1)
						.toString());
				date_db_income = incomecursor.getString(2).toString();
				timedb_income = date_db_income.split("/");
				timerq_income = dateRequest.split("/");
				if ((timedb_income[1].equals(timerq_income[1]))
						&& (timedb_income[2].equals(timerq_income[2]))) {
					money_income_month += money;
				}
			} while (incomecursor.moveToNext());
		}
		database.close();
		ArrayList<BalanceObject> arraybalance = new ArrayList<BalanceObject>();
		BalanceObject b1 = new BalanceObject("Thu nhập", money_income_month);
		BalanceObject b2 = new BalanceObject("Chi tiêu", money_chi_month);
		BalanceObject b3 = new BalanceObject("Cân bằng", money_income_month
				- money_chi_month);
		arraybalance.add(b1);
		arraybalance.add(b2);
		arraybalance.add(b3);
		MyArrayAdapterBalance adapterbalance = new MyArrayAdapterBalance(
				IndexActivity.this, R.layout.custom_listview_balance,
				arraybalance);
		listviewBalance.setAdapter(adapterbalance);
		listviewBalance.setDivider(null);
		listviewBalance.setDividerHeight(0);
		listviewBalance.setClickable(false);

	}

	private void showBalanceVayNo() {
		database.open();
		Cursor cursor = database.getPaymentVayDate(dateRequest, contactname);
		int moneyvay = 0;
		if (cursor.moveToFirst()) {
			do {
				int money = 0;
				try {
					money = Integer.parseInt(cursor.getString(0).toString());
				} catch (NumberFormatException nfe) {
					Toast.makeText(IndexActivity.this, "Sai số",
							Toast.LENGTH_SHORT).show();
				}
				moneyvay += money;
			} while (cursor.moveToNext());
		}
		Cursor incomecursor = database.getPaymentNoDate(dateRequest,
				contactname);
		int moneyno = 0;
		if (incomecursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(incomecursor.getString(0)
						.toString());
				moneyno += money;
			} while (incomecursor.moveToNext());
		}
		database.close();
		ArrayList<BalanceObject> arraybalance = new ArrayList<BalanceObject>();
		BalanceObject b1 = new BalanceObject("Cho Vay", moneyvay);
		BalanceObject b2 = new BalanceObject("Nợ", moneyno);
		arraybalance.add(b1);
		arraybalance.add(b2);
		MyArrayAdapterBalance adapterbalance = new MyArrayAdapterBalance(
				IndexActivity.this, R.layout.custom_listview_balance,
				arraybalance);
		listviewBalance.setAdapter(adapterbalance);
		listviewBalance.setDivider(null);
		listviewBalance.setDividerHeight(0);

	}

	private void showBalanceVayNoMonth() {
		database.open();
		Cursor cursor = database.getAllPaymentVay(contactname);
		int moneyno = 0;
		String date_db_no = "";
		String[] timedb_no = null;
		String[] timerq_no = null;
		if (cursor.moveToFirst()) {
			do {
				int money = 0;
				money = Integer.parseInt(cursor.getString(1).toString());
				date_db_no = cursor.getString(2).toString();
				String nocategory = cursor.getString(5).toString();
				timedb_no = date_db_no.split("/");
				timerq_no = dateRequest.split("/");
				if ((timedb_no[1].equals(timerq_no[1]))
						&& (timedb_no[2].equals(timerq_no[2]))
						&& (nocategory.equals("Nợ"))) {
					moneyno += money;
				}
			} while (cursor.moveToNext());
		}
		Cursor vaycursor = database.getAllPaymentVay(contactname);
		int moneyvay = 0;
		String date_db = "";
		String[] timedb = null;
		String[] timerq = null;
		if (vaycursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(vaycursor.getString(1).toString());
				date_db = vaycursor.getString(2).toString();
				String vaycategory = vaycursor.getString(5).toString();
				timedb = date_db.split("/");
				timerq = dateRequest.split("/");
				if ((timedb[1].equals(timerq[1]))
						&& (timedb[2].equals(timerq[2]))
						&& (vaycategory.equals("Vay"))) {
					moneyvay += money;
				}
			} while (vaycursor.moveToNext());
		}
		database.close();
		ArrayList<BalanceObject> arraybalance = new ArrayList<BalanceObject>();
		BalanceObject b1 = new BalanceObject("Cho Vay", moneyvay);
		BalanceObject b2 = new BalanceObject("Nợ", moneyno);
		arraybalance.add(b1);
		arraybalance.add(b2);
		MyArrayAdapterBalance adapterbalance = new MyArrayAdapterBalance(
				IndexActivity.this, R.layout.custom_listview_balance,
				arraybalance);
		listviewBalance.setAdapter(adapterbalance);
		listviewBalance.setDivider(null);
		listviewBalance.setDividerHeight(0);

	}

	private void showBalanceVayNoYear() {
		database.open();
		Cursor cursor = database.getAllPaymentVay(contactname);
		int moneyno = 0;
		String date_db_no = "";
		String[] timedb_no = null;
		String[] timerq_no = null;
		if (cursor.moveToFirst()) {
			do {
				int money = 0;
				money = Integer.parseInt(cursor.getString(1).toString());
				date_db_no = cursor.getString(2).toString();
				String nocategory = cursor.getString(5).toString();
				timedb_no = date_db_no.split("/");
				timerq_no = dateRequest.split("/");
				if ((timedb_no[2].equals(timerq_no[2]))
						&& (nocategory.equals("Nợ"))) {
					moneyno += money;
				}
			} while (cursor.moveToNext());
		}
		Cursor vaycursor = database.getAllPaymentVay(contactname);
		int moneyvay = 0;
		String date_db = "";
		String[] timedb = null;
		String[] timerq = null;
		if (vaycursor.moveToFirst()) {
			do {
				int money = Integer.parseInt(vaycursor.getString(1).toString());
				date_db = vaycursor.getString(2).toString();
				String vaycategory = vaycursor.getString(5).toString();
				timedb = date_db.split("/");
				timerq = dateRequest.split("/");
				if ((timedb[2].equals(timerq[2]))
						&& (vaycategory.equals("Vay"))) {
					moneyvay += money;
				}
			} while (vaycursor.moveToNext());
		}
		database.close();
		ArrayList<BalanceObject> arraybalance = new ArrayList<BalanceObject>();
		BalanceObject b1 = new BalanceObject("Cho Vay", moneyvay);
		BalanceObject b2 = new BalanceObject("Nợ", moneyno);
		arraybalance.add(b1);
		arraybalance.add(b2);
		MyArrayAdapterBalance adapterbalance = new MyArrayAdapterBalance(
				IndexActivity.this, R.layout.custom_listview_balance,
				arraybalance);
		listviewBalance.setAdapter(adapterbalance);
		listviewBalance.setDivider(null);
		listviewBalance.setDividerHeight(0);

	}

	private void showPopupMenus() {
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.setting:
					Intent i = new Intent(IndexActivity.this,
							SettingActivity.class);
					Bundle b = new Bundle();
					b.putString("DATE", dateRequest);
					b.putString("namecontact", contactname);
					i.putExtra("DATEBUNDLE", b);
					startActivity(i);
					break;
				case R.id.report:
					Intent intent = new Intent(IndexActivity.this, Chart.class);
					Bundle bundle = new Bundle();
					bundle.putString("DATE", dateRequest);
					bundle.putString("namecontact", contactname);
					intent.putExtra("DATEBUNDLE", bundle);
					startActivity(intent);
					break;
				case R.id.budget:
					Toast.makeText(IndexActivity.this, "Chua update",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.export:
					Toast.makeText(IndexActivity.this, "Chua update",
							Toast.LENGTH_SHORT).show();
					break;
				case R.id.about:
					Toast.makeText(IndexActivity.this, "Chua update",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				return true;
			}
		});
		popup.show();
	}
}
