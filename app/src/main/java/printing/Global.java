package printing;

public class Global {



	public static final int MSG_WORKTHREAD_HANDLER_CONNECTNET = 100000;
	public static final int MSG_WORKTHREAD_SEND_CONNECTNETRESULT = 100001;

	public static final int MSG_WORKTHREAD_HANDLER_CONNECTBT = 100004;
	public static final int MSG_WORKTHREAD_SEND_CONNECTBTRESULT = 100005;

	public static final int MSG_WORKTHREAD_HANDLER_CONNECTUSB = 100028;
	public static final int MSG_WORKTHREAD_SEND_CONNECTUSBRESULT = 100029;

	// Bundle data使用
	public static final String BYTESPARA1 = "bytespara1";
	public static final String BYTESPARA2 = "bytespara2";

	public static final String INTPARA1 = "intpara1";
	public static final String INTPARA2 = "intpara2";
	public static final String INTPARA3 = "intpara3";
	public static final String INTPARA4 = "intpara4";
	public static final String INTPARA5 = "intpara5";
	public static final String INTPARA6 = "intpara6";
	public static final String STRPARA1 = "strpara1";
	public static final String STRPARA2 = "strpara2";

	public static final String PARCE1 = "parce1";
	public static final String PARCE2 = "parce2";


	public static final int CMD_POS_WRITE = 100100;
	public static final int CMD_POS_WRITERESULT = 100101;
	public static final int CMD_POS_READ = 100102;

	public static final int CMD_POS_SETKEY = 100104;
	public static final int CMD_POS_SETKEYRESULT = 100105;
	public static final int CMD_POS_CHECKKEY = 100106;
	public static final int CMD_POS_CHECKKEYRESULT = 100107;
	public static final int CMD_POS_PRINTPICTURE = 100108;
	public static final int CMD_POS_PRINTPICTURERESULT = 100109;
	public static final int CMD_POS_STEXTOUT = 100110;
	public static final int CMD_POS_STEXTOUTRESULT = 100111;
	public static final int CMD_POS_SALIGN = 100112;
	public static final int CMD_POS_SALIGNRESULT = 100113;
	public static final int CMD_POS_SETLINEHEIGHT = 100114;
	public static final int CMD_POS_SETLINEHEIGHTRESULT = 100115;
	public static final int CMD_POS_SETRIGHTSPACE = 100116;
	public static final int CMD_POS_SETRIGHTSPACERESULT = 100117;
	public static final int CMD_POS_SETCHARSETANDCODEPAGE = 100118;
	public static final int CMD_POS_SETCHARSETANDCODEPAGERESULT = 100119;
	public static final int CMD_POS_SETBARCODE = 100120;
	public static final int CMD_POS_SETBARCODERESULT = 100121;
	public static final int CMD_POS_SETQRCODE = 100122;
	public static final int CMD_POS_SETQRCODERESULT = 100123;
	public static final int CMD_EPSON_SETQRCODE = 100123;
	public static final int CMD_EPSON_SETQRCODERESULT = 100124;
	public static final int MSG_ALLTHREAD_READY = 100300;
	public static final int MSG_PAUSE_HEARTBEAT = 100301;
	public static final int MSG_RESUME_HEARTBEAT = 100302;

	public static final int CMD_WRITE = 100304;
	public static final int CMD_WRITERESULT = 100305;
	public static final int CMD_POS_PRINTBWPICTURE = 100306;
}
