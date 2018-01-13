package care.skuniv.ac.kr.care;

/**
 * Created by 김주현 on 2017-11-18.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CareAttendance extends AsyncTask<Void, Void, String> {
    private String answer;
    private String url;
    private String stdNo;
    private int state;
    private String message[] = {""};


    CareAttendance(String url, String stdNo, int state) {
        this.url = url;
        this.stdNo = stdNo;
        this.state = state;
    }

    @Override
    protected String doInBackground(Void... params) {
        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody = null;

        String[] msg = {"학생이 출석하였습니다.", "학생이 지각하였습니다.", "학생이 결석하였습니다."};
        for(int i = 0; i<msg.length; i++){
            try {
                msg[i] = URLEncoder.encode(msg[i],"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        switch (state) {
            case 1:
                requestBody = new FormBody.Builder().add("message",msg[0]).add("stdNo",stdNo).add("state",String.valueOf(state)).build();
                break;
            case 2:
                requestBody = new FormBody.Builder().add("message",msg[1]).add("stdNo",stdNo).add("state",String.valueOf(state)).build();
                break;
            case 3:
                requestBody = new FormBody.Builder().add("message",msg[2]).add("stdNo",stdNo).add("state",String.valueOf(state)).build();
                break;
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("answer", ""+answer);
        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.


    }

    public RequestBody selectStudentList(RequestBody requestBody) {
        return requestBody = new FormBody.Builder().build();
    }
}
