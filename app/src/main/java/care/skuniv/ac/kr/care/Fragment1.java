package care.skuniv.ac.kr.care;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class Fragment1 extends Fragment  {

    private Button input_notice;

    public Fragment1()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment1, null);
        final NoticeAdapter adapter = new NoticeAdapter(getActivity());
        final ListView listview = (ListView) view.findViewById(R.id.noticeListView);
        listview.setAdapter(adapter);
        View header = inflater.inflate(R.layout.notice_header, null, false);
        View footer = inflater.inflate(R.layout.notice_footer, null, false);
        listview.addHeaderView(header);
        listview.addFooterView(footer);

        input_notice = (Button) view.findViewById(R.id.input_notice);
        input_notice.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final LayoutInflater inflater=getLayoutInflater(savedInstanceState); //Dialog에서 보여줄 입력화면 View 객체 생성 작업
                final View dialogView= inflater.inflate(R.layout.notice_input_dialog, null); //Dialog의 listener에서 사용하기 위해 final

                //멤버의 세부내역 입력 Dialog 생성 및 보이기
                AlertDialog.Builder buider= new AlertDialog.Builder(getActivity()); //AlertDialog.Builder 객체 생성
                buider.setTitle("공지사항 입력"); //Dialog 제목
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅
                buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        final EditText noticeTitle= (EditText)dialogView.findViewById(R.id.notice_title);
                        final EditText noticeContent= (EditText)dialogView.findViewById(R.id.notice_content);
                        String title= noticeTitle.getText().toString();
                        String content = noticeContent.getText().toString();

                        try {
                            title = URLEncoder.encode(title, "UTF-8");
                            content = URLEncoder.encode(content, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        SendNotice sendNotice= new SendNotice("http://"+URLPath.url+":8080/CareServer/getNoticeInput",title,content);
                        sendNotice.execute();
                        //Toast메세지
                        Toast.makeText(getActivity(), "공지사항을 추가했습니다", Toast.LENGTH_SHORT).show();
                        //리플레쉬
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                });
                buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                AlertDialog dialog=buider.create();
                //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        //DB에서 공지사항 목록 불러오기
        CarePush carePush = new CarePush("http://"+URLPath.url+":8080/CareServer/getNoticeList", 1);
        try {
            String result = carePush.execute().get();
            Log.d("connect",result);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            InfoNotice[] infoNotices = gson.fromJson(result, InfoNotice[].class);

            for (InfoNotice infoNotice : infoNotices) {
                try {
                    String title = infoNotice.getNotice_title();
                    title = URLDecoder.decode(title, "UTF-8");
                    infoNotice.setNotice_title(title);
                    String content = infoNotice.getNotice_content();
                    content = URLDecoder.decode(content, "UTF-8");
                    infoNotice.setNotice_content(content);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                adapter.add(infoNotice);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        return view;
    }

}