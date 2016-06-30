package nemi.in;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import common.view.SlidingTabLayout;
import in.nemi.ncontrol.R;
import mmsl.DeviceUtility.CardType;
import mmsl.DeviceUtility.DeviceBluetoothCommunication;
import mmsl.DeviceUtility.DeviceCallBacks;
import mmsl.DeviceUtility.TemplateType;
import mmsl.GetPrintableImage.GetPrintableImage;

import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PosFragment extends Fragment implements DeviceCallBacks {
    ListView lv, items_list;
    Button pay_button, clear_button;
    TextView total_amo;
    static OutputStream outStream;

    ArrayAdapter<BillItems> billAdap;
    ArrayList<BillItems> alist;
    Button set_qty_btn, delete_bill_btn;
    EditText qty_et, c_name_et, c_contact_et;
    ndbHelper databaseHelper;

    String data = "00:02:0A:02:E9:9E";
    BillItems billItems;

    DeviceBluetoothCommunication bluetoothCommunication;
    int count = 0;
    int slipnumber;
    String transactionName = "TRANSFER";
    String date = "Date : 04/07/2014";
    String time = "Time : 12:18:40";

    String bcName = "BC Name : MaestrosLTD.";
    String bcLoc = "BC Location : Navi Mumbai";
    String agentID = "Agent ID :Sachin";
    String tID = "Terminal ID :TID00001";
    String aadharNo = "REM AADHAR : XXXX XX99 99";
    String uidaiAuthCode = "UID Auth.Code: 123456781234567";
    String custName = "Customer Name : AAKASH JAGTAP";

    String status = "Txn Status :SUCCESS";

    String totalAmountString = " Transfered Amount Rs:100";
    boolean isLongSlip = false;
    TextView recievedTextView, filepath;
    ImageView Finger_Image;
    Button connectButton, filechooser;
    Spinner slotNumberSpinner;
    EditText userIDEditText;
    byte FontStyleVal;
    String _ChhosenPath = null;
    Context ctx;
    CardType cardType;
    static byte templatecode;
    private boolean SC_Write_Flag = false;
    private boolean SC_Read_Flag = false;
    int slotnumber;
    byte[] userid, username;
    int fileid = 6;

    enum SMSTATUS {
        READ_USERID, READ_USERNAME, WRITE_USERID, WRITE_USERNAME, WRITE_MEMORY, READ_MEMORY, APDU

    }

    ;

    enum SmartCardCommand {
        CreateMF, SelectMF, CreateEF, SelectEF, Write_CPU, Read_CPU
    }

    ;

    SmartCardCommand smartcardcommand;
    SMSTATUS smstatus;
    TemplateType templateType;
    int numberofscan;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(data);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pos, container, false);

//        unpairDevice(device);
//        pairDevice(device);
        bluetoothCommunication = new DeviceBluetoothCommunication();
        bluetoothCommunication.StartConnection(device, this);

        templateType = TemplateType.ANSI_378;
        numberofscan = 1;
        cardType = CardType.CPU;
        filechooser = (Button) view.findViewById(R.id.filechooser);
        filepath = (TextView) view.findViewById(R.id.tempfilepath);

        alist = new ArrayList<BillItems>();
        lv = (ListView) view.findViewById(R.id.userlist);
        total_amo = (TextView) view.findViewById(R.id.total_amo);
        databaseHelper = new ndbHelper(getActivity(), null, null, 1);
        pay_button = (Button) view.findViewById(R.id.pay);
        clear_button = (Button) view.findViewById(R.id.clear);
        c_name_et = (EditText) view.findViewById(R.id.c_name_id);
        c_contact_et = (EditText) view.findViewById(R.id.c_number_id);
        pay_button.setEnabled(false);

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alist.isEmpty()) {
                    String c_name = c_name_et.getText().toString();
                    String c_contact = c_contact_et.getText().toString();

                    //print
                    // Bluetooth printer
                    bluetoothCommunication.setPrinterFont(FontStyleVal);
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    String blank = "                                            ";
                    bluetoothCommunication.SendData(blank.getBytes());
                    bluetoothCommunication.SendData(blank.getBytes());
                    bluetoothCommunication.LineFeed();
                    String paid = "                    *PAID*                    ";
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.SendData(paid.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    String line="------------------------------------------------";
                    bluetoothCommunication.SendData(line.getBytes());

                    String address = "  3rd Floor, #330,27th Main,Sector 2,HSR Layout,\n  Bangalore-560102,Karnataka, India.";
                    bluetoothCommunication.SendData(address.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.SendData(line.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    String name = "  Name : " + c_name;
                    bluetoothCommunication.SendData(name.getBytes());
                    bluetoothCommunication.LineFeed();
                    String amount = "  Contact : " + c_contact;
                    bluetoothCommunication.SendData(amount.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.SendData(line.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();

                    int a = Integer.parseInt(total_amo.getText().toString());
                    databaseHelper.bill(c_name, c_contact, a);
                    c_name_et.setText("");
                    c_contact_et.setText("");
                    int billnumber = databaseHelper.checkLastBillNumber();
                    billnumber++;

                    bluetoothCommunication.LineFeed();
                    String qty_h= "     QUANTITY     ";
                    String item_h = "     ITEM     ";
                    String price_h = "     PRICE     ";
                    bluetoothCommunication.SendData(qty_h.getBytes());
                    bluetoothCommunication.SendData(item_h.getBytes());
                    bluetoothCommunication.SendData(price_h.getBytes());

                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();

                    for (int i = 0; i < alist.size(); i++) {
                        databaseHelper.sales(billnumber, alist.get(i).getItem(), alist.get(i).getQty(), alist.get(i).getPrice());

                        String qty = "        "+String.valueOf(alist.get(i).getQty());
                        String item ="              "+alist.get(i).getItem();
                        String price ="         "+String.valueOf(alist.get(i).getPrice())+"        ";

                        bluetoothCommunication.LineFeed();
                        bluetoothCommunication.SendData(qty.getBytes());
                        bluetoothCommunication.SendData(item.getBytes());
                        bluetoothCommunication.SendData(price.getBytes());
                    }
                    bluetoothCommunication.SendData(line.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();

                    lv.setAdapter(billAdap);   // set value
                    billAdap.notifyDataSetChanged();
                    int total = 0;
                    for (int j = 0; j < alist.size(); j++) {
                        total += alist.get(j).getPrice() * alist.get(j).getQty();
                        total_amo.setText("" + total);
                    }
                    String sub_total = "                             SubTot  "+String.valueOf(total);
                    String service_tax = "                        Service Tax @ 4.944% "+String.valueOf(total);
                    String vat = "                                vat @ 14.5%: "+String.valueOf(total);
                    String total_bill = "                               Total  "+String.valueOf(total);
                    bluetoothCommunication.SendData(sub_total.getBytes());
                    bluetoothCommunication.SendData(service_tax.getBytes());
                    bluetoothCommunication.SendData(vat.getBytes());
                    bluetoothCommunication.LineFeed();
                    String line1 = "                              -----------------";
                    bluetoothCommunication.SendData(line1.getBytes());
                    bluetoothCommunication.SendData(total_bill.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.LineFeed();
                    billAdap.clear();
                    total_amo.setText("0");


                }
            }

        });


        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billAdap.clear();
                total_amo.setText("0");
            }
        });


        if (savedInstanceState == null) {
            android.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bluetoothCommunication.StopConnection();
//        pay_button.setEnabled(false);
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                final int state        = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
//                final int prevState    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
//
//                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
//                    showToast("Paired");
//                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
//                    showToast("Unpaired");
//                }
//
//            }
//        }
//    };

    public void GenerateImage(byte[] data) {
        int[] Imagedata = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            Imagedata[i] = (int) (data[i] & 0xFF);
        }
        int rows = ((int) (Imagedata[3] << 8) + Imagedata[2]);// ByteBuffer.wrap(rawHeader).order(mOrder).getInt(1);
        int columns = ((int) Imagedata[5] << 8) + Imagedata[4];// ByteBuffer.wrap(rawHeader).order(mOrder).getInt(1);
        // int vres = ((int) Imagedata[7] << 8) + Imagedata[6];//
        // ByteBuffer.wrap(rawHeader).order(mOrder).getInt(1);
        // int hres = ((int) Imagedata[9] << 8) + Imagedata[8];//
        // ByteBuffer.wrap(rawHeader).order(mOrder).getInt(1);
        // fingerMessageTextView.setText("Rows : " + rows + " Columns : "
        // + columns + " Vres : " + vres + " Hres : " + hres);
        Bitmap bitmap = Bitmap.createBitmap(rows, columns, Bitmap.Config.RGB_565);
        bitmap.setHasAlpha(true);
        int counter = 12;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                bitmap.setPixel(i, j, Color.argb(255, Imagedata[counter],
                        Imagedata[counter], Imagedata[counter]));
                counter++;
            }
        }
        // Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length);
        Finger_Image.setImageBitmap(bitmap);

    }

    protected void showFileChooser() {

        // Create DirectoryChooserDialog and register a callback
        DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(
                ctx, new DirectoryChooserDialog.ChosenDirectoryListener() {
            @Override
            public void onChosenDir(String chosenDir) {
                SharedPreferences preferences = ctx
                        .getSharedPreferences("templatepath", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("path", chosenDir);
                editor.commit();
                _ChhosenPath = preferences.getString("path", null);
                Toast.makeText(ctx,
                        "Chosen directory: " + _ChhosenPath,
                        Toast.LENGTH_LONG).show();

                System.out.println("---->> Chosen directory: "
                        + _ChhosenPath);
                filepath.setText(_ChhosenPath);
                filechooser.setText("ChangePath");

            }
        });
        // Load directory chooser dialog for initial 'm_chosenDir' directory and
        // select a required directory.
        // The registered callback will be called upon final directory selection
        directoryChooserDialog.chooseDirectory("");
        // MainActivity.GetPrintPATHfromConfigFile(filename, context)

    }

    private void CreateMF(int slotnumber) {

        byte[] create_mf_command = {0x00, (byte) 0xE0, 0x00, 0x00, 0x10, 0x62,
                0x0E, (byte) 0x82, 0x01, 0x38, (byte) 0x83, 0x02, 0x3F, 0x00,
                (byte) 0x84, 0x05, 0x4D, 0x45, 0x54, 0x53, 0x4C};
        // mConnection.sendData(create_mf_command, 0);
        bluetoothCommunication.CPUSmartCardCommand(create_mf_command,
                slotnumber);

    }

    private void SelectMF(int slotnumber) {

        byte[] Select_MF_Command = {0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x3F,
                0x00};
        // mConnection.sendData(Select_MF_Command, 0);
        bluetoothCommunication.CPUSmartCardCommand(Select_MF_Command,
                slotnumber);
        smartcardcommand = SmartCardCommand.SelectMF;

    }

    private void createEF(int slotnumber, int maxbytesinfile, int fileid) {

        byte[] Create_EF_User_ID_Command = {0x00, (byte) 0xE0, 0x00, 0x00,
                0x0E, 0x62, 0x0C, (byte) 0x80, 0x02, 0x00,
                (byte) maxbytesinfile, (byte) 0x82, 0x02, 0x01, 0x01,
                (byte) 0x83, 0x02, 0x30, (byte) fileid};
        // mConnection.sendData(Create_EF_User_ID_Command, 0);
        // smartcardcommand = SmartCardCommand.CreateEF ;
        bluetoothCommunication.CPUSmartCardCommand(Create_EF_User_ID_Command,
                slotnumber);

    }

    private void SelectEF(int slotnumber, int fileid) {

        byte[] Select_EF_User_ID_Command = {0x00, (byte) 0xA4, 0x00, 0x00,
                0x02, 0x30, (byte) fileid};
        // mConnection.sendData(Select_EF_User_ID_Command, 0);
        bluetoothCommunication.CPUSmartCardCommand(Select_EF_User_ID_Command,
                slotnumber);
        // smartcardcommand = SmartCardCommand.SelectEF ;

    }

    private void write_To_CPU(int slotnumber, byte[] data) {

        byte[] User_ID_Write_Command = new byte[(6 + data.length)];
        int Length = data.length;
        // User_ID_Write_Command[0] = 0x1B;
        // User_ID_Write_Command[1] = 0x2E;
        // User_ID_Write_Command[2] = (byte) slotnumber;
        User_ID_Write_Command[0] = 0x00;
        User_ID_Write_Command[1] = (byte) 0xD6;
        User_ID_Write_Command[2] = 0x00;
        User_ID_Write_Command[3] = 0x00;
        User_ID_Write_Command[4] = (byte) (Length + 1);
        User_ID_Write_Command[5] = (byte) Length;
        // Length += 6;
        // User_ID_Write_Command[3] = (byte) Length;
        // User_ID_Write_Command[4] = (byte) (Length >> 8);

        System.arraycopy(data, 0, User_ID_Write_Command, 6, Length);

        // mConnection.sendData(User_ID_Write_Command, 0);
        // smartcardcommand = SmartCardCommand.Write_CPU;

        bluetoothCommunication.CPUSmartCardCommand(User_ID_Write_Command,
                slotnumber);

    }

    private void Read_From_CPU() {

        byte[] User_ID_Read_Command = new byte[5];

        // User_ID_Read_Command[0] = 0x1B;
        // User_ID_Read_Command[1] = 0x2E;
        // User_ID_Read_Command[2] = (byte) SlotNumber;
        // User_ID_Read_Command[3] = 0x05;
        // User_ID_Read_Command[4] = 0x00;
        User_ID_Read_Command[0] = 0x00;
        User_ID_Read_Command[1] = (byte) 0xB0;
        User_ID_Read_Command[2] = 0x00;
        User_ID_Read_Command[3] = 0x00;
        User_ID_Read_Command[4] = (byte) 210;

        // mConnection.sendData(User_ID_Read_Command, 0);
        // smartcardcommand = SmartCardCommand.Read_CPU;
        bluetoothCommunication.CPUSmartCardCommand(User_ID_Read_Command,
                slotnumber);

    }

    @Override
    public void onCancelledCommand() {

    }

    @Override
    public void onCommandRecievedWhileAnotherRunning() {

        recievedTextView.setText("Command Recieved  While Another Running.");
    }

    @Override
    public void onCommandRecievedWhileProcessing() {

        recievedTextView.setText("Command Recieved While Processing.");
    }

    @Override
    public void onConnectComplete() {
        pay_button.setEnabled(true);
        Toast.makeText(getActivity(), "Printer Connected Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed() {
        pay_button.setEnabled(false);
        Toast.makeText(getActivity(), "Printer Connected Failed", Toast.LENGTH_SHORT).show();
//        unpairDevice(device);
//        pairDevice(device);
//        Intent i = getActivity().getPackageManager()
//                .getLaunchIntentForPackage( getActivity().getPackageName() );
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
    }

    @Override
    public void onCryptographicError() {

//        recievedTextView.setText("Cryptographic error.");
    }

    @Override
    public void onErrorOccured() {

//        recievedTextView
//                .setText("Error occurs during the execution of the function.");
    }

    @Override
    public void onErrorOccuredWhileProccess() {

//        recievedTextView.setText("Error occurs during process.");
    }

    @Override
    public void onErrorReadingSmartCard() {

//        recievedTextView.setText("Error occurs while reading from card.");
    }

    @Override
    public void onErrorWritingSmartCard() {

//        recievedTextView.setText("Error occurs while writing to card.");
    }

    @Override
    public void onFalseFingerDetected() {

//        recievedTextView.setText("False Finger Detected.");
    }

    @Override
    public void onFingerImageRecieved(byte[] data) {

//        recievedTextView.setText("Finger Image recieved.");
//        GenerateImage(data);

    }

    @Override
    public void onFingerPrintTimeout() {

    }

    @Override
    public void onFingerScanStarted(int scan) {

        recievedTextView.setText("Finger scan " + scan + " started");
    }

    @Override
    public void onFingerTooMoist() {

        recievedTextView.setText("Finger too moist.");
    }

    @Override
    public void onFingeracquisitioncompeted(String msg) {

    }

    @Override
    public void onImproveSwipe() {

        recievedTextView.setText("Improper Swipe");
        EnableAll();
    }

    @Override
    public void onInvalidCommand() {

        recievedTextView.setText("Invalid Command");
        EnableAll();

    }

    @Override
    public void onLatentFingerHard(String msg) {

        recievedTextView.setText(msg);
    }

    @Override
    public void onMSRDataRecieved(String msg) {

    }

    @Override
    public void onMoveFingerDown() {

        recievedTextView.setText("Move Finger Down.");
    }

    @Override
    public void onMoveFingerLeft() {

        recievedTextView.setText("Move Finger Left.");
    }

    @Override
    public void onMoveFingerRight() {

        recievedTextView.setText("Move Finger Right.");
    }

    @Override
    public void onMoveFingerUP() {

        recievedTextView.setText("Move Finger UP.");
    }

    @Override
    public void onNoData() {

    }

    @Override
    public void onNoSmartCardFound() {

    }

    @Override
    public void onOperationNotSupported() {

        recievedTextView.setText("operation not supported");
    }

    @Override
    public void onParameterOutofRange() {

        recievedTextView.setText("Parameter out of range.");
    }

    @Override
    public void onPlaceFinger() {

        recievedTextView.setText("Place Finger.");
    }

    @Override
    public void onPressFingerHard() {

        recievedTextView.setText("Press Finger Hard.");
    }

    @Override
    public void onRemoveFinger() {

        recievedTextView.setText("Remove Finger.");
    }

    @Override
    public void onSameFinger() {

        recievedTextView.setText("Same Finger Used.");
    }

    @Override
    public void onSmartCardDataRecieved(byte[] data) {

        recievedTextView.setText("Data on Smart Card :"
                + (new String(data)).trim());
    }

    @Override
    public void onSmartCardPresent() {

        if (cardType == CardType.CPU) {

            SelectMF(slotnumber);

        } else {
            switch (smstatus) {
                case READ_MEMORY:
                    bluetoothCommunication.ReadfromSmartCard(
                            slotNumberSpinner.getSelectedItemPosition() + 1, 50,
                            200, cardType, 6, 210);

                    break;
                case WRITE_MEMORY:
                    String data = userIDEditText.getText().toString() + ","
                            + userIDEditText.getText().toString();

                    byte[] useridarray = data.getBytes();
                    bluetoothCommunication.WritetoSmartCard(
                            slotNumberSpinner.getSelectedItemPosition() + 1, 50,
                            useridarray.length, useridarray, cardType, 6, 210);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTemplateRecieved(byte[] data) {
        try {
            File f = new File(_ChhosenPath + File.separator + "Template.iso");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream bos = new FileOutputStream(_ChhosenPath
                    + File.separator + "Template.iso", true);
            bos.write(data);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVerificationSuccessful(int templatenumber) {

    }

    @Override
    public void onVerificationfailed() {

    }

    @Override
    public void onWriteToSmartCardSuccessful() {

    }

    @Override
    public void onOutofPaper() {

        recievedTextView.setText("Printer Out of Paper.");
        EnableAll();
    }

    @Override
    public void onPlatenOpen() {

        recievedTextView.setText("Platen is Open.");
    }

    @Override
    public void onHighHeadTemperature() {

        recievedTextView.setText("High Head Temperature.");
    }

    @Override
    public void onLowHeadTemperature() {

        recievedTextView.setText("Head Temperature is Low.");

    }

    @Override
    public void onImproperVoltage() {

        recievedTextView.setText("Improper Voltage.");

    }

    @Override
    public void onSuccessfulPrintIndication() {

        recievedTextView.setText("Indication for successful print.");
        if (isLongSlip) {
            switch (count) {

                case 0:
                    printImagedata();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 1:
                    bluetoothCommunication.SendData(transactionName.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 2:
                    bluetoothCommunication.SendData(date.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 3:
                    bluetoothCommunication.SendData(time.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 4:
                    bluetoothCommunication.SendData(bcName.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 5:
                    bluetoothCommunication.SendData(bcLoc.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 6:
                    bluetoothCommunication.SendData(agentID.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 7:
                    bluetoothCommunication.SendData(tID.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 8:
                    bluetoothCommunication.SendData(aadharNo.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 9:
                    bluetoothCommunication.SendData(custName.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;

                case 10:
                    bluetoothCommunication.SendData(uidaiAuthCode.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 11:
                    bluetoothCommunication.SendData(status.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;
                case 12:
                    bluetoothCommunication.SendData(totalAmountString.getBytes());
                    bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();
                    count++;
                    break;

                case 13:
                    printImagedata();
                    bluetoothCommunication.GetPrinterStatus();
                    if (slipcounter == slipnumber) {
                        count++;
                    } else {
                        slipcounter++;
                        count = 0;
                    }
                    break;

                default:
                    bluetoothCommunication.LineFeed();
                    EnableAll();
                    isLongSlip = false;
                    break;
            }
        }

    }

    int slipcounter = 1;

    class BTPrnImage extends Thread {
        // private String receipt;

        private int width, height;
        private int[] img;

		/*
         * public void setReceipt(String r) { receipt = r; }
		 */

        public BTPrnImage(int l_width, int l_height, int[]

                img_raster) {

            img = img_raster;
            width = l_width;
            height = l_height;
        }

        public void run() {

            try {
                sleep(1000);

                byte[] CommandImagePrint = new byte[254];
                // int ImageYPointer = 0, ImageXPointer = 0;
                int ImageHeight = height, ImageWidth = width;
                // int ImageCounter = 0;

                // // Command to set printer head

                // position to 150th Dot

                CommandImagePrint[0] = 0x1B; // Command to for bit image mode
                // please refer the previous

                // document
                CommandImagePrint[1] = 0x23; // Exc #
                CommandImagePrint[2] = (byte) ImageWidth; // 8 Vertical
                // Dots(Heights) &
                // Single Width Mode

                // selected
                CommandImagePrint[3] = (byte) (ImageHeight / 256);// f8 //
                // Decimal
                // 248 since
                // the Image
                // width is

                // 248 Pixels as mentioned above
                CommandImagePrint[4] = (byte) (ImageHeight % 256);

                byte[] CommandImageData = new byte[5];
                for (int i = 0; i < CommandImageData.length; i++) {
                    CommandImageData[i] = (CommandImagePrint[i]);
                }
                // Send Command to print image
                bluetoothCommunication.SendData(CommandImageData);

                CommandImagePrint = new byte[img.length];
                for (int i = 0; i < CommandImagePrint.length; i++) {
                    CommandImagePrint[i] = (byte) (img[i] & 0xFF);
                }

                bluetoothCommunication.SendData(CommandImagePrint); // CommandImagePrint.length

                byte[] CommandImagePrint2 = {0x1B, 0x32}; // Command to set
                // Default Line
                // Spacing

                // Send Command to set Default Line Spacing
                bluetoothCommunication.SendData(CommandImagePrint2);
                CommandImagePrint2[0] = 0x0A;
                CommandImagePrint2[1] = 0x0A;
                bluetoothCommunication.SendData(CommandImagePrint2);
                sleep(2000);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        EnableAll();
                    }
                });

            } catch (Exception ioe) {
                System.out
                        .println("Problems reading from or writing to serial port."
                                + ioe.getMessage());

            }

        }
    }

    private void EnableAll() {
    }

    @Override
    public void onCPUSmartCardCommandDataRecieved(byte[] data) {

        int Data_Length = 0;
        Data_Length = data[0] & 0xFF;
        Data_Length <<= 8;
        Data_Length |= data[1] & 0xFF;

        switch (smartcardcommand) {

            case Read_CPU:

                if ((Data_Length > 0)) {

                    // Create a byte array buffer to hold the incoming
                    // data
                    byte[] Tempbuffer1 = new byte[(data[0] & 0xFF)];

                    for (int k = 1; k <= Tempbuffer1.length; k++)
                        Tempbuffer1[k - 1] = data[k];

                    onSmartCardDataRecieved(Tempbuffer1);

                } else {

                    onNoData();

                }
                break;
            case Write_CPU:

                if (Data_Length == 0x9000) {
                    onWriteToSmartCardSuccessful();
                } else {
                    // Smart Card Error
                    onErrorWritingSmartCard();
                }
                break;
            case CreateEF:

                if (Data_Length == 0x9000) {
                    smartcardcommand = SmartCardCommand.SelectEF;
                    SelectEF(slotnumber, 10);

                } else {
                    // Smart Card Error
                    onInvalidCommand();
                }
                break;
            case SelectEF:

                if (Data_Length == 0x9000) {
                    if (SC_Write_Flag == true) {
                        smartcardcommand = SmartCardCommand.Write_CPU;
                        write_To_CPU(slotnumber, userid);
                    } else if (SC_Read_Flag == true) {
                        // REad from Smart Card
                        smartcardcommand = SmartCardCommand.Read_CPU;
                        Read_From_CPU();
                    }
                } else {
                    // Smart Card Error
                    if (SC_Write_Flag == true) {
                        smartcardcommand = SmartCardCommand.CreateEF;
                        createEF(slotnumber, 210, 10);
                        // mCallBacks.selectEF();
                    } else if (SC_Read_Flag == true) {
                        // No data found
                        onNoData();
                    }
                }
                break;
            case CreateMF:

                if (Data_Length == 0x9000) {
                    smartcardcommand = SmartCardCommand.SelectMF;
                    SelectMF(slotnumber);
                    // mCallBacks.createMF();

                } else {
                    // Smart Card Error
                    onInvalidCommand();
                }
                break;
            case SelectMF:

                if (Data_Length == 0x9000) {
                    smartcardcommand = SmartCardCommand.SelectEF;
                    SelectEF(slotnumber, 10);
                    // mCallBacks.selectMF();

                } else {
                    if (SC_Write_Flag == true) {
                        smartcardcommand = SmartCardCommand.CreateMF;
                        CreateMF(slotnumber);
                        // mCallBacks.selectMF();
                    } else if (SC_Read_Flag == true) {
                        // Smart Card Error
                        onInvalidCommand();
                    }
                }
                break;
        }
    }

    public void printImagedata() {
        byte[] prnRasterImg = Readcsv(getActivity());
        bluetoothCommunication.SendData(prnRasterImg);
    }

    private static byte[] Readcsv(Context context) {
        try {

            InputStream in = context.getAssets().open("logo.txt");// openFileInput();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            byte[] mona = new byte[14926];
            int cnt = 0;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] RowData = line.split(",");
                    for (int i = 0; i < RowData.length; i++) {
                        mona[cnt] = (byte) Integer.parseInt(
                                RowData[i].replace("0x", "").replace(" ", ""),
                                16);
                        cnt++;
                    }
                    // do something with "data" and "value"
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // handle exception
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    // handle exception
                }
            }

            return mona;
        } catch (Exception e) {
            return null;
        }
    }

    private void DisableAll() {

    }

    @Override
    public void onCorruptDataRecieved() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCorruptDataSent() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInternalFPModuleCommunicationerror() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNoResponseFromCard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCardNotSupported() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCommandNotSupported() {
        // TODO Auto-generated method stub

    }


    public class SlidingTabsBasicFragment extends Fragment {
        ndbHelper databaseHelper;
        static final String LOG_TAG = "SlidingTabsBasicFragment";
        private SlidingTabLayout mSlidingTabLayout;
        private ViewPager mViewPager;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_sample, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            // BillAdapre set here
            billAdap = new BillAdapter(getActivity(), alist);
            return view;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
            mViewPager.setAdapter(new SamplePagerAdapter());
            mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
            mSlidingTabLayout.setViewPager(mViewPager);

        }

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< PagerAdapter >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        public class POSCursorAdapter extends CursorAdapter {


            public POSCursorAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, final Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }

            @Override

            public void bindView(View view, final Context context, final Cursor cursor) {
                TextView tv_column = (TextView) view.findViewById(R.id.fetch);
                TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);

                tv_column.setText(cursor.getString(0));
                tv_item.setText(cursor.getString(1));
                tv_price.setText(cursor.getString(3));
            }

        }

        class SamplePagerAdapter extends PagerAdapter {
            String col;

            @Override
            public int getCount() {
                Cursor c = databaseHelper.getCategories();
                return c.getCount();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return o == view;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                Toast.makeText(getContext(), "Its on: " + position, Toast.LENGTH_SHORT).show();
                return "Item " + (position + 1);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);

                container.addView(view);
                String ourTabName;
                POSCursorAdapter posCursorAdapter;
                databaseHelper = new ndbHelper(getActivity(), null, null, 1);
                final Cursor c = databaseHelper.getCategories();

                String[] TabbyName = new String[c.getCount()];
                c.moveToFirst();

                for (int i = 0; i < c.getCount(); i++) {
                    ourTabName = c.getString(0);
                    TabbyName[i] = ourTabName;
                    c.moveToNext();
                }

                final String a = TabbyName[position];
                posCursorAdapter = new POSCursorAdapter(getActivity(), databaseHelper.getPOSItems(a));
                items_list = (ListView) view.findViewById(R.id.items_list_id);
                items_list.setAdapter(posCursorAdapter);


                items_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        TextView itemidfetch = (TextView) view.findViewById(R.id.fetch);
                        TextView fetchitem = (TextView) view.findViewById(R.id.item_fetch);
                        TextView pricefetch = (TextView) view.findViewById(R.id.price_fetch);
                        String itemidfetchvar = itemidfetch.getText().toString();
                        String fetchitemvar = fetchitem.getText().toString();
                        int pricefetchvar = Integer.parseInt(pricefetch.getText().toString());

                        if (alist.isEmpty()) {
                            alist.add(new BillItems(itemidfetchvar, fetchitemvar, 1, pricefetchvar));
                            billAdap.notifyDataSetChanged();
                            lv.setAdapter(billAdap);
                        } else {
                            int flag = 0;
                            for (int i = 0; i < alist.size(); i++) {

                                flag = 0;
                                //                                //match _id
                                if (itemidfetchvar.equalsIgnoreCase(alist.get(i).getId())) {
                                    alist.set(i, new BillItems(itemidfetchvar, fetchitemvar, alist.get(i).getQty() + 1,
                                            pricefetchvar));

                                    //* alist.get(i).getQty() + pricefetchvar   increment by items
                                    lv.setAdapter(billAdap);
                                    break;
                                } else {
                                    flag = 1;
                                }
                            }
                            if (flag == 1) {
                                alist.add(new BillItems(itemidfetchvar, fetchitemvar, 1, pricefetchvar));
                                lv.setAdapter(billAdap);
                            }
                        }

                        int total = 0;

                        for (int j = 0; j < alist.size(); j++) {
                            total += alist.get(j).getPrice() * alist.get(j).getQty();
                            total_amo.setText("" + total);
                        }

                    }
                });
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        }

        public class BillAdapter extends ArrayAdapter<BillItems> {
            public BillAdapter(Context context, ArrayList<BillItems> alist) {
                super(context, 0, alist);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                billItems = getItem(position);
//                Log.e("ibillItems", billItems.toString());
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_on_front, parent, false);
                }
                // Lookup view for data population
                final TextView fetch_col = (TextView) convertView.findViewById(R.id.fetch_fe);
                final TextView fetch_item = (TextView) convertView.findViewById(R.id.item_fe);
                final TextView fetch_qty = (TextView) convertView.findViewById(R.id.category_fe);
                TextView fetch_price = (TextView) convertView.findViewById(R.id.price_fe);

                delete_bill_btn = (Button) convertView.findViewById(R.id.minus_item);
                set_qty_btn = (Button) convertView.findViewById(R.id.qty_item);

                set_qty_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog d = new Dialog(getActivity());
                        d.setTitle("Update Quantity !");
                        d.setContentView(R.layout.dialog);
                        Button b1 = (Button) d.findViewById(R.id.button1);
                        Button b2 = (Button) d.findViewById(R.id.button2);
                        qty_et = (EditText) d.findViewById(R.id.numberPicker1);
                        qty_et.setText(String.valueOf(billItems.getQty()));
                        String sTextFromET = qty_et.getText().toString();
                        final int qty = new Integer(sTextFromET).intValue();


                        b1.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {

                                                      int quantity = Integer.parseInt(qty_et.getText().toString());
                                                      if (quantity <= 0) {
                                                          qty_et.setError("Quantity must be greater than 0");
                                                      } else {
                                                          alist.set(position, new BillItems(alist.get(position).getId(), alist.get(position).getItem(),
                                                                  quantity, alist.get(position).getPrice()));
                                                          lv.setAdapter(billAdap);   // set value
                                                          billAdap.notifyDataSetChanged();

                                                          int total = 0;
                                                          for (int j = 0; j < alist.size(); j++) {
                                                              total += alist.get(j).getPrice() * alist.get(j).getQty();
                                                              total_amo.setText("" + total);
                                                          }
                                                          d.dismiss();
                                                      }
                                                  }
                                              }
                        );
                        b2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        d.show();

                    }
                });

                delete_bill_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Please select an action!");
                        alertDialogBuilder.setMessage("Are you sure you want to delete this item?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        alist.remove(position);
                                        lv.setAdapter(billAdap);   // set value
                                        billAdap.notifyDataSetChanged();

                                        //Total calculation
                                        int total = 0;
                                        for (int j = 0; j < alist.size(); j++) {
                                            total += alist.get(j).getPrice() * alist.get(j).getQty();
                                            total_amo.setText("" + total);
                                        }
                                    }
                                }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });


                // Populate the data into the template view using the data object
                fetch_col.setText(billItems.getId());
                fetch_item.setText(billItems.getItem());
                fetch_qty.setText(String.valueOf(billItems.getQty()));
                fetch_price.setText(String.valueOf(billItems.getPrice()));

                // Return the completed view to render on screen
                return convertView;
            }


        }

    }


}