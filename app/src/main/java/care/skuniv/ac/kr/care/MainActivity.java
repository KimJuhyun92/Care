package care.skuniv.ac.kr.care;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    Button btn_notice;
    Button btn_student;
    Button btn_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseMessaging.getInstance().subscribeToTopic("notice");

        pager = (ViewPager)findViewById(R.id.pager);
        btn_notice = (Button)findViewById(R.id.btn_notice);
        btn_student = (Button)findViewById(R.id.btn_student);
        btn_setting = (Button)findViewById(R.id.btn_setting);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        View.OnClickListener movePageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int tag = (int)view.getTag();
                pager.setCurrentItem(tag);
            }
        };

        btn_notice.setOnClickListener(movePageListener);
        btn_notice.setTag(0);
        btn_student.setOnClickListener(movePageListener);
        btn_student.setTag(1);
//        btn_setting.setOnClickListener(movePageListener);
//        btn_setting.setTag(2);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm )
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
//                case 2:
//                    return new Fragment3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // total page count
            return 2;
        }
        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }
    }
}
