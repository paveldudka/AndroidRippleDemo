package trickyandroid.com.rippletest;

import android.app.Activity;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Space;


public class MainActivity extends Activity implements View.OnTouchListener {

    private ViewGroup buttonsContainer;
    private ViewGroup activeButton = null;
    private final int MAX_BUTTONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonsContainer = (ViewGroup) findViewById(R.id.buttonsContainer);

        int buttonsSpacing = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int buttonSize = (int) getResources().getDimension(R.dimen.button_size);
        Outline circularOutline = new Outline();
        circularOutline.setOval(0, 0, buttonSize, buttonSize);

        for (int i = 0; i < MAX_BUTTONS; i++) {
            ViewGroup buttonHost = (ViewGroup) getLayoutInflater().inflate(R.layout.circular_button_layout, buttonsContainer, false);
            TextView button = (TextView) buttonHost.getChildAt(0);
            buttonHost.setOutline(circularOutline);

            button.setText("Test " + i);

            buttonHost.setOnTouchListener(this);
            buttonsContainer.addView(buttonHost);

            //Add margin between buttons manually
            if (i != MAX_BUTTONS - 1) {
                buttonsContainer.addView(new Space(this), new ViewGroup.LayoutParams(buttonsSpacing, buttonSize));
            }
        }
        selectButton(((ViewGroup) buttonsContainer.getChildAt(0)), false);
    }

    private void selectButton(ViewGroup buttonHost, boolean reveal) {
        selectButton(buttonHost, reveal, buttonHost.getWidth(), buttonHost.getHeight());
    }

    private void selectButton(ViewGroup buttonHost, boolean reveal, int startX, int startY) {
        if (buttonHost == activeButton) {
            return;
        }

        if (activeButton != null) {
            activeButton.setSelected(false);
            activeButton = null;
        }

        activeButton = buttonHost;
        activeButton.setSelected(true);

        View button = activeButton.getChildAt(0);

        if (reveal) {
            ViewAnimationUtils.createCircularReveal(button,
                    startX,
                    startY,
                    0,
                    button.getHeight()).start();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ViewGroup) view).getChildAt(0).getBackground().setHotspot(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                selectButton((ViewGroup) view, true, (int) motionEvent.getX(), (int) motionEvent.getY());
                break;

        }
        return false;
    }
}
