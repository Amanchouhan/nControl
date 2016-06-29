package nemi.in;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import in.nemi.ncontrol.R;
import mmsl.DeviceUtility.CardType;
import mmsl.DeviceUtility.DeviceBluetoothCommunication;
import mmsl.DeviceUtility.DeviceCallBacks;
import mmsl.DeviceUtility.TemplateType;
import mmsl.GetPrintableImage.GetPrintableImage;

public class AccordionWidgetDemoActivity extends Fragment implements
        DeviceCallBacks {
    // private static final String TAG = "AccordionWidgetDemoActivity";
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
    Button disconnectButton;
    Button resetButton;
    Spinner swipetimeoutSpinner;
    Button swipestartButton;
    Button swipestopButton;
    Button mPrintSlipButton;
    Spinner fpsTimeoutSpinner;
    Spinner fpsScanperfingerSpinner;
    Spinner fpsTemplateformatSpinner;
    Button fpsScanButton;
    Button fpsVerifyButton;
    Button fpsStopButton;
    Spinner mprintslipSpinner;
    Spinner slotNumberSpinner;
    Spinner smartcardTypeSpinner;
    EditText userIDEditText;
    // EditText userNameEditText;
    Button smartcardReadButton; // smartcardapdu;
    Button smartcardWriteButton;
    Button smartcardStopButton;

    EditText mNameEditText;
    EditText mAmountEditText;
    Button mPrintButton;
    Button mDisableAutoOffButton;
    Button mPrinttestSlipButton;
    Button mPrintBarcodeButton;
    Button mPrinterStatusButton;

    Button mMonalisaButton;
    byte FontStyleVal;
    boolean fingerimageenable;
    RadioButton m9x24CheckBox;
    RadioButton m12x24CheckBox;
    RadioButton m16x24CheckBox;

    CheckBox mBoldCheckBox;
    CheckBox mUnderlineCheckBox;
    CheckBox mDoubleHeightCheckBox;
    CheckBox mDoubleWidthCheckBox;
    CheckBox disableFingerPrint;
    Uri imageUri;
    int[] prnRasterImg;
    int image_width;
    int image_height;
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

    /**
     * Called when the activity is first created.
     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_printer);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_printer, container, false);
        // final AccordionView v = (AccordionView)
        // findViewById(R.id.accordion_view);
        recievedTextView = (TextView) view.findViewById(R.id.readertextview);
        Finger_Image = (ImageView) view.findViewById(R.id.Finger_Image);
        connectButton = (Button) view.findViewById(R.id.connect);
        disconnectButton = (Button) view.findViewById(R.id.disconnect);
        resetButton = (Button) view.findViewById(R.id.reset);
        swipetimeoutSpinner = (Spinner) view.findViewById(R.id.swipespinner);
        swipestartButton = (Button) view.findViewById(R.id.swipestart);
        swipestopButton = (Button) view.findViewById(R.id.swipestop);
        mprintslipSpinner = (Spinner) view.findViewById(R.id.printslipnumberspinner);
        fpsTimeoutSpinner = (Spinner) view.findViewById(R.id.fpstimeoutspinner);
        fpsScanperfingerSpinner = (Spinner) view.findViewById(R.id.fpsscancountspinner);
        disableFingerPrint = (CheckBox) view.findViewById(R.id.disableFingerPrint);
        fpsTemplateformatSpinner = (Spinner) view.findViewById(R.id.fpstemplatespinner);
        fpsScanButton = (Button) view.findViewById(R.id.fpsscan);
        fpsVerifyButton = (Button) view.findViewById(R.id.fpsverify);
        fpsStopButton = (Button) view.findViewById(R.id.fpsstop);
        // smartcardapdu = (Button) findViewById(R.id.smapdu);
        mPrintSlipButton = (Button) view.findViewById(R.id.printSlip);
        slotNumberSpinner = (Spinner) view.findViewById(R.id.slotnumberspinner);
        smartcardTypeSpinner = (Spinner) view.findViewById(R.id.cardtypespinner);
        userIDEditText = (EditText) view.findViewById(R.id.useridedittext);
        // userNameEditText = (EditText) findViewById(R.id.usernameedittext);
        smartcardReadButton = (Button) view.findViewById(R.id.smread);
        smartcardWriteButton = (Button) view.findViewById(R.id.smwrite);
        smartcardStopButton = (Button) view.findViewById(R.id.smstop);

        mNameEditText = (EditText) view.findViewById(R.id.nameEditText);
        mAmountEditText = (EditText) view.findViewById(R.id.amountEditText);
        mPrintButton = (Button) view.findViewById(R.id.printButton);
        mDisableAutoOffButton = (Button) view.findViewById(R.id.printimageButton);
        mPrinttestSlipButton = (Button) view.findViewById(R.id.printtestButton);

        m9x24CheckBox = (RadioButton) view.findViewById(R.id.RadioButton9x24);
        m12x24CheckBox = (RadioButton) view.findViewById(R.id.RadioButton12x24);
        m16x24CheckBox = (RadioButton) view.findViewById(R.id.RadioButton16x24);
        mBoldCheckBox = (CheckBox) view.findViewById(R.id.boldCheckBox);
        mUnderlineCheckBox = (CheckBox) view.findViewById(R.id.underlineCheckBox01);
        mDoubleHeightCheckBox = (CheckBox) view.findViewById(R.id.doubleheightCheckBox01);
        mDoubleWidthCheckBox = (CheckBox) view.findViewById(R.id.doublewidthCheckBox01);
        mPrintBarcodeButton = (Button) view.findViewById(R.id.barcodeButton);
        mPrinterStatusButton = (Button) view.findViewById(R.id.captButton);

        mMonalisaButton = (Button) view.findViewById(R.id.monalisaButton);
        mprintslipSpinner.setEnabled(false);
        fpsTimeoutSpinner.setEnabled(false);
        fpsScanperfingerSpinner.setEnabled(false);
        fpsTemplateformatSpinner.setEnabled(false);
        fpsScanButton.setEnabled(false);
        fpsVerifyButton.setEnabled(false);
        fpsStopButton.setEnabled(false);
        swipestartButton.setEnabled(false);
        swipetimeoutSpinner.setEnabled(false);
        mPrintSlipButton.setEnabled(false);
        slotNumberSpinner.setEnabled(false);
        smartcardTypeSpinner.setEnabled(false);
        userIDEditText.setEnabled(false);
        // userNameEditText.setEnabled(false);
        smartcardReadButton.setEnabled(false);
        smartcardWriteButton.setEnabled(false);
        smartcardStopButton.setEnabled(false);
        // smartcardapdu.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(false);
        mPrinttestSlipButton.setEnabled(false);
        mPrintBarcodeButton.setEnabled(false);
        mPrinterStatusButton.setEnabled(false);

        mMonalisaButton.setEnabled(false);

        templateType = TemplateType.ANSI_378;
        numberofscan = 1;
        cardType = CardType.CPU;
        filechooser = (Button) view.findViewById(R.id.filechooser);
        filepath = (TextView) view.findViewById(R.id.tempfilepath);

        SharedPreferences preferences = getActivity().getSharedPreferences("templatepath", 0);

        _ChhosenPath = preferences.getString("path", "No path Selected");

        if (_ChhosenPath != null) {
            filepath.setText("Change Path");
            filepath.setText(_ChhosenPath);

        } else {

            filechooser.setText("Select Path");

        }

        filechooser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                showFileChooser();
            }
        });

        mBoldCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {

                    FontStyleVal |= 0x08;
                } else {
                    FontStyleVal &= 0xF7;
                }
            }
        });

        mUnderlineCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            FontStyleVal |= 0x80;
                        } else {
                            FontStyleVal &= 0x7F;
                        }
                    }
                });

        mDoubleHeightCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            FontStyleVal |= 0x10;
                        } else {
                            FontStyleVal &= 0xEF;
                        }
                    }
                });

        mDoubleWidthCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            FontStyleVal |= 0x20;
                        } else {
                            FontStyleVal &= 0xDF;
                        }
                    }
                });

        m9x24CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    FontStyleVal &= 0xFC;
                    FontStyleVal |= 0x02;
                }
            }
        });

        m12x24CheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            FontStyleVal &= 0xFC;
                            FontStyleVal |= 0x00;
                        }
                    }
                });

        m16x24CheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            FontStyleVal &= 0xFC;
                            FontStyleVal |= 0x01;
                        }
                    }
                });

        smartcardTypeSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                        switch (position) {
                            case 0:
                                cardType = CardType.CPU;
                                break;
                            case 1:
                                cardType = CardType.MEMORY;
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        mPrinttestSlipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                bluetoothCommunication.PrintTestSlip();

            }
        });

        mDisableAutoOffButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                bluetoothCommunication.DisableAutoOff();
            }
        });

        mPrinterStatusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bluetoothCommunication.GetPrinterStatus();
            }
        });



        mPrintButton.setOnClickListener(new View.OnClickListener() {
//            homesafe
            @Override
            public void onClick(View arg0) {
                bluetoothCommunication.setPrinterFont(FontStyleVal);
                String name = "Name : " + mNameEditText.getText().toString();
                // byte[] imageDataBytes = Base64.decode(name, 0);
                // bluetoothCommunication.SendData(imageDataBytes);
                bluetoothCommunication.SendData(name.getBytes());
                bluetoothCommunication.LineFeed();
                String amount = "Amount : "
                        + mAmountEditText.getText().toString();
                bluetoothCommunication.SendData(amount.getBytes());
                bluetoothCommunication.LineFeed();
                bluetoothCommunication.LineFeed();
                bluetoothCommunication.LineFeed();
            }
        });

        mPrintBarcodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                byte[] val = {0x1B, 0x39, 0x01, 0x30, 0x31, 0x32, 0x33, 0x34,
                        0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x0A, 0x0A,
                        0x0A, 0x0A};
                bluetoothCommunication.SendData(val);
                bluetoothCommunication.LineFeed();
            }
        });
        mPrintSlipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DisableAll();
                count = 0;
                slipnumber = mprintslipSpinner.getSelectedItemPosition() + 1;
                slipcounter = 1;
                isLongSlip = true;
                bluetoothCommunication.GetPrinterStatus();

            }
        });
        mMonalisaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DisableAll();
                isLongSlip = false;
                try {
                    InputStream inputStream = getActivity().getAssets().open("monalisa.jpg");
                    Bitmap bit = BitmapFactory.decodeStream(inputStream);
                    GetPrintableImage getPrintableImage = new GetPrintableImage();
                    prnRasterImg = getPrintableImage.GetPrintableArray(
                            getActivity(), bit.getWidth(),
                            bit);
                    image_width = getPrintableImage.getPrintWidth();
                    image_height = getPrintableImage.getPrintHeight();
                    getPrintableImage = null;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    // sleep(1000);

                    byte[] CommandImagePrint = new byte[prnRasterImg.length + 5];

                    CommandImagePrint[0] = 0x1B; // Command to for bit image
                    // mode
                    // please refer the previous

                    // document
                    CommandImagePrint[1] = 0x23; // Exc #
                    CommandImagePrint[2] = (byte) image_width; // 8 Vertical
                    // Dots(Heights)
                    // &
                    // Single Width
                    // Mode

                    // selected
                    CommandImagePrint[3] = (byte) (image_height / 256);// f8 //
                    // Decimal
                    // 248
                    // since
                    // the
                    // Image
                    // width
                    // is

                    // 248 Pixels as mentioned above
                    CommandImagePrint[4] = (byte) (image_height % 256);

                    for (int i = 0; i < prnRasterImg.length; i++) {
                        CommandImagePrint[i + 5] = (byte) (prnRasterImg[i] & 0xFF);
                    }

                    bluetoothCommunication.SendData(CommandImagePrint); // CommandImagePrint.length
                    count = 14;
                    isLongSlip = true;
                    // bluetoothCommunication.LineFeed();
                    bluetoothCommunication.GetPrinterStatus();

                } catch (Exception ioe) {
                    System.out
                            .println("Problems reading from or writing to serial port."
                                    + ioe.getMessage());

                }

                // bluetoothCommunication.GetPrinterStatus();
            }
        });

        smartcardReadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                swipestartButton.setEnabled(false);
                swipetimeoutSpinner.setEnabled(false);
                swipestopButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                fpsTimeoutSpinner.setEnabled(false);
                fpsScanperfingerSpinner.setEnabled(false);
                fpsTemplateformatSpinner.setEnabled(false);
                fpsScanButton.setEnabled(false);
                fpsVerifyButton.setEnabled(false);
                fpsStopButton.setEnabled(false);

                mPrintButton.setEnabled(true);
                mDisableAutoOffButton.setEnabled(false);
                mPrinttestSlipButton.setEnabled(false);
                mPrintBarcodeButton.setEnabled(false);
                mPrinterStatusButton.setEnabled(false);

                mMonalisaButton.setEnabled(false);

                slotNumberSpinner.setEnabled(false);
                smartcardTypeSpinner.setEnabled(false);
                userIDEditText.setEnabled(false);
                // userNameEditText.setEnabled(false);
                smartcardReadButton.setEnabled(false);
                smartcardWriteButton.setEnabled(false);
                smartcardStopButton.setEnabled(true);
                mprintslipSpinner.setEnabled(false);
                mPrintSlipButton.setEnabled(false);

                slotnumber = slotNumberSpinner.getSelectedItemPosition() + 1;
                if (cardType == CardType.CPU)
                    smstatus = SMSTATUS.READ_USERID;
                else
                    smstatus = SMSTATUS.READ_MEMORY;
                bluetoothCommunication.CheckSmartCardStatus(slotnumber);
                SC_Write_Flag = false;
                SC_Read_Flag = true;
            }
        });

        smartcardWriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                swipestartButton.setEnabled(false);
                swipetimeoutSpinner.setEnabled(false);
                swipestopButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                fpsTimeoutSpinner.setEnabled(false);
                fpsScanperfingerSpinner.setEnabled(false);
                fpsTemplateformatSpinner.setEnabled(false);
                fpsScanButton.setEnabled(false);
                fpsVerifyButton.setEnabled(false);
                fpsStopButton.setEnabled(false);
                mPrintButton.setEnabled(true);
                mDisableAutoOffButton.setEnabled(false);
                mPrinttestSlipButton.setEnabled(false);
                mPrintBarcodeButton.setEnabled(false);
                mPrinterStatusButton.setEnabled(false);

                mMonalisaButton.setEnabled(false);

                slotNumberSpinner.setEnabled(false);
                smartcardTypeSpinner.setEnabled(false);
                userIDEditText.setEnabled(false);
                // userNameEditText.setEnabled(false);
                smartcardReadButton.setEnabled(false);
                smartcardWriteButton.setEnabled(false);
                smartcardStopButton.setEnabled(true);

                userid = userIDEditText.getText().toString().getBytes();
                // username = userNameEditText.getText().toString().getBytes();

                slotnumber = slotNumberSpinner.getSelectedItemPosition() + 1;
                if (cardType == CardType.CPU)
                    smstatus = SMSTATUS.WRITE_USERID;
                else
                    smstatus = SMSTATUS.WRITE_MEMORY;
                bluetoothCommunication.CheckSmartCardStatus(slotnumber);
                SC_Write_Flag = true;
                SC_Read_Flag = false;

            }
        });

		/*
         * smartcardapdu.setOnClickListener(new OnClickListener(){
		 *
		 * @Override public void onClick(View v) { stub smstatus =
		 * SMSTATUS.APDU;
		 * bluetoothCommunication.CheckSmartCardStatus(slotNumberSpinner
		 * .getSelectedItemPosition() + 1);
		 *
		 * }});
		 */

        smartcardStopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                bluetoothCommunication.CancelScan();
            }
        });

        fpsScanperfingerSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {

                        switch (position) {
                            case 0:
                                numberofscan = 1;
                                break;
                            case 1:
                                numberofscan = 3;
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
        disableFingerPrint
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            fingerimageenable = false;
                        } else {
                            fingerimageenable = true;
                            Finger_Image.setImageDrawable(null);
                        }
                    }
                });

        fpsScanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (fingerimageenable == true) {
                    swipestartButton.setEnabled(false);
                    swipetimeoutSpinner.setEnabled(false);
                    swipestopButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                    fpsTimeoutSpinner.setEnabled(false);
                    fpsScanperfingerSpinner.setEnabled(false);
                    fpsTemplateformatSpinner.setEnabled(false);
                    fpsScanButton.setEnabled(false);
                    fpsVerifyButton.setEnabled(false);
                    fpsStopButton.setEnabled(true);

                    slotNumberSpinner.setEnabled(false);
                    smartcardTypeSpinner.setEnabled(false);
                    userIDEditText.setEnabled(false);
                    // userNameEditText.setEnabled(false);
                    smartcardReadButton.setEnabled(false);
                    smartcardWriteButton.setEnabled(false);
                    smartcardStopButton.setEnabled(false);

                    mPrintButton.setEnabled(true);
                    mDisableAutoOffButton.setEnabled(false);
                    mPrinttestSlipButton.setEnabled(false);
                    mPrintBarcodeButton.setEnabled(false);
                    mPrinterStatusButton.setEnabled(false);

                    mMonalisaButton.setEnabled(false);
                    bluetoothCommunication.GetTemplate(templateType,
                            (int) fpsTimeoutSpinner.getSelectedItemPosition(),
                            numberofscan, 0);
                    Finger_Image.setImageBitmap(null);

                } else {
                    swipestartButton.setEnabled(false);
                    swipetimeoutSpinner.setEnabled(false);
                    swipestopButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                    fpsTimeoutSpinner.setEnabled(false);
                    fpsScanperfingerSpinner.setEnabled(false);
                    fpsTemplateformatSpinner.setEnabled(false);
                    fpsScanButton.setEnabled(false);
                    fpsVerifyButton.setEnabled(false);
                    fpsStopButton.setEnabled(true);

                    slotNumberSpinner.setEnabled(false);
                    smartcardTypeSpinner.setEnabled(false);
                    userIDEditText.setEnabled(false);
                    // userNameEditText.setEnabled(false);
                    smartcardReadButton.setEnabled(false);
                    smartcardWriteButton.setEnabled(false);
                    smartcardStopButton.setEnabled(false);

                    mPrintButton.setEnabled(true);
                    mDisableAutoOffButton.setEnabled(false);
                    mPrinttestSlipButton.setEnabled(false);
                    mPrintBarcodeButton.setEnabled(false);
                    mPrinterStatusButton.setEnabled(false);

                    mMonalisaButton.setEnabled(false);

                    bluetoothCommunication.GetTemplate(templateType,
                            (int) fpsTimeoutSpinner.getSelectedItemPosition(),
                            numberofscan, 1);

                }
            }
        });

        fpsVerifyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    File f = new File(_ChhosenPath + File.separator
                            + "Template.iso");
                    if (f.exists()) {
                        FileInputStream bos = new FileInputStream(_ChhosenPath
                                + File.separator + "Template.iso");
                        byte[] templatedata = new byte[bos.available()];
                        bos.read(templatedata);
                        bos.close();

                        swipestartButton.setEnabled(false);
                        swipetimeoutSpinner.setEnabled(false);
                        swipestopButton.setEnabled(false);
                        disconnectButton.setEnabled(false);
                        fpsTimeoutSpinner.setEnabled(false);
                        fpsScanperfingerSpinner.setEnabled(false);
                        fpsTemplateformatSpinner.setEnabled(false);
                        fpsScanButton.setEnabled(false);
                        fpsVerifyButton.setEnabled(false);
                        fpsStopButton.setEnabled(true);
                        slotNumberSpinner.setEnabled(false);
                        smartcardTypeSpinner.setEnabled(false);
                        userIDEditText.setEnabled(false);
                        // userNameEditText.setEnabled(false);
                        smartcardReadButton.setEnabled(false);
                        smartcardWriteButton.setEnabled(false);
                        smartcardStopButton.setEnabled(false);

                        mPrintButton.setEnabled(true);
                        mDisableAutoOffButton.setEnabled(false);
                        mPrinttestSlipButton.setEnabled(false);
                        mPrintBarcodeButton.setEnabled(false);
                        mPrinterStatusButton.setEnabled(false);

                        mMonalisaButton.setEnabled(false);

                        bluetoothCommunication.MatchTemplate(
                                fpsTimeoutSpinner.getSelectedItemPosition(), 1,
                                templateType, templatedata);

                    } else {
                        Toast.makeText(getActivity(),
                                "Please Scan First", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(getActivity(),
                            "Please Scan First", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fpsStopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                bluetoothCommunication.CancelScan();
            }
        });

        fpsTemplateformatSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {

                        switch (position) {
						/*
						 * case 0: templateType = TemplateType.ANSI_378; break;
						 * case 1: templateType =
						 * TemplateType.DIN_V66400_UNORDERED; break; case 2:
						 * templateType = TemplateType.DIN_V66400_ORDERED;
						 * break; case 3: templateType = TemplateType.ISO_FMR;
						 * break; case 4: templateType =
						 * TemplateType.ISO_FMC_CS; break; case 5: templateType
						 * = TemplateType.ISO_FMC_CS_AA; break; case 6:
						 * templateType = TemplateType.ISO_FMC_NS; break; case
						 * 7: templateType = TemplateType.MINEX_A; break; case
						 * 8: templateType = TemplateType.PK_COMP; break; case
						 * 9: templateType = TemplateType.PK_COMP_NORM; break;
						 * case 10: templateType = TemplateType.PK_MAT; break;
						 * case 11: templateType = TemplateType.PK_MAT_NORM;
						 * break; default: break;
						 */

                            case 0:
                                templateType = TemplateType.ISO_FMR;
                                templatecode = 0x6E;
                                break;
                            case 1:
                                templateType = TemplateType.DIN_V66400_UNORDERED;
                                templatecode = 0x7D;
                                break;
                            case 2:
                                templateType = TemplateType.DIN_V66400_ORDERED;
                                templatecode = 0x7E;
                                break;
                            case 3:
                                templateType = TemplateType.ANSI_378;
                                templatecode = 0x41;
                                break;
                            case 4:
                                templateType = TemplateType.ISO_FMC_CS;
                                templatecode = 0x6D;
                                break;
                            case 5:
                                templateType = TemplateType.ISO_FMC_CS_AA;
                                templatecode = 0x7F;
                                break;
                            case 6:
                                templateType = TemplateType.ISO_FMC_NS;
                                templatecode = 0x6C;
                                break;
                            case 7:
                                templateType = TemplateType.MINEX_A;
                                templatecode = 0x6F;
                                break;
                            case 8:
                                templateType = TemplateType.PK_COMP;
                                templatecode = 0x02;
                                break;
                            case 9:
                                templateType = TemplateType.PK_COMP_NORM;
                                templatecode = 0x37;
                                break;
                            case 10:
                                templateType = TemplateType.PK_MAT;
                                templatecode = 0x03;
                                break;
                            case 11:
                                templateType = TemplateType.PK_MAT_NORM;
                                templatecode = 0x35;
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        swipestartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                swipestartButton.setEnabled(false);
                swipetimeoutSpinner.setEnabled(false);
                swipestopButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                fpsTimeoutSpinner.setEnabled(false);
                fpsScanperfingerSpinner.setEnabled(false);
                fpsTemplateformatSpinner.setEnabled(false);
                fpsScanButton.setEnabled(false);
                fpsVerifyButton.setEnabled(false);
                fpsStopButton.setEnabled(false);
                slotNumberSpinner.setEnabled(false);
                smartcardTypeSpinner.setEnabled(false);
                userIDEditText.setEnabled(false);
                // userNameEditText.setEnabled(false);
                smartcardReadButton.setEnabled(false);
                smartcardWriteButton.setEnabled(false);
                smartcardStopButton.setEnabled(false);

                mPrintButton.setEnabled(true);
                mDisableAutoOffButton.setEnabled(false);
                mPrinttestSlipButton.setEnabled(false);
                mPrintBarcodeButton.setEnabled(false);
                mPrinterStatusButton.setEnabled(false);

                mMonalisaButton.setEnabled(false);

                bluetoothCommunication.GetMSRData(swipetimeoutSpinner
                        .getSelectedItemPosition());
            }
        });

        swipestopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                bluetoothCommunication.CancelScan();

            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(i, 12);
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bluetoothCommunication.StopConnection();
                disconnectButton.setEnabled(false);
                mprintslipSpinner.setEnabled(false);
                mPrintSlipButton.setEnabled(false);
                resetButton.setEnabled(false);
                connectButton.setEnabled(true);
                swipestartButton.setEnabled(false);
                swipetimeoutSpinner.setEnabled(false);
                fpsTimeoutSpinner.setEnabled(false);
                fpsScanperfingerSpinner.setEnabled(false);
                fpsTemplateformatSpinner.setEnabled(false);
                fpsScanButton.setEnabled(false);
                fpsVerifyButton.setEnabled(false);
                fpsStopButton.setEnabled(false);
                slotNumberSpinner.setEnabled(false);
                smartcardTypeSpinner.setEnabled(false);
                userIDEditText.setEnabled(false);
                // userNameEditText.setEnabled(false);
                smartcardReadButton.setEnabled(false);
                smartcardWriteButton.setEnabled(false);
                smartcardStopButton.setEnabled(false);
                // smartcardapdu.setEnabled(false);
                mPrintButton.setEnabled(true);
                mDisableAutoOffButton.setEnabled(false);
                mPrinttestSlipButton.setEnabled(false);
                mPrintBarcodeButton.setEnabled(false);
                mPrinterStatusButton.setEnabled(false);

                mMonalisaButton.setEnabled(false);

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                fpsScanButton.setEnabled(true);
                fpsVerifyButton.setEnabled(false);
                fpsStopButton.setEnabled(false);
                recievedTextView.setText("");
                EnableAll();
                Finger_Image.setImageBitmap(null);

            }
        });
        return view;
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            connectButton.setEnabled(false);
            Toast.makeText(getActivity(),"Device selected : " + data.getStringExtra("Device"), Toast.LENGTH_SHORT).show();
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(data.getStringExtra("Device"));
            bluetoothCommunication = new DeviceBluetoothCommunication();
            bluetoothCommunication.StartConnection(device, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (bluetoothCommunication != null)
//            bluetoothCommunication.StopConnection();
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

        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);

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

        connectButton.setEnabled(false);
        disconnectButton.setEnabled(true);
        resetButton.setEnabled(true);
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);
        mPrintSlipButton.setEnabled(true);
        mprintslipSpinner.setEnabled(true);
        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);
        // smartcardapdu.setEnabled(true);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);

    }

    @Override
    public void onConnectionFailed() {

        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        swipestartButton.setEnabled(false);
        swipetimeoutSpinner.setEnabled(false);
        fpsTimeoutSpinner.setEnabled(false);
        fpsScanperfingerSpinner.setEnabled(false);
        fpsTemplateformatSpinner.setEnabled(false);
        fpsScanButton.setEnabled(false);
        fpsVerifyButton.setEnabled(false);
        fpsStopButton.setEnabled(false);
        slotNumberSpinner.setEnabled(false);
        smartcardTypeSpinner.setEnabled(false);
        userIDEditText.setEnabled(false);
        mPrintSlipButton.setEnabled(false);
        mprintslipSpinner.setEnabled(false);
        // userNameEditText.setEnabled(false);
        smartcardReadButton.setEnabled(false);
        smartcardWriteButton.setEnabled(false);
        smartcardStopButton.setEnabled(false);
        // smartcardapdu.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(false);
        mPrinttestSlipButton.setEnabled(false);
        mPrintBarcodeButton.setEnabled(false);
        mPrinterStatusButton.setEnabled(false);

        mMonalisaButton.setEnabled(false);
    }

    @Override
    public void onCryptographicError() {

        recievedTextView.setText("Cryptographic error.");
    }

    @Override
    public void onErrorOccured() {

        recievedTextView
                .setText("Error occurs during the execution of the function.");
    }

    @Override
    public void onErrorOccuredWhileProccess() {

        recievedTextView.setText("Error occurs during process.");
    }

    @Override
    public void onErrorReadingSmartCard() {

        recievedTextView.setText("Error occurs while reading from card.");
    }

    @Override
    public void onErrorWritingSmartCard() {

        recievedTextView.setText("Error occurs while writing to card.");
    }

    @Override
    public void onFalseFingerDetected() {

        recievedTextView.setText("False Finger Detected.");
    }

    @Override
    public void onFingerImageRecieved(byte[] data) {

        recievedTextView.setText("Finger Image recieved.");
        GenerateImage(data);

    }

    @Override
    public void onFingerPrintTimeout() {

        recievedTextView.setText("Scan Timeout.");
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);

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

        recievedTextView.setText(msg);
        fpsStopButton.setEnabled(false);
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

        recievedTextView.setText(msg.trim());
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);

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

        recievedTextView.setText("No Data.");
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);
    }

    @Override
    public void onNoSmartCardFound() {

        recievedTextView.setText("No smart card found");
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);
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

		/*
		 * if (cardType == CardType.CPU) { switch (smstatus) { case READ_USERID:
		 * recievedTextView.setText("User ID :" + (new String(data)).trim());
		 *
		 * bluetoothCommunication.ReadfromSmartCard(
		 * slotNumberSpinner.getSelectedItemPosition() + 1, 50, 200, cardType,
		 * 7, 210); smstatus = SMSTATUS.READ_USERNAME; fileid = 7;
		 * SelectMF(slotnumber);
		 *
		 * break; case READ_USERNAME:
		 * recievedTextView.setText(recievedTextView.getText().toString() + "\n"
		 * + "User Name :" + new String(data)); fileid = 6;
		 * swipestartButton.setEnabled(true);
		 * swipetimeoutSpinner.setEnabled(true);
		 * swipestopButton.setEnabled(false); disconnectButton.setEnabled(true);
		 *
		 * fpsTimeoutSpinner.setEnabled(true);
		 * fpsScanperfingerSpinner.setEnabled(true);
		 * fpsTemplateformatSpinner.setEnabled(true);
		 * fpsScanButton.setEnabled(true); fpsVerifyButton.setEnabled(true);
		 * fpsStopButton.setEnabled(false);
		 *
		 * slotNumberSpinner.setEnabled(true);
		 * smartcardTypeSpinner.setEnabled(true);
		 * userIDEditText.setEnabled(true); userNameEditText.setEnabled(true);
		 * smartcardReadButton.setEnabled(true);
		 * smartcardWriteButton.setEnabled(true);
		 * smartcardStopButton.setEnabled(false);
		 *
		 * mPrintButton.setEnabled(true);
		 * mDisableAutoOffButton.setEnabled(true);
		 * mPrinttestSlipButton.setEnabled(true);
		 * mPrintBarcodeButton.setEnabled(true);
		 * mPrinterStatusButton.setEnabled(true);
		 *
		 * mMonalisaButton.setEnabled(true); break; default: break; } } else {
		 * String value = new String(data); String[] val = value.split(",");
		 * recievedTextView.setText("User Id : " + val[0] + "\n" +
		 * "User Name : " + val[1]); EnableAll(); }
		 */

        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);

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
        recievedTextView.setText("Template Received");
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);
    }

    @Override
    public void onVerificationSuccessful(int templatenumber) {

        recievedTextView.setText("Verification Successful.");

        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);
    }

    @Override
    public void onVerificationfailed() {

        recievedTextView.setText("Verification Failed.");

        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);
    }

    @Override
    public void onWriteToSmartCardSuccessful() {

        recievedTextView.setText("Data written on SmartCard Successfully.");

		/*
		 * if (cardType == CardType.CPU) { switch (smstatus) { case
		 * WRITE_USERID:
		 * recievedTextView.setText("User ID writtern Successfully."); byte[]
		 * usernamearray = userNameEditText.getText().toString() .getBytes();
		 * fileid = 7; SelectMF(slotnumber); smstatus = SMSTATUS.WRITE_USERNAME;
		 *
		 * break; case WRITE_USERNAME: fileid = 6;
		 * recievedTextView.setText(recievedTextView.getText().toString() + "\n"
		 * + "User Name writtern Successfully.");
		 * swipestartButton.setEnabled(true);
		 * swipetimeoutSpinner.setEnabled(true);
		 * swipestopButton.setEnabled(false); disconnectButton.setEnabled(true);
		 *
		 * fpsTimeoutSpinner.setEnabled(true);
		 * fpsScanperfingerSpinner.setEnabled(true);
		 * fpsTemplateformatSpinner.setEnabled(true);
		 * fpsScanButton.setEnabled(true); fpsVerifyButton.setEnabled(true);
		 * fpsStopButton.setEnabled(false);
		 *
		 * slotNumberSpinner.setEnabled(true);
		 * smartcardTypeSpinner.setEnabled(true);
		 * userIDEditText.setEnabled(true); userNameEditText.setEnabled(true);
		 * smartcardReadButton.setEnabled(true);
		 * smartcardWriteButton.setEnabled(true);
		 * smartcardStopButton.setEnabled(false);
		 *
		 * mPrintButton.setEnabled(true);
		 * mDisableAutoOffButton.setEnabled(true);
		 * mPrinttestSlipButton.setEnabled(true);
		 * mPrintBarcodeButton.setEnabled(true);
		 * mPrinterStatusButton.setEnabled(true);
		 *
		 * mMonalisaButton.setEnabled(true); break;
		 *
		 * default: break; } } else {
		 * recievedTextView.setText("Data writtern Successfully."); EnableAll();
		 * }
		 */

        // recievedTextView.setText(recievedTextView.getText().toString()
        // + "\n" + "User Name writtern Successfully.");
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);

        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);

        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);

        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);

        mMonalisaButton.setEnabled(true);
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
        // mPrintSlipButton.setEnabled(false);
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
                        mPrintSlipButton.setEnabled(true);
                        count++;
                        // bluetoothCommunication.LineFeed();
                        // bluetoothCommunication.GetPrinterStatus();
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
        swipestartButton.setEnabled(true);
        swipetimeoutSpinner.setEnabled(true);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(true);
        mprintslipSpinner.setEnabled(true);
        fpsTimeoutSpinner.setEnabled(true);
        fpsScanperfingerSpinner.setEnabled(true);
        fpsTemplateformatSpinner.setEnabled(true);
        fpsScanButton.setEnabled(true);
        fpsVerifyButton.setEnabled(true);
        fpsStopButton.setEnabled(false);
        slotNumberSpinner.setEnabled(true);
        smartcardTypeSpinner.setEnabled(true);
        userIDEditText.setEnabled(true);
        // userNameEditText.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);
        smartcardReadButton.setEnabled(true);
        smartcardWriteButton.setEnabled(true);
        smartcardStopButton.setEnabled(false);
        mPrintSlipButton.setEnabled(true);
        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(true);
        mPrinttestSlipButton.setEnabled(true);
        mPrintBarcodeButton.setEnabled(true);
        mPrinterStatusButton.setEnabled(true);
        mMonalisaButton.setEnabled(true);
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
        {

            byte[] prnRasterImg = Readcsv(getActivity());
            bluetoothCommunication.SendData(prnRasterImg);

        }
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
        slotNumberSpinner.setEnabled(false);
        smartcardTypeSpinner.setEnabled(false);
        smartcardReadButton.setEnabled(false);
        smartcardWriteButton.setEnabled(false);
        fpsScanButton.setEnabled(false);
        fpsVerifyButton.setEnabled(false);
        swipestartButton.setEnabled(false);
        swipetimeoutSpinner.setEnabled(false);
        swipestopButton.setEnabled(false);
        disconnectButton.setEnabled(false);
        fpsTimeoutSpinner.setEnabled(false);
        fpsScanperfingerSpinner.setEnabled(false);
        fpsTemplateformatSpinner.setEnabled(false);
        // userNameEditText.setEnabled(true);
        mprintslipSpinner.setEnabled(false);
        mPrinterStatusButton.setEnabled(false);
        mPrintButton.setEnabled(true);
        mDisableAutoOffButton.setEnabled(false);
        mPrinttestSlipButton.setEnabled(false);
        mPrintBarcodeButton.setEnabled(false);
        mPrintSlipButton.setEnabled(false);
        mMonalisaButton.setEnabled(false);
        mPrintBarcodeButton.setEnabled(false);

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
}