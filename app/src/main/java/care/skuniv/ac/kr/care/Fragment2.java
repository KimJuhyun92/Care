package care.skuniv.ac.kr.care;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class Fragment2 extends Fragment {

    public Fragment2() {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null);
        ListViewAdapter adapter = new ListViewAdapter(getActivity());
        ListView listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        View header = inflater.inflate(R.layout.listview_header, null, false);
        listview.addHeaderView(header);

        CarePush carePush = new CarePush("http://"+URLPath.url+":8080/CareServer/getStudentList", 1);
        try {
            String result = carePush.execute().get();
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            InfoStudent[] infoStudents = gson.fromJson(result, InfoStudent[].class);


            //정렬
                Comparator<InfoStudent> StudentAsc = new Comparator<InfoStudent>() {
                    @Override
                    public int compare(InfoStudent item1, InfoStudent item2) {
                        int compare;
                        if(item1.getGrade() < item2.getGrade())
                            compare = -1;
                        else if(item1.getGrade() == item2.getGrade())
                            compare = 0;
                        else
                            compare = 1;
                        return compare;
                    }
                };
                Collections.sort(Arrays.asList(infoStudents),StudentAsc);

                for (InfoStudent infoStudent : infoStudents) {
                    infoStudent.decoding();
                    adapter.add(infoStudent);
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