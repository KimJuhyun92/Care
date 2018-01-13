package care.skuniv.ac.kr.care;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener {

    private LayoutInflater layoutInflater;

    // Activity에서 가져온 객체정보를 저장할 변수
    private InfoStudent mStudent;
    private Context mContext;

    // ListView 내부 View들을 가르킬 변수들
    private String index;
    private TextView grade;
    private TextView name;
    private Button message, attend, late, absent;

    // 리스트 아이템 데이터를 저장할 배열
    private ArrayList<InfoStudent> mStudentData;

    public ListViewAdapter(Context context) {
        super();
 //       mContext = context;
        mStudentData = new ArrayList<InfoStudent>();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    /**
     * @return 아이템의 총 개수를 반환
     */
    public int getCount() {
        // TODO Auto-generated method stub
        return mStudentData.size();
    }

    @Override
    /**
     * @return 선택된 아이템을 반환
     */
    public InfoStudent getItem(int position) {
        // TODO Auto-generated method stub
        return mStudentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    /**
     * getView
     *
     * @param position - 현재 몇 번째로 아이템이 추가되고 있는지 정보를 갖고 있다.
     * @param convertView - 현재 사용되고 있는 어떤 레이아웃을 가지고 있는지 정보를 갖고 있다.
     * @param parent - 현재 뷰의 부모를 지칭하지만 특별히 사용되지는 않는다.
     * @return 리스트 아이템이 저장된 convertView
     */
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
         mContext = parent.getContext();

        // 리스트 아이템이 새로 추가될 경우에는 v가 null값이다.
        // view는 어느 정도 생성된 뒤에는 재사용이 일어나기 때문에 효율을 위해서 해준다.
        if (v == null) {
            // inflater를 이용하여 사용할 레이아웃을 가져옵니다.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.listview_btn, parent, false);
        }
            // 레이아웃이 메모리에 올라왔기 때문에 이를 이용하여 포함된 뷰들을 참조할 수 있습니다.
            grade = (TextView) v.findViewById(R.id.grade);
            name = (TextView) v.findViewById(R.id.name);
            message = (Button) v.findViewById(R.id.message);
            attend = (Button) v.findViewById(R.id.attend);
            late = (Button) v.findViewById(R.id.late);
            absent = (Button) v.findViewById(R.id.absent);

            // 받아온 position 값을 이용하여 배열에서 아이템을 가져온다.
            mStudent = getItem(position);

            // Tag를 이용하여 데이터와 뷰를 묶습니다.
            name.setTag(mStudent);
            message.setTag(mStudent);
            attend.setTag(mStudent);
            late.setTag(mStudent);
            absent.setTag(mStudent);


        // 데이터의 실존 여부를 판별합니다.
        if (mStudent != null) {
            message.setOnClickListener(this);
            attend.setOnClickListener(this);
            late.setOnClickListener(this);
            absent.setOnClickListener(this);
            grade.setText(String.valueOf(mStudent.getGrade()));
            name.setText(mStudent.getStd_name());
            name.setOnLongClickListener(this);
        }
        else{
            grade.setText("");
            name.setText("");
        }
        // 완성된 아이템 뷰를 반환합니다.
        return v;
    }

    // 데이터를 추가하는 것을 위해서 만들어 준다.
    public void add(InfoStudent infoStudent) {
        mStudentData.add(infoStudent);
    }

    @Override
    public void onClick(View v) {
        final InfoStudent infoStudent = (InfoStudent) v.getTag();
        switch (v.getId()) {
            case R.id.message:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("메시지 입력");
                final EditText edit_msg = new EditText(mContext);
                dialog.setView(edit_msg);

                        dialog.setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String msg = edit_msg.getText().toString();
                                try {
                                    msg = URLEncoder.encode(msg, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                PushMessage sendMessage = new PushMessage("http://"+URLPath.url+":8080/CareServer/pushMessage",String.valueOf(infoStudent.getParent_id()),msg);
                                sendMessage.execute();
                                Log.d("message" ,""+ msg);
                        Log.d("parent_no" ,""+ String.valueOf(infoStudent.getParent_id()));
                }
                });
                dialog.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                dialog.show();
                break;

            case R.id.attend:
                String stdNo_attend = String.valueOf(infoStudent.getStd_no());
                CareAttendance att_attend = new CareAttendance("http://"+URLPath.url+":8080/CareServer/insertAttendance",stdNo_attend,1);
                att_attend.execute();
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("token",refreshedToken);
                break;
            case R.id.late:
                String stdNo_late = String.valueOf(infoStudent.getStd_no());
                CareAttendance att_late = new CareAttendance("http://"+ URLPath.url+":8080/CareServer/insertAttendance",stdNo_late,2);
                att_late.execute();
                break;
            case R.id.absent:
                String stdNo_absent = String.valueOf(infoStudent.getStd_no());
                CareAttendance att_absent = new CareAttendance("http://"+ URLPath.url+":8080/CareServer/insertAttendance",stdNo_absent,3);
                att_absent.execute();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final InfoStudent infoStudent = (InfoStudent) v.getTag();
        switch (v.getId()){
            case R.id.name:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("학생정보 수정");
                dialog.setMessage("정말 삭제 하시겠습니까?");
                dialog.setPositiveButton("확인",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        index = String.valueOf(infoStudent.getStd_no());
                        DeleteInfoToServer deleteInfoToServer = new DeleteInfoToServer("http://"+ URLPath.url+":8080/CareServer/deleteFromDB",index,1);
                        deleteInfoToServer.execute();
                        Toast.makeText(layoutInflater.getContext(), "학생정보를 삭제했습니다", Toast.LENGTH_SHORT).show();
                        //리플레쉬
                        Intent intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }
                });
                dialog.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                dialog.show();
        }
        return true;
    }
}