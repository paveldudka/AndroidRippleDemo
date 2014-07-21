package trickyandroid.com.rippletest;

import android.app.Activity;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Space;


public class MainActivity extends Activity implements View.OnTouchListener {

    private ViewGroup buttonsContainer;
    private Button activeButton = null;
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
            Button button = (Button) buttonHost.getChildAt(0);
            button.setOutline(circularOutline);
            button.setClipToOutline(true);

            button.setText("Test " + i);

            button.setOnTouchListener(this);
            buttonsContainer.addView(buttonHost);

            //Add margin between buttons manually
            if (i != MAX_BUTTONS - 1) {
                buttonsContainer.addView(new Space(this), new ViewGroup.LayoutParams(buttonsSpacing, buttonSize));
            }
        }
        selectButton((Button) ((ViewGroup) buttonsContainer.getChildAt(0)).getChildAt(0), false);
    }

    private void selectButton(Button button, boolean reveal) {
        selectButton(button, reveal, button.getWidth(), button.getHeight());
    }

    private void selectButton(Button button, boolean reveal, int startX, int startY) {
        if (button == activeButton) {
            return;
        }

        if (activeButton != null) {
            activeButton.setSelected(false);
            activeButton = null;
        }

        activeButton = button;
        activeButton.setSelected(true);

        if (reveal) {
            ViewAnimationUtils.createCircularReveal(activeButton,
                    startX,
                    startY,
                    0,
                    activeButton.getHeight()).start();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            selectButton((Button) view, true, (int) motionEvent.getX(), (int) motionEvent.getY());
        }
        return false;
    }
}
