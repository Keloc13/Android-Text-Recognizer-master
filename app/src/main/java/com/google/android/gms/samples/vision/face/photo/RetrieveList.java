package com.google.android.gms.samples.vision.face.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by KevinVuNguyen on 6/14/17.
 */

public class RetrieveList extends Activity {

    private TextView
            mNumberOfList,
            mURL, mList;

    private static final String EXTRA_LIST = "listTester";
    private String URL;
    private int stringListSize;

    private ArrayList<String> stringList;
    private ArrayList<String> URL_List;

    public RetrieveList()
    {
        stringList = new ArrayList<>();
        URL_List = new ArrayList<>();
        URL = "";
    }
    @Override
    public void onCreate(Bundle onInstanceState)
    {
        super.onCreate(onInstanceState);
        setContentView(R.layout.retrieve_list_view);


       stringList =  getIntent().getStringArrayListExtra(EXTRA_LIST);

        stringListSize = stringList.size();

       findURL(stringList);

        mNumberOfList = (TextView) findViewById(R.id.id_number_of_strings_num);
        mNumberOfList.setText(Integer.toString(stringListSize));

        mURL = (TextView) findViewById(R.id.id_http);
        mURL.setText(URL);

        mList = (TextView) findViewById(R.id.id_list);
       // mList.setText(stringList.toString());

    }

    /**
     The newIntent function allows for a different activity to send an arraylist of strings
     to the retrieve_list_view activity. Hence, when this file opens, the information will be sent to
     this class.
     */
    public static Intent newIntent(Context packageContext, ArrayList<String> list)
    {
        Intent i = new Intent(packageContext, RetrieveList.class);
        i.putExtra(EXTRA_LIST, list);
        return i;
    }

    public void findURL(ArrayList<String> list)
    {
        int size = list.size();

       for(int i = 0; i < size; i++)
        {
            for(int k = 0; k < list.get(i).length()-4; k++)
            {
                if(list.get(i).substring(k,k+2).equalsIgnoreCase("ht"))
                {
                    if((list.get(i).substring(k+2,k+3).equalsIgnoreCase("t") ||
                            list.get(i).substring(k+2,k+3).equalsIgnoreCase("p"))
                            && list.get(i).charAt(k+3)==':')
                    {
                        URL_List.add( "http" + list.get(i).substring(k + 3));

                    }
                    else if(k < list.get(i).length() -5 && (list.get(i).substring(k+2,k+5).equalsIgnoreCase("tp:")))
                    {
                        URL_List.add(list.get(i).substring(k));

                    }
                    else
                    {
                        int j = k;

                        while( j < list.get(i).length() && list.get(i).charAt(j) != ':')
                            j++;

                        if(j < list.get(i).length())
                            URL_List.add( "http" + list.get(i).substring(j));

                    }
                }
            }
        }

        int URL_size = URL_List.size();

        for(int i = 0; i < URL_size; i++)
        {
            URL += URL_List.get(i) + "\n";
        }
    }
}
