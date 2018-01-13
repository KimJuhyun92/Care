package care.skuniv.ac.kr.care;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by 김주현 on 2017-11-24.
 */

public class NoticeAdapter extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener{

    private LayoutInflater layoutInflater;

    // Activity에서 가져온 객체정보를 저장할 변수
    private InfoNotice mNotice;
    private Context mContext;
    // 리스트 아이템 데이터를 저장할 배열
    private ArrayList<InfoNotice> mNoticeData;
    // ListView 내부 View들을 가르킬 변수들
    private TextView index, date, title,content;
    private Button checkNotice;

    public NoticeAdapter ( Context context){
        super();
        mNoticeData = new ArrayList<InfoNotice>();
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mNoticeData.size();
    }

    @Override
    public InfoNotice getItem(int position) {
        return mNoticeData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        mContext = parent.getContext();
//        View v = View.inflate(mContext, R.layout.notice, null);
        if (v == null) {
            // inflater를 이용하여 사용할 레이아웃 가져오기
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.notice, parent, false);
        }

        // 레이아웃이 메모리에 올라왔기 때문에 참조가능
        index = (TextView) v.findViewById(R.id.index);
        date = (TextView) v.findViewById(R.id.date);
        title = (TextView) v.findViewById(R.id.title);
        checkNotice = (Button) v.findViewById(R.id.checkNotice);

        mNotice = getItem(position);

        // Tag를 이용하여 데이터와 뷰 묶기.
        index.setTag(mNotice);
        date.setTag(mNotice);
        title.setTag(mNotice);
        checkNotice.setTag(mNotice);

        // 데이터의 실존 여부를 판별
        if (mNotice != null) {
            checkNotice.setOnClickListener(this);
            title.setOnLongClickListener(this);
            index.setText(String.valueOf(mNotice.getNotice_id()));
            date.setText(String.valueOf(mNotice.getNotice_date()));
            title.setText(String.valueOf(mNotice.getNotice_title()));
        }
        else{
            index.setText("");
            date.setText("");
            title.setText("");
        }
        return v;
    }
    // 데이터를 추가하는 것을 위해서 만들어 준다.
    public void add(InfoNotice infoNotice) {
        mNoticeData.add(infoNotice);
    }

    @Override
    public void onClick(View v) {
        final InfoNotice infoNotice = (InfoNotice) v.getTag();
        switch (v.getId()) {
            case R.id.checkNotice:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("공지사항 내용");
                content = new TextView(mContext);
                content.setText(String.valueOf(infoNotice.getNotice_content()));

                dialog.setView(content);

                dialog.setNegativeButton("확인",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                dialog.show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final InfoNotice infoNotice = (InfoNotice) v.getTag();
        switch (v.getId()){
            case R.id.title:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("공지사항 삭제");
                dialog.setMessage("정말 삭제 하시겠습니까?");
                dialog.setPositiveButton("확인",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String index = String.valueOf(infoNotice.getNotice_id());
                        Log.d("index<<<<<<<<<<<<<",index);
                        DeleteInfoToServer deleteInfoToServer = new DeleteInfoToServer("http://"+URLPath.url +":8080/CareServer/deleteFromDB",index,0);
                        deleteInfoToServer.execute();
                        Toast.makeText(layoutInflater.getContext(), "공지사항을 삭제했습니다", Toast.LENGTH_SHORT).show();
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
