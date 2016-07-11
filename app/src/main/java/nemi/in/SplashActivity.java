package nemi.in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import in.nemi.ncontrol.R;


public class SplashActivity extends Activity{
	
	private final int SPLASH_DISPLAY_LEN=3000;
	ImageView imgview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		imgview=(ImageView)findViewById(R.id.imgview);
		Animation a=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
		imgview.setAnimation(a);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() 
			{
				Intent in=new Intent(SplashActivity.this, Main.class);
				SplashActivity.this.startActivity(in);
				SplashActivity.this.finish();
				
			}
		}, SPLASH_DISPLAY_LEN);
	}
}
